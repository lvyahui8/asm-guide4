package org.example.chapter3;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class C325StateChangeTest {
    static abstract class PatternMethodAdapter extends MethodVisitor {
        public static final int SEEN_NOTHING = 0;
        protected int state;

        protected PatternMethodAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {
            visitInsn();
            super.visitInsn(opcode);
        }

        @Override
        public void visitIntInsn(int opcode, int operand) {
            visitInsn();
            super.visitIntInsn(opcode, operand);
        }

        protected abstract void visitInsn();
    }

    static class RemoveAddZeroAdapter extends PatternMethodAdapter {
        public static final int SEEN_ICONST_0 = 1;
        public RemoveAddZeroAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitInsn(int opcode) {
            if (state == SEEN_ICONST_0) {
                state = SEEN_NOTHING;
                return;
            }
            visitInsn();
            if (opcode == Opcodes.ICONST_0) {
                state = SEEN_ICONST_0;
                return;
            }
            super.visitInsn(opcode);
        }

        @Override
        protected void visitInsn() {
            if (state == SEEN_ICONST_0) {
                mv.visitInsn(Opcodes.ICONST_0);
            }
            state = SEEN_NOTHING;
        }
    }
}
