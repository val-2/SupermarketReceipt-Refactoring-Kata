package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.ProductQuantities;
import dojo.supermarket.model.SupermarketCatalog;

import java.util.List;

public interface OfferStrategy {
    List<Discount> computeDiscounts(SupermarketCatalog catalog, ProductQuantities productQuantities);

    default boolean isCumulative() {
        return true;
    }
}
