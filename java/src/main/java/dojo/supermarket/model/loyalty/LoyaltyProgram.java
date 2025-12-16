package dojo.supermarket.model.loyalty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoyaltyProgram {
    private final BigDecimal amountPerPoint;
    private final BigDecimal monetaryValuePerPoint;

    public LoyaltyProgram(BigDecimal amountPerPoint, BigDecimal monetaryValuePerPoint) {
        this.amountPerPoint = amountPerPoint;
        this.monetaryValuePerPoint = monetaryValuePerPoint;
    }

    public int calculatePointsEarned(BigDecimal purchaseAmount) {
        return purchaseAmount.divide(amountPerPoint, 0, RoundingMode.DOWN).intValue();
    }

    public BigDecimal calculateMonetaryValueForPoints(int points) {
        return monetaryValuePerPoint.multiply(BigDecimal.valueOf(points));
    }

    public int calculatePointsNeededForAmount(BigDecimal amount) {
        return amount.divide(monetaryValuePerPoint, 0, RoundingMode.CEILING).intValue();
    }
}
