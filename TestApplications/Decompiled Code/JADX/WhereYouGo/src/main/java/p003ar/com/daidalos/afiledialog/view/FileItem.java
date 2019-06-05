package p003ar.com.daidalos.afiledialog.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import p003ar.com.daidalos.afiledialog.C0175R;

/* renamed from: ar.com.daidalos.afiledialog.view.FileItem */
public class FileItem extends LinearLayout {
    private OnClickListener clickListener;
    private File file;
    private ImageView icon;
    private TextView label;
    private List<OnFileClickListener> listeners;
    private boolean selectable;

    /* renamed from: ar.com.daidalos.afiledialog.view.FileItem$1 */
    class C01761 implements OnClickListener {
        C01761() {
        }

        public void onClick(View v) {
            if (FileItem.this.selectable) {
                for (int i = 0; i < FileItem.this.listeners.size(); i++) {
                    ((OnFileClickListener) FileItem.this.listeners.get(i)).onClick(FileItem.this);
                }
            }
        }
    }

    /* renamed from: ar.com.daidalos.afiledialog.view.FileItem$OnFileClickListener */
    public interface OnFileClickListener {
        void onClick(FileItem fileItem);
    }

    public FileItem(Context context) {
        super(context);
        this.clickListener = new C01761();
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0175R.layout.daidalos_file_item, this, true);
        this.file = null;
        this.selectable = true;
        this.icon = (ImageView) findViewById(C0175R.C0174id.imageViewIcon);
        this.label = (TextView) findViewById(C0175R.C0174id.textViewLabel);
        this.listeners = new LinkedList();
        setOnClickListener(this.clickListener);
    }

    public FileItem(Context context, File file) {
        this(context);
        setFile(file);
    }

    public FileItem(Context context, File file, String label) {
        this(context, file);
        setLabel(label);
    }

    public void setFile(File file) {
        if (file != null) {
            this.file = file;
            setLabel(file.getName());
            updateIcon();
        }
    }

    public File getFile() {
        return this.file;
    }

    public void setLabel(String label) {
        CharSequence label2;
        if (label2 == null) {
            label2 = "";
        }
        this.label.setText(label2);
    }

    public boolean isSelectable() {
        return this.selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
        updateIcon();
    }

    private void updateIcon() {
        int icon = C0175R.C0173drawable.document_gray;
        if (this.selectable) {
            icon = (this.file == null || !this.file.isDirectory()) ? C0175R.C0173drawable.document : C0175R.C0173drawable.folder;
        }
        this.icon.setImageDrawable(getResources().getDrawable(icon));
        if (icon != C0175R.C0173drawable.document_gray) {
            this.label.setTextColor(getResources().getColor(C0175R.color.daidalos_active_file));
        } else {
            this.label.setTextColor(getResources().getColor(C0175R.color.daidalos_inactive_file));
        }
    }

    public void addListener(OnFileClickListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(OnFileClickListener listener) {
        this.listeners.remove(listener);
    }

    public void removeAllListeners() {
        this.listeners.clear();
    }
}
