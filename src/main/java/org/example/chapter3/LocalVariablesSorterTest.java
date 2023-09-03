package org.example.chapter3;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.File;
import java.io.PrintWriter;

public class LocalVariablesSorterTest {
    static class AddTimerMethodAdapter4 extends LocalVariablesSorter implements Opcodes {
        private int time;

        public AddTimerMethodAdapter4(int api, int access, String descriptor, MethodVisitor methodVisitor) {
            super(api, access, descriptor, methodVisitor); // LocalVariablesSorter 另外一个构造函数，子类是不可以调用的。
        }

        @Override
        public void visitCode() {
            super.visitCode();
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(System.class),
                    "currentTimeMillis", "()J", false);
            time = newLocal(Type.LONG_TYPE); // 返回变量在局部变量表中的index，后续局部变量表的相关指令可以用这个index作为操作数
            mv.visitVarInsn(Opcodes.LSTORE,time);
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(System.class),
                        "currentTimeMillis", "()J", false); // 静态调用的结果入栈
                mv.visitVarInsn(LLOAD,time); // 原来存储的time入栈
                mv.visitInsn(LSUB); // 弹出栈顶的两个元素相减，stack[top-1] - stack[top]; 并将结果重新入栈
                mv.visitFieldInsn(GETSTATIC,Type.getInternalName(C.class),"timer","J");
                mv.visitInsn(LADD);
                mv.visitFieldInsn(PUTSTATIC,Type.getInternalName(C.class),"timer","J");
            }
            super.visitInsn(opcode);
        }

    }

    static class AddTimerClassAdapter extends ClassVisitor implements Opcodes {

        protected AddTimerClassAdapter(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor nextVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
            // 在writer之前，对方法增加插入内容
            if (! name.equals("<init>")) {
                nextVisitor = new AddTimerMethodAdapter4(api,access,descriptor,nextVisitor);
            }
            return nextVisitor;
        }

        @Override
        public void visitEnd() {
            // 增加timer字段
            visitField(ACC_PUBLIC + ACC_STATIC ,"timer","J",null,0L);
            super.visitEnd();
        }

    }

    public static void main(String[] args) throws Exception {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES); // 创建了新的局部变量，会影响栈帧快照，需要重新计算。
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(writer,new PrintWriter(System.out));
        AddTimerClassAdapter adapter = new AddTimerClassAdapter(Opcodes.ASM9, traceClassVisitor);
        ClassReader reader = new ClassReader(C.class.getName());
        reader.accept(adapter,0);
        IOUtils.write(writer.toByteArray(),
                FileUtils.newOutputStream(new File("target/" + C.class.getSimpleName() + "2.class"),false));
    }

}
