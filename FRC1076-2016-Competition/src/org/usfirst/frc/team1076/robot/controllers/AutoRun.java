package org.usfirst.frc.team1076.robot.controllers;
import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;

public interface AutoRun {
	default void init() { }
	
	default boolean shouldChange() {
		return false;
	}
	
	default MotorOutput driveTrainSpeed() {
		return new MotorOutput(0, 0);
	}
	
	default double armSpeed() {
		return 0;
	}
	
	default double intakeSpeed() {
		return 0;
	}
}