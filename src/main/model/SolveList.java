package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Represents a list of Solves, and provides common operations involving them
public class SolveList {
  private List<Solve> solveList;

  //EFFECTS: constructs an empty SolveList
  public SolveList() {
    solveList = new ArrayList<>();
  }

  // MODIFIES: this
  // EFFECTS: clears the list of solves
  public void clear() {
    solveList.clear();
    EventLog.getInstance().logEvent(new Event("Cleared solve list."));
  }

  // MODIFIES: this
  // EFFECTS: adds given solve to the list of solves
  public void add(Solve solve) {
    solveList.add(solve);
    Event newEvent = new Event("Added a solve to the list: " + solve.getSolveTime());
    EventLog.getInstance().logEvent(newEvent);
  }

  // REQUIRES: solveList.get(index) exists
  // MODIFIES: this
  // EFFECTS: removes the Solve at given index from solveList
  public void remove(int index) {
    solveList.remove(index);
    EventLog.getInstance().logEvent(new Event("Removed solve number " + index + " from the list."));
  }

  // REQUIRES: 0 <= n
  // EFFECTS: returns a String representation of up to n of the most recent solves
  public String listLatestSolves(int n) {
    StringBuilder output = new StringBuilder();
    List<Solve> lastNSolves = getLatestSolves(n);
    for (Solve solve : lastNSolves) {
      output.append("[").append(solveList.lastIndexOf(solve) + 1).append("] ");
      output.append(solve.getSolveTime());
      output.append("\n");
    }
    EventLog.getInstance().logEvent(new Event("Exported solve list."));
    return output.toString().trim();
  }

  // REQUIRES: 3 <= n <= solveList.size()
  // EFFECTS: returns the mean of the last n solves, ignoring the fastest and slowest ones
  public double currentAverageOfN(int n) {
    List<Solve> lastFive = getLatestSolves(n);
    Solve max = Collections.max(lastFive, Comparator.comparing(Solve::getSolveTime));
    Solve min = Collections.min(lastFive, Comparator.comparing(Solve::getSolveTime));
    lastFive.remove(max);
    lastFive.remove(min);
    double sum = lastFive.stream().mapToDouble(Solve::getSolveTime).sum();
    return sum / (n - 2);
  }

  // REQUIRES: solveList is not empty
  // EFFECTS: returns the mean of all solve times in solveList
  public double currentSessionMean() {
    double sum = solveList.stream().mapToDouble(Solve::getSolveTime).sum();
    return sum / solveList.size();
  }

  // REQUIRES: solveList is not empty
  // EFFECTS: returns the fastest solve in solveList
  public Solve currentFastestSolve() {
    return Collections.min(solveList, Comparator.comparing(Solve::getSolveTime));
  }

  public List<Solve> getSolveList() {
    return solveList;
  }

  // REQUIRES: 0 <= n
  // EFFECTS: returns last n solves in reverse order.
  // If n > solveList.size(), produce solveList reversed
  private List<Solve> getLatestSolves(int n) {
    List<Solve> output = new ArrayList<>();
    int lastSolveIndex = Math.max(solveList.size() - n, 0); // if n is too large, return all solves
    for (int i = solveList.size() - 1; i >= lastSolveIndex; i--) {
      output.add(solveList.get(i));
    }
    return output;
  }

}
