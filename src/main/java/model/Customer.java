package model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//достает данные из json как объект
public class Customer {

    public static JSONObject getCustomer(String body) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject customer = (JSONObject) parser.parse(body);
        return customer;
    }
}
