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

/*
    Stage 0: Error
    Stage 1: Move Build Plate
        1.1: Go to plate
        1.2: Plates lowered
        1.3: Pull
        1.4: Exit to right
    Stage 2: Move toward blocks
    Stage 3: Scan Blocks, add detected skystones to variables ss1 & ss2 (1 - 6)
        3.1: End at first skystone
        3.N: Note: Scans all 6 stones, applying each positive to a variable
    Stage 4: Pick up skystone 1
        4.1: Extend Crane
        4.2: Rotate Crane
        4.3: lower Crane
        4.4: Clamp
        4.5: Move to plate
        4.5: Raise Crane
        4.6: Position over plate
        4.7: lower crane
        4.8: release
        4.9: raise crane
       4.10: move away
       4.11: Lower Crane
       4.12: move to second skystone
    Stage 5: Pick up skystone 2
        5.1: Extend Crane
        5.2: Rotate Crane
        5.3: lower Crane
        5.4: Clamp
        5.5: Move to plate
        5.5: Raise Crane
        5.6: Position over plate
        5.7: lower crane
        5.8: release
        5.9: raise crane
       5.10: move away
       5.11: Lower Crane
       5.12: move to second skystone
    Stage 6: Move to line under bridge
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous

public class Autonomous extends LinearOpMode {

        // Define Motors
    private ElapsedTime runtime     = new ElapsedTime();
    private DcMotor leftRearDrive   = null; // Back  Left  Wheel
    private DcMotor leftFrontDrive  = null; // Front Left  Wheel
    private DcMotor rightRearDrive  = null; // Back  Right Wheel
    private DcMotor rightFrontDrive = null; // Front Right Wheel
    private DcMotor Lift            = null; // Lift  Height
    private DcMotor Lift2           = null; // Lift Height #2
    private Servo   LiftRotate      = null; // Lift  Rotation
    private DcMotor intakeRight     = null; // Right Intake
    private DcMotor intakeLeft      = null; // Left  Intake
    private Servo   clamp           = null; // Clamp
    private Servo   clampRotate     = null; // Clamp Rotation
	private Servo   leftGrab        = null; // Left  Plate Grabber
    private Servo   rightGrab       = null; // Right Plate Grabber
    
    ColorSensor color_sensor;
    
        // Define Mecanum values
    double RF; double LF; double RR; double LR; // Drive Speeds
    double X1;            double X2; double Y2; // Joystick Values
    
        // Variables
    
    //int ;

    double  tgtLiftPower       =     0;
    double  tgtLiftRotate      =     0;
    double  tgtIntakePower     =     0;
    double  tgtClampRotation   =     0;

    double colors = 0;
    double red = 0;
    double green = 0;
    double blue = 0;
 
    boolean clamped            = false;
    boolean plates             = false;

    int ss1 = null;
    int ss2 = null;

    int i = 0;
    int per = 10888;
    int stage = 1;

    @Override
    public void runOpMode() {

            // Set Initialized status
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
            // Map DcMotors
        leftFrontDrive  = hardwareMap.dcMotor.get( "leftFrontDrive" );
        leftRearDrive   = hardwareMap.dcMotor.get( "leftRearDrive"  );
        rightFrontDrive = hardwareMap.dcMotor.get( "rightFrontDrive");
        rightRearDrive  = hardwareMap.dcMotor.get( "rightRearDrive" );
        Lift            = hardwareMap.dcMotor.get( "Lift"           );
        Lift2           = hardwareMap.dcMotor.get( "Lift2"          );
        LiftRotate      = hardwareMap.servo.get(   "LiftRotate"     ); 
        intakeRight     = hardwareMap.dcMotor.get( "intakeRight"    );
        intakeLeft      = hardwareMap.dcMotor.get( "intakeLeft"     );
        clamp           = hardwareMap.servo.get(   "clamp"          );
        clampRotate     = hardwareMap.servo.get(   "clampRotate"    );
		leftGrab        = hardwareMap.servo.get(   "leftGrab"       );
        rightGrab       = hardwareMap.servo.get(   "rightGrab"      );
        color_sensor = hardwareMap.colorSensor.get("color"          );
        
            // Setup Mecanum Directions
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD );
        leftRearDrive.setDirection( DcMotor.Direction.FORWARD );
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightRearDrive.setDirection( DcMotor.Direction.FORWARD);
       
            // Set Encoders
        leftFrontDrive.setMode( DcMotor.RunMode.RUN_USING_ENCODER);
        leftRearDrive.setMode(  DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LiftRotate.setMode(     DcMotor.RunMode.RUN_USING_ENCODER);
        
        clampRotate.setPosition(1);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
        colors = color_sensor.argb();
        red    = color_sensor.red();
        green  = color_sensor.green();
        blue   = color_sensor.blue();

        tgtIntakePower = 0;
        
        telemetry.addData("Status", "Running"); // Set Status Running
        telemetry.update();                     // Update Telemetry
            
        LF = 0; RF = 0; LR = 0; RR = 0; // Mecanum Reset
        
        Y2 = 0;
        X1 = 0; // Reset Values so we don't move infinitely
        X2 = 0;

        
        //if(gamepad2.x){
        //    tgtLiftPower = 1;
        //}
        
        //if(gamepad2.y){
        //    tgtLiftPower = -0.75;
        //}
        
        //if(gamepad2.right_bumper){
        //    clamped = true;
        //}
        
        //if(gamepad2.left_bumper){
        //    clamped = false;
        //}
        
        //if(gamepad1.right_trigger > 0 || gamepad1.left_trigger > 0){
        //    tgtIntakePower = -1;
        //}
        
        //if(gamepad1.right_bumper || gamepad1.left_bumper){
        //    tgtIntakePower = 1;
        //}
            
        //if(gamepad1.dpad_up){
            // Slow Forward
        //    Y2 = 0.25;
        //}
        
        //if(gamepad1.dpad_down){
            // Slow Backward
        //    Y2 = -0.25;
        //}
       
       //if(gamepad1.dpad_left){
               // Slow Left
            // Might need to swap values with dpad_right if scale goes from 1 on the left instead of -1 on the left
        //    X2 = -0.25;
       //}
       
       //if(gamepad1.dpad_right){
               //Slow Right
       //     X2 = 0.25;
       //}
        
        //if( gamepad1.right_stick_x > 0 ||  gamepad1.right_stick_x < 0){
        //    X1 = gamepad1.right_stick_x;
        //}
    
        //if(-gamepad1.left_stick_y  > 0 || -gamepad1.left_stick_y  < 0){
        //    Y2 = -gamepad1.left_stick_y;
        //}
    
        //if( gamepad1.left_stick_x  > 0 ||  gamepad1.left_stick_x  < 0){
        //    X2 = gamepad1.left_stick_x;
        //}
		
		//if(gamepad1.a){
		//	plates = true;
		//}
		
		//if(gamepad1.b){
		//	plates = false;
		//}

		if(plates){
			 leftGrab.setPosition(1);
			rightGrab.setPosition(0);
		} else {
			 leftGrab.setPosition(0);
			rightGrab.setPosition(1);
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
        
        Lift.setPower(           tgtLiftPower      );
        LiftRotate.setPosition(  tgtLiftRotate     );
        intakeRight.setPower(   -tgtIntakePower / 2);
        intakeLeft.setPower(     tgtIntakePower / 2);
        
        //clampRotate.setPosition(tgtClampRotation);
        
        if(clamped){
            clamp.setPosition(1);
        } else {
            clamp.setPosition(0); 
        }
		
		//clampRotate.setPosition(tgtClampRotation);
        
      //telemetry.addData("Crane Mode",          craneMode         );
      //telemetry.addData("Clamped",             clamped           );
      //telemetry.addData("tgt Clamp Rotation",  tgtClampRotation  );
      //telemetry.addData("tgt Intake Power",    tgtIntakePower / 2);
      //telemetry.addData("tgt Lift Power",      tgtLiftPower      );
      //telemetry.addData("tgt Lift Rotate pwr", tgtLiftRotatePower);
        telemetry.addData("Tick Num ",            i                );
        telemetry.addData("Stage Num",            stage            );
        
      //telemetry.addData("LF", "%.3f", RR);
      //telemetry.addData("RF", "%.3f", RF);
      //telemetry.addData("LR", "%.3f", LR);
      //telemetry.addData("RR", "%.3f", RR);

        if(stage == 0){
            telemetry.addData("Status", "Error");
            //initActiveOpMode(OpModeManagerImpl.Autonomous)
        } else if(stage = 1){
            
        }

      i++;
        }
    }

    telemetry.addData("Status", "Dead");
    telemetry.update();

}