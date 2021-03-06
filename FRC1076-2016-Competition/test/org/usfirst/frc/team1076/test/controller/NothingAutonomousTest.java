package org.usfirst.frc.team1076.test.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc.team1076.robot.gamepad.IInput.MotorOutput;
import org.usfirst.frc.team1076.robot.statemachine.AutoState;
import org.usfirst.frc.team1076.robot.statemachine.NothingAutonomous;

public class NothingAutonomousTest {
	private static final double EPSILON = 1e-12;

	@Test
	public void testNext() {
		AutoState auto = new NothingAutonomous();
		assertSame(null, auto.next());
		auto.setNext(auto);
		assertSame(auto, auto.next());
	}
	
	@Test
	public void testShouldNeverChange() {
		AutoState auto = new NothingAutonomous();
		assertEquals(false, auto.shouldChange());
	}
	
	@Test
	public void testNoMotion() {
		AutoState auto = new NothingAutonomous();
		MotorOutput motorOutput = auto.driveTrainSpeed();
		assertEquals(0, motorOutput.left, EPSILON);
		assertEquals(0, motorOutput.right, EPSILON);
	}
	
	@Test
	public void testNoArmMotion() {
		AutoState auto = new NothingAutonomous();
		assertEquals(0, auto.armSpeed(), EPSILON);
	}
	
	@Test
	public void testNoIntakeMotion() {
		AutoState auto = new NothingAutonomous();
		assertEquals(0, auto.intakeSpeed(), EPSILON);
	}
}
