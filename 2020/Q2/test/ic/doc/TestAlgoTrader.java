package ic.doc;

import com.londonstockexchange.TickerSymbol;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class TestAlgoTrader {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  AnyBroker broker = context.mock(AnyBroker.class);
  DataFeed df = context.mock(DataFeed.class);
  TickerSymbol sym = TickerSymbol.AMZN
  AlgoTrader at = new AlgoTrader(broker, df, List.of(sym));

  @Test
  public void buyStockIfRising() {
    context.checking(new Expectations() {{
      exactly(1).of(df).currentPriceFor(sym);
      will(returnValue(1));
    }});
    at.trade();
  }



}
