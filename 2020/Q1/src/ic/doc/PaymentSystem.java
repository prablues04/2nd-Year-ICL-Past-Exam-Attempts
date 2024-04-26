package ic.doc;

public interface PaymentSystem {
  boolean charge(double unitPrice, Customer customer);
}
