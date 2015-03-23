package net.baragon.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;


public abstract class AHttpServer implements HttpHandler{
    public PrintStream out = System.out;
    protected String dbname;
    protected Connection databaseConnection;
    public AHttpServer(String dbname,Connection databaseConnection){
        this.dbname=dbname;
        this.databaseConnection=databaseConnection;
    }
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().put("Content-Type", Arrays.asList("text/html; charset=UTF-8"));
        String query = httpExchange.getRequestURI().getQuery();
        HashMap<String,String> map = queryToMap(query);
        String response = handleRequest(map);
        if(response!=null) {
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream out = httpExchange.getResponseBody();
            out.write(response.getBytes());
            out.close();
        }
        httpExchange.close();
    }
    private HashMap<String,String> queryToMap(String query)
    {
        HashMap<String,String> map = new HashMap<String, String>();
        if (query!=null) {
            String[] parameters = query.split("&");
            for (String s : parameters) {
                String[] pair = s.split("=");
                map.put(pair[0].toLowerCase(), pair[1]);
            }
        }
        return map;
    }
    public abstract String handleRequest(HashMap<String, String> parameters);
}
