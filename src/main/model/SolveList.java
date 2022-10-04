package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Represents a list of Solves, and provides common operations involving them
public class SolveList {
    private List<Solve> solveList;

    //effects: constructs an empty SolveList
    public SolveList() {
        solveList = new ArrayList<>();
    }

    // modifies: this
    // effects: clears the list of solves
    public void clear() {
        solveList.clear();
    }

    // modifies: this
    // effects: adds given solve to the list of solves
    public void add(Solve solve) {
        solveList.add(solve);
    }

    // requires: solveList.get(index) exists
    // modifies: this
    // effects: removes the Solve at given index from solveList
    public void remove(int index) {
        solveList.remove(index);
    }

    // requires: 0 <= n
    // effects: returns a String representation of up to n of the most recent solves
    public String listLatestSolves(int n) {
        StringBuilder output = new StringBuilder();
        List<Solve> lastNSolves = getLatestSolves(n);
        for (Solve solve : lastNSolves) {
            output.append("[").append(solveList.lastIndexOf(solve) + 1).append("] ");
            output.append(solve.getSolveTime());
            output.append("\n");
        }
        return output.toString().trim();
    }

    // requires: 3 <= n <= solveList.size()
    // effects: returns the mean of the last n solves, ignoring the fastest and slowest ones
    public double currentAverageOfN(int n) {
        List<Solve> lastFive = getLatestSolves(n);
        Solve max = Collections.max(lastFive, Comparator.comparing(Solve::getSolveTime));
        Solve min = Collections.min(lastFive, Comparator.comparing(Solve::getSolveTime));
        lastFive.remove(max);
        lastFive.remove(min);
        double sum = lastFive.stream().mapToDouble(Solve::getSolveTime).sum();
        return sum / (n - 2);
    }

    // requires: solveList is not empty
    // effects: returns the fastest solve in solveList
    public Solve currentFastestSolve() {
        return Collections.min(solveList, Comparator.comparing(Solve::getSolveTime));
    }

    public List<Solve> getSolveList() {
        return solveList;
    }

    // requires: 0 <= n
    // effects: returns last n solves in reverse order. If n > solveList.size(), produce solveList reversed
    private List<Solve> getLatestSolves(int n) {
        List<Solve> output = new ArrayList<>();
        int lastSolveIndex = Math.max(solveList.size() - n, 0); // if n is too large, return all solves
        for (int i = solveList.size() - 1; i >= lastSolveIndex; i--) {
            output.add(solveList.get(i));
        }
        return output;
    }
}
