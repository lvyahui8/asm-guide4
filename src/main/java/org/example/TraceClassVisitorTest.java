package org.example;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;

public class TraceClassVisitorTest {
    public static void main(String[] args) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(writer,new PrintWriter(System.out));
        RemoveMethodAdapter adapter = new RemoveMethodAdapter(traceClassVisitor,"toString","()Ljava/lang/String;");
        ClassReader reader = new ClassReader("java.lang.String");
        reader.accept(adapter,0);
    }
}
