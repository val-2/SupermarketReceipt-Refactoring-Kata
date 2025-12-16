package dojo.supermarket.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {

    private final List<ReceiptItem> items = new ArrayList<>();
    private final List<Discount> discounts = new ArrayList<>();
    private int earnedPoints;

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (ReceiptItem item : items) {
            total = total.add(item.getTotalPrice());
        }
        for (Discount discount : discounts) {
            total = total.subtract(discount.getDiscountAmount());
        }
        return total;
    }

    public void addProduct(Product p, Quantity quantity, BigDecimal unitPrice) {
        items.add(new ReceiptItem(p, quantity, unitPrice));
    }

    public List<ReceiptItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return Collections.unmodifiableList(discounts);
    }

    public void addEarnedPoints(int points) {
        this.earnedPoints += points;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }
}
