package retail;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class TestCreditCardOrderProcessing {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  CreditCardProcessors cardProc = context.mock(CreditCardProcessors.class);
  Courier courier = context.mock(Courier.class);
  CreditCardDetails account = new CreditCardDetails("1234123412341234", 111);
  Address billingAddress = new Address("180 Queens Gate, London, SW7 2AZ");

  OrderBuilder orderBuilder = new OrderBuilder()
      .withCreditCardDetails(account)
      .withBillingAddress(billingAddress)
      .withCourier(courier)
      .withCreditCardProcessor(cardProc);

  @Test
  public void smallOrderChargedCorrectly() {
    Order smallOrder = orderBuilder
        .addOneItem(new Product("One Book", new BigDecimal("10.00")))
        .addOneItem(new Product("One Book", new BigDecimal("10.00")))
        .withGiftWrap(true)
        .buildOrder();

    BigDecimal total = new BigDecimal("28.00");
    context.checking(new Expectations() {{
      ignoring(courier).deliveryCharge();
      will(returnValue(new BigDecimal(5)));
      exactly(1).of(cardProc).charge(total, account, billingAddress);
      ignoring(courier); // Since we are not testing this interaction
//      exactly(1).of(courier).send(with(any(Parcel.class)), with(any(Address.class)));
    }});
    smallOrder.process();
  }

  @Test
  public void bulkOrderChargedCorrectly() {
    Order bigOrder = orderBuilder
        .addOneItem(new Product("One Book", new BigDecimal("10.00")))
        .addOneItem(new Product("One Book", new BigDecimal("10.00")))
        .addOneItem(new Product("One Book", new BigDecimal("10.00")))
        .addOneItem(new Product("One Book", new BigDecimal("10.00")))
        .addOneItem(new Product("One Book", new BigDecimal("10.00")))
        .addOneItem(new Product("One Book", new BigDecimal("10.00")))
        .withDiscount(BigDecimal.ZERO)
        .buildOrder();

    BigDecimal total = new BigDecimal("54.00");
    context.checking(new Expectations() {{
      exactly(1).of(cardProc).charge(total, account, billingAddress);
      ignoring(courier); // Since we are not testing this interaction
//      exactly(1).of(courier).send(with(any(Parcel.class)), with(any(Address.class)));
    }});
    bigOrder.process();
  }
}
