package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Property;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.SharedAudioCell;
import org.telegram.ui.Cells.SharedDocumentCell;
import org.telegram.ui.Cells.SharedLinkCell;
import org.telegram.ui.Cells.SharedMediaSectionCell;
import org.telegram.ui.Cells.SharedPhotoVideoCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;

public class MediaActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private static final int delete = 4;
   private static final int forward = 3;
   private static final int gotochat = 7;
   private static final Interpolator interpolator;
   public final Property SCROLL_Y;
   private View actionModeBackground;
   private ArrayList actionModeViews;
   private int additionalPadding;
   private boolean animatingForward;
   private MediaActivity.SharedDocumentsAdapter audioAdapter;
   private ArrayList audioCache;
   private ArrayList audioCellCache;
   private MediaActivity.MediaSearchAdapter audioSearchAdapter;
   private boolean backAnimation;
   private Paint backgroundPaint;
   private ArrayList cache;
   private int cantDeleteMessagesCount;
   private ArrayList cellCache;
   private int columnsCount;
   private long dialog_id;
   private MediaActivity.SharedDocumentsAdapter documentsAdapter;
   private MediaActivity.MediaSearchAdapter documentsSearchAdapter;
   private FragmentContextView fragmentContextView;
   private ActionBarMenuItem gotoItem;
   private int[] hasMedia;
   private boolean ignoreSearchCollapse;
   protected TLRPC.ChatFull info;
   private int initialTab;
   private MediaActivity.SharedLinksAdapter linksAdapter;
   private MediaActivity.MediaSearchAdapter linksSearchAdapter;
   private int maximumVelocity;
   private MediaActivity.MediaPage[] mediaPages;
   private long mergeDialogId;
   private MediaActivity.SharedPhotoVideoAdapter photoVideoAdapter;
   private Drawable pinnedHeaderShadowDrawable;
   private ActionBarPopupWindow.ActionBarPopupWindowLayout popupLayout;
   private PhotoViewer.PhotoViewerProvider provider;
   private ScrollSlidingTextTabStrip scrollSlidingTextTabStrip;
   private boolean scrolling;
   private ActionBarMenuItem searchItem;
   private int searchItemState;
   private boolean searchWas;
   private boolean searching;
   private SparseArray[] selectedFiles;
   private NumberTextView selectedMessagesCountTextView;
   SharedLinkCell.SharedLinkCellDelegate sharedLinkCellDelegate;
   private MediaActivity.SharedMediaData[] sharedMediaData;
   private AnimatorSet tabsAnimation;
   private boolean tabsAnimationInProgress;
   private MediaActivity.SharedDocumentsAdapter voiceAdapter;

   static {
      interpolator = _$$Lambda$MediaActivity$tH1_61TdB1I4pi5VJWH_K__slwQ.INSTANCE;
   }

   public MediaActivity(Bundle var1, int[] var2) {
      this(var1, var2, (MediaActivity.SharedMediaData[])null, 0);
   }

   public MediaActivity(Bundle var1, int[] var2, MediaActivity.SharedMediaData[] var3, int var4) {
      super(var1);
      this.mediaPages = new MediaActivity.MediaPage[2];
      this.cellCache = new ArrayList(10);
      this.cache = new ArrayList(10);
      this.audioCellCache = new ArrayList(10);
      this.audioCache = new ArrayList(10);
      this.backgroundPaint = new Paint();
      this.selectedFiles = new SparseArray[]{new SparseArray(), new SparseArray()};
      this.actionModeViews = new ArrayList();
      this.info = null;
      this.columnsCount = 4;
      this.SCROLL_Y = new AnimationProperties.FloatProperty("animationValue") {
         public Float get(MediaActivity var1) {
            return MediaActivity.access$300(MediaActivity.this).getTranslationY();
         }

         public void setValue(MediaActivity var1, float var2) {
            var1.setScrollY(var2);

            for(int var3 = 0; var3 < MediaActivity.this.mediaPages.length; ++var3) {
               MediaActivity.this.mediaPages[var3].listView.checkSection();
            }

         }
      };
      this.provider = new PhotoViewer.EmptyPhotoViewerProvider() {
         public PhotoViewer.PlaceProviderObject getPlaceForPhoto(MessageObject var1, TLRPC.FileLocation var2, int var3, boolean var4) {
            if (var1 != null && (MediaActivity.this.mediaPages[0].selectedType == 0 || MediaActivity.this.mediaPages[0].selectedType == 1)) {
               int var5 = MediaActivity.this.mediaPages[0].listView.getChildCount();

               for(var3 = 0; var3 < var5; ++var3) {
                  View var10 = MediaActivity.this.mediaPages[0].listView.getChildAt(var3);
                  int var7;
                  BackupImageView var12;
                  if (var10 instanceof SharedPhotoVideoCell) {
                     SharedPhotoVideoCell var6 = (SharedPhotoVideoCell)var10;
                     var12 = null;

                     for(var7 = 0; var7 < 6; ++var7) {
                        MessageObject var8 = var6.getMessageObject(var7);
                        if (var8 == null) {
                           break;
                        }

                        if (var8.getId() == var1.getId()) {
                           var12 = var6.getImageView(var7);
                        }
                     }
                  } else {
                     label71: {
                        if (var10 instanceof SharedDocumentCell) {
                           SharedDocumentCell var11 = (SharedDocumentCell)var10;
                           if (var11.getMessage().getId() == var1.getId()) {
                              var12 = var11.getImageView();
                              break label71;
                           }
                        }

                        var12 = null;
                     }
                  }

                  if (var12 != null) {
                     int[] var13 = new int[2];
                     var12.getLocationInWindow(var13);
                     PhotoViewer.PlaceProviderObject var9 = new PhotoViewer.PlaceProviderObject();
                     var9.viewX = var13[0];
                     var7 = var13[1];
                     if (VERSION.SDK_INT >= 21) {
                        var3 = 0;
                     } else {
                        var3 = AndroidUtilities.statusBarHeight;
                     }

                     var9.viewY = var7 - var3;
                     var9.parentView = MediaActivity.this.mediaPages[0].listView;
                     var9.imageReceiver = var12.getImageReceiver();
                     var9.thumb = var9.imageReceiver.getBitmapSafe();
                     var9.parentView.getLocationInWindow(var13);
                     var9.clipTopAddition = (int)((float)MediaActivity.access$500(MediaActivity.this).getHeight() + MediaActivity.access$600(MediaActivity.this).getTranslationY());
                     if (MediaActivity.this.fragmentContextView != null && MediaActivity.this.fragmentContextView.getVisibility() == 0) {
                        var9.clipTopAddition += AndroidUtilities.dp(36.0F);
                     }

                     return var9;
                  }
               }
            }

            return null;
         }
      };
      this.sharedMediaData = new MediaActivity.SharedMediaData[5];
      this.sharedLinkCellDelegate = new SharedLinkCell.SharedLinkCellDelegate() {
         public boolean canPerformActions() {
            return MediaActivity.access$9700(MediaActivity.this).isActionModeShowed() ^ true;
         }

         // $FF: synthetic method
         public void lambda$onLinkLongPress$0$MediaActivity$15(String var1, DialogInterface var2, int var3) {
            if (var3 == 0) {
               Browser.openUrl(MediaActivity.this.getParentActivity(), (String)var1, true);
            } else if (var3 == 1) {
               String var4;
               if (var1.startsWith("mailto:")) {
                  var4 = var1.substring(7);
               } else {
                  var4 = var1;
                  if (var1.startsWith("tel:")) {
                     var4 = var1.substring(4);
                  }
               }

               AndroidUtilities.addToClipboard(var4);
            }

         }

         public void needOpenWebView(TLRPC.WebPage var1) {
            MediaActivity.this.openWebView(var1);
         }

         public void onLinkLongPress(String var1) {
            BottomSheet.Builder var2 = new BottomSheet.Builder(MediaActivity.this.getParentActivity());
            var2.setTitle(var1);
            String var3 = LocaleController.getString("Open", 2131560110);
            String var4 = LocaleController.getString("Copy", 2131559163);
            _$$Lambda$MediaActivity$15$jEYZUoDNUJ8Uqel_4NLZ8joo4_I var5 = new _$$Lambda$MediaActivity$15$jEYZUoDNUJ8Uqel_4NLZ8joo4_I(this, var1);
            var2.setItems(new CharSequence[]{var3, var4}, var5);
            MediaActivity.this.showDialog(var2.create());
         }
      };
      this.hasMedia = var2;
      this.initialTab = var4;
      this.dialog_id = var1.getLong("dialog_id", 0L);
      var4 = 0;

      while(true) {
         MediaActivity.SharedMediaData[] var6 = this.sharedMediaData;
         if (var4 >= var6.length) {
            return;
         }

         var6[var4] = new MediaActivity.SharedMediaData();
         int[] var7 = this.sharedMediaData[var4].max_id;
         int var5;
         if ((int)this.dialog_id == 0) {
            var5 = Integer.MIN_VALUE;
         } else {
            var5 = Integer.MAX_VALUE;
         }

         var7[0] = var5;
         if (this.mergeDialogId != 0L && this.info != null) {
            this.sharedMediaData[var4].max_id[1] = this.info.migrated_from_max_id;
            this.sharedMediaData[var4].endReached[1] = false;
         }

         if (var3 != null) {
            this.sharedMediaData[var4].totalCount = var3[var4].totalCount;
            this.sharedMediaData[var4].messages.addAll(var3[var4].messages);
            this.sharedMediaData[var4].sections.addAll(var3[var4].sections);
            Iterator var9 = var3[var4].sectionArrays.entrySet().iterator();

            while(var9.hasNext()) {
               Entry var8 = (Entry)var9.next();
               this.sharedMediaData[var4].sectionArrays.put(var8.getKey(), new ArrayList((Collection)var8.getValue()));
            }

            for(var5 = 0; var5 < 2; ++var5) {
               this.sharedMediaData[var4].messagesDict[var5] = var3[var4].messagesDict[var5].clone();
               this.sharedMediaData[var4].max_id[var5] = var3[var4].max_id[var5];
            }
         }

         ++var4;
      }
   }

   // $FF: synthetic method
   static ActionBar access$10200(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$10300(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$10700(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$11000(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$11100(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$11200(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$11300(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$11400(MediaActivity var0) {
      return var0.classGuid;
   }

   // $FF: synthetic method
   static int access$11500(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$11600(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$11800(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$11900(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$12000(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$12100(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$12200(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$12300(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$12400(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$12500(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1600(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$1900(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$2200(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2300(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2400(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2600(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2700(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$2800(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$2900(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$300(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$3000(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3100(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3200(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3300(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$3400(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$3500(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static boolean access$3602(MediaActivity var0, boolean var1) {
      var0.swipeBackEnabled = var1;
      return var1;
   }

   // $FF: synthetic method
   static ActionBar access$4900(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$500(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$5000(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$5100(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$5500(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$5600(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$5700(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$5800(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$5900(MediaActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBar access$600(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$6000(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$6100(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$6200(MediaActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBar access$6700(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$6800(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$6900(MediaActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static boolean access$7202(MediaActivity var0, boolean var1) {
      var0.swipeBackEnabled = var1;
      return var1;
   }

   // $FF: synthetic method
   static ActionBar access$7500(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$7600(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$8000(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$8200(MediaActivity var0) {
      return var0.classGuid;
   }

   // $FF: synthetic method
   static int access$8300(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$8400(MediaActivity var0) {
      return var0.classGuid;
   }

   // $FF: synthetic method
   static int access$8500(MediaActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$8600(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$8700(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$8800(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$8900(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$9200(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$9300(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$9700(MediaActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$9800(MediaActivity var0) {
      return var0.actionBar;
   }

   private void fixLayoutInternal(int var1) {
      int var2 = ((WindowManager)ApplicationLoader.applicationContext.getSystemService("window")).getDefaultDisplay().getRotation();
      if (var1 == 0) {
         if (!AndroidUtilities.isTablet() && ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == 2) {
            this.selectedMessagesCountTextView.setTextSize(18);
         } else {
            this.selectedMessagesCountTextView.setTextSize(20);
         }
      }

      if (AndroidUtilities.isTablet()) {
         this.columnsCount = 4;
         this.mediaPages[var1].emptyTextView.setPadding(AndroidUtilities.dp(40.0F), 0, AndroidUtilities.dp(40.0F), AndroidUtilities.dp(128.0F));
      } else if (var2 != 3 && var2 != 1) {
         this.columnsCount = 4;
         this.mediaPages[var1].emptyTextView.setPadding(AndroidUtilities.dp(40.0F), 0, AndroidUtilities.dp(40.0F), AndroidUtilities.dp(128.0F));
      } else {
         this.columnsCount = 6;
         this.mediaPages[var1].emptyTextView.setPadding(AndroidUtilities.dp(40.0F), 0, AndroidUtilities.dp(40.0F), 0);
      }

      if (var1 == 0) {
         this.photoVideoAdapter.notifyDataSetChanged();
      }

   }

   // $FF: synthetic method
   static boolean lambda$createView$1(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static boolean lambda$createView$4(View var0, MotionEvent var1) {
      return true;
   }

   // $FF: synthetic method
   static float lambda$static$0(float var0) {
      --var0;
      return var0 * var0 * var0 * var0 * var0 + 1.0F;
   }

   private void onItemClick(int var1, View var2, MessageObject var3, int var4, int var5) {
      if (var3 != null) {
         boolean var6 = super.actionBar.isActionModeShowed();
         Object var7 = null;
         boolean var8 = false;
         boolean var9 = false;
         boolean var10 = false;
         boolean var11 = false;
         SharedDocumentCell var22;
         if (var6) {
            byte var21;
            if (var3.getDialogId() == this.dialog_id) {
               var21 = 0;
            } else {
               var21 = 1;
            }

            if (this.selectedFiles[var21].indexOfKey(var3.getId()) >= 0) {
               this.selectedFiles[var21].remove(var3.getId());
               if (!var3.canDeleteMessage((TLRPC.Chat)null)) {
                  --this.cantDeleteMessagesCount;
               }
            } else {
               if (this.selectedFiles[0].size() + this.selectedFiles[1].size() >= 100) {
                  return;
               }

               this.selectedFiles[var21].put(var3.getId(), var3);
               if (!var3.canDeleteMessage((TLRPC.Chat)null)) {
                  ++this.cantDeleteMessagesCount;
               }
            }

            if (this.selectedFiles[0].size() == 0 && this.selectedFiles[1].size() == 0) {
               super.actionBar.hideActionMode();
            } else {
               this.selectedMessagesCountTextView.setNumber(this.selectedFiles[0].size() + this.selectedFiles[1].size(), true);
               ActionBarMenuItem var12 = super.actionBar.createActionMode().getItem(4);
               var5 = this.cantDeleteMessagesCount;
               byte var13 = 8;
               byte var29;
               if (var5 == 0) {
                  var29 = 0;
               } else {
                  var29 = 8;
               }

               var12.setVisibility(var29);
               var12 = this.gotoItem;
               if (var12 != null) {
                  var29 = var13;
                  if (this.selectedFiles[0].size() == 1) {
                     var29 = 0;
                  }

                  var12.setVisibility(var29);
               }
            }

            this.scrolling = false;
            if (var2 instanceof SharedDocumentCell) {
               var22 = (SharedDocumentCell)var2;
               if (this.selectedFiles[var21].indexOfKey(var3.getId()) >= 0) {
                  var11 = true;
               }

               var22.setChecked(var11, true);
            } else if (var2 instanceof SharedPhotoVideoCell) {
               SharedPhotoVideoCell var23 = (SharedPhotoVideoCell)var2;
               if (this.selectedFiles[var21].indexOfKey(var3.getId()) >= 0) {
                  var11 = true;
               } else {
                  var11 = var8;
               }

               var23.setChecked(var4, var11, true);
            } else if (var2 instanceof SharedLinkCell) {
               SharedLinkCell var24 = (SharedLinkCell)var2;
               var11 = var9;
               if (this.selectedFiles[var21].indexOfKey(var3.getId()) >= 0) {
                  var11 = true;
               }

               var24.setChecked(var11, true);
            } else if (var2 instanceof SharedAudioCell) {
               SharedAudioCell var25 = (SharedAudioCell)var2;
               var11 = var10;
               if (this.selectedFiles[var21].indexOfKey(var3.getId()) >= 0) {
                  var11 = true;
               }

               var25.setChecked(var11, true);
            }
         } else if (var5 == 0) {
            PhotoViewer.getInstance().setParentActivity(this.getParentActivity());
            PhotoViewer.getInstance().openPhoto(this.sharedMediaData[var5].messages, var1, this.dialog_id, this.mergeDialogId, this.provider);
         } else if (var5 != 2 && var5 != 4) {
            if (var5 == 1) {
               if (var2 instanceof SharedDocumentCell) {
                  var22 = (SharedDocumentCell)var2;
                  TLRPC.Document var30 = var3.getDocument();
                  if (var22.isLoaded()) {
                     if (var3.canPreviewDocument()) {
                        PhotoViewer.getInstance().setParentActivity(this.getParentActivity());
                        var1 = this.sharedMediaData[var5].messages.indexOf(var3);
                        if (var1 < 0) {
                           ArrayList var27 = new ArrayList();
                           var27.add(var3);
                           PhotoViewer.getInstance().openPhoto(var27, 0, 0L, 0L, this.provider);
                        } else {
                           PhotoViewer.getInstance().openPhoto(this.sharedMediaData[var5].messages, var1, this.dialog_id, this.mergeDialogId, this.provider);
                        }

                        return;
                     }

                     AndroidUtilities.openDocument(var3, this.getParentActivity(), this);
                  } else if (!var22.isLoading()) {
                     var3 = var22.getMessage();
                     FileLoader.getInstance(super.currentAccount).loadFile(var30, var3, 0, 0);
                     var22.updateFileExistIcon();
                  } else {
                     FileLoader.getInstance(super.currentAccount).cancelLoadFile(var30);
                     var22.updateFileExistIcon();
                  }
               }
            } else if (var5 == 3) {
               Exception var10000;
               label178: {
                  TLRPC.WebPage var14;
                  boolean var10001;
                  try {
                     var14 = var3.messageOwner.media.webpage;
                  } catch (Exception var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label178;
                  }

                  String var31 = (String)var7;
                  if (var14 != null) {
                     label179: {
                        var31 = (String)var7;

                        try {
                           if (var14 instanceof TLRPC.TL_webPageEmpty) {
                              break label179;
                           }

                           if (var14.cached_page != null) {
                              ArticleViewer.getInstance().setParentActivity(this.getParentActivity(), this);
                              ArticleViewer.getInstance().open(var3);
                              return;
                           }
                        } catch (Exception var19) {
                           var10000 = var19;
                           var10001 = false;
                           break label178;
                        }

                        try {
                           if (var14.embed_url != null && var14.embed_url.length() != 0) {
                              this.openWebView(var14);
                              return;
                           }
                        } catch (Exception var18) {
                           var10000 = var18;
                           var10001 = false;
                           break label178;
                        }

                        try {
                           var31 = var14.url;
                        } catch (Exception var17) {
                           var10000 = var17;
                           var10001 = false;
                           break label178;
                        }
                     }
                  }

                  String var26 = var31;
                  if (var31 == null) {
                     try {
                        var26 = ((SharedLinkCell)var2).getLink(0);
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label178;
                     }
                  }

                  if (var26 == null) {
                     return;
                  }

                  try {
                     Browser.openUrl(this.getParentActivity(), (String)var26);
                     return;
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                  }
               }

               Exception var28 = var10000;
               FileLog.e((Throwable)var28);
            }
         } else if (var2 instanceof SharedAudioCell) {
            ((SharedAudioCell)var2).didPressedButton();
         }

      }
   }

   private boolean onItemLongClick(MessageObject var1, View var2, int var3) {
      if (!super.actionBar.isActionModeShowed() && this.getParentActivity() != null) {
         AndroidUtilities.hideKeyboard(this.getParentActivity().getCurrentFocus());
         SparseArray[] var4 = this.selectedFiles;
         byte var5;
         if (var1.getDialogId() == this.dialog_id) {
            var5 = 0;
         } else {
            var5 = 1;
         }

         var4[var5].put(var1.getId(), var1);
         if (!var1.canDeleteMessage((TLRPC.Chat)null)) {
            ++this.cantDeleteMessagesCount;
         }

         ActionBarMenuItem var7 = super.actionBar.createActionMode().getItem(4);
         if (this.cantDeleteMessagesCount == 0) {
            var5 = 0;
         } else {
            var5 = 8;
         }

         var7.setVisibility(var5);
         var7 = this.gotoItem;
         if (var7 != null) {
            var7.setVisibility(0);
         }

         this.selectedMessagesCountTextView.setNumber(1, false);
         AnimatorSet var8 = new AnimatorSet();
         ArrayList var6 = new ArrayList();

         for(int var10 = 0; var10 < this.actionModeViews.size(); ++var10) {
            View var9 = (View)this.actionModeViews.get(var10);
            AndroidUtilities.clearDrawableAnimation(var9);
            var6.add(ObjectAnimator.ofFloat(var9, View.SCALE_Y, new float[]{0.1F, 1.0F}));
         }

         var8.playTogether(var6);
         var8.setDuration(250L);
         var8.start();
         this.scrolling = false;
         if (var2 instanceof SharedDocumentCell) {
            ((SharedDocumentCell)var2).setChecked(true, true);
         } else if (var2 instanceof SharedPhotoVideoCell) {
            ((SharedPhotoVideoCell)var2).setChecked(var3, true, true);
         } else if (var2 instanceof SharedLinkCell) {
            ((SharedLinkCell)var2).setChecked(true, true);
         } else if (var2 instanceof SharedAudioCell) {
            ((SharedAudioCell)var2).setChecked(true, true);
         }

         if (!super.actionBar.isActionModeShowed()) {
            super.actionBar.showActionMode((View)null, this.actionModeBackground, (View[])null, (boolean[])null, (View)null, 0);
            this.resetScroll();
         }

         return true;
      } else {
         return false;
      }
   }

   private void openWebView(TLRPC.WebPage var1) {
      EmbedBottomSheet.show(this.getParentActivity(), var1.site_name, var1.description, var1.url, var1.embed_url, var1.embed_width, var1.embed_height);
   }

   private void recycleAdapter(RecyclerView.Adapter var1) {
      if (var1 instanceof MediaActivity.SharedPhotoVideoAdapter) {
         this.cellCache.addAll(this.cache);
         this.cache.clear();
      } else if (var1 == this.audioAdapter) {
         this.audioCellCache.addAll(this.audioCache);
         this.audioCache.clear();
      }

   }

   private void resetScroll() {
      if (super.actionBar.getTranslationY() != 0.0F) {
         AnimatorSet var1 = new AnimatorSet();
         var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(this, this.SCROLL_Y, new float[]{0.0F})});
         var1.setInterpolator(new DecelerateInterpolator());
         var1.setDuration(180L);
         var1.start();
      }
   }

   private void setScrollY(float var1) {
      super.actionBar.setTranslationY(var1);
      FragmentContextView var2 = this.fragmentContextView;
      if (var2 != null) {
         var2.setTranslationY((float)this.additionalPadding + var1);
      }

      int var3 = 0;

      while(true) {
         MediaActivity.MediaPage[] var4 = this.mediaPages;
         if (var3 >= var4.length) {
            super.fragmentView.invalidate();
            return;
         }

         var4[var3].listView.setPinnedSectionOffsetY((int)var1);
         ++var3;
      }
   }

   private void switchToCurrentSelectedMode(boolean var1) {
      int var2 = 0;

      while(true) {
         MediaActivity.MediaPage[] var3 = this.mediaPages;
         if (var2 >= var3.length) {
            RecyclerView.Adapter var6 = var3[var1].listView.getAdapter();
            if (this.searching && this.searchWas) {
               if (var1 != 0) {
                  if (this.mediaPages[var1].selectedType == 0 || this.mediaPages[var1].selectedType == 2) {
                     this.searching = false;
                     this.searchWas = false;
                     this.switchToCurrentSelectedMode(true);
                     return;
                  }

                  String var4 = this.searchItem.getSearchField().getText().toString();
                  MediaActivity.MediaSearchAdapter var5;
                  if (this.mediaPages[var1].selectedType == 1) {
                     var5 = this.documentsSearchAdapter;
                     if (var5 != null) {
                        var5.search(var4);
                        if (var6 != this.documentsSearchAdapter) {
                           this.recycleAdapter(var6);
                           this.mediaPages[var1].listView.setAdapter(this.documentsSearchAdapter);
                        }
                     }
                  } else if (this.mediaPages[var1].selectedType == 3) {
                     var5 = this.linksSearchAdapter;
                     if (var5 != null) {
                        var5.search(var4);
                        if (var6 != this.linksSearchAdapter) {
                           this.recycleAdapter(var6);
                           this.mediaPages[var1].listView.setAdapter(this.linksSearchAdapter);
                        }
                     }
                  } else if (this.mediaPages[var1].selectedType == 4) {
                     var5 = this.audioSearchAdapter;
                     if (var5 != null) {
                        var5.search(var4);
                        if (var6 != this.audioSearchAdapter) {
                           this.recycleAdapter(var6);
                           this.mediaPages[var1].listView.setAdapter(this.audioSearchAdapter);
                        }
                     }
                  }

                  if (this.searchItemState != 2 && this.mediaPages[var1].emptyTextView != null) {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoResult", 2131559943));
                     this.mediaPages[var1].emptyTextView.setPadding(AndroidUtilities.dp(40.0F), 0, AndroidUtilities.dp(40.0F), AndroidUtilities.dp(30.0F));
                     this.mediaPages[var1].emptyTextView.setTextSize(1, 20.0F);
                     this.mediaPages[var1].emptyImageView.setVisibility(8);
                  }
               } else {
                  if (this.mediaPages[var1].listView != null) {
                     if (this.mediaPages[var1].selectedType == 1) {
                        if (var6 != this.documentsSearchAdapter) {
                           this.recycleAdapter(var6);
                           this.mediaPages[var1].listView.setAdapter(this.documentsSearchAdapter);
                        }

                        this.documentsSearchAdapter.notifyDataSetChanged();
                     } else if (this.mediaPages[var1].selectedType == 3) {
                        if (var6 != this.linksSearchAdapter) {
                           this.recycleAdapter(var6);
                           this.mediaPages[var1].listView.setAdapter(this.linksSearchAdapter);
                        }

                        this.linksSearchAdapter.notifyDataSetChanged();
                     } else if (this.mediaPages[var1].selectedType == 4) {
                        if (var6 != this.audioSearchAdapter) {
                           this.recycleAdapter(var6);
                           this.mediaPages[var1].listView.setAdapter(this.audioSearchAdapter);
                        }

                        this.audioSearchAdapter.notifyDataSetChanged();
                     }
                  }

                  if (this.searchItemState != 2 && this.mediaPages[var1].emptyTextView != null) {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoResult", 2131559943));
                     this.mediaPages[var1].emptyTextView.setPadding(AndroidUtilities.dp(40.0F), 0, AndroidUtilities.dp(40.0F), AndroidUtilities.dp(30.0F));
                     this.mediaPages[var1].emptyTextView.setTextSize(1, 20.0F);
                     this.mediaPages[var1].emptyImageView.setVisibility(8);
                  }
               }
            } else {
               this.mediaPages[var1].emptyTextView.setTextSize(1, 17.0F);
               this.mediaPages[var1].emptyImageView.setVisibility(0);
               this.mediaPages[var1].listView.setPinnedHeaderShadowDrawable((Drawable)null);
               if (this.mediaPages[var1].selectedType == 0) {
                  if (var6 != this.photoVideoAdapter) {
                     this.recycleAdapter(var6);
                     this.mediaPages[var1].listView.setAdapter(this.photoVideoAdapter);
                  }

                  this.mediaPages[var1].listView.setPinnedHeaderShadowDrawable(this.pinnedHeaderShadowDrawable);
                  this.mediaPages[var1].emptyImageView.setImageResource(2131165875);
                  if ((int)this.dialog_id == 0) {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoMediaSecret", 2131559931));
                  } else {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoMedia", 2131559929));
                  }
               } else if (this.mediaPages[var1].selectedType == 1) {
                  if (var6 != this.documentsAdapter) {
                     this.recycleAdapter(var6);
                     this.mediaPages[var1].listView.setAdapter(this.documentsAdapter);
                  }

                  this.mediaPages[var1].emptyImageView.setImageResource(2131165876);
                  if ((int)this.dialog_id == 0) {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoSharedFilesSecret", 2131559947));
                  } else {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoSharedFiles", 2131559946));
                  }
               } else if (this.mediaPages[var1].selectedType == 2) {
                  if (var6 != this.voiceAdapter) {
                     this.recycleAdapter(var6);
                     this.mediaPages[var1].listView.setAdapter(this.voiceAdapter);
                  }

                  this.mediaPages[var1].emptyImageView.setImageResource(2131165879);
                  if ((int)this.dialog_id == 0) {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoSharedVoiceSecret", 2131559951));
                  } else {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoSharedVoice", 2131559950));
                  }
               } else if (this.mediaPages[var1].selectedType == 3) {
                  if (var6 != this.linksAdapter) {
                     this.recycleAdapter(var6);
                     this.mediaPages[var1].listView.setAdapter(this.linksAdapter);
                  }

                  this.mediaPages[var1].emptyImageView.setImageResource(2131165877);
                  if ((int)this.dialog_id == 0) {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoSharedLinksSecret", 2131559949));
                  } else {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoSharedLinks", 2131559948));
                  }
               } else if (this.mediaPages[var1].selectedType == 4) {
                  if (var6 != this.audioAdapter) {
                     this.recycleAdapter(var6);
                     this.mediaPages[var1].listView.setAdapter(this.audioAdapter);
                  }

                  this.mediaPages[var1].emptyImageView.setImageResource(2131165878);
                  if ((int)this.dialog_id == 0) {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoSharedAudioSecret", 2131559945));
                  } else {
                     this.mediaPages[var1].emptyTextView.setText(LocaleController.getString("NoSharedAudio", 2131559944));
                  }
               }

               this.mediaPages[var1].emptyTextView.setPadding(AndroidUtilities.dp(40.0F), 0, AndroidUtilities.dp(40.0F), AndroidUtilities.dp(128.0F));
               if (this.mediaPages[var1].selectedType != 0 && this.mediaPages[var1].selectedType != 2) {
                  if (var1 != 0) {
                     if (this.searchItem.getVisibility() == 4 && !super.actionBar.isSearchFieldVisible()) {
                        this.searchItemState = 1;
                        this.searchItem.setVisibility(0);
                        this.searchItem.setAlpha(0.0F);
                     } else {
                        this.searchItemState = 0;
                     }
                  } else if (this.searchItem.getVisibility() == 4) {
                     this.searchItemState = 0;
                     this.searchItem.setAlpha(1.0F);
                     this.searchItem.setVisibility(0);
                  }
               } else if (var1 != 0) {
                  this.searchItemState = 2;
               } else {
                  this.searchItemState = 0;
                  this.searchItem.setVisibility(4);
               }

               if (!this.sharedMediaData[this.mediaPages[var1].selectedType].loading && !this.sharedMediaData[this.mediaPages[var1].selectedType].endReached[0] && this.sharedMediaData[this.mediaPages[var1].selectedType].messages.isEmpty()) {
                  this.sharedMediaData[this.mediaPages[var1].selectedType].loading = true;
                  DataQuery.getInstance(super.currentAccount).loadMedia(this.dialog_id, 50, 0, this.mediaPages[var1].selectedType, 1, super.classGuid);
               }

               if (this.sharedMediaData[this.mediaPages[var1].selectedType].loading && this.sharedMediaData[this.mediaPages[var1].selectedType].messages.isEmpty()) {
                  this.mediaPages[var1].progressView.setVisibility(0);
                  this.mediaPages[var1].listView.setEmptyView((View)null);
                  this.mediaPages[var1].emptyView.setVisibility(8);
               } else {
                  this.mediaPages[var1].progressView.setVisibility(8);
                  this.mediaPages[var1].listView.setEmptyView(this.mediaPages[var1].emptyView);
               }

               this.mediaPages[var1].listView.setVisibility(0);
            }

            if (this.searchItemState == 2 && super.actionBar.isSearchFieldVisible()) {
               this.ignoreSearchCollapse = true;
               super.actionBar.closeSearchField();
            }

            if (super.actionBar.getTranslationY() != 0.0F) {
               this.mediaPages[var1].layoutManager.scrollToPositionWithOffset(0, (int)super.actionBar.getTranslationY());
            }

            return;
         }

         var3[var2].listView.stopScroll();
         ++var2;
      }
   }

   private void updateRowsSelection() {
      int var1 = 0;

      while(true) {
         MediaActivity.MediaPage[] var2 = this.mediaPages;
         if (var1 >= var2.length) {
            return;
         }

         int var3 = var2[var1].listView.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            View var6 = this.mediaPages[var1].listView.getChildAt(var4);
            if (var6 instanceof SharedDocumentCell) {
               ((SharedDocumentCell)var6).setChecked(false, true);
            } else if (var6 instanceof SharedPhotoVideoCell) {
               for(int var5 = 0; var5 < 6; ++var5) {
                  ((SharedPhotoVideoCell)var6).setChecked(var5, false, true);
               }
            } else if (var6 instanceof SharedLinkCell) {
               ((SharedLinkCell)var6).setChecked(false, true);
            } else if (var6 instanceof SharedAudioCell) {
               ((SharedAudioCell)var6).setChecked(false, true);
            }
         }

         ++var1;
      }
   }

   private void updateSections(ViewGroup var1, boolean var2) {
      int var3 = var1.getChildCount();
      float var4 = (float)var1.getPaddingTop() + super.actionBar.getTranslationY();
      int var5 = 0;
      View var6 = null;
      int var7 = 0;

      int var10;
      for(int var8 = Integer.MAX_VALUE; var5 < var3; var8 = var10) {
         View var9 = var1.getChildAt(var5);
         var10 = var9.getBottom();
         var7 = Math.max(var10, var7);
         View var11;
         if ((float)var10 <= var4) {
            var11 = var6;
            var10 = var8;
         } else {
            label52: {
               int var12 = var9.getBottom();
               if (!(var9 instanceof SharedMediaSectionCell)) {
                  var11 = var6;
                  var10 = var8;
                  if (!(var9 instanceof GraySectionCell)) {
                     break label52;
                  }
               }

               if (var9.getAlpha() != 1.0F) {
                  var9.setAlpha(1.0F);
               }

               var11 = var6;
               var10 = var8;
               if (var12 < var8) {
                  var11 = var9;
                  var10 = var12;
               }
            }
         }

         ++var5;
         var6 = var11;
      }

      if (var6 != null) {
         if ((float)var6.getTop() > var4) {
            if (var6.getAlpha() != 1.0F) {
               var6.setAlpha(1.0F);
            }
         } else if (var6.getAlpha() != 0.0F) {
            var6.setAlpha(0.0F);
         }
      }

      if (var2 && var7 != 0 && var7 < var1.getMeasuredHeight() - var1.getPaddingBottom()) {
         this.resetScroll();
      }

   }

   private void updateTabs() {
      if (this.scrollSlidingTextTabStrip != null) {
         int[] var1 = this.hasMedia;
         boolean var2;
         if ((var1[0] != 0 || var1[1] == 0 && var1[2] == 0 && var1[3] == 0 && var1[4] == 0) && !this.scrollSlidingTextTabStrip.hasTab(0)) {
            var2 = true;
         } else {
            var2 = false;
         }

         boolean var3 = var2;
         if (this.hasMedia[1] != 0) {
            var3 = var2;
            if (!this.scrollSlidingTextTabStrip.hasTab(1)) {
               var3 = true;
            }
         }

         TLRPC.EncryptedChat var5;
         label119: {
            if ((int)this.dialog_id != 0) {
               boolean var4 = var3;
               if (this.hasMedia[3] != 0) {
                  var4 = var3;
                  if (!this.scrollSlidingTextTabStrip.hasTab(3)) {
                     var4 = true;
                  }
               }

               var2 = var4;
               if (this.hasMedia[4] == 0) {
                  break label119;
               }

               var2 = var4;
               if (this.scrollSlidingTextTabStrip.hasTab(4)) {
                  break label119;
               }
            } else {
               var5 = MessagesController.getInstance(super.currentAccount).getEncryptedChat((int)(this.dialog_id >> 32));
               var2 = var3;
               if (var5 == null) {
                  break label119;
               }

               var2 = var3;
               if (AndroidUtilities.getPeerLayerVersion(var5.layer) < 46) {
                  break label119;
               }

               var2 = var3;
               if (this.hasMedia[4] == 0) {
                  break label119;
               }

               var2 = var3;
               if (this.scrollSlidingTextTabStrip.hasTab(4)) {
                  break label119;
               }
            }

            var2 = true;
         }

         var3 = var2;
         if (this.hasMedia[2] != 0) {
            var3 = var2;
            if (!this.scrollSlidingTextTabStrip.hasTab(2)) {
               var3 = true;
            }
         }

         if (var3) {
            this.scrollSlidingTextTabStrip.removeTabs();
            var1 = this.hasMedia;
            if ((var1[0] != 0 || var1[1] == 0 && var1[2] == 0 && var1[3] == 0 && var1[4] == 0) && !this.scrollSlidingTextTabStrip.hasTab(0)) {
               this.scrollSlidingTextTabStrip.addTextTab(0, LocaleController.getString("SharedMediaTab", 2131560767));
            }

            if (this.hasMedia[1] != 0 && !this.scrollSlidingTextTabStrip.hasTab(1)) {
               this.scrollSlidingTextTabStrip.addTextTab(1, LocaleController.getString("SharedFilesTab", 2131560763));
            }

            if ((int)this.dialog_id != 0) {
               if (this.hasMedia[3] != 0 && !this.scrollSlidingTextTabStrip.hasTab(3)) {
                  this.scrollSlidingTextTabStrip.addTextTab(3, LocaleController.getString("SharedLinksTab", 2131560765));
               }

               if (this.hasMedia[4] != 0 && !this.scrollSlidingTextTabStrip.hasTab(4)) {
                  this.scrollSlidingTextTabStrip.addTextTab(4, LocaleController.getString("SharedMusicTab", 2131560768));
               }
            } else {
               var5 = MessagesController.getInstance(super.currentAccount).getEncryptedChat((int)(this.dialog_id >> 32));
               if (var5 != null && AndroidUtilities.getPeerLayerVersion(var5.layer) >= 46 && this.hasMedia[4] != 0 && !this.scrollSlidingTextTabStrip.hasTab(4)) {
                  this.scrollSlidingTextTabStrip.addTextTab(4, LocaleController.getString("SharedMusicTab", 2131560768));
               }
            }

            if (this.hasMedia[2] != 0 && !this.scrollSlidingTextTabStrip.hasTab(2)) {
               this.scrollSlidingTextTabStrip.addTextTab(2, LocaleController.getString("SharedVoiceTab", 2131560771));
            }
         }

         if (this.scrollSlidingTextTabStrip.getTabsCount() <= 1) {
            this.scrollSlidingTextTabStrip.setVisibility(8);
            super.actionBar.setExtraHeight(0);
         } else {
            this.scrollSlidingTextTabStrip.setVisibility(0);
            super.actionBar.setExtraHeight(AndroidUtilities.dp(44.0F));
         }

         int var6 = this.scrollSlidingTextTabStrip.getCurrentTabId();
         if (var6 >= 0) {
            this.mediaPages[0].selectedType = var6;
         }

         this.scrollSlidingTextTabStrip.finishAddingTabs();
      }
   }

   public View createView(Context var1) {
      boolean var2 = false;

      int var3;
      for(var3 = 0; var3 < 10; ++var3) {
         this.cellCache.add(new SharedPhotoVideoCell(var1));
         if (this.initialTab == 4) {
            SharedAudioCell var4 = new SharedAudioCell(var1) {
               public boolean needPlayMessage(MessageObject var1) {
                  if (!var1.isVoice() && !var1.isRoundVideo()) {
                     return var1.isMusic() ? MediaController.getInstance().setPlaylist(MediaActivity.this.sharedMediaData[4].messages, var1) : false;
                  } else {
                     boolean var2 = MediaController.getInstance().playMessage(var1);
                     MediaController var3 = MediaController.getInstance();
                     ArrayList var4;
                     if (var2) {
                        var4 = MediaActivity.this.sharedMediaData[4].messages;
                     } else {
                        var4 = null;
                     }

                     var3.setVoiceMessagesPlaylist(var4, false);
                     return var2;
                  }
               }
            };
            var4.initStreamingIcons();
            this.audioCellCache.add(var4);
         }
      }

      this.maximumVelocity = ViewConfiguration.get(var1).getScaledMaximumFlingVelocity();
      this.searching = false;
      this.searchWas = false;
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setBackButtonDrawable(new BackDrawable(false));
      super.actionBar.setAddToContainer(false);
      super.actionBar.setClipContent(true);
      var3 = (int)this.dialog_id;
      TLRPC.User var13;
      if (var3 != 0) {
         if (var3 > 0) {
            var13 = MessagesController.getInstance(super.currentAccount).getUser(var3);
            if (var13 != null) {
               if (var13.self) {
                  super.actionBar.setTitle(LocaleController.getString("SavedMessages", 2131560633));
               } else {
                  super.actionBar.setTitle(ContactsController.formatName(var13.first_name, var13.last_name));
               }
            }
         } else {
            TLRPC.Chat var14 = MessagesController.getInstance(super.currentAccount).getChat(-var3);
            if (var14 != null) {
               super.actionBar.setTitle(var14.title);
            }
         }
      } else {
         TLRPC.EncryptedChat var15 = MessagesController.getInstance(super.currentAccount).getEncryptedChat((int)(this.dialog_id >> 32));
         if (var15 != null) {
            var13 = MessagesController.getInstance(super.currentAccount).getUser(var15.user_id);
            if (var13 != null) {
               super.actionBar.setTitle(ContactsController.formatName(var13.first_name, var13.last_name));
            }
         }
      }

      if (TextUtils.isEmpty(super.actionBar.getTitle())) {
         super.actionBar.setTitle(LocaleController.getString("SharedContentTitle", 2131560762));
      }

      super.actionBar.setExtraHeight(AndroidUtilities.dp(44.0F));
      super.actionBar.setAllowOverlayTitle(false);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         // $FF: synthetic method
         public void lambda$onItemClick$0$MediaActivity$4() {
            MediaActivity.access$3400(MediaActivity.this).hideActionMode();
            MediaActivity.access$3500(MediaActivity.this).closeSearchField();
            MediaActivity.this.cantDeleteMessagesCount = 0;
         }

         // $FF: synthetic method
         public void lambda$onItemClick$1$MediaActivity$4(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
            ArrayList var5 = new ArrayList();
            int var6 = 1;

            while(true) {
               byte var7 = 0;
               int var8 = 0;
               if (var6 < 0) {
                  MediaActivity.this.cantDeleteMessagesCount = 0;
                  MediaActivity.access$2800(MediaActivity.this).hideActionMode();
                  long var11;
                  if (var2.size() <= 1 && (Long)var2.get(0) != (long)UserConfig.getInstance(MediaActivity.access$2900(MediaActivity.this)).getClientUserId() && var3 == null) {
                     var11 = (Long)var2.get(0);
                     var8 = (int)var11;
                     var6 = (int)(var11 >> 32);
                     Bundle var14 = new Bundle();
                     var14.putBoolean("scrollToTopOnResume", true);
                     if (var8 != 0) {
                        if (var8 > 0) {
                           var14.putInt("user_id", var8);
                        } else if (var8 < 0) {
                           var14.putInt("chat_id", -var8);
                        }
                     } else {
                        var14.putInt("enc_id", var6);
                     }

                     if (var8 != 0 && !MessagesController.getInstance(MediaActivity.access$3200(MediaActivity.this)).checkCanOpenChat(var14, var1)) {
                        return;
                     }

                     NotificationCenter.getInstance(MediaActivity.access$3300(MediaActivity.this)).postNotificationName(NotificationCenter.closeChats);
                     ChatActivity var13 = new ChatActivity(var14);
                     MediaActivity.this.presentFragment(var13, true);
                     var13.showFieldPanelForForward(true, var5);
                     if (!AndroidUtilities.isTablet()) {
                        MediaActivity.this.removeSelfFromStack();
                     }
                  } else {
                     MediaActivity.this.updateRowsSelection();

                     for(var6 = var7; var6 < var2.size(); ++var6) {
                        var11 = (Long)var2.get(var6);
                        if (var3 != null) {
                           SendMessagesHelper.getInstance(MediaActivity.access$3000(MediaActivity.this)).sendMessage(var3.toString(), var11, (MessageObject)null, (TLRPC.WebPage)null, true, (ArrayList)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
                        }

                        SendMessagesHelper.getInstance(MediaActivity.access$3100(MediaActivity.this)).sendMessage(var5, var11);
                     }

                     var1.finishFragment();
                  }

                  return;
               }

               ArrayList var9;
               for(var9 = new ArrayList(); var8 < MediaActivity.this.selectedFiles[var6].size(); ++var8) {
                  var9.add(MediaActivity.this.selectedFiles[var6].keyAt(var8));
               }

               Collections.sort(var9);
               Iterator var15 = var9.iterator();

               while(var15.hasNext()) {
                  Integer var10 = (Integer)var15.next();
                  if (var10 > 0) {
                     var5.add(MediaActivity.this.selectedFiles[var6].get(var10));
                  }
               }

               MediaActivity.this.selectedFiles[var6].clear();
               --var6;
            }
         }

         public void onItemClick(int var1) {
            byte var2 = 1;
            if (var1 == -1) {
               if (MediaActivity.access$1600(MediaActivity.this).isActionModeShowed()) {
                  for(var1 = var2; var1 >= 0; --var1) {
                     MediaActivity.this.selectedFiles[var1].clear();
                  }

                  MediaActivity.this.cantDeleteMessagesCount = 0;
                  MediaActivity.access$1900(MediaActivity.this).hideActionMode();
                  MediaActivity.this.updateRowsSelection();
               } else {
                  MediaActivity.this.finishFragment();
               }
            } else if (var1 == 4) {
               var1 = (int)MediaActivity.this.dialog_id;
               TLRPC.User var3;
               Object var4;
               Object var5;
               if (var1 != 0) {
                  if (var1 > 0) {
                     var3 = MessagesController.getInstance(MediaActivity.access$2200(MediaActivity.this)).getUser(var1);
                     var4 = null;
                     var5 = var4;
                  } else {
                     var4 = MessagesController.getInstance(MediaActivity.access$2300(MediaActivity.this)).getChat(-var1);
                     var3 = null;
                     var5 = var3;
                  }
               } else {
                  var5 = MessagesController.getInstance(MediaActivity.access$2400(MediaActivity.this)).getEncryptedChat((int)(MediaActivity.this.dialog_id >> 32));
                  var3 = null;
                  var4 = var3;
               }

               MediaActivity var6 = MediaActivity.this;
               AlertsCreator.createDeleteMessagesAlert(var6, var3, (TLRPC.Chat)var4, (TLRPC.EncryptedChat)var5, (TLRPC.ChatFull)null, var6.mergeDialogId, (MessageObject)null, MediaActivity.this.selectedFiles, (MessageObject.GroupedMessages)null, 1, new _$$Lambda$MediaActivity$4$aYBqp6dz0HCr_EWMktVHdulVPRM(this));
            } else if (var1 == 3) {
               Bundle var8 = new Bundle();
               var8.putBoolean("onlySelect", true);
               var8.putInt("dialogsType", 3);
               DialogsActivity var9 = new DialogsActivity(var8);
               var9.setDelegate(new _$$Lambda$MediaActivity$4$oszW3hNDwc0SI1livFARQcBcLgc(this));
               MediaActivity.this.presentFragment(var9);
            } else if (var1 == 7) {
               if (MediaActivity.this.selectedFiles[0].size() != 1) {
                  return;
               }

               Bundle var11 = new Bundle();
               int var7 = (int)MediaActivity.this.dialog_id;
               var1 = (int)(MediaActivity.this.dialog_id >> 32);
               if (var7 != 0) {
                  if (var1 == 1) {
                     var11.putInt("chat_id", var7);
                  } else if (var7 > 0) {
                     var11.putInt("user_id", var7);
                  } else if (var7 < 0) {
                     TLRPC.Chat var10 = MessagesController.getInstance(MediaActivity.access$2600(MediaActivity.this)).getChat(-var7);
                     var1 = var7;
                     if (var10 != null) {
                        var1 = var7;
                        if (var10.migrated_to != null) {
                           var11.putInt("migrated_to", var7);
                           var1 = -var10.migrated_to.channel_id;
                        }
                     }

                     var11.putInt("chat_id", -var1);
                  }
               } else {
                  var11.putInt("enc_id", var1);
               }

               var11.putInt("message_id", MediaActivity.this.selectedFiles[0].keyAt(0));
               NotificationCenter.getInstance(MediaActivity.access$2700(MediaActivity.this)).postNotificationName(NotificationCenter.closeChats);
               MediaActivity.this.presentFragment(new ChatActivity(var11), true);
            }

         }
      });
      this.pinnedHeaderShadowDrawable = var1.getResources().getDrawable(2131165758);
      this.pinnedHeaderShadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundGrayShadow"), Mode.MULTIPLY));
      ScrollSlidingTextTabStrip var17 = this.scrollSlidingTextTabStrip;
      if (var17 != null) {
         this.initialTab = var17.getCurrentTabId();
      }

      this.scrollSlidingTextTabStrip = new ScrollSlidingTextTabStrip(var1);
      var3 = this.initialTab;
      if (var3 != -1) {
         this.scrollSlidingTextTabStrip.setInitialTabId(var3);
         this.initialTab = -1;
      }

      super.actionBar.addView(this.scrollSlidingTextTabStrip, LayoutHelper.createFrame(-1, 44, 83));
      this.scrollSlidingTextTabStrip.setDelegate(new ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate() {
         public void onPageScrolled(float var1) {
            if (var1 != 1.0F || MediaActivity.this.mediaPages[1].getVisibility() == 0) {
               if (MediaActivity.this.animatingForward) {
                  MediaActivity.this.mediaPages[0].setTranslationX(-var1 * (float)MediaActivity.this.mediaPages[0].getMeasuredWidth());
                  MediaActivity.this.mediaPages[1].setTranslationX((float)MediaActivity.this.mediaPages[0].getMeasuredWidth() - (float)MediaActivity.this.mediaPages[0].getMeasuredWidth() * var1);
               } else {
                  MediaActivity.this.mediaPages[0].setTranslationX((float)MediaActivity.this.mediaPages[0].getMeasuredWidth() * var1);
                  MediaActivity.this.mediaPages[1].setTranslationX((float)MediaActivity.this.mediaPages[0].getMeasuredWidth() * var1 - (float)MediaActivity.this.mediaPages[0].getMeasuredWidth());
               }

               if (MediaActivity.this.searchItemState == 1) {
                  MediaActivity.this.searchItem.setAlpha(var1);
               } else if (MediaActivity.this.searchItemState == 2) {
                  MediaActivity.this.searchItem.setAlpha(1.0F - var1);
               }

               if (var1 == 1.0F) {
                  MediaActivity.MediaPage var2 = MediaActivity.this.mediaPages[0];
                  MediaActivity.this.mediaPages[0] = MediaActivity.this.mediaPages[1];
                  MediaActivity.this.mediaPages[1] = var2;
                  MediaActivity.this.mediaPages[1].setVisibility(8);
                  if (MediaActivity.this.searchItemState == 2) {
                     MediaActivity.this.searchItem.setVisibility(4);
                  }

                  MediaActivity.this.searchItemState = 0;
               }

            }
         }

         public void onPageSelected(int var1, boolean var2) {
            if (MediaActivity.this.mediaPages[0].selectedType != var1) {
               MediaActivity var3 = MediaActivity.this;
               boolean var4;
               if (var1 == var3.scrollSlidingTextTabStrip.getFirstTabId()) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               MediaActivity.access$3602(var3, var4);
               MediaActivity.this.mediaPages[1].selectedType = var1;
               MediaActivity.this.mediaPages[1].setVisibility(0);
               MediaActivity.this.switchToCurrentSelectedMode(true);
               MediaActivity.this.animatingForward = var2;
            }
         }
      });

      for(var3 = 1; var3 >= 0; --var3) {
         this.selectedFiles[var3].clear();
      }

      this.cantDeleteMessagesCount = 0;
      this.actionModeViews.clear();
      this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public void onSearchCollapse() {
            MediaActivity.this.searching = false;
            MediaActivity.this.searchWas = false;
            MediaActivity.this.documentsSearchAdapter.search((String)null);
            MediaActivity.this.linksSearchAdapter.search((String)null);
            MediaActivity.this.audioSearchAdapter.search((String)null);
            if (MediaActivity.this.ignoreSearchCollapse) {
               MediaActivity.this.ignoreSearchCollapse = false;
            } else {
               MediaActivity.this.switchToCurrentSelectedMode(false);
            }
         }

         public void onSearchExpand() {
            MediaActivity.this.searching = true;
            MediaActivity.this.resetScroll();
         }

         public void onTextChanged(EditText var1) {
            String var2 = var1.getText().toString();
            if (var2.length() != 0) {
               MediaActivity.this.searchWas = true;
               MediaActivity.this.switchToCurrentSelectedMode(false);
            } else {
               MediaActivity.this.searchWas = false;
               MediaActivity.this.switchToCurrentSelectedMode(false);
            }

            if (MediaActivity.this.mediaPages[0].selectedType == 1) {
               if (MediaActivity.this.documentsSearchAdapter == null) {
                  return;
               }

               MediaActivity.this.documentsSearchAdapter.search(var2);
            } else if (MediaActivity.this.mediaPages[0].selectedType == 3) {
               if (MediaActivity.this.linksSearchAdapter == null) {
                  return;
               }

               MediaActivity.this.linksSearchAdapter.search(var2);
            } else if (MediaActivity.this.mediaPages[0].selectedType == 4) {
               if (MediaActivity.this.audioSearchAdapter == null) {
                  return;
               }

               MediaActivity.this.audioSearchAdapter.search(var2);
            }

         }
      });
      this.searchItem.setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      this.searchItem.setContentDescription(LocaleController.getString("Search", 2131560640));
      this.searchItem.setVisibility(4);
      this.searchItemState = 0;
      super.hasOwnBackground = true;
      ActionBarMenu var19 = super.actionBar.createActionMode(false);
      var19.setBackgroundDrawable((Drawable)null);
      super.actionBar.setItemsColor(Theme.getColor("actionBarDefaultIcon"), true);
      super.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarDefaultSelector"), true);
      this.actionModeBackground = new View(var1);
      this.actionModeBackground.setBackgroundColor(Theme.getColor("sharedMedia_actionMode"));
      this.actionModeBackground.setAlpha(0.0F);
      ActionBar var5 = super.actionBar;
      var5.addView(this.actionModeBackground, var5.indexOfChild(var19));
      this.selectedMessagesCountTextView = new NumberTextView(var19.getContext());
      this.selectedMessagesCountTextView.setTextSize(18);
      this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.selectedMessagesCountTextView.setTextColor(Theme.getColor("actionBarDefaultIcon"));
      this.selectedMessagesCountTextView.setOnTouchListener(_$$Lambda$MediaActivity$44_9wkbXSuSmSVGID3HV1JW_tzw.INSTANCE);
      var19.addView(this.selectedMessagesCountTextView, LayoutHelper.createLinear(0, -1, 1.0F, 72, 0, 0, 0));
      if ((int)this.dialog_id != 0) {
         ArrayList var16 = this.actionModeViews;
         ActionBarMenuItem var6 = var19.addItemWithWidth(7, 2131165647, AndroidUtilities.dp(54.0F), LocaleController.getString("AccDescrGoToMessage", 2131558436));
         this.gotoItem = var6;
         var16.add(var6);
         this.actionModeViews.add(var19.addItemWithWidth(3, 2131165627, AndroidUtilities.dp(54.0F), LocaleController.getString("Forward", 2131559504)));
      }

      this.actionModeViews.add(var19.addItemWithWidth(4, 2131165623, AndroidUtilities.dp(54.0F), LocaleController.getString("Delete", 2131559227)));
      this.photoVideoAdapter = new MediaActivity.SharedPhotoVideoAdapter(var1);
      this.documentsAdapter = new MediaActivity.SharedDocumentsAdapter(var1, 1);
      this.voiceAdapter = new MediaActivity.SharedDocumentsAdapter(var1, 2);
      this.audioAdapter = new MediaActivity.SharedDocumentsAdapter(var1, 4);
      this.documentsSearchAdapter = new MediaActivity.MediaSearchAdapter(var1, 1);
      this.audioSearchAdapter = new MediaActivity.MediaSearchAdapter(var1, 4);
      this.linksSearchAdapter = new MediaActivity.MediaSearchAdapter(var1, 3);
      this.linksAdapter = new MediaActivity.SharedLinksAdapter(var1);
      FrameLayout var21 = new FrameLayout(var1) {
         private boolean globalIgnoreLayout;
         private boolean maybeStartTracking;
         private boolean startedTracking;
         private int startedTrackingPointerId;
         private int startedTrackingX;
         private int startedTrackingY;
         private VelocityTracker velocityTracker;

         private boolean prepareForMoving(MotionEvent var1, boolean var2) {
            int var3 = MediaActivity.this.scrollSlidingTextTabStrip.getNextPageId(var2);
            if (var3 < 0) {
               return false;
            } else {
               if (MediaActivity.this.searchItemState != 0) {
                  if (MediaActivity.this.searchItemState == 2) {
                     MediaActivity.this.searchItem.setAlpha(1.0F);
                  } else if (MediaActivity.this.searchItemState == 1) {
                     MediaActivity.this.searchItem.setAlpha(0.0F);
                     MediaActivity.this.searchItem.setVisibility(4);
                  }

                  MediaActivity.this.searchItemState = 0;
               }

               this.getParent().requestDisallowInterceptTouchEvent(true);
               this.maybeStartTracking = false;
               this.startedTracking = true;
               this.startedTrackingX = (int)var1.getX();
               MediaActivity.access$4900(MediaActivity.this).setEnabled(false);
               MediaActivity.this.scrollSlidingTextTabStrip.setEnabled(false);
               MediaActivity.this.mediaPages[1].selectedType = var3;
               MediaActivity.this.mediaPages[1].setVisibility(0);
               MediaActivity.this.animatingForward = var2;
               MediaActivity.this.switchToCurrentSelectedMode(true);
               if (var2) {
                  MediaActivity.this.mediaPages[1].setTranslationX((float)MediaActivity.this.mediaPages[0].getMeasuredWidth());
               } else {
                  MediaActivity.this.mediaPages[1].setTranslationX((float)(-MediaActivity.this.mediaPages[0].getMeasuredWidth()));
               }

               return true;
            }
         }

         public boolean checkTabsAnimationInProgress() {
            if (!MediaActivity.this.tabsAnimationInProgress) {
               return false;
            } else {
               boolean var6;
               label35: {
                  boolean var1 = MediaActivity.this.backAnimation;
                  byte var2 = -1;
                  boolean var3 = true;
                  MediaActivity.MediaPage var4;
                  int var5;
                  if (var1) {
                     if (Math.abs(MediaActivity.this.mediaPages[0].getTranslationX()) < 1.0F) {
                        MediaActivity.this.mediaPages[0].setTranslationX(0.0F);
                        var4 = MediaActivity.this.mediaPages[1];
                        var5 = MediaActivity.this.mediaPages[0].getMeasuredWidth();
                        if (MediaActivity.this.animatingForward) {
                           var2 = 1;
                        }

                        var4.setTranslationX((float)(var5 * var2));
                        var6 = var3;
                        break label35;
                     }
                  } else if (Math.abs(MediaActivity.this.mediaPages[1].getTranslationX()) < 1.0F) {
                     var4 = MediaActivity.this.mediaPages[0];
                     var5 = MediaActivity.this.mediaPages[0].getMeasuredWidth();
                     if (!MediaActivity.this.animatingForward) {
                        var2 = 1;
                     }

                     var4.setTranslationX((float)(var5 * var2));
                     MediaActivity.this.mediaPages[1].setTranslationX(0.0F);
                     var6 = var3;
                     break label35;
                  }

                  var6 = false;
               }

               if (var6) {
                  if (MediaActivity.this.tabsAnimation != null) {
                     MediaActivity.this.tabsAnimation.cancel();
                     MediaActivity.this.tabsAnimation = null;
                  }

                  MediaActivity.this.tabsAnimationInProgress = false;
               }

               return MediaActivity.this.tabsAnimationInProgress;
            }
         }

         protected void dispatchDraw(Canvas var1) {
            super.dispatchDraw(var1);
            if (MediaActivity.access$5900(MediaActivity.this) != null) {
               MediaActivity.access$6200(MediaActivity.this).drawHeaderShadow(var1, MediaActivity.access$6000(MediaActivity.this).getMeasuredHeight() + (int)MediaActivity.access$6100(MediaActivity.this).getTranslationY());
            }

         }

         public void forceHasOverlappingRendering(boolean var1) {
            super.forceHasOverlappingRendering(var1);
         }

         protected void onDraw(Canvas var1) {
            MediaActivity.this.backgroundPaint.setColor(Theme.getColor("windowBackgroundWhite"));
            var1.drawRect(0.0F, (float)MediaActivity.access$6700(MediaActivity.this).getMeasuredHeight() + MediaActivity.access$6800(MediaActivity.this).getTranslationY(), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), MediaActivity.this.backgroundPaint);
         }

         public boolean onInterceptTouchEvent(MotionEvent var1) {
            boolean var2;
            if (!this.checkTabsAnimationInProgress() && !MediaActivity.this.scrollSlidingTextTabStrip.isAnimatingIndicator() && !this.onTouchEvent(var1)) {
               var2 = false;
            } else {
               var2 = true;
            }

            return var2;
         }

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            super.onLayout(var1, var2, var3, var4, var5);
            if (MediaActivity.this.fragmentContextView != null) {
               var2 = MediaActivity.access$5600(MediaActivity.this).getMeasuredHeight();
               MediaActivity.this.fragmentContextView.layout(MediaActivity.this.fragmentContextView.getLeft(), MediaActivity.this.fragmentContextView.getTop() + var2, MediaActivity.this.fragmentContextView.getRight(), MediaActivity.this.fragmentContextView.getBottom() + var2);
            }

         }

         protected void onMeasure(int var1, int var2) {
            this.setMeasuredDimension(MeasureSpec.getSize(var1), MeasureSpec.getSize(var2));
            this.measureChildWithMargins(MediaActivity.access$5000(MediaActivity.this), var1, 0, var2, 0);
            int var3 = MediaActivity.access$5100(MediaActivity.this).getMeasuredHeight();
            this.globalIgnoreLayout = true;
            byte var4 = 0;

            int var5;
            for(var5 = 0; var5 < MediaActivity.this.mediaPages.length; ++var5) {
               if (MediaActivity.this.mediaPages[var5] != null) {
                  if (MediaActivity.this.mediaPages[var5].listView != null) {
                     MediaActivity.this.mediaPages[var5].listView.setPadding(0, MediaActivity.this.additionalPadding + var3, 0, AndroidUtilities.dp(4.0F));
                  }

                  if (MediaActivity.this.mediaPages[var5].emptyView != null) {
                     MediaActivity.this.mediaPages[var5].emptyView.setPadding(0, MediaActivity.this.additionalPadding + var3, 0, 0);
                  }

                  if (MediaActivity.this.mediaPages[var5].progressView != null) {
                     MediaActivity.this.mediaPages[var5].progressView.setPadding(0, MediaActivity.this.additionalPadding + var3, 0, 0);
                  }
               }
            }

            this.globalIgnoreLayout = false;
            var3 = this.getChildCount();

            for(var5 = var4; var5 < var3; ++var5) {
               View var6 = this.getChildAt(var5);
               if (var6 != null && var6.getVisibility() != 8 && var6 != MediaActivity.access$5500(MediaActivity.this)) {
                  this.measureChildWithMargins(var6, var1, 0, var2, 0);
               }
            }

         }

         public boolean onTouchEvent(MotionEvent var1) {
            if (!MediaActivity.access$6900(MediaActivity.this).checkTransitionAnimation() && !this.checkTabsAnimationInProgress()) {
               boolean var2 = true;
               VelocityTracker var11;
               if (var1 != null && var1.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                  this.startedTrackingPointerId = var1.getPointerId(0);
                  this.maybeStartTracking = true;
                  this.startedTrackingX = (int)var1.getX();
                  this.startedTrackingY = (int)var1.getY();
                  var11 = this.velocityTracker;
                  if (var11 != null) {
                     var11.clear();
                  }
               } else {
                  int var3;
                  int var4;
                  boolean var5;
                  float var6;
                  if (var1 != null && var1.getAction() == 2 && var1.getPointerId(0) == this.startedTrackingPointerId) {
                     if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                     }

                     var3 = (int)(var1.getX() - (float)this.startedTrackingX);
                     var4 = Math.abs((int)var1.getY() - this.startedTrackingY);
                     this.velocityTracker.addMovement(var1);
                     if (this.startedTracking && (MediaActivity.this.animatingForward && var3 > 0 || !MediaActivity.this.animatingForward && var3 < 0)) {
                        if (var3 < 0) {
                           var5 = true;
                        } else {
                           var5 = false;
                        }

                        if (!this.prepareForMoving(var1, var5)) {
                           this.maybeStartTracking = true;
                           this.startedTracking = false;
                           MediaActivity.this.mediaPages[0].setTranslationX(0.0F);
                           if (MediaActivity.this.animatingForward) {
                              MediaActivity.this.mediaPages[1].setTranslationX((float)MediaActivity.this.mediaPages[0].getMeasuredWidth());
                           } else {
                              MediaActivity.this.mediaPages[1].setTranslationX((float)(-MediaActivity.this.mediaPages[0].getMeasuredWidth()));
                           }
                        }
                     }

                     if (this.maybeStartTracking && !this.startedTracking) {
                        var6 = AndroidUtilities.getPixelsInCM(0.3F, true);
                        if ((float)Math.abs(var3) >= var6 && Math.abs(var3) / 3 > var4) {
                           if (var3 < 0) {
                              var5 = var2;
                           } else {
                              var5 = false;
                           }

                           this.prepareForMoving(var1, var5);
                        }
                     } else if (this.startedTracking) {
                        MediaActivity.this.mediaPages[0].setTranslationX((float)var3);
                        if (MediaActivity.this.animatingForward) {
                           MediaActivity.this.mediaPages[1].setTranslationX((float)(MediaActivity.this.mediaPages[0].getMeasuredWidth() + var3));
                        } else {
                           MediaActivity.this.mediaPages[1].setTranslationX((float)(var3 - MediaActivity.this.mediaPages[0].getMeasuredWidth()));
                        }

                        var6 = (float)Math.abs(var3) / (float)MediaActivity.this.mediaPages[0].getMeasuredWidth();
                        if (MediaActivity.this.searchItemState == 2) {
                           MediaActivity.this.searchItem.setAlpha(1.0F - var6);
                        } else if (MediaActivity.this.searchItemState == 1) {
                           MediaActivity.this.searchItem.setAlpha(var6);
                        }

                        MediaActivity.this.scrollSlidingTextTabStrip.selectTabWithId(MediaActivity.this.mediaPages[1].selectedType, var6);
                     }
                  } else if (var1 != null && var1.getPointerId(0) == this.startedTrackingPointerId && (var1.getAction() == 3 || var1.getAction() == 1 || var1.getAction() == 6)) {
                     if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                     }

                     this.velocityTracker.computeCurrentVelocity(1000, (float)MediaActivity.this.maximumVelocity);
                     float var7;
                     if (!this.startedTracking) {
                        var7 = this.velocityTracker.getXVelocity();
                        var6 = this.velocityTracker.getYVelocity();
                        if (Math.abs(var7) >= 3000.0F && Math.abs(var7) > Math.abs(var6)) {
                           if (var7 < 0.0F) {
                              var5 = true;
                           } else {
                              var5 = false;
                           }

                           this.prepareForMoving(var1, var5);
                        }
                     }

                     if (!this.startedTracking) {
                        this.maybeStartTracking = false;
                        this.startedTracking = false;
                        MediaActivity.access$7600(MediaActivity.this).setEnabled(true);
                        MediaActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                     } else {
                        var6 = MediaActivity.this.mediaPages[0].getX();
                        MediaActivity.this.tabsAnimation = new AnimatorSet();
                        var7 = this.velocityTracker.getXVelocity();
                        float var8 = this.velocityTracker.getYVelocity();
                        MediaActivity var10 = MediaActivity.this;
                        if (Math.abs(var6) >= (float)MediaActivity.this.mediaPages[0].getMeasuredWidth() / 3.0F || Math.abs(var7) >= 3500.0F && Math.abs(var7) >= Math.abs(var8)) {
                           var5 = false;
                        } else {
                           var5 = true;
                        }

                        var10.backAnimation = var5;
                        if (MediaActivity.this.backAnimation) {
                           var6 = Math.abs(var6);
                           if (MediaActivity.this.animatingForward) {
                              MediaActivity.this.tabsAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(MediaActivity.this.mediaPages[0], View.TRANSLATION_X, new float[]{0.0F}), ObjectAnimator.ofFloat(MediaActivity.this.mediaPages[1], View.TRANSLATION_X, new float[]{(float)MediaActivity.this.mediaPages[1].getMeasuredWidth()})});
                           } else {
                              MediaActivity.this.tabsAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(MediaActivity.this.mediaPages[0], View.TRANSLATION_X, new float[]{0.0F}), ObjectAnimator.ofFloat(MediaActivity.this.mediaPages[1], View.TRANSLATION_X, new float[]{(float)(-MediaActivity.this.mediaPages[1].getMeasuredWidth())})});
                           }
                        } else {
                           var6 = (float)MediaActivity.this.mediaPages[0].getMeasuredWidth() - Math.abs(var6);
                           if (MediaActivity.this.animatingForward) {
                              MediaActivity.this.tabsAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(MediaActivity.this.mediaPages[0], View.TRANSLATION_X, new float[]{(float)(-MediaActivity.this.mediaPages[0].getMeasuredWidth())}), ObjectAnimator.ofFloat(MediaActivity.this.mediaPages[1], View.TRANSLATION_X, new float[]{0.0F})});
                           } else {
                              MediaActivity.this.tabsAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(MediaActivity.this.mediaPages[0], View.TRANSLATION_X, new float[]{(float)MediaActivity.this.mediaPages[0].getMeasuredWidth()}), ObjectAnimator.ofFloat(MediaActivity.this.mediaPages[1], View.TRANSLATION_X, new float[]{0.0F})});
                           }
                        }

                        MediaActivity.this.tabsAnimation.setInterpolator(MediaActivity.interpolator);
                        var3 = this.getMeasuredWidth();
                        var4 = var3 / 2;
                        float var9 = Math.min(1.0F, var6 * 1.0F / (float)var3);
                        var8 = (float)var4;
                        var9 = AndroidUtilities.distanceInfluenceForSnapDuration(var9);
                        var7 = Math.abs(var7);
                        if (var7 > 0.0F) {
                           var4 = Math.round(Math.abs((var8 + var9 * var8) / var7) * 1000.0F) * 4;
                        } else {
                           var4 = (int)((var6 / (float)this.getMeasuredWidth() + 1.0F) * 100.0F);
                        }

                        var4 = Math.max(150, Math.min(var4, 600));
                        MediaActivity.this.tabsAnimation.setDuration((long)var4);
                        MediaActivity.this.tabsAnimation.addListener(new AnimatorListenerAdapter() {
                           public void onAnimationEnd(Animator var1) {
                              MediaActivity.this.tabsAnimation = null;
                              if (MediaActivity.this.backAnimation) {
                                 MediaActivity.this.mediaPages[1].setVisibility(8);
                                 if (MediaActivity.this.searchItemState == 2) {
                                    MediaActivity.this.searchItem.setAlpha(1.0F);
                                 } else if (MediaActivity.this.searchItemState == 1) {
                                    MediaActivity.this.searchItem.setAlpha(0.0F);
                                    MediaActivity.this.searchItem.setVisibility(4);
                                 }

                                 MediaActivity.this.searchItemState = 0;
                              } else {
                                 MediaActivity.MediaPage var3 = MediaActivity.this.mediaPages[0];
                                 MediaActivity.this.mediaPages[0] = MediaActivity.this.mediaPages[1];
                                 MediaActivity.this.mediaPages[1] = var3;
                                 MediaActivity.this.mediaPages[1].setVisibility(8);
                                 if (MediaActivity.this.searchItemState == 2) {
                                    MediaActivity.this.searchItem.setVisibility(4);
                                 }

                                 MediaActivity.this.searchItemState = 0;
                                 MediaActivity var4 = MediaActivity.this;
                                 boolean var2;
                                 if (var4.mediaPages[0].selectedType == MediaActivity.this.scrollSlidingTextTabStrip.getFirstTabId()) {
                                    var2 = true;
                                 } else {
                                    var2 = false;
                                 }

                                 MediaActivity.access$7202(var4, var2);
                                 MediaActivity.this.scrollSlidingTextTabStrip.selectTabWithId(MediaActivity.this.mediaPages[0].selectedType, 1.0F);
                              }

                              MediaActivity.this.tabsAnimationInProgress = false;
                              maybeStartTracking = false;
                              startedTracking = false;
                              MediaActivity.access$7500(MediaActivity.this).setEnabled(true);
                              MediaActivity.this.scrollSlidingTextTabStrip.setEnabled(true);
                           }
                        });
                        MediaActivity.this.tabsAnimation.start();
                        MediaActivity.this.tabsAnimationInProgress = true;
                     }

                     var11 = this.velocityTracker;
                     if (var11 != null) {
                        var11.recycle();
                        this.velocityTracker = null;
                     }
                  }
               }

               return this.startedTracking;
            } else {
               return false;
            }
         }

         public void requestLayout() {
            if (!this.globalIgnoreLayout) {
               super.requestLayout();
            }
         }

         public void setPadding(int var1, int var2, int var3, int var4) {
            MediaActivity.this.additionalPadding = var2;
            if (MediaActivity.this.fragmentContextView != null) {
               MediaActivity.this.fragmentContextView.setTranslationY((float)var2 + MediaActivity.access$5700(MediaActivity.this).getTranslationY());
            }

            var2 = MediaActivity.access$5800(MediaActivity.this).getMeasuredHeight();

            for(var1 = 0; var1 < MediaActivity.this.mediaPages.length; ++var1) {
               if (MediaActivity.this.mediaPages[var1] != null) {
                  if (MediaActivity.this.mediaPages[var1].emptyView != null) {
                     MediaActivity.this.mediaPages[var1].emptyView.setPadding(0, MediaActivity.this.additionalPadding + var2, 0, 0);
                  }

                  if (MediaActivity.this.mediaPages[var1].progressView != null) {
                     MediaActivity.this.mediaPages[var1].progressView.setPadding(0, MediaActivity.this.additionalPadding + var2, 0, 0);
                  }

                  if (MediaActivity.this.mediaPages[var1].listView != null) {
                     MediaActivity.this.mediaPages[var1].listView.setPadding(0, MediaActivity.this.additionalPadding + var2, 0, AndroidUtilities.dp(4.0F));
                     MediaActivity.this.mediaPages[var1].listView.checkSection();
                  }
               }
            }

         }
      };
      super.fragmentView = var21;
      var21.setWillNotDraw(false);
      int var7 = 0;
      int var8 = -1;
      int var9 = 0;

      while(true) {
         MediaActivity.MediaPage[] var18 = this.mediaPages;
         if (var7 >= var18.length) {
            if (!AndroidUtilities.isTablet()) {
               FragmentContextView var12 = new FragmentContextView(var1, this, false);
               this.fragmentContextView = var12;
               var21.addView(var12, LayoutHelper.createFrame(-1, 39.0F, 51, 0.0F, 8.0F, 0.0F, 0.0F));
            }

            var21.addView(super.actionBar, LayoutHelper.createFrame(-1, -2.0F));
            this.updateTabs();
            this.switchToCurrentSelectedMode(false);
            if (this.scrollSlidingTextTabStrip.getCurrentTabId() == this.scrollSlidingTextTabStrip.getFirstTabId()) {
               var2 = true;
            }

            super.swipeBackEnabled = var2;
            return super.fragmentView;
         }

         var3 = var8;
         int var10 = var9;
         if (var7 == 0) {
            var3 = var8;
            var10 = var9;
            if (var18[var7] != null) {
               var3 = var8;
               var10 = var9;
               if (var18[var7].layoutManager != null) {
                  label116: {
                     var3 = this.mediaPages[var7].layoutManager.findFirstVisibleItemPosition();
                     if (var3 != this.mediaPages[var7].layoutManager.getItemCount() - 1) {
                        RecyclerListView.Holder var20 = (RecyclerListView.Holder)this.mediaPages[var7].listView.findViewHolderForAdapterPosition(var3);
                        if (var20 != null) {
                           var10 = var20.itemView.getTop();
                           break label116;
                        }
                     }

                     var3 = -1;
                     var10 = var9;
                  }
               }
            }
         }

         final MediaActivity.MediaPage var22 = new MediaActivity.MediaPage(var1) {
            public void setTranslationX(float var1) {
               super.setTranslationX(var1);
               if (MediaActivity.this.tabsAnimationInProgress && MediaActivity.this.mediaPages[0] == this) {
                  var1 = Math.abs(MediaActivity.this.mediaPages[0].getTranslationX()) / (float)MediaActivity.this.mediaPages[0].getMeasuredWidth();
                  MediaActivity.this.scrollSlidingTextTabStrip.selectTabWithId(MediaActivity.this.mediaPages[1].selectedType, var1);
                  if (MediaActivity.this.searchItemState == 2) {
                     MediaActivity.this.searchItem.setAlpha(1.0F - var1);
                  } else if (MediaActivity.this.searchItemState == 1) {
                     MediaActivity.this.searchItem.setAlpha(var1);
                  }
               }

            }
         };
         var21.addView(var22, LayoutHelper.createFrame(-1, -1.0F));
         MediaActivity.MediaPage[] var23 = this.mediaPages;
         var23[var7] = var22;
         MediaActivity.MediaPage var11 = var23[var7];
         final LinearLayoutManager var24 = new LinearLayoutManager(var1, 1, false) {
            public boolean supportsPredictiveItemAnimations() {
               return false;
            }
         };
         var11.layoutManager = var24;
         this.mediaPages[var7].listView = new RecyclerListView(var1) {
            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               super.onLayout(var1, var2, var3, var4, var5);
               MediaActivity.this.updateSections(this, true);
            }
         };
         this.mediaPages[var7].listView.setItemAnimator((RecyclerView.ItemAnimator)null);
         this.mediaPages[var7].listView.setClipToPadding(false);
         this.mediaPages[var7].listView.setSectionsType(2);
         this.mediaPages[var7].listView.setLayoutManager(var24);
         MediaActivity.MediaPage[] var25 = this.mediaPages;
         var25[var7].addView(var25[var7].listView, LayoutHelper.createFrame(-1, -1.0F));
         this.mediaPages[var7].listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$MediaActivity$gt9PcHemY5TDJnj_A3DgU8jzVM0(this, var22)));
         this.mediaPages[var7].listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView var1, int var2) {
               if (var2 == 1 && MediaActivity.this.searching && MediaActivity.this.searchWas) {
                  AndroidUtilities.hideKeyboard(MediaActivity.this.getParentActivity().getCurrentFocus());
               }

               MediaActivity var5 = MediaActivity.this;
               boolean var3;
               if (var2 != 0) {
                  var3 = true;
               } else {
                  var3 = false;
               }

               var5.scrolling = var3;
               if (var2 != 1) {
                  var2 = (int)(-MediaActivity.access$8000(MediaActivity.this).getTranslationY());
                  int var4 = ActionBar.getCurrentActionBarHeight();
                  if (var2 != 0 && var2 != var4) {
                     if (var2 < var4 / 2) {
                        MediaActivity.this.mediaPages[0].listView.smoothScrollBy(0, -var2);
                     } else {
                        MediaActivity.this.mediaPages[0].listView.smoothScrollBy(0, var4 - var2);
                     }
                  }
               }

            }

            public void onScrolled(RecyclerView var1, int var2, int var3) {
               if (!MediaActivity.this.searching || !MediaActivity.this.searchWas) {
                  int var4 = var24.findFirstVisibleItemPosition();
                  if (var4 == -1) {
                     var2 = 0;
                  } else {
                     var2 = Math.abs(var24.findLastVisibleItemPosition() - var4) + 1;
                  }

                  int var5 = var1.getAdapter().getItemCount();
                  if (var2 != 0 && var4 + var2 > var5 - 2 && !MediaActivity.this.sharedMediaData[var22.selectedType].loading) {
                     byte var9;
                     if (var22.selectedType == 0) {
                        var9 = 0;
                     } else if (var22.selectedType == 1) {
                        var9 = 1;
                     } else if (var22.selectedType == 2) {
                        var9 = 2;
                     } else if (var22.selectedType == 4) {
                        var9 = 4;
                     } else {
                        var9 = 3;
                     }

                     if (!MediaActivity.this.sharedMediaData[var22.selectedType].endReached[0]) {
                        MediaActivity.this.sharedMediaData[var22.selectedType].loading = true;
                        DataQuery.getInstance(MediaActivity.access$8300(MediaActivity.this)).loadMedia(MediaActivity.this.dialog_id, 50, MediaActivity.this.sharedMediaData[var22.selectedType].max_id[0], var9, 1, MediaActivity.access$8200(MediaActivity.this));
                     } else if (MediaActivity.this.mergeDialogId != 0L && !MediaActivity.this.sharedMediaData[var22.selectedType].endReached[1]) {
                        MediaActivity.this.sharedMediaData[var22.selectedType].loading = true;
                        DataQuery.getInstance(MediaActivity.access$8500(MediaActivity.this)).loadMedia(MediaActivity.this.mergeDialogId, 50, MediaActivity.this.sharedMediaData[var22.selectedType].max_id[1], var9, 1, MediaActivity.access$8400(MediaActivity.this));
                     }
                  }

                  if (var1 == MediaActivity.this.mediaPages[0].listView && !MediaActivity.this.searching && !MediaActivity.access$8600(MediaActivity.this).isActionModeShowed()) {
                     float var6 = MediaActivity.access$8700(MediaActivity.this).getTranslationY();
                     float var7 = var6 - (float)var3;
                     float var8;
                     if (var7 < (float)(-ActionBar.getCurrentActionBarHeight())) {
                        var8 = (float)(-ActionBar.getCurrentActionBarHeight());
                     } else {
                        var8 = var7;
                        if (var7 > 0.0F) {
                           var8 = 0.0F;
                        }
                     }

                     if (var8 != var6) {
                        MediaActivity.this.setScrollY(var8);
                     }
                  }

                  MediaActivity.this.updateSections(var1, false);
               }
            }
         });
         this.mediaPages[var7].listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$MediaActivity$RwU3_9vRjx_ylKGSBWguLaWu_aA(this, var22)));
         if (var7 == 0 && var3 != -1) {
            var24.scrollToPositionWithOffset(var3, var10);
         }

         this.mediaPages[var7].emptyView = new LinearLayout(var1) {
            protected void onDraw(Canvas var1) {
               MediaActivity.this.backgroundPaint.setColor(Theme.getColor("windowBackgroundGray"));
               var1.drawRect(0.0F, (float)MediaActivity.access$8800(MediaActivity.this).getMeasuredHeight() + MediaActivity.access$8900(MediaActivity.this).getTranslationY(), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), MediaActivity.this.backgroundPaint);
            }
         };
         this.mediaPages[var7].emptyView.setWillNotDraw(false);
         this.mediaPages[var7].emptyView.setOrientation(1);
         this.mediaPages[var7].emptyView.setGravity(17);
         this.mediaPages[var7].emptyView.setVisibility(8);
         var18 = this.mediaPages;
         var18[var7].addView(var18[var7].emptyView, LayoutHelper.createFrame(-1, -1.0F));
         this.mediaPages[var7].emptyView.setOnTouchListener(_$$Lambda$MediaActivity$KVxLpoziroW7rOfn3d0nOSI4Va4.INSTANCE);
         this.mediaPages[var7].emptyImageView = new ImageView(var1);
         this.mediaPages[var7].emptyView.addView(this.mediaPages[var7].emptyImageView, LayoutHelper.createLinear(-2, -2));
         this.mediaPages[var7].emptyTextView = new TextView(var1);
         this.mediaPages[var7].emptyTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         this.mediaPages[var7].emptyTextView.setGravity(17);
         this.mediaPages[var7].emptyTextView.setTextSize(1, 17.0F);
         this.mediaPages[var7].emptyTextView.setPadding(AndroidUtilities.dp(40.0F), 0, AndroidUtilities.dp(40.0F), AndroidUtilities.dp(128.0F));
         this.mediaPages[var7].emptyView.addView(this.mediaPages[var7].emptyTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 24, 0, 0));
         this.mediaPages[var7].progressView = new LinearLayout(var1) {
            protected void onDraw(Canvas var1) {
               MediaActivity.this.backgroundPaint.setColor(Theme.getColor("windowBackgroundGray"));
               var1.drawRect(0.0F, (float)MediaActivity.access$9200(MediaActivity.this).getMeasuredHeight() + MediaActivity.access$9300(MediaActivity.this).getTranslationY(), (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight(), MediaActivity.this.backgroundPaint);
            }
         };
         this.mediaPages[var7].progressView.setWillNotDraw(false);
         this.mediaPages[var7].progressView.setGravity(17);
         this.mediaPages[var7].progressView.setOrientation(1);
         this.mediaPages[var7].progressView.setVisibility(8);
         var18 = this.mediaPages;
         var18[var7].addView(var18[var7].progressView, LayoutHelper.createFrame(-1, -1.0F));
         this.mediaPages[var7].progressBar = new RadialProgressView(var1);
         this.mediaPages[var7].progressView.addView(this.mediaPages[var7].progressBar, LayoutHelper.createLinear(-2, -2));
         if (var7 != 0) {
            this.mediaPages[var7].setVisibility(8);
         }

         ++var7;
         var8 = var3;
         var9 = var10;
      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.mediaDidLoad;
      byte var15 = 0;
      long var5;
      int var7;
      boolean var9;
      MediaActivity.MediaPage[] var17;
      if (var1 == var4) {
         var5 = (Long)var3[0];
         if ((Integer)var3[3] == super.classGuid) {
            var7 = (Integer)var3[4];
            this.sharedMediaData[var7].loading = false;
            this.sharedMediaData[var7].totalCount = (Integer)var3[1];
            ArrayList var8 = (ArrayList)var3[2];
            if ((int)this.dialog_id == 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            if (var5 == this.dialog_id) {
               var15 = 0;
            } else {
               var15 = 1;
            }

            Object var10;
            if (var7 == 0) {
               var10 = this.photoVideoAdapter;
            } else if (var7 == 1) {
               var10 = this.documentsAdapter;
            } else if (var7 == 2) {
               var10 = this.voiceAdapter;
            } else if (var7 == 3) {
               var10 = this.linksAdapter;
            } else if (var7 == 4) {
               var10 = this.audioAdapter;
            } else {
               var10 = null;
            }

            if (var10 != null) {
               var4 = ((RecyclerListView.SectionsAdapter)var10).getItemCount();
               var1 = var4;
               if (var10 instanceof RecyclerListView.SectionsAdapter) {
                  ((RecyclerListView.SectionsAdapter)var10).notifySectionsChanged();
                  var1 = var4;
               }
            } else {
               var1 = 0;
            }

            for(var4 = 0; var4 < var8.size(); ++var4) {
               MessageObject var11 = (MessageObject)var8.get(var4);
               this.sharedMediaData[var7].addMessage(var11, var15, false, var9);
            }

            this.sharedMediaData[var7].endReached[var15] = (Boolean)var3[5];
            if (var15 == 0 && this.sharedMediaData[var7].endReached[var15] && this.mergeDialogId != 0L) {
               this.sharedMediaData[var7].loading = true;
               DataQuery.getInstance(super.currentAccount).loadMedia(this.mergeDialogId, 50, this.sharedMediaData[var7].max_id[1], var7, 1, super.classGuid);
            }

            if (var10 != null) {
               var2 = 0;

               while(true) {
                  var17 = this.mediaPages;
                  if (var2 >= var17.length) {
                     var2 = ((RecyclerListView.SectionsAdapter)var10).getItemCount();
                     if (var1 > 1) {
                        ((RecyclerView.Adapter)var10).notifyItemChanged(var1 - 2);
                     }

                     if (var2 > var1) {
                        ((RecyclerView.Adapter)var10).notifyItemRangeInserted(var1, var2);
                     } else if (var2 < var1) {
                        ((RecyclerView.Adapter)var10).notifyItemRangeRemoved(var2, var1 - var2);
                     }
                     break;
                  }

                  if (var17[var2].listView.getAdapter() == var10) {
                     this.mediaPages[var2].listView.stopScroll();
                  }

                  ++var2;
               }
            }

            this.scrolling = true;
            var2 = 0;

            while(true) {
               var17 = this.mediaPages;
               if (var2 >= var17.length) {
                  break;
               }

               if (var17[var2].selectedType == var7 && !this.sharedMediaData[var7].loading) {
                  if (this.mediaPages[var2].progressView != null) {
                     this.mediaPages[var2].progressView.setVisibility(8);
                  }

                  if (this.mediaPages[var2].selectedType == var7 && this.mediaPages[var2].listView != null && this.mediaPages[var2].listView.getEmptyView() == null) {
                     this.mediaPages[var2].listView.setEmptyView(this.mediaPages[var2].emptyView);
                  }
               }

               if (var1 == 0 && super.actionBar.getTranslationY() != 0.0F && this.mediaPages[var2].listView.getAdapter() == var10) {
                  this.mediaPages[var2].layoutManager.scrollToPositionWithOffset(0, (int)super.actionBar.getTranslationY());
               }

               ++var2;
            }
         }
      } else {
         int var12;
         boolean var16;
         if (var1 == NotificationCenter.messagesDeleted) {
            TLRPC.Chat var31;
            if ((int)this.dialog_id < 0) {
               var31 = MessagesController.getInstance(super.currentAccount).getChat(-((int)this.dialog_id));
            } else {
               var31 = null;
            }

            label259: {
               var1 = (Integer)var3[1];
               if (ChatObject.isChannel(var31)) {
                  if (var1 == 0 && this.mergeDialogId != 0L) {
                     var15 = 1;
                     break label259;
                  }

                  if (var1 != var31.id) {
                     return;
                  }
               } else if (var1 != 0) {
                  return;
               }

               var15 = 0;
            }

            ArrayList var18 = (ArrayList)var3[0];
            var12 = var18.size();
            var4 = 0;

            for(var16 = false; var4 < var12; ++var4) {
               var7 = 0;

               while(true) {
                  MediaActivity.SharedMediaData[] var32 = this.sharedMediaData;
                  if (var7 >= var32.length) {
                     break;
                  }

                  if (var32[var7].deleteMessage((Integer)var18.get(var4), var15)) {
                     var16 = true;
                  }

                  ++var7;
               }
            }

            if (var16) {
               this.scrolling = true;
               MediaActivity.SharedPhotoVideoAdapter var19 = this.photoVideoAdapter;
               if (var19 != null) {
                  var19.notifyDataSetChanged();
               }

               MediaActivity.SharedDocumentsAdapter var20 = this.documentsAdapter;
               if (var20 != null) {
                  var20.notifyDataSetChanged();
               }

               var20 = this.voiceAdapter;
               if (var20 != null) {
                  var20.notifyDataSetChanged();
               }

               MediaActivity.SharedLinksAdapter var21 = this.linksAdapter;
               if (var21 != null) {
                  var21.notifyDataSetChanged();
               }

               var20 = this.audioAdapter;
               if (var20 != null) {
                  var20.notifyDataSetChanged();
               }
            }
         } else if (var1 == NotificationCenter.didReceiveNewMessages) {
            var5 = (Long)var3[0];
            long var13 = this.dialog_id;
            if (var5 == var13) {
               ArrayList var33 = (ArrayList)var3[1];
               if ((int)var13 == 0) {
                  var9 = true;
               } else {
                  var9 = false;
               }

               var2 = 0;

               boolean var23;
               for(var16 = false; var2 < var33.size(); var16 = var23) {
                  MessageObject var22 = (MessageObject)var33.get(var2);
                  var23 = var16;
                  if (var22.messageOwner.media != null) {
                     if (var22.needDrawBluredPreview()) {
                        var23 = var16;
                     } else {
                        var12 = DataQuery.getMediaType(var22.messageOwner);
                        if (var12 == -1) {
                           return;
                        }

                        MediaActivity.SharedMediaData var26 = this.sharedMediaData[var12];
                        byte var25;
                        if (var22.getDialogId() == this.dialog_id) {
                           var25 = 0;
                        } else {
                           var25 = 1;
                        }

                        var23 = var16;
                        if (var26.addMessage(var22, var25, true, var9)) {
                           this.hasMedia[var12] = 1;
                           var23 = true;
                        }
                     }
                  }

                  ++var2;
               }

               if (var16) {
                  this.scrolling = true;
                  var1 = 0;

                  while(true) {
                     var17 = this.mediaPages;
                     if (var1 >= var17.length) {
                        this.updateTabs();
                        break;
                     }

                     Object var24;
                     if (var17[var1].selectedType == 0) {
                        var24 = this.photoVideoAdapter;
                     } else if (this.mediaPages[var1].selectedType == 1) {
                        var24 = this.documentsAdapter;
                     } else if (this.mediaPages[var1].selectedType == 2) {
                        var24 = this.voiceAdapter;
                     } else if (this.mediaPages[var1].selectedType == 3) {
                        var24 = this.linksAdapter;
                     } else if (this.mediaPages[var1].selectedType == 4) {
                        var24 = this.audioAdapter;
                     } else {
                        var24 = null;
                     }

                     if (var24 != null) {
                        var2 = ((RecyclerListView.SectionsAdapter)var24).getItemCount();
                        this.photoVideoAdapter.notifyDataSetChanged();
                        this.documentsAdapter.notifyDataSetChanged();
                        this.voiceAdapter.notifyDataSetChanged();
                        this.linksAdapter.notifyDataSetChanged();
                        this.audioAdapter.notifyDataSetChanged();
                        if (var2 == 0 && super.actionBar.getTranslationY() != 0.0F) {
                           this.mediaPages[var1].layoutManager.scrollToPositionWithOffset(0, (int)super.actionBar.getTranslationY());
                        }
                     }

                     ++var1;
                  }
               }
            }
         } else if (var1 == NotificationCenter.messageReceivedByServer) {
            Integer var34 = (Integer)var3[0];
            Integer var28 = (Integer)var3[1];
            MediaActivity.SharedMediaData[] var27 = this.sharedMediaData;
            var4 = var27.length;

            for(var1 = var15; var1 < var4; ++var1) {
               var27[var1].replaceMid(var34, var28);
            }
         } else if (var1 == NotificationCenter.messagePlayingDidStart || var1 == NotificationCenter.messagePlayingPlayStateChanged || var1 == NotificationCenter.messagePlayingDidReset) {
            View var29;
            SharedAudioCell var30;
            if (var1 != NotificationCenter.messagePlayingDidReset && var1 != NotificationCenter.messagePlayingPlayStateChanged) {
               if (var1 == NotificationCenter.messagePlayingDidStart) {
                  if (((MessageObject)var3[0]).eventId != 0L) {
                     return;
                  }

                  var1 = 0;

                  while(true) {
                     var17 = this.mediaPages;
                     if (var1 >= var17.length) {
                        break;
                     }

                     var4 = var17[var1].listView.getChildCount();

                     for(var2 = 0; var2 < var4; ++var2) {
                        var29 = this.mediaPages[var1].listView.getChildAt(var2);
                        if (var29 instanceof SharedAudioCell) {
                           var30 = (SharedAudioCell)var29;
                           if (var30.getMessage() != null) {
                              var30.updateButtonState(false, true);
                           }
                        }
                     }

                     ++var1;
                  }
               }
            } else {
               var1 = 0;

               while(true) {
                  var17 = this.mediaPages;
                  if (var1 >= var17.length) {
                     break;
                  }

                  var4 = var17[var1].listView.getChildCount();

                  for(var2 = 0; var2 < var4; ++var2) {
                     var29 = this.mediaPages[var1].listView.getChildAt(var2);
                     if (var29 instanceof SharedAudioCell) {
                        var30 = (SharedAudioCell)var29;
                        if (var30.getMessage() != null) {
                           var30.updateButtonState(false, true);
                        }
                     }
                  }

                  ++var1;
               }
            }
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ArrayList var1 = new ArrayList();
      var1.add(new ThemeDescription(super.fragmentView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var1.add(new ThemeDescription(this.actionModeBackground, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "sharedMedia_actionMode"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCH, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearch"));
      var1.add(new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSearchPlaceholder"));
      var1.add(new ThemeDescription(this.selectedMessagesCountTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"));
      var1.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerBackground"));
      var1.add(new ThemeDescription(this.fragmentContextView, 0, new Class[]{FragmentContextView.class}, new String[]{"playButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerPlayPause"));
      var1.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerTitle"));
      var1.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerPerformer"));
      var1.add(new ThemeDescription(this.fragmentContextView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{FragmentContextView.class}, new String[]{"closeButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "inappPlayerClose"));
      var1.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarTabActiveText"));
      var1.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarTabUnactiveText"));
      var1.add(new ThemeDescription(this.scrollSlidingTextTabStrip.getTabsContainer(), ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{TextView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarTabLine"));
      var1.add(new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, new Drawable[]{this.scrollSlidingTextTabStrip.getSelectorDrawable()}, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarTabSelector"));
      int var2 = 0;

      while(true) {
         MediaActivity.MediaPage[] var3 = this.mediaPages;
         if (var2 >= var3.length) {
            return (ThemeDescription[])var1.toArray(new ThemeDescription[0]);
         }

         _$$Lambda$MediaActivity$_V3QwZ76KmAjddBwePNYNf2a2E4 var4 = new _$$Lambda$MediaActivity$_V3QwZ76KmAjddBwePNYNf2a2E4(this, var2);
         var1.add(new ThemeDescription(var3[var2].emptyView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"));
         var1.add(new ThemeDescription(this.mediaPages[var2].progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21"));
         var1.add(new ThemeDescription(this.mediaPages[var2].emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder"));
         var1.add(new ThemeDescription(this.mediaPages[var2].progressBar, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"));
         var1.add(new ThemeDescription(this.mediaPages[var2].emptyTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_SECTIONS, new Class[]{GraySectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "key_graySectionText"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_SECTIONS, new Class[]{GraySectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "graySection"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"dateTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{SharedDocumentCell.class}, new String[]{"progressView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "sharedMedia_startStopLoadIcon"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"statusImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "sharedMedia_startStopLoadIcon"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedDocumentCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkbox"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedDocumentCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxCheck"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"thumbImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "files_folderIcon"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{SharedDocumentCell.class}, new String[]{"extTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "files_iconText"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedAudioCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkbox"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedAudioCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxCheck"));
         RecyclerListView var5 = this.mediaPages[var2].listView;
         int var6 = ThemeDescription.FLAG_TEXTCOLOR;
         TextPaint var7 = Theme.chat_contextResult_titleTextPaint;
         var1.add(new ThemeDescription(var5, var6, new Class[]{SharedAudioCell.class}, var7, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         RecyclerListView var8 = this.mediaPages[var2].listView;
         var6 = ThemeDescription.FLAG_TEXTCOLOR;
         TextPaint var10 = Theme.chat_contextResult_descriptionTextPaint;
         var1.add(new ThemeDescription(var8, var6, new Class[]{SharedAudioCell.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedLinkCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkbox"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedLinkCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxCheck"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, 0, new Class[]{SharedLinkCell.class}, new String[]{"titleTextPaint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, 0, new Class[]{SharedLinkCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText"));
         var5 = this.mediaPages[var2].listView;
         Paint var9 = Theme.linkSelectionPaint;
         var1.add(new ThemeDescription(var5, 0, new Class[]{SharedLinkCell.class}, var9, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkSelection"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, 0, new Class[]{SharedLinkCell.class}, new String[]{"letterDrawable"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "sharedMedia_linkPlaceholderText"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{SharedLinkCell.class}, new String[]{"letterDrawable"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "sharedMedia_linkPlaceholder"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_SECTIONS, new Class[]{SharedMediaSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_SECTIONS, new Class[]{SharedMediaSectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, 0, new Class[]{SharedMediaSectionCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, 0, new Class[]{SharedPhotoVideoCell.class}, new String[]{"backgroundPaint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "sharedMedia_photoPlaceholder"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CHECKBOX, new Class[]{SharedPhotoVideoCell.class}, (Paint)null, (Drawable[])null, var4, "checkbox"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{SharedPhotoVideoCell.class}, (Paint)null, (Drawable[])null, var4, "checkboxCheck"));
         var1.add(new ThemeDescription(this.mediaPages[var2].listView, 0, (Class[])null, (Paint)null, new Drawable[]{this.pinnedHeaderShadowDrawable}, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"));
         ++var2;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$2$MediaActivity(MediaActivity.MediaPage var1, View var2, int var3) {
      if (var1.selectedType == 1 && var2 instanceof SharedDocumentCell) {
         this.onItemClick(var3, var2, ((SharedDocumentCell)var2).getMessage(), 0, var1.selectedType);
      } else if (var1.selectedType == 3 && var2 instanceof SharedLinkCell) {
         this.onItemClick(var3, var2, ((SharedLinkCell)var2).getMessage(), 0, var1.selectedType);
      } else if ((var1.selectedType == 2 || var1.selectedType == 4) && var2 instanceof SharedAudioCell) {
         this.onItemClick(var3, var2, ((SharedAudioCell)var2).getMessage(), 0, var1.selectedType);
      }

   }

   // $FF: synthetic method
   public boolean lambda$createView$3$MediaActivity(MediaActivity.MediaPage var1, View var2, int var3) {
      if (super.actionBar.isActionModeShowed()) {
         var1.listView.getOnItemClickListener().onItemClick(var2, var3);
         return true;
      } else if (var1.selectedType == 1 && var2 instanceof SharedDocumentCell) {
         return this.onItemLongClick(((SharedDocumentCell)var2).getMessage(), var2, 0);
      } else if (var1.selectedType == 3 && var2 instanceof SharedLinkCell) {
         return this.onItemLongClick(((SharedLinkCell)var2).getMessage(), var2, 0);
      } else {
         return (var1.selectedType == 2 || var1.selectedType == 4) && var2 instanceof SharedAudioCell ? this.onItemLongClick(((SharedAudioCell)var2).getMessage(), var2, 0) : false;
      }
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$5$MediaActivity(int var1) {
      if (this.mediaPages[var1].listView != null) {
         int var2 = this.mediaPages[var1].listView.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.mediaPages[var1].listView.getChildAt(var3);
            if (var4 instanceof SharedPhotoVideoCell) {
               ((SharedPhotoVideoCell)var4).updateCheckboxColor();
            }
         }
      }

   }

   public boolean onBackPressed() {
      return super.actionBar.isEnabled();
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      final int var2 = 0;

      while(true) {
         MediaActivity.MediaPage[] var3 = this.mediaPages;
         if (var2 >= var3.length) {
            return;
         }

         if (var3[var2].listView != null) {
            this.mediaPages[var2].listView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
               public boolean onPreDraw() {
                  MediaActivity.this.mediaPages[var2].getViewTreeObserver().removeOnPreDrawListener(this);
                  MediaActivity.this.fixLayoutInternal(var2);
                  return true;
               }
            });
         }

         ++var2;
      }
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.mediaDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didReceiveNewMessages);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messageReceivedByServer);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.mediaDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didReceiveNewMessages);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messageReceivedByServer);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
   }

   public void onResume() {
      super.onResume();
      this.scrolling = true;
      MediaActivity.SharedPhotoVideoAdapter var1 = this.photoVideoAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      MediaActivity.SharedDocumentsAdapter var3 = this.documentsAdapter;
      if (var3 != null) {
         var3.notifyDataSetChanged();
      }

      MediaActivity.SharedLinksAdapter var4 = this.linksAdapter;
      if (var4 != null) {
         var4.notifyDataSetChanged();
      }

      for(int var2 = 0; var2 < this.mediaPages.length; ++var2) {
         this.fixLayoutInternal(var2);
      }

   }

   public void setChatInfo(TLRPC.ChatFull var1) {
      this.info = var1;
      var1 = this.info;
      if (var1 != null) {
         int var2 = var1.migrated_from_chat_id;
         if (var2 != 0 && this.mergeDialogId == 0L) {
            this.mergeDialogId = (long)(-var2);
            var2 = 0;

            while(true) {
               MediaActivity.SharedMediaData[] var3 = this.sharedMediaData;
               if (var2 >= var3.length) {
                  break;
               }

               var3[var2].max_id[1] = this.info.migrated_from_max_id;
               this.sharedMediaData[var2].endReached[1] = false;
               ++var2;
            }
         }
      }

   }

   public void setMergeDialogId(long var1) {
      this.mergeDialogId = var1;
   }

   public void updateAdapters() {
      MediaActivity.SharedPhotoVideoAdapter var1 = this.photoVideoAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      MediaActivity.SharedDocumentsAdapter var2 = this.documentsAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

      var2 = this.voiceAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

      MediaActivity.SharedLinksAdapter var3 = this.linksAdapter;
      if (var3 != null) {
         var3.notifyDataSetChanged();
      }

      var2 = this.audioAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

   }

   private class MediaPage extends FrameLayout {
      private ImageView emptyImageView;
      private TextView emptyTextView;
      private LinearLayout emptyView;
      private LinearLayoutManager layoutManager;
      private RecyclerListView listView;
      private RadialProgressView progressBar;
      private LinearLayout progressView;
      private int selectedType;

      public MediaPage(Context var2) {
         super(var2);
      }
   }

   public class MediaSearchAdapter extends RecyclerListView.SelectionAdapter {
      private int currentType;
      protected ArrayList globalSearch = new ArrayList();
      private int lastReqId;
      private Context mContext;
      private int reqId = 0;
      private ArrayList searchResult = new ArrayList();
      private Runnable searchRunnable;
      private int searchesInProgress;

      public MediaSearchAdapter(Context var2, int var3) {
         this.mContext = var2;
         this.currentType = var3;
      }

      private void updateSearchResults(ArrayList var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$MediaActivity$MediaSearchAdapter$VRYl3PP_Z3UXWvfCBf3wim5v314(this, var1));
      }

      public MessageObject getItem(int var1) {
         return var1 < this.searchResult.size() ? (MessageObject)this.searchResult.get(var1) : (MessageObject)this.globalSearch.get(var1 - this.searchResult.size());
      }

      public int getItemCount() {
         int var1 = this.searchResult.size();
         int var2 = this.globalSearch.size();
         int var3 = var1;
         if (var2 != 0) {
            var3 = var1 + var2;
         }

         return var3;
      }

      public int getItemViewType(int var1) {
         return 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() != this.searchResult.size() + this.globalSearch.size()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean isGlobalSearch(int var1) {
         int var2 = this.searchResult.size();
         int var3 = this.globalSearch.size();
         if (var1 >= 0 && var1 < var2) {
            return false;
         } else {
            return var1 > var2 && var1 <= var3 + var2;
         }
      }

      // $FF: synthetic method
      public void lambda$null$0$MediaActivity$MediaSearchAdapter(int var1, ArrayList var2) {
         if (this.reqId != 0) {
            if (var1 == this.lastReqId) {
               this.globalSearch = var2;
               --this.searchesInProgress;
               int var3 = this.getItemCount();
               this.notifyDataSetChanged();

               for(var1 = 0; var1 < MediaActivity.this.mediaPages.length; ++var1) {
                  if (MediaActivity.this.mediaPages[var1].listView.getAdapter() == this && var3 == 0 && MediaActivity.access$12400(MediaActivity.this).getTranslationY() != 0.0F) {
                     MediaActivity.this.mediaPages[var1].layoutManager.scrollToPositionWithOffset(0, (int)MediaActivity.access$12500(MediaActivity.this).getTranslationY());
                     break;
                  }
               }
            }

            this.reqId = 0;
         }

      }

      // $FF: synthetic method
      public void lambda$null$2$MediaActivity$MediaSearchAdapter(String var1, ArrayList var2) {
         String var3 = var1.trim().toLowerCase();
         if (var3.length() == 0) {
            this.updateSearchResults(new ArrayList());
         } else {
            label87: {
               String var4 = LocaleController.getInstance().getTranslitString(var3);
               if (!var3.equals(var4)) {
                  var1 = var4;
                  if (var4.length() != 0) {
                     break label87;
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

            String[] var14 = new String[var5 + 1];
            var14[0] = var3;
            if (var1 != null) {
               var14[1] = var1;
            }

            ArrayList var13 = new ArrayList();

            for(int var15 = 0; var15 < var2.size(); ++var15) {
               MessageObject var6 = (MessageObject)var2.get(var15);

               for(int var7 = 0; var7 < var14.length; ++var7) {
                  String var8 = var14[var7];
                  var1 = var6.getDocumentName();
                  if (var1 != null && var1.length() != 0) {
                     if (var1.toLowerCase().contains(var8)) {
                        var13.add(var6);
                        break;
                     }

                     if (this.currentType == 4) {
                        TLRPC.Document var12;
                        if (var6.type == 0) {
                           var12 = var6.messageOwner.media.webpage.document;
                        } else {
                           var12 = var6.messageOwner.media.document;
                        }

                        int var9 = 0;

                        boolean var11;
                        while(true) {
                           if (var9 >= var12.attributes.size()) {
                              var11 = false;
                              break;
                           }

                           TLRPC.DocumentAttribute var10 = (TLRPC.DocumentAttribute)var12.attributes.get(var9);
                           if (var10 instanceof TLRPC.TL_documentAttributeAudio) {
                              var1 = var10.performer;
                              if (var1 != null) {
                                 var11 = var1.toLowerCase().contains(var8);
                              } else {
                                 var11 = false;
                              }

                              if (!var11) {
                                 var1 = var10.title;
                                 if (var1 != null) {
                                    var11 = var1.toLowerCase().contains(var8);
                                 }
                              }
                              break;
                           }

                           ++var9;
                        }

                        if (var11) {
                           var13.add(var6);
                           break;
                        }
                     }
                  }
               }
            }

            this.updateSearchResults(var13);
         }
      }

      // $FF: synthetic method
      public void lambda$queryServerSearch$1$MediaActivity$MediaSearchAdapter(int var1, int var2, TLObject var3, TLRPC.TL_error var4) {
         ArrayList var5 = new ArrayList();
         if (var4 == null) {
            TLRPC.messages_Messages var7 = (TLRPC.messages_Messages)var3;

            for(int var6 = 0; var6 < var7.messages.size(); ++var6) {
               TLRPC.Message var8 = (TLRPC.Message)var7.messages.get(var6);
               if (var1 == 0 || var8.id <= var1) {
                  var5.add(new MessageObject(MediaActivity.access$12300(MediaActivity.this), var8, false));
               }
            }
         }

         AndroidUtilities.runOnUIThread(new _$$Lambda$MediaActivity$MediaSearchAdapter$G_Zh_DsdJtsIjKabH6nVQe9wHJY(this, var2, var5));
      }

      // $FF: synthetic method
      public void lambda$search$3$MediaActivity$MediaSearchAdapter(String var1) {
         int var2;
         label26: {
            if (!MediaActivity.this.sharedMediaData[this.currentType].messages.isEmpty()) {
               var2 = this.currentType;
               if (var2 == 1 || var2 == 4) {
                  MessageObject var3 = (MessageObject)MediaActivity.this.sharedMediaData[this.currentType].messages.get(MediaActivity.this.sharedMediaData[this.currentType].messages.size() - 1);
                  this.queryServerSearch(var1, var3.getId(), var3.getDialogId());
                  break label26;
               }
            }

            if (this.currentType == 3) {
               this.queryServerSearch(var1, 0, MediaActivity.this.dialog_id);
            }
         }

         var2 = this.currentType;
         if (var2 == 1 || var2 == 4) {
            ArrayList var4 = new ArrayList(MediaActivity.this.sharedMediaData[this.currentType].messages);
            ++this.searchesInProgress;
            Utilities.searchQueue.postRunnable(new _$$Lambda$MediaActivity$MediaSearchAdapter$qpWFvGqsff_CgcNaNHZBu9Ymjms(this, var1, var4));
         }

      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$4$MediaActivity$MediaSearchAdapter(ArrayList var1) {
         --this.searchesInProgress;
         this.searchResult = var1;
         int var2 = this.getItemCount();
         this.notifyDataSetChanged();

         for(int var3 = 0; var3 < MediaActivity.this.mediaPages.length; ++var3) {
            if (MediaActivity.this.mediaPages[var3].listView.getAdapter() == this && var2 == 0 && MediaActivity.access$12100(MediaActivity.this).getTranslationY() != 0.0F) {
               MediaActivity.this.mediaPages[var3].layoutManager.scrollToPositionWithOffset(0, (int)MediaActivity.access$12200(MediaActivity.this).getTranslationY());
               break;
            }
         }

      }

      public void notifyDataSetChanged() {
         super.notifyDataSetChanged();
         if (this.searchesInProgress == 0) {
            for(int var1 = 0; var1 < MediaActivity.this.mediaPages.length; ++var1) {
               if (MediaActivity.this.mediaPages[var1].selectedType == this.currentType) {
                  MediaActivity.this.mediaPages[var1].listView.setEmptyView(MediaActivity.this.mediaPages[var1].emptyView);
                  MediaActivity.this.mediaPages[var1].progressView.setVisibility(8);
               }
            }
         }

      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = this.currentType;
         boolean var4 = false;
         boolean var5 = false;
         boolean var6 = false;
         boolean var8;
         SparseArray[] var9;
         MessageObject var10;
         byte var11;
         if (var3 == 1) {
            SharedDocumentCell var7 = (SharedDocumentCell)var1.itemView;
            var10 = this.getItem(var2);
            if (var2 != this.getItemCount() - 1) {
               var8 = true;
            } else {
               var8 = false;
            }

            var7.setDocument(var10, var8);
            if (MediaActivity.access$11800(MediaActivity.this).isActionModeShowed()) {
               var9 = MediaActivity.this.selectedFiles;
               if (var10.getDialogId() == MediaActivity.this.dialog_id) {
                  var11 = 0;
               } else {
                  var11 = 1;
               }

               var8 = var6;
               if (var9[var11].indexOfKey(var10.getId()) >= 0) {
                  var8 = true;
               }

               var7.setChecked(var8, MediaActivity.this.scrolling ^ true);
            } else {
               var7.setChecked(false, MediaActivity.this.scrolling ^ true);
            }
         } else if (var3 == 3) {
            SharedLinkCell var12 = (SharedLinkCell)var1.itemView;
            var10 = this.getItem(var2);
            if (var2 != this.getItemCount() - 1) {
               var8 = true;
            } else {
               var8 = false;
            }

            var12.setLink(var10, var8);
            if (MediaActivity.access$11900(MediaActivity.this).isActionModeShowed()) {
               var9 = MediaActivity.this.selectedFiles;
               if (var10.getDialogId() == MediaActivity.this.dialog_id) {
                  var11 = 0;
               } else {
                  var11 = 1;
               }

               var8 = var4;
               if (var9[var11].indexOfKey(var10.getId()) >= 0) {
                  var8 = true;
               }

               var12.setChecked(var8, MediaActivity.this.scrolling ^ true);
            } else {
               var12.setChecked(false, MediaActivity.this.scrolling ^ true);
            }
         } else if (var3 == 4) {
            SharedAudioCell var14 = (SharedAudioCell)var1.itemView;
            var10 = this.getItem(var2);
            if (var2 != this.getItemCount() - 1) {
               var8 = true;
            } else {
               var8 = false;
            }

            var14.setMessageObject(var10, var8);
            if (MediaActivity.access$12000(MediaActivity.this).isActionModeShowed()) {
               SparseArray[] var13 = MediaActivity.this.selectedFiles;
               if (var10.getDialogId() == MediaActivity.this.dialog_id) {
                  var11 = 0;
               } else {
                  var11 = 1;
               }

               var8 = var5;
               if (var13[var11].indexOfKey(var10.getId()) >= 0) {
                  var8 = true;
               }

               var14.setChecked(var8, MediaActivity.this.scrolling ^ true);
            } else {
               var14.setChecked(false, MediaActivity.this.scrolling ^ true);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         var2 = this.currentType;
         Object var3;
         if (var2 == 1) {
            var3 = new SharedDocumentCell(this.mContext);
         } else if (var2 == 4) {
            var3 = new SharedAudioCell(this.mContext) {
               public boolean needPlayMessage(MessageObject var1) {
                  if (!var1.isVoice() && !var1.isRoundVideo()) {
                     return var1.isMusic() ? MediaController.getInstance().setPlaylist(MediaSearchAdapter.this.searchResult, var1) : false;
                  } else {
                     boolean var2 = MediaController.getInstance().playMessage(var1);
                     MediaController var3 = MediaController.getInstance();
                     ArrayList var4;
                     if (var2) {
                        var4 = MediaSearchAdapter.this.searchResult;
                     } else {
                        var4 = null;
                     }

                     var3.setVoiceMessagesPlaylist(var4, false);
                     if (var1.isRoundVideo()) {
                        MediaController.getInstance().setCurrentVideoVisible(false);
                     }

                     return var2;
                  }
               }
            };
         } else {
            var3 = new SharedLinkCell(this.mContext);
            ((SharedLinkCell)var3).setDelegate(MediaActivity.this.sharedLinkCellDelegate);
         }

         return new RecyclerListView.Holder((View)var3);
      }

      public void queryServerSearch(String var1, int var2, long var3) {
         int var5 = (int)var3;
         if (var5 != 0) {
            if (this.reqId != 0) {
               ConnectionsManager.getInstance(MediaActivity.access$11100(MediaActivity.this)).cancelRequest(this.reqId, true);
               this.reqId = 0;
               --this.searchesInProgress;
            }

            if (var1 != null && var1.length() != 0) {
               TLRPC.TL_messages_search var6 = new TLRPC.TL_messages_search();
               var6.limit = 50;
               var6.offset_id = var2;
               int var7 = this.currentType;
               if (var7 == 1) {
                  var6.filter = new TLRPC.TL_inputMessagesFilterDocument();
               } else if (var7 == 3) {
                  var6.filter = new TLRPC.TL_inputMessagesFilterUrl();
               } else if (var7 == 4) {
                  var6.filter = new TLRPC.TL_inputMessagesFilterMusic();
               }

               var6.q = var1;
               var6.peer = MessagesController.getInstance(MediaActivity.access$11200(MediaActivity.this)).getInputPeer(var5);
               if (var6.peer != null) {
                  var7 = this.lastReqId + 1;
                  this.lastReqId = var7;
                  ++this.searchesInProgress;
                  this.reqId = ConnectionsManager.getInstance(MediaActivity.access$11300(MediaActivity.this)).sendRequest(var6, new _$$Lambda$MediaActivity$MediaSearchAdapter$TrL5Kc9dMCjvGfdjifj6CuBCggQ(this, var2, var7), 2);
                  ConnectionsManager.getInstance(MediaActivity.access$11500(MediaActivity.this)).bindRequestToGuid(this.reqId, MediaActivity.access$11400(MediaActivity.this));
               }
            } else {
               this.globalSearch.clear();
               this.lastReqId = 0;
               this.notifyDataSetChanged();
            }
         }
      }

      public void search(String var1) {
         Runnable var2 = this.searchRunnable;
         if (var2 != null) {
            AndroidUtilities.cancelRunOnUIThread(var2);
            this.searchRunnable = null;
         }

         if (TextUtils.isEmpty(var1)) {
            if (!this.searchResult.isEmpty() || !this.globalSearch.isEmpty() || this.searchesInProgress != 0) {
               this.searchResult.clear();
               this.globalSearch.clear();
               if (this.reqId != 0) {
                  ConnectionsManager.getInstance(MediaActivity.access$11600(MediaActivity.this)).cancelRequest(this.reqId, true);
                  this.reqId = 0;
                  --this.searchesInProgress;
               }
            }

            this.notifyDataSetChanged();
         } else {
            for(int var3 = 0; var3 < MediaActivity.this.mediaPages.length; ++var3) {
               if (MediaActivity.this.mediaPages[var3].selectedType == this.currentType) {
                  if (this.getItemCount() != 0) {
                     MediaActivity.this.mediaPages[var3].listView.setEmptyView(MediaActivity.this.mediaPages[var3].emptyView);
                     MediaActivity.this.mediaPages[var3].progressView.setVisibility(8);
                  } else {
                     MediaActivity.this.mediaPages[var3].listView.setEmptyView((View)null);
                     MediaActivity.this.mediaPages[var3].emptyView.setVisibility(8);
                     MediaActivity.this.mediaPages[var3].progressView.setVisibility(0);
                  }
               }
            }

            _$$Lambda$MediaActivity$MediaSearchAdapter$bqzErS4mWWgqwF4bou3KhFeJTaw var4 = new _$$Lambda$MediaActivity$MediaSearchAdapter$bqzErS4mWWgqwF4bou3KhFeJTaw(this, var1);
            this.searchRunnable = var4;
            AndroidUtilities.runOnUIThread(var4, 300L);
         }

      }
   }

   private class SharedDocumentsAdapter extends RecyclerListView.SectionsAdapter {
      private int currentType;
      private Context mContext;

      public SharedDocumentsAdapter(Context var2, int var3) {
         this.mContext = var2;
         this.currentType = var3;
      }

      public int getCountForSection(int var1) {
         return var1 < MediaActivity.this.sharedMediaData[this.currentType].sections.size() ? ((ArrayList)MediaActivity.this.sharedMediaData[this.currentType].sectionArrays.get(MediaActivity.this.sharedMediaData[this.currentType].sections.get(var1))).size() + 1 : 1;
      }

      public Object getItem(int var1, int var2) {
         return null;
      }

      public int getItemViewType(int var1, int var2) {
         if (var1 < MediaActivity.this.sharedMediaData[this.currentType].sections.size()) {
            if (var2 == 0) {
               return 0;
            } else {
               var1 = this.currentType;
               return var1 != 2 && var1 != 4 ? 1 : 3;
            }
         } else {
            return 2;
         }
      }

      public String getLetter(int var1) {
         return null;
      }

      public int getPositionForScrollProgress(float var1) {
         return 0;
      }

      public int getSectionCount() {
         int var1 = MediaActivity.this.sharedMediaData[this.currentType].sections.size();
         boolean var2 = MediaActivity.this.sharedMediaData[this.currentType].sections.isEmpty();
         byte var3 = 1;
         byte var4;
         if (!var2) {
            var4 = var3;
            if (!MediaActivity.this.sharedMediaData[this.currentType].endReached[0]) {
               return var1 + var4;
            }

            var4 = var3;
            if (!MediaActivity.this.sharedMediaData[this.currentType].endReached[1]) {
               return var1 + var4;
            }
         }

         var4 = 0;
         return var1 + var4;
      }

      public View getSectionHeaderView(int var1, View var2) {
         Object var3 = var2;
         if (var2 == null) {
            var3 = new GraySectionCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("graySection") & -218103809);
         }

         if (var1 < MediaActivity.this.sharedMediaData[this.currentType].sections.size()) {
            String var4 = (String)MediaActivity.this.sharedMediaData[this.currentType].sections.get(var1);
            MessageObject var5 = (MessageObject)((ArrayList)MediaActivity.this.sharedMediaData[this.currentType].sectionArrays.get(var4)).get(0);
            ((GraySectionCell)var3).setText(LocaleController.formatSectionDate((long)var5.messageOwner.date));
         }

         return (View)var3;
      }

      public boolean isEnabled(int var1, int var2) {
         boolean var3;
         if (var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(int var1, int var2, RecyclerView.ViewHolder var3) {
         if (var3.getItemViewType() != 2) {
            String var4 = (String)MediaActivity.this.sharedMediaData[this.currentType].sections.get(var1);
            ArrayList var13 = (ArrayList)MediaActivity.this.sharedMediaData[this.currentType].sectionArrays.get(var4);
            int var5 = var3.getItemViewType();
            boolean var6 = false;
            boolean var7 = false;
            if (var5 != 0) {
               boolean var9;
               byte var10;
               SparseArray[] var14;
               if (var5 != 1) {
                  if (var5 == 3) {
                     SharedAudioCell var11 = (SharedAudioCell)var3.itemView;
                     MessageObject var8 = (MessageObject)var13.get(var2 - 1);
                     if (var2 != var13.size() || var1 == MediaActivity.this.sharedMediaData[this.currentType].sections.size() - 1 && MediaActivity.this.sharedMediaData[this.currentType].loading) {
                        var9 = true;
                     } else {
                        var9 = false;
                     }

                     var11.setMessageObject(var8, var9);
                     if (MediaActivity.access$10300(MediaActivity.this).isActionModeShowed()) {
                        var14 = MediaActivity.this.selectedFiles;
                        if (var8.getDialogId() == MediaActivity.this.dialog_id) {
                           var10 = 0;
                        } else {
                           var10 = 1;
                        }

                        var9 = var7;
                        if (var14[var10].indexOfKey(var8.getId()) >= 0) {
                           var9 = true;
                        }

                        var11.setChecked(var9, MediaActivity.this.scrolling ^ true);
                     } else {
                        var11.setChecked(false, MediaActivity.this.scrolling ^ true);
                     }
                  }
               } else {
                  SharedDocumentCell var16 = (SharedDocumentCell)var3.itemView;
                  MessageObject var12 = (MessageObject)var13.get(var2 - 1);
                  if (var2 != var13.size() || var1 == MediaActivity.this.sharedMediaData[this.currentType].sections.size() - 1 && MediaActivity.this.sharedMediaData[this.currentType].loading) {
                     var9 = true;
                  } else {
                     var9 = false;
                  }

                  var16.setDocument(var12, var9);
                  if (MediaActivity.access$10200(MediaActivity.this).isActionModeShowed()) {
                     var14 = MediaActivity.this.selectedFiles;
                     if (var12.getDialogId() == MediaActivity.this.dialog_id) {
                        var10 = 0;
                     } else {
                        var10 = 1;
                     }

                     var9 = var6;
                     if (var14[var10].indexOfKey(var12.getId()) >= 0) {
                        var9 = true;
                     }

                     var16.setChecked(var9, MediaActivity.this.scrolling ^ true);
                  } else {
                     var16.setChecked(false, MediaActivity.this.scrolling ^ true);
                  }
               }
            } else {
               MessageObject var15 = (MessageObject)var13.get(0);
               ((GraySectionCell)var3.itemView).setText(LocaleController.formatSectionDate((long)var15.messageOwner.date));
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var6;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  Object var4;
                  if (this.currentType == 4 && !MediaActivity.this.audioCellCache.isEmpty()) {
                     View var5 = (View)MediaActivity.this.audioCellCache.get(0);
                     MediaActivity.this.audioCellCache.remove(0);
                     ViewGroup var3 = (ViewGroup)var5.getParent();
                     var4 = var5;
                     if (var3 != null) {
                        var3.removeView(var5);
                        var4 = var5;
                     }
                  } else {
                     var4 = new SharedAudioCell(this.mContext) {
                        public boolean needPlayMessage(MessageObject var1) {
                           if (!var1.isVoice() && !var1.isRoundVideo()) {
                              return var1.isMusic() ? MediaController.getInstance().setPlaylist(MediaActivity.this.sharedMediaData[SharedDocumentsAdapter.this.currentType].messages, var1) : false;
                           } else {
                              boolean var2 = MediaController.getInstance().playMessage(var1);
                              MediaController var3 = MediaController.getInstance();
                              ArrayList var4;
                              if (var2) {
                                 var4 = MediaActivity.this.sharedMediaData[SharedDocumentsAdapter.this.currentType].messages;
                              } else {
                                 var4 = null;
                              }

                              var3.setVoiceMessagesPlaylist(var4, false);
                              return var2;
                           }
                        }
                     };
                  }

                  var6 = var4;
                  if (this.currentType == 4) {
                     MediaActivity.this.audioCache.add((SharedAudioCell)var4);
                     var6 = var4;
                  }
               } else {
                  var6 = new LoadingCell(this.mContext, AndroidUtilities.dp(32.0F), AndroidUtilities.dp(54.0F));
               }
            } else {
               var6 = new SharedDocumentCell(this.mContext);
            }
         } else {
            var6 = new GraySectionCell(this.mContext);
         }

         return new RecyclerListView.Holder((View)var6);
      }
   }

   private class SharedLinksAdapter extends RecyclerListView.SectionsAdapter {
      private Context mContext;

      public SharedLinksAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getCountForSection(int var1) {
         return var1 < MediaActivity.this.sharedMediaData[3].sections.size() ? ((ArrayList)MediaActivity.this.sharedMediaData[3].sectionArrays.get(MediaActivity.this.sharedMediaData[3].sections.get(var1))).size() + 1 : 1;
      }

      public Object getItem(int var1, int var2) {
         return null;
      }

      public int getItemViewType(int var1, int var2) {
         if (var1 < MediaActivity.this.sharedMediaData[3].sections.size()) {
            return var2 == 0 ? 0 : 1;
         } else {
            return 2;
         }
      }

      public String getLetter(int var1) {
         return null;
      }

      public int getPositionForScrollProgress(float var1) {
         return 0;
      }

      public int getSectionCount() {
         int var1 = MediaActivity.this.sharedMediaData[3].sections.size();
         boolean var2 = MediaActivity.this.sharedMediaData[3].sections.isEmpty();
         byte var3 = 1;
         byte var4;
         if (!var2) {
            var4 = var3;
            if (!MediaActivity.this.sharedMediaData[3].endReached[0]) {
               return var1 + var4;
            }

            var4 = var3;
            if (!MediaActivity.this.sharedMediaData[3].endReached[1]) {
               return var1 + var4;
            }
         }

         var4 = 0;
         return var1 + var4;
      }

      public View getSectionHeaderView(int var1, View var2) {
         Object var3 = var2;
         if (var2 == null) {
            var3 = new GraySectionCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("graySection") & -218103809);
         }

         if (var1 < MediaActivity.this.sharedMediaData[3].sections.size()) {
            String var4 = (String)MediaActivity.this.sharedMediaData[3].sections.get(var1);
            MessageObject var5 = (MessageObject)((ArrayList)MediaActivity.this.sharedMediaData[3].sectionArrays.get(var4)).get(0);
            ((GraySectionCell)var3).setText(LocaleController.formatSectionDate((long)var5.messageOwner.date));
         }

         return (View)var3;
      }

      public boolean isEnabled(int var1, int var2) {
         boolean var3;
         if (var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(int var1, int var2, RecyclerView.ViewHolder var3) {
         if (var3.getItemViewType() != 2) {
            String var4 = (String)MediaActivity.this.sharedMediaData[3].sections.get(var1);
            ArrayList var11 = (ArrayList)MediaActivity.this.sharedMediaData[3].sectionArrays.get(var4);
            int var5 = var3.getItemViewType();
            boolean var6 = false;
            if (var5 != 0) {
               if (var5 == 1) {
                  SharedLinkCell var7 = (SharedLinkCell)var3.itemView;
                  MessageObject var10 = (MessageObject)var11.get(var2 - 1);
                  boolean var8;
                  if (var2 != var11.size() || var1 == MediaActivity.this.sharedMediaData[3].sections.size() - 1 && MediaActivity.this.sharedMediaData[3].loading) {
                     var8 = true;
                  } else {
                     var8 = false;
                  }

                  var7.setLink(var10, var8);
                  if (MediaActivity.access$9800(MediaActivity.this).isActionModeShowed()) {
                     SparseArray[] var12 = MediaActivity.this.selectedFiles;
                     byte var9;
                     if (var10.getDialogId() == MediaActivity.this.dialog_id) {
                        var9 = 0;
                     } else {
                        var9 = 1;
                     }

                     var8 = var6;
                     if (var12[var9].indexOfKey(var10.getId()) >= 0) {
                        var8 = true;
                     }

                     var7.setChecked(var8, MediaActivity.this.scrolling ^ true);
                  } else {
                     var7.setChecked(false, MediaActivity.this.scrolling ^ true);
                  }
               }
            } else {
               MessageObject var13 = (MessageObject)var11.get(0);
               ((GraySectionCell)var3.itemView).setText(LocaleController.formatSectionDate((long)var13.messageOwner.date));
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = new LoadingCell(this.mContext, AndroidUtilities.dp(32.0F), AndroidUtilities.dp(54.0F));
            } else {
               var3 = new SharedLinkCell(this.mContext);
               ((SharedLinkCell)var3).setDelegate(MediaActivity.this.sharedLinkCellDelegate);
            }
         } else {
            var3 = new GraySectionCell(this.mContext);
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   public static class SharedMediaData {
      private boolean[] endReached = new boolean[]{(boolean)0, (boolean)1};
      private boolean loading;
      private int[] max_id = new int[]{0, 0};
      private ArrayList messages = new ArrayList();
      private SparseArray[] messagesDict = new SparseArray[]{new SparseArray(), new SparseArray()};
      private HashMap sectionArrays = new HashMap();
      private ArrayList sections = new ArrayList();
      private int totalCount;

      public boolean addMessage(MessageObject var1, int var2, boolean var3, boolean var4) {
         if (this.messagesDict[var2].indexOfKey(var1.getId()) >= 0) {
            return false;
         } else {
            ArrayList var5 = (ArrayList)this.sectionArrays.get(var1.monthKey);
            ArrayList var6 = var5;
            if (var5 == null) {
               var6 = new ArrayList();
               this.sectionArrays.put(var1.monthKey, var6);
               if (var3) {
                  this.sections.add(0, var1.monthKey);
               } else {
                  this.sections.add(var1.monthKey);
               }
            }

            if (var3) {
               var6.add(0, var1);
               this.messages.add(0, var1);
            } else {
               var6.add(var1);
               this.messages.add(var1);
            }

            this.messagesDict[var2].put(var1.getId(), var1);
            if (!var4) {
               if (var1.getId() > 0) {
                  this.max_id[var2] = Math.min(var1.getId(), this.max_id[var2]);
               }
            } else {
               this.max_id[var2] = Math.max(var1.getId(), this.max_id[var2]);
            }

            return true;
         }
      }

      public boolean deleteMessage(int var1, int var2) {
         MessageObject var3 = (MessageObject)this.messagesDict[var2].get(var1);
         if (var3 == null) {
            return false;
         } else {
            ArrayList var4 = (ArrayList)this.sectionArrays.get(var3.monthKey);
            if (var4 == null) {
               return false;
            } else {
               var4.remove(var3);
               this.messages.remove(var3);
               this.messagesDict[var2].remove(var3.getId());
               if (var4.isEmpty()) {
                  this.sectionArrays.remove(var3.monthKey);
                  this.sections.remove(var3.monthKey);
               }

               --this.totalCount;
               return true;
            }
         }
      }

      public void replaceMid(int var1, int var2) {
         MessageObject var3 = (MessageObject)this.messagesDict[0].get(var1);
         if (var3 != null) {
            this.messagesDict[0].remove(var1);
            this.messagesDict[0].put(var2, var3);
            var3.messageOwner.id = var2;
         }

      }

      public void setEndReached(int var1, boolean var2) {
         this.endReached[var1] = var2;
      }

      public void setMaxId(int var1, int var2) {
         this.max_id[var1] = var2;
      }

      public void setTotalCount(int var1) {
         this.totalCount = var1;
      }
   }

   private class SharedPhotoVideoAdapter extends RecyclerListView.SectionsAdapter {
      private Context mContext;

      public SharedPhotoVideoAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getCountForSection(int var1) {
         return var1 < MediaActivity.this.sharedMediaData[0].sections.size() ? (int)Math.ceil((double)((float)((ArrayList)MediaActivity.this.sharedMediaData[0].sectionArrays.get(MediaActivity.this.sharedMediaData[0].sections.get(var1))).size() / (float)MediaActivity.this.columnsCount)) + 1 : 1;
      }

      public Object getItem(int var1, int var2) {
         return null;
      }

      public int getItemViewType(int var1, int var2) {
         if (var1 < MediaActivity.this.sharedMediaData[0].sections.size()) {
            return var2 == 0 ? 0 : 1;
         } else {
            return 2;
         }
      }

      public String getLetter(int var1) {
         return null;
      }

      public int getPositionForScrollProgress(float var1) {
         return 0;
      }

      public int getSectionCount() {
         MediaActivity.SharedMediaData[] var1 = MediaActivity.this.sharedMediaData;
         byte var2 = 0;
         int var3 = var1[0].sections.size();
         byte var4 = var2;
         if (!MediaActivity.this.sharedMediaData[0].sections.isEmpty()) {
            if (MediaActivity.this.sharedMediaData[0].endReached[0] && MediaActivity.this.sharedMediaData[0].endReached[1]) {
               var4 = var2;
            } else {
               var4 = 1;
            }
         }

         return var3 + var4;
      }

      public View getSectionHeaderView(int var1, View var2) {
         Object var3 = var2;
         if (var2 == null) {
            var3 = new SharedMediaSectionCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite") & -436207617);
         }

         if (var1 < MediaActivity.this.sharedMediaData[0].sections.size()) {
            String var4 = (String)MediaActivity.this.sharedMediaData[0].sections.get(var1);
            MessageObject var5 = (MessageObject)((ArrayList)MediaActivity.this.sharedMediaData[0].sectionArrays.get(var4)).get(0);
            ((SharedMediaSectionCell)var3).setText(LocaleController.formatSectionDate((long)var5.messageOwner.date));
         }

         return (View)var3;
      }

      public boolean isEnabled(int var1, int var2) {
         return false;
      }

      public void onBindViewHolder(int var1, int var2, RecyclerView.ViewHolder var3) {
         if (var3.getItemViewType() != 2) {
            String var4 = (String)MediaActivity.this.sharedMediaData[0].sections.get(var1);
            ArrayList var10 = (ArrayList)MediaActivity.this.sharedMediaData[0].sectionArrays.get(var4);
            var1 = var3.getItemViewType();
            if (var1 != 0) {
               if (var1 == 1) {
                  SharedPhotoVideoCell var5 = (SharedPhotoVideoCell)var3.itemView;
                  var5.setItemsCount(MediaActivity.this.columnsCount);
                  boolean var6;
                  if (var2 == 1) {
                     var6 = true;
                  } else {
                     var6 = false;
                  }

                  var5.setIsFirst(var6);

                  for(var1 = 0; var1 < MediaActivity.this.columnsCount; ++var1) {
                     int var7 = (var2 - 1) * MediaActivity.this.columnsCount + var1;
                     if (var7 < var10.size()) {
                        MessageObject var9 = (MessageObject)var10.get(var7);
                        var5.setItem(var1, MediaActivity.this.sharedMediaData[0].messages.indexOf(var9), var9);
                        if (MediaActivity.access$11000(MediaActivity.this).isActionModeShowed()) {
                           SparseArray[] var8 = MediaActivity.this.selectedFiles;
                           byte var12;
                           if (var9.getDialogId() == MediaActivity.this.dialog_id) {
                              var12 = 0;
                           } else {
                              var12 = 1;
                           }

                           if (var8[var12].indexOfKey(var9.getId()) >= 0) {
                              var6 = true;
                           } else {
                              var6 = false;
                           }

                           var5.setChecked(var1, var6, MediaActivity.this.scrolling ^ true);
                        } else {
                           var5.setChecked(var1, false, MediaActivity.this.scrolling ^ true);
                        }
                     } else {
                        var5.setItem(var1, var7, (MessageObject)null);
                     }
                  }

                  var5.requestLayout();
               }
            } else {
               MessageObject var11 = (MessageObject)var10.get(0);
               ((SharedMediaSectionCell)var3.itemView).setText(LocaleController.formatSectionDate((long)var11.messageOwner.date));
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var5;
         if (var2 != 0) {
            if (var2 != 1) {
               var5 = new LoadingCell(this.mContext, AndroidUtilities.dp(32.0F), AndroidUtilities.dp(74.0F));
            } else {
               if (!MediaActivity.this.cellCache.isEmpty()) {
                  View var3 = (View)MediaActivity.this.cellCache.get(0);
                  MediaActivity.this.cellCache.remove(0);
                  ViewGroup var4 = (ViewGroup)var3.getParent();
                  var5 = var3;
                  if (var4 != null) {
                     var4.removeView(var3);
                     var5 = var3;
                  }
               } else {
                  var5 = new SharedPhotoVideoCell(this.mContext);
               }

               SharedPhotoVideoCell var6 = (SharedPhotoVideoCell)var5;
               var6.setDelegate(new SharedPhotoVideoCell.SharedPhotoVideoCellDelegate() {
                  public void didClickItem(SharedPhotoVideoCell var1, int var2, MessageObject var3, int var4) {
                     MediaActivity.this.onItemClick(var2, var1, var3, var4, 0);
                  }

                  public boolean didLongClickItem(SharedPhotoVideoCell var1, int var2, MessageObject var3, int var4) {
                     if (MediaActivity.access$10700(MediaActivity.this).isActionModeShowed()) {
                        this.didClickItem(var1, var2, var3, var4);
                        return true;
                     } else {
                        return MediaActivity.this.onItemLongClick(var3, var1, var4);
                     }
                  }
               });
               MediaActivity.this.cache.add(var6);
            }
         } else {
            var5 = new SharedMediaSectionCell(this.mContext);
         }

         return new RecyclerListView.Holder((View)var5);
      }
   }
}
