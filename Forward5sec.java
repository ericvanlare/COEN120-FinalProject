import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.robotics.Servo;

public class Forward5sec implements Runnable {
	public void run() {
		try {
			Servo leftServo = IntelliBrain.getServo(1);
			Servo rightServo = IntelliBrain.getServo(2);
			leftServo.setPosition(100);
			rightServo.setPosition(0);
			Thread.sleep(5000);
			leftServo.off();
			rightServo.off();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public String toString() {
		return "Forward";
	}
}