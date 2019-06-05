package menion.android.whereyougo.gui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog.OnClickListener;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.guide.Guide;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.UtilsFormat;

public class CartridgeDetailsActivity extends CustomActivity {
    private static final String TAG = "CartridgeDetails";

    /* renamed from: menion.android.whereyougo.gui.activity.CartridgeDetailsActivity$1 */
    class C04071 implements OnClickListener {
        C04071() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            CartridgeDetailsActivity.this.finish();
            MainActivity.startSelectedCartridge(false);
            return true;
        }
    }

    /* renamed from: menion.android.whereyougo.gui.activity.CartridgeDetailsActivity$2 */
    class C04082 implements OnClickListener {
        C04082() {
        }

        public boolean onClick(CustomDialog dialog, View v, int btn) {
            Location loc = new Location(CartridgeDetailsActivity.TAG);
            loc.setLatitude(MainActivity.cartridgeFile.latitude);
            loc.setLongitude(MainActivity.cartridgeFile.longitude);
            C0322A.getGuidingContent().guideStart(new Guide(MainActivity.cartridgeFile.name, loc));
            MainActivity.callGudingScreen(CartridgeDetailsActivity.this);
            CartridgeDetailsActivity.this.finish();
            return true;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (C0322A.getMain() == null || MainActivity.selectedFile == null || MainActivity.cartridgeFile == null) {
            finish();
            return;
        }
        setContentView(C0254R.layout.layout_details);
        ((TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewName)).setText(MainActivity.cartridgeFile.name);
        ((TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewState)).setText(getString(C0254R.string.author) + ": " + MainActivity.cartridgeFile.author);
        ((TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewDescription)).setText(UtilsGUI.simpleHtml(MainActivity.cartridgeFile.description));
        ImageView ivImage = (ImageView) findViewById(C0254R.C0253id.mediaImageView);
        try {
            byte[] data = MainActivity.cartridgeFile.getFile(MainActivity.cartridgeFile.splashId);
            ivImage.setImageBitmap(Images.resizeBitmap(BitmapFactory.decodeByteArray(data, 0, data.length)));
        } catch (Exception e) {
        }
        ((TextView) findViewById(C0254R.C0253id.mediaTextView)).setVisibility(8);
        TextView tvDistance = (TextView) findViewById(C0254R.C0253id.layoutDetailsTextViewDistance);
        Location loc = new Location(TAG);
        loc.setLatitude(MainActivity.cartridgeFile.latitude);
        loc.setLongitude(MainActivity.cartridgeFile.longitude);
        tvDistance.setText(Html.fromHtml(getString(C0254R.string.distance) + ": <b>" + UtilsFormat.formatDistance((double) LocationState.getLocation().distanceTo(loc), false) + "</b><br />" + getString(C0254R.string.latitude) + ": " + UtilsFormat.formatLatitude(MainActivity.cartridgeFile.latitude) + "<br />" + getString(C0254R.string.longitude) + ": " + UtilsFormat.formatLatitude(MainActivity.cartridgeFile.longitude)));
        CustomDialog.setBottom(this, getString(C0254R.string.start), new C04071(), null, null, getString(C0254R.string.navigate), new C04082());
    }
}
