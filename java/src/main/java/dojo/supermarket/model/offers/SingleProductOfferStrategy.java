package dojo.supermarket.model.offers;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.ProductQuantities;
import dojo.supermarket.model.Quantity;
import dojo.supermarket.model.SupermarketCatalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SingleProductOfferStrategy implements OfferStrategy {
    protected final Product product;

    public SingleProductOfferStrategy(Product product) {
        this.product = product;
    }

    @Override
    public List<Discount> computeDiscounts(SupermarketCatalog catalog, ProductQuantities productQuantities) {
        List<Discount> discounts = new ArrayList<>();

        if (!productQuantities.contains(this.product)) {
            return discounts;
        }

        Quantity quantity = productQuantities.get(this.product);
        BigDecimal unitPrice = catalog.getUnitPrice(product);
        Optional<Discount> discount = computeForProduct(product, unitPrice, quantity);
        if (discount.isPresent()) {
            discounts.add(discount.get());
        }

        return discounts;
    }

    public abstract Optional<Discount> computeForProduct(Product product, BigDecimal unitPrice, Quantity quantity);
}
