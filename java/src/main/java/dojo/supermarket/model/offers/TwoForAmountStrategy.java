package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import java.math.BigDecimal;

public class TwoForAmountStrategy extends SingleProductOfferStrategy {
    private final BigDecimal amount;

    public TwoForAmountStrategy(Product product, BigDecimal amount) {
        super(product);
        this.amount = amount;
    }

    @Override
    public Discount computeForProduct(Product product, BigDecimal unitPrice, double quantity) {
        int quantityAsInt = (int) quantity;
        int packSize = 2;
        if (quantityAsInt >= packSize) {
            BigDecimal packsTotal = amount.multiply(BigDecimal.valueOf(quantityAsInt / packSize));
            BigDecimal remainderTotal = unitPrice.multiply(BigDecimal.valueOf(quantityAsInt % packSize));
            BigDecimal total = packsTotal.add(remainderTotal);
            BigDecimal full = unitPrice.multiply(BigDecimal.valueOf(quantity));
            BigDecimal discountAmount = full.subtract(total);
            if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                return new Discount(product, "2 for " + amount, discountAmount.negate());
            }
        }
        return null;
    }
}
