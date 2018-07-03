## FileInputStream、FileOutputStream、BufferedInputStream、BufferedOutputStream ##
### 一、FileOutputStream ###

#### （1）FileOutputStream ####
构造方法：

 * 	FileOutputStream(File file) 
 *	FileOutputStream(String name)


字节输出流操作步骤：

 * 	创建字节输出流对象
 * 	写数据
 * 	释放资源


		public class FileOutputStreamDemo {
			public static void main(String[] args) throws IOException {
				// 创建字节输出流对象
				FileOutputStream fos = new FileOutputStream("fos.txt");
				/*
				 * 创建字节输出流对象了做了几件事情：
				 * A:调用系统功能去创建文件
				 * B:创建fos对象
				 * C:把fos对象指向这个文件
				 */
				
				//写数据
				fos.write("hello,IO".getBytes());
				fos.write("java".getBytes());
				
				//释放资源
				//关闭此文件输出流并释放与此流有关的所有系统资源。
				fos.close();
			}
		}


#### （2）write()方法 ####

 * public void write(int b):写一个字节
 * public void write(byte[] b):写一个字节数组
 * public void write(byte[] b,int off,int len):写一个字节数组的一部分


		public class FileOutputStreamDemo2 {
			public static void main(String[] args) throws IOException {
				// 创建字节输出流对象
				// OutputStream os = new FileOutputStream("fos2.txt"); // 多态
				FileOutputStream fos = new FileOutputStream("fos2.txt");
		
				// 调用write()方法
				//fos.write(97); //97 -- 底层二进制数据	-- 通过记事本打开 -- 找97对应的字符值 -- a
				
				//public void write(byte[] b):写一个字节数组
				byte[] bys={97,98,99,100,101};
				fos.write(bys);
				
				//public void write(byte[] b,int off,int len):写一个字节数组的一部分
				fos.write(bys,1,3);
				
				//释放资源
				fos.close();
			}
		}

#### （3）实现数据的换行 ####
不同的系统针对不同的换行符号识别是不一样的：

 * 	windows:\r\n
 * 	linux:\n
 * 	Mac:\r

实现数据的追加写入：

 * 	用构造方法带第二个参数是true的情况即可


		public class FileOutputStreamDemo3 {
			public static void main(String[] args) throws IOException {
				// 创建字节输出流对象
				// FileOutputStream fos = new FileOutputStream("fos3.txt");
				// 创建一个向具有指定 name 的文件中写入数据的输出文件流。如果第二个参数为 true，则将字节写入文件末尾处，而不是写入文件开始处。
				FileOutputStream fos = new FileOutputStream("fos3.txt", true);
		
				// 写数据
				for (int x = 0; x < 10; x++) {
					fos.write(("hello" + x).getBytes());
					fos.write("\r\n".getBytes());
				}
		
				// 释放资源
				fos.close();
			}
		}


#### （4）加入异常处理 ####

	public class FileOutputStreamDemo4 {
		public static void main(String[] args) {
			// 为了在finally里面能够看到该对象就必须定义到外面，为了访问不出问题，还必须给初始化值
			FileOutputStream fos = null;
			try {
				// fos = new FileOutputStream("z:\\fos4.txt");
				fos = new FileOutputStream("fos4.txt");
				fos.write("java".getBytes());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 如果fos不是null，才需要close()
				if (fos != null) {
					// 为了保证close()一定会执行，就放到这里了
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


### 二、FileInputStream ###

字节输入流操作步骤：

 * 创建字节输入流对象
 * 调用read()方法读取数据，并把数据显示在控制台
 * 释放资源

读取数据的方式一：

 * int read():一次读取一个字节

		public class FileInputStreamDemo {
			public static void main(String[] args) throws IOException {
		
				FileInputStream fis = new FileInputStream("FileOutputStreamDemo.java");
		
				int by = 0;
				// 读取，赋值，判断
				while ((by = fis.read()) != -1) {
					System.out.print((char) by);
				}
		
				// 释放资源
				fis.close();
			}
		}


读取数据的方式二：

* 一次读取一个字节数组：int read(byte[] b)

		public class FileInputStreamDemo2 {
			public static void main(String[] args) throws IOException {
				// 创建字节输入流对象
				FileInputStream fis = new FileInputStream("FileOutputStreamDemo.java");
				// 数组的长度一般是1024或者1024的整数倍
				byte[] bys = new byte[1024];
				int len = 0;
				while ((len = fis.read(bys)) != -1) {
					System.out.print(new String(bys, 0, len));
				}
		
				// 释放资源
				fis.close();
			}
		}

### 三、BufferedInputStream ###

	public class BufferedInputStreamDemo {
		public static void main(String[] args) throws IOException {
			// BufferedInputStream(InputStream in)
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
					"bos.txt"));
	
			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = bis.read(bys)) != -1) {
				System.out.print(new String(bys, 0, len));
			}
	
			// 释放资源
			bis.close();
		}
	}

### 四、BufferedOutputStream ###

	public class BufferedOutputStreamDemo {
		public static void main(String[] args) throws IOException {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream("bos.txt"));
	
			// 写数据
			bos.write("hello".getBytes());
	
			// 释放资源
			bos.close();
		}
	}


### 五、字节流拷贝综合案例 ###
需求：把e:\\testmp4.mp4复制到当前项目目录下的copy.mp4中。

字节流四种方式复制文件：

 * 基本字节流一次读写一个字节：	共耗时：117235毫秒
 * 基本字节流一次读写一个字节数组： 共耗时：156毫秒
 * 高效字节流一次读写一个字节： 共耗时：1141毫秒
 * 高效字节流一次读写一个字节数组： 共耗时：47毫秒

		public class CopyMp4Demo {
			public static void main(String[] args) throws IOException {
				long start = System.currentTimeMillis();
				// method1("e:\\testmp4.mp4", "copy1.mp4");
				// method2("e:\\testmp4.mp4", "copy2.mp4");
				// method3("e:\\testmp4.mp4", "copy3.mp4");
				method4("e:\\testmp4.mp4", "copy4.mp4");
				long end = System.currentTimeMillis();
				System.out.println("共耗时：" + (end - start) + "毫秒");
			}
		
			// 高效字节流一次读写一个字节数组：
			public static void method4(String srcString, String destString)
					throws IOException {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
						srcString));
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(destString));
		
				byte[] bys = new byte[1024];
				int len = 0;
				while ((len = bis.read(bys)) != -1) {
					bos.write(bys, 0, len);
				}
		
				bos.close();
				bis.close();
			}
		
			// 高效字节流一次读写一个字节：
			public static void method3(String srcString, String destString)
					throws IOException {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
						srcString));
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(destString));
		
				int by = 0;
				while ((by = bis.read()) != -1) {
					bos.write(by);
		
				}
		
				bos.close();
				bis.close();
			}
		
			// 基本字节流一次读写一个字节数组
			public static void method2(String srcString, String destString)
					throws IOException {
				FileInputStream fis = new FileInputStream(srcString);
				FileOutputStream fos = new FileOutputStream(destString);
		
				byte[] bys = new byte[1024];
				int len = 0;
				while ((len = fis.read(bys)) != -1) {
					fos.write(bys, 0, len);
				}
		
				fos.close();
				fis.close();
			}
		
			// 基本字节流一次读写一个字节
			public static void method1(String srcString, String destString)
					throws IOException {
				FileInputStream fis = new FileInputStream(srcString);
				FileOutputStream fos = new FileOutputStream(destString);
		
				int by = 0;
				while ((by = fis.read()) != -1) {
					fos.write(by);
				}
		
				fos.close();
				fis.close();
			}
		}
