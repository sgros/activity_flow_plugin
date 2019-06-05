// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.extension.activity;

import android.view.View;
import android.widget.VideoView;
import pl.droidsonroids.gif.GifImageView;
import android.net.Uri;
import java.io.File;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Images;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.widget.TextView;
import cz.matejcik.openwig.Media;

public class MediaActivity extends CustomActivity
{
    protected int cachedMediaId;
    
    protected void setMedia(final Media media) {
        if (media != null && media.id != this.cachedMediaId) {
            ((TextView)this.findViewById(2131492939)).setText(UtilsGUI.simpleHtml(media.altText));
            if (media.type != null) {
                Object o = null;
                final String lowerCase = media.type.toLowerCase();
                int n = -1;
            Label_0312_Outer:
                while (true) {
                    byte[] mediaFile;
                    String string;
                    Uri fromFile;
                    Label_0156:Label_0322_Outer:
                    while (true) {
                    Label_0302_Outer:
                        while (true) {
                            while (true) {
                                Label_0272: {
                                    switch (lowerCase.hashCode()) {
                                        case 108273: {
                                            break Label_0272;
                                        }
                                        case 102340: {
                                            break Label_0272;
                                        }
                                        case 3268712: {
                                            break Label_0272;
                                        }
                                        case 105441: {
                                            break Label_0272;
                                        }
                                        case 111145: {
                                            break Label_0272;
                                        }
                                        case 97669: {
                                            Label_0287: {
                                                break Label_0287;
                                                while (true) {
                                                    while (true) {
                                                        Label_0336: {
                                                            try {
                                                                mediaFile = Engine.mediaFile(media);
                                                                if (((View)o).getId() == 2131492936) {
                                                                    ((ImageView)o).setImageBitmap(Images.resizeBitmap(BitmapFactory.decodeByteArray(mediaFile, 0, mediaFile.length)));
                                                                    ((View)o).setVisibility(0);
                                                                    this.cachedMediaId = media.id;
                                                                    return;
                                                                }
                                                                break Label_0336;
                                                                // iftrue(Label_0116:, !lowerCase.equals((Object)"gif"))
                                                                // iftrue(Label_0116:, !lowerCase.equals((Object)"jpeg"))
                                                                // iftrue(Label_0116:, !lowerCase.equals((Object)"png"))
                                                                // iftrue(Label_0116:, !lowerCase.equals((Object)"mp4"))
                                                                // iftrue(Label_0116:, !lowerCase.equals((Object)"bmp"))
                                                                while (true) {
                                                                    Block_9: {
                                                                        break Block_9;
                                                                        Block_12: {
                                                                            Block_10: {
                                                                                break Block_10;
                                                                                break Block_12;
                                                                                o = this.findViewById(2131492938);
                                                                                break Label_0156;
                                                                            }
                                                                            n = 2;
                                                                            break Label_0272;
                                                                            Block_8: {
                                                                                break Block_8;
                                                                                n = 3;
                                                                                break Label_0272;
                                                                            }
                                                                            n = 0;
                                                                            break Label_0272;
                                                                            while (true) {
                                                                                n = 5;
                                                                                break Label_0272;
                                                                                o = this.findViewById(2131492936);
                                                                                break Label_0156;
                                                                                o = this.findViewById(2131492937);
                                                                                break Label_0156;
                                                                                continue Label_0322_Outer;
                                                                            }
                                                                        }
                                                                        n = 4;
                                                                        break Label_0272;
                                                                    }
                                                                    n = 1;
                                                                    break Label_0272;
                                                                    continue Label_0322_Outer;
                                                                }
                                                            }
                                                            // iftrue(Label_0116:, !lowerCase.equals((Object)"jpg"))
                                                            catch (Exception ex) {
                                                                return;
                                                            }
                                                        }
                                                        string = FileSystem.CACHE + media.jarFilename();
                                                        FileSystem.saveBytes(string, mediaFile);
                                                        fromFile = Uri.fromFile(new File(string));
                                                        if (((View)o).getId() == 2131492938) {
                                                            ((GifImageView)o).setImageURI(fromFile);
                                                            continue Label_0312_Outer;
                                                        }
                                                        if (((View)o).getId() == 2131492937) {
                                                            ((VideoView)o).setVideoURI(fromFile);
                                                            continue Label_0312_Outer;
                                                        }
                                                        continue Label_0312_Outer;
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                                switch (n) {
                                    case 0: {
                                        continue;
                                    }
                                    case 1: {
                                        continue Label_0322_Outer;
                                    }
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5: {
                                        continue Label_0302_Outer;
                                    }
                                }
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    if (o != null) {
                        continue Label_0312_Outer;
                    }
                    break;
                }
            }
        }
    }
}
