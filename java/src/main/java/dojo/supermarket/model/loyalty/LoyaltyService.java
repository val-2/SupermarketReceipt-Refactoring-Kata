package dojo.supermarket.model.loyalty;

import dojo.supermarket.model.CheckoutOptions;
import dojo.supermarket.model.Discount;
import dojo.supermarket.model.ProductQuantities;
import dojo.supermarket.model.Receipt;

import java.math.BigDecimal;
import java.util.Optional;

public class LoyaltyService {

    private final LoyaltyProgram loyaltyProgram;

    public LoyaltyService(LoyaltyProgram loyaltyProgram) {
        this.loyaltyProgram = loyaltyProgram;
    }

    public void applyRedemptionIfApplicable(Receipt receipt, CheckoutOptions options) {
        Optional<LoyaltyCard> nullableLoyaltyCard = options.getLoyaltyCard();
        if (nullableLoyaltyCard.isEmpty() || !options.isRedeemPoints()) {
            return;
        }

        LoyaltyCard loyaltyCard = nullableLoyaltyCard.get();

        int pointsToUse = calculateRedeemablePoints(receipt, loyaltyCard);
        if (pointsToUse > 0) {
            applyRedemption(receipt, loyaltyCard, pointsToUse);
        }
    }

    private int calculateRedeemablePoints(Receipt receipt, LoyaltyCard loyaltyCard) {
        int pointsBalance = loyaltyCard.getPoints();
        BigDecimal total = receipt.getTotalPrice();

        int pointsNeeded = loyaltyProgram.calculatePointsNeededForAmount(total);
        return Math.min(pointsBalance, pointsNeeded);
    }

    private void applyRedemption(Receipt receipt, LoyaltyCard loyaltyCard, int pointsToUse) {
        BigDecimal discountAmount = loyaltyProgram.calculateMonetaryValueForPoints(pointsToUse);

        loyaltyCard.usePoints(pointsToUse);
        receipt.addDiscount(
                new Discount(ProductQuantities.empty(), "Loyalty Redemption", discountAmount));
    }

    public void applyEarning(Receipt receipt, CheckoutOptions options) {
        Optional<LoyaltyCard> nullableLoyaltyCard = options.getLoyaltyCard();
        if (nullableLoyaltyCard.isEmpty()) {
            return;
        }

        LoyaltyCard loyaltyCard = nullableLoyaltyCard.get();

        BigDecimal finalTotal = receipt.getTotalPrice();
        int earned = loyaltyProgram.calculatePointsEarned(finalTotal);
        receipt.addEarnedPoints(earned);
        loyaltyCard.addPoints(earned);
    }
}
