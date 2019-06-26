// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.ui.LaunchActivity;
import android.content.IntentFilter;
import org.telegram.SQLite.SQLiteCursor;
import android.service.chooser.ChooserTarget;
import org.telegram.tgnet.TLObject;
import android.os.Bundle;
import android.text.TextUtils;
import java.util.Locale;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import android.content.ComponentName;
import java.util.List;
import android.graphics.Shader;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Bitmap$Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import java.io.File;
import android.graphics.Paint;
import android.graphics.RectF;
import android.annotation.TargetApi;
import android.service.chooser.ChooserTargetService;

@TargetApi(23)
public class TgChooserTargetService extends ChooserTargetService
{
    private RectF bitmapRect;
    private Paint roundPaint;
    
    private Icon createRoundBitmap(final File file) {
        try {
            final Bitmap decodeFile = BitmapFactory.decodeFile(file.toString());
            if (decodeFile != null) {
                final Bitmap bitmap = Bitmap.createBitmap(decodeFile.getWidth(), decodeFile.getHeight(), Bitmap$Config.ARGB_8888);
                bitmap.eraseColor(0);
                final Canvas canvas = new Canvas(bitmap);
                final BitmapShader shader = new BitmapShader(decodeFile, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
                if (this.roundPaint == null) {
                    this.roundPaint = new Paint(1);
                    this.bitmapRect = new RectF();
                }
                this.roundPaint.setShader((Shader)shader);
                this.bitmapRect.set(0.0f, 0.0f, (float)decodeFile.getWidth(), (float)decodeFile.getHeight());
                canvas.drawRoundRect(this.bitmapRect, (float)decodeFile.getWidth(), (float)decodeFile.getHeight(), this.roundPaint);
                return Icon.createWithBitmap(bitmap);
            }
        }
        catch (Throwable t) {
            FileLog.e(t);
        }
        return null;
    }
    
    public List<ChooserTarget> onGetChooserTargets(ComponentName componentName, final IntentFilter intentFilter) {
        final int selectedAccount = UserConfig.selectedAccount;
        componentName = (ComponentName)new ArrayList();
        if (!UserConfig.getInstance(selectedAccount).isClientActivated()) {
            return (List<ChooserTarget>)componentName;
        }
        if (!MessagesController.getGlobalMainSettings().getBoolean("direct_share", true)) {
            return (List<ChooserTarget>)componentName;
        }
        ImageLoader.getInstance();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        MessagesStorage.getInstance(selectedAccount).getStorageQueue().postRunnable(new _$$Lambda$TgChooserTargetService$Tk7TkprF_pFKgMncgdcbNzQeTqs(this, selectedAccount, (List)componentName, new ComponentName(this.getPackageName(), LaunchActivity.class.getCanonicalName()), countDownLatch));
        try {
            countDownLatch.await();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return (List<ChooserTarget>)componentName;
    }
}
