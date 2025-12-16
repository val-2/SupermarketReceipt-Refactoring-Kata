package dojo.supermarket.model;

import dojo.supermarket.model.offers.LimitedQuantityDiscountStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CouponTest {

    private SupermarketCatalog catalog;
    private Teller teller;
    private Product orangeJuice;
    private Coupon coupon;

    private static final LocalDate VALID_FROM = LocalDate.of(2023, 11, 13);
    private static final LocalDate VALID_TO = LocalDate.of(2023, 11, 15);
    private static final LocalDate DATE_WITHIN_RANGE = LocalDate.of(2023, 11, 14);
    private static final LocalDate DATE_AFTER_EXPIRY = LocalDate.of(2023, 11, 16);

    @BeforeEach
    void setUp() {
        catalog = new FakeCatalog();
        orangeJuice = new Product("Orange Juice", ProductUnit.EACH);
        catalog.addProduct(orangeJuice, new BigDecimal("2.00"));

        teller = new Teller(catalog);

        coupon = new Coupon(
                VALID_FROM,
                VALID_TO,
                new LimitedQuantityDiscountStrategy(orangeJuice, 6, 6, BigDecimal.valueOf(50.0)));
    }

    @Test
    void couponValidityAndApplication() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(orangeJuice, new Quantity(12));

        Receipt receipt = teller.checkout(cart, CheckoutOptions.builder()
                .coupons(Collections.singletonList(coupon))
                .date(DATE_WITHIN_RANGE)
                .build());

        assertEquals(18.00, receipt.getTotalPrice().doubleValue(), 0.001);
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(6.00, receipt.getDiscounts().get(0).getDiscountAmount().doubleValue(), 0.001);
    }

    @Test
    void couponExpired() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(orangeJuice, new Quantity(12));

        Receipt receipt = teller.checkout(cart, CheckoutOptions.builder()
                .coupons(Collections.singletonList(coupon))
                .date(DATE_AFTER_EXPIRY)
                .build());

        assertEquals(24.00, receipt.getTotalPrice().doubleValue(), 0.001);
        assertEquals(0, receipt.getDiscounts().size());
    }

    @Test
    void couponInsufficientQuantity() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(orangeJuice, new Quantity(4));

        Receipt receipt = teller.checkout(cart, CheckoutOptions.builder()
                .coupons(Collections.singletonList(coupon))
                .date(DATE_WITHIN_RANGE)
                .build());

        assertEquals(8.00, receipt.getTotalPrice().doubleValue(), 0.001);
        assertEquals(0, receipt.getDiscounts().size());
    }

    @Test
    void couponPartialQuantity() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItemQuantity(orangeJuice, new Quantity(8));

        Receipt receipt = teller.checkout(cart, CheckoutOptions.builder()
                .coupons(Collections.singletonList(coupon))
                .date(DATE_WITHIN_RANGE)
                .build());

        assertEquals(14.00, receipt.getTotalPrice().doubleValue(), 0.001);
        assertEquals(1, receipt.getDiscounts().size());
        assertEquals(2.00, receipt.getDiscounts().get(0).getDiscountAmount().doubleValue(), 0.001);
    }
}
