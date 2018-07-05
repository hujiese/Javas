## 反射 ##

JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。
要想解剖一个类,必须先要获取到该类的字节码文件对象。而解剖使用的就是Class类中的方法.所以先要获取到每一个字节码文件对应的Class类型的对象.

### 一、获取Class对象 ###

获取class文件对象的方式：

* Object类的getClass()方法
* 数据类型的静态属性class
* Class类中的静态方法：public static Class forName(String className)

开发选第三章，因为第三种是一个字符串，而不是一个具体的类名。这样就可以把这样的字符串配置到配置文件中。

ReflectDemo：

	public class ReflectDemo {
		public static void main(String[] args) throws ClassNotFoundException {
			// 方式1
			Person p = new Person();
			Class c = p.getClass();
	
			Person p2 = new Person();
			Class c2 = p2.getClass();
	
			System.out.println(p == p2);// false
			System.out.println(c == c2);// true
	
			// 方式2
			Class c3 = Person.class;
			// int.class;
			// String.class;
			System.out.println(c == c3);
	
			// 方式3
			// ClassNotFoundException
			Class c4 = Class.forName("cn.scut_01.Person");
			System.out.println(c == c4);
		}
	}

用于测试的Person类：

	public class Person {
		private String name;
		int age;
		public String address;
	
		public Person() {
		}
	
		private Person(String name) {
			this.name = name;
		}
	
		Person(String name, int age) {
			this.name = name;
			this.age = age;
		}
	
		public Person(String name, int age, String address) {
			this.name = name;
			this.age = age;
			this.address = address;
		}
	
		public void show() {
			System.out.println("show");
		}
	
		public void method(String s) {
			System.out.println("method " + s);
		}
	
		public String getString(String s, int i) {
			return s + "---" + i;
		}
	
		private void function() {
			System.out.println("function");
		}
	
		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + ", address=" + address
					+ "]";
		}
	
	}


### 二、通过反射获取构造方法 ###

	public Constructor[] getConstructors():所有公共构造方法
	public Constructor[] getDeclaredConstructors():所有构造方法
	public Constructor<T> getConstructor(Class<?>... parameterTypes)：获取单个构造方法，参数表示的是：要获取的构造方法的构造参数个数及数据类型的class字节码文件对象
	public T newInstance(Object... initargs)：使用此 Constructor 对象表示的构造方法来创建该构造方法的声明类的新实例，并用指定的初始化参数初始化该实例。
#### （1）获取默认无参构造方法 ####
	/*
	 * 通过反射获取构造方法并使用。
	 */
	public class ReflectDemo {
		public static void main(String[] args) throws Exception {
			// 获取字节码文件对象
			Class c = Class.forName("cn.scut_01.Person");
	
			// 获取构造方法
			// public Constructor[] getConstructors():所有公共构造方法
			// public Constructor[] getDeclaredConstructors():所有构造方法
			// Constructor[] cons = c.getDeclaredConstructors();
			// for (Constructor con : cons) {
			// System.out.println(con);
			// }
	
			// 获取单个构造方法
			// public Constructor<T> getConstructor(Class<?>... parameterTypes)
			// 参数表示的是：你要获取的构造方法的构造参数个数及数据类型的class字节码文件对象
			Constructor con = c.getConstructor();// 返回的是构造方法对象
	
			// Person p = new Person();
			// System.out.println(p);
			// public T newInstance(Object... initargs)
			// 使用此 Constructor 对象表示的构造方法来创建该构造方法的声明类的新实例，并用指定的初始化参数初始化该实例。
			Object obj = con.newInstance();
			System.out.println(obj);
			
			// Person p = (Person)obj;
			// p.show();
		}
	}

#### （2）获取带参构造方法 ####

	public class ReflectDemo2 {
		public static void main(String[] args) throws Exception {
			// 获取字节码文件对象
			Class c = Class.forName("cn.itcast_01.Person");
	
			// 获取带参构造方法对象
			// public Constructor<T> getConstructor(Class<?>... parameterTypes)
			Constructor con = c.getConstructor(String.class, int.class,
					String.class);
	
			// 通过带参构造方法对象创建对象
			// public T newInstance(Object... initargs)
			Object obj = con.newInstance("jack", 27, "北京");
			
			System.out.println(obj);
		}
	}


#### （3）获取私有构造方法 ####

	public class ReflectDemo3 {
		public static void main(String[] args) throws Exception {
			// 获取字节码文件对象
			Class c = Class.forName("cn.itcast_01.Person");
	
			// 获取私有构造方法对象
			// NoSuchMethodException：每个这个方法异常
			// 原因是一开始我们使用的方法只能获取公共的，下面这种方式就可以了。
			Constructor con = c.getDeclaredConstructor(String.class);
	
			// 用该私有构造方法创建对象
			// IllegalAccessException:非法的访问异常。
			// 暴力访问
			con.setAccessible(true);// 值为true则指示反射的对象在使用时应该取消Java语言访问检查。
			Object obj = con.newInstance("jack");
	
			System.out.println(obj);
		}
	}


