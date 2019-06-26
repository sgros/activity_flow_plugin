package org.greenrobot.greendao;

import android.database.Cursor;
import java.util.List;
import org.greenrobot.greendao.internal.TableStatements;

public final class InternalQueryDaoAccess {
   private final AbstractDao dao;

   public InternalQueryDaoAccess(AbstractDao var1) {
      this.dao = var1;
   }

   public static TableStatements getStatements(AbstractDao var0) {
      return var0.getStatements();
   }

   public TableStatements getStatements() {
      return this.dao.getStatements();
   }

   public List loadAllAndCloseCursor(Cursor var1) {
      return this.dao.loadAllAndCloseCursor(var1);
   }

   public Object loadCurrent(Cursor var1, int var2, boolean var3) {
      return this.dao.loadCurrent(var1, var2, var3);
   }

   public Object loadUniqueAndCloseCursor(Cursor var1) {
      return this.dao.loadUniqueAndCloseCursor(var1);
   }
}
