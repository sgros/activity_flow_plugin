package org.greenrobot.greendao.async;

import org.greenrobot.greendao.DaoException;

public class AsyncDaoException extends DaoException {
   private static final long serialVersionUID = 5872157552005102382L;
   private final AsyncOperation failedOperation;

   public AsyncDaoException(AsyncOperation var1, Throwable var2) {
      super(var2);
      this.failedOperation = var1;
   }

   public AsyncOperation getFailedOperation() {
      return this.failedOperation;
   }
}
