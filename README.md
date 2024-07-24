# Fast Reminder Project

## Overview

The Fast Reminder Project is a tool designed to help users manage their fasting reminders based on the lunar calendar. The application supports both Marathi tithis and weekly reminders. Users will receive notifications via WhatsApp message.

## Features

1. **User Authentication:**
   - Users log in using their username and contact number.

2. **Reminder Creation:**
   - After logging in, users are directly taken to the reminder creation section.
   - To set a reminder, select the Fast Type. Based on this selection, the available options for "Moon Phase," "Week Days," and "Schedule" will adjust accordingly.

     - **Tithis:**
       - **Moon Phase:** Options for moon phases related to tithis will be available.
       - **Schedule:** Choose how often the reminder should recur (e.g., once, every week, every month).

     - **Weekly Reminders:**
       - **Week Days:** Select the days of the week for the reminder.
       - **Schedule:** Choose how often the reminder should recur (e.g., every week).



## Getting Started

### Prerequisites

- Java 21
- Spring Boot 3.2.5
- Maven for dependency management
- Vaadin for the frontend

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/aniketsabaleapptware/fast-reminder-project.git
2. Install dependencies and build the project:

   ```bash
   mvn clean install
3. Run the application:
  
   ```bash
   mvn spring-boot:run

## Usage
### Login:

Access the login page and enter your username and contact number.
### Add Reminder:

- After logging in, users are directly taken to the reminder creation section.
- Select the Fast Type. Based on this selection, the available options for "Moon Phase," "Week Days," and "Schedule" will adjust accordingly:
  - **Tithis:**
       - **Moon Phase:** Options for moon phases related to tithis will be available.
       - **Schedule:** Choose how often the reminder should recur (e.g., Once, Every Month, Months).

   - **Weekly Reminders:**
       - **Week Days:** Select the days of the week for the reminder.
       - **Schedule:** Choose how often the reminder should recur (e.g., Once, Every Week).

- You will receive a WhatsApp notification one day before the selected reminder.

