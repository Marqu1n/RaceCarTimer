package persistance.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Lap {
	private int id;
	private String lapTime;
	private int carId;
	
	public Lap() {}
	
	public Lap(String lapTime, Car car) {
		setLapTime(lapTime);
		setCarId(car.getId());
	}
	
	public Lap(int id, String lapTime, int carId) {
		setId(id);
		setLapTime(lapTime);
		setCarId(carId);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLapTime() {
		return lapTime;
	}

	public void setLapTime(String lapTime) {
		try {
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
			if (LocalTime.parse(lapTime, timeFormatter).format(timeFormatter).equals(lapTime)) {
				this.lapTime = lapTime;
			};
		} catch (DateTimeParseException e) {
			this.lapTime = "00:00:00.000";
			throw new RuntimeException(e);
		}

	}
	
	public int getCarId() {
		return carId;
	}

	public void setCarId(int car_id) {
		this.carId = car_id;
	}

	public String toString() {
		if (this.lapTime != null) {
			String[] splittedLapTime = this.lapTime.split(":");
			return String.join(":", Arrays.copyOfRange(splittedLapTime, 1, splittedLapTime.length));
		}

		return "00:00.000";
	}
}
