package net.baragon.server;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class MFBServer {
    public PrintStream out;
    private String dbname;
    private Connection databaseConnection;
    private Properties properties;
    private static final String SERVER_PORT_STRING = "server_port";
    private static final String DATABASE_IP_STRING = "database_ip";
    private static final String DATABASE_PORT_STRING = "database_port";
    private static final String DATABASE_USER_STRING = "database_user";
    private static final String DATABASE_PASSWORD_STRING = "database_password";
    private static final String DATABASE_NAME_STRING = "database_name";

    public static void main(String[] args) {
        new MFBServer().start();
    }

    public MFBServer() {
        out = System.out;
    }

    public void start() {
        properties = LoadProperties();
        if (properties == null) return;
        if (!SetupDatabaseConnection()) return;
        HttpServer server = null;
        try {
            int port = Integer.valueOf(properties.getProperty(SERVER_PORT_STRING));
            if ((port < 1) || (port > 65535)) {
                out.println("Bad server_port property: must be 1-65535");
            }
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (NumberFormatException e) {
            out.println("Bad server_port property: check configuration file");
            return;
        } catch (IOException e) {
            out.println(e.getMessage());
        }
        server.createContext("/addfood", new AddFoodHttpServer(dbname, databaseConnection));
        server.createContext("/addserving", new AddServingHttpServer(dbname, databaseConnection));
        server.createContext("/find", new FindFoodHttpServer(dbname, databaseConnection));
        server.setExecutor(null);
        server.start();
        out.println("Server started");
    }

    public boolean SetupDatabaseConnection() {
        try {
            String ip = properties.getProperty(DATABASE_IP_STRING);
            int port = Integer.valueOf(properties.getProperty(DATABASE_PORT_STRING));
            if ((port < 1) || (port > 65535)) {
                out.println("Bad database_port property: must be 1-65535");
            }
            databaseConnection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/",
                    properties.getProperty(DATABASE_USER_STRING),
                    properties.getProperty(DATABASE_PASSWORD_STRING)
            );
        } catch (NumberFormatException e) {
            out.println("Bad database_port property: check configuration file");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        dbname = properties.getProperty(DATABASE_NAME_STRING);
        try {
            databaseConnection.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbname + "`");
            databaseConnection.setCatalog(dbname);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(MFBServer.class.getResourceAsStream("db.sql")));
        StringBuilder sqlBuilder = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null)
                sqlBuilder.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sql = sqlBuilder.toString();
        String[] statements = sql.split(";");
        for (String statement : statements)
            try {
                databaseConnection.createStatement().execute(statement);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        return true;
    }

    public Properties GetDefaultProperties() {
        Properties defaultProperties = new Properties();
        defaultProperties.put(DATABASE_IP_STRING, "");
        defaultProperties.put(DATABASE_USER_STRING, "");
        defaultProperties.put(DATABASE_PASSWORD_STRING, "");
        defaultProperties.put(DATABASE_PORT_STRING, "");
        defaultProperties.put(SERVER_PORT_STRING, "");
        defaultProperties.put(DATABASE_NAME_STRING, "");
        return defaultProperties;
    }

    public Properties LoadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("server.cfg"));
        } catch (FileNotFoundException e) {
            File configurationFile = new File("server.cfg");
            try {
                configurationFile.createNewFile();
                GetDefaultProperties().store(new FileOutputStream(configurationFile), "MyFitnessBuddy server configuration file");
                out.println("Please, setup program properties in '" + configurationFile.getAbsolutePath() + "'");
            } catch (IOException e1) {
                out.println("Failed to create configuration file");
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
