# D308 Vacation Planner 📅✈️ 

https://gitlab.com/wgu-gitlab-environment/student-repos/bmcd134/d308-mobile-application-development-android/-/tree/working_branch

## 📌 Purpose
The D308 Vacation Planner is an Android application built to help users organize and manage their vacations 
and excursions with ease. Users can create vacation entries, assign excursions to them with date validations, 
and edit or delete entries interactively. The app is designed with simplicity in mind while incorporating best 
practices using the Room persistence library for data storage.

---

## 🚀 Features

- Add, edit, and delete **vacations**
- Add, edit, and delete **excursions** associated with vacations
- Date validation ensures excursions occur within the vacation's date range
- **Set alerts for both vacations and excursions**.
- Toast-based alerts will trigger for upcoming or same-day events
- Share excursion details via native Android share intent
- Data persistence using **Room** + **ViewModel** architecture
- Signed APK ready for deployment or sharing

---

## 🎮 How to Use the App

### 1. **Home Page**
- Starts with a welcoming screen (“Expand your world”)
- Tap to begin vacation planning

### 2. **Add Vacation**
- Set vacation name, hotel, and dates using date pickers
- Save the vacation from the top menu

### 3. **Vacation List Tracker**
- Displays all vacations
- Tap ➕ to add more
- Tap a vacation name to view its details

### 4. **Vacation Detail Page**
- Shows selected vacation info and allows editing
- Set alert from top menu
- Share from top menu
- Delete Vacation from top menu as along as no excursions attached
- Tap ➕ to add an excursion

### 5. **Add Excursion**
- Input excursion title and date
- Excursion date must fall **within the vacation's range**
- Save via top-right menu

### 6. **Excursion Detail Page**
- View or edit excursion details
- Options available in the 3-dot menu:
    - Update excursion
    - Delete excursion
    - **Share via other apps**
    - **Set toast alert** for same-day or future excursions

---

## ⚠️ Important Behavior: Deleting Data

- To delete a **vacation**, you must **first delete all excursions** associated with it.
- This prevents data integrity issues due to foreign key constraints (`RESTRICT`).
- Attempting to delete a vacation with excursions will not succeed.

---

## 🔔 Alert System

- **Toast alerts** can be set for **vacations and excursions**
- If the date is **today**, a toast message pops up immediately (great for testing!)
- If the date is in the **future**, a toast alert is scheduled with a delay
- Past dates won’t trigger alerts

---

## 📦 APK Deployment

- Built and signed using Android Studio → **Build > Generate Signed APK**
- APK is located under: `app/release/`
- Target Android Version: **API 31 (Android 12)**

---

## 🛠️ Technical Stack

- **Language**: Java
- **Architecture**: MVVM (ViewModel + LiveData)
- **Database**: Room (SQLite abstraction)
- **UI Components**: MaterialDatePicker, Toolbar, Toast, RecyclerView
- **Min SDK**: 24
- **Target SDK**: 31

---

## 📲 Installing the APK

1. Transfer the `.apk` to your Android device
2. Enable `Install unknown apps` in device settings (under security)
3. Tap the `.apk` file in a file manager or downloads folder
4. Click **Install** to launch the app

---

## 🧪 Testing Verification

- Verified on Android Emulator (API 31) and physical Android devices
- Vacation and excursion date constraints tested
- Insert/update/delete functionality validated
- Alert timing and toast messages confirmed

---
