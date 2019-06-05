// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl;

import android.arch.lifecycle.MutableLiveData;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.Operation;

public class OperationImpl implements Operation
{
    private final SettableFuture<SUCCESS> mOperationFuture;
    private final MutableLiveData<State> mOperationState;
    
    public OperationImpl() {
        this.mOperationState = new MutableLiveData<State>();
        this.mOperationFuture = SettableFuture.create();
        this.setState((State)Operation.IN_PROGRESS);
    }
    
    public void setState(final State state) {
        this.mOperationState.postValue(state);
        if (state instanceof SUCCESS) {
            this.mOperationFuture.set((SUCCESS)state);
        }
        else if (state instanceof FAILURE) {
            this.mOperationFuture.setException(((FAILURE)state).getThrowable());
        }
    }
}
