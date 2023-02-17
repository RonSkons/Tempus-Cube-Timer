package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Used to generate random Rubik's Cube scrambles of variable lengths
public class ScrambleGenerator {
  /* ASIDE: CUBE NOTATION.
   Each letter represents a 90-degree clockwise turn of a cube face.
   R = right, L = Left, F = front, B = back, U = up, D = down
   R' (pronounced R prime) is a counterclockwise right turn.
   R2 is a 180 degree right turn.
   In this way, any sequence of moves on the cube can be encoded as text.
   */
  public static final List<String> VALID_MOVES = Arrays.asList("R", "L", "F", "B", "U", "D",
      "R'", "L'", "F'", "B'", "U'", "D'", "R2", "L2", "F2", "B2", "U2", "D2");
  private List<String> currentScramble;
  private Random rand;

  // EFFECTS: constructs a new ScrambleGenerator
  public ScrambleGenerator() {
    currentScramble = new ArrayList<>();
    rand = new Random();
  }

  // REQUIRES: scrambleLength > 0
  // MODIFIES: this
  // EFFECTS: stores a sequence of scrambleLength random meaningful moves in currentScramble
  public void generateScramble(int scrambleLength) {
    currentScramble.clear();
    String previousMove = " ";
    String currentMove;
    while (currentScramble.size() < scrambleLength) {
      currentMove = VALID_MOVES.get(rand.nextInt(VALID_MOVES.size())); // Get a random move
      if (currentMove.charAt(0) != previousMove.charAt(0)) { // No repeat moves
        currentScramble.add(currentMove);
        previousMove = currentMove;
      }
    }
  }

  // EFFECTS: returns a string representation of currentScramble
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    for (String move : currentScramble) {
      output.append(" ").append(move);
    }
    return output.toString().trim(); // Trim to remove leading space
  }

  public List<String> getCurrentScramble() {
    return currentScramble;
  }
}
