// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.LocaleController;
import android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityNodeInfo;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Point;
import android.text.Layout$Alignment;
import android.text.TextUtils$TruncateAt;
import android.text.TextUtils;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.TLObject;
import android.content.Context;
import android.graphics.RectF;
import android.text.StaticLayout;
import org.telegram.messenger.ImageReceiver;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ContactsController;
import android.text.TextPaint;
import android.graphics.Paint;
import android.view.View;

public class GroupCreateSpan extends View
{
    private static Paint backPaint;
    private static TextPaint textPaint;
    private AvatarDrawable avatarDrawable;
    private int[] colors;
    private ContactsController.Contact currentContact;
    private Drawable deleteDrawable;
    private boolean deleting;
    private ImageReceiver imageReceiver;
    private String key;
    private long lastUpdateTime;
    private StaticLayout nameLayout;
    private float progress;
    private RectF rect;
    private int textWidth;
    private float textX;
    private int uid;
    
    static {
        GroupCreateSpan.textPaint = new TextPaint(1);
        GroupCreateSpan.backPaint = new Paint(1);
    }
    
    public GroupCreateSpan(final Context context, final ContactsController.Contact contact) {
        this(context, null, contact);
    }
    
    public GroupCreateSpan(final Context context, final TLObject tlObject) {
        this(context, tlObject, null);
    }
    
    public GroupCreateSpan(final Context context, final TLObject tlObject, final ContactsController.Contact currentContact) {
        super(context);
        this.rect = new RectF();
        this.colors = new int[8];
        this.currentContact = currentContact;
        this.deleteDrawable = this.getResources().getDrawable(2131165372);
        GroupCreateSpan.textPaint.setTextSize((float)AndroidUtilities.dp(14.0f));
        (this.avatarDrawable = new AvatarDrawable()).setTextSize(AndroidUtilities.dp(12.0f));
        String s = null;
        TLObject tlObject3 = null;
        Object o = null;
        Label_0230: {
            TLObject tlObject2;
            ImageLocation imageLocation;
            if (tlObject instanceof TLRPC.User) {
                tlObject2 = tlObject;
                this.avatarDrawable.setInfo((TLRPC.User)tlObject2);
                this.uid = ((TLRPC.User)tlObject2).id;
                s = UserObject.getFirstName((TLRPC.User)tlObject2);
                imageLocation = ImageLocation.getForUser((TLRPC.User)tlObject2, false);
            }
            else {
                if (!(tlObject instanceof TLRPC.Chat)) {
                    this.avatarDrawable.setInfo(0, currentContact.first_name, currentContact.last_name, false);
                    this.uid = currentContact.contact_id;
                    this.key = currentContact.key;
                    if (!TextUtils.isEmpty((CharSequence)currentContact.first_name)) {
                        s = currentContact.first_name;
                    }
                    else {
                        s = currentContact.last_name;
                    }
                    o = (tlObject3 = null);
                    break Label_0230;
                }
                tlObject2 = tlObject;
                this.avatarDrawable.setInfo((TLRPC.Chat)tlObject2);
                this.uid = -((TLRPC.Chat)tlObject2).id;
                s = ((TLRPC.Chat)tlObject2).title;
                imageLocation = ImageLocation.getForChat((TLRPC.Chat)tlObject2, false);
            }
            final TLRPC.Chat chat = (TLRPC.Chat)tlObject2;
            o = imageLocation;
            tlObject3 = chat;
        }
        (this.imageReceiver = new ImageReceiver()).setRoundRadius(AndroidUtilities.dp(16.0f));
        this.imageReceiver.setParentView(this);
        this.imageReceiver.setImageCoords(0, 0, AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
        int n;
        if (AndroidUtilities.isTablet()) {
            n = AndroidUtilities.dp(366.0f) / 2;
        }
        else {
            final Point displaySize = AndroidUtilities.displaySize;
            n = (Math.min(displaySize.x, displaySize.y) - AndroidUtilities.dp(164.0f)) / 2;
        }
        this.nameLayout = new StaticLayout(TextUtils.ellipsize((CharSequence)s.replace('\n', ' '), GroupCreateSpan.textPaint, (float)n, TextUtils$TruncateAt.END), GroupCreateSpan.textPaint, 1000, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        if (this.nameLayout.getLineCount() > 0) {
            this.textWidth = (int)Math.ceil(this.nameLayout.getLineWidth(0));
            this.textX = -this.nameLayout.getLineLeft(0);
        }
        this.imageReceiver.setImage((ImageLocation)o, "50_50", this.avatarDrawable, 0, null, tlObject3, 1);
        this.updateColors();
    }
    
    public void cancelDeleteAnimation() {
        if (!this.deleting) {
            return;
        }
        this.deleting = false;
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    public ContactsController.Contact getContact() {
        return this.currentContact;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public int getUid() {
        return this.uid;
    }
    
    public boolean isDeleting() {
        return this.deleting;
    }
    
    protected void onDraw(final Canvas canvas) {
        if ((this.deleting && this.progress != 1.0f) || (!this.deleting && this.progress != 0.0f)) {
            final long n = System.currentTimeMillis() - this.lastUpdateTime;
            long n2 = 0L;
            Label_0063: {
                if (n >= 0L) {
                    n2 = n;
                    if (n <= 17L) {
                        break Label_0063;
                    }
                }
                n2 = 17L;
            }
            if (this.deleting) {
                this.progress += n2 / 120.0f;
                if (this.progress >= 1.0f) {
                    this.progress = 1.0f;
                }
            }
            else {
                this.progress -= n2 / 120.0f;
                if (this.progress < 0.0f) {
                    this.progress = 0.0f;
                }
            }
            this.invalidate();
        }
        canvas.save();
        this.rect.set(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)AndroidUtilities.dp(32.0f));
        final Paint backPaint = GroupCreateSpan.backPaint;
        final int[] colors = this.colors;
        final int n3 = colors[6];
        final float n4 = (float)(colors[7] - colors[6]);
        final float progress = this.progress;
        backPaint.setColor(Color.argb(n3 + (int)(n4 * progress), colors[0] + (int)((colors[1] - colors[0]) * progress), colors[2] + (int)((colors[3] - colors[2]) * progress), colors[4] + (int)((colors[5] - colors[4]) * progress)));
        canvas.drawRoundRect(this.rect, (float)AndroidUtilities.dp(16.0f), (float)AndroidUtilities.dp(16.0f), GroupCreateSpan.backPaint);
        this.imageReceiver.draw(canvas);
        if (this.progress != 0.0f) {
            final int color = this.avatarDrawable.getColor();
            final float n5 = Color.alpha(color) / 255.0f;
            GroupCreateSpan.backPaint.setColor(color);
            GroupCreateSpan.backPaint.setAlpha((int)(this.progress * 255.0f * n5));
            canvas.drawCircle((float)AndroidUtilities.dp(16.0f), (float)AndroidUtilities.dp(16.0f), (float)AndroidUtilities.dp(16.0f), GroupCreateSpan.backPaint);
            canvas.save();
            canvas.rotate((1.0f - this.progress) * 45.0f, (float)AndroidUtilities.dp(16.0f), (float)AndroidUtilities.dp(16.0f));
            this.deleteDrawable.setBounds(AndroidUtilities.dp(11.0f), AndroidUtilities.dp(11.0f), AndroidUtilities.dp(21.0f), AndroidUtilities.dp(21.0f));
            this.deleteDrawable.setAlpha((int)(this.progress * 255.0f));
            this.deleteDrawable.draw(canvas);
            canvas.restore();
        }
        canvas.translate(this.textX + AndroidUtilities.dp(41.0f), (float)AndroidUtilities.dp(8.0f));
        this.nameLayout.draw(canvas);
        canvas.restore();
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setText(this.nameLayout.getText());
        if (this.isDeleting() && Build$VERSION.SDK_INT >= 21) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo$AccessibilityAction(AccessibilityNodeInfo$AccessibilityAction.ACTION_CLICK.getId(), (CharSequence)LocaleController.getString("Delete", 2131559227)));
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(AndroidUtilities.dp(57.0f) + this.textWidth, AndroidUtilities.dp(32.0f));
    }
    
    public void startDeleteAnimation() {
        if (this.deleting) {
            return;
        }
        this.deleting = true;
        this.lastUpdateTime = System.currentTimeMillis();
        this.invalidate();
    }
    
    public void updateColors() {
        final int color = Theme.getColor("avatar_backgroundGroupCreateSpanBlue");
        final int color2 = Theme.getColor("groupcreate_spanBackground");
        final int color3 = Theme.getColor("groupcreate_spanText");
        final int color4 = Theme.getColor("groupcreate_spanDelete");
        this.colors[0] = Color.red(color2);
        this.colors[1] = Color.red(color);
        this.colors[2] = Color.green(color2);
        this.colors[3] = Color.green(color);
        this.colors[4] = Color.blue(color2);
        this.colors[5] = Color.blue(color);
        this.colors[6] = Color.alpha(color2);
        this.colors[7] = Color.alpha(color);
        GroupCreateSpan.textPaint.setColor(color3);
        this.deleteDrawable.setColorFilter((ColorFilter)new PorterDuffColorFilter(color4, PorterDuff$Mode.MULTIPLY));
        GroupCreateSpan.backPaint.setColor(color2);
        this.avatarDrawable.setColor(AvatarDrawable.getColorForId(5));
    }
}
