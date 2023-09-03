package org.example.chapter3;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.File;

public class C322MethodCreateTest implements Opcodes {
    public static void main(String[] args) throws Exception {
        String userClassInternalName = "test/User";
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        writer.visit(V1_8, ACC_PUBLIC,userClassInternalName,null, Type.getInternalName(Object.class),null);
        writer.visitField(ACC_PRIVATE,"f","I",null,0).visitEnd();
        {

            MethodVisitor getFVisitor = writer.visitMethod(ACC_PUBLIC, "getF", "()I", null, null);
            /*
             字节码
             ALOAD 0   // 加载this到操作数栈
             GETFIELD user f I // 弹栈this，获取this.f的值并压栈
             IRETURN // 弹栈返回值
             */
            getFVisitor.visitVarInsn(ALOAD,0);
            getFVisitor.visitFieldInsn(GETFIELD,userClassInternalName,"f","I");
            getFVisitor.visitInsn(IRETURN);

            // 必须调用，以确保writer自行计算帧容量
            // 实际是1，1，因为局部变量表只有this，整个执行过程操作数栈也最多1个元素。
            getFVisitor.visitMaxs(0,0);
            getFVisitor.visitEnd();
        }

        {
            MethodVisitor checkAndSetFVisitor = writer.visitMethod(ACC_PUBLIC, "checkAndSetF", "(I)V", null, null);
            // see org/example/chapter3/Bean.java:54
            /*
                public void checkAndSetF(int f) {
                    if (f >= 0) {
                        this.f = f;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
             */
            checkAndSetFVisitor.visitCode();
            checkAndSetFVisitor.visitLabel(new Label()); // if表达式本身
            checkAndSetFVisitor.visitVarInsn(ILOAD,1); // 0 是this，1是第一个入参
            Label ifBlock = new Label();
            Label elseBlock = new Label();
            checkAndSetFVisitor.visitJumpInsn(IFLE,elseBlock); //IFLE的第一个指令参数是0，所以是0<栈顶操作数
            checkAndSetFVisitor.visitLabel(ifBlock);
            checkAndSetFVisitor.visitVarInsn(ALOAD,0); // this入栈
            checkAndSetFVisitor.visitVarInsn(ILOAD,1); // 参数f再次入栈
            checkAndSetFVisitor.visitFieldInsn(PUTFIELD,userClassInternalName,"f","I"); // this.f = f;
            checkAndSetFVisitor.visitLabel(elseBlock);
            String expInnerName = Type.getInternalName(IllegalArgumentException.class);
            checkAndSetFVisitor.visitTypeInsn(NEW,expInnerName);
            checkAndSetFVisitor.visitInsn(DUP);
            checkAndSetFVisitor.visitMethodInsn(INVOKESPECIAL,expInnerName,   "<init>","()V",false);
            checkAndSetFVisitor.visitInsn(ATHROW);

            checkAndSetFVisitor.visitMaxs(0,0);
            checkAndSetFVisitor.visitEnd();
        }

        writer.visitEnd();

        byte[] bytes = writer.toByteArray();
        IOUtils.write(bytes,
                FileUtils.newOutputStream(new File("target/" + userClassInternalName + ".class"),false));
    }
}
