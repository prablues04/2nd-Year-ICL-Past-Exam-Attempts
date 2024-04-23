package retail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SmallOrder extends Order {

  private static final BigDecimal GIFT_WRAP_CHARGE = new BigDecimal(3);
  private final boolean giftWrap;

  public SmallOrder(
      List<Product> items,
      CreditCardDetails creditCardDetails,
      Address billingAddress,
      Address shippingAddress,
      Courier courier,
      CreditCardProcessors ccp,
      boolean giftWrap) {
    super(items, creditCardDetails, billingAddress, shippingAddress, courier, ccp);
    this.giftWrap = giftWrap;
  }

  @Override
  protected void sendParcel(Courier courier, List<Product> items, Address shippingAddress) {
    if (giftWrap) {
      courier.send(new GiftBox(items), shippingAddress);
    } else {
      super.sendParcel(courier, items, shippingAddress);
    }
  }

  @Override
  protected BigDecimal addExtraFeaturesCost(BigDecimal total, List<Product> items,
                                            Courier courier) {
    total = total.add(courier.deliveryCharge());
    if (giftWrap) {
      total = total.add(GIFT_WRAP_CHARGE);
    }
    return total;
  }
}
