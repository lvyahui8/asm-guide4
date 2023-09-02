package org.example;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

public class TraceClassVisitorTest {
    public static void main(String[] args) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(writer,new PrintWriter(System.out));
        AddFieldAdapter adapter = new AddFieldAdapter(Opcodes.ASM7,traceClassVisitor);
        adapter.fName = "hello";
        adapter.fAcc = Opcodes.ACC_FINAL + Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC;
        adapter.fDesc = "Ljava/lang/String;";
        adapter.defaultVal = "dj";
        ClassReader reader = new ClassReader("java.lang.Runnable");
        reader.accept(adapter,0);
    }
}
