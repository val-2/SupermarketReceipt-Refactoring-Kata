package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceiptPrinterTest {
    @Test
    void printsSingleItem() {
        Receipt receipt = new Receipt();
        Product apples = new Product("apples", ProductUnit.KILO);
        receipt.addProduct(apples, new Quantity(2.5), new BigDecimal("1.99"));

        String output = new ReceiptPrinter().printReceipt(receipt);

        assertTrue(output.contains("apples"));
        assertTrue(output.contains("1.99 * 2.500"));
        assertTrue(output.contains("Total:"));
    }

    @Test
    void printsDiscount() {
        Receipt receipt = new Receipt();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        receipt.addProduct(toothbrush, new Quantity(3.0), new BigDecimal("0.99"));
        receipt.addDiscount(new Discount(toothbrush, new Quantity(3.0), "3 for 2", new BigDecimal("0.99")));

        String output = new ReceiptPrinter().printReceipt(receipt);

        assertTrue(output.contains("3 for 2"));
        assertTrue(output.contains("-0.99"));
    }

    @Test
    void printsBundleDiscount() {
        Receipt receipt = new Receipt();
        Product toothbrush = new Product("toothbrush", ProductUnit.EACH);
        Product toothpaste = new Product("toothpaste", ProductUnit.EACH);
        receipt.addProduct(toothbrush, new Quantity(1.0), new BigDecimal("0.99"));
        receipt.addProduct(toothpaste, new Quantity(1.0), new BigDecimal("1.79"));

        ProductQuantities bundleQuantities = new ProductQuantities();
        bundleQuantities.add(toothbrush, new Quantity(1.0));
        bundleQuantities.add(toothpaste, new Quantity(1.0));
        receipt.addDiscount(new Discount(bundleQuantities, "Bundle Offer", new BigDecimal("0.28")));

        String output = new ReceiptPrinter().printReceipt(receipt);

        assertTrue(output.contains("Bundle Offer"));
        assertTrue(output.contains("toothbrush"));
        assertTrue(output.contains("toothpaste"));
    }

    @Test
    void printsLoyaltyPoints() {
        Receipt receipt = new Receipt();
        Product apples = new Product("apples", ProductUnit.KILO);
        receipt.addProduct(apples, new Quantity(5.0), new BigDecimal("1.99"));
        receipt.addEarnedPoints(50);

        String output = new ReceiptPrinter().printReceipt(receipt);

        assertTrue(output.contains("Loyalty Points Earned: 50"));
    }
}
