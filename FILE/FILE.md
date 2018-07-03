## FILE API 用例参考 ##
File:文件和目录(文件夹)路径名的抽象表示形式
### 构造方法 ###

 * 	File(String pathname):根据一个路径得到File对象
 * 	File(String parent, String child):根据一个目录和一个子文件/目录得到File对象
 * 	File(File parent, String child):根据一个父File对象和一个子文件/目录得到File对象


		public class FileDemo {
			public static void main(String[] args) {
				// File(String pathname)：根据一个路径得到File对象
				// 把e:\\demo\\a.txt封装成一个File对象
				File file = new File("E:\\demo\\a.txt");
		
				// File(String parent, String child):根据一个目录和一个子文件/目录得到File对象
				File file2 = new File("E:\\demo", "a.txt");
		
				// File(File parent, String child):根据一个父File对象和一个子文件/目录得到File对象
				File file3 = new File("e:\\demo");
				File file4 = new File(file3, "a.txt");
		
				// 以上三种方式其实效果一样
			}
		}


### 创建功能 ###
 *	public boolean createNewFile():创建文件 如果存在这样的文件，就不创建了
 *	public boolean mkdir():创建文件夹 如果存在这样的文件夹，就不创建了
 *	public boolean mkdirs():创建文件夹,如果父文件夹不存在，会帮你创建出来


		public class FileDemo {
			public static void main(String[] args) throws IOException {
				// 需求：我要在e盘目录下创建一个文件夹demo
				File file = new File("e:\\demo");
				System.out.println("mkdir:" + file.mkdir());
		
				// 需求:我要在e盘目录demo下创建一个文件a.txt
				File file2 = new File("e:\\demo\\a.txt");
				System.out.println("createNewFile:" + file2.createNewFile());
		
				// 其实我们有更简单的方法
				File file7 = new File("e:\\aaa\\bbb\\ccc\\ddd");
				System.out.println("mkdirs:" + file7.mkdirs());
			}
		}

### 删除功能 ###
* public boolean delete()

注意：

 * 	如果你创建文件或者文件夹忘了写盘符路径，那么，默认在项目路径下。
 * 	Java中的删除不走回收站。
 * 	要删除一个文件夹，请注意该文件夹内不能包含文件或者文件夹
 

		public class FileDemo {
			public static void main(String[] args) throws IOException {
		
				File file = new File("a.txt");
				System.out.println("createNewFile:" + file.createNewFile());
		
				File file2 = new File("aaa\\bbb\\ccc");
				System.out.println("mkdirs:" + file2.mkdirs());
		
				// 删除功能：删除a.txt这个文件
				File file3 = new File("a.txt");
				System.out.println("delete:" + file3.delete());
		
				// 删除功能：删除ccc这个文件夹
				File file4 = new File("aaa\\bbb\\ccc");
				System.out.println("delete:" + file4.delete());
		
				File file6 = new File("aaa\\bbb");
				File file7 = new File("aaa");
				System.out.println("delete:" + file6.delete());
				System.out.println("delete:" + file7.delete());
			}
		}

### 重命名功能 ###
* public boolean renameTo(File dest)

注意：

 * 	如果路径名相同，就是改名。
 * 	如果路径名不同，就是改名并剪切。


		public class FileDemo {
			public static void main(String[] args) {	
			// 创建一个文件对象
			// File file = new File("a.jpg");
			// // 需求：改这个文件的名称为"b.jpg"
			// File newFile = new File("b.jpg");
			// System.out.println("renameTo:" + file.renameTo(newFile));

				File file2 = new File("a.jpg");
				File newFile2 = new File("e:\\b.jpg");
				System.out.println("renameTo:" + file2.renameTo(newFile2));
			}
		}

### 判断功能 ###
 * public boolean isDirectory():判断是否是目录
 * public boolean isFile():判断是否是文件
 * public boolean exists():判断是否存在
 * public boolean canRead():判断是否可读
 * public boolean canWrite():判断是否可写
 * public boolean isHidden():判断是否隐藏


		public class FileDemo {
			public static void main(String[] args) {
				// 创建文件对象
				File file = new File("a.txt");
		
				System.out.println("isDirectory:" + file.isDirectory());// false
				System.out.println("isFile:" + file.isFile());// true
				System.out.println("exists:" + file.exists());// true
				System.out.println("canRead:" + file.canRead());// true
				System.out.println("canWrite:" + file.canWrite());// true
				System.out.println("isHidden:" + file.isHidden());// false
			}
		}

