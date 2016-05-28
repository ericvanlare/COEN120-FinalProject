/* Thread run from MyBot.java to initialize Pounce Behavior*/

public class Pounce_Behavior implements Runnable{

    //the one sensor threads we need
    private Pounce_FSM pounceFSM;
	private BlockingQueue mBuf;

    public Pounce_Behavior(Pounce_FSM pFSM, BlockingQueue buffer){
        mFSM = pFSM;
		mBuf = buffer;
		mThread = new Thread(mFSM);
		mThread.start();
    }
    
    public void run(){
        while(true) {
			int e = mBuf.get();
			//System.out.println(e);
            mFSM.dispatch(e);//changes the event in the home FSM
		}
    }
    
    public String toString() {
        return "Pouncing Behavior"
    }

}