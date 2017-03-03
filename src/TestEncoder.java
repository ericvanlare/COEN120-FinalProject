import com.ridgesoft.robotics.AnalogInput;
import com.ridgesoft.robotics.PushButton;
import com.ridgesoft.robotics.Servo;

public class TestEncoder implements Runnable {
	private PushButton mButton;
	private Servo mServo;
	private AnalogInput mEncoderInput;

	public TestEncoder(AnalogInput encoderInput, Servo servo, PushButton button) {
		mEncoderInput = encoderInput;
		mServo = servo;
		mButton = button;
	}

	public String toString() {
		return "Test Encoder";
	}

	public void run() {
		mServo.setPosition(100);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}

		int[] samples = new int[100];
		long nextTime = System.currentTimeMillis();
		for(int i=0; i<samples.length; ++i) {
			samples[i] = mEncoderInput.sample();
			nextTime += 5;
			try {
				Thread.sleep(nextTime - System.currentTimeMillis());
			} catch (InterruptedException e) {}
		}

		mServo.setPosition(50);
		while (!mButton.isPressed());
		for(int i=0; i<samples.length; i++) {
			System.out.println(Integer.toString(i*5)+'\t'+samples[i]);
		}
	}
}