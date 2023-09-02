package org.example.chapter3;

/*
// class version 52.0 (52)
// access flags 0x21
public class org/example/chapter3/ExceptionCatchByteCode {

  // compiled from: ExceptionCatchByteCode.java

  // access flags 0x1
  public <init>()V
   L0
    LINENUMBER 3 L0
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
   L1
    LOCALVARIABLE this Lorg/example/chapter3/ExceptionCatchByteCode; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x9
  public static sleep(J)V
    TRYCATCHBLOCK L0 L1 L2 java/lang/InterruptedException // try 代码块位于L0-L1之间，L2对应的代码块为catch异常处理部分
   L0
    LINENUMBER 6 L0
    LLOAD 0
    INVOKESTATIC java/lang/Thread.sleep (J)V
   L1 // 最后的return语句
    LINENUMBER 9 L1
    GOTO L3
   L2
    LINENUMBER 7 L2
   FRAME SAME1 java/lang/InterruptedException
    ASTORE 2 // 将异常存入到局部变量表2（即e，见L5 声明）
   L4
    LINENUMBER 8 L4
    ALOAD 2 // 将局部变量表2（即e）压入操作数栈
    INVOKEVIRTUAL java/lang/InterruptedException.printStackTrace ()V // 弹出栈顶的e，调用printStackTrace方法
   L3
    LINENUMBER 10 L3
   FRAME SAME
    RETURN
   L5 // 这里并没有将this在局部变量表做声明，因为这是static方法，没有对象this。PS：考虑对象方法，如果方法内没有访问任何this内容（字段、方法），this会声明吗？
    LOCALVARIABLE e Ljava/lang/InterruptedException; L4 L3 2
    LOCALVARIABLE d J L0 L5 0
    MAXSTACK = 2
    MAXLOCALS = 3
}

 */
public class ExceptionCatchByteCode {
    public static void sleep(long d) {
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
