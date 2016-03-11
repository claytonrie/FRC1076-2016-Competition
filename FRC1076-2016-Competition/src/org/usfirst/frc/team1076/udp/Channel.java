package org.usfirst.frc.team1076.udp;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class Channel implements IChannel {
	private int port;
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
				while (!Thread.interrupted()) {
					if (!this.shouldReceive()) { continue; }
					DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
					try {
						serverSocket.receive(receivePacket);
					} catch (Exception e) {
						e.printStackTrace();
					}
					String data = new String(receivePacket.getData());
					data = data.substring(0, receivePacket.getLength());
					System.out.println(data);
					containerChannel.putMessage(new UDPMessage(data, receivePacket.getAddress()));
				}
				serverSocket.close();
			}
		}.init(this));
		this.receiveWorker.start();
	}
	
	@Override
	public UDPMessage popLatestMessage() {
		if(queue.isEmpty()) {
			return null;
		}
		return queue.poll();
	}

	public void putMessage(UDPMessage msg) {
		queue.add(msg);
	}
	
	@Override
	public boolean hasMessage() {
		return !queue.isEmpty();
	}
	
	@Override
	public void sendMessage(String message, InetAddress target) throws IOException {
		serverSocket.send(new UDPMessage(message, target).sendPacket(this.port));
	}
	
	public void close() {
		receiveWorker.interrupt();
	}
	
	protected boolean getReceiveStatus() { return doesReceive; }
	public void setReceiveStatus(boolean receive) { doesReceive = receive; }
}
