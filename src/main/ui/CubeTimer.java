package ui;

import model.ScrambleGenerator;
import model.Solve;
import model.SolveList;

import java.util.Scanner;

// Cube timer application
public class CubeTimer {
    private static final int SCRAMBLE_LEN = 20;

    private SolveList solves;
    private ScrambleGenerator scrambler;
    private Scanner in;

    // EFFECTS: constructs a CubeTimer
    public CubeTimer() {
        solves = new SolveList();
        scrambler = new ScrambleGenerator();
        in = new Scanner(System.in);
        runCubeTimer();
    }

    // the main program loop. Shows the main menu until user quits out.
    private void runCubeTimer() {
        boolean loop = true;
        String command;

        System.out.println("Tempus Cube Timer");
        while (loop) {
            System.out.println("=================");
            showScramble();
            showMenu();
            command = in.nextLine().toLowerCase();
            if (command.equals("q")) {
                shutdown();
                loop = false;
            } else {
                handleCommand(command);
            }
        }
    }

    // EFFECTS: Handles a command
    private void handleCommand(String command) {
        switch (command) {
            case "l":
                showSolveList();
                break;
            case "s":
                showStatistics();
                break;
            case "a":
                addTime();
                break;
            case "d":
                deleteSolve();
                break;
            case "c":
                clearSolves();
                break;
            default:
                timeSolve();
        }
    }

    // MODIFIES: this
    // EFFECTS: generates a new scramble, and prints it
    private void showScramble() {
        scrambler.generateScramble(SCRAMBLE_LEN);
        System.out.println("Next Scramble: " + scrambler.toString());
    }

    // EFFECTS: Prints a menu with options
    private void showMenu() {
        System.out.println("(L)ist solves, view (s)tats, (a)dd, (d)elete, or (c)lear solves, or (q)uit");
        System.out.println("Press ENTER to start timer!");
    }

    // EFFECTS: prints a list of all solve times
    private void showSolveList() {
        int solveCount = solves.getSolveList().size();
        System.out.println(solveCount + " solves on record:");
        System.out.println(solves.listLatestSolves(solveCount));
    }

    // EFFECTS: shows relevant averages and personal best
    private void showStatistics() {
        int solveCount = solves.getSolveList().size();
        System.out.println("Total solves: " + solveCount);

        if (solveCount > 0) {
            Solve fastest = solves.currentFastestSolve();
            System.out.println("Fastest time: " + fastest.getSolveTime());
            System.out.println("Fastest time scramble: " + fastest.getScrambleString());
            System.out.println("Session mean: " + solves.currentSessionMean());
        }

        if (solveCount >= 5) {
            System.out.println("Current Average of 5: " + solves.currentAverageOfN(5));
        }

        if (solveCount >= 12) {
            System.out.println("Current Average of 12: " + solves.currentAverageOfN(12));
        }

        if (solveCount >= 25) {
            System.out.println("Current Average of 25: " + solves.currentAverageOfN(25));
        }
    }

    // MODIFIES: this
    // EFFECTS: allows the user to add a time manually
    private void addTime() {
        System.out.println("Enter time to add, or (c)ancel.");
        try {
            double time = in.nextDouble();
            if (time > 0) {
                solves.add(new Solve(time));
            } else {
                System.out.println("Times must be positive. You're not that fast!");
            }
        } catch (Exception e) { // User didn't enter a double; don't add anything.
            System.out.println("No time added.");
        }
        in.nextLine();
    }

    // MODIFIES: this
    // EFFECTS: allows the user to remove a time, if it exists
    private void deleteSolve() {
        showSolveList();
        System.out.println("Enter the number of the solve you would like to delete, or (c)ancel.");
        try {
            int index = in.nextInt();
            if (0 < index && index <= solves.getSolveList().size()) {
                solves.remove(index - 1);
            } else {
                System.out.println("Solve " + index + " does not exist.");
            }
        } catch (Exception e) { // User didn't enter an int; don't add anything.
            System.out.println("No solve deleted.");
        }
        in.nextLine();
    }

    // MODIFIES: this
    // EFFECTS: double-check that user wants to clear solve list. If true, do so.
    private void clearSolves() {
        System.out.println("Are you sure you want to delete ALL saved solves? (y/n)");
        String response = in.nextLine().toLowerCase();
        if (response.equals("y")) {
            solves.clear();
            System.out.println("Cleared all solves.");
        }
    }

    // EFFECTS: Runs before program termination. Code to save state will go here eventually(?)
    private void shutdown() {
        System.out.println("Goodbye!");
    }

    // MODIFIES: this
    // EFFECTS: times a solve and records the result
    private void timeSolve() {
        long startTime = System.currentTimeMillis();
        System.out.println("Timing, press ENTER when done.");
        in.nextLine(); // Wait until user presses enter again
        long stopTime = System.currentTimeMillis();

        long difference = stopTime - startTime;
        double solveTime = difference / 1_000d;

        System.out.println(solveTime);
        solves.add(new Solve(solveTime, scrambler.toString()));
    }
}
