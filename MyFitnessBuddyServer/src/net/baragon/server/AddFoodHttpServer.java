package net.baragon.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AddFoodHttpServer extends AHttpServer {
    public AddFoodHttpServer(String dbname, Connection databaseConnection) {
        super(dbname, databaseConnection);
    }

    @Override
    public String handleRequest(HashMap<String, String> parameters) {
        String name = parameters.get("name");
        String cal = parameters.get("cal");
        String protein = parameters.get("protein");
        String carbs = parameters.get("carbs");
        String fat = parameters.get("fat");
        try {
            PreparedStatement addFood = databaseConnection.prepareStatement(
                    "INSERT INTO " + dbname + ".foods(name,cal,protein,carbs,fat) VALUES(?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS
            );
            addFood.setString(1, name);
            addFood.setFloat(2, Float.valueOf(cal));
            addFood.setFloat(3, Float.valueOf(protein));
            addFood.setFloat(4, Float.valueOf(carbs));
            addFood.setFloat(5, Float.valueOf(fat));
            addFood.executeUpdate();
            ResultSet result = addFood.getGeneratedKeys();
            result.next();
            int foodid = result.getInt(1);

            PreparedStatement newServing = databaseConnection.prepareStatement(
                    "INSERT INTO " + dbname + ".serving(name,food,gramms) VALUES(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS
            );
            newServing.setString(1, "100g");
            newServing.setInt(2, foodid);
            newServing.setFloat(3, 100);
            newServing.executeUpdate();
            ResultSet servingIDSet = addFood.getGeneratedKeys();
            servingIDSet.next();
            int servingID = servingIDSet.getInt(1);
            return String.valueOf(foodid) + ":" + servingID;
        } catch (SQLException e) {
            e.printStackTrace();
            return "[Error] " + e.toString();
        }

    }
}
