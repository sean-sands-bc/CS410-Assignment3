# CS410-Assignment3

## 1. Identify at least 4 "code smells" in descending order by smell

1. SimpleNotePad's constructor, SimpleNotePad(), lines 34-65, is a **long method**
2. SimpleNotePad's actionPerformed(ActionEvent e), lines 69-119, is a **long method**
3. The unimplemented "undo" functionality, lines 31, 41, 56, and 116-118, are **speculative generality**
4. SimpleNotePad's actionPerformed(ActionEvent e), lines 69-119, is a **switch statement**
5. The System.out.println on line 114 looks to be an artifact of testing, it is a **dispensable**.

## 2. Refactor the code. Provide a log of updates.

1. I shortened the constructor, SimpleNotePad(), lines 34-65, by performing multiple **method extractions**, organizing the constructor's functions by type and location. I moved lines 36-43 into makeMenus(), I moved lines 44-56 into makeMenuItems(), and I moved lines 57-59 into makeMenuBar().
2. I shortened actionPerformed(ActionEvent e), lines 69-119,  by performing multiple **method extractions**, placing the actions for each case in their own function. I moved line 72 into doNew(), I moved lines 74-85 into doSave(), I moved lines 87-108 into doPrint(), I moved line 110 into doCopy, and I moved lines 112-115 into doPaste().
3. I removed anything related to "undo", lines 31, 41, 56, and 116-118. I kept the TODO comment.
4. I removed SimpleNotePad's ActionListener implementation, and the associated actionPerformed() function. I then attached actionListeners to each MenuItem with their own function that I extracted in log entry 2, in effect **replacing parameters with explicit methods**
5. My previous refactor made makeMenuItems() a **long method**, so I performed **method extractions** for each MenuItem, moving their respective actionListener setup into makeNew(), makeSave(), makePrint(), makeCopy(), and makePaste().
6. I removed the console out on line 114.

## 3. Add new features.

1. I added an open file feature by adding an "Open File" menu item to the menu, and a doOpen() function attached to an ActionListener on that item by a makeOpen() function called in makeMenuItems().
2. I added a recent files feature by adding a HashMapList of Files and their names with a max length of 5, a "Recent" submenu that was populated by an updateRecent function with attached ActionListeners that call doOpenRecent(File).
3. I added a replace feature by adding a "Replace" menu item to the menu, and a doReplace() function attached to an ActionListener on that item by a makeReplace function called in makeMenuItems().
