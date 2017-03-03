import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.AnalogInput;

public class WheelSensorScreen implements Screen {
	private AnalogInput mLeftWheelInput;
	private AnalogInput mRightWheelInput;
	
	public WheelSensorScreen(AnalogInput leftWheelInput, AnalogInput rightWheelInput) {
		mLeftWheelInput = leftWheelInput;
		mRightWheelInput = rightWheelInput;
	}

	public void update(Display display) {
		display.print(0, "L Wheel: " + mLeftWheelInput.sample());
		display.print(1, "R Wheel: " + mRightWheelInput.sample());
	}
}