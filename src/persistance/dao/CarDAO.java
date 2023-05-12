package persistance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.ConnectionFactory;
import persistance.model.Car;

public class CarDAO {
	public static Car getById(Car car) {
		String sql = "SELECT * FROM `car` WHERE `id` = ?;";
		
		Connection conn = ConnectionFactory.getConnection();
		if (conn != null) {
			try { 
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, car.getId());
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					return (new Car(rs.getInt(1), rs.getString(2)));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static Car getByName(Car car) {
		String sql = "SELECT * FROM `car` WHERE `name` = ?;";
		
		Connection conn = ConnectionFactory.getConnection();
		if (conn != null) {
			try { 
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, car.getName());
				ResultSet rs = stmt.executeQuery();
				
				if (rs.next()) {
					return (new Car(rs.getInt(1), rs.getString(2)));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static ArrayList<Car> getAll() {
		String sql = "SELECT * FROM `car`;";
		
		Connection conn = ConnectionFactory.getConnection();
		if (conn != null) {
			try {
				ArrayList<Car> cars = new ArrayList<>();
				ResultSet rs = conn.createStatement().executeQuery(sql);
				while (rs.next()) {
					cars.add(
							new Car(rs.getInt(1), rs.getString(2))
					);
				}
				
				if (cars.size() >= 1) return cars;
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return new ArrayList<>();
	}
	
	public static void insert(Car car) {
		String sql = "INSERT INTO `car` (`name`) VALUES (?);";
		
		Connection conn = ConnectionFactory.getConnection();
		if (conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, car.getName());
				stmt.executeUpdate();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int deleteById(Car car) {
		String sql = "DELETE FROM `car` WHERE `id` = ?";
		
		Connection conn = ConnectionFactory.getConnection();
		if (conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, car.getId());
				int affectedRows = stmt.executeUpdate();
				
				conn.close();
				return affectedRows;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 0;
	}
}
