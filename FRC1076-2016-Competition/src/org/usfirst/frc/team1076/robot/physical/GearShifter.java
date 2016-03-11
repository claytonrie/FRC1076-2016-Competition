package org.usfirst.frc.team1076.robot.physical;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.IRobot.SolenoidValue;
import org.usfirst.frc.team1076.robot.sensors.GearShiftStateManager;

public class GearShifter extends GearShiftStateManager {
	private GearStates gearState = GearStates.Low;
	private int counter = 0;
	private final int HOLD_TICKS = 10;
	
	public void shiftGear(GearStates newState, IRobot robot) {
		if(newState == gearState && counter > 0) {
			robot.setGear(gearState.getValue());
			counter -= 1;
		} else if(newState != gearState) {
			robot.setGear(gearState.getValue());
			gearState = newState;
			counter = HOLD_TICKS;
		} else {
			robot.setGear(SolenoidValue.Off);
		}
	}
}
