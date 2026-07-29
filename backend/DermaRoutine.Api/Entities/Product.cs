using DermaRoutine.Api.Enums;

namespace DermaRoutine.Api.Entities
{
    public class Product
    {
        public int Id { get; set; }
        public int UserId { get; set; }

        public string Name { get; set; } = string.Empty;
        public string Brand { get; set; } = string.Empty;
        public string? Barcode { get; set; }
        public ProductCategory Category { get; set; }
        public string? ImageUrl { get; set; }
        public string? Notes { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
        public DateTime UpdatedAt { get; set; } = DateTime.UtcNow;

        // Navigation Properties
        public User User { get; set; } = null!;
        public ICollection<ProductIngredient> Ingredients { get; set; } = new List<ProductIngredient>();
        public ICollection<RoutineItem> RoutineItems { get; set; } = new List<RoutineItem>();
    }
}
