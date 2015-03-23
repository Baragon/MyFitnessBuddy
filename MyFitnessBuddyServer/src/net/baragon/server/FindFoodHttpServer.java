package net.baragon.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public class FindFoodHttpServer extends AHttpServer {
    public static final int RESULT_COUNT = 10;

    public FindFoodHttpServer(String dbname, Connection databaseConnection) {
        super(dbname, databaseConnection);
    }

    @Override
    public String handleRequest(HashMap<String, String> parameters) {
        String name = parameters.containsKey("name") ? parameters.get("name") : null;
        if (name == null) return "Error : not enough parameters";
        try {
            PreparedStatement searchStatement = databaseConnection.prepareStatement(
                    "SELECT * FROM " + dbname + ".foods WHERE name LIKE ? LIMIT ?"
            );
            searchStatement.setString(1, "%" + name + "%");
            searchStatement.setInt(2, RESULT_COUNT);
            ResultSet searchResult = searchStatement.executeQuery();
            JSONObject json = new JSONObject();
            JSONArray jarray = new JSONArray();
            while (searchResult.next()) {
                JSONObject food = RecentFoodsHttpHandler.ResultSetRowToJSON(searchResult);
                JSONArray servingsArray = new JSONArray();
                PreparedStatement servingsStatement = databaseConnection.prepareStatement("SELECT id,name,gramms FROM " + dbname + ".serving WHERE food=?");
                servingsStatement.setInt(1, Integer.valueOf((String) food.get("id")));
                servingsStatement.executeQuery();
                ResultSet servingsSet = servingsStatement.getResultSet();
                while (servingsSet.next()) {
                    JSONObject serving = RecentFoodsHttpHandler.ResultSetRowToJSON(servingsSet);
                    servingsArray.add(serving);
                }
                food.put("servings", servingsArray);
                jarray.add(food);
            }
            json.put("foods", jarray);
            return json.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
