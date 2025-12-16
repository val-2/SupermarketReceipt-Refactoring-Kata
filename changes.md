# SupermarketReceipt-Refactoring-Kata — Changes

## Code smells
- Long method: ShoppingCart.handleOffers
- Feature envy: Cart fetching prices and using Offer internals.
- Primitive obsession: Offer.argument used for different meanings across types
- Magic numbers: for Offer.argument 2/3/5.
- In general duplicated or muddy logic: es. repeated discount formulations.

## Refactoring
- Added tests in SupermarketTest for all offer types.
- Introduced OfferStrategy interface to decouple discount calculations from ShoppingCart
- Centralized offer selection via OfferCalculator, removing switch statements, Offer, SpecialOfferType and discount application logic from ShoppingCart.
- Moved product(s) of application of an offer into the strategy instance itself and not in a Map in Teller.
  - Eventual other arguments required by the strategy (es. previous Offer.argument) are passed via constructor.
  - Now discount application logic is computed inside OfferStrategy.computeDiscounts.
  - Since we may have more than one discount applied per offer, OfferStrategy.computeDiscounts returns List<Discount>.
- Since all previous offers only applied to a single product, created SingleProductOfferStrategy abstract class to reduce duplication.
  - Created concrete offer strategy classes corresponding to previous offer types, generalizing where possible in offers/ and then implementing es. TenPercentStrategy, ThreeForTwoStrategy, TwoForAmountStrategy, FiveForAmountStrategy purely to keep previous "API" for tests.
  - Made computeForProduct return Optional<Discount> instead of Discount to handle cases where no discount can be applied.
- Changed type for monetary values and discounts to BigDecimal to avoid floating point precision issues.
  - Changed equals implementations accordingly.
- Receipt.addProduct computes totalPrice internally (in ReceiptItem constructor); it is now only required to pass only unit price and quantity
- Refactored ShoppingCart attributes to keep track of product quantities with a Map (via ProductQuantities) instead of List<ProductQuantity>, to simplify quantity lookups.
  - Insertion order is preserved via LinkedHashMap (in ProductQuantities).

## Bundles feature
- Implemented new discounted bundles feature.
  - Simply by implementing OfferStrategy interface and doing the necessary calculations internally.
  - Refactored Discount class to use a map of product to quantities instead of a single optional Product, since now a discount may apply to multiple products in different quantities.
  - Refactored ReceiptPrinter to print multi-product discounts correctly. 

## Coupons feature
- Implemented Coupon-based discounts with validity periods and quantity limits.
  - Created Coupon class to encapsulate date range, and offer strategy (that includes the products of application).
  - Created LimitedQuantityDiscountStrategy for "Buy X get next Y at Z% off" logic as described.
    - It can be used on its own or with a coupon.
- Modified Teller to accept coupons at checkout time and apply them if valid.

## Loyalty program feature
- Loyalty program
  - Implemented point accumulation based on spending and point redemption for discounts.
  - Integrated into Teller checkout process, correctly calculating points on the actually paid total after other discounts.
    - Logic is implemented in LoyaltyService and called from Teller.
  - Added LoyaltyCard to track customer balance and LoyaltyProgram for rules configuration.
- Refactored Teller to accept CheckoutOptions that encapsulate coupons and loyalty cards.
  - CheckoutOptions uses builder pattern for easier passing of optional parameters.
  - Exposed LoyaltyCard as Optional to explicitly model null values.
  - If not specified, date is set to current date at instance creation time (and not at builder creation).
    - It was decided to default to the current date to simplify the API for the common case while still allowing overrides for testing or historical checks.
  - Otherwise it would be necessary to have a lot of overloads for checkout, each that include/exclude coupons and loyalty cards.

## Non-cumulative offers
- Implemented NotCumulativeOfferStrategy decorator to mark offers as non-cumulative.
   - Refactored OfferCalculator to implement a priority-based resolution: Exclusive offers are processed first and consume the products quantities they affect, preventing any subsequent offers (cumulative or not) from applying to those same items. Cumulative offers stack on any remaining products.
   - Assumptions:
      - Order of registration of offers gives them priority
      - Non-cumulative offers always take precedence over cumulative ones.
   - Implemented record ExclusiveOfferResult as dataclass to pass remaining product quantities and discounts together.

## Other changes
- To remove ambiguity, Discount amounts are always non-negative; only final calculation in Receipt.getTotalPrice subtracts discounts from the total.
  - ReceiptPrinter displays discounts as negative values for clarity.
- Adjusted tests to new behaviors without changing their original values.
- Addressed a possible Primitive Obsession code smell by replacing quantity modeled as Double with proper class Quantity, applying Replace Data Value with Object.
  - Runtime check: it cannot be negative.
- Modeled product quantities with class ProductQuantities.
- Created tests for all newly implemented features.
- Use of @BeforeEach to initialize common objects in tests, es. catalog for SupermarketTest.
- Renamed Teller.checksOutArticlesFrom to checkout for clarity
