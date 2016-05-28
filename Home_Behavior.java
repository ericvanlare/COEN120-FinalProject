public class Home_Behavior implements Runnable {
    //the one sensor threads we need
    private Home_FSM mFSM;
	private Thread mThread;
	private BlockingQueue mBuf;

    public Home_Behavior(Home_FSM hFSM, BlockingQueue buffer){
        mFSM = hFSM;
		mThread = new Thread(mFSM);
		mThread.start();
		mBuf = buffer;
    }

    public void run(){
        //possibly while current position is not equal to destination
        while(true){
			int e = mBuf.get();
            mFSM.dispatch(e);//changes the event in the home FSM
        }
    }
    
    public String toString() {
        return "Homing Behavior";
    }
}