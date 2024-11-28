# Project Overview
This Personal Informatics (PI) System is designed to help students improve their work-life balance by tracking and analyzing their activities. The system enables users to log and review their work and leisure hours, set goals, and track progress over time through visual insights and feedback. The project emphasizes a user-centered design and integrates features that promote productivity while fostering personal well-being.

This project was developed as a collaborative effort, leveraging Java for its backend and database management and adhering to Agile methodologies to ensure iterative progress and adaptability.

## Key Features
### Activity Logging:
Users can manually log activities into "Work" or "Life" categories and view historical data.

### Goal Setting:
Set personalized daily or weekly goals for work-life balance, with progress tracking.

### Visual Data Representation:
Graphical summaries (e.g., bar and pie charts) provide users with insights into their activity patterns over time.

### Data Persistence:
Activity and goal data are stored in a robust SQL database to ensure consistency and reliability.

## Modular Design:
The system uses a modular architecture, allowing future scalability and feature additions.

## Technologies Used
Java: Backend logic and data processing.
SQLite: Database management for persistent data storage.
JUnit: Unit testing to ensure reliability and correctness of the system.
Agile Development: SCRUM methodology for iterative development and collaborative teamwork.

### System Architecture
Frontend Interface:
Handles user interactions, such as activity logging, goal setting, and viewing visual data summaries.

### Backend Logic:
Processes and validates user inputs, manages goal tracking, and interacts with the database through the PIApp and DatabaseManager classes.

### Database:
A two-table structure:

Activities Table: Stores user activity data, including type (Work/Life), duration, and timestamp.

Goals Table: Tracks user-defined goals and progress.

###Testing Framework:

Comprehensive unit testing was implemented using JUnit, focusing on input validation, database operations, and system workflows.
