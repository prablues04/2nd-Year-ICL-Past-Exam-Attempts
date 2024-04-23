package retail;

import java.math.BigDecimal;

public class CreditCardProcessor implements CreditCardProcessors {

  private static final CreditCardProcessor INSTANCE = new CreditCardProcessor();

  private CreditCardProcessor() {}

  public static CreditCardProcessor getInstance() {
    return INSTANCE;
  }

  @Override
  public void charge(BigDecimal amount, CreditCardDetails account, Address billingAddress) {

    System.out.println("Credit card charged: " + account + " amount: " + amount);

    // further implementation omitted for exam question
  }
}
