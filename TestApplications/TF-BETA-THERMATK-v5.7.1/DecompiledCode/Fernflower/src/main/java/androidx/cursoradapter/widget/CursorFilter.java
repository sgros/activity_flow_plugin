package androidx.cursoradapter.widget;

import android.database.Cursor;
import android.widget.Filter;
import android.widget.Filter.FilterResults;

class CursorFilter extends Filter {
   CursorFilter.CursorFilterClient mClient;

   CursorFilter(CursorFilter.CursorFilterClient var1) {
      this.mClient = var1;
   }

   public CharSequence convertResultToString(Object var1) {
      return this.mClient.convertToString((Cursor)var1);
   }

   protected FilterResults performFiltering(CharSequence var1) {
      Cursor var3 = this.mClient.runQueryOnBackgroundThread(var1);
      FilterResults var2 = new FilterResults();
      if (var3 != null) {
         var2.count = var3.getCount();
         var2.values = var3;
      } else {
         var2.count = 0;
         var2.values = null;
      }

      return var2;
   }

   protected void publishResults(CharSequence var1, FilterResults var2) {
      Cursor var3 = this.mClient.getCursor();
      Object var4 = var2.values;
      if (var4 != null && var4 != var3) {
         this.mClient.changeCursor((Cursor)var4);
      }

   }

   interface CursorFilterClient {
      void changeCursor(Cursor var1);

      CharSequence convertToString(Cursor var1);

      Cursor getCursor();

      Cursor runQueryOnBackgroundThread(CharSequence var1);
   }
}
