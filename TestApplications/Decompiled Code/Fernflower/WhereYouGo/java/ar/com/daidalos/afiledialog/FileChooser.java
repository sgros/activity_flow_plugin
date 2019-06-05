package ar.com.daidalos.afiledialog;

import android.content.Context;
import android.widget.LinearLayout;

interface FileChooser {
   Context getContext();

   LinearLayout getRootLayout();

   void setCurrentFolderName(String var1);
}
