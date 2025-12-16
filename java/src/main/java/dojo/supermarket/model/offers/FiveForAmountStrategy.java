package dojo.supermarket.model.offers;

import dojo.supermarket.model.Product;
import java.math.BigDecimal;

public class FiveForAmountStrategy extends XForAmountStrategy {

    public FiveForAmountStrategy(Product product, BigDecimal amount) {
        super(product, 5, amount);
    }
}
