package org.usfirst.frc.team1076.robot.controllers;

import org.usfirst.frc.team1076.robot.IRobot;
import org.usfirst.frc.team1076.robot.gamepad.IInput;
import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;
import org.usfirst.frc.team1076.robot.statemachine.ForwardAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.IAutoState;
import org.usfirst.frc.team1076.robot.statemachine.RunnableAutonomous;
import org.usfirst.frc.team1076.udp.SensorData;

public class AutoController implements IRobotController {

	SensorData sensorData;
	IAutoState autoState;
	
	public AutoController(IAutoState mode) {
		this.autoState = mode;
		sensorData = new SensorData(5880);
		/*this.autoState = new RunnableAutonomous(new AutoRun() {
			private final double sp = 1;
			private double time = 0;
			
			public void init() {
				time = System.currentTimeMillis();
			}
			
			public MotorOutput driveTrainSpeed() {
				if(System.currentTimeMillis() - time < 1000) {
					return new MotorOutput(sp, sp);
				} else {
					return new MotorOutput(0, 0);
				}
			}
		});*/
	}
	
	@Override
	public void robotInit(IRobot robot) { }

	@Override
	public void teleopInit(IRobot robot) { }

	@Override
	public void teleopPeriodic(IRobot robot) { }

	@Override
	public void autonomousInit(IRobot robot) { }
	
	@Override
	public void autonomousPeriodic(IRobot robot) {
		sensorData.interpretData();
		if (autoState.shouldChange()) {
			autoState = autoState.next();
			autoState.init(); // Initialize the new autonomous mode
		}
		robot.setArmSpeed(autoState.armSpeed());
		robot.setIntakeSpeed(autoState.intakeSpeed());
		MotorOutput drive = autoState.driveTrainSpeed();
		robot.setLeftSpeed(drive.left);
		robot.setRightSpeed(drive.right);		
	}
}