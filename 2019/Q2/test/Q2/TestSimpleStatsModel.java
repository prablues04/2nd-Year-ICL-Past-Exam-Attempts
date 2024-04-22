package Q2;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TestSimpleStatsModel {

  private final SimpleStatsModel model = new SimpleStatsModel(List.of());

  @Test
  public void maxTermAtInitialisationIsZero() {
    assertThat(model.getMax(), is(0));
  }

  @Test
  public void meanTermAtInitialisationIsZero() {
    assertThat(model.getMean(), is(0.0));
  }

  @Test
  public void meanTermUpdatesCorrectly() {
    model.addNum(4);
    assertThat(model.getMean(), is(4.0));
    model.addNum(9);
    assertThat(model.getMean(), is(6.5));
    model.addNum(5);
    assertThat(model.getMean(), is(6.0));
  }

  @Test
  public void maxTermUpdatesCorrectly() {
    model.addNum(4);
    assertThat(model.getMax(), is(4));
    model.addNum(9);
    assertThat(model.getMax(), is(9));
    model.addNum(5);
    assertThat(model.getMax(), is(9));
  }

}
