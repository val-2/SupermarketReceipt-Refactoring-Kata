package dojo.supermarket;

import dojo.supermarket.model.*;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

public class ReceiptPrinter {

    private static final int DEFAULT_COLUMNS = 40;

    private final int columns;

    public ReceiptPrinter() {
        this(DEFAULT_COLUMNS);
    }

    public ReceiptPrinter(int columns) {
        this.columns = columns;
    }

    public String printReceipt(Receipt receipt) {
        StringBuilder result = new StringBuilder();
        result.append(presentItems(receipt));
        result.append(presentDiscounts(receipt));
        result.append("\n");
        result.append(presentTotal(receipt));
        result.append(presentLoyaltyPoints(receipt));
        return result.toString();
    }

    private String presentItems(Receipt receipt) {
        StringBuilder result = new StringBuilder();
        for (ReceiptItem item : receipt.getItems()) {
            result.append(presentReceiptItem(item));
        }
        return result.toString();
    }

    private String presentDiscounts(Receipt receipt) {
        StringBuilder result = new StringBuilder();
        for (Discount discount : receipt.getDiscounts()) {
            result.append(presentDiscount(discount));
        }
        return result.toString();
    }

    private String presentLoyaltyPoints(Receipt receipt) {
        if (receipt.getEarnedPoints() > 0) {
            return "\nLoyalty Points Earned: " + receipt.getEarnedPoints() + "\n";
        }
        return "";
    }

    private String presentReceiptItem(ReceiptItem item) {
        String totalPricePresentation = presentPrice(item.getTotalPrice());
        String name = item.getProduct().getName();

        String line = formatLineWithWhitespace(name, totalPricePresentation);

        if (item.getQuantity().getDouble() != 1) {
            line += "  " + presentPrice(item.getPrice()) + " * "
                    + presentQuantity(item.getProduct(), item.getQuantity()) + "\n";
        }
        return line;
    }

    private String presentDiscount(Discount discount) {
        String name = discount.getDescription();
        ProductQuantities productQuantities = discount.getProductQuantities();

        if (!productQuantities.isEmpty()) {
            StringJoiner joiner = new StringJoiner(", ", "(", ")");
            for (var entry : productQuantities) {
                joiner.add(formatProductEntry(entry));
            }
            name += joiner.toString();
        }

        String value = presentPrice(discount.getDiscountAmount().negate());
        return formatLineWithWhitespace(name, value);
    }

    private String formatProductEntry(Map.Entry<Product, Quantity> entry) {
        String productName = entry.getKey().getName();
        double quantity = entry.getValue().getDouble();

        if (quantity != 1.0 && quantity != 0.0) {
            return productName + " x" + presentQuantityValue(entry.getValue());
        }
        return productName;
    }

    private String presentTotal(Receipt receipt) {
        String name = "Total: ";
        String value = presentPrice(receipt.getTotalPrice());
        return formatLineWithWhitespace(name, value);
    }

    private String formatLineWithWhitespace(String name, String value) {
        int whitespaceSize = Math.max(0, this.columns - name.length() - value.length());
        return name + " ".repeat(whitespaceSize) + value + "\n";
    }

    private static String presentPrice(BigDecimal price) {
        return String.format(Locale.UK, "%.2f", price);
    }

    private static String presentQuantity(Product product, Quantity quantity) {
        if (ProductUnit.EACH.equals(product.getUnit())) {
            return String.format("%d", (int) quantity.getDouble());
        }
        return presentQuantityValue(quantity);
    }

    private static String presentQuantityValue(Quantity quantity) {
        double value = quantity.getDouble();
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        }
        return String.format(Locale.UK, "%.3f", value);
    }
}
