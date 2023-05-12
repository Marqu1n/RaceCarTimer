package persistance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connection.ConnectionFactory;
import persistance.model.Car;
import persistance.model.Lap;

public class LapDAO {
	public static ArrayList<Lap> getByCar(Car car) {
		String sql = "SELECT * FROM `lap` WHERE `car_id` = ?;";
		
		Connection conn = ConnectionFactory.getConnection();
		if (conn != null) {
			ArrayList<Lap> laps = new ArrayList<>();
			try {
				
				PreparedStatement stmt = conn.prepareStatement(sql);	
				stmt.setInt(1, car.getId());
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					laps.add(
							new Lap(rs.getInt(1), rs.getString(2), rs.getInt(3))
					);
				}
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return laps;
		}
		
		return new ArrayList<>();
	}
	
	public static void insert(Lap lap) {
		String sql = "INSERT INTO `lap` (`lap_time`, `car_id`) VALUES (?, ?);";
		
		Connection conn = ConnectionFactory.getConnection();
		if (conn != null) {
			PreparedStatement stmt;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, lap.getLapTime());
				stmt.setInt(2, lap.getCarId());
				stmt.executeUpdate();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteAllByCar(Car car) {
		String sql = "DELETE FROM `lap` WHERE `car_id` = ?;";
		
		Connection conn = ConnectionFactory.getConnection();
		if (conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setInt(1, car.getId());
				stmt.executeUpdate();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
	}
}
