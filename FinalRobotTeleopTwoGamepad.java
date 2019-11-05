/*
Copyright 2019 FIRST Tech Challenge Team 4891 (PHS Team RED)
          __    ___    ________    ___________     _____
         / /   /   |  /        |  / _____     |  _/     | 
        / /   /   /  / _____  /  / /    /    /  /      / 
       / /   /   /  / /    / /  / /    /    /  /___   / 
      / /___/   /  / /    / /  / /____/    /      /  / Prosper High School
     |_____    /  | /____/ /  |______     /      /  /     4891 RED Team
          /   /  / _____  |         /    /      /  /     all three of us
         /   /  / /    / /         /    /      /  /
        /   /  / /    / /         /    /  ____/  /____
       /   /  / /____/ /         /    /  /            |
      |___/  |________/         |____/  |____________/

< ASCII 4891 and this program made by Rylan >

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

// This Program is the Dual   Gamepad Version of the Final TeleOp OpMode, for the single Gamepad version (probably not going to be used), see "FinalRobotTeleopOneGamepad.java"

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
 
 /*
 Press button for 180 rotation
 
 Things controls need to do:
    - Lift Up / Down (Motor)
    - Lift Left / Right (Motor)
    - Clamp / Unclamp (Servo)
    - Clamp Left / Right (Servo)
    - Bot Forward / Backward (Motor)
    - Bot Strafe (Motor)
    - Bot Rotation (Motor)
    - Intakes (Motors)
    - Plate Clamps (Servo)
 
 TODO: 
 - Make a diagram for easy control reference [ ] (Will do at practice 11.5.19 if the wifi works)
 - Practice, practice, practice!

 Proposed Dual   Gamepad Setup:
 GP1 (Movement): 
    Dpad    Up: Slow Forward
    Dpad  Down: Slow Backward
    Dpad  Left: Slow Strafe Left
    Dpad Right: Slow Strafe Right
    A   Button: Clamp   Plate Clamps
    B   Button: Unclamp plate Clamps
    X   Button: None
    Y   Button: None
    Right Trig: Intakes
    Left  Trig: Intakes
    Right Bump: Intakes Reverse
    Left  Bump: Intakes Reverse
    Start  Btn: None
    Back   Btn: None
    Rght Stick
             x: Rotation
             y: None
    Left Stick
             x: Strafe
             y: Forward / Backward
    Right SBtn: None
    Left  SBtn: None

GP2 (Crane   ):
    Dpad    Up: None
    Dpad  Down: None
    Dpad  Left: None
    Dpad Right: None
    A   Button: Rotate Crane
    B   Button: Rotate Crane reverse
    X   Button: Extand Crane
    Y   Button: Extend Crane reverse
    Right Trig: None
    Left  Trig: None
    Right Bump: Clamp
    Left  Bump: Clamp Release
    Start  Btn: None
    Back   Btn: None
    Rght Stick
             x: Clamp Rotate
             y: None
    Left Stick
             x: Crane Rotation
             y: Crane Height
    Right SBtn: None
    Left  SBtn: None
 */
@TeleOp

public class FinalRobotTeleopTwoGamepad extends LinearOpMode {

        // Define Motors
    private ElapsedTime runtime     = new ElapsedTime();
    private DcMotor leftRearDrive   = null; // Back  Left  Wheel
    private DcMotor leftFrontDrive  = null; // Front Left  Wheel
    private DcMotor rightRearDrive  = null; // Back  Right Wheel
    private DcMotor rightFrontDrive = null; // Front Right Wheel
    private DcMotor Lift            = null; // Lift  Height
    private DcMotor LiftRotate      = null; // Lift  Rotation
    private DcMotor intakeRight     = null; // Right Intake
    private DcMotor intakeLeft      = null; // Left  Intake
    private Servo   clamp           = null; // Clamp
    private Servo   clampRotate     = null; // Clamp Rotation
	private Servo   leftGrab        = null; // Left  Plate Grabber
	private Servo   rightGrab       = null; // Right Plate Grabber
    
        // Define Mecanum values
    double RF; double LF; double RR; double LR; // Drive Speeds
    double X1;            double X2; double Y2; // Joystick Values
    
