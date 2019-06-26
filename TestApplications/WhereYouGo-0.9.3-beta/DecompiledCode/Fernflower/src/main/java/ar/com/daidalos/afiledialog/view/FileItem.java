package ar.com.daidalos.afiledialog.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ar.com.daidalos.afiledialog.R;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileItem extends LinearLayout {
   private OnClickListener clickListener;
   private File file;
   private ImageView icon;
   private TextView label;
   private List listeners;
   private boolean selectable;

   public FileItem(Context var1) {
      super(var1);
      this.clickListener = new OnClickListener() {
         public void onClick(View var1) {
            if (FileItem.this.selectable) {
               for(int var2 = 0; var2 < FileItem.this.listeners.size(); ++var2) {
                  ((FileItem.OnFileClickListener)FileItem.this.listeners.get(var2)).onClick(FileItem.this);
               }
            }

         }
      };
      ((LayoutInflater)var1.getSystemService("layout_inflater")).inflate(R.layout.daidalos_file_item, this, true);
      this.file = null;
      this.selectable = true;
      this.icon = (ImageView)this.findViewById(R.id.imageViewIcon);
      this.label = (TextView)this.findViewById(R.id.textViewLabel);
      this.listeners = new LinkedList();
      this.setOnClickListener(this.clickListener);
   }

   public FileItem(Context var1, File var2) {
      this(var1);
      this.setFile(var2);
   }

   public FileItem(Context var1, File var2, String var3) {
      this(var1, var2);
      this.setLabel(var3);
   }

   private void updateIcon() {
      int var1 = R.drawable.document_gray;
      if (this.selectable) {
         if (this.file != null && this.file.isDirectory()) {
            var1 = R.drawable.folder;
         } else {
            var1 = R.drawable.document;
         }
      }

      this.icon.setImageDrawable(this.getResources().getDrawable(var1));
      if (var1 != R.drawable.document_gray) {
         this.label.setTextColor(this.getResources().getColor(R.color.daidalos_active_file));
      } else {
         this.label.setTextColor(this.getResources().getColor(R.color.daidalos_inactive_file));
      }

   }

   public void addListener(FileItem.OnFileClickListener var1) {
      this.listeners.add(var1);
   }

   public File getFile() {
      return this.file;
   }

   public boolean isSelectable() {
      return this.selectable;
   }

   public void removeAllListeners() {
      this.listeners.clear();
   }

   public void removeListener(FileItem.OnFileClickListener var1) {
      this.listeners.remove(var1);
   }

   public void setFile(File var1) {
      if (var1 != null) {
         this.file = var1;
         this.setLabel(var1.getName());
         this.updateIcon();
      }

   }

   public void setLabel(String var1) {
      String var2 = var1;
      if (var1 == null) {
         var2 = "";
      }

      this.label.setText(var2);
   }

   public void setSelectable(boolean var1) {
      this.selectable = var1;
      this.updateIcon();
   }

   public interface OnFileClickListener {
      void onClick(FileItem var1);
   }
}
