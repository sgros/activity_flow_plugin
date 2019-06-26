// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.View$OnTouchListener;
import org.telegram.ui.Components.LayoutHelper;
import android.view.View$OnApplyWindowInsetsListener;
import org.telegram.messenger.UserConfig;
import android.view.ViewGroup$LayoutParams;
import org.telegram.messenger.WebFile;
import android.text.Layout$Alignment;
import org.telegram.messenger.Emoji;
import android.text.TextUtils;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import android.view.MotionEvent;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Cells.StickerEmojiCell;
import android.graphics.Bitmap;
import org.telegram.messenger.FileLog;
import android.view.WindowManager;
import android.os.Build$VERSION;
import org.telegram.ui.Components.RecyclerListView;
import android.graphics.Canvas;
import org.telegram.ui.ActionBar.Theme;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.DataQuery;
import android.content.DialogInterface;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import android.widget.FrameLayout;
import android.view.WindowManager$LayoutParams;
import org.telegram.ui.ActionBar.BottomSheet;
import android.text.StaticLayout;
import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.view.WindowInsets;
import android.view.View;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.ImageReceiver;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.annotation.SuppressLint;

public class ContentPreviewViewer
{
    private static final int CONTENT_TYPE_GIF = 1;
    private static final int CONTENT_TYPE_NONE = -1;
    private static final int CONTENT_TYPE_STICKER = 0;
    @SuppressLint({ "StaticFieldLeak" })
    private static volatile ContentPreviewViewer Instance;
    private static TextPaint textPaint;
    private boolean animateY;
    private ColorDrawable backgroundDrawable;
    private ImageReceiver centerImage;
    private boolean clearsInputField;
    private FrameLayoutDrawer containerView;
    private int currentAccount;
    private int currentContentType;
    private TLRPC.Document currentDocument;
    private float currentMoveY;
    private float currentMoveYProgress;
    private View currentPreviewCell;
    private TLRPC.InputStickerSet currentStickerSet;
    private ContentPreviewViewerDelegate delegate;
    private float finalMoveY;
    private TLRPC.BotInlineResult inlineResult;
    private boolean isVisible;
    private int keyboardHeight;
    private WindowInsets lastInsets;
    private float lastTouchY;
    private long lastUpdateTime;
    private float moveY;
    private Runnable openPreviewRunnable;
    private Activity parentActivity;
    private float showProgress;
    private Runnable showSheetRunnable;
    private Drawable slideUpDrawable;
    private float startMoveY;
    private int startX;
    private int startY;
    private StaticLayout stickerEmojiLayout;
    private BottomSheet visibleDialog;
    private WindowManager$LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    
    public ContentPreviewViewer() {
        this.moveY = 0.0f;
        this.backgroundDrawable = new ColorDrawable(1895825408);
        this.centerImage = new ImageReceiver();
        this.isVisible = false;
        this.keyboardHeight = AndroidUtilities.dp(200.0f);
        this.showSheetRunnable = new Runnable() {
            @Override
            public void run() {
                if (ContentPreviewViewer.this.parentActivity == null) {
                    return;
                }
                if (ContentPreviewViewer.this.currentContentType == 0) {
                    if (ContentPreviewViewer.this.currentStickerSet == null) {
                        return;
                    }
                    final boolean stickerInFavorites = DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).isStickerInFavorites(ContentPreviewViewer.this.currentDocument);
                    final BottomSheet.Builder builder = new BottomSheet.Builder((Context)ContentPreviewViewer.this.parentActivity);
                    final ArrayList<String> list = new ArrayList<String>();
                    final ArrayList<Integer> list2 = new ArrayList<Integer>();
                    final ArrayList<Integer> list3 = new ArrayList<Integer>();
                    if (ContentPreviewViewer.this.delegate != null) {
                        if (ContentPreviewViewer.this.delegate.needSend()) {
                            list.add(LocaleController.getString("SendStickerPreview", 2131560708));
                            list3.add(2131165723);
                            list2.add(0);
                        }
                        if (ContentPreviewViewer.this.delegate.needOpen()) {
                            list.add(LocaleController.formatString("ViewPackPreview", 2131561054, new Object[0]));
                            list3.add(2131165722);
                            list2.add(1);
                        }
                    }
                    if (!MessageObject.isMaskDocument(ContentPreviewViewer.this.currentDocument) && (stickerInFavorites || DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).canAddStickerToFavorites())) {
                        int n;
                        String s;
                        if (stickerInFavorites) {
                            n = 2131559245;
                            s = "DeleteFromFavorites";
                        }
                        else {
                            n = 2131558591;
                            s = "AddToFavorites";
                        }
                        list.add(LocaleController.getString(s, n));
                        int i;
                        if (stickerInFavorites) {
                            i = 2131165724;
                        }
                        else {
                            i = 2131165721;
                        }
                        list3.add(i);
                        list2.add(2);
                    }
                    if (list.isEmpty()) {
                        return;
                    }
                    final int[] array = new int[list3.size()];
                    for (int j = 0; j < list3.size(); ++j) {
                        array[j] = list3.get(j);
                    }
                    builder.setItems(list.toArray(new CharSequence[0]), array, (DialogInterface$OnClickListener)new _$$Lambda$ContentPreviewViewer$1$_tphIjLgQDrHLUWAgGWRuEayPA8(this, list2, stickerInFavorites));
                    builder.setDimBehind(false);
                    ContentPreviewViewer.this.visibleDialog = builder.create();
                    ContentPreviewViewer.this.visibleDialog.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$ContentPreviewViewer$1$rUBBWoN2ti7pHLcw01tGVjaLoPY(this));
                    ContentPreviewViewer.this.visibleDialog.show();
                    ContentPreviewViewer.this.containerView.performHapticFeedback(0);
                }
                else if (ContentPreviewViewer.this.delegate != null) {
                    ContentPreviewViewer.this.animateY = true;
                    final ContentPreviewViewer this$0 = ContentPreviewViewer.this;
                    this$0.visibleDialog = new BottomSheet(this$0.parentActivity, false, 0) {
                        @Override
                        protected void onContainerTranslationYChanged(final float n) {
                            if (ContentPreviewViewer.this.animateY) {
                                this.getSheetContainer();
                                if (ContentPreviewViewer.this.finalMoveY == 0.0f) {
                                    ContentPreviewViewer.this.finalMoveY = 0.0f;
                                    final ContentPreviewViewer this$0 = ContentPreviewViewer.this;
                                    this$0.startMoveY = this$0.moveY;
                                }
                                ContentPreviewViewer.this.currentMoveYProgress = 1.0f - Math.min(1.0f, n / super.containerView.getMeasuredHeight());
                                final ContentPreviewViewer this$2 = ContentPreviewViewer.this;
                                this$2.moveY = this$2.startMoveY + (ContentPreviewViewer.this.finalMoveY - ContentPreviewViewer.this.startMoveY) * ContentPreviewViewer.this.currentMoveYProgress;
                                ContentPreviewViewer.this.containerView.invalidate();
                                if (ContentPreviewViewer.this.currentMoveYProgress == 1.0f) {
                                    ContentPreviewViewer.this.animateY = false;
                                }
                            }
                        }
                    };
                    final ArrayList<String> list4 = new ArrayList<String>();
                    final ArrayList<Integer> list5 = new ArrayList<Integer>();
                    final ArrayList<Integer> list6 = new ArrayList<Integer>();
                    if (ContentPreviewViewer.this.delegate.needSend()) {
                        list4.add(LocaleController.getString("SendGifPreview", 2131560693));
                        list6.add(2131165723);
                        list5.add(0);
                    }
                    int hasRecentGif;
                    if (ContentPreviewViewer.this.currentDocument != null) {
                        hasRecentGif = (DataQuery.getInstance(ContentPreviewViewer.this.currentAccount).hasRecentGif(ContentPreviewViewer.this.currentDocument) ? 1 : 0);
                        if (hasRecentGif != 0) {
                            list4.add(LocaleController.formatString("Delete", 2131559227, new Object[0]));
                            list6.add(2131165348);
                            list5.add(1);
                        }
                        else {
                            list4.add(LocaleController.formatString("SaveToGIFs", 2131560629, new Object[0]));
                            list6.add(2131165720);
                            list5.add(2);
                        }
                    }
                    else {
                        hasRecentGif = 0;
                    }
                    final int[] array2 = new int[list6.size()];
                    for (int k = 0; k < list6.size(); ++k) {
                        array2[k] = list6.get(k);
                    }
                    ContentPreviewViewer.this.visibleDialog.setItems(list4.toArray(new CharSequence[0]), array2, (DialogInterface$OnClickListener)new _$$Lambda$ContentPreviewViewer$1$S1kRdazvJNKM_pErU5YcNROqRxU(this, list5));
                    ContentPreviewViewer.this.visibleDialog.setDimBehind(false);
                    ContentPreviewViewer.this.visibleDialog.setOnDismissListener((DialogInterface$OnDismissListener)new _$$Lambda$ContentPreviewViewer$1$jPj7FNt8_HCUYPFDcuvR11RuA7g(this));
                    ContentPreviewViewer.this.visibleDialog.show();
                    ContentPreviewViewer.this.containerView.performHapticFeedback(0);
                    if (hasRecentGif != 0) {
                        ContentPreviewViewer.this.visibleDialog.setItemColor(list4.size() - 1, Theme.getColor("dialogTextRed2"), Theme.getColor("dialogRedIcon"));
                    }
                }
            }
        };
    }
    
    public static ContentPreviewViewer getInstance() {
        final ContentPreviewViewer instance;
        if ((instance = ContentPreviewViewer.Instance) == null) {
            synchronized (PhotoViewer.class) {
                if (ContentPreviewViewer.Instance == null) {
                    ContentPreviewViewer.Instance = new ContentPreviewViewer();
                }
            }
        }
        return instance;
    }
    
    public static boolean hasInstance() {
        return ContentPreviewViewer.Instance != null;
    }
    
    @SuppressLint({ "DrawAllocation" })
    private void onDraw(final Canvas canvas) {
        if (this.containerView != null) {
            final ColorDrawable backgroundDrawable = this.backgroundDrawable;
            if (backgroundDrawable != null) {
                backgroundDrawable.setAlpha((int)(this.showProgress * 180.0f));
                final ColorDrawable backgroundDrawable2 = this.backgroundDrawable;
                final int width = this.containerView.getWidth();
                final int height = this.containerView.getHeight();
                int dp = 0;
                backgroundDrawable2.setBounds(0, 0, width, height);
                this.backgroundDrawable.draw(canvas);
                canvas.save();
                int n = 0;
                int n2 = 0;
                Label_0127: {
                    if (Build$VERSION.SDK_INT >= 21) {
                        final WindowInsets lastInsets = this.lastInsets;
                        if (lastInsets != null) {
                            n = lastInsets.getStableInsetBottom() + this.lastInsets.getStableInsetTop();
                            n2 = this.lastInsets.getStableInsetTop();
                            break Label_0127;
                        }
                    }
                    n2 = AndroidUtilities.statusBarHeight;
                    n = 0;
                }
                int n3;
                if (this.currentContentType == 1) {
                    n3 = Math.min(this.containerView.getWidth(), this.containerView.getHeight() - n) - AndroidUtilities.dp(40.0f);
                }
                else {
                    n3 = (int)(Math.min(this.containerView.getWidth(), this.containerView.getHeight() - n) / 1.8f);
                }
                final float n4 = (float)(this.containerView.getWidth() / 2);
                final float moveY = this.moveY;
                final int n5 = n3 / 2;
                if (this.stickerEmojiLayout != null) {
                    dp = AndroidUtilities.dp(40.0f);
                }
                canvas.translate(n4, moveY + Math.max(n5 + n2 + dp, (this.containerView.getHeight() - n - this.keyboardHeight) / 2));
                if (this.centerImage.getBitmap() != null) {
                    final float showProgress = this.showProgress;
                    final int n6 = (int)(n3 * (showProgress * 0.8f / 0.8f));
                    this.centerImage.setAlpha(showProgress);
                    final ImageReceiver centerImage = this.centerImage;
                    final int n7 = -n6 / 2;
                    centerImage.setImageCoords(n7, n7, n6, n6);
                    this.centerImage.draw(canvas);
                    if (this.currentContentType == 1) {
                        final Drawable slideUpDrawable = this.slideUpDrawable;
                        if (slideUpDrawable != null) {
                            final int intrinsicWidth = slideUpDrawable.getIntrinsicWidth();
                            final int intrinsicHeight = this.slideUpDrawable.getIntrinsicHeight();
                            final int n8 = (int)(this.centerImage.getDrawRegion().top - AndroidUtilities.dp(this.currentMoveY / AndroidUtilities.dp(60.0f) * 6.0f + 17.0f));
                            this.slideUpDrawable.setAlpha((int)((1.0f - this.currentMoveYProgress) * 255.0f));
                            this.slideUpDrawable.setBounds(-intrinsicWidth / 2, -intrinsicHeight + n8, intrinsicWidth / 2, n8);
                            this.slideUpDrawable.draw(canvas);
                        }
                    }
                }
                if (this.stickerEmojiLayout != null) {
                    canvas.translate((float)(-AndroidUtilities.dp(50.0f)), (float)(-this.centerImage.getImageHeight() / 2 - AndroidUtilities.dp(30.0f)));
                    this.stickerEmojiLayout.draw(canvas);
                }
                canvas.restore();
                if (this.isVisible) {
                    if (this.showProgress != 1.0f) {
                        final long currentTimeMillis = System.currentTimeMillis();
                        final long lastUpdateTime = this.lastUpdateTime;
                        this.lastUpdateTime = currentTimeMillis;
                        this.showProgress += (currentTimeMillis - lastUpdateTime) / 120.0f;
                        this.containerView.invalidate();
                        if (this.showProgress > 1.0f) {
                            this.showProgress = 1.0f;
                        }
                    }
                }
                else if (this.showProgress != 0.0f) {
                    final long currentTimeMillis2 = System.currentTimeMillis();
                    final long lastUpdateTime2 = this.lastUpdateTime;
                    this.lastUpdateTime = currentTimeMillis2;
                    this.showProgress -= (currentTimeMillis2 - lastUpdateTime2) / 120.0f;
                    this.containerView.invalidate();
                    if (this.showProgress < 0.0f) {
                        this.showProgress = 0.0f;
                    }
                    if (this.showProgress == 0.0f) {
                        this.centerImage.setImageBitmap((Drawable)null);
                        AndroidUtilities.unlockOrientation(this.parentActivity);
                        AndroidUtilities.runOnUIThread(new _$$Lambda$ContentPreviewViewer$3zyytvnhTcdtAb2UIfBrX_cZ_go(this));
                        try {
                            if (this.windowView.getParent() != null) {
                                ((WindowManager)this.parentActivity.getSystemService("window")).removeView((View)this.windowView);
                            }
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                }
            }
        }
    }
    
    private float rubberYPoisition(float a, float n) {
        final float n2 = Math.abs(a) * 0.55f / n;
        final float n3 = 1.0f;
        n = -((1.0f - 1.0f / (n2 + 1.0f)) * n);
        if (a < 0.0f) {
            a = n3;
        }
        else {
            a = -1.0f;
        }
        return n * a;
    }
    
    public void close() {
        if (this.parentActivity != null) {
            if (this.visibleDialog == null) {
                AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                this.showProgress = 1.0f;
                this.lastUpdateTime = System.currentTimeMillis();
                this.containerView.invalidate();
                try {
                    if (this.visibleDialog != null) {
                        this.visibleDialog.dismiss();
                        this.visibleDialog = null;
                    }
                }
                catch (Exception ex) {
                    FileLog.e(ex);
                }
                this.currentDocument = null;
                this.currentStickerSet = null;
                this.delegate = null;
                this.isVisible = false;
            }
        }
    }
    
    public void destroy() {
        this.isVisible = false;
        this.delegate = null;
        this.currentDocument = null;
        this.currentStickerSet = null;
        try {
            if (this.visibleDialog != null) {
                this.visibleDialog.dismiss();
                this.visibleDialog = null;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        if (this.parentActivity != null) {
            final FrameLayout windowView = this.windowView;
            if (windowView != null) {
                try {
                    if (windowView.getParent() != null) {
                        ((WindowManager)this.parentActivity.getSystemService("window")).removeViewImmediate((View)this.windowView);
                    }
                    this.windowView = null;
                }
                catch (Exception ex2) {
                    FileLog.e(ex2);
                }
                ContentPreviewViewer.Instance = null;
            }
        }
    }
    
    public boolean isVisible() {
        return this.isVisible;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent, final RecyclerListView recyclerListView, final int n, final ContentPreviewViewerDelegate delegate) {
        this.delegate = delegate;
        if (motionEvent.getAction() == 0) {
            final int startX = (int)motionEvent.getX();
            final int startY = (int)motionEvent.getY();
            final int childCount = recyclerListView.getChildCount();
            int i = 0;
            while (i < childCount) {
                View child = null;
                if (recyclerListView instanceof RecyclerListView) {
                    child = recyclerListView.getChildAt(i);
                }
                if (child == null) {
                    return false;
                }
                final int top = child.getTop();
                final int bottom = child.getBottom();
                final int left = child.getLeft();
                final int right = child.getRight();
                if (top <= startY && bottom >= startY && left <= startX && right >= startX) {
                    int n2 = 0;
                    Label_0249: {
                        Label_0246: {
                            if (child instanceof StickerEmojiCell) {
                                if (!((StickerEmojiCell)child).showingBitmap()) {
                                    break Label_0246;
                                }
                                this.centerImage.setRoundRadius(0);
                            }
                            else if (child instanceof StickerCell) {
                                if (!((StickerCell)child).showingBitmap()) {
                                    break Label_0246;
                                }
                                this.centerImage.setRoundRadius(0);
                            }
                            else {
                                if (!(child instanceof ContextLinkCell)) {
                                    break Label_0246;
                                }
                                final ContextLinkCell contextLinkCell = (ContextLinkCell)child;
                                if (!contextLinkCell.showingBitmap()) {
                                    break Label_0246;
                                }
                                if (contextLinkCell.isSticker()) {
                                    this.centerImage.setRoundRadius(0);
                                }
                                else {
                                    if (contextLinkCell.isGif()) {
                                        this.centerImage.setRoundRadius(AndroidUtilities.dp(6.0f));
                                        n2 = 1;
                                        break Label_0249;
                                    }
                                    break Label_0246;
                                }
                            }
                            n2 = 0;
                            break Label_0249;
                        }
                        n2 = -1;
                    }
                    if (n2 == -1) {
                        return false;
                    }
                    this.startX = startX;
                    this.startY = startY;
                    this.currentPreviewCell = child;
                    AndroidUtilities.runOnUIThread(this.openPreviewRunnable = new _$$Lambda$ContentPreviewViewer$wFY_75sBoPTmhZIpv5QBGVqAeaE(this, recyclerListView, n, n2), 200L);
                    return true;
                }
                else {
                    ++i;
                }
            }
        }
        return false;
    }
    
    public boolean onTouch(final MotionEvent motionEvent, final RecyclerListView recyclerListView, final int keyboardHeight, final Object o, final ContentPreviewViewerDelegate delegate) {
        this.delegate = delegate;
        if (this.openPreviewRunnable != null || this.isVisible()) {
            if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3 && motionEvent.getAction() != 6) {
                if (motionEvent.getAction() != 0) {
                    if (this.isVisible) {
                        if (motionEvent.getAction() == 2) {
                            if (this.currentContentType == 1) {
                                if (this.visibleDialog == null && this.showProgress == 1.0f) {
                                    if (this.lastTouchY == -10000.0f) {
                                        this.lastTouchY = motionEvent.getY();
                                        this.currentMoveY = 0.0f;
                                        this.moveY = 0.0f;
                                    }
                                    else {
                                        final float y = motionEvent.getY();
                                        this.currentMoveY += y - this.lastTouchY;
                                        this.lastTouchY = y;
                                        final float currentMoveY = this.currentMoveY;
                                        if (currentMoveY > 0.0f) {
                                            this.currentMoveY = 0.0f;
                                        }
                                        else if (currentMoveY < -AndroidUtilities.dp(60.0f)) {
                                            this.currentMoveY = (float)(-AndroidUtilities.dp(60.0f));
                                        }
                                        this.moveY = this.rubberYPoisition(this.currentMoveY, (float)AndroidUtilities.dp(200.0f));
                                        this.containerView.invalidate();
                                        if (this.currentMoveY <= -AndroidUtilities.dp(55.0f)) {
                                            AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                                            this.showSheetRunnable.run();
                                        }
                                    }
                                }
                                return true;
                            }
                            final int n = (int)motionEvent.getX();
                            final int n2 = (int)motionEvent.getY();
                            final int childCount = recyclerListView.getChildCount();
                            int i = 0;
                            while (i < childCount) {
                                View child;
                                if (recyclerListView instanceof RecyclerListView) {
                                    child = recyclerListView.getChildAt(i);
                                }
                                else {
                                    child = null;
                                }
                                if (child == null) {
                                    return false;
                                }
                                final int top = child.getTop();
                                final int bottom = child.getBottom();
                                final int left = child.getLeft();
                                final int right = child.getRight();
                                if (top <= n2 && bottom >= n2 && left <= n && right >= n) {
                                    int n3 = 0;
                                    Label_0468: {
                                        Label_0385: {
                                            if (child instanceof StickerEmojiCell) {
                                                this.centerImage.setRoundRadius(0);
                                            }
                                            else {
                                                if (!(child instanceof StickerCell)) {
                                                    if (child instanceof ContextLinkCell) {
                                                        final ContextLinkCell contextLinkCell = (ContextLinkCell)child;
                                                        if (contextLinkCell.isSticker()) {
                                                            this.centerImage.setRoundRadius(0);
                                                            break Label_0385;
                                                        }
                                                        if (contextLinkCell.isGif()) {
                                                            this.centerImage.setRoundRadius(AndroidUtilities.dp(6.0f));
                                                            n3 = 1;
                                                            break Label_0468;
                                                        }
                                                    }
                                                    n3 = -1;
                                                    break Label_0468;
                                                }
                                                this.centerImage.setRoundRadius(0);
                                            }
                                        }
                                        n3 = 0;
                                    }
                                    if (n3 == -1) {
                                        break;
                                    }
                                    final View currentPreviewCell = this.currentPreviewCell;
                                    if (child == currentPreviewCell) {
                                        break;
                                    }
                                    if (currentPreviewCell instanceof StickerEmojiCell) {
                                        ((StickerEmojiCell)currentPreviewCell).setScaled(false);
                                    }
                                    else if (currentPreviewCell instanceof StickerCell) {
                                        ((StickerCell)currentPreviewCell).setScaled(false);
                                    }
                                    else if (currentPreviewCell instanceof ContextLinkCell) {
                                        ((ContextLinkCell)currentPreviewCell).setScaled(false);
                                    }
                                    this.currentPreviewCell = child;
                                    this.setKeyboardHeight(keyboardHeight);
                                    this.clearsInputField = false;
                                    final View currentPreviewCell2 = this.currentPreviewCell;
                                    if (currentPreviewCell2 instanceof StickerEmojiCell) {
                                        final StickerEmojiCell stickerEmojiCell = (StickerEmojiCell)currentPreviewCell2;
                                        this.open(stickerEmojiCell.getSticker(), null, n3, ((StickerEmojiCell)this.currentPreviewCell).isRecent());
                                        stickerEmojiCell.setScaled(true);
                                    }
                                    else if (currentPreviewCell2 instanceof StickerCell) {
                                        final StickerCell stickerCell = (StickerCell)currentPreviewCell2;
                                        this.open(stickerCell.getSticker(), null, n3, false);
                                        stickerCell.setScaled(true);
                                        this.clearsInputField = stickerCell.isClearsInputField();
                                    }
                                    else if (currentPreviewCell2 instanceof ContextLinkCell) {
                                        final ContextLinkCell contextLinkCell2 = (ContextLinkCell)currentPreviewCell2;
                                        this.open(contextLinkCell2.getDocument(), contextLinkCell2.getBotInlineResult(), n3, false);
                                        if (n3 != 1) {
                                            contextLinkCell2.setScaled(true);
                                        }
                                    }
                                    return true;
                                }
                                else {
                                    ++i;
                                }
                            }
                        }
                        return true;
                    }
                    if (this.openPreviewRunnable != null) {
                        if (motionEvent.getAction() == 2) {
                            if (Math.hypot(this.startX - motionEvent.getX(), this.startY - motionEvent.getY()) > AndroidUtilities.dp(10.0f)) {
                                AndroidUtilities.cancelRunOnUIThread(this.openPreviewRunnable);
                                this.openPreviewRunnable = null;
                            }
                        }
                        else {
                            AndroidUtilities.cancelRunOnUIThread(this.openPreviewRunnable);
                            this.openPreviewRunnable = null;
                        }
                    }
                }
            }
            else {
                AndroidUtilities.runOnUIThread(new _$$Lambda$ContentPreviewViewer$EMKDqwNyTHEkiYf1BXP5lN4E1U8(recyclerListView, o), 150L);
                final Runnable openPreviewRunnable = this.openPreviewRunnable;
                if (openPreviewRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(openPreviewRunnable);
                    this.openPreviewRunnable = null;
                }
                else if (this.isVisible()) {
                    this.close();
                    final View currentPreviewCell3 = this.currentPreviewCell;
                    if (currentPreviewCell3 != null) {
                        if (currentPreviewCell3 instanceof StickerEmojiCell) {
                            ((StickerEmojiCell)currentPreviewCell3).setScaled(false);
                        }
                        else if (currentPreviewCell3 instanceof StickerCell) {
                            ((StickerCell)currentPreviewCell3).setScaled(false);
                        }
                        else if (currentPreviewCell3 instanceof ContextLinkCell) {
                            ((ContextLinkCell)currentPreviewCell3).setScaled(false);
                        }
                        this.currentPreviewCell = null;
                    }
                }
            }
        }
        return false;
    }
    
    public void open(final TLRPC.Document document, final TLRPC.BotInlineResult botInlineResult, final int currentContentType, final boolean b) {
        if (this.parentActivity != null) {
            if (this.windowView != null) {
                this.stickerEmojiLayout = null;
                Label_0537: {
                    if (currentContentType == 0) {
                        if (document == null) {
                            return;
                        }
                        if (ContentPreviewViewer.textPaint == null) {
                            (ContentPreviewViewer.textPaint = new TextPaint(1)).setTextSize((float)AndroidUtilities.dp(24.0f));
                        }
                        while (true) {
                            for (int i = 0; i < document.attributes.size(); ++i) {
                                final TLRPC.DocumentAttribute documentAttribute = document.attributes.get(i);
                                if (documentAttribute instanceof TLRPC.TL_documentAttributeSticker) {
                                    final TLRPC.InputStickerSet stickerset = documentAttribute.stickerset;
                                    if (stickerset != null) {
                                        if (stickerset != null) {
                                            try {
                                                if (this.visibleDialog != null) {
                                                    this.visibleDialog.setOnDismissListener((DialogInterface$OnDismissListener)null);
                                                    this.visibleDialog.dismiss();
                                                    this.visibleDialog = null;
                                                }
                                            }
                                            catch (Exception ex) {
                                                FileLog.e(ex);
                                            }
                                            AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                                            AndroidUtilities.runOnUIThread(this.showSheetRunnable, 1300L);
                                        }
                                        this.currentStickerSet = stickerset;
                                        this.centerImage.setImage(ImageLocation.getForDocument(document), null, ImageLocation.getForDocument(FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90), document), null, "webp", this.currentStickerSet, 1);
                                        for (int j = 0; j < document.attributes.size(); ++j) {
                                            final TLRPC.DocumentAttribute documentAttribute2 = document.attributes.get(j);
                                            if (documentAttribute2 instanceof TLRPC.TL_documentAttributeSticker && !TextUtils.isEmpty((CharSequence)documentAttribute2.alt)) {
                                                this.stickerEmojiLayout = new StaticLayout(Emoji.replaceEmoji(documentAttribute2.alt, ContentPreviewViewer.textPaint.getFontMetricsInt(), AndroidUtilities.dp(24.0f), false), ContentPreviewViewer.textPaint, AndroidUtilities.dp(100.0f), Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                                                break;
                                            }
                                        }
                                        break Label_0537;
                                    }
                                }
                            }
                            final TLRPC.InputStickerSet stickerset = null;
                            continue;
                        }
                    }
                    else {
                        if (document != null) {
                            final TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
                            final ImageReceiver centerImage = this.centerImage;
                            final ImageLocation forDocument = ImageLocation.getForDocument(document);
                            final ImageLocation forDocument2 = ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
                            final int size = document.size;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("gif");
                            sb.append(document);
                            centerImage.setImage(forDocument, null, forDocument2, "90_90_b", size, null, sb.toString(), 0);
                        }
                        else {
                            if (botInlineResult == null) {
                                return;
                            }
                            final TLRPC.WebDocument content = botInlineResult.content;
                            if (content == null) {
                                return;
                            }
                            final ImageReceiver centerImage2 = this.centerImage;
                            final ImageLocation forWebFile = ImageLocation.getForWebFile(WebFile.createWithWebDocument(content));
                            final ImageLocation forWebFile2 = ImageLocation.getForWebFile(WebFile.createWithWebDocument(botInlineResult.thumb));
                            final int size2 = botInlineResult.content.size;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("gif");
                            sb2.append(botInlineResult);
                            centerImage2.setImage(forWebFile, null, forWebFile2, "90_90_b", size2, null, sb2.toString(), 1);
                        }
                        AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                        AndroidUtilities.runOnUIThread(this.showSheetRunnable, 2000L);
                    }
                }
                this.currentContentType = currentContentType;
                this.currentDocument = document;
                this.inlineResult = botInlineResult;
                this.containerView.invalidate();
                if (!this.isVisible) {
                    AndroidUtilities.lockOrientation(this.parentActivity);
                    try {
                        if (this.windowView.getParent() != null) {
                            ((WindowManager)this.parentActivity.getSystemService("window")).removeView((View)this.windowView);
                        }
                    }
                    catch (Exception ex2) {
                        FileLog.e(ex2);
                    }
                    ((WindowManager)this.parentActivity.getSystemService("window")).addView((View)this.windowView, (ViewGroup$LayoutParams)this.windowLayoutParams);
                    this.isVisible = true;
                    this.showProgress = 0.0f;
                    this.lastTouchY = -10000.0f;
                    this.currentMoveYProgress = 0.0f;
                    this.finalMoveY = 0.0f;
                    this.currentMoveY = 0.0f;
                    this.moveY = 0.0f;
                    this.lastUpdateTime = System.currentTimeMillis();
                }
            }
        }
    }
    
    public void reset() {
        final Runnable openPreviewRunnable = this.openPreviewRunnable;
        if (openPreviewRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(openPreviewRunnable);
            this.openPreviewRunnable = null;
        }
        final View currentPreviewCell = this.currentPreviewCell;
        if (currentPreviewCell != null) {
            if (currentPreviewCell instanceof StickerEmojiCell) {
                ((StickerEmojiCell)currentPreviewCell).setScaled(false);
            }
            else if (currentPreviewCell instanceof StickerCell) {
                ((StickerCell)currentPreviewCell).setScaled(false);
            }
            else if (currentPreviewCell instanceof ContextLinkCell) {
                ((ContextLinkCell)currentPreviewCell).setScaled(false);
            }
            this.currentPreviewCell = null;
        }
    }
    
    public void setDelegate(final ContentPreviewViewerDelegate delegate) {
        this.delegate = delegate;
    }
    
    public void setKeyboardHeight(final int keyboardHeight) {
        this.keyboardHeight = keyboardHeight;
    }
    
    public void setParentActivity(final Activity parentActivity) {
        this.currentAccount = UserConfig.selectedAccount;
        this.centerImage.setCurrentAccount(this.currentAccount);
        if (this.parentActivity == parentActivity) {
            return;
        }
        this.parentActivity = parentActivity;
        this.slideUpDrawable = this.parentActivity.getResources().getDrawable(2131165779);
        (this.windowView = new FrameLayout((Context)parentActivity)).setFocusable(true);
        this.windowView.setFocusableInTouchMode(true);
        if (Build$VERSION.SDK_INT >= 21) {
            this.windowView.setFitsSystemWindows(true);
            this.windowView.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new _$$Lambda$ContentPreviewViewer$_mc9Jej9PVWpKQwOMkaWMnnQlFE(this));
        }
        (this.containerView = new FrameLayoutDrawer((Context)parentActivity)).setFocusable(false);
        this.windowView.addView((View)this.containerView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.containerView.setOnTouchListener((View$OnTouchListener)new _$$Lambda$ContentPreviewViewer$SmRs4xgfa5hV52_8SV4XVqx9cV4(this));
        this.windowLayoutParams = new WindowManager$LayoutParams();
        final WindowManager$LayoutParams windowLayoutParams = this.windowLayoutParams;
        windowLayoutParams.height = -1;
        windowLayoutParams.format = -3;
        windowLayoutParams.width = -1;
        windowLayoutParams.gravity = 48;
        windowLayoutParams.type = 99;
        if (Build$VERSION.SDK_INT >= 21) {
            windowLayoutParams.flags = -2147417848;
        }
        else {
            windowLayoutParams.flags = 8;
        }
        this.centerImage.setAspectFit(true);
        this.centerImage.setInvalidateAll(true);
        this.centerImage.setParentView((View)this.containerView);
    }
    
    public interface ContentPreviewViewerDelegate
    {
        void gifAddedOrDeleted();
        
        boolean needOpen();
        
        boolean needSend();
        
        void openSet(final TLRPC.InputStickerSet p0, final boolean p1);
        
        void sendGif(final Object p0);
        
        void sendSticker(final TLRPC.Document p0, final Object p1);
    }
    
    private class FrameLayoutDrawer extends FrameLayout
    {
        public FrameLayoutDrawer(final Context context) {
            super(context);
            this.setWillNotDraw(false);
        }
        
        protected void onDraw(final Canvas canvas) {
            ContentPreviewViewer.this.onDraw(canvas);
        }
    }
}
