package persistence;

import model.Solve;
import model.SolveList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Writes a SolveList to JSON file
public class JsonWriter {
    private static final int TAB = 4; // Indentation level for JSON pretty-printing

    private String location;
    private PrintWriter writer;

    // EFFECTS: Constructs a JsonWriter with given target
    public JsonWriter(String location) {
        this.location = location;
    }

    // Based on code from JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(location);
    }

    // Based on code from JsonSerializationDemo
    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // EFFECTS: Writes JSON representation of solveList to this.location
    public void writeSolveList(SolveList solveList) {
        JSONArray json = new JSONArray();
        JSONObject sub;
        for (Solve solve : solveList.getSolveList()) {
            sub = new JSONObject();
            sub.put("time", solve.getSolveTime());
            sub.put("scramble", solve.getScrambleString());
            json.put(sub);
        }
        writeString(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes given string to this.location
    private void writeString(String string) {
        writer.print(string);
    }
}
