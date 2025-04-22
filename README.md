
# 🏠 SC2002 - BTO Management System (Build-To-Order Housing Application)

Welcome to the **SC2002 BTO Management System**, a Java-based Command Line Interface (CLI) application simulating the real-world BTO housing process in Singapore. Built by NTU students for the SC2002 Object-Oriented Design & Programming module, this system allows Applicants, HDB Officers, and HDB Managers to interact with BTO listings, applications, and administration seamlessly – all through a text-based interface.

---

## ✨ Features

### 🔐 Authentication
- Singpass-style NRIC login with password
- Role-based access (Applicant / Officer / Manager)
- Password change functionality

### 👤 Applicants
- View BTO project listings (filtered by marital status and age)
- Apply for eligible projects
- Track application status
- Withdraw applications
- Submit, edit, and delete enquiries
- Book a flat after successful application

### 🧑‍💼 HDB Officers
- Register to manage a project
- View applicant applications and reply to enquiries
- Assist with flat bookings
- Update project flat availability
- Generate booking receipts

### 👨‍💼 HDB Managers
- Create, edit, and delete BTO projects
- Toggle project visibility
- Approve or reject applications and officer registrations
- Manage withdrawals
- View and respond to enquiries
- Generate reports with filters (e.g., flat type, marital status)

---

## 🖥️ User Interface (CLI)

The system uses a clean, structured text-based UI for all roles:
![image](https://github.com/user-attachments/assets/3a1c7b9a-29e7-4f28-a6eb-80968053284b)
![image](https://github.com/user-attachments/assets/91d09fec-d71a-4473-91a4-80d360c88c82)
```plaintext
Welcome to the BTO Management System
1) Applicant Login
2) Officer Login
3) Manager Login
4) Exit
```

Upon login, each role gets their own dashboard and menu options, customized for their capabilities.

---

## 🛠️ Tech Stack

- Java 17
- Command Line Interface (CLI)
- Object-Oriented Programming (OOP)
- SOLID Design Principles
- Data Persistence via CSV

---

## 🚀 How to Run

### ✅ Prerequisites
- Java JDK 17 or higher installed
- Terminal (Linux/macOS) or Command Prompt (Windows)

### 🏃‍♂️ Compile & Run

From the **root directory**, run the following commands:

```bash
cd src
javac -d ../bin interact/MainMenu.java
cd ../bin
java interact.MainMenu
```

---

## 📂 Project Structure

```plaintext
SC2002-BTO-Management-System/
├── src/
│   ├── entity/         # Core models like User, Applicant, Manager, Project, etc.
│   ├── controller/     # ApplicantController, OfficerController, ManagerController
│   ├── boundary/       # CLI Menus (ApplicantUI, OfficerUI, ManagerUI)
│   ├── interact/       # MainMenu, UserSession, AuthenticationService
│   ├── data/           # CSV reading utility
├── bin/                # Compiled class files
├── data/
│   ├── applicants.csv
│   ├── officers.csv
│   └── managers.csv
└── README.md
```

---

## 📊 UML Class Diagram

- SOLID compliant design
- Abstract `User` superclass
- Role-specific subclasses (`Applicant`, `Officer`, `Manager`)
- Controllers and Boundaries follow clean MVC-ish separation


---

## 📄 Data Format (CSV)

CSV files are used to load users at startup:
```plaintext
Name,NRIC,Age,Marital Status,Password
John,S1234567A,35,Single,password
```

Ensure these are placed in the `data/` directory.

---

## 👥 Contributors

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/Nitecry7">
        <img src="https://github.com/Nitecry7.png" width="100" height="100" style="border-radius: 50%;"><br />
        <sub><b>Wayne Wee</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/stevennoctavianus">
        <img src="https://github.com/stevennoctavianus.png" width="100" height="100" style="border-radius: 50%;"><br />
        <sub><b>Steven</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Eishani">
        <img src="https://github.com/Eishani.png" width="100" height="100" style="border-radius: 50%;"><br />
        <sub><b>Hoang Thinh</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/MABSTAN">
        <img src="https://github.com/MABSTAN.png" width="100" height="100" style="border-radius: 50%;"><br />
        <sub><b>Mabel</b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/vanillatte11037">
        <img src="https://github.com/vanillatte11037.png" width="100" height="100" style="border-radius: 50%;"><br />
        <sub><b>Nigel</b></sub>
      </a>
    </td>
  </tr>
</table>

---

## 📚 JavaDocs

To generate JavaDocs:

```bash
cd src
javadoc -d ../docs -author -version -private -subpackages .
```

Then open `docs/index.html` in your browser.

---

## 🧪 Sample Login Credentials

### 🔸 Applicant
```
NRIC: S1234567A
Password: password
```

### 🔸 Officer
```
NRIC: T7654321B
Password: password
```

### 🔸 Manager
```
NRIC: S9876543C
Password: password
```

---

## 🎓 About

A role-based Java CLI system for managing Singapore's BTO application and housing process, developed for **NTU SC2002: Object-Oriented Design & Programming**.
