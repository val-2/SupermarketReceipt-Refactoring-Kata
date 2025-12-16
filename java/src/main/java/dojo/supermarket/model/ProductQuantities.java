package dojo.supermarket.model;

import java.util.*;

public class ProductQuantities implements Iterable<Map.Entry<Product, Quantity>> {

    private final Map<Product, Quantity> quantities;

    public ProductQuantities() {
        this.quantities = new LinkedHashMap<>();
    }

    public ProductQuantities(ProductQuantities other) {
        this.quantities = new LinkedHashMap<>(other.quantities);
    }

    public static ProductQuantities empty() {
        return new ProductQuantities();
    }

    public void add(Product product, Quantity amount) {
        Quantity current = quantities.getOrDefault(product, Quantity.ZERO);
        quantities.put(product, current.add(amount));
    }

    public Quantity get(Product product) {
        return quantities.getOrDefault(product, Quantity.ZERO);
    }

    public boolean contains(Product product) {
        Quantity qty = quantities.get(product);
        return qty != null && qty.isPositive();
    }

    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(quantities.keySet());
    }

    public boolean isEmpty() {
        return quantities.isEmpty();
    }

    public int size() {
        return quantities.size();
    }

    public ProductQuantities subtract(ProductQuantities toConsume) {
        ProductQuantities result = new ProductQuantities(this);
        for (Map.Entry<Product, Quantity> entry : toConsume) {
            Product product = entry.getKey();
            Quantity toSubtract = entry.getValue();
            Quantity current = result.quantities.getOrDefault(product, Quantity.ZERO);
            Quantity updated = current.subtract(toSubtract);
            if (updated.isZero()) {
                result.quantities.remove(product);
            } else {
                result.quantities.put(product, updated);
            }
        }
        return result;
    }

    @Override
    public Iterator<Map.Entry<Product, Quantity>> iterator() {
        return quantities.entrySet().iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductQuantities))
            return false;
        ProductQuantities that = (ProductQuantities) o;
        return Objects.equals(quantities, that.quantities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantities);
    }

    @Override
    public String toString() {
        return quantities.toString();
    }
}