### 三、获取成员变量 ###

获取所有成员：getFields、getDeclaredFields

获取单个成员L：getField、getDeclaredField

修改成员的值：set(Object obj,Object value)：将指定对象变量上此 Field 对象表示的字段设置为指定的新值。

	public class ReflectDemo {
		public static void main(String[] args) throws Exception {
			// 获取字节码文件对象
			Class c = Class.forName("cn.itcast_01.Person");
	
			// 获取所有的成员变量
			// Field[] fields = c.getFields();
			// Field[] fields = c.getDeclaredFields();
			// for (Field field : fields) {
			// System.out.println(field);
			// }
	
			/*
			 * Person p = new Person(); p.address = "北京"; System.out.println(p);
			 */
	
			// 通过无参构造方法创建对象
			Constructor con = c.getConstructor();
			Object obj = con.newInstance();
			System.out.println(obj);
	
			// 获取单个的成员变量
			// 获取address并对其赋值
			Field addressField = c.getField("address");
			// public void set(Object obj,Object value)
			// 将指定对象变量上此 Field 对象表示的字段设置为指定的新值。
			addressField.set(obj, "北京"); // 给obj对象的addressField字段设置值为"北京"
			System.out.println(obj);
	
			// 获取name并对其赋值
			// NoSuchFieldException
			Field nameField = c.getDeclaredField("name");
			// IllegalAccessException
			nameField.setAccessible(true);
			nameField.set(obj, "jack");
			System.out.println(obj);
	
			// 获取age并对其赋值
			Field ageField = c.getDeclaredField("age");
			ageField.setAccessible(true);
			ageField.set(obj, 27);
			System.out.println(obj);
		}
	}

### 四、获取成员方法 ###

获取所有方法：getMethods、getDeclaredMethods

获取单个方法：getMethod、getDeclaredMethod

暴力访问：method.setAccessible(true)

	public class ReflectDemo {
		public static void main(String[] args) throws Exception {
			// 获取字节码文件对象
			Class c = Class.forName("cn.itcast_01.Person");
	
			// 获取所有的方法
			// Method[] methods = c.getMethods(); // 获取自己的包括父亲的公共方法
			// Method[] methods = c.getDeclaredMethods(); // 获取自己的所有的方法
			// for (Method method : methods) {
			// System.out.println(method);
			// }
	
			Constructor con = c.getConstructor();
			Object obj = con.newInstance();
	
			/*
			 * Person p = new Person(); p.show();
			 */
	
			// 获取单个方法并使用
			// public void show()
			// public Method getMethod(String name,Class<?>... parameterTypes)
			// 第一个参数表示的方法名，第二个参数表示的是方法的参数的class类型
			Method m1 = c.getMethod("show");
			// obj.m1(); // 错误
			// public Object invoke(Object obj,Object... args)
			// 返回值是Object接收,第一个参数表示对象是谁，第二参数表示调用该方法的实际参数
			m1.invoke(obj); // 调用obj对象的m1方法
	
			System.out.println("----------");
			// public void method(String s)
			Method m2 = c.getMethod("method", String.class);
			m2.invoke(obj, "hello");
			System.out.println("----------");
	
			// public String getString(String s, int i)
			Method m3 = c.getMethod("getString", String.class, int.class);
			Object objString = m3.invoke(obj, "hello", 100);
			System.out.println(objString);
			// String s = (String)m3.invoke(obj, "hello",100);
			// System.out.println(s);
			System.out.println("----------");
	
			// private void function()
			Method m4 = c.getDeclaredMethod("function");
			m4.setAccessible(true);
			m4.invoke(obj);
		}
	}

### 五、反射应用举例 ###

#### （1）通过配置文件运行类中的方法 ####

测试类：

	public class Test {
		public static void main(String[] args) throws Exception {
			// 加载键值对数据
			Properties prop = new Properties();
			FileReader fr = new FileReader("class.txt");
			prop.load(fr);
			fr.close();
	
			// 获取数据
			String className = prop.getProperty("className");
			String methodName = prop.getProperty("methodName");
	
			// 反射
			Class c = Class.forName(className);
	
			Constructor con = c.getConstructor();
			Object obj = con.newInstance();
	
			// 调用方法
			Method m = c.getMethod(methodName);
			m.invoke(obj);
		}
	}

配置文件class.txt:

	className=scut.hujie.test.Worker
	methodName=love

Worker类：

	public class Worker {
		public void love() {
			System.out.println("爱生活");
		}
	}


#### （2）通过反射越过泛型检查 ####

需求：有ArrayList<Integer>的一个对象，往这个集合中添加一个字符串数据，如何实现呢？

	public class ArrayListDemo {
		public static void main(String[] args) throws NoSuchMethodException,
				SecurityException, IllegalAccessException,
				IllegalArgumentException, InvocationTargetException {
			// 创建集合对象
			ArrayList<Integer> array = new ArrayList<Integer>();
	
			// array.add("hello");
			// array.add(10);
	
			Class c = array.getClass(); // 集合ArrayList的class文件对象
			Method m = c.getMethod("add", Object.class);
	
			m.invoke(array, "hello"); // 调用array的add方法，传入的值是hello
			m.invoke(array, "world");
			m.invoke(array, "java");
	
			System.out.println(array);
		}
	}

