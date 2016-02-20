package org.usfirst.frc.team1076.udp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SensorData {
	enum FieldPosition { Right, Left; }
	private int port;
	private Channel receiver;
	private double heading;
	private double distance;
	private FieldPosition position;
	private JSONParser parser = new JSONParser();
	
	private double leftSideBack, rightSideBack, leftSideFront, rightSideFront;
	private double leftFront, rightFront;	
	
	public SensorData(int port) {
		this.port = port;
		receiver = new Channel(this.port);
	}
	
	public void interpretData() {
		UDPMessage latest;
		while ((latest = receiver.popLatestMessage()) != null) {
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(latest.getMessage());
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
			
			switch ((String) obj.get("sender")) {
			case "lidar":
				handleLidarMessage(obj);
				break;
			case "vision":
				handleVisionMessage(obj);
				break;
			case "sonar":
				handleSonarMessage(obj);
				break;
			default:
			}
		}
	}
	
	private void handleSonarMessage(JSONObject msg) {
		try {
			String type = (String) msg.get("message");
			if (!type.equals("ranges")) {
				System.err.println("Error, sonar message was \"" + type + "\" expecting \"ranges\"");
				// If the message type is wrong, we can't trust it to have all the attributes
				return;
			}
			
			leftSideBack = ((Number) msg.get("left side back")).doubleValue();
			leftSideFront = ((Number) msg.get("left side front")).doubleValue();
			rightSideBack = ((Number) msg.get("right side back")).doubleValue();
			rightSideFront = ((Number) msg.get("right side front")).doubleValue();
			leftFront = ((Number) msg.get("left front")).doubleValue();
			rightFront = ((Number) msg.get("right front")).doubleValue();
			System.out.println("Got the sonar data");
			System.out.println("Left side back: " + leftSideBack);
			System.out.println("Left side front: " + leftSideFront);
			System.out.println("Right side back: " + rightSideBack);
			System.out.println("Right side front: " + rightSideFront);
			System.out.println("Left front: " + leftFront);
			System.out.println("Right front: " + rightFront);
		} catch (Throwable e) {
			// TODO: Figure out what the correct exception is for missing JSON attributes
			e.printStackTrace();
		}
	}
	
	private void handleVisionMessage(JSONObject msg) {
		String status = (String) msg.get("status");
		double heading = ((Number) msg.get("heading")).doubleValue();
		double range = ((Number) msg.get("range")).doubleValue();
		switch (status) {
		case "left":
			if (position == FieldPosition.Right) set(heading, range);
			break;
		case "right":
			if (position == FieldPosition.Left) set(heading, range);
			break;
		case "ok":
			set(heading, range);
			break;
		default:
		}
	}
	
	private void handleLidarMessage(JSONObject msg) {
		String message = (String) msg.get("message");
		switch (message) {
		case "range and heading":
			double heading = ((Number) msg.get("heading")).doubleValue();
			double range = ((Number) msg.get("range")).doubleValue();
			this.heading = heading;
			this.distance = range;
			break;
		default:
		}
	}
	
	public void set(double h, double d) {
		this.heading = h;
		this.distance = d;
	}
	
	public double getHeading() {
		return heading;
	}
	
	public double getDistance() {
		return heading;
	}
	
	public Channel getChannel() {
		return receiver;
	}

	public double currentHeading() {
		return 0;
	}
}
