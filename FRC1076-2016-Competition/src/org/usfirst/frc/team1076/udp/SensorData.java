package org.usfirst.frc.team1076.udp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.usfirst.frc.team1076.robot.sensors.IGyro;

public class SensorData implements ISensorData {
	public enum FieldPosition { Right, Left; }
	
	private IChannel receiver;
	private double visionHeading, visionRange;
	private double lidarHeading, lidarRange;
	private FieldPosition position;
	private JSONParser parser = new JSONParser();
	private IGyro gyro;
	
	private double lidarRpm = 250;
	private double leftSideBack, rightSideBack, leftSideFront, rightSideFront;
	private double leftFront, rightFront;	
	
	public SensorData(IChannel channel, FieldPosition position, IGyro gyro) {
		this.gyro = gyro; // Should we change this to be a different method?
		this.position = position;
		receiver = channel;
	}

	@Override
	public void interpretData() {
		while (receiver.hasMessage()) {
			UDPMessage latest = receiver.popLatestMessage();
			System.out.println(latest.getMessage());
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(latest.getMessage());
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
			System.out.println("Object: " + obj);
			
			String sender = (String) obj.get("sender");
			switch (sender.toLowerCase()) {
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
				System.err.println("Error, unexpected message sender \"" + sender + "\"");
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
		String message = (String) msg.get("message");
		switch (message.toLowerCase()) {
		case "heading and range":
			String status = (String) msg.get("status");
			double heading = ((Number) msg.get("heading")).doubleValue();
			double range = ((Number) msg.get("range")).doubleValue();
			switch (status) {
			case "left":
				if (position == FieldPosition.Left) {
					setVision(heading, range);
				}
				break;
			case "right":
				if (position == FieldPosition.Right) {
					setVision(heading, range);
				}
				break;
			case "ok":
				setVision(heading, range);
				break;
			default:
			}
			break;
		default:
			System.err.println("Error, unexpected vision message \"" + message + "\"");
		}
	}
	
	private void handleLidarMessage(JSONObject msg) {
		double heading, range;
		String message = (String) msg.get("message");
		// TODO: Handle errors more specifically
		if (msg.get("status").equals("ok")) {
			System.err.println("Error: " + msg);
			return;
		}
		switch (message.toLowerCase()) {
		case "wall":
			heading = ((Number) msg.get("heading")).doubleValue();
			range = ((Number) msg.get("range")).doubleValue();
			this.lidarHeading = heading;
			this.lidarRange = range;
			break;
		case "range at heading":
			heading = ((Number) msg.get("heading")).doubleValue();
			range = ((Number) msg.get("range")).doubleValue();
			System.out.println("Range " + range + " at " + heading);
		case "periodic":
			double rpm = ((Number) msg.get("rpm")).doubleValue();
			this.lidarRpm = rpm;
			break;
		default:
			System.err.println("Error, unexpected LIDAR message \"" + message + "\"");
		}
	}
	
	@Override
	public void setVision(double h, double r) {
		this.visionHeading = h;
		this.visionRange = r;
	}
	
	@Override
	public void setLidar(double h, double r) {
		this.lidarHeading = h;
		this.lidarRange = r;
	}
	
	@Override public IGyro getGyro() { return gyro; }
	
	@Override public FieldPosition getFieldPosition() { return position; }
	@Override public void setFieldPosition(FieldPosition pos) { position = pos; }
	
	@Override public double getLidarRpm() { return lidarRpm; }
	@Override public double getLidarHeading() { return lidarHeading; }
	@Override public double getLidarRange() { return lidarRange; }
	@Override public double getVisionHeading() { return visionHeading; }
	@Override public double getVisionRange() { return visionRange; }
	@Override public IChannel getChannel() { return receiver; }
	@Override public double getLeftSideBack() { return leftSideBack; }
	@Override public double getRightSideBack() { return rightSideBack; }
	@Override public double getLeftSideFront() { return leftSideFront; }
	@Override public double getRightSideFront() { return rightSideFront; }
	@Override public double getLeftFront() { return leftFront; }
	@Override public double getRightFront() { return rightFront; }
}
