# LanguaLink Project Tasks

This file tracks the to-do and completed tasks for the LanguaLink project.

## Phase 1: Project Setup & Core Models

- [ ] Set up Room database dependencies.
- [ ] Define data models (`User`, `Language`, `Level`, `Exercise`, `Badge`) as Room entities.
- [ ] Create Data Access Objects (DAOs) for each entity.
- [ ] Create the main `AppDatabase` class.
- [ ] Create the `AppRepository` to abstract data access.
- [ ] Set up dependency injection (e.g., using Hilt) to provide the repository to ViewModels.

## Phase 2: Onboarding Flow

- [ ] Create `OnboardingFragment`.
- [ ] Design the UI for language and level selection.
- [ ] Create `OnboardingViewModel` to handle user selections.
- [ ] Save the user's choices to the database via the repository.
- [ ] Navigate to the main screen after onboarding is complete.

## Phase 3: Main App UI (Learn & Profile)

- [ ] Set up `MainActivity` with `BottomNavigationView` and `NavHostFragment`.
- [ ] Create `LearnFragment` and its layout.
- [ ] Create `LearnViewModel` to fetch and display exercises from the repository.
- [ ] Create a `RecyclerView` to display the list of exercises.
- [ ] Create `ProfileFragment` and its layout.
- [ ] Create `ProfileViewModel` to fetch and display user information (name, level, badges).

## Phase 4: Exercise Logic

- [ ] Create a basic UI for an exercise (e.g., a multiple-choice question).
- [ ] Implement logic to check the user's answer.
- [ ] Update the user's progress (e.g., `completedExerciseIds`) in the database.
- [ ] Implement logic for awarding badges based on progress.

## Phase 5: Polishing

- [ ] Add sample data to the database for testing.
- [ ] Refine UI and UX.
- [ ] Add proper navigation between screens.
- [ ] Handle edge cases (e.g., no exercises available).
