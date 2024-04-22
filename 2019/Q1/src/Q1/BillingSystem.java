package Q1;

public class BillingSystem implements Bill {

  private static final BillingSystem INSTANCE = new BillingSystem();

  public static BillingSystem getInstance() {
    return INSTANCE;
  }

  @Override
  public void addBillItem(String caller, String callee, long callCostInPence) {

    // Imagine lots more code here that really does payment processing - we
    // did not implement it all for the purposes of the exam.

    System.out.println(
        String.format("Bill item added: %s => %s [ cost: %d ]", caller, callee, callCostInPence));
  }

}
