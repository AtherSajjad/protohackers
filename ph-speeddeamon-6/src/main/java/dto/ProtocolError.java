package dto;

public class ProtocolError {
	String message;

	public ProtocolError(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ProtocolError [message=" + message + "]";
	}

}
