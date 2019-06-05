package menion.android.whereyougo.gui.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import menion.android.whereyougo.geo.location.Location;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialog;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.guide.Guide;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Images;
import menion.android.whereyougo.utils.UtilsFormat;

public class CartridgeDetailsActivity extends CustomActivity {
   private static final String TAG = "CartridgeDetails";

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (A.getMain() != null && MainActivity.selectedFile != null && MainActivity.cartridgeFile != null) {
         this.setContentView(2130903052);
         ((TextView)this.findViewById(2131492940)).setText(MainActivity.cartridgeFile.name);
         ((TextView)this.findViewById(2131492941)).setText(this.getString(2131165188) + ": " + MainActivity.cartridgeFile.author);
         ((TextView)this.findViewById(2131492943)).setText(UtilsGUI.simpleHtml(MainActivity.cartridgeFile.description));
         ImageView var4 = (ImageView)this.findViewById(2131492936);

         try {
            byte[] var2 = MainActivity.cartridgeFile.getFile(MainActivity.cartridgeFile.splashId);
            var4.setImageBitmap(Images.resizeBitmap(BitmapFactory.decodeByteArray(var2, 0, var2.length)));
         } catch (Exception var3) {
         }

         ((TextView)this.findViewById(2131492939)).setVisibility(8);
         TextView var5 = (TextView)this.findViewById(2131492942);
         Location var6 = new Location("CartridgeDetails");
         var6.setLatitude(MainActivity.cartridgeFile.latitude);
         var6.setLongitude(MainActivity.cartridgeFile.longitude);
         var5.setText(Html.fromHtml(this.getString(2131165195) + ": <b>" + UtilsFormat.formatDistance((double)LocationState.getLocation().distanceTo(var6), false) + "</b><br />" + this.getString(2131165217) + ": " + UtilsFormat.formatLatitude(MainActivity.cartridgeFile.latitude) + "<br />" + this.getString(2131165220) + ": " + UtilsFormat.formatLatitude(MainActivity.cartridgeFile.longitude)));
         CustomDialog.setBottom(this, this.getString(2131165309), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               CartridgeDetailsActivity.this.finish();
               MainActivity.startSelectedCartridge(false);
               return true;
            }
         }, (String)null, (CustomDialog.OnClickListener)null, this.getString(2131165345), new CustomDialog.OnClickListener() {
            public boolean onClick(CustomDialog var1, View var2, int var3) {
               Location var4 = new Location("CartridgeDetails");
               var4.setLatitude(MainActivity.cartridgeFile.latitude);
               var4.setLongitude(MainActivity.cartridgeFile.longitude);
               Guide var5 = new Guide(MainActivity.cartridgeFile.name, var4);
               A.getGuidingContent().guideStart(var5);
               MainActivity.callGudingScreen(CartridgeDetailsActivity.this);
               CartridgeDetailsActivity.this.finish();
               return true;
            }
         });
      } else {
         this.finish();
      }

   }
}
