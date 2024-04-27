package retail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


abstract public class Order {
  private final List<Product> items;
  private final CreditCardDetails creditCardDetails;
  private final Address billingAddress;
  private final Address shippingAddress;
  private final Courier courier;
  private final CreditCardProcessors ccp;

  protected Order(List<Product> items, CreditCardDetails creditCardDetails, Address billingAddress, Address shippingAddress, Courier courier, CreditCardProcessors ccp) {
    this.items = items;
    this.creditCardDetails = creditCardDetails;
    this.billingAddress = billingAddress;
    this.shippingAddress = shippingAddress;
    this.courier = courier;
    this.ccp = ccp;
  }

  public void process() {
    BigDecimal total = new BigDecimal(0);

    for (Product item : items) {
      total = total.add(item.unitPrice());
    }

    total = addExtraFeaturesCost(total, items, courier);

    ccp.charge(round(total), creditCardDetails, billingAddress);

    sendParcel(courier, items, shippingAddress);
  }

  protected void sendParcel(Courier courier, List<Product> items, Address shippingAddress) {
    courier.send(new Parcel(items), shippingAddress);
  };

  protected abstract BigDecimal addExtraFeaturesCost(BigDecimal total, List<Product> items,
                                                     Courier courier);

  private BigDecimal round(BigDecimal amount) {
    return amount.setScale(2, RoundingMode.CEILING);
  }

  ;
}
