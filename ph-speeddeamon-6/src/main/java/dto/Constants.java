package dto;

public class Constants {
	public final static byte ERROR = 0x10; // Server to client
	public final static byte HEARTBEAT = 0x41; // Servewf to client

	public final static byte PLATE = 0x20; // client to server
	public final static byte TICKET = 0x21; // client to server
	public final static byte WANT_HEARTBEAT = 0x40; // client to server

	// This will be the initial message to receive if no identity assigned
	public final static byte CAMERA = (byte) 0x80; // client to server
	public final static byte DISPATCHER = (byte) 0x81; // client to server

}
