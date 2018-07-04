## 多线程 API ##
### 一、多线程概述 ###
进程：

* 正在运行的程序，是系统进行资源分配和调用的独立单位。
* 每一个进程都有它自己的内存空间和系统资源。

线程：

* 是进程中的单个顺序控制流，是一条执行路径
* 一个进程如果只有一条执行路径，则称为单线程程序。
* 一个进程如果有多条执行路径，则称为多线程程序。

Java程序的运行原理：

* 由java命令启动JVM，JVM启动就相当于启动了一个进程。
* 接着有该进程创建了一个主线程去调用main方法。

jvm虚拟机的启动是多线程的，原因是垃圾回收线程也要先启动，否则很容易会出现内存溢出。现在的垃圾回收线程加上前面的主线程，最低启动了两个线程，所以，jvm的启动其实是多线程的。

### 二、Thread ###

#### （1）继承Thread类实现多线程： ####

步骤：

* 自定义类MyThread继承Thread类。
* MyThread类里面重写run()?
* 创建对象
* 启动线程

		public class MyThreadDemo {
			public static void main(String[] args) {
				// 创建两个线程对象
				MyThread my1 = new MyThread();
				MyThread my2 = new MyThread();
		
				my1.start();
				my2.start();
			}
		}

MyThread类：

	public class MyThread extends Thread {
	
		@Override
		public void run() {
			// 自己写代码
			// System.out.println("好好学习，天天向上");
			// 一般来说，被线程执行的代码肯定是比较耗时的。所以我们用循环改进
			for (int x = 0; x < 200; x++) {
				System.out.println(x);
			}
		}
	
	}


#### （2）获取线程对象名称 ####

获取线程对象名称：

* public final String getName():获取线程的名称。


设置线程对象名称：

* public final void setName(String name):设置线程的名称

不是Thread类的子类中获取线程对象名称：

* public static Thread currentThread():返回当前正在执行的线程对象Thread.currentThread().getName()

		public class MyThreadDemo {
			public static void main(String[] args) {
				// 创建线程对象
				//无参构造+setXxx()
				// MyThread my1 = new MyThread();
				// MyThread my2 = new MyThread();
				// //调用方法设置名称
				// my1.setName("mary");
				// my2.setName("jack");
				// my1.start();
				// my2.start();
				
				//带参构造方法给线程起名字
				// MyThread my1 = new MyThread("mary");
				// MyThread my2 = new MyThread("jack");
				// my1.start();
				// my2.start();
				
				//public static Thread currentThread():返回当前正在执行的线程对象
				System.out.println(Thread.currentThread().getName());
			}
		}


MyThread类：

	public class MyThread extends Thread {
	
		public MyThread() {
		}
		
		public MyThread(String name){
			super(name);
		}
	
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				System.out.println(getName() + ":" + x);
			}
		}
	}


### 三、线程调度 ###

线程有两种调度模型：

* 分时调度模型：所有线程轮流使用 CPU 的使用权，平均分配每个线程占用 CPU 的时间片。
* 抢占式调度模型：优先让优先级高的线程使用 CPU，如果线程的优先级相同，那么会随机选择一个，优先级高的线程获取的 CPU 时间片相对多一些。 Java使用的是抢占式调度模型。

获取线程对象的优先级：

* public final int getPriority():返回线程对象的优先级

设置线程对象的优先级呢：

* public final void setPriority(int newPriority)：更改线程的优先级。

注意：

* 线程默认优先级是5。
* 线程优先级的范围是：1-10。
* 线程优先级高仅仅表示线程获取的 CPU时间片的几率高，但是要在次数比较多，或者多次运行的时候才能看到比较好的效果。

		public class ThreadPriorityDemo {
			public static void main(String[] args) {
				ThreadPriority tp1 = new ThreadPriority();
				ThreadPriority tp2 = new ThreadPriority();
				ThreadPriority tp3 = new ThreadPriority();
		
				tp1.setName("东方不败");
				tp2.setName("岳不群");
				tp3.setName("林平之");
		
				// 获取默认优先级
				// System.out.println(tp1.getPriority());
				// System.out.println(tp2.getPriority());
				// System.out.println(tp3.getPriority());
				
				//设置正确的线程优先级
				tp1.setPriority(10);
				tp2.setPriority(1);
		
				tp1.start();
				tp2.start();
				tp3.start();
			}
		}

ThreadPriority类：

	public class ThreadPriority extends Thread {
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				System.out.println(getName() + ":" + x);
			}
		}
	}


### 四、线程控制 ###

#### （1）线程休眠 ####

* public static void sleep(long millis)

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

ThreadSleep类：

	public class ThreadSleep extends Thread {
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				System.out.println(getName() + ":" + x + ",日期：" + new Date());
				// 睡眠
				// 困了，我稍微休息1秒钟
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

#### （2）线程加入 ####

* public final void join():阻塞，等待该线程终止。

		public class ThreadJoinDemo {
			public static void main(String[] args) {
				ThreadJoin tj1 = new ThreadJoin();
				ThreadJoin tj2 = new ThreadJoin();
				ThreadJoin tj3 = new ThreadJoin();
		
				tj1.setName("jack");
				tj2.setName("mary");
				tj3.setName("orange");
		
				tj1.start();
				try {
					tj1.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				tj2.start();
				tj3.start();
			}
		}

ThreadJoin类：

	public class ThreadJoin extends Thread {
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				System.out.println(getName() + ":" + x);
			}
		}
	}


