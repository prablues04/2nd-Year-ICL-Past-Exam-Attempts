package ic.doc;

import com.londonstockexchange.TickerSymbol;

public interface DataFeed {
  Price currentPriceFor(TickerSymbol symbol);
}
