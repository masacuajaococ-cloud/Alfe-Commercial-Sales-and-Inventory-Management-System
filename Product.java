public class Product {

    private String productId;
    private String name;
    private String category;
    private String brand;
    private String variant;
    private String size;
    private String specification;
    private String color;
    private String unit;
    private double price;
    private int stock;
    private String status;

    public Product(String productId,
                   String name,
                   String category,
                   String brand,
                   String variant,
                   String size,
                   String specification,
                   String color,
                   String unit,
                   double price,
                   int stock,
                   String status) {

        this.productId = productId;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.variant = variant;
        this.size = size;
        this.specification = specification;
        this.color = color;
        this.unit = unit;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getVariant() {
        return variant;
    }

    public String getSize() {
        return size;
    }

    public String getSpecification() {
        return specification;
    }

    public String getColor() {
        return color;
    }

    public String getUnit() {
        return unit;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getStatus() {
        return status;
    }
}