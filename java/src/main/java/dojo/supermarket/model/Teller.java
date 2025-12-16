package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import dojo.supermarket.model.loyalty.LoyaltyProgram;
import dojo.supermarket.model.loyalty.LoyaltyService;
import dojo.supermarket.model.offers.OfferCalculator;
import dojo.supermarket.model.offers.OfferStrategy;

public class Teller {

    private final SupermarketCatalog catalog;
    private final List<OfferStrategy> offers = new ArrayList<>();
    private LoyaltyService loyaltyService;

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void setLoyaltyProgram(LoyaltyProgram loyaltyProgram) {
        this.loyaltyService = new LoyaltyService(loyaltyProgram);
    }

    public void addOffer(OfferStrategy offer) {
        offers.add(offer);
    }

    public Receipt checkout(ShoppingCart cart) {
        return checkout(cart, CheckoutOptions.builder().build());
    }

    public Receipt checkout(ShoppingCart cart, CheckoutOptions options) {
        Receipt receipt = buildReceiptFromCart(cart);

        List<OfferStrategy> allOffers = collectOffers(options);
        applyOffers(receipt, cart, allOffers);

        if (loyaltyService != null) {
            loyaltyService.applyRedemptionIfApplicable(receipt, options);
            loyaltyService.applyEarning(receipt, options);
        }

        return receipt;
    }

    private Receipt buildReceiptFromCart(ShoppingCart cart) {
        Receipt receipt = new Receipt();
        ProductQuantities productQuantities = cart.getProductQuantities();
        for (Map.Entry<Product, Quantity> entry : productQuantities) {
            Product p = entry.getKey();
            Quantity quantity = entry.getValue();
            BigDecimal unitPrice = catalog.getUnitPrice(p);
            receipt.addProduct(p, quantity, unitPrice);
        }
        return receipt;
    }

    private List<OfferStrategy> collectOffers(CheckoutOptions options) {
        List<OfferStrategy> allOffers = new ArrayList<>(this.offers);
        for (Coupon coupon : options.getCoupons()) {
            if (coupon.isValid(options.getDate())) {
                allOffers.add(coupon.getOfferStrategy());
            }
        }
        return allOffers;
    }

    private void applyOffers(Receipt receipt, ShoppingCart cart, List<OfferStrategy> offersList) {
        OfferCalculator calculator = new OfferCalculator(offersList);
        List<Discount> discounts = calculator.applyOffers(catalog, cart.getProductQuantities());
        for (Discount d : discounts) {
            receipt.addDiscount(d);
        }
    }

}
