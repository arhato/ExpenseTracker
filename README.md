Overview

The Expense Tracker app is designed to help users track and manage their personal expenses.
It allows the users to add, view and categorize their expenses with an easy-to-use interface. The
app provides insights into spending habits and helps users monitor their financial health
through visual representations of their data.
This app feature three main screen that provides different functionalities, including adding new
expense, updating and deleting them, viewing statistics, settings options and other features.

Layout and Functionalities

  1. Main Scaffold
  
    • Functionality: This is the main layout for the app. It has a TopBar, BottomBar and the Floating
    Action Button (FAB) and are consistent throughout the three main screens.
    
    • Layout:
    o TopBar has a Text and a dropdown menu button to go to certain screens.
    o BottomBar has icons to navigate to the other screens of the application.
    o A FAB at the bottom right opens dialog to add a new expense.
    o The dialog has a form for data entry and a cancel and save button to add the entry.
    
    • Actions:
    o Clicking the icons on the bottom bar opens the respective screens.
    o Clicking on the FAB brings up an alert dialog for the user for expense entry.
    o Clicking on the three-dot menu opens options to navigate to Settings and About menus.
    o Clicking on the Date field open a DataPicker.

  ![image](https://github.com/user-attachments/assets/4a577812-1e16-4d4a-bdc1-163302319d1c)    ![image](https://github.com/user-attachments/assets/8f45074f-a1ed-4d13-9d77-c3dc37abab44)

  3. Home Screen
  
    • Functionality: Displays a list of expenses that have been added by the user. Each list item
    shows the expense details such as date, amount, pay type (e.g., cash, credit), and category
    (e.g., food, transportation). Each of these expenses are clickable which opens a dialog to
    change and save or delete them. This dialog also shows more details of the entry like the
    description and location of the expense.
    
    • Layout:
    o The list of expenses is group according to their date. A header is shown for the date of
    expense.
    o Each expense has an icon to represent the category the day and date, the amount,
    category and the payment type.
    o A scroll up button appears when scrolled down.
    
    • Actions:
    o Clicking on an expense open the expense page where users can see more information.
    o User can change the data of that expense.
    o Users can delete that expense.
    o Clicking the scroll up button takes the user to the top of the list.
    
  ![image](https://github.com/user-attachments/assets/830150ea-631e-41ea-b86f-3ad252d2e23d)    ![image](https://github.com/user-attachments/assets/450c5ca7-2dbb-4ee4-8f5e-a735e81d4aae)

  4. Stats Screen
  
    • Functionality: Displays a breakdown of the user’s expenses in the form of chart. It provides
    insights into the total spending by category.
    
    • Layout:
    o A pie chart visualizes the expenses on each category.
    o A list of categories with their corresponding total expenses.
    o All the expenses on each category can be views when clicking on that category.
    
    • Actions:
    o Users can click on the category to look at the entries of that category.
    o Users can click on the expense on the category list to view the expense dialog.

  ![image](https://github.com/user-attachments/assets/46aa23c8-69ba-4409-86b0-e0776d7878d6)    ![image](https://github.com/user-attachments/assets/f046d6a4-9497-4bd0-b8ce-c53f93116f6f)

  5. More Screen
  
    Main
    
        • Functionality: This page allows users access to some
        customizable, security and file export features for the
        application.
        
        • Layout:
        o Four icons within a box which the user can click to go
        to the respective options.
        
        • Actions:
        o Clicking the icons takes the user to that specific
        screen.

  ![image](https://github.com/user-attachments/assets/7dc0f3b5-9b2a-4ad8-802a-2a4e926816b9)

    Settings
    
        • Functionality: This page allows users access to
        customizability features for the application. User can
        change the theme and the currency for the application.
        
        • Layout:
        o Theme change option has a switch the user can
        toggle for a dark theme.
        o Currency change option has a dropdown menu
        where the user can select from multiple currencies.
        
        • Actions:
        o Toggling the dark mode switch changes the app
        theme between a dark and a light theme.
        o Clicking the menu opens a dropdown menu for user
        to choose their preferred currency.
        
  ![image](https://github.com/user-attachments/assets/a2398984-279b-42d3-8682-4924f54bdc84)

    Export
    
      • Functionality: This page allows users access to ability to export
      either a JSON or a CSV file of their data into their device file system.
      
      • Layout:
      o Users have button to select the file type they want.
      o They can user the Select here button to choose a location
      and save the file.
      
      • Actions:
      o Click the button changes the file type for the export.
      o Clicking the Select here button prompt the user to choose
      the location and save the file.
      
  ![image](https://github.com/user-attachments/assets/1cc6a94b-358e-4285-840f-f8cf6e8b6ba3)

    Secure
    
      • Functionality: This page allows users setup a pin lock to secure the app.
      
      • Layout:
      o No PIN: A setup button that open screen for user to enter a new pin.
      o PIN setup: Two buttons that the user can use to remove or change the PIN. User is
      prompted to enter the PIN before being able to change or remove it.
      
      • Actions:
      o Clicking the buttons opens screen for the respective options.
     
  ![image](https://github.com/user-attachments/assets/d149af08-383e-449f-9ecc-aa50038a53f3)    ![image](https://github.com/user-attachments/assets/0e64576a-8066-417a-946c-ee7e4ebf6b0c)    ![image](https://github.com/user-attachments/assets/f6d0b8c4-c44f-4a66-9b0a-79c0aa0c5b24)

    About
    
      • Functionality: This page has not function. It
      displays some information about the app.
      
      • Layout:
      o Four sections of header and a body of text.
      
      • Actions: No actions.
      
  ![image](https://github.com/user-attachments/assets/fa4ebf23-6d48-494c-b609-d8b8c67c30bd)
  
  Sensor Use
  
    • GPS Sensor: Users have the choice to include their location when add a new expense to the
    app. The GPS functionality can help users see their spending habits in different locations and
    can be used to offer more personalized recommendations or insights.
    User can view this location data when clicking on one of the expenses in either the Home page
    or in the category list in the Stats page.
    [A Google Cloud API is used for this and the file that holds the API key is not included in the
    remote GitHub repository.]
    [Biometric sensor use added in Milestone 2 has been removed.]
  
  Storage
  
    • Storage Mechanism:
    
    o Room Database: The Room Database is utilized to store and manage all expense data,
    ensuring efficient and secure handling of user transactions.
    
    o Shared Preferences will be used for storing user settings like currency choice, theme
    choice, variables for app function and other preferences.