        // Variables
    double  tgtLiftPower       =     0;
    double  tgtLiftRotatePower =     0;
    double  tgtIntakePower     =     0;
    double  tgtClampRotation   =     0;

    boolean clamped            = false;
	boolean plates             = false;

    int i = 0;

    @Override
    public void runOpMode() {

            // Set Initialized status
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
            // Map DcMotors
        leftFrontDrive  = hardwareMap.dcMotor.get("leftFrontDrive" );
        leftRearDrive   = hardwareMap.dcMotor.get("leftRearDrive"  );
        rightFrontDrive = hardwareMap.dcMotor.get("rightFrontDrive");
        rightRearDrive  = hardwareMap.dcMotor.get("rightRearDrive" );
        Lift            = hardwareMap.dcMotor.get("Lift"           );
        LiftRotate      = hardwareMap.dcMotor.get("LiftRotate"     ); 
        intakeRight     = hardwareMap.dcMotor.get("intakeRight"    );
        intakeLeft      = hardwareMap.dcMotor.get("intakeLeft"     );
        clamp           = hardwareMap.servo.get(  "clamp"          );
        clampRotate     = hardwareMap.servo.get(  "clampRotate"    );
		leftGrab        = hardwareMap.servo.get(  "leftGrab"       );
		rightGrab       = hardwareMap.servo.get(  "rightGrab"      );
        
            // Setup Mecanum Directions
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE );
        leftRearDrive.setDirection( DcMotor.Direction.FORWARD );
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightRearDrive.setDirection( DcMotor.Direction.FORWARD);
       
