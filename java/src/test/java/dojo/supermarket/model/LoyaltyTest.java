package dojo.supermarket.model;

import dojo.supermarket.model.loyalty.LoyaltyCard;
import dojo.supermarket.model.loyalty.LoyaltyProgram;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoyaltyTest {

    @Test
    void testEarnPoints() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product milk = new Product("Milk", ProductUnit.EACH);
        catalog.addProduct(milk, new BigDecimal("1.00"));

        Teller teller = new Teller(catalog);

        LoyaltyProgram program = new LoyaltyProgram(new BigDecimal("2.0"), new BigDecimal("0.10"));
        teller.setLoyaltyProgram(program);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(milk, new Quantity(10));

        LoyaltyCard card = new LoyaltyCard("User", 0);

        Receipt receipt = teller.checkout(cart, CheckoutOptions.builder()
                .loyaltyCard(card)
                .redeemPoints(false)
                .build());

        assertEquals(5, receipt.getEarnedPoints());
        assertEquals(5, card.getPoints());
    }

    @Test
    void testRedeemPoints() {
        SupermarketCatalog catalog = new FakeCatalog();
        Product milk = new Product("Milk", ProductUnit.EACH);
        catalog.addProduct(milk, new BigDecimal("1.00"));

        Teller teller = new Teller(catalog);
        LoyaltyProgram program = new LoyaltyProgram(new BigDecimal("2.0"), new BigDecimal("0.10"));
        teller.setLoyaltyProgram(program);

        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(milk, new Quantity(5));

        LoyaltyCard card = new LoyaltyCard("User", 20);

        Receipt receipt = teller.checkout(cart, CheckoutOptions.builder()
                .loyaltyCard(card)
                .redeemPoints(true)
                .build());

        assertEquals(3.00, receipt.getTotalPrice().doubleValue(), 0.001);
        assertEquals(1, receipt.getEarnedPoints());
        assertEquals(1, card.getPoints());

        boolean foundRedemption = receipt.getDiscounts().stream()
                .anyMatch(d -> d.getDescription().equals("Loyalty Redemption"));
        assertTrue(foundRedemption);
    }

}
