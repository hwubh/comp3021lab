# Code Review(Lab2)

## Task1: 
First, we need to design the structure. From the UML picture, we can see there is a base class called "Node" and two derived classed named "textNode" and "imageNode" respectively. Other than these, class "noteBook" contains "Folder" while class "Folder" contains "Note".
The first difficulty happened when we use the gernerator to create function "equals()". Function "equals" will compare all member variable by default but however, we only need to compare the "title". Hence, we should make some change to fulfill our requirement.
Another difficult point is we should convert "textNode" or "imageNode" into "Node" before we insert them to the "folder".

## Task2:
Failure may occur when there is a note in the traget folder, which is as same as the node moved. Therefore, it's necessary to compare the node being moved with the existing nodes of the target folder.
One possible way to move the node between folders is use the member functions of ArrayList, specifically "remove()" and "add()".
To record the history, we may add a member variable "ArrayList<*String*> history" and a member function called "record()". Each element of "history" combine with the name of "folder" and "Date". And function "record" will connvert and conbine the name of "folder" and "Date" into a string. Lastly, we need to call function "record" every time we move the "node".