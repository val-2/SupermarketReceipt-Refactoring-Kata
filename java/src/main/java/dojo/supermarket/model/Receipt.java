package dojo.supermarket.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {

    private final List<ReceiptItem> items = new ArrayList<>();
    private final List<Discount> discounts = new ArrayList<>();

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (ReceiptItem item : items) {
            total = total.add(item.getTotalPrice());
        }
        for (Discount discount : discounts) {
            total = total.add(discount.getDiscountAmount());
        }
        return total;
    }

    public void addProduct(Product p, double quantity, BigDecimal price) {
        BigDecimal computedTotal = price.multiply(BigDecimal.valueOf(quantity));
        items.add(new ReceiptItem(p, quantity, price, computedTotal));
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
}
