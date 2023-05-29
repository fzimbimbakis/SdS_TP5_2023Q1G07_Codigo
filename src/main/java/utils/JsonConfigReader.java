package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonConfigReader {

    private final Double mass;
    private final Double W;
    private final Double L;
    private final Double dt;
    private final Double maxTime;
    private final Integer N;

    public JsonConfigReader(String jsonConfigFilePath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(jsonConfigFilePath)) {
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;

            this.mass = Double.parseDouble(jsonObject.get("mass").toString());
            this.W = Double.parseDouble(jsonObject.get("W").toString());
            this.L = Double.parseDouble(jsonObject.get("L").toString());
            this.dt = Double.parseDouble(jsonObject.get("dt").toString());
            this.maxTime = Double.parseDouble(jsonObject.get("max_t").toString());
            this.N = Integer.parseInt(jsonObject.get("N").toString());

        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error reading parameters from config.json");
        }
    }


    public Double getMass() {
        return mass;
    }

    public Integer getN() {
        return N;
    }

    public Double getW() {
        return W;
    }

    public Double getL() {
        return L;
    }

    public Double getDt() {
        return dt;
    }

    public Double getMaxTime() {
        return maxTime;
    }
}
