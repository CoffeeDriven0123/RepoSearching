# GitHub Repo Search App

A modern Android application for searching GitHub repositories, developed as a practice project to explore AI-assisted coding with **Antigravity**.

## 📱 Features
- Search GitHub repositories by keyword with debounce.
- Infinite scrolling (Pagination) support.
- Displays repository details including owner avatar, star count, and URL.
- Handles various UI states: Loading, Empty List, Network Error, and API Error.
- Mock mode for testing UI states without network dependencies.

## 🛠 Tech Stack & Architecture
This project follows the official [Android App Architecture](https://developer.android.com/topic/architecture) guidelines, divided into UI, Domain, and Data layers.

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for a declarative and reactive UI.
- **Language**: Kotlin
- **Architecture**: MVVM + Clean Architecture principles
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)
- **Networking**: [Retrofit](https://square.github.io/retrofit/) + OkHttp
- **JSON Parsing**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for efficient data loading.
- **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Testing**: JUnit 4, MockK, Coroutines Test, Paging Testing

## 🤝 AI Assistant
This project was initially scaffolded and developed through pair programming with **Google DeepMind's Antigravity (Gemini)**, demonstrating the capabilities of agentic AI coding assistants in building modern Android architectures.

---

# GitHub Repo 搜尋 App (繁體中文)

這是一個現代化的 Android 應用程式，用於搜尋 GitHub Repositories。這也是一個透過與 **Antigravity** 進行 AI 輔助協同開發的實戰練習專案。

## 📱 功能特色
- 透過關鍵字搜尋 GitHub Repositories (具備 Debounce 防手震機制)。
- 支援無限滾動 (分頁載入 / Pagination)。
- 顯示 Repository 詳細資訊，包含擁有者大頭貼、星數、以及網址。
- 完整處理多種 UI 狀態：載入中、查無資料、無網路連線、API 發生錯誤。
- 包含 Mock 模式，不需網路即可測試各種 UI 狀態與邊界情況。

## 🛠 技術堆疊與架構
本專案遵循官方的 [Android App Architecture](https://developer.android.com/topic/architecture) 指南，切分為 UI 層、Domain 層與 Data 層。

- **UI 框架**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (宣告式與響應式 UI)
- **開發語言**: Kotlin
- **架構設計**: MVVM + Clean Architecture 原則
- **依賴注入**: [Hilt](https://dagger.dev/hilt/)
- **網路請求**: [Retrofit](https://square.github.io/retrofit/) + OkHttp
- **JSON 解析**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **分頁處理**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- **圖片載入**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **非同步處理**: Kotlin Coroutines & Flow
- **單元測試**: JUnit 4, MockK, Coroutines Test, Paging Testing

## 🤝 AI 開發助手
本專案由 **Google DeepMind 的 Antigravity (Gemini)** 協助初始化與雙人協同開發 (Pair Programming)，展示了具備 Agentic 能力的 AI 在建立現代化 Android 架構上的應用成果。
