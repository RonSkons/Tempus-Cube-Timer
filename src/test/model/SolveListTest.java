package model;

import model.SolveList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolveListTest {
    public SolveList sl1;
    public Solve s1, s2, s3;

    @BeforeEach
    public void setup() {
        sl1 = new SolveList();
        s1 = new Solve(3.141, "R2 U2 F D' B F'");
        s2 = new Solve(12.891);
        s3 = new Solve(32.89);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, sl1.getSolveList().size());
    }

    @Test
    public void testAdd1() {
        sl1.add(s1);
        assertEquals(s1, sl1.getSolveList().get(0));
        assertEquals(1, sl1.getSolveList().size());
    }

    @Test
    public void testAddMultiple() {
        sl1.add(s1);
        sl1.add(s2);
        sl1.add(s3);
        assertEquals(s1, sl1.getSolveList().get(0));
        assertEquals(s2, sl1.getSolveList().get(1));
        assertEquals(s3, sl1.getSolveList().get(2));
        assertEquals(3, sl1.getSolveList().size());
    }

    @Test
    public void testClear() {
        sl1.add(s1);
        sl1.add(s2);
        sl1.add(s3);
        sl1.clear();
        assertEquals(0, sl1.getSolveList().size());
    }

    @Test
    public void testRemove1() {
        sl1.add(s1);
        sl1.remove(0);
        assertEquals(0, sl1.getSolveList().size());
    }

    @Test
    public void testRemoveMultiple() {
        sl1.add(s1);
        sl1.add(s2);
        sl1.add(s3);
        sl1.remove(1);
        sl1.remove(1);
        assertEquals(s1, sl1.getSolveList().get(0));
        assertEquals(1, sl1.getSolveList().size());
    }

    @Test
    public void testRemoveFromMiddle() {
        sl1.add(s1);
        sl1.add(s2);
        sl1.add(s3);
        sl1.remove(1);
        assertEquals(s1, sl1.getSolveList().get(0));
        assertEquals(s3, sl1.getSolveList().get(1));
        assertEquals(2, sl1.getSolveList().size());
    }

    @Test
    public void testListLatestSolvesEmpty() {
        assertEquals("", sl1.listLatestSolves(5));
    }

    @Test
    public void testListLatestSolvesAll() {
        sl1.add(s1);
        sl1.add(s2);
        sl1.add(s3);
        assertEquals("[3] 32.89\n" +
                "[2] 12.891\n" +
                "[1] 3.141", sl1.listLatestSolves(3));
    }

    @Test
    public void testListLatestSolvesPartial() {
        sl1.add(s1);
        sl1.add(s2);
        sl1.add(s3);
        assertEquals("[3] 32.89\n" +
                "[2] 12.891", sl1.listLatestSolves(2));
    }

    @Test
    public void testListLatestSolvesOvershoot() {
        sl1.add(s1);
        sl1.add(s2);
        sl1.add(s3);
        assertEquals("[3] 32.89\n" +
                "[2] 12.891\n" +
                "[1] 3.141", sl1.listLatestSolves(2000));
    }
}
