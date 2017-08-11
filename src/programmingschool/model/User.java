package programmingschool.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class User {
	
	private static final int UNSAVED_ID = 0;
	private static final String TABLE = "users";
	
	private Long id = new Long(UNSAVED_ID);
	private String username;
	private String email;
	private String password;
	private Integer groupId = new Integer(0);
	
	public User() {
		super();
	}

	public User(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Id       : " + this.getId() +
				"\nUser     : " + this.getUsername() + 
				"\nEmail    : " + this.getEmail() + 
				"\nPassword : " + this.getPassword() + 
				"\nGroupId  : " + this.getGroupId();
	}
	
	public void save(Connection conn) throws SQLException, com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException {
		if (this.id == UNSAVED_ID) {
			final String sql = "INSERT INTO " + TABLE + " VALUES (default,?,?,?);";
			final String[] generatedColumns = {"id"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.username);
			ps.setString(2, this.email);
			ps.setString(3, this.password);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getLong(1);
			}
			rs.close();
			ps.close();
		}
	}
	
	public User loadById(Connection conn, final Long id) throws SQLException {
		User user = null;
		final String sql = "SELECT username, email, password FROM "
				+ TABLE + " WHERE id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			String username = rs.getString("username");
			String email = rs.getString("email");
			String password = rs.getString("password");
			user = new User(username, email, password);
			user.id = id;
		}
		
		rs.close();
		ps.close();
		return user;
	}

}
