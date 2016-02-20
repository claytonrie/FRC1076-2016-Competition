package org.usfirst.frc.team1076.robot.statemachine;
import org.usfirst.frc.team1076.robot.controllers.AutoRun;
import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;

public class RunnableAutonomous implements IAutoState {
	private IAutoState nextState = null;
	private AutoRun customWorker;

	public RunnableAutonomous(AutoRun autoRun) {
		customWorker = autoRun;
	}

	public void init() {
		customWorker.init();
	}
	
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
		return customWorker.shouldChange();
	}

	public MotorOutput driveTrainSpeed() {
		return customWorker.driveTrainSpeed();
	}

	public double armSpeed() {
		return customWorker.armSpeed();
	}

	public double intakeSpeed() {
		return customWorker.intakeSpeed();
	}

}
