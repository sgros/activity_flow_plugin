// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.dialog;

import android.widget.ListView;
import android.graphics.Bitmap;
import android.app.AlertDialog$Builder;
import android.widget.AdapterView$OnItemLongClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.content.Context;
import menion.android.whereyougo.utils.Images;
import android.graphics.BitmapFactory;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.utils.A;
import android.app.Dialog;
import android.os.Bundle;
import android.app.Activity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.FileSystem;
import java.io.FileFilter;
import java.io.File;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.extension.DataInfo;
import java.util.ArrayList;
import cz.matejcik.openwig.formats.CartridgeFile;
import java.util.Vector;
import android.widget.BaseAdapter;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;

public class ChooseCartridgeDialog extends CustomDialogFragment
{
    private static final String TAG = "ChooseCartridgeDialog";
    private BaseAdapter adapter;
    private Vector<CartridgeFile> cartridgeFiles;
    private ArrayList<DataInfo> data;
    
    private void itemClicked(final int index) {
        while (true) {
            try {
                MainActivity.openCartridge(this.cartridgeFiles.get(index));
                this.dismiss();
            }
            catch (Exception ex) {
                Logger.e("ChooseCartridgeDialog", "onCreate()", ex);
                continue;
            }
            break;
        }
    }
    
    private void itemLongClicked(final int index) {
        try {
            final CartridgeFile cartridgeFile = this.cartridgeFiles.get(index);
            UtilsGUI.showDialogQuestion(this.getActivity(), 2131165330, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                final /* synthetic */ String val$filename = cartridgeFile.filename.substring(0, cartridgeFile.filename.length() - 3);
                
                public void onClick(final DialogInterface dialogInterface, int i) {
                    final File[] files2 = FileSystem.getFiles2(new File(cartridgeFile.filename).getParent(), new FileFilter() {
                        @Override
                        public boolean accept(final File file) {
                            return file.getAbsolutePath().startsWith(DialogInterface$OnClickListener.this.val$filename);
                        }
                    });
                    int length;
                    for (length = files2.length, i = 0; i < length; ++i) {
                        files2[i].delete();
                    }
                    MainActivity.refreshCartridges();
                    ChooseCartridgeDialog.this.cartridgeFiles.remove(index);
                    ChooseCartridgeDialog.this.data.remove(index);
                    if (ChooseCartridgeDialog.this.adapter != null) {
                        ChooseCartridgeDialog.this.adapter.notifyDataSetChanged();
                    }
                }
            }, null);
        }
        catch (Exception ex) {
            Logger.e("ChooseCartridgeDialog", "onCreate()", ex);
        }
    }
    
    @Override
    public Dialog createDialog(final Bundle bundle) {
        Object create;
        if (A.getMain() == null || this.cartridgeFiles == null) {
            create = null;
        }
        else {
            try {
                final Location location = LocationState.getLocation();
                Collections.sort(this.cartridgeFiles, new Comparator<CartridgeFile>() {
                    final /* synthetic */ Location val$loc1 = new Location("ChooseCartridgeDialog");
                    final /* synthetic */ Location val$loc2 = new Location("ChooseCartridgeDialog");
                    
                    @Override
                    public int compare(final CartridgeFile cartridgeFile, final CartridgeFile cartridgeFile2) {
                        this.val$loc1.setLatitude(cartridgeFile.latitude);
                        this.val$loc1.setLongitude(cartridgeFile.longitude);
                        this.val$loc2.setLatitude(cartridgeFile2.latitude);
                        this.val$loc2.setLongitude(cartridgeFile2.longitude);
                        return (int)(location.distanceTo(this.val$loc1) - location.distanceTo(this.val$loc2));
                    }
                });
                this.data = new ArrayList<DataInfo>();
                int i = 0;
            Label_0129_Outer:
                while (i < this.cartridgeFiles.size()) {
                    final CartridgeFile cartridgeFile = this.cartridgeFiles.get(i);
                    final byte[] file = cartridgeFile.getFile(cartridgeFile.iconId);
                    while (true) {
                        try {
                            final Bitmap bitmap = BitmapFactory.decodeByteArray(file, 0, file.length);
                            final DataInfo e = new DataInfo(cartridgeFile.name, cartridgeFile.type + ", " + cartridgeFile.author + ", " + cartridgeFile.version, bitmap);
                            e.value01 = cartridgeFile.latitude;
                            e.value02 = cartridgeFile.longitude;
                            e.setDistAzi(location);
                            this.data.add(e);
                            ++i;
                            continue Label_0129_Outer;
                        }
                        catch (Exception ex2) {
                            final Bitmap bitmap = Images.getImageB(2130837554);
                            continue;
                        }
                        break;
                    }
                    break;
                }
                final ListView listView = UtilsGUI.createListView((Context)this.getActivity(), false, this.data);
                listView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        ChooseCartridgeDialog.this.itemClicked(n);
                    }
                });
                listView.setOnItemLongClickListener((AdapterView$OnItemLongClickListener)new AdapterView$OnItemLongClickListener() {
                    public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        ChooseCartridgeDialog.this.itemLongClicked(n);
                        return true;
                    }
                });
                this.adapter = (BaseAdapter)listView.getAdapter();
                create = new AlertDialog$Builder((Context)this.getActivity()).setTitle(2131165191).setIcon(2130837551).setView((View)listView).setNeutralButton(2131165192, (DialogInterface$OnClickListener)null).create();
            }
            catch (Exception ex) {
                Logger.e("ChooseCartridgeDialog", "createDialog()", ex);
                create = null;
            }
        }
        return (Dialog)create;
    }
    
    public void setParams(final Vector<CartridgeFile> cartridgeFiles) {
        this.cartridgeFiles = cartridgeFiles;
    }
}
