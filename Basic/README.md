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

## 三、Array部分

## 1、数组拷贝

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