package org.example.chapter3;

import org.objectweb.asm.*;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

public class C323RemoveNopAdapter extends MethodVisitor implements Opcodes {
    public C323RemoveNopAdapter(int api, MethodVisitor mv) {
        super(api, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == NOP) {
            // 如果是nop指令，则不做转发，相当于删除字节码中的所有nop指令
            return;
        }
        super.visitInsn(opcode);
    }

    public static void main(String[] args) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(writer,new PrintWriter(System.out));
        ClassReader reader = new ClassReader("java.lang.String");
        reader.accept(new ClassVisitor(Opcodes.ASM9,traceClassVisitor) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                // 获取next下游的methodVisitor，在它之前插入RemoveNopAdapter
                MethodVisitor v = super.visitMethod(access, name, descriptor, signature, exceptions);
                if (v != null
                 && !name.equals("<init>")/*指定不删除构造方法中的NOP指令*/) {
                    v = new C323RemoveNopAdapter(Opcodes.ASM9,v);
                }
                return v;
            }
        },0);
    }
}
