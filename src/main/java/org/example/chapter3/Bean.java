package org.example.chapter3;

/*
// class version 52.0 (52)
// access flags 0x21
public class org/example/chapter3/Bean {

  // compiled from: Bean.java

  // access flags 0x2
  private I f

  // access flags 0x1
  public <init>()V // 默认的构造函数  Bean() {super();}
   L0
    LINENUMBER 3 L0
    ALOAD 0
    INVOKESPECIAL java/lang/Object.<init> ()V
    RETURN
   L1
    LOCALVARIABLE this Lorg/example/chapter3/Bean; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x1
  public getF()I
   L0
    LINENUMBER 7 L0
    ALOAD 0 // 帧局部变量表第一个元素是this，将this压入帧中的操作数栈
    GETFIELD org/example/chapter3/Bean.f : I //  弹出操作数栈的栈顶，即this，并访问他的字段f，将结果重新压入操作数栈
    IRETURN // 取出操作数栈栈顶数据，即字段f的值，返回
   L1 // 局部变量表和操作数栈初始化，将this放在局部变量表的0位置
    LOCALVARIABLE this Lorg/example/chapter3/Bean; L0 L1 0
    MAXSTACK = 1
    MAXLOCALS = 1

  // access flags 0x1
  public setF(I)V
   L0
    LINENUMBER 11 L0
    ALOAD 0 // 取局部变量表0，即this，压入操作数栈。
    ILOAD 1 // 取局部变量表1，即方法的第一个入参，压入操作数栈
    PUTFIELD org/example/chapter3/Bean.f : I // putfield 一次性弹出2个栈元素，即this和第一个入参，将入参赋值给this.f
   L1
    LINENUMBER 12 L1
    RETURN
   L2 // 局部变量表和操作数栈初始化，将this、第一个入参放在局部变量表的0、1
    LOCALVARIABLE this Lorg/example/chapter3/Bean; L0 L2 0
    LOCALVARIABLE f I L0 L2 1
    MAXSTACK = 2
    MAXLOCALS = 2

  // access flags 0x1
  public checkAndSetF(I)V
   L0
    LINENUMBER 66 L0
    ILOAD 1 // 取局部变量1（即第一个入参f），压入操作数栈
    IFLT L1 // 取栈顶操作数与0进行比较，如果<0则跳转到L1标签。否则顺序往下执行L2标签 可以看到这里实际编译器做了优化，将if、else的顺序调换了一下
   L2
    LINENUMBER 67 L2
    ALOAD 0 // 取局部变量0 this压入操作数栈
    ILOAD 1 // 取局部变量1 f压入操作数栈
    PUTFIELD org/example/chapter3/Bean.f : I // putfield 一次弹出2个操作数，将f赋值给this.f
    GOTO L3 // 跳转到L3标签执行return
   L1
    LINENUMBER 69 L1
   FRAME SAME // 定义 栈映射帧，有点类似与帧快照（存储的不是具体的值快照，而是类型快照，变量表和操作数栈内元素的具体类型表），
              // 在编译阶段便分析执行下一条语句之前，当前帧的内容应该都是什么类型的。实际上java不在每一个语句都记录帧快照，只在一些关键节点记录帧快照，并且不记录全部的帧内容，而只记录diff。初始帧不用存储，可以通过方法签名计算出来。
              // 主要作用用于加快虚拟机的类验证过程
    NEW java/lang/IllegalArgumentException // new对象并压入操作数栈
    DUP // 拷贝一次操作数栈顶并压栈，即操作数栈最上面有2个Exception的引用。复制一次入栈的原因是，接下来要用两次
    INVOKESPECIAL java/lang/IllegalArgumentException.<init> ()V // 弹出副本，并执行init构造函数
    ATHROW // 弹出剩下的那个exception引用，作为异常抛出
   L3
    LINENUMBER 71 L3
   FRAME SAME
    RETURN // 从 l2跳转过来的，执行空的return
   L4
    LOCALVARIABLE this Lorg/example/chapter3/Bean; L0 L4 0
    LOCALVARIABLE f I L0 L4 1
    MAXSTACK = 2
    MAXLOCALS = 2
}
 */
public class Bean {
    private int f;

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public void checkAndSetF(int f) {
        if (f >= 0) {
            this.f = f;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
