package org.firstinspires.ftc.teamcode.edubot;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.paladins.common.GamePadSteerDrive;
import org.firstinspires.ftc.teamcode.paladins.common.PaladinsOpMode;


@TeleOp(name = "SteerDrive")
public class CharlesSteerDrive extends PaladinsOpMode {
    private CharlesConfiguration config;
    private GamePadSteerDrive drive;
    CharlesArmController armLift;

    @Override
    protected void onInit() {
        config = CharlesConfiguration.newConfig(hardwareMap, telemetry);

        drive = new GamePadSteerDrive(this, gamepad1, config.leftMotor, config.rightMotor);
        armLift = new CharlesArmController(this, gamepad1, config, 0.4f, true);
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        drive.update();
        armLift.update();
    }
}