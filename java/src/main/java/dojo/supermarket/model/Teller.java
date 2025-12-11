package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import dojo.supermarket.model.offers.OfferCalculator;
import dojo.supermarket.model.offers.OfferStrategy;

public class Teller {

    private final SupermarketCatalog catalog;
    private final List<OfferStrategy> offers = new ArrayList<>();

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addOffer(OfferStrategy offer) {
        offers.add(offer);
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        Receipt receipt = new Receipt();
        List<ProductQuantity> productQuantities = theCart.getItems();
        for (ProductQuantity pq : productQuantities) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            BigDecimal unitPrice = catalog.getUnitPrice(p);
            receipt.addProduct(p, quantity, unitPrice);
        }
        OfferCalculator calculator = new OfferCalculator(offers);
        var discounts = calculator.applyOffers(catalog, theCart.productQuantities());
        for (Discount d : discounts) {
            receipt.addDiscount(d);
        }

        return receipt;
    }
}
