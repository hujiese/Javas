# Java Basic

## 一、String部分

### 1、String、StringBuilder和StringBuffer

```java
public class StringDemos {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = "world";
        String s3 = "!";

        // 线程安全，但是效率低
        StringBuffer sb = new StringBuffer();
        sb.append(s1);
        sb.append(s2);
        sb.append(s3);
        System.out.println(sb.toString());

        // 线程不安全，但是效率高，通常使用这个
        StringBuilder sl = new StringBuilder();
        sl.append(s1);
        sl.append(s2);
        sl.append(s3);
        System.out.println(sl.toString());
    }
}
```

## 二、Math部分

### 1、BigInteger和BigDecimal

* BigInteger用于整数大数运算
* BigDecimal用于浮点大数运算

```java
import java.math.BigInteger;

public class MathDemo {
    public static void main(String[] args){
        BigInteger bi = BigInteger.valueOf(1);
        for(int i = 2; i < 1000; ++i){
            bi = bi.multiply(BigInteger.valueOf(i));
        }
        System.out.println(bi);
    }
}
```

### 2、equals方法

自动装箱规范，boolean、byte、char <= 127，介于-128 ~ 127之间的short和int会被包装到固定的对象中，所以比较会相等，例如：

```java
public class EqualsDemo {
    public static void main(String[] args){
        Integer a = 127;
        Integer b = 127;
        System.out.println(a == b); // true

        Integer a1 = -128;
        Integer b1 = -128;
        System.out.println(a1 == b1); // true

        Integer a2 = 128;
        Integer b2 = 128;
        System.out.println(a2 == b2); // false

    }
}
```

## 三、Array部分

### 1、数组拷贝

```java
import java.util.Arrays;

public class ArrayDemo {
    public static void main(String[] args){
        int[] array1 = new int[]{1, 2, 3, 4, 5};
        int[] array2 = array1;// 浅拷贝
        int[] array3 = Arrays.copyOf(array1, array1.length);// 深拷贝
    }
}
```

## 四、lambda表达式

### 1、常规操作

```java
public static void test1(){
    Java8Tester tester = new Java8Tester();

    // 类型声明
    MathOperation addition = (int a, int b) -> a + b;

    // 不用类型声明
    MathOperation subtraction = (a, b) -> a - b;

    // 大括号中的返回语句
    MathOperation multiplication = (int a, int b) -> { return a * b; };

    // 没有大括号及返回语句
    MathOperation division = (int a, int b) -> a / b;

    System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
    System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
    System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
    System.out.println("10 / 5 = " + tester.operate(10, 5, division));

    // 不用括号
    GreetingService greetService1 = message ->
            System.out.println("Hello " + message);

    // 用括号
    GreetingService greetService2 = (message) ->
            System.out.println("Hello " + message);

    greetService1.sayMessage("Runoob");
    greetService2.sayMessage("Google");
}

interface MathOperation {
    int operation(int a, int b);
}

interface GreetingService {
    void sayMessage(String message);
}

private int operate(int a, int b, MathOperation mathOperation){
    return mathOperation.operation(a, b);
}
```

### 2、Runnable方法

```java
private static void test2() {
    // 1.1使用匿名内部类
    new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("Hello world !");
        }
    }).start();

   // 1.2使用 lambda expression
    new Thread(() -> {
        System.out.println("Hello world !");
    }).start();
}
```

## 五、动态代理

## 六、反射

## 七、并发

### 1、Future

参考 https://www.cnblogs.com/jcjssl/p/9592145.html

并发编程中，我们经常用到非阻塞的模型，在之前的多线程的三种实现中，不管是继承thread类还是实现runnable接口，都无法保证获取到之前的执行结果。通过实现Callback接口，并用Future可以来接收多线程的执行结果。

Future表示一个可能还没有完成的异步任务的结果，针对这个结果可以添加Callback以便在任务执行成功或失败后作出相应的操作。

举个例子：比如去吃早点时，点了包子和凉菜，包子需要等3分钟，凉菜只需1分钟，如果是串行的一个执行，在吃上早点的时候需要等待4分钟，但是因为你在等包子的时候，可以同时准备凉菜，所以在准备凉菜的过程中，可以同时准备包子，这样只需要等待3分钟。那Future这种模式就是后面这种执行模式。

例如：

```java
import java.util.concurrent.*;

/**
 * @version 1.01 2012-01-26
 * @author Cay Horstmann
 */
public class FutureTest
{
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();

        // 等凉菜
        Callable ca1 = new Callable(){

            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "凉菜准备完毕";
            }
        };
        FutureTask<String> ft1 = new FutureTask<String>(ca1);
        new Thread(ft1).start();

        // 等包子 -- 必须要等待返回的结果，所以要调用join方法
        Callable ca2 = new Callable(){

            @Override
            public Object call() throws Exception {
                try {
                    Thread.sleep(1000*3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "包子准备完毕";
            }
        };
        FutureTask<String> ft2 = new FutureTask<String>(ca2);
        new Thread(ft2).start();
        
        System.out.println(ft2.get());
        System.out.println(ft1.get());
        
        long end = System.currentTimeMillis();
        System.out.println("准备完毕时间："+(end-start));
    }
}
```

输出结果如下，其中，get（）方法可以当任务结束后返回一个结果，如果调用时，工作还没有结束，则会阻塞线程，直到任务执行完毕：

```shell
包子准备完毕
凉菜准备完毕
准备完毕时间：3004
```

等待3s之后先后输出”包子准备完毕“和”凉菜准备完毕“。

设想，准备包子和准备凉菜是两个相关的任务，包子上好了才能再上凉菜。如果换成同步方法，包子准备3s，凉菜准备1s，总共4s；如果换成多线程方法，为了实现这样的先后通过流程，也需要4s（不然可能先上了凉菜再上包子）。使用异步的方法，准备包子3s过程中凉菜也准备好了，上完包子马上就可以上凉菜了，只需要3s时间。

## 八、序列化

src/serialable 文件夹下有Manager和Employee两个对象，ObjectStreamTest中将这两个对象序列化到employee.dat中，然后读取该文件，将三个对象反序列化出来：

```java
class ObjectStreamTest
{
   public static void main(String[] args) throws IOException, ClassNotFoundException
   {
      Employee harry = new Employee("Harry Hacker", 50000, 1989, 10, 1);
      Manager carl = new Manager("Carl Cracker", 80000, 1987, 12, 15);
      carl.setSecretary(harry);
      Manager tony = new Manager("Tony Tester", 40000, 1990, 3, 15);
      tony.setSecretary(harry);

      Employee[] staff = new Employee[3];

      staff[0] = carl;
      staff[1] = harry;
      staff[2] = tony;

      // save all employee records to the file employee.dat         
      try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("employee.dat"))) 
      {
         out.writeObject(staff);
      }

      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("employee.dat")))
      {
         // retrieve all records into a new array

         Employee[] newStaff = (Employee[]) in.readObject();

         // raise secretary's salary
         newStaff[1].raiseSalary(10);

         // print the newly read employee records
         for (Employee e : newStaff)
            System.out.println(e);
      }
   }
}
```

序列化可以用于clone一个对象，而且是深拷贝，可以实现对象的clone方法，将对象序列化到字节流中，然后从该字节流中还原出一个对象，以此达到深拷贝目的。需要注意的是，这个方法虽然很方便，但比显式构建新对象并复制或者克隆数据域的方法会慢很多。

测试代码可见src/serialable/SerialCloneTest.java。