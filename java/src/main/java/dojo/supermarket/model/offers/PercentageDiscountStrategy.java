package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.Quantity;

import java.math.BigDecimal;
import java.util.Optional;

public class PercentageDiscountStrategy extends SingleProductOfferStrategy {
    private final BigDecimal percent;

    public PercentageDiscountStrategy(Product product, BigDecimal percent) {
        super(product);
        this.percent = percent;
    }

    @Override
    public Optional<Discount> computeForProduct(Product product, BigDecimal unitPrice, Quantity quantity) {
        BigDecimal discountAmount = computeDiscountAmount(unitPrice, quantity);
        if (discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.empty();
        }
        return Optional.of(new Discount(product, quantity, buildDescription(), discountAmount));
    }

    private BigDecimal percentFraction() {
        return percent.movePointLeft(2);
    }

    private BigDecimal computeDiscountAmount(BigDecimal unitPrice, Quantity quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity.getDouble())).multiply(percentFraction());
    }

    private String buildDescription() {
        return percent + "% off";
    }
}
