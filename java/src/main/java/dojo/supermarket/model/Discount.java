package dojo.supermarket.model;

import java.math.BigDecimal;

public class Discount {

    private final String description;
    private final BigDecimal discountAmount;
    private final ProductQuantities productQuantities;

    public Discount(Product product, Quantity quantity, String description, BigDecimal discountAmount) {
        ProductQuantities quantities = new ProductQuantities();
        quantities.add(product, quantity);
        this.productQuantities = quantities;
        this.description = description;
        this.discountAmount = discountAmount;
    }

    public Discount(ProductQuantities productQuantities, String description, BigDecimal discountAmount) {
        this.productQuantities = new ProductQuantities(productQuantities);
        this.description = description;
        this.discountAmount = discountAmount;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public ProductQuantities getProductQuantities() {
        return productQuantities;
    }
}
