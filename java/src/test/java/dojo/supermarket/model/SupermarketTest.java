package dojo.supermarket.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dojo.supermarket.model.offers.*;

class SupermarketTest {

    private SupermarketCatalog catalog;
    private Product toothbrush;
    private Product toothpaste;
    private Product apples;

    @BeforeEach
    void setUp() {
        catalog = new FakeCatalog();
        toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, new BigDecimal("0.99"));
        toothpaste = new Product("toothpaste", ProductUnit.EACH);
        catalog.addProduct(toothpaste, new BigDecimal("1.79"));
        apples = new Product("apples", ProductUnit.KILO);
        catalog.addProduct(apples, new BigDecimal("1.99"));
    }

    @Test
    void tenPercentDiscount() {
        Teller teller = new Teller(catalog);
        teller.addOffer(new TenPercentStrategy(toothbrush));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(apples, new Quantity(2.5));

        Receipt receipt = teller.checkout(cart);

        assertEquals(4.975, receipt.getTotalPrice().doubleValue(), 0.01);
        assertEquals(Collections.emptyList(), receipt.getDiscounts());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(apples, receiptItem.getProduct());
        assertEquals(1.99, receiptItem.getPrice().doubleValue());
        assertEquals(2.5 * 1.99, receiptItem.getTotalPrice().doubleValue());
        assertEquals(new Quantity(2.5), receiptItem.getQuantity());
    }

    @Test
    void bundleSingleBundle() {
        Teller teller = new Teller(catalog);
        ProductQuantities bundle = new ProductQuantities();
        bundle.add(toothbrush, new Quantity(1.0));
        bundle.add(toothpaste, new Quantity(1.0));
        teller.addOffer(new BundleStrategy(bundle, BigDecimal.valueOf(10.0)));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(1));
        cart.addItemQuantity(toothpaste, new Quantity(1));

        Receipt receipt = teller.checkout(cart);

        double bundleTotal = 0.99 + 1.79;
        double expectedDiscount = bundleTotal * 0.10;
        double expectedTotal = bundleTotal - expectedDiscount;

        assertEquals(expectedTotal, receipt.getTotalPrice().doubleValue(), 0.001);
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(expectedDiscount, receipt.getDiscounts().get(0).getDiscountAmount().doubleValue(), 0.001);
    }

    @Test
    void bundleMultipleBundles() {
        Teller teller = new Teller(catalog);
        ProductQuantities bundle = new ProductQuantities();
        bundle.add(toothbrush, new Quantity(1.0));
        bundle.add(toothpaste, new Quantity(1.0));
        teller.addOffer(new BundleStrategy(bundle, BigDecimal.valueOf(10.0)));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(2));
        cart.addItemQuantity(toothpaste, new Quantity(2));

        Receipt receipt = teller.checkout(cart);

        double bundleTotal = 0.99 + 1.79;
        double expectedDiscount = bundleTotal * 0.10 * 2;
        double expectedTotal = (bundleTotal * 2) - expectedDiscount;

        assertEquals(expectedTotal, receipt.getTotalPrice().doubleValue(), 0.001);
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(expectedDiscount, receipt.getDiscounts().get(0).getDiscountAmount().doubleValue(), 0.001);
    }

    @Test
    void bundleIncompleteAppliesOnce() {
        Teller teller = new Teller(catalog);
        ProductQuantities bundle = new ProductQuantities();
        bundle.add(toothbrush, new Quantity(1.0));
        bundle.add(toothpaste, new Quantity(1.0));
        teller.addOffer(new BundleStrategy(bundle, BigDecimal.valueOf(10.0)));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(2));
        cart.addItemQuantity(toothpaste, new Quantity(1));

        Receipt receipt = teller.checkout(cart);

        double bundleTotal = 0.99 + 1.79;
        double expectedDiscount = bundleTotal * 0.10;
        double expectedTotal = (2 * 0.99 + 1.79) - expectedDiscount;

        assertEquals(expectedTotal, receipt.getTotalPrice().doubleValue(), 0.001);
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(expectedDiscount, receipt.getDiscounts().get(0).getDiscountAmount().doubleValue(), 0.001);
    }

    @Test
    void threeForTwoDiscount() {
        Teller teller = new Teller(catalog);
        teller.addOffer(new ThreeForTwoStrategy(toothbrush));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(3));

        Receipt receipt = teller.checkout(cart);

        assertEquals(1.98, receipt.getTotalPrice().doubleValue());
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(toothbrush, receiptItem.getProduct());
        assertEquals(0.99, receiptItem.getPrice().doubleValue());
        assertEquals(3 * 0.99, receiptItem.getTotalPrice().doubleValue(), 0.0001);
        assertEquals(new Quantity(3), receiptItem.getQuantity());
    }

    @Test
    void twoForAmountDiscount() {
        Teller teller = new Teller(catalog);
        teller.addOffer(new TwoForAmountStrategy(toothbrush, new BigDecimal("1")));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(2));

        Receipt receipt = teller.checkout(cart);

        assertEquals(1.00, receipt.getTotalPrice().doubleValue());
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(toothbrush, receiptItem.getProduct());
        assertEquals(0.99, receiptItem.getPrice().doubleValue());
        assertEquals(2 * 0.99, receiptItem.getTotalPrice().doubleValue());
        assertEquals(new Quantity(2), receiptItem.getQuantity());
    }

    @Test
    void fiveForAmountDiscount() {
        Teller teller = new Teller(catalog);
        teller.addOffer(new FiveForAmountStrategy(toothbrush, new BigDecimal("2")));

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, new Quantity(7));

        Receipt receipt = teller.checkout(cart);

        assertEquals(3.98, receipt.getTotalPrice().doubleValue());
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(toothbrush, receiptItem.getProduct());
        assertEquals(0.99, receiptItem.getPrice().doubleValue());
        assertEquals(7 * 0.99, receiptItem.getTotalPrice().doubleValue());
        assertEquals(new Quantity(7), receiptItem.getQuantity());
    }
}
