using DermaRoutine.Api.Data;
using DermaRoutine.Api.DTOs;
using DermaRoutine.Api.Entities;
using DermaRoutine.Api.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace DermaRoutine.Api.Controllers
{
    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class ProductController : ControllerBase
    {
        private readonly AppDbContext _context;
        private readonly IUserContext _userContext;

        public ProductController(AppDbContext context, IUserContext userContext)
        {
            _context = context;
            _userContext = userContext;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<ProductDto>>> GetProducts([FromQuery] string? search, [FromQuery] string? category)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var query = _context.Products.Where(p => p.UserId == userId);

            if (!string.IsNullOrEmpty(search))
            {
                query = query.Where(p => p.Name.ToLower().Contains(search.ToLower()) || p.Brand.ToLower().Contains(search.ToLower()));
            }

            if (!string.IsNullOrEmpty(category) && Enum.TryParse<Enums.ProductCategory>(category, out var catEnum))
            {
                query = query.Where(p => p.Category == catEnum);
            }

            var products = await query
                .Select(p => new ProductDto
                {
                    Id = p.Id,
                    Name = p.Name,
                    Brand = p.Brand,
                    Barcode = p.Barcode,
                    Category = p.Category,
                    ImageUrl = p.ImageUrl,
                    Notes = p.Notes
                })
                .ToListAsync();

            return Ok(products);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<ProductDetailDto>> GetProduct(int id)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var product = await _context.Products
                .Include(p => p.Ingredients)
                .FirstOrDefaultAsync(p => p.Id == id && p.UserId == userId);

            if (product == null) return NotFound();

            return Ok(new ProductDetailDto
            {
                Id = product.Id,
                Name = product.Name,
                Brand = product.Brand,
                Barcode = product.Barcode,
                Category = product.Category,
                ImageUrl = product.ImageUrl,
                Notes = product.Notes,
                Ingredients = product.Ingredients.OrderBy(i => i.Position).Select(i => new ProductIngredientDto
                {
                    Id = i.Id,
                    Name = i.Name,
                    Position = i.Position,
                    Description = i.Description
                }).ToList()
            });
        }

        [HttpPost]
        public async Task<ActionResult<ProductDto>> CreateProduct(CreateProductRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var product = new Product
            {
                UserId = userId.Value,
                Name = request.Name,
                Brand = request.Brand,
                Barcode = request.Barcode,
                Category = request.Category,
                Notes = request.Notes,
                CreatedAt = DateTime.UtcNow,
                UpdatedAt = DateTime.UtcNow
            };

            _context.Products.Add(product);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetProduct), new { id = product.Id }, new ProductDto
            {
                Id = product.Id,
                Name = product.Name,
                Brand = product.Brand,
                Barcode = product.Barcode,
                Category = product.Category,
                Notes = product.Notes
            });
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateProduct(int id, UpdateProductRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var product = await _context.Products.FirstOrDefaultAsync(p => p.Id == id && p.UserId == userId);
            if (product == null) return NotFound();

            product.Name = request.Name;
            product.Brand = request.Brand;
            product.Barcode = request.Barcode;
            product.Category = request.Category;
            product.Notes = request.Notes;
            product.UpdatedAt = DateTime.UtcNow;

            await _context.SaveChangesAsync();
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteProduct(int id)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var product = await _context.Products
                .Include(p => p.RoutineItems)
                .FirstOrDefaultAsync(p => p.Id == id && p.UserId == userId);

            if (product == null) return NotFound();

            if (product.RoutineItems.Any())
            {
                return Conflict(new { message = "This product is used in a routine. Remove it from your routines first." });
            }

            _context.Products.Remove(product);
            await _context.SaveChangesAsync();
            return NoContent();
        }

        [HttpPost("{id}/ingredients")]
        public async Task<IActionResult> AddIngredient(int id, AddIngredientRequest request)
        {
            var userId = await _userContext.GetUserIdAsync();
            if (userId == null) return Unauthorized();

            var productExists = await _context.Products.AnyAsync(p => p.Id == id && p.UserId == userId);
            if (!productExists) return NotFound();

            var ingredient = new ProductIngredient
            {
                ProductId = id,
                Name = request.Name,
                Position = request.Position,
                Description = request.Description
            };

            _context.ProductIngredients.Add(ingredient);
            await _context.SaveChangesAsync();

            return Ok();
        }
    }
}
