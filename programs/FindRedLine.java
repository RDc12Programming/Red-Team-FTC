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

// This Program is the Final Version of Team 4891's TeleOp for the 2019 FTC Competition season
// There is a very real possibility that this will be updated as the season progresses

package org.firstinspires.ftc.teamcode; // Something to do with the RC code? It's required...

// To anyone wanting to mess with these, don't. It does weird stuff
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;          // imports "LinearOpMode", Required for a linear OpMode    | Used
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;            // imports "Autonomous", used for Auton OpModes            | Used
import com.qualcomm.robotcore.hardware.NormalizedRGBA;                // imports something for Color Sensors, required           | Used
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;         // imports more color sensor things                        | Used
import com.qualcomm.robotcore.hardware.Servo;                         // imports "Servo", Required for servos                    | Used
import com.qualcomm.robotcore.eventloop.opmode.Disabled;              // imports "Disabled", OnBot says it's required            | Maybe used?
import com.qualcomm.robotcore.hardware.DcMotor;                       // imports "DcMotor",  Required to control DcMotors        | Used
import com.qualcomm.robotcore.hardware.DcMotorSimple;                 // imports "DcMotorSimple", also required for DcMotors     | Used
import com.qualcomm.robotcore.util.ElapsedTime;                       // imports "ElapsedTime" utility, not used as of right now | Not used

@Autonomous

public class FindBlueLine extends LinearOpMode {

        // Define Motors
    private ElapsedTime runtime     = new ElapsedTime();
    private DcMotor leftRearDrive   = null; // Back  Left  Wheel
    private DcMotor leftFrontDrive  = null; // Front Left  Wheel
    private DcMotor rightRearDrive  = null; // Back  Right Wheel
    private DcMotor rightFrontDrive = null; // Front Right Wheel
    NormalizedColorSensor colorSensor;
    //private DcMotor intakeRight     = null; // Right Intake
    //private DcMotor intakeLeft      = null; // Left  Intake
    //private Servo   leftGrab        = null; // Left  Plate Grabber
    //private Servo   rightGrab       = null; // Right Plate Grabber
    
    float[] hsvValues = new float[3];
    final float values[] = hsvValues;
    
        // Define Mecanum values
    double RF; double LF; double RR; double LR; // Drive Speeds
    double X1;            double X2; double Y2; // Joystick Values
    
        // Variables
    double  tgtIntakePower     =     0; // Intake power variable
    boolean plates             = false; // Whether or not the build plate movers are up or down

    int i = 0; // Tick counter for reference

