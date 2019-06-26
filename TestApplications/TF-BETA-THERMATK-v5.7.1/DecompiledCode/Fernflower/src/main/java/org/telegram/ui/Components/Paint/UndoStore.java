package org.telegram.ui.Components.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.telegram.messenger.AndroidUtilities;

public class UndoStore {
   private UndoStore.UndoStoreDelegate delegate;
   private List operations = new ArrayList();
   private Map uuidToOperationMap = new HashMap();

   private void notifyOfHistoryChanges() {
      AndroidUtilities.runOnUIThread(new Runnable() {
         public void run() {
            if (UndoStore.this.delegate != null) {
               UndoStore.this.delegate.historyChanged();
            }

         }
      });
   }

   public boolean canUndo() {
      return this.operations.isEmpty() ^ true;
   }

   public void registerUndo(UUID var1, Runnable var2) {
      this.uuidToOperationMap.put(var1, var2);
      this.operations.add(var1);
      this.notifyOfHistoryChanges();
   }

   public void reset() {
      this.operations.clear();
      this.uuidToOperationMap.clear();
      this.notifyOfHistoryChanges();
   }

   public void setDelegate(UndoStore.UndoStoreDelegate var1) {
      this.delegate = var1;
   }

   public void undo() {
      if (this.operations.size() != 0) {
         int var1 = this.operations.size() - 1;
         UUID var2 = (UUID)this.operations.get(var1);
         Runnable var3 = (Runnable)this.uuidToOperationMap.get(var2);
         this.uuidToOperationMap.remove(var2);
         this.operations.remove(var1);
         var3.run();
         this.notifyOfHistoryChanges();
      }
   }

   public void unregisterUndo(UUID var1) {
      this.uuidToOperationMap.remove(var1);
      this.operations.remove(var1);
      this.notifyOfHistoryChanges();
   }

   public interface UndoStoreDelegate {
      void historyChanged();
   }
}
