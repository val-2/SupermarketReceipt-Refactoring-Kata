package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupermarketTest {

    @Test
    void tenPercentDiscount() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);
        Product apples = new Product("apples", ProductUnit.KILO);
        catalog.addProduct(apples, 1.99);

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 10.0);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(apples, 2.5);
        
        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        assertEquals(4.975, receipt.getTotalPrice(), 0.01);
        assertEquals(Collections.emptyList(), receipt.getDiscounts());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(apples, receiptItem.getProduct());
        assertEquals(1.99, receiptItem.getPrice());
        assertEquals(2.5*1.99, receiptItem.getTotalPrice());
        assertEquals(2.5, receiptItem.getQuantity());
    }

    @Test
    void threeForTwoDiscount() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);
        Product apples = new Product("apples", ProductUnit.KILO);
        catalog.addProduct(apples, 1.99);

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, toothbrush, 2);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 3);

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        assertEquals(1.98, receipt.getTotalPrice());
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(toothbrush , receiptItem.getProduct());
        assertEquals(0.99, receiptItem.getPrice());
        assertEquals(3*0.99, receiptItem.getTotalPrice());
        assertEquals(3, receiptItem.getQuantity());

    }

    @Test
    void TwoForAmountDiscount() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, toothbrush, 1);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 2);

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        assertEquals(1.00, receipt.getTotalPrice());
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(toothbrush , receiptItem.getProduct());
        assertEquals(0.99, receiptItem.getPrice());
        assertEquals(2*0.99, receiptItem.getTotalPrice());
        assertEquals(2, receiptItem.getQuantity());
    }


    @Test
    void FiveForAmountDiscount() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);
        Product apples = new Product("apples", ProductUnit.KILO);
        catalog.addProduct(apples, 1.99);

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, toothbrush, 2);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 7);

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        assertEquals(3.98, receipt.getTotalPrice());
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(1, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(toothbrush , receiptItem.getProduct());
        assertEquals(0.99, receiptItem.getPrice());
        assertEquals(7*0.99, receiptItem.getTotalPrice());
        assertEquals(7, receiptItem.getQuantity());
    }

    @Test
    void MixDiscounts() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        catalog.addProduct(toothbrush, 0.99);
        Product apples = new Product("apples", ProductUnit.KILO);
        catalog.addProduct(apples, 1.99);

        Teller teller = new Teller(catalog);
        teller.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, toothbrush, 1);
        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, apples, 2);
//        teller.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, apples, 3);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(toothbrush, 7);
        cart.addItemQuantity(apples, 5);

        // ACT
        Receipt receipt = teller.checksOutArticlesFrom(cart);

        // ASSERT
        assertEquals(8.8607, receipt.getTotalPrice(), 0.001);
        assertEquals(2, receipt.getDiscounts().size());
        assertEquals(2, receipt.getItems().size());
        ReceiptItem receiptItem = receipt.getItems().get(0);
        assertEquals(toothbrush , receiptItem.getProduct());
        assertEquals(0.99, receiptItem.getPrice());
        assertEquals(7*0.99, receiptItem.getTotalPrice());
        assertEquals(7, receiptItem.getQuantity());

        ReceiptItem secondReceiptItem = receipt.getItems().get(1);
        assertEquals(apples, secondReceiptItem.getProduct());
        assertEquals(1.99, secondReceiptItem.getPrice());
        assertEquals(5*1.99, secondReceiptItem.getTotalPrice());
        assertEquals(5, secondReceiptItem.getQuantity());

    }

}
