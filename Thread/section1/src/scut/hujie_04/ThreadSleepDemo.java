package scut.hujie_04;

/*
 * Ïß³ÌÐÝÃß
 *		public static void sleep(long millis)
 */
public class ThreadSleepDemo {
	public static void main(String[] args) {
		ThreadSleep ts1 = new ThreadSleep();
		ThreadSleep ts2 = new ThreadSleep();
		ThreadSleep ts3 = new ThreadSleep();

		ts1.setName("jack");
		ts2.setName("mary");
		ts3.setName("orange");

		ts1.start();
		ts2.start();
		ts3.start();
	}
}
