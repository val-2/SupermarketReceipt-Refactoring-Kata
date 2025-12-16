package dojo.supermarket.model;

import java.math.BigDecimal;

public interface SupermarketCatalog {

    void addProduct(Product product, BigDecimal price);

    BigDecimal getUnitPrice(Product product);

    BigDecimal getTotalPrice(Product product, Quantity quantity);
}
