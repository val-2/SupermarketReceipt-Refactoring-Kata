package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.SupermarketCatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract public class SingleProductOfferStrategy implements OfferStrategy {
    Product product;

    public SingleProductOfferStrategy(Product product) {
        this.product = product;
    }

    @Override
    public List<Discount> compute(SupermarketCatalog catalog, Map<Product, Double> productQuantities) {
        List<Discount> discounts = new ArrayList<>();

        for (Map.Entry<Product, Double> entry : productQuantities.entrySet()) {
            Product product = entry.getKey();
            double quantity = entry.getValue();
            if (!this.product.equals(product)) {
                continue;
            }
            BigDecimal unitPrice = catalog.getUnitPrice(product);
            Discount discount = computeForProduct(product, unitPrice, quantity);
            if (discount != null) {
                discounts.add(discount);
            }
        }
        return discounts;
    }

    abstract public Discount computeForProduct(Product product, BigDecimal unitPrice, double quantity);
}
