package androidx.work.impl;

import android.arch.lifecycle.MutableLiveData;
import androidx.work.Operation;
import androidx.work.impl.utils.futures.SettableFuture;

public class OperationImpl implements Operation {
   private final SettableFuture mOperationFuture = SettableFuture.create();
   private final MutableLiveData mOperationState = new MutableLiveData();

   public OperationImpl() {
      this.setState(Operation.IN_PROGRESS);
   }

   public void setState(Operation.State var1) {
      this.mOperationState.postValue(var1);
      if (var1 instanceof Operation.State.SUCCESS) {
         this.mOperationFuture.set((Operation.State.SUCCESS)var1);
      } else if (var1 instanceof Operation.State.FAILURE) {
         Operation.State.FAILURE var2 = (Operation.State.FAILURE)var1;
         this.mOperationFuture.setException(var2.getThrowable());
      }

   }
}
