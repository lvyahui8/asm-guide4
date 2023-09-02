package org.example;

import org.objectweb.asm.util.ASMifier;

public class ASMifierTest {
    public static void main(String[] args) throws Exception {
        // 输出应该用什么样的代码来生成java.lang.Runnable
        ASMifier.main(new String[]{"java.lang.Runnable"});
    }
}
