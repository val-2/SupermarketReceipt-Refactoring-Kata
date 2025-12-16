package dojo.supermarket.model;

import dojo.supermarket.model.offers.OfferStrategy;
import java.time.LocalDate;

public class Coupon {
    private final LocalDate validFrom;
    private final LocalDate validTo;
    private final OfferStrategy offerStrategy;

    public Coupon(LocalDate validFrom, LocalDate validTo, OfferStrategy offerStrategy) {
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.offerStrategy = offerStrategy;
    }

    public boolean isValid(LocalDate date) {
        return (date.isEqual(validFrom) || date.isAfter(validFrom)) &&
                (date.isEqual(validTo) || date.isBefore(validTo));
    }

    public OfferStrategy getOfferStrategy() {
        return offerStrategy;
    }
}
