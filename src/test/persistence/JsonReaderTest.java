package persistence;

import model.Solve;
import model.SolveList;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    // Based on code from JsonSerializationDemo
    @Test
    public void testGetSavedDataNoSuchFile() {
        JsonReader reader = new JsonReader("./data/foobar.txt");
        try {
            SolveList sl = reader.getSavedData();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testGetSavedDataNoSolves() {
        JsonReader reader = new JsonReader("./data/testReaderNoSolves.txt");
        try {
            List<Solve> sl = reader.getSavedData().getSolveList();
            assertEquals(0, sl.size());
        } catch (IOException e) {
            System.out.println("Failed to read file");
        }
    }

    @Test
    public void testGetSavedDataOneSolve() {
        JsonReader reader = new JsonReader("./data/testReaderOneSolve.txt");
        try {
            List<Solve> sl = reader.getSavedData().getSolveList();
            assertEquals(1, sl.size());
            assertEquals(5.302, sl.get(0).getSolveTime());
            assertEquals("D' L R2 F2 U B' R2 D' U2 D2 B' U2 R L2 D2 U R B2 D' B",
                    sl.get(0).getScrambleString());
        } catch (IOException e) {
            System.out.println("Failed to read file");
        }
    }

    @Test
    public void testGetSavedDataMultipleSolves() {
        JsonReader reader = new JsonReader("./data/testReaderMultipleSolves.txt");
        try {
            List<Solve> sl = reader.getSavedData().getSolveList();
            assertEquals(4, sl.size());

            assertEquals(9.865, sl.get(0).getSolveTime());
            assertEquals("U2 F U' D R2 U' L R' L2 F U L' R' D' B D R F D' F",
                    sl.get(0).getScrambleString());

            assertEquals(7.202, sl.get(1).getSolveTime());
            assertEquals("L D R D F' B F2 D U' B2 D' F2 D' F D U2 F2 L' U' R'",
                    sl.get(1).getScrambleString());

            assertEquals(8.081, sl.get(2).getSolveTime());
            assertEquals("U2 L B L2 F2 L U2 L B L2 B' D' F D' U' R' B F2 U D",
                    sl.get(2).getScrambleString());

            assertEquals(9.41, sl.get(3).getSolveTime());
            assertEquals("Unspecified Scramble", sl.get(3).getScrambleString());

        } catch (IOException e) {
            System.out.println("Failed to read file");
        }
    }
}
