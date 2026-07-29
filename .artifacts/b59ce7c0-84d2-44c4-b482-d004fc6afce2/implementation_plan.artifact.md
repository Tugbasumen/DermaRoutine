# Phase 8: Product Management Implementation Plan

This phase focuses on the "Product Library" feature, allowing users to manage their personal skincare products and their ingredients. We will also integrate these real products into the routine management system.

## User Review Required

> [!IMPORTANT]
> **Product Deletion**: I will implement a safety check to prevent users from deleting a product that is currently used in one or more routines. The API will return a `409 Conflict` in this case.
>
> **Ingredient Entry**: For this phase, ingredients are manually entered as a simple list. Automated OCR and AI analysis will be handled in later phases.

## Proposed Changes

### 1. Backend (DermaRoutine.Api)

#### [NEW] [ProductCategory.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Enums/ProductCategory.cs)
- Enum for categories: `Cleanser`, `Toner`, `Serum`, `Moisturizer`, `Sunscreen`, `Mask`, `Exfoliant`, `EyeCare`, `Treatment`, `Other`.

#### [MODIFY] [Product.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Entities/Product.cs)
- Update `Category` type to `ProductCategory` (will require a migration).

#### [NEW] [ProductController.cs](file:///C:/kotlinproje/DermaRoutine/backend/DermaRoutine.Api/Controllers/ProductController.cs)
- `GET /api/products`: Filtered/Searched list of user's products.
- `POST /api/products`: Create product.
- `GET /api/products/{id}`: Detailed product info with ingredients.
- `PUT /api/products/{id}`: Update info.
- `DELETE /api/products/{id}`: Secure deletion with routine check.
- `POST /api/products/{id}/ingredients`: Manage ingredients manually.

### 2. Android (app)

#### [NEW] Domain Layer
- **Models**: `Product`, `ProductCategory`, `ProductIngredient`.
- **Use Cases**: `GetProductsUseCase`, `CreateProductUseCase`, `UpdateProductUseCase`, `DeleteProductUseCase`, `SearchProductsUseCase`.

#### [NEW] Data Layer
- **DTOs**: `ProductDto`, `ProductIngredientDto`, `CreateProductRequest`.
- **Repository**: `ProductRepositoryImpl` using Retrofit `ProductApiService`.

#### [NEW] Presentation Layer
- **Product List Screen**: Search bar, category filter chips, and product grid/list.
- **Product Detail Screen**: Displays full info, notes, and the ingredient list.
- **Add/Edit Product Screen**: Form with validation for creating or modifying products.

### 3. Routine Integration
- **Add to Routine**: Update `RoutineDetailScreen` to navigate to a "Select Product" screen.
- **Selector**: A simplified version of the product list where clicking a product adds it to the current routine.

## Verification Plan

### Automated Tests
- `dotnet build` & `gradlew assembleDebug`.
- Verify `409 Conflict` when trying to delete a product used in a routine.

### Manual Verification
1. **Add Product**: Add "Hydrating Cleanser" under the "Cleanser" category.
2. **Search/Filter**: Search for "Hydrating" or filter by "Cleanser" to find it.
3. **Routine Link**: Add this cleanser to your "Morning Routine".
4. **Delete Safety**: Try deleting the cleanser while it's in the routine (should show an error).
5. **Isolation**: Verify User A cannot see User B's product list via Swagger.
