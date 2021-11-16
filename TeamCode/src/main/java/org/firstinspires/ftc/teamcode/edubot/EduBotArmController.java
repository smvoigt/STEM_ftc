package org.firstinspires.ftc.teamcode.edubot;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.paladins.common.ButtonControl;
import org.firstinspires.ftc.teamcode.paladins.common.PaladinsComponent;
import org.firstinspires.ftc.teamcode.paladins.common.PaladinsOpMode;


/**
 * Operation to assist with Gamepad actions on DCMotors
 */
public class EduBotArmController extends PaladinsComponent {

    private final ButtonControl armUpButtonControl;
    private final ButtonControl armDownButtonControl;
    private final ButtonControl armClawOpenControl;
    private final ButtonControl armClawCloseControl;

    private final DcMotor armMotor;
    private final Servo clawServo;
    private final Gamepad gamepad;
    private final TouchSensor stopSensor;
    private final float motorPower;
    private final Telemetry.Item item;
    private boolean showtelemetry = false;

    /**
     * Constructor for operation.  Telemetry enabled by default.
     *
     * @param opMode
     * @param gamepad              Gamepad
     * @param config               EduBotConfiguration
     * @param power                power to apply when using gamepad buttons
     * @param showTelemetry        display the power values on the telemetry
     */
    public EduBotArmController(PaladinsOpMode opMode, Gamepad gamepad, EduBotConfiguration config,
                               float power, boolean showTelemetry) {
        super(opMode);

        this.gamepad = gamepad;
        this.armMotor = config.armMotor;
        this.clawServo = config.armServo;
        this.armUpButtonControl = ButtonControl.DPAD_UP;
        this.armDownButtonControl = ButtonControl.DPAD_DOWN;
        this.armClawOpenControl = ButtonControl.DPAD_LEFT;
        this.armClawCloseControl = ButtonControl.DPAD_RIGHT;
        this.motorPower = power;
        this.stopSensor = config.touchSensor;

        if (showTelemetry) {
            item = opMode.telemetry.addData("Arm " + armUpButtonControl.name() + "/" + armDownButtonControl.name(), new Func<Double>() {
                @Override
                public Double value() {
                    return armMotor.getPower();
                }
            });
            item.setRetained(true);
        } else {
            item = null;
        }
    }


    public EduBotArmController(PaladinsOpMode opMode, Gamepad gamepad, EduBotConfiguration config,
                                                              float power) {
        this(opMode, gamepad, config,  power, true);
    }

    /**
     * Update motors with latest gamepad state
     */
    public void update() {
        // Arm
        if (buttonPressed(gamepad, armUpButtonControl)) {
            armMotor.setPower(motorPower);
        } else  if (stopSensor != null && stopSensor.isPressed()) {
            armMotor.setPower(0.0);
        } else if (buttonPressed(gamepad, armDownButtonControl)) {
            armMotor.setPower(-motorPower);
        } else {
            armMotor.setPower(0.0);
        }

        // Claw
        if (buttonPressed(gamepad, armClawOpenControl)) {
            clawServo.setPosition(0.6);
        } else if (buttonPressed(gamepad, armClawCloseControl)) {
            clawServo.setPosition(0.8);
        }
    }
}
