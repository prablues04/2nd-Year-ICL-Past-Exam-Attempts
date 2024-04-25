package ic.doc;

import org.junit.Rule;
import org.junit.Test;
import org.jmock.integration.junit4.JUnitRuleMockery;

public class OrderProcessorTest {

  static final Book DESIGN_PATTERNS_BOOK =
      new Book("Design Patterns", "Gamma, Helm, Johnson and Vlissides", 25.99);
  static final Book LEGACY_CODE_BOOK =
      new Book("Working Effectively with Legacy Code", "Feathers", 29.99);

  static final Customer ALICE = new Customer("Alice Jones");
  static final Customer BOB = new Customer("Bob Smith");

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  Warehouse warehouse = context.mock(Warehouse.class);

  // write your tests here
  @Test
  public void orderProcessorChecksBookStockInWarehouseUponOrder() {
    context.checking(new Expectations() {{
      exactly(1).of(warehouse).checkStockLevel(LEGACY_CODE_BOOK);
    }});
    op.order(LEGACY_CODE_BOOK, 1, BOB);
  }

}
