## 网络编程 ##

### 一、InetAddress类 ###

用于获取某台主机的主机名和IP地址：

* public static InetAddress getByName(String host):根据主机名或者IP地址的字符串表示得到IP地址对象

测试代码如下：

	public class InetAddressDemo {
		public static void main(String[] args) throws UnknownHostException {
			// public static InetAddress getByName(String host)
			// InetAddress address = InetAddress.getByName("DESKTOP-DVA2NMJ");
			InetAddress address = InetAddress.getByName("192.168.1.121");
	
			// 获取两个东西：主机名，IP地址
			// public String getHostName()
			String name = address.getHostName();
			// public String getHostAddress()
			String ip = address.getHostAddress();
			System.out.println(name + "---" + ip);
		}
	}


### 二、UDP方式实现网络通信 ###

#### （1）第一版 ####

发送端SendDemo：

	/*
	 * UDP协议发送数据：
	 * A:创建发送端Socket对象
	 * B:创建数据，并把数据打包
	 * C:调用Socket对象的发送方法发送数据包
	 * D:释放资源
	 */
	public class SendDemo {
		public static void main(String[] args) throws IOException {
			// 创建发送端Socket对象
			// DatagramSocket()
			DatagramSocket ds = new DatagramSocket();
	
			// 创建数据，并把数据打包
			// DatagramPacket(byte[] buf, int length, InetAddress address, int port)
			// 创建数据
			byte[] bys = "hello,udp,我来了".getBytes();
			// 长度
			int length = bys.length;
			// IP地址对象
			InetAddress address = InetAddress.getByName("192.168.12.92");
			// 端口
			int port = 10086;
			DatagramPacket dp = new DatagramPacket(bys, length, address, port);
	
			// 调用Socket对象的发送方法发送数据包
			// public void send(DatagramPacket p)
			ds.send(dp);
	
			// 释放资源
			ds.close();
		}
	}

接收端ReceiveDemo：

	/*
	 * UDP协议接收数据：
	 * A:创建接收端Socket对象
	 * B:创建一个数据包(接收容器)
	 * C:调用Socket对象的接收方法接收数据
	 * D:解析数据包，并显示在控制台
	 * E:释放资源
	 */
	public class ReceiveDemo {
		public static void main(String[] args) throws IOException {
			// 创建接收端Socket对象
			// DatagramSocket(int port)
			DatagramSocket ds = new DatagramSocket(10086);
	
			// 创建一个数据包(接收容器)
			// DatagramPacket(byte[] buf, int length)
			byte[] bys = new byte[1024];
			int length = bys.length;
			DatagramPacket dp = new DatagramPacket(bys, length);
	
			// 调用Socket对象的接收方法接收数据
			// public void receive(DatagramPacket p)
			ds.receive(dp); // 阻塞式
	
			// 解析数据包，并显示在控制台
			// 获取对方的ip
			// public InetAddress getAddress()
			InetAddress address = dp.getAddress();
			String ip = address.getHostAddress();
			// public byte[] getData():获取数据缓冲区
			// public int getLength():获取数据的实际长度
			byte[] bys2 = dp.getData();
			int len = dp.getLength();
			String s = new String(bys2, 0, len);
			System.out.println(ip + "传递的数据是:" + s);
	
			// 释放资源
			ds.close();
		}
	}


#### （2）第二版--整合优化版 ####

发送端SendDemo：

	public class SendDemo {
		public static void main(String[] args) throws IOException {
			// 创建发送端的Socket对象
			DatagramSocket ds = new DatagramSocket();
	
			// 创建数据并打包
			byte[] bys = "helloworld".getBytes();
			DatagramPacket dp = new DatagramPacket(bys, bys.length,
					InetAddress.getByName("192.168.12.92"), 12345);
	
			// 发送数据
			ds.send(dp);
	
			// 释放资源
			ds.close();
		}
	}

