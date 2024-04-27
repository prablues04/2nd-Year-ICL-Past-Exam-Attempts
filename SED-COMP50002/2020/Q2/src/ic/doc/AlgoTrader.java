package ic.doc;

import com.londonstockexchange.TickerSymbol;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlgoTrader {

  private final List<TickerSymbol> stocksToWatch;

  private final Map<TickerSymbol, Integer> lastPrices = new HashMap<>();
  private final AnyBroker broker;

  private final DataFeed df;

  public AlgoTrader(AnyBroker broker, DataFeed df, List<TickerSymbol> stocksToWatch) {
    this.df = df;
    this.broker = broker;
    this.stocksToWatch = stocksToWatch;
  }

  public void trade() {

    for (TickerSymbol stock : stocksToWatch) {

      Price price = df.currentPriceFor(stock);

      if (isRising(stock, price)) {
        broker.buy(String.valueOf(stock));
      }

      if (isFalling(stock, price)) {
        broker.sell(String.valueOf(stock));
      }

      lastPrices.put(stock, price.inPennies());
    }
  }

  private boolean isFalling(TickerSymbol stock, Price price) {
    int lastPrice = lastPrices.containsKey(stock) ? lastPrices.get(stock) : 0;
    return price.inPennies() < lastPrice;
  }

  private boolean isRising(TickerSymbol stock, Price price) {
    int lastPrice = lastPrices.containsKey(stock) ? lastPrices.get(stock) : Integer.MAX_VALUE;
    return price.inPennies() > lastPrice;
  }

  public static void main(String[] args) {
    new AlgoTrader(new SimpleBroker(), new LSEDataFeedAdapter(), List.of(TickerSymbol.GOOG,
        TickerSymbol.MSFT, TickerSymbol.APPL)).start();
  }

  // code below here is not important for the exam

  private void logPrices(TickerSymbol stock, Price price, int lastPrice) {
    System.out.println(
        String.format("%s used to be %s, now %s ", stock, lastPrice, price.inPennies()));
  }

  private void start() {

    // run trade() every minute

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    executorService.scheduleAtFixedRate(this::trade, 0, 60, TimeUnit.SECONDS);
  }
}
