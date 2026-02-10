# Testing Documentation

Documentation for how unit tests are done, and how you can run them locally.

# Unit Test Structure (Controllers Section)

The assignment uses @SpringBootTest because I found online that it is apparently 
the closest thing to your real application in development server. I used @AutoConfigureMockMvc which
provides MockMvc, allowing us to simulate HTTP Requests, like GET and POST to our API endpoints. @SpringBootTest
also provides HTTP Requests, but I found the syntax to be really unintuitive, so I used MockMvc inside @SpringBootTest instead.
I also used @Transactional so that database queries in the testing environment would not mess up my actual production data, 
allowing all changes to be rolled back when done.

# What I Tested (Controllers)

- Proper display of the templates/pages upon a GET request from the user, as well as proper models/flash attributes, such as staff
ratings, and their average score of ratings

- Creating a New Staff Rating by form submission through POST request, with valid redirections and status codes:
  - Form with all valid data fields
  - Form where email is invalid (invalid email format)
  - Form where email is duplicate (email uniqueness)
  - Form where name field is blank (min char is 1)
  - Form that has out of range values for clarity, niceness, and knowledgeable

- Editing a Staff Rating by Form Submission through POST request, with valid redirections and status codes:
  - Form with all valid data
  - Form with invalid data
  - Form with duplicate email (that is not the email of the entry being edited)
  - Form with same email
  - Attempt to edit entry that is not saved in the DB, like GET /ratings/edit/9999999
  
- Delete operations, just two tests, a valid delete operation, and an attempt to delete a nonexistent entry from the DB.

# What I Tested (Class Methods)

- Proper input validation with the type annotations when using setters
- Unique Email
- No NULL values for scores
- Name must not be blank
- Polymorphism with ENUMS must work properly
- Proper Email Format
- Out of Range Values
- Comment Length
- Valid Staff Rating Entry

# How to Run Tests

I used VSCode and next to the class name like StaffRatingControllers, there was a 'Run Tests' Button you 
could click and it would run them all and show you their results, whether they passed or failed.