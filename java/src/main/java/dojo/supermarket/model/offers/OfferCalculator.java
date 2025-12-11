package dojo.supermarket.model.offers;

import dojo.supermarket.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfferCalculator {

    private final List<OfferStrategy> offers;

    public OfferCalculator(List<OfferStrategy> offers) {
        this.offers = offers;
    }

    public List<Discount> applyOffers(SupermarketCatalog catalog, Map<Product, Double> productQuantities) {
        List<Discount> discounts = new ArrayList<>();

        for (var strategy : offers) {
            var partialDiscounts = strategy.compute(catalog, productQuantities);
            discounts.addAll(partialDiscounts);
        }
        return discounts;
    }
}
