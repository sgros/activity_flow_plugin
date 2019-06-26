// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.activity;

import android.app.Activity;
import menion.android.whereyougo.guide.IGuide;
import menion.android.whereyougo.guide.Guide;
import android.view.View;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import android.text.Html;
import menion.android.whereyougo.utils.UtilsFormat;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.utils.Images;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.widget.TextView;
import menion.android.whereyougo.utils.A;
import android.os.Bundle;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;

public class CartridgeDetailsActivity extends CustomActivity
{
    private static final String TAG = "CartridgeDetails";
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (A.getMain() == null || MainActivity.selectedFile == null || MainActivity.cartridgeFile == null) {
            this.finish();
        }
        else {
            this.setContentView(2130903052);
            ((TextView)this.findViewById(2131492940)).setText((CharSequence)MainActivity.cartridgeFile.name);
            ((TextView)this.findViewById(2131492941)).setText((CharSequence)(this.getString(2131165188) + ": " + MainActivity.cartridgeFile.author));
            ((TextView)this.findViewById(2131492943)).setText(UtilsGUI.simpleHtml(MainActivity.cartridgeFile.description));
            final ImageView imageView = (ImageView)this.findViewById(2131492936);
            while (true) {
                try {
                    final byte[] file = MainActivity.cartridgeFile.getFile(MainActivity.cartridgeFile.splashId);
                    imageView.setImageBitmap(Images.resizeBitmap(BitmapFactory.decodeByteArray(file, 0, file.length)));
                    imageView.setVisibility(0);
                    ((TextView)this.findViewById(2131492939)).setVisibility(8);
                    final TextView textView = (TextView)this.findViewById(2131492942);
                    final Location location = new Location("CartridgeDetails");
                    location.setLatitude(MainActivity.cartridgeFile.latitude);
                    location.setLongitude(MainActivity.cartridgeFile.longitude);
                    textView.setText((CharSequence)Html.fromHtml(this.getString(2131165195) + ": <b>" + UtilsFormat.formatDistance(LocationState.getLocation().distanceTo(location), false) + "</b><br />" + this.getString(2131165217) + ": " + UtilsFormat.formatLatitude(MainActivity.cartridgeFile.latitude) + "<br />" + this.getString(2131165220) + ": " + UtilsFormat.formatLatitude(MainActivity.cartridgeFile.longitude)));
                    CustomDialog.setBottom(this, this.getString(2131165309), (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                        @Override
                        public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                            CartridgeDetailsActivity.this.finish();
                            MainActivity.startSelectedCartridge(false);
                            return true;
                        }
                    }, null, null, this.getString(2131165345), (CustomDialog.OnClickListener)new CustomDialog.OnClickListener() {
                        @Override
                        public boolean onClick(final CustomDialog customDialog, final View view, final int n) {
                            final Location location = new Location("CartridgeDetails");
                            location.setLatitude(MainActivity.cartridgeFile.latitude);
                            location.setLongitude(MainActivity.cartridgeFile.longitude);
                            A.getGuidingContent().guideStart(new Guide(MainActivity.cartridgeFile.name, location));
                            MainActivity.callGudingScreen(CartridgeDetailsActivity.this);
                            CartridgeDetailsActivity.this.finish();
                            return true;
                        }
                    });
                }
                catch (Exception ex) {
                    continue;
                }
                break;
            }
        }
    }
}
