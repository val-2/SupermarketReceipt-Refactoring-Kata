package dojo.supermarket.model;

import java.math.BigDecimal;

public class Discount {

    private final String description;
    private final BigDecimal discountAmount;
    private final Product product;

    public Discount(Product product, String description, BigDecimal discountAmount) {
        this.product = product;
        this.description = description;
        this.discountAmount = discountAmount;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public Product getProduct() {
        return product;
    }
}
