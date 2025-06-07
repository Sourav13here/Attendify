# 📚 Attendify - Smart Attendance Management System

> A modern Android application for seamless attendance tracking built with Jetpack Compose and Firebase

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.0-green.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-Latest-orange.svg)](https://firebase.google.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)

---

## 🚀 Features

### 👨‍🎓 For Students
- ✅ **Quick Check-In** - Mark attendance with a single tap
- 📊 **Attendance History** - View personal attendance records and statistics
- 📱 **Real-time Updates** - Get instant notifications about class schedules
- 🔐 **Secure Authentication** - Firebase-powered login system
- 📈 **Progress Tracking** - Monitor attendance percentage and trends
- 🎯 **Class Schedule View** - See upcoming classes and deadlines

### 👨‍🏫 For Teachers
- 📝 **Class Management** - Create and manage multiple classes efficiently
- 👥 **Student Roster** - Add, remove, and organize student information
- 📊 **Attendance Reports** - Generate detailed attendance analytics
- 📅 **Schedule Management** - Set up class timings and recurring sessions
- 🔍 **Quick Search** - Find students and attendance records instantly
- 📤 **Export Data** - Download attendance reports in various formats
- 🚨 **Absence Alerts** - Get notified about frequently absent students

---

## 🛠️ Tech Stack

| Technology | Purpose | Version |
|------------|---------|---------|
| **Kotlin** | Primary Language | 1.9.0+ |
| **Jetpack Compose** | Modern UI Toolkit | 1.5.0+ |
| **Firebase Auth** | User Authentication | Latest |
| **Firestore** | NoSQL Database | Latest |
| **Hilt** | Dependency Injection | 2.47+ |
| **MVVM** | Architecture Pattern | - |
| **Navigation Component** | App Navigation | 2.7.0+ |
| **Kotlin Coroutines** | Asynchronous Programming | 1.7.0+ |
| **Material Design 3** | UI Components | Latest |

---

## 📱 Screenshots

| Feature | Screenshot |
|---------|------------|
| **Login Screen** | ![Login](screenshots/login.png) |
| **Student Dashboard** | ![Student Dashboard](screenshots/student_dashboard.png) |
| **Teacher Dashboard** | ![Teacher Dashboard](screenshots/teacher_dashboard.png) |
| **Attendance Marking** | ![Attendance](screenshots/attendance_marking.png) |
| **Reports & Analytics** | ![Reports](screenshots/reports.png) |
| **Class Management** | ![Class Management](screenshots/class_management.png) |

---

## 📂 Project Structure

```
Attendify/
├── 📁 app/
│   ├── 📁 src/main/java/com/yourname/attendify/
│   │   ├── 📁 data/
│   │   │   ├── 📁 local/           # Room database entities
│   │   │   ├── 📁 remote/          # Firebase repositories
│   │   │   └── 📁 repository/      # Data layer implementation
│   │   ├── 📁 di/                  # Hilt dependency injection modules
│   │   ├── 📁 domain/
│   │   │   ├── 📁 model/           # Data models
│   │   │   ├── 📁 repository/      # Repository interfaces
│   │   │   └── 📁 usecase/         # Business logic use cases
│   │   ├── 📁 presentation/
│   │   │   ├── 📁 ui/
│   │   │   │   ├── 📁 auth/        # Authentication screens
│   │   │   │   ├── 📁 student/     # Student-specific UI
│   │   │   │   ├── 📁 teacher/     # Teacher-specific UI
│   │   │   │   ├── 📁 common/      # Shared UI components
│   │   │   │   └── 📁 theme/       # App theming
│   │   │   └── 📁 viewmodel/       # ViewModels for MVVM
│   │   ├── 📁 utils/               # Utility classes and extensions
│   │   └── 📄 MainActivity.kt      # Main entry point
│   ├── 📁 src/main/res/            # Android resources
│   └── 📄 build.gradle.kts         # App-level build configuration
├── 📄 build.gradle.kts             # Project-level build configuration
├── 📄 google-services.json         # Firebase configuration (add this)
└── 📄 README.md                    # This file
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

Copyright (c) 2024 Your Name

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

**Developer:** Mridul Roy
**GitHub:** [@Mridul60](https://github.com/Mridul60)  
**Email:** mridulroy543@gmail.com  
**LinkedIn:** [Your LinkedIn Profile](https://linkedin.com/in/mridul-roy-39b297263)



### 🔗 Connect with Us
- 💼 **Portfolio:** [yourportfolio.com](https://yourportfolio.com)
- 🐦 **Twitter:** [@yourusername](https://twitter.com/yourusername)
- 📧 **Contact:** your.email@example.com

---
