package dojo.supermarket.model.offers;

import dojo.supermarket.model.Product;
import java.math.BigDecimal;

public class TwoForAmountStrategy extends XForAmountStrategy {

    public TwoForAmountStrategy(Product product, BigDecimal amount) {
        super(product, 2, amount);
    }
}
