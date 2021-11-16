package org.firstinspires.ftc.teamcode.paladins.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Operation to assist with Gamepad actions on DCMotors
 */
public class GamePadMomentaryForwardReverseMotor extends PaladinsComponent {

    private final ButtonControl forwardButtonControl;
    private final ButtonControl reverseButtonControl;

    private final DcMotor motor;
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
     * @param motor               DcMotor to operate on
     * @param forwardButtonControl {@link ButtonControl}
     * @param reverseButtonControl {@link ButtonControl}
     * @param power                power to apply when using gamepad buttons
     * @param showTelemetry        display the power values on the telemetry
     */
    public GamePadMomentaryForwardReverseMotor(PaladinsOpMode opMode, Gamepad gamepad, final DcMotor motor, TouchSensor stopSensor,
                                               ButtonControl forwardButtonControl, ButtonControl reverseButtonControl,
                                               float power, boolean showTelemetry) {
        super(opMode);

        this.gamepad = gamepad;
        this.motor = motor;
        this.forwardButtonControl = forwardButtonControl;
        this.reverseButtonControl = reverseButtonControl;
        this.motorPower = power;
        this.stopSensor = stopSensor;

        if (showTelemetry) {
            item = opMode.telemetry.addData("Control " + forwardButtonControl.name() + "/" + reverseButtonControl, new Func<Double>() {
                @Override
                public Double value() {
                    return motor.getPower();
                }
            });
            item.setRetained(true);
        } else {
            item = null;
        }
    }


    public GamePadMomentaryForwardReverseMotor(PaladinsOpMode opMode, Gamepad gamepad, DcMotor motor, TouchSensor stopSensor,
                                               ButtonControl forwardButtonControl, ButtonControl reverseButtonControl,
                                               float power) {
        this(opMode, gamepad, motor, stopSensor, forwardButtonControl, reverseButtonControl, power, true);
    }

    /**
     * Update motors with latest gamepad state
     */
    public void update() {
        if (buttonPressed(gamepad, forwardButtonControl)) {
            motor.setPower(motorPower);
        } else  if (stopSensor != null && stopSensor.isPressed()) {
            motor.setPower(0.0);
        } else if (buttonPressed(gamepad, reverseButtonControl)) {
            motor.setPower(-motorPower);
        } else {
            motor.setPower(0.0);
        }
    }
}
