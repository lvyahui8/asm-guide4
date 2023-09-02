package org.example;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.File;

public class AddFieldAdapter extends ClassVisitor {

    private int fAcc;
    private String fName;
    private String fDesc;
    private boolean isFieldPresent;

    private Object defaultVal;

    public AddFieldAdapter(int api) {
        super(api);
    }

    public AddFieldAdapter(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (name.equals(fName)) {
            isFieldPresent = true;
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!isFieldPresent) {
            FieldVisitor fv =  cv.visitField(fAcc,fName,fDesc,null,defaultVal);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }

    public static void main(String[] args) throws Exception {
        ClassWriter writer = new ClassWriter(0);
        AddFieldAdapter adapter = new AddFieldAdapter(Opcodes.ASM7, writer);
        adapter.fAcc = Opcodes.ACC_FINAL + Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC;
        adapter.fName = "testFlag";
        adapter.fDesc = "Z";
        adapter.defaultVal = false;
        ClassReader reader = new ClassReader("java.lang.String");
        reader.accept(adapter,0);
        byte[] bytes = writer.toByteArray();
        IOUtils.write(bytes,
                FileUtils.newOutputStream(new File("target/StringAddedFiled.class"),false));
    }
}

