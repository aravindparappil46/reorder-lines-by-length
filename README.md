# Reorder Lines By Length Plugin
IntelliJ plugin to reorder selected lines by their length! <br/>
Reduce cognitive load and make your code look pretty!

## Example
<i>Before</i>
``` java
this.queryBuilder = queryBuilder;
this.dao = dao;
this.validator = validator;
this.mapper = mapper;
```
<i>After</i>
``` java
this.dao = dao;
this.mapper = mapper;
this.validator = validator;
this.queryBuilder = queryBuilder;
```
## How To Use
* Select the lines of text you want to reorder
* Right click and choose `Reorder Lines By Length`

## What does it do?
* Grabs the selected text
* Splits them into an array by the new-line character
* Sorts the array by ascending order or string length
* Replaces selected text with the sorted text
* Indents the selected text as well, so that everything looks neat!

## Disclaimer
Please note that this will work only for groups of single lines i.e., 
the selected text should not contain any code that spans more than a single line <br/>
For example, the below snippet of code cannot be formatted.
``` java
// This will break since the code spans more than one line
Caret primaryCaret = editor.getCaretModel() 
        .getPrimaryCaret();
```