package ic.doc;

import java.util.ArrayList;
import java.util.List;

public class OrderProcessor {
  private final Warehouse warehouse; //Could Be list of warehouses in future implementations
  private final Buyer buyer;
  private final PaymentSystem ps;
  private final List<Order> backlog = new ArrayList<>();
  public OrderProcessor(Warehouse warehouse, Buyer buyer, PaymentSystem ps) {
    this.warehouse = warehouse;
    this.buyer = buyer;
    this.ps = ps;
  }

  public boolean order(Book book, int qty, Customer customer) {
    boolean fulfilled = fulfilOrder(book, qty, customer);
    if (!fulfilled) {
      buyer.buyMoreOf(book);
      backlog.add(new Order(book, qty, customer));
    }
    return fulfilled;
  }

  public boolean fulfilOrder(Book book, int qty, Customer customer) {
    int stock = warehouse.checkStockLevel(book);
    if (stock >= qty) {
      boolean charged = ps.charge(qty * book.price(), customer);
      if (charged) {
        warehouse.dispatch(book, qty, customer);
        return true;
      }
//      else {
//        throw new RuntimeException("Payment Failure");
//      }
      return true; // for time being
    }
    return false;
  }

  public void newBooksArrived() {
    backlog.forEach(order -> fulfilOrder(order.book, order.qty, order.customer));
  }

  private class Order {
    private final Book book;
    private final int qty;
    private final Customer customer;

    private Order(Book book, int qty, Customer customer) {
      this.book = book;
      this.qty = qty;
      this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Order order) {
        return order.book.equals(this.book) && order.qty == this.qty && order.customer.equals(this.customer);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return 0;
    }
  }
}
