package com.bumptech.glide.util.pool;

public abstract class StateVerifier {

    private static class DefaultStateVerifier extends StateVerifier {
        private volatile boolean isReleased;

        DefaultStateVerifier() {
            super();
        }

        public void throwIfRecycled() {
            if (this.isReleased) {
                throw new IllegalStateException("Already released");
            }
        }

        public void setRecycled(boolean z) {
            this.isReleased = z;
        }
    }

    public abstract void setRecycled(boolean z);

    public abstract void throwIfRecycled();

    public static StateVerifier newInstance() {
        return new DefaultStateVerifier();
    }

    private StateVerifier() {
    }
}
