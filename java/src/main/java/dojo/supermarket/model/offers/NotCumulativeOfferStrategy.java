package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.ProductQuantities;
import dojo.supermarket.model.SupermarketCatalog;

import java.util.List;

public class NotCumulativeOfferStrategy implements OfferStrategy {
    private final OfferStrategy wrapped;

    public NotCumulativeOfferStrategy(OfferStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public List<Discount> computeDiscounts(SupermarketCatalog catalog, ProductQuantities productQuantities) {
        return wrapped.computeDiscounts(catalog, productQuantities);
    }

    @Override
    public boolean isCumulative() {
        return false;
    }
}
