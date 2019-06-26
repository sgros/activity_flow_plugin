package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.LongSparseArray;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ShareDialogCell;

public class ShareAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
   private AnimatorSet animatorSet;
   private EditTextEmoji commentTextView;
   private boolean copyLinkOnEnd;
   private int currentAccount;
   private TLRPC.TL_exportedMessageLink exportedMessageLink;
   private FrameLayout frameLayout;
   private FrameLayout frameLayout2;
   private RecyclerListView gridView;
   private boolean isChannel;
   private GridLayoutManager layoutManager;
   private String linkToCopy;
   private ShareAlert.ShareDialogsAdapter listAdapter;
   private boolean loadingLink;
   private Paint paint = new Paint(1);
   private TextView pickerBottomLayout;
   private RectF rect = new RectF();
   private int scrollOffsetY;
   private ShareAlert.ShareSearchAdapter searchAdapter;
   private EmptyTextProgressView searchEmptyView;
   private View selectedCountView;
   private LongSparseArray selectedDialogs = new LongSparseArray();
   private ArrayList sendingMessageObjects;
   private String sendingText;
   private View[] shadow = new View[2];
   private AnimatorSet[] shadowAnimation = new AnimatorSet[2];
   private Drawable shadowDrawable;
   private TextPaint textPaint = new TextPaint(1);
   private int topBeforeSwitch;
   private FrameLayout writeButtonContainer;

   public ShareAlert(Context var1, ArrayList var2, String var3, boolean var4, String var5, boolean var6) {
      super(var1, true, 1);
      this.currentAccount = UserConfig.selectedAccount;
      this.shadowDrawable = var1.getResources().getDrawable(2131165824).mutate();
      this.shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogBackground"), Mode.MULTIPLY));
      super.isFullscreen = var6;
      this.linkToCopy = var5;
      this.sendingMessageObjects = var2;
      this.searchAdapter = new ShareAlert.ShareSearchAdapter(var1);
      this.isChannel = var4;
      this.sendingText = var3;
      if (var4) {
         this.loadingLink = true;
         TLRPC.TL_channels_exportMessageLink var15 = new TLRPC.TL_channels_exportMessageLink();
         var15.id = ((MessageObject)var2.get(0)).getId();
         var15.channel = MessagesController.getInstance(this.currentAccount).getInputChannel(((MessageObject)var2.get(0)).messageOwner.to_id.channel_id);
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var15, new _$$Lambda$ShareAlert$_zt1k1mc1Pf5YI45UjblkHkDCiI(this, var1));
      }

      SizeNotifierFrameLayout var12 = new SizeNotifierFrameLayout(var1) {
         private boolean fullHeight;
         private boolean ignoreLayout = false;
         private RectF rect1 = new RectF();

         private void onMeasureInternal(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var1);
            int var4 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(var3, var4);
            int var5 = var3 - ShareAlert.access$1400(ShareAlert.this) * 2;
            var3 = this.getKeyboardHeight();
            int var6 = AndroidUtilities.dp(20.0F);
            float var7 = 1.0F;
            View var8;
            if (var3 <= var6) {
               if (!AndroidUtilities.isInMultiwindow) {
                  var2 = var4 - ShareAlert.this.commentTextView.getEmojiPadding();
                  var4 = MeasureSpec.makeMeasureSpec(var2, 1073741824);
               } else {
                  var3 = var2;
                  var2 = var4;
                  var4 = var3;
               }

               this.ignoreLayout = true;
               byte var11;
               if (ShareAlert.this.commentTextView.isPopupShowing()) {
                  var11 = 8;
               } else {
                  var11 = 0;
               }

               if (ShareAlert.this.pickerBottomLayout != null) {
                  ShareAlert.this.pickerBottomLayout.setVisibility(var11);
                  var8 = ShareAlert.this.shadow[1];
                  float var9 = var7;
                  if (ShareAlert.this.frameLayout2.getVisibility() != 0) {
                     if (var11 == 0) {
                        var9 = var7;
                     } else {
                        var9 = 0.0F;
                     }
                  }

                  var8.setAlpha(var9);
               }

               this.ignoreLayout = false;
               var3 = var4;
            } else {
               this.ignoreLayout = true;
               ShareAlert.this.commentTextView.hideEmojiView();
               if (ShareAlert.this.pickerBottomLayout != null) {
                  ShareAlert.this.pickerBottomLayout.setVisibility(8);
                  var8 = ShareAlert.this.shadow[1];
                  if (ShareAlert.this.frameLayout2.getVisibility() != 0) {
                     var7 = 0.0F;
                  }

                  var8.setAlpha(var7);
               }

               this.ignoreLayout = false;
               var3 = var2;
               var2 = var4;
            }

            var6 = this.getChildCount();

            for(var4 = 0; var4 < var6; ++var4) {
               var8 = this.getChildAt(var4);
               if (var8 != null && var8.getVisibility() != 8) {
                  if (ShareAlert.this.commentTextView != null && ShareAlert.this.commentTextView.isPopupView(var8)) {
                     if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                        var8.measure(MeasureSpec.makeMeasureSpec(var5, 1073741824), MeasureSpec.makeMeasureSpec(var8.getLayoutParams().height, 1073741824));
                     } else if (AndroidUtilities.isTablet()) {
                        int var10 = MeasureSpec.makeMeasureSpec(var5, 1073741824);
                        if (AndroidUtilities.isTablet()) {
                           var7 = 200.0F;
                        } else {
                           var7 = 320.0F;
                        }

                        var8.measure(var10, MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(var7), var2 - AndroidUtilities.statusBarHeight + this.getPaddingTop()), 1073741824));
                     } else {
                        var8.measure(MeasureSpec.makeMeasureSpec(var5, 1073741824), MeasureSpec.makeMeasureSpec(var2 - AndroidUtilities.statusBarHeight + this.getPaddingTop(), 1073741824));
                     }
                  } else {
                     this.measureChildWithMargins(var8, var1, 0, var3, 0);
                  }
               }
            }

         }

         protected void onDraw(Canvas var1) {
            int var2;
            int var3;
            int var4;
            int var7;
            float var8;
            label32: {
               int var5;
               int var6;
               float var11;
               label31: {
                  var2 = ShareAlert.this.scrollOffsetY - ShareAlert.access$2100(ShareAlert.this) + AndroidUtilities.dp(6.0F);
                  var3 = ShareAlert.this.scrollOffsetY - ShareAlert.access$2200(ShareAlert.this) - AndroidUtilities.dp(13.0F);
                  var4 = this.getMeasuredHeight() + AndroidUtilities.dp(30.0F) + ShareAlert.access$2300(ShareAlert.this);
                  var5 = var2;
                  var6 = var3;
                  var7 = var4;
                  if (!ShareAlert.access$2400(ShareAlert.this)) {
                     var5 = var2;
                     var6 = var3;
                     var7 = var4;
                     if (VERSION.SDK_INT >= 21) {
                        var5 = AndroidUtilities.statusBarHeight;
                        var3 += var5;
                        var2 += var5;
                        var4 -= var5;
                        var5 = var2;
                        var6 = var3;
                        var7 = var4;
                        if (this.fullHeight) {
                           var5 = ShareAlert.access$2500(ShareAlert.this);
                           var6 = AndroidUtilities.statusBarHeight;
                           if (var5 + var3 < var6 * 2) {
                              var5 = Math.min(var6, var6 * 2 - var3 - ShareAlert.access$2600(ShareAlert.this));
                              var3 -= var5;
                              var4 += var5;
                              var8 = 1.0F - Math.min(1.0F, (float)(var5 * 2) / (float)AndroidUtilities.statusBarHeight);
                           } else {
                              var8 = 1.0F;
                           }

                           int var9 = ShareAlert.access$2700(ShareAlert.this);
                           int var10 = AndroidUtilities.statusBarHeight;
                           var5 = var2;
                           var6 = var3;
                           var7 = var4;
                           var11 = var8;
                           if (var9 + var3 < var10) {
                              var5 = Math.min(var10, var10 - var3 - ShareAlert.access$2800(ShareAlert.this));
                              var7 = var4;
                              var4 = var5;
                              break label32;
                           }
                           break label31;
                        }
                     }
                  }

                  var11 = 1.0F;
               }

               var4 = 0;
               var8 = var11;
               var3 = var6;
               var2 = var5;
            }

            ShareAlert.this.shadowDrawable.setBounds(0, var3, this.getMeasuredWidth(), var7);
            ShareAlert.this.shadowDrawable.draw(var1);
            if (var8 != 1.0F) {
               Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("dialogBackground"));
               this.rect1.set((float)ShareAlert.access$3000(ShareAlert.this), (float)(ShareAlert.access$3100(ShareAlert.this) + var3), (float)(this.getMeasuredWidth() - ShareAlert.access$3200(ShareAlert.this)), (float)(ShareAlert.access$3300(ShareAlert.this) + var3 + AndroidUtilities.dp(24.0F)));
               var1.drawRoundRect(this.rect1, (float)AndroidUtilities.dp(12.0F) * var8, (float)AndroidUtilities.dp(12.0F) * var8, Theme.dialogs_onlineCirclePaint);
            }

            var3 = AndroidUtilities.dp(36.0F);
            this.rect1.set((float)((this.getMeasuredWidth() - var3) / 2), (float)var2, (float)((this.getMeasuredWidth() + var3) / 2), (float)(var2 + AndroidUtilities.dp(4.0F)));
            Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor("key_sheet_scrollUp"));
            var1.drawRoundRect(this.rect1, (float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(2.0F), Theme.dialogs_onlineCirclePaint);
            if (var4 > 0) {
               var3 = Theme.getColor("dialogBackground");
               var3 = Color.argb(255, (int)((float)Color.red(var3) * 0.8F), (int)((float)Color.green(var3) * 0.8F), (int)((float)Color.blue(var3) * 0.8F));
               Theme.dialogs_onlineCirclePaint.setColor(var3);
               var1.drawRect((float)ShareAlert.access$3400(ShareAlert.this), (float)(AndroidUtilities.statusBarHeight - var4), (float)(this.getMeasuredWidth() - ShareAlert.access$3500(ShareAlert.this)), (float)AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
            }

         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (var1.getAction() == 0 && ShareAlert.this.scrollOffsetY != 0 && var1.getY() < (float)(ShareAlert.this.scrollOffsetY - AndroidUtilities.dp(30.0F))) {
               ShareAlert.this.dismiss();
               return true;
            } else {
               return super.onInterceptTouchEvent(var1);
            }
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            int var6 = this.getChildCount();
            int var7 = this.getKeyboardHeight();
            int var8 = AndroidUtilities.dp(20.0F);
            int var9 = 0;
            if (var7 <= var8 && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
               var8 = ShareAlert.this.commentTextView.getEmojiPadding();
            } else {
               var8 = 0;
            }

            this.setBottomClip(var8);

            for(; var9 < var6; ++var9) {
               View var10 = this.getChildAt(var9);
               if (var10.getVisibility() != 8) {
                  LayoutParams var11 = (LayoutParams)var10.getLayoutParams();
                  int var12 = var10.getMeasuredWidth();
                  int var13 = var10.getMeasuredHeight();
                  int var14 = var11.gravity;
                  var7 = var14;
                  if (var14 == -1) {
                     var7 = 51;
                  }

                  int var15;
                  label61: {
                     var15 = var7 & 112;
                     var7 = var7 & 7 & 7;
                     if (var7 != 1) {
                        if (var7 != 5) {
                           var14 = var11.leftMargin + this.getPaddingLeft();
                           break label61;
                        }

                        var14 = var4 - var2 - var12 - var11.rightMargin - this.getPaddingRight();
                        var7 = ShareAlert.access$1800(ShareAlert.this);
                     } else {
                        var14 = (var4 - var2 - var12) / 2 + var11.leftMargin;
                        var7 = var11.rightMargin;
                     }

                     var14 -= var7;
                  }

                  label55: {
                     if (var15 != 16) {
                        if (var15 == 48) {
                           var7 = var11.topMargin + this.getPaddingTop();
                           break label55;
                        }

                        if (var15 != 80) {
                           var7 = var11.topMargin;
                           break label55;
                        }

                        var7 = var5 - var8 - var3 - var13;
                        var15 = var11.bottomMargin;
                     } else {
                        var7 = (var5 - var8 - var3 - var13) / 2 + var11.topMargin;
                        var15 = var11.bottomMargin;
                     }

                     var7 -= var15;
                  }

                  var15 = var7;
                  if (ShareAlert.this.commentTextView != null) {
                     var15 = var7;
                     if (ShareAlert.this.commentTextView.isPopupView(var10)) {
                        if (AndroidUtilities.isTablet()) {
                           var15 = this.getMeasuredHeight();
                           var7 = var10.getMeasuredHeight();
                        } else {
                           var15 = this.getMeasuredHeight() + this.getKeyboardHeight();
                           var7 = var10.getMeasuredHeight();
                        }

                        var15 -= var7;
                     }
                  }

                  var10.layout(var14, var15, var12 + var14, var13 + var15);
               }
            }

            this.notifyHeightChanged();
            ShareAlert.this.updateLayout();
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var2);
            var2 = VERSION.SDK_INT;
            boolean var4 = true;
            if (var2 >= 21 && !ShareAlert.access$900(ShareAlert.this)) {
               this.ignoreLayout = true;
               this.setPadding(ShareAlert.access$1000(ShareAlert.this), AndroidUtilities.statusBarHeight, ShareAlert.access$1100(ShareAlert.this), 0);
               this.ignoreLayout = false;
            }

            int var5 = var3 - this.getPaddingTop();
            int var6 = this.getKeyboardHeight();
            var2 = var5;
            if (!AndroidUtilities.isInMultiwindow) {
               var2 = var5;
               if (var6 <= AndroidUtilities.dp(20.0F)) {
                  var2 = var5 - ShareAlert.this.commentTextView.getEmojiPadding();
               }
            }

            var5 = Math.max(ShareAlert.this.searchAdapter.getItemCount(), ShareAlert.this.listAdapter.getItemCount());
            var5 = AndroidUtilities.dp(48.0F) + Math.max(3, (int)Math.ceil((double)((float)var5 / 4.0F))) * AndroidUtilities.dp(103.0F) + ShareAlert.access$1300(ShareAlert.this);
            if (var5 < var2) {
               var2 = 0;
            } else {
               var2 -= var2 / 5 * 3;
            }

            var2 += AndroidUtilities.dp(8.0F);
            if (ShareAlert.this.gridView.getPaddingTop() != var2) {
               this.ignoreLayout = true;
               ShareAlert.this.gridView.setPadding(0, var2, 0, AndroidUtilities.dp(48.0F));
               this.ignoreLayout = false;
            }

            if (var5 < var3) {
               var4 = false;
            }

            this.fullHeight = var4;
            this.onMeasureInternal(var1, MeasureSpec.makeMeasureSpec(Math.min(var5, var3), 1073741824));
         }

         public boolean onTouchEvent(MotionEvent var1) {
            boolean var2;
            if (!ShareAlert.this.isDismissed() && super.onTouchEvent(var1)) {
               var2 = true;
            } else {
               var2 = false;
            }

            return var2;
         }

         public void requestLayout() {
            if (!this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      super.containerView = var12;
      super.containerView.setWillNotDraw(false);
      ViewGroup var16 = super.containerView;
      int var7 = super.backgroundPaddingLeft;
      var16.setPadding(var7, 0, var7, 0);
      this.frameLayout = new FrameLayout(var1);
      this.frameLayout.setBackgroundColor(Theme.getColor("dialogBackground"));
      ShareAlert.SearchField var17 = new ShareAlert.SearchField(var1);
      this.frameLayout.addView(var17, LayoutHelper.createFrame(-1, -1, 51));
      this.gridView = new RecyclerListView(var1) {
         protected boolean allowSelectChildAtPosition(float var1, float var2) {
            int var3 = ShareAlert.this.scrollOffsetY;
            int var4 = AndroidUtilities.dp(48.0F);
            int var5 = VERSION.SDK_INT;
            boolean var6 = false;
            if (var5 >= 21) {
               var5 = AndroidUtilities.statusBarHeight;
            } else {
               var5 = 0;
            }

            if (var2 >= (float)(var3 + var4 + var5)) {
               var6 = true;
            }

            return var6;
         }
      };
      this.gridView.setTag(13);
      this.gridView.setPadding(0, 0, 0, AndroidUtilities.dp(48.0F));
      this.gridView.setClipToPadding(false);
      RecyclerListView var21 = this.gridView;
      GridLayoutManager var8 = new GridLayoutManager(this.getContext(), 4);
      this.layoutManager = var8;
      var21.setLayoutManager(var8);
      this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
         public int getSpanSize(int var1) {
            return var1 == 0 ? ShareAlert.this.layoutManager.getSpanCount() : 1;
         }
      });
      this.gridView.setHorizontalScrollBarEnabled(false);
      this.gridView.setVerticalScrollBarEnabled(false);
      this.gridView.addItemDecoration(new RecyclerView.ItemDecoration() {
         public void getItemOffsets(android.graphics.Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
            RecyclerListView.Holder var8 = (RecyclerListView.Holder)var3.getChildViewHolder(var2);
            if (var8 != null) {
               int var5 = var8.getAdapterPosition() % 4;
               byte var6 = 0;
               int var7;
               if (var5 == 0) {
                  var7 = 0;
               } else {
                  var7 = AndroidUtilities.dp(4.0F);
               }

               var1.left = var7;
               if (var5 == 3) {
                  var7 = var6;
               } else {
                  var7 = AndroidUtilities.dp(4.0F);
               }

               var1.right = var7;
            } else {
               var1.left = AndroidUtilities.dp(4.0F);
               var1.right = AndroidUtilities.dp(4.0F);
            }

         }
      });
      super.containerView.addView(this.gridView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 0.0F));
      RecyclerListView var25 = this.gridView;
      ShareAlert.ShareDialogsAdapter var22 = new ShareAlert.ShareDialogsAdapter(var1);
      this.listAdapter = var22;
      var25.setAdapter(var22);
      this.gridView.setGlowColor(Theme.getColor("dialogScrollGlow"));
      this.gridView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ShareAlert$W5k8hWsyZg9DfQTgiUPSh2_mvaQ(this, var17)));
      this.gridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            ShareAlert.this.updateLayout();
         }
      });
      this.searchEmptyView = new EmptyTextProgressView(var1);
      this.searchEmptyView.setShowAtCenter(true);
      this.searchEmptyView.showTextView();
      this.searchEmptyView.setText(LocaleController.getString("NoChats", 2131559918));
      this.gridView.setEmptyView(this.searchEmptyView);
      super.containerView.addView(this.searchEmptyView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 52.0F, 0.0F, 0.0F));
      LayoutParams var19 = new LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
      var19.topMargin = AndroidUtilities.dp(58.0F);
      this.shadow[0] = new View(var1);
      this.shadow[0].setBackgroundColor(Theme.getColor("dialogShadowLine"));
      this.shadow[0].setAlpha(0.0F);
      this.shadow[0].setTag(1);
      super.containerView.addView(this.shadow[0], var19);
      super.containerView.addView(this.frameLayout, LayoutHelper.createFrame(-1, 58, 51));
      var19 = new LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83);
      var19.bottomMargin = AndroidUtilities.dp(48.0F);
      this.shadow[1] = new View(var1);
      this.shadow[1].setBackgroundColor(Theme.getColor("dialogShadowLine"));
      super.containerView.addView(this.shadow[1], var19);
      if (!this.isChannel && this.linkToCopy == null) {
         this.shadow[1].setAlpha(0.0F);
      } else {
         this.pickerBottomLayout = new TextView(var1);
         this.pickerBottomLayout.setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("dialogBackground"), Theme.getColor("listSelectorSDK21")));
         this.pickerBottomLayout.setTextColor(Theme.getColor("dialogTextBlue2"));
         this.pickerBottomLayout.setTextSize(1, 14.0F);
         this.pickerBottomLayout.setPadding(AndroidUtilities.dp(18.0F), 0, AndroidUtilities.dp(18.0F), 0);
         this.pickerBottomLayout.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
         this.pickerBottomLayout.setGravity(17);
         this.pickerBottomLayout.setText(LocaleController.getString("CopyLink", 2131559164).toUpperCase());
         this.pickerBottomLayout.setOnClickListener(new _$$Lambda$ShareAlert$Y4YbUqwWCug5T2hNA6sxQRQyLJ0(this));
         super.containerView.addView(this.pickerBottomLayout, LayoutHelper.createFrame(-1, 48, 83));
      }

      this.frameLayout2 = new FrameLayout(var1);
      this.frameLayout2.setBackgroundColor(Theme.getColor("dialogBackground"));
      this.frameLayout2.setAlpha(0.0F);
      this.frameLayout2.setVisibility(4);
      super.containerView.addView(this.frameLayout2, LayoutHelper.createFrame(-1, 48, 83));
      this.frameLayout2.setOnTouchListener(_$$Lambda$ShareAlert$e30oQsfKYZ5WdxxCC6xZg65lanw.INSTANCE);
      this.commentTextView = new EditTextEmoji(var1, var12, (BaseFragment)null, 1);
      this.commentTextView.setHint(LocaleController.getString("ShareComment", 2131560746));
      this.commentTextView.onResume();
      EditTextBoldCursor var13 = this.commentTextView.getEditText();
      var13.setMaxLines(1);
      var13.setSingleLine(true);
      this.frameLayout2.addView(this.commentTextView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 84.0F, 0.0F));
      this.writeButtonContainer = new FrameLayout(var1);
      this.writeButtonContainer.setVisibility(4);
      this.writeButtonContainer.setScaleX(0.2F);
      this.writeButtonContainer.setScaleY(0.2F);
      this.writeButtonContainer.setAlpha(0.0F);
      this.writeButtonContainer.setContentDescription(LocaleController.getString("Send", 2131560685));
      super.containerView.addView(this.writeButtonContainer, LayoutHelper.createFrame(60, 60.0F, 85, 0.0F, 0.0F, 6.0F, 10.0F));
      this.writeButtonContainer.setOnClickListener(new _$$Lambda$ShareAlert$7m2JWNxMvSKoRyVHsLYwsIAMWC0(this));
      ImageView var20 = new ImageView(var1);
      Object var14 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("dialogFloatingButton"), Theme.getColor("dialogFloatingButtonPressed"));
      if (VERSION.SDK_INT < 21) {
         Drawable var23 = var1.getResources().getDrawable(2131165388).mutate();
         var23.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
         var14 = new CombinedDrawable(var23, (Drawable)var14, 0, 0);
         ((CombinedDrawable)var14).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
      }

      var20.setBackgroundDrawable((Drawable)var14);
      var20.setImageResource(2131165292);
      var20.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogFloatingIcon"), Mode.MULTIPLY));
      var20.setScaleType(ScaleType.CENTER);
      if (VERSION.SDK_INT >= 21) {
         var20.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint({"NewApi"})
            public void getOutline(View var1, Outline var2) {
               var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
            }
         });
      }

      FrameLayout var18 = this.writeButtonContainer;
      byte var24;
      if (VERSION.SDK_INT >= 21) {
         var24 = 56;
      } else {
         var24 = 60;
      }

      float var9;
      if (VERSION.SDK_INT >= 21) {
         var9 = 56.0F;
      } else {
         var9 = 60.0F;
      }

      float var10;
      if (VERSION.SDK_INT >= 21) {
         var10 = 2.0F;
      } else {
         var10 = 0.0F;
      }

      var18.addView(var20, LayoutHelper.createFrame(var24, var9, 51, var10, 0.0F, 0.0F, 0.0F));
      this.textPaint.setTextSize((float)AndroidUtilities.dp(12.0F));
      this.textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.selectedCountView = new View(var1) {
         protected void onDraw(Canvas var1) {
            String var2 = String.format("%d", Math.max(1, ShareAlert.this.selectedDialogs.size()));
            int var3 = (int)Math.ceil((double)ShareAlert.this.textPaint.measureText(var2));
            int var4 = Math.max(AndroidUtilities.dp(16.0F) + var3, AndroidUtilities.dp(24.0F));
            int var5 = this.getMeasuredWidth() / 2;
            int var6 = this.getMeasuredHeight() / 2;
            ShareAlert.this.textPaint.setColor(Theme.getColor("dialogRoundCheckBoxCheck"));
            ShareAlert.this.paint.setColor(Theme.getColor("dialogBackground"));
            RectF var7 = ShareAlert.this.rect;
            var6 = var4 / 2;
            var4 = var5 - var6;
            float var8 = (float)var4;
            var6 += var5;
            var7.set(var8, 0.0F, (float)var6, (float)this.getMeasuredHeight());
            var1.drawRoundRect(ShareAlert.this.rect, (float)AndroidUtilities.dp(12.0F), (float)AndroidUtilities.dp(12.0F), ShareAlert.this.paint);
            ShareAlert.this.paint.setColor(Theme.getColor("dialogRoundCheckBox"));
            ShareAlert.this.rect.set((float)(var4 + AndroidUtilities.dp(2.0F)), (float)AndroidUtilities.dp(2.0F), (float)(var6 - AndroidUtilities.dp(2.0F)), (float)(this.getMeasuredHeight() - AndroidUtilities.dp(2.0F)));
            var1.drawRoundRect(ShareAlert.this.rect, (float)AndroidUtilities.dp(10.0F), (float)AndroidUtilities.dp(10.0F), ShareAlert.this.paint);
            var1.drawText(var2, (float)(var5 - var3 / 2), (float)AndroidUtilities.dp(16.2F), ShareAlert.this.textPaint);
         }
      };
      this.selectedCountView.setAlpha(0.0F);
      this.selectedCountView.setScaleX(0.2F);
      this.selectedCountView.setScaleY(0.2F);
      super.containerView.addView(this.selectedCountView, LayoutHelper.createFrame(42, 24.0F, 85, 0.0F, 0.0F, -8.0F, 9.0F));
      this.updateSelectedCount(0);
      boolean[] var11 = DialogsActivity.dialogsLoaded;
      var7 = this.currentAccount;
      if (!var11[var7]) {
         MessagesController.getInstance(var7).loadDialogs(0, 0, 100, true);
         ContactsController.getInstance(this.currentAccount).checkInviteText();
         DialogsActivity.dialogsLoaded[this.currentAccount] = true;
      }

      if (this.listAdapter.dialogs.isEmpty()) {
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.dialogsNeedReload);
      }

   }

   // $FF: synthetic method
   static ViewGroup access$000(ShareAlert var0) {
      return var0.containerView;
   }

   // $FF: synthetic method
   static int access$1000(ShareAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$1100(ShareAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$1300(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$1400(ShareAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$1800(ShareAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$2100(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2200(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2300(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static boolean access$2400(ShareAlert var0) {
      return var0.isFullscreen;
   }

   // $FF: synthetic method
   static int access$2500(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2600(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2700(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$2800(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$3000(ShareAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$3100(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$3200(ShareAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$3300(ShareAlert var0) {
      return var0.backgroundPaddingTop;
   }

   // $FF: synthetic method
   static int access$3400(ShareAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static int access$3500(ShareAlert var0) {
      return var0.backgroundPaddingLeft;
   }

   // $FF: synthetic method
   static boolean access$900(ShareAlert var0) {
      return var0.isFullscreen;
   }

   private void copyLink(Context var1) {
      if (this.exportedMessageLink != null || this.linkToCopy != null) {
         Exception var10000;
         label56: {
            boolean var10001;
            ClipboardManager var2;
            String var7;
            label48: {
               try {
                  var2 = (ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard");
                  if (this.linkToCopy != null) {
                     var7 = this.linkToCopy;
                     break label48;
                  }
               } catch (Exception var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label56;
               }

               try {
                  var7 = this.exportedMessageLink.link;
               } catch (Exception var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label56;
               }
            }

            try {
               var2.setPrimaryClip(ClipData.newPlainText("label", var7));
               if (this.exportedMessageLink != null && this.exportedMessageLink.link.contains("/c/")) {
                  Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("LinkCopiedPrivate", 2131559752), 0).show();
                  return;
               }
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label56;
            }

            try {
               Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("LinkCopied", 2131559751), 0).show();
               return;
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         Exception var8 = var10000;
         FileLog.e((Throwable)var8);
      }
   }

   public static ShareAlert createShareAlert(Context var0, MessageObject var1, String var2, boolean var3, String var4, boolean var5) {
      ArrayList var7;
      if (var1 != null) {
         ArrayList var6 = new ArrayList();
         var6.add(var1);
         var7 = var6;
      } else {
         var7 = null;
      }

      return new ShareAlert(var0, var7, var2, var3, var4, var5);
   }

   private int getCurrentTop() {
      if (this.gridView.getChildCount() != 0) {
         RecyclerListView var1 = this.gridView;
         byte var2 = 0;
         View var6 = var1.getChildAt(0);
         RecyclerListView.Holder var3 = (RecyclerListView.Holder)this.gridView.findContainingViewHolder(var6);
         if (var3 != null) {
            int var4 = this.gridView.getPaddingTop();
            int var5 = var2;
            if (var3.getAdapterPosition() == 0) {
               var5 = var2;
               if (var6.getTop() >= 0) {
                  var5 = var6.getTop();
               }
            }

            return var4 - var5;
         }
      }

      return -1000;
   }

   // $FF: synthetic method
   static boolean lambda$new$4(View var0, MotionEvent var1) {
      return true;
   }

   private void runShadowAnimation(final int var1, final boolean var2) {
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
         AnimatorSet var5 = this.shadowAnimation[var1];
         View var9 = this.shadow[var1];
         Property var7 = View.ALPHA;
         float var6;
         if (var2) {
            var6 = 1.0F;
         } else {
            var6 = 0.0F;
         }

         var5.playTogether(new Animator[]{ObjectAnimator.ofFloat(var9, var7, new float[]{var6})});
         this.shadowAnimation[var1].setDuration(150L);
         this.shadowAnimation[var1].addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               if (ShareAlert.this.shadowAnimation[var1] != null && ShareAlert.this.shadowAnimation[var1].equals(var1x)) {
                  ShareAlert.this.shadowAnimation[var1] = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               if (ShareAlert.this.shadowAnimation[var1] != null && ShareAlert.this.shadowAnimation[var1].equals(var1x)) {
                  if (!var2) {
                     ShareAlert.this.shadow[var1].setVisibility(4);
                  }

                  ShareAlert.this.shadowAnimation[var1] = null;
               }

            }
         });
         this.shadowAnimation[var1].start();
      }

   }

   private boolean showCommentTextView(final boolean var1) {
      boolean var2;
      if (this.frameLayout2.getTag() != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var1 == var2) {
         return false;
      } else {
         AnimatorSet var3 = this.animatorSet;
         if (var3 != null) {
            var3.cancel();
         }

         FrameLayout var4 = this.frameLayout2;
         Integer var9;
         if (var1) {
            var9 = 1;
         } else {
            var9 = null;
         }

         var4.setTag(var9);
         if (this.commentTextView.getEditText().isFocused()) {
            AndroidUtilities.hideKeyboard(this.commentTextView.getEditText());
         }

         this.commentTextView.hidePopup(true);
         if (var1) {
            this.frameLayout2.setVisibility(0);
            this.writeButtonContainer.setVisibility(0);
         }

         this.animatorSet = new AnimatorSet();
         ArrayList var10 = new ArrayList();
         var4 = this.frameLayout2;
         Property var5 = View.ALPHA;
         float var6 = 0.0F;
         float var7;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var10.add(ObjectAnimator.ofFloat(var4, var5, new float[]{var7}));
         FrameLayout var12 = this.writeButtonContainer;
         Property var11 = View.SCALE_X;
         float var8 = 0.2F;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.2F;
         }

         var10.add(ObjectAnimator.ofFloat(var12, var11, new float[]{var7}));
         var4 = this.writeButtonContainer;
         var5 = View.SCALE_Y;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.2F;
         }

         var10.add(ObjectAnimator.ofFloat(var4, var5, new float[]{var7}));
         var12 = this.writeButtonContainer;
         var11 = View.ALPHA;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var10.add(ObjectAnimator.ofFloat(var12, var11, new float[]{var7}));
         View var13 = this.selectedCountView;
         var5 = View.SCALE_X;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.2F;
         }

         var10.add(ObjectAnimator.ofFloat(var13, var5, new float[]{var7}));
         var13 = this.selectedCountView;
         var5 = View.SCALE_Y;
         var7 = var8;
         if (var1) {
            var7 = 1.0F;
         }

         var10.add(ObjectAnimator.ofFloat(var13, var5, new float[]{var7}));
         var13 = this.selectedCountView;
         var5 = View.ALPHA;
         if (var1) {
            var7 = 1.0F;
         } else {
            var7 = 0.0F;
         }

         var10.add(ObjectAnimator.ofFloat(var13, var5, new float[]{var7}));
         TextView var14 = this.pickerBottomLayout;
         if (var14 == null || var14.getVisibility() != 0) {
            View var15 = this.shadow[1];
            var11 = View.ALPHA;
            var7 = var6;
            if (var1) {
               var7 = 1.0F;
            }

            var10.add(ObjectAnimator.ofFloat(var15, var11, new float[]{var7}));
         }

         this.animatorSet.playTogether(var10);
         this.animatorSet.setInterpolator(new DecelerateInterpolator());
         this.animatorSet.setDuration(180L);
         this.animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator var1x) {
               if (var1x.equals(ShareAlert.this.animatorSet)) {
                  ShareAlert.this.animatorSet = null;
               }

            }

            public void onAnimationEnd(Animator var1x) {
               if (var1x.equals(ShareAlert.this.animatorSet)) {
                  if (!var1) {
                     ShareAlert.this.frameLayout2.setVisibility(4);
                     ShareAlert.this.writeButtonContainer.setVisibility(4);
                  }

                  ShareAlert.this.animatorSet = null;
               }

            }
         });
         this.animatorSet.start();
         return true;
      }
   }

   @SuppressLint({"NewApi"})
   private void updateLayout() {
      if (this.gridView.getChildCount() > 0) {
         View var1 = this.gridView.getChildAt(0);
         RecyclerListView.Holder var2 = (RecyclerListView.Holder)this.gridView.findContainingViewHolder(var1);
         int var3 = var1.getTop() - AndroidUtilities.dp(8.0F);
         int var4;
         if (var3 > 0 && var2 != null && var2.getAdapterPosition() == 0) {
            var4 = var3;
         } else {
            var4 = 0;
         }

         if (var3 >= 0 && var2 != null && var2.getAdapterPosition() == 0) {
            this.runShadowAnimation(0, false);
         } else {
            this.runShadowAnimation(0, true);
            var3 = var4;
         }

         if (this.scrollOffsetY != var3) {
            RecyclerListView var5 = this.gridView;
            this.scrollOffsetY = var3;
            var5.setTopGlowOffset(var3);
            this.frameLayout.setTranslationY((float)this.scrollOffsetY);
            this.searchEmptyView.setTranslationY((float)this.scrollOffsetY);
            super.containerView.invalidate();
         }

      }
   }

   protected boolean canDismissWithSwipe() {
      return false;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.dialogsNeedReload) {
         ShareAlert.ShareDialogsAdapter var4 = this.listAdapter;
         if (var4 != null) {
            var4.fetchDialogs();
         }

         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
      }

   }

   public void dismiss() {
      EditTextEmoji var1 = this.commentTextView;
      if (var1 != null) {
         AndroidUtilities.hideKeyboard(var1.getEditText());
      }

      super.dismiss();
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.dialogsNeedReload);
   }

   public void dismissInternal() {
      super.dismissInternal();
      EditTextEmoji var1 = this.commentTextView;
      if (var1 != null) {
         var1.onDestroy();
      }

   }

   // $FF: synthetic method
   public void lambda$new$1$ShareAlert(Context var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ShareAlert$sheoLWnPgs4Uu5PP1z2vS_Dfuq4(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$new$2$ShareAlert(ShareAlert.SearchField var1, View var2, int var3) {
      if (var3 >= 0) {
         RecyclerView.Adapter var4 = this.gridView.getAdapter();
         ShareAlert.ShareDialogsAdapter var5 = this.listAdapter;
         TLRPC.Dialog var8;
         if (var4 == var5) {
            var8 = var5.getItem(var3);
         } else {
            var8 = this.searchAdapter.getItem(var3);
         }

         if (var8 != null) {
            ShareDialogCell var6 = (ShareDialogCell)var2;
            if (this.selectedDialogs.indexOfKey(var8.id) >= 0) {
               this.selectedDialogs.remove(var8.id);
               var6.setChecked(false, true);
               this.updateSelectedCount(1);
            } else {
               this.selectedDialogs.put(var8.id, var8);
               var6.setChecked(true, true);
               this.updateSelectedCount(2);
               var3 = UserConfig.getInstance(this.currentAccount).clientUserId;
               if (this.gridView.getAdapter() == this.searchAdapter) {
                  TLRPC.Dialog var7 = (TLRPC.Dialog)this.listAdapter.dialogsMap.get(var8.id);
                  if (var7 == null) {
                     this.listAdapter.dialogsMap.put(var8.id, var8);
                     this.listAdapter.dialogs.add(1, var8);
                  } else if (var7.id != (long)var3) {
                     this.listAdapter.dialogs.remove(var7);
                     this.listAdapter.dialogs.add(1, var7);
                  }

                  var1.searchEditText.setText("");
                  this.gridView.setAdapter(this.listAdapter);
                  var1.hideKeyboard();
               }
            }

         }
      }
   }

   // $FF: synthetic method
   public void lambda$new$3$ShareAlert(View var1) {
      if (this.selectedDialogs.size() == 0 && (this.isChannel || this.linkToCopy != null)) {
         if (this.linkToCopy == null && this.loadingLink) {
            this.copyLinkOnEnd = true;
            Toast.makeText(this.getContext(), LocaleController.getString("Loading", 2131559768), 0).show();
         } else {
            this.copyLink(this.getContext());
         }

         this.dismiss();
      }

   }

   // $FF: synthetic method
   public void lambda$new$5$ShareAlert(View var1) {
      ArrayList var6 = this.sendingMessageObjects;
      byte var2 = 0;
      int var3 = 0;
      long var4;
      if (var6 != null) {
         while(var3 < this.selectedDialogs.size()) {
            var4 = this.selectedDialogs.keyAt(var3);
            if (this.frameLayout2.getTag() != null && this.commentTextView.length() > 0) {
               SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.commentTextView.getText().toString(), var4, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
            }

            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.sendingMessageObjects, var4);
            ++var3;
         }
      } else if (this.sendingText != null) {
         for(var3 = var2; var3 < this.selectedDialogs.size(); ++var3) {
            var4 = this.selectedDialogs.keyAt(var3);
            if (this.frameLayout2.getTag() != null && this.commentTextView.length() > 0) {
               SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.commentTextView.getText().toString(), var4, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
            }

            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(this.sendingText, var4, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
         }
      }

      this.dismiss();
   }

   // $FF: synthetic method
   public void lambda$null$0$ShareAlert(TLObject var1, Context var2) {
      if (var1 != null) {
         this.exportedMessageLink = (TLRPC.TL_exportedMessageLink)var1;
         if (this.copyLinkOnEnd) {
            this.copyLink(var2);
         }
      }

      this.loadingLink = false;
   }

   public void onBackPressed() {
      EditTextEmoji var1 = this.commentTextView;
      if (var1 != null && var1.isPopupShowing()) {
         this.commentTextView.hidePopup(true);
      } else {
         super.onBackPressed();
      }
   }

   public void updateSelectedCount(int var1) {
      if (this.selectedDialogs.size() == 0) {
         this.selectedCountView.setPivotX(0.0F);
         this.selectedCountView.setPivotY(0.0F);
         this.showCommentTextView(false);
      } else {
         this.selectedCountView.invalidate();
         if (var1 != 0 && !this.showCommentTextView(true)) {
            this.selectedCountView.setPivotX((float)AndroidUtilities.dp(21.0F));
            this.selectedCountView.setPivotY((float)AndroidUtilities.dp(12.0F));
            AnimatorSet var2 = new AnimatorSet();
            View var3 = this.selectedCountView;
            Property var4 = View.SCALE_X;
            float var5 = 1.1F;
            float var6;
            if (var1 == 1) {
               var6 = 1.1F;
            } else {
               var6 = 0.9F;
            }

            ObjectAnimator var8 = ObjectAnimator.ofFloat(var3, var4, new float[]{var6, 1.0F});
            var3 = this.selectedCountView;
            Property var7 = View.SCALE_Y;
            if (var1 == 1) {
               var6 = var5;
            } else {
               var6 = 0.9F;
            }

            var2.playTogether(new Animator[]{var8, ObjectAnimator.ofFloat(var3, var7, new float[]{var6, 1.0F})});
            var2.setInterpolator(new OvershootInterpolator());
            var2.setDuration(180L);
            var2.start();
         } else {
            this.selectedCountView.setPivotX(0.0F);
            this.selectedCountView.setPivotY(0.0F);
         }
      }

   }

   private class SearchField extends FrameLayout {
      private View backgroundView;
      private ImageView clearSearchImageView;
      private CloseProgressDrawable2 progressDrawable;
      private View searchBackground;
      private EditTextBoldCursor searchEditText;
      private ImageView searchIconImageView;

      public SearchField(Context var2) {
         super(var2);
         this.searchBackground = new View(var2);
         this.searchBackground.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0F), Theme.getColor("dialogSearchBackground")));
         this.addView(this.searchBackground, LayoutHelper.createFrame(-1, 36.0F, 51, 14.0F, 11.0F, 14.0F, 0.0F));
         this.searchIconImageView = new ImageView(var2);
         this.searchIconImageView.setScaleType(ScaleType.CENTER);
         this.searchIconImageView.setImageResource(2131165834);
         this.searchIconImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogSearchIcon"), Mode.MULTIPLY));
         this.addView(this.searchIconImageView, LayoutHelper.createFrame(36, 36.0F, 51, 16.0F, 11.0F, 0.0F, 0.0F));
         this.clearSearchImageView = new ImageView(var2);
         this.clearSearchImageView.setScaleType(ScaleType.CENTER);
         ImageView var3 = this.clearSearchImageView;
         CloseProgressDrawable2 var4 = new CloseProgressDrawable2();
         this.progressDrawable = var4;
         var3.setImageDrawable(var4);
         this.progressDrawable.setSide(AndroidUtilities.dp(7.0F));
         this.clearSearchImageView.setScaleX(0.1F);
         this.clearSearchImageView.setScaleY(0.1F);
         this.clearSearchImageView.setAlpha(0.0F);
         this.clearSearchImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("dialogSearchIcon"), Mode.MULTIPLY));
         this.addView(this.clearSearchImageView, LayoutHelper.createFrame(36, 36.0F, 53, 14.0F, 11.0F, 14.0F, 0.0F));
         this.clearSearchImageView.setOnClickListener(new _$$Lambda$ShareAlert$SearchField$Io_3RrJcNRJnH5Q8y3dN5bJYiIw(this));
         this.searchEditText = new EditTextBoldCursor(var2) {
            public boolean dispatchTouchEvent(MotionEvent var1) {
               MotionEvent var2 = MotionEvent.obtain(var1);
               var2.setLocation(var2.getRawX(), var2.getRawY() - ShareAlert.access$000(ShareAlert.this).getTranslationY());
               ShareAlert.this.gridView.dispatchTouchEvent(var2);
               var2.recycle();
               return super.dispatchTouchEvent(var1);
            }
         };
         this.searchEditText.setTextSize(1, 16.0F);
         this.searchEditText.setHintTextColor(Theme.getColor("dialogSearchHint"));
         this.searchEditText.setTextColor(Theme.getColor("dialogSearchText"));
         this.searchEditText.setBackgroundDrawable((Drawable)null);
         this.searchEditText.setPadding(0, 0, 0, 0);
         this.searchEditText.setMaxLines(1);
         this.searchEditText.setLines(1);
         this.searchEditText.setSingleLine(true);
         this.searchEditText.setImeOptions(268435459);
         this.searchEditText.setHint(LocaleController.getString("ShareSendTo", 2131560752));
         this.searchEditText.setCursorColor(Theme.getColor("featuredStickers_addedIcon"));
         this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0F));
         this.searchEditText.setCursorWidth(1.5F);
         this.addView(this.searchEditText, LayoutHelper.createFrame(-1, 40.0F, 51, 54.0F, 9.0F, 46.0F, 0.0F));
         this.searchEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               int var2 = SearchField.this.searchEditText.length();
               boolean var3 = true;
               boolean var7;
               if (var2 > 0) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               float var4 = SearchField.this.clearSearchImageView.getAlpha();
               float var5 = 0.0F;
               if (var4 == 0.0F) {
                  var3 = false;
               }

               if (var7 != var3) {
                  ViewPropertyAnimator var6 = SearchField.this.clearSearchImageView.animate();
                  var4 = 1.0F;
                  if (var7) {
                     var5 = 1.0F;
                  }

                  var6 = var6.alpha(var5).setDuration(150L);
                  if (var7) {
                     var5 = 1.0F;
                  } else {
                     var5 = 0.1F;
                  }

                  var6 = var6.scaleX(var5);
                  if (var7) {
                     var5 = var4;
                  } else {
                     var5 = 0.1F;
                  }

                  var6.scaleY(var5).start();
               }

               String var8 = SearchField.this.searchEditText.getText().toString();
               if (var8.length() != 0) {
                  if (ShareAlert.this.searchEmptyView != null) {
                     ShareAlert.this.searchEmptyView.setText(LocaleController.getString("NoResult", 2131559943));
                  }
               } else if (ShareAlert.this.gridView.getAdapter() != ShareAlert.this.listAdapter) {
                  var2 = ShareAlert.this.getCurrentTop();
                  ShareAlert.this.searchEmptyView.setText(LocaleController.getString("NoChats", 2131559918));
                  ShareAlert.this.searchEmptyView.showTextView();
                  ShareAlert.this.gridView.setAdapter(ShareAlert.this.listAdapter);
                  ShareAlert.this.listAdapter.notifyDataSetChanged();
                  if (var2 > 0) {
                     ShareAlert.this.layoutManager.scrollToPositionWithOffset(0, -var2);
                  }
               }

               if (ShareAlert.this.searchAdapter != null) {
                  ShareAlert.this.searchAdapter.searchDialogs(var8);
               }

            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         this.searchEditText.setOnEditorActionListener(new _$$Lambda$ShareAlert$SearchField$18q3qLqc5Z_Zq5YApOn8ryeRHg4(this));
      }

      public void hideKeyboard() {
         AndroidUtilities.hideKeyboard(this.searchEditText);
      }

      // $FF: synthetic method
      public void lambda$new$0$ShareAlert$SearchField(View var1) {
         this.searchEditText.setText("");
         AndroidUtilities.showKeyboard(this.searchEditText);
      }

      // $FF: synthetic method
      public boolean lambda$new$1$ShareAlert$SearchField(TextView var1, int var2, KeyEvent var3) {
         if (var3 != null && (var3.getAction() == 1 && var3.getKeyCode() == 84 || var3.getAction() == 0 && var3.getKeyCode() == 66)) {
            AndroidUtilities.hideKeyboard(this.searchEditText);
         }

         return false;
      }

      public void requestDisallowInterceptTouchEvent(boolean var1) {
         super.requestDisallowInterceptTouchEvent(var1);
      }
   }

   private class ShareDialogsAdapter extends RecyclerListView.SelectionAdapter {
      private Context context;
      private int currentCount;
      private ArrayList dialogs = new ArrayList();
      private LongSparseArray dialogsMap = new LongSparseArray();

      public ShareDialogsAdapter(Context var2) {
         this.context = var2;
         this.fetchDialogs();
      }

      public void fetchDialogs() {
         this.dialogs.clear();
         this.dialogsMap.clear();
         int var1 = UserConfig.getInstance(ShareAlert.this.currentAccount).clientUserId;
         boolean var2 = MessagesController.getInstance(ShareAlert.this.currentAccount).dialogsForward.isEmpty();
         int var3 = 0;
         TLRPC.Dialog var4;
         if (!var2) {
            var4 = (TLRPC.Dialog)MessagesController.getInstance(ShareAlert.this.currentAccount).dialogsForward.get(0);
            this.dialogs.add(var4);
            this.dialogsMap.put(var4.id, var4);
         }

         for(ArrayList var5 = MessagesController.getInstance(ShareAlert.this.currentAccount).getAllDialogs(); var3 < var5.size(); ++var3) {
            var4 = (TLRPC.Dialog)var5.get(var3);
            if (var4 instanceof TLRPC.TL_dialog) {
               long var6 = var4.id;
               int var8 = (int)var6;
               if (var8 != var1) {
                  int var9 = (int)(var6 >> 32);
                  if (var8 != 0 && var9 != 1) {
                     if (var8 > 0) {
                        this.dialogs.add(var4);
                        this.dialogsMap.put(var4.id, var4);
                     } else {
                        TLRPC.Chat var10 = MessagesController.getInstance(ShareAlert.this.currentAccount).getChat(-var8);
                        if (var10 != null && !ChatObject.isNotInChat(var10)) {
                           if (ChatObject.isChannel(var10) && !var10.creator) {
                              TLRPC.TL_chatAdminRights var11 = var10.admin_rights;
                              if ((var11 == null || !var11.post_messages) && !var10.megagroup) {
                                 continue;
                              }
                           }

                           this.dialogs.add(var4);
                           this.dialogsMap.put(var4.id, var4);
                        }
                     }
                  }
               }
            }
         }

         this.notifyDataSetChanged();
      }

      public TLRPC.Dialog getItem(int var1) {
         --var1;
         return var1 >= 0 && var1 < this.dialogs.size() ? (TLRPC.Dialog)this.dialogs.get(var1) : null;
      }

      public int getItemCount() {
         int var1 = this.dialogs.size();
         int var2 = var1;
         if (var1 != 0) {
            var2 = var1 + 1;
         }

         return var2;
      }

      public int getItemViewType(int var1) {
         return var1 == 0 ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return var1.getItemViewType() != 1;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            ShareDialogCell var3 = (ShareDialogCell)var1.itemView;
            TLRPC.Dialog var5 = this.getItem(var2);
            var2 = (int)var5.id;
            boolean var4;
            if (ShareAlert.this.selectedDialogs.indexOfKey(var5.id) >= 0) {
               var4 = true;
            } else {
               var4 = false;
            }

            var3.setDialog(var2, var4, (CharSequence)null);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new View(this.context);
            ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0F)));
         } else {
            var3 = new ShareDialogCell(this.context);
            ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0F)));
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   public class ShareSearchAdapter extends RecyclerListView.SelectionAdapter {
      private Context context;
      private int lastReqId;
      private int lastSearchId;
      private String lastSearchText;
      private int reqId;
      private ArrayList searchResult = new ArrayList();
      private Runnable searchRunnable;

      public ShareSearchAdapter(Context var2) {
         this.context = var2;
      }

      // $FF: synthetic method
      static int lambda$null$0(ShareAlert.ShareSearchAdapter.DialogSearchResult var0, ShareAlert.ShareSearchAdapter.DialogSearchResult var1) {
         int var2 = var0.date;
         int var3 = var1.date;
         if (var2 < var3) {
            return 1;
         } else {
            return var2 > var3 ? -1 : 0;
         }
      }

      private void searchDialogsInternal(String var1, int var2) {
         MessagesStorage.getInstance(ShareAlert.this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$ShareAlert$ShareSearchAdapter$_0pnpfSXHTiImTPZ850FgfY7mkY(this, var1, var2));
      }

      private void updateSearchResults(ArrayList var1, int var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ShareAlert$ShareSearchAdapter$HTelFIImAnZsj4Mdi1ZJ7VKMmtA(this, var2, var1));
      }

      public TLRPC.Dialog getItem(int var1) {
         --var1;
         return var1 >= 0 && var1 < this.searchResult.size() ? ((ShareAlert.ShareSearchAdapter.DialogSearchResult)this.searchResult.get(var1)).dialog : null;
      }

      public int getItemCount() {
         int var1 = this.searchResult.size();
         int var2 = var1;
         if (var1 != 0) {
            var2 = var1 + 1;
         }

         return var2;
      }

      public int getItemViewType(int var1) {
         return var1 == 0 ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return var1.getItemViewType() != 1;
      }

      // $FF: synthetic method
      public void lambda$searchDialogs$3$ShareAlert$ShareSearchAdapter(String var1, int var2) {
         this.searchDialogsInternal(var1, var2);
      }

      // $FF: synthetic method
      public void lambda$searchDialogsInternal$1$ShareAlert$ShareSearchAdapter(String var1, int var2) {
         Exception var10000;
         label595: {
            String var3;
            boolean var10001;
            try {
               var3 = var1.trim().toLowerCase();
               if (var3.length() == 0) {
                  this.lastSearchId = -1;
                  ArrayList var77 = new ArrayList();
                  this.updateSearchResults(var77, this.lastSearchId);
                  return;
               }
            } catch (Exception var73) {
               var10000 = var73;
               var10001 = false;
               break label595;
            }

            String var4;
            label563: {
               label571: {
                  try {
                     var4 = LocaleController.getInstance().getTranslitString(var3);
                     if (var3.equals(var4)) {
                        break label571;
                     }
                  } catch (Exception var72) {
                     var10000 = var72;
                     var10001 = false;
                     break label595;
                  }

                  var1 = var4;

                  try {
                     if (var4.length() != 0) {
                        break label563;
                     }
                  } catch (Exception var71) {
                     var10000 = var71;
                     var10001 = false;
                     break label595;
                  }
               }

               var1 = null;
            }

            byte var5;
            if (var1 != null) {
               var5 = 1;
            } else {
               var5 = 0;
            }

            String[] var6;
            try {
               var6 = new String[var5 + 1];
            } catch (Exception var70) {
               var10000 = var70;
               var10001 = false;
               break label595;
            }

            var6[0] = var3;
            if (var1 != null) {
               var6[1] = var1;
            }

            ArrayList var7;
            LongSparseArray var8;
            SQLiteCursor var75;
            ArrayList var78;
            try {
               var78 = new ArrayList();
               var7 = new ArrayList();
               var8 = new LongSparseArray();
               var75 = MessagesStorage.getInstance(ShareAlert.this.currentAccount).getDatabase().queryFinalized("SELECT did, date FROM dialogs ORDER BY date DESC LIMIT 400");
            } catch (Exception var66) {
               var10000 = var66;
               var10001 = false;
               break label595;
            }

            int var11;
            int var82;
            while(true) {
               long var9;
               try {
                  if (!var75.next()) {
                     break;
                  }

                  var9 = var75.longValue(0);
                  ShareAlert.ShareSearchAdapter.DialogSearchResult var74 = new ShareAlert.ShareSearchAdapter.DialogSearchResult();
                  var74.date = var75.intValue(1);
                  var8.put(var9, var74);
               } catch (Exception var69) {
                  var10000 = var69;
                  var10001 = false;
                  break label595;
               }

               var11 = (int)var9;
               var82 = (int)(var9 >> 32);
               if (var11 != 0 && var82 != 1) {
                  if (var11 > 0) {
                     try {
                        if (!var78.contains(var11)) {
                           var78.add(var11);
                        }
                     } catch (Exception var67) {
                        var10000 = var67;
                        var10001 = false;
                        break label595;
                     }
                  } else {
                     var82 = -var11;

                     try {
                        if (!var7.contains(var82)) {
                           var7.add(var82);
                        }
                     } catch (Exception var68) {
                        var10000 = var68;
                        var10001 = false;
                        break label595;
                     }
                  }
               }
            }

            boolean var12;
            try {
               var75.dispose();
               var12 = var78.isEmpty();
            } catch (Exception var65) {
               var10000 = var65;
               var10001 = false;
               break label595;
            }

            var1 = ";;;";
            SQLiteCursor var13;
            String var14;
            int var16;
            String var17;
            StringBuilder var18;
            StringBuilder var91;
            if (!var12) {
               try {
                  var13 = MessagesStorage.getInstance(ShareAlert.this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, status, name FROM users WHERE uid IN(%s)", TextUtils.join(",", var78)));
               } catch (Exception var59) {
                  var10000 = var59;
                  var10001 = false;
                  break label595;
               }

               var11 = 0;

               label516:
               while(true) {
                  while(true) {
                     try {
                        if (!var13.next()) {
                           break label516;
                        }

                        var14 = var13.stringValue(2);
                        var3 = LocaleController.getInstance().getTranslitString(var14);
                     } catch (Exception var60) {
                        var10000 = var60;
                        var10001 = false;
                        break label595;
                     }

                     var4 = var3;

                     label482: {
                        try {
                           if (!var14.equals(var3)) {
                              break label482;
                           }
                        } catch (Exception var61) {
                           var10000 = var61;
                           var10001 = false;
                           break label595;
                        }

                        var4 = null;
                     }

                     try {
                        var82 = var14.lastIndexOf(var1);
                     } catch (Exception var58) {
                        var10000 = var58;
                        var10001 = false;
                        break label595;
                     }

                     if (var82 != -1) {
                        try {
                           var3 = var14.substring(var82 + 3);
                        } catch (Exception var57) {
                           var10000 = var57;
                           var10001 = false;
                           break label595;
                        }
                     } else {
                        var3 = null;
                     }

                     int var15;
                     try {
                        var15 = var6.length;
                     } catch (Exception var56) {
                        var10000 = var56;
                        var10001 = false;
                        break label595;
                     }

                     var16 = 0;

                     for(var5 = 0; var16 < var15; ++var16) {
                        var17 = var6[var16];

                        label509: {
                           label577: {
                              try {
                                 if (var14.startsWith(var17)) {
                                    break label577;
                                 }

                                 var18 = new StringBuilder();
                                 var18.append(" ");
                                 var18.append(var17);
                                 if (var14.contains(var18.toString())) {
                                    break label577;
                                 }
                              } catch (Exception var64) {
                                 var10000 = var64;
                                 var10001 = false;
                                 break label595;
                              }

                              if (var4 != null) {
                                 try {
                                    if (var4.startsWith(var17)) {
                                       break label577;
                                    }

                                    var18 = new StringBuilder();
                                    var18.append(" ");
                                    var18.append(var17);
                                    if (var4.contains(var18.toString())) {
                                       break label577;
                                    }
                                 } catch (Exception var63) {
                                    var10000 = var63;
                                    var10001 = false;
                                    break label595;
                                 }
                              }

                              if (var3 == null) {
                                 break label509;
                              }

                              try {
                                 if (!var3.startsWith(var17)) {
                                    break label509;
                                 }
                              } catch (Exception var62) {
                                 var10000 = var62;
                                 var10001 = false;
                                 break label595;
                              }

                              var5 = 2;
                              break label509;
                           }

                           var5 = 1;
                        }

                        if (var5 != 0) {
                           NativeByteBuffer var79;
                           try {
                              var79 = var13.byteBufferValue(0);
                           } catch (Exception var55) {
                              var10000 = var55;
                              var10001 = false;
                              break label595;
                           }

                           if (var79 != null) {
                              ShareAlert.ShareSearchAdapter.DialogSearchResult var80;
                              TLRPC.User var81;
                              try {
                                 var81 = TLRPC.User.TLdeserialize(var79, var79.readInt32(false), false);
                                 var79.reuse();
                                 var80 = (ShareAlert.ShareSearchAdapter.DialogSearchResult)var8.get((long)var81.id);
                                 if (var81.status != null) {
                                    var81.status.expires = var13.intValue(1);
                                 }
                              } catch (Exception var54) {
                                 var10000 = var54;
                                 var10001 = false;
                                 break label595;
                              }

                              if (var5 == 1) {
                                 try {
                                    var80.name = AndroidUtilities.generateSearchName(var81.first_name, var81.last_name, var17);
                                 } catch (Exception var53) {
                                    var10000 = var53;
                                    var10001 = false;
                                    break label595;
                                 }
                              } else {
                                 try {
                                    var91 = new StringBuilder();
                                    var91.append("@");
                                    var91.append(var81.username);
                                    String var94 = var91.toString();
                                    var91 = new StringBuilder();
                                    var91.append("@");
                                    var91.append(var17);
                                    var80.name = AndroidUtilities.generateSearchName(var94, (String)null, var91.toString());
                                 } catch (Exception var52) {
                                    var10000 = var52;
                                    var10001 = false;
                                    break label595;
                                 }
                              }

                              try {
                                 var80.object = var81;
                                 var80.dialog.id = (long)var81.id;
                              } catch (Exception var51) {
                                 var10000 = var51;
                                 var10001 = false;
                                 break label595;
                              }

                              ++var11;
                           }
                           break;
                        }
                     }
                  }
               }

               try {
                  var13.dispose();
               } catch (Exception var50) {
                  var10000 = var50;
                  var10001 = false;
                  break label595;
               }

               var82 = var11;
            } else {
               var1 = ";;;";
               var82 = 0;
            }

            var11 = var82;

            label446: {
               SQLiteCursor var84;
               try {
                  if (var7.isEmpty()) {
                     break label446;
                  }

                  var84 = MessagesStorage.getInstance(ShareAlert.this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT data, name FROM chats WHERE uid IN(%s)", TextUtils.join(",", var7)));
               } catch (Exception var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label595;
               }

               label445:
               while(true) {
                  String var88;
                  label580: {
                     try {
                        if (var84.next()) {
                           var88 = var84.stringValue(1);
                           var3 = LocaleController.getInstance().getTranslitString(var88);
                           break label580;
                        }
                     } catch (Exception var49) {
                        var10000 = var49;
                        var10001 = false;
                        break label595;
                     }

                     try {
                        var84.dispose();
                     } catch (Exception var38) {
                        var10000 = var38;
                        var10001 = false;
                        break label595;
                     }

                     var11 = var82;
                     break;
                  }

                  var4 = var3;

                  label435: {
                     try {
                        if (!var88.equals(var3)) {
                           break label435;
                        }
                     } catch (Exception var48) {
                        var10000 = var48;
                        var10001 = false;
                        break label595;
                     }

                     var4 = null;
                  }

                  var11 = 0;

                  while(true) {
                     try {
                        if (var11 >= var6.length) {
                           continue label445;
                        }
                     } catch (Exception var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label595;
                     }

                     var3 = var6[var11];

                     StringBuilder var93;
                     try {
                        if (var88.startsWith(var3)) {
                           break;
                        }

                        var93 = new StringBuilder();
                        var93.append(" ");
                        var93.append(var3);
                        if (var88.contains(var93.toString())) {
                           break;
                        }
                     } catch (Exception var46) {
                        var10000 = var46;
                        var10001 = false;
                        break label595;
                     }

                     if (var4 != null) {
                        try {
                           if (var4.startsWith(var3)) {
                              break;
                           }

                           var93 = new StringBuilder();
                           var93.append(" ");
                           var93.append(var3);
                           if (var4.contains(var93.toString())) {
                              break;
                           }
                        } catch (Exception var47) {
                           var10000 = var47;
                           var10001 = false;
                           break label595;
                        }
                     }

                     ++var11;
                  }

                  NativeByteBuffer var89;
                  try {
                     var89 = var84.byteBufferValue(0);
                  } catch (Exception var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label595;
                  }

                  if (var89 != null) {
                     TLRPC.Chat var83;
                     try {
                        var83 = TLRPC.Chat.TLdeserialize(var89, var89.readInt32(false), false);
                        var89.reuse();
                     } catch (Exception var40) {
                        var10000 = var40;
                        var10001 = false;
                        break label595;
                     }

                     if (var83 != null) {
                        label410: {
                           try {
                              if (ChatObject.isNotInChat(var83)) {
                                 continue;
                              }

                              if (!ChatObject.isChannel(var83) || var83.creator || var83.admin_rights != null && var83.admin_rights.post_messages) {
                                 break label410;
                              }
                           } catch (Exception var45) {
                              var10000 = var45;
                              var10001 = false;
                              break label595;
                           }

                           try {
                              if (!var83.megagroup) {
                                 continue;
                              }
                           } catch (Exception var43) {
                              var10000 = var43;
                              var10001 = false;
                              break label595;
                           }
                        }

                        try {
                           ShareAlert.ShareSearchAdapter.DialogSearchResult var90 = (ShareAlert.ShareSearchAdapter.DialogSearchResult)var8.get(-((long)var83.id));
                           var90.name = AndroidUtilities.generateSearchName(var83.title, (String)null, var3);
                           var90.object = var83;
                           var90.dialog.id = (long)(-var83.id);
                        } catch (Exception var39) {
                           var10000 = var39;
                           var10001 = false;
                           break label595;
                        }

                        ++var82;
                     }
                  }
               }
            }

            try {
               var7 = new ArrayList(var11);
            } catch (Exception var37) {
               var10000 = var37;
               var10001 = false;
               break label595;
            }

            var82 = 0;

            ShareAlert.ShareSearchAdapter.DialogSearchResult var85;
            while(true) {
               try {
                  if (var82 >= var8.size()) {
                     break;
                  }

                  var85 = (ShareAlert.ShareSearchAdapter.DialogSearchResult)var8.valueAt(var82);
                  if (var85.object != null && var85.name != null) {
                     var7.add(var85);
                  }
               } catch (Exception var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label595;
               }

               ++var82;
            }

            try {
               var13 = MessagesStorage.getInstance(ShareAlert.this.currentAccount).getDatabase().queryFinalized("SELECT u.data, u.status, u.name, u.uid FROM users as u INNER JOIN contacts as c ON u.uid = c.uid");
            } catch (Exception var29) {
               var10000 = var29;
               var10001 = false;
               break label595;
            }

            label361:
            while(true) {
               while(true) {
                  try {
                     if (!var13.next()) {
                        break label361;
                     }

                     if (var8.indexOfKey((long)var13.intValue(3)) < 0) {
                        break;
                     }
                  } catch (Exception var35) {
                     var10000 = var35;
                     var10001 = false;
                     break label595;
                  }
               }

               try {
                  var14 = var13.stringValue(2);
                  var3 = LocaleController.getInstance().getTranslitString(var14);
               } catch (Exception var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label595;
               }

               var4 = var3;

               label351: {
                  try {
                     if (!var14.equals(var3)) {
                        break label351;
                     }
                  } catch (Exception var34) {
                     var10000 = var34;
                     var10001 = false;
                     break label595;
                  }

                  var4 = null;
               }

               try {
                  var82 = var14.lastIndexOf(var1);
               } catch (Exception var27) {
                  var10000 = var27;
                  var10001 = false;
                  break label595;
               }

               if (var82 != -1) {
                  try {
                     var3 = var14.substring(var82 + 3);
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label595;
                  }
               } else {
                  var3 = null;
               }

               try {
                  var16 = var6.length;
               } catch (Exception var25) {
                  var10000 = var25;
                  var10001 = false;
                  break label595;
               }

               var11 = 0;

               for(byte var92 = 0; var11 < var16; var92 = var5) {
                  var17 = var6[var11];

                  label586: {
                     label342: {
                        label587: {
                           try {
                              if (var14.startsWith(var17)) {
                                 break label587;
                              }

                              var18 = new StringBuilder();
                              var18.append(" ");
                              var18.append(var17);
                              if (var14.contains(var18.toString())) {
                                 break label587;
                              }
                           } catch (Exception var33) {
                              var10000 = var33;
                              var10001 = false;
                              break label595;
                           }

                           if (var4 == null) {
                              break label342;
                           }

                           try {
                              if (!var4.startsWith(var17)) {
                                 var18 = new StringBuilder();
                                 var18.append(" ");
                                 var18.append(var17);
                                 if (!var4.contains(var18.toString())) {
                                    break label342;
                                 }
                              }
                           } catch (Exception var32) {
                              var10000 = var32;
                              var10001 = false;
                              break label595;
                           }
                        }

                        var5 = 1;
                        break label586;
                     }

                     var5 = var92;
                     if (var3 != null) {
                        label594: {
                           var5 = var92;

                           try {
                              if (!var3.startsWith(var17)) {
                                 break label594;
                              }
                           } catch (Exception var31) {
                              var10000 = var31;
                              var10001 = false;
                              break label595;
                           }

                           var5 = 2;
                        }
                     }
                  }

                  if (var5 != 0) {
                     NativeByteBuffer var87;
                     try {
                        var87 = var13.byteBufferValue(0);
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label595;
                     }

                     if (var87 == null) {
                        break;
                     }

                     TLRPC.User var86;
                     try {
                        var86 = TLRPC.User.TLdeserialize(var87, var87.readInt32(false), false);
                        var87.reuse();
                        var85 = new ShareAlert.ShareSearchAdapter.DialogSearchResult();
                        if (var86.status != null) {
                           var86.status.expires = var13.intValue(1);
                        }
                     } catch (Exception var30) {
                        var10000 = var30;
                        var10001 = false;
                        break label595;
                     }

                     try {
                        var85.dialog.id = (long)var86.id;
                        var85.object = var86;
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label595;
                     }

                     if (var5 == 1) {
                        try {
                           var85.name = AndroidUtilities.generateSearchName(var86.first_name, var86.last_name, var17);
                        } catch (Exception var22) {
                           var10000 = var22;
                           var10001 = false;
                           break label595;
                        }
                     } else {
                        try {
                           var91 = new StringBuilder();
                           var91.append("@");
                           var91.append(var86.username);
                           var3 = var91.toString();
                           var91 = new StringBuilder();
                           var91.append("@");
                           var91.append(var17);
                           var85.name = AndroidUtilities.generateSearchName(var3, (String)null, var91.toString());
                        } catch (Exception var21) {
                           var10000 = var21;
                           var10001 = false;
                           break label595;
                        }
                     }

                     try {
                        var7.add(var85);
                        break;
                     } catch (Exception var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label595;
                     }
                  }

                  ++var11;
               }
            }

            try {
               var13.dispose();
               Collections.sort(var7, _$$Lambda$ShareAlert$ShareSearchAdapter$yOcjYAtW1X0Tew0aYHc9oPG3azM.INSTANCE);
               this.updateSearchResults(var7, var2);
               return;
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
            }
         }

         Exception var76 = var10000;
         FileLog.e((Throwable)var76);
      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$2$ShareAlert$ShareSearchAdapter(int var1, ArrayList var2) {
         if (var1 == this.lastSearchId) {
            ShareAlert var3;
            if (ShareAlert.this.gridView.getAdapter() != ShareAlert.this.searchAdapter) {
               var3 = ShareAlert.this;
               var3.topBeforeSwitch = var3.getCurrentTop();
               ShareAlert.this.gridView.setAdapter(ShareAlert.this.searchAdapter);
               ShareAlert.this.searchAdapter.notifyDataSetChanged();
            }

            var1 = 0;

            while(true) {
               int var4 = var2.size();
               boolean var5 = true;
               if (var1 >= var4) {
                  boolean var6;
                  if (!this.searchResult.isEmpty() && var2.isEmpty()) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  if (!this.searchResult.isEmpty() || !var2.isEmpty()) {
                     var5 = false;
                  }

                  if (var6) {
                     var3 = ShareAlert.this;
                     var3.topBeforeSwitch = var3.getCurrentTop();
                  }

                  this.searchResult = var2;
                  this.notifyDataSetChanged();
                  if (!var5 && !var6 && ShareAlert.this.topBeforeSwitch > 0) {
                     ShareAlert.this.layoutManager.scrollToPositionWithOffset(0, -ShareAlert.this.topBeforeSwitch);
                     ShareAlert.this.topBeforeSwitch = -1000;
                  }

                  ShareAlert.this.searchEmptyView.showTextView();
                  return;
               }

               TLObject var7 = ((ShareAlert.ShareSearchAdapter.DialogSearchResult)var2.get(var1)).object;
               if (var7 instanceof TLRPC.User) {
                  TLRPC.User var8 = (TLRPC.User)var7;
                  MessagesController.getInstance(ShareAlert.this.currentAccount).putUser(var8, true);
               } else if (var7 instanceof TLRPC.Chat) {
                  TLRPC.Chat var9 = (TLRPC.Chat)var7;
                  MessagesController.getInstance(ShareAlert.this.currentAccount).putChat(var9, true);
               }

               ++var1;
            }
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            ShareDialogCell var5 = (ShareDialogCell)var1.itemView;
            ArrayList var3 = this.searchResult;
            boolean var4 = true;
            ShareAlert.ShareSearchAdapter.DialogSearchResult var6 = (ShareAlert.ShareSearchAdapter.DialogSearchResult)var3.get(var2 - 1);
            var2 = (int)var6.dialog.id;
            if (ShareAlert.this.selectedDialogs.indexOfKey(var6.dialog.id) < 0) {
               var4 = false;
            }

            var5.setDialog(var2, var4, var6.name);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new View(this.context);
            ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(56.0F)));
         } else {
            var3 = new ShareDialogCell(this.context);
            ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, AndroidUtilities.dp(100.0F)));
         }

         return new RecyclerListView.Holder((View)var3);
      }

      public void searchDialogs(String var1) {
         if (var1 == null || !var1.equals(this.lastSearchText)) {
            this.lastSearchText = var1;
            if (this.searchRunnable != null) {
               Utilities.searchQueue.cancelRunnable(this.searchRunnable);
               this.searchRunnable = null;
            }

            if (var1 != null && var1.length() != 0) {
               int var2 = this.lastSearchId + 1;
               this.lastSearchId = var2;
               this.searchRunnable = new _$$Lambda$ShareAlert$ShareSearchAdapter$JorHI9Z_U3Y0Qlm94ObAFcE_y04(this, var1, var2);
               Utilities.searchQueue.postRunnable(this.searchRunnable, 300L);
            } else {
               this.searchResult.clear();
               ShareAlert var3 = ShareAlert.this;
               var3.topBeforeSwitch = var3.getCurrentTop();
               this.lastSearchId = -1;
               this.notifyDataSetChanged();
            }

         }
      }

      private class DialogSearchResult {
         public int date;
         public TLRPC.Dialog dialog;
         public CharSequence name;
         public TLObject object;

         private DialogSearchResult() {
            this.dialog = new TLRPC.TL_dialog();
         }

         // $FF: synthetic method
         DialogSearchResult(Object var2) {
            this();
         }
      }
   }
}