接收端ReceiveDemo:

	/*
	 * 多次启动接收端：
	 * 		java.net.BindException: Address already in use: Cannot bind
	 * 		端口被占用。
	 */
	public class ReceiveDemo {
		public static void main(String[] args) throws IOException {
			// 创建接收端的Socket对象
			DatagramSocket ds = new DatagramSocket(12345);
	
			// 创建一个包裹
			byte[] bys = new byte[1024];
			DatagramPacket dp = new DatagramPacket(bys, bys.length);
	
			// 接收数据
			ds.receive(dp);
	
			// 解析数据
			String ip = dp.getAddress().getHostAddress();
			String s = new String(dp.getData(), 0, dp.getLength());
			System.out.println("from " + ip + " data is : " + s);
	
			// 释放资源
			ds.close();
		}
	}


#### （3）综合案例一 ####

从键盘录入数据进行发送，如果输入的是886那么客户端就结束输入数据。

接收端ReceiveDemo：

	public class ReceiveDemo {
		public static void main(String[] args) throws IOException {
			// 创建接收端的Socket对象
			DatagramSocket ds = new DatagramSocket(12345);
	
			while (true) {
				// 创建一个包裹
				byte[] bys = new byte[1024];
				DatagramPacket dp = new DatagramPacket(bys, bys.length);
	
				// 接收数据
				ds.receive(dp);
	
				// 解析数据
				String ip = dp.getAddress().getHostAddress();
				String s = new String(dp.getData(), 0, dp.getLength());
				System.out.println("from " + ip + " data is : " + s);
			}
	
			// 释放资源
			// 接收端应该一直开着等待接收数据，是不需要关闭
			// ds.close();
		}
	}

发送端SendDemo：

	/*
	 * 数据来自于键盘录入
	 * 键盘录入数据要自己控制录入结束。
	 */
	public class SendDemo {
		public static void main(String[] args) throws IOException {
			// 创建发送端的Socket对象
			DatagramSocket ds = new DatagramSocket();
	
			// 封装键盘录入数据
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line = null;
			while ((line = br.readLine()) != null) {
				if ("886".equals(line)) {
					break;
				}
	
				// 创建数据并打包
				byte[] bys = line.getBytes();
				// DatagramPacket dp = new DatagramPacket(bys, bys.length,
				// InetAddress.getByName("192.168.12.92"), 12345);
				DatagramPacket dp = new DatagramPacket(bys, bys.length,
						InetAddress.getByName("192.168.12.255"), 12345);
	
				// 发送数据
				ds.send(dp);
			}
	
			// 释放资源
			ds.close();
		}
	}

#### （4）综合案例二 ####
实现一个简易的聊天室。

ChatRoom类：

	public class ChatRoom {
		public static void main(String[] args) throws IOException {
			DatagramSocket dsSend = new DatagramSocket();
			DatagramSocket dsReceive = new DatagramSocket(12306);
	
			SendThread st = new SendThread(dsSend);
			ReceiveThread rt = new ReceiveThread(dsReceive);
	
			Thread t1 = new Thread(st);
			Thread t2 = new Thread(rt);
	
			t1.start();
			t2.start();
		}
	}

