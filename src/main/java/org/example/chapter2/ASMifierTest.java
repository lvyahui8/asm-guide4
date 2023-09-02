package org.example.chapter2;

import org.objectweb.asm.util.ASMifier;

import java.lang.reflect.Field;

public class ASMifierTest {
    public static void main(String[] args) throws Exception {
        // 输出应该用什么样的代码来生成java.lang.Runnable
        ASMifier.main(new String[]{"java.lang.Runnable"});
        Field field = ASMifier.class.getDeclaredField("USAGE");
        field.setAccessible(true);
        System.out.println(field.get(null));
    }
}
