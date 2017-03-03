import com.ridgesoft.robotics.AnalogInput;
import com.ridgesoft.robotics.ShaftEncoder;

public class AnalogShaftEncoder extends Thread implements ShaftEncoder, DirectionListener{
	private AnalogInput mInput;
	private int mLowThreshold;
	private int mHighThreshold;
	private boolean mIsForward;
	private int mCounts;
	private int mPeriod;

	public void updateDirection(boolean isForward) {
		mIsForward = isForward;
	}

	public int getRate() {
		return 0;
	}

	public int getCounts() {
		return mCounts;
	}

	public void run() {
		try {
			//take initial sensor sample
			boolean wasHigh = false;
			if (mInput.sample() > mLowThreshold)
				wasHigh = true;
			while (true) {
				//sample the sensor and count spoke edges
				int value = mInput.sample();
				if (wasHigh) {
					if (value < mLowThreshold) {
						if (mIsForward)
							mCounts++;
						else
							mCounts--;
						wasHigh = false;
					}
				}
				else {
					if (value > mHighThreshold) {
						if (mIsForward)
							mCounts++;
						else
							mCounts--;
						wasHigh = true;
					}
				}
				Thread.sleep(mPeriod);
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public AnalogShaftEncoder(AnalogInput input, int lowThreshold, int highThreshold, int period, int threadPriority) {
		mInput = input;
		mLowThreshold = lowThreshold;
		mHighThreshold = highThreshold;
		mPeriod = period;
		mIsForward = true;
		setPriority(threadPriority);
		setDaemon(true);
		start();
	}
}