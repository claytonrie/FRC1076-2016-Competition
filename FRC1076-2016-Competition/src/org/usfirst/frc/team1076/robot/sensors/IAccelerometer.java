package org.usfirst.frc.team1076.robot.sensors;

public interface IAccelerometer {
	public class Vector {
		public double x, y, z;
		
		public Vector(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	
	Vector getVector();
	double getX();
	double getY();
	double getZ();
}
