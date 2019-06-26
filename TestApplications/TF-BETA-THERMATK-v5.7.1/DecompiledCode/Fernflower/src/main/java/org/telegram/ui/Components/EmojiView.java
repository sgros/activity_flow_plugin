package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.LongSparseArray;
import android.util.Property;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmojiData;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.ContentPreviewViewer$ContentPreviewViewerDelegate$_CC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.FeaturedStickerSetInfoCell;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Cells.StickerSetGroupInfoCell;
import org.telegram.ui.Cells.StickerSetNameCell;

public class EmojiView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
   private static final OnScrollChangedListener NOP;
   private static final Field superListenerField;
   private ImageView backspaceButton;
   private AnimatorSet backspaceButtonAnimation;
   private boolean backspaceOnce;
   private boolean backspacePressed;
   private FrameLayout bottomTabContainer;
   private AnimatorSet bottomTabContainerAnimation;
   private View bottomTabContainerBackground;
   private ContentPreviewViewer.ContentPreviewViewerDelegate contentPreviewViewerDelegate;
   private int currentAccount;
   private int currentBackgroundType;
   private int currentChatId;
   private int currentPage;
   private EmojiView.EmojiViewDelegate delegate;
   private Paint dotPaint;
   private EmojiView.DragListener dragListener;
   private EmojiView.EmojiGridAdapter emojiAdapter;
   private FrameLayout emojiContainer;
   private RecyclerListView emojiGridView;
   private Drawable[] emojiIcons;
   private float emojiLastX;
   private float emojiLastY;
   private GridLayoutManager emojiLayoutManager;
   private int emojiMinusDy;
   private EmojiView.EmojiSearchAdapter emojiSearchAdapter;
   private EmojiView.SearchField emojiSearchField;
   private int emojiSize;
   private AnimatorSet emojiTabShadowAnimator;
   private ScrollSlidingTabStrip emojiTabs;
   private View emojiTabsShadow;
   private String[] emojiTitles;
   private EmojiView.ImageViewEmoji emojiTouchedView;
   private float emojiTouchedX;
   private float emojiTouchedY;
   private int favTabBum;
   private ArrayList favouriteStickers;
   private int featuredStickersHash;
   private boolean firstEmojiAttach = true;
   private boolean firstGifAttach = true;
   private boolean firstStickersAttach = true;
   private ImageView floatingButton;
   private boolean forseMultiwindowLayout;
   private EmojiView.GifAdapter gifAdapter;
   private FrameLayout gifContainer;
   private RecyclerListView gifGridView;
   private ExtendedGridLayoutManager gifLayoutManager;
   private RecyclerListView.OnItemClickListener gifOnItemClickListener;
   private EmojiView.GifSearchAdapter gifSearchAdapter;
   private EmojiView.SearchField gifSearchField;
   private int groupStickerPackNum;
   private int groupStickerPackPosition;
   private TLRPC.TL_messages_stickerSet groupStickerSet;
   private boolean groupStickersHidden;
   private int hasRecentEmoji = -1;
   private TLRPC.ChatFull info;
   private LongSparseArray installingStickerSets;
   private boolean isLayout;
   private float lastBottomScrollDy;
   private int lastNotifyHeight;
   private int lastNotifyHeight2;
   private int lastNotifyWidth;
   private String[] lastSearchKeyboardLanguage;
   private int[] location;
   private TextView mediaBanTooltip;
   private boolean needEmojiSearch;
   private Object outlineProvider;
   private ViewPager pager;
   private EmojiView.EmojiColorPickerView pickerView;
   private EmojiView.EmojiPopupWindow pickerViewPopup;
   private int popupHeight;
   private int popupWidth;
   private ArrayList recentGifs;
   private ArrayList recentStickers;
   private int recentTabBum;
   private LongSparseArray removingStickerSets;
   private int scrolledToTrending;
   private AnimatorSet searchAnimation;
   private ImageView searchButton;
   private int searchFieldHeight;
   private View shadowLine;
   private boolean showGifs;
   private Drawable[] stickerIcons;
   private ArrayList stickerSets;
   private ImageView stickerSettingsButton;
   private AnimatorSet stickersButtonAnimation;
   private FrameLayout stickersContainer;
   private TextView stickersCounter;
   private EmojiView.StickersGridAdapter stickersGridAdapter;
   private RecyclerListView stickersGridView;
   private GridLayoutManager stickersLayoutManager;
   private int stickersMinusDy;
   private RecyclerListView.OnItemClickListener stickersOnItemClickListener;
   private EmojiView.SearchField stickersSearchField;
   private EmojiView.StickersSearchGridAdapter stickersSearchGridAdapter;
   private ScrollSlidingTabStrip stickersTab;
   private int stickersTabOffset;
   private Drawable[] tabIcons;
   private View topShadow;
   private EmojiView.TrendingGridAdapter trendingGridAdapter;
   private RecyclerListView trendingGridView;
   private GridLayoutManager trendingLayoutManager;
   private boolean trendingLoaded;
   private int trendingTabNum;
   private PagerSlidingTabStrip typeTabs;
   private ArrayList views = new ArrayList();

   static {
      Field var0 = null;

      label31: {
         Field var1;
         boolean var10001;
         try {
            var1 = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
         } catch (NoSuchFieldException var3) {
            var10001 = false;
            break label31;
         }

         var0 = var1;

         try {
            var1.setAccessible(true);
         } catch (NoSuchFieldException var2) {
            var10001 = false;
            break label31;
         }

         var0 = var1;
      }

      superListenerField = var0;
      NOP = _$$Lambda$EmojiView$PY_mfpY1F_JSo9ExUvlHDRxz4bQ.INSTANCE;
   }

   public EmojiView(boolean var1, boolean var2, Context var3, boolean var4, TLRPC.ChatFull var5) {
      super(var3);
      this.currentAccount = UserConfig.selectedAccount;
      this.stickerSets = new ArrayList();
      this.recentGifs = new ArrayList();
      this.recentStickers = new ArrayList();
      this.favouriteStickers = new ArrayList();
      this.installingStickerSets = new LongSparseArray();
      this.removingStickerSets = new LongSparseArray();
      this.location = new int[2];
      this.recentTabBum = -2;
      this.favTabBum = -2;
      this.trendingTabNum = -2;
      this.currentBackgroundType = -1;
      this.contentPreviewViewerDelegate = new ContentPreviewViewer.ContentPreviewViewerDelegate() {
         public void gifAddedOrDeleted() {
            EmojiView var1 = EmojiView.this;
            var1.recentGifs = DataQuery.getInstance(var1.currentAccount).getRecentGifs();
            if (EmojiView.this.gifAdapter != null) {
               EmojiView.this.gifAdapter.notifyDataSetChanged();
            }

         }

         // $FF: synthetic method
         public boolean needOpen() {
            return ContentPreviewViewer$ContentPreviewViewerDelegate$_CC.$default$needOpen(this);
         }

         public boolean needSend() {
            return true;
         }

         public void openSet(TLRPC.InputStickerSet var1, boolean var2) {
            if (var1 != null) {
               EmojiView.this.delegate.onShowStickerSet((TLRPC.StickerSet)null, var1);
            }
         }

         public void sendGif(Object var1) {
            if (EmojiView.this.gifGridView.getAdapter() == EmojiView.this.gifAdapter) {
               EmojiView.this.delegate.onGifSelected(var1, "gif");
            } else if (EmojiView.this.gifGridView.getAdapter() == EmojiView.this.gifSearchAdapter) {
               EmojiView.this.delegate.onGifSelected(var1, EmojiView.this.gifSearchAdapter.bot);
            }

         }

         public void sendSticker(TLRPC.Document var1, Object var2) {
            EmojiView.this.delegate.onStickerSelected(var1, var2);
         }
      };
      this.searchFieldHeight = AndroidUtilities.dp(64.0F);
      this.needEmojiSearch = var4;
      this.tabIcons = new Drawable[]{Theme.createEmojiIconSelectorDrawable(var3, 2131165852, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(var3, 2131165849, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(var3, 2131165853, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"))};
      Drawable var6 = Theme.createEmojiIconSelectorDrawable(var3, 2131165843, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
      Drawable var7 = Theme.createEmojiIconSelectorDrawable(var3, 2131165844, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
      Drawable var8 = Theme.createEmojiIconSelectorDrawable(var3, 2131165836, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
      Drawable var9 = Theme.createEmojiIconSelectorDrawable(var3, 2131165839, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
      Drawable var10 = Theme.createEmojiIconSelectorDrawable(var3, 2131165835, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
      Drawable var11 = Theme.createEmojiIconSelectorDrawable(var3, 2131165845, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"));
      byte var12 = 5;
      this.emojiIcons = new Drawable[]{var6, var7, var8, var9, var10, var11, Theme.createEmojiIconSelectorDrawable(var3, 2131165840, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(var3, 2131165841, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(var3, 2131165838, Theme.getColor("chat_emojiPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"))};
      this.stickerIcons = new Drawable[]{Theme.createEmojiIconSelectorDrawable(var3, 2131165843, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(var3, 2131165837, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected")), Theme.createEmojiIconSelectorDrawable(var3, 2131165846, Theme.getColor("chat_emojiBottomPanelIcon"), Theme.getColor("chat_emojiPanelIconSelected"))};
      this.emojiTitles = new String[]{LocaleController.getString("Emoji1", 2131559332), LocaleController.getString("Emoji2", 2131559333), LocaleController.getString("Emoji3", 2131559334), LocaleController.getString("Emoji4", 2131559335), LocaleController.getString("Emoji5", 2131559336), LocaleController.getString("Emoji6", 2131559337), LocaleController.getString("Emoji7", 2131559338), LocaleController.getString("Emoji8", 2131559339)};
      this.showGifs = var2;
      this.info = var5;
      this.dotPaint = new Paint(1);
      this.dotPaint.setColor(Theme.getColor("chat_emojiPanelNewTrending"));
      if (VERSION.SDK_INT >= 21) {
         this.outlineProvider = new ViewOutlineProvider() {
            @TargetApi(21)
            public void getOutline(View var1, Outline var2) {
               var2.setRoundRect(var1.getPaddingLeft(), var1.getPaddingTop(), var1.getMeasuredWidth() - var1.getPaddingRight(), var1.getMeasuredHeight() - var1.getPaddingBottom(), (float)AndroidUtilities.dp(6.0F));
            }
         };
      }

      this.emojiContainer = new FrameLayout(var3);
      this.views.add(this.emojiContainer);
      this.emojiGridView = new RecyclerListView(var3) {
         private boolean ignoreLayout;

         protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
            if (EmojiView.this.needEmojiSearch && EmojiView.this.firstEmojiAttach) {
               this.ignoreLayout = true;
               EmojiView.this.emojiLayoutManager.scrollToPositionWithOffset(1, 0);
               EmojiView.this.firstEmojiAttach = false;
               this.ignoreLayout = false;
            }

            super.onLayout(var1, var2, var3, var4, var5);
            EmojiView.this.checkEmojiSearchFieldScroll(true);
         }

         protected void onMeasure(int var1, int var2) {
            this.ignoreLayout = true;
            int var3 = MeasureSpec.getSize(var1);
            GridLayoutManager var4 = EmojiView.this.emojiLayoutManager;
            float var5;
            if (AndroidUtilities.isTablet()) {
               var5 = 60.0F;
            } else {
               var5 = 45.0F;
            }

            var4.setSpanCount(var3 / AndroidUtilities.dp(var5));
            this.ignoreLayout = false;
            super.onMeasure(var1, var2);
         }

         public boolean onTouchEvent(MotionEvent var1) {
            if (EmojiView.this.emojiTouchedView == null) {
               EmojiView.this.emojiLastX = var1.getX();
               EmojiView.this.emojiLastY = var1.getY();
               return super.onTouchEvent(var1);
            } else {
               int var2;
               if (var1.getAction() != 1 && var1.getAction() != 3) {
                  if (var1.getAction() == 2) {
                     boolean var8;
                     label69: {
                        if (EmojiView.this.emojiTouchedX != -10000.0F) {
                           if (Math.abs(EmojiView.this.emojiTouchedX - var1.getX()) <= AndroidUtilities.getPixelsInCM(0.2F, true) && Math.abs(EmojiView.this.emojiTouchedY - var1.getY()) <= AndroidUtilities.getPixelsInCM(0.2F, false)) {
                              var8 = true;
                              break label69;
                           }

                           EmojiView.this.emojiTouchedX = -10000.0F;
                           EmojiView.this.emojiTouchedY = -10000.0F;
                        }

                        var8 = false;
                     }

                     if (!var8) {
                        this.getLocationOnScreen(EmojiView.this.location);
                        float var3 = (float)EmojiView.this.location[0];
                        float var4 = var1.getX();
                        EmojiView.this.pickerView.getLocationOnScreen(EmojiView.this.location);
                        int var5 = (int)((var3 + var4 - (float)(EmojiView.this.location[0] + AndroidUtilities.dp(3.0F))) / (float)(EmojiView.this.emojiSize + AndroidUtilities.dp(4.0F)));
                        if (var5 < 0) {
                           var2 = 0;
                        } else {
                           var2 = var5;
                           if (var5 > 5) {
                              var2 = 5;
                           }
                        }

                        EmojiView.this.pickerView.setSelection(var2);
                     }
                  }
               } else {
                  if (EmojiView.this.pickerViewPopup != null && EmojiView.this.pickerViewPopup.isShowing()) {
                     EmojiView.this.pickerViewPopup.dismiss();
                     var2 = EmojiView.this.pickerView.getSelection();
                     String var7;
                     if (var2 != 1) {
                        if (var2 != 2) {
                           if (var2 != 3) {
                              if (var2 != 4) {
                                 if (var2 != 5) {
                                    var7 = null;
                                 } else {
                                    var7 = "\ud83c\udfff";
                                 }
                              } else {
                                 var7 = "\ud83c\udffe";
                              }
                           } else {
                              var7 = "\ud83c\udffd";
                           }
                        } else {
                           var7 = "\ud83c\udffc";
                        }
                     } else {
                        var7 = "\ud83c\udffb";
                     }

                     String var6 = (String)EmojiView.this.emojiTouchedView.getTag();
                     if (!EmojiView.this.emojiTouchedView.isRecent) {
                        if (var7 != null) {
                           Emoji.emojiColor.put(var6, var7);
                           var7 = EmojiView.addColorToCode(var6, var7);
                        } else {
                           Emoji.emojiColor.remove(var6);
                           var7 = var6;
                        }

                        EmojiView.this.emojiTouchedView.setImageDrawable(Emoji.getEmojiBigDrawable(var7), EmojiView.this.emojiTouchedView.isRecent);
                        EmojiView.this.emojiTouchedView.sendEmoji((String)null);
                        Emoji.saveEmojiColors();
                     } else {
                        var6 = var6.replace("\ud83c\udffb", "").replace("\ud83c\udffc", "").replace("\ud83c\udffd", "").replace("\ud83c\udffe", "").replace("\ud83c\udfff", "");
                        if (var7 != null) {
                           EmojiView.this.emojiTouchedView.sendEmoji(EmojiView.addColorToCode(var6, var7));
                        } else {
                           EmojiView.this.emojiTouchedView.sendEmoji(var6);
                        }
                     }
                  }

                  EmojiView.this.emojiTouchedView = null;
                  EmojiView.this.emojiTouchedX = -10000.0F;
                  EmojiView.this.emojiTouchedY = -10000.0F;
               }

               return true;
            }
         }

         public void requestLayout() {
            if (!this.ignoreLayout) {
               super.requestLayout();
            }
         }
      };
      this.emojiGridView.setInstantClick(true);
      RecyclerListView var29 = this.emojiGridView;
      GridLayoutManager var19 = new GridLayoutManager(var3, 8);
      this.emojiLayoutManager = var19;
      var29.setLayoutManager(var19);
      this.emojiGridView.setTopGlowOffset(AndroidUtilities.dp(38.0F));
      this.emojiGridView.setBottomGlowOffset(AndroidUtilities.dp(48.0F));
      this.emojiGridView.setPadding(0, AndroidUtilities.dp(38.0F), 0, 0);
      this.emojiGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
      this.emojiGridView.setClipToPadding(false);
      this.emojiLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
         public int getSpanSize(int var1) {
            if (EmojiView.this.emojiGridView.getAdapter() == EmojiView.this.emojiSearchAdapter) {
               if (var1 == 0 || var1 == 1 && EmojiView.this.emojiSearchAdapter.searchWas && EmojiView.this.emojiSearchAdapter.result.isEmpty()) {
                  return EmojiView.this.emojiLayoutManager.getSpanCount();
               }
            } else if (EmojiView.this.needEmojiSearch && var1 == 0 || EmojiView.this.emojiAdapter.positionToSection.indexOfKey(var1) >= 0) {
               return EmojiView.this.emojiLayoutManager.getSpanCount();
            }

            return 1;
         }
      });
      RecyclerListView var20 = this.emojiGridView;
      EmojiView.EmojiGridAdapter var30 = new EmojiView.EmojiGridAdapter();
      this.emojiAdapter = var30;
      var20.setAdapter(var30);
      this.emojiSearchAdapter = new EmojiView.EmojiSearchAdapter();
      this.emojiContainer.addView(this.emojiGridView, LayoutHelper.createFrame(-1, -1.0F));
      this.emojiGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1 && EmojiView.this.emojiSearchField != null) {
               EmojiView.this.emojiSearchField.hideKeyboard();
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            int var4 = EmojiView.this.emojiLayoutManager.findFirstVisibleItemPosition();
            if (var4 != -1) {
               label23: {
                  var2 = Emoji.recentEmoji.size() + EmojiView.this.needEmojiSearch;
                  if (var4 >= var2) {
                     int var5 = 0;

                     while(true) {
                        String[][] var6 = EmojiData.dataColored;
                        if (var5 >= var6.length) {
                           break;
                        }

                        var2 += var6[var5].length + 1;
                        if (var4 < var2) {
                           var2 = (Emoji.recentEmoji.isEmpty() ^ 1) + var5;
                           break label23;
                        }

                        ++var5;
                     }
                  }

                  var2 = 0;
               }

               EmojiView.this.emojiTabs.onPageScrolled(var2, 0);
            }

            EmojiView.this.checkEmojiTabY(var1, var3);
            EmojiView.this.checkEmojiSearchFieldScroll(false);
            EmojiView.this.checkBottomTabScroll((float)var3);
         }
      });
      this.emojiGridView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() {
         public void onItemClick(View var1, int var2) {
            if (var1 instanceof EmojiView.ImageViewEmoji) {
               ((EmojiView.ImageViewEmoji)var1).sendEmoji((String)null);
            }

         }
      });
      this.emojiGridView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() {
         public boolean onItemClick(View var1, int var2) {
            boolean var3 = var1 instanceof EmojiView.ImageViewEmoji;
            byte var4 = 0;
            if (var3) {
               EmojiView.ImageViewEmoji var5 = (EmojiView.ImageViewEmoji)var1;
               String var6 = (String)var5.getTag();
               String var7 = null;
               String var8 = var6.replace("\ud83c\udffb", "");
               if (var8 != var6) {
                  var7 = "\ud83c\udffb";
               }

               String var9 = var7;
               String var10;
               if (var7 == null) {
                  var10 = var6.replace("\ud83c\udffc", "");
                  var9 = var7;
                  var8 = var10;
                  if (var10 != var6) {
                     var9 = "\ud83c\udffc";
                     var8 = var10;
                  }
               }

               var7 = var9;
               if (var9 == null) {
                  var10 = var6.replace("\ud83c\udffd", "");
                  var7 = var9;
                  var8 = var10;
                  if (var10 != var6) {
                     var7 = "\ud83c\udffd";
                     var8 = var10;
                  }
               }

               var9 = var7;
               if (var7 == null) {
                  var10 = var6.replace("\ud83c\udffe", "");
                  var9 = var7;
                  var8 = var10;
                  if (var10 != var6) {
                     var9 = "\ud83c\udffe";
                     var8 = var10;
                  }
               }

               var7 = var9;
               if (var9 == null) {
                  var10 = var6.replace("\ud83c\udfff", "");
                  var7 = var9;
                  var8 = var10;
                  if (var10 != var6) {
                     var7 = "\ud83c\udfff";
                     var8 = var10;
                  }
               }

               if (EmojiData.emojiColoredMap.containsKey(var8)) {
                  EmojiView.this.emojiTouchedView = var5;
                  EmojiView var20 = EmojiView.this;
                  var20.emojiTouchedX = var20.emojiLastX;
                  var20 = EmojiView.this;
                  var20.emojiTouchedY = var20.emojiLastY;
                  var9 = var7;
                  if (var7 == null) {
                     var9 = var7;
                     if (!var5.isRecent) {
                        var9 = (String)Emoji.emojiColor.get(var8);
                     }
                  }

                  byte var11 = 5;
                  if (var9 != null) {
                     byte var17;
                     label108: {
                        switch(var9.hashCode()) {
                        case 1773375:
                           if (var9.equals("\ud83c\udffb")) {
                              var17 = 0;
                              break label108;
                           }
                           break;
                        case 1773376:
                           if (var9.equals("\ud83c\udffc")) {
                              var17 = 1;
                              break label108;
                           }
                           break;
                        case 1773377:
                           if (var9.equals("\ud83c\udffd")) {
                              var17 = 2;
                              break label108;
                           }
                           break;
                        case 1773378:
                           if (var9.equals("\ud83c\udffe")) {
                              var17 = 3;
                              break label108;
                           }
                           break;
                        case 1773379:
                           if (var9.equals("\ud83c\udfff")) {
                              var17 = 4;
                              break label108;
                           }
                        }

                        var17 = -1;
                     }

                     if (var17 != 0) {
                        if (var17 != 1) {
                           if (var17 != 2) {
                              if (var17 != 3) {
                                 if (var17 == 4) {
                                    EmojiView.this.pickerView.setSelection(5);
                                 }
                              } else {
                                 EmojiView.this.pickerView.setSelection(4);
                              }
                           } else {
                              EmojiView.this.pickerView.setSelection(3);
                           }
                        } else {
                           EmojiView.this.pickerView.setSelection(2);
                        }
                     } else {
                        EmojiView.this.pickerView.setSelection(1);
                     }
                  } else {
                     EmojiView.this.pickerView.setSelection(0);
                  }

                  var5.getLocationOnScreen(EmojiView.this.location);
                  int var12 = EmojiView.this.emojiSize;
                  int var13 = EmojiView.this.pickerView.getSelection();
                  int var14 = EmojiView.this.pickerView.getSelection();
                  byte var18;
                  if (AndroidUtilities.isTablet()) {
                     var18 = var11;
                  } else {
                     var18 = 1;
                  }

                  int var19;
                  label96: {
                     var19 = var12 * var13 + AndroidUtilities.dp((float)(var14 * 4 - var18));
                     if (EmojiView.this.location[0] - var19 < AndroidUtilities.dp(5.0F)) {
                        var2 = EmojiView.this.location[0] - var19 - AndroidUtilities.dp(5.0F);
                     } else {
                        var2 = var19;
                        if (EmojiView.this.location[0] - var19 + EmojiView.this.popupWidth <= AndroidUtilities.displaySize.x - AndroidUtilities.dp(5.0F)) {
                           break label96;
                        }

                        var2 = EmojiView.this.location[0] - var19 + EmojiView.this.popupWidth - (AndroidUtilities.displaySize.x - AndroidUtilities.dp(5.0F));
                     }

                     var2 += var19;
                  }

                  var19 = -var2;
                  var2 = var4;
                  if (var5.getTop() < 0) {
                     var2 = var5.getTop();
                  }

                  EmojiView.EmojiColorPickerView var21 = EmojiView.this.pickerView;
                  float var15;
                  if (AndroidUtilities.isTablet()) {
                     var15 = 30.0F;
                  } else {
                     var15 = 22.0F;
                  }

                  var21.setEmoji(var8, AndroidUtilities.dp(var15) - var19 + (int)AndroidUtilities.dpf2(0.5F));
                  EmojiView.this.pickerViewPopup.setFocusable(true);
                  EmojiView.this.pickerViewPopup.showAsDropDown(var1, var19, -var1.getMeasuredHeight() - EmojiView.this.popupHeight + (var1.getMeasuredHeight() - EmojiView.this.emojiSize) / 2 - var2);
                  EmojiView.this.pager.requestDisallowInterceptTouchEvent(true);
                  EmojiView.this.emojiGridView.hideSelector();
                  return true;
               }

               if (var5.isRecent) {
                  RecyclerView.ViewHolder var16 = EmojiView.this.emojiGridView.findContainingViewHolder(var1);
                  if (var16 != null && var16.getAdapterPosition() <= Emoji.recentEmoji.size()) {
                     EmojiView.this.delegate.onClearEmojiRecent();
                  }

                  return true;
               }
            }

            return false;
         }
      });
      this.emojiTabs = new ScrollSlidingTabStrip(var3);
      if (var4) {
         this.emojiSearchField = new EmojiView.SearchField(var3, 1);
         this.emojiContainer.addView(this.emojiSearchField, new LayoutParams(-1, this.searchFieldHeight + AndroidUtilities.getShadowHeight()));
         this.emojiSearchField.searchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View var1, boolean var2) {
               if (var2) {
                  EmojiView.this.lastSearchKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
                  DataQuery.getInstance(EmojiView.this.currentAccount).fetchNewEmojiKeywords(EmojiView.this.lastSearchKeyboardLanguage);
               }

            }
         });
      }

      this.emojiTabs.setShouldExpand(true);
      this.emojiTabs.setIndicatorHeight(-1);
      this.emojiTabs.setUnderlineHeight(-1);
      this.emojiTabs.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
      this.emojiContainer.addView(this.emojiTabs, LayoutHelper.createFrame(-1, 38.0F));
      this.emojiTabs.setDelegate(new ScrollSlidingTabStrip.ScrollSlidingTabStripDelegate() {
         public void onPageSelected(int var1) {
            int var2 = var1;
            if (!Emoji.recentEmoji.isEmpty()) {
               if (var1 == 0) {
                  EmojiView.this.emojiLayoutManager.scrollToPositionWithOffset(EmojiView.this.needEmojiSearch, 0);
                  return;
               }

               var2 = var1 - 1;
            }

            EmojiView.this.emojiGridView.stopScroll();
            EmojiView.this.emojiLayoutManager.scrollToPositionWithOffset(EmojiView.this.emojiAdapter.sectionToPosition.get(var2), 0);
            EmojiView.this.checkEmojiTabY((View)null, 0);
         }
      });
      this.emojiTabsShadow = new View(var3);
      this.emojiTabsShadow.setAlpha(0.0F);
      this.emojiTabsShadow.setTag(1);
      this.emojiTabsShadow.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
      LayoutParams var21 = new LayoutParams(-1, AndroidUtilities.getShadowHeight(), 51);
      var21.topMargin = AndroidUtilities.dp(38.0F);
      this.emojiContainer.addView(this.emojiTabsShadow, var21);
      if (var1) {
         if (var2) {
            this.gifContainer = new FrameLayout(var3);
            this.views.add(this.gifContainer);
            this.gifGridView = new RecyclerListView(var3) {
               private boolean ignoreLayout;

               public boolean onInterceptTouchEvent(MotionEvent var1) {
                  ContentPreviewViewer var2 = ContentPreviewViewer.getInstance();
                  RecyclerListView var3 = EmojiView.this.gifGridView;
                  ContentPreviewViewer.ContentPreviewViewerDelegate var4 = EmojiView.this.contentPreviewViewerDelegate;
                  boolean var5 = false;
                  boolean var6 = var2.onInterceptTouchEvent(var1, var3, 0, var4);
                  if (super.onInterceptTouchEvent(var1) || var6) {
                     var5 = true;
                  }

                  return var5;
               }

               protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
                  if (EmojiView.this.firstGifAttach && EmojiView.this.gifAdapter.getItemCount() > 1) {
                     this.ignoreLayout = true;
                     EmojiView.this.gifLayoutManager.scrollToPositionWithOffset(1, 0);
                     EmojiView.this.firstGifAttach = false;
                     this.ignoreLayout = false;
                  }

                  super.onLayout(var1, var2, var3, var4, var5);
                  EmojiView.this.checkGifSearchFieldScroll(true);
               }

               public void requestLayout() {
                  if (!this.ignoreLayout) {
                     super.requestLayout();
                  }
               }
            };
            this.gifGridView.setClipToPadding(false);
            var20 = this.gifGridView;
            ExtendedGridLayoutManager var31 = new ExtendedGridLayoutManager(var3, 100) {
               private Size size = new Size();

               protected int getFlowItemCount() {
                  return EmojiView.this.gifGridView.getAdapter() == EmojiView.this.gifSearchAdapter && EmojiView.this.gifSearchAdapter.results.isEmpty() ? 0 : this.getItemCount() - 1;
               }

               protected Size getSizeForItem(int var1) {
                  RecyclerView.Adapter var2 = EmojiView.this.gifGridView.getAdapter();
                  EmojiView.GifAdapter var3 = EmojiView.this.gifAdapter;
                  TLRPC.Document var4 = null;
                  ArrayList var5 = null;
                  if (var2 == var3) {
                     var4 = (TLRPC.Document)EmojiView.this.recentGifs.get(var1);
                     var5 = var4.attributes;
                  } else if (!EmojiView.this.gifSearchAdapter.results.isEmpty()) {
                     TLRPC.BotInlineResult var7 = (TLRPC.BotInlineResult)EmojiView.this.gifSearchAdapter.results.get(var1);
                     var4 = var7.document;
                     if (var4 != null) {
                        var5 = var4.attributes;
                     } else {
                        TLRPC.WebDocument var10 = var7.content;
                        if (var10 != null) {
                           var5 = var10.attributes;
                        } else {
                           TLRPC.WebDocument var8 = var7.thumb;
                           if (var8 != null) {
                              var5 = var8.attributes;
                           }
                        }
                     }
                  } else {
                     var5 = null;
                  }

                  Size var9 = this.size;
                  var9.height = 100.0F;
                  var9.width = 100.0F;
                  if (var4 != null) {
                     TLRPC.PhotoSize var11 = FileLoader.getClosestPhotoSizeWithSize(var4.thumbs, 90);
                     if (var11 != null) {
                        int var6 = var11.w;
                        if (var6 != 0) {
                           var1 = var11.h;
                           if (var1 != 0) {
                              Size var12 = this.size;
                              var12.width = (float)var6;
                              var12.height = (float)var1;
                           }
                        }
                     }
                  }

                  if (var5 != null) {
                     for(var1 = 0; var1 < var5.size(); ++var1) {
                        TLRPC.DocumentAttribute var13 = (TLRPC.DocumentAttribute)var5.get(var1);
                        if (var13 instanceof TLRPC.TL_documentAttributeImageSize || var13 instanceof TLRPC.TL_documentAttributeVideo) {
                           Size var14 = this.size;
                           var14.width = (float)var13.w;
                           var14.height = (float)var13.h;
                           break;
                        }
                     }
                  }

                  return this.size;
               }
            };
            this.gifLayoutManager = var31;
            var20.setLayoutManager(var31);
            this.gifLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
               public int getSpanSize(int var1) {
                  return var1 == 0 || EmojiView.this.gifGridView.getAdapter() == EmojiView.this.gifSearchAdapter && EmojiView.this.gifSearchAdapter.results.isEmpty() ? EmojiView.this.gifLayoutManager.getSpanCount() : EmojiView.this.gifLayoutManager.getSpanSizeForItem(var1 - 1);
               }
            });
            this.gifGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
               public void getItemOffsets(android.graphics.Rect var1, View var2, RecyclerView var3, RecyclerView.State var4) {
                  int var5 = var3.getChildAdapterPosition(var2);
                  int var6 = 0;
                  if (var5 != 0) {
                     var1.left = 0;
                     var1.bottom = 0;
                     ExtendedGridLayoutManager var7 = EmojiView.this.gifLayoutManager;
                     --var5;
                     if (!var7.isFirstRow(var5)) {
                        var1.top = AndroidUtilities.dp(2.0F);
                     } else {
                        var1.top = 0;
                     }

                     if (!EmojiView.this.gifLayoutManager.isLastInRow(var5)) {
                        var6 = AndroidUtilities.dp(2.0F);
                     }

                     var1.right = var6;
                  } else {
                     var1.left = 0;
                     var1.top = 0;
                     var1.bottom = 0;
                     var1.right = 0;
                  }

               }
            });
            this.gifGridView.setOverScrollMode(2);
            var29 = this.gifGridView;
            EmojiView.GifAdapter var22 = new EmojiView.GifAdapter(var3);
            this.gifAdapter = var22;
            var29.setAdapter(var22);
            this.gifSearchAdapter = new EmojiView.GifSearchAdapter(var3);
            this.gifGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
               public void onScrollStateChanged(RecyclerView var1, int var2) {
                  if (var2 == 1) {
                     EmojiView.this.gifSearchField.hideKeyboard();
                  }

               }

               public void onScrolled(RecyclerView var1, int var2, int var3) {
                  EmojiView.this.checkGifSearchFieldScroll(false);
                  EmojiView.this.checkBottomTabScroll((float)var3);
               }
            });
            this.gifGridView.setOnTouchListener(new _$$Lambda$EmojiView$l7_k5UkFPpeHpKVTvoFojtQIyNg(this));
            this.gifOnItemClickListener = new _$$Lambda$EmojiView$qDPcvu2cn8NHi3uzHeRPO6CJgew(this);
            this.gifGridView.setOnItemClickListener(this.gifOnItemClickListener);
            this.gifContainer.addView(this.gifGridView, LayoutHelper.createFrame(-1, -1.0F));
            this.gifSearchField = new EmojiView.SearchField(var3, 2);
            this.gifContainer.addView(this.gifSearchField, new LayoutParams(-1, this.searchFieldHeight + AndroidUtilities.getShadowHeight()));
         }

         this.stickersContainer = new FrameLayout(var3);
         DataQuery.getInstance(this.currentAccount).checkStickers(0);
         DataQuery.getInstance(this.currentAccount).checkFeaturedStickers();
         this.stickersGridView = new RecyclerListView(var3) {
            boolean ignoreLayout;

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               boolean var2 = ContentPreviewViewer.getInstance().onInterceptTouchEvent(var1, EmojiView.this.stickersGridView, EmojiView.this.getMeasuredHeight(), EmojiView.this.contentPreviewViewerDelegate);
               if (!super.onInterceptTouchEvent(var1) && !var2) {
                  var2 = false;
               } else {
                  var2 = true;
               }

               return var2;
            }

            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               if (EmojiView.this.firstStickersAttach && EmojiView.this.stickersGridAdapter.getItemCount() > 0) {
                  this.ignoreLayout = true;
                  EmojiView.this.stickersLayoutManager.scrollToPositionWithOffset(1, 0);
                  EmojiView.this.firstStickersAttach = false;
                  this.ignoreLayout = false;
               }

               super.onLayout(var1, var2, var3, var4, var5);
               EmojiView.this.checkStickersSearchFieldScroll(true);
            }

            public void requestLayout() {
               if (!this.ignoreLayout) {
                  super.requestLayout();
               }
            }

            public void setVisibility(int var1) {
               if (EmojiView.this.trendingGridView != null && EmojiView.this.trendingGridView.getVisibility() == 0) {
                  super.setVisibility(8);
               } else {
                  super.setVisibility(var1);
               }
            }
         };
         var20 = this.stickersGridView;
         GridLayoutManager var32 = new GridLayoutManager(var3, 5);
         this.stickersLayoutManager = var32;
         var20.setLayoutManager(var32);
         this.stickersLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int var1) {
               if (EmojiView.this.stickersGridView.getAdapter() == EmojiView.this.stickersGridAdapter) {
                  if (var1 == 0) {
                     return EmojiView.this.stickersGridAdapter.stickersPerRow;
                  } else {
                     return var1 == EmojiView.this.stickersGridAdapter.totalItems || EmojiView.this.stickersGridAdapter.cache.get(var1) != null && !(EmojiView.this.stickersGridAdapter.cache.get(var1) instanceof TLRPC.Document) ? EmojiView.this.stickersGridAdapter.stickersPerRow : 1;
                  }
               } else {
                  return var1 == EmojiView.this.stickersSearchGridAdapter.totalItems || EmojiView.this.stickersSearchGridAdapter.cache.get(var1) != null && !(EmojiView.this.stickersSearchGridAdapter.cache.get(var1) instanceof TLRPC.Document) ? EmojiView.this.stickersGridAdapter.stickersPerRow : 1;
               }
            }
         });
         this.stickersGridView.setPadding(0, AndroidUtilities.dp(52.0F), 0, 0);
         this.stickersGridView.setClipToPadding(false);
         this.views.add(this.stickersContainer);
         this.stickersSearchGridAdapter = new EmojiView.StickersSearchGridAdapter(var3);
         var29 = this.stickersGridView;
         EmojiView.StickersGridAdapter var23 = new EmojiView.StickersGridAdapter(var3);
         this.stickersGridAdapter = var23;
         var29.setAdapter(var23);
         this.stickersGridView.setOnTouchListener(new _$$Lambda$EmojiView$qCmH2CuXJBuotfYIBsa5LNpa8lQ(this));
         this.stickersOnItemClickListener = new _$$Lambda$EmojiView$sBpUanL5xQpXEXKoK_PK5yrP_k0(this);
         this.stickersGridView.setOnItemClickListener(this.stickersOnItemClickListener);
         this.stickersGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
         this.stickersContainer.addView(this.stickersGridView);
         this.stickersTab = new ScrollSlidingTabStrip(var3) {
            float downX;
            float downY;
            boolean draggingHorizontally;
            boolean draggingVertically;
            boolean first = true;
            float lastTranslateX;
            float lastX;
            boolean startedScroll;
            final int touchslop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
            VelocityTracker vTracker;

            public boolean onInterceptTouchEvent(MotionEvent var1) {
               if (this.getParent() != null) {
                  this.getParent().requestDisallowInterceptTouchEvent(true);
               }

               if (var1.getAction() == 0) {
                  this.draggingHorizontally = false;
                  this.draggingVertically = false;
                  this.downX = var1.getRawX();
                  this.downY = var1.getRawY();
               } else if (!this.draggingVertically && !this.draggingHorizontally && EmojiView.this.dragListener != null && Math.abs(var1.getRawY() - this.downY) >= (float)this.touchslop) {
                  this.draggingVertically = true;
                  this.downY = var1.getRawY();
                  EmojiView.this.dragListener.onDragStart();
                  if (this.startedScroll) {
                     EmojiView.this.pager.endFakeDrag();
                     this.startedScroll = false;
                  }

                  return true;
               }

               return super.onInterceptTouchEvent(var1);
            }

            public boolean onTouchEvent(MotionEvent var1) {
               boolean var2 = this.first;
               boolean var3 = false;
               if (var2) {
                  this.first = false;
                  this.lastX = var1.getX();
               }

               if (var1.getAction() == 0) {
                  this.draggingHorizontally = false;
                  this.draggingVertically = false;
                  this.downX = var1.getRawX();
                  this.downY = var1.getRawY();
               } else if (!this.draggingVertically && !this.draggingHorizontally && EmojiView.this.dragListener != null) {
                  if (Math.abs(var1.getRawX() - this.downX) >= (float)this.touchslop) {
                     this.draggingHorizontally = true;
                  } else if (Math.abs(var1.getRawY() - this.downY) >= (float)this.touchslop) {
                     this.draggingVertically = true;
                     this.downY = var1.getRawY();
                     EmojiView.this.dragListener.onDragStart();
                     if (this.startedScroll) {
                        EmojiView.this.pager.endFakeDrag();
                        this.startedScroll = false;
                     }
                  }
               }

               float var4;
               if (this.draggingVertically) {
                  if (this.vTracker == null) {
                     this.vTracker = VelocityTracker.obtain();
                  }

                  this.vTracker.addMovement(var1);
                  if (var1.getAction() != 1 && var1.getAction() != 3) {
                     EmojiView.this.dragListener.onDrag(Math.round(var1.getRawY() - this.downY));
                  } else {
                     this.vTracker.computeCurrentVelocity(1000);
                     var4 = this.vTracker.getYVelocity();
                     this.vTracker.recycle();
                     this.vTracker = null;
                     if (var1.getAction() == 1) {
                        EmojiView.this.dragListener.onDragEnd(var4);
                     } else {
                        EmojiView.this.dragListener.onDragCancel();
                     }

                     this.first = true;
                     this.draggingHorizontally = false;
                     this.draggingVertically = false;
                  }

                  return true;
               } else {
                  var4 = EmojiView.this.stickersTab.getTranslationX();
                  if (EmojiView.this.stickersTab.getScrollX() == 0 && var4 == 0.0F) {
                     if (!this.startedScroll && this.lastX - var1.getX() < 0.0F) {
                        if (EmojiView.this.pager.beginFakeDrag()) {
                           this.startedScroll = true;
                           this.lastTranslateX = EmojiView.this.stickersTab.getTranslationX();
                        }
                     } else if (this.startedScroll && this.lastX - var1.getX() > 0.0F && EmojiView.this.pager.isFakeDragging()) {
                        EmojiView.this.pager.endFakeDrag();
                        this.startedScroll = false;
                     }
                  }

                  if (this.startedScroll) {
                     var1.getX();
                     float var5 = this.lastX;
                     var5 = this.lastTranslateX;

                     try {
                        this.lastTranslateX = var4;
                     } catch (Exception var9) {
                        try {
                           EmojiView.this.pager.endFakeDrag();
                        } catch (Exception var8) {
                        }

                        this.startedScroll = false;
                        FileLog.e((Throwable)var9);
                     }
                  }

                  this.lastX = var1.getX();
                  if (var1.getAction() == 3 || var1.getAction() == 1) {
                     this.first = true;
                     this.draggingHorizontally = false;
                     this.draggingVertically = false;
                     if (this.startedScroll) {
                        EmojiView.this.pager.endFakeDrag();
                        this.startedScroll = false;
                     }
                  }

                  if (this.startedScroll || super.onTouchEvent(var1)) {
                     var3 = true;
                  }

                  return var3;
               }
            }
         };
         this.stickersSearchField = new EmojiView.SearchField(var3, 0);
         this.stickersContainer.addView(this.stickersSearchField, new LayoutParams(-1, this.searchFieldHeight + AndroidUtilities.getShadowHeight()));
         this.trendingGridView = new RecyclerListView(var3);
         this.trendingGridView.setItemAnimator((RecyclerView.ItemAnimator)null);
         this.trendingGridView.setLayoutAnimation((LayoutAnimationController)null);
         var20 = this.trendingGridView;
         var32 = new GridLayoutManager(var3, 5) {
            public boolean supportsPredictiveItemAnimations() {
               return false;
            }
         };
         this.trendingLayoutManager = var32;
         var20.setLayoutManager(var32);
         this.trendingLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            public int getSpanSize(int var1) {
               return !(EmojiView.this.trendingGridAdapter.cache.get(var1) instanceof Integer) && var1 != EmojiView.this.trendingGridAdapter.totalItems ? 1 : EmojiView.this.trendingGridAdapter.stickersPerRow;
            }
         });
         this.trendingGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView var1, int var2, int var3) {
               EmojiView.this.checkStickersTabY(var1, var3);
               EmojiView.this.checkBottomTabScroll((float)var3);
            }
         });
         this.trendingGridView.setClipToPadding(false);
         this.trendingGridView.setPadding(0, AndroidUtilities.dp(48.0F), 0, 0);
         var29 = this.trendingGridView;
         EmojiView.TrendingGridAdapter var24 = new EmojiView.TrendingGridAdapter(var3);
         this.trendingGridAdapter = var24;
         var29.setAdapter(var24);
         this.trendingGridView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$EmojiView$dYtQT1qlPrE3oYWHySyt2vGHEgc(this)));
         this.trendingGridAdapter.notifyDataSetChanged();
         this.trendingGridView.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
         this.trendingGridView.setVisibility(8);
         this.stickersContainer.addView(this.trendingGridView);
         this.stickersTab.setUnderlineHeight(AndroidUtilities.getShadowHeight());
         this.stickersTab.setIndicatorHeight(AndroidUtilities.dp(2.0F));
         this.stickersTab.setIndicatorColor(Theme.getColor("chat_emojiPanelStickerPackSelectorLine"));
         this.stickersTab.setUnderlineColor(Theme.getColor("chat_emojiPanelShadowLine"));
         this.stickersTab.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
         this.stickersContainer.addView(this.stickersTab, LayoutHelper.createFrame(-1, 48, 51));
         this.updateStickerTabs();
         this.stickersTab.setDelegate(new _$$Lambda$EmojiView$EeKK6r6yP0jttMlGjDS2ja6SA2o(this));
         this.stickersGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView var1, int var2) {
               if (var2 == 1) {
                  EmojiView.this.stickersSearchField.hideKeyboard();
               }

            }

            public void onScrolled(RecyclerView var1, int var2, int var3) {
               EmojiView.this.checkScroll();
               EmojiView.this.checkStickersTabY(var1, var3);
               EmojiView.this.checkStickersSearchFieldScroll(false);
               EmojiView.this.checkBottomTabScroll((float)var3);
            }
         });
      }

      this.pager = new ViewPager(var3) {
         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (this.getParent() != null) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            return super.onInterceptTouchEvent(var1);
         }

         public void setCurrentItem(int var1, boolean var2) {
            EmojiView var3 = EmojiView.this;
            boolean var4;
            if (var1 == 1) {
               var4 = true;
            } else {
               var4 = false;
            }

            var3.startStopVisibleGifs(var4);
            if (var1 == this.getCurrentItem()) {
               if (var1 == 0) {
                  EmojiView.this.emojiGridView.smoothScrollToPosition(EmojiView.this.needEmojiSearch);
               } else if (var1 == 1) {
                  EmojiView.this.gifGridView.smoothScrollToPosition(1);
               } else {
                  EmojiView.this.stickersGridView.smoothScrollToPosition(1);
               }

            } else {
               super.setCurrentItem(var1, var2);
            }
         }
      };
      this.pager.setAdapter(new EmojiView.EmojiPagesAdapter());
      this.topShadow = new View(var3);
      this.topShadow.setBackgroundDrawable(Theme.getThemedDrawable(var3, 2131165395, -1907225));
      this.addView(this.topShadow, LayoutHelper.createFrame(-1, 6.0F));
      this.backspaceButton = new ImageView(var3) {
         public boolean onTouchEvent(MotionEvent var1) {
            if (var1.getAction() == 0) {
               EmojiView.this.backspacePressed = true;
               EmojiView.this.backspaceOnce = false;
               EmojiView.this.postBackspaceRunnable(350);
            } else if (var1.getAction() == 3 || var1.getAction() == 1) {
               EmojiView.this.backspacePressed = false;
               if (!EmojiView.this.backspaceOnce && EmojiView.this.delegate != null && EmojiView.this.delegate.onBackspace()) {
                  EmojiView.this.backspaceButton.performHapticFeedback(3);
               }
            }

            super.onTouchEvent(var1);
            return true;
         }
      };
      this.backspaceButton.setImageResource(2131165848);
      this.backspaceButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), Mode.MULTIPLY));
      this.backspaceButton.setScaleType(ScaleType.CENTER);
      this.backspaceButton.setContentDescription(LocaleController.getString("AccDescrBackspace", 2131558414));
      this.backspaceButton.setFocusable(true);
      this.bottomTabContainer = new FrameLayout(var3) {
         public boolean onInterceptTouchEvent(MotionEvent var1) {
            if (this.getParent() != null) {
               this.getParent().requestDisallowInterceptTouchEvent(true);
            }

            return super.onInterceptTouchEvent(var1);
         }
      };
      this.shadowLine = new View(var3);
      this.shadowLine.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
      this.bottomTabContainer.addView(this.shadowLine, new LayoutParams(-1, AndroidUtilities.getShadowHeight()));
      this.bottomTabContainerBackground = new View(var3);
      this.bottomTabContainer.addView(this.bottomTabContainerBackground, new LayoutParams(-1, AndroidUtilities.dp(44.0F), 83));
      byte var13 = 40;
      byte var14 = 44;
      byte var15;
      float var17;
      if (var4) {
         this.addView(this.bottomTabContainer, new LayoutParams(-1, AndroidUtilities.dp(44.0F) + AndroidUtilities.getShadowHeight(), 83));
         this.bottomTabContainer.addView(this.backspaceButton, LayoutHelper.createFrame(52, 44, 85));
         this.stickerSettingsButton = new ImageView(var3);
         this.stickerSettingsButton.setImageResource(2131165851);
         this.stickerSettingsButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), Mode.MULTIPLY));
         this.stickerSettingsButton.setScaleType(ScaleType.CENTER);
         this.stickerSettingsButton.setFocusable(true);
         this.stickerSettingsButton.setContentDescription(LocaleController.getString("Settings", 2131560738));
         this.bottomTabContainer.addView(this.stickerSettingsButton, LayoutHelper.createFrame(52, 44, 85));
         this.stickerSettingsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               if (EmojiView.this.delegate != null) {
                  EmojiView.this.delegate.onStickersSettingsClick();
               }

            }
         });
         this.typeTabs = new PagerSlidingTabStrip(var3);
         this.typeTabs.setViewPager(this.pager);
         this.typeTabs.setShouldExpand(false);
         this.typeTabs.setIndicatorHeight(0);
         this.typeTabs.setUnderlineHeight(0);
         this.typeTabs.setTabPaddingLeftRight(AndroidUtilities.dp(10.0F));
         this.bottomTabContainer.addView(this.typeTabs, LayoutHelper.createFrame(-2, 44, 81));
         this.typeTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int var1) {
            }

            public void onPageScrolled(int var1, float var2, int var3) {
               EmojiView var4 = EmojiView.this;
               var4.onPageScrolled(var1, var4.getMeasuredWidth() - EmojiView.this.getPaddingLeft() - EmojiView.this.getPaddingRight(), var3);
               EmojiView.this.showBottomTab(true, true);
               var1 = EmojiView.this.pager.getCurrentItem();
               EmojiView.SearchField var7;
               if (var1 == 0) {
                  var7 = EmojiView.this.emojiSearchField;
               } else if (var1 == 1) {
                  var7 = EmojiView.this.gifSearchField;
               } else {
                  var7 = EmojiView.this.stickersSearchField;
               }

               String var5 = var7.searchEditText.getText().toString();

               for(var1 = 0; var1 < 3; ++var1) {
                  EmojiView.SearchField var6;
                  if (var1 == 0) {
                     var6 = EmojiView.this.emojiSearchField;
                  } else if (var1 == 1) {
                     var6 = EmojiView.this.gifSearchField;
                  } else {
                     var6 = EmojiView.this.stickersSearchField;
                  }

                  if (var6 != null && var6 != var7 && var6.searchEditText != null && !var6.searchEditText.getText().toString().equals(var5)) {
                     var6.searchEditText.setText(var5);
                     var6.searchEditText.setSelection(var5.length());
                  }
               }

            }

            public void onPageSelected(int var1) {
               EmojiView.this.saveNewPage();
               EmojiView var2 = EmojiView.this;
               boolean var3 = false;
               boolean var4;
               if (var1 == 0) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               var2.showBackspaceButton(var4, true);
               var2 = EmojiView.this;
               var4 = var3;
               if (var1 == 2) {
                  var4 = true;
               }

               var2.showStickerSettingsButton(var4, true);
               if (EmojiView.this.delegate.isSearchOpened()) {
                  if (var1 == 0) {
                     if (EmojiView.this.emojiSearchField != null) {
                        EmojiView.this.emojiSearchField.searchEditText.requestFocus();
                     }
                  } else if (var1 == 1) {
                     if (EmojiView.this.gifSearchField != null) {
                        EmojiView.this.gifSearchField.searchEditText.requestFocus();
                     }
                  } else if (EmojiView.this.stickersSearchField != null) {
                     EmojiView.this.stickersSearchField.searchEditText.requestFocus();
                  }
               }

            }
         });
         this.searchButton = new ImageView(var3);
         this.searchButton.setImageResource(2131165850);
         this.searchButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), Mode.MULTIPLY));
         this.searchButton.setScaleType(ScaleType.CENTER);
         this.searchButton.setContentDescription(LocaleController.getString("Search", 2131560640));
         this.searchButton.setFocusable(true);
         this.bottomTabContainer.addView(this.searchButton, LayoutHelper.createFrame(52, 44, 83));
         this.searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View var1) {
               int var2 = EmojiView.this.pager.getCurrentItem();
               EmojiView.SearchField var4;
               if (var2 == 0) {
                  var4 = EmojiView.this.emojiSearchField;
               } else if (var2 == 1) {
                  var4 = EmojiView.this.gifSearchField;
               } else {
                  var4 = EmojiView.this.stickersSearchField;
               }

               if (var4 != null) {
                  var4.searchEditText.requestFocus();
                  MotionEvent var3 = MotionEvent.obtain(0L, 0L, 0, 0.0F, 0.0F, 0);
                  var4.searchEditText.onTouchEvent(var3);
                  var3.recycle();
                  var3 = MotionEvent.obtain(0L, 0L, 1, 0.0F, 0.0F, 0);
                  var4.searchEditText.onTouchEvent(var3);
                  var3.recycle();
               }
            }
         });
      } else {
         FrameLayout var25 = this.bottomTabContainer;
         if (VERSION.SDK_INT >= 21) {
            var15 = 40;
         } else {
            var15 = 44;
         }

         byte var16;
         if (VERSION.SDK_INT >= 21) {
            var16 = 40;
         } else {
            var16 = 44;
         }

         var17 = (float)(var16 + 12);
         var16 = var12;
         if (LocaleController.isRTL) {
            var16 = 3;
         }

         this.addView(var25, LayoutHelper.createFrame(var15 + 20, var17, var16 | 80, 0.0F, 0.0F, 2.0F, 0.0F));
         Object var26 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("chat_emojiPanelBackground"), Theme.getColor("chat_emojiPanelBackground"));
         if (VERSION.SDK_INT < 21) {
            var11 = var3.getResources().getDrawable(2131165387).mutate();
            var11.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
            var26 = new CombinedDrawable(var11, (Drawable)var26, 0, 0);
            ((CombinedDrawable)var26).setIconSize(AndroidUtilities.dp(40.0F), AndroidUtilities.dp(40.0F));
         } else {
            StateListAnimator var33 = new StateListAnimator();
            ObjectAnimator var27 = ObjectAnimator.ofFloat(this.floatingButton, View.TRANSLATION_Z, new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
            var33.addState(new int[]{16842919}, var27);
            var27 = ObjectAnimator.ofFloat(this.floatingButton, View.TRANSLATION_Z, new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
            var33.addState(new int[0], var27);
            this.backspaceButton.setStateListAnimator(var33);
            this.backspaceButton.setOutlineProvider(new ViewOutlineProvider() {
               @SuppressLint({"NewApi"})
               public void getOutline(View var1, Outline var2) {
                  var2.setOval(0, 0, AndroidUtilities.dp(40.0F), AndroidUtilities.dp(40.0F));
               }
            });
         }

         this.backspaceButton.setPadding(0, 0, AndroidUtilities.dp(2.0F), 0);
         this.backspaceButton.setBackgroundDrawable((Drawable)var26);
         this.backspaceButton.setContentDescription(LocaleController.getString("AccDescrBackspace", 2131558414));
         this.backspaceButton.setFocusable(true);
         FrameLayout var34 = this.bottomTabContainer;
         ImageView var28 = this.backspaceButton;
         if (VERSION.SDK_INT >= 21) {
            var15 = 40;
         } else {
            var15 = 44;
         }

         var16 = var14;
         if (VERSION.SDK_INT >= 21) {
            var16 = 40;
         }

         var34.addView(var28, LayoutHelper.createFrame(var15, (float)var16, 51, 10.0F, 0.0F, 10.0F, 0.0F));
         this.shadowLine.setVisibility(8);
         this.bottomTabContainerBackground.setVisibility(8);
      }

      this.addView(this.pager, 0, LayoutHelper.createFrame(-1, -1, 51));
      this.mediaBanTooltip = new CorrectlyMeasuringTextView(var3);
      this.mediaBanTooltip.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0F), Theme.getColor("chat_gifSaveHintBackground")));
      this.mediaBanTooltip.setTextColor(Theme.getColor("chat_gifSaveHintText"));
      this.mediaBanTooltip.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(7.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(7.0F));
      this.mediaBanTooltip.setGravity(16);
      this.mediaBanTooltip.setTextSize(1, 14.0F);
      this.mediaBanTooltip.setVisibility(4);
      this.addView(this.mediaBanTooltip, LayoutHelper.createFrame(-2, -2.0F, 81, 5.0F, 0.0F, 5.0F, 53.0F));
      if (AndroidUtilities.isTablet()) {
         var17 = 40.0F;
      } else {
         var17 = 32.0F;
      }

      this.emojiSize = AndroidUtilities.dp(var17);
      this.pickerView = new EmojiView.EmojiColorPickerView(var3);
      EmojiView.EmojiColorPickerView var18 = this.pickerView;
      if (AndroidUtilities.isTablet()) {
         var15 = var13;
      } else {
         var15 = 32;
      }

      int var36 = AndroidUtilities.dp((float)(var15 * 6 + 10 + 20));
      this.popupWidth = var36;
      if (AndroidUtilities.isTablet()) {
         var17 = 64.0F;
      } else {
         var17 = 56.0F;
      }

      int var35 = AndroidUtilities.dp(var17);
      this.popupHeight = var35;
      this.pickerViewPopup = new EmojiView.EmojiPopupWindow(var18, var36, var35);
      this.pickerViewPopup.setOutsideTouchable(true);
      this.pickerViewPopup.setClippingEnabled(true);
      this.pickerViewPopup.setInputMethodMode(2);
      this.pickerViewPopup.setSoftInputMode(0);
      this.pickerViewPopup.getContentView().setFocusableInTouchMode(true);
      this.pickerViewPopup.getContentView().setOnKeyListener(new _$$Lambda$EmojiView$YBM1z0ObV6f6L9y0M_Yz0CakSrc(this));
      this.currentPage = MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0);
      Emoji.loadRecentEmoji();
      this.emojiAdapter.notifyDataSetChanged();
      if (this.typeTabs != null) {
         if (this.views.size() == 1 && this.typeTabs.getVisibility() == 0) {
            this.typeTabs.setVisibility(4);
         } else if (this.views.size() != 1 && this.typeTabs.getVisibility() != 0) {
            this.typeTabs.setVisibility(0);
         }
      }

   }

   private static String addColorToCode(String var0, String var1) {
      int var2 = var0.length();
      String var3;
      String var4;
      if (var2 > 2 && var0.charAt(var0.length() - 2) == 8205) {
         var3 = var0.substring(var0.length() - 2);
         var4 = var0.substring(0, var0.length() - 2);
         var0 = var3;
      } else if (var2 > 3 && var0.charAt(var0.length() - 3) == 8205) {
         var3 = var0.substring(var0.length() - 3);
         var4 = var0.substring(0, var0.length() - 3);
         var0 = var3;
      } else {
         var3 = null;
         var4 = var0;
         var0 = var3;
      }

      StringBuilder var6 = new StringBuilder();
      var6.append(var4);
      var6.append(var1);
      var4 = var6.toString();
      var1 = var4;
      if (var0 != null) {
         StringBuilder var5 = new StringBuilder();
         var5.append(var4);
         var5.append(var0);
         var1 = var5.toString();
      }

      return var1;
   }

   private void checkBottomTabScroll(float var1) {
      this.lastBottomScrollDy += var1;
      int var2;
      if (this.pager.getCurrentItem() == 0) {
         var2 = AndroidUtilities.dp(38.0F);
      } else {
         var2 = AndroidUtilities.dp(48.0F);
      }

      var1 = this.lastBottomScrollDy;
      if (var1 >= (float)var2) {
         this.showBottomTab(false, true);
      } else if (var1 <= (float)(-var2)) {
         this.showBottomTab(true, true);
      } else if (this.bottomTabContainer.getTag() == null && this.lastBottomScrollDy < 0.0F || this.bottomTabContainer.getTag() != null && this.lastBottomScrollDy > 0.0F) {
         this.lastBottomScrollDy = 0.0F;
      }

   }

   private void checkDocuments(boolean var1) {
      if (var1) {
         this.recentGifs = DataQuery.getInstance(this.currentAccount).getRecentGifs();
         EmojiView.GifAdapter var2 = this.gifAdapter;
         if (var2 != null) {
            var2.notifyDataSetChanged();
         }
      } else {
         int var3 = this.recentStickers.size();
         int var4 = this.favouriteStickers.size();
         this.recentStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(0);
         this.favouriteStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(2);

         for(int var5 = 0; var5 < this.favouriteStickers.size(); ++var5) {
            TLRPC.Document var8 = (TLRPC.Document)this.favouriteStickers.get(var5);

            for(int var6 = 0; var6 < this.recentStickers.size(); ++var6) {
               TLRPC.Document var7 = (TLRPC.Document)this.recentStickers.get(var6);
               if (var7.dc_id == var8.dc_id && var7.id == var8.id) {
                  this.recentStickers.remove(var6);
                  break;
               }
            }
         }

         if (var3 != this.recentStickers.size() || var4 != this.favouriteStickers.size()) {
            this.updateStickerTabs();
         }

         EmojiView.StickersGridAdapter var9 = this.stickersGridAdapter;
         if (var9 != null) {
            var9.notifyDataSetChanged();
         }

         this.checkPanels();
      }

   }

   private void checkEmojiSearchFieldScroll(boolean var1) {
      EmojiView.EmojiViewDelegate var2 = this.delegate;
      boolean var3 = false;
      RecyclerView.ViewHolder var6;
      if (var2 != null && var2.isSearchOpened()) {
         var6 = this.emojiGridView.findViewHolderForAdapterPosition(0);
         if (var6 == null) {
            this.emojiSearchField.showShadow(true, var1 ^ true);
         } else {
            EmojiView.SearchField var4 = this.emojiSearchField;
            if (var6.itemView.getTop() < this.emojiGridView.getPaddingTop()) {
               var3 = true;
            } else {
               var3 = false;
            }

            var4.showShadow(var3, var1 ^ true);
         }

         this.showEmojiShadow(false, var1 ^ true);
      } else {
         if (this.emojiSearchField != null) {
            RecyclerListView var5 = this.emojiGridView;
            if (var5 != null) {
               var6 = var5.findViewHolderForAdapterPosition(0);
               if (var6 != null) {
                  this.emojiSearchField.setTranslationY((float)var6.itemView.getTop());
               } else {
                  this.emojiSearchField.setTranslationY((float)(-this.searchFieldHeight));
               }

               this.emojiSearchField.showShadow(false, var1 ^ true);
               if (var6 == null || (float)var6.itemView.getTop() < (float)(AndroidUtilities.dp(38.0F) - this.searchFieldHeight) + this.emojiTabs.getTranslationY()) {
                  var3 = true;
               }

               this.showEmojiShadow(var3, var1 ^ true);
            }
         }

      }
   }

   private void checkEmojiTabY(View var1, int var2) {
      if (var1 == null) {
         ScrollSlidingTabStrip var8 = this.emojiTabs;
         this.emojiMinusDy = 0;
         var8.setTranslationY((float)0);
         this.emojiTabsShadow.setTranslationY((float)this.emojiMinusDy);
      } else if (var1.getVisibility() == 0) {
         EmojiView.EmojiViewDelegate var5 = this.delegate;
         if (var5 == null || !var5.isSearchOpened()) {
            if (var2 > 0) {
               RecyclerListView var6 = this.emojiGridView;
               if (var6 != null && var6.getVisibility() == 0) {
                  RecyclerView.ViewHolder var7 = this.emojiGridView.findViewHolderForAdapterPosition(0);
                  if (var7 != null) {
                     int var3 = var7.itemView.getTop();
                     int var4;
                     if (this.needEmojiSearch) {
                        var4 = this.searchFieldHeight;
                     } else {
                        var4 = 0;
                     }

                     if (var3 + var4 >= this.emojiGridView.getPaddingTop()) {
                        return;
                     }
                  }
               }
            }

            this.emojiMinusDy -= var2;
            var2 = this.emojiMinusDy;
            if (var2 > 0) {
               this.emojiMinusDy = 0;
            } else if (var2 < -AndroidUtilities.dp(288.0F)) {
               this.emojiMinusDy = -AndroidUtilities.dp(288.0F);
            }

            this.emojiTabs.setTranslationY((float)Math.max(-AndroidUtilities.dp(38.0F), this.emojiMinusDy));
            this.emojiTabsShadow.setTranslationY(this.emojiTabs.getTranslationY());
         }
      }
   }

   private void checkGifSearchFieldScroll(boolean var1) {
      RecyclerListView var2 = this.gifGridView;
      if (var2 != null) {
         RecyclerView.Adapter var6 = var2.getAdapter();
         EmojiView.GifSearchAdapter var3 = this.gifSearchAdapter;
         if (var6 == var3 && !var3.searchEndReached && this.gifSearchAdapter.reqId == 0 && !this.gifSearchAdapter.results.isEmpty()) {
            int var4 = this.gifLayoutManager.findLastVisibleItemPosition();
            if (var4 != -1 && var4 > this.gifLayoutManager.getItemCount() - 5) {
               EmojiView.GifSearchAdapter var7 = this.gifSearchAdapter;
               var7.search(var7.lastSearchImageString, this.gifSearchAdapter.nextSearchOffset, true);
            }
         }
      }

      EmojiView.EmojiViewDelegate var8 = this.delegate;
      boolean var5 = false;
      if (var8 != null && var8.isSearchOpened()) {
         RecyclerView.ViewHolder var9 = this.gifGridView.findViewHolderForAdapterPosition(0);
         if (var9 == null) {
            this.gifSearchField.showShadow(true, var1 ^ true);
         } else {
            EmojiView.SearchField var11 = this.gifSearchField;
            if (var9.itemView.getTop() < this.gifGridView.getPaddingTop()) {
               var5 = true;
            }

            var11.showShadow(var5, var1 ^ true);
         }

      } else {
         if (this.gifSearchField != null) {
            var2 = this.gifGridView;
            if (var2 != null) {
               RecyclerView.ViewHolder var10 = var2.findViewHolderForAdapterPosition(0);
               if (var10 != null) {
                  this.gifSearchField.setTranslationY((float)var10.itemView.getTop());
               } else {
                  this.gifSearchField.setTranslationY((float)(-this.searchFieldHeight));
               }

               this.gifSearchField.showShadow(false, var1 ^ true);
            }
         }

      }
   }

   private void checkPanels() {
      if (this.stickersTab != null) {
         RecyclerListView var1;
         if (this.trendingTabNum == -2) {
            var1 = this.trendingGridView;
            if (var1 != null && var1.getVisibility() == 0) {
               this.trendingGridView.setVisibility(8);
               this.stickersGridView.setVisibility(0);
               this.stickersSearchField.setVisibility(0);
            }
         }

         var1 = this.trendingGridView;
         int var2;
         int var3;
         if (var1 != null && var1.getVisibility() == 0) {
            ScrollSlidingTabStrip var4 = this.stickersTab;
            var2 = this.trendingTabNum;
            var3 = this.recentTabBum;
            if (var3 <= 0) {
               var3 = this.stickersTabOffset;
            }

            var4.onPageScrolled(var2, var3);
         } else {
            var2 = this.stickersLayoutManager.findFirstVisibleItemPosition();
            if (var2 != -1) {
               var3 = this.favTabBum;
               if (var3 <= 0) {
                  var3 = this.recentTabBum;
                  if (var3 <= 0) {
                     var3 = this.stickersTabOffset;
                  }
               }

               this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(var2), var3);
            }
         }

      }
   }

   private void checkScroll() {
      int var1 = this.stickersLayoutManager.findFirstVisibleItemPosition();
      if (var1 != -1) {
         if (this.stickersGridView != null) {
            int var2 = this.favTabBum;
            if (var2 <= 0) {
               var2 = this.recentTabBum;
               if (var2 <= 0) {
                  var2 = this.stickersTabOffset;
               }
            }

            this.stickersTab.onPageScrolled(this.stickersGridAdapter.getTabForPosition(var1), var2);
         }
      }
   }

   private void checkStickersSearchFieldScroll(boolean var1) {
      EmojiView.EmojiViewDelegate var2 = this.delegate;
      boolean var3 = false;
      if (var2 != null && var2.isSearchOpened()) {
         RecyclerView.ViewHolder var4 = this.stickersGridView.findViewHolderForAdapterPosition(0);
         if (var4 == null) {
            this.stickersSearchField.showShadow(true, var1 ^ true);
         } else {
            EmojiView.SearchField var7 = this.stickersSearchField;
            if (var4.itemView.getTop() < this.stickersGridView.getPaddingTop()) {
               var3 = true;
            }

            var7.showShadow(var3, var1 ^ true);
         }

      } else {
         if (this.stickersSearchField != null) {
            RecyclerListView var5 = this.stickersGridView;
            if (var5 != null) {
               RecyclerView.ViewHolder var6 = var5.findViewHolderForAdapterPosition(0);
               if (var6 != null) {
                  this.stickersSearchField.setTranslationY((float)var6.itemView.getTop());
               } else {
                  this.stickersSearchField.setTranslationY((float)(-this.searchFieldHeight));
               }

               this.stickersSearchField.showShadow(false, var1 ^ true);
            }
         }

      }
   }

   private void checkStickersTabY(View var1, int var2) {
      if (var1 == null) {
         ScrollSlidingTabStrip var6 = this.stickersTab;
         this.stickersMinusDy = 0;
         var6.setTranslationY((float)0);
      } else if (var1.getVisibility() == 0) {
         EmojiView.EmojiViewDelegate var3 = this.delegate;
         if (var3 == null || !var3.isSearchOpened()) {
            if (var2 > 0) {
               RecyclerListView var4 = this.stickersGridView;
               if (var4 != null && var4.getVisibility() == 0) {
                  RecyclerView.ViewHolder var5 = this.stickersGridView.findViewHolderForAdapterPosition(0);
                  if (var5 != null && var5.itemView.getTop() + this.searchFieldHeight >= this.stickersGridView.getPaddingTop()) {
                     return;
                  }
               }
            }

            this.stickersMinusDy -= var2;
            var2 = this.stickersMinusDy;
            if (var2 > 0) {
               this.stickersMinusDy = 0;
            } else if (var2 < -AndroidUtilities.dp(288.0F)) {
               this.stickersMinusDy = -AndroidUtilities.dp(288.0F);
            }

            this.stickersTab.setTranslationY((float)Math.max(-AndroidUtilities.dp(48.0F), this.stickersMinusDy));
         }
      }
   }

   // $FF: synthetic method
   static void lambda$static$0() {
   }

   private void onPageScrolled(int var1, int var2, int var3) {
      EmojiView.EmojiViewDelegate var4 = this.delegate;
      if (var4 != null) {
         byte var6 = 0;
         if (var1 == 1) {
            byte var5 = var6;
            if (var3 != 0) {
               var5 = 2;
            }

            var4.onTabOpened(var5);
         } else if (var1 == 2) {
            var4.onTabOpened(3);
         } else {
            var4.onTabOpened(0);
         }

      }
   }

   private void openSearch(EmojiView.SearchField var1) {
      AnimatorSet var2 = this.searchAnimation;
      if (var2 != null) {
         var2.cancel();
         this.searchAnimation = null;
      }

      this.firstStickersAttach = false;
      this.firstGifAttach = false;
      this.firstEmojiAttach = false;

      for(int var3 = 0; var3 < 3; ++var3) {
         final RecyclerListView var4;
         ScrollSlidingTabStrip var5;
         Object var6;
         EmojiView.SearchField var8;
         if (var3 == 0) {
            var8 = this.emojiSearchField;
            var4 = this.emojiGridView;
            var5 = this.emojiTabs;
            var6 = this.emojiLayoutManager;
         } else if (var3 == 1) {
            var8 = this.gifSearchField;
            var4 = this.gifGridView;
            var6 = this.gifLayoutManager;
            var5 = null;
         } else {
            var8 = this.stickersSearchField;
            var4 = this.stickersGridView;
            var5 = this.stickersTab;
            var6 = this.stickersLayoutManager;
         }

         if (var8 != null) {
            if (var8 != this.gifSearchField && var1 == var8) {
               EmojiView.EmojiViewDelegate var7 = this.delegate;
               if (var7 != null && var7.isExpanded()) {
                  this.searchAnimation = new AnimatorSet();
                  if (var5 != null) {
                     this.searchAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(var5, View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(48.0F))}), ObjectAnimator.ofFloat(var4, View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(48.0F))}), ObjectAnimator.ofFloat(var8, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(0.0F)})});
                  } else {
                     this.searchAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(var4, View.TRANSLATION_Y, new float[]{(float)(-AndroidUtilities.dp(48.0F))}), ObjectAnimator.ofFloat(var8, View.TRANSLATION_Y, new float[]{(float)AndroidUtilities.dp(0.0F)})});
                  }

                  this.searchAnimation.setDuration(200L);
                  this.searchAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                  this.searchAnimation.addListener(new AnimatorListenerAdapter() {
                     public void onAnimationCancel(Animator var1) {
                        if (var1.equals(EmojiView.this.searchAnimation)) {
                           EmojiView.this.searchAnimation = null;
                        }

                     }

                     public void onAnimationEnd(Animator var1) {
                        if (var1.equals(EmojiView.this.searchAnimation)) {
                           var4.setTranslationY(0.0F);
                           if (var4 == EmojiView.this.stickersGridView) {
                              var4.setPadding(0, AndroidUtilities.dp(4.0F), 0, 0);
                           } else if (var4 == EmojiView.this.emojiGridView) {
                              var4.setPadding(0, 0, 0, 0);
                           }

                           EmojiView.this.searchAnimation = null;
                        }

                     }
                  });
                  this.searchAnimation.start();
                  continue;
               }
            }

            var8.setTranslationY((float)AndroidUtilities.dp(0.0F));
            if (var5 != null) {
               var5.setTranslationY((float)(-AndroidUtilities.dp(48.0F)));
            }

            if (var4 == this.stickersGridView) {
               var4.setPadding(0, AndroidUtilities.dp(4.0F), 0, 0);
            } else if (var4 == this.emojiGridView) {
               var4.setPadding(0, 0, 0, 0);
            }

            ((LinearLayoutManager)var6).scrollToPositionWithOffset(0, 0);
         }
      }

   }

   private void postBackspaceRunnable(int var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$Jrw9DaGNjc5_52tMJtvct_h0u6A(this, var1), (long)var1);
   }

   private void reloadStickersAdapter() {
      EmojiView.StickersGridAdapter var1 = this.stickersGridAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      EmojiView.TrendingGridAdapter var2 = this.trendingGridAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

      EmojiView.StickersSearchGridAdapter var3 = this.stickersSearchGridAdapter;
      if (var3 != null) {
         var3.notifyDataSetChanged();
      }

      if (ContentPreviewViewer.getInstance().isVisible()) {
         ContentPreviewViewer.getInstance().close();
      }

      ContentPreviewViewer.getInstance().reset();
   }

   private void saveNewPage() {
      ViewPager var1 = this.pager;
      if (var1 != null) {
         int var2 = var1.getCurrentItem();
         byte var3 = 1;
         if (var2 != 2) {
            if (var2 == 1) {
               var3 = 2;
            } else {
               var3 = 0;
            }
         }

         if (this.currentPage != var3) {
            this.currentPage = var3;
            MessagesController.getGlobalEmojiSettings().edit().putInt("selected_page", var3).commit();
         }

      }
   }

   private void showBackspaceButton(final boolean var1, boolean var2) {
      if ((!var1 || this.backspaceButton.getTag() != null) && (var1 || this.backspaceButton.getTag() == null)) {
         AnimatorSet var3 = this.backspaceButtonAnimation;
         Integer var4 = null;
         if (var3 != null) {
            var3.cancel();
            this.backspaceButtonAnimation = null;
         }

         ImageView var11 = this.backspaceButton;
         if (!var1) {
            var4 = 1;
         }

         var11.setTag(var4);
         byte var5 = 0;
         float var6 = 1.0F;
         float var8;
         if (var2) {
            if (var1) {
               this.backspaceButton.setVisibility(0);
            }

            this.backspaceButtonAnimation = new AnimatorSet();
            AnimatorSet var13 = this.backspaceButtonAnimation;
            var11 = this.backspaceButton;
            Property var7 = View.ALPHA;
            if (var1) {
               var8 = 1.0F;
            } else {
               var8 = 0.0F;
            }

            ObjectAnimator var12 = ObjectAnimator.ofFloat(var11, var7, new float[]{var8});
            ImageView var9 = this.backspaceButton;
            var7 = View.SCALE_X;
            if (var1) {
               var8 = 1.0F;
            } else {
               var8 = 0.0F;
            }

            ObjectAnimator var16 = ObjectAnimator.ofFloat(var9, var7, new float[]{var8});
            ImageView var15 = this.backspaceButton;
            Property var10 = View.SCALE_Y;
            if (!var1) {
               var6 = 0.0F;
            }

            var13.playTogether(new Animator[]{var12, var16, ObjectAnimator.ofFloat(var15, var10, new float[]{var6})});
            this.backspaceButtonAnimation.setDuration(200L);
            this.backspaceButtonAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.backspaceButtonAnimation.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  if (!var1) {
                     EmojiView.this.backspaceButton.setVisibility(4);
                  }

               }
            });
            this.backspaceButtonAnimation.start();
         } else {
            ImageView var14 = this.backspaceButton;
            if (var1) {
               var8 = 1.0F;
            } else {
               var8 = 0.0F;
            }

            var14.setAlpha(var8);
            var14 = this.backspaceButton;
            if (var1) {
               var8 = 1.0F;
            } else {
               var8 = 0.0F;
            }

            var14.setScaleX(var8);
            var14 = this.backspaceButton;
            if (!var1) {
               var6 = 0.0F;
            }

            var14.setScaleY(var6);
            var14 = this.backspaceButton;
            if (!var1) {
               var5 = 4;
            }

            var14.setVisibility(var5);
         }

      }
   }

   private void showBottomTab(boolean var1, boolean var2) {
      float var3 = 0.0F;
      float var4 = 0.0F;
      this.lastBottomScrollDy = 0.0F;
      if ((!var1 || this.bottomTabContainer.getTag() != null) && (var1 || this.bottomTabContainer.getTag() == null)) {
         EmojiView.EmojiViewDelegate var5 = this.delegate;
         if (var5 == null || !var5.isSearchOpened()) {
            AnimatorSet var6 = this.bottomTabContainerAnimation;
            Integer var10 = null;
            if (var6 != null) {
               var6.cancel();
               this.bottomTabContainerAnimation = null;
            }

            FrameLayout var11 = this.bottomTabContainer;
            if (!var1) {
               var10 = 1;
            }

            var11.setTag(var10);
            float var7 = 54.0F;
            if (var2) {
               this.bottomTabContainerAnimation = new AnimatorSet();
               AnimatorSet var12 = this.bottomTabContainerAnimation;
               var11 = this.bottomTabContainer;
               Property var8 = View.TRANSLATION_Y;
               if (var1) {
                  var7 = 0.0F;
               } else {
                  if (this.needEmojiSearch) {
                     var7 = 49.0F;
                  }

                  var7 = (float)AndroidUtilities.dp(var7);
               }

               ObjectAnimator var15 = ObjectAnimator.ofFloat(var11, var8, new float[]{var7});
               View var16 = this.shadowLine;
               Property var9 = View.TRANSLATION_Y;
               if (var1) {
                  var7 = var4;
               } else {
                  var7 = (float)AndroidUtilities.dp(49.0F);
               }

               var12.playTogether(new Animator[]{var15, ObjectAnimator.ofFloat(var16, var9, new float[]{var7})});
               this.bottomTabContainerAnimation.setDuration(200L);
               this.bottomTabContainerAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
               this.bottomTabContainerAnimation.start();
            } else {
               FrameLayout var13 = this.bottomTabContainer;
               if (var1) {
                  var7 = 0.0F;
               } else {
                  if (this.needEmojiSearch) {
                     var7 = 49.0F;
                  }

                  var7 = (float)AndroidUtilities.dp(var7);
               }

               var13.setTranslationY(var7);
               View var14 = this.shadowLine;
               if (var1) {
                  var7 = var3;
               } else {
                  var7 = (float)AndroidUtilities.dp(49.0F);
               }

               var14.setTranslationY(var7);
            }

            return;
         }
      }

   }

   private void showEmojiShadow(boolean var1, boolean var2) {
      if ((!var1 || this.emojiTabsShadow.getTag() != null) && (var1 || this.emojiTabsShadow.getTag() == null)) {
         AnimatorSet var3 = this.emojiTabShadowAnimator;
         Integer var4 = null;
         if (var3 != null) {
            var3.cancel();
            this.emojiTabShadowAnimator = null;
         }

         View var7 = this.emojiTabsShadow;
         if (!var1) {
            var4 = 1;
         }

         var7.setTag(var4);
         float var5 = 1.0F;
         View var8;
         if (var2) {
            this.emojiTabShadowAnimator = new AnimatorSet();
            var3 = this.emojiTabShadowAnimator;
            var8 = this.emojiTabsShadow;
            Property var6 = View.ALPHA;
            if (!var1) {
               var5 = 0.0F;
            }

            var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(var8, var6, new float[]{var5})});
            this.emojiTabShadowAnimator.setDuration(200L);
            this.emojiTabShadowAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.emojiTabShadowAnimator.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  EmojiView.this.emojiTabShadowAnimator = null;
               }
            });
            this.emojiTabShadowAnimator.start();
         } else {
            var8 = this.emojiTabsShadow;
            if (!var1) {
               var5 = 0.0F;
            }

            var8.setAlpha(var5);
         }

      }
   }

   private void showStickerSettingsButton(final boolean var1, boolean var2) {
      ImageView var3 = this.stickerSettingsButton;
      if (var3 != null) {
         if ((!var1 || var3.getTag() != null) && (var1 || this.stickerSettingsButton.getTag() == null)) {
            AnimatorSet var4 = this.stickersButtonAnimation;
            Integer var11 = null;
            if (var4 != null) {
               var4.cancel();
               this.stickersButtonAnimation = null;
            }

            ImageView var12 = this.stickerSettingsButton;
            if (!var1) {
               var11 = 1;
            }

            var12.setTag(var11);
            byte var5 = 0;
            float var6 = 1.0F;
            float var8;
            if (var2) {
               if (var1) {
                  this.stickerSettingsButton.setVisibility(0);
               }

               this.stickersButtonAnimation = new AnimatorSet();
               AnimatorSet var13 = this.stickersButtonAnimation;
               var12 = this.stickerSettingsButton;
               Property var7 = View.ALPHA;
               if (var1) {
                  var8 = 1.0F;
               } else {
                  var8 = 0.0F;
               }

               ObjectAnimator var14 = ObjectAnimator.ofFloat(var12, var7, new float[]{var8});
               ImageView var15 = this.stickerSettingsButton;
               Property var9 = View.SCALE_X;
               if (var1) {
                  var8 = 1.0F;
               } else {
                  var8 = 0.0F;
               }

               ObjectAnimator var10 = ObjectAnimator.ofFloat(var15, var9, new float[]{var8});
               var15 = this.stickerSettingsButton;
               var9 = View.SCALE_Y;
               if (!var1) {
                  var6 = 0.0F;
               }

               var13.playTogether(new Animator[]{var14, var10, ObjectAnimator.ofFloat(var15, var9, new float[]{var6})});
               this.stickersButtonAnimation.setDuration(200L);
               this.stickersButtonAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT);
               this.stickersButtonAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1x) {
                     if (!var1) {
                        EmojiView.this.stickerSettingsButton.setVisibility(4);
                     }

                  }
               });
               this.stickersButtonAnimation.start();
            } else {
               var3 = this.stickerSettingsButton;
               if (var1) {
                  var8 = 1.0F;
               } else {
                  var8 = 0.0F;
               }

               var3.setAlpha(var8);
               var3 = this.stickerSettingsButton;
               if (var1) {
                  var8 = 1.0F;
               } else {
                  var8 = 0.0F;
               }

               var3.setScaleX(var8);
               var3 = this.stickerSettingsButton;
               if (!var1) {
                  var6 = 0.0F;
               }

               var3.setScaleY(var6);
               var3 = this.stickerSettingsButton;
               if (!var1) {
                  var5 = 4;
               }

               var3.setVisibility(var5);
            }

         }
      }
   }

   private void showTrendingTab(boolean var1) {
      if (var1) {
         this.trendingGridView.setVisibility(0);
         this.stickersGridView.setVisibility(8);
         this.stickersSearchField.setVisibility(8);
         ScrollSlidingTabStrip var2 = this.stickersTab;
         int var3 = this.trendingTabNum;
         int var4 = this.recentTabBum;
         if (var4 <= 0) {
            var4 = this.stickersTabOffset;
         }

         var2.onPageScrolled(var3, var4);
         this.saveNewPage();
      } else {
         this.trendingGridView.setVisibility(8);
         this.stickersGridView.setVisibility(0);
         this.stickersSearchField.setVisibility(0);
      }

   }

   private void startStopVisibleGifs(boolean var1) {
      RecyclerListView var2 = this.gifGridView;
      if (var2 != null) {
         int var3 = var2.getChildCount();

         for(int var4 = 0; var4 < var3; ++var4) {
            View var5 = this.gifGridView.getChildAt(var4);
            if (var5 instanceof ContextLinkCell) {
               ImageReceiver var6 = ((ContextLinkCell)var5).getPhotoImage();
               if (var1) {
                  var6.setAllowStartAnimation(true);
                  var6.startAnimation();
               } else {
                  var6.setAllowStartAnimation(false);
                  var6.stopAnimation();
               }
            }
         }

      }
   }

   private void updateEmojiTabs() {
      int var1 = Emoji.recentEmoji.isEmpty() ^ 1;
      int var2 = this.hasRecentEmoji;
      if (var2 == -1 || var2 != var1) {
         this.hasRecentEmoji = var1;
         this.emojiTabs.removeTabs();
         String var3 = LocaleController.getString("RecentStickers", 2131560538);
         var2 = 0;
         String var4 = LocaleController.getString("Emoji1", 2131559332);
         String var5 = LocaleController.getString("Emoji2", 2131559333);
         String var6 = LocaleController.getString("Emoji3", 2131559334);
         String var7 = LocaleController.getString("Emoji4", 2131559335);
         String var8 = LocaleController.getString("Emoji5", 2131559336);
         String var9 = LocaleController.getString("Emoji6", 2131559337);
         String var10 = LocaleController.getString("Emoji7", 2131559338);

         for(String var11 = LocaleController.getString("Emoji8", 2131559339); var2 < this.emojiIcons.length; ++var2) {
            if (var2 != 0 || !Emoji.recentEmoji.isEmpty()) {
               this.emojiTabs.addIconTab(this.emojiIcons[var2]).setContentDescription((new String[]{var3, var4, var5, var6, var7, var8, var9, var10, var11})[var2]);
            }
         }

         this.emojiTabs.updateTabStyles();
      }
   }

   private void updateStickerTabs() {
      ScrollSlidingTabStrip var1 = this.stickersTab;
      if (var1 != null) {
         this.recentTabBum = -2;
         this.favTabBum = -2;
         this.trendingTabNum = -2;
         this.stickersTabOffset = 0;
         int var2 = var1.getCurrentPosition();
         this.stickersTab.removeTabs();
         ArrayList var3 = DataQuery.getInstance(this.currentAccount).getUnreadStickerSets();
         EmojiView.TrendingGridAdapter var14 = this.trendingGridAdapter;
         byte var4 = 2;
         int var5;
         if (var14 != null && var14.getItemCount() != 0 && !var3.isEmpty()) {
            this.stickersCounter = this.stickersTab.addIconTabWithCounter(this.stickerIcons[2]);
            var5 = this.stickersTabOffset;
            this.trendingTabNum = var5;
            this.stickersTabOffset = var5 + 1;
            this.stickersCounter.setText(String.format("%d", var3.size()));
         }

         boolean var19;
         if (!this.favouriteStickers.isEmpty()) {
            var5 = this.stickersTabOffset;
            this.favTabBum = var5;
            this.stickersTabOffset = var5 + 1;
            this.stickersTab.addIconTab(this.stickerIcons[1]).setContentDescription(LocaleController.getString("FavoriteStickers", 2131559478));
            var19 = true;
         } else {
            var19 = false;
         }

         if (!this.recentStickers.isEmpty()) {
            var5 = this.stickersTabOffset;
            this.recentTabBum = var5;
            this.stickersTabOffset = var5 + 1;
            this.stickersTab.addIconTab(this.stickerIcons[0]).setContentDescription(LocaleController.getString("RecentStickers", 2131560538));
            var19 = true;
         }

         this.stickerSets.clear();
         TLRPC.TL_messages_stickerSet var15 = null;
         this.groupStickerSet = null;
         this.groupStickerPackPosition = -1;
         this.groupStickerPackNum = -10;
         ArrayList var6 = DataQuery.getInstance(this.currentAccount).getStickerSets(0);

         TLRPC.TL_messages_stickerSet var8;
         boolean var9;
         for(int var7 = 0; var7 < var6.size(); var19 = var9) {
            var8 = (TLRPC.TL_messages_stickerSet)var6.get(var7);
            var9 = var19;
            if (!var8.set.archived) {
               ArrayList var10 = var8.documents;
               var9 = var19;
               if (var10 != null) {
                  if (var10.isEmpty()) {
                     var9 = var19;
                  } else {
                     this.stickerSets.add(var8);
                     var9 = true;
                  }
               }
            }

            ++var7;
         }

         TLRPC.TL_messages_stickerSet var29;
         if (this.info != null) {
            SharedPreferences var21 = MessagesController.getEmojiSettings(this.currentAccount);
            StringBuilder var27 = new StringBuilder();
            var27.append("group_hide_stickers_");
            var27.append(this.info.id);
            long var11 = var21.getLong(var27.toString(), -1L);
            TLRPC.Chat var22 = MessagesController.getInstance(this.currentAccount).getChat(this.info.id);
            boolean var13;
            if (var22 != null && this.info.stickerset != null && ChatObject.hasAdminRights(var22)) {
               TLRPC.StickerSet var23 = this.info.stickerset;
               if (var23 != null) {
                  if (var11 == var23.id) {
                     var13 = true;
                  } else {
                     var13 = false;
                  }

                  this.groupStickersHidden = var13;
               }
            } else {
               if (var11 != -1L) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               this.groupStickersHidden = var13;
            }

            TLRPC.ChatFull var24 = this.info;
            if (var24.stickerset != null) {
               var29 = DataQuery.getInstance(this.currentAccount).getGroupStickerSetById(this.info.stickerset);
               if (var29 != null) {
                  ArrayList var26 = var29.documents;
                  if (var26 != null && !var26.isEmpty() && var29.set != null) {
                     var8 = new TLRPC.TL_messages_stickerSet();
                     var8.documents = var29.documents;
                     var8.packs = var29.packs;
                     var8.set = var29.set;
                     if (this.groupStickersHidden) {
                        this.groupStickerPackNum = this.stickerSets.size();
                        this.stickerSets.add(var8);
                     } else {
                        this.groupStickerPackNum = 0;
                        this.stickerSets.add(0, var8);
                     }

                     if (this.info.can_set_stickers) {
                        var15 = var8;
                     }

                     this.groupStickerSet = var15;
                  }
               }
            } else if (var24.can_set_stickers) {
               var15 = new TLRPC.TL_messages_stickerSet();
               if (this.groupStickersHidden) {
                  this.groupStickerPackNum = this.stickerSets.size();
                  this.stickerSets.add(var15);
               } else {
                  this.groupStickerPackNum = 0;
                  this.stickerSets.add(0, var15);
               }
            }
         }

         byte var20 = 0;
         var9 = var19;

         for(var5 = var20; var5 < this.stickerSets.size(); ++var5) {
            if (var5 == this.groupStickerPackNum) {
               TLRPC.Chat var16 = MessagesController.getInstance(this.currentAccount).getChat(this.info.id);
               if (var16 == null) {
                  this.stickerSets.remove(0);
                  --var5;
                  continue;
               }

               this.stickersTab.addStickerTab(var16);
            } else {
               var29 = (TLRPC.TL_messages_stickerSet)this.stickerSets.get(var5);
               TLRPC.Document var28 = (TLRPC.Document)var29.documents.get(0);
               Object var17 = var29.set.thumb;
               if (!(var17 instanceof TLRPC.TL_photoSize)) {
                  var17 = var28;
               }

               View var30 = this.stickersTab.addStickerTab((TLObject)var17, var28, var29);
               StringBuilder var18 = new StringBuilder();
               var18.append(var29.set.title);
               var18.append(", ");
               var18.append(LocaleController.getString("AccDescrStickerSet", 2131558475));
               var30.setContentDescription(var18.toString());
            }

            var9 = true;
         }

         var14 = this.trendingGridAdapter;
         if (var14 != null && var14.getItemCount() != 0 && var3.isEmpty()) {
            this.trendingTabNum = this.stickersTabOffset + this.stickerSets.size();
            this.stickersTab.addIconTab(this.stickerIcons[2]).setContentDescription(LocaleController.getString("FeaturedStickers", 2131559479));
         }

         this.stickersTab.updateTabStyles();
         if (var2 != 0) {
            this.stickersTab.onPageScrolled(var2, var2);
         }

         this.checkPanels();
         if ((!var9 || this.trendingTabNum == 0 && DataQuery.getInstance(this.currentAccount).areAllTrendingStickerSetsUnread()) && this.trendingTabNum >= 0) {
            if (this.scrolledToTrending == 0) {
               this.showTrendingTab(true);
               byte var25;
               if (var9) {
                  var25 = var4;
               } else {
                  var25 = 1;
               }

               this.scrolledToTrending = var25;
            }
         } else if (this.scrolledToTrending == 1) {
            this.showTrendingTab(false);
            this.checkScroll();
            this.stickersTab.cancelPositionAnimation();
         }

      }
   }

   private void updateVisibleTrendingSets() {
      EmojiView.TrendingGridAdapter var1 = this.trendingGridAdapter;
      if (var1 != null && var1 != null) {
         label160:
         for(int var2 = 0; var2 < 2; ++var2) {
            Exception var10000;
            label166: {
               boolean var10001;
               RecyclerListView var27;
               if (var2 == 0) {
                  try {
                     var27 = this.trendingGridView;
                  } catch (Exception var26) {
                     var10000 = var26;
                     var10001 = false;
                     break label166;
                  }
               } else {
                  try {
                     var27 = this.stickersGridView;
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label166;
                  }
               }

               int var3;
               try {
                  var3 = var27.getChildCount();
               } catch (Exception var24) {
                  var10000 = var24;
                  var10001 = false;
                  break label166;
               }

               int var4 = 0;

               while(true) {
                  if (var4 >= var3) {
                     continue label160;
                  }

                  label168: {
                     View var5;
                     try {
                        var5 = var27.getChildAt(var4);
                        if (!(var5 instanceof FeaturedStickerSetInfoCell) || (RecyclerListView.Holder)var27.getChildViewHolder(var5) == null) {
                           break label168;
                        }
                     } catch (Exception var23) {
                        var10000 = var23;
                        var10001 = false;
                        break;
                     }

                     FeaturedStickerSetInfoCell var6;
                     ArrayList var7;
                     TLRPC.StickerSetCovered var29;
                     try {
                        var6 = (FeaturedStickerSetInfoCell)var5;
                        var7 = DataQuery.getInstance(this.currentAccount).getUnreadStickerSets();
                        var29 = var6.getStickerSet();
                     } catch (Exception var17) {
                        var10000 = var17;
                        var10001 = false;
                        break;
                     }

                     boolean var8;
                     boolean var9;
                     label135: {
                        label134: {
                           var8 = true;
                           if (var7 != null) {
                              try {
                                 if (var7.contains(var29.set.id)) {
                                    break label134;
                                 }
                              } catch (Exception var22) {
                                 var10000 = var22;
                                 var10001 = false;
                                 break;
                              }
                           }

                           var9 = false;
                           break label135;
                        }

                        var9 = true;
                     }

                     try {
                        var6.setStickerSet(var29, var9);
                     } catch (Exception var16) {
                        var10000 = var16;
                        var10001 = false;
                        break;
                     }

                     if (var9) {
                        try {
                           DataQuery.getInstance(this.currentAccount).markFaturedStickersByIdAsRead(var29.set.id);
                        } catch (Exception var15) {
                           var10000 = var15;
                           var10001 = false;
                           break;
                        }
                     }

                     boolean var10;
                     label126: {
                        label125: {
                           try {
                              if (this.installingStickerSets.indexOfKey(var29.set.id) >= 0) {
                                 break label125;
                              }
                           } catch (Exception var21) {
                              var10000 = var21;
                              var10001 = false;
                              break;
                           }

                           var10 = false;
                           break label126;
                        }

                        var10 = true;
                     }

                     boolean var11;
                     label119: {
                        label118: {
                           try {
                              if (this.removingStickerSets.indexOfKey(var29.set.id) >= 0) {
                                 break label118;
                              }
                           } catch (Exception var20) {
                              var10000 = var20;
                              var10001 = false;
                              break;
                           }

                           var11 = false;
                           break label119;
                        }

                        var11 = true;
                     }

                     boolean var12;
                     boolean var13;
                     label169: {
                        if (!var10) {
                           var12 = var10;
                           var13 = var11;
                           if (!var11) {
                              break label169;
                           }
                        }

                        label170: {
                           if (var10) {
                              try {
                                 if (var6.isInstalled()) {
                                    this.installingStickerSets.remove(var29.set.id);
                                    break label170;
                                 }
                              } catch (Exception var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 break;
                              }
                           }

                           var12 = var10;
                           var13 = var11;
                           if (!var11) {
                              break label169;
                           }

                           var12 = var10;
                           var13 = var11;

                           try {
                              if (var6.isInstalled()) {
                                 break label169;
                              }

                              this.removingStickerSets.remove(var29.set.id);
                           } catch (Exception var18) {
                              var10000 = var18;
                              var10001 = false;
                              break;
                           }

                           var13 = false;
                           var12 = var10;
                           break label169;
                        }

                        var12 = false;
                        var13 = var11;
                     }

                     var9 = var8;
                     if (!var12) {
                        if (var13) {
                           var9 = var8;
                        } else {
                           var9 = false;
                        }
                     }

                     try {
                        var6.setDrawProgress(var9);
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                        break;
                     }
                  }

                  ++var4;
               }
            }

            Exception var28 = var10000;
            FileLog.e((Throwable)var28);
            break;
         }
      }

   }

   public void addEmojiToRecent(String var1) {
      if (Emoji.isValidEmoji(var1)) {
         Emoji.recentEmoji.size();
         Emoji.addRecentEmoji(var1);
         if (this.getVisibility() != 0 || this.pager.getCurrentItem() != 0) {
            Emoji.sortEmoji();
            this.emojiAdapter.notifyDataSetChanged();
         }

         Emoji.saveRecentEmoji();
      }
   }

   public void addRecentGif(TLRPC.Document var1) {
      if (var1 != null) {
         boolean var2 = this.recentGifs.isEmpty();
         this.recentGifs = DataQuery.getInstance(this.currentAccount).getRecentGifs();
         EmojiView.GifAdapter var3 = this.gifAdapter;
         if (var3 != null) {
            var3.notifyDataSetChanged();
         }

         if (var2) {
            this.updateStickerTabs();
         }

      }
   }

   public void addRecentSticker(TLRPC.Document var1) {
      if (var1 != null) {
         DataQuery.getInstance(this.currentAccount).addRecentSticker(0, (Object)null, var1, (int)(System.currentTimeMillis() / 1000L), false);
         boolean var2 = this.recentStickers.isEmpty();
         this.recentStickers = DataQuery.getInstance(this.currentAccount).getRecentStickers(0);
         EmojiView.StickersGridAdapter var3 = this.stickersGridAdapter;
         if (var3 != null) {
            var3.notifyDataSetChanged();
         }

         if (var2) {
            this.updateStickerTabs();
         }

      }
   }

   public boolean areThereAnyStickers() {
      EmojiView.StickersGridAdapter var1 = this.stickersGridAdapter;
      boolean var2;
      if (var1 != null && var1.getItemCount() > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void clearRecentEmoji() {
      Emoji.clearRecentEmoji();
      this.emojiAdapter.notifyDataSetChanged();
   }

   public void closeSearch(boolean var1) {
      this.closeSearch(var1, -1L);
   }

   public void closeSearch(boolean var1, long var2) {
      AnimatorSet var4 = this.searchAnimation;
      if (var4 != null) {
         var4.cancel();
         this.searchAnimation = null;
      }

      int var5 = this.pager.getCurrentItem();
      byte var6 = 2;
      int var7;
      if (var5 == 2 && var2 != -1L) {
         TLRPC.TL_messages_stickerSet var14 = DataQuery.getInstance(this.currentAccount).getStickerSetById(var2);
         if (var14 != null) {
            var7 = this.stickersGridAdapter.getPositionForPack(var14);
            if (var7 >= 0) {
               this.stickersLayoutManager.scrollToPositionWithOffset(var7, AndroidUtilities.dp(60.0F));
            }
         }
      }

      byte var11;
      for(var7 = 0; var7 < 3; var6 = var11) {
         EmojiView.SearchField var8;
         final Object var9;
         ScrollSlidingTabStrip var10;
         final RecyclerListView var15;
         if (var7 == 0) {
            var8 = this.emojiSearchField;
            var15 = this.emojiGridView;
            var9 = this.emojiLayoutManager;
            var10 = this.emojiTabs;
         } else if (var7 == 1) {
            var8 = this.gifSearchField;
            var15 = this.gifGridView;
            var9 = this.gifLayoutManager;
            var10 = null;
         } else {
            var8 = this.stickersSearchField;
            var15 = this.stickersGridView;
            var9 = this.stickersLayoutManager;
            var10 = this.stickersTab;
         }

         if (var8 == null) {
            var11 = var6;
         } else {
            var8.searchEditText.setText("");
            if (var7 == var5 && var1) {
               this.searchAnimation = new AnimatorSet();
               AnimatorSet var12;
               if (var10 != null) {
                  var12 = this.searchAnimation;
                  ObjectAnimator var17 = ObjectAnimator.ofFloat(var10, View.TRANSLATION_Y, new float[]{0.0F});
                  ObjectAnimator var13 = ObjectAnimator.ofFloat(var15, View.TRANSLATION_Y, new float[]{(float)(AndroidUtilities.dp(48.0F) - this.searchFieldHeight)});
                  ObjectAnimator var16 = ObjectAnimator.ofFloat(var8, View.TRANSLATION_Y, new float[]{(float)(AndroidUtilities.dp(48.0F) - this.searchFieldHeight)});
                  var6 = 2;
                  var12.playTogether(new Animator[]{var17, var13, var16});
               } else {
                  var12 = this.searchAnimation;
                  Animator[] var18 = new Animator[var6];
                  var18[0] = ObjectAnimator.ofFloat(var15, View.TRANSLATION_Y, new float[]{(float)(-this.searchFieldHeight)});
                  var18[1] = ObjectAnimator.ofFloat(var8, View.TRANSLATION_Y, new float[]{(float)(-this.searchFieldHeight)});
                  var12.playTogether(var18);
               }

               this.searchAnimation.setDuration(200L);
               this.searchAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
               this.searchAnimation.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationCancel(Animator var1) {
                     if (var1.equals(EmojiView.this.searchAnimation)) {
                        EmojiView.this.searchAnimation = null;
                     }

                  }

                  public void onAnimationEnd(Animator var1) {
                     if (var1.equals(EmojiView.this.searchAnimation)) {
                        ((LinearLayoutManager)var9).findFirstVisibleItemPosition();
                        int var2 = ((LinearLayoutManager)var9).findFirstVisibleItemPosition();
                        int var3;
                        if (var2 != -1) {
                           var3 = (int)((float)((LinearLayoutManager)var9).findViewByPosition(var2).getTop() + var15.getTranslationY());
                        } else {
                           var3 = 0;
                        }

                        var15.setTranslationY(0.0F);
                        if (var15 == EmojiView.this.stickersGridView) {
                           var15.setPadding(0, AndroidUtilities.dp(52.0F), 0, 0);
                        } else if (var15 == EmojiView.this.emojiGridView) {
                           var15.setPadding(0, AndroidUtilities.dp(38.0F), 0, 0);
                        }

                        if (var15 == EmojiView.this.gifGridView) {
                           ((LinearLayoutManager)var9).scrollToPositionWithOffset(1, 0);
                        } else if (var2 != -1) {
                           ((LinearLayoutManager)var9).scrollToPositionWithOffset(var2, var3 - var15.getPaddingTop());
                        }

                        EmojiView.this.searchAnimation = null;
                     }

                  }
               });
               this.searchAnimation.start();
               var11 = var6;
            } else {
               ((LinearLayoutManager)var9).scrollToPositionWithOffset(1, 0);
               var8.setTranslationY((float)(AndroidUtilities.dp(48.0F) - this.searchFieldHeight));
               if (var10 != null) {
                  var10.setTranslationY(0.0F);
               }

               if (var15 == this.stickersGridView) {
                  var15.setPadding(0, AndroidUtilities.dp(52.0F), 0, 0);
                  var11 = var6;
               } else {
                  var11 = var6;
                  if (var15 == this.emojiGridView) {
                     var15.setPadding(0, AndroidUtilities.dp(38.0F), 0, 0);
                     var11 = var6;
                  }
               }
            }
         }

         ++var7;
      }

      if (!var1) {
         this.delegate.onSearchOpenClose(0);
      }

      this.showBottomTab(true, var1);
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.stickersDidLoad;
      byte var5 = 0;
      byte var8 = 0;
      if (var1 == var4) {
         if ((Integer)var3[0] == 0) {
            EmojiView.TrendingGridAdapter var9 = this.trendingGridAdapter;
            if (var9 != null) {
               if (this.trendingLoaded) {
                  this.updateVisibleTrendingSets();
               } else {
                  var9.notifyDataSetChanged();
               }
            }

            this.updateStickerTabs();
            this.reloadStickersAdapter();
            this.checkPanels();
         }
      } else if (var1 == NotificationCenter.recentDocumentsDidLoad) {
         boolean var6 = (Boolean)var3[0];
         var1 = (Integer)var3[1];
         if (var6 || var1 == 0 || var1 == 2) {
            this.checkDocuments(var6);
         }
      } else if (var1 == NotificationCenter.featuredStickersDidLoad) {
         if (this.trendingGridAdapter != null) {
            if (this.featuredStickersHash != DataQuery.getInstance(this.currentAccount).getFeaturesStickersHashWithoutUnread()) {
               this.trendingLoaded = false;
            }

            if (this.trendingLoaded) {
               this.updateVisibleTrendingSets();
            } else {
               this.trendingGridAdapter.notifyDataSetChanged();
            }
         }

         PagerSlidingTabStrip var10 = this.typeTabs;
         if (var10 != null) {
            int var14 = var10.getChildCount();

            for(var1 = var8; var1 < var14; ++var1) {
               this.typeTabs.getChildAt(var1).invalidate();
            }
         }

         this.updateStickerTabs();
      } else if (var1 == NotificationCenter.groupStickersDidLoad) {
         TLRPC.ChatFull var7 = this.info;
         if (var7 != null) {
            TLRPC.StickerSet var15 = var7.stickerset;
            if (var15 != null && var15.id == (Long)var3[0]) {
               this.updateStickerTabs();
            }
         }
      } else if (var1 == NotificationCenter.emojiDidLoad) {
         RecyclerListView var11 = this.stickersGridView;
         if (var11 != null) {
            var2 = var11.getChildCount();

            for(var1 = var5; var1 < var2; ++var1) {
               View var12 = this.stickersGridView.getChildAt(var1);
               if (var12 instanceof StickerSetNameCell || var12 instanceof StickerEmojiCell) {
                  var12.invalidate();
               }
            }
         }
      } else if (var1 == NotificationCenter.newEmojiSuggestionsAvailable && this.emojiGridView != null && this.needEmojiSearch && (this.emojiSearchField.progressDrawable.isAnimating() || this.emojiGridView.getAdapter() == this.emojiSearchAdapter) && !TextUtils.isEmpty(this.emojiSearchAdapter.lastSearchEmojiString)) {
         EmojiView.EmojiSearchAdapter var13 = this.emojiSearchAdapter;
         var13.search(var13.lastSearchEmojiString);
      }

   }

   public int getCurrentPage() {
      return this.currentPage;
   }

   public void hideSearchKeyboard() {
      EmojiView.SearchField var1 = this.stickersSearchField;
      if (var1 != null) {
         var1.hideKeyboard();
      }

      var1 = this.gifSearchField;
      if (var1 != null) {
         var1.hideKeyboard();
      }

      var1 = this.emojiSearchField;
      if (var1 != null) {
         var1.hideKeyboard();
      }

   }

   public void invalidateViews() {
      this.emojiGridView.invalidateViews();
   }

   // $FF: synthetic method
   public boolean lambda$new$1$EmojiView(View var1, MotionEvent var2) {
      return ContentPreviewViewer.getInstance().onTouch(var2, this.gifGridView, 0, this.gifOnItemClickListener, this.contentPreviewViewerDelegate);
   }

   // $FF: synthetic method
   public void lambda$new$2$EmojiView(View var1, int var2) {
      if (this.delegate != null) {
         --var2;
         if (this.gifGridView.getAdapter() == this.gifAdapter) {
            if (var2 < 0 || var2 >= this.recentGifs.size()) {
               return;
            }

            this.delegate.onGifSelected(this.recentGifs.get(var2), "gif");
         } else {
            RecyclerView.Adapter var3 = this.gifGridView.getAdapter();
            EmojiView.GifSearchAdapter var4 = this.gifSearchAdapter;
            if (var3 == var4 && var2 >= 0 && var2 < var4.results.size()) {
               this.delegate.onGifSelected(this.gifSearchAdapter.results.get(var2), this.gifSearchAdapter.bot);
               this.recentGifs = DataQuery.getInstance(this.currentAccount).getRecentGifs();
               EmojiView.GifAdapter var5 = this.gifAdapter;
               if (var5 != null) {
                  var5.notifyDataSetChanged();
               }
            }
         }

      }
   }

   // $FF: synthetic method
   public boolean lambda$new$3$EmojiView(View var1, MotionEvent var2) {
      return ContentPreviewViewer.getInstance().onTouch(var2, this.stickersGridView, this.getMeasuredHeight(), this.stickersOnItemClickListener, this.contentPreviewViewerDelegate);
   }

   // $FF: synthetic method
   public void lambda$new$4$EmojiView(View var1, int var2) {
      RecyclerView.Adapter var3 = this.stickersGridView.getAdapter();
      EmojiView.StickersSearchGridAdapter var4 = this.stickersSearchGridAdapter;
      if (var3 == var4) {
         TLRPC.StickerSetCovered var6 = (TLRPC.StickerSetCovered)var4.positionsToSets.get(var2);
         if (var6 != null) {
            this.delegate.onShowStickerSet(var6.set, (TLRPC.InputStickerSet)null);
            return;
         }
      }

      if (var1 instanceof StickerEmojiCell) {
         ContentPreviewViewer.getInstance().reset();
         StickerEmojiCell var5 = (StickerEmojiCell)var1;
         if (!var5.isDisabled()) {
            var5.disable();
            this.delegate.onStickerSelected(var5.getSticker(), var5.getParentObject());
         }
      }
   }

   // $FF: synthetic method
   public void lambda$new$5$EmojiView(View var1, int var2) {
      TLRPC.StickerSetCovered var3 = (TLRPC.StickerSetCovered)this.trendingGridAdapter.positionsToSets.get(var2);
      if (var3 != null) {
         this.delegate.onShowStickerSet(var3.set, (TLRPC.InputStickerSet)null);
      }

   }

   // $FF: synthetic method
   public void lambda$new$6$EmojiView(int var1) {
      if (var1 == this.trendingTabNum) {
         if (this.trendingGridView.getVisibility() != 0) {
            this.showTrendingTab(true);
         }
      } else if (this.trendingGridView.getVisibility() == 0) {
         this.showTrendingTab(false);
         this.saveNewPage();
      }

      if (var1 != this.trendingTabNum) {
         ScrollSlidingTabStrip var2;
         int var3;
         if (var1 == this.recentTabBum) {
            this.stickersGridView.stopScroll();
            this.stickersLayoutManager.scrollToPositionWithOffset(this.stickersGridAdapter.getPositionForPack("recent"), 0);
            this.checkStickersTabY((View)null, 0);
            var2 = this.stickersTab;
            var3 = this.recentTabBum;
            if (var3 > 0) {
               var1 = var3;
            } else {
               var1 = this.stickersTabOffset;
            }

            var2.onPageScrolled(var3, var1);
         } else if (var1 == this.favTabBum) {
            this.stickersGridView.stopScroll();
            this.stickersLayoutManager.scrollToPositionWithOffset(this.stickersGridAdapter.getPositionForPack("fav"), 0);
            this.checkStickersTabY((View)null, 0);
            var2 = this.stickersTab;
            var3 = this.favTabBum;
            if (var3 > 0) {
               var1 = var3;
            } else {
               var1 = this.stickersTabOffset;
            }

            var2.onPageScrolled(var3, var1);
         } else {
            var3 = var1 - this.stickersTabOffset;
            if (var3 < this.stickerSets.size()) {
               var1 = var3;
               if (var3 >= this.stickerSets.size()) {
                  var1 = this.stickerSets.size() - 1;
               }

               this.firstStickersAttach = false;
               this.stickersGridView.stopScroll();
               this.stickersLayoutManager.scrollToPositionWithOffset(this.stickersGridAdapter.getPositionForPack(this.stickerSets.get(var1)), 0);
               this.checkStickersTabY((View)null, 0);
               this.checkScroll();
            }
         }
      }
   }

   // $FF: synthetic method
   public boolean lambda$new$7$EmojiView(View var1, int var2, KeyEvent var3) {
      if (var2 == 82 && var3.getRepeatCount() == 0 && var3.getAction() == 1) {
         EmojiView.EmojiPopupWindow var4 = this.pickerViewPopup;
         if (var4 != null && var4.isShowing()) {
            this.pickerViewPopup.dismiss();
            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$onAttachedToWindow$9$EmojiView() {
      this.updateStickerTabs();
      this.reloadStickersAdapter();
   }

   // $FF: synthetic method
   public void lambda$postBackspaceRunnable$8$EmojiView(int var1) {
      if (this.backspacePressed) {
         EmojiView.EmojiViewDelegate var2 = this.delegate;
         if (var2 != null && var2.onBackspace()) {
            this.backspaceButton.performHapticFeedback(3);
         }

         this.backspaceOnce = true;
         this.postBackspaceRunnable(Math.max(50, var1 - 100));
      }
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
      if (this.stickersGridAdapter != null) {
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoad);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoad);
         AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$nydI8RFLQx0Boo3VLUW9Gt2FzYg(this));
      }

   }

   public void onDestroy() {
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
      if (this.stickersGridAdapter != null) {
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentDocumentsDidLoad);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoad);
      }

   }

   protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      EmojiView.EmojiPopupWindow var1 = this.pickerViewPopup;
      if (var1 != null && var1.isShowing()) {
         this.pickerViewPopup.dismiss();
      }

   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = this.lastNotifyWidth;
      int var7 = var4 - var2;
      if (var6 != var7) {
         this.lastNotifyWidth = var7;
         this.reloadStickersAdapter();
      }

      View var8 = (View)this.getParent();
      if (var8 != null) {
         var6 = var5 - var3;
         var7 = var8.getHeight();
         if (this.lastNotifyHeight != var6 || this.lastNotifyHeight2 != var7) {
            EmojiView.EmojiViewDelegate var9 = this.delegate;
            if (var9 != null && var9.isSearchOpened()) {
               this.bottomTabContainer.setTranslationY((float)AndroidUtilities.dp(49.0F));
            } else if (this.bottomTabContainer.getTag() == null) {
               if (var6 < this.lastNotifyHeight) {
                  this.bottomTabContainer.setTranslationY(0.0F);
               } else {
                  float var10 = this.getY();
                  float var11 = (float)this.getMeasuredHeight();
                  float var12 = (float)var8.getHeight();
                  this.bottomTabContainer.setTranslationY(-(var10 + var11 - var12));
               }
            }

            this.lastNotifyHeight = var6;
            this.lastNotifyHeight2 = var7;
         }
      }

      super.onLayout(var1, var2, var3, var4, var5);
   }

   public void onMeasure(int var1, int var2) {
      this.isLayout = true;
      if (!AndroidUtilities.isInMultiwindow && !this.forseMultiwindowLayout) {
         if (this.currentBackgroundType != 0) {
            if (VERSION.SDK_INT >= 21) {
               this.setOutlineProvider((ViewOutlineProvider)null);
               this.setClipToOutline(false);
               this.setElevation(0.0F);
            }

            this.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
            if (this.needEmojiSearch) {
               this.bottomTabContainerBackground.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
            }

            this.currentBackgroundType = 0;
         }
      } else if (this.currentBackgroundType != 1) {
         if (VERSION.SDK_INT >= 21) {
            this.setOutlineProvider((ViewOutlineProvider)this.outlineProvider);
            this.setClipToOutline(true);
            this.setElevation((float)AndroidUtilities.dp(2.0F));
         }

         this.setBackgroundResource(2131165847);
         this.getBackground().setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackground"), Mode.MULTIPLY));
         if (this.needEmojiSearch) {
            this.bottomTabContainerBackground.setBackgroundDrawable((Drawable)null);
         }

         this.currentBackgroundType = 1;
      }

      super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var2), 1073741824));
      this.isLayout = false;
   }

   public void onOpen(boolean var1) {
      if (this.currentPage != 0 && this.currentChatId != 0) {
         this.currentPage = 0;
      }

      if (this.currentPage != 0 && !var1 && this.views.size() != 1) {
         int var2 = this.currentPage;
         if (var2 == 1) {
            this.showBackspaceButton(false, false);
            this.showStickerSettingsButton(true, false);
            if (this.pager.getCurrentItem() != 2) {
               this.pager.setCurrentItem(2, false);
            }

            if (this.stickersTab != null) {
               if (this.trendingTabNum == 0 && DataQuery.getInstance(this.currentAccount).areAllTrendingStickerSetsUnread()) {
                  this.showTrendingTab(true);
               } else {
                  var2 = this.recentTabBum;
                  if (var2 >= 0) {
                     this.stickersTab.selectTab(var2);
                  } else {
                     var2 = this.favTabBum;
                     if (var2 >= 0) {
                        this.stickersTab.selectTab(var2);
                     } else {
                        this.stickersTab.selectTab(this.stickersTabOffset);
                     }
                  }
               }
            }
         } else if (var2 == 2) {
            this.showBackspaceButton(false, false);
            this.showStickerSettingsButton(false, false);
            if (this.pager.getCurrentItem() != 1) {
               this.pager.setCurrentItem(1, false);
            }
         }
      } else {
         this.showBackspaceButton(true, false);
         this.showStickerSettingsButton(false, false);
         if (this.pager.getCurrentItem() != 0) {
            this.pager.setCurrentItem(0, var1 ^ true);
         }
      }

   }

   public void requestLayout() {
      if (!this.isLayout) {
         super.requestLayout();
      }
   }

   public void setChatInfo(TLRPC.ChatFull var1) {
      this.info = var1;
      this.updateStickerTabs();
   }

   public void setDelegate(EmojiView.EmojiViewDelegate var1) {
      this.delegate = var1;
   }

   public void setDragListener(EmojiView.DragListener var1) {
      this.dragListener = var1;
   }

   public void setForseMultiwindowLayout(boolean var1) {
      this.forseMultiwindowLayout = var1;
   }

   public void setStickersBanned(boolean var1, int var2) {
      if (this.typeTabs != null) {
         if (var1) {
            this.currentChatId = var2;
         } else {
            this.currentChatId = 0;
         }

         View var3 = this.typeTabs.getTab(2);
         if (var3 != null) {
            float var4;
            if (this.currentChatId != 0) {
               var4 = 0.5F;
            } else {
               var4 = 1.0F;
            }

            var3.setAlpha(var4);
            if (this.currentChatId != 0 && this.pager.getCurrentItem() != 0) {
               this.showBackspaceButton(true, true);
               this.showStickerSettingsButton(false, true);
               this.pager.setCurrentItem(0, false);
            }
         }

      }
   }

   public void setTranslationY(float var1) {
      super.setTranslationY(var1);
      if (this.bottomTabContainer.getTag() == null) {
         EmojiView.EmojiViewDelegate var2 = this.delegate;
         if (var2 == null || !var2.isSearchOpened()) {
            View var5 = (View)this.getParent();
            if (var5 != null) {
               float var3 = this.getY();
               float var4 = (float)this.getMeasuredHeight();
               var1 = (float)var5.getHeight();
               this.bottomTabContainer.setTranslationY(-(var3 + var4 - var1));
            }
         }
      }

   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      if (var1 != 8) {
         Emoji.sortEmoji();
         this.emojiAdapter.notifyDataSetChanged();
         if (this.stickersGridAdapter != null) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentDocumentsDidLoad);
            this.updateStickerTabs();
            this.reloadStickersAdapter();
         }

         EmojiView.TrendingGridAdapter var2 = this.trendingGridAdapter;
         if (var2 != null) {
            this.trendingLoaded = false;
            var2.notifyDataSetChanged();
         }

         this.checkDocuments(true);
         this.checkDocuments(false);
         DataQuery.getInstance(this.currentAccount).loadRecents(0, true, true, false);
         DataQuery.getInstance(this.currentAccount).loadRecents(0, false, true, false);
         DataQuery.getInstance(this.currentAccount).loadRecents(2, false, true, false);
      }

   }

   public void showSearchField(boolean var1) {
      for(int var2 = 0; var2 < 3; ++var2) {
         Object var3;
         ScrollSlidingTabStrip var4;
         if (var2 == 0) {
            var3 = this.emojiLayoutManager;
            var4 = this.emojiTabs;
         } else if (var2 == 1) {
            var3 = this.gifLayoutManager;
            var4 = null;
         } else {
            var3 = this.stickersLayoutManager;
            var4 = this.stickersTab;
         }

         if (var3 != null) {
            int var5 = ((LinearLayoutManager)var3).findFirstVisibleItemPosition();
            if (var1) {
               if (var5 == 1 || var5 == 2) {
                  ((LinearLayoutManager)var3).scrollToPosition(0);
                  if (var4 != null) {
                     var4.setTranslationY(0.0F);
                  }
               }
            } else if (var5 == 0) {
               ((LinearLayoutManager)var3).scrollToPositionWithOffset(1, 0);
            }
         }
      }

   }

   public void showStickerBanHint(boolean var1) {
      if (this.mediaBanTooltip.getVisibility() != 0) {
         TLRPC.Chat var2 = MessagesController.getInstance(this.currentAccount).getChat(this.currentChatId);
         if (var2 != null) {
            label41: {
               TLRPC.TL_chatBannedRights var3;
               if (!ChatObject.hasAdminRights(var2)) {
                  var3 = var2.default_banned_rights;
                  if (var3 != null && var3.send_stickers) {
                     if (var1) {
                        this.mediaBanTooltip.setText(LocaleController.getString("GlobalAttachGifRestricted", 2131559590));
                     } else {
                        this.mediaBanTooltip.setText(LocaleController.getString("GlobalAttachStickersRestricted", 2131559593));
                     }
                     break label41;
                  }
               }

               var3 = var2.banned_rights;
               if (var3 == null) {
                  return;
               }

               if (AndroidUtilities.isBannedForever(var3)) {
                  if (var1) {
                     this.mediaBanTooltip.setText(LocaleController.getString("AttachGifRestrictedForever", 2131558718));
                  } else {
                     this.mediaBanTooltip.setText(LocaleController.getString("AttachStickersRestrictedForever", 2131558732));
                  }
               } else if (var1) {
                  this.mediaBanTooltip.setText(LocaleController.formatString("AttachGifRestricted", 2131558717, LocaleController.formatDateForBan((long)var2.banned_rights.until_date)));
               } else {
                  this.mediaBanTooltip.setText(LocaleController.formatString("AttachStickersRestricted", 2131558731, LocaleController.formatDateForBan((long)var2.banned_rights.until_date)));
               }
            }

            this.mediaBanTooltip.setVisibility(0);
            AnimatorSet var4 = new AnimatorSet();
            var4.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.mediaBanTooltip, View.ALPHA, new float[]{0.0F, 1.0F})});
            var4.addListener(new AnimatorListenerAdapter() {
               // $FF: synthetic method
               public void lambda$onAnimationEnd$0$EmojiView$34() {
                  if (EmojiView.this.mediaBanTooltip != null) {
                     AnimatorSet var1 = new AnimatorSet();
                     var1.playTogether(new Animator[]{ObjectAnimator.ofFloat(EmojiView.this.mediaBanTooltip, View.ALPHA, new float[]{0.0F})});
                     var1.addListener(new EmojiView$34$1(this));
                     var1.setDuration(300L);
                     var1.start();
                  }
               }

               public void onAnimationEnd(Animator var1) {
                  AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$34$syxP2tKTMRS9T48q548gWNuUVOM(this), 5000L);
               }
            });
            var4.setDuration(300L);
            var4.start();
         }
      }
   }

   public void switchToGifRecent() {
      this.showBackspaceButton(false, false);
      this.showStickerSettingsButton(false, false);
      this.pager.setCurrentItem(1, false);
   }

   public void updateUIColors() {
      if (!AndroidUtilities.isInMultiwindow && !this.forseMultiwindowLayout) {
         this.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
         if (this.needEmojiSearch) {
            this.bottomTabContainerBackground.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
         }
      } else {
         Drawable var1 = this.getBackground();
         if (var1 != null) {
            var1.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackground"), Mode.MULTIPLY));
         }
      }

      ScrollSlidingTabStrip var3 = this.emojiTabs;
      if (var3 != null) {
         var3.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
         this.emojiTabsShadow.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
      }

      int var2;
      for(var2 = 0; var2 < 3; ++var2) {
         EmojiView.SearchField var4;
         if (var2 == 0) {
            var4 = this.stickersSearchField;
         } else if (var2 == 1) {
            var4 = this.emojiSearchField;
         } else {
            var4 = this.gifSearchField;
         }

         if (var4 != null) {
            var4.backgroundView.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
            var4.shadowView.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
            var4.clearSearchImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiSearchIcon"), Mode.MULTIPLY));
            var4.searchIconImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiSearchIcon"), Mode.MULTIPLY));
            Theme.setDrawableColorByKey(var4.searchBackground.getBackground(), "chat_emojiSearchBackground");
            var4.searchBackground.invalidate();
            var4.searchEditText.setHintTextColor(Theme.getColor("chat_emojiSearchIcon"));
            var4.searchEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         }
      }

      Paint var5 = this.dotPaint;
      if (var5 != null) {
         var5.setColor(Theme.getColor("chat_emojiPanelNewTrending"));
      }

      RecyclerListView var6 = this.emojiGridView;
      if (var6 != null) {
         var6.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
      }

      var6 = this.stickersGridView;
      if (var6 != null) {
         var6.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
      }

      var6 = this.trendingGridView;
      if (var6 != null) {
         var6.setGlowColor(Theme.getColor("chat_emojiPanelBackground"));
      }

      var3 = this.stickersTab;
      if (var3 != null) {
         var3.setIndicatorColor(Theme.getColor("chat_emojiPanelStickerPackSelectorLine"));
         this.stickersTab.setUnderlineColor(Theme.getColor("chat_emojiPanelShadowLine"));
         this.stickersTab.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
      }

      ImageView var7 = this.backspaceButton;
      if (var7 != null) {
         var7.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), Mode.MULTIPLY));
      }

      var7 = this.stickerSettingsButton;
      if (var7 != null) {
         var7.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), Mode.MULTIPLY));
      }

      var7 = this.searchButton;
      if (var7 != null) {
         var7.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelBackspace"), Mode.MULTIPLY));
      }

      View var8 = this.shadowLine;
      if (var8 != null) {
         var8.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
      }

      TextView var9 = this.mediaBanTooltip;
      if (var9 != null) {
         ((ShapeDrawable)var9.getBackground()).getPaint().setColor(Theme.getColor("chat_gifSaveHintBackground"));
         this.mediaBanTooltip.setTextColor(Theme.getColor("chat_gifSaveHintText"));
      }

      var9 = this.stickersCounter;
      if (var9 != null) {
         var9.setTextColor(Theme.getColor("chat_emojiPanelBadgeText"));
         Theme.setDrawableColor(this.stickersCounter.getBackground(), Theme.getColor("chat_emojiPanelBadgeBackground"));
         this.stickersCounter.invalidate();
      }

      var2 = 0;

      while(true) {
         Drawable[] var10 = this.tabIcons;
         if (var2 >= var10.length) {
            var2 = 0;

            while(true) {
               var10 = this.emojiIcons;
               if (var2 >= var10.length) {
                  var2 = 0;

                  while(true) {
                     var10 = this.stickerIcons;
                     if (var2 >= var10.length) {
                        return;
                     }

                     Theme.setEmojiDrawableColor(var10[var2], Theme.getColor("chat_emojiPanelIcon"), false);
                     Theme.setEmojiDrawableColor(this.stickerIcons[var2], Theme.getColor("chat_emojiPanelIconSelected"), true);
                     ++var2;
                  }
               }

               Theme.setEmojiDrawableColor(var10[var2], Theme.getColor("chat_emojiPanelIcon"), false);
               Theme.setEmojiDrawableColor(this.emojiIcons[var2], Theme.getColor("chat_emojiPanelIconSelected"), true);
               ++var2;
            }
         }

         Theme.setEmojiDrawableColor(var10[var2], Theme.getColor("chat_emojiBottomPanelIcon"), false);
         Theme.setEmojiDrawableColor(this.tabIcons[var2], Theme.getColor("chat_emojiPanelIconSelected"), true);
         ++var2;
      }
   }

   public interface DragListener {
      void onDrag(int var1);

      void onDragCancel();

      void onDragEnd(float var1);

      void onDragStart();
   }

   private class EmojiColorPickerView extends View {
      private Drawable arrowDrawable = this.getResources().getDrawable(2131165860);
      private int arrowX;
      private Drawable backgroundDrawable = this.getResources().getDrawable(2131165859);
      private String currentEmoji;
      private RectF rect = new RectF();
      private Paint rectPaint = new Paint(1);
      private int selection;

      public EmojiColorPickerView(Context var2) {
         super(var2);
      }

      public String getEmoji() {
         return this.currentEmoji;
      }

      public int getSelection() {
         return this.selection;
      }

      protected void onDraw(Canvas var1) {
         Drawable var2 = this.backgroundDrawable;
         int var3 = this.getMeasuredWidth();
         float var4;
         if (AndroidUtilities.isTablet()) {
            var4 = 60.0F;
         } else {
            var4 = 52.0F;
         }

         int var5 = AndroidUtilities.dp(var4);
         int var6 = 0;
         var2.setBounds(0, 0, var3, var5);
         this.backgroundDrawable.draw(var1);
         var2 = this.arrowDrawable;
         var3 = this.arrowX;
         int var7 = AndroidUtilities.dp(9.0F);
         boolean var8 = AndroidUtilities.isTablet();
         float var9 = 55.5F;
         if (var8) {
            var4 = 55.5F;
         } else {
            var4 = 47.5F;
         }

         int var10 = AndroidUtilities.dp(var4);
         int var11 = this.arrowX;
         var5 = AndroidUtilities.dp(9.0F);
         if (AndroidUtilities.isTablet()) {
            var4 = var9;
         } else {
            var4 = 47.5F;
         }

         var2.setBounds(var3 - var7, var10, var11 + var5, AndroidUtilities.dp(var4 + 8.0F));
         this.arrowDrawable.draw(var1);
         if (this.currentEmoji != null) {
            for(; var6 < 6; ++var6) {
               var3 = EmojiView.this.emojiSize * var6 + AndroidUtilities.dp((float)(var6 * 4 + 5));
               var5 = AndroidUtilities.dp(9.0F);
               if (this.selection == var6) {
                  this.rect.set((float)var3, (float)(var5 - (int)AndroidUtilities.dpf2(3.5F)), (float)(EmojiView.this.emojiSize + var3), (float)(EmojiView.this.emojiSize + var5 + AndroidUtilities.dp(3.0F)));
                  var1.drawRoundRect(this.rect, (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(4.0F), this.rectPaint);
               }

               String var12 = this.currentEmoji;
               String var13 = var12;
               if (var6 != 0) {
                  if (var6 != 1) {
                     if (var6 != 2) {
                        if (var6 != 3) {
                           if (var6 != 4) {
                              if (var6 != 5) {
                                 var13 = "";
                              } else {
                                 var13 = "\ud83c\udfff";
                              }
                           } else {
                              var13 = "\ud83c\udffe";
                           }
                        } else {
                           var13 = "\ud83c\udffd";
                        }
                     } else {
                        var13 = "\ud83c\udffc";
                     }
                  } else {
                     var13 = "\ud83c\udffb";
                  }

                  var13 = EmojiView.addColorToCode(var12, var13);
               }

               var2 = Emoji.getEmojiBigDrawable(var13);
               if (var2 != null) {
                  var2.setBounds(var3, var5, EmojiView.this.emojiSize + var3, EmojiView.this.emojiSize + var5);
                  var2.draw(var1);
               }
            }
         }

      }

      public void setEmoji(String var1, int var2) {
         this.currentEmoji = var1;
         this.arrowX = var2;
         this.rectPaint.setColor(788529152);
         this.invalidate();
      }

      public void setSelection(int var1) {
         if (this.selection != var1) {
            this.selection = var1;
            this.invalidate();
         }
      }
   }

   private class EmojiGridAdapter extends RecyclerListView.SelectionAdapter {
      private int itemCount;
      private SparseIntArray positionToSection;
      private SparseIntArray sectionToPosition;

      private EmojiGridAdapter() {
         this.positionToSection = new SparseIntArray();
         this.sectionToPosition = new SparseIntArray();
      }

      // $FF: synthetic method
      EmojiGridAdapter(Object var2) {
         this();
      }

      public int getItemCount() {
         return this.itemCount;
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         if (EmojiView.this.needEmojiSearch && var1 == 0) {
            return 2;
         } else {
            return this.positionToSection.indexOfKey(var1) >= 0 ? 1 : 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void notifyDataSetChanged() {
         this.positionToSection.clear();
         this.itemCount = Emoji.recentEmoji.size() + EmojiView.this.needEmojiSearch;

         for(int var1 = 0; var1 < EmojiData.dataColored.length; ++var1) {
            this.positionToSection.put(this.itemCount, var1);
            this.sectionToPosition.put(var1, this.itemCount);
            this.itemCount += EmojiData.dataColored[var1].length + 1;
         }

         EmojiView.this.updateEmojiTabs();
         super.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         if (var3 != 0) {
            if (var3 == 1) {
               ((StickerSetNameCell)var1.itemView).setText(EmojiView.this.emojiTitles[this.positionToSection.get(var2)], 0);
            }
         } else {
            EmojiView.ImageViewEmoji var5 = (EmojiView.ImageViewEmoji)var1.itemView;
            var3 = var2;
            if (EmojiView.this.needEmojiSearch) {
               var3 = var2 - 1;
            }

            var2 = Emoji.recentEmoji.size();
            String var6;
            String var11;
            if (var3 < var2) {
               var11 = (String)Emoji.recentEmoji.get(var3);
               var6 = var11;
               var4 = true;
            } else {
               label42: {
                  int var7 = 0;

                  while(true) {
                     String[][] var10 = EmojiData.dataColored;
                     if (var7 >= var10.length) {
                        var11 = null;
                        break;
                     }

                     int var8 = var10[var7].length + 1 + var2;
                     if (var3 < var8) {
                        var6 = var10[var7][var3 - var2 - 1];
                        String var9 = (String)Emoji.emojiColor.get(var6);
                        var11 = var6;
                        if (var9 != null) {
                           var11 = EmojiView.addColorToCode(var6, var9);
                           break label42;
                        }
                        break;
                     }

                     ++var7;
                     var2 = var8;
                  }

                  var6 = var11;
                  var11 = var11;
               }
            }

            var5.setImageDrawable(Emoji.getEmojiBigDrawable(var11), var4);
            var5.setTag(var6);
            var5.setContentDescription(var11);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = new View(EmojiView.this.getContext());
               ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
            } else {
               var3 = new StickerSetNameCell(EmojiView.this.getContext(), true);
            }
         } else {
            EmojiView var4 = EmojiView.this;
            var3 = var4.new ImageViewEmoji(var4.getContext());
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   private class EmojiPagesAdapter extends PagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
      private EmojiPagesAdapter() {
      }

      // $FF: synthetic method
      EmojiPagesAdapter(Object var2) {
         this();
      }

      public boolean canScrollToTab(int var1) {
         boolean var2 = true;
         if ((var1 == 1 || var1 == 2) && EmojiView.this.currentChatId != 0) {
            EmojiView var3 = EmojiView.this;
            if (var1 != 1) {
               var2 = false;
            }

            var3.showStickerBanHint(var2);
            return false;
         } else {
            return true;
         }
      }

      public void customOnDraw(Canvas var1, int var2) {
         if (var2 == 2 && !DataQuery.getInstance(EmojiView.this.currentAccount).getUnreadStickerSets().isEmpty() && EmojiView.this.dotPaint != null) {
            int var3 = var1.getWidth() / 2;
            var2 = AndroidUtilities.dp(9.0F);
            int var4 = var1.getHeight() / 2;
            int var5 = AndroidUtilities.dp(8.0F);
            var1.drawCircle((float)(var3 + var2), (float)(var4 - var5), (float)AndroidUtilities.dp(5.0F), EmojiView.this.dotPaint);
         }

      }

      public void destroyItem(ViewGroup var1, int var2, Object var3) {
         var1.removeView((View)EmojiView.this.views.get(var2));
      }

      public int getCount() {
         return EmojiView.this.views.size();
      }

      public Drawable getPageIconDrawable(int var1) {
         return EmojiView.this.tabIcons[var1];
      }

      public CharSequence getPageTitle(int var1) {
         if (var1 != 0) {
            if (var1 != 1) {
               return var1 != 2 ? null : LocaleController.getString("AccDescrStickers", 2131558476);
            } else {
               return LocaleController.getString("AccDescrGIFs", 2131558434);
            }
         } else {
            return LocaleController.getString("Emoji", 2131559331);
         }
      }

      public Object instantiateItem(ViewGroup var1, int var2) {
         View var3 = (View)EmojiView.this.views.get(var2);
         var1.addView(var3);
         return var3;
      }

      public boolean isViewFromObject(View var1, Object var2) {
         boolean var3;
         if (var1 == var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void unregisterDataSetObserver(DataSetObserver var1) {
         if (var1 != null) {
            super.unregisterDataSetObserver(var1);
         }

      }
   }

   private class EmojiPopupWindow extends PopupWindow {
      private OnScrollChangedListener mSuperScrollListener;
      private ViewTreeObserver mViewTreeObserver;

      public EmojiPopupWindow() {
         this.init();
      }

      public EmojiPopupWindow(int var2, int var3) {
         super(var2, var3);
         this.init();
      }

      public EmojiPopupWindow(Context var2) {
         super(var2);
         this.init();
      }

      public EmojiPopupWindow(View var2) {
         super(var2);
         this.init();
      }

      public EmojiPopupWindow(View var2, int var3, int var4) {
         super(var2, var3, var4);
         this.init();
      }

      public EmojiPopupWindow(View var2, int var3, int var4, boolean var5) {
         super(var2, var3, var4, var5);
         this.init();
      }

      private void init() {
         if (EmojiView.superListenerField != null) {
            try {
               this.mSuperScrollListener = (OnScrollChangedListener)EmojiView.superListenerField.get(this);
               EmojiView.superListenerField.set(this, EmojiView.NOP);
            } catch (Exception var2) {
               this.mSuperScrollListener = null;
            }
         }

      }

      private void registerListener(View var1) {
         if (this.mSuperScrollListener != null) {
            ViewTreeObserver var3;
            if (var1.getWindowToken() != null) {
               var3 = var1.getViewTreeObserver();
            } else {
               var3 = null;
            }

            ViewTreeObserver var2 = this.mViewTreeObserver;
            if (var3 != var2) {
               if (var2 != null && var2.isAlive()) {
                  this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
               }

               this.mViewTreeObserver = var3;
               if (var3 != null) {
                  var3.addOnScrollChangedListener(this.mSuperScrollListener);
               }
            }
         }

      }

      private void unregisterListener() {
         if (this.mSuperScrollListener != null) {
            ViewTreeObserver var1 = this.mViewTreeObserver;
            if (var1 != null) {
               if (var1.isAlive()) {
                  this.mViewTreeObserver.removeOnScrollChangedListener(this.mSuperScrollListener);
               }

               this.mViewTreeObserver = null;
            }
         }

      }

      public void dismiss() {
         this.setFocusable(false);

         try {
            super.dismiss();
         } catch (Exception var2) {
         }

         this.unregisterListener();
      }

      public void showAsDropDown(View var1, int var2, int var3) {
         try {
            super.showAsDropDown(var1, var2, var3);
            this.registerListener(var1);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

      }

      public void showAtLocation(View var1, int var2, int var3, int var4) {
         super.showAtLocation(var1, var2, var3, var4);
         this.unregisterListener();
      }

      public void update(View var1, int var2, int var3) {
         super.update(var1, var2, var3);
         this.registerListener(var1);
      }

      public void update(View var1, int var2, int var3, int var4, int var5) {
         super.update(var1, var2, var3, var4, var5);
         this.registerListener(var1);
      }
   }

   private class EmojiSearchAdapter extends RecyclerListView.SelectionAdapter {
      private String lastSearchAlias;
      private String lastSearchEmojiString;
      private ArrayList result;
      private Runnable searchRunnable;
      private boolean searchWas;

      private EmojiSearchAdapter() {
         this.result = new ArrayList();
      }

      // $FF: synthetic method
      EmojiSearchAdapter(Object var2) {
         this();
      }

      public int getItemCount() {
         int var1;
         if (this.result.isEmpty() && !this.searchWas) {
            var1 = Emoji.recentEmoji.size();
         } else {
            if (this.result.isEmpty()) {
               return 2;
            }

            var1 = this.result.size();
         }

         return var1 + 1;
      }

      public int getItemViewType(int var1) {
         if (var1 == 0) {
            return 1;
         } else {
            return var1 == 1 && this.searchWas && this.result.isEmpty() ? 2 : 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            EmojiView.ImageViewEmoji var3 = (EmojiView.ImageViewEmoji)var1.itemView;
            --var2;
            boolean var4;
            String var5;
            if (this.result.isEmpty() && !this.searchWas) {
               var5 = (String)Emoji.recentEmoji.get(var2);
               var4 = true;
            } else {
               var5 = ((DataQuery.KeywordResult)this.result.get(var2)).emoji;
               var4 = false;
            }

            var3.setImageDrawable(Emoji.getEmojiBigDrawable(var5), var4);
            var3.setTag(var5);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 0) {
            if (var2 != 1) {
               var4 = new FrameLayout(EmojiView.this.getContext()) {
                  protected void onMeasure(int var1, int var2) {
                     View var3 = (View)EmojiView.this.getParent();
                     if (var3 != null) {
                        var2 = (int)((float)var3.getMeasuredHeight() - EmojiView.this.getY());
                     } else {
                        var2 = AndroidUtilities.dp(120.0F);
                     }

                     super.onMeasure(var1, MeasureSpec.makeMeasureSpec(var2 - EmojiView.this.searchFieldHeight, 1073741824));
                  }
               };
               TextView var3 = new TextView(EmojiView.this.getContext());
               var3.setText(LocaleController.getString("NoEmojiFound", 2131559922));
               var3.setTextSize(1, 16.0F);
               var3.setTextColor(Theme.getColor("chat_emojiPanelEmptyText"));
               ((FrameLayout)var4).addView(var3, LayoutHelper.createFrame(-2, -2.0F, 49, 0.0F, 10.0F, 0.0F, 0.0F));
               ImageView var6 = new ImageView(EmojiView.this.getContext());
               var6.setScaleType(ScaleType.CENTER);
               var6.setImageResource(2131165842);
               var6.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelEmptyText"), Mode.MULTIPLY));
               ((FrameLayout)var4).addView(var6, LayoutHelper.createFrame(48, 48, 85));
               var6.setOnClickListener(new OnClickListener() {
                  public void onClick(View var1) {
                     final boolean[] var2 = new boolean[1];
                     final BottomSheet.Builder var3 = new BottomSheet.Builder(EmojiView.this.getContext());
                     LinearLayout var4 = new LinearLayout(EmojiView.this.getContext());
                     var4.setOrientation(1);
                     var4.setPadding(AndroidUtilities.dp(21.0F), 0, AndroidUtilities.dp(21.0F), 0);
                     ImageView var9 = new ImageView(EmojiView.this.getContext());
                     var9.setImageResource(2131165833);
                     var4.addView(var9, LayoutHelper.createLinear(-2, -2, 49, 0, 15, 0, 0));
                     TextView var10 = new TextView(EmojiView.this.getContext());
                     var10.setText(LocaleController.getString("EmojiSuggestions", 2131559341));
                     var10.setTextSize(1, 15.0F);
                     var10.setTextColor(Theme.getColor("dialogTextBlue2"));
                     boolean var5 = LocaleController.isRTL;
                     byte var6 = 5;
                     byte var7;
                     if (var5) {
                        var7 = 5;
                     } else {
                        var7 = 3;
                     }

                     var10.setGravity(var7);
                     var10.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                     var4.addView(var10, LayoutHelper.createLinear(-2, -2, 51, 0, 24, 0, 0));
                     var10 = new TextView(EmojiView.this.getContext());
                     var10.setText(AndroidUtilities.replaceTags(LocaleController.getString("EmojiSuggestionsInfo", 2131559342)));
                     var10.setTextSize(1, 15.0F);
                     var10.setTextColor(Theme.getColor("dialogTextBlack"));
                     if (LocaleController.isRTL) {
                        var7 = 5;
                     } else {
                        var7 = 3;
                     }

                     var10.setGravity(var7);
                     var4.addView(var10, LayoutHelper.createLinear(-2, -2, 51, 0, 11, 0, 0));
                     TextView var8 = new TextView(EmojiView.this.getContext());
                     Object var11;
                     if (EmojiSearchAdapter.this.lastSearchAlias != null) {
                        var11 = EmojiSearchAdapter.this.lastSearchAlias;
                     } else {
                        var11 = EmojiView.this.lastSearchKeyboardLanguage;
                     }

                     var8.setText(LocaleController.formatString("EmojiSuggestionsUrl", 2131559343, var11));
                     var8.setTextSize(1, 15.0F);
                     var8.setTextColor(Theme.getColor("dialogTextLink"));
                     if (LocaleController.isRTL) {
                        var7 = var6;
                     } else {
                        var7 = 3;
                     }

                     var8.setGravity(var7);
                     var4.addView(var8, LayoutHelper.createLinear(-2, -2, 51, 0, 18, 0, 16));
                     var8.setOnClickListener(new OnClickListener() {
                        // $FF: synthetic method
                        public void lambda$null$0$EmojiView$EmojiSearchAdapter$2$1(AlertDialog[] var1, TLObject var2x, BottomSheet.Builder var3x) {
                           try {
                              var1[0].dismiss();
                           } catch (Throwable var5) {
                           }

                           var1[0] = null;
                           if (var2x instanceof TLRPC.TL_emojiURL) {
                              Browser.openUrl(EmojiView.this.getContext(), ((TLRPC.TL_emojiURL)var2x).url);
                              var3x.getDismissRunnable().run();
                           }

                        }

                        // $FF: synthetic method
                        public void lambda$null$2$EmojiView$EmojiSearchAdapter$2$1(int var1, DialogInterface var2x) {
                           ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(var1, true);
                        }

                        // $FF: synthetic method
                        public void lambda$onClick$1$EmojiView$EmojiSearchAdapter$2$1(AlertDialog[] var1, BottomSheet.Builder var2x, TLObject var3x, TLRPC.TL_error var4) {
                           AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$EmojiSearchAdapter$2$1$k1LSrqBP6ptq3f6NASRqH9_nzpY(this, var1, var3x, var2x));
                        }

                        // $FF: synthetic method
                        public void lambda$onClick$3$EmojiView$EmojiSearchAdapter$2$1(AlertDialog[] var1, int var2x) {
                           if (var1[0] != null) {
                              var1[0].setOnCancelListener(new _$$Lambda$EmojiView$EmojiSearchAdapter$2$1$BvShOFTdaqiZ1q6_dGuWuV6cZ2o(this, var2x));
                              var1[0].show();
                           }
                        }

                        public void onClick(View var1) {
                           boolean[] var4 = var2;
                           if (!var4[0]) {
                              var4[0] = true;
                              AlertDialog[] var2x = new AlertDialog[]{new AlertDialog(EmojiView.this.getContext(), 3)};
                              TLRPC.TL_messages_getEmojiURL var3x = new TLRPC.TL_messages_getEmojiURL();
                              String var5;
                              if (EmojiSearchAdapter.this.lastSearchAlias != null) {
                                 var5 = EmojiSearchAdapter.this.lastSearchAlias;
                              } else {
                                 var5 = EmojiView.this.lastSearchKeyboardLanguage[0];
                              }

                              var3x.lang_code = var5;
                              AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$EmojiSearchAdapter$2$1$TwKjymEFVh1Ja7xLl_9KmhIF6eg(this, var2x, ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(var3x, new _$$Lambda$EmojiView$EmojiSearchAdapter$2$1$vESTLZoOoKr_laMmjTYW6gGhW9U(this, var2x, var3))), 1000L);
                           }
                        }
                     });
                     var3.setCustomView(var4);
                     var3.show();
                  }
               });
               ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            } else {
               var4 = new View(EmojiView.this.getContext());
               ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
            }
         } else {
            EmojiView var5 = EmojiView.this;
            var4 = var5.new ImageViewEmoji(var5.getContext());
         }

         return new RecyclerListView.Holder((View)var4);
      }

      public void search(String var1) {
         if (TextUtils.isEmpty(var1)) {
            this.lastSearchEmojiString = null;
            if (EmojiView.this.emojiGridView.getAdapter() != EmojiView.this.emojiAdapter) {
               EmojiView.this.emojiGridView.setAdapter(EmojiView.this.emojiAdapter);
               this.searchWas = false;
            }

            this.notifyDataSetChanged();
         } else {
            this.lastSearchEmojiString = var1.toLowerCase();
         }

         Runnable var2 = this.searchRunnable;
         if (var2 != null) {
            AndroidUtilities.cancelRunOnUIThread(var2);
         }

         if (!TextUtils.isEmpty(this.lastSearchEmojiString)) {
            var2 = new Runnable() {
               public void run() {
                  EmojiView.this.emojiSearchField.progressDrawable.startAnimation();
                  final String var1 = EmojiSearchAdapter.this.lastSearchEmojiString;
                  String[] var2 = AndroidUtilities.getCurrentKeyboardLanguage();
                  if (!Arrays.equals(EmojiView.this.lastSearchKeyboardLanguage, var2)) {
                     DataQuery.getInstance(EmojiView.this.currentAccount).fetchNewEmojiKeywords(var2);
                  }

                  EmojiView.this.lastSearchKeyboardLanguage = var2;
                  DataQuery.getInstance(EmojiView.this.currentAccount).getEmojiSuggestions(EmojiView.this.lastSearchKeyboardLanguage, EmojiSearchAdapter.this.lastSearchEmojiString, false, new DataQuery.KeywordResultCallback() {
                     public void run(ArrayList var1x, String var2) {
                        if (var1.equals(EmojiSearchAdapter.this.lastSearchEmojiString)) {
                           EmojiSearchAdapter.this.lastSearchAlias = var2;
                           EmojiView.this.emojiSearchField.progressDrawable.stopAnimation();
                           EmojiSearchAdapter.this.searchWas = true;
                           if (EmojiView.this.emojiGridView.getAdapter() != EmojiView.this.emojiSearchAdapter) {
                              EmojiView.this.emojiGridView.setAdapter(EmojiView.this.emojiSearchAdapter);
                           }

                           EmojiSearchAdapter.this.result = var1x;
                           EmojiSearchAdapter.this.notifyDataSetChanged();
                        }

                     }
                  });
               }
            };
            this.searchRunnable = var2;
            AndroidUtilities.runOnUIThread(var2, 300L);
         }

      }
   }

   public interface EmojiViewDelegate {
      boolean isExpanded();

      boolean isSearchOpened();

      boolean onBackspace();

      void onClearEmojiRecent();

      void onEmojiSelected(String var1);

      void onGifSelected(Object var1, Object var2);

      void onSearchOpenClose(int var1);

      void onShowStickerSet(TLRPC.StickerSet var1, TLRPC.InputStickerSet var2);

      void onStickerSelected(TLRPC.Document var1, Object var2);

      void onStickerSetAdd(TLRPC.StickerSetCovered var1);

      void onStickerSetRemove(TLRPC.StickerSetCovered var1);

      void onStickersGroupClick(int var1);

      void onStickersSettingsClick();

      void onTabOpened(int var1);
   }

   private class GifAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public GifAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return EmojiView.this.recentGifs.size() + 1;
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public int getItemViewType(int var1) {
         return var1 == 0 ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            TLRPC.Document var3 = (TLRPC.Document)EmojiView.this.recentGifs.get(var2 - 1);
            if (var3 != null) {
               ((ContextLinkCell)var1.itemView).setGif(var3, false);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            var3 = new View(EmojiView.this.getContext());
            ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
         } else {
            var3 = new ContextLinkCell(this.mContext);
            ((View)var3).setContentDescription(LocaleController.getString("AttachGif", 2131558716));
            ((ContextLinkCell)var3).setCanPreviewGif(true);
         }

         return new RecyclerListView.Holder((View)var3);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         View var2 = var1.itemView;
         if (var2 instanceof ContextLinkCell) {
            ImageReceiver var3 = ((ContextLinkCell)var2).getPhotoImage();
            if (EmojiView.this.pager.getCurrentItem() == 1) {
               var3.setAllowStartAnimation(true);
               var3.startAnimation();
            } else {
               var3.setAllowStartAnimation(false);
               var3.stopAnimation();
            }
         }

      }
   }

   private class GifSearchAdapter extends RecyclerListView.SelectionAdapter {
      private TLRPC.User bot;
      private String lastSearchImageString;
      private Context mContext;
      private String nextSearchOffset;
      private int reqId;
      private ArrayList results = new ArrayList();
      private HashMap resultsMap = new HashMap();
      private boolean searchEndReached;
      private Runnable searchRunnable;
      private boolean searchingUser;

      public GifSearchAdapter(Context var2) {
         this.mContext = var2;
      }

      private void search(String var1, String var2, boolean var3) {
         if (this.reqId != 0) {
            ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
         }

         this.lastSearchImageString = var1;
         TLObject var4 = MessagesController.getInstance(EmojiView.this.currentAccount).getUserOrChat(MessagesController.getInstance(EmojiView.this.currentAccount).gifSearchBot);
         if (!(var4 instanceof TLRPC.User)) {
            if (var3) {
               this.searchBotUser();
               EmojiView.this.gifSearchField.progressDrawable.startAnimation();
            }

         } else {
            if (TextUtils.isEmpty(var2)) {
               EmojiView.this.gifSearchField.progressDrawable.startAnimation();
            }

            this.bot = (TLRPC.User)var4;
            TLRPC.TL_messages_getInlineBotResults var5 = new TLRPC.TL_messages_getInlineBotResults();
            String var6 = var1;
            if (var1 == null) {
               var6 = "";
            }

            var5.query = var6;
            var5.bot = MessagesController.getInstance(EmojiView.this.currentAccount).getInputUser(this.bot);
            var5.offset = var2;
            var5.peer = new TLRPC.TL_inputPeerEmpty();
            this.reqId = ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(var5, new _$$Lambda$EmojiView$GifSearchAdapter$rHXwwVWZa9pK_feyWosndOy6a0o(this, var5, var2), 2);
         }
      }

      private void searchBotUser() {
         if (!this.searchingUser) {
            this.searchingUser = true;
            TLRPC.TL_contacts_resolveUsername var1 = new TLRPC.TL_contacts_resolveUsername();
            var1.username = MessagesController.getInstance(EmojiView.this.currentAccount).gifSearchBot;
            ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(var1, new _$$Lambda$EmojiView$GifSearchAdapter$rB6iGH2IIIWpfXKf_CfIBRZyOE8(this));
         }
      }

      public int getItemCount() {
         int var1;
         if (this.results.isEmpty()) {
            var1 = 1;
         } else {
            var1 = this.results.size();
         }

         return var1 + 1;
      }

      public int getItemViewType(int var1) {
         if (var1 == 0) {
            return 1;
         } else {
            return this.results.isEmpty() ? 2 : 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      // $FF: synthetic method
      public void lambda$null$0$EmojiView$GifSearchAdapter(TLObject var1) {
         TLRPC.TL_contacts_resolvedPeer var2 = (TLRPC.TL_contacts_resolvedPeer)var1;
         MessagesController.getInstance(EmojiView.this.currentAccount).putUsers(var2.users, false);
         MessagesController.getInstance(EmojiView.this.currentAccount).putChats(var2.chats, false);
         MessagesStorage.getInstance(EmojiView.this.currentAccount).putUsersAndChats(var2.users, var2.chats, true, true);
         String var3 = this.lastSearchImageString;
         this.lastSearchImageString = null;
         this.search(var3, "", false);
      }

      // $FF: synthetic method
      public void lambda$null$2$EmojiView$GifSearchAdapter(TLRPC.TL_messages_getInlineBotResults var1, String var2, TLObject var3) {
         if (var1.query.equals(this.lastSearchImageString)) {
            if (EmojiView.this.gifGridView.getAdapter() != EmojiView.this.gifSearchAdapter) {
               EmojiView.this.gifGridView.setAdapter(EmojiView.this.gifSearchAdapter);
            }

            if (TextUtils.isEmpty(var2)) {
               this.results.clear();
               this.resultsMap.clear();
               EmojiView.this.gifSearchField.progressDrawable.stopAnimation();
            }

            boolean var4 = false;
            this.reqId = 0;
            if (var3 instanceof TLRPC.messages_BotResults) {
               int var5 = this.results.size();
               TLRPC.messages_BotResults var9 = (TLRPC.messages_BotResults)var3;
               this.nextSearchOffset = var9.next_offset;
               int var6 = 0;

               int var7;
               for(var7 = 0; var6 < var9.results.size(); ++var6) {
                  TLRPC.BotInlineResult var8 = (TLRPC.BotInlineResult)var9.results.get(var6);
                  if (!this.resultsMap.containsKey(var8.id)) {
                     var8.query_id = var9.query_id;
                     this.results.add(var8);
                     this.resultsMap.put(var8.id, var8);
                     ++var7;
                  }
               }

               if (var5 == this.results.size()) {
                  var4 = true;
               }

               this.searchEndReached = var4;
               if (var7 != 0) {
                  if (var5 != 0) {
                     this.notifyItemChanged(var5);
                  }

                  this.notifyItemRangeInserted(var5 + 1, var7);
               } else if (this.results.isEmpty()) {
                  this.notifyDataSetChanged();
               }
            } else {
               this.notifyDataSetChanged();
            }
         }

      }

      // $FF: synthetic method
      public void lambda$search$3$EmojiView$GifSearchAdapter(TLRPC.TL_messages_getInlineBotResults var1, String var2, TLObject var3, TLRPC.TL_error var4) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$GifSearchAdapter$T00z6XPCmLZsEfaqGzAOZW0Q_C8(this, var1, var2, var3));
      }

      // $FF: synthetic method
      public void lambda$searchBotUser$1$EmojiView$GifSearchAdapter(TLObject var1, TLRPC.TL_error var2) {
         if (var1 != null) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$GifSearchAdapter$XYVthzC0iAXb1tv95YkceAYL3QE(this, var1));
         }

      }

      public void notifyDataSetChanged() {
         super.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            TLRPC.BotInlineResult var3 = (TLRPC.BotInlineResult)this.results.get(var2 - 1);
            ((ContextLinkCell)var1.itemView).setLink(var3, true, false, false);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 0) {
            if (var2 != 1) {
               var4 = new FrameLayout(EmojiView.this.getContext()) {
                  protected void onMeasure(int var1, int var2) {
                     super.onMeasure(var1, MeasureSpec.makeMeasureSpec((int)((float)((EmojiView.this.gifGridView.getMeasuredHeight() - EmojiView.this.searchFieldHeight - AndroidUtilities.dp(8.0F)) / 3) * 1.7F), 1073741824));
                  }
               };
               ImageView var3 = new ImageView(EmojiView.this.getContext());
               var3.setScaleType(ScaleType.CENTER);
               var3.setImageResource(2131165391);
               var3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelEmptyText"), Mode.MULTIPLY));
               ((FrameLayout)var4).addView(var3, LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, 0.0F, 0.0F, 59.0F));
               TextView var5 = new TextView(EmojiView.this.getContext());
               var5.setText(LocaleController.getString("NoGIFsFound", 2131559924));
               var5.setTextSize(1, 16.0F);
               var5.setTextColor(Theme.getColor("chat_emojiPanelEmptyText"));
               ((FrameLayout)var4).addView(var5, LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, 0.0F, 0.0F, 9.0F));
               ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            } else {
               var4 = new View(EmojiView.this.getContext());
               ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
            }
         } else {
            var4 = new ContextLinkCell(this.mContext);
            ((View)var4).setContentDescription(LocaleController.getString("AttachGif", 2131558716));
            ((ContextLinkCell)var4).setCanPreviewGif(true);
         }

         return new RecyclerListView.Holder((View)var4);
      }

      public void search(final String var1) {
         if (this.reqId != 0) {
            ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
         }

         if (TextUtils.isEmpty(var1)) {
            this.lastSearchImageString = null;
            if (EmojiView.this.gifGridView.getAdapter() != EmojiView.this.gifAdapter) {
               EmojiView.this.gifGridView.setAdapter(EmojiView.this.gifAdapter);
            }

            this.notifyDataSetChanged();
         } else {
            this.lastSearchImageString = var1.toLowerCase();
         }

         Runnable var2 = this.searchRunnable;
         if (var2 != null) {
            AndroidUtilities.cancelRunOnUIThread(var2);
         }

         if (!TextUtils.isEmpty(this.lastSearchImageString)) {
            Runnable var3 = new Runnable() {
               public void run() {
                  GifSearchAdapter.this.search(var1, "", true);
               }
            };
            this.searchRunnable = var3;
            AndroidUtilities.runOnUIThread(var3, 300L);
         }

      }
   }

   private class ImageViewEmoji extends ImageView {
      private boolean isRecent;

      public ImageViewEmoji(Context var2) {
         super(var2);
         this.setScaleType(ScaleType.CENTER);
      }

      private void sendEmoji(String var1) {
         EmojiView.this.showBottomTab(true, true);
         String var2;
         if (var1 != null) {
            var2 = var1;
         } else {
            var2 = (String)this.getTag();
         }

         (new SpannableStringBuilder()).append(var2);
         if (var1 == null) {
            var1 = var2;
            if (!this.isRecent) {
               String var3 = (String)Emoji.emojiColor.get(var2);
               var1 = var2;
               if (var3 != null) {
                  var1 = EmojiView.addColorToCode(var2, var3);
               }
            }

            EmojiView.this.addEmojiToRecent(var1);
            if (EmojiView.this.delegate != null) {
               EmojiView.this.delegate.onEmojiSelected(Emoji.fixEmoji(var1));
            }
         } else if (EmojiView.this.delegate != null) {
            EmojiView.this.delegate.onEmojiSelected(Emoji.fixEmoji(var1));
         }

      }

      public void onMeasure(int var1, int var2) {
         this.setMeasuredDimension(MeasureSpec.getSize(var1), MeasureSpec.getSize(var1));
      }

      public void setImageDrawable(Drawable var1, boolean var2) {
         super.setImageDrawable(var1);
         this.isRecent = var2;
      }
   }

   private class SearchField extends FrameLayout {
      private View backgroundView;
      private ImageView clearSearchImageView;
      private CloseProgressDrawable2 progressDrawable;
      private View searchBackground;
      private EditTextBoldCursor searchEditText;
      private ImageView searchIconImageView;
      private AnimatorSet shadowAnimator;
      private View shadowView;

      public SearchField(Context var2, final int var3) {
         super(var2);
         this.shadowView = new View(var2);
         this.shadowView.setAlpha(0.0F);
         this.shadowView.setTag(1);
         this.shadowView.setBackgroundColor(Theme.getColor("chat_emojiPanelShadowLine"));
         this.addView(this.shadowView, new LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83));
         this.backgroundView = new View(var2);
         this.backgroundView.setBackgroundColor(Theme.getColor("chat_emojiPanelBackground"));
         this.addView(this.backgroundView, new LayoutParams(-1, EmojiView.this.searchFieldHeight));
         this.searchBackground = new View(var2);
         this.searchBackground.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0F), Theme.getColor("chat_emojiSearchBackground")));
         this.addView(this.searchBackground, LayoutHelper.createFrame(-1, 36.0F, 51, 14.0F, 14.0F, 14.0F, 0.0F));
         this.searchIconImageView = new ImageView(var2);
         this.searchIconImageView.setScaleType(ScaleType.CENTER);
         this.searchIconImageView.setImageResource(2131165834);
         this.searchIconImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiSearchIcon"), Mode.MULTIPLY));
         this.addView(this.searchIconImageView, LayoutHelper.createFrame(36, 36.0F, 51, 16.0F, 14.0F, 0.0F, 0.0F));
         this.clearSearchImageView = new ImageView(var2);
         this.clearSearchImageView.setScaleType(ScaleType.CENTER);
         ImageView var4 = this.clearSearchImageView;
         CloseProgressDrawable2 var5 = new CloseProgressDrawable2();
         this.progressDrawable = var5;
         var4.setImageDrawable(var5);
         this.progressDrawable.setSide(AndroidUtilities.dp(7.0F));
         this.clearSearchImageView.setScaleX(0.1F);
         this.clearSearchImageView.setScaleY(0.1F);
         this.clearSearchImageView.setAlpha(0.0F);
         this.clearSearchImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiSearchIcon"), Mode.MULTIPLY));
         this.addView(this.clearSearchImageView, LayoutHelper.createFrame(36, 36.0F, 53, 14.0F, 14.0F, 14.0F, 0.0F));
         this.clearSearchImageView.setOnClickListener(new _$$Lambda$EmojiView$SearchField$04evSuS5X_E_yJiP7OOqbVezHUE(this));
         this.searchEditText = new EditTextBoldCursor(var2) {
            public boolean onTouchEvent(MotionEvent var1) {
               if (var1.getAction() == 0) {
                  if (!EmojiView.this.delegate.isSearchOpened()) {
                     EmojiView.SearchField var2 = SearchField.this;
                     EmojiView.this.openSearch(var2);
                  }

                  EmojiView.EmojiViewDelegate var5 = EmojiView.this.delegate;
                  int var3x = var3;
                  byte var4 = 1;
                  if (var3x == 1) {
                     var4 = 2;
                  }

                  var5.onSearchOpenClose(var4);
                  SearchField.this.searchEditText.requestFocus();
                  AndroidUtilities.showKeyboard(SearchField.this.searchEditText);
                  if (EmojiView.this.trendingGridView != null && EmojiView.this.trendingGridView.getVisibility() == 0) {
                     EmojiView.this.showTrendingTab(false);
                  }
               }

               return super.onTouchEvent(var1);
            }
         };
         this.searchEditText.setTextSize(1, 16.0F);
         this.searchEditText.setHintTextColor(Theme.getColor("chat_emojiSearchIcon"));
         this.searchEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.searchEditText.setBackgroundDrawable((Drawable)null);
         this.searchEditText.setPadding(0, 0, 0, 0);
         this.searchEditText.setMaxLines(1);
         this.searchEditText.setLines(1);
         this.searchEditText.setSingleLine(true);
         this.searchEditText.setImeOptions(268435459);
         if (var3 == 0) {
            this.searchEditText.setHint(LocaleController.getString("SearchStickersHint", 2131560656));
         } else if (var3 == 1) {
            this.searchEditText.setHint(LocaleController.getString("SearchEmojiHint", 2131560643));
         } else if (var3 == 2) {
            this.searchEditText.setHint(LocaleController.getString("SearchGifsTitle", 2131560649));
         }

         this.searchEditText.setCursorColor(Theme.getColor("featuredStickers_addedIcon"));
         this.searchEditText.setCursorSize(AndroidUtilities.dp(20.0F));
         this.searchEditText.setCursorWidth(1.5F);
         this.addView(this.searchEditText, LayoutHelper.createFrame(-1, 40.0F, 51, 54.0F, 12.0F, 46.0F, 0.0F));
         this.searchEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               int var2 = SearchField.this.searchEditText.length();
               boolean var3x = false;
               boolean var7;
               if (var2 > 0) {
                  var7 = true;
               } else {
                  var7 = false;
               }

               float var4 = SearchField.this.clearSearchImageView.getAlpha();
               float var5 = 0.0F;
               if (var4 != 0.0F) {
                  var3x = true;
               }

               if (var7 != var3x) {
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

               var2 = var3;
               if (var2 == 0) {
                  EmojiView.this.stickersSearchGridAdapter.search(SearchField.this.searchEditText.getText().toString());
               } else if (var2 == 1) {
                  EmojiView.this.emojiSearchAdapter.search(SearchField.this.searchEditText.getText().toString());
               } else if (var2 == 2) {
                  EmojiView.this.gifSearchAdapter.search(SearchField.this.searchEditText.getText().toString());
               }

            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3x, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3x, int var4) {
            }
         });
      }

      private void showShadow(boolean var1, boolean var2) {
         if ((!var1 || this.shadowView.getTag() != null) && (var1 || this.shadowView.getTag() == null)) {
            AnimatorSet var3 = this.shadowAnimator;
            Integer var4 = null;
            if (var3 != null) {
               var3.cancel();
               this.shadowAnimator = null;
            }

            View var7 = this.shadowView;
            if (!var1) {
               var4 = 1;
            }

            var7.setTag(var4);
            float var5 = 1.0F;
            View var8;
            if (var2) {
               this.shadowAnimator = new AnimatorSet();
               var3 = this.shadowAnimator;
               var8 = this.shadowView;
               Property var6 = View.ALPHA;
               if (!var1) {
                  var5 = 0.0F;
               }

               var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(var8, var6, new float[]{var5})});
               this.shadowAnimator.setDuration(200L);
               this.shadowAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
               this.shadowAnimator.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     SearchField.this.shadowAnimator = null;
                  }
               });
               this.shadowAnimator.start();
            } else {
               var8 = this.shadowView;
               if (!var1) {
                  var5 = 0.0F;
               }

               var8.setAlpha(var5);
            }

         }
      }

      public void hideKeyboard() {
         AndroidUtilities.hideKeyboard(this.searchEditText);
      }

      // $FF: synthetic method
      public void lambda$new$0$EmojiView$SearchField(View var1) {
         this.searchEditText.setText("");
         AndroidUtilities.showKeyboard(this.searchEditText);
      }
   }

   private class StickersGridAdapter extends RecyclerListView.SelectionAdapter {
      private SparseArray cache = new SparseArray();
      private SparseArray cacheParents = new SparseArray();
      private Context context;
      private HashMap packStartPosition = new HashMap();
      private SparseIntArray positionToRow = new SparseIntArray();
      private SparseArray rowStartPack = new SparseArray();
      private int stickersPerRow;
      private int totalItems;

      public StickersGridAdapter(Context var2) {
         this.context = var2;
      }

      public Object getItem(int var1) {
         return this.cache.get(var1);
      }

      public int getItemCount() {
         int var1 = this.totalItems;
         if (var1 != 0) {
            ++var1;
         } else {
            var1 = 0;
         }

         return var1;
      }

      public int getItemViewType(int var1) {
         if (var1 == 0) {
            return 4;
         } else {
            Object var2 = this.cache.get(var1);
            if (var2 != null) {
               if (var2 instanceof TLRPC.Document) {
                  return 0;
               } else {
                  return var2 instanceof String ? 3 : 2;
               }
            } else {
               return 1;
            }
         }
      }

      public int getPositionForPack(Object var1) {
         Integer var2 = (Integer)this.packStartPosition.get(var1);
         return var2 == null ? -1 : var2;
      }

      public int getTabForPosition(int var1) {
         int var2 = var1;
         if (var1 == 0) {
            var2 = 1;
         }

         if (this.stickersPerRow == 0) {
            int var3 = EmojiView.this.getMeasuredWidth();
            var1 = var3;
            if (var3 == 0) {
               var1 = AndroidUtilities.displaySize.x;
            }

            this.stickersPerRow = var1 / AndroidUtilities.dp(72.0F);
         }

         var1 = this.positionToRow.get(var2, Integer.MIN_VALUE);
         if (var1 == Integer.MIN_VALUE) {
            return EmojiView.this.stickerSets.size() - 1 + EmojiView.this.stickersTabOffset;
         } else {
            Object var4 = this.rowStartPack.get(var1);
            if (var4 instanceof String) {
               return "recent".equals(var4) ? EmojiView.this.recentTabBum : EmojiView.this.favTabBum;
            } else {
               TLRPC.TL_messages_stickerSet var5 = (TLRPC.TL_messages_stickerSet)var4;
               return EmojiView.this.stickerSets.indexOf(var5) + EmojiView.this.stickersTabOffset;
            }
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$EmojiView$StickersGridAdapter(View var1) {
         if (EmojiView.this.groupStickerSet != null) {
            if (EmojiView.this.delegate != null) {
               EmojiView.this.delegate.onStickersGroupClick(EmojiView.this.info.id);
            }
         } else {
            Editor var5 = MessagesController.getEmojiSettings(EmojiView.this.currentAccount).edit();
            StringBuilder var2 = new StringBuilder();
            var2.append("group_hide_stickers_");
            var2.append(EmojiView.this.info.id);
            String var6 = var2.toString();
            long var3;
            if (EmojiView.this.info.stickerset != null) {
               var3 = EmojiView.this.info.stickerset.id;
            } else {
               var3 = 0L;
            }

            var5.putLong(var6, var3).commit();
            EmojiView.this.updateStickerTabs();
            if (EmojiView.this.stickersGridAdapter != null) {
               EmojiView.this.stickersGridAdapter.notifyDataSetChanged();
            }
         }

      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$1$EmojiView$StickersGridAdapter(View var1) {
         if (EmojiView.this.delegate != null) {
            EmojiView.this.delegate.onStickersGroupClick(EmojiView.this.info.id);
         }

      }

      public void notifyDataSetChanged() {
         int var1 = EmojiView.this.getMeasuredWidth();
         int var2 = var1;
         if (var1 == 0) {
            var2 = AndroidUtilities.displaySize.x;
         }

         this.stickersPerRow = var2 / AndroidUtilities.dp(72.0F);
         EmojiView.this.stickersLayoutManager.setSpanCount(this.stickersPerRow);
         this.rowStartPack.clear();
         this.packStartPosition.clear();
         this.positionToRow.clear();
         this.cache.clear();
         this.totalItems = 0;
         ArrayList var3 = EmojiView.this.stickerSets;
         var1 = -3;

         for(var2 = 0; var1 < var3.size(); ++var1) {
            int var5;
            SparseArray var13;
            if (var1 == -3) {
               var13 = this.cache;
               var5 = this.totalItems++;
               var13.put(var5, "search");
               ++var2;
            } else {
               TLRPC.TL_messages_stickerSet var6 = null;
               ArrayList var4;
               String var7;
               if (var1 == -2) {
                  var4 = EmojiView.this.favouriteStickers;
                  this.packStartPosition.put("fav", this.totalItems);
                  var7 = "fav";
               } else if (var1 == -1) {
                  var4 = EmojiView.this.recentStickers;
                  this.packStartPosition.put("recent", this.totalItems);
                  var7 = "recent";
               } else {
                  var6 = (TLRPC.TL_messages_stickerSet)var3.get(var1);
                  var4 = var6.documents;
                  this.packStartPosition.put(var6, this.totalItems);
                  var7 = null;
               }

               int var8;
               if (var1 == EmojiView.this.groupStickerPackNum) {
                  EmojiView.this.groupStickerPackPosition = this.totalItems;
                  if (var4.isEmpty()) {
                     this.rowStartPack.put(var2, var6);
                     SparseIntArray var12 = this.positionToRow;
                     var8 = this.totalItems;
                     var5 = var2 + 1;
                     var12.put(var8, var2);
                     this.rowStartPack.put(var5, var6);
                     this.positionToRow.put(this.totalItems + 1, var5);
                     var13 = this.cache;
                     var2 = this.totalItems++;
                     var13.put(var2, var6);
                     var13 = this.cache;
                     var2 = this.totalItems++;
                     var13.put(var2, "group");
                     var2 = var5 + 1;
                     continue;
                  }
               }

               if (!var4.isEmpty()) {
                  int var9 = (int)Math.ceil((double)((float)var4.size() / (float)this.stickersPerRow));
                  if (var6 != null) {
                     this.cache.put(this.totalItems, var6);
                  } else {
                     this.cache.put(this.totalItems, var4);
                  }

                  this.positionToRow.put(this.totalItems, var2);

                  for(var5 = 0; var5 < var4.size(); var5 = var8) {
                     var8 = var5 + 1;
                     int var10 = this.totalItems + var8;
                     this.cache.put(var10, var4.get(var5));
                     if (var6 != null) {
                        this.cacheParents.put(var10, var6);
                     } else {
                        this.cacheParents.put(var10, var7);
                     }

                     this.positionToRow.put(this.totalItems + var8, var2 + 1 + var5 / this.stickersPerRow);
                  }

                  var5 = 0;

                  while(true) {
                     var8 = var9 + 1;
                     if (var5 >= var8) {
                        this.totalItems += var9 * this.stickersPerRow + 1;
                        var2 += var8;
                        break;
                     }

                     if (var6 != null) {
                        this.rowStartPack.put(var2 + var5, var6);
                     } else {
                        SparseArray var14 = this.rowStartPack;
                        String var11;
                        if (var1 == -1) {
                           var11 = "recent";
                        } else {
                           var11 = "fav";
                        }

                        var14.put(var2 + var5, var11);
                     }

                     ++var5;
                  }
               }
            }
         }

         super.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         boolean var5 = false;
         if (var3 != 0) {
            StickerSetNameCell var6 = null;
            EmptyCell var7 = null;
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 == 3) {
                     StickerSetGroupInfoCell var9 = (StickerSetGroupInfoCell)var1.itemView;
                     if (var2 == this.totalItems - 1) {
                        var5 = true;
                     }

                     var9.setIsLast(var5);
                  }
               } else {
                  var6 = (StickerSetNameCell)var1.itemView;
                  if (var2 == EmojiView.this.groupStickerPackPosition) {
                     if (EmojiView.this.groupStickersHidden && EmojiView.this.groupStickerSet == null) {
                        var2 = 0;
                     } else if (EmojiView.this.groupStickerSet != null) {
                        var2 = 2131165865;
                     } else {
                        var2 = 2131165866;
                     }

                     TLRPC.Chat var10 = var7;
                     if (EmojiView.this.info != null) {
                        var10 = MessagesController.getInstance(EmojiView.this.currentAccount).getChat(EmojiView.this.info.id);
                     }

                     String var11;
                     if (var10 != null) {
                        var11 = var10.title;
                     } else {
                        var11 = "Group Stickers";
                     }

                     var6.setText(LocaleController.formatString("CurrentGroupStickers", 2131559180, var11), var2);
                  } else {
                     Object var12 = this.cache.get(var2);
                     if (var12 instanceof TLRPC.TL_messages_stickerSet) {
                        TLRPC.StickerSet var13 = ((TLRPC.TL_messages_stickerSet)var12).set;
                        if (var13 != null) {
                           var6.setText(var13.title, 0);
                        }
                     } else if (var12 == EmojiView.this.recentStickers) {
                        var6.setText(LocaleController.getString("RecentStickers", 2131560538), 0);
                     } else if (var12 == EmojiView.this.favouriteStickers) {
                        var6.setText(LocaleController.getString("FavoriteStickers", 2131559478), 0);
                     }
                  }
               }
            } else {
               var7 = (EmptyCell)var1.itemView;
               if (var2 == this.totalItems) {
                  var2 = this.positionToRow.get(var2 - 1, Integer.MIN_VALUE);
                  if (var2 == Integer.MIN_VALUE) {
                     var7.setHeight(1);
                  } else {
                     Object var8 = this.rowStartPack.get(var2);
                     ArrayList var14;
                     if (var8 instanceof TLRPC.TL_messages_stickerSet) {
                        var14 = ((TLRPC.TL_messages_stickerSet)var8).documents;
                     } else {
                        var14 = var6;
                        if (var8 instanceof String) {
                           if ("recent".equals(var8)) {
                              var14 = EmojiView.this.recentStickers;
                           } else {
                              var14 = EmojiView.this.favouriteStickers;
                           }
                        }
                     }

                     if (var14 == null) {
                        var7.setHeight(1);
                     } else if (var14.isEmpty()) {
                        var7.setHeight(AndroidUtilities.dp(8.0F));
                     } else {
                        var2 = EmojiView.this.pager.getHeight() - (int)Math.ceil((double)((float)var14.size() / (float)this.stickersPerRow)) * AndroidUtilities.dp(82.0F);
                        if (var2 <= 0) {
                           var2 = 1;
                        }

                        var7.setHeight(var2);
                     }
                  }
               } else {
                  var7.setHeight(AndroidUtilities.dp(82.0F));
               }
            }
         } else {
            StickerEmojiCell var15;
            label88: {
               TLRPC.Document var16 = (TLRPC.Document)this.cache.get(var2);
               var15 = (StickerEmojiCell)var1.itemView;
               var15.setSticker(var16, this.cacheParents.get(var2), false);
               if (!EmojiView.this.recentStickers.contains(var16)) {
                  var5 = var4;
                  if (!EmojiView.this.favouriteStickers.contains(var16)) {
                     break label88;
                  }
               }

               var5 = true;
            }

            var15.setRecent(var5);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        var3 = null;
                     } else {
                        var3 = new View(this.context);
                        ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
                     }
                  } else {
                     var3 = new StickerSetGroupInfoCell(this.context);
                     ((StickerSetGroupInfoCell)var3).setAddOnClickListener(new _$$Lambda$EmojiView$StickersGridAdapter$rEbSNlpXftcal36it1dYPmUfJQ8(this));
                     ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                  }
               } else {
                  var3 = new StickerSetNameCell(this.context, false);
                  ((StickerSetNameCell)var3).setOnIconClickListener(new _$$Lambda$EmojiView$StickersGridAdapter$LJAPCN9WKc70C6I4z_4Z05VncJg(this));
               }
            } else {
               var3 = new EmptyCell(this.context);
            }
         } else {
            var3 = new StickerEmojiCell(this.context) {
               public void onMeasure(int var1, int var2) {
                  super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0F), 1073741824));
               }
            };
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }

   private class StickersSearchGridAdapter extends RecyclerListView.SelectionAdapter {
      private SparseArray cache = new SparseArray();
      private SparseArray cacheParent = new SparseArray();
      boolean cleared;
      private Context context;
      private ArrayList emojiArrays = new ArrayList();
      private int emojiSearchId;
      private HashMap emojiStickers = new HashMap();
      private ArrayList localPacks = new ArrayList();
      private HashMap localPacksByName = new HashMap();
      private HashMap localPacksByShortName = new HashMap();
      private SparseArray positionToEmoji = new SparseArray();
      private SparseIntArray positionToRow = new SparseIntArray();
      private SparseArray positionsToSets = new SparseArray();
      private int reqId;
      private int reqId2;
      private SparseArray rowStartPack = new SparseArray();
      private String searchQuery;
      private Runnable searchRunnable = new Runnable() {
         private void clear() {
            EmojiView.StickersSearchGridAdapter var1 = StickersSearchGridAdapter.this;
            if (!var1.cleared) {
               var1.cleared = true;
               var1.emojiStickers.clear();
               StickersSearchGridAdapter.this.emojiArrays.clear();
               StickersSearchGridAdapter.this.localPacks.clear();
               StickersSearchGridAdapter.this.serverPacks.clear();
               StickersSearchGridAdapter.this.localPacksByShortName.clear();
               StickersSearchGridAdapter.this.localPacksByName.clear();
            }
         }

         // $FF: synthetic method
         public void lambda$null$0$EmojiView$StickersSearchGridAdapter$1(TLRPC.TL_messages_searchStickerSets var1, TLObject var2) {
            if (var1.q.equals(StickersSearchGridAdapter.this.searchQuery)) {
               this.clear();
               EmojiView.this.stickersSearchField.progressDrawable.stopAnimation();
               StickersSearchGridAdapter.this.reqId = 0;
               if (EmojiView.this.stickersGridView.getAdapter() != EmojiView.this.stickersSearchGridAdapter) {
                  EmojiView.this.stickersGridView.setAdapter(EmojiView.this.stickersSearchGridAdapter);
               }

               TLRPC.TL_messages_foundStickerSets var3 = (TLRPC.TL_messages_foundStickerSets)var2;
               StickersSearchGridAdapter.this.serverPacks.addAll(var3.sets);
               StickersSearchGridAdapter.this.notifyDataSetChanged();
            }

         }

         // $FF: synthetic method
         public void lambda$null$2$EmojiView$StickersSearchGridAdapter$1(TLRPC.TL_messages_getStickers var1, TLObject var2, ArrayList var3, LongSparseArray var4) {
            if (var1.emoticon.equals(StickersSearchGridAdapter.this.searchQuery)) {
               EmojiView.StickersSearchGridAdapter var8 = StickersSearchGridAdapter.this;
               int var5 = 0;
               var8.reqId2 = 0;
               if (!(var2 instanceof TLRPC.TL_messages_stickers)) {
                  return;
               }

               TLRPC.TL_messages_stickers var10 = (TLRPC.TL_messages_stickers)var2;
               int var6 = var3.size();

               for(int var7 = var10.stickers.size(); var5 < var7; ++var5) {
                  TLRPC.Document var9 = (TLRPC.Document)var10.stickers.get(var5);
                  if (var4.indexOfKey(var9.id) < 0) {
                     var3.add(var9);
                  }
               }

               if (var6 != var3.size()) {
                  StickersSearchGridAdapter.this.emojiStickers.put(var3, StickersSearchGridAdapter.this.searchQuery);
                  if (var6 == 0) {
                     StickersSearchGridAdapter.this.emojiArrays.add(var3);
                  }

                  StickersSearchGridAdapter.this.notifyDataSetChanged();
               }
            }

         }

         // $FF: synthetic method
         public void lambda$run$1$EmojiView$StickersSearchGridAdapter$1(TLRPC.TL_messages_searchStickerSets var1, TLObject var2, TLRPC.TL_error var3) {
            if (var2 instanceof TLRPC.TL_messages_foundStickerSets) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$StickersSearchGridAdapter$1$jguHwpSdjaICAoSBsd6Lf5EhaUQ(this, var1, var2));
            }

         }

         // $FF: synthetic method
         public void lambda$run$3$EmojiView$StickersSearchGridAdapter$1(TLRPC.TL_messages_getStickers var1, ArrayList var2, LongSparseArray var3, TLObject var4, TLRPC.TL_error var5) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$EmojiView$StickersSearchGridAdapter$1$0O6pCOi0jii8pwBAvqjd7az_Jp8(this, var1, var4, var2, var3));
         }

         public void run() {
            if (!TextUtils.isEmpty(StickersSearchGridAdapter.this.searchQuery)) {
               EmojiView.this.stickersSearchField.progressDrawable.startAnimation();
               EmojiView.StickersSearchGridAdapter var1 = StickersSearchGridAdapter.this;
               var1.cleared = false;
               final int var2 = EmojiView.StickersSearchGridAdapter.access$13304(var1);
               ArrayList var3 = new ArrayList(0);
               LongSparseArray var4 = new LongSparseArray(0);
               final HashMap var5 = DataQuery.getInstance(EmojiView.this.currentAccount).getAllStickers();
               int var6;
               int var7;
               int var8;
               ArrayList var13;
               if (StickersSearchGridAdapter.this.searchQuery.length() <= 14) {
                  Object var11 = StickersSearchGridAdapter.this.searchQuery;
                  var6 = ((CharSequence)var11).length();

                  Object var10;
                  for(var7 = 0; var7 < var6; var11 = var10) {
                     int var9;
                     label148: {
                        CharSequence var12;
                        label177: {
                           if (var7 < var6 - 1) {
                              label170: {
                                 label162: {
                                    if (((CharSequence)var11).charAt(var7) == '\ud83c') {
                                       var8 = var7 + 1;
                                       if (((CharSequence)var11).charAt(var8) >= '\udffb' && ((CharSequence)var11).charAt(var8) <= '\udfff') {
                                          break label162;
                                       }
                                    }

                                    if (((CharSequence)var11).charAt(var7) != 8205) {
                                       break label170;
                                    }

                                    var8 = var7 + 1;
                                    if (((CharSequence)var11).charAt(var8) != 9792 && ((CharSequence)var11).charAt(var8) != 9794) {
                                       break label170;
                                    }
                                 }

                                 var12 = TextUtils.concat(new CharSequence[]{((CharSequence)var11).subSequence(0, var7), ((CharSequence)var11).subSequence(var7 + 2, ((CharSequence)var11).length())});
                                 var6 -= 2;
                                 break label177;
                              }
                           }

                           var9 = var7;
                           var8 = var6;
                           var10 = var11;
                           if (((CharSequence)var11).charAt(var7) != '️') {
                              break label148;
                           }

                           var12 = TextUtils.concat(new CharSequence[]{((CharSequence)var11).subSequence(0, var7), ((CharSequence)var11).subSequence(var7 + 1, ((CharSequence)var11).length())});
                           --var6;
                        }

                        var9 = var7 - 1;
                        var10 = var12;
                        var8 = var6;
                     }

                     var7 = var9 + 1;
                     var6 = var8;
                  }

                  if (var5 != null) {
                     var13 = (ArrayList)var5.get(((CharSequence)var11).toString());
                  } else {
                     var13 = null;
                  }

                  if (var13 != null && !var13.isEmpty()) {
                     this.clear();
                     var3.addAll(var13);
                     var6 = var13.size();

                     for(var7 = 0; var7 < var6; ++var7) {
                        TLRPC.Document var19 = (TLRPC.Document)var13.get(var7);
                        var4.put(var19.id, var19);
                     }

                     StickersSearchGridAdapter.this.emojiStickers.put(var3, StickersSearchGridAdapter.this.searchQuery);
                     StickersSearchGridAdapter.this.emojiArrays.add(var3);
                  }
               }

               if (var5 != null && !var5.isEmpty() && StickersSearchGridAdapter.this.searchQuery.length() > 1) {
                  String[] var14 = AndroidUtilities.getCurrentKeyboardLanguage();
                  if (!Arrays.equals(EmojiView.this.lastSearchKeyboardLanguage, var14)) {
                     DataQuery.getInstance(EmojiView.this.currentAccount).fetchNewEmojiKeywords(var14);
                  }

                  EmojiView.this.lastSearchKeyboardLanguage = var14;
                  DataQuery.getInstance(EmojiView.this.currentAccount).getEmojiSuggestions(EmojiView.this.lastSearchKeyboardLanguage, StickersSearchGridAdapter.this.searchQuery, false, new DataQuery.KeywordResultCallback() {
                     public void run(ArrayList var1, String var2x) {
                        if (var2 == StickersSearchGridAdapter.this.emojiSearchId) {
                           int var3 = var1.size();
                           int var4 = 0;

                           boolean var5x;
                           boolean var7;
                           for(var5x = false; var4 < var3; var5x = var7) {
                              String var6 = ((DataQuery.KeywordResult)var1.get(var4)).emoji;
                              HashMap var8 = var5;
                              ArrayList var9;
                              if (var8 != null) {
                                 var9 = (ArrayList)var8.get(var6);
                              } else {
                                 var9 = null;
                              }

                              var7 = var5x;
                              if (var9 != null) {
                                 var7 = var5x;
                                 if (!var9.isEmpty()) {
                                    clear();
                                    var7 = var5x;
                                    if (!StickersSearchGridAdapter.this.emojiStickers.containsKey(var9)) {
                                       StickersSearchGridAdapter.this.emojiStickers.put(var9, var6);
                                       StickersSearchGridAdapter.this.emojiArrays.add(var9);
                                       var7 = true;
                                    }
                                 }
                              }

                              ++var4;
                           }

                           if (var5x) {
                              StickersSearchGridAdapter.this.notifyDataSetChanged();
                           }

                        }
                     }
                  });
               }

               var13 = DataQuery.getInstance(EmojiView.this.currentAccount).getStickerSets(0);
               var6 = var13.size();

               String var20;
               for(var7 = 0; var7 < var6; ++var7) {
                  TLRPC.TL_messages_stickerSet var16 = (TLRPC.TL_messages_stickerSet)var13.get(var7);
                  var8 = var16.set.title.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                  if (var8 >= 0) {
                     if (var8 == 0 || var16.set.title.charAt(var8 - 1) == ' ') {
                        this.clear();
                        StickersSearchGridAdapter.this.localPacks.add(var16);
                        StickersSearchGridAdapter.this.localPacksByName.put(var16, var8);
                     }
                  } else {
                     var20 = var16.set.short_name;
                     if (var20 != null) {
                        var8 = var20.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                        if (var8 >= 0 && (var8 == 0 || var16.set.short_name.charAt(var8 - 1) == ' ')) {
                           this.clear();
                           StickersSearchGridAdapter.this.localPacks.add(var16);
                           StickersSearchGridAdapter.this.localPacksByShortName.put(var16, true);
                        }
                     }
                  }
               }

               ArrayList var18 = DataQuery.getInstance(EmojiView.this.currentAccount).getStickerSets(3);
               var6 = var18.size();

               for(var7 = 0; var7 < var6; ++var7) {
                  TLRPC.TL_messages_stickerSet var15 = (TLRPC.TL_messages_stickerSet)var18.get(var7);
                  var8 = var15.set.title.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                  if (var8 >= 0) {
                     if (var8 == 0 || var15.set.title.charAt(var8 - 1) == ' ') {
                        this.clear();
                        StickersSearchGridAdapter.this.localPacks.add(var15);
                        StickersSearchGridAdapter.this.localPacksByName.put(var15, var8);
                     }
                  } else {
                     var20 = var15.set.short_name;
                     if (var20 != null) {
                        var8 = var20.toLowerCase().indexOf(StickersSearchGridAdapter.this.searchQuery);
                        if (var8 >= 0 && (var8 == 0 || var15.set.short_name.charAt(var8 - 1) == ' ')) {
                           this.clear();
                           StickersSearchGridAdapter.this.localPacks.add(var15);
                           StickersSearchGridAdapter.this.localPacksByShortName.put(var15, true);
                        }
                     }
                  }
               }

               if ((!StickersSearchGridAdapter.this.localPacks.isEmpty() || !StickersSearchGridAdapter.this.emojiStickers.isEmpty()) && EmojiView.this.stickersGridView.getAdapter() != EmojiView.this.stickersSearchGridAdapter) {
                  EmojiView.this.stickersGridView.setAdapter(EmojiView.this.stickersSearchGridAdapter);
               }

               TLRPC.TL_messages_searchStickerSets var21 = new TLRPC.TL_messages_searchStickerSets();
               var21.q = StickersSearchGridAdapter.this.searchQuery;
               var1 = StickersSearchGridAdapter.this;
               var1.reqId = ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(var21, new _$$Lambda$EmojiView$StickersSearchGridAdapter$1$PMXaBA9vcwjG4TQSEF8AuHh_dEU(this, var21));
               if (Emoji.isValidEmoji(StickersSearchGridAdapter.this.searchQuery)) {
                  TLRPC.TL_messages_getStickers var17 = new TLRPC.TL_messages_getStickers();
                  var17.emoticon = StickersSearchGridAdapter.this.searchQuery;
                  var17.hash = 0;
                  EmojiView.StickersSearchGridAdapter var22 = StickersSearchGridAdapter.this;
                  var22.reqId2 = ConnectionsManager.getInstance(EmojiView.this.currentAccount).sendRequest(var17, new _$$Lambda$EmojiView$StickersSearchGridAdapter$1$D2YaPWN88kYmZJFvtUqCfq785Bw(this, var17, var3, var4));
               }

               StickersSearchGridAdapter.this.notifyDataSetChanged();
            }
         }
      };
      private ArrayList serverPacks = new ArrayList();
      private int totalItems;

      public StickersSearchGridAdapter(Context var2) {
         this.context = var2;
      }

      // $FF: synthetic method
      static int access$13304(EmojiView.StickersSearchGridAdapter var0) {
         int var1 = var0.emojiSearchId + 1;
         var0.emojiSearchId = var1;
         return var1;
      }

      public Object getItem(int var1) {
         return this.cache.get(var1);
      }

      public int getItemCount() {
         int var1 = this.totalItems;
         return var1 != 1 ? var1 + 1 : 2;
      }

      public int getItemViewType(int var1) {
         if (var1 == 0) {
            return 4;
         } else if (var1 == 1 && this.totalItems == 1) {
            return 5;
         } else {
            Object var2 = this.cache.get(var1);
            if (var2 != null) {
               if (var2 instanceof TLRPC.Document) {
                  return 0;
               } else {
                  return var2 instanceof TLRPC.StickerSetCovered ? 3 : 2;
               }
            } else {
               return 1;
            }
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$EmojiView$StickersSearchGridAdapter(View var1) {
         FeaturedStickerSetInfoCell var3 = (FeaturedStickerSetInfoCell)var1.getParent();
         TLRPC.StickerSetCovered var2 = var3.getStickerSet();
         if (EmojiView.this.installingStickerSets.indexOfKey(var2.set.id) < 0 && EmojiView.this.removingStickerSets.indexOfKey(var2.set.id) < 0) {
            if (var3.isInstalled()) {
               EmojiView.this.removingStickerSets.put(var2.set.id, var2);
               EmojiView.this.delegate.onStickerSetRemove(var3.getStickerSet());
            } else {
               EmojiView.this.installingStickerSets.put(var2.set.id, var2);
               EmojiView.this.delegate.onStickerSetAdd(var3.getStickerSet());
            }

            var3.setDrawProgress(true);
         }

      }

      public void notifyDataSetChanged() {
         this.rowStartPack.clear();
         this.positionToRow.clear();
         this.cache.clear();
         this.positionsToSets.clear();
         this.positionToEmoji.clear();
         this.totalItems = 0;
         int var1 = this.serverPacks.size();
         int var2 = this.localPacks.size();
         int var3 = this.emojiArrays.isEmpty() ^ 1;
         int var4 = -1;

         int var7;
         for(int var5 = 0; var4 < var1 + var2 + var3; var5 = var7) {
            if (var4 == -1) {
               SparseArray var18 = this.cache;
               var7 = this.totalItems++;
               var18.put(var7, "search");
               var7 = var5 + 1;
            } else {
               label106: {
                  ArrayList var6;
                  Object var8;
                  int var9;
                  int var10;
                  int var11;
                  TLRPC.Document var12;
                  int var14;
                  int var15;
                  if (var4 < var2) {
                     var8 = (TLRPC.TL_messages_stickerSet)this.localPacks.get(var4);
                     var6 = ((TLRPC.TL_messages_stickerSet)var8).documents;
                  } else {
                     var7 = var4 - var2;
                     if (var7 < var3) {
                        var9 = this.emojiArrays.size();
                        String var19 = "";
                        var10 = 0;

                        String var17;
                        for(var7 = 0; var10 < var9; var19 = var17) {
                           ArrayList var21 = (ArrayList)this.emojiArrays.get(var10);
                           String var13 = (String)this.emojiStickers.get(var21);
                           var17 = var19;
                           if (var13 != null) {
                              var17 = var19;
                              if (!var19.equals(var13)) {
                                 this.positionToEmoji.put(this.totalItems + var7, var13);
                                 var17 = var13;
                              }
                           }

                           var14 = var21.size();
                           var1 = var7;
                           var7 = 0;

                           for(ArrayList var20 = var21; var7 < var14; ++var7) {
                              var15 = this.totalItems + var1;
                              int var16 = var1 / EmojiView.this.stickersGridAdapter.stickersPerRow;
                              var12 = (TLRPC.Document)var20.get(var7);
                              this.cache.put(var15, var12);
                              TLRPC.TL_messages_stickerSet var22 = DataQuery.getInstance(EmojiView.this.currentAccount).getStickerSetById(DataQuery.getStickerSetId(var12));
                              if (var22 != null) {
                                 this.cacheParent.put(var15, var22);
                              }

                              this.positionToRow.put(var15, var16 + var5);
                              ++var1;
                           }

                           ++var10;
                           var7 = var1;
                        }

                        var1 = var1;
                        var10 = (int)Math.ceil((double)((float)var7 / (float)EmojiView.this.stickersGridAdapter.stickersPerRow));

                        for(var11 = 0; var11 < var10; ++var11) {
                           this.rowStartPack.put(var5 + var11, var7);
                        }

                        this.totalItems += EmojiView.this.stickersGridAdapter.stickersPerRow * var10;
                        var7 = var5 + var10;
                        break label106;
                     }

                     var8 = (TLRPC.StickerSetCovered)this.serverPacks.get(var7 - var3);
                     var6 = ((TLRPC.StickerSetCovered)var8).covers;
                  }

                  if (var6.isEmpty()) {
                     var7 = var5;
                  } else {
                     var10 = (int)Math.ceil((double)((float)var6.size() / (float)EmojiView.this.stickersGridAdapter.stickersPerRow));
                     this.cache.put(this.totalItems, var8);
                     if (var4 >= var2 && var8 instanceof TLRPC.StickerSetCovered) {
                        this.positionsToSets.put(this.totalItems, (TLRPC.StickerSetCovered)var8);
                     }

                     this.positionToRow.put(this.totalItems, var5);
                     var9 = var6.size();

                     for(var7 = 0; var7 < var9; var7 = var11) {
                        var11 = var7 + 1;
                        var15 = this.totalItems + var11;
                        var14 = var7 / EmojiView.this.stickersGridAdapter.stickersPerRow;
                        var12 = (TLRPC.Document)var6.get(var7);
                        this.cache.put(var15, var12);
                        if (var8 != null) {
                           this.cacheParent.put(var15, var8);
                        }

                        this.positionToRow.put(var15, var5 + 1 + var14);
                        if (var4 >= var2 && var8 instanceof TLRPC.StickerSetCovered) {
                           this.positionsToSets.put(var15, (TLRPC.StickerSetCovered)var8);
                        }
                     }

                     var11 = var10 + 1;

                     for(var7 = 0; var7 < var11; ++var7) {
                        this.rowStartPack.put(var5 + var7, var8);
                     }

                     this.totalItems += var10 * EmojiView.this.stickersGridAdapter.stickersPerRow + 1;
                     var7 = var5 + var11;
                  }
               }
            }

            ++var4;
         }

         super.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = true;
         boolean var5 = true;
         boolean var9;
         if (var3 != 0) {
            TLRPC.StickerSetCovered var6 = null;
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 == 3) {
                     var6 = (TLRPC.StickerSetCovered)this.cache.get(var2);
                     FeaturedStickerSetInfoCell var12 = (FeaturedStickerSetInfoCell)var1.itemView;
                     boolean var15;
                     if (EmojiView.this.installingStickerSets.indexOfKey(var6.set.id) >= 0) {
                        var15 = true;
                     } else {
                        var15 = false;
                     }

                     boolean var17;
                     if (EmojiView.this.removingStickerSets.indexOfKey(var6.set.id) >= 0) {
                        var17 = true;
                     } else {
                        var17 = false;
                     }

                     boolean var7;
                     boolean var8;
                     label122: {
                        if (!var15) {
                           var7 = var15;
                           var8 = var17;
                           if (!var17) {
                              break label122;
                           }
                        }

                        if (var15 && var12.isInstalled()) {
                           EmojiView.this.installingStickerSets.remove(var6.set.id);
                           var7 = false;
                           var8 = var17;
                        } else {
                           var7 = var15;
                           var8 = var17;
                           if (var17) {
                              var7 = var15;
                              var8 = var17;
                              if (!var12.isInstalled()) {
                                 EmojiView.this.removingStickerSets.remove(var6.set.id);
                                 var8 = false;
                                 var7 = var15;
                              }
                           }
                        }
                     }

                     var9 = var5;
                     if (!var7) {
                        if (var8) {
                           var9 = var5;
                        } else {
                           var9 = false;
                        }
                     }

                     var12.setDrawProgress(var9);
                     if (TextUtils.isEmpty(this.searchQuery)) {
                        var2 = -1;
                     } else {
                        var2 = var6.set.title.toLowerCase().indexOf(this.searchQuery);
                     }

                     if (var2 >= 0) {
                        var12.setStickerSet(var6, false, var2, this.searchQuery.length());
                     } else {
                        var12.setStickerSet(var6, false);
                        if (!TextUtils.isEmpty(this.searchQuery) && var6.set.short_name.toLowerCase().startsWith(this.searchQuery)) {
                           var12.setUrl(var6.set.short_name, this.searchQuery.length());
                        }
                     }
                  }
               } else {
                  StickerSetNameCell var13 = (StickerSetNameCell)var1.itemView;
                  Object var18 = this.cache.get(var2);
                  if (var18 instanceof TLRPC.TL_messages_stickerSet) {
                     TLRPC.TL_messages_stickerSet var10 = (TLRPC.TL_messages_stickerSet)var18;
                     if (!TextUtils.isEmpty(this.searchQuery) && this.localPacksByShortName.containsKey(var10)) {
                        TLRPC.StickerSet var20 = var10.set;
                        if (var20 != null) {
                           var13.setText(var20.title, 0);
                        }

                        var13.setUrl(var10.set.short_name, this.searchQuery.length());
                     } else {
                        Integer var19 = (Integer)this.localPacksByName.get(var10);
                        TLRPC.StickerSet var22 = var10.set;
                        if (var22 != null && var19 != null) {
                           String var23 = var22.title;
                           var3 = var19;
                           if (!TextUtils.isEmpty(this.searchQuery)) {
                              var2 = this.searchQuery.length();
                           } else {
                              var2 = 0;
                           }

                           var13.setText(var23, 0, var3, var2);
                        }

                        var13.setUrl((CharSequence)null, 0);
                     }
                  }
               }
            } else {
               EmptyCell var24 = (EmptyCell)var1.itemView;
               if (var2 == this.totalItems) {
                  var2 = this.positionToRow.get(var2 - 1, Integer.MIN_VALUE);
                  if (var2 == Integer.MIN_VALUE) {
                     var24.setHeight(1);
                  } else {
                     Object var11 = this.rowStartPack.get(var2);
                     Integer var14;
                     if (var11 instanceof TLRPC.TL_messages_stickerSet) {
                        var14 = ((TLRPC.TL_messages_stickerSet)var11).documents.size();
                     } else {
                        var14 = var6;
                        if (var11 instanceof Integer) {
                           var14 = (Integer)var11;
                        }
                     }

                     if (var14 == null) {
                        var24.setHeight(1);
                     } else if (var14 == 0) {
                        var24.setHeight(AndroidUtilities.dp(8.0F));
                     } else {
                        var2 = EmojiView.this.pager.getHeight() - (int)Math.ceil((double)((float)var14 / (float)EmojiView.this.stickersGridAdapter.stickersPerRow)) * AndroidUtilities.dp(82.0F);
                        if (var2 <= 0) {
                           var2 = 1;
                        }

                        var24.setHeight(var2);
                     }
                  }
               } else {
                  var24.setHeight(AndroidUtilities.dp(82.0F));
               }
            }
         } else {
            TLRPC.Document var21 = (TLRPC.Document)this.cache.get(var2);
            StickerEmojiCell var16 = (StickerEmojiCell)var1.itemView;
            var16.setSticker(var21, this.cacheParent.get(var2), (String)this.positionToEmoji.get(var2), false);
            var9 = var4;
            if (!EmojiView.this.recentStickers.contains(var21)) {
               if (EmojiView.this.favouriteStickers.contains(var21)) {
                  var9 = var4;
               } else {
                  var9 = false;
               }
            }

            var16.setRecent(var9);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var4;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        if (var2 != 5) {
                           var4 = null;
                        } else {
                           var4 = new FrameLayout(this.context) {
                              protected void onMeasure(int var1, int var2) {
                                 super.onMeasure(var1, MeasureSpec.makeMeasureSpec((int)((float)((EmojiView.this.stickersGridView.getMeasuredHeight() - EmojiView.this.searchFieldHeight - AndroidUtilities.dp(8.0F)) / 3) * 1.7F), 1073741824));
                              }
                           };
                           ImageView var3 = new ImageView(this.context);
                           var3.setScaleType(ScaleType.CENTER);
                           var3.setImageResource(2131165864);
                           var3.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_emojiPanelEmptyText"), Mode.MULTIPLY));
                           ((FrameLayout)var4).addView(var3, LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, 0.0F, 0.0F, 59.0F));
                           TextView var5 = new TextView(this.context);
                           var5.setText(LocaleController.getString("NoStickersFound", 2131559954));
                           var5.setTextSize(1, 16.0F);
                           var5.setTextColor(Theme.getColor("chat_emojiPanelEmptyText"));
                           ((FrameLayout)var4).addView(var5, LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, 0.0F, 0.0F, 9.0F));
                           ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
                        }
                     } else {
                        var4 = new View(this.context);
                        ((View)var4).setLayoutParams(new RecyclerView.LayoutParams(-1, EmojiView.this.searchFieldHeight));
                     }
                  } else {
                     var4 = new FeaturedStickerSetInfoCell(this.context, 17);
                     ((FeaturedStickerSetInfoCell)var4).setAddOnClickListener(new _$$Lambda$EmojiView$StickersSearchGridAdapter$An1o7aFGx9Hb6YuxBcvVH4f8K_M(this));
                  }
               } else {
                  var4 = new StickerSetNameCell(this.context, false);
               }
            } else {
               var4 = new EmptyCell(this.context);
            }
         } else {
            var4 = new StickerEmojiCell(this.context) {
               public void onMeasure(int var1, int var2) {
                  super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0F), 1073741824));
               }
            };
         }

         return new RecyclerListView.Holder((View)var4);
      }

      public void search(String var1) {
         if (this.reqId != 0) {
            ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId, true);
            this.reqId = 0;
         }

         if (this.reqId2 != 0) {
            ConnectionsManager.getInstance(EmojiView.this.currentAccount).cancelRequest(this.reqId2, true);
            this.reqId2 = 0;
         }

         if (TextUtils.isEmpty(var1)) {
            this.searchQuery = null;
            this.localPacks.clear();
            this.emojiStickers.clear();
            this.serverPacks.clear();
            if (EmojiView.this.stickersGridView.getAdapter() != EmojiView.this.stickersGridAdapter) {
               EmojiView.this.stickersGridView.setAdapter(EmojiView.this.stickersGridAdapter);
            }

            this.notifyDataSetChanged();
         } else {
            this.searchQuery = var1.toLowerCase();
         }

         AndroidUtilities.cancelRunOnUIThread(this.searchRunnable);
         AndroidUtilities.runOnUIThread(this.searchRunnable, 300L);
      }
   }

   private class TrendingGridAdapter extends RecyclerListView.SelectionAdapter {
      private SparseArray cache = new SparseArray();
      private Context context;
      private SparseArray positionsToSets = new SparseArray();
      private ArrayList sets = new ArrayList();
      private int stickersPerRow;
      private int totalItems;

      public TrendingGridAdapter(Context var2) {
         this.context = var2;
      }

      public Object getItem(int var1) {
         return this.cache.get(var1);
      }

      public int getItemCount() {
         return this.totalItems;
      }

      public int getItemViewType(int var1) {
         Object var2 = this.cache.get(var1);
         if (var2 != null) {
            return var2 instanceof TLRPC.Document ? 0 : 2;
         } else {
            return 1;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$EmojiView$TrendingGridAdapter(View var1) {
         FeaturedStickerSetInfoCell var3 = (FeaturedStickerSetInfoCell)var1.getParent();
         TLRPC.StickerSetCovered var2 = var3.getStickerSet();
         if (EmojiView.this.installingStickerSets.indexOfKey(var2.set.id) < 0 && EmojiView.this.removingStickerSets.indexOfKey(var2.set.id) < 0) {
            if (var3.isInstalled()) {
               EmojiView.this.removingStickerSets.put(var2.set.id, var2);
               EmojiView.this.delegate.onStickerSetRemove(var3.getStickerSet());
            } else {
               EmojiView.this.installingStickerSets.put(var2.set.id, var2);
               EmojiView.this.delegate.onStickerSetAdd(var3.getStickerSet());
            }

            var3.setDrawProgress(true);
         }

      }

      public void notifyDataSetChanged() {
         int var1 = EmojiView.this.getMeasuredWidth();
         int var2 = var1;
         int var3;
         if (var1 == 0) {
            if (AndroidUtilities.isTablet()) {
               var3 = AndroidUtilities.displaySize.x;
               var1 = var3 * 35 / 100;
               var2 = var1;
               if (var1 < AndroidUtilities.dp(320.0F)) {
                  var2 = AndroidUtilities.dp(320.0F);
               }

               var1 = var3 - var2;
            } else {
               var1 = AndroidUtilities.displaySize.x;
            }

            var2 = var1;
            if (var1 == 0) {
               var2 = 1080;
            }
         }

         this.stickersPerRow = Math.max(1, var2 / AndroidUtilities.dp(72.0F));
         EmojiView.this.trendingLayoutManager.setSpanCount(this.stickersPerRow);
         if (!EmojiView.this.trendingLoaded) {
            this.cache.clear();
            this.positionsToSets.clear();
            this.sets.clear();
            this.totalItems = 0;
            ArrayList var4 = DataQuery.getInstance(EmojiView.this.currentAccount).getFeaturedStickerSets();
            var2 = 0;

            for(var1 = 0; var2 < var4.size(); var1 = var3) {
               TLRPC.StickerSetCovered var5 = (TLRPC.StickerSetCovered)var4.get(var2);
               var3 = var1;
               if (!DataQuery.getInstance(EmojiView.this.currentAccount).isStickerPackInstalled(var5.set.id)) {
                  if (var5.covers.isEmpty() && var5.cover == null) {
                     var3 = var1;
                  } else {
                     this.sets.add(var5);
                     this.positionsToSets.put(this.totalItems, var5);
                     SparseArray var6 = this.cache;
                     var3 = this.totalItems++;
                     var6.put(var3, var1);
                     int var10000 = this.totalItems / this.stickersPerRow;
                     int var7;
                     int var8;
                     if (!var5.covers.isEmpty()) {
                        var7 = (int)Math.ceil((double)((float)var5.covers.size() / (float)this.stickersPerRow));
                        var8 = 0;

                        while(true) {
                           var3 = var7;
                           if (var8 >= var5.covers.size()) {
                              break;
                           }

                           this.cache.put(this.totalItems + var8, var5.covers.get(var8));
                           ++var8;
                        }
                     } else {
                        this.cache.put(this.totalItems, var5.cover);
                        var3 = 1;
                     }

                     var8 = 0;

                     while(true) {
                        var7 = this.stickersPerRow;
                        if (var8 >= var3 * var7) {
                           this.totalItems += var3 * var7;
                           var3 = var1 + 1;
                           break;
                        }

                        this.positionsToSets.put(this.totalItems + var8, var5);
                        ++var8;
                     }
                  }
               }

               ++var2;
            }

            if (this.totalItems != 0) {
               EmojiView.this.trendingLoaded = true;
               EmojiView var9 = EmojiView.this;
               var9.featuredStickersHash = DataQuery.getInstance(var9.currentAccount).getFeaturesStickersHashWithoutUnread();
            }

            super.notifyDataSetChanged();
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  ArrayList var5 = DataQuery.getInstance(EmojiView.this.currentAccount).getUnreadStickerSets();
                  TLRPC.StickerSetCovered var6 = (TLRPC.StickerSetCovered)this.sets.get((Integer)this.cache.get(var2));
                  boolean var7;
                  if (var5 != null && var5.contains(var6.set.id)) {
                     var7 = true;
                  } else {
                     var7 = false;
                  }

                  FeaturedStickerSetInfoCell var10 = (FeaturedStickerSetInfoCell)var1.itemView;
                  var10.setStickerSet(var6, var7);
                  if (var7) {
                     DataQuery.getInstance(EmojiView.this.currentAccount).markFaturedStickersByIdAsRead(var6.set.id);
                  }

                  boolean var11;
                  if (EmojiView.this.installingStickerSets.indexOfKey(var6.set.id) >= 0) {
                     var11 = true;
                  } else {
                     var11 = false;
                  }

                  boolean var12;
                  if (EmojiView.this.removingStickerSets.indexOfKey(var6.set.id) >= 0) {
                     var12 = true;
                  } else {
                     var12 = false;
                  }

                  boolean var8;
                  boolean var9;
                  label66: {
                     if (!var11) {
                        var8 = var11;
                        var9 = var12;
                        if (!var12) {
                           break label66;
                        }
                     }

                     if (var11 && var10.isInstalled()) {
                        EmojiView.this.installingStickerSets.remove(var6.set.id);
                        var8 = false;
                        var9 = var12;
                     } else {
                        var8 = var11;
                        var9 = var12;
                        if (var12) {
                           var8 = var11;
                           var9 = var12;
                           if (!var10.isInstalled()) {
                              EmojiView.this.removingStickerSets.remove(var6.set.id);
                              var9 = false;
                              var8 = var11;
                           }
                        }
                     }
                  }

                  label43: {
                     if (!var8) {
                        var7 = var4;
                        if (!var9) {
                           break label43;
                        }
                     }

                     var7 = true;
                  }

                  var10.setDrawProgress(var7);
               }
            } else {
               ((EmptyCell)var1.itemView).setHeight(AndroidUtilities.dp(82.0F));
            }
         } else {
            TLRPC.Document var13 = (TLRPC.Document)this.cache.get(var2);
            ((StickerEmojiCell)var1.itemView).setSticker(var13, this.positionsToSets.get(var2), false);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  var3 = null;
               } else {
                  var3 = new FeaturedStickerSetInfoCell(this.context, 17);
                  ((FeaturedStickerSetInfoCell)var3).setAddOnClickListener(new _$$Lambda$EmojiView$TrendingGridAdapter$KqfE7v9vPbOMyNkHB4d4a59Ij7c(this));
               }
            } else {
               var3 = new EmptyCell(this.context);
            }
         } else {
            var3 = new StickerEmojiCell(this.context) {
               public void onMeasure(int var1, int var2) {
                  super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0F), 1073741824));
               }
            };
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}
