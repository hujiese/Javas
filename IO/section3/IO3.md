### 一、数据流 DataInputStream & DataOutputStream ###
该流可以读写基本数据类型的数据。

数据输入流：DataInputStream

* DataInputStream(InputStream in)

数据输出流：DataOutputStream

* DataOutputStream(OutputStream out) 

		public class DataStreamDemo {
			public static void main(String[] args) throws IOException {
				// 写
				// write();
		
				// 读
				read();
			}
		
			private static void read() throws IOException {
				// DataInputStream(InputStream in)
				// 创建数据输入流对象
				DataInputStream dis = new DataInputStream(
						new FileInputStream("dos.txt"));
		
				// 读数据
				byte b = dis.readByte();
				short s = dis.readShort();
				int i = dis.readInt();
				long l = dis.readLong();
				float f = dis.readFloat();
				double d = dis.readDouble();
				char c = dis.readChar();
				boolean bb = dis.readBoolean();
		
				// 释放资源
				dis.close();
		
				System.out.println(b);
				System.out.println(s);
				System.out.println(i);
				System.out.println(l);
				System.out.println(f);
				System.out.println(d);
				System.out.println(c);
				System.out.println(bb);
			}
		
			private static void write() throws IOException {
				// DataOutputStream(OutputStream out)
				// 创建数据输出流对象
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(
						"dos.txt"));
		
				// 写数据了
				dos.writeByte(10);
				dos.writeShort(100);
				dos.writeInt(1000);
				dos.writeLong(10000);
				dos.writeFloat(12.34F);
				dos.writeDouble(12.56);
				dos.writeChar('a');
				dos.writeBoolean(true);
		
				// 释放资源
				dos.close();
			}
		}


### 二、内存操作流 ###
用于处理临时存储信息的，程序结束，数据就从内存中消失。

字节数组：

* ByteArrayInputStream
* ByteArrayOutputStream


字符数组：

* CharArrayReader
* CharArrayWriter

字符串：

* StringReader
* StringWriter

		public class ByteArrayStreamDemo {
			public static void main(String[] args) throws IOException {
				// 写数据
				// ByteArrayOutputStream()
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
				// 写数据
				for (int x = 0; x < 10; x++) {
					baos.write(("hello" + x).getBytes());
				}
		
				// 释放资源
				// 通过查看源码我们知道这里什么都没做，所以根本需要close()
				// baos.close();
		
				// public byte[] toByteArray()
				byte[] bys = baos.toByteArray();
		
				// 读数据
				// ByteArrayInputStream(byte[] buf)
				ByteArrayInputStream bais = new ByteArrayInputStream(bys);
		
				int by = 0;
				while ((by = bais.read()) != -1) {
					System.out.print((char) by);
				}
		
				// bais.close();
			}
		}


### 三、打印流 ###
字节流打印流	PrintStream

字符打印流	PrintWriter

打印流的特点：

* 只有写数据的，没有读取数据。只能操作目的地，不能操作数据源。
* 可以操作任意类型的数据。
* 如果启动了自动刷新，能够自动刷新。
* 该流是可以直接操作文本文件的。

		public class PrintWriterDemo {
			public static void main(String[] args) throws IOException {
				// 作为Writer的子类使用
				PrintWriter pw = new PrintWriter("pw.txt");
		
				pw.write("hello");
				pw.write("world");
				pw.write("java");
				
				pw.close();
			}
		}

启动自动刷新：

	public class PrintWriterDemo2 {
		public static void main(String[] args) throws IOException {
			// 创建打印流对象
			PrintWriter pw = new PrintWriter(new FileWriter("pw2.txt"), true);
	
			pw.println("hello");
			pw.println(true);
			pw.println(100);
	
			pw.close();
		}
	}

打印流拷贝文件：

数据源：

* DataStreamDemo.java -- 读取数据 -- FileReader -- BufferedReader

目的地：

* Copy.java -- 写出数据 -- FileWriter -- BufferedWriter -- PrintWriter

		public class CopyFileDemo {
			public static void main(String[] args) throws IOException {
				// 打印流的改进版
				// 封装数据源
				BufferedReader br = new BufferedReader(new FileReader(
						"DataStreamDemo.java"));
				// 封装目的地
				PrintWriter pw = new PrintWriter(new FileWriter("Copy.java"), true);
				
				String line = null;
				while((line=br.readLine())!=null){
					pw.println(line);
				}
				
				pw.close();
				br.close();
			}
		}

