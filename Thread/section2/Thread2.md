## 多线程 API ##
### 一、线程的状态转换 ###
![](https://i.imgur.com/7DSlzDW.png)

### 二、线程组 ###

线程组： 把多个线程组合到一起，它可以对一批线程进行分类管理，Java允许程序直接对线程组进行控制。

MyRunnable类：

	public class MyRunnable implements Runnable {
	
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				System.out.println(Thread.currentThread().getName() + ":" + x);
			}
		}
	
	}

测试类：

	public class ThreadGroupDemo {
		public static void main(String[] args) {
			// method1();
	
			// 我们如何修改线程所在的组呢?
			// 创建一个线程组
			// 创建其他线程的时候，把其他线程的组指定为我们自己新建线程组
			method2();
	
		}
	
		private static void method2() {
			// ThreadGroup(String name)
			ThreadGroup tg = new ThreadGroup("这是一个新的组");
	
			MyRunnable my = new MyRunnable();
			// Thread(ThreadGroup group, Runnable target, String name)
			Thread t1 = new Thread(tg, my, "jack");
			Thread t2 = new Thread(tg, my, "tom");
			
			System.out.println(t1.getThreadGroup().getName());
			System.out.println(t2.getThreadGroup().getName());
			
			//通过组名称设置后台线程，表示该组的线程都是后台线程
			tg.setDaemon(true);
		}
	
		private static void method1() {
			MyRunnable my = new MyRunnable();
			Thread t1 = new Thread(my, "jack");
			Thread t2 = new Thread(my, "tom");
			// 我不知道他们属于那个线程组,我想知道，怎么办
			// 线程类里面的方法：public final ThreadGroup getThreadGroup()
			ThreadGroup tg1 = t1.getThreadGroup();
			ThreadGroup tg2 = t2.getThreadGroup();
			// 线程组里面的方法：public final String getName()
			String name1 = tg1.getName();
			String name2 = tg2.getName();
			System.out.println(name1);
			System.out.println(name2);
			// 通过结果我们知道了：线程默认情况下属于main线程组
			// 通过下面的测试，你应该能够看到，默任情况下，所有的线程都属于同一个组
			System.out.println(Thread.currentThread().getThreadGroup().getName());
		}
	}

### 三、线程池 ###
程序启动一个新线程成本是比较高的，因为它涉及到要与操作系统进行交互。而使用线程池可以很好的提高性能，尤其是当程序中要创建大量生存期很短的线程时，更应该考虑使用线程池。线程池里的每一个线程代码结束后，并不会死亡，而是再次回到线程池中成为空闲状态，等待下一个对象来使用。

在JDK5之前，我们必须手动实现自己的线程池，从JDK5开始，Java内置支持线程池，JDK5新增了一个Executors工厂类来产生线程池，有如下几个方法：

	public static ExecutorService newCachedThreadPool()
	public static ExecutorService newFixedThreadPool(int nThreads)
	public static ExecutorService newSingleThreadExecutor()

这些方法的返回值是ExecutorService对象，该对象表示一个线程池，可以执行Runnable对象或者Callable对象代表的线程。它提供了如下方法：

	Future<?> submit(Runnable task)
	<T> Future<T> submit(Callable<T> task)

线程池的好处：线程池里的每一个线程代码结束后，并不会死亡，而是再次回到线程池中成为空闲状态，等待下一个对象来使用。

如何实现线程的代码：

* 1、创建一个线程池对象，控制要创建几个线程对象：public static ExecutorService newFixedThreadPool(int nThreads)
* 2、让这种线程池的线程可以执行：可以执行Runnable对象或者Callable对象代表的线程，做一个类实现Runnable接口。
* 3、调用如下方法即可

		Future<?> submit(Runnable task)
		<T> Future<T> submit(Callable<T> task)
* 关闭线程池

测试代码如下：

		public class ExecutorsDemo {
			public static void main(String[] args) {
				// 创建一个线程池对象，控制要创建几个线程对象。
				// public static ExecutorService newFixedThreadPool(int nThreads)
				ExecutorService pool = Executors.newFixedThreadPool(2);
		
				// 可以执行Runnable对象或者Callable对象代表的线程
				pool.submit(new MyRunnable());
				pool.submit(new MyRunnable());
		
				//结束线程池
				pool.shutdown();
			}
		}

MyRunnable类：

	public class MyRunnable implements Runnable {
	
		@Override
		public void run() {
			for (int x = 0; x < 100; x++) {
				System.out.println(Thread.currentThread().getName() + ":" + x);
			}
		}
	
	}

