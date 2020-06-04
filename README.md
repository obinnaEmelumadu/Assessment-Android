# Assessment Android
 
Assessment Guidelines
Please read this section. Very Important
This task has been tailored to match the sort of tasks you will be required to do on a daily basis. Your
overall score will be dependent on several aspects of your work
1. Your ability to understand the given problem
2. Your ability to follow laid out instructions
3. Your ability to find and implement simple solutions to complex problems
4. The quality of your code; aim to use the simplest implementations while keeping the code as
decoupled as possible.
5. Your ability to create an application with good User Experience and User Interface
6. Your use of Git (branch and commit style)
7. Your ability to write unit tests
Assessment Task
You will be working with 2 resources
1. An excel file which contains the records of car owners in the United States over several years. You
are to download this list and save it on your device in a folder called Decagon. Here is the link to
download the file [ https://drive.google.com/file/d/1giBv3pK6qbOPo0Y02H-wjT9ULPksfBCm/view ].
2. A GET API which gives a list of filters. You will fetch this when the application is launched and display
the contents on a page. Here is the link to the API endpoint
[https://android-json-test-api.herokuapp.com/accounts
Application Workflow
Please see the wireframe for the application on page 3 & 4 of this document
1. User installs and opens the app and a list of filters is displayed
2. User clicks on a filter and the filtered list of car owners is displayed
3. If the filter returns an empty list display a message to inform the user
Technical Requirements
1. Fetch the list of predefined filters from an API
Assume that more records can be added to this list and it is expected to work properly or that
the list can be empty. If a property of a filter is empty or null then ignore that property
2. Process and display the list
All properties of the filter and the car owners should be displayed on the list (except id)
3. Save the excel file locally on your device in a folder called decagon
Please do not rename the file or change the format. Handle cases for when this file is not found
4. Write a testable function which will handle the filteration
5. Write unit tests for this function
6. Ensure to use Git and write useful commit messages
Here is a link that may help you https://chris.beams.io/posts/git-commit
7. Create a Pull Request between your working branch (develop) and master
8. Important: The name of the application should be your full name
9. You will be judged on UI as well so find a good user interface online which you can
copy. Here are some links from Dribble and Pinterest to help you
Submission
To submit your assessment, send an email with the following
1. Send the link of your GitHub repo that hosts your code
2. Google Drive link to the Application APK file
3. Link to your code on any git platform
4. Link to a Pull Request you have created to merge your code.