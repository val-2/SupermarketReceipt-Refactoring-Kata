package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.SupermarketCatalog;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OfferStrategy {
    List<Discount> compute(SupermarketCatalog catalog, Map<Product, Double> productQuantities);
}
