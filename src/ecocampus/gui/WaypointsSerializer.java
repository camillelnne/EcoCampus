package ecocampus.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WaypointsSerializer {
    public static void saveWaypoints(ObservableList<Waypoint> waypoints) throws IOException {

        var mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("waypoints.json"))) {
            mapper.writeValue(bw, waypoints);
        }
    }

    public static ObservableList<Waypoint> loadWaypoints() throws IOException {
        //read json file data to String
        byte[] jsonData = Files.readAllBytes(Paths.get("waypoints.json"));

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        //convert json string to object
        TypeReference<List<Waypoint>> mapType = new TypeReference<>() {};
        List<Waypoint> waypoints = objectMapper.readValue(jsonData, mapType);
        return FXCollections.observableArrayList(waypoints);
    }

}