package org.example.chapter3;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.File;
import java.lang.reflect.Method;

public class C321MethodWithBodyCreateTest {
    public static void main(String[] args) throws Exception {
        // 0 什么都不自动计算。
        // COMPUTE_MAXS 自动计算栈帧最大容量（局部变量表和操作数栈的大小），visitMaxs依然需要调用（下游事件需要触发），但参数会被忽略，自动计算。此选项不会计算帧类型快照。
        // COMPUTE_FRAMES 自动计算所有，包括栈帧最大容量和帧类型快照
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassReader reader = new ClassReader(TestTarget.class.getName());
        Method method = TestTarget.class.getMethod("setA", int.class);
        reader.accept(new ClassVisitor(Opcodes.ASM8,writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                // 拦截reader触发的时间源，增加一个方法。
                if (name.equals(method.getName()) && descriptor.equals(Type.getMethodDescriptor(method))) {
                    // 生产一个setAxxx的方法。
                    /*
                        public void setAxxx(int a) {
                            printf(a);
                        }
                        对应生成的字节码：
                          // access flags 0x1
                          public setAxxx(I)V
                           L0
                            ILOAD 1
                            INVOKESTATIC org/example/chapter3/TestTarget.printf (I)V
                           L1
                            LOCALVARIABLE this Lorg/example/chapter3/TestTarget; L0 L1 0
                            LOCALVARIABLE a I L0 L1 1
                            MAXSTACK = 1
                            MAXLOCALS = 2
                     */
                    MethodVisitor addedMethodVisitor = cv.visitMethod(access, name + "xxx", descriptor, signature, exceptions);
                    // 实际methodVisitor可以生成多个，交叉着向下游转发事件，是完全可以的，相互隔离不受影响。并发下发除外，asm应该不支持并发处理。
                    addedMethodVisitor.visitCode(); // 必须首先调用
                    Label start, end;
                    addedMethodVisitor.visitLabel(start = new Label());
                    addedMethodVisitor.visitVarInsn(Opcodes.ILOAD,1); // 将参数a压入操作数栈
                    addedMethodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,Type.getInternalName(TestTarget.class),"printf",
                            "(I)V",false); // 执行静态方法调用

                    addedMethodVisitor.visitLabel(end = new Label());
                    addedMethodVisitor.visitLocalVariable("this",Type.getDescriptor(TestTarget.class),null,start,end,0);
                    addedMethodVisitor.visitLocalVariable("a","I",null,start,end,1);

                    addedMethodVisitor.visitMaxs(10,10); // 实际用不到这个参数。如果classWriter传递的是0，则需要手动计算，非常麻烦。
                    addedMethodVisitor.visitEnd(); // 必须最后调用
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        },0);

        byte[] bytes = writer.toByteArray();
        IOUtils.write(bytes,
                FileUtils.newOutputStream(new File("target/TestTarget.class"),false));
    }
}
