package org.example.chapter3;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.File;

/**
 * public class C {
 *     public static long timer;
 *     public void m() throws Exception {
 *         timer -= System.currentTimeMillis();
 *         Thread.sleep(100);
 *         timer += System.currentTimeMillis();
 *     }
 * }
 *
 * PS：考虑try finally怎么实现？
 */
public class C324CounterAdapter extends ClassVisitor implements Opcodes {

    public C324CounterAdapter(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor nextMethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        // 这里没有排除构造函数，所以其实构造函数也会被添加。静态方法也是。
        return new MethodVisitor(Opcodes.ASM9,nextMethodVisitor) {
            @Override
            public void visitCode() {
                super.visitCode();
                /*
                 * 插入  timer -= System.currentTimeMillis();
                 */
                // timer是静态表中，并不在方法栈的局部变量表中。所以不是通过*LOAD从局部变量表压栈，而是通过GETSTATIC
                mv.visitFieldInsn(GETSTATIC, Type.getInternalName(C.class),"timer","J");
                mv.visitMethodInsn(INVOKESTATIC,Type.getInternalName(System.class),"currentTimeMillis","()J",false);
                // 将栈顶两 long 型数值相减并将结果压入栈顶
                mv.visitInsn(LSUB);
                // 将栈顶结果写回timer字段
                mv.visitFieldInsn(PUTSTATIC, Type.getInternalName(C.class),"timer","J");
            }

            @Override
            public void visitInsn(int opcode) {
                /*
                 * 在return\throw指令之前添加
                 */
                if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                    // timer是静态表中，并不在方法栈的局部变量表中。所以不是通过*LOAD从局部变量表压栈，而是通过GETSTATIC
                    mv.visitFieldInsn(GETSTATIC, Type.getInternalName(C.class),"timer","J");
                    mv.visitMethodInsn(INVOKESTATIC,Type.getInternalName(System.class),"currentTimeMillis","()J",false);
                    // 将栈顶两 long 型数值相加并将结果压入栈顶
                    mv.visitInsn(LADD);
                    // 将栈顶结果写回timer字段
                    mv.visitFieldInsn(PUTSTATIC, Type.getInternalName(C.class),"timer","J");
                }
                super.visitInsn(opcode);
            }
        };
    }

    @Override
    public void visitEnd() {
        // 增加一个timer字段
        this.visitField(ACC_PUBLIC + ACC_STATIC,"timer","J",null,0);
        super.visitEnd();
    }

    public static void main(String[] args) throws Exception {
        /*
         * 插入的代码，没有改变任何控制流程，对栈帧类型快照其实是没有影响的，但是有可能超过原来的操作数栈空间大小，所有只需要重新计算一下栈帧容量
         */
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        C324CounterAdapter adapter = new C324CounterAdapter(writer);
        ClassReader reader = new ClassReader(C.class.getName());
        reader.accept(adapter,0);
        byte[] bytes = writer.toByteArray();
        IOUtils.write(bytes,
                FileUtils.newOutputStream(new File("target/" + C.class.getSimpleName() + ".class"),false));
    }
}
