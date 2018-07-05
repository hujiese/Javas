package scut.hujie_02;

import java.lang.reflect.Constructor;

/*
 * ����ͨ�������ȡ˽�й��췽����ʹ��
 * private Person(String name){}
 * 
 * Person p = new Person("������");
 * System.out.println(p);
 */
public class ReflectDemo3 {
	public static void main(String[] args) throws Exception {
		// ��ȡ�ֽ����ļ�����
		Class c = Class.forName("cn.scut_01.Person");

		// ��ȡ˽�й��췽������
		// NoSuchMethodException��ÿ����������쳣
		// ԭ����һ��ʼ����ʹ�õķ���ֻ�ܻ�ȡ�����ģ��������ַ�ʽ�Ϳ����ˡ�
		Constructor con = c.getDeclaredConstructor(String.class);

		// �ø�˽�й��췽����������
		// IllegalAccessException:�Ƿ��ķ����쳣��
		// ��������
		con.setAccessible(true);// ֵΪtrue��ָʾ����Ķ�����ʹ��ʱӦ��ȡ��Java���Է��ʼ�顣
		Object obj = con.newInstance("jack");

		System.out.println(obj);
	}
}