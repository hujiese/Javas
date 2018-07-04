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
