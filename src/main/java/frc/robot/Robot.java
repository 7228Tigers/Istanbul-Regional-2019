/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Levitate;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
 
  private XboxController controller;
  private XboxController controller2;
  
  private Spark rearRightMotor;
  private Spark rearLeftMotor;
  private Spark frontRightMotor;
  private Spark frontLeftMotor;

  private PWMTalonSRX levitateFrontMotor;
  private PWMTalonSRX levitateRearMotor;
  private PWMTalonSRX levitatedMoveMotor; 

  private PWMTalonSRX liftMotor;
  private Spark grabberMotor;

  private MecanumDrive mDrive;

  private Gyro gyro;

  private Encoder rearEncoder;
  private Encoder frontEncoder;

  private DigitalInput liftLimiter;
  
  private Levitate levitateLift;
  Timer timer;

  private boolean autonomStarted;

  private int controller2pov;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    rearRightMotor = new Spark(1);
    rearLeftMotor = new Spark(0);
    frontLeftMotor = new Spark(3);
    frontRightMotor = new Spark(2);

    levitateFrontMotor = new PWMTalonSRX(6);
    levitateRearMotor = new PWMTalonSRX(4);
    levitatedMoveMotor = new PWMTalonSRX(7);

    liftMotor = new PWMTalonSRX(5);
    grabberMotor = new Spark(8);
    
    controller = new XboxController(0);
    controller2 = new XboxController(1);

    rearEncoder = new Encoder(0, 1);
    frontEncoder = new Encoder(2, 3);

    liftLimiter = new DigitalInput(5);

    timer = new Timer();

    levitateLift = new Levitate(controller, levitateFrontMotor, levitateRearMotor, levitatedMoveMotor, timer);

    rearEncoder.reset();
    frontEncoder.reset();
  
    mDrive = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

    autonomStarted = false;
    
    CameraServer cam = CameraServer.getInstance();
    cam.startAutomaticCapture();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
 
    // Higher both motors when pressed 
    levitateLift.elevate();

    // Positive lowers, negative highers the lift
   
    if (controller.getPOV() == 0) {
      liftMotor.set(-1.0);
    } else if (controller.getPOV() == 180) {
      liftMotor.set(1.0);
    } else {
      liftMotor.set(0);
    }
    
    /* Move while lifted
     * 7 - Left Trigger Button
     * 8 - Right Trigger Button
     */ 
    if (controller.getRawButton(7)) {
      levitatedMoveMotor.set(1.0);
    } else if (controller.getRawButton(8)) {
      levitatedMoveMotor.set(-1.0);
    } else {
      levitatedMoveMotor.set(0);
    }

    // Gripper system - THROW THE BALL
    if (controller.getBumper(Hand.kLeft)) {
      grabberMotor.set(-1.0);
    } else if (controller.getBumper(Hand.kRight)) {
      grabberMotor.set(1.0);
    } else {
      grabberMotor.set(0);
    }
    /*
    if (controller.getBumper(Hand.kLeft)) {
      levitatedMoveMotor.set(1.0);
    } else if (controller.getBumper(Hand.kRight)) {
      levitatedMoveMotor.set(-1.0);
    } else {
      levitatedMoveMotor.set(0);
    }
    */
    // Normal Mecanum Drive
    /**
     * First Param - Rotation
     * Second Param - Front/Backwards
     * Third Param - Left/Right Mecanum Move
     */
    System.out.println(-controller.getRawAxis(2) * 0.7);
     mDrive.driveCartesian(-controller.getRawAxis(2) * 0.7, controller.getY(Hand.kLeft), -controller.getX(Hand.kLeft));
  }

  

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
 
    // Higher both motors when pressed 
    levitateLift.elevate();

    // Positive lowers, negative highers the lift
  
    if (controller.getPOV() == 0) {
      liftMotor.set(-1.0);
    } else if (controller.getPOV() == 180) {
      liftMotor.set(1.0);
    } else {
      liftMotor.set(0);
    }
    
    
  
    /* Move while lifted
     * 7 - Left Trigger Button
     * 8 - Right Trigger Button
     */ 
    if (controller.getRawButton(7)) {
      levitatedMoveMotor.set(1.0);
    } else if (controller.getRawButton(8)) {
      levitatedMoveMotor.set(-1.0);
    } else {
      levitatedMoveMotor.set(0);
    }

    // Gripper system - THROW THE BALL
    if (controller.getBumper(Hand.kLeft)) {
      grabberMotor.set(-1.0);
    } else if (controller.getBumper(Hand.kRight)) {
      grabberMotor.set(1.0);
    } else {
      grabberMotor.set(0);
    }
    /*
    if (controller.getBumper(Hand.kLeft)) {
      levitatedMoveMotor.set(1.0);
    } else if (controller.getBumper(Hand.kRight)) {
      levitatedMoveMotor.set(-1.0);
    } else {
      levitatedMoveMotor.set(0);
    }
    */
    // Normal Mecanum Drive
    /**
     * First Param - Rotation
     * Second Param - Front/Backwards
     * Third Param - Left/Right Mecanum Move
     */
    System.out.println(-controller.getRawAxis(2) * 0.7);
     mDrive.driveCartesian(-controller.getRawAxis(2) * 0.7, controller.getY(Hand.kLeft), -controller.getX(Hand.kLeft));
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
