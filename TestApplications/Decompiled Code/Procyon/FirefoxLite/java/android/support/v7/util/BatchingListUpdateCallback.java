// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.util;

public class BatchingListUpdateCallback implements ListUpdateCallback
{
    int mLastEventCount;
    Object mLastEventPayload;
    int mLastEventPosition;
    int mLastEventType;
    final ListUpdateCallback mWrapped;
    
    public BatchingListUpdateCallback(final ListUpdateCallback mWrapped) {
        this.mLastEventType = 0;
        this.mLastEventPosition = -1;
        this.mLastEventCount = -1;
        this.mLastEventPayload = null;
        this.mWrapped = mWrapped;
    }
    
    public void dispatchLastEvent() {
        if (this.mLastEventType == 0) {
            return;
        }
        switch (this.mLastEventType) {
            case 3: {
                this.mWrapped.onChanged(this.mLastEventPosition, this.mLastEventCount, this.mLastEventPayload);
                break;
            }
            case 2: {
                this.mWrapped.onRemoved(this.mLastEventPosition, this.mLastEventCount);
                break;
            }
            case 1: {
                this.mWrapped.onInserted(this.mLastEventPosition, this.mLastEventCount);
                break;
            }
        }
        this.mLastEventPayload = null;
        this.mLastEventType = 0;
    }
    
    @Override
    public void onChanged(final int n, int mLastEventCount, final Object mLastEventPayload) {
        if (this.mLastEventType == 3 && n <= this.mLastEventPosition + this.mLastEventCount) {
            final int b = n + mLastEventCount;
            if (b >= this.mLastEventPosition && this.mLastEventPayload == mLastEventPayload) {
                final int mLastEventPosition = this.mLastEventPosition;
                mLastEventCount = this.mLastEventCount;
                this.mLastEventPosition = Math.min(n, this.mLastEventPosition);
                this.mLastEventCount = Math.max(mLastEventPosition + mLastEventCount, b) - this.mLastEventPosition;
                return;
            }
        }
        this.dispatchLastEvent();
        this.mLastEventPosition = n;
        this.mLastEventCount = mLastEventCount;
        this.mLastEventPayload = mLastEventPayload;
        this.mLastEventType = 3;
    }
    
    @Override
    public void onInserted(final int n, final int mLastEventCount) {
        if (this.mLastEventType == 1 && n >= this.mLastEventPosition && n <= this.mLastEventPosition + this.mLastEventCount) {
            this.mLastEventCount += mLastEventCount;
            this.mLastEventPosition = Math.min(n, this.mLastEventPosition);
            return;
        }
        this.dispatchLastEvent();
        this.mLastEventPosition = n;
        this.mLastEventCount = mLastEventCount;
        this.mLastEventType = 1;
    }
    
    @Override
    public void onMoved(final int n, final int n2) {
        this.dispatchLastEvent();
        this.mWrapped.onMoved(n, n2);
    }
    
    @Override
    public void onRemoved(final int n, final int mLastEventCount) {
        if (this.mLastEventType == 2 && this.mLastEventPosition >= n && this.mLastEventPosition <= n + mLastEventCount) {
            this.mLastEventCount += mLastEventCount;
            this.mLastEventPosition = n;
            return;
        }
        this.dispatchLastEvent();
        this.mLastEventPosition = n;
        this.mLastEventCount = mLastEventCount;
        this.mLastEventType = 2;
    }
}
