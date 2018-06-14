package fr.yoannroche;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Connect {
	private static String url = "jdbc:postgresql://localhost:5432/Ecole";
	private static String user = "postgres";
	private static String passwd = "Yocorps17";
	private static Connection connect;

	 public static Connection getInstance(){
		    if(connect == null){
		      try {
		        connect = DriverManager.getConnection(url, user, passwd);
		      } catch (SQLException e) {
		        JOptionPane.showMessageDialog(null, e.getMessage(), "ERREUR DE CONNEXION ! ", JOptionPane.ERROR_MESSAGE);
		      }
		    }		
		    return connect;	
		  }
		}

