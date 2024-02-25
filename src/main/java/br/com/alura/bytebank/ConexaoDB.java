package br.com.alura.bytebank;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoDB {
	
	public static void main(String... x) {
		try {
			String url = "jdbc:mysql://0.0.0.0:3306/byte_bank?user=root&password=cocobosta";
			//String username = "root";
			//String password = "meu_password";

			Connection conn = DriverManager.getConnection(url);//,username, password")/
			System.out.println("Conexao ok!!!!");
			conn.close();
			
		}catch(Exception e) {
			System.out.println("Errro de conexão!!!!"+e.getMessage());
		}
	}

}
