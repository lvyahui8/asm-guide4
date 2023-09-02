package org.example;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

public class CheckClassAdapterTest  {


    public static void main(String[] args) {
        ClassWriter writer = new ClassWriter(0);
        TraceClassVisitor traceVisitor = new TraceClassVisitor(writer, new PrintWriter(System.out));
        CheckClassAdapter checkClassAdapter = new CheckClassAdapter(traceVisitor);
        checkClassAdapter.visit(Opcodes.V1_8,Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,"MQConsumer",null,
                Type.getInternalName(Object.class),
                new String[]{
                Type.getInternalName(Runnable.class),
        });
        checkClassAdapter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT,"consume","(Ljava/lang/Object;)V",null,null).visitEnd();
        checkClassAdapter.visitEnd();
    }
}
