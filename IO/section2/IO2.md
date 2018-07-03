## 字符流--OutputStreamWriter、InputStreamReader、FileWriter、FileReader、BufferedWriter、BufferedReader ##
![](https://i.imgur.com/HS8NUFw.png)
### 一、转换流出现的原因及思想 ###

* 由于字节流操作中文不是特别方便，所以，java就提供了转换流。
* 字符流=字节流+编码表。

### 二、编码表概述和常见的编码表 ###
编码表，即由字符及其对应的数值组成的一张表。常见编码表：

* ASCII/Unicode 字符集
* ISO-8859-1
* GB2312/GBK/GB18030
* BIG5
* UTF-8

String类提供了编码功能：

* String(byte[] bytes, String charsetName):通过指定的字符集解码字节数组
* byte[] getBytes(String charsetName):使用指定的字符集合把字符串编码为字节数组


		public class StringDemo {
			public static void main(String[] args) throws UnsupportedEncodingException {
				String s = "你好";
		
				// String -- byte[]
				byte[] bys = s.getBytes(); // [-60, -29, -70, -61]
				// byte[] bys = s.getBytes("GBK");// [-60, -29, -70, -61]
				// byte[] bys = s.getBytes("UTF-8");// [-28, -67, -96, -27, -91, -67]
				System.out.println(Arrays.toString(bys));
		
				// byte[] -- String
				String ss = new String(bys); // 你好
				// String ss = new String(bys, "GBK"); // 你好
				// String ss = new String(bys, "UTF-8"); // ???
				System.out.println(ss);
			}
		}


### 三、InputStreamReader ###
#### （1）构造函数 ####

* InputStreamReader(InputStream is):用默认的编码读取数据
* InputStreamReader(InputStream is,String charsetName):用指定的编码读取数据


		public class InputStreamReaderDemo {
			public static void main(String[] args) throws IOException {
				// 创建对象
				// InputStreamReader isr = new InputStreamReader(new FileInputStream(
				// "osw.txt"));
		
				// InputStreamReader isr = new InputStreamReader(new FileInputStream(
				// "osw.txt"), "GBK");
		
				InputStreamReader isr = new InputStreamReader(new FileInputStream(
						"osw.txt"), "UTF-8");
		
				// 读取数据
				// 一次读取一个字符
				int ch = 0;
				while ((ch = isr.read()) != -1) {
					System.out.print((char) ch);
				}
		
				// 释放资源
				isr.close();
			}
		}


#### （2）InputStreamReader的读方法 ####

* int read():一次读取一个字符
* int read(char[] chs):一次读取一个字符数组


		public class InputStreamReaderDemo {
			public static void main(String[] args) throws IOException {
				// 创建对象
				InputStreamReader isr = new InputStreamReader(new FileInputStream(
						"StringDemo.java"));
		
				// 一次读取一个字符
				// int ch = 0;
				// while ((ch = isr.read()) != -1) {
				// System.out.print((char) ch);
				// }
		
				// 一次读取一个字符数组
				char[] chs = new char[1024];
				int len = 0;
				while ((len = isr.read(chs)) != -1) {
					System.out.print(new String(chs, 0, len));
				}
		
				// 释放资源
				isr.close();
			}
		}


### 四、OutputStreamWriterDemo ###
#### （1）构造函数 ####

* OutputStreamWriter(OutputStream out):根据默认编码把字节流的数据转换为字符流
* OutputStreamWriter(OutputStream out,String charsetName):根据指定编码把字节流数据转换为字符流

		public class OutputStreamWriterDemo {
			public static void main(String[] args) throws IOException {
				// 创建对象
				// OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				// "osw.txt")); // 默认GBK
				// OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				// "osw.txt"), "GBK"); // 指定GBK
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
						"osw.txt"), "UTF-8"); // 指定UTF-8
				// 写数据
				osw.write("中国");
		
				// 释放资源
				osw.close();
			}
		}


#### （2）OutputStreamWriter的写方法 ####

* public void write(int c):写一个字符
* public void write(char[] cbuf):写一个字符数组
* public void write(char[] cbuf,int off,int len):写一个字符数组的一部分
* public void write(String str):写一个字符串
* public void write(String str,int off,int len):写一个字符串的一部分

		public class OutputStreamWriterDemo {
			public static void main(String[] args) throws IOException {
				// 创建对象
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
						"osw2.txt"));
		
				// 写数据
				// public void write(int c):写一个字符
				// osw.write('a');
				// osw.write(97);
				// 为什么数据没有进去呢?
				// 原因是：字符 = 2字节
				// 文件中数据存储的基本单位是字节。
				// void flush()
		
				// public void write(char[] cbuf):写一个字符数组
				// char[] chs = {'a','b','c','d','e'};
				// osw.write(chs);
		
				// public void write(char[] cbuf,int off,int len):写一个字符数组的一部分
				// osw.write(chs,1,3);
		
				// public void write(String str):写一个字符串
				// osw.write("我爱scut");
		
				// public void write(String str,int off,int len):写一个字符串的一部分
				osw.write("我爱scut", 2, 3);
		
				// 刷新缓冲区
				osw.flush();
				// osw.write("我爱scut", 2, 3);
		
				// 释放资源
				osw.close();
				// java.io.IOException: Stream closed
				// osw.write("我爱scut", 2, 3);
			}
		}

### 五、BufferedReader & BufferedWriter ###
字符流为了高效读写，也提供了对应的字符缓冲流：

 * BufferedWriter:字符缓冲输出流
 * BufferedReader:字符缓冲输入流
 

#### （1）BufferedReader ####
BufferedReader从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。 可以指定缓冲区的大小，或者可使用默认的大小。大多数情况下，默认值就足够了。

