# 📚 Attendify - Smart Attendance Management System

> A modern Android application for seamless attendance tracking built with Jetpack Compose and Firebase

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-green.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-Latest-orange.svg)](https://firebase.google.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

---

## 🚀 Features

### 👨‍🎓 For Students
- 📊 **Detailed Attendance Records** – View comprehensive logs of your attendance across subjects and dates
- 🔔 **Live Attendance Updates** – Instantly see attendance as marked by the teacher
- 📈 **Attendance Prediction** – Get smart insights into future attendance trends and required presence to meet goals
- 🔐 **Secure Authentication** – Firebase-powered login system


### 👨‍🏫 For Teachers
- 📝 **Class Management** – Create and manage multiple classes efficiently
- ✅ **Mark Attendance** – Easily mark attendance for students in each session
- 📤 **Export Reports (.xlsx)** – Download Excel reports containing student names and their attendance percentages
- 🔍 **Verification System** – Verify student accounts and manage unverified teacher profiles

---

## 🛠️ Tech Stack

## 🛠️ Tech Stack

| Technology            | Purpose                          | Version     |
|-----------------------|----------------------------------|-------------|
| **Kotlin**            | Primary Language                 | 1.9.0+      |
| **Jetpack Compose**   | Modern UI Toolkit                | 1.5.0+      |
| **Firebase Auth**     | User Authentication              | Latest      |
| **Firestore**         | NoSQL Database                   | Latest      |
| **Hilt**              | Dependency Injection             | 2.47+       |
| **MVVM**              | Architecture Pattern             | ✅ Used      |
| **Navigation Compose**| Navigation between screens       | 2.7.0+      |
| **Material 3**        | UI Components & Theming          | Latest      |
| **Excel Export**      | Report Generation in `.xlsx`     | Apache POI / Other |


---

## 📱 Screenshots

| Feature | Screenshot |
|---------|------------|
| **Login Screen** | <img src="https://github.com/Sourav13here/Attendify/blob/master/assests/1.jpg?raw=true" alt="Login" width="300"/> |
| **Signup Screen** | <img src="https://github.com/Sourav13here/Attendify/blob/master/assests/3.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Verification Status** | <img src="https://github.com/Sourav13here/Attendify/blob/master/assests/4.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Student Dashboard** |  <img src="https://github.com/Sourav13here/Attendify/blob/master/assests/8.jpg?raw=true" alt="Screenshot" width="300"/>  |
| **Instructions** |<img src="https://github.com/Sourav13here/Attendify/blob/master/assests/15.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Attendance Tracking** |<img src="https://github.com/Sourav13here/Attendify/blob/master/assests/14.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Attendance Details** |<img src="https://github.com/Sourav13here/Attendify/blob/master/assests/13.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Teacher Dashboard** |<img src="https://github.com/Sourav13here/Attendify/blob/master/assests/5.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Add Subject** |<img src="https://github.com/Sourav13here/Attendify/blob/master/assests/6.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Attendance Marking** | <img src="https://github.com/Sourav13here/Attendify/blob/master/assests/9.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Verification List** |<img src="https://github.com/Sourav13here/Attendify/blob/master/assests/7.jpg?raw=true" alt="Screenshot" width="300"/> |
| **Reports & Analytics** |<img src="https://github.com/Sourav13here/Attendify/blob/master/assests/12.jpg?raw=true" alt="Screenshot" width="300"/>|


---

## 📂 Project Structure

```
Attendify/
├── 📁 app/
    ├── 📁 release/                  # Release artifacts or configurations
│   ├── 📁 src/main/java/com/attendify/
│   │   ├── 📁 common/               # Shared components, extensions, and viewmodels
│   │   │   ├── 📁 composable/       # Reusable Composables
│   │   │   ├── 📁 ext/              # Extension functions
│   │   │   └── 📁 viewmodel/        # Common/shared ViewModels
│   │   ├── 📁 data/                 # Data layer
│   │   │   ├── 📁 model/            # Data models
│   │   │   ├── 📁 module/           # Dependency injection modules
│   │   │   └── 📁 repository/       # Data repositories (Firebase/local)
│   │   ├── 📁 navigation/           # Navigation graph and routes
│   │   ├── 📁 ui/                   # UI screens
│   │   │   ├── 📁 login/            # Login screen
│   │   │   ├── 📁 sign_up/          # Sign-up screen
│   │   │   ├── 📁 splashscreen/     # Splash screen
│   │   │   ├── 📁 student/          # Student-specific UI
│   │   │   ├── 📁 teacher/          # Teacher-specific UI
│   │   │   ├── 📁 verification/     # Verification screens
│   │   │   └── 📁 theme/            # App theming (Material 3, typography, colors)
│   │   ├── 📁 utils/                # Utility classes and helpers
│   │   ├── 📄 AttendifyAppHilt.kt   # Hilt application class
│   │   ├── 📄 AttendifyAppUi.kt     # Root UI composable or navigation host
│   │   └── 📄 MainActivity.kt       # App entry point
│   ├── 📄 .gitignore                # Git ignore rules specific to the module
│   ├── 📄 build.gradle.kts          # App-level Gradle configuration
│   ├── 📄 google-services.json      # Firebase config file
│   └── 📄 proguard-rules.pro        # Proguard rules for code obfuscation
├── 📄 build.gradle.kts              # Project-level Gradle configuration
└── 📄 README.md                     # Project documentation

```
---
### Want to try? Install the app - https://drive.google.com/file/d/1r6rM8jhHD9P1hTAH4FC3BbxKuRMbPNxJ/view?usp=drivesdk

---

## 👨‍💻 Developers Information

### 🚀 Faruk Khan  
- **GitHub:** [@FarukKhan](https://github.com/iamfaruk01)  
- **Email:** faruk.khan.cse@gmail.com  
- **LinkedIn:** [Faruk](https://www.linkedin.com/in/iamfarukkhan)

### 🚀 Mridul Roy  
- **GitHub:** [@MridulRoy](https://github.com/Mridul60)  
- **Email:** roy.mridul.cse@gmail.com  
- **LinkedIn:** [Mridul](https://linkedin.com/in/mridul-roy-39b297263)

### 🚀 Sourav Sharma  
- **GitHub:** [@SouravSharma](https://github.com/SouravSharma)  
- **Email:** souravsharma13here@gmail.com  
- **LinkedIn:** [Sourav](https://www.linkedin.com/in/sourav13sharma/)

---
