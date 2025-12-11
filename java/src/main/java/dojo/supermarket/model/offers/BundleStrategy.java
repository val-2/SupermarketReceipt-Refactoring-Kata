package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.SupermarketCatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BundleStrategy implements OfferStrategy {
    Map<Product, Double> productsRequired;
    double discount;

    public BundleStrategy(Map<Product, Double> productsRequired, double discount) {
        this.productsRequired = productsRequired;
        this.discount = discount;
    }

    public BundleStrategy(Map<Product, Double> productsRequired) {
        this.productsRequired = productsRequired;
        this.discount = 10.0;
    }

    BigDecimal getBundleTotalPrice(SupermarketCatalog catalog) {
        BigDecimal total = BigDecimal.ZERO;

        for (var productEntry : this.productsRequired.entrySet()) {
            Product product = productEntry.getKey();
            Double requiredQuantity = productEntry.getValue();

            total = total.add(catalog.getTotalPrice(product, requiredQuantity));
        }

        return total;
    }

    @Override
    public List<Discount> compute(SupermarketCatalog catalog, Map<Product, Double> productQuantities) {
        List<Discount> discounts = new ArrayList<>();

        int maxBundles = Integer.MAX_VALUE;

        for (var productQuantityRequired : this.productsRequired.entrySet()) {
            Product requiredProduct = productQuantityRequired.getKey();
            Double requiredQuantity = productQuantityRequired.getValue();

            Double productQuantity = productQuantities.getOrDefault(requiredProduct, (double) 0);

            var nBundles = (int) Math.floor(productQuantity / requiredQuantity);

            if (nBundles < maxBundles) {
                maxBundles = nBundles;
            }
        }

        if (maxBundles > 0 && maxBundles != Integer.MAX_VALUE) {
            double totalDiscountPercentage = this.discount * maxBundles;
            BigDecimal totalPrice = getBundleTotalPrice(catalog);
            BigDecimal totalDiscount = totalPrice.multiply(BigDecimal.valueOf(totalDiscountPercentage)).negate();
            discounts.add(new Discount("Bundle Offer", totalDiscount));
            return discounts;
        }

        return null;
    }

}
