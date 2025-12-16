package dojo.supermarket.model;

import dojo.supermarket.model.offers.BundleStrategy;
import dojo.supermarket.model.offers.NotCumulativeOfferStrategy;
import dojo.supermarket.model.offers.OfferStrategy;
import dojo.supermarket.model.offers.TenPercentStrategy;
import dojo.supermarket.model.offers.ThreeForTwoStrategy;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotCumulativeOfferTest {

    @Test
    void testNotCumulativeDoesNotStack() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, new BigDecimal("0.99"));

        Teller teller = new Teller(catalog);

        teller.addOffer(new TenPercentStrategy(toothbrush));

        OfferStrategy threeForTwo = new ThreeForTwoStrategy(toothbrush);
        teller.addOffer(new NotCumulativeOfferStrategy(threeForTwo));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(3));

        Receipt receipt = teller.checkout(cart);

        assertEquals(1, receipt.getDiscounts().size());
        assertEquals("3 for 2", receipt.getDiscounts().get(0).getDescription());
    }

    @Test
    void testNotCumulativeIsApplyingIfFirst() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, new BigDecimal("0.99"));

        Teller teller = new Teller(catalog);

        OfferStrategy threeForTwo = new ThreeForTwoStrategy(toothbrush);
        teller.addOffer(new NotCumulativeOfferStrategy(threeForTwo));

        teller.addOffer(new TenPercentStrategy(toothbrush));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(3));

        Receipt receipt = teller.checkout(cart);

        assertEquals(1, receipt.getDiscounts().size());
    }

    @Test
    void testExclusiveBundleConsumesOnlyUsedQuantities() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, new BigDecimal("0.99"));

        Teller teller = new Teller(catalog);

        ProductQuantities bundle = new ProductQuantities();
        bundle.add(toothbrush, new Quantity(5.0));
        teller.addOffer(new NotCumulativeOfferStrategy(new BundleStrategy(bundle, BigDecimal.valueOf(10.0))));

        teller.addOffer(new TenPercentStrategy(toothbrush));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(7));

        Receipt receipt = teller.checkout(cart);

        assertEquals(2, receipt.getDiscounts().size());
        assertEquals(6.237, receipt.getTotalPrice().doubleValue(), 0.0001);
    }
}
