package tennis;

import java.util.ArrayList;
import java.util.List;

public class TennisScorerModel {

  private final List<Updatable> observers;

  private int playerOneScore = 0;
  private int playerTwoScore = 0;
  private final String[] scoreNames = {"Love", "15", "30", "40"};

  public TennisScorerModel(ArrayList<Updatable> observers) {
    this.observers = observers;
  }

  public void addObserver(Updatable ob) {
    observers.add(ob);
  }

  public void notifyObservers() {
    observers.forEach(o -> o.update(this));
  }

  String score() {

    if (playerOneScore > 2 && playerTwoScore > 2) {
      int difference = playerOneScore - playerTwoScore;
      switch (difference) {
        case 0:
          return "Deuce";
        case 1:
          return "Advantage Player 1";
        case -1:
          return "Advantage Player 2";
        case 2:
          return "Game Player 1";
        case -2:
          return "Game Player 2";
      }
    }

    if (playerOneScore > 3) {
      return "Game Player 1";
    }
    if (playerTwoScore > 3) {
      return "Game Player 2";
    }
    if (playerOneScore == playerTwoScore) {
      return scoreNames[playerOneScore] + " all";
    }
    return scoreNames[playerOneScore] + " - " + scoreNames[playerTwoScore];
  }

  void playerOneWinsPoint() {
    playerOneScore++;
    notifyObservers();
  }

  void playerTwoWinsPoint() {
    playerTwoScore++;
    notifyObservers();
  }

  boolean gameHasEnded() {
    return score().contains("Game");
  }

}
