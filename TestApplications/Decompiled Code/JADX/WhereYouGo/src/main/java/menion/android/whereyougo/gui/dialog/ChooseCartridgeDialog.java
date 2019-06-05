package menion.android.whereyougo.gui.dialog;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.activity.MainActivity;
import menion.android.whereyougo.gui.extension.DataInfo;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.Logger;
import p005cz.matejcik.openwig.formats.CartridgeFile;

public class ChooseCartridgeDialog extends CustomDialogFragment {
    private static final String TAG = "ChooseCartridgeDialog";
    private BaseAdapter adapter;
    private Vector<CartridgeFile> cartridgeFiles;
    private ArrayList<DataInfo> data;

    /* renamed from: menion.android.whereyougo.gui.dialog.ChooseCartridgeDialog$2 */
    class C02852 implements OnItemClickListener {
        C02852() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ChooseCartridgeDialog.this.itemClicked(position);
        }
    }

    /* renamed from: menion.android.whereyougo.gui.dialog.ChooseCartridgeDialog$3 */
    class C02863 implements OnItemLongClickListener {
        C02863() {
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
            ChooseCartridgeDialog.this.itemLongClicked(position);
            return true;
        }
    }

    public Dialog createDialog(Bundle savedInstanceState) {
        if (C0322A.getMain() == null || this.cartridgeFiles == null) {
            return null;
        }
        try {
            final Location actLoc = LocationState.getLocation();
            final Location loc1 = new Location(TAG);
            final Location loc2 = new Location(TAG);
            Collections.sort(this.cartridgeFiles, new Comparator<CartridgeFile>() {
                public int compare(CartridgeFile object1, CartridgeFile object2) {
                    loc1.setLatitude(object1.latitude);
                    loc1.setLongitude(object1.longitude);
                    loc2.setLatitude(object2.latitude);
                    loc2.setLongitude(object2.longitude);
                    return (int) (actLoc.distanceTo(loc1) - actLoc.distanceTo(loc2));
                }
            });
            this.data = new ArrayList();
            for (int i = 0; i < this.cartridgeFiles.size(); i++) {
                Bitmap icon;
                CartridgeFile file = (CartridgeFile) this.cartridgeFiles.get(i);
                byte[] iconData = file.getFile(file.iconId);
                try {
                    icon = BitmapFactory.decodeByteArray(iconData, 0, iconData.length);
                } catch (Exception e) {
                    icon = Images.getImageB(C0254R.C0252drawable.icon_gc_wherigo);
                }
                DataInfo di = new DataInfo(file.name, file.type + ", " + file.author + ", " + file.version, icon);
                di.value01 = file.latitude;
                di.value02 = file.longitude;
                di.setDistAzi(actLoc);
                this.data.add(di);
            }
            ListView listView = UtilsGUI.createListView(getActivity(), false, this.data);
            listView.setOnItemClickListener(new C02852());
            listView.setOnItemLongClickListener(new C02863());
            this.adapter = (BaseAdapter) listView.getAdapter();
            return new Builder(getActivity()).setTitle(C0254R.string.choose_cartridge).setIcon(C0254R.C0252drawable.ic_title_logo).setView(listView).setNeutralButton(C0254R.string.close, null).create();
        } catch (Exception e2) {
            Logger.m22e(TAG, "createDialog()", e2);
            return null;
        }
    }

    private void itemClicked(int position) {
        try {
            MainActivity.openCartridge((CartridgeFile) this.cartridgeFiles.get(position));
        } catch (Exception e) {
            Logger.m22e(TAG, "onCreate()", e);
        }
        dismiss();
    }

    private void itemLongClicked(final int position) {
        try {
            final CartridgeFile cartridgeFile = (CartridgeFile) this.cartridgeFiles.get(position);
            final String filename = cartridgeFile.filename.substring(0, cartridgeFile.filename.length() - 3);
            UtilsGUI.showDialogQuestion(getActivity(), (int) C0254R.string.delete_cartridge, new OnClickListener() {

                /* renamed from: menion.android.whereyougo.gui.dialog.ChooseCartridgeDialog$4$1 */
                class C02871 implements FileFilter {
                    C02871() {
                    }

                    public boolean accept(File pathname) {
                        return pathname.getAbsolutePath().startsWith(filename);
                    }
                }

                public void onClick(DialogInterface dialog, int btn) {
                    for (File file : FileSystem.getFiles2(new File(cartridgeFile.filename).getParent(), new C02871())) {
                        file.delete();
                    }
                    MainActivity.refreshCartridges();
                    ChooseCartridgeDialog.this.cartridgeFiles.remove(position);
                    ChooseCartridgeDialog.this.data.remove(position);
                    if (ChooseCartridgeDialog.this.adapter != null) {
                        ChooseCartridgeDialog.this.adapter.notifyDataSetChanged();
                    }
                }
            }, null);
        } catch (Exception e) {
            Logger.m22e(TAG, "onCreate()", e);
        }
    }

    public void setParams(Vector<CartridgeFile> cartridgeFiles) {
        this.cartridgeFiles = cartridgeFiles;
    }
}
