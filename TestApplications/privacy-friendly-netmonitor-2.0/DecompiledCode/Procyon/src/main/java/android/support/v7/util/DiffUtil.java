// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.util;

import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import java.util.Iterator;
import android.support.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

public class DiffUtil
{
    private static final Comparator<Snake> SNAKE_COMPARATOR;
    
    static {
        SNAKE_COMPARATOR = new Comparator<Snake>() {
            @Override
            public int compare(final Snake snake, final Snake snake2) {
                int n;
                if ((n = snake.x - snake2.x) == 0) {
                    n = snake.y - snake2.y;
                }
                return n;
            }
        };
    }
    
    private DiffUtil() {
    }
    
    public static DiffResult calculateDiff(final Callback callback) {
        return calculateDiff(callback, true);
    }
    
    public static DiffResult calculateDiff(final Callback callback, final boolean b) {
        final int oldListSize = callback.getOldListSize();
        final int newListSize = callback.getNewListSize();
        final ArrayList<Object> list = new ArrayList<Object>();
        final ArrayList<Range> list2 = new ArrayList<Range>();
        list2.add(new Range(0, oldListSize, 0, newListSize));
        final int n = Math.abs(oldListSize - newListSize) + (oldListSize + newListSize);
        final int n2 = n * 2;
        final int[] array = new int[n2];
        final int[] array2 = new int[n2];
        final ArrayList<Range> list3 = (ArrayList<Range>)new ArrayList<Object>();
        while (!list2.isEmpty()) {
            final Range range = list2.remove(list2.size() - 1);
            final Snake diffPartial = diffPartial(callback, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, array, array2, n);
            if (diffPartial != null) {
                if (diffPartial.size > 0) {
                    list.add(diffPartial);
                }
                diffPartial.x += range.oldListStart;
                diffPartial.y += range.newListStart;
                Range range2;
                if (list3.isEmpty()) {
                    range2 = new Range();
                }
                else {
                    range2 = list3.remove(list3.size() - 1);
                }
                range2.oldListStart = range.oldListStart;
                range2.newListStart = range.newListStart;
                if (diffPartial.reverse) {
                    range2.oldListEnd = diffPartial.x;
                    range2.newListEnd = diffPartial.y;
                }
                else if (diffPartial.removal) {
                    range2.oldListEnd = diffPartial.x - 1;
                    range2.newListEnd = diffPartial.y;
                }
                else {
                    range2.oldListEnd = diffPartial.x;
                    range2.newListEnd = diffPartial.y - 1;
                }
                list2.add(range2);
                if (diffPartial.reverse) {
                    if (diffPartial.removal) {
                        range.oldListStart = diffPartial.x + diffPartial.size + 1;
                        range.newListStart = diffPartial.y + diffPartial.size;
                    }
                    else {
                        range.oldListStart = diffPartial.x + diffPartial.size;
                        range.newListStart = diffPartial.y + diffPartial.size + 1;
                    }
                }
                else {
                    range.oldListStart = diffPartial.x + diffPartial.size;
                    range.newListStart = diffPartial.y + diffPartial.size;
                }
                list2.add(range);
            }
            else {
                list3.add(range);
            }
        }
        Collections.sort(list, (Comparator<? super Object>)DiffUtil.SNAKE_COMPARATOR);
        return new DiffResult(callback, (List<Snake>)list, array, array2, b);
    }
    
