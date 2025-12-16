package dojo.supermarket.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ReceiptItem {

    private final Product product;
    private final BigDecimal price;
    private final BigDecimal totalPrice;
    private final Quantity quantity;

    ReceiptItem(Product p, Quantity quantity, BigDecimal unitPrice) {
        this.product = p;
        this.quantity = quantity;
        this.price = unitPrice;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity.getDouble()));
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReceiptItem)) {
            return false;
        }
        ReceiptItem that = (ReceiptItem) o;
        return Objects.equals(price, that.price) &&
                Objects.equals(totalPrice, that.totalPrice) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, price, totalPrice, quantity);
    }
}
