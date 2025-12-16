package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.Quantity;

import java.math.BigDecimal;
import java.util.Optional;

public class NForMStrategy extends SingleProductOfferStrategy {
    private final int n;
    private final int m;

    public NForMStrategy(Product product, int n, int m) {
        super(product);
        this.n = n;
        this.m = m;
    }

    @Override
    public Optional<Discount> computeForProduct(Product product, BigDecimal unitPrice, Quantity quantity) {
        int bundles = quantity.floorDivide(n);
        if (bundles <= 0) {
            return Optional.empty();
        }

        BigDecimal discountAmount = computeDiscountAmount(unitPrice, bundles);
        if (discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.empty();
        }

        Quantity discountedQuantity = computeDiscountedQuantity(bundles);
        return Optional.of(new Discount(product, discountedQuantity, buildDescription(), discountAmount));
    }

    private BigDecimal computeDiscountAmount(BigDecimal unitPrice, int bundles) {
        int freePerBundle = n - m;
        return unitPrice.multiply(BigDecimal.valueOf((long) freePerBundle * bundles));
    }

    private Quantity computeDiscountedQuantity(int bundles) {
        return new Quantity(n * bundles);
    }

    private String buildDescription() {
        return n + " for " + m;
    }
}
