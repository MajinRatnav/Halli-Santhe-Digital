# 🛕 Halli-Santhe Digital — Android App

Hyper-local marketplace connecting rural artisans with urban buyers.

---

## 📁 Project Structure

```
app/src/main/
├── java/com/halliSanthe/
│   ├── data/
│   │   ├── model/          # Product, Artisan, Message data classes
│   │   └── repository/     # ProductRepository, Room DB, DAO
│   ├── ui/
│   │   ├── home/           # MainActivity, HomeFragment (buyer browse)
│   │   ├── buyer/          # ProductAdapter, ProductDetailFragment
│   │   └── artisan/        # Dashboard, AddProduct, Messages, Adapters
│   └── viewmodel/          # ProductViewModel (shared)
└── res/
    ├── layout/             # All XML layouts
    ├── navigation/         # nav_graph.xml
    ├── menu/               # bottom_nav_menu.xml
    ├── values/             # colors, strings, themes
    └── anim/               # Slide animations
```

---

## 🚀 Setup Instructions

### 1. Create Android Studio Project
- Open Android Studio → New Project → Empty Activity
- Package name: `com.halliSanthe`
- Language: **Kotlin**
- Min SDK: **24**
- Copy all files from this project into your project

### 2. Firebase Setup
1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a new project → Add Android app (`com.halliSanthe`)
3. Download `google-services.json` → place in `app/` folder
4. Enable **Firestore Database** (start in test mode)
5. Enable **Firebase Storage**
6. Enable **Firebase Authentication** (Phone or Anonymous)
7. Deploy `firestore.rules` in Firebase Console → Firestore → Rules

### 3. Add Required Icons
Place these vector drawables in `res/drawable/`:
- `ic_home.xml` — house icon
- `ic_store.xml` — shop/store icon
- `ic_message.xml` — chat/message icon
- `ic_add.xml` — plus icon
- `ic_edit.xml` — pencil icon
- `ic_delete.xml` — trash icon
- `ic_save.xml` — save/check icon
- `ic_search.xml` — search magnifier
- `ic_product_placeholder.xml` — placeholder product image
- `ic_artisan_avatar.xml` — default person avatar

> **Tip:** Use Android Studio's built-in Vector Asset Studio (right-click `drawable` → New → Vector Asset) to add Material icons for all of the above.

### 4. Add Drawables
Create these in `res/drawable/`:

**bg_search.xml** (rounded search background):
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FFFFFF" />
    <corners android:radius="24dp" />
</shape>
```

**bg_avatar_circle.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="#FFEEDD" />
</shape>
```

**bg_message_bubble.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="#FFF3E0" />
    <corners android:radius="8dp" />
</shape>
```

**bg_chip_outline.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <stroke android:width="1dp" android:color="@color/primary_saffron" />
    <corners android:radius="12dp" />
</shape>
```

**chip_selector.xml** (color state list in `res/color/`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="@color/primary_saffron" android:state_checked="true" />
    <item android:color="#FFDCC0" />
</selector>
```

**chip_text_selector.xml** (color state list in `res/color/`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="@color/white" android:state_checked="true" />
    <item android:color="@color/primary_saffron" />
</selector>
```

### 5. Optional: Add Custom Font
In `res/font/`, add `tiro_kannada.ttf` from [Google Fonts](https://fonts.google.com/specimen/Tiro+Kannada) for the app title.

---

## 🏗️ Architecture

```
UI (Fragments) ←→ ViewModel ←→ Repository ←→ Firebase Firestore
                                           ↕
                                        Room DB (offline cache)
```

- **MVVM** pattern with LiveData and Coroutines
- **Repository** abstracts Firebase + Room with offline-first strategy
- **Single ViewModel** (`ProductViewModel`) shared across fragments via `activityViewModels()`
- **Image compression** before upload (Compressor library)

---

## 📱 Screens

| Screen | Description |
|--------|-------------|
| **Home (Explore)** | Grid browse with search + category chip filter |
| **Product Detail** | Full info + inquiry CTA button |
| **Inquiry Dialog** | Buyer sends name/phone/message to artisan |
| **Artisan Dashboard** | List of artisan's own products, edit/delete |
| **Add/Edit Product** | Form with image picker + compression |
| **Messages (Inquiries)** | Artisan sees all buyer inquiries |

---

## 🔮 Future Enhancements

- Firebase Auth (phone OTP) for artisan login
- Push notifications for new inquiries
- Google Maps integration for artisan village location
- UPI payment integration
- Kannada / regional language support (i18n)
- Product rating & reviews
