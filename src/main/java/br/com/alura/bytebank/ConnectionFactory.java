package br.com.alura.bytebank;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public Connection recuperarConexao(){
        try {
            return createDataSource().getConnection();

        }catch(Exception e) {
            //System.out.println("Errro de conexão!!!!"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource createDataSource(){
        HikariConfig config = new HikariConfig();
        String url = "jdbc:mysql://0.0.0.0:3306/byte_bank?user=root&password=cocobosta";
        config.setJdbcUrl("jdbc:mysql://0.0.0.0:3306/byte_bank");
        config.setUsername("root");
        config.setPassword("cocobosta");
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }
}
