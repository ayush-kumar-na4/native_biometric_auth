# 🔐 Flutter Android Authentication

![Platform](https://img.shields.io/badge/platform-Android-green)
![Flutter](https://img.shields.io/badge/built_with-Flutter-blue)
![Status](https://img.shields.io/badge/status-Active-brightgreen)

A Flutter application integrating **Android’s native biometric and device credential authentication** using **Kotlin** via **Flutter Method Channels** — mimicking the secure experience of apps like CRED, PhonePe, and Paytm.

---

## 🚀 Features

- ✅ Native Android biometric authentication via Kotlin & Method Channels
- 🔁 Progressive Authentication Flow:
  - Biometric prompt shown at app startup
  - Fallback to PIN/Pattern/Password if biometrics fail
- 📲 Conditional Authentication:
  - If device has **no lock** → App is directly accessible
  - If lock exists → Auth prompt shown
- 🔄 System-managed lockout after multiple failed attempts

---

## 📱 App Behavior

1. **App Launch**
2. Check if device security is configured:
   - If **not configured** → Skip auth, enter app directly
   - If **configured** → Show biometric prompt
3. **Biometric Auth Flow**
   - Success → Access granted (“Welcome to the app!”)
   - Multiple biometric failures → Fallback to device PIN/pattern/password
   - Multiple PIN/password failures → Temporary system lockout
   - User cancels → App exits

---

## 🧠 Edge Cases Handled

- Device has **no screen lock** set
- User **cancels** biometric or PIN prompt
- **Biometric hardware not available**
- **No biometrics enrolled**, but PIN/Pattern exists
- System **lockout** behavior after repeated failures

---

## 🛠️ Tech Stack

| Layer              | Tech Used              |
|-------------------|------------------------|
| Frontend (UI)     | Flutter                |
| Platform Channels | Flutter MethodChannel  |
| Native Integration| Kotlin (Android)       |
| Authentication    | Android BiometricPrompt API |

---

## 📥 Download APK

You can download the latest APK release from Google Drive and test it on your own device:

👉 [Download APK](https://drive.google.com/file/d/1kNYvw9pbjMHL4wGrCc2a6kjUf2ahk7-q/view?usp=sharing)

---

## 📸 Screenshots

| 🔐 Biometric Prompt | 🔁 Fallback to PIN/Password | ✅ Welcome Screen | ❌ Wrong Credentials Attempted |
|---------------------|----------------------------|------------------|-------------------------------|
| ![Biometric Prompt](https://github.com/user-attachments/assets/84312d07-9933-4f70-be06-0f6db2062334) | ![Fallback Screen](https://github.com/user-attachments/assets/3c88dcec-427d-4e69-ad61-d44220633bda) | ![Successful Authentication](https://github.com/user-attachments/assets/782cb643-5c0d-4b11-8304-3a0be49d2970) | ![Too many incorrect attempts](https://github.com/user-attachments/assets/803027af-ece4-406a-9419-b84dc740fc5c) |

