package org.usfirst.frc.team1076.udp;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDPMessage {
	private String message;
	private InetAddress IP;
	
	public UDPMessage(String data, InetAddress messageIP) {
		message = data;
		this.IP = messageIP;
	}

	public DatagramPacket sendPacket(int port) {
		return new DatagramPacket(message.getBytes(), message.length(),
				IP, port);
	}
	
	public InetAddress getIP() {
		return IP;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return IP.getHostAddress() + ": " + message;
	}
}