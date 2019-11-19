/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="AutonTest", group="TEST")

public class AutonTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftRearDrive = null;
    private DcMotor leftFrontDrive = null;
    private DcMotor rightRearDrive = null;
    private DcMotor rightFrontDrive = null;
    //private DcMotor TEST = null;
   // private DcMotor LiftRotate = null;
    // declare motor speed variables
    double RF; double LF; double RR; double LR; double T;// double Lift;
    // declare joystick position variables 
    double X1; double Y1; double X2; double Y2; boolean A;// boolean ButtonX;
    // operational EnumConstantNotPresentException
    double joyScale = 1.0;
    double motorMax = 1.0; // Limit Motor power to this value for Andymark

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    double left_x = 0;     double left_y = 0;
    double right_x = 0;    double right_y = 0;
    
    boolean a = false;     boolean b = false;
    boolean x = false;     boolean y = false;
    
    int stage = 0;

        leftFrontDrive = hardwareMap.dcMotor.get("leftFrontDrive");
        leftRearDrive = hardwareMap.dcMotor.get("leftRearDrive");
        rightFrontDrive = hardwareMap.dcMotor.get("rightFrontDrive");
        rightRearDrive = hardwareMap.dcMotor.get("rightRearDrive");
        //TEST = hardwareMap.dcMotor.get("TEST");
        //LiftRotate = hardwareMap.dcMotor.get("LiftRotate");

        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftRearDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);
        //TEST.setDirection(DcMotor.Direction.FORWARD);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //TEST.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Setup a variable for each drive wheel to save power level for telemetry
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            LF = 0; RF = 0; LR = 0; RR = 0; //Lift = 0;

            Y1 = -right_y * joyScale;
            X1 = right_x * joyScale;
            Y2 = -left_y * joyScale;
            X2 = left_x * joyScale;
            //A = gamepad1.x;
            //if(A){
            // T = 1;
            // }else{
            // T = 0;
            // }

            LF += X2; RF += X2; LR += X2; RR += X2;//Forward / Backward Movement
            LF += Y2; RF -= Y2; LR -= Y2; RR += Y2;//Side-to-Side Movement
            LF += X1; RF += X1; LR -= X1; RR -= X1;// Rotational Movement
            
            LF = Math.max(-motorMax, Math.min(LF, motorMax));
            RF = Math.max(-motorMax, Math.min(RF, motorMax));
            LR = Math.max(-motorMax, Math.min(LR, motorMax));
            RR = Math.max(-motorMax, Math.min(RR, motorMax));
            
            leftFrontDrive.setPower(LF);
            leftRearDrive.setPower(LR);
            rightFrontDrive.setPower(RF);
            rightRearDrive.setPower(RR);
            //TEST.setPower(T);
            
            telemetry.addData("LF", "%.3f", RR);
            telemetry.addData("RF", "%.3f", RF);
            telemetry.addData("LR", "%.3f", LR);
            telemetry.addData("RR", "%.3f", RR);
            telemetry.addData("", "");
            telemetry.addData("LF Encoder", leftFrontDrive.getTargetPosition());
            telemetry.addData("LR Encoder", leftRearDrive.getTargetPosition());
            telemetry.addData("RF Encoder", rightFrontDrive.getTargetPosition());
            telemetry.addData("RR Encoder", rightRearDrive.getTargetPosition());
            //telemetry.addData("TEST", "%.3f", TEST);
            
            if(stage == 0){
                            left_y = 0.5;
                            
                            if(runtime.toString() == "3.0"){
                                stage++;
                            }
            } else if (stage == 1){
                
            }
        }
    }
}