#### （3）线程礼让 ####

* public static void yield():暂停当前正在执行的线程对象，并执行其他线程。 让多个线程的执行更和谐，但是不能靠它保证线程连续轮流执行。

		public class ThreadYieldDemo {
			public static void main(String[] args) {
				ThreadYield ty1 = new ThreadYield();
				ThreadYield ty2 = new ThreadYield();
		
				ty1.setName("jack");
				ty2.setName("orange");
		
				ty1.start();
				ty2.start();
			}
		}

ThreadYield类：

	public class ThreadYield extends Thread {
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				System.out.println(getName() + ":" + x);
				Thread.yield();
			}
		}
	}


#### （4）后台线程 ####

* public final void setDaemon(boolean on):将该线程标记为守护线程或用户线程。当正在运行的线程都是守护线程时，Java 虚拟机退出。 该方法必须在启动线程前调用。

		public class ThreadDaemonDemo {
			public static void main(String[] args) {
				ThreadDaemon td1 = new ThreadDaemon();
				ThreadDaemon td2 = new ThreadDaemon();
		
				td1.setName("关羽");
				td2.setName("张飞");
		
				// 设置收获线程
				td1.setDaemon(true);
				td2.setDaemon(true);
		
				td1.start();
				td2.start();
		
				Thread.currentThread().setName("刘备");
				for (int x = 0; x < 5; x++) {
					System.out.println(Thread.currentThread().getName() + ":" + x);
				}
			}
		}

ThreadDaemon类：

	public class ThreadDaemon extends Thread {
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				System.out.println(getName() + ":" + x);
			}
		}
	}


#### （5）中断线程 ####

* public final void stop():让线程停止，过时了，但是还可以使用。
* public void interrupt():中断线程。 把线程的状态终止，并抛出一个InterruptedException。

		public class ThreadStopDemo {
			public static void main(String[] args) {
				ThreadStop ts = new ThreadStop();
				ts.start();
		
				// 超过三秒不醒过来，就终止线程
				try {
					Thread.sleep(3000);
					// ts.stop();
					ts.interrupt();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

ThreadStop类：

	public class ThreadStop extends Thread {
		@Override
		public void run() {
			System.out.println("开始执行：" + new Date());
	
			// 我要休息10秒钟，亲，不要打扰我哦
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
				System.out.println("线程被终止了");
			}
	
			System.out.println("结束执行：" + new Date());
		}
	}


### 五、线程生命周期 ###
![](https://i.imgur.com/yAGUE2S.png)

### 六、Runnable实现线程 ###

步骤：

* 自定义类MyRunnable实现Runnable接口
* 重写run()方法
* 创建MyRunnable类的对象
* 创建Thread类的对象，并把C步骤的对象作为构造参数传递

好处：

* 可以避免由于Java单继承带来的局限性。
* 适合多个相同程序的代码去处理同一个资源的情况，把线程同程序的代码，数据有效分离，较好的体现了面向对象的设计思想。

		public class MyRunnableDemo {
			public static void main(String[] args) {
				// 创建MyRunnable类的对象
				MyRunnable my = new MyRunnable();
		
				// Thread(Runnable target, String name)
				Thread t1 = new Thread(my, "jack");
				Thread t2 = new Thread(my, "orange");
		
				t1.start();
				t2.start();
			}
		}

MyRunnable类：

	public class MyRunnable implements Runnable {
	
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				// 由于实现接口的方式就不能直接使用Thread类的方法了,但是可以间接的使用
				System.out.println(Thread.currentThread().getName() + ":" + x);
			}
		}
	}

### 七、两种创建线程方法对比 ###
![](https://i.imgur.com/Qi9kUOi.png)


### 八、综合示例 ###
需求：

* 某电影院目前正在上映贺岁大片(红高粱,少林寺传奇藏经阁)，共有100张票，而它有3个售票窗口售票，请设计一个程序模拟该电影院售票。


#### （1）继承Thread类实现 ####

SellTicket类：

	public class SellTicket extends Thread {
	
		// 定义100张票
		// private int tickets = 100;
		// 为了让多个线程对象共享这100张票，我们其实应该用静态修饰
		private static int tickets = 100;
	
		@Override
		public void run() {
			// 定义100张票
			// 每个线程进来都会走这里，这样的话，每个线程对象相当于买的是自己的那100张票，这不合理，所以应该定义到外面
			// int tickets = 100;
	
			// 是为了模拟一直有票
			while (true) {
				if (tickets > 0) {
					System.out.println(getName() + "正在出售第" + (tickets--) + "张票");
				}
			}
		}
	}

测试类：

	public class SellTicketDemo {
		public static void main(String[] args) {
			// 创建三个线程对象
			SellTicket st1 = new SellTicket();
			SellTicket st2 = new SellTicket();
			SellTicket st3 = new SellTicket();
	
			// 给线程对象起名字
			st1.setName("窗口1");
			st2.setName("窗口2");
			st3.setName("窗口3");
	
			// 启动线程
			st1.start();
			st2.start();
			st3.start();
		}
	}


#### （2）实现Runnable接口的方式实现 ####

SellTicket类：

	public class SellTicket implements Runnable {
		// 定义100张票
		private int tickets = 100;
	
		@Override
		public void run() {
			while (true) {
				if (tickets > 0) {
					System.out.println(Thread.currentThread().getName() + "正在出售第"
							+ (tickets--) + "张票");
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

