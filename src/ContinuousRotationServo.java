import com.ridgesoft.robotics.Motor;
import com.ridgesoft.robotics.Servo;

public class ContinuousRotationServo implements Motor {
	private Servo mServo;
	private boolean mReverse;
	private int mRange;
	private DirectionListener mDirectionListener;

	public ContinuousRotationServo(Servo servo,
							boolean reverse, int range) {
		mServo = servo;
		mReverse = reverse;
		mRange = range;
		mDirectionListener = null;
	}

	public ContinuousRotationServo(Servo servo,
							boolean reverse, int range,
							DirectionListener directionListener) {
		mServo = servo;
		mReverse = reverse;
		mRange = range;
		mDirectionListener = directionListener;
	}

	public void setDirectionListener(
							DirectionListener directionListener) {
		mDirectionListener = directionListener;
	}

	public void brake() {
		// servos don't provide braking, just turn the power off
		setPower(Motor.STOP);
	}

	public void stop() {
		// servos don't provide braking, just turn the power off
		setPower(Motor.STOP);
	}

	public void setPower(int power) {
		if (mDirectionListener != null)
			if (power != 0)
				mDirectionListener.updateDirection(power > 0);
			if (mReverse)
				power = -power;
			if (power == 0) {
				mServo.off();
				return;
			}
			else if (power > Motor.MAX_FORWARD)
				power = Motor.MAX_FORWARD;
			else if (power < Motor.MAX_REVERSE)
				power = Motor.MAX_REVERSE;
			mServo.setPosition(
						(power * mRange) / Motor.MAX_FORWARD + 50);
	}
}