### 获取功能 ###
 * public String getAbsolutePath()：获取绝对路径
 * public String getPath():获取相对路径
 * public String getName():获取名称
 * public long length():获取长度。字节数
 * public long lastModified():获取最后一次的修改时间，毫秒值


		public class FileDemo {
			public static void main(String[] args) {
				// 创建文件对象
				File file = new File("demo\\test.txt");
		
				System.out.println("getAbsolutePath:" + file.getAbsolutePath());
				System.out.println("getPath:" + file.getPath());
				System.out.println("getName:" + file.getName());
				System.out.println("length:" + file.length());
				System.out.println("lastModified:" + file.lastModified());
		
				// 1416471971031
				Date d = new Date(1416471971031L);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String s = sdf.format(d);
				System.out.println(s);
			}
		}

### 获取功能 ###
 * public String[] list():获取指定目录下的所有文件或者文件夹的名称数组
 * public File[] listFiles():获取指定目录下的所有文件或者文件夹的File数组


		public class FileDemo {
			public static void main(String[] args) {
				// 指定一个目录
				File file = new File("e:\\");
		
				// public String[] list():获取指定目录下的所有文件或者文件夹的名称数组
				String[] strArray = file.list();
				for (String s : strArray) {
					System.out.println(s);
				}
				System.out.println("------------");
		
				// public File[] listFiles():获取指定目录下的所有文件或者文件夹的File数组
				File[] fileArray = file.listFiles();
				for (File f : fileArray) {
					System.out.println(f.getName());
				}
			}
		}


### 综合案例一 ###
需求：判断E盘目录下是否有后缀名为.jpg的文件，如果有，就输出此文件名称。

方案一：

		public class FileDemo {
			public static void main(String[] args) {
				// 封装e判断目录
				File file = new File("e:\\");
		
				// 获取该目录下所有文件或者文件夹的File数组
				File[] fileArray = file.listFiles();
		
				// 遍历该File数组，得到每一个File对象，然后判断
				for (File f : fileArray) {
					// 是否是文件
					if (f.isFile()) {
						// 继续判断是否以.jpg结尾
						if (f.getName().endsWith(".jpg")) {
							// 就输出该文件名称
							System.out.println(f.getName());
						}
					}
				}
			}
		}


方案二--使用FilenameFilter:

 * public String[] list(FilenameFilter filter)
 * public File[] listFiles(FilenameFilter filter)


		public class FileDemo2 {
			public static void main(String[] args) {
				// 封装e判断目录
				File file = new File("e:\\");
		
				// 获取该目录下所有文件或者文件夹的String数组
				String[] strArray = file.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						// 是否把某个文件或者文件夹的名称加不加到数组中，取决于这里的返回值是true还是false，这个的true或者false应该是我们通过某种判断得到的。
						return new File(dir, name).isFile() && name.endsWith(".jpg");
					}
				});
		
				// 遍历
				for (String s : strArray) {
					System.out.println(s);
				}
			}
		}


### 综合案例二 ###
需求：把E:\评书\三国演义\三国演义\_001\_[评书网-今天很高兴,明天就IO了]_桃园三结义.avi重命名为E:\评书\三国演义\001\_桃园三结义.avi。

 思路：

 * 	封装目录
 * 	获取该目录下所有的文件的File数组
 * 	遍历该File数组，得到每一个File对象
 * 	拼接一个新的名称，然后重命名即可
 

		public class FileDemo {
			public static void main(String[] args) {
				// 封装目录
				File srcFolder = new File("E:\\评书\\三国演义");
		
				// 获取该目录下所有的文件的File数组
				File[] fileArray = srcFolder.listFiles();
		
				// 遍历该File数组，得到每一个File对象
				for (File file : fileArray) {
					// System.out.println(file);
					// E:\评书\三国演义\三国演义_001_[评书网-今天很高兴,明天就IO了]_桃园三结义.avi
					// 改后：E:\评书\三国演义\001_桃园三结义.avi
					String name = file.getName(); // 三国演义_001_[评书网-今天很高兴,明天就IO了]_桃园三结义.avi
		
					int index = name.indexOf("_");
					String numberString = name.substring(index + 1, index + 4);
		
					int endIndex = name.lastIndexOf('_');
					String nameString = name.substring(endIndex);
		
					String newName = numberString.concat(nameString); // 001_桃园三结义.avi
					// System.out.println(newName);
		
					File newFile = new File(srcFolder, newName); // E:\\评书\\三国演义\\001_桃园三结义.avi
		
					// 重命名即可
					file.renameTo(newFile);
				}
			}
		}