package retail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BulkOrder extends Order {


  private final BigDecimal discount;

  public BulkOrder(
      List<Product> items,
      CreditCardDetails creditCardDetails,
      Address billingAddress,
      Address shippingAddress,
      Courier courier,
      CreditCardProcessors ccp,
      BigDecimal discount) {
    super(items, creditCardDetails, billingAddress, shippingAddress, courier, ccp);
    this.discount = discount;
  }

  @Override
  protected BigDecimal addExtraFeaturesCost(BigDecimal total, List<Product> items, Courier courier) {
    if (items.size() > 10) {
      total = total.multiply(BigDecimal.valueOf(0.8));
    } else if (items.size() > 5) {
      total = total.multiply(BigDecimal.valueOf(0.9));
    }
    return total.subtract(discount);
  }

}
