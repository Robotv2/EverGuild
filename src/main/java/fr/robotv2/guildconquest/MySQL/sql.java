package fr.robotv2.guildconquest.MySQL;

import fr.robotv2.guildconquest.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class sql {

    private Connection connection;

    public getter getter;

    public String host;
    public String port;
    public String database;
    public String username;
    public String password;

    public String ssl;

    public sql(main main) {
        getter = new getter(main);
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + ssl, username, password);
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
