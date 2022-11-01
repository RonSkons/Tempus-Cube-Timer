package ui;

import model.ScrambleGenerator;
import model.Solve;
import model.SolveList;
import persistence.SolveListJsonReader;
import persistence.SolveListJsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Cube timer application with GUI
public class CubeTimerUI extends KeyAdapter implements ActionListener {
    private static final int SCRAMBLE_LEN = 20;
    private static final String saveLocation = "./data/solveData.txt";

    private boolean isTiming;
    private long startTime;
    private ScrambleGenerator scrambler;
    private SolveList solves;
    private SolveListJsonWriter jsonWriter;
    private SolveListJsonReader jsonReader;
    private JLabel statsLabel;
    private JButton timerButton;
    private JButton addButton;
    private JButton removeButton;
    private JButton clearButton;
    private JLabel scrambleLabel;
    private DefaultListModel<String> solveListModel; // So we can add/delete solves
    private JList<String> solveListDisplay;
    private JFrame frame;

    // EFFECTS: Contructs a CubeTimerUI
    public CubeTimerUI() {
        isTiming = false;
        startTime = 0;
        scrambler = new ScrambleGenerator();
        jsonReader = new SolveListJsonReader(saveLocation);
        jsonWriter = new SolveListJsonWriter(saveLocation);
        scrambleLabel = new JLabel("", SwingConstants.CENTER);
        statsLabel = new JLabel();
        solveListModel = new DefaultListModel<>();
        solveListDisplay = new JList<>(solveListModel);
        solveListDisplay.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        frame = new JFrame("mainPanel");

        initializeButtons();
        loadSaveData();
        setUpWindow();
    }

    public void initializeButtons() {
        timerButton = new JButton("START TIMER");
        addButton = new JButton("+");
        removeButton = new JButton("-");
        clearButton = new JButton("Clear");
        timerButton.addActionListener(this);
        clearButton.addActionListener(this);
        addButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    // EFFECTS: Displays the initial timer window
    public void setUpWindow() {
        frame.addKeyListener(this);
        frame.setTitle("Tempus Cube Timer");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO SAVE BEFORE CLOSE
        Container pane = frame.getContentPane();

        pane.add(new JLabel("<html><h1>Tempus Cube Timer</h1></html>", SwingConstants.CENTER),
                BorderLayout.PAGE_START);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));

        left.add(new JLabel("<html><h3>Solves:</h3></html>"));
        left.add(new JScrollPane(solveListDisplay));
        left.add(addButton);
        left.add(removeButton);
        left.add(clearButton);
        contentPanel.add(left, BorderLayout.LINE_START);
        updateSolveList();

        JPanel center = new JPanel();
        center.add(new JLabel("Press space to"));
        center.add(timerButton);
        contentPanel.add(center);

        JPanel right = new JPanel();
        updateStats();
        right.add(statsLabel);
        contentPanel.add(right);

        updateScramble();
        pane.add(scrambleLabel, BorderLayout.PAGE_END);

        pane.add(contentPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
    }

    // MODIFIES: this
    // EFFECTS: Displays a list of all solves in reverse chronological order
    private void updateSolveList() {
        solveListModel.clear();
        List<Solve> solveListCopy = new ArrayList(solves.getSolveList()); // copy to preserve
        Collections.reverse(solveListCopy); // Show newest solves first
        for (Solve solve : solveListCopy) {
            solveListModel.addElement(String.valueOf(solve.getSolveTime()));
        }
    }

    // MODIFIES: this
    // EFFECTS: Generates a new scramble and updates scrambleLabel to display it
    public void updateScramble() {
        scrambler.generateScramble(SCRAMBLE_LEN);
        scrambleLabel.setText("<html><h4>Scramble: " + scrambler.toString() + "</h4></html>");
    }

    // MODIFIES: this
    // EFFECTS: if there is a saved SolveList, load it into solves
    private void loadSaveData() {
        try {
            solves = jsonReader.getSavedData();
        } catch (IOException e) {
            // Save not found, that's okay.
            // It will be created the next time the user saves.
        }
    }

    // MODIFIES: this
    // EFFECTS: updates statsLabel with relevant solve statistics
    private void updateStats() {
        StringBuilder content = new StringBuilder("<html><h3>Statistics:<br>");
        int size = solves.getSolveList().size();
        if (size >= 1) {
            content.append("<p>Session mean: <b>");
            content.append(roundToTwoDigits(solves.currentSessionMean()));
            content.append("</b></p><p>Fastest time: <b>");
            content.append(solves.currentFastestSolve().getSolveTime()).append("</b></p>");
        }

        if (size >= 5) {
            content.append("<p>Current Ao5: <b>");
            content.append(roundToTwoDigits(solves.currentAverageOfN(5))).append("</b></p>");
        }

        if (size >= 12) {
            content.append("<p>Current Ao12: <b>");
            content.append(roundToTwoDigits(solves.currentAverageOfN(12))).append("</b></p>");
        }

        content.append("</html>");
        statsLabel.setText(content.toString());
    }

    // EFFECTS: produces d, rounded to the hundredth
    private double roundToTwoDigits(double d) {
        return Math.round(d * 100d) / 100d;
    }

    // MODIFIES: this
    // EFFECTS: Responds to a user action appropriately
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.requestFocus(); // Unfocus button so pressing space doesn't click it again

        switch (e.getActionCommand()) {
            case "+":
                System.out.println("Add solve");
                break;
            case "-":
                System.out.println("Remove solve");
                break;
            case "Clear":
                System.out.println("Clear");
                break;
            default:
                if (!isTiming) {
                    startTimer();
                } else {
                    stopTimer();
                }
        }
    }

    // MODIFIES: this
    // EFFECTS: Handle KeyEvents, starting or stopping the timer appropriately
    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case ' ':
                if (!isTiming) {
                    startTimer();
                } else {
                    stopTimer();
                }
                break;
            default:
                if (isTiming) { // Pressing anything while timing should stop timer
                    stopTimer();
                }
        }
    }

    // MODIFIES: this
    // REQUIRES: isTiming must be false
    // EFFECTS: Records the current time to startTime and provides an indicator that timer is on
    public void startTimer() {
        startTime = System.currentTimeMillis();
        isTiming = true;
        // TODO Visual element, time label
    }

    // MODIFIES: this
    // REQUIRES: isTiming must be true
    // EFFECTS: Adds new to solves with solve time of seconds since startTime, updates GUI components appropriately
    public void stopTimer() {
        long stopTime = System.currentTimeMillis();
        long difference = stopTime - startTime;
        double solveTime = difference / 1_000d; // Convert from ms to s
        solves.add(new Solve(solveTime, scrambler.toString()));
        scrambler.generateScramble(SCRAMBLE_LEN);
        updateScramble();
        updateSolveList();
        updateStats();
        // TODO Visual element, time label

        isTiming = false;
    }

}
