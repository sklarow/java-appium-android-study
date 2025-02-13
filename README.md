# Mobile App Automation Testing Project

## Introduction
This project is designed to automate the testing of the Swag Labs Android application using **Appium, Cucumber, and Java**. The framework ensures a **graceful execution of Appium and Appium Server**, supports **emulator** and should support real devices too, and generates **Cucumber reports** that are published on a GitHub page.

## Test Execution Report
The latest test execution report can be found in here:
https://sklarow.github.io/java-appium-android-study

## Project Overview
This project is a study of implementation of Appium to test the Android app provided by Sauce Labs that can be found here:
https://github.com/saucelabs/sample-app-mobile/releases

It was developed to practice the following stack:
- Cucumber Framework
- Appium
- Java Programming Language
- TestNG or JUnit

⚠️ Java and Cucumber are not my primary technologies, so I acknowledge that this code may 
not adhere to best practices. The Gherkin scenarios, as well as the Java classes, could likely 
be improved. However, this repository is intended for study purposes, and I do not wish to 
invest excessive time in refining it to perfection.


### **Features Implemented:**
- **Appium-based test automation** for Android applications.
- **Cucumber (Gherkin) BDD approach** for test scripting.
- **Execution on emulator (AVD).**
- **Graceful handling of Appium Server execution** (auto-start and auto-stop mechanisms included).
- **Minimal but important test cases covering:**
  - **Login functionality** (valid and invalid credentials handling).
  - **E2E Cart Test** guarantee that the happy path is working and also test a plentora of buttons, inputs and text fields.
- **Print screen of the last test screen for each test scenario**.
- **Reports generated using Cucumber HTML reports**.
- **Automatic hosting of test reports on a GitHub page.**

## Tech Stack
- **Programming Language:** Java
- **Testing Framework:** Cucumber (BDD)
- **Test Runner:** JUnit
- **Mobile Automation Tool:** Appium (UIAutomator2)
- **Reporting:** Cucumber HTML Reports

## Setup Instructions
### Prerequisites
Ensure you have the following installed on your system:
1. **Java Development Kit (JDK)** (Recommended: Java 11 or later)
2. **Android Studio & Android Emulator**
3. **Appium Server** (Can be installed via npm: `npm install -g appium`)
4. **Node.js and npm** (Required for Appium installation)
5. **Maven** (For project dependencies)

### Steps to Set Up
1. **Clone the repository:**
   ```bash
   git clone <repository-link>
   cd java-appium-android-study/androidcucumber
   ```
   Mind the java project folder ins /androidcucumber
2. **Install dependencies using Maven:**
   ```bash
   mvn clean install
   ```
3. **Run tests using Maven:**
   ```bash
   mvn test
   ```
    3.1 **Run just a feature file tests (for example the cart.feature):**
    ```bash
    mvn test -Dcucumber.features="src/test/resources/androidcucumber/cart.feature"
    ```
    3.2 **Run a specific test (for example the scenario named "Locked out login"):**
    ```bash
    mvn test -Dcucumber.filter.name="Locked out login"  
    ```
4. **View test reports:**
   - Reports are generated in `target/cucumber-reports/`.
   - The latest report is published on the project's **GitHub page** (link provided in the repository).

## Test Scenarios Implemented
The following test cases have been automated using Cucumber BDD:
- **Valid Login Test**: User can successfully log in.
- **Invalid Login Test**: Incorrect credentials show an error message.
- **End-to-end Car Test**
  - **Navigation Test**: User can navigate between pages.
  - **Button Interaction Test**: Ensures UI elements function as expected.
  - **User most important action covered**: Ensures the User cand login, add a product to the cart and checkout.

## TODO / Future Improvements
This project was **not focused on achieving extensive test coverage**, but rather on setting up a robust and scalable automation framework.
The key goal here was having a working github action worflow working capable of execute the scenarios, take screenshots and provide a report
in an easy to reach way.


Future improvements include:
- **Adding more test scenarios** to improve coverage.
- **Make use of Helpers and Pages** as the project grows, to avoid excessive responsabilities on the StepDefinitions.java
- **Enhance APK selection capabilities** so it can test another releases.
- **Add capabilitiy to choose the Emulator**
- **Test the execution on a Real Device**
- **Better logging instead of System.out.println all over the place**
- **Parallel execution of tests on multiple devices.**
- **Integration with cloud-based testing platforms (e.g., Sauce Labs, BrowserStack).**
- **Enhanced reporting and analytics for test results.**
- **More robust error handling for flaky test cases.**
- **Optimization of test execution time and resource usage.**

## Conclusion
This project efficiently automates Android app testing using **Appium and Cucumber BDD**, ensuring smooth execution, **server management**, and **reporting via GitHub pages**. 

## To take a look:
The execution report:
https://sklarow.github.io/java-appium-android-study

A Successfull job run:
https://github.com/sklarow/java-appium-android-study/actions/runs/13263927683/job/37026579927

The worflow file:
https://github.com/sklarow/java-appium-android-study/blob/main/.github/workflows/android-appium-tests.yml

The feature files:
https://github.com/sklarow/java-appium-android-study/tree/200cc73c6b147005370abe53f4e3ad6932ec760b/androidcucumber/src/test/resources/androidcucumber

The Appium Driver Manager:
https://github.com/sklarow/java-appium-android-study/blob/200cc73c6b147005370abe53f4e3ad6932ec760b/androidcucumber/src/test/java/utils/AppiumDriverManager.java


---
**For any questions or improvements, feel free to contribute or reach out!**

