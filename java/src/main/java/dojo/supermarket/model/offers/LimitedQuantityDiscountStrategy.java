package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.Quantity;

import java.math.BigDecimal;
import java.util.Optional;

public class LimitedQuantityDiscountStrategy extends SingleProductOfferStrategy {
    private final int triggerQuantity;
    private final int limitQuantity;
    private final BigDecimal discountPercentage;

    public LimitedQuantityDiscountStrategy(Product product, int triggerQuantity, int limitQuantity,
            BigDecimal discountPercentage) {
        super(product);
        this.triggerQuantity = triggerQuantity;
        this.limitQuantity = limitQuantity;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Optional<Discount> computeForProduct(Product product, BigDecimal unitPrice, Quantity quantity) {
        if (!isDiscountTriggered(quantity)) {
            return Optional.empty();
        }

        Quantity discountableQuantity = computeDiscountableQuantity(quantity);
        BigDecimal discountAmount = computeDiscountAmount(unitPrice, discountableQuantity);

        return Optional.of(new Discount(product, discountableQuantity, buildDescription(), discountAmount));
    }

    private boolean isDiscountTriggered(Quantity quantity) {
        return quantity.getDouble() > triggerQuantity;
    }

    private Quantity computeDiscountableQuantity(Quantity quantity) {
        double discountable = Math.min(quantity.getDouble() - triggerQuantity, limitQuantity);
        return new Quantity(discountable);
    }

    private BigDecimal discountFraction() {
        return discountPercentage.movePointLeft(2);
    }

    private BigDecimal computeDiscountAmount(BigDecimal unitPrice, Quantity discountableQuantity) {
        return unitPrice
                .multiply(BigDecimal.valueOf(discountableQuantity.getDouble()))
                .multiply(discountFraction());
    }

    private String buildDescription() {
        return "Coupon discount";
    }
}
