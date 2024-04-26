package retail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class OrderBuilder {
  private static final int ORDER_CATEGORY_BOUNDARY = 3;
  private List<Product> items = new ArrayList<>();
  private CreditCardDetails creditCardDetails;
  private Address billingAddress;
  private Address shippingAddress;
  private Courier courier;
  private CreditCardProcessors ccp;
  private BigDecimal discount = BigDecimal.ZERO;
  private boolean giftWrap = false;

  public OrderBuilder withItems(List<Product> items) {
    this.items.addAll(items);
    return this;
  }

  public OrderBuilder addOneItem(Product item) {
    items.add(item);
    return this;
  }

  public OrderBuilder withCreditCardDetails(CreditCardDetails creditCardDetails) {
    this.creditCardDetails = creditCardDetails;
    return this;
  }

  public OrderBuilder withBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
    return this;
  }

  public OrderBuilder withShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
    return this;
  }

  public OrderBuilder withCourier(Courier courier) {
    this.courier = courier;
    return this;
  }

  public OrderBuilder withDiscount(BigDecimal discount) {
    this.discount = discount;
    return this;
  }

  public OrderBuilder withGiftWrap(boolean giftWrap) {
    this.giftWrap = giftWrap;
    return this;
  }
  public OrderBuilder withCreditCardProcessor(CreditCardProcessors ccp) {
    this.ccp = ccp;
    return this;
  }

  /*
    NOTE: Check for whether gift wrap/discount has been deferred to build method, to make code
    more extensible. for example, future implementations may have a remove order feature which
    should allow for discounts to be added and then invalidated later when items are removed, or
    gift wrap to be added and then to add more items later.
   */
  public Order buildOrder() {

    if(items.isEmpty() || creditCardDetails == null || billingAddress == null
        || courier == null || ccp == null) {
      throw new UnsupportedOperationException("All mandatory fields must be filled");
    } else if (shippingAddress == null) {
      shippingAddress = billingAddress;
    }

    if (items.size() > ORDER_CATEGORY_BOUNDARY) {
      if (giftWrap) {
        throw new UnsupportedOperationException("Gift wrapping not available for large orders (>3 items)");
      }
      return new BulkOrder(items, creditCardDetails, billingAddress, shippingAddress, courier, ccp,
          discount);
    } else {
      if (!discount.equals(BigDecimal.ZERO)) {
        throw new UnsupportedOperationException("No discount available for small order (<3 items)");
      }
      return new SmallOrder(items, creditCardDetails, billingAddress, shippingAddress, courier, ccp,
          giftWrap);
    }
  }

}