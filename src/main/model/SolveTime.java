package model;

// Represents a completed solve, with time and scramble
public class SolveTime {
    private double seconds;
    private String scrambleString;

    // requires: seconds > 0
    // effects: constructs a SolveTime with given time (in seconds) and empty scramble string
    public SolveTime(double seconds) {
        this.seconds = seconds;
        scrambleString = "Unspecified Scramble";
    }

    // requires: seconds > 0
    // effects: constructs a SolveTime with given time (in seconds) and scramble string
    public SolveTime(double seconds, String scrambleString) {
        this.seconds = seconds;
        this.scrambleString = scrambleString;
    }

    public double getSeconds() {
        return seconds;
    }

    public String getScrambleString() {
        return scrambleString;
    }
}
