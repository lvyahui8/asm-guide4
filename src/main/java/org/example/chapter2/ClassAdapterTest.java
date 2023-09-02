package org.example.chapter2;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;


public class ClassAdapterTest {

    static class ChangeVersionAdapter extends ClassVisitor {
        public ChangeVersionAdapter(ClassVisitor cv) {
            super(Opcodes.ASM4, cv);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            cv.visit(Opcodes.V1_5,access,name,signature,superName,interfaces);
        }
    }

    public static void main(String[] args) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        ClassVisitor adapter = new ChangeVersionAdapter(writer);
        ClassReader reader = new ClassReader("java.lang.Runnable");
        reader.accept(adapter,0);
        byte[] bytes = writer.toByteArray();

        IOUtils.write(bytes,
                FileUtils.newOutputStream(new File("target/Runnable.class"),false));
    }
}
