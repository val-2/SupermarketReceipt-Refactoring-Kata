package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import java.math.BigDecimal;

public class ThreeForTwoStrategy extends SingleProductOfferStrategy {

    public ThreeForTwoStrategy(Product product) {
        super(product);
    }

    @Override
    public Discount computeForProduct(Product product, BigDecimal unitPrice, double quantity) {
        int quantityAsInt = (int) quantity;
        int packSize = 3;
        int numberOfPacks = quantityAsInt / packSize;
        if (quantityAsInt >= packSize) {
            BigDecimal fullPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
            BigDecimal packsPrice = unitPrice.multiply(BigDecimal.valueOf(2 * numberOfPacks));
            BigDecimal remainderPrice = unitPrice.multiply(BigDecimal.valueOf(quantityAsInt % packSize));
            BigDecimal discountAmount = fullPrice.subtract(packsPrice.add(remainderPrice));
            if (discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                return new Discount(product, "3 for 2", discountAmount.negate());
            }
        }
        return null;
    }
}