### 四、Callable实现线程 ###

DEMO1：

	public class CallableDemo {
		public static void main(String[] args) {
			//创建线程池对象
			ExecutorService pool = Executors.newFixedThreadPool(2);
			
			//可以执行Runnable对象或者Callable对象代表的线程
			pool.submit(new MyCallable());
			pool.submit(new MyCallable());
			
			//结束
			pool.shutdown();
		}
	}

MyCallable类：

	//Callable:是带泛型的接口。
	//这里指定的泛型其实是call()方法的返回值类型。
	public class MyCallable implements Callable {
	
		@Override
		public Object call() throws Exception {
			for (int x = 0; x < 100; x++) {
				System.out.println(Thread.currentThread().getName() + ":" + x);
			}
			return null;
		}
	
	}

DEMO2（带参数返回值）：

	public class CallableDemo {
		public static void main(String[] args) throws InterruptedException, ExecutionException {
			// 创建线程池对象
			ExecutorService pool = Executors.newFixedThreadPool(2);
	
			// 可以执行Runnable对象或者Callable对象代表的线程
			Future<Integer> f1 = pool.submit(new MyCallable(100));
			Future<Integer> f2 = pool.submit(new MyCallable(200));
	
			// V get()
			Integer i1 = f1.get();
			Integer i2 = f2.get();
	
			System.out.println(i1);
			System.out.println(i2);
	
			// 结束
			pool.shutdown();
		}
	}

MyCallable类：

	/*
	 * 线程求和案例
	 */
	public class MyCallable implements Callable<Integer> {
	
		private int number;
	
		public MyCallable(int number) {
			this.number = number;
		}
	
		@Override
		public Integer call() throws Exception {
			int sum = 0;
			for (int x = 1; x <= number; x++) {
				sum += x;
			}
			return sum;
		}
	
	}


### 五、匿名内部类方式使用多线程 ###

	new Thread(){代码…}.start();
	New Thread(new Runnable(){代码…}).start();

测试代码如下：

	/*
	 * 匿名内部类的格式：
	 * 		new 类名或者接口名() {
	 * 			重写方法;
	 * 		};
	 * 		本质：是该类或者接口的子类对象。
	 */
	public class ThreadDemo {
		public static void main(String[] args) {
			// 继承Thread类来实现多线程
			new Thread() {
				public void run() {
					for (int x = 0; x < 100; x++) {
						System.out.println(Thread.currentThread().getName() + ":"
								+ x);
					}
				}
			}.start();
	
			// 实现Runnable接口来实现多线程
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int x = 0; x < 100; x++) {
						System.out.println(Thread.currentThread().getName() + ":"
								+ x);
					}
				}
			}) {
			}.start();
	
			// 更有难度的
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int x = 0; x < 100; x++) {
						System.out.println("hello" + ":" + x);
					}
				}
			}) {
				public void run() {
					for (int x = 0; x < 100; x++) {
						System.out.println("world" + ":" + x);
					}
				}
			}.start();
		}
	}

### 六、定时器 ###

定时器是一个应用十分广泛的线程工具，可用于调度多个定时任务以后台线程的方式执行。在Java中，可以通过Timer和TimerTask类来实现定义调度的功能

#### Timer： ####

	public Timer()
	public void schedule(TimerTask task, long delay)
	public void schedule(TimerTask task,long delay,long period)

#### TimerTask： ####

	public abstract void run()
	public boolean cancel()

案例一--定时一次：

	public class TimerDemo {
		public static void main(String[] args) {
			// 创建定时器对象
			Timer t = new Timer();
			// 3秒后执行爆炸任务
			// t.schedule(new MyTask(), 3000);
			//结束任务
			t.schedule(new MyTask(t), 3000);
		}
	}
	
	// 做一个任务
	class MyTask extends TimerTask {
	
		private Timer t;
		
		public MyTask(){}
		
		public MyTask(Timer t){
			this.t = t;
		}
		
		@Override
		public void run() {
			System.out.println("beng,爆炸了");
			t.cancel();
		}
	
	}


案例二--定时多次：

	public class TimerDemo2 {
		public static void main(String[] args) {
			// 创建定时器对象
			Timer t = new Timer();
			// 3秒后执行爆炸任务第一次，如果不成功，每隔2秒再继续炸
			t.schedule(new MyTask2(), 3000, 2000);
		}
	}
	
	// 做一个任务
	class MyTask2 extends TimerTask {
		@Override
		public void run() {
			System.out.println("beng,爆炸了");
		}
	}