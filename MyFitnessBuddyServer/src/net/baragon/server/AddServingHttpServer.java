package net.baragon.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AddServingHttpServer extends AHttpServer {
    public AddServingHttpServer(String dbname, Connection databaseConnection) {
        super(dbname, databaseConnection);
    }

    @Override
    public String handleRequest(HashMap<String, String> parameters) {
        String foodid = parameters.get("foodid");
        String name = parameters.get("name");
        String gramms = parameters.get("g");
        try {
            PreparedStatement addServingStatement = databaseConnection.prepareStatement(
                    "INSERT INTO " + dbname + ".serving(name,food,gramms) VALUES(?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS
            );
            addServingStatement.setString(1, name);
            addServingStatement.setInt(2, Integer.valueOf(foodid));
            addServingStatement.setFloat(3, Float.valueOf(gramms));
            addServingStatement.executeUpdate();
            ResultSet result = addServingStatement.getGeneratedKeys();
            result.next();
            int servingID = result.getInt(1);
            return String.valueOf(servingID);
        } catch (SQLException e) {
            e.printStackTrace();
            return "[Error]" + e.toString();
        }

    }
}
