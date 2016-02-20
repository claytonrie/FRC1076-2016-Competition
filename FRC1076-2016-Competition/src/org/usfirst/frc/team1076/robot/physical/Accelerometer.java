package org.usfirst.frc.team1076.robot.physical;
import org.usfirst.frc.team1076.robot.sensors.IAccelerometer;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;


public class Accelerometer implements IAccelerometer {
	private BuiltInAccelerometer accel;
	
	public Accelerometer() {
		accel = new BuiltInAccelerometer();
	}
	
	public double getX() {
		return accel.getX();
	}
	
	public double getY() {
		return accel.getY();
	}
	
	public double getZ() {
		return accel.getZ();
	}
	
	public Vector getVector() {
		return new Vector(accel.getX(), accel.getY(), accel.getZ());
	}
}
