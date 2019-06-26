// 
// Decompiled by Procyon v0.5.34
// 

package ar.com.daidalos.afiledialog.view;

import java.util.LinkedList;
import android.view.ViewGroup;
import ar.com.daidalos.afiledialog.R;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import java.util.List;
import android.widget.TextView;
import android.widget.ImageView;
import java.io.File;
import android.view.View$OnClickListener;
import android.widget.LinearLayout;

public class FileItem extends LinearLayout
{
    private View$OnClickListener clickListener;
    private File file;
    private ImageView icon;
    private TextView label;
    private List<OnFileClickListener> listeners;
    private boolean selectable;
    
    public FileItem(final Context context) {
        super(context);
        this.clickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (FileItem.this.selectable) {
                    for (int i = 0; i < FileItem.this.listeners.size(); ++i) {
                        ((OnFileClickListener)FileItem.this.listeners.get(i)).onClick(FileItem.this);
                    }
                }
            }
        };
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(R.layout.daidalos_file_item, (ViewGroup)this, true);
        this.file = null;
        this.selectable = true;
        this.icon = (ImageView)this.findViewById(R.id.imageViewIcon);
        this.label = (TextView)this.findViewById(R.id.textViewLabel);
        this.listeners = new LinkedList<OnFileClickListener>();
        this.setOnClickListener(this.clickListener);
    }
    
    public FileItem(final Context context, final File file) {
        this(context);
        this.setFile(file);
    }
    
    public FileItem(final Context context, final File file, final String label) {
        this(context, file);
        this.setLabel(label);
    }
    
    private void updateIcon() {
        int n = R.drawable.document_gray;
        if (this.selectable) {
            if (this.file != null && this.file.isDirectory()) {
                n = R.drawable.folder;
            }
            else {
                n = R.drawable.document;
            }
        }
        this.icon.setImageDrawable(this.getResources().getDrawable(n));
        if (n != R.drawable.document_gray) {
            this.label.setTextColor(this.getResources().getColor(R.color.daidalos_active_file));
        }
        else {
            this.label.setTextColor(this.getResources().getColor(R.color.daidalos_inactive_file));
        }
    }
    
    public void addListener(final OnFileClickListener onFileClickListener) {
        this.listeners.add(onFileClickListener);
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
    
    public void removeListener(final OnFileClickListener onFileClickListener) {
        this.listeners.remove(onFileClickListener);
    }
    
    public void setFile(final File file) {
        if (file != null) {
            this.file = file;
            this.setLabel(file.getName());
            this.updateIcon();
        }
    }
    
    public void setLabel(final String s) {
        String text = s;
        if (s == null) {
            text = "";
        }
        this.label.setText((CharSequence)text);
    }
    
    public void setSelectable(final boolean selectable) {
        this.selectable = selectable;
        this.updateIcon();
    }
    
    public interface OnFileClickListener
    {
        void onClick(final FileItem p0);
    }
}
