package persistance.model;

public class Car {
	private int id;
	private String name;
	
	public Car() {}
	
	public Car(int id) {
		setId(id);
	}
	
	public Car(String name) {
		setName(name);
	}
	
	public Car(int id, String name) {
		setId(id);
		setName(name);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name.length() <= 80) this.name = name;
		else throw new RuntimeException("Car's name is too long (max. of 80 chars)");
	}
}