SendThread线程类：

	public class SendThread implements Runnable {
	
		private DatagramSocket ds;
	
		public SendThread(DatagramSocket ds) {
			this.ds = ds;
		}
	
		@Override
		public void run() {
			try {
				// 封装键盘录入数据
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				String line = null;
				while ((line = br.readLine()) != null) {
					if ("886".equals(line)) {
						break;
					}
	
					// 创建数据并打包
					byte[] bys = line.getBytes();
					// DatagramPacket dp = new DatagramPacket(bys, bys.length,
					// InetAddress.getByName("192.168.12.92"), 12345);
					DatagramPacket dp = new DatagramPacket(bys, bys.length,
							InetAddress.getByName("192.168.12.255"), 12306);
	
					// 发送数据
					ds.send(dp);
				}
	
				// 释放资源
				ds.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

ReceiveThread线程类：

	public class ReceiveThread implements Runnable {
		private DatagramSocket ds;
	
		public ReceiveThread(DatagramSocket ds) {
			this.ds = ds;
		}
	
		@Override
		public void run() {
			try {
				while (true) {
					// 创建一个包裹
					byte[] bys = new byte[1024];
					DatagramPacket dp = new DatagramPacket(bys, bys.length);
	
					// 接收数据
					ds.receive(dp);
	
					// 解析数据
					String ip = dp.getAddress().getHostAddress();
					String s = new String(dp.getData(), 0, dp.getLength());
					System.out.println("from " + ip + " data is : " + s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

### 三、TCP方式实现网络通信 ###

#### 基本实现 ####

接收端ServerDemo：

	/*
	 * TCP协议接收数据：
	 * A:创建接收端的Socket对象
	 * B:监听客户端连接。返回一个对应的Socket对象
	 * C:获取输入流，读取数据显示在控制台
	 * D:释放资源
	 */
	public class ServerDemo {
		public static void main(String[] args) throws IOException {
			// 创建接收端的Socket对象
			// ServerSocket(int port)
			ServerSocket ss = new ServerSocket(8888);
	
			// 监听客户端连接。返回一个对应的Socket对象
			// public Socket accept()
			Socket s = ss.accept(); // 侦听并接受到此套接字的连接。此方法在连接传入之前一直阻塞。
	
			// 获取输入流，读取数据显示在控制台
			InputStream is = s.getInputStream();
	
			byte[] bys = new byte[1024];
			int len = is.read(bys); // 阻塞式方法
			String str = new String(bys, 0, len);
	
			String ip = s.getInetAddress().getHostAddress();
	
			System.out.println(ip + "---" + str);
	
			// 释放资源
			s.close();
			// ss.close(); //这个不应该关闭
		}
	}

发送端ClientDemo：

	/*
	 * TCP协议发送数据：
	 * A:创建发送端的Socket对象
	 * 		这一步如果成功，就说明连接已经建立成功了。
	 * B:获取输出流，写数据
	 * C:释放资源
	 * 
	 * 连接被拒绝。TCP协议一定要先看服务器。
	 * java.net.ConnectException: Connection refused: connect
	 */
	public class ClientDemo {
		public static void main(String[] args) throws IOException {
			// 创建发送端的Socket对象
			// Socket(InetAddress address, int port)
			// Socket(String host, int port)
			// Socket s = new Socket(InetAddress.getByName("192.168.12.92"), 8888);
			Socket s = new Socket("192.168.12.92", 8888);
	
			// 获取输出流，写数据
			// public OutputStream getOutputStream()
			OutputStream os = s.getOutputStream();
			os.write("hello,tcp,我来了".getBytes());
	
			// 释放资源
			s.close();
		}
	}


#### 综合案例 ####

#####（1）服务器给客户端反馈 #####

服务端ServerDemo：

	public class ServerDemo {
		public static void main(String[] args) throws IOException {
			// 创建服务器Socket对象
			ServerSocket ss = new ServerSocket(9999);
	
			// 监听客户端的连接
			Socket s = ss.accept(); // 阻塞
	
			// 获取输入流
			InputStream is = s.getInputStream();
			byte[] bys = new byte[1024];
			int len = is.read(bys); // 阻塞
			String server = new String(bys, 0, len);
			System.out.println("server:" + server);
	
			// 获取输出流
			OutputStream os = s.getOutputStream();
			os.write("数据已经收到".getBytes());
	
			// 释放资源
			s.close();
			// ss.close();
		}
	}

客户端ClientDemo：

	public class ClientDemo {
		public static void main(String[] args) throws IOException {
			// 创建客户端Socket对象
			Socket s = new Socket("192.168.12.92", 9999);
	
			// 获取输出流
			OutputStream os = s.getOutputStream();
			os.write("今天天气很好,适合睡觉".getBytes());
	
			// 获取输入流
			InputStream is = s.getInputStream();
			byte[] bys = new byte[1024];
			int len = is.read(bys);// 阻塞
			String client = new String(bys, 0, len);
			System.out.println("client:" + client);
	
			// 释放资源
			s.close();
		}
	}


##### （2）客户端键盘录入，服务器输出到控制台 #####

服务端ServerDemo：

	public class ServerDemo {
		public static void main(String[] args) throws IOException {
			// 创建服务器Socket对象
			ServerSocket ss = new ServerSocket(22222);
	
			// 监听客户端连接
			Socket s = ss.accept();
	
			// 包装通道内容的流
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
	
			// br.close();
			s.close();
			// ss.close();
		}
	}

客户端ClientDemo：

	/*
	 * 客户端键盘录入，服务器输出到控制台
	 */
	public class ClientDemo {
		public static void main(String[] args) throws IOException {
			// 创建客户端Socket对象
			Socket s = new Socket("192.168.12.92", 22222);
	
			// 键盘录入数据
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// 把通道内的流给包装一下
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));
	
			String line = null;
			while ((line = br.readLine()) != null) {
				// 键盘录入数据要自定义结束标记
				if ("886".equals(line)) {
					break;
				}
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
	
			// 释放资源
			// bw.close();
			// br.close();
			s.close();
		}
	}


##### （3）客户端键盘录入，服务器输出文本文件 #####

服务端ServerDemo：

	public class ServerDemo {
		public static void main(String[] args) throws IOException {
			// 创建服务器Socket对象
			ServerSocket ss = new ServerSocket(23456);
	
			// 监听客户端连接
			Socket s = ss.accept();
	
			// 封装通道内的数据
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			// 封装文本文件
			BufferedWriter bw = new BufferedWriter(new FileWriter("a.txt"));
	
			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
	
			bw.close();
			// br.close();
			s.close();
			// ss.close();
		}
	}

客户端ClientDemo：

	/*
	 * 客户端键盘录入，服务器输出文本文件
	 */
	public class ClientDemo {
		public static void main(String[] args) throws IOException {
			// 创建客户端Socket对象
			Socket s = new Socket("192.168.12.92", 23456);
	
			// 封装键盘录入
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// 封装通道内的数据
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));
	
			String line = null;
			while ((line = br.readLine()) != null) {
				if ("over".equals(line)) {
					break;
				}
	
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
	
			// bw.close();
			// br.close();
			s.close();
		}
	}


##### （4）客户端文本文件，服务器输出到控制台 #####

服务端ServerDemo：

	public class ServerDemo {
		public static void main(String[] args) throws IOException {
			// 创建服务器Socket对象
			ServerSocket ss = new ServerSocket(34567);
	
			// 监听客户端连接
			Socket s = ss.accept();
	
			// 封装通道内的流
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
	
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
	
			
			s.close();
		}
	}

客户端ClientDemo：

	/*
	 * 客户端文本文件，服务器输出到控制台
	 */
	public class ClientDemo {
		public static void main(String[] args) throws IOException {
			// 创建Socket对象
			Socket s = new Socket("192.168.12.92", 34567);
	
			// 封装文本文件
			BufferedReader br = new BufferedReader(new FileReader(
					"InetAddressDemo.java"));
			// 封装通道内的流
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));
	
			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
	
			br.close();
			s.close();
		}
	}


##### （5）客户端文本文件，服务器输出文本文件 #####

服务端UploadServer：

	public class UploadServer {
		public static void main(String[] args) throws IOException {
			// 创建服务器端的Socket对象
			ServerSocket ss = new ServerSocket(11111);
	
			// 监听客户端连接
			Socket s = ss.accept();// 阻塞
	
			// 封装通道内的流
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			// 封装文本文件
			BufferedWriter bw = new BufferedWriter(new FileWriter("Copy.java"));
	
			String line = null;
			while ((line = br.readLine()) != null) { // 阻塞
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
	
			// 给出反馈
			BufferedWriter bwServer = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));
			bwServer.write("文件上传成功");
			bwServer.newLine();
			bwServer.flush();
	
			// 释放资源
			bw.close();
			s.close();
		}
	}


客户端UploadClient：

	/*
	 * 按照我们正常的思路加入反馈信息，结果却没反应。为什么呢?
	 * 读取文本文件是可以以null作为结束信息的，但是呢，通道内是不能这样结束信息的。
	 * 所以，服务器根本就不知道你结束了。而你还想服务器给你反馈。所以，就相互等待了。
	 * 
	 * 如何解决呢?
	 * A:在多写一条数据，告诉服务器，读取到这条数据说明我就结束，你也结束吧。
	 * 		这样做可以解决问题，但是不好。
	 * B:Socket对象提供了一种解决方案
	 * 		public void shutdownOutput()
	 */
	
	public class UploadClient {
		public static void main(String[] args) throws IOException {
			// 创建客户端Socket对象
			Socket s = new Socket("192.168.12.92", 11111);
	
			// 封装文本文件
			BufferedReader br = new BufferedReader(new FileReader(
					"InetAddressDemo.java"));
			// 封装通道内流
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));
	
			String line = null;
			while ((line = br.readLine()) != null) { // 阻塞
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
			
			//Socket提供了一个终止，它会通知服务器你别等了，我没有数据过来了
			s.shutdownOutput();
	
			// 接收反馈
			BufferedReader brClient = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String client = brClient.readLine(); // 阻塞
			System.out.println(client);
	
			// 释放资源
			br.close();
			s.close();
		}
	}


##### （6）上传图片案例 #####

服务端UploadServer：

	public class UploadServer {
		public static void main(String[] args) throws IOException {
			// 创建服务器Socket对象
			ServerSocket ss = new ServerSocket(19191);
	
			// 监听客户端连接
			Socket s = ss.accept();
	
			// 封装通道内流
			BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
			// 封装图片文件
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream("mn.jpg"));
	
			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
	
			// 给一个反馈
			OutputStream os = s.getOutputStream();
			os.write("图片上传成功".getBytes());
	
			bos.close();
			s.close();
		}
	}

客户端UploadClient：

	public class UploadClient {
		public static void main(String[] args) throws IOException {
			// 创建客户端Socket对象
			Socket s = new Socket("192.168.12.92", 19191);
	
			// 封装图片文件
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
					"林青霞.jpg"));
			// 封装通道内的流
			BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
	
			byte[] bys = new byte[1024];
			int len = 0;
			while ((len = bis.read(bys)) != -1) {
				bos.write(bys, 0, len);
				bos.flush();
			}
			
			s.shutdownOutput();
	
			// 读取反馈
			InputStream is = s.getInputStream();
			byte[] bys2 = new byte[1024];
			int len2 = is.read(bys2);
			String client = new String(bys2, 0, len2);
			System.out.println(client);
	
			// 释放资源
			bis.close();
			s.close();
		}
	}

