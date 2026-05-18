# 🛕 Halli-Santhe Digital

**A hyper-local marketplace Android application connecting rural artisans with urban buyers.**

Halli-Santhe Digital bridges the gap between traditional rural craftsmanship and modern urban consumers by enabling artisans to showcase and sell handmade products through a mobile platform.

---

## 📱 Features

### 🏠 Buyer Side
- Browse artisan products in a clean grid layout
- Search products instantly
- Filter products using category chips
- View detailed product information
- Send inquiries directly to artisans

### 🧑‍🎨 Artisan Side
- Add new products with images
- Edit or delete existing products
- Manage personal product listings
- Receive buyer inquiries/messages
- Dashboard for product management

### ⚡ Core Features
- Firebase Firestore integration
- Offline-first architecture using Room Database
- Image compression before upload
- MVVM architecture with ViewModel + Repository
- Smooth navigation with animations

---

## 🏗️ Tech Stack

### Frontend
- **Kotlin**
- **XML Layouts**
- Android Jetpack Components
- Navigation Component
- RecyclerView

### Backend & Database
- **Firebase Firestore**
- Firebase Storage
- Firebase Authentication
- **Room Database** (Offline Cache)

### Architecture
- **MVVM (Model-View-ViewModel)**
- Repository Pattern
- LiveData
- Coroutines

---

## 📂 Project Structure

```text
app/src/main/
├── java/com/halliSanthe/
│   ├── data/
│   │   ├── model/
│   │   │   ├── Product
│   │   │   ├── Artisan
│   │   │   └── Message
│   │   └── repository/
│   │       ├── ProductRepository
│   │       ├── Room Database
│   │       └── DAO
│   │
│   ├── ui/
│   │   ├── home/
│   │   │   ├── MainActivity
│   │   │   └── HomeFragment
│   │   │
│   │   ├── buyer/
│   │   │   ├── ProductAdapter
│   │   │   └── ProductDetailFragment
│   │   │
│   │   └── artisan/
│   │       ├── Dashboard
│   │       ├── AddProduct
│   │       ├── Messages
│   │       └── Adapters
│   │
│   └── viewmodel/
│       └── ProductViewModel
│
└── res/
    ├── layout/
    ├── navigation/
    ├── menu/
    ├── values/
    └── anim/
```

---

## 🚀 Setup Instructions

### 1. Clone Repository

```bash
git clone https://github.com/your-username/HalliSanthe.git
```

Open the project in **Android Studio**.

---

### 2. Project Configuration

Create an Android Studio project with:

- **Package Name:** `com.halliSanthe`
- **Language:** Kotlin
- **Minimum SDK:** 24

Sync Gradle dependencies.

---

### 3. Firebase Setup

1. Go to **Firebase Console**
2. Create a new Firebase project
3. Add Android App:

```text
com.halliSanthe
```

4. Download:

```text
google-services.json
```

5. Place it inside:

```text
app/
```

Enable:

- Firestore Database *(Test Mode)*
- Firebase Storage
- Firebase Authentication *(Phone / Anonymous)*

Deploy:

```text
firestore.rules
```

in Firebase Console → **Firestore → Rules**

---

## 🎨 Required Assets

### Vector Icons

Add the following drawable icons in:

```text
res/drawable/
```

- `ic_home.xml`
- `ic_store.xml`
- `ic_message.xml`
- `ic_add.xml`
- `ic_edit.xml`
- `ic_delete.xml`
- `ic_save.xml`
- `ic_search.xml`
- `ic_product_placeholder.xml`
- `ic_artisan_avatar.xml`

💡 **Tip:** Use Android Studio →  
`Drawable > New > Vector Asset`

to add Material Icons.

---

## 🎭 Custom Drawables

Required drawable resources:

- `bg_search.xml`
- `bg_avatar_circle.xml`
- `bg_message_bubble.xml`
- `bg_chip_outline.xml`

Required color selectors:

```text
res/color/
```

- `chip_selector.xml`
- `chip_text_selector.xml`

---

## 🔤 Optional Font

Add:

```text
tiro_kannada.ttf
```

inside:

```text
res/font/
```

for Kannada-inspired branding.

---

## 🧠 Architecture

```text
UI (Fragments)
      ↕
 ViewModel
      ↕
 Repository
      ↕
Firebase Firestore
      ↕
 Room Database
```

### Architecture Highlights
- **MVVM Pattern**
- Shared `ProductViewModel`
- `activityViewModels()` across fragments
- Offline-first strategy
- LiveData + Coroutines
- Repository abstraction for Firebase + Room

---

## 📸 App Screens

| Screen | Description |
|--------|-------------|
| Home (Explore) | Product grid with search & category filters |
| Product Detail | Full product details with inquiry option |
| Inquiry Dialog | Buyer inquiry form |
| Artisan Dashboard | Manage artisan products |
| Add/Edit Product | Product upload with image compression |
| Messages | Buyer inquiries for artisans |

---

## 🔮 Future Enhancements

- Firebase Phone OTP Login
- Push Notifications
- Google Maps Integration
- UPI Payment Gateway
- Kannada / Regional Language Support
- Product Ratings & Reviews

---

## 👨‍💻 Developed Using

- Kotlin
- Firebase
- Room Database
- Android Jetpack
- MVVM Architecture

---

## 📜 License

This project is developed for educational and internship evaluation purposes.
