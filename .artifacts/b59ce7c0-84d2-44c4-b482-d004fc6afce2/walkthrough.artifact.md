# Phase 4: Backend Foundation Walkthrough

Phase 4 is completed. I have established the ASP.NET Core Web API foundation with a PostgreSQL database schema and Entity Framework Core integration.

## Changes Made

### 1. Project Infrastructure
- **Technology**: Created a new ASP.NET Core 8 Web API project in `backend/DermaRoutine.Api`.
- **NuGet Packages**:
    - `Npgsql.EntityFrameworkCore.PostgreSQL`: For PostgreSQL integration.
    - `Microsoft.EntityFrameworkCore.Design` & `Tools`: For EF Core migrations.

### 2. Domain Models (Entities)
- **Entities**: Implemented all specified entities in the `Entities/` folder:
    - `User`, `SkinProfile`, `Product`, `ProductIngredient`, `Routine`, `RoutineItem`, `RoutineCompletion`, `SkinPhoto`, `JournalEntry`.
- **Enums**: Created strongly-typed enums for `RoutineType`, `SkinType`, and `SensitivityLevel`.

### 3. Data Layer & EF Core
- **AppDbContext**: Created the central database context with explicit Fluent API configurations for:
    - 1:1 Relationship between `User` and `SkinProfile`.
    - 1:N Relationships for Products, Routines, Photos, and Journal Entries.
    - Many-to-Many logic handled via join tables/explicit keys for `RoutineItems`.
- **Migration**: Generated the `InitialCreate` migration files which represent the full database schema.

### 4. API Endpoints
- **HealthCheck**: Added `HealthController` with a `GET /api/health` endpoint to verify the API's status.
- **Swagger**: Configured Swagger UI, accessible at `/swagger` in development mode.

## Manual Steps Required (Important)

> [!IMPORTANT]
> **To complete the database setup, follow these steps:**
> 1. Open `backend/DermaRoutine.Api/appsettings.Development.json`.
> 2. Update the `DefaultConnection` string with your local PostgreSQL **Username** and **Password**.
> 3. Open a terminal in `backend/DermaRoutine.Api`.
> 4. Run the following command to create the tables in your PostgreSQL instance:
>    ```bash
>    dotnet ef database update
>    ```

## Build Status
- [x] Project builds successfully (`dotnet build`).
- [x] Initial migration created and ready for execution.
- [x] API configured on default ports 5222 (HTTP) and 7151 (HTTPS).
