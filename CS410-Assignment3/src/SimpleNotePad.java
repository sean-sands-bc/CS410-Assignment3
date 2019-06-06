import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
public class SimpleNotePad extends JFrame{
	//	list/hashmap or recent files, size and laod factor chosen to minimize collision chance, ordered from least recently opened to most recently
	LinkedHashMap<File,String> recentFiles = new LinkedHashMap<File,String>(16,.75f,true)
	{
		//	Limit hashmap size to 5 newest entries (most recently inserted/searched)
		protected boolean removeEldestEntry(Map.Entry<File,String> eldest) {
	        return size() > 5;
	     }
	};
	
    JMenuBar mb = new JMenuBar();
    JMenu fm = new JMenu("File");
    JMenu em = new JMenu("Edit");
    JTextPane d = new JTextPane();
    JMenuItem nf = new JMenuItem("New File");
    JMenuItem sf = new JMenuItem("Save File");
    JMenuItem pf = new JMenuItem("Print File");
    JMenuItem of = new JMenuItem("Open File");
    JMenu rm = new JMenu("Recent");
    JMenuItem c = new JMenuItem("Copy");
    JMenuItem p = new JMenuItem("Paste");
    JMenuItem r = new JMenuItem("Replace");
    
    public SimpleNotePad() {
        setTitle("A Simple Notepad Tool");
        
        makeMenus();
        makeMenuItems();
        makeMenuBar();
        
        add(new JScrollPane(d));
        setPreferredSize(new Dimension(600,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    public static void main(String[] args) {
        SimpleNotePad app = new SimpleNotePad();
    }
    
    private void makeMenus()
    {
    	fm.add(of);
    	fm.addSeparator();
    	fm.add(nf);
        fm.addSeparator();
        fm.add(sf);
        fm.addSeparator();
        fm.add(pf);
        fm.addSeparator();
        fm.add(rm);
        
        em.add(c);
        em.add(p);
        em.add(r);
    }
    //	calls functions that set up JMenuItems
    private void makeMenuItems()
    {
    	makeOpen();
    	makeNew();
    	makeSave();
    	makePrint();
    	//updateRecent();	//	uncomment if recent files gains persistence
        makeCopy();
        makePaste();
        makeReplace();
    }
    //
    private void makeMenuBar()
    {
    	mb.add(fm);
        mb.add(em);
        setJMenuBar(mb);
    }

    //	functions that set up JMenuItems
    private void makeNew()
    {
    	nf.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doNew();	
			}
    		
    	});
    }
    private void makeSave()
    {
    	sf.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doSave();	
			}
    		
    	});
    }
    private void makePrint()
    {
    	pf.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doPrint();	
			}
    		
    	});
    }
    private void makeCopy()
    {
    	c.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doCopy();	
			}
    		
    	});
    }
    private void makePaste()
    {
    	p.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doPaste();	
			}
    		
    	});
    }
    private void makeOpen()
    {
    	of.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doOpen();	
			}
    		
    	});
    }
    private void makeReplace()
    {
    	r.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doReplace();	
			}
    		
    	});
    }
    
    //	updates recent submenu
    private void updateRecent()
    {
    	rm.removeAll();	//	clear recent submenu
    	recentFiles.forEach(addRecentItem);	//	create and add menu items to recent submenu
    }
    
    //	add JMenuItem to recent submenu
    BiConsumer<File, String> addRecentItem = (f, fn) ->
    {
    	JMenuItem rf = new JMenuItem(fn);
    	rf.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doOpenFile(f);
			}
    		
    	});
    	rm.insert(rf, 0);	//	add JMenuItem to top of recent submenu, last added, most recent, goes on top
    	
    };
    
    //	functions that perform menu tasks
    private void doNew()
    {
    	d.setText("");
    }
    private void doSave()
    {
    	File fileToWrite = null;
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
            fileToWrite = fc.getSelectedFile();
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));
            out.println(d.getText());
            JOptionPane.showMessageDialog(null, "File is saved successfully...");
            out.close();
        } catch (IOException ex) {
        }
    }
    private void doPrint()
    {
    	try{
            PrinterJob pjob = PrinterJob.getPrinterJob();
            pjob.setJobName("Sample Command Pattern");
            pjob.setCopies(1);
            pjob.setPrintable(new Printable() {
                public int print(Graphics pg, PageFormat pf, int pageNum) {
                    if (pageNum>0)
                        return Printable.NO_SUCH_PAGE;
                    pg.drawString(d.getText(), 500, 500);
                    paint(pg);
                    return Printable.PAGE_EXISTS;
                }
            });

            if (pjob.printDialog() == false)
                return;
            pjob.print();
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(null,
                    "Printer error" + pe, "Printing error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void doCopy()
    {
    	d.copy();
    }
    private void doPaste()
    {
    	StyledDocument doc = d.getStyledDocument();
        Position position = doc.getEndPosition();
        d.paste();
    }
    private void doOpen()
    {
    	File fileToOpen = null;
    	JFileChooser fc = new JFileChooser();
    	int returnVal = fc.showOpenDialog(null);
    	if (returnVal == JFileChooser.APPROVE_OPTION)
    	{
    		fileToOpen = fc.getSelectedFile();
    	}
    	doOpenFile(fileToOpen);
    	
    }
    private void doOpenFile(File f)
    {
    	try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			d.read(br, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	recentFiles.put(f, f.getName());
    	updateRecent();
    }
    private void doReplace()
    {
    	//	spawn input window for replace
    	makeReplaceFrame();
    	
    }
    
    private void makeReplaceFrame()
    {
    	JFrame inputReplaceFrame = new JFrame("Input");
    	JLabel inputReplaceLabel = new JLabel("Replace or insert with:");
    	JTextField inputReplaceField = new JTextField();
    	JButton inputReplaceCancel = new JButton("Cancel");
    	JButton inputReplaceOK = new JButton("OK");
    	//	add ActionListeners to buttons
    	inputReplaceCancel.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				inputReplaceFrame.dispose();
				
			}
    		
    	});
    	
    	inputReplaceOK.addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				d.replaceSelection(inputReplaceField.getText());
				inputReplaceFrame.dispose();
				
			}
    		
    	});
    	
    	inputReplaceFrame.setLayout(new GridBagLayout());
    	
    	GridBagConstraints c = new GridBagConstraints();
    	c.weightx = 1;
    	c.weighty = 1;
    	//	add elements to frame
    	c.gridx=0;
    	c.gridy=0;
    	c.gridwidth=2;
    	c.gridheight=1;
    	inputReplaceFrame.add(inputReplaceLabel, c);
    	
    	c.gridx=0;
    	c.gridy=1;
    	c.gridwidth=2;
    	c.gridheight=1;
    	c.fill=GridBagConstraints.HORIZONTAL;
    	inputReplaceFrame.add(inputReplaceField, c);
    	c.fill=GridBagConstraints.NONE;	//	default
    	
    	c.gridx=0;
    	c.gridy=2;
    	c.gridwidth=1;
    	c.gridheight=1;
    	inputReplaceFrame.add(inputReplaceCancel, c);
    	c.gridx=1;
    	c.gridy=2;
    	c.gridwidth=1;
    	c.gridheight=1;
    	inputReplaceFrame.add(inputReplaceOK, c);
    	
    	inputReplaceFrame.setPreferredSize(new Dimension(300,200));
    	inputReplaceFrame.pack();
    	inputReplaceFrame.setVisible(true);
    		
    }
    
}