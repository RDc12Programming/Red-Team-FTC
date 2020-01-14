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

package org.firstinspires.ftc.teamcode;

// To anyone wanting to mess with these, don't. It does weird stuff
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp

public class TeleOpFinal extends LinearOpMode {

        // Define Motors
    private ElapsedTime runtime     = new ElapsedTime();
    private DcMotor leftRearDrive   = null; // Back  Left  Wheel
    private DcMotor leftFrontDrive  = null; // Front Left  Wheel
    private DcMotor rightRearDrive  = null; // Back  Right Wheel
    private DcMotor rightFrontDrive = null; // Front Right Wheel
    private DcMotor intakeRight     = null; // Right Intake
    private DcMotor intakeRightBack = null; // ''
    private DcMotor intakeLeft      = null; // Left  Intake
    private DcMotor intakeLeftBack  = null; // ''
    private Servo   leftGrab        = null; // Left  Plate Grabber
    private Servo   rightGrab       = null; // Right Plate Grabber
    private Servo   capstone        = null; // Capstone holder
    private NormalizedColorSensor colorSensor;
    private NormalizedColorSensor colorSensor2;
    private NormalizedColorSensor blockType;
    private DistanceSensor blockDist;
   
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
        intakeRight     = hardwareMap.dcMotor.get("intakeRight"    ); // Right Intake Motor       "intakeRight"     _____ Hub Motor Port _
        intakeRightBack = hardwareMap.dcMotor.get("intakeRightBack"); //         ''               "intakeRightBack" _____ Hub Motor Port _
        intakeLeft      = hardwareMap.dcMotor.get("intakeLeft"     ); // Left Intake Motor        "intakeLeft"      _____ Hub Motor Port _
        intakeLeftBack  = hardwareMap.dcMotor.get("intakeLeftBack" ); //         ''               "intakeLeftBack"  _____ Hub Motor Port _
        leftGrab        = hardwareMap.servo.get(  "leftClamp"      ); // Left Plate Servo         "leftGrab"        _____ Hub Servo Port _
        rightGrab       = hardwareMap.servo.get(  "rightClamp"     ); // Right Plate Servo        "rightGrab"       _____ Hub Servo Port _
        capstone        = hardwareMap.servo.get(  "capstone"       ); // Capstone Holder
        blockType       = hardwareMap.get(NormalizedColorSensor.class, "blockType");
        colorSensor     = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");
        colorSensor2    = hardwareMap.get(NormalizedColorSensor.class, "sensor_color2"); // TODO: Config "sensor_color2"
        blockDist       = hardwareMap.get(DistanceSensor.class, "block_distance");
       
