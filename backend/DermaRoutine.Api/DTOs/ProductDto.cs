using DermaRoutine.Api.Enums;

namespace DermaRoutine.Api.DTOs
{
    public class ProductDto
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public string Brand { get; set; } = string.Empty;
        public string? Barcode { get; set; }
        public ProductCategory Category { get; set; }
        public string? ImageUrl { get; set; }
        public string? Notes { get; set; }
    }

    public class ProductDetailDto : ProductDto
    {
        public List<ProductIngredientDto> Ingredients { get; set; } = new();
    }

    public class ProductIngredientDto
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public int Position { get; set; }
        public string? Description { get; set; }
    }

    public class CreateProductRequest
    {
        public string Name { get; set; } = string.Empty;
        public string Brand { get; set; } = string.Empty;
        public string? Barcode { get; set; }
        public ProductCategory Category { get; set; }
        public string? Notes { get; set; }
    }

    public class UpdateProductRequest : CreateProductRequest
    {
    }

    public class AddIngredientRequest
    {
        public string Name { get; set; } = string.Empty;
        public int Position { get; set; }
        public string? Description { get; set; }
    }

    public class BulkAddIngredientsRequest
    {
        public List<AddIngredientRequest> Ingredients { get; set; } = new();
    }
}