    private static Snake diffPartial(final Callback callback, final int n, int n2, final int n3, int toIndex, final int[] a, final int[] a2, final int n4) {
        final int val = n2 - n;
        n2 = toIndex - n3;
        if (val >= 1 && n2 >= 1) {
            final int n5 = val - n2;
            final int n6 = (val + n2 + 1) / 2;
            final int fromIndex = n4 - n6 - 1;
            toIndex = n4 + n6 + 1;
            Arrays.fill(a, fromIndex, toIndex, 0);
            Arrays.fill(a2, fromIndex + n5, toIndex + n5, val);
            final boolean b = n5 % 2 != 0;
            int i = 0;
            toIndex = val;
            while (i <= n6) {
                int j;
                int n7;
                for (n7 = (j = -i); j <= i; j += 2) {
                    int n9 = 0;
                    boolean removal = false;
                    Label_0207: {
                        Label_0192: {
                            if (j != n7) {
                                if (j != i) {
                                    final int n8 = n4 + j;
                                    if (a[n8 - 1] < a[n8 + 1]) {
                                        break Label_0192;
                                    }
                                }
                                n9 = a[n4 + j - 1] + 1;
                                removal = true;
                                break Label_0207;
                            }
                        }
                        n9 = a[n4 + j + 1];
                        removal = false;
                    }
                    for (int n10 = n9 - j; n9 < toIndex && n10 < n2 && callback.areItemsTheSame(n + n9, n3 + n10); ++n9, ++n10) {}
                    final int n11 = n4 + j;
                    a[n11] = n9;
                    if (b && j >= n5 - i + 1 && j <= n5 + i - 1 && a[n11] >= a2[n11]) {
                        final Snake snake = new Snake();
                        snake.x = a2[n11];
                        snake.y = snake.x - j;
                        snake.size = a[n11] - a2[n11];
                        snake.removal = removal;
                        snake.reverse = false;
                        return snake;
                    }
                }
                boolean removal2 = false;
                for (int k = n7; k <= i; k += 2, removal2 = false) {
                    final int n12 = k + n5;
                    int n14 = 0;
                    Label_0469: {
                        Label_0457: {
                            if (n12 != i + n5) {
                                if (n12 != n7 + n5) {
                                    final int n13 = n4 + n12;
                                    if (a2[n13 - 1] < a2[n13 + 1]) {
                                        break Label_0457;
                                    }
                                }
                                n14 = a2[n4 + n12 + 1] - 1;
                                removal2 = true;
                                break Label_0469;
                            }
                        }
                        n14 = a2[n4 + n12 - 1];
                    }
                    for (int n15 = n14 - n12; n14 > 0 && n15 > 0 && callback.areItemsTheSame(n + n14 - 1, n3 + n15 - 1); --n14, --n15) {}
                    final int n16 = n4 + n12;
                    a2[n16] = n14;
                    if (!b && n12 >= n7 && n12 <= i && a[n16] >= a2[n16]) {
                        final Snake snake2 = new Snake();
                        snake2.x = a2[n16];
                        snake2.y = snake2.x - n12;
                        snake2.size = a[n16] - a2[n16];
                        snake2.removal = removal2;
                        snake2.reverse = true;
                        return snake2;
                    }
                }
                ++i;
            }
            throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
        }
        return null;
    }
    
    public abstract static class Callback
    {
        public abstract boolean areContentsTheSame(final int p0, final int p1);
        
        public abstract boolean areItemsTheSame(final int p0, final int p1);
        
        @Nullable
        public Object getChangePayload(final int n, final int n2) {
            return null;
        }
        
        public abstract int getNewListSize();
        
        public abstract int getOldListSize();
    }
    
    public static class DiffResult
    {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_IGNORE = 16;
        private static final int FLAG_MASK = 31;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 5;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;
        private final List<Snake> mSnakes;
        
        DiffResult(final Callback mCallback, final List<Snake> mSnakes, final int[] mOldItemStatuses, final int[] mNewItemStatuses, final boolean mDetectMoves) {
            this.mSnakes = mSnakes;
            this.mOldItemStatuses = mOldItemStatuses;
            this.mNewItemStatuses = mNewItemStatuses;
            Arrays.fill(this.mOldItemStatuses, 0);
            Arrays.fill(this.mNewItemStatuses, 0);
            this.mCallback = mCallback;
            this.mOldListSize = mCallback.getOldListSize();
            this.mNewListSize = mCallback.getNewListSize();
            this.mDetectMoves = mDetectMoves;
            this.addRootSnake();
            this.findMatchingItems();
        }
        
        private void addRootSnake() {
            Snake snake;
            if (this.mSnakes.isEmpty()) {
                snake = null;
            }
            else {
                snake = this.mSnakes.get(0);
            }
            if (snake == null || snake.x != 0 || snake.y != 0) {
                final Snake snake2 = new Snake();
                snake2.x = 0;
                snake2.y = 0;
                snake2.removal = false;
                snake2.size = 0;
                snake2.reverse = false;
                this.mSnakes.add(0, snake2);
            }
        }
        
