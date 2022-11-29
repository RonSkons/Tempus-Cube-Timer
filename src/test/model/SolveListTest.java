package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolveListTest {
    public SolveList solveList;
    public Solve s1, s2, s3, fastSolve, slowSolve;
    public EventLog el = EventLog.getInstance();
    @BeforeEach
    public void setup() {
        solveList = new SolveList();
        s1 = new Solve(8.141, "R2 U2 F D' B F'");
        s2 = new Solve(12.891);
        s3 = new Solve(32.89, "F2 U D L2 B U' L");
        fastSolve = new Solve(3.141);
        slowSolve = new Solve(40.983);
        el.clear();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, solveList.getSolveList().size());
    }

    @Test
    public void testAdd1() {
        solveList.add(s1);
        assertEquals(s1, solveList.getSolveList().get(0));
        assertEquals(1, solveList.getSolveList().size());

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        assertEquals("Added a solve to the list: 8.141",
                iterator.next().getDescription());
    }

    @Test
    public void testAddMultiple() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        assertEquals(s1, solveList.getSolveList().get(0));
        assertEquals(s2, solveList.getSolveList().get(1));
        assertEquals(s3, solveList.getSolveList().get(2));
        assertEquals(3, solveList.getSolveList().size());

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        assertEquals("Added a solve to the list: 8.141",
                iterator.next().getDescription());
        assertEquals("Added a solve to the list: 12.891",
                iterator.next().getDescription());
        assertEquals("Added a solve to the list: 32.89",
                iterator.next().getDescription());
    }

    @Test
    public void testClear() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        solveList.clear();
        assertEquals(0, solveList.getSolveList().size());

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertEquals("Cleared solve list.",
                iterator.next().getDescription());
    }

    @Test
    public void testRemove1() {
        solveList.add(s1);
        solveList.remove(0);
        assertEquals(0, solveList.getSolveList().size());

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        iterator.next();
        assertEquals("Removed solve number 0 from the list.",
                iterator.next().getDescription());
    }

    @Test
    public void testRemoveMultiple() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        solveList.remove(1);
        solveList.remove(1);
        assertEquals(s1, solveList.getSolveList().get(0));
        assertEquals(1, solveList.getSolveList().size());

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertEquals("Removed solve number 1 from the list.",
                iterator.next().getDescription());
        assertEquals("Removed solve number 1 from the list.",
                iterator.next().getDescription());
    }

    @Test
    public void testRemoveFromMiddle() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        solveList.remove(1);
        assertEquals(s1, solveList.getSolveList().get(0));
        assertEquals(s3, solveList.getSolveList().get(1));
        assertEquals(2, solveList.getSolveList().size());

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertEquals("Removed solve number 1 from the list.",
                iterator.next().getDescription());
    }

    @Test
    public void testListLatestSolvesEmpty() {
        assertEquals("", solveList.listLatestSolves(5));

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        assertEquals("Exported solve list.",
                iterator.next().getDescription());
    }

    @Test
    public void testListLatestSolvesAll() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        assertEquals("[3] 32.89\n" +
                "[2] 12.891\n" +
                "[1] 8.141", solveList.listLatestSolves(3));

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertEquals("Exported solve list.",
                iterator.next().getDescription());
    }

    @Test
    public void testListLatestSolvesPartial() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        assertEquals("[3] 32.89\n" +
                "[2] 12.891", solveList.listLatestSolves(2));

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertEquals("Exported solve list.",
                iterator.next().getDescription());
    }

    @Test
    public void testListLatestSolvesOvershoot() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        assertEquals("[3] 32.89\n" +
                "[2] 12.891\n" +
                "[1] 8.141", solveList.listLatestSolves(2000));

        Iterator<Event> iterator = el.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        assertEquals("Exported solve list.",
                iterator.next().getDescription());
    }

    @Test
    public void testCurrentAverageOf5() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        solveList.add(fastSolve);
        solveList.add(slowSolve);
        assertEquals((s1.getSolveTime() + s2.getSolveTime() + s3.getSolveTime()) / 3,
                solveList.currentAverageOfN(5));
    }

    @Test
    public void testCurrentAverageOf3() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        solveList.add(fastSolve);
        solveList.add(slowSolve);
        assertEquals(s3.getSolveTime(),
                solveList.currentAverageOfN(3));
    }

    @Test
    public void testCurrentSessionMeanOne() {
        solveList.add(s1);
        assertEquals(s1.getSolveTime(), solveList.currentSessionMean());
    }

    @Test
    public void testCurrentSessionMeanMultiple() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        assertEquals((s1.getSolveTime() + s2.getSolveTime() + s3.getSolveTime()) / 3,
                solveList.currentSessionMean());
    }

    @Test
    public void testCurrentFastestSolve() {
        solveList.add(s1);
        solveList.add(s2);
        solveList.add(s3);
        solveList.add(fastSolve);
        solveList.add(slowSolve);
        assertEquals(fastSolve, solveList.currentFastestSolve());
    }

    @Test
    public void testCurrentFastestSolveTie() {
        //Same times, different scrambles
        solveList.add(new Solve(10.5, "R2 U2 F"));
        solveList.add(new Solve(10.5, "B2 D2"));
        solveList.add(new Solve(10.5, "L' F U2 D'"));
        assertEquals("R2 U2 F", solveList.currentFastestSolve().getScrambleString()); //Produces first when tied
    }
}
