package dojo.supermarket.model.offers;

import dojo.supermarket.model.Product;

public class ThreeForTwoStrategy extends NForMStrategy {

    public ThreeForTwoStrategy(Product product) {
        super(product, 3, 2);
    }
}
