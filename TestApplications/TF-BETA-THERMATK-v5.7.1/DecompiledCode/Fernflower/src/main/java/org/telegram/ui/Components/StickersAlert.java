package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.text.method.LinkMovementMethod;
import android.util.Property;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.FileRefController;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.ContentPreviewViewer$ContentPreviewViewerDelegate$_CC;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.FeaturedStickerSetInfoCell;
import org.telegram.ui.Cells.StickerEmojiCell;

public class StickersAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
   private StickersAlert.GridAdapter adapter;
   private boolean clearsInputField;
   private StickersAlert.StickersAlertDelegate delegate;
   private FrameLayout emptyView;
   private RecyclerListView gridView;
   private boolean ignoreLayout;
   private TLRPC.InputStickerSet inputStickerSet;
   private StickersAlert.StickersAlertInstallDelegate installDelegate;
   private int itemSize;
   private GridLayoutManager layoutManager;
   private ActionBarMenuItem optionsButton;
   private Activity parentActivity;
   private BaseFragment parentFragment;
   private TextView pickerBottomLayout;
   private ContentPreviewViewer.ContentPreviewViewerDelegate previewDelegate = new ContentPreviewViewer.ContentPreviewViewerDelegate() {
      // $FF: synthetic method
      public void gifAddedOrDeleted() {
         ContentPreviewViewer$ContentPreviewViewerDelegate$_CC.$default$gifAddedOrDeleted(this);
      }

      public boolean needOpen() {
         return false;
      }

      public boolean needSend() {
         boolean var1;
         if (StickersAlert.this.previewSendButton.getVisibility() == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public void openSet(TLRPC.InputStickerSet var1, boolean var2) {
      }

      // $FF: synthetic method
      public void sendGif(Object var1) {
         ContentPreviewViewer$ContentPreviewViewerDelegate$_CC.$default$sendGif(this, var1);
      }

      public void sendSticker(TLRPC.Document var1, Object var2) {
         StickersAlert.this.delegate.onStickerSelected(var1, var2, StickersAlert.this.clearsInputField);
         StickersAlert.this.dismiss();
      }
   };
   private TextView previewSendButton;
   private View previewSendButtonShadow;
   private int reqId;
   private int scrollOffsetY;
   private TLRPC.Document selectedSticker;
   private View[] shadow = new View[2];
   private AnimatorSet[] shadowAnimation = new AnimatorSet[2];
   private Drawable shadowDrawable;
   private boolean showEmoji;
   private TextView stickerEmojiTextView;
   private BackupImageView stickerImageView;
   private FrameLayout stickerPreviewLayout;
   private TLRPC.TL_messages_stickerSet stickerSet;
   private ArrayList stickerSetCovereds;
   private RecyclerListView.OnItemClickListener stickersOnItemClickListener;
   private TextView titleTextView;
   private Pattern urlPattern;

   public StickersAlert(Context var1, Object var2, TLRPC.Photo var3) {
      super(var1, false, 1);
      this.parentActivity = (Activity)var1;
      TLRPC.TL_messages_getAttachedStickers var4 = new TLRPC.TL_messages_getAttachedStickers();
      TLRPC.TL_inputStickeredMediaPhoto var5 = new TLRPC.TL_inputStickeredMediaPhoto();
      var5.id = new TLRPC.TL_inputPhoto();
      TLRPC.InputPhoto var6 = var5.id;
      var6.id = var3.id;
      var6.access_hash = var3.access_hash;
      var6.file_reference = var3.file_reference;
      if (var6.file_reference == null) {
         var6.file_reference = new byte[0];
      }

      var4.media = var5;
      _$$Lambda$StickersAlert$R4T4Ne_ypM57cFmDbPuLpbHC0Ro var7 = new _$$Lambda$StickersAlert$R4T4Ne_ypM57cFmDbPuLpbHC0Ro(this, var4);
      this.reqId = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var4, new _$$Lambda$StickersAlert$70AzTfODxLbeSRDlB8gJ7WKPlnE(this, var2, var4, var7));
      this.init(var1);
   }

   public StickersAlert(Context var1, BaseFragment var2, TLRPC.InputStickerSet var3, TLRPC.TL_messages_stickerSet var4, StickersAlert.StickersAlertDelegate var5) {
      super(var1, false, 1);
      this.delegate = var5;
      this.inputStickerSet = var3;
      this.stickerSet = var4;
      this.parentFragment = var2;
      this.loadStickerSet();
      this.init(var1);
   }

   // $FF: synthetic method
   static int access$1100(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$1300(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$1400(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$1800(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$1900(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2000(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2100(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2200(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2300(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2400(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2600(StickersAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$2700(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2800(StickersAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$2900(StickersAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$3000(StickersAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$3100(StickersAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$3800(StickersAlert var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$500(StickersAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$600(StickersAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   private void hidePreview() {
      AnimatorSet var1 = new AnimatorSet();
      var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.stickerPreviewLayout, View.ALPHA, new float[]{0.0F})});
      var1.setDuration(200L);
      var1.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            StickersAlert.this.stickerPreviewLayout.setVisibility(8);
         }
      });
      var1.start();
   }

   private void init(Context var1) {
      this.shadowDrawable = var1.getResources().getDrawable(2131165824).mutate();
      this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogBackground"), Mode.MULTIPLY));
      super.containerView = new FrameLayout(var1) {
         private boolean fullHeight;
         private int lastNotifyWidth;
         private RectF rect = new RectF();

         protected void onDraw(Canvas var1) {
            int var2;
            int var3;
            int var4;
            int var7;
            float var8;
            label30: {
               int var5;
               int var6;
               float var11;
               label29: {
                  var2 = StickersAlert.this.scrollOffsetY - StickersAlert.access$1800(StickersAlert.this) + AndroidUtilities.dp(6.0F);
                  var3 = StickersAlert.this.scrollOffsetY - StickersAlert.access$1900(StickersAlert.this) - AndroidUtilities.dp(13.0F);
                  var4 = this.getMeasuredHeight() + AndroidUtilities.dp(15.0F) + StickersAlert.access$2000(StickersAlert.this);
                  var5 = var2;
                  var6 = var3;
                  var7 = var4;
                  if (VERSION.SDK_INT >= 21) {
                     var7 = AndroidUtilities.statusBarHeight;
                     var3 += var7;
                     var2 += var7;
                     var4 -= var7;
                     var5 = var2;
                     var6 = var3;
                     var7 = var4;
                     if (this.fullHeight) {
                        var7 = StickersAlert.access$2100(StickersAlert.this);
                        var6 = AndroidUtilities.statusBarHeight;
                        if (var7 + var3 < var6 * 2) {
                           var7 = Math.min(var6, var6 * 2 - var3 - StickersAlert.access$2200(StickersAlert.this));
                           var3 -= var7;
                           var4 += var7;
                           var8 = 1.0F - Math.min(1.0F, (float)(var7 * 2) / (float)AndroidUtilities.statusBarHeight);
                        } else {
                           var8 = 1.0F;
                        }

                        int var9 = StickersAlert.access$2300(StickersAlert.this);
                        int var10 = AndroidUtilities.statusBarHeight;
                        var5 = var2;
                        var6 = var3;
                        var7 = var4;
                        var11 = var8;
                        if (var9 + var3 < var10) {
                           var6 = Math.min(var10, var10 - var3 - StickersAlert.access$2400(StickersAlert.this));
                           var7 = var4;
                           var4 = var6;
                           break label30;
                        }
                        break label29;
                     }
                  }

                  var11 = 1.0F;
               }

               var4 = 0;
               var8 = var11;
               var3 = var6;
               var2 = var5;
            }

            StickersAlert.this.shadowDrawable.setBounds(0, var3, this.getMeasuredWidth(), var7);
            StickersAlert.this.shadowDrawable.draw(var1);
            if (var8 != 1.0F) {
               Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("dialogBackground"));
               this.rect.set((float)StickersAlert.access$2600(StickersAlert.this), (float)(StickersAlert.access$2700(StickersAlert.this) + var3), (float)(this.getMeasuredWidth() - StickersAlert.access$2800(StickersAlert.this)), (float)(StickersAlert.access$2900(StickersAlert.this) + var3 + AndroidUtilities.dp(24.0F)));
               var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(12.0F) * var8, (float)AndroidUtilities.dp(12.0F) * var8, Theme.dialogs_onlineCirclePaint);
            }

            var3 = AndroidUtilities.dp(36.0F);
            this.rect.set((float)((this.getMeasuredWidth() - var3) / 2), (float)var2, (float)((this.getMeasuredWidth() + var3) / 2), (float)(var2 + AndroidUtilities.dp(4.0F)));
            Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("key_sheet_scrollUp"));
            var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), Theme.dialogs_onlineCirclePaint);
            if (var4 > 0) {
               var3 = Theme.getColor("dialogBackground");
               var3 = Color.argb(255, (int)((float)Color.red(var3) * 0.8F), (int)((float)Color.green(var3) * 0.8F), (int)((float)Color.blue(var3) * 0.8F));
               Theme.dialogs_onlineCirclePaint.setColor(var3);
               var1.drawRect((float)StickersAlert.access$3000(StickersAlert.this), (float)(AndroidUtilities.statusBarHeight - var4), (float)(this.getMeasuredWidth() - StickersAlert.access$3100(StickersAlert.this)), (float)AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
            }

         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (var1.getAction() == 0 && StickersAlert.this.scrollOffsetY != 0 && var1.getY() < (float)StickersAlert.this.scrollOffsetY) {
               StickersAlert.this.dismiss();
               return true;
            } else {
               return super.onInterceptTouchEvent(var1);
            }
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            int var6 = this.lastNotifyWidth;
            int var7 = var4 - var2;
            if (var6 != var7) {
               this.lastNotifyWidth = var7;
               if (StickersAlert.this.adapter != null && StickersAlert.this.stickerSetCovereds != null) {
                  StickersAlert.this.adapter.notifyDataSetChanged();
               }
            }

            super.onLayout(var1, var2, var3, var4, var5);
            StickersAlert.this.updateLayout();
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var2);
            var2 = VERSION.SDK_INT;
            boolean var4 = true;
            if (var2 >= 21) {
               StickersAlert.this.ignoreLayout = true;
               this.setPadding(StickersAlert.access$500(StickersAlert.this), AndroidUtilities.statusBarHeight, StickersAlert.access$600(StickersAlert.this), 0);
               StickersAlert.this.ignoreLayout = false;
            }

            this.getMeasuredWidth();
            StickersAlert.this.itemSize = (MeasureSpec.getSize(var1) - AndroidUtilities.dp(36.0F)) / 5;
            int var5;
            if (StickersAlert.this.stickerSetCovereds != null) {
               var5 = AndroidUtilities.dp(56.0F) + AndroidUtilities.dp(60.0F) * StickersAlert.this.stickerSetCovereds.size() + StickersAlert.this.adapter.stickersRowCount * AndroidUtilities.dp(82.0F) + StickersAlert.access$1100(StickersAlert.this);
               var2 = AndroidUtilities.dp(24.0F);
            } else {
               var5 = AndroidUtilities.dp(96.0F);
               if (StickersAlert.this.stickerSet != null) {
                  var2 = (int)Math.ceil((double)((float)StickersAlert.this.stickerSet.documents.size() / 5.0F));
               } else {
                  var2 = 0;
               }

               var5 = var5 + Math.max(3, var2) * AndroidUtilities.dp(82.0F) + StickersAlert.access$1300(StickersAlert.this);
               var2 = AndroidUtilities.statusBarHeight;
            }

            int var6 = var5 + var2;
            double var7 = (double)var6;
            var2 = var3 / 5;
            double var9 = (double)var2;
            Double.isNaN(var9);
            if (var7 < var9 * 3.2D) {
               var5 = 0;
            } else {
               var5 = var2 * 2;
            }

            var2 = var5;
            if (var5 != 0) {
               var2 = var5;
               if (var6 < var3) {
                  var2 = var5 - (var3 - var6);
               }
            }

            var5 = var2;
            if (var2 == 0) {
               var5 = StickersAlert.access$1400(StickersAlert.this);
            }

            var2 = var5;
            if (StickersAlert.this.stickerSetCovereds != null) {
               var2 = var5 + AndroidUtilities.dp(8.0F);
            }

            if (StickersAlert.this.gridView.getPaddingTop() != var2) {
               StickersAlert.this.ignoreLayout = true;
               StickersAlert.this.gridView.setPadding(AndroidUtilities.dp(10.0F), var2, AndroidUtilities.dp(10.0F), 0);
               StickersAlert.this.emptyView.setPadding(0, var2, 0, 0);
               StickersAlert.this.ignoreLayout = false;
            }

            if (var6 < var3) {
               var4 = false;
            }

            this.fullHeight = var4;
            super.onMeasure(var1, MeasureSpec.makeMeasureSpec(Math.min(var6, var3), 1073741824));
         }

         public boolean onTouchEvent(MotionEvent var1) {
            boolean var2;
            if (!StickersAlert.this.isDismissed() && super.onTouchEvent(var1)) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }

         public void requestLayout() {
            if (!StickersAlert.this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      super.containerView.setWillNotDraw(false);
      ViewGroup var2 = super.containerView;
      int var3 = super.backgroundPaddingLeft;
      var2.setPadding(var3, 0, var3, 0);
      LayoutParams var5 = new LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
      var5.topMargin = AndroidUtilities.dp(48.0F);
      this.shadow[0] = new View(var1);
      this.shadow[0].setBackgroundColor(Theme.getColor("dialogShadowLine"));
      this.shadow[0].setAlpha(0.0F);
      this.shadow[0].setVisibility(4);
      this.shadow[0].setTag(1);
      super.containerView.addView(this.shadow[0], var5);
      this.gridView = new RecyclerListView(var1) {
         public boolean onInterceptTouchEvent(MotionEvent var1) {
            ContentPreviewViewer var2 = ContentPreviewViewer.getInstance();
            RecyclerListView var3 = StickersAlert.this.gridView;
            ContentPreviewViewer.ContentPreviewViewerDelegate var4 = StickersAlert.this.previewDelegate;
            boolean var5 = false;
            boolean var6 = var2.onInterceptTouchEvent(var1, var3, 0, var4);
            if (super.onInterceptTouchEvent(var1) || var6) {
               var5 = true;
            }

            return var5;
         }

         public void requestLayout() {
            if (!StickersAlert.this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      this.gridView.setTag(14);
      RecyclerListView var4 = this.gridView;
      GridLayoutManager var6 = new GridLayoutManager(this.getContext(), 5);
      this.layoutManager = var6;
      var4.setLayoutManager(var6);
      this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
         public int getSpanSize(int var1) {
            return (StickersAlert.this.stickerSetCovereds == null || !(StickersAlert.this.adapter.cache.get(var1) instanceof Integer)) && var1 != StickersAlert.this.adapter.totalItems ? 1 : StickersAlert.this.adapter.stickersPerRow;
         }
      });
      var4 = this.gridView;
      StickersAlert.GridAdapter var7 = new StickersAlert.GridAdapter(var1);
      this.adapter = var7;
      var4.setAdapter(var7);
      this.gridView.setVerticalScrollBarEnabled(false);
      this.gridView.addItemDecoration(new RecyclerView.ItemDecoration() {
         public void getItemOffsets(android.graphics.Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
            var1.left = 0;
            var1.right = 0;
            var1.bottom = 0;
            var1.top = 0;
         }
      });
      this.gridView.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
      this.gridView.setClipToPadding(false);
      this.gridView.setEnabled(true);
      this.gridView.setGlowColor(Theme.getColor("dialogScrollGlow"));
      this.gridView.setOnTouchListener(new _$$Lambda$StickersAlert$_nwA8t_8QJfVh6HJUgTR9__hiGc(this));
      this.gridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            StickersAlert.this.updateLayout();
         }
      });
      this.stickersOnItemClickListener = new _$$Lambda$StickersAlert$pqY8OpOZq71Xrxq3rh8h2TYv4Gk(this);
      this.gridView.setOnItemClickListener(this.stickersOnItemClickListener);
      super.containerView.addView(this.gridView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 48.0F, 0.0F, 48.0F));
      this.emptyView = new FrameLayout(var1) {
         public void requestLayout() {
            if (!StickersAlert.this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      super.containerView.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
      this.gridView.setEmptyView(this.emptyView);
      this.emptyView.setOnTouchListener(_$$Lambda$StickersAlert$x4wnZ8iSeVTFndyEhtM4m12j_W8.INSTANCE);
      this.titleTextView = new TextView(var1);
      this.titleTextView.setLines(1);
      this.titleTextView.setSingleLine(true);
      this.titleTextView.setTextColor(Theme.getColor("dialogTextBlack"));
      this.titleTextView.setTextSize(1, 20.0F);
      this.titleTextView.setLinkTextColor(Theme.getColor("dialogTextLink"));
      this.titleTextView.setHighlightColor(Theme.getColor("dialogLinkSelection"));
      this.titleTextView.setEllipsize(TruncateAt.END);
      this.titleTextView.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      this.titleTextView.setGravity(16);
      this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      super.containerView.addView(this.titleTextView, LayoutHelper.createFrame(-1, 50.0F, 51, 0.0F, 0.0F, 40.0F, 0.0F));
      this.optionsButton = new ActionBarMenuItem(var1, (ActionBarMenu)null, 0, Theme.getColor("key_sheet_other"));
      this.optionsButton.setLongClickEnabled(false);
      this.optionsButton.setSubMenuOpenSide(2);
      this.optionsButton.setIcon(2131165416);
      this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("player_actionBarSelector"), 1));
      super.containerView.addView(this.optionsButton, LayoutHelper.createFrame(40, 40.0F, 53, 0.0F, 5.0F, 5.0F, 0.0F));
      this.optionsButton.addSubItem(1, 2131165671, LocaleController.getString("StickersShare", 2131560813));
      this.optionsButton.addSubItem(2, 2131165640, LocaleController.getString("CopyLink", 2131559164));
      this.optionsButton.setOnClickListener(new _$$Lambda$StickersAlert$7978RXQKfGxYvNzfCRh5mokiXDU(this));
      this.optionsButton.setDelegate(new _$$Lambda$StickersAlert$9rn6i7qVo7Tr6wYcNUHGgSRyKIM(this));
      this.optionsButton.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131558443));
      ActionBarMenuItem var8 = this.optionsButton;
      byte var9;
      if (this.inputStickerSet != null) {
         var9 = 0;
      } else {
         var9 = 8;
      }

      var8.setVisibility(var9);
      RadialProgressView var10 = new RadialProgressView(var1);
      this.emptyView.addView(var10, LayoutHelper.createFrame(-2, -2, 17));
      var5 = new LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
      var5.bottomMargin = AndroidUtilities.dp(48.0F);
      this.shadow[1] = new View(var1);
      this.shadow[1].setBackgroundColor(Theme.getColor("dialogShadowLine"));
      super.containerView.addView(this.shadow[1], var5);
      this.pickerBottomLayout = new TextView(var1);
      this.pickerBottomLayout.setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("dialogBackground"), Theme.getColor("listSelectorSDK21")));
      this.pickerBottomLayout.setTextColor(Theme.getColor("dialogTextBlue2"));
      this.pickerBottomLayout.setTextSize(1, 14.0F);
      this.pickerBottomLayout.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
      this.pickerBottomLayout.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.pickerBottomLayout.setGravity(17);
      super.containerView.addView(this.pickerBottomLayout, LayoutHelper.createFrame(-1, 48, 83));
      this.stickerPreviewLayout = new FrameLayout(var1);
      this.stickerPreviewLayout.setBackgroundColor(Theme.getColor("dialogBackground") & -536870913);
      this.stickerPreviewLayout.setVisibility(8);
      this.stickerPreviewLayout.setSoundEffectsEnabled(false);
      super.containerView.addView(this.stickerPreviewLayout, LayoutHelper.createFrame(-1, -1.0F));
      this.stickerPreviewLayout.setOnClickListener(new _$$Lambda$StickersAlert$nsigcALZ_A8eXyilWi1cmTZCn2o(this));
      this.stickerImageView = new BackupImageView(var1);
      this.stickerImageView.setAspectFit(true);
      this.stickerPreviewLayout.addView(this.stickerImageView);
      this.stickerEmojiTextView = new TextView(var1);
      this.stickerEmojiTextView.setTextSize(1, 30.0F);
      this.stickerEmojiTextView.setGravity(85);
      this.stickerPreviewLayout.addView(this.stickerEmojiTextView);
      this.previewSendButton = new TextView(var1);
      this.previewSendButton.setTextSize(1, 14.0F);
      this.previewSendButton.setTextColor(Theme.getColor("dialogTextBlue2"));
      this.previewSendButton.setGravity(17);
      this.previewSendButton.setBackgroundColor(Theme.getColor("dialogBackground"));
      this.previewSendButton.setPadding(AndroidUtilities.dp(29.0F), 0, AndroidUtilities.dp(29.0F), 0);
      this.previewSendButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.stickerPreviewLayout.addView(this.previewSendButton, LayoutHelper.createFrame(-1, 48, 83));
      this.previewSendButton.setOnClickListener(new _$$Lambda$StickersAlert$BjajSs__vXEFUr5UZfKatrtPlkU(this));
      var5 = new LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
      var5.bottomMargin = AndroidUtilities.dp(48.0F);
      this.previewSendButtonShadow = new View(var1);
      this.previewSendButtonShadow.setBackgroundColor(Theme.getColor("dialogShadowLine"));
      this.stickerPreviewLayout.addView(this.previewSendButtonShadow, var5);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      this.updateFields();
      this.updateSendButton();
      this.adapter.notifyDataSetChanged();
   }

   // $FF: synthetic method
   public static void lambda$9rn6i7qVo7Tr6wYcNUHGgSRyKIM(StickersAlert var0, int var1) {
      var0.onSubItemClick(var1);
   }

   // $FF: synthetic method
   static boolean lambda$init$7(View var0, MotionEvent var1) {
      return true;
   }

   private void loadStickerSet() {
      TLRPC.InputStickerSet var1 = this.inputStickerSet;
      if (var1 != null) {
         if (this.stickerSet == null && var1.short_name != null) {
            this.stickerSet = DataQuery.getInstance(super.currentAccount).getStickerSetByName(this.inputStickerSet.short_name);
         }

         if (this.stickerSet == null) {
            this.stickerSet = DataQuery.getInstance(super.currentAccount).getStickerSetById(this.inputStickerSet.id);
         }

         if (this.stickerSet == null) {
            TLRPC.TL_messages_getStickerSet var2 = new TLRPC.TL_messages_getStickerSet();
            var2.stickerset = this.inputStickerSet;
            ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$StickersAlert$dpylIMqKi3ssdFN3dGRn2NGUFco(this));
         } else if (this.adapter != null) {
            this.updateSendButton();
            this.updateFields();
            this.adapter.notifyDataSetChanged();
         }
      }

      TLRPC.TL_messages_stickerSet var3 = this.stickerSet;
      if (var3 != null) {
         this.showEmoji = var3.set.masks ^ true;
      }

   }

   private void onSubItemClick(int var1) {
      if (this.stickerSet != null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("https://");
         var2.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
         var2.append("/addstickers/");
         var2.append(this.stickerSet.set.short_name);
         String var5 = var2.toString();
         if (var1 == 1) {
            ShareAlert var6 = new ShareAlert(this.getContext(), (ArrayList)null, var5, false, var5, false);
            BaseFragment var3 = this.parentFragment;
            if (var3 != null) {
               var3.showDialog(var6);
            } else {
               var6.show();
            }
         } else if (var1 == 2) {
            try {
               AndroidUtilities.addToClipboard(var5);
               Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("LinkCopied", 2131559751), 0).show();
            } catch (Exception var4) {
               FileLog.e((Throwable)var4);
            }
         }

      }
   }

   private void runShadowAnimation(final int var1, final boolean var2) {
      if (this.stickerSetCovereds == null) {
         if (var2 && this.shadow[var1].getTag() != null || !var2 && this.shadow[var1].getTag() == null) {
            View var3 = this.shadow[var1];
            Integer var4;
            if (var2) {
               var4 = null;
            } else {
               var4 = 1;
            }

            var3.setTag(var4);
            if (var2) {
               this.shadow[var1].setVisibility(0);
            }

            AnimatorSet[] var8 = this.shadowAnimation;
            if (var8[var1] != null) {
               var8[var1].cancel();
            }

            this.shadowAnimation[var1] = new AnimatorSet();
            AnimatorSet var7 = this.shadowAnimation[var1];
            View var9 = this.shadow[var1];
            Property var5 = View.ALPHA;
            float var6;
            if (var2) {
               var6 = 1.0F;
            } else {
               var6 = 0.0F;
            }

            var7.playTogether(new Animator[]{ObjectAnimator.ofFloat(var9, var5, new float[]{var6})});
            this.shadowAnimation[var1].setDuration(150L);
            this.shadowAnimation[var1].addListener(new AnimatorListenerAdapter() {
               public void onAnimationCancel(Animator var1x) {
                  if (StickersAlert.this.shadowAnimation[var1] != null && StickersAlert.this.shadowAnimation[var1].equals(var1x)) {
                     StickersAlert.this.shadowAnimation[var1] = null;
                  }

               }

               public void onAnimationEnd(Animator var1x) {
                  if (StickersAlert.this.shadowAnimation[var1] != null && StickersAlert.this.shadowAnimation[var1].equals(var1x)) {
                     if (!var2) {
                        StickersAlert.this.shadow[var1].setVisibility(4);
                     }

                     StickersAlert.this.shadowAnimation[var1] = null;
                  }

               }
            });
            this.shadowAnimation[var1].start();
         }

      }
   }

   private void setButton(OnClickListener var1, String var2, int var3) {
      this.pickerBottomLayout.setTextColor(var3);
      this.pickerBottomLayout.setText(var2.toUpperCase());
      this.pickerBottomLayout.setOnClickListener(var1);
   }

   private void updateFields() {
      if (this.titleTextView != null) {
         String var26;
         if (this.stickerSet != null) {
            TextView var20;
            Object var22;
            label154: {
               SpannableStringBuilder var2;
               Exception var4;
               label161: {
                  Matcher var1;
                  Exception var10000;
                  boolean var10001;
                  label152: {
                     label151: {
                        try {
                           if (this.urlPattern == null) {
                              this.urlPattern = Pattern.compile("@[a-zA-Z\\d_]{1,32}");
                           }
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label151;
                        }

                        try {
                           var1 = this.urlPattern.matcher(this.stickerSet.set.title);
                           break label152;
                        } catch (Exception var18) {
                           var10000 = var18;
                           var10001 = false;
                        }
                     }

                     var4 = var10000;
                     var2 = null;
                     break label161;
                  }

                  var2 = null;

                  while(true) {
                     SpannableStringBuilder var3 = var2;
                     var22 = var2;

                     SpannableStringBuilder var24;
                     label140: {
                        label163: {
                           try {
                              if (!var1.find()) {
                                 break label154;
                              }
                           } catch (Exception var17) {
                              var10000 = var17;
                              var10001 = false;
                              break label163;
                           }

                           var24 = var2;
                           if (var2 == null) {
                              var3 = var2;

                              try {
                                 var24 = new SpannableStringBuilder;
                              } catch (Exception var16) {
                                 var10000 = var16;
                                 var10001 = false;
                                 break label163;
                              }

                              var3 = var2;

                              try {
                                 var24.<init>(this.stickerSet.set.title);
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label163;
                              }

                              try {
                                 var20 = this.titleTextView;
                                 StickersAlert.LinkMovementMethodMy var23 = new StickersAlert.LinkMovementMethodMy();
                                 var20.setMovementMethod(var23);
                              } catch (Exception var8) {
                                 var3 = var24;
                                 var4 = var8;
                                 var2 = var3;
                                 break;
                              }
                           }

                           var3 = var24;

                           int var5;
                           try {
                              var5 = var1.start();
                           } catch (Exception var14) {
                              var10000 = var14;
                              var10001 = false;
                              break label163;
                           }

                           var3 = var24;

                           int var6;
                           try {
                              var6 = var1.end();
                           } catch (Exception var13) {
                              var10000 = var13;
                              var10001 = false;
                              break label163;
                           }

                           int var7 = var5;
                           var3 = var24;

                           label117: {
                              try {
                                 if (this.stickerSet.set.title.charAt(var5) == '@') {
                                    break label117;
                                 }
                              } catch (Exception var12) {
                                 var10000 = var12;
                                 var10001 = false;
                                 break label163;
                              }

                              var7 = var5 + 1;
                           }

                           var3 = var24;

                           URLSpanNoUnderline var21;
                           try {
                              var21 = new URLSpanNoUnderline() {
                                 public void onClick(View var1) {
                                    MessagesController.getInstance(StickersAlert.access$3800(StickersAlert.this)).openByUserName(this.getURL(), StickersAlert.this.parentFragment, 1);
                                    StickersAlert.this.dismiss();
                                 }
                              };
                           } catch (Exception var11) {
                              var10000 = var11;
                              var10001 = false;
                              break label163;
                           }

                           var3 = var24;

                           try {
                              var21.<init>(this.stickerSet.set.title.subSequence(var7 + 1, var6).toString());
                           } catch (Exception var10) {
                              var10000 = var10;
                              var10001 = false;
                              break label163;
                           }

                           var3 = var24;

                           try {
                              var24.setSpan(var21, var7, var6, 0);
                              break label140;
                           } catch (Exception var9) {
                              var10000 = var9;
                              var10001 = false;
                           }
                        }

                        var4 = var10000;
                        var2 = var3;
                        break;
                     }

                     var2 = var24;
                  }
               }

               FileLog.e((Throwable)var4);
               var22 = var2;
            }

            var20 = this.titleTextView;
            if (var22 == null) {
               var22 = this.stickerSet.set.title;
            }

            var20.setText((CharSequence)var22);
            TLRPC.TL_messages_stickerSet var25;
            if (this.stickerSet.set != null && DataQuery.getInstance(super.currentAccount).isStickerPackInstalled(this.stickerSet.set.id)) {
               var25 = this.stickerSet;
               if (var25.set.masks) {
                  var26 = LocaleController.formatString("RemoveStickersCount", 2131560554, LocaleController.formatPluralString("MasksCount", var25.documents.size())).toUpperCase();
               } else {
                  var26 = LocaleController.formatString("RemoveStickersCount", 2131560554, LocaleController.formatPluralString("Stickers", var25.documents.size())).toUpperCase();
               }

               if (this.stickerSet.set.official) {
                  this.setButton(new _$$Lambda$StickersAlert$DGFO8T79ri5mrUocraZaq3nn2Bk(this), var26, Theme.getColor("dialogTextRed"));
               } else {
                  this.setButton(new _$$Lambda$StickersAlert$2Q0RkK5JO2x7zurWeDAdNpHEWcg(this), var26, Theme.getColor("dialogTextRed"));
               }
            } else {
               var25 = this.stickerSet;
               if (var25.set.masks) {
                  var26 = LocaleController.formatString("AddStickersCount", 2131558585, LocaleController.formatPluralString("MasksCount", var25.documents.size())).toUpperCase();
               } else {
                  var26 = LocaleController.formatString("AddStickersCount", 2131558585, LocaleController.formatPluralString("Stickers", var25.documents.size())).toUpperCase();
               }

               this.setButton(new _$$Lambda$StickersAlert$TDKayuR9rZfu7Rq3IHs_62RtgkQ(this), var26, Theme.getColor("dialogTextBlue2"));
            }

            this.adapter.notifyDataSetChanged();
         } else {
            var26 = LocaleController.getString("Close", 2131559117).toUpperCase();
            this.setButton(new _$$Lambda$StickersAlert$v5kDf2Uds3Mnng7wUMVzZRK7fiE(this), var26, Theme.getColor("dialogTextBlue2"));
         }

      }
   }

   @SuppressLint({"NewApi"})
   private void updateLayout() {
      int var2;
      RecyclerListView var4;
      if (this.gridView.getChildCount() <= 0) {
         var4 = this.gridView;
         var2 = var4.getPaddingTop();
         this.scrollOffsetY = var2;
         var4.setTopGlowOffset(var2);
         if (this.stickerSetCovereds == null) {
            this.titleTextView.setTranslationY((float)this.scrollOffsetY);
            this.optionsButton.setTranslationY((float)this.scrollOffsetY);
            this.shadow[0].setTranslationY((float)this.scrollOffsetY);
         }

         super.containerView.invalidate();
      } else {
         View var3 = this.gridView.getChildAt(0);
         RecyclerListView.Holder var1 = (RecyclerListView.Holder)this.gridView.findContainingViewHolder(var3);
         var2 = var3.getTop();
         if (var2 >= 0 && var1 != null && var1.getAdapterPosition() == 0) {
            this.runShadowAnimation(0, false);
         } else {
            this.runShadowAnimation(0, true);
            var2 = 0;
         }

         if (this.scrollOffsetY != var2) {
            var4 = this.gridView;
            this.scrollOffsetY = var2;
            var4.setTopGlowOffset(var2);
            if (this.stickerSetCovereds == null) {
               this.titleTextView.setTranslationY((float)this.scrollOffsetY);
               this.optionsButton.setTranslationY((float)this.scrollOffsetY);
               this.shadow[0].setTranslationY((float)this.scrollOffsetY);
            }

            super.containerView.invalidate();
         }

      }
   }

   private void updateSendButton() {
      android.graphics.Point var1 = AndroidUtilities.displaySize;
      int var2 = (int)((float)(Math.min(var1.x, var1.y) / 2) / AndroidUtilities.density);
      if (this.delegate != null) {
         TLRPC.TL_messages_stickerSet var4 = this.stickerSet;
         if (var4 == null || !var4.set.masks) {
            this.previewSendButton.setText(LocaleController.getString("SendSticker", 2131560707).toUpperCase());
            BackupImageView var5 = this.stickerImageView;
            float var3 = (float)var2;
            var5.setLayoutParams(LayoutHelper.createFrame(var2, var3, 17, 0.0F, 0.0F, 0.0F, 30.0F));
            this.stickerEmojiTextView.setLayoutParams(LayoutHelper.createFrame(var2, var3, 17, 0.0F, 0.0F, 0.0F, 30.0F));
            this.previewSendButton.setVisibility(0);
            this.previewSendButtonShadow.setVisibility(0);
            return;
         }
      }

      this.previewSendButton.setText(LocaleController.getString("Close", 2131559117).toUpperCase());
      this.stickerImageView.setLayoutParams(LayoutHelper.createFrame(var2, var2, 17));
      this.stickerEmojiTextView.setLayoutParams(LayoutHelper.createFrame(var2, var2, 17));
      this.previewSendButton.setVisibility(8);
      this.previewSendButtonShadow.setVisibility(8);
   }

   protected boolean canDismissWithSwipe() {
      return false;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.emojiDidLoad) {
         RecyclerListView var4 = this.gridView;
         if (var4 != null) {
            var2 = var4.getChildCount();

            for(var1 = 0; var1 < var2; ++var1) {
               this.gridView.getChildAt(var1).invalidate();
            }
         }

         if (ContentPreviewViewer.getInstance().isVisible()) {
            ContentPreviewViewer.getInstance().close();
         }

         ContentPreviewViewer.getInstance().reset();
      }

   }

   public void dismiss() {
      super.dismiss();
      if (this.reqId != 0) {
         ConnectionsManager.getInstance(super.currentAccount).cancelRequest(this.reqId, true);
         this.reqId = 0;
      }

      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
   }

   public boolean isClearsInputField() {
      return this.clearsInputField;
   }

   // $FF: synthetic method
   public void lambda$init$10$StickersAlert(View var1) {
      this.delegate.onStickerSelected(this.selectedSticker, this.stickerSet, this.clearsInputField);
      this.dismiss();
   }

   // $FF: synthetic method
   public boolean lambda$init$5$StickersAlert(View var1, MotionEvent var2) {
      return ContentPreviewViewer.getInstance().onTouch(var2, this.gridView, 0, this.stickersOnItemClickListener, this.previewDelegate);
   }

   // $FF: synthetic method
   public void lambda$init$6$StickersAlert(View var1, int var2) {
      if (this.stickerSetCovereds != null) {
         TLRPC.StickerSetCovered var3 = (TLRPC.StickerSetCovered)this.adapter.positionsToSets.get(var2);
         if (var3 != null) {
            this.dismiss();
            TLRPC.TL_inputStickerSetID var4 = new TLRPC.TL_inputStickerSetID();
            TLRPC.StickerSet var11 = var3.set;
            var4.access_hash = var11.access_hash;
            var4.id = var11.id;
            (new StickersAlert(this.parentActivity, this.parentFragment, var4, (TLRPC.TL_messages_stickerSet)null, (StickersAlert.StickersAlertDelegate)null)).show();
         }
      } else {
         TLRPC.TL_messages_stickerSet var5 = this.stickerSet;
         if (var5 != null && var2 >= 0 && var2 < var5.documents.size()) {
            this.selectedSticker = (TLRPC.Document)this.stickerSet.documents.get(var2);

            boolean var9;
            label35: {
               for(var2 = 0; var2 < this.selectedSticker.attributes.size(); ++var2) {
                  TLRPC.DocumentAttribute var6 = (TLRPC.DocumentAttribute)this.selectedSticker.attributes.get(var2);
                  if (var6 instanceof TLRPC.TL_documentAttributeSticker) {
                     String var12 = var6.alt;
                     if (var12 != null && var12.length() > 0) {
                        TextView var13 = this.stickerEmojiTextView;
                        var13.setText(Emoji.replaceEmoji(var6.alt, var13.getPaint().getFontMetricsInt(), AndroidUtilities.dp(30.0F), false));
                        var9 = true;
                        break label35;
                     }
                     break;
                  }
               }

               var9 = false;
            }

            if (!var9) {
               this.stickerEmojiTextView.setText(Emoji.replaceEmoji(DataQuery.getInstance(super.currentAccount).getEmojiForSticker(this.selectedSticker.id), this.stickerEmojiTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(30.0F), false));
            }

            TLRPC.PhotoSize var7 = FileLoader.getClosestPhotoSizeWithSize(this.selectedSticker.thumbs, 90);
            this.stickerImageView.getImageReceiver().setImage(ImageLocation.getForDocument(this.selectedSticker), (String)null, ImageLocation.getForDocument(var7, this.selectedSticker), (String)null, "webp", this.stickerSet, 1);
            LayoutParams var8 = (LayoutParams)this.stickerPreviewLayout.getLayoutParams();
            var8.topMargin = this.scrollOffsetY;
            this.stickerPreviewLayout.setLayoutParams(var8);
            this.stickerPreviewLayout.setVisibility(0);
            AnimatorSet var10 = new AnimatorSet();
            var10.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.stickerPreviewLayout, View.ALPHA, new float[]{0.0F, 1.0F})});
            var10.setDuration(200L);
            var10.start();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$init$8$StickersAlert(View var1) {
      this.optionsButton.toggleSubMenu();
   }

   // $FF: synthetic method
   public void lambda$init$9$StickersAlert(View var1) {
      this.hidePreview();
   }

   // $FF: synthetic method
   public void lambda$loadStickerSet$4$StickersAlert(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$StickersAlert$6dF2CqzwWOiq_mtWdyHxV9DIihc(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$new$1$StickersAlert(TLRPC.TL_messages_getAttachedStickers var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$StickersAlert$NU6Lj7s7oms1kuoHZ_7sXpiftZ4(this, var3, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$new$2$StickersAlert(Object var1, TLRPC.TL_messages_getAttachedStickers var2, RequestDelegate var3, TLObject var4, TLRPC.TL_error var5) {
      if (var5 != null && FileRefController.isFileRefError(var5.text) && var1 != null) {
         FileRefController.getInstance(super.currentAccount).requestReference(var1, var2, var3);
      } else {
         var3.run(var4, var5);
      }
   }

   // $FF: synthetic method
   public void lambda$null$0$StickersAlert(TLRPC.TL_error var1, TLObject var2, TLRPC.TL_messages_getAttachedStickers var3) {
      this.reqId = 0;
      if (var1 == null) {
         TLRPC.Vector var5 = (TLRPC.Vector)var2;
         if (var5.objects.isEmpty()) {
            this.dismiss();
         } else if (var5.objects.size() == 1) {
            TLRPC.StickerSetCovered var7 = (TLRPC.StickerSetCovered)var5.objects.get(0);
            this.inputStickerSet = new TLRPC.TL_inputStickerSetID();
            TLRPC.InputStickerSet var6 = this.inputStickerSet;
            TLRPC.StickerSet var8 = var7.set;
            var6.id = var8.id;
            var6.access_hash = var8.access_hash;
            this.loadStickerSet();
         } else {
            this.stickerSetCovereds = new ArrayList();

            for(int var4 = 0; var4 < var5.objects.size(); ++var4) {
               this.stickerSetCovereds.add((TLRPC.StickerSetCovered)var5.objects.get(var4));
            }

            this.gridView.setLayoutParams(LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
            this.titleTextView.setVisibility(8);
            this.shadow[0].setVisibility(8);
            this.adapter.notifyDataSetChanged();
         }
      } else {
         AlertsCreator.processError(super.currentAccount, var1, this.parentFragment, var3);
         this.dismiss();
      }

   }

   // $FF: synthetic method
   public void lambda$null$11$StickersAlert(TLRPC.TL_error var1, TLObject var2) {
      label46: {
         Exception var10000;
         boolean var10001;
         if (var1 == null) {
            label42: {
               label48: {
                  try {
                     if (this.stickerSet.set.masks) {
                        Toast.makeText(this.getContext(), LocaleController.getString("AddMasksInstalled", 2131558572), 0).show();
                        break label48;
                     }
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                     break label42;
                  }

                  try {
                     Toast.makeText(this.getContext(), LocaleController.getString("AddStickersInstalled", 2131558586), 0).show();
                  } catch (Exception var4) {
                     var10000 = var4;
                     var10001 = false;
                     break label42;
                  }
               }

               try {
                  if (var2 instanceof TLRPC.TL_messages_stickerSetInstallResultArchive) {
                     NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.needReloadArchivedStickers);
                     if (this.parentFragment != null && this.parentFragment.getParentActivity() != null) {
                        StickersArchiveAlert var7 = new StickersArchiveAlert(this.parentFragment.getParentActivity(), this.parentFragment, ((TLRPC.TL_messages_stickerSetInstallResultArchive)var2).sets);
                        this.parentFragment.showDialog(var7.create());
                     }
                  }
                  break label46;
               } catch (Exception var3) {
                  var10000 = var3;
                  var10001 = false;
               }
            }
         } else {
            try {
               Toast.makeText(this.getContext(), LocaleController.getString("ErrorOccurred", 2131559375), 0).show();
               break label46;
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         }

         Exception var8 = var10000;
         FileLog.e((Throwable)var8);
      }

      DataQuery.getInstance(super.currentAccount).loadStickers(this.stickerSet.set.masks, false, true);
   }

   // $FF: synthetic method
   public void lambda$null$12$StickersAlert(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$StickersAlert$u0aZ0wOaqO_c5AszLHG59q2qf_I(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$3$StickersAlert(TLRPC.TL_error var1, TLObject var2) {
      this.reqId = 0;
      if (var1 == null) {
         this.optionsButton.setVisibility(0);
         this.stickerSet = (TLRPC.TL_messages_stickerSet)var2;
         this.showEmoji = this.stickerSet.set.masks ^ true;
         this.updateSendButton();
         this.updateFields();
         this.adapter.notifyDataSetChanged();
      } else {
         Toast.makeText(this.getContext(), LocaleController.getString("AddStickersNotFound", 2131558587), 0).show();
         this.dismiss();
      }

   }

   // $FF: synthetic method
   public void lambda$updateFields$13$StickersAlert(View var1) {
      this.dismiss();
      StickersAlert.StickersAlertInstallDelegate var2 = this.installDelegate;
      if (var2 != null) {
         var2.onStickerSetInstalled();
      }

      TLRPC.TL_messages_installStickerSet var3 = new TLRPC.TL_messages_installStickerSet();
      var3.stickerset = this.inputStickerSet;
      ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$StickersAlert$93kNqcLfjawvVuCFuwipI3J4db8(this));
   }

   // $FF: synthetic method
   public void lambda$updateFields$14$StickersAlert(View var1) {
      StickersAlert.StickersAlertInstallDelegate var2 = this.installDelegate;
      if (var2 != null) {
         var2.onStickerSetUninstalled();
      }

      this.dismiss();
      DataQuery.getInstance(super.currentAccount).removeStickersSet(this.getContext(), this.stickerSet.set, 1, this.parentFragment, true);
   }

   // $FF: synthetic method
   public void lambda$updateFields$15$StickersAlert(View var1) {
      StickersAlert.StickersAlertInstallDelegate var2 = this.installDelegate;
      if (var2 != null) {
         var2.onStickerSetUninstalled();
      }

      this.dismiss();
      DataQuery.getInstance(super.currentAccount).removeStickersSet(this.getContext(), this.stickerSet.set, 0, this.parentFragment, true);
   }

   // $FF: synthetic method
   public void lambda$updateFields$16$StickersAlert(View var1) {
      this.dismiss();
   }

   public void setClearsInputField(boolean var1) {
      this.clearsInputField = var1;
   }

   public void setInstallDelegate(StickersAlert.StickersAlertInstallDelegate var1) {
      this.installDelegate = var1;
   }

   private class GridAdapter extends RecyclerListView.SelectionAdapter {
      private SparseArray cache = new SparseArray();
      private Context context;
      private SparseArray positionsToSets = new SparseArray();
      private int stickersPerRow;
      private int stickersRowCount;
      private int totalItems;

      public GridAdapter(Context var2) {
         this.context = var2;
      }

      public int getItemCount() {
         return this.totalItems;
      }

      public int getItemViewType(int var1) {
         if (StickersAlert.this.stickerSetCovereds != null) {
            Object var2 = this.cache.get(var1);
            if (var2 != null) {
               return var2 instanceof TLRPC.Document ? 0 : 2;
            } else {
               return 1;
            }
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void notifyDataSetChanged() {
         ArrayList var1 = StickersAlert.this.stickerSetCovereds;
         int var2 = 0;
         if (var1 != null) {
            int var3 = StickersAlert.this.gridView.getMeasuredWidth();
            var2 = var3;
            if (var3 == 0) {
               var2 = AndroidUtilities.displaySize.x;
            }

            this.stickersPerRow = var2 / AndroidUtilities.dp(72.0F);
            StickersAlert.this.layoutManager.setSpanCount(this.stickersPerRow);
            this.cache.clear();
            this.positionsToSets.clear();
            this.totalItems = 0;
            this.stickersRowCount = 0;

            for(var2 = 0; var2 < StickersAlert.this.stickerSetCovereds.size(); ++var2) {
               TLRPC.StickerSetCovered var11 = (TLRPC.StickerSetCovered)StickersAlert.this.stickerSetCovereds.get(var2);
               if (!var11.covers.isEmpty() || var11.cover != null) {
                  double var4 = (double)this.stickersRowCount;
                  double var6 = Math.ceil((double)((float)StickersAlert.this.stickerSetCovereds.size() / (float)this.stickersPerRow));
                  Double.isNaN(var4);
                  this.stickersRowCount = (int)(var4 + var6);
                  this.positionsToSets.put(this.totalItems, var11);
                  SparseArray var8 = this.cache;
                  var3 = this.totalItems++;
                  var8.put(var3, var2);
                  int var10000 = this.totalItems / this.stickersPerRow;
                  int var9;
                  int var10;
                  if (!var11.covers.isEmpty()) {
                     var9 = (int)Math.ceil((double)((float)var11.covers.size() / (float)this.stickersPerRow));
                     var10 = 0;

                     while(true) {
                        var3 = var9;
                        if (var10 >= var11.covers.size()) {
                           break;
                        }

                        this.cache.put(this.totalItems + var10, var11.covers.get(var10));
                        ++var10;
                     }
                  } else {
                     this.cache.put(this.totalItems, var11.cover);
                     var3 = 1;
                  }

                  var10 = 0;

                  while(true) {
                     var9 = this.stickersPerRow;
                     if (var10 >= var3 * var9) {
                        this.totalItems += var3 * var9;
                        break;
                     }

                     this.positionsToSets.put(this.totalItems + var10, var11);
                     ++var10;
                  }
               }
            }
         } else {
            if (StickersAlert.this.stickerSet != null) {
               var2 = StickersAlert.this.stickerSet.documents.size();
            }

            this.totalItems = var2;
         }

         super.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (StickersAlert.this.stickerSetCovereds != null) {
            int var3 = var1.getItemViewType();
            if (var3 != 0) {
               if (var3 != 1) {
                  if (var3 == 2) {
                     TLRPC.StickerSetCovered var4 = (TLRPC.StickerSetCovered)StickersAlert.this.stickerSetCovereds.get((Integer)this.cache.get(var2));
                     ((FeaturedStickerSetInfoCell)var1.itemView).setStickerSet(var4, false);
                  }
               } else {
                  ((EmptyCell)var1.itemView).setHeight(AndroidUtilities.dp(82.0F));
               }
            } else {
               TLRPC.Document var5 = (TLRPC.Document)this.cache.get(var2);
               ((StickerEmojiCell)var1.itemView).setSticker(var5, this.positionsToSets.get(var2), false);
            }
         } else {
            ((StickerEmojiCell)var1.itemView).setSticker((TLRPC.Document)StickersAlert.this.stickerSet.documents.get(var2), StickersAlert.this.stickerSet, StickersAlert.this.showEmoji);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  var3 = null;
               } else {
                  var3 = new FeaturedStickerSetInfoCell(this.context, 8);
               }
            } else {
               var3 = new EmptyCell(this.context);
            }
         } else {
            var3 = new StickerEmojiCell(this.context) {
               public void onMeasure(int var1, int var2) {
                  super.onMeasure(MeasureSpec.makeMeasureSpec(StickersAlert.this.itemSize, 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0F), 1073741824));
               }
            };
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   private static class LinkMovementMethodMy extends LinkMovementMethod {
      private LinkMovementMethodMy() {
      }

      // $FF: synthetic method
      LinkMovementMethodMy(Object var1) {
         this();
      }

      public boolean onTouchEvent(TextView var1, Spannable var2, MotionEvent var3) {
         try {
            boolean var4 = super.onTouchEvent(var1, var2, var3);
            if (var3.getAction() == 1 || var3.getAction() == 3) {
               Selection.removeSelection(var2);
            }

            return var4;
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
            return false;
         }
      }
   }

   public interface StickersAlertDelegate {
      void onStickerSelected(TLRPC.Document var1, Object var2, boolean var3);
   }

   public interface StickersAlertInstallDelegate {
      void onStickerSetInstalled();

      void onStickerSetUninstalled();
   }
}
