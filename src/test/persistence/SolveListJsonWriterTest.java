package persistence;

import model.Solve;
import model.SolveList;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SolveListJsonWriterTest {

  // Based on code from JsonSerializationDemo
  @Test
  void testWriterInvalidFile() {
    try {
      SolveList solveList = new SolveList();
      SolveListJsonWriter writer = new SolveListJsonWriter("./data/my\0illegal:fileName.json");
      writer.open();
      fail("FileNotFoundException was expected");
    } catch (FileNotFoundException e) {
      // pass
    }
  }

  @Test
  public void testWriteSolveListNoSolves() {
    try {
      SolveListJsonWriter writer = new SolveListJsonWriter("./data/testWriterNoSolves.txt");
      SolveList solveList = new SolveList();
      writer.open();
      writer.writeSolveList(solveList);
      writer.close();

      SolveListJsonReader reader = new SolveListJsonReader("./data/testWriterNoSolves.txt");
      List<Solve> sl = reader.getSavedData().getSolveList();
      assertEquals(0, sl.size());
    } catch (IOException e) {
      System.out.println("Failed to read file");
    }
  }

  @Test
  public void testWriteSolveListOneSolve() {
    try {
      SolveListJsonWriter writer = new SolveListJsonWriter("./data/testWriterOneSolve.txt");
      SolveList solveList = new SolveList();
      solveList.add(new Solve(12.4, "R2 D2"));
      writer.open();
      writer.writeSolveList(solveList);
      writer.close();

      SolveListJsonReader reader = new SolveListJsonReader("./data/testWriterOneSolve.txt");
      List<Solve> sl = reader.getSavedData().getSolveList();
      assertEquals(1, sl.size());
      assertEquals(12.4, sl.get(0).getSolveTime());
      assertEquals("R2 D2", sl.get(0).getScrambleString());
    } catch (IOException e) {
      System.out.println("Failed to read file");
    }
  }

  @Test
  public void testWriteSolveListMultipleSolves() {
    try {
      SolveListJsonWriter writer = new SolveListJsonWriter("./data/testWriterMultipleSolves.txt");
      SolveList solveList = new SolveList();
      solveList.add(new Solve(12.4, "R2 D2"));
      solveList.add(new Solve(10.12, "F' D L2 R' B"));
      solveList.add(new Solve(8.63));
      writer.open();
      writer.writeSolveList(solveList);
      writer.close();

      SolveListJsonReader reader = new SolveListJsonReader("./data/testWriterMultipleSolves.txt");
      List<Solve> sl = reader.getSavedData().getSolveList();
      assertEquals(3, sl.size());

      assertEquals(12.4, sl.get(0).getSolveTime());
      assertEquals("R2 D2",
          sl.get(0).getScrambleString());

      assertEquals(10.12, sl.get(1).getSolveTime());
      assertEquals("F' D L2 R' B",
          sl.get(1).getScrambleString());

      assertEquals(8.63, sl.get(2).getSolveTime());
      assertEquals("Unspecified Scramble",
          sl.get(2).getScrambleString());
    } catch (IOException e) {
      System.out.println("Failed to read file");
    }
  }
}
