package org.usfirst.frc.team1076.udp;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class Channel {
	private int port;
	private InetAddress receiverIP;
	private DatagramSocket serverSocket;
	private Thread receiveWorker;
	private boolean doesReceive = true;
	private Queue<UDPMessage> queue;
	
	public Channel(int port) {
		this.queue = new LinkedList<UDPMessage>();
		this.port = port;
		try {
			this.serverSocket = new DatagramSocket(port);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		this.receiveWorker = new Thread(new Runnable() {
			private Channel containerChannel;
			
			private boolean shouldReceive() {
				return this.containerChannel.getReceiveStatus();
			}
			
			public Runnable init(Channel containerChannel) {
				this.containerChannel = containerChannel;
				return this;
			}
			
			public void run() {
				while (true) {
					if (!this.shouldReceive()) {
						continue;
					}
					DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
					try {
						serverSocket.receive(receivePacket);
						containerChannel.setIP(receivePacket.getAddress());
					} catch (Exception e) {
						e.printStackTrace();
					}
					String data = new String(receivePacket.getData());
					data = data.substring(0, receivePacket.getLength());
					containerChannel.putMessage(new UDPMessage(data, receivePacket.getAddress()));
				}
			}
		}.init(this));
		this.receiveWorker.start();
	}
	
	public InetAddress getIP() {
		return this.receiverIP;
	}
	
	public void setIP(InetAddress newIP) {
		this.receiverIP = newIP;
	}
	
	protected boolean getReceiveStatus() {
		return doesReceive;
	}
	
	public void setReceiveStatus(boolean receive) {
		doesReceive = receive;
	}
	
	public UDPMessage popLatestMessage() {
		if(queue.isEmpty()) {
			return null;
		}
		return queue.poll();
	}

	public void putMessage(UDPMessage str) {
		queue.add(str);
	}
	
	public void sendMessage(String message) {
		try {
			serverSocket.send(new UDPMessage(message, this.receiverIP).sendPacket(this.port));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		serverSocket.close();
	}
}