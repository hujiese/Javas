package scut.hujie_04;

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
					s.name = "jack";//���ߵ�����ͱ�����������ִ��Ȩ
					s.age = 27;
				} else {
					s.name = "mary"; //���ߵ�����ͱ�����������ִ��Ȩ
					s.age = 30;
				}
				x++;
			}
		}
	}
}