### 四、随机访问流RandomAccessFile ###
RandomAccessFile类不属于流，是Object类的子类，但它融合了InputStream和OutputStream的功能，支持对文件的随机访问读取和写入。

* public RandomAccessFile(String name,String mode)：第一个参数是文件路径，第二个参数是操作文件的模式。模式有四种，我们最常用的一种叫"rw",这种方式表示我既可以写数据，也可以读取数据：

		public class RandomAccessFileDemo {
			public static void main(String[] args) throws IOException {
				// write();
				read();
			}
		
			private static void read() throws IOException {
				// 创建随机访问流对象
				RandomAccessFile raf = new RandomAccessFile("raf.txt", "rw");
		
				int i = raf.readInt();
				System.out.println(i);
				// 该文件指针可以通过 getFilePointer方法读取，并通过 seek 方法设置。
				System.out.println("当前文件的指针位置是：" + raf.getFilePointer());
		
				char ch = raf.readChar();
				System.out.println(ch);
				System.out.println("当前文件的指针位置是：" + raf.getFilePointer());
		
				String s = raf.readUTF();
				System.out.println(s);
				System.out.println("当前文件的指针位置是：" + raf.getFilePointer());
		
				// 我不想重头开始了，我就要读取a，怎么办呢?
				raf.seek(4);
				ch = raf.readChar();
				System.out.println(ch);
			}
		
			private static void write() throws IOException {
				// 创建随机访问流对象
				RandomAccessFile raf = new RandomAccessFile("raf.txt", "rw");
		
				// 怎么玩呢?
				raf.writeInt(100);
				raf.writeChar('a');
				raf.writeUTF("中国");
		
				raf.close();
			}
		}


### 五、合并流SequenceInputStream ###
#### （1）合并两个文件：SequenceInputStream(InputStream s1, InputStream s2) ####
	public class SequenceInputStreamDemo {
		public static void main(String[] args) throws IOException {
			// SequenceInputStream(InputStream s1, InputStream s2)
			// 需求：把ByteArrayStreamDemo.java和DataStreamDemo.java的内容复制到Copy.java中
			InputStream s1 = new FileInputStream("ByteArrayStreamDemo.java");
			InputStream s2 = new FileInputStream("DataStreamDemo.java");
			SequenceInputStream sis = new SequenceInputStream(s1, s2);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream("Copy.java"));
	
			// 如何写读写呢，其实很简单，你就按照以前怎么读写，现在还是怎么读写
			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = sis.read(bys)) != -1) {
				bos.write(bys, 0, len);
			}
	
			bos.close();
			sis.close();
		}
	}

#### （2）合并多个文件SequenceInputStream(Enumeration e) ####
	public class SequenceInputStreamDemo2 {
		public static void main(String[] args) throws IOException {
			// 需求：把下面的三个文件的内容复制到Copy.java中
			// ByteArrayStreamDemo.java,CopyFileDemo.java,DataStreamDemo.java
	
			// SequenceInputStream(Enumeration e)
			// 通过简单的回顾我们知道了Enumeration是Vector中的一个方法的返回值类型。
			// Enumeration<E> elements()
			Vector<InputStream> v = new Vector<InputStream>();
			InputStream s1 = new FileInputStream("ByteArrayStreamDemo.java");
			InputStream s2 = new FileInputStream("CopyFileDemo.java");
			InputStream s3 = new FileInputStream("DataStreamDemo.java");
			v.add(s1);
			v.add(s2);
			v.add(s3);
			Enumeration<InputStream> en = v.elements();
			SequenceInputStream sis = new SequenceInputStream(en);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream("Copy.java"));
	
			// 如何写读写呢，其实很简单，你就按照以前怎么读写，现在还是怎么读写
			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = sis.read(bys)) != -1) {
				bos.write(bys, 0, len);
			}
	
			bos.close();
			sis.close();
		}
	}