##### （7）多线程上传案例 #####

服务端UploadServer：

	public class UploadServer {
		public static void main(String[] args) throws IOException {
			// 创建服务器Socket对象
			ServerSocket ss = new ServerSocket(11111);
	
			while (true) {
				Socket s = ss.accept();
				new Thread(new UserThread(s)).start();
			}
		}
	}


客户端UploadClient：

	public class UploadClient {
		public static void main(String[] args) throws IOException {
			// 创建客户端Socket对象
			Socket s = new Socket("192.168.12.92", 11111);
	
			// 封装文本文件
			// BufferedReader br = new BufferedReader(new FileReader(
			// "InetAddressDemo.java"));
			BufferedReader br = new BufferedReader(new FileReader(
					"ReceiveDemo.java"));
			// 封装通道内流
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));
	
			String line = null;
			while ((line = br.readLine()) != null) { // 阻塞
				bw.write(line);
				bw.newLine();
				bw.flush();
			}
	
			// Socket提供了一个终止，它会通知服务器你别等了，我没有数据过来了
			s.shutdownOutput();
	
			// 接收反馈
			BufferedReader brClient = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String client = brClient.readLine(); // 阻塞
			System.out.println(client);
	
			// 释放资源
			br.close();
			s.close();
		}
	}

UserThread类：

	public class UserThread implements Runnable {
		private Socket s;
	
		public UserThread(Socket s) {
			this.s = s;
		}
	
		@Override
		public void run() {
			try {
				// 封装通道内的流
				BufferedReader br = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				// 为了防止名称冲突
				String newName = System.currentTimeMillis() + ".java";
				BufferedWriter bw = new BufferedWriter(new FileWriter(newName));
	
				String line = null;
				while ((line = br.readLine()) != null) { // 阻塞
					bw.write(line);
					bw.newLine();
					bw.flush();
				}
	
				// 给出反馈
				BufferedWriter bwServer = new BufferedWriter(
						new OutputStreamWriter(s.getOutputStream()));
				bwServer.write("文件上传成功");
				bwServer.newLine();
				bwServer.flush();
	
				// 释放资源
				bw.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

