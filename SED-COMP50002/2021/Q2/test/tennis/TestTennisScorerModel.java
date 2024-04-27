package tennis;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TestTennisScorerModel {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  Updatable view = context.mock(Updatable.class);
  TennisScorerModel model = new TennisScorerModel(new ArrayList<>(List.of()));


  @Test
  public void scoresAreLevelAtTheStartOfTheGame() {
    assertThat(model.score(), is("Love all"));
  }

  @Test
  public void addingAPointToPlayerOneImprovesOnesScore() {
    model.addObserver(view);
    context.checking(new Expectations() {{
      exactly(1).of(view).update(model);
    }});
    model.playerOneWinsPoint();
    assertThat(model.score(), is("15 - Love"));
  }

  @Test
  public void addingAPointToPlayerTwoImprovesTwosScore() {
    model.addObserver(view);
    context.checking(new Expectations() {{
      exactly(1).of(view).update(model);
    }});
    model.playerTwoWinsPoint();
    assertThat(model.score(), is("Love - 15"));
  }

  @Test
  public void winningPointOnDeuceGivesAdvantage() {
    model.playerOneWinsPoint();
    model.playerOneWinsPoint();
    model.playerOneWinsPoint();
    model.playerTwoWinsPoint();
    model.playerTwoWinsPoint();
    model.playerTwoWinsPoint();
    model.playerOneWinsPoint();
    assertThat(model.score(), is("Advantage Player 1"));
  }

  @Test
  public void winningMatchPointWinsGame() {
    model.playerOneWinsPoint();
    model.playerOneWinsPoint();
    model.playerOneWinsPoint();
    model.playerOneWinsPoint();
    assertThat(model.score(), is("Game Player 1"));
  }

  /* Many more tests can be added given time */
}