### 六、序列化流ObjectOutputStream & ObjectInputStream ###

 * 序列化流：把对象按照流一样的方式存入文本文件或者在网络中传输。对象 -- 流数据(ObjectOutputStream)
 * 反序列化流:把文本文件中的流对象数据或者网络中的流对象数据还原成对象。流数据 -- 对象(ObjectInputStream)

		public class ObjectStreamDemo {
			public static void main(String[] args) throws IOException,
					ClassNotFoundException {
				// 由于我们要对对象进行序列化，所以我们先自定义一个类
				// 序列化数据其实就是把对象写到文本文件
				// write();
		
				read();
			}
		
			private static void read() throws IOException, ClassNotFoundException {
				// 创建反序列化对象
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
						"oos.txt"));
		
				// 还原对象
				Object obj = ois.readObject();
		
				// 释放资源
				ois.close();
		
				// 输出对象
				System.out.println(obj);
			}
		
			private static void write() throws IOException {
				// 创建序列化流对象
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
						"oos.txt"));
		
				// 创建对象
				Person p = new Person("林青霞", 27);
		
				// public final void writeObject(Object obj)
				oos.writeObject(p);
		
				// 释放资源
				oos.close();
			}
		}

其中Person类如下：

	public class Person implements Serializable {
		private static final long serialVersionUID = -2071565876962058344L;//点击黄色警告线，让eclipse自动生成
	
		private String name;
	
		// private int age;
	
		private transient int age;
	
		// int age;
	
		public Person() {
			super();
		}
	
		public Person(String name, int age) {
			super();
			this.name = name;
			this.age = age;
		}
	
		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
	
		public int getAge() {
			return age;
		}
	
		public void setAge(int age) {
			this.age = age;
		}
	
		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + "]";
		}
	}

### 七、属性集合类Properties ###
Properties:，属性集合类是一个可以和IO流相结合使用的集合类。Properties 可保存在流中或从流中加载。属性列表中每个键及其对应值都是一个字符串。 

#### （1）创建测试 ####
	public class PropertiesDemo {
		public static void main(String[] args) {
			// 作为Map集合的使用
			Properties prop = new Properties();
	
			// 添加元素
			prop.put("it002", "hello");
			prop.put("it001", "world");
			prop.put("it003", "java");
	
			// System.out.println("prop:" + prop);
	
			// 遍历集合
			Set<Object> set = prop.keySet();
			for (Object key : set) {
				Object value = prop.get(key);
				System.out.println(key + "---" + value);
			}
		}
	}

#### （2）特殊功能 ####

* public Object setProperty(String key,String value)：添加元素
* public String getProperty(String key):获取元素
* public Set<String> stringPropertyNames():获取所有的键的集合

		public class PropertiesDemo2 {
			public static void main(String[] args) {
				// 创建集合对象
				Properties prop = new Properties();
		
				// 添加元素
				prop.setProperty("张三", "30");
				prop.setProperty("李四", "40");
				prop.setProperty("王五", "50");
		
				// public Set<String> stringPropertyNames():获取所有的键的集合
				Set<String> set = prop.stringPropertyNames();
				for (String key : set) {
					String value = prop.getProperty(key);
					System.out.println(key + "---" + value);
				}
			}
		}


#### （3）持久化 ####

* public void load(Reader reader):把文件中的数据读取到集合中
* public void store(Writer writer,String comments):把集合中的数据存储到文件

		public class PropertiesDemo3 {
			public static void main(String[] args) throws IOException {
				// myLoad();
		
				myStore();
			}
		
			private static void myStore() throws IOException {
				// 创建集合对象
				Properties prop = new Properties();
		
				prop.setProperty("林青霞", "27");
				prop.setProperty("武鑫", "30");
				prop.setProperty("刘晓曲", "18");
				
				//public void store(Writer writer,String comments):把集合中的数据存储到文件
				Writer w = new FileWriter("name.txt");
				prop.store(w, "helloworld");
				w.close();
			}
		
			private static void myLoad() throws IOException {
				Properties prop = new Properties();
		
				// public void load(Reader reader):把文件中的数据读取到集合中
				// 注意：这个文件的数据必须是键值对形式
				Reader r = new FileReader("prop.txt");
				prop.load(r);
				r.close();
		
				System.out.println("prop:" + prop);
			}
		}
