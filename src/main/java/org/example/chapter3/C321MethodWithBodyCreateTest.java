package org.example.chapter3;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.File;
import java.lang.reflect.Method;

public class C321MethodWithBodyCreateTest {
    public static void main(String[] args) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        ClassReader reader = new ClassReader(TestTarget.class.getName());
        Method method = TestTarget.class.getMethod("setA", int.class);
        reader.accept(new ClassVisitor(Opcodes.ASM8,writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                // 拦截reader触发的时间源，增加一个方法。
                if (name.equals(method.getName()) && descriptor.equals(Type.getMethodDescriptor(method))) {
                    // 生产一个setAxxx的方法。
                    MethodVisitor addedMethodVisitor = cv.visitMethod(access, name + "xxx", descriptor, signature, exceptions);
                    addedMethodVisitor.visitCode(); // 必须首先调用
                    Label start, end;
                    addedMethodVisitor.visitLabel(start = new Label());
                    addedMethodVisitor.visitVarInsn(Opcodes.ILOAD,1); // 将参数a压入操作数栈
                    addedMethodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC,Type.getInternalName(TestTarget.class),"printf",
                            "(I)",false); // 执行静态方法调用


                    addedMethodVisitor.visitLabel(end = new Label());
                    addedMethodVisitor.visitLocalVariable("this",Type.getDescriptor(TestTarget.class),null,start,end,0);
                    addedMethodVisitor.visitLocalVariable("a","I",null,start,end,1);

                    addedMethodVisitor.visitMaxs(10,10); // 实际用不到这个大
                    addedMethodVisitor.visitEnd(); // 必须最后调用
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        },0);

        byte[] bytes = writer.toByteArray();
        IOUtils.write(bytes,
                FileUtils.newOutputStream(new File("target/TestTargetC321.class"),false));
    }
}