        private void dispatchAdditions(final List<PostponedUpdate> list, final ListUpdateCallback listUpdateCallback, final int n, int i, final int n2) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onInserted(n, i);
                return;
            }
            --i;
            while (i >= 0) {
                final int[] mNewItemStatuses = this.mNewItemStatuses;
                final int j = n2 + i;
                final int n3 = mNewItemStatuses[j] & 0x1F;
                if (n3 != 0) {
                    if (n3 != 4 && n3 != 8) {
                        if (n3 != 16) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("unknown flag for pos ");
                            sb.append(j);
                            sb.append(" ");
                            sb.append(Long.toBinaryString(n3));
                            throw new IllegalStateException(sb.toString());
                        }
                        list.add(new PostponedUpdate(j, n, false));
                    }
                    else {
                        final int n4 = this.mNewItemStatuses[j] >> 5;
                        listUpdateCallback.onMoved(removePostponedUpdate(list, n4, true).currentPos, n);
                        if (n3 == 4) {
                            listUpdateCallback.onChanged(n, 1, this.mCallback.getChangePayload(n4, j));
                        }
                    }
                }
                else {
                    listUpdateCallback.onInserted(n, 1);
                    for (final PostponedUpdate postponedUpdate : list) {
                        ++postponedUpdate.currentPos;
                    }
                }
                --i;
            }
        }
        
        private void dispatchRemovals(final List<PostponedUpdate> list, final ListUpdateCallback listUpdateCallback, final int n, int i, final int n2) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onRemoved(n, i);
                return;
            }
            --i;
            while (i >= 0) {
                final int[] mOldItemStatuses = this.mOldItemStatuses;
                final int j = n2 + i;
                final int n3 = mOldItemStatuses[j] & 0x1F;
                if (n3 != 0) {
                    if (n3 != 4 && n3 != 8) {
                        if (n3 != 16) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("unknown flag for pos ");
                            sb.append(j);
                            sb.append(" ");
                            sb.append(Long.toBinaryString(n3));
                            throw new IllegalStateException(sb.toString());
                        }
                        list.add(new PostponedUpdate(j, n + i, true));
                    }
                    else {
                        final int n4 = this.mOldItemStatuses[j] >> 5;
                        final PostponedUpdate removePostponedUpdate = removePostponedUpdate(list, n4, false);
                        listUpdateCallback.onMoved(n + i, removePostponedUpdate.currentPos - 1);
                        if (n3 == 4) {
                            listUpdateCallback.onChanged(removePostponedUpdate.currentPos - 1, 1, this.mCallback.getChangePayload(j, n4));
                        }
                    }
                }
                else {
                    listUpdateCallback.onRemoved(n + i, 1);
                    for (final PostponedUpdate postponedUpdate : list) {
                        --postponedUpdate.currentPos;
                    }
                }
                --i;
            }
        }
        
        private void findAddition(final int n, final int n2, final int n3) {
            if (this.mOldItemStatuses[n - 1] != 0) {
                return;
            }
            this.findMatchingItem(n, n2, n3, false);
        }
        
        private boolean findMatchingItem(int n, int i, int j, final boolean b) {
            int y;
            int n2;
            if (b) {
                y = i - 1;
                i = n;
                n2 = y;
            }
            else {
                final int n3 = n2 = n - 1;
                y = i;
                i = n3;
            }
            while (j >= 0) {
                final Snake snake = this.mSnakes.get(j);
                final int x = snake.x;
                final int size = snake.size;
                final int y2 = snake.y;
                final int size2 = snake.size;
                int n4 = 4;
                if (b) {
                    --i;
                    while (i >= x + size) {
                        if (this.mCallback.areItemsTheSame(i, n2)) {
                            if (this.mCallback.areContentsTheSame(i, n2)) {
                                n4 = 8;
                            }
                            this.mNewItemStatuses[n2] = (i << 5 | 0x10);
                            this.mOldItemStatuses[i] = (n2 << 5 | n4);
                            return true;
                        }
                        --i;
                    }
                }
                else {
                    int[] mOldItemStatuses;
                    for (i = y - 1; i >= y2 + size2; --i) {
                        if (this.mCallback.areItemsTheSame(n2, i)) {
                            if (this.mCallback.areContentsTheSame(n2, i)) {
                                n4 = 8;
                            }
                            mOldItemStatuses = this.mOldItemStatuses;
                            --n;
                            mOldItemStatuses[n] = (i << 5 | 0x10);
                            this.mNewItemStatuses[i] = (n << 5 | n4);
                            return true;
                        }
                    }
                }
                i = snake.x;
                y = snake.y;
                --j;
            }
            return false;
        }
        
        private void findMatchingItems() {
            int n = this.mOldListSize;
            int n2 = this.mNewListSize;
            for (int i = this.mSnakes.size() - 1; i >= 0; --i) {
                final Snake snake = this.mSnakes.get(i);
                final int x = snake.x;
                final int size = snake.size;
                final int y = snake.y;
                final int size2 = snake.size;
                if (this.mDetectMoves) {
                    int j;
                    while (true) {
                        j = n2;
                        if (n <= x + size) {
                            break;
                        }
                        this.findAddition(n, n2, i);
                        --n;
                    }
                    while (j > y + size2) {
                        this.findRemoval(n, j, i);
                        --j;
                    }
                }
                for (int k = 0; k < snake.size; ++k) {
                    final int n3 = snake.x + k;
                    final int n4 = snake.y + k;
                    int n5;
                    if (this.mCallback.areContentsTheSame(n3, n4)) {
                        n5 = 1;
                    }
                    else {
                        n5 = 2;
                    }
                    this.mOldItemStatuses[n3] = (n4 << 5 | n5);
                    this.mNewItemStatuses[n4] = (n3 << 5 | n5);
                }
                n = snake.x;
                n2 = snake.y;
            }
        }
        
        private void findRemoval(final int n, final int n2, final int n3) {
            if (this.mNewItemStatuses[n2 - 1] != 0) {
                return;
            }
            this.findMatchingItem(n, n2, n3, true);
        }
        
        private static PostponedUpdate removePostponedUpdate(final List<PostponedUpdate> list, int n, final boolean b) {
            for (int i = list.size() - 1; i >= 0; --i) {
                final PostponedUpdate postponedUpdate = list.get(i);
                if (postponedUpdate.posInOwnerList == n && postponedUpdate.removal == b) {
                    list.remove(i);
                    while (i < list.size()) {
                        final PostponedUpdate postponedUpdate2 = list.get(i);
                        final int currentPos = postponedUpdate2.currentPos;
                        if (b) {
                            n = 1;
                        }
                        else {
                            n = -1;
                        }
                        postponedUpdate2.currentPos = currentPos + n;
                        ++i;
                    }
                    return postponedUpdate;
                }
            }
            return null;
        }
        
        public void dispatchUpdatesTo(final ListUpdateCallback listUpdateCallback) {
            BatchingListUpdateCallback batchingListUpdateCallback;
            if (listUpdateCallback instanceof BatchingListUpdateCallback) {
                batchingListUpdateCallback = (BatchingListUpdateCallback)listUpdateCallback;
            }
            else {
                batchingListUpdateCallback = new BatchingListUpdateCallback(listUpdateCallback);
            }
            final ArrayList<PostponedUpdate> list = new ArrayList<PostponedUpdate>();
            int n = this.mOldListSize;
            int n2 = this.mNewListSize;
            int i = this.mSnakes.size();
            --i;
            while (i >= 0) {
                final Snake snake = this.mSnakes.get(i);
                final int size = snake.size;
                final int n3 = snake.x + size;
                final int n4 = snake.y + size;
                if (n3 < n) {
                    this.dispatchRemovals(list, batchingListUpdateCallback, n3, n - n3, n3);
                }
                if (n4 < n2) {
                    this.dispatchAdditions(list, batchingListUpdateCallback, n3, n2 - n4, n4);
                }
                for (int j = size - 1; j >= 0; --j) {
                    if ((this.mOldItemStatuses[snake.x + j] & 0x1F) == 0x2) {
                        batchingListUpdateCallback.onChanged(snake.x + j, 1, this.mCallback.getChangePayload(snake.x + j, snake.y + j));
                    }
                }
                n = snake.x;
                n2 = snake.y;
                --i;
            }
            batchingListUpdateCallback.dispatchLastEvent();
        }
        
        public void dispatchUpdatesTo(final RecyclerView.Adapter adapter) {
            this.dispatchUpdatesTo(new ListUpdateCallback() {
                @Override
                public void onChanged(final int n, final int n2, final Object o) {
                    adapter.notifyItemRangeChanged(n, n2, o);
                }
                
                @Override
                public void onInserted(final int n, final int n2) {
                    adapter.notifyItemRangeInserted(n, n2);
                }
                
                @Override
                public void onMoved(final int n, final int n2) {
                    adapter.notifyItemMoved(n, n2);
                }
                
                @Override
                public void onRemoved(final int n, final int n2) {
                    adapter.notifyItemRangeRemoved(n, n2);
                }
            });
        }
        
        @VisibleForTesting
        List<Snake> getSnakes() {
            return this.mSnakes;
        }
    }
    
    private static class PostponedUpdate
    {
        int currentPos;
        int posInOwnerList;
        boolean removal;
        
        public PostponedUpdate(final int posInOwnerList, final int currentPos, final boolean removal) {
            this.posInOwnerList = posInOwnerList;
            this.currentPos = currentPos;
            this.removal = removal;
        }
    }
    
    static class Range
    {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;
        
        public Range() {
        }
        
        public Range(final int oldListStart, final int oldListEnd, final int newListStart, final int newListEnd) {
            this.oldListStart = oldListStart;
            this.oldListEnd = oldListEnd;
            this.newListStart = newListStart;
            this.newListEnd = newListEnd;
        }
    }
    
    static class Snake
    {
        boolean removal;
        boolean reverse;
        int size;
        int x;
        int y;
    }
}
