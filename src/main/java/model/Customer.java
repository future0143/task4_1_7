package model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//достает данные из json как объект
public class Customer {

    public static JSONObject getCustomer(String body) {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(body);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
