package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.ProductQuantities;
import dojo.supermarket.model.Quantity;
import dojo.supermarket.model.SupermarketCatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class BundleStrategy implements OfferStrategy {
    private final ProductQuantities productsRequired;
    private final BigDecimal discountPercent;

    public BundleStrategy(ProductQuantities productsRequired, BigDecimal discountPercent) {
        this.productsRequired = productsRequired;
        this.discountPercent = discountPercent;
    }

    private BigDecimal computeBundlePrice(SupermarketCatalog catalog) {
        BigDecimal total = BigDecimal.ZERO;

        for (var productEntry : productsRequired) {
            Product product = productEntry.getKey();
            Quantity requiredQuantity = productEntry.getValue();

            total = total.add(catalog.getTotalPrice(product, requiredQuantity));
        }

        return total;
    }

    private int calculateMaxBundles(ProductQuantities productQuantities) {
        int maxBundles = Integer.MAX_VALUE;
        for (var entry : productsRequired) {
            Quantity available = productQuantities.get(entry.getKey());
            int nBundles = available.floorDivide(entry.getValue());
            maxBundles = Math.min(maxBundles, nBundles);
        }
        return maxBundles;
    }

    private boolean hasValidBundle(int maxBundles) {
        return maxBundles > 0 && maxBundles != Integer.MAX_VALUE;
    }

    private BigDecimal discountPercentFraction() {
        return discountPercent.movePointLeft(2);
    }

    private BigDecimal computeDiscountAmount(BigDecimal bundlePrice, int maxBundles) {
        return bundlePrice.multiply(discountPercentFraction()).multiply(BigDecimal.valueOf(maxBundles));
    }

    private String buildBundleDescription() {
        StringJoiner joiner = new StringJoiner("+", "Bundle Offer: ", "");
        for (var entry : productsRequired) {
            joiner.add(entry.getKey().getName());
        }
        return joiner.toString();
    }

    private ProductQuantities computeConsumedQuantities(int maxBundles) {
        ProductQuantities consumedQuantities = new ProductQuantities();
        for (var entry : productsRequired) {
            consumedQuantities.add(entry.getKey(), entry.getValue().multiply(maxBundles));
        }
        return consumedQuantities;
    }

    @Override
    public List<Discount> computeDiscounts(SupermarketCatalog catalog, ProductQuantities productQuantities) {
        int maxBundles = calculateMaxBundles(productQuantities);

        List<Discount> discounts = new ArrayList<>();
        if (!hasValidBundle(maxBundles)) {
            return discounts;
        }

        BigDecimal bundlePrice = computeBundlePrice(catalog);
        BigDecimal totalDiscount = computeDiscountAmount(bundlePrice, maxBundles);
        String description = buildBundleDescription();
        ProductQuantities consumedQuantities = computeConsumedQuantities(maxBundles);

        discounts.add(new Discount(consumedQuantities, description, totalDiscount));
        return discounts;
    }

}
