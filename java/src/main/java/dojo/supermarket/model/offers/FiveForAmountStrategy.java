package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import java.math.BigDecimal;

public class FiveForAmountStrategy extends SingleProductOfferStrategy {
    private final BigDecimal amount;

    public FiveForAmountStrategy(Product product, BigDecimal amount) {
        super(product);
        this.amount = amount;
    }

    @Override
    public Discount computeForProduct(Product product, BigDecimal unitPrice, double quantity) {
        int quantityAsInt = (int) quantity;
        int packSize = 5;
        int numberOfPacks = quantityAsInt / packSize;
        if (quantityAsInt >= packSize) {
            BigDecimal full = unitPrice.multiply(BigDecimal.valueOf(quantity));
            BigDecimal packsTotal = amount.multiply(BigDecimal.valueOf(numberOfPacks));
            BigDecimal remainderTotal = unitPrice.multiply(BigDecimal.valueOf(quantityAsInt % packSize));
            BigDecimal discountTotal = full.subtract(packsTotal.add(remainderTotal));
            if (discountTotal.compareTo(BigDecimal.ZERO) > 0) {
                return new Discount(product, packSize + " for " + amount, discountTotal.negate());
            }
        }
        return null;
    }
}
