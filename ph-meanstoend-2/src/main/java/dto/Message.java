package dto;

public class Message {
	private char op; // I or Q
	private int arg1;
	private int arg2;

	public Message(char op, int arg1, int arg2) {
		super();
		this.op = op;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}

	public char getOp() {
		return op;
	}

	public void setOp(char op) {
		this.op = op;
	}

	public int getArg1() {
		return arg1;
	}

	public void setArg1(int arg1) {
		this.arg1 = arg1;
	}

	public int getArg2() {
		return arg2;
	}

	public void setArg2(int arg2) {
		this.arg2 = arg2;
	}

	@Override
	public String toString() {
		return "Message [op=" + op + ", arg1=" + arg1 + ", arg2=" + arg2 + "]";
	}

}
