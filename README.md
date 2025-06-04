# 🚌 UNI Bus Shuttle

UNI Bus Shuttle is an Android application that allows university students to book private shuttle buses easily and efficiently. The app also provides drivers with a simple interface to manage ride requests in real time.

---

## 📱 Features

### 👨‍🎓 For Students:
- Sign up with full name, university ID, email, phone number, password, and optional profile picture.
- Book a bus by choosing a pickup time and location via Google Maps.
- View nearby available buses and estimated time of arrival.
- See fare calculated based on the distance (min 1 KWD, max 5 KWD).
- Preview driver details before confirming a booking.
- Receive real-time notifications:
  - Booking confirmation or rejection.
  - Driver arrival.
- Pay via cash or KNET payment gateway.

### 🚍 For Drivers:
- Receive booking requests with distance info.
- Accept or reject student requests.
- Navigate to student location via Google Maps.
- Notify student upon arrival.
- Update seat availability dynamically.
- Mark trip as completed upon drop-off.

---

## 🛠️ Tech Stack

- **Android (Kotlin)**  
  Core language used for app development with Android Studio.

- **Firebase Authentication**  
  For student and driver sign-in and secure account creation.

- **Firebase Firestore**  
  Cloud NoSQL database used to store user data, bus reservations, seat availability, and ride statuses efficiently and in real time.

- **Local Notifications**  
  Used to notify users about booking confirmation, driver arrival, and redirections without relying on external push notification services.

- **Firebase Storage**
  For uploading and retrieving user profile pictures.

- **Google Maps API & Location Services**  
  Enables real-time location selection, navigation, and distance-based fare calculation.

---

## 🧮 Fare Calculation Logic

- The fare is calculated as `0.1 KWD per kilometer`.
- **Minimum fare:** 1 KWD  
- **Maximum fare:** 5 KWD  
- Distance is calculated via Google Maps API between the student location and the university campus.

---

## 🚀 Getting Started

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/elsabagh/UniBus.git
