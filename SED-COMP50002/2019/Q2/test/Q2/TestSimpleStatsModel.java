package Q2;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TestSimpleStatsModel {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  Updatable view = context.mock(Updatable.class);
  private final SimpleStatsModel model = new SimpleStatsModel(new ArrayList<>(List.of()));

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

  @Test
  public void updatesToModelIsPropagatedToObserver() {
    context.checking(new Expectations() {{
      exactly(1).of(view).update(model);
    }});
    model.addObserver(view);
    model.addNum(10);
  }

  @Test
  public void updatesToModelIsNotPropagatedIfNoObserversExist() {
    context.checking(new Expectations() {{
      exactly(0).of(view).update(model);
    }});
    model.addNum(10);
  }
}
