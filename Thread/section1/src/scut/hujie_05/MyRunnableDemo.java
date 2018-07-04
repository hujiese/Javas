package scut.hujie_05;

/*
 * ��ʽ2��ʵ��Runnable�ӿ�
 * ���裺
 * 		A:�Զ�����MyRunnableʵ��Runnable�ӿ�
 * 		B:��дrun()����
 * 		C:����MyRunnable��Ķ���
 * 		D:����Thread��Ķ��󣬲���C����Ķ�����Ϊ�����������
 */
public class MyRunnableDemo {
	public static void main(String[] args) {
		// ����MyRunnable��Ķ���
		MyRunnable my = new MyRunnable();

		// ����Thread��Ķ��󣬲���C����Ķ�����Ϊ�����������
		// Thread(Runnable target)
		// Thread t1 = new Thread(my);
		// Thread t2 = new Thread(my);
		// t1.setName("jack");
		// t2.setName("orange");

		// Thread(Runnable target, String name)
		Thread t1 = new Thread(my, "jack");
		Thread t2 = new Thread(my, "orange");

		t1.start();
		t2.start();
	}
}