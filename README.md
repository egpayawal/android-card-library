# Android Card-Library

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=17)
[![](https://jitpack.io/v/egpayawal/android-card-library.svg)](https://jitpack.io/#egpayawal/android-card-library)

## Technology Stack

* Android Studio Bumblebee | 2021.1.1 Patch 1
* Android SDK 30
* Supported OS: Min 17 up to latest
* Gradle 7.2


## Demo

<img src="https://user-images.githubusercontent.com/6327558/159071085-6b2004c8-1381-4289-9a18-98da20233576.gif" height="800">

## Download
add the dependency to your project `build.gradle` file:
```kotlin
dependencies {
  implementation 'com.github.egpayawal:android-card-library:$version'
}
```

## Usage
```xml
<com.egpayawal.card_library.view.CardTextInputEditText
    android:id="@+id/txtInputCard"
    style="@style/Styles.Input.TextInputEditText.Filled.Outlined"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:displayCardIcon="true"
    app:separatorState="spaceSpan" />
```
or

```xml
<com.egpayawal.card_library.view.CardTextInputEditText
    android:id="@+id/txtInputCard"
    style="@style/Styles.Input.TextInputEditText.Filled.Outlined"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:displayCardIcon="true"
    app:separatorState="spaceWithDash" />
```

For card expiration:
```kotlin
binding.txtInputCardExpiration.addTextChangedListener(CreditCardExpiryTextWatcher(binding.txtInputCardExpiration))
```
