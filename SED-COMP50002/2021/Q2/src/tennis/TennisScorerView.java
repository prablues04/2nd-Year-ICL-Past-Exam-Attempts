package tennis;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TennisScorerView implements Updatable {

  private final JFrame window;
  private final JButton playerOneScores;
  private final JButton playerTwoScores;
  private final JTextField scoreDisplay;
  private final JPanel panel;
  TennisScorerModel model = new TennisScorerModel(new ArrayList<>(List.of(this)));

  public TennisScorerView() {
    window = new JFrame("Tennis");
    window.setSize(400, 150);

    playerOneScores = new JButton("Player One Scores");
    playerTwoScores = new JButton("Player Two Scores");

    scoreDisplay = new JTextField(20);
    scoreDisplay.setHorizontalAlignment(JTextField.CENTER);
    scoreDisplay.setEditable(false);

    playerOneScores.addActionListener(
        e -> {
          model.playerOneWinsPoint();
        });

    playerTwoScores.addActionListener(
        e -> {
          model.playerTwoWinsPoint();
        });

    panel = new JPanel();
    panel.add(playerOneScores);
    panel.add(playerTwoScores);
    panel.add(scoreDisplay);

    window.add(panel);

    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  public static void main(String[] args) {
    new TennisScorerView().display();
  }


  private void display() {
    window.setVisible(true);
  }

  @Override
  public void update(TennisScorerModel model) {
    scoreDisplay.setText(model.score());
    if (model.gameHasEnded()) {
      playerOneScores.setEnabled(false);
      playerTwoScores.setEnabled(false);
    }
  }
}