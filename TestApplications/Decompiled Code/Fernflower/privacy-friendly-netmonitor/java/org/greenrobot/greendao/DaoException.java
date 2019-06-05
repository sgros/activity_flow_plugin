package org.greenrobot.greendao;

import android.database.SQLException;

public class DaoException extends SQLException {
   private static final long serialVersionUID = -5877937327907457779L;

   public DaoException() {
   }

   public DaoException(String var1) {
      super(var1);
   }

   public DaoException(String var1, Throwable var2) {
      super(var1);
      this.safeInitCause(var2);
   }

   public DaoException(Throwable var1) {
      this.safeInitCause(var1);
   }

   protected void safeInitCause(Throwable var1) {
      try {
         this.initCause(var1);
      } catch (Throwable var3) {
         DaoLog.e("Could not set initial cause", var3);
         DaoLog.e("Initial cause is:", var1);
      }

   }
}
