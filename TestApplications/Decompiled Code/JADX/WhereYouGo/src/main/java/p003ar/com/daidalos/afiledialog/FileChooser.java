package p003ar.com.daidalos.afiledialog;

import android.content.Context;
import android.widget.LinearLayout;

/* renamed from: ar.com.daidalos.afiledialog.FileChooser */
interface FileChooser {
    Context getContext();

    LinearLayout getRootLayout();

    void setCurrentFolderName(String str);
}