* BufferedReader(Reader in) 

		public class BufferedReaderDemo {
			public static void main(String[] args) throws IOException {
				// 创建字符缓冲输入流对象
				BufferedReader br = new BufferedReader(new FileReader("bw.txt"));
		
				// 方式1
				// int ch = 0;
				// while ((ch = br.read()) != -1) {
				// System.out.print((char) ch);
				// }
		
				// 方式2
				char[] chs = new char[1024];
				int len = 0;
				while ((len = br.read(chs)) != -1) {
					System.out.print(new String(chs, 0, len));
				}
		
				// 释放资源
				br.close();
			}
		}


#### （2）BufferedWriter ###
BufferedWriter将文本写入字符输出流，缓冲各个字符，从而提供单个字符、数组和字符串的高效写入。 可以指定缓冲区的大小，或者接受默认的大小。在大多数情况下，默认值就足够了。

		public class BufferedWriterDemo {
			public static void main(String[] args) throws IOException {
				// BufferedWriter(Writer out)
				// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				// new FileOutputStream("bw.txt")));
		
				BufferedWriter bw = new BufferedWriter(new FileWriter("bw.txt"));
		
				bw.write("hello");
				bw.write("world");
				bw.write("java");
				bw.flush();
		
				bw.close();
			}
		}


#### （3）字符缓冲流的特殊方法 ####
 BufferedWriter:

* public void newLine():根据系统来决定换行符


BufferedReader:

* public String readLine()：一次读取一行数据
* 包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null


		public class BufferedDemo {
			public static void main(String[] args) throws IOException {
				// write();
				read();
			}
		
			private static void read() throws IOException {
				// 创建字符缓冲输入流对象
				BufferedReader br = new BufferedReader(new FileReader("bw2.txt"));
		
				// public String readLine()：一次读取一行数据
				// String line = br.readLine();
				// System.out.println(line);
				// line = br.readLine();
				// System.out.println(line);
		
				// 最终版代码
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
				
				//释放资源
				br.close();
			}
		
			private static void write() throws IOException {
				// 创建字符缓冲输出流对象
				BufferedWriter bw = new BufferedWriter(new FileWriter("bw2.txt"));
				for (int x = 0; x < 10; x++) {
					bw.write("hello" + x);
					// bw.write("\r\n");
					bw.newLine();
					bw.flush();
				}
				bw.close();
			}
		
		}


### 六、文件拷贝 ###
#### （1）InputStreamReader & OutputStreamWriter方式 ####

数据源：

* a.txt -- 读取数据 -- 字符转换流 -- InputStreamReader


目的地：

* b.txt -- 写出数据 -- 字符转换流 -- OutputStreamWriter


		public class CopyFileDemo {
			public static void main(String[] args) throws IOException {
				// 封装数据源
				InputStreamReader isr = new InputStreamReader(new FileInputStream(
						"a.txt"));
				// 封装目的地
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
						"b.txt"));
		
				// 读写数据
				// 方式1
				// int ch = 0;
				// while ((ch = isr.read()) != -1) {
				// osw.write(ch);
				// }
		
				// 方式2
				char[] chs = new char[1024];
				int len = 0;
				while ((len = isr.read(chs)) != -1) {
					osw.write(chs, 0, len);
					// osw.flush();
				}
		
				// 释放资源
				osw.close();
				isr.close();
			}
		}


#### （2）FileReader & FileWriter方式 ####
数据源：

* a.txt -- 读取数据 -- 字符转换流 -- InputStreamReader -- FileReader


目的地：

* b.txt -- 写出数据 -- 字符转换流 -- OutputStreamWriter -- FileWriter


		public class CopyFileDemo2 {
			public static void main(String[] args) throws IOException {
				// 封装数据源
				FileReader fr = new FileReader("a.txt");
				// 封装目的地
				FileWriter fw = new FileWriter("b.txt");
		
				// 一次一个字符
				// int ch = 0;
				// while ((ch = fr.read()) != -1) {
				// fw.write(ch);
				// }
		
				// 一次一个字符数组
				char[] chs = new char[1024];
				int len = 0;
				while ((len = fr.read(chs)) != -1) {
					fw.write(chs, 0, len);
					fw.flush();
				}
		
				// 释放资源
				fw.close();
				fr.close();
			}
		}


#### （3）BufferedReader & BufferedWriter方式 ####
数据源：

* a.txt -- 读取数据 -- 字符转换流 -- InputStreamReader -- FileReader -- BufferedReader


目的地：

* b.txt -- 写出数据 -- 字符转换流 -- OutputStreamWriter -- FileWriter -- BufferedWriter

方法一--字符数组缓冲方式：

	public class CopyFileDemo {
		public static void main(String[] args) throws IOException {
			// 封装数据源
			BufferedReader br = new BufferedReader(new FileReader("a.txt"));
			// 封装目的地
			BufferedWriter bw = new BufferedWriter(new FileWriter("b.txt"));
	
			// 两种方式其中的一种一次读写一个字符数组
			char[] chs = new char[1024];
			int len = 0;
			while ((len = br.read(chs)) != -1) {
				bw.write(chs, 0, len);
				bw.flush();
			}
	
			// 释放资源
			bw.close();
			br.close();
		}
	}

方法二--行方式：

	public class CopyFileDemo2 {
		public static void main(String[] args) throws IOException {
			// 封装数据源
			BufferedReader br = new BufferedReader(new FileReader("a.txt"));
			// 封装目的地
			BufferedWriter bw = new BufferedWriter(new FileWriter("b.txt"));
	
			// 读写数据
			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
	
			// 释放资源
			bw.close();
			br.close();
		}
	}