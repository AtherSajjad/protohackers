package dto;

public class WantHeartBeat {
	int interval; // in decisecond, so 25 is 25/10 = 2.5

	public WantHeartBeat(int interval) {
		super();
		this.interval = interval; // convert to seconds and store
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public String toString() {
		return "WantHeartBeat [interval=" + interval + "]";
	}

}
