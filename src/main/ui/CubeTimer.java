package ui;

import model.ScrambleGenerator;
import model.Solve;
import model.SolveList;

import java.util.Scanner;

// Cube timer application
public class CubeTimer {
    public static final int SCRAMBLE_LEN = 20;

    private SolveList solves;
    private ScrambleGenerator scrambler;
    private Scanner in;
    private long startTime;
    private long stopTime;

    // effects: constructs a CubeTimer
    public CubeTimer() {
        solves = new SolveList();
        scrambler = new ScrambleGenerator();
        in = new Scanner(System.in);
        runCubeTimer();
    }

    private void runCubeTimer() {
        boolean loop = true;
        String command;

        System.out.println("Tempus Cube Timer");
        while (loop) {
            System.out.println("=================");
            showScramble();
            showMenu();
            command = in.nextLine().toLowerCase();
            switch (command) {
                case "l":
                    showSolveList();
                    break;
                case "s":
                    showStatistics();
                    break;
                case "q":
                    shutdown();
                    loop = false;
                    break;
                default:
                    timeSolve();
            }
        }
    }

    // effects: generates a new scramble, and prints it
    private void showScramble() {
        scrambler.generateScramble(SCRAMBLE_LEN);
        System.out.println("Next Scramble: " + scrambler.toString());
    }

    // effects: Prints a menu with options
    private void showMenu() {
        System.out.println("(L)ist solves, view (s)tats, (a)dd, (d)elete, or (c)lear solves, or (q)uit");
        System.out.println("Press ENTER to start timer!");
    }

    // effects: prints a list of all solve times
    private void showSolveList() {
        int solveCount = solves.getSolveList().size();
        System.out.println(solveCount + " solves on record:");
        System.out.println(solves.listLatestSolves(solveCount));
    }

    // effects: shows relevant averages and personal best
    private void showStatistics() {
        int solveCount = solves.getSolveList().size();
        System.out.println("Total solves: " + solveCount);

        if (solveCount > 0) {
            System.out.println("Fastest time: " + solves.currentFastestSolve().getSolveTime());
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

    // effects: Runs before program termination. Code to save state will go here eventually(?)
    private void shutdown() {
        System.out.println("Goodbye!");
    }

    // effects: times a solve and records the result
    private void timeSolve() {
        startTime = System.currentTimeMillis();
        System.out.println("Timing, press ENTER when done.");
        in.nextLine(); // Wait until user presses enter again
        stopTime = System.currentTimeMillis();

        long difference = stopTime - startTime;
        double solveTime = difference / 1_000d;

        System.out.println(solveTime);
        solves.add(new Solve(solveTime, scrambler.toString()));
    }
}
