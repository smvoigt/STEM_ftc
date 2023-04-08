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
public class CharlesArmController extends PaladinsComponent {

    private final ButtonControl armUpButtonControl;
    private final ButtonControl armDownButtonControl;
    private final ButtonControl armClawOpenControl;
    private final ButtonControl armClawCloseControl;

    private final DcMotor armMotor;
    private final Servo clawServo;
    private final Gamepad gamepad;
    private final TouchSensor stopSensor;
    private final float motorPower;
    private final Telemetry.Item armPowerItem;
    private final Telemetry.Item armPosItem;
    private final Telemetry.Item servePowerItem;
    private boolean showtelemetry = false;

    private float clawPositions[] = {0.2f, 0.3f, 0.5f, 0.6f, 0.8f, 1.0f};
    private int clawPosition = 0;
    private ButtonControl lastClawButton = null;

    /**
     * Constructor for operation.  Telemetry enabled by default.
     *
     * @param opMode
     * @param gamepad       Gamepad
     * @param config        EduBotConfiguration
     * @param power         power to apply when using gamepad buttons
     * @param showTelemetry display the power values on the telemetry
     */
    public CharlesArmController(PaladinsOpMode opMode, Gamepad gamepad, CharlesConfiguration config,
                                float power, boolean showTelemetry) {
        super(opMode);

        this.gamepad = gamepad;
        this.armMotor = config.armMotor;
        this.clawServo = config.armServo;
        this.armUpButtonControl = ButtonControl.Y;
        this.armDownButtonControl = ButtonControl.X;
        this.armClawOpenControl = ButtonControl.A;
        this.armClawCloseControl = ButtonControl.B;
        this.motorPower = power;
        this.stopSensor = config.touchSensor;

        if (showTelemetry) {
            armPowerItem = opMode.telemetry.addData("Arm " + armUpButtonControl.name() + "/" + armDownButtonControl.name(), new Func<Double>() {
                @Override
                public Double value() {
                    return armMotor.getPower();
                }
            });
            armPowerItem.setRetained(true);

            armPosItem = opMode.telemetry.addData("Arm Pos" + armUpButtonControl.name() + "/" + armDownButtonControl.name(), new Func<Integer>() {
                @Override
                public Integer value() {
                    return armMotor.getCurrentPosition();
                }
            });
            armPosItem.setRetained(true);
            
            servePowerItem = opMode.telemetry.addData("servo " + armClawOpenControl.name() + "/" + armClawCloseControl.name(), new Func<Double>() {
                @Override
                public Double value() {
                    return clawServo.getPosition();
                }
            });
            servePowerItem.setRetained(true);            
        } else {
            armPowerItem = null;
            armPosItem = null;
            servePowerItem = null;
        }
    }


    public CharlesArmController(PaladinsOpMode opMode, Gamepad gamepad, CharlesConfiguration config,
                                float power) {
        this(opMode, gamepad, config, power, true);
    }

    /**
     * Update motors with latest gamepad state
     */
    public void update() {
        // Arm
        if (buttonPressed(gamepad, armUpButtonControl)) {
            if (armMotor.getCurrentPosition() < 600) {
                armMotor.setPower(motorPower);
            } else if (armMotor.getCurrentPosition() < 700) {
                armMotor.setPower(motorPower/2.0);
            } else {
                armMotor.setPower(0.0);
            }
        } else if (stopSensor != null && stopSensor.isPressed()) {
            armMotor.setPower(0.0);
            armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } else if (buttonPressed(gamepad, armDownButtonControl)) {
            armMotor.setPower(-motorPower);
        } else {
            armMotor.setPower(0.0);
        }

        // Claw
        if (buttonPressed(gamepad, armClawOpenControl)) {
            if (lastClawButton != armClawOpenControl) {
                clawPosition++;
            }
            lastClawButton = armClawOpenControl;
        } else if (buttonPressed(gamepad, armClawCloseControl)) {
            if (lastClawButton != armClawCloseControl) {
                clawPosition--;
            }
            lastClawButton = armClawCloseControl;
        } else {
            lastClawButton = null;
        }
        clawPosition = clawPosition < 0 ? 0 : (clawPosition >= clawPositions.length) ? clawPositions.length - 1 : clawPosition;
        clawServo.setPosition(clawPositions[clawPosition]);
    }
}
