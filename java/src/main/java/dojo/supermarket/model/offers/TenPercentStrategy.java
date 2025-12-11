package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import java.math.BigDecimal;

public class TenPercentStrategy extends SingleProductOfferStrategy {
    private final BigDecimal percent;

    public TenPercentStrategy(Product product, BigDecimal percent) {
        super(product);
        this.percent = percent;
    }

    @Override
    public Discount computeForProduct(Product product, BigDecimal unitPrice, double quantity) {
        BigDecimal ratio = percent.divide(BigDecimal.valueOf(100));
        BigDecimal discountAmount = unitPrice.multiply(BigDecimal.valueOf(quantity)).multiply(ratio);
        if (discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return new Discount(product, percent + "% off", discountAmount.negate());
    }
}
