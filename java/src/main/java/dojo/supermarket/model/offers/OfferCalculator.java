package dojo.supermarket.model.offers;

import dojo.supermarket.model.*;

import java.util.*;

public class OfferCalculator {

    private final List<OfferStrategy> offers;

    public OfferCalculator(List<OfferStrategy> offers) {
        this.offers = offers;
    }

    private record ExclusiveOfferResult(List<Discount> discounts, ProductQuantities remainingQuantities) {
    }

    public List<Discount> applyOffers(SupermarketCatalog catalog, ProductQuantities initialQuantities) {
        List<Discount> finalDiscounts = new ArrayList<>();

        ExclusiveOfferResult exclusiveResult = applyExclusiveStrategies(catalog, initialQuantities);
        finalDiscounts.addAll(exclusiveResult.discounts());
        ProductQuantities remainingQuantities = exclusiveResult.remainingQuantities();

        List<Discount> standardDiscounts = applyStandardStrategies(catalog, remainingQuantities);
        finalDiscounts.addAll(standardDiscounts);

        return finalDiscounts;
    }

    private ExclusiveOfferResult applyExclusiveStrategies(SupermarketCatalog catalog, ProductQuantities quantities) {
        ProductQuantities currentRemaining = new ProductQuantities(quantities);
        List<Discount> collected = new ArrayList<>();

        for (OfferStrategy strategy : offers) {
            if (!strategy.isCumulative()) {
                List<Discount> discounts = strategy.computeDiscounts(catalog, currentRemaining);
                for (Discount d : discounts) {
                    collected.add(d);
                    currentRemaining = currentRemaining.subtract(d.getProductQuantities());
                }
            }
        }

        return new ExclusiveOfferResult(collected, currentRemaining);
    }

    private List<Discount> applyStandardStrategies(SupermarketCatalog catalog, ProductQuantities remainingQuantities) {
        List<Discount> discounts = new ArrayList<>();
        for (OfferStrategy strategy : offers) {
            if (strategy.isCumulative()) {
                discounts.addAll(strategy.computeDiscounts(catalog, remainingQuantities));
            }
        }
        return discounts;
    }
}
