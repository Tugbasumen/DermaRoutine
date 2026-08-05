using DermaRoutine.Api.Entities;
using Microsoft.EntityFrameworkCore;

namespace DermaRoutine.Api.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {
        }

        public DbSet<User> Users { get; set; }
        public DbSet<SkinProfile> SkinProfiles { get; set; }
        public DbSet<Product> Products { get; set; }
        public DbSet<ProductIngredient> ProductIngredients { get; set; }
        public DbSet<Routine> Routines { get; set; }
        public DbSet<RoutineItem> RoutineItems { get; set; }
        public DbSet<RoutineCompletion> RoutineCompletions { get; set; }
        public DbSet<RoutineItemCompletion> RoutineItemCompletions { get; set; }
        public DbSet<SkinPhoto> SkinPhotos { get; set; }
        public DbSet<JournalEntry> JournalEntries { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // User - SkinProfile (1:1)
            modelBuilder.Entity<User>()
                .HasOne(u => u.SkinProfile)
                .WithOne(p => p.User)
                .HasForeignKey<SkinProfile>(p => p.UserId)
                .OnDelete(DeleteBehavior.Cascade);

            // User - Products (1:N)
            modelBuilder.Entity<Product>()
                .HasOne(p => p.User)
                .WithMany(u => u.Products)
                .HasForeignKey(p => p.UserId);

            // Product - Ingredients (1:N)
            modelBuilder.Entity<ProductIngredient>()
                .HasOne(pi => pi.Product)
                .WithMany(p => p.Ingredients)
                .HasForeignKey(pi => pi.ProductId);

            // User - Routines (1:N)
            modelBuilder.Entity<Routine>()
                .HasOne(r => r.User)
                .WithMany(u => u.Routines)
                .HasForeignKey(r => r.UserId);

            // Routine - RoutineItems (1:N)
            modelBuilder.Entity<RoutineItem>()
                .HasOne(ri => ri.Routine)
                .WithMany(r => r.RoutineItems)
                .HasForeignKey(ri => ri.RoutineId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<RoutineItem>()
                .HasOne(ri => ri.Product)
                .WithMany(p => p.RoutineItems)
                .HasForeignKey(ri => ri.ProductId)
                .OnDelete(DeleteBehavior.Restrict);

            // User - RoutineCompletions (1:N)
            modelBuilder.Entity<RoutineCompletion>()
                .HasOne(rc => rc.User)
                .WithMany(u => u.RoutineCompletions)
                .HasForeignKey(rc => rc.UserId);

            // Routine - RoutineCompletions (1:N)
            modelBuilder.Entity<RoutineCompletion>()
                .HasOne(rc => rc.Routine)
                .WithMany(r => r.Completions)
                .HasForeignKey(rc => rc.RoutineId);

            // RoutineItem - Completions (1:N)
            modelBuilder.Entity<RoutineItemCompletion>()
                .HasOne(ric => ric.RoutineItem)
                .WithMany()
                .HasForeignKey(ric => ric.RoutineItemId);

            modelBuilder.Entity<RoutineItemCompletion>()
                .HasOne(ric => ric.User)
                .WithMany()
                .HasForeignKey(ric => ric.UserId);

            // User - SkinPhotos (1:N)
            modelBuilder.Entity<SkinPhoto>()
                .HasOne(sp => sp.User)
                .WithMany(u => u.SkinPhotos)
                .HasForeignKey(sp => sp.UserId);

            // User - JournalEntries (1:N)
            modelBuilder.Entity<JournalEntry>()
                .HasOne(je => je.User)
                .WithMany(u => u.JournalEntries)
                .HasForeignKey(je => je.UserId);
        }
    }
}
