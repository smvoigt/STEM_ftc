package org.firstinspires.ftc.teamcode.edubot;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.paladins.common.ButtonControl;
import org.firstinspires.ftc.teamcode.paladins.common.GamePadMomentaryForwardReverseMotor;
import org.firstinspires.ftc.teamcode.paladins.common.GamePadToggleMotorWithReverse;
import org.firstinspires.ftc.teamcode.paladins.common.PaladinsOpMode;
import org.firstinspires.ftc.teamcode.paladins.common.TankDrive;


//@TeleOp(name = "EduBot TankDrive")
public class EduBotTeleOpTankDrive extends PaladinsOpMode {
    private EduBotConfiguration config;
    private TankDrive drive;
    EduBotArmController armLift;

    @Override
    protected void onInit() {
        config = EduBotConfiguration.newConfig(hardwareMap, telemetry);

        drive = new TankDrive(this, gamepad1, config.leftMotor, config.rightMotor);
        armLift = new EduBotArmController(this, gamepad1, config,0.2f, true);
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        drive.setPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
        drive.update();
        armLift.update();
    }
}