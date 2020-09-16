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

