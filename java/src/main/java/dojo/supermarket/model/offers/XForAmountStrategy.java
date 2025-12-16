package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.Quantity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class XForAmountStrategy extends SingleProductOfferStrategy {
    private final int packSize;
    private final BigDecimal amount;

    public XForAmountStrategy(Product product, int packSize, BigDecimal amount) {
        super(product);
        if (packSize <= 0) {
            throw new IllegalArgumentException("packSize must be > 0");
        }
        this.packSize = packSize;
        this.amount = Objects.requireNonNull(amount, "amount");
    }

    @Override
    public Optional<Discount> computeForProduct(Product product, BigDecimal unitPrice, Quantity quantity) {
        int numberOfPacks = quantity.floorDivide(packSize);
        if (numberOfPacks <= 0) {
            return Optional.empty();
        }

        BigDecimal discountAmount = computeDiscountAmount(unitPrice, quantity, numberOfPacks);
        if (discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.empty();
        }

        Quantity discountedQuantity = computeDiscountedQuantity(numberOfPacks);
        return Optional.of(new Discount(product, discountedQuantity, buildDescription(), discountAmount));
    }

    private BigDecimal computeDiscountAmount(BigDecimal unitPrice, Quantity quantity, int numberOfPacks) {
        BigDecimal packTotal = amount.multiply(BigDecimal.valueOf(numberOfPacks));
        double remainderQuantity = quantity.getDouble() - (packSize * numberOfPacks);
        BigDecimal remainderTotal = unitPrice.multiply(BigDecimal.valueOf(remainderQuantity));
        BigDecimal discountedTotal = packTotal.add(remainderTotal);
        BigDecimal fullTotal = unitPrice.multiply(BigDecimal.valueOf(quantity.getDouble()));
        return fullTotal.subtract(discountedTotal);
    }

    private Quantity computeDiscountedQuantity(int numberOfPacks) {
        return new Quantity(packSize * numberOfPacks);
    }

    private String buildDescription() {
        return packSize + " for " + amount;
    }
}
