package org.example.chapter2;

import org.objectweb.asm.Type;

public class TypeTest {
    public static void main(String[] args) throws Exception {
        // class
        Type stringType = Type.getType(String.class);
        System.out.println(stringType.getInternalName());
        System.out.println(stringType.getDescriptor());
        Type stringType2 = Type.getType("Ljava/lang/String;"); // 对象类型的';'不能少
        System.out.println(stringType2.getInternalName()); // true
        System.out.println(stringType2.getInternalName().equals(stringType.getInternalName())); // true
        System.out.println("Ljava/lang/Integer;".equals(Type.getDescriptor(Integer.class))); // true
        System.out.println("I".equals(Type.INT_TYPE.getDescriptor())); // true

        // 方法
        Type toStringMethodType = Type.getType(String.class.getMethod("toString"));
        System.out.println(toStringMethodType.getDescriptor()); // ()Ljava/lang/String;

        Type itoaMethodType = Type.getMethodType("(Ljava/lang/Integer;)Ljava/lang/String;"); // itoa
        System.out.println(itoaMethodType.getReturnType().getDescriptor()); // Ljava/lang/String;
    }
}
