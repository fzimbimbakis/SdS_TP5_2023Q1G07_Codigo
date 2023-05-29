package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonConfigReader {

    private final Double radius;
    private final Double mass;
    private final Double maxX;
    private final Double maxY;
    private final Double whiteX;
    private final Double whiteY;
    private final Double whiteV;
    private final Double triangleX;
    private final Double triangleY;
    private final Double minEpsilon;
    private final Double maxEpsilon;
    private final Double dt;
    private final Double maxTime;

    public JsonConfigReader(String jsonConfigFilePath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(jsonConfigFilePath)) {
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            this.radius = Double.parseDouble(jsonObject.get("radius").toString());
            this.mass = Double.parseDouble(jsonObject.get("mass").toString());
            this.whiteV = Double.parseDouble(jsonObject.get("white_v").toString());
            this.whiteX = Double.parseDouble(jsonObject.get("white_x").toString());
            this.whiteY = Double.parseDouble(jsonObject.get("white_y").toString());
            this.maxX = Double.parseDouble(jsonObject.get("max_x").toString());
            this.maxY = Double.parseDouble(jsonObject.get("max_y").toString());
            this.triangleX = Double.parseDouble(jsonObject.get("triangle_x").toString());
            this.triangleY = Double.parseDouble(jsonObject.get("triangle_y").toString());
            this.minEpsilon = Double.parseDouble(jsonObject.get("min_epsilon").toString());
            this.maxEpsilon = Double.parseDouble(jsonObject.get("max_epsilon").toString());
            this.dt = Double.parseDouble(jsonObject.get("dt").toString());
            this.maxTime = Double.parseDouble(jsonObject.get("max_t").toString());

        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error reading parameters from config.json");
        }
    }

    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public Double getWhiteX() {
        return whiteX;
    }

    public Double getWhiteY() {
        return whiteY;
    }

    public Double getWhiteV() {
        return whiteV;
    }

    public Double getMaxX(){
        return maxX;
    }

    public Double getMaxY(){
        return maxY;
    }


    public Double getTriangleX() {
        return triangleX;
    }

    public Double getTriangleY() {
        return triangleY;
    }

    public Double getMinEpsilon() {
        return minEpsilon;
    }

    public Double getMaxEpsilon() {
        return maxEpsilon;
    }

    public Double getDt() {
        return dt;
    }

    public Double getMaxTime() {
        return maxTime;
    }
}
