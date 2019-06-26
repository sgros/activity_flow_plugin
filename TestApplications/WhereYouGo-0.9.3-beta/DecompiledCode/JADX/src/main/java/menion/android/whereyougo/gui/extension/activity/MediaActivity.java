package menion.android.whereyougo.gui.extension.activity;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import java.io.File;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Images;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.Media;
import p008pl.droidsonroids.gif.GifImageView;

public class MediaActivity extends CustomActivity {
    protected int cachedMediaId;

    /* Access modifiers changed, original: protected */
    public void setMedia(Media media) {
        if (media != null && media.f98id != this.cachedMediaId) {
            ((TextView) findViewById(C0254R.C0253id.mediaTextView)).setText(UtilsGUI.simpleHtml(media.altText));
            if (media.type != null) {
                View view = null;
                String toLowerCase = media.type.toLowerCase();
                Object obj = -1;
                switch (toLowerCase.hashCode()) {
                    case 97669:
                        if (toLowerCase.equals("bmp")) {
                            obj = 5;
                            break;
                        }
                        break;
                    case 102340:
                        if (toLowerCase.equals("gif")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 105441:
                        if (toLowerCase.equals("jpg")) {
                            obj = 3;
                            break;
                        }
                        break;
                    case 108273:
                        if (toLowerCase.equals("mp4")) {
                            obj = null;
                            break;
                        }
                        break;
                    case 111145:
                        if (toLowerCase.equals("png")) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 3268712:
                        if (toLowerCase.equals("jpeg")) {
                            obj = 2;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case null:
                        view = findViewById(C0254R.C0253id.mediaVideoView);
                        break;
                    case 1:
                        view = findViewById(C0254R.C0253id.mediaGifView);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        view = findViewById(C0254R.C0253id.mediaImageView);
                        break;
                }
                if (view != null) {
                    try {
                        byte[] data = Engine.mediaFile(media);
                        if (view.getId() == C0254R.C0253id.mediaImageView) {
                            ((ImageView) view).setImageBitmap(Images.resizeBitmap(BitmapFactory.decodeByteArray(data, 0, data.length)));
                        } else {
                            String filename = FileSystem.CACHE + media.jarFilename();
                            FileSystem.saveBytes(filename, data);
                            Uri uri = Uri.fromFile(new File(filename));
                            if (view.getId() == C0254R.C0253id.mediaGifView) {
                                ((GifImageView) view).setImageURI(uri);
                            } else if (view.getId() == C0254R.C0253id.mediaVideoView) {
                                ((VideoView) view).setVideoURI(uri);
                            }
                        }
                        view.setVisibility(0);
                        this.cachedMediaId = media.f98id;
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
