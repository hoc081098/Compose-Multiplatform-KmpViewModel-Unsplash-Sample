# Compose-Multiplatform-KmpViewModel-Unsplash-Sample

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fhoc081098%2FCompose-Multiplatform-KmpViewModel-Unsplash-Sample&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
[![Build Desktop App CI](https://github.com/hoc081098/Compose-Multiplatform-KmpViewModel-KMM-Unsplash-Sample/actions/workflows/build-desktop-app.yml/badge.svg)](https://github.com/hoc081098/Compose-Multiplatform-KmpViewModel-KMM-Unsplash-Sample/actions/workflows/build-desktop-app.yml)
[![Build Android App CI](https://github.com/hoc081098/Compose-Multiplatform-KmpViewModel-Unsplash-Sample/actions/workflows/build-android-app.yml/badge.svg)](https://github.com/hoc081098/Compose-Multiplatform-KmpViewModel-Unsplash-Sample/actions/workflows/build-android-app.yml)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.22-purple.svg?logo=kotlin)](http://kotlinlang.org)

This repo is a template for getting started with Compose Multiplatform or Kotlin Multiplatform with support for Android, iOS, and Desktop.

**Compose Multiplatform** sample:
 - https://github.com/hoc081098/kmp-viewmodel: Multiplatform ViewModel, SavedStateHandle
 - https://github.com/hoc081098/solivagant: Compose Multiplatform Navigation
- https://github.com/JetBrains/compose-multiplatform

Liked some of my work? Buy me a coffee (or more likely a beer)

<a href="https://www.buymeacoffee.com/hoc081098" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-blue.png" alt="Buy Me A Coffee" height=64></a>

### Modern Development
 - Kotlin Multiplatform
 - [JetBrains Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
 - [Kotlin Coroutines & Flows](https://github.com/hoc081098/FlowExt)
 - Koin Dependency Injection
 - Model-View-Intent (MVI) / FlowRedux state management
 - [Kotlin Multiplatform ViewModel](https://github.com/hoc081098/kmp-viewmodel)
 - Clean Architecture
 - Multiplatform type-safe navigation by [Freeletics/Khonshu](https://github.com/freeletics/khonshu)

https://user-images.githubusercontent.com/36917223/270357793-11cb7264-59fe-4f58-884a-c92c204b566f.mov

### Credits

- [Freeletics/Khonshu](https://github.com/freeletics/khonshu).

### Set up the environment

> **Warning**
> You need a Mac with macOS to write and run iOS-specific code on simulated or real devices.
> This is an Apple requirement.

To work with this template, you need the following:

* A machine running a recent version of macOS
* [Xcode](https://apps.apple.com/us/app/xcode/id497799835)
* [Android Studio](https://developer.android.com/studio)
* The [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
* The [CocoaPods dependency manager](https://kotlinlang.org/docs/native-cocoapods.html)

#### Check your environment

Before you start, use the [KDoctor](https://github.com/Kotlin/kdoctor) tool to ensure that your development environment
is configured correctly:

1. Install KDoctor with [Homebrew](https://brew.sh/):

    ```text
    brew install kdoctor
    ```

2. Run KDoctor in your terminal:

    ```text
    kdoctor
    ```

   If everything is set up correctly, you'll see valid output:

   ```text
   Environment diagnose (to see all details, use -v option):
   [✓] Operation System
   [✓] Java
   [✓] Android Studio
   [✓] Xcode
   [✓] Cocoapods

   Conclusion:
     ✓ Your system is ready for Kotlin Multiplatform Mobile development!
   ```

Otherwise, KDoctor will highlight which parts of your setup still need to be configured and will suggest a way to fix
them.
