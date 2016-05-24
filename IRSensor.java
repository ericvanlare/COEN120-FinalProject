import com.ridgesoft.intellibrain.IntelliBrain; 
import com.ridgesoft.robotics.sensors.SharpGP2D12;
/*
 *  
 * Initializes the left and right IR sensors.  
 *  
 * The run method adds the appropriate event to the blocking queue when one of the IR sensors is triggered.  
 *  
 *  
 * @author J.P. Ertola and Paul Thurston  
 
    this would work only for home/might need to make another IR class for pounce based on this 
 
 	
 
 *  
 */ 
public class IRSensor extends Thread {
    private BlockingQueue mBuf; 
    private float mThreshold; 
    private int mPeriod;
    private SharpGP2D12 rangeFinderLeft; 
    private SharpGP2D12 rangeFinderRight;
    
    //possibly add another variable to switch between pounce and home?
    public IRSensor(BlockingQueue buf, float threshold, int priority, int period){
        mBuf = buf; 
        mThreshold = threshold; 
        mPeriod = period;
        
        try{ 
            rangeFinderLeft = new SharpGP2D12(IntelliBrain.getAnalogInput(1), null);
            rangeFinderRight = new
            SharpGP2D12(IntelliBrain.getAnalogInput(2), null);
        }catch(Exception e){
		  System.out.print("t1");
		} 
        setPriority(priority); 
        setDaemon(true); 
        start();
	}
	public void run(){
        try{
			while(true){
				rangeFinderLeft.ping(); 
                float distanceLeft = rangeFinderLeft.getDistanceInches();
				rangeFinderRight.ping(); 
                float distanceRight = rangeFinderRight.getDistanceInches();
				if(distanceLeft < mThreshold && distanceLeft > 0){
                    mBuf.put(Home_FSM.BACKUP_SPIN_RIGHT); 
                }
				if(distanceRight < mThreshold && distanceRight > 0){
					mBuf.put(Home_FSM.BACKUP_SPIN_LEFT);
				}

                Thread.sleep(mPeriod);
			}

		}catch(Throwable t){
			System.out.print("t2");
		}
	}
	public float getLeftDistance(){ 
        rangeFinderLeft.ping();
		return rangeFinderLeft.getDistanceInches();
	}
	public float getRightDistance(){ 
        rangeFinderRight.ping();
		return rangeFinderRight.getDistanceInches();
	}
}