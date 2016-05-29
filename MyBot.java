import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.PushButton;
import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.AnalogInput;
import com.ridgesoft.robotics.Servo;
import com.ridgesoft.robotics.ShaftEncoder;
 
public class MyBot {
    public static void main(String[] args) {
        try {
			//variables to communicate between threads and IR setup logistics
	    	final int SAMPLE_RATE = 100;//milliseconds 1 second is too much, 0.5 a second still seems too much
	    	final float SENSOR_THRESHOLD = 5.0f;//inches :better farther than closer, especially if these things are sensitive
	    	BlockingQueue buffer = new BlockingQueue();

            Display display = IntelliBrain.getLcdDisplay();
            PushButton startButton = IntelliBrain.getStartButton();
            PushButton stopButton = IntelliBrain.getStopButton();

            AnalogInput leftWheelInput = IntelliBrain.getAnalogInput(4);
			AnalogInput rightWheelInput = IntelliBrain.getAnalogInput(5);

			Servo leftServo = IntelliBrain.getServo(1);
			Servo rightServo = IntelliBrain.getServo(2);

			ShaftEncoder leftEncoder = new AnalogShaftEncoder(leftWheelInput, 250, 750, 30, Thread.MAX_PRIORITY);
			ShaftEncoder rightEncoder = new AnalogShaftEncoder(rightWheelInput, 250, 750, 30, Thread.MAX_PRIORITY);

			Localizer localizer = new OdometricLocalizer(leftEncoder, rightEncoder, 2.65f, 4.55f, 16, Thread.MAX_PRIORITY-1, 30);

			ContinuousRotationServo leftMotor =
						new ContinuousRotationServo(leftServo, false, 14,
						(DirectionListener)leftEncoder);
			ContinuousRotationServo rightMotor =
						new ContinuousRotationServo(rightServo, true, 14,
						(DirectionListener)rightEncoder);

			Navigator navigator = new DifferentialDriveNavigator(
												leftMotor, rightMotor,
												localizer,
												8, 6, 25.0f, 0.5f, 0.08f,
												Thread.MAX_PRIORITY-2, 300);//originally -2, but I think it is getting in the way of the IR sensor 50 period

			Home_FSM hFSM = new Home_FSM(navigator, localizer);
			IRSensor irs = new IRSensor(buffer, SENSOR_THRESHOLD, Thread.MAX_PRIORITY-1, SAMPLE_RATE);
 
			Runnable functions[] = new Runnable[] {
				new Home_Behavior(hFSM, buffer),
            };
            startButton.waitReleased();
            IntelliBrain.setTerminateOnStop(false);
            int selectedFunction = 0;
            display.print(0, "Funtion");
            display.print(1, functions[selectedFunction].toString());
  
            while (!startButton.isPressed()) {
                if (stopButton.isPressed()) {
                    if (++selectedFunction >= functions.length)
                        selectedFunction = 0;
                    display.print(1, functions[selectedFunction].toString());
                    stopButton.waitReleased();
                }
            }
            IntelliBrain.setTerminateOnStop(true);
  
            Screen[] screens = new Screen[] {
				new PoseScreen(localizer),
				new EncoderCountsScreen(leftEncoder, rightEncoder),
				new WheelSensorScreen(leftWheelInput, rightWheelInput),
                new StaticTextScreen("MyBot", "Version 0.3"),
            };
  
            new ScreenManager(display, screens, IntelliBrain.getThumbWheel(), Thread.MIN_PRIORITY, 500);
  
            functions[selectedFunction].run();
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
}