    @Override
    public void runOpMode() {

            // Set Initialized status
        telemetry.addData("Status", "Initialized");
        telemetry.update(); // Update telemetry statuses
        
            // Map DcMotors and Servos                                                             For Config Reference:
        leftFrontDrive  = hardwareMap.dcMotor.get("leftFrontDrive" ); // Left Front Drive Motor   "leftFrontDrive"  _Con_ Hub Motor Port 0 
        leftRearDrive   = hardwareMap.dcMotor.get("leftRearDrive"  ); // Left Back Drive Motor    "leftRearDrive"   _Con_ Hub Motor Port 1
        rightFrontDrive = hardwareMap.dcMotor.get("rightFrontDrive"); // Right Front Drive Motor  "rightFrontDrive" _Exp_ Hub Motor Port 1
        rightRearDrive  = hardwareMap.dcMotor.get("rightRearDrive" ); // Right Back Drive Motor   "rightRearDrive"  _Exp_ Hub Motor Port 0
        colorSensor     = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");
        /*intakeRight     = hardwareMap.dcMotor.get("intakeRight"    ); // Right Intake Motor       "intakeRight"     _____ Hub Motor Port _
        intakeLeft      = hardwareMap.dcMotor.get("intakeLeft"     ); // Left Intake Motor        "intakeLeft"      _____ Hub Motor Port _
        leftGrab        = hardwareMap.servo.get(  "leftGrab"       ); // Left Plate Servo         "leftGrab"        _____ Hub Servo Port _
        rightGrab       = hardwareMap.servo.get(  "rightGrab"      ); // Right Plate Servo        "rightGrab"       _____ Hub Servo Port _
        */
            // Setup Motor Directions
        leftFrontDrive.setDirection( DcMotor.Direction.FORWARD);
        leftRearDrive.setDirection(  DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightRearDrive.setDirection( DcMotor.Direction.FORWARD);
       
            // Set Motor Modes
        leftFrontDrive.setMode( DcMotor.RunMode.RUN_USING_ENCODER); // Turn on encoders for lf
        leftRearDrive.setMode(  DcMotor.RunMode.RUN_USING_ENCODER); // Turn on encoders for lr
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn on encoders for rf
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Turn on encoders for rr
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        
        tgtIntakePower = 0;
        
        telemetry.addData("Status", "Running"); // Set Status Running
        telemetry.update();                     // Update Telemetry
            
        LF = 0; RF = 0; LR = 0; RR = 0; // Mecanum Reset
        
        Y2 = 0;
        X1 = 0; // Reset Values so we don't move infinitely
        X2 = 0;
        
        if(!(colors.red > colors.blue && colors.red > colors.green)){
            Y2 = 0.1;
        }
        
        //if(gamepad1.right_trigger > 0 || gamepad1.left_trigger > 0){
        //    tgtIntakePower = 1; // Intakes inward (Triggers)
        //}
        
        //if(gamepad1.right_bumper || gamepad1.left_bumper){
        //    tgtIntakePower = -1; // Intakes outward (Bumpers)
        //}
            
        //if(gamepad1.dpad_up){
        //    // Slow Forward (DPAD up)
        //    Y2 = 0.25;
        //}
        
        //if(gamepad1.dpad_down){
        //    // Slow Backward (DPAD down)
        //    Y2 = -0.25;
        //}
       
       //if(gamepad1.dpad_left){
        //       // Slow Left (DPAD left)
         //   X2 = -0.25;
       //}
       
       //if(gamepad1.dpad_right){
               //Slow Right (DPAD right)
         //   X2 = 0.25;
       //}
        
        //if( gamepad1.right_stick_x > 0 ||  gamepad1.right_stick_x < 0){
          //  X1 = gamepad1.right_stick_x; // Rotation controls (Right Stick X axis)
        //}
    
        //if(-gamepad1.left_stick_y  > 0 || -gamepad1.left_stick_y  < 0){
          //  Y2 = -gamepad1.left_stick_y; // Forward / Backward controls (Left Stick Y axis)
        //}
    
        //if( gamepad1.left_stick_x  > 0 ||  gamepad1.left_stick_x  < 0){
          //  X2 = gamepad1.left_stick_x; // Strafe (Left Stick X axis)
        //}
        
        /*if(gamepad1.a){
            plates = true; // Move arms down
        }
        
        if(gamepad1.b){
            plates = false; // Move arms up
        }
        
        if(plates){
             leftGrab.setPosition(1); // Might need to swap with below
            rightGrab.setPosition(0); // Might need to swap with above
        } else {
             leftGrab.setPosition(0); // Might need to swap with below
            rightGrab.setPosition(1); // Might need to swap with above
        }*/
         
        LF += X2; RF -= X2; LR -= X2; RR += X2; // Strafe Movement
        LF += Y2; RF += Y2; LR += Y2; RR += Y2; // Forward / Backward Movement
        LF += X1; RF += X1; LR -= X1; RR -= X1; // Rotate Left / Right
        
        LF = Math.max(-1.0, Math.min(LF, 1.0)); // Make sure the motors don't get set with powers higher than they can handle
        RF = Math.max(-1.0, Math.min(RF, 1.0)); // ''
        LR = Math.max(-1.0, Math.min(LR, 1.0)); // ''
        RR = Math.max(-1.0, Math.min(RR, 1.0)); // ''
        
        leftFrontDrive.setPower( LF); // set lf power
        leftRearDrive.setPower(  LR); // set lr power
        rightFrontDrive.setPower(RF); // set rf power
        rightRearDrive.setPower( RR); // set rr power
        
        //intakeRight.setPower(-tgtIntakePower); // reversed with below?
        //intakeLeft.setPower(  tgtIntakePower);
        
        i++; // inrease tick counter
    
        // Set Telemetry Data
        //telemetry.addData("tgt Intake Power",    tgtIntakePower    );
        
        if(colors.red > colors.blue && colors.red > colors.green){
            // Red Line
        } else if(colors.blue > colors.red && colors.blue > colors.green){
            // Blue Line
        } else if(colors.green > colors.red && colors.green > colors.red){
            // Tiles
        }
        
        telemetry.addData("Tick Num",            i                  );
        telemetry.addData("Blue", colors.blue > colors.red && colors.blue > colors.green);
        telemetry.addData("Red", colors.red > colors.blue && colors.red > colors.green);
        telemetry.addData("Green", colors.green > colors.red && colors.green > colors.red);
        telemetry.addData("Y2",                                  Y2 );
        telemetry.addData("LF",                                  LF );
        telemetry.addData("LR",                                  LR );
        telemetry.addData("RF",                                  RF );
        telemetry.addData("RR",                                  RR );
        telemetry.addData("LF Power",     leftFrontDrive.getPower() );
        telemetry.addData("LR Power",     leftRearDrive.getPower()  );
        telemetry.addData("RF Power",     rightFrontDrive.getPower());
        telemetry.addData("RR Power",     rightRearDrive.getPower() );
        telemetry.addData("",                    "All Systems Good" );
     telemetry.addLine()
              .addData("a", "%.3f", colors.alpha)
              .addData("r", "%.3f", colors.red)
              .addData("g", "%.3f", colors.green)
              .addData("b", "%.3f", colors.blue);
        }
    }
}
