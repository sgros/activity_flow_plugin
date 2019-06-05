// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.widget;

import java.util.List;
import android.support.v4.util.Pools;
import java.util.ArrayList;

class AdapterHelper implements OpReorderer.Callback
{
    final Callback mCallback;
    final boolean mDisableRecycler;
    private int mExistingUpdateTypes;
    Runnable mOnItemProcessedCallback;
    final OpReorderer mOpReorderer;
    final ArrayList<UpdateOp> mPendingUpdates;
    final ArrayList<UpdateOp> mPostponedList;
    private Pools.Pool<UpdateOp> mUpdateOpPool;
    
    AdapterHelper(final Callback callback) {
        this(callback, false);
    }
    
    AdapterHelper(final Callback mCallback, final boolean mDisableRecycler) {
        this.mUpdateOpPool = new Pools.SimplePool<UpdateOp>(30);
        this.mPendingUpdates = new ArrayList<UpdateOp>();
        this.mPostponedList = new ArrayList<UpdateOp>();
        this.mExistingUpdateTypes = 0;
        this.mCallback = mCallback;
        this.mDisableRecycler = mDisableRecycler;
        this.mOpReorderer = new OpReorderer((OpReorderer.Callback)this);
    }
    
    private void applyAdd(final UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }
    
    private void applyMove(final UpdateOp updateOp) {
        this.postponeAndUpdateViewHolders(updateOp);
    }
    
