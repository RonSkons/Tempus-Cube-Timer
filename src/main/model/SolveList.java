package model;

import java.util.ArrayList;
import java.util.Collections;
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
        int lastSolveIndex = Math.max(solveList.size() - n, 0); // if n is too large, return as many solves as possible
        for (int i = solveList.size() - 1; i >= lastSolveIndex; i--) {
            output.append("[").append(i + 1).append("] ");
            output.append(solveList.get(i).getSolveTime());
            output.append("\n");
        }
        return output.toString().trim();
    }

    public List<Solve> getSolveList() {
        return solveList;
    }
}
