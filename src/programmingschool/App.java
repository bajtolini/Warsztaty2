package programmingschool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import programmingschool.model.User;

public class App {
	
	public static void main(String[] args) {
		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/programming_school?useSSL=false",
					"root", "coderslab");
			User user = new User();
			
//			toString test
//			user = new User("user1","email@email.com","1234");
//			System.out.println(user.toString());
			
//			save to MySQL test
//			user.save(conn);
			
//			load from MySQL test
			user = user.loadById(conn, 1l);
			System.out.println("___________\n" + user.toString());
			
			conn.close();
		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
			System.err.println(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
