package org.usfirst.frc.team1076.udp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SensorData {
	public enum FieldPosition { Right, Left; }
	private IChannel receiver;
	private double heading;
	private double distance;
	private FieldPosition position;
	private JSONParser parser = new JSONParser();
	
	private double leftSideBack, rightSideBack, leftSideFront, rightSideFront;
	private double leftFront, rightFront;	
	
	public SensorData(IChannel channel, FieldPosition position) {
		this.position = position;
		receiver = channel;
	}
	
	public void interpretData() {
		while (receiver.hasMessage()) {
			UDPMessage latest = receiver.popLatestMessage();
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(latest.getMessage());
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
			
			switch (((String) obj.get("sender")).toLowerCase()) {
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
		} catch (Throwable e) {
			// TODO: Figure out what the correct exception is for missing JSON attributes
			e.printStackTrace();
		}
	}
	
	private void handleVisionMessage(JSONObject msg) {
		String status = (String) msg.get("status");
		double heading = ((Number) msg.get("heading")).doubleValue();
		double range = ((Number) msg.get("range")).doubleValue();
		switch (status.toLowerCase()) {
		case "left":
			if (position == FieldPosition.Left) {
				set(heading, range);
			}
			break;
		case "right":
			if (position == FieldPosition.Right) {
				set(heading, range);
			}
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
	
	public FieldPosition getFieldPosition() { return position; }
	public void setFieldPosition(FieldPosition pos) { position = pos; }
	
	public double getHeading() { return heading; }
	public double getDistance() { return distance; }
	public IChannel getChannel() { return receiver; }
	public double getLeftSideBack() { return leftSideBack; }
	public double getRightSideBack() { return rightSideBack; }
	public double getLeftSideFront() { return leftSideFront; }
	public double getRightSideFront() { return rightSideFront; }
	public double getLeftFront() { return leftFront; }
	public double getRightFront() { return rightFront; }
}
