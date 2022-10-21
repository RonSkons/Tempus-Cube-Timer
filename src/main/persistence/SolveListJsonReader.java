package persistence;

import model.Solve;
import model.SolveList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Parses JSON data from files to SolveList objects
public class SolveListJsonReader {
    private String location;

    // EFFECTS: constructs a JSON reader with given JSON file
    public SolveListJsonReader(String location) {
        this.location = location;
    }

    // EFFECTS: parses source JSON data as SolveList
    // Throws IOException if there are problems reading file
    public SolveList getSavedData() throws IOException {
        SolveList result = new SolveList();
        JSONArray json = new JSONArray(readFile(location));
        for (Object obj : json) {
            JSONObject jsonObj = (JSONObject) obj;
            result.add(new Solve(jsonObj.getDouble("time"), jsonObj.getString("scramble")));
        }
        return result;
    }

    // Based on code from JsonSerializationDemo
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }
}
