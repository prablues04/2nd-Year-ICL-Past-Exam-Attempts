package retail;

import java.math.BigDecimal;

public interface CreditCardProcessors {
  void charge(BigDecimal amount, CreditCardDetails account, Address billingAddress);
}
