## 线程同步与线程安全二 ##

### 一、Lock锁 ###

* void lock()： 获取锁。
* void unlock():释放锁。  
* ReentrantLock是Lock的实现类。

SellTicket类：

	public class SellTicket implements Runnable {
	
		// 定义票
		private int tickets = 100;
	
		// 定义锁对象
		private Lock lock = new ReentrantLock();
	
		@Override
		public void run() {
			while (true) {
				try {
					// 加锁
					lock.lock();
					if (tickets > 0) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName()
								+ "正在出售第" + (tickets--) + "张票");
					}
				} finally {
					// 释放锁
					lock.unlock();
				}
			}
		}
	
	}

测试类：

	public class SellTicketDemo {
		public static void main(String[] args) {
			// 创建资源对象
			SellTicket st = new SellTicket();
	
			// 创建三个窗口
			Thread t1 = new Thread(st, "窗口1");
			Thread t2 = new Thread(st, "窗口2");
			Thread t3 = new Thread(st, "窗口3");
	
			// 启动线程
			t1.start();
			t2.start();
			t3.start();
		}
	}


### 二、死锁 ###

同步的弊端：

* 效率低
* 容易产生死锁

死锁：

* 两个或两个以上的线程在争夺资源的过程中，发生的一种相互等待的现象。

MyLock类：

	public class MyLock {
		// 创建两把锁对象
		public static final Object objA = new Object();
		public static final Object objB = new Object();
	}

DieLock类：

	public class DieLock extends Thread {
	
		private boolean flag;
	
		public DieLock(boolean flag) {
			this.flag = flag;
		}
	
		@Override
		public void run() {
			if (flag) {
				synchronized (MyLock.objA) {
					System.out.println("if objA");
					synchronized (MyLock.objB) {
						System.out.println("if objB");
					}
				}
			} else {
				synchronized (MyLock.objB) {
					System.out.println("else objB");
					synchronized (MyLock.objA) {
						System.out.println("else objA");
					}
				}
			}
		}
	}

测试类：

	public class DieLockDemo {
		public static void main(String[] args) {
			DieLock dl1 = new DieLock(true);
			DieLock dl2 = new DieLock(false);
	
			dl1.start();
			dl2.start();
		}
	}

编译运行结果如下：

![](https://i.imgur.com/Kp8i4K1.png)

A和B都在等待执行下面语句，死锁。

### 三、学生类生产者消费者同步案例 ###

#### （1）使用synchronized ####

Student类：

	public class Student {
		String name;
		int age;
	}


生产者SetThread类：

	public class SetThread implements Runnable {
	
		private Student s;
		private int x = 0;
	
		public SetThread(Student s) {
			this.s = s;
		}
	
		@Override
		public void run() {
			while (true) {
				synchronized (s) {
					if (x % 2 == 0) {
						s.name = "jack";//刚走到这里，就被别人抢到了执行权
						s.age = 27;
					} else {
						s.name = "mary"; //刚走到这里，就被别人抢到了执行权
						s.age = 30;
					}
					x++;
				}
			}
		}
	}

消费者GetThread类：

	public class GetThread implements Runnable {
		private Student s;
	
		public GetThread(Student s) {
			this.s = s;
		}
	
		@Override
		public void run() {
			while (true) {
				synchronized (s) {
					System.out.println(s.name + "---" + s.age);
				}
			}
		}
	}

测试类：

	public class StudentDemo {
		public static void main(String[] args) {
			//创建资源
			Student s = new Student();
			
			//设置和获取的类
			SetThread st = new SetThread(s);
			GetThread gt = new GetThread(s);
	
			//线程类
			Thread t1 = new Thread(st);
			Thread t2 = new Thread(gt);
	
			//启动线程
			t1.start();
			t2.start();
		}
	}

#### （2）等待唤醒 ####

Object类中提供了三个方法：

* wait():等待
* notify():唤醒单个线程
* notifyAll():唤醒所有线程

为什么这些方法不定义在Thread类中呢?因为这些方法的调用必须通过锁对象调用，而我们刚才使用的锁对象是任意锁对象。所以，这些方法必须定义在Object类中。

Student类：

	public class Student {
		String name;
		int age;
		boolean flag; // 默认情况是没有数据，如果是true，说明有数据
	}

消费者GetThread类：

	public class GetThread implements Runnable {
		private Student s;
	
		public GetThread(Student s) {
			this.s = s;
		}
	
		@Override
		public void run() {
			while (true) {
				synchronized (s) {
					if(!s.flag){
						try {
							s.wait(); //t2就等待了。立即释放锁。将来醒过来的时候，是从这里醒过来的时候
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					System.out.println(s.name + "---" + s.age);
					//jack---27
					//mary---30
					
					//修改标记
					s.flag = false;
					//唤醒线程
					s.notify(); //唤醒t1
				}
			}
		}
	}

生产者SetThread类:

	public class SetThread implements Runnable {
	
		private Student s;
		private int x = 0;
	
		public SetThread(Student s) {
			this.s = s;
		}
	
		@Override
		public void run() {
			while (true) {
				synchronized (s) {
					//判断有没有
					if(s.flag){
						try {
							s.wait(); //t1等着，释放锁
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					if (x % 2 == 0) {
						s.name = "jack";
						s.age = 27;
					} else {
						s.name = "mary";
						s.age = 30;
					}
					x++; //x=1
					
					//修改标记
					s.flag = true;
					//唤醒线程
					s.notify(); //唤醒t2,唤醒并不表示你立马可以执行，必须还得抢CPU的执行权。
				}
				//t1有，或者t2有
			}
		}
	}

测试代码：

	public class StudentDemo {
		public static void main(String[] args) {
			//创建资源
			Student s = new Student();
			
			//设置和获取的类
			SetThread st = new SetThread(s);
			GetThread gt = new GetThread(s);
	
			//线程类
			Thread t1 = new Thread(st);
			Thread t2 = new Thread(gt);
	
			//启动线程
			t1.start();
			t2.start();
		}
	}