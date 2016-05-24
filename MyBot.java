import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.PushButton;
import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.AnalogInput;
import com.ridgesoft.robotics.Servo;
import com.ridgesoft.robotics.ShaftEncoder;
 
public class MyBot {
    public static void main(String[] args) {
        try {
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
												Thread.MAX_PRIORITY - 2, 50);
 
			Runnable functions[] = new Runnable[] {
				new Home_Behavior(navigator, localizer),
				new NavigateTriangle(navigator, 16.0f),
				new NavigateSquare(navigator, 16.0f),
				new NavigateFigureEight(navigator, 48.0f, 32.0f),
				new Rotate(navigator),
				new NavigateForward(navigator, 100.0f),
				new TimedForward(leftServo, rightServo, (DirectionListener)leftEncoder, (DirectionListener)rightEncoder, 10000),
				new TimedRotate(leftServo, rightServo, (DirectionListener)leftEncoder, (DirectionListener)rightEncoder, 1800),
				new TestEncoder(leftWheelInput, leftServo, startButton),
                new DoBeep(),
                new DoNothing(),
                new Forward5sec(),
                new Clockwise(),
                new AntiClockwise(),
                new DisplayName(),
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
                new StaticTextScreen("Screen 1", "abcd"),
                new StaticTextScreen("Screen 2", "1234"),
            };
  
            new ScreenManager(display, screens, IntelliBrain.getThumbWheel(), Thread.MIN_PRIORITY, 500);
  
            functions[selectedFunction].run();
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
}