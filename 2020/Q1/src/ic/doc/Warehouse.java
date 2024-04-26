package ic.doc;

public interface Warehouse {
  int checkStockLevel(Book book);

  void dispatch(Book book, int qty, Customer customer);
}
