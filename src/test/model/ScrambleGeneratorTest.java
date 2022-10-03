package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScrambleGeneratorTest {
    private ScrambleGenerator scrambler;

    @BeforeEach
    public void setup() {
        scrambler = new ScrambleGenerator();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, scrambler.getCurrentScramble().size());
    }

    @Test
    public void testGenerateScramble1Move() {
        scrambler.generateScramble(1);
        assertEquals(1, scrambler.getCurrentScramble().size());
        assertTrue(ScrambleGenerator.VALID_MOVES.contains(scrambler.getCurrentScramble().get(0)));
    }

    @Test
    public void testGenerateScramble20Moves() {
        scrambler.generateScramble(20);
        assertEquals(20, scrambler.getCurrentScramble().size());
        assertTrue(ScrambleGenerator.VALID_MOVES.containsAll(scrambler.getCurrentScramble()));
    }

    @Test
    public void testGenerateScrambleUniqueness() {
        scrambler.generateScramble(3);
        List<String> moves = scrambler.getCurrentScramble();
        // No 2 subsequent moves should operate on the same face
        // (Meaning, should not start with the same letter)
        assertNotEquals(moves.get(0).charAt(0), moves.get(1).charAt(0));
        assertNotEquals(moves.get(1).charAt(0), moves.get(2).charAt(0));
    }

    @Test
    public void testToStringEmptyScramble(){
        assertEquals("", scrambler.toString());
    }

    @Test
    public void testToStringShortScramble(){
        scrambler.generateScramble(1);
        String stringified = scrambler.toString();
        // String representation of a single move shouldn't exceed 2 chars
        assertFalse(stringified.length() > 2);
    }

    @Test
    public void testToStringLongScramble(){
        scrambler.generateScramble(45);
        String stringified = scrambler.toString();
        String[] split = stringified.split(" ");
        // When stringified is split on " ", result should have appropriate # of moves
        assertEquals(45, split.length);
    }
}