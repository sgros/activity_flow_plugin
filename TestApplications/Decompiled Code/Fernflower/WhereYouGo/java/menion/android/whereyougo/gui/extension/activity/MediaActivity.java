package menion.android.whereyougo.gui.extension.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.Media;
import java.io.File;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Images;
import pl.droidsonroids.gif.GifImageView;

public class MediaActivity extends CustomActivity {
   protected int cachedMediaId;

   protected void setMedia(Media var1) {
      if (var1 != null && var1.id != this.cachedMediaId) {
         ((TextView)this.findViewById(2131492939)).setText(UtilsGUI.simpleHtml(var1.altText));
         if (var1.type != null) {
            View var2 = null;
            String var3 = var1.type.toLowerCase();
            byte var4 = -1;
            switch(var3.hashCode()) {
            case 97669:
               if (var3.equals("bmp")) {
                  var4 = 5;
               }
               break;
            case 102340:
               if (var3.equals("gif")) {
                  var4 = 1;
               }
               break;
            case 105441:
               if (var3.equals("jpg")) {
                  var4 = 3;
               }
               break;
            case 108273:
               if (var3.equals("mp4")) {
                  var4 = 0;
               }
               break;
            case 111145:
               if (var3.equals("png")) {
                  var4 = 4;
               }
               break;
            case 3268712:
               if (var3.equals("jpeg")) {
                  var4 = 2;
               }
            }

            switch(var4) {
            case 0:
               var2 = this.findViewById(2131492937);
               break;
            case 1:
               var2 = this.findViewById(2131492938);
               break;
            case 2:
            case 3:
            case 4:
            case 5:
               var2 = this.findViewById(2131492936);
            }

            if (var2 != null) {
               byte[] var5;
               try {
                  var5 = Engine.mediaFile(var1);
               } catch (Exception var6) {
                  return;
               }

               if (var2.getId() == 2131492936) {
                  Bitmap var7 = Images.resizeBitmap(BitmapFactory.decodeByteArray(var5, 0, var5.length));
                  ((ImageView)var2).setImageBitmap(var7);
               } else {
                  var3 = FileSystem.CACHE + var1.jarFilename();
                  FileSystem.saveBytes(var3, var5);
                  Uri var8 = Uri.fromFile(new File(var3));
                  if (var2.getId() == 2131492938) {
                     ((GifImageView)var2).setImageURI(var8);
                  } else if (var2.getId() == 2131492937) {
                     ((VideoView)var2).setVideoURI(var8);
                  }
               }

               var2.setVisibility(0);
               this.cachedMediaId = var1.id;
            }
         }
      }

   }
}