            // Set Encoders
        leftFrontDrive.setMode( DcMotor.RunMode.RUN_USING_ENCODER);
        leftRearDrive.setMode(  DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LiftRotate.setMode(     DcMotor.RunMode.RUN_USING_ENCODER);
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
        tgtIntakePower = 0;
        
        telemetry.addData("Status", "Running"); // Set Status Running
        telemetry.update();                     // Update Telemetry
            
        LF = 0; RF = 0; LR = 0; RR = 0; // Mecanum Reset
        
        Y2 = 0;
        X1 = 0; // Reset Values so we don't move infinitely
        X2 = 0;
        
        tgtClampRotation    =   Math.pow(gamepad2.right_stick_x, -1);
        tgtLiftPower        =   gamepad2.left_stick_y * 0.75; // 3/4 Power, may change by trial & err
        tgtLiftRotatePower  =  -gamepad2.left_stick_x / 4;    // 1/4 Power, may need decreased more if it's still jerky; smooth, precise  rotation is the goal
		
		if(gamepad2.right_trigger > 0){                    // At the beginning of the game, move the arm to "0" or inside at a 90 deg angle from the back frame so that these don't make the whole thing go wack when we try to spin
		
		// Holding it shouldn't do anything (may be wrong...) and might actually fix overcorrection which could be a real problem
			while(!liftRotate.getPosition() = 180){        // 180 being 180 deg from origin pos
				if(liftRotate.getPosition() > 180){        // 180 being 180 deg from origin pos
					liftRotate.setPower(-0.5);             // This might need to go down if its too fast
				} else if(liftRotate.getPosition() < 180){ // 180 being 180 deg from origin pos
					liftRotate.setPower( 0.5);             // This also might need to go down
				}
			}
        }
		
		if(gamepad2.left_trigger > 0){                    // All comments from the above group of statements apply here as well
			while(!liftRotate.getPosition() = 0){
				if(liftRotate.getPosition() > 0){
					liftRotate.setPower(-0.5);
				} else if(liftRotate.getPosition() < 0){
					liftRotate.setPower( 0.5);
				}
			}
		}
		
        //if(gamepad2.a){
        //    tgtLiftRotatePower = 0.25;           These might need added back but shouldn't need to be because they might screw up the origin position but the movement may be needed for block placement (In theory, if we get the clamp rotation servo back online and movement controls to unrealistic exactness then this could be unnecessary but thats probably not going to happen) [WHY DIDN'T WE JUST USE SERVOS FOR THIS AND THE HEIGHT CONTROL LIKE WE ORIGINALLY PLANNED?!?!]
        //}
        
        //if(gamepad2.b){
        //    tgtLiftRotatePower = -0.25;
        //}
        
        if(gamepad2.x){
            tgtLiftPower = 1;
        }
        
        if(gamepad2.y){
            tgtLiftPower = -0.75;
        }
        
        if(gamepad2.right_bumper){
            clamped = true;
        }
        
        if(gamepad2.left_bumper){
            clamped = false;
        }
        
        if(gamepad1.right_trigger > 0 || gamepad1.left_trigger > 0){
            tgtIntakePower = -1;
        }
        
        if(gamepad1.right_bumper || gamepad1.left_bumper){
            tgtIntakePower = 1;
        }
            
        if(gamepad1.dpad_up){
            // Slow Forward
            Y2 = 0.25;
        }
        
        if(gamepad1.dpad_down){
            // Slow Backward
            Y2 = -0.25;
        }
       
       if(gamepad1.dpad_left){
               // Slow Left
            // Might need to swap values with dpad_right if scale goes from 1 on the left instead of -1 on the left
            X2 = -0.25;
       }
       
       if(gamepad1.dpad_right){
               //Slow Right
            X2 = 0.25;
       }
        
        if( gamepad1.right_stick_x > 0 ||  gamepad1.right_stick_x < 0){
            X1 = gamepad1.right_stick_x;
        }
    
        if(-gamepad1.left_stick_y  > 0 || -gamepad1.left_stick_y  < 0){
            Y2 = -gamepad1.left_stick_y;
        }
    
        if( gamepad1.left_stick_x  > 0 ||  gamepad1.left_stick_x  < 0){
            X2 = gamepad1.left_stick_x;
        }
		
		if(gamepad1.a){
			plates = true;
		}
		
		if(gamepad1.b){
			plates = false;
		}
        
		if(plates){
			 leftGrab.setPosition(0.5);
			rightGrab.setPosition(0.5);
		} else {
			 leftGrab.setPosition(0.0);
			rightGrab.setPosition(0.0);
		}
		 
        LF += X2; RF += X2; LR += X2; RR += X2; // Strafe Movement
        LF += Y2; RF -= Y2; LR -= Y2; RR += Y2; // Forward / Backward Movement
        LF += X1; RF += X1; LR -= X1; RR -= X1; // Rotate Left / Right
        
        LF = Math.max(-1.0, Math.min(LF, 1.0));
        RF = Math.max(-1.0, Math.min(RF, 1.0));
        LR = Math.max(-1.0, Math.min(LR, 1.0));
        RR = Math.max(-1.0, Math.min(RR, 1.0));
        
        leftFrontDrive.setPower( LF);
        leftRearDrive.setPower(  LR);
        rightFrontDrive.setPower(RF);
        rightRearDrive.setPower( RR);
        
        Lift.setPower(        tgtLiftPower      );
        LiftRotate.setPower(  tgtLiftRotatePower);
        intakeRight.setPower(-tgtIntakePower / 2);
        intakeLeft.setPower(  tgtIntakePower / 2);
        
        clampRotate.setPosition(tgtClampRotation);
        
        if(clamped){
            clamp.setPosition(1);
        } else {
            clamp.setPosition(0.25); // 0.25 might need to change
        }
		
		clampRotate.setPosition(tgtClampRotation);
        
        i++;
        
      //telemetry.addData("Crane Mode",          craneMode         );
        telemetry.addData("Clamped",             clamped           );
        telemetry.addData("tgt Clamp Rotation",  tgtClampRotation  );
        telemetry.addData("tgt Intake Power",    tgtIntakePower / 2);
        telemetry.addData("tgt Lift Power",      tgtLiftPower      );
        telemetry.addData("tgt Lift Rotate pwr", tgtLiftRotatePower);
        telemetry.addData("Tick Num",            i                 );
        
        //telemetry.addData("LF", "%.3f", RR);
        //telemetry.addData("RF", "%.3f", RF);
        //telemetry.addData("LR", "%.3f", LR);
        //telemetry.addData("RR", "%.3f", RR);
        }
    }
}
