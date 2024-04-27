package Q2;

import java.util.ArrayList;
import java.util.List;

public class SimpleStatsModel {
  List<Integer> numbers = new ArrayList<>();
  List<Updatable> observers;
  private int max;
  private double mean;

  public SimpleStatsModel(ArrayList<Updatable> observers) {
    this.observers = observers;
  }

  public void addObserver(Updatable ob) {
    observers.add(ob);
  }

  void addNum(int n) {
    calculateMax(n);
    numbers.add(n);
    calculateMean();
    updateAll();
  }

  private void updateAll() {
    observers.forEach(o -> o.update(this));
  }

  private void calculateMax(int n) {
    max = Math.max(max, n);
  }

  private void calculateMean() {
    mean = numbers.stream().mapToInt(val -> val).average().orElse(0.0);
  }

  int getMax() {
    return max;
  }

  double getMean() {
    return mean;
  }

}
