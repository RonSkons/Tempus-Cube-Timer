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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Cube timer application with GUI
public class CubeTimerUI {
    private static final int SCRAMBLE_LEN = 20;
    private static final String saveLocation = "./data/solveData.txt";

    private ScrambleGenerator scrambler;
    private SolveList solves;
    private SolveListJsonWriter jsonWriter;
    private SolveListJsonReader jsonReader;
    private JButton timerButton;
    private JLabel scrambleLabel;
    private DefaultListModel<String> solveListModel; // So we can add/delete solves
    private JList<String> solveListDisplay;

    // EFFECTS: Contructs a CubeTimerUI
    public CubeTimerUI() {
        scrambler = new ScrambleGenerator();
        jsonReader = new SolveListJsonReader(saveLocation);
        jsonWriter = new SolveListJsonWriter(saveLocation);
        scrambleLabel = new JLabel("", SwingConstants.CENTER);
        solveListModel = new DefaultListModel<>();
        solveListDisplay = new JList<>(solveListModel);
        solveListDisplay.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        timerButton = new JButton("START TIMER");
        timerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("test"); // TODO IMPLEMENT !!!
            }
        });

        loadSaveData();
        setUpWindow();
    }

    // EFFECTS: Displays the initial timer window
    public void setUpWindow() {
        JFrame frame = new JFrame("mainPanel");
        frame.setTitle("Tempus Cube Timer");
        frame.setSize(600, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO SAVE BEFORE CLOSE
        Container pane = frame.getContentPane();

        pane.add(new JLabel("<html><h1>Tempus Cube Timer</h1></html>", SwingConstants.CENTER),
                BorderLayout.PAGE_START);

        JPanel center = new JPanel();
        center.add(new JLabel("Press space to", SwingConstants.CENTER));
        center.add(timerButton);
        pane.add(center, BorderLayout.CENTER);

        updateScramble();
        pane.add(scrambleLabel, BorderLayout.PAGE_END);


        JScrollPane scrollPane = new JScrollPane(solveListDisplay);
        pane.add(scrollPane, BorderLayout.LINE_START);
        updateSolveList();

        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Displays a list of all solves in reverse chronological order
    private void updateSolveList() {
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
}
