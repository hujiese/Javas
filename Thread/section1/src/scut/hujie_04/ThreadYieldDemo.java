package scut.hujie_04;

/*
 * public static void yield():��ͣ��ǰ����ִ�е��̶߳��󣬲�ִ�������̡߳� 
 * �ö���̵߳�ִ�и���г�����ǲ��ܿ�����֤һ��һ�Ρ�
 */
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
