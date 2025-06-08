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
| **Kotlin Coroutines** | Asynchronous Programming         | 1.7.0+      |
| **Material 3**        | UI Components & Theming          | Latest      |
| **Room (optional)**   | Local Offline Storage (optional) | If used     |
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
│   ├── 📁 src/main/java/com/yourname/attendify/
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

## ⚙️ Setup Instructions

### Prerequisites
- **Android Studio** Flamingo or newer
- **JDK** 17 or higher
- **Minimum SDK** 24 (Android 7.0)
- **Target SDK** 34

### 🔧 Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/attendify.git
   cd attendify
   ```

2. **Firebase Configuration**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project or use existing one
   - Add Android app with package name `com.yourname.attendify`
   - Download `google-services.json`
   - Place it in `app/` directory

3. **Firebase Services Setup**
   ```bash
   # Enable the following in Firebase Console:
   # ✅ Authentication (Email/Password)
   # ✅ Firestore Database
   # ✅ Firebase Storage (optional)
   ```

4. **Build and Run**
   ```bash
   # Open in Android Studio and sync project
   # Or use command line:
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

5. **Environment Variables** (Optional)
   ```kotlin
   // Create local.properties file in root directory
   API_KEY="your_api_key_here"
   BASE_URL="your_backend_url_here"
   ```

---

## 🌐 Deployment Options

### Backend Extensions
- **Firebase Hosting** - Host web dashboard for teachers
- **Vercel** - Deploy additional web components
- **Firebase Functions** - Serverless backend logic
- **Firebase App Distribution** - Beta testing distribution

### Release Build
```bash
# Generate signed APK
./gradlew assembleRelease

# Generate App Bundle for Play Store
./gradlew bundleRelease
```

---

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### 📋 Contributing Steps

1. **Fork the Repository**
   ```bash
   # Click 'Fork' on GitHub or use GitHub CLI
   gh repo fork yourusername/attendify
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   # Example: git checkout -b feature/student-profile-update
   ```

3. **Make Your Changes**
   ```bash
   # Follow the existing code style
   # Add tests for new features
   # Update documentation if needed
   ```

4. **Commit Your Changes**
   ```bash
   git add .
   git commit -m "feat: add student profile update functionality"
   
   # Follow conventional commit format:
   # feat: new feature
   # fix: bug fix
   # docs: documentation changes
   # style: formatting changes
   # refactor: code refactoring
   # test: adding tests
   ```

5. **Push and Create PR**
   ```bash
   git push origin feature/your-feature-name
   # Then create Pull Request on GitHub
   ```

### 🎯 Contribution Guidelines
- Follow **MVVM architecture** patterns
- Write **unit tests** for new features
- Use **conventional commit** messages
- Ensure **code formatting** with ktlint
- Update **documentation** for API changes

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 Attendify

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

## 👨‍💻 Developers Information

## 👨‍💻 Developers

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


### 🔗 Connect with Us
- 📧 **Contact:** attendacemanagmentapp@gmail.com

---
