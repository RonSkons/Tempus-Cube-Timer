package ui;

import model.ScrambleGenerator;
import model.Solve;
import model.SolveList;
import persistence.SolveListJsonReader;
import persistence.SolveListJsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

// Cube timer application with GUI TODO ADD VISUAL ELEMENT CRITERION
public class CubeTimerUI extends KeyAdapter implements ActionListener {
    private static final int SCRAMBLE_LEN = 20;
    private static final String saveLocation = "./data/solveData.txt";

    private boolean isTiming;
    private long startTime;
    private ScrambleGenerator scrambler;
    private SolveList solves;
    private SolveListJsonWriter jsonWriter;
    private SolveListJsonReader jsonReader;
    private JLabel statisticLabel;
    private JLabel mainLabel;
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
        solves = new SolveList();
        isTiming = false;
        startTime = 0;
        scrambler = new ScrambleGenerator();
        jsonReader = new SolveListJsonReader(saveLocation);
        jsonWriter = new SolveListJsonWriter(saveLocation);
        scrambleLabel = new JLabel("", SwingConstants.CENTER);
        statisticLabel = new JLabel();
        mainLabel = new JLabel();
        solveListModel = new DefaultListModel<>();
        solveListDisplay = new JList<>(solveListModel);
        solveListDisplay.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        initializeButtons();
        initializeFrame();
        loadSaveData();
        setUpWindow();
    }


    // MODIFIES: this
    // EFFECTS: initializes all required buttons and sets up action listeners
    private void initializeButtons() {
        timerButton = new JButton("START TIMER");
        addButton = new JButton("+");
        removeButton = new JButton("-");
        clearButton = new JButton("Clear");
        mainLabel = new JLabel("<html><h1>Tempus Cube Timer</h1></html>", SwingConstants.CENTER);
        timerButton.addActionListener(this);
        clearButton.addActionListener(this);
        addButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    // EFFECTS: constructs main frame, defining initial parameters
    private void initializeFrame() {
        frame = new JFrame("Tempus Cube Timer");
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                writeSaveData();
            }
        });
    }

    // REQUIRES: relevant elements are appropriately initialized. Should always occur under normal operations.
    // EFFECTS: Displays the initial timer window TODO CUT DOWN LINE COUNT
    private void setUpWindow() {
        Container pane = frame.getContentPane();
        pane.add(mainLabel, BorderLayout.PAGE_START);

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
        right.add(statisticLabel);
        contentPanel.add(right);

        updateScramble();
        pane.add(scrambleLabel, BorderLayout.PAGE_END);

        pane.add(contentPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center window on screen
        frame.setVisible(true);
        frame.requestFocus(); // Request focus so keyboard inputs are captured
    }

    // MODIFIES: this
    // EFFECTS: Displays a list of all solves in reverse chronological order
    private void updateSolveList() {
        solveListModel.clear();
        ArrayList<Solve> solveListCopy = new ArrayList<>(solves.getSolveList()); // copy to preserve
        Collections.reverse(solveListCopy); // Show newest solves first
        for (Solve solve : solveListCopy) {
            solveListModel.addElement(String.valueOf(solve.getSolveTime()));
        }
    }

    // MODIFIES: this
    // EFFECTS: Generates a new scramble and updates scrambleLabel to display it
    private void updateScramble() {
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
    // EFFECTS: saves the list of solves to persistent data
    private void writeSaveData() {
        try {
            jsonWriter.open();
            jsonWriter.writeSolveList(solves);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Error writing to " + saveLocation);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates statsLabel with relevant solve statistics
    private void updateStats() {
        StringBuilder content = new StringBuilder("<html><h3>Statistics:<br>");
        int size = solves.getSolveList().size();
        if (size >= 1) {
            content.append("<p>Session mean: <b>");
            content.append(roundTo3Digits(solves.currentSessionMean()));
            content.append("</b></p><p>Fastest time: <b>");
            content.append(solves.currentFastestSolve().getSolveTime()).append("</b></p>");
        }

        if (size >= 5) {
            content.append("<p>Current Ao5: <b>");
            content.append(roundTo3Digits(solves.currentAverageOfN(5))).append("</b></p>");
        }

        if (size >= 12) {
            content.append("<p>Current Ao12: <b>");
            content.append(roundTo3Digits(solves.currentAverageOfN(12))).append("</b></p>");
        }

        content.append("</html>");
        statisticLabel.setText(content.toString());
    }

    // EFFECTS: produces d, rounded to the thousandth
    private double roundTo3Digits(double d) {
        return Math.round(d * 1000d) / 1000d;
    }

    // MODIFIES: this
    // EFFECTS: Responds to a user action appropriately
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.requestFocus(); // Unfocus button so pressing space doesn't click it again

        switch (e.getActionCommand()) {
            case "+":
                // TODO Implement
                break;
            case "-":
                deleteSelectedSolve();
                break;
            case "Clear":
                clearSolves();
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
        if (e.getKeyChar() == ' ' && !isTiming) {
            startTimer();
        } else if (isTiming) { // Pressing anything while timing should stop timer
            stopTimer();
        }
    }

    // REQUIRES: isTiming must be false
    // MODIFIES: this
    // EFFECTS: Records the current time to startTime and provides an indicator that timer is on
    private void startTimer() {
        startTime = System.currentTimeMillis();
        isTiming = true;
        mainLabel.setText("<html><h1>Timing...</html></h1>");
    }

    // REQUIRES: isTiming must be true
    // MODIFIES: this
    // EFFECTS: Adds new to solves with solve time of seconds since startTime, updates GUI components appropriately
    private void stopTimer() {
        long stopTime = System.currentTimeMillis();
        long difference = stopTime - startTime;
        double solveTime = difference / 1_000d; // Convert from ms to s
        mainLabel.setText("<html><h1>" + solveTime + "</html></h1>");
        solves.add(new Solve(solveTime, scrambler.toString()));
        scrambler.generateScramble(SCRAMBLE_LEN);
        updateScramble();
        updateSolveList();
        updateStats();
        isTiming = false;
    }

    // MODIFIES: this
    // EFFECTS: If a solve is selected in the GUI, delete it.
    public void deleteSelectedSolve() {
        int index = solveListDisplay.getSelectedIndex();
        if (index != -1) { // Valid selection
            solves.remove(solves.getSolveList().size() - 1 - index); // Because solveListDisplay is reversed
            updateSolveList();
            updateStats();
        }
    }

    // MODIFIES: this
    // EFFECTS: double-check that user wants to clear all solves, and do so if they want to
    public void clearSolves() {
        int confirm = JOptionPane.showConfirmDialog(frame,"Are you sure you want to delete all solves?");
        if (confirm == JOptionPane.YES_OPTION) {
            solves.clear();
            updateSolveList();
            updateStats();
        }
    }
}
