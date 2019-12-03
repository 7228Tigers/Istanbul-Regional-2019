package frc.robot;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;

public class Levitate {
    private XboxController controller;
    private PWMTalonSRX rearMotor;
    private PWMTalonSRX frontMotor;
    private PWMTalonSRX moveMotor;
    private Timer timer;

    public Levitate(XboxController controller, PWMTalonSRX frontMotor, PWMTalonSRX rearMotor, PWMTalonSRX moveMotor,Timer timer) {
        this.controller = controller;
        this.frontMotor = frontMotor;
        this.rearMotor = rearMotor;
        this.moveMotor = moveMotor;
        this.timer = timer;
    }

    public void elevate () {
        // Higher both motors when pressed A
        if (controller.getAButton()) {
            frontMotor.set(1.0);
            rearMotor.set(1.0);
        } else if (controller.getBButton()) {
            frontMotor.set(-1.0);
            rearMotor.set(-0.9);
        } else if (controller.getXButton()) {
            frontMotor.set(1.0);
        } else if (controller.getYButton()) {
            rearMotor.set(1.0);
        } else if (!controller.getAButton() && !controller.getBButton()) {
            frontMotor.set(0);
            rearMotor.set(0);
        } 
    }

    public void elevateAutomatic(Timer timer) {
        if (timer.get() < 4) {
            frontMotor.set(-1.0);
            rearMotor.set(-0.9);
        } else if (timer.get() > 5 && timer.get() < 8) {
            frontMotor.set(0);
            rearMotor.set(0);
            moveMotor.set(-1.0);
        } else if (timer.get() > 8 && timer.get() < 13) {
            frontMotor.set(1.0);
            moveMotor.set(0);
        } else if (timer.get() > 14 && timer.get() < 20) {
            moveMotor.set(-1.0);
            frontMotor.set(0);
        } else if (timer.get() > 20 && timer.get() < 24) {
            moveMotor.set(0);
            rearMotor.set(1.0);
        } else {
            frontMotor.set(0);
            rearMotor.set(0);
            moveMotor.set(0);
        }
    }
}