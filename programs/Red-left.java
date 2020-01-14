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

// To anyone wanting to mess with these, please don't. It does weird stuff
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
    private NormalizedColorSensor colorSensor;
    private NormalizedColorSensor colorSensor2;
    private NormalizedColorSensor blockType;
    private DistanceSensor blockDist;
    int ticks;

    int stage = 0;
   
        // Define Mecanum values
    double RF; double LF; double RR; double LR; // Drive Speeds
    double X1;            double X2; double Y2; // Joystick Values
   
        // Variables
    double  tgtIntakePower     =     0; // Intake power variable
    boolean plates             = false; // Whether or not the build plate movers are up or down

    int i = 0; // Tick counter for reference

    int stone1 = 0;

    isSkystone() {
        if(!(block.red >= 0.01) && new Double(blockDistance.getDistance(DistanceUnit.INCH)) <= 2 && new Double(blockDistance.getDistance(DistanceUnit.INCH)) >= 0.5 ){
            return true;
        } else {
            return false;
        }
    }

    move(String direction, int inches){
        ticks = inches/0.01656492242;
        switch(direction){
            case "forward":
                leftFrontDrive.setTargetPosition(  ticks);
                leftRearDrive.setTargetPosition(   ticks);
                rightFrontDrive.setTargetPosition(-ticks);
                rightRearDrive.setTargetPosition( -ticks);
                while(leftFrontDrive.isBusy() || leftRearDrive.isBusy() || rightFrontDrive.isBusy() || rightRearDrive.isBusy()){
                    sleep(0);
                }
                return true;
                break;
            case "backward":
                leftFrontDrive.setTargetPosition( -ticks);
                leftRearDrive.setTargetPosition(  -ticks);
                rightFrontDrive.setTargetPosition( ticks);
                rightRearDrive.setTargetPosition(  ticks);
                while(leftFrontDrive.isBusy() || leftRearDrive.isBusy() || rightFrontDrive.isBusy() || rightRearDrive.isBusy()){
                    sleep(0);
                }
                return true;
                break;
            case "left":
                leftFrontDrive.setTargetPosition(  ticks);
                leftRearDrive.setTargetPosition(  -ticks);
                rightFrontDrive.setTargetPosition(-ticks);
                rightRearDrive.setTargetPosition(  ticks);
                while(leftFrontDrive.isBusy() || leftRearDrive.isBusy() || rightFrontDrive.isBusy() || rightRearDrive.isBusy()){
                    sleep(0);
                }
                return true;
                break;
            case "right":
                leftFrontDrive.setTargetPosition( -ticks);
                leftRearDrive.setTargetPosition(   ticks);
                rightFrontDrive.setTargetPosition(-ticks);
                rightRearDrive.setTargetPosition(  ticks);
                while(leftFrontDrive.isBusy() || leftRearDrive.isBusy() || rightFrontDrive.isBusy() || rightRearDrive.isBusy()){
                    sleep(0);
                }
                return true;
                break;
            default: return false;
        }
    }

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
        leftGrab        = hardwareMap.servo.get(  "leftClamp"      ); // Left Plate Servo         "leftGrab"        _____ Hub Servo Port _
        rightGrab       = hardwareMap.servo.get(  "rightClamp"     ); // Right Plate Servo        "rightGrab"       _____ Hub Servo Port _
        intakeLeftBack  = hardwareMap.dcMotor.get("intakeLeftBack" ); //         ''               "intakeLeftBack"  _____ Hub Motor Port _
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
        leftFrontDrive.setMode( DcMotor.RunMode.RUN_TO_POSITION); // Turn on encoders for lf
        leftRearDrive.setMode(  DcMotor.RunMode.RUN_TO_POSITION); // '' for lr
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION); // '' for rf
        rightRearDrive.setMode( DcMotor.RunMode.RUN_TO_POSITION); // '' for rr
       
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
        
        switch(stage){
            case 1: if(blockDistance.getDistance(DistanceUnit.INCH) > 1.5){
                       move("forward", 1);
                    } else {
                        stage++;
                    }
                    break;

            case 2: move("right", 12);
                    stage++;
                    break;

            case 3: int x = 1;
                    while(stone1 == 0){
                        if(x <= 5){
                            if(isSkystone()){
                                stone1 = x;
                                stage++;
                            } else {
                                move("left", 8);
                            }
                            x++;
                        }
                        else {
                            move("right", 40);
                            stage = 3;
                        }
                    }

            case 4: move("right", 5);
                    rightFrontDrive.setTargetPosition(-1811); // 90 degrees, concerning the front left wheel
                    rightRearDrive.setTargetPosition( -1811);
                    while(leftFrontDrive.isBusy() || leftRearDrive.isBusy() || rightFrontDrive.isBusy() || rightRearDrive.isBusy()){
                        sleep(0);
                    }
                    intakeLeft.setPower(-1);
                    intakeLeftBack.setPower(-1);
                    intakeRight.setPower(1);
                    intakeRightBack.setPower(1);
                    move("forward", 2);
                    intakeLeft.setPower(0);
                    intakeLeftBack.setPower(0);
                    intakeRight.setPower(0);
                    intakeRightBack.setPower(0);
                    while(leftFrontDrive.isBusy() || leftRearDrive.isBusy() || rightFrontDrive.isBusy() || rightRearDrive.isBusy()){
                        sleep(0);
                    }
                    stage++;
                    break;
            case 5: move("left", 12);
                    move("backward", 30 + 4 + ((block1 - 1) * 8));
                    leftFrontDrive.setTargetPosition(-1811); // 90 degrees, concerning the back right wheel
                    leftRearDrive.setTargetPosition( -1811);
                    while(leftFrontDrive.isBusy() || leftRearDrive.isBusy() || rightFrontDrive.isBusy() || rightRearDrive.isBusy()){
                        sleep(0);
                    }
                    move("backward", 2);
                    leftGrab.setPosition(1); // Need to trial & error these into shape
                    rightGrab.setPosition(0); 
                    move("forward", 24);
                    leftGrab.setPosition(0); // Need to trial & error these into shape
                    rightGrab.setPosition(1); 
                    move("right", 48);
                    while(!(colors.red > colors.blue && colors.red > colors.green)){
                        move("right", 1);
                    }
                    break;
                }
        /*
        if(colors.red > colors.blue && colors.red > colors.green){
            // Red Line
        } else if(colors.blue > colors.red && colors.blue > colors.green){
            // Blue Line
        } else if(colors.green > colors.red && colors.green > colors.blue){
            // Tiles
        }*/
       
        i++; // inrease tick counter
   
        // Set Telemetry Data
        telemetry.addData("Tick Num", i); 
        telemetry.addData("Stage", stage);
        telemetry.addData("Status", "Running");
        telemetry.update();
        }
    }
}