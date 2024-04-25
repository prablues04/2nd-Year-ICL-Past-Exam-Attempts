package ic.doc;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class OrderProcessorTest {

  static final Book DESIGN_PATTERNS_BOOK =
      new Book("Design Patterns", "Gamma, Helm, Johnson and Vlissides", 25.99);
  static final Book LEGACY_CODE_BOOK =
      new Book("Working Effectively with Legacy Code", "Feathers", 29.99);

  static final Customer ALICE = new Customer("Alice Jones");
  static final Customer BOB = new Customer("Bob Smith");

  // write your tests here
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  Warehouse warehouse = context.mock(Warehouse.class);
  Buyer buyer = context.mock(Buyer.class);
  PaymentSystem paymentSystem = context.mock(PaymentSystem.class);
  private final OrderProcessor op = new OrderProcessor(warehouse, buyer, paymentSystem);

  // write your tests here
  @Test
  public void orderProcessorChecksBookStockInWarehouseUponOrder() {
    context.checking(new Expectations() {{
      exactly(1).of(warehouse).checkStockLevel(LEGACY_CODE_BOOK);
      will(returnValue(0));
      ignoring(buyer);
    }});
    op.order(LEGACY_CODE_BOOK, 1, BOB);
  }

  @Test
  public void buysMoreBooksIfOutOfStock() {
    context.checking((new Expectations() {{
      ignoring(warehouse).checkStockLevel(LEGACY_CODE_BOOK);
      will(returnValue(0));
      exactly(1).of(buyer).buyMoreOf(LEGACY_CODE_BOOK);
    }}));
    op.order(LEGACY_CODE_BOOK, 1, BOB);
  }

  @Test
  public void chargesForBookIfInSufficientInStock() {
    int orderQty = 2;
    context.checking(new Expectations() {{
      ignoring(warehouse).checkStockLevel(DESIGN_PATTERNS_BOOK);
      will(returnValue(3));
      exactly(1).of(paymentSystem).charge(orderQty * DESIGN_PATTERNS_BOOK.price(), ALICE);
      will(returnValue(false));
    }});
    op.order(DESIGN_PATTERNS_BOOK, orderQty, ALICE);
  }

  @Test
  public void dispatchesBookIfSufficientInStockAndSuccessfullyCharged() {
    int orderQty = 2;
    context.checking(new Expectations() {{
      ignoring(warehouse).checkStockLevel(DESIGN_PATTERNS_BOOK);
      will(returnValue(3));
      ignoring(paymentSystem).charge(orderQty * DESIGN_PATTERNS_BOOK.price(), ALICE);
      will(returnValue(true));
      exactly(1).of(warehouse).dispatch(DESIGN_PATTERNS_BOOK, orderQty, ALICE);
    }});
    op.order(DESIGN_PATTERNS_BOOK, orderQty, ALICE);
  }

  @Test
  public void fulfilOrderAfterNewBooksArrive() {
    context.checking((new Expectations() {{
      exactly(1).of(warehouse).checkStockLevel(LEGACY_CODE_BOOK);
      will(returnValue(0));
      ignoring(buyer).buyMoreOf(LEGACY_CODE_BOOK);
      exactly(1).of(warehouse).checkStockLevel(LEGACY_CODE_BOOK);
      will(returnValue(5));
      exactly(1).of(paymentSystem).charge(LEGACY_CODE_BOOK.price(), BOB);
      will(returnValue(true));
      exactly(1).of(warehouse).dispatch(LEGACY_CODE_BOOK, 1, BOB);
    }}));
    boolean fulfilled = op.order(LEGACY_CODE_BOOK, 1, BOB);
    assertThat(fulfilled, is(false));
    op.newBooksArrived();
  }

}
