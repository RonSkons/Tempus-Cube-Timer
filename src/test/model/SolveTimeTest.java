package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolveTimeTest {
    private SolveTime st1;
    private SolveTime st2;

    @BeforeEach
    public void setup() {
        st1 = new SolveTime(5.12);
        st2 = new SolveTime(12.345, "R2 L2 B2 F2 D2 U2");
    }

    @Test
    public void testConstructor() {
        assertEquals(5.12, st1.getSeconds());
        assertEquals("Unspecified Scramble", st1.getScrambleString());

        assertEquals(12.345, st2.getSeconds());
        assertEquals("R2 L2 B2 F2 D2 U2", st2.getScrambleString());
    }
}
