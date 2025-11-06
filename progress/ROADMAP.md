# LanguaLink Functional Roadmap and Progress

This document outlines the functional roadmap for the LanguaLink application, tracking the progress of each feature from the initial MVP to future expansions. 

## Phase 1: The Core Single-Player Experience (MVP)

**Goal:** A new user can install the app, set up their profile, learn content, and track their own progress. (No social or online features).

### 1.1: First-Time User Onboarding

**As a new user, I want to:** Open the app and create a local profile so I can get started.

| Feature | Status | Details |
| :--- | :--- | :--- |
| Splash Screen | ✅ **Done** | A simple, welcoming screen is displayed on app launch. |
| Local "Registration" | 🚧 **In Progress** | A basic form asks for the user's name. This creates a local account on the phone. **Issue:** Data is not being saved when the app is closed. App is crashing. | 
| Language Selection | 🚧 **In Progress** | The user can see a list of available languages and pick the one they want to learn. **Issue:** The language list doesn't load until the screen is rotated. |
| Level Selection | ✅ **Done** | After picking a language, the user can select their current level (A1-C2). |

### 1.2: The Learning Hub ("Apprendre" Tab)

**As a user, I want to:** Easily find and start lessons that match my chosen language and level.

| Feature | Status | Details |
| :--- | :--- | :--- |
| Dashboard View | 🚧 **In Progress** | The main screen ("Apprendre") should show a list of chapters. **Issue:** No chapters are shown. |
| Chapter Details | 🚧 **In Progress** | Tapping a chapter shows the lessons inside it. **Issue:** App is crashing before this can be tested. |
| Lesson Variety (Local-Only) | 🚧 **In Progress** | Text/Content Lessons, Interactive Quizzes (QCM), and Simulated Chat are planned. Basic lesson display is implemented. |
| Lesson Navigation | 🚧 **In Progress** | "Next" and "Back" buttons are planned. |

### 1.3: Personal Progression ("Progression" Tab)

**As a user, I want to:** See my progress and feel motivated to continue learning.

| Feature | Status | Details |
| :--- | :--- | :--- |
| Lesson Completion | 🚧 **In Progress** | When a user finishes a lesson, it is marked as "complete". **Issue:** App is crashing before this can be tested. |
| Progression Dashboard | 🚧 **In Progress** | A dedicated tab shows the user's current level, a progress bar, and a points counter. **Issue:** App is crashing before this can be tested. |
| Points Counter | 🚧 **In Progress**| The user earns points for completing lessons. **Issue:** App is crashing before this can be tested. |
| Badges | 🚧 **In Progress** | A list of earned badges is planned. |
| "Bravo!" Modal | 🚧 **In Progress** | A popup appears to congratulate the user when they earn new points. **Issue:** App is crashing before this can be tested. |

### 1.4: Basic Profile ("Profil" Tab)

**As a user, I want to:** See my basic account details.

| Feature | Status | Details |
| :--- | :--- | :--- |
| Profile View | 🚧 **In Progress** | A simple screen shows the user's name and their current learning language/level. **Issue:** App is crashing before this can be tested. |

### 1.5: Placeholder Tab ("Amis" Tab)

**As a user, I want to:** Understand what all the tabs do.

| Feature | Status | Details |
| :--- | :--- | :--- |
| "Coming Soon" Screen | ✅ **Done** | The "Amis" (Friends) tab leads to a simple screen that says, "Social features are coming soon!" |

---

## Phase 2: The Social App (The Big Upgrade)

**Goal:** To transition the app from a local-only experience to a connected, multi-user social platform.

### 2.1: Real Online Authentication

**As a user, I want to:** Create a secure online account so my progress is saved and I can find friends.

| Feature | Status | Details |
| :--- | :--- | :--- |
| Real Registration | ❌ **To Do** | Full sign-up with email/password and social login (Google, Apple) using a backend (like Firebase). |
| Login Screen | ❌ **To Do** | The user will be able to log in to their account from any device. |
| Data Migration | ❌ **To Do** | The app should (ideally) upload the user's local progress (from Phase 1) to their new online account. |

### 2.2: The "Amis" Tab (Fully Functional)

**As a user, I want to:** Find and connect with other learners.

| Feature | Status | Details |
| :--- | :--- | :--- |
| User Search | ❌ **To Do** | A search bar to find other users by their username. |
| Friend Requests | ❌ **To Do** | The user will be able to send a friend request to other users. |
| Friend List | ❌ **To Do** | The "Amis" tab will show a list of the user's accepted friends. |

### 2.3: Real-Time Communication

**As a user, I want to:** Practice my language skills by talking to real people.

| Feature | Status | Details |
| :--- | :--- | :--- |
| Notifications | ❌ **To Do** | The user will get a notification (the bell icon) when someone adds them as a friend. |
| Real-Time Chat | ❌ **To Do** | The user will be able to select a friend from their list and start a 1-on-1 text chat with them. |
| Chat History | ❌ **To Do** | The user's conversations will be saved. |

---

## Phase 3: Future Polish & Expansion

**Goal:** To enrich the app with advanced features that make it a premium learning tool.

| Feature | Status | Details |
| :--- | :--- | :--- |
| Push Notifications | ❌ **To Do** | Send alerts to the user's phone when they get a new message or friend request. |
| Voice Chat | ❌ **To Do** | Add a feature to have real-time voice conversations with language partners. |
| Content Management | ❌ **To Do** | A web portal for developers to add new chapters and lessons without needing to update the app. |