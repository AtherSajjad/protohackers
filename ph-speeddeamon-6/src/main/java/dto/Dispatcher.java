package dto;

import java.util.Arrays;

public class Dispatcher {
	byte numRoads;
	short[] roads;

	public Dispatcher(byte numRoads, short[] roads) {
		super();
		this.numRoads = numRoads;
		this.roads = roads;
	}

	public byte getNumRoads() {
		return numRoads;
	}

	public void setNumRoads(byte numRoads) {
		this.numRoads = numRoads;
	}

	public short[] getRoads() {
		return roads;
	}

	public void setRoads(short[] roads) {
		this.roads = roads;
	}

	@Override
	public String toString() {
		return "Dispatcher [numRoads=" + numRoads + ", roads=" + Arrays.toString(roads) + "]";
	}

}
