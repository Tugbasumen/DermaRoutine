namespace DermaRoutine.Api.Entities
{
    public class ProductIngredient
    {
        public int Id { get; set; }
        public int ProductId { get; set; }

        public string Name { get; set; } = string.Empty;
        public int Position { get; set; }
        public string? Description { get; set; }

        // Navigation Properties
        public Product Product { get; set; } = null!;
    }
}
