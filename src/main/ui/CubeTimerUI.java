package ui;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import model.Event;
import model.EventLog;
import model.ScrambleGenerator;
import model.Solve;
import model.SolveList;
import persistence.SolveListJsonReader;
import persistence.SolveListJsonWriter;

// Cube timer application with GUI
public class CubeTimerUI extends KeyAdapter implements ActionListener {
  private static final int SCRAMBLE_LEN = 20;
  private static final String SAVE_LOCATION = "./data/solveData.txt";
  private static final ImageIcon ICON = new ImageIcon("./data/cube.png");

  private JPanel left;
  private JPanel right;
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
  private JButton exportButton;
  private JLabel scrambleLabel;
  private DefaultListModel<String> solveListModel; // So we can add/delete solves
  private JList<String> solveListDisplay;
  private JFrame frame;

  /** EFFECTS: Constructs a CubeTimerUI. */
  public CubeTimerUI() {
    solves = new SolveList();
    isTiming = false;
    startTime = 0;
    scrambler = new ScrambleGenerator();
    jsonReader = new SolveListJsonReader(SAVE_LOCATION);
    jsonWriter = new SolveListJsonWriter(SAVE_LOCATION);
    scrambleLabel = new JLabel("", SwingConstants.CENTER);
    statisticLabel = new JLabel();
    mainLabel = new JLabel("<html><h1>Tempus Cube Timer</h1></html>", ICON, SwingConstants.CENTER);
    solveListModel = new DefaultListModel<>();
    solveListDisplay = new JList<>(solveListModel);
    solveListDisplay.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

    left = new JPanel();
    right = new JPanel();

    initializeButtons();
    initializeFrame();
    loadSaveData();
    setUpWindow();
  }


  // MODIFIES: this
  // EFFECTS: initializes all required buttons and sets up action listeners
  private void initializeButtons() {
    String buttonText = "<html>Press space or <br><b>CLICK HERE</b><br> to start/stop timer</html>";
    timerButton = new JButton(buttonText);
    addButton = new JButton("+");
    removeButton = new JButton("-");
    clearButton = new JButton("Clear");
    exportButton = new JButton("Export");
    timerButton.addActionListener(this);
    clearButton.addActionListener(this);
    addButton.addActionListener(this);
    removeButton.addActionListener(this);
    exportButton.addActionListener(this);
  }

  // EFFECTS: constructs main frame, defining initial parameters
  private void initializeFrame() {
    frame = new JFrame("Tempus Cube Timer");
    frame.addKeyListener(this);
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(new Dimension(700, 500));
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        int confirm = JOptionPane.showConfirmDialog(frame,"Save changes?");
        if (confirm == JOptionPane.YES_OPTION) {
          writeSaveData();
          printLog(EventLog.getInstance());
          System.exit(0);
        } else if (confirm == JOptionPane.NO_OPTION) {
          printLog(EventLog.getInstance());
          System.exit(0);
        }
      }
    });
  }

  // Based on code from AlarmSystem
  // EFFECTS: Prints the EventLog to the console
  private void printLog(EventLog el) {
    for (Event next : el) {
      System.out.println();
      System.out.println(next.toString());
    }
  }

  // REQUIRES: relevant elements are appropriately initialized.
  // Should always occur under normal operations.
  // EFFECTS: Displays the initial timer window
  private void setUpWindow() {
    Container pane = frame.getContentPane();
    pane.add(mainLabel, BorderLayout.PAGE_START);

    left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));

    left.add(new JLabel("<html><h3>Solves:</h3></html>"));
    left.add(new JScrollPane(solveListDisplay));
    left.add(addButton);
    left.add(removeButton);
    left.add(clearButton);
    left.add(exportButton);
    pane.add(left, BorderLayout.LINE_START);
    updateSolveList();

    pane.add(timerButton, BorderLayout.CENTER);

    updateStats();
    right.add(statisticLabel);
    pane.add(right, BorderLayout.LINE_END);

    updateScramble();
    pane.add(scrambleLabel, BorderLayout.PAGE_END);

    frame.setLocationRelativeTo(null);
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
    scrambleLabel.setText("<html><h3>Scramble: " + scrambler.toString() + "</h3></html>");
  }

  // MODIFIES: this
  // EFFECTS: if there is a saved SolveList, give user the option to load it into solves
  private void loadSaveData() {
    int confirm = JOptionPane.showConfirmDialog(frame,"Load previous solves?");
    if (confirm == JOptionPane.YES_OPTION) {
      try {
        solves = jsonReader.getSavedData();
      } catch (IOException e) {
        // Save not found, that's okay.
        // It will be created the next time the user saves.
      }
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
      JOptionPane.showMessageDialog(frame, "Error writing to " + SAVE_LOCATION);
    }
  }

  // MODIFIES: this
  // EFFECTS: updates statsLabel with relevant solve statistics
  private void updateStats() {
    StringBuilder content = new StringBuilder("<html><h3>Statistics:<br>");
    int size = solves.getSolveList().size();
    content.append("<p>Solves: <b>").append(size).append("</p></b>");

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
    frame.requestFocus(); // Un-focus button so pressing space doesn't click it again

    switch (e.getActionCommand()) {
      case "+":
        addCustomTime();
        break;
      case "-":
        deleteSelectedSolve();
        break;
      case "Clear":
        clearSolves();
        break;
      case "Export":
        exportSolves();
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
  // EFFECTS: Prompts the user to enter a solve time, and adds it to solves if valid
  private void addCustomTime() {
    String time = JOptionPane.showInputDialog("Enter Solve Time:");
    try {
      double timeDouble = Double.parseDouble(time);
      if (timeDouble > 0) {
        solves.add(new Solve(timeDouble));
        updateStats();
        updateSolveList();
      } else {
        JOptionPane.showMessageDialog(frame,"Times must be positive. You're not that fast!");
      }
    } catch (Exception e) { // User didn't enter a double; don't add anything.
      JOptionPane.showMessageDialog(frame,"Time not added.");
    }
  }

  //EFFECTS: Presents the user with a string representation of solves
  private void exportSolves() {
    JTextArea textArea = new JTextArea(solves.listLatestSolves(solves.getSolveList().size()));
    JOptionPane.showMessageDialog(frame, new JScrollPane(textArea));
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
  // EFFECTS: Adds new to solves with solve time of seconds since
  // startTime, and updates GUI components appropriately
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
  // EFFECTS: If a Solve is selected in the GUI, delete it.
  public void deleteSelectedSolve() {
    int index = solveListDisplay.getSelectedIndex();
    if (index != -1) { // Valid selection
      solves.remove(solves.getSolveList().size() - 1 - index); // Since solveListDisplay is reversed
      updateSolveList();
      updateStats();
    }
  }

  // MODIFIES: this
  // EFFECTS: double-check that user wants to clear all solves, and do so if they want to
  public void clearSolves() {
    int confirm = JOptionPane.showConfirmDialog(frame,"Are you sure you want to clear all solves?");
    if (confirm == JOptionPane.YES_OPTION) {
      solves.clear();
      updateSolveList();
      updateStats();
    }
  }
}
