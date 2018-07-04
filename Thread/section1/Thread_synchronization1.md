## 线程同步与线程安全一 ##

### 一、问题引入 ###
需求：

* 某电影院目前正在上映贺岁大片(红高粱,少林寺传奇藏经阁)，共有100张票，而它有3个售票窗口售票，请设计一个程序模拟该电影院售票。

实现代码如下：

SellTicket类：

	public class SellTicket implements Runnable {
		// 定义100张票
		private int tickets = 100;
		
		@Override
		public void run() {
			while (true) {
				// t1,t2,t3三个线程
				// 这一次的tickets = 1;
				if (tickets > 0) {
					// 为了模拟更真实的场景，我们稍作休息
					try {
						Thread.sleep(100); //t1进来了并休息，t2进来了并休息，t3进来了并休息，
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	
					System.out.println(Thread.currentThread().getName() + "正在出售第"
							+ (tickets--) + "张票");
					//窗口1正在出售第1张票,tickets=0
					//窗口2正在出售第0张票,tickets=-1
					//窗口3正在出售第-1张票,tickets=-2
				}
			}
		}
	}

测试类：

	public class SellTicketDemo {
		public static void main(String[] args) {
			// 创建资源对象
			SellTicket st = new SellTicket();
	
			// 创建三个线程对象
			Thread t1 = new Thread(st, "窗口1");
			Thread t2 = new Thread(st, "窗口2");
			Thread t3 = new Thread(st, "窗口3");
	
			// 启动线程
			t1.start();
			t2.start();
			t3.start();
		}
	}

运行结果如下：

![](https://i.imgur.com/VSTxmcd.png)

很明显，出现一张票被几个窗口出售的现象。这是随机性和延迟导致的，由于访问共享资源。所以需要线程同步。


### 二、使用synchronized实现同步 ###

#### （1）测试synchronized同步代码块 ####
同步的好处：

* 同步的出现解决了多线程的安全问题。

同步的弊端：

* 当线程相当多时，因为每个线程都会去判断同步上的锁，这是很耗费资源的，无形中会降低程序的运行效率。

SellTicket类：

	public class SellTicket implements Runnable {
	
		// 定义100张票
		private int tickets = 100;
	
		// 定义同一把锁
		private Object obj = new Object();
	
		@Override
		public void run() {
			while (true) {
				// t1,t2,t3都能走到这里
				// 假设t1抢到CPU的执行权，t1就要进来
				// 假设t2抢到CPU的执行权，t2就要进来,发现门是关着的，进不去。所以就等着。
				// 门(开,关)
				synchronized (obj) { // 发现这里的代码将来是会被锁上的，所以t1进来后，就锁了。(关)
					if (tickets > 0) {
						try {
							Thread.sleep(100); // t1就睡眠了
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName()
								+ "正在出售第" + (tickets--) + "张票 ");
						//窗口1正在出售第100张票
					}
				} //t1就出来可，然后就开门。(开)
			}
		}
	}

测试类：

	public class SellTicketDemo {
		public static void main(String[] args) {
			// 创建资源对象
			SellTicket st = new SellTicket();
	
			// 创建三个线程对象
			Thread t1 = new Thread(st, "窗口1");
			Thread t2 = new Thread(st, "窗口2");
			Thread t3 = new Thread(st, "窗口3");
	
			// 启动线程
			t1.start();
			t2.start();
			t3.start();
		}
	}

#### （2）同步方法 ####
详见src\scut\hujie_11中代码。