package fr.robotv2.guildconquest.mysql;

import fr.robotv2.guildconquest.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sql {

    private main main;
    private Connection connection;

    private getter getter;

    public String host;
    public String port;
    public String database;
    public String username;
    public String password;

    public String ssl;

    public sql(main main) {
        this.main = main;
        getter = new getter(main);
    }

    public void initializeConnection(String host, String port, String database, String username, String password, String ssl) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.ssl = ssl;

        connect();
        if(isConnected()) {
            main.getLogger().info("§aConnection à la BDD réussit.");
        } else {
            main.getLogger().warning("§cImpossible de se connecter à la BDD.");
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() {
        if(!isConnected()) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + ssl, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if(!isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public Connection getConnection() {
        return connection;
    }

    public getter getGetter() { return getter; }
}