    private void applyRemove(final UpdateOp updateOp) {
        final int positionStart = updateOp.positionStart;
        int n = updateOp.positionStart + updateOp.itemCount;
        int i = updateOp.positionStart;
        int n2 = 0;
        int n3 = -1;
        while (i < n) {
            boolean b2;
            if (this.mCallback.findViewHolder(i) == null && !this.canFindInPreLayout(i)) {
                boolean b;
                if (n3 == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(2, positionStart, n2, null));
                    b = true;
                }
                else {
                    b = false;
                }
                final int n4 = 0;
                b2 = b;
                n3 = n4;
            }
            else {
                if (n3 == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(2, positionStart, n2, null));
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                n3 = 1;
            }
            int n5;
            if (b2) {
                i -= n2;
                n -= n2;
                n5 = 1;
            }
            else {
                n5 = n2 + 1;
            }
            ++i;
            n2 = n5;
        }
        UpdateOp obtainUpdateOp = updateOp;
        if (n2 != updateOp.itemCount) {
            this.recycleUpdateOp(updateOp);
            obtainUpdateOp = this.obtainUpdateOp(2, positionStart, n2, null);
        }
        if (n3 == 0) {
            this.dispatchAndUpdateViewHolders(obtainUpdateOp);
        }
        else {
            this.postponeAndUpdateViewHolders(obtainUpdateOp);
        }
    }
    
    private void applyUpdate(final UpdateOp updateOp) {
        int positionStart = updateOp.positionStart;
        final int positionStart2 = updateOp.positionStart;
        final int itemCount = updateOp.itemCount;
        int i = updateOp.positionStart;
        int n = -1;
        int n2 = 0;
        while (i < positionStart2 + itemCount) {
            int n6;
            if (this.mCallback.findViewHolder(i) == null && !this.canFindInPreLayout(i)) {
                int n3 = n2;
                int n4 = positionStart;
                if (n == 1) {
                    this.postponeAndUpdateViewHolders(this.obtainUpdateOp(4, positionStart, n2, updateOp.payload));
                    n4 = i;
                    n3 = 0;
                }
                final int n5 = 0;
                n2 = n3;
                n6 = n5;
                positionStart = n4;
            }
            else {
                int n7 = n2;
                int n8 = positionStart;
                if (n == 0) {
                    this.dispatchAndUpdateViewHolders(this.obtainUpdateOp(4, positionStart, n2, updateOp.payload));
                    n8 = i;
                    n7 = 0;
                }
                final int n9 = 1;
                positionStart = n8;
                n6 = n9;
                n2 = n7;
            }
            ++n2;
            ++i;
            n = n6;
        }
        UpdateOp obtainUpdateOp = updateOp;
        if (n2 != updateOp.itemCount) {
            final Object payload = updateOp.payload;
            this.recycleUpdateOp(updateOp);
            obtainUpdateOp = this.obtainUpdateOp(4, positionStart, n2, payload);
        }
        if (n == 0) {
            this.dispatchAndUpdateViewHolders(obtainUpdateOp);
        }
        else {
            this.postponeAndUpdateViewHolders(obtainUpdateOp);
        }
    }
    
    private boolean canFindInPreLayout(final int n) {
        for (int size = this.mPostponedList.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 8) {
                if (this.findPositionOffset(updateOp.itemCount, i + 1) == n) {
                    return true;
                }
            }
            else if (updateOp.cmd == 1) {
                for (int positionStart = updateOp.positionStart, itemCount = updateOp.itemCount, j = updateOp.positionStart; j < positionStart + itemCount; ++j) {
                    if (this.findPositionOffset(j, i + 1) == n) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void dispatchAndUpdateViewHolders(UpdateOp obtainUpdateOp) {
        if (obtainUpdateOp.cmd != 1 && obtainUpdateOp.cmd != 8) {
            int updatePositionWithPostponed = this.updatePositionWithPostponed(obtainUpdateOp.positionStart, obtainUpdateOp.cmd);
            int positionStart = obtainUpdateOp.positionStart;
            final int cmd = obtainUpdateOp.cmd;
            int n;
            if (cmd != 2) {
                if (cmd != 4) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("op should be remove or update.");
                    sb.append(obtainUpdateOp);
                    throw new IllegalArgumentException(sb.toString());
                }
                n = 1;
            }
            else {
                n = 0;
            }
            int i = 1;
            int n2 = 1;
            while (i < obtainUpdateOp.itemCount) {
                final int updatePositionWithPostponed2 = this.updatePositionWithPostponed(obtainUpdateOp.positionStart + n * i, obtainUpdateOp.cmd);
                final int cmd2 = obtainUpdateOp.cmd;
                int n3;
                if ((cmd2 == 2) ? (updatePositionWithPostponed2 == updatePositionWithPostponed) : (cmd2 == 4 && updatePositionWithPostponed2 == updatePositionWithPostponed + 1)) {
                    n3 = n2 + 1;
                }
                else {
                    final UpdateOp obtainUpdateOp2 = this.obtainUpdateOp(obtainUpdateOp.cmd, updatePositionWithPostponed, n2, obtainUpdateOp.payload);
                    this.dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp2, positionStart);
                    this.recycleUpdateOp(obtainUpdateOp2);
                    int n4 = positionStart;
                    if (obtainUpdateOp.cmd == 4) {
                        n4 = positionStart + n2;
                    }
                    updatePositionWithPostponed = updatePositionWithPostponed2;
                    final int n5 = 1;
                    positionStart = n4;
                    n3 = n5;
                }
                ++i;
                n2 = n3;
            }
            final Object payload = obtainUpdateOp.payload;
            this.recycleUpdateOp(obtainUpdateOp);
            if (n2 > 0) {
                obtainUpdateOp = this.obtainUpdateOp(obtainUpdateOp.cmd, updatePositionWithPostponed, n2, payload);
                this.dispatchFirstPassAndUpdateViewHolders(obtainUpdateOp, positionStart);
                this.recycleUpdateOp(obtainUpdateOp);
            }
            return;
        }
        throw new IllegalArgumentException("should not dispatch add or move for pre layout");
    }
    
    private void postponeAndUpdateViewHolders(final UpdateOp updateOp) {
        this.mPostponedList.add(updateOp);
        final int cmd = updateOp.cmd;
        if (cmd != 4) {
            if (cmd != 8) {
                switch (cmd) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown update op type for ");
                        sb.append(updateOp);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    case 2: {
                        this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(updateOp.positionStart, updateOp.itemCount);
                        break;
                    }
                    case 1: {
                        this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
                        break;
                    }
                }
            }
            else {
                this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
            }
        }
        else {
            this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
        }
    }
    
    private int updatePositionWithPostponed(int i, final int n) {
        int j = this.mPostponedList.size() - 1;
        int n2 = i;
        while (j >= 0) {
            final UpdateOp updateOp = this.mPostponedList.get(j);
            if (updateOp.cmd == 8) {
                int n3;
                if (updateOp.positionStart < updateOp.itemCount) {
                    i = updateOp.positionStart;
                    n3 = updateOp.itemCount;
                }
                else {
                    i = updateOp.itemCount;
                    n3 = updateOp.positionStart;
                }
                if (n2 >= i && n2 <= n3) {
                    if (i == updateOp.positionStart) {
                        if (n == 1) {
                            ++updateOp.itemCount;
                        }
                        else if (n == 2) {
                            --updateOp.itemCount;
                        }
                        i = n2 + 1;
                    }
                    else {
                        if (n == 1) {
                            ++updateOp.positionStart;
                        }
                        else if (n == 2) {
                            --updateOp.positionStart;
                        }
                        i = n2 - 1;
                    }
                }
                else if ((i = n2) < updateOp.positionStart) {
                    if (n == 1) {
                        ++updateOp.positionStart;
                        ++updateOp.itemCount;
                        i = n2;
                    }
                    else {
                        i = n2;
                        if (n == 2) {
                            --updateOp.positionStart;
                            --updateOp.itemCount;
                            i = n2;
                        }
                    }
                }
            }
            else if (updateOp.positionStart <= n2) {
                if (updateOp.cmd == 1) {
                    i = n2 - updateOp.itemCount;
                }
                else {
                    i = n2;
                    if (updateOp.cmd == 2) {
                        i = n2 + updateOp.itemCount;
                    }
                }
            }
            else if (n == 1) {
                ++updateOp.positionStart;
                i = n2;
            }
            else {
                i = n2;
                if (n == 2) {
                    --updateOp.positionStart;
                    i = n2;
                }
            }
            --j;
            n2 = i;
        }
        UpdateOp updateOp2;
        for (i = this.mPostponedList.size() - 1; i >= 0; --i) {
            updateOp2 = this.mPostponedList.get(i);
            if (updateOp2.cmd == 8) {
                if (updateOp2.itemCount == updateOp2.positionStart || updateOp2.itemCount < 0) {
                    this.mPostponedList.remove(i);
                    this.recycleUpdateOp(updateOp2);
                }
            }
            else if (updateOp2.itemCount <= 0) {
                this.mPostponedList.remove(i);
                this.recycleUpdateOp(updateOp2);
            }
        }
        return n2;
    }
    
    public int applyPendingUpdatesToPosition(int n) {
        final int size = this.mPendingUpdates.size();
        int i = 0;
        int n2 = n;
        while (i < size) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            n = updateOp.cmd;
            if (n != 8) {
                switch (n) {
                    default: {
                        n = n2;
                        break;
                    }
                    case 2: {
                        n = n2;
                        if (updateOp.positionStart > n2) {
                            break;
                        }
                        if (updateOp.positionStart + updateOp.itemCount > n2) {
                            return -1;
                        }
                        n = n2 - updateOp.itemCount;
                        break;
                    }
                    case 1: {
                        n = n2;
                        if (updateOp.positionStart <= n2) {
                            n = n2 + updateOp.itemCount;
                            break;
                        }
                        break;
                    }
                }
            }
            else if (updateOp.positionStart == n2) {
                n = updateOp.itemCount;
            }
            else {
                int n3;
                if (updateOp.positionStart < (n3 = n2)) {
                    n3 = n2 - 1;
                }
                if (updateOp.itemCount <= (n = n3)) {
                    n = n3 + 1;
                }
            }
            ++i;
            n2 = n;
        }
        return n2;
    }
    
    void consumePostponedUpdates() {
        for (int size = this.mPostponedList.size(), i = 0; i < size; ++i) {
            this.mCallback.onDispatchSecondPass((UpdateOp)this.mPostponedList.get(i));
        }
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }
    
    void consumeUpdatesInOnePass() {
        this.consumePostponedUpdates();
        for (int size = this.mPendingUpdates.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            final int cmd = updateOp.cmd;
            if (cmd != 4) {
                if (cmd != 8) {
                    switch (cmd) {
                        case 2: {
                            this.mCallback.onDispatchSecondPass(updateOp);
                            this.mCallback.offsetPositionsForRemovingInvisible(updateOp.positionStart, updateOp.itemCount);
                            break;
                        }
                        case 1: {
                            this.mCallback.onDispatchSecondPass(updateOp);
                            this.mCallback.offsetPositionsForAdd(updateOp.positionStart, updateOp.itemCount);
                            break;
                        }
                    }
                }
                else {
                    this.mCallback.onDispatchSecondPass(updateOp);
                    this.mCallback.offsetPositionsForMove(updateOp.positionStart, updateOp.itemCount);
                }
            }
            else {
                this.mCallback.onDispatchSecondPass(updateOp);
                this.mCallback.markViewHoldersUpdated(updateOp.positionStart, updateOp.itemCount, updateOp.payload);
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.mExistingUpdateTypes = 0;
    }
    
    void dispatchFirstPassAndUpdateViewHolders(final UpdateOp updateOp, final int n) {
        this.mCallback.onDispatchFirstPass(updateOp);
        final int cmd = updateOp.cmd;
        if (cmd != 2) {
            if (cmd != 4) {
                throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
            }
            this.mCallback.markViewHoldersUpdated(n, updateOp.itemCount, updateOp.payload);
        }
        else {
            this.mCallback.offsetPositionsForRemovingInvisible(n, updateOp.itemCount);
        }
    }
    
    int findPositionOffset(final int n) {
        return this.findPositionOffset(n, 0);
    }
    
    int findPositionOffset(int itemCount, int n) {
        final int size = this.mPostponedList.size();
        int i = n;
        n = itemCount;
        while (i < size) {
            final UpdateOp updateOp = this.mPostponedList.get(i);
            if (updateOp.cmd == 8) {
                if (updateOp.positionStart == n) {
                    itemCount = updateOp.itemCount;
                }
                else {
                    int n2;
                    if (updateOp.positionStart < (n2 = n)) {
                        n2 = n - 1;
                    }
                    if (updateOp.itemCount <= (itemCount = n2)) {
                        itemCount = n2 + 1;
                    }
                }
            }
            else if (updateOp.positionStart <= (itemCount = n)) {
                if (updateOp.cmd == 2) {
                    if (n < updateOp.positionStart + updateOp.itemCount) {
                        return -1;
                    }
                    itemCount = n - updateOp.itemCount;
                }
                else {
                    itemCount = n;
                    if (updateOp.cmd == 1) {
                        itemCount = n + updateOp.itemCount;
                    }
                }
            }
            ++i;
            n = itemCount;
        }
        return n;
    }
    
    boolean hasAnyUpdateTypes(final int n) {
        return (n & this.mExistingUpdateTypes) != 0x0;
    }
    
    boolean hasPendingUpdates() {
        return this.mPendingUpdates.size() > 0;
    }
    
    boolean hasUpdates() {
        return !this.mPostponedList.isEmpty() && !this.mPendingUpdates.isEmpty();
    }
    
    @Override
    public UpdateOp obtainUpdateOp(final int cmd, final int positionStart, final int itemCount, final Object payload) {
        final UpdateOp updateOp = this.mUpdateOpPool.acquire();
        UpdateOp updateOp2;
        if (updateOp == null) {
            updateOp2 = new UpdateOp(cmd, positionStart, itemCount, payload);
        }
        else {
            updateOp.cmd = cmd;
            updateOp.positionStart = positionStart;
            updateOp.itemCount = itemCount;
            updateOp.payload = payload;
            updateOp2 = updateOp;
        }
        return updateOp2;
    }
    
    boolean onItemRangeChanged(final int n, final int n2, final Object o) {
        boolean b = false;
        if (n2 < 1) {
            return false;
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(4, n, n2, o));
        this.mExistingUpdateTypes |= 0x4;
        if (this.mPendingUpdates.size() == 1) {
            b = true;
        }
        return b;
    }
    
    boolean onItemRangeInserted(final int n, final int n2) {
        boolean b = false;
        if (n2 < 1) {
            return false;
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(1, n, n2, null));
        this.mExistingUpdateTypes |= 0x1;
        if (this.mPendingUpdates.size() == 1) {
            b = true;
        }
        return b;
    }
    
    boolean onItemRangeMoved(final int n, final int n2, final int n3) {
        boolean b = false;
        if (n == n2) {
            return false;
        }
        if (n3 == 1) {
            this.mPendingUpdates.add(this.obtainUpdateOp(8, n, n2, null));
            this.mExistingUpdateTypes |= 0x8;
            if (this.mPendingUpdates.size() == 1) {
                b = true;
            }
            return b;
        }
        throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
    }
    
    boolean onItemRangeRemoved(final int n, final int n2) {
        boolean b = false;
        if (n2 < 1) {
            return false;
        }
        this.mPendingUpdates.add(this.obtainUpdateOp(2, n, n2, null));
        this.mExistingUpdateTypes |= 0x2;
        if (this.mPendingUpdates.size() == 1) {
            b = true;
        }
        return b;
    }
    
    void preProcess() {
        this.mOpReorderer.reorderOps(this.mPendingUpdates);
        for (int size = this.mPendingUpdates.size(), i = 0; i < size; ++i) {
            final UpdateOp updateOp = this.mPendingUpdates.get(i);
            final int cmd = updateOp.cmd;
            if (cmd != 4) {
                if (cmd != 8) {
                    switch (cmd) {
                        case 2: {
                            this.applyRemove(updateOp);
                            break;
                        }
                        case 1: {
                            this.applyAdd(updateOp);
                            break;
                        }
                    }
                }
                else {
                    this.applyMove(updateOp);
                }
            }
            else {
                this.applyUpdate(updateOp);
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.mPendingUpdates.clear();
    }
    
    @Override
    public void recycleUpdateOp(final UpdateOp updateOp) {
        if (!this.mDisableRecycler) {
            updateOp.payload = null;
            this.mUpdateOpPool.release(updateOp);
        }
    }
    
    void recycleUpdateOpsAndClearList(final List<UpdateOp> list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            this.recycleUpdateOp(list.get(i));
        }
        list.clear();
    }
    
    void reset() {
        this.recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }
    
    interface Callback
    {
        RecyclerView.ViewHolder findViewHolder(final int p0);
        
        void markViewHoldersUpdated(final int p0, final int p1, final Object p2);
        
        void offsetPositionsForAdd(final int p0, final int p1);
        
        void offsetPositionsForMove(final int p0, final int p1);
        
        void offsetPositionsForRemovingInvisible(final int p0, final int p1);
        
        void offsetPositionsForRemovingLaidOutOrNewView(final int p0, final int p1);
        
        void onDispatchFirstPass(final UpdateOp p0);
        
        void onDispatchSecondPass(final UpdateOp p0);
    }
    
    static class UpdateOp
    {
        int cmd;
        int itemCount;
        Object payload;
        int positionStart;
        
        UpdateOp(final int cmd, final int positionStart, final int itemCount, final Object payload) {
            this.cmd = cmd;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
            this.payload = payload;
        }
        
        String cmdToString() {
            final int cmd = this.cmd;
            if (cmd == 4) {
                return "up";
            }
            if (cmd == 8) {
                return "mv";
            }
            switch (cmd) {
                default: {
                    return "??";
                }
                case 2: {
                    return "rm";
                }
                case 1: {
                    return "add";
                }
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final UpdateOp updateOp = (UpdateOp)o;
            if (this.cmd != updateOp.cmd) {
                return false;
            }
            if (this.cmd == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == updateOp.positionStart && this.positionStart == updateOp.itemCount) {
                return true;
            }
            if (this.itemCount != updateOp.itemCount) {
                return false;
            }
            if (this.positionStart != updateOp.positionStart) {
                return false;
            }
            if (this.payload != null) {
                if (!this.payload.equals(updateOp.payload)) {
                    return false;
                }
            }
            else if (updateOp.payload != null) {
                return false;
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return (this.cmd * 31 + this.positionStart) * 31 + this.itemCount;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append("[");
            sb.append(this.cmdToString());
            sb.append(",s:");
            sb.append(this.positionStart);
            sb.append("c:");
            sb.append(this.itemCount);
            sb.append(",p:");
            sb.append(this.payload);
            sb.append("]");
            return sb.toString();
        }
    }
}
