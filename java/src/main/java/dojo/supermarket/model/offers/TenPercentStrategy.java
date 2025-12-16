package dojo.supermarket.model.offers;

import dojo.supermarket.model.Product;
import java.math.BigDecimal;

public class TenPercentStrategy extends PercentageDiscountStrategy {
    public TenPercentStrategy(Product product) {
        super(product, BigDecimal.valueOf(10));
    }
}
