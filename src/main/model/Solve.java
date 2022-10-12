package model;

// Represents a completed cube solve, with a time in seconds and a string of its scramble
public class Solve {
    private double solveTime;
    private String scrambleString;

    // REQUIRES: seconds > 0
    // EFFECTS: constructs a Solve with given time (in seconds) and empty scramble string
    public Solve(double solveTime) {
        this.solveTime = solveTime;
        scrambleString = "Unspecified Scramble";
    }

    // REQUIRES: seconds > 0
    // EFFECTS: constructs a Solve with given time (in seconds) and scramble string
    public Solve(double solveTime, String scrambleString) {
        this.solveTime = solveTime;
        this.scrambleString = scrambleString;
    }

    public double getSolveTime() {
        return solveTime;
    }

    public String getScrambleString() {
        return scrambleString;
    }
}