            // Setup Motor Directions
        leftFrontDrive.setDirection( DcMotor.Direction.FORWARD);
        leftRearDrive.setDirection(  DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightRearDrive.setDirection( DcMotor.Direction.FORWARD);
       
            // Set Motor Modes
        leftFrontDrive.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER); // Turn off encoders for lf so we can move full speed
        leftRearDrive.setMode(  DcMotor.RunMode.RUN_WITHOUT_ENCODER); // '' for lr
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // '' for rf
        rightRearDrive.setMode( DcMotor.RunMode.RUN_WITHOUT_ENCODER); // '' for rr
       
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
    while (opModeIsActive()) {
        NormalizedRGBA colors2 = colorSensor2.getNormalizedColors();
        NormalizedRGBA colors  = colorSensor.getNormalizedColors();
        NormalizedRGBA block   = blockType.getNormalizedColors();
       
        Rev2mDistanceSensor blockDistance = (Rev2mDistanceSensor)blockDist;
       
        tgtIntakePower = 0;
           
        LF = 0; RF = 0; LR = 0; RR = 0; // Variable Reset:
        Y2 = 0; X1 = 0; X2 = 0;         // Reset Values so we don't move infinitely
       
        if(gamepad1.right_trigger > 0 || gamepad1.left_trigger > 0){
            tgtIntakePower = 1; // Intakes inward (Triggers)
        }
       
        if(gamepad1.right_bumper || gamepad1.left_bumper){
            tgtIntakePower = -1; // Intakes outward (Bumpers)
        }
           
        if(gamepad1.dpad_up){
            // Slow Forward (DPAD up)
            Y2 = 0.25;
        }
       
        if(gamepad1.dpad_down){
            // Slow Backward (DPAD down)
            Y2 = -0.25;
        }
       
       if(gamepad1.dpad_left){
               // Slow Left (DPAD left)
            X2 = -0.25;
       }
       
       if(gamepad1.dpad_right){
               //Slow Right (DPAD right)
            X2 = 0.25;
       }
       
        if( gamepad1.right_stick_x > 0 ||  gamepad1.right_stick_x < 0){
            X1 = gamepad1.right_stick_x; // Rotation controls (Right Stick X axis)
        }
   
        if(-gamepad1.left_stick_y  > 0 || -gamepad1.left_stick_y  < 0){
            Y2 = -gamepad1.left_stick_y; // Forward / Backward controls (Left Stick Y axis)
        }
   
        if( gamepad1.left_stick_x  > 0 ||  gamepad1.left_stick_x  < 0){
            X2 = gamepad1.left_stick_x; // Strafe (Left Stick X axis)
        }
       
        if(gamepad1.a){
            plates = true; // Move arms down
        }
       
        if(gamepad1.b){
            plates = false; // Move arms up
        }
       
        if(plates){ // Build Plate pulling devices on servos
            leftGrab.setPosition(1); // Need to trial & error these into shape
            rightGrab.setPosition(0); 
        } else {
             leftGrab.setPosition(0);
            rightGrab.setPosition(1);
        }

        if(gamepad1.x){
            // kick out capstone
            capstone.setPosition(1); // value is probably wrong
        }

        if(gamepad1.y){
            // reset capstone arm
            capstone.setPosition(0); // value is probably wrong
        }
         
        if(colors.red > colors.blue && colors.red > colors.green){
            // Red Line
        } else if(colors.blue > colors.red && colors.blue > colors.green){
            // Blue Line
        } else if(colors.green > colors.red && colors.green > colors.red){
            // Tiles
        }
         
        LF += X2; RF -= X2; LR -= X2; RR += X2; // Strafe Movement             | Good
        LF += Y2; RF -= Y2; LR += Y2; RR -= Y2; // Forward / Backward Movement | Good
        LF += X1; RF += X1; LR += X1; RR += X1; // Rotate Left / Right         | Good
       
        LF = Math.max(-1.0, Math.min(LF, 1.0)); // Make sure the motors don't get set with powers higher than they can handle
        RF = Math.max(-1.0, Math.min(RF, 1.0)); // ''
        LR = Math.max(-1.0, Math.min(LR, 1.0)); // ''
        RR = Math.max(-1.0, Math.min(RR, 1.0)); // ''
       
        leftFrontDrive.setPower( LF); // set lf power
        leftRearDrive.setPower(  LR); // set lr power
        rightFrontDrive.setPower(RF); // set rf power
        rightRearDrive.setPower( RR); // set rr power
       
        intakeRight.setPower(     tgtIntakePower);
        intakeRightBack.setPower( tgtIntakePower);
        intakeLeft.setPower(     -tgtIntakePower);
        intakeLeftBack.setPower( -tgtIntakePower);
       
        i++; // inrease tick counter
   
        // Set Telemetry Data
        telemetry.addData("Tick Num", i); 
        // That monstrosity below this line detects whether or not it sees a skystone, *as compared to a regular stone*; it has been known to give false positives with the wall and black objects
        telemetry.addData("Skystone?", !(block.red >= 0.01) && new Double(blockDistance.getDistance(DistanceUnit.INCH)) <= 2 && new Double(blockDistance.getDistance(DistanceUnit.INCH)) >= 0.5 );
        telemetry.addData("Status", "Running");
        telemetry.update();
        }
    }
}