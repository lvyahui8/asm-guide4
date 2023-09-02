package org.example.chapter2;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;

public class RemoveDebugAdapter extends ClassVisitor {
    public RemoveDebugAdapter(ClassVisitor next) {
        super(Opcodes.ASM7, next);
    }

    @Override
    public void visitSource(String source, String debug) {
        // 不做任何转发，相当于删除此类容
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        // 不做任何转发，相当于删除此类容
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        // 不做任何转发，相当于删除此类容
    }

    public static void main(String[] args) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        RemoveDebugAdapter adapter = new RemoveDebugAdapter(writer);
        ClassReader reader = new ClassReader("java.lang.String");
        reader.accept(adapter,0);
        byte[] bytes = writer.toByteArray();
        IOUtils.write(bytes,
                FileUtils.newOutputStream(new File("target/String.class"),false));
    }
}
