package com.google.android.exoplayer2.source;

public interface ShuffleOrder {

    public static final class UnshuffledShuffleOrder implements ShuffleOrder {
        private final int length;

        public int getPreviousIndex(int i) {
            i--;
            return i >= 0 ? i : -1;
        }

        public UnshuffledShuffleOrder(int i) {
            this.length = i;
        }

        public int getLength() {
            return this.length;
        }

        public int getNextIndex(int i) {
            i++;
            return i < this.length ? i : -1;
        }

        public int getLastIndex() {
            int i = this.length;
            return i > 0 ? i - 1 : -1;
        }

        public int getFirstIndex() {
            return this.length > 0 ? 0 : -1;
        }
    }

    int getFirstIndex();

    int getLastIndex();

    int getLength();

    int getNextIndex(int i);

    int getPreviousIndex(int i);
}
