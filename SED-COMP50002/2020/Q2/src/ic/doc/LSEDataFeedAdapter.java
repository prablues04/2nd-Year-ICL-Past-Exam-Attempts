package ic.doc;

import com.londonstockexchange.StockMarketDataFeed;
import com.londonstockexchange.TickerSymbol;

public class LSEDataFeedAdapter implements DataFeed {
  private final StockMarketDataFeed df = StockMarketDataFeed.getInstance();

  @Override
  public Price currentPriceFor(TickerSymbol symbol) {
    return new StockPriceAdapter(df.currentPriceFor(symbol));
  }
}