#### （3）通过反射写一个通用的设置某个对象的某个属性为指定的值 ####

需求：

写一个方法：public void setProperty(Object obj, String propertyName, Object value){}，
此方法可将obj对象中名为propertyName的属性的值设置为value。

Tool类：

	public class Tool {
		public void setProperty(Object obj, String propertyName, Object value)
				throws NoSuchFieldException, SecurityException,
				IllegalArgumentException, IllegalAccessException {
			// 根据对象获取字节码文件对象
			Class c = obj.getClass();
			// 获取该对象的propertyName成员变量
			Field field = c.getDeclaredField(propertyName);
			// 取消访问检查
			field.setAccessible(true);
			// 给对象的成员变量赋值为指定的值
			field.set(obj, value);
		}
	}

测试类：

	public class ToolDemo {
		public static void main(String[] args) throws NoSuchFieldException,
				SecurityException, IllegalArgumentException, IllegalAccessException {
			Person p = new Person();
			Tool t = new Tool();
			t.setProperty(p, "name", "林青霞");
			t.setProperty(p, "age", 27);
			System.out.println(p);
			System.out.println("-----------");
	
			Dog d = new Dog();
	
			t.setProperty(d, "sex", '男');
			t.setProperty(d, "price", 12.34f);
	
			System.out.println(d);
		}
	}
	
	class Dog {
		char sex;
		float price;
	
		@Override
		public String toString() {
			return sex + "---" + price;
		}
	}
	
	class Person {
		private String name;
		public int age;
	
		@Override
		public String toString() {
			return name + "---" + age;
		}
	}

### 六、动态代理 ###

代理：本来应该自己做的事情，却请了别人来做，被请的人就是代理对象。而程序运行过程中产生对象其实就是通过反射，所以，动态代理其实就是通过反射来生成一个代理。

在Java中java.lang.reflect包下提供了一个Proxy类和一个InvocationHandler接口，通过使用这个类和接口就可以生成动态代理对象。JDK提供的代理只能针对接口做代理。我们有更强大的代理cglib。Proxy类中的方法创建动态代理类对象：

	public static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)

最终会调用InvocationHandler的方法：

	InvocationHandler
	Object invoke(Object proxy,Method method,Object[] args)

Proxy类中创建动态代理对象的方法的三个参数：

* ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载
* Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
* InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上。

每一个动态代理类都必须要实现InvocationHandler这个接口，并且每个代理类的实例都关联到了一个handler，当我们通过代理对象调用一个方法的时候，这个方法的调用就会被转发为由InvocationHandler这个接口的invoke 方法来进行调用。InvocationHandler接口中invoke方法的三个参数：

* proxy:代表动态代理对象
* method:代表正在执行的方法
* args:代表调用目标方法时传入的实参

Proxy.newProxyInstance创建的代理对象是在jvm运行时动态生成的一个对象，它并不是我们的InvocationHandler类型，也不是我们定义的那组接口的类型，而是在运行是动态生成的一个对象，并且命名方式都是这样的形式，以$开头，proxy为中，最后一个数字表示对象的标号。System.out.println(u.getClass().getName());

用户操作接口：

	public interface UserDao {
		public abstract void add();
	
		public abstract void delete();
	
		public abstract void update();
	
		public abstract void find();
	}

UserDaoImpl类：

	public class UserDaoImpl implements UserDao {
	
		@Override
		public void add() {
			System.out.println("添加功能");
		}
	
		@Override
		public void delete() {
			System.out.println("删除功能");
		}
	
		@Override
		public void update() {
			System.out.println("修改功能");
		}
	
		@Override
		public void find() {
			System.out.println("查找功能");
		}
	
	}

MyInvocationHandler类：

	public class MyInvocationHandler implements InvocationHandler {
		private Object target; // 目标对象
	
		public MyInvocationHandler(Object target) {
			this.target = target;
		}
	
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			System.out.println("权限校验");
			Object result = method.invoke(target, args);
			System.out.println("日志记录");
			return result; // 返回的是代理对象
		}
	}

测试类：

	public class Test {
		public static void main(String[] args) {
			UserDao ud = new UserDaoImpl();
			// 创建一个动态代理对象
			// Proxy类中有一个方法可以创建动态代理对象
			// public static Object newProxyInstance(ClassLoader loader,Class<?>[]
			// interfaces,InvocationHandler h)
			// 准备对ud对象做一个代理对象
			MyInvocationHandler handler = new MyInvocationHandler(ud);
			UserDao proxy = (UserDao) Proxy.newProxyInstance(ud.getClass()
					.getClassLoader(), ud.getClass().getInterfaces(), handler);
			proxy.add();
			proxy.delete();
			proxy.update();
			proxy.find();
	
		}
	}