/* Thread run from MyBot.java to initialize Pounce Behavior*/

public class Pounce_Behavior implements Runnable{

    //the one sensor threads we need
    private Pounce_FSM mFSM;
	private Thread mThread;
	private BlockingQueue mBuf;

    public Pounce_Behavior(Pounce_FSM pFSM, BlockingQueue buffer){
        mFSM = pFSM;
		mBuf = buffer;
		mThread = new Thread(mFSM);
    }
    
    public void run(){
		mThread.start();

		int count = 0;
		long tStart,tEnd;
        while(true) {
			int e = mBuf.get();
			tStart = System.currentTimeMillis();
            mFSM.dispatch(e);//changes the event in the home FSM
			tEnd = System.currentTimeMillis();
			count++;
			System.out.println("Execution: " + (tEnd-tStart));
			System.out.println("Period: " + (tEnd-tStart));
			System.out.println("Count: "+ count);
			System.out.println();
		}
    }
    
    public String toString() {
        return "Pouncing Behavior";
    }

}