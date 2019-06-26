// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import org.telegram.messenger.FileLog;
import android.text.Layout$Alignment;
import android.os.Build$VERSION;
import android.graphics.Rect;
import android.graphics.ColorFilter;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import android.graphics.Canvas;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.AndroidUtilities;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.graphics.drawable.Drawable;

public class AvatarDrawable extends Drawable
{
    public static final int AVATAR_TYPE_ARCHIVED = 3;
    public static final int AVATAR_TYPE_NORMAL = 0;
    public static final int AVATAR_TYPE_SAVED = 1;
    public static final int AVATAR_TYPE_SAVED_SMALL = 2;
    private float archivedAvatarProgress;
    private int avatarType;
    private int color;
    private boolean drawBrodcast;
    private boolean isProfile;
    private TextPaint namePaint;
    private StringBuilder stringBuilder;
    private float textHeight;
    private StaticLayout textLayout;
    private float textLeft;
    private float textWidth;
    
    public AvatarDrawable() {
        this.stringBuilder = new StringBuilder(5);
        (this.namePaint = new TextPaint(1)).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.namePaint.setTextSize((float)AndroidUtilities.dp(18.0f));
    }
    
    public AvatarDrawable(final TLRPC.Chat chat) {
        this(chat, false);
    }
    
    public AvatarDrawable(final TLRPC.Chat chat, final boolean isProfile) {
        this();
        this.isProfile = isProfile;
        if (chat != null) {
            final int id = chat.id;
            this.setInfo(id, chat.title, null, id < 0, null);
        }
    }
    
    public AvatarDrawable(final TLRPC.User user) {
        this(user, false);
    }
    
    public AvatarDrawable(final TLRPC.User user, final boolean isProfile) {
        this();
        this.isProfile = isProfile;
        if (user != null) {
            this.setInfo(user.id, user.first_name, user.last_name, false, null);
        }
    }
    
    public static int getButtonColorForId(final int n) {
        return Theme.getColor("avatar_actionBarSelectorBlue");
    }
    
    public static int getColorForId(final int n) {
        return Theme.getColor(Theme.keys_avatar_background[getColorIndex(n)]);
    }
    
    public static int getColorIndex(final int n) {
        if (n >= 0 && n < 7) {
            return n;
        }
        return Math.abs(n % Theme.keys_avatar_background.length);
    }
    
    public static int getIconColorForId(final int n) {
        return Theme.getColor("avatar_actionBarIconBlue");
    }
    
    public static int getNameColorForId(final int n) {
        return Theme.getColor(Theme.keys_avatar_nameInMessage[getColorIndex(n)]);
    }
    
    public static int getProfileBackColorForId(final int n) {
        return Theme.getColor("avatar_backgroundActionBarBlue");
    }
    
    public static int getProfileColorForId(final int n) {
        return Theme.getColor(Theme.keys_avatar_background[getColorIndex(n)]);
    }
    
    public static int getProfileTextColorForId(final int n) {
        return Theme.getColor("avatar_subtitleInProfileBlue");
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        if (bounds == null) {
            return;
        }
        final int width = bounds.width();
        this.namePaint.setColor(Theme.getColor("avatar_text"));
        Theme.avatar_backgroundPaint.setColor(this.color);
        canvas.save();
        canvas.translate((float)bounds.left, (float)bounds.top);
        final float n = (float)width;
        final float n2 = n / 2.0f;
        canvas.drawCircle(n2, n2, n2, Theme.avatar_backgroundPaint);
        final int avatarType = this.avatarType;
        Label_0653: {
            if (avatarType == 3) {
                if (this.archivedAvatarProgress != 0.0f) {
                    Theme.avatar_backgroundPaint.setColor(Theme.getColor("avatar_backgroundArchived"));
                    canvas.drawCircle(n2, n2, this.archivedAvatarProgress * n2, Theme.avatar_backgroundPaint);
                    if (Theme.dialogs_archiveAvatarDrawableRecolored) {
                        Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "Arrow1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("avatar_backgroundArchived"))));
                        Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "Arrow2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("avatar_backgroundArchived"))));
                        Theme.dialogs_archiveAvatarDrawableRecolored = false;
                    }
                }
                else if (!Theme.dialogs_archiveAvatarDrawableRecolored) {
                    Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "Arrow1", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("avatar_backgroundArchivedHidden"))));
                    Theme.dialogs_archiveAvatarDrawable.addValueCallback(new KeyPath(new String[] { "Arrow2", "**" }), LottieProperty.COLOR_FILTER, new LottieValueCallback<ColorFilter>((ColorFilter)new SimpleColorFilter(Theme.getColor("avatar_backgroundArchivedHidden"))));
                    Theme.dialogs_archiveAvatarDrawableRecolored = true;
                }
                final int intrinsicWidth = Theme.dialogs_archiveAvatarDrawable.getIntrinsicWidth();
                final int intrinsicHeight = Theme.dialogs_archiveAvatarDrawable.getIntrinsicHeight();
                final int n3 = (width - intrinsicWidth) / 2;
                final int n4 = (width - intrinsicHeight) / 2;
                canvas.save();
                canvas.translate((float)n3, (float)n4);
                Theme.dialogs_archiveAvatarDrawable.setBounds(n3, n4, intrinsicWidth + n3, intrinsicHeight + n4);
                Theme.dialogs_archiveAvatarDrawable.draw(canvas);
                canvas.restore();
            }
            else {
                if (avatarType != 0) {
                    final Drawable avatar_savedDrawable = Theme.avatar_savedDrawable;
                    if (avatar_savedDrawable != null) {
                        final int intrinsicWidth2 = avatar_savedDrawable.getIntrinsicWidth();
                        final int intrinsicHeight2 = Theme.avatar_savedDrawable.getIntrinsicHeight();
                        int n5 = intrinsicWidth2;
                        int n6 = intrinsicHeight2;
                        if (this.avatarType == 2) {
                            n5 = (int)(intrinsicWidth2 * 0.8f);
                            n6 = (int)(intrinsicHeight2 * 0.8f);
                        }
                        final int n7 = (width - n5) / 2;
                        final int n8 = (width - n6) / 2;
                        Theme.avatar_savedDrawable.setBounds(n7, n8, n5 + n7, n6 + n8);
                        Theme.avatar_savedDrawable.draw(canvas);
                        break Label_0653;
                    }
                }
                if (this.drawBrodcast) {
                    final Drawable avatar_broadcastDrawable = Theme.avatar_broadcastDrawable;
                    if (avatar_broadcastDrawable != null) {
                        final int n9 = (width - avatar_broadcastDrawable.getIntrinsicWidth()) / 2;
                        final int n10 = (width - Theme.avatar_broadcastDrawable.getIntrinsicHeight()) / 2;
                        final Drawable avatar_broadcastDrawable2 = Theme.avatar_broadcastDrawable;
                        avatar_broadcastDrawable2.setBounds(n9, n10, avatar_broadcastDrawable2.getIntrinsicWidth() + n9, Theme.avatar_broadcastDrawable.getIntrinsicHeight() + n10);
                        Theme.avatar_broadcastDrawable.draw(canvas);
                        break Label_0653;
                    }
                }
                if (this.textLayout != null) {
                    canvas.translate((n - this.textWidth) / 2.0f - this.textLeft, (n - this.textHeight) / 2.0f);
                    this.textLayout.draw(canvas);
                }
            }
        }
        canvas.restore();
    }
    
    public int getAvatarType() {
        return this.avatarType;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public int getIntrinsicHeight() {
        return 0;
    }
    
    public int getIntrinsicWidth() {
        return 0;
    }
    
    public int getOpacity() {
        return -2;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setArchivedAvatarHiddenProgress(final float archivedAvatarProgress) {
        this.archivedAvatarProgress = archivedAvatarProgress;
    }
    
    public void setAvatarType(final int avatarType) {
        this.avatarType = avatarType;
        if (this.avatarType == 3) {
            this.color = Theme.getColor("avatar_backgroundArchivedHidden");
        }
        else {
            this.color = Theme.getColor("avatar_backgroundSaved");
        }
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
    
    public void setInfo(final int n, final String s, final String s2, final boolean b) {
        this.setInfo(n, s, s2, b, null);
    }
    
    public void setInfo(int i, final String s, String upperCase, final boolean drawBrodcast, final String str) {
        if (this.isProfile) {
            this.color = getProfileColorForId(i);
        }
        else {
            this.color = getColorForId(i);
        }
        this.drawBrodcast = drawBrodcast;
        this.avatarType = 0;
        String s2 = null;
        String s3 = null;
        Label_0060: {
            if (s != null) {
                s2 = s;
                s3 = upperCase;
                if (s.length() != 0) {
                    break Label_0060;
                }
            }
            s3 = null;
            s2 = upperCase;
        }
        this.stringBuilder.setLength(0);
        if (str != null) {
            this.stringBuilder.append(str);
        }
        else {
            if (s2 != null && s2.length() > 0) {
                this.stringBuilder.appendCodePoint(s2.codePointAt(0));
            }
            if (s3 != null && s3.length() > 0) {
                Integer value;
                for (i = s3.length() - 1, value = null; i >= 0 && (value == null || s3.charAt(i) != ' '); value = s3.codePointAt(i), --i) {}
                if (Build$VERSION.SDK_INT > 17) {
                    this.stringBuilder.append("\u200c");
                }
                this.stringBuilder.appendCodePoint(value);
            }
            else if (s2 != null && s2.length() > 0) {
                int n;
                for (i = s2.length() - 1; i >= 0; --i) {
                    if (s2.charAt(i) == ' ' && i != s2.length() - 1) {
                        n = i + 1;
                        if (s2.charAt(n) != ' ') {
                            if (Build$VERSION.SDK_INT > 17) {
                                this.stringBuilder.append("\u200c");
                            }
                            this.stringBuilder.appendCodePoint(s2.codePointAt(n));
                            break;
                        }
                    }
                }
            }
        }
        if (this.stringBuilder.length() > 0) {
            upperCase = this.stringBuilder.toString().toUpperCase();
            try {
                this.textLayout = new StaticLayout((CharSequence)upperCase, this.namePaint, AndroidUtilities.dp(100.0f), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                if (this.textLayout.getLineCount() > 0) {
                    this.textLeft = this.textLayout.getLineLeft(0);
                    this.textWidth = this.textLayout.getLineWidth(0);
                    this.textHeight = (float)this.textLayout.getLineBottom(0);
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
        }
        else {
            this.textLayout = null;
        }
    }
    
    public void setInfo(final TLRPC.Chat chat) {
        if (chat != null) {
            final int id = chat.id;
            this.setInfo(id, chat.title, null, id < 0, null);
        }
    }
    
    public void setInfo(final TLRPC.User user) {
        if (user != null) {
            this.setInfo(user.id, user.first_name, user.last_name, false, null);
        }
    }
    
    public void setProfile(final boolean isProfile) {
        this.isProfile = isProfile;
    }
    
    public void setTextSize(final int n) {
        this.namePaint.setTextSize((float)n);
    }
}
