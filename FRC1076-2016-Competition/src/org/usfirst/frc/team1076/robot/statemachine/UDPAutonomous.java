package org.usfirst.frc.team1076.robot.statemachine;

import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;
import org.usfirst.frc.team1076.robot.sensors.IAccelerometer;
import org.usfirst.frc.team1076.udp.SensorData;

public class UDPAutonomous implements IAutoState {
	private int port;
	private SensorData sensors;
	private IAccelerometer accel;
	private IAutoState nextState = null;
	
	public UDPAutonomous(int port) {
		this.port = port;
		sensors = new SensorData(this.port);
	}

	public void init() { }
	
	public IAutoState next() {
		return nextState;
	}
	
	public IAutoState setNext(IAutoState nextState) {
		if(this.nextState == null) {
			this.nextState = nextState;
		} else {
			this.nextState.setNext(nextState);
		}
		return this;
	}
	
	public boolean shouldChange() {
		return false;
	}
	
	public MotorOutput driveTrainSpeed() {
		sensors.interpretData();
		if (Math.abs(sensors.getHeading() - sensors.currentHeading()) > 1) {
			return new MotorOutput(1, -1);
		}
		return new MotorOutput(1, 1);
	}
	
	public double armSpeed() {
		return 0;
	}
	
	public double intakeSpeed() {
		return 0;
	}
}
