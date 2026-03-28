# Reusable Module: Generic CSV Export Engine

This document outlines the **Highly Decoupled CSV Export Engine** built for the backend. 

Because exporting database tables to spreadsheets is a requirement in nearly 100% of enterprise applications, this engine was designed to be completely agnostic. It knows nothing about the database. You can instantly drag-and-drop this into **any** Spring Boot project to instantly grant it enterprise-grade, memory-safe CSV streaming.

---

## 1. The Core Engine
Copy the entire file: `src/main/java/com/blogapp/common/service/CsvExportService.java`.

This service contains a single generic method:
```java
public <T> void exportCsvToResponse(
    HttpServletResponse response,
    String filename,
    String[] headers,
    Iterable<T> data,
    Function<T, String[]> rowMapper
)
```
**Why this is enterprise grade:**
1. **Memory Safe Stream**: It does not build a massive String in RAM. It pipes the data directly through the `HttpServletResponse` writer, meaning you can export 10 million rows without crashing a $5 VPS.
2. **Auto-Escaping**: If a user submits a review containing commas (`"Great course, I loved it"`), standard CSVs will break. This engine automatically detects internal commas, quotes, and newlines and securely wraps the cell in double-quotes.
3. **UTF-8 BOM**: It automatically injects the exact Byte-Order-Mark required for Microsoft Excel to render international characters (like Hindi or Arabic) perfectly.

---

## 2. How to use it in any Controller 

To export *any* database table (Users, Products, Testimonials, etc.), just inject the `CsvExportService` into your REST Controller and follow these 3 steps:

```java
@RestController
@RequestMapping("/api/admin/export")
@RequiredArgsConstructor
public class AdminExportController {

    private final CsvExportService csvExportService;
    private final ProductRepository productRepository; // 1. Fetch data from anywhere

    @GetMapping("/products")
    public void exportProducts(HttpServletResponse response) {
        
        List<Product> data = productRepository.findAll();

        // 2. Define exactly what your Excel columns should be named
        String[] headers = {"ID", "Product Name", "Price", "In Stock"};

        // 3. Map your Java Object into the columns!
        csvExportService.exportCsvToResponse(
                response, 
                "products_export.csv", 
                headers, 
                data, 
                (Product p) -> new String[]{
                        p.getId(),
                        p.getName(),
                        String.valueOf(p.getPrice()),
                        p.isInStock() ? "Yes" : "No"
                }
        );
    }
}
```

## Security Note
Because exporting a database dumps sensitive user data, **always** ensure the endpoints invoking this engine are strictly locked behind an `.authenticated()` or `.hasRole("ADMIN")` filter inside your `SecurityConfig.java`.
