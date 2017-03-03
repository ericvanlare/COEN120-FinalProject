import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.PushButton;
import com.ridgesoft.intellibrain.IntelliBrain;

public class DisplayName implements Runnable {
	public void run() {
		Display display = IntelliBrain.getLcdDisplay();
		display.print(0, "Displaying Name:");
		display.print(1, "Eric Van Lare");
	}

	public String toString() {
		return "Display Name";
	}
}