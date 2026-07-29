# Phase 4: Backend Foundation Implementation Plan

This phase focuses on setting up the ASP.NET Core Web API backend with PostgreSQL and EF Core. We will establish the core data models and relationships that will power the DermaRoutine application.

## User Review Required

> [!IMPORTANT]
> **Project Structure**: I will create a `backend` folder in the root directory. To avoid breaking your current Android Studio session, I will **not** move the existing Android files into an `android` subfolder yet, unless you confirm you are ready for a project-wide reorganization.
>
> **Database Credentials**: I will use a placeholder connection string in `appsettings.Development.json`. You will need to provide your local PostgreSQL credentials (server, port, database, user, password) for the migrations to run successfully.

## Proposed Changes

### 1. Project Initialization

#### [NEW] Backend Project
- Create directory `backend/DermaRoutine.Api`.
- Initialize a new ASP.NET Core Web API project using .NET 8.
- Add NuGet Packages:
    - `Npgsql.EntityFrameworkCore.PostgreSQL`
    - `Microsoft.EntityFrameworkCore.Design`
    - `Microsoft.EntityFrameworkCore.Tools`

### 2. Data Layer (EF Core & PostgreSQL)

#### [NEW] [AppDbContext.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Data/AppDbContext.cs)
- Define `DbSet` for all entities.
- Configure relationships (1:1, 1:N) and constraints using Fluent API.

#### [NEW] Configuration
- Configure PostgreSQL connection in `Program.cs`.
- Set up development connection string in `appsettings.Development.json`.

### 3. Entities (Domain Model)

#### [NEW] Core Entities
Create the following classes in `Entities/`:
- `User`, `SkinProfile`, `Product`, `ProductIngredient`, `Routine`, `RoutineItem`, `RoutineCompletion`, `SkinPhoto`, `JournalEntry`.
- Implement enums: `RoutineType`, `SkinType`, `SensitivityLevel`.

### 4. API Endpoints

#### [NEW] [HealthController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/HealthController.cs)
- Implement `GET /api/health` returning:
  ```json
  { "status": "ok", "service": "DermaRoutine API" }
  ```

### 5. Database Migration

- Create the initial migration: `InitialCreate`.
- (Optional/Manual) Update the database if credentials are provided.

## Verification Plan

### Automated Tests
- Run `dotnet build` to ensure the project compiles without errors.
- Run `dotnet ef migrations add InitialCreate` to verify the EF Core configuration.

### Manual Verification
- Start the API and access `http://localhost:<port>/swagger` to verify the Swagger UI and Health endpoint.
- Verify that the database schema is generated correctly in PostgreSQL (if credentials available).
