/* Should we make this circular?*/

public class BlockingQueue {
	private int[] list;
	private final int maxSize = 10;
	private int inPtr = 0;
	private int outPtr = 0;
	private int count = 0;

	public BlockingQueue() {
		list = new int[maxSize];
	}
	
	public synchronized void waitWhileEmpty(){
		while(count == 0){
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void waitWhileFull(){
		while(count == maxSize){
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public synchronized void put(int x) {
		waitWhileFull();
		list[inPtr] = x;
		inPtr = (inPtr+1)% maxSize;
		count++;
		notifyAll();
	}

	public synchronized int get() {
		waitWhileEmpty();
		int temp = list[outPtr];
		outPtr = (outPtr+1)% maxSize;
		count--;
		notifyAll();
		return temp;
	}
	
	public synchronized void print(){
		System.out.print("[ ");
		for(int i = outPtr; i < inPtr; i++)
			System.out.print(list[i] + " ");
		
		System.out.print(" ]");
	}
}