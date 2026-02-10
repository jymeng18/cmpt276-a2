# CMPT276 Assignment 2 Desc
Simple CRUD app in Springboot using template engine Thyemleaf, connected to PostgreSQL for database.

# Author
Jerry Meng - 301612333

# Usage of AI: 
A few lines in form.css and index.css for some minmal styling for the UI,
and the factory design in StaffRatingTest.java.

# How to Run Locally
Navigate to A2Application.java and Run code, your local development server will start up. However,
to ensure full functionality, you need to create an applications.properties or in an .env file,
and connect your application with your Database.

# How to Deploy on Render

Create a Dockerfile first that containerizes our application, then replace the sensitive information 
in application.properties with environment variables. For example DATABASE_URL=${info}, and on Render
when we deploy, we can add our environment variables with the sensitive information, to ensure
no security issues occur with our app.