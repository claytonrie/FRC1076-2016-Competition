package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.controllers.AutoController;
import org.usfirst.frc.team1076.robot.controllers.AutoRun;
import org.usfirst.frc.team1076.robot.controllers.TeleopController;
import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;
import org.usfirst.frc.team1076.robot.statemachine.ForwardAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.NothingAutonomous;
import org.usfirst.frc.team1076.robot.statemachine.RunnableAutonomous;
import org.usfirst.frc.team1076.test.mock.MockInput;
import org.usfirst.frc.team1076.test.mock.MockRobot;

public class AutoControllerTest {
	private static final double EPSILON = 1e-10;
	AutoController controller = new AutoController(new ForwardAutonomous(0));
	MockRobot robot = new MockRobot();
	
	@Test
	public void testForward() {
		controller.autonomousPeriodic(robot);
		assertEquals("The left motor should be 0;",
				0, robot.left, EPSILON);
		assertEquals("The right motor should be 0;",
				0, robot.right, EPSILON);
	}
}
