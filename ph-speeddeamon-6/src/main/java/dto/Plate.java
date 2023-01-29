package dto;

public class Plate {
	String plate;
	int timestamp;

	public Plate(String plate, int timestamp) {
		super();
		this.plate = plate;
		this.timestamp = timestamp;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Plate [plate=" + plate + ", timestamp=" + timestamp + "]";
	}

	
}
