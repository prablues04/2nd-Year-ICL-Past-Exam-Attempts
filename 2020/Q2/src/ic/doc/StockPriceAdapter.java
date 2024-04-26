package ic.doc;

import com.londonstockexchange.StockPrice;

public class StockPriceAdapter implements Price {
  private final StockPrice price;

  public StockPriceAdapter(StockPrice price) {
    this.price = price;
  }
  @Override
  public int inPennies() {
    return price.inPennies();
  }
}
