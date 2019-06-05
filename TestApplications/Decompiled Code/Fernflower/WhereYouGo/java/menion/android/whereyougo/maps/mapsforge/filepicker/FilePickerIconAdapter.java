package menion.android.whereyougo.maps.mapsforge.filepicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.io.File;

class FilePickerIconAdapter extends BaseAdapter {
   private final Context context;
   private File currentFile;
   private File[] files;
   private boolean hasParentFolder;
   private TextView textView;

   FilePickerIconAdapter(Context var1) {
      this.context = var1;
   }

   public int getCount() {
      int var1;
      if (this.files == null) {
         var1 = 0;
      } else {
         var1 = this.files.length;
      }

      return var1;
   }

   public Object getItem(int var1) {
      return this.files[var1];
   }

   public long getItemId(int var1) {
      return (long)var1;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      if (var2 instanceof TextView) {
         this.textView = (TextView)var2;
      } else {
         this.textView = new TextView(this.context);
         this.textView.setLines(2);
         this.textView.setGravity(1);
         this.textView.setPadding(5, 10, 5, 10);
      }

      if (var1 == 0 && this.hasParentFolder) {
         this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 2130837516, 0, 0);
         this.textView.setText("..");
      } else {
         this.currentFile = this.files[var1];
         if (this.currentFile.isDirectory()) {
            this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 2130837518, 0, 0);
         } else {
            this.textView.setCompoundDrawablesWithIntrinsicBounds(0, 2130837517, 0, 0);
         }

         this.textView.setText(this.currentFile.getName());
      }

      return this.textView;
   }

   void setFiles(File[] var1, boolean var2) {
      this.files = (File[])var1.clone();
      this.hasParentFolder = var2;
   }
}
