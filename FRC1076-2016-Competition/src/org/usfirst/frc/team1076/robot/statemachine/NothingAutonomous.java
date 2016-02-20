package org.usfirst.frc.team1076.robot.statemachine;

import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;

public class NothingAutonomous implements IAutoState {
	private IAutoState nextState = null;

	public void init() { }
	
	public IAutoState next() {
		return nextState ;
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
		return new MotorOutput(0, 0);
	}
	
	public double armSpeed() {
		return 0;
	}
	
	public double intakeSpeed() {
		return 0;
	}
}
