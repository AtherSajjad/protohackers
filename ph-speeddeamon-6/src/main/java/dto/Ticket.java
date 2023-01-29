package dto;

public class Ticket {

	String plate;
	short road;
	short mile1;
	int timestamp1;
	short mile2;
	int timestamp2;
	short speed;

	public Ticket(String plate, short road, short mile1, int timestamp1, short mile2, int timestamp2, short speed) {
		super();
		this.plate = plate;
		this.road = road;
		this.mile1 = mile1;
		this.timestamp1 = timestamp1;
		this.mile2 = mile2;
		this.timestamp2 = timestamp2;
		this.speed = speed;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public short getRoad() {
		return road;
	}

	public void setRoad(short road) {
		this.road = road;
	}

	public short getMile1() {
		return mile1;
	}

	public void setMile1(short mile1) {
		this.mile1 = mile1;
	}

	public int getTimestamp1() {
		return timestamp1;
	}

	public void setTimestamp1(int timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public short getMile2() {
		return mile2;
	}

	public void setMile2(short mile2) {
		this.mile2 = mile2;
	}

	public int getTimestamp2() {
		return timestamp2;
	}

	public void setTimestamp2(int timestamp2) {
		this.timestamp2 = timestamp2;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	@Override
	public String toString() {
		return "Ticket [plate=" + plate + ", road=" + road + ", mile1=" + mile1 + ", timestamp1=" + timestamp1
				+ ", mile2=" + mile2 + ", timestamp2=" + timestamp2 + ", speed=" + speed + "]";
	}

}
