using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace DermaRoutine.Api.Migrations
{
    /// <inheritdoc />
    public partial class ImproveProductManagement : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            // Manual fix for Postgres type casting
            migrationBuilder.Sql("ALTER TABLE \"Products\" ALTER COLUMN \"Category\" TYPE integer USING (CASE WHEN \"Category\" IS NULL OR \"Category\" = '' THEN '0' ELSE \"Category\" END)::integer;");

            migrationBuilder.AlterColumn<int>(
                name: "Category",
                table: "Products",
                type: "integer",
                nullable: false,
                defaultValue: 0,
                oldClrType: typeof(string),
                oldType: "text",
                oldNullable: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<string>(
                name: "Category",
                table: "Products",
                type: "text",
                nullable: true,
                oldClrType: typeof(int),
                oldType: "integer");
        }
    }
}
