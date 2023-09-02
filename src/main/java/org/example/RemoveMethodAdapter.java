package org.example;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.*;

import java.io.File;

public class RemoveMethodAdapter extends ClassVisitor {
    private String mName;
    private String mDesc;

    protected RemoveMethodAdapter(ClassVisitor classVisitor,String mName, String mDesc) {
        super(Opcodes.ASM7, classVisitor);
        this.mName = mName;
        this.mDesc = mDesc;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(mName) && descriptor.equals(mDesc)) {
            //不转发给下一个visitor处理，最终将移除此方法。
            return null;
        }
        return cv.visitMethod(access,name,descriptor,signature,exceptions);
    }

    public static void main(String[] args) throws Exception{
        ClassWriter writer = new ClassWriter(0);
        // 移除String类的toString方法
        RemoveMethodAdapter adapter = new RemoveMethodAdapter(writer, "toString","()Ljava/lang/String;");
        ClassReader reader = new ClassReader("java.lang.String");
        reader.accept(adapter,0);
        byte[] bytes = writer.toByteArray();
        IOUtils.write(bytes,
                FileUtils.newOutputStream(new File("target/StringRemovedStringMethod.class"),false));
    }
}
