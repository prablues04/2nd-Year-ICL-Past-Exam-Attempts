package Q2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SimpleStatsView implements Updatable {

  private static final int MAX_INTEGER = 12;
  private final SimpleStatsModel model = new SimpleStatsModel(List.of(this));

  /* View components as fields */
  private final JFrame frame;
  private final JPanel panel;
  private final JTextField currentMax;
  private final JTextField currentMean;

  public SimpleStatsView() {
    frame = new JFrame("Simple Stats");
    frame.setSize(250, 350);
    panel = new JPanel();
    currentMax = new JTextField(11);
    currentMean = new JTextField(11);

    panel.add(new JLabel("Max: value "));
    panel.add(currentMax);
    panel.add(new JLabel("Mean: value "));
    panel.add(currentMean);

    for (int i = 1; i <= MAX_INTEGER; i++) {
      final int n = i;
      JButton button = new JButton(String.valueOf(i));
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          model.addNum(n);
        }
      });
      panel.add(button);
      frame.getContentPane().add(panel);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
  }

  private void display() {
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    new SimpleStatsView().display();
  }

  @Override
  public void update(SimpleStatsModel model) {
    currentMax.setText(String.valueOf(model.getMax()));
    currentMean.setText(String.valueOf(model.getMean()));
  }
}
