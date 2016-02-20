package org.usfirst.frc.team1076.robot.gamepad;

import org.usfirst.frc.team1076.robot.gamepad.IGamepad.GamepadAxis;

public class OperatorInput implements IInput {
	IGamepad gamepad;
	
	public OperatorInput(IGamepad gamepad) {
		this.gamepad = gamepad;
	}
	
	@Override
	public MotorOutput driveTrainSpeed() {
		return new MotorOutput(0, 0);
	}
	
	@Override
	public double armSpeed() {
		return gamepad.getAxis(GamepadAxis.RightY);
	}

	@Override
	public double intakeSpeed() {
		double in = gamepad.getAxis(GamepadAxis.LeftTrigger);
		double out = gamepad.getAxis(GamepadAxis.RightTrigger);
		return in - out;
	}

}
