# Phase 22: Room Persistence & Offline Support - Task List

- [ ] Build Configuration
    - [ ] Add Room dependencies to `libs.versions.toml`
    - [ ] Update `app/build.gradle.kts` with Room plugins and libs
- [x] Local Data Layer (Room)
    - [x] Create `ProductEntity.kt` and `ProductDao.kt`
    - [x] Create `RoutineEntity.kt`, `RoutineItemEntity.kt` and `RoutineDao.kt`
    - [x] Implement `DermaRoutineDatabase.kt`
    - [x] Add Room TypeConverters (`Converters.kt`)
- [ ] Repository Refactoring (Single Source of Truth)
    - [ ] Create `ProductMapper.kt` for local-domain mapping [/]
    - [ ] Create `RoutineMapper.kt` for local-domain mapping [/]
    - [ ] Refactor `ProductRepositoryImpl.kt` to cache network data in Room
    - [ ] Refactor `RoutineRepositoryImpl.kt` to handle offline completion states
- [x] DI Update
    - [x] Provide Database and DAOs in a new `DatabaseModule.kt`
- [ ] Build and Verify
    - [ ] Successful `gradlew assembleDebug`
    - [ ] Verify data persistence by disabling internet
