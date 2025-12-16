package dojo.supermarket.model;

import dojo.supermarket.model.loyalty.LoyaltyCard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CheckoutOptions {

    private final List<Coupon> coupons;
    private final LoyaltyCard loyaltyCard;
    private final boolean redeemPoints;
    private final LocalDate date;

    private CheckoutOptions(Builder b) {
        this.coupons = List.copyOf(b.coupons);
        this.loyaltyCard = b.loyaltyCard;
        this.redeemPoints = b.redeemPoints;
        if (b.date == null) {
            this.date = LocalDate.now();
        } else {
            this.date = b.date;
        }
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public Optional<LoyaltyCard> getLoyaltyCard() {
        return Optional.ofNullable(loyaltyCard);
    }

    public boolean isRedeemPoints() {
        return redeemPoints;
    }

    public LocalDate getDate() {
        return date;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<Coupon> coupons = new ArrayList<>();
        private LoyaltyCard loyaltyCard;
        private boolean redeemPoints = false;
        private LocalDate date;

        public Builder coupons(List<Coupon> coupons) {
            this.coupons = new ArrayList<>(coupons);
            return this;
        }

        public Builder loyaltyCard(LoyaltyCard loyaltyCard) {
            this.loyaltyCard = loyaltyCard;
            return this;
        }

        public Builder redeemPoints(boolean redeemPoints) {
            this.redeemPoints = redeemPoints;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public CheckoutOptions build() {
            return new CheckoutOptions(this);
        }
    }
}
