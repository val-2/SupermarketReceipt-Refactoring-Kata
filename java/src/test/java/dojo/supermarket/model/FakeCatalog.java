package dojo.supermarket.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FakeCatalog implements SupermarketCatalog {
    private Map<String, Product> products = new HashMap<>();
    private Map<String, BigDecimal> prices = new HashMap<>();

    @Override
    public void addProduct(Product product, BigDecimal price) {
        this.products.put(product.getName(), product);
        this.prices.put(product.getName(), price);
    }

    @Override
    public BigDecimal getUnitPrice(Product p) {
        return this.prices.get(p.getName());
    }

    @Override
    public BigDecimal getTotalPrice(Product product, double quantity) {
        BigDecimal unitPrice = this.getUnitPrice(product);
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
