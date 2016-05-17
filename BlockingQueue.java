public class BlockingQueue {
	private int[] list;
	private int maxSize = 10;
	private int frontPtr = 0;
	private int endPtr = 0;
	private int count = 0;

	public BlockingQueue() {
		list = new int[maxSize];
	}

	public synchronized void put(int x) {
		while (count >= maxSize) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		list[endPtr++] = x;
		count++;
		notifyAll();
	}

	public synchronized int get() {
		while (count <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		int x = list[frontPtr++];
		count--;
		notifyAll();
		return x;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public int size() {
		return count;
	}
}