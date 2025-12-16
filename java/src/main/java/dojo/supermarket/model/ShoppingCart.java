package dojo.supermarket.model;

public class ShoppingCart {

    private final ProductQuantities productQuantities = new ProductQuantities();

    public void addItem(Product product) {
        addItemQuantity(product, new Quantity(1.0));
    }

    public ProductQuantities getProductQuantities() {
        return productQuantities;
    }

    public void addItemQuantity(Product product, Quantity quantity) {
        productQuantities.add(product, quantity);
    }

}
