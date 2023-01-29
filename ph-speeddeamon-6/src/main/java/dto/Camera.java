package dto;

public class Camera {
	short road;
	short mile;
	short limit;

	public Camera(short road, short mile, short limit) {
		super();
		this.road = road;
		this.mile = mile;
		this.limit = limit;
	}

	public short getRoad() {
		return road;
	}

	public void setRoad(short road) {
		this.road = road;
	}

	public short getMile() {
		return mile;
	}

	public void setMile(short mile) {
		this.mile = mile;
	}

	public short getLimit() {
		return limit;
	}

	public void setLimit(short limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "Camera [road=" + road + ", mile=" + mile + ", limit=" + limit + "]";
	}

}
