package org.telegram.ui;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.style.CharacterStyle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.View.MeasureSpec;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.MenuDrawable;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatActionCell$ChatActionCellDelegate$_CC;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.ChatMessageCell$ChatMessageCellDelegate$_CC;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;

public class ThemePreviewActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private ActionBar actionBar2;
   private boolean applied;
   private Theme.ThemeInfo applyingTheme;
   private ThemePreviewActivity.DialogsAdapter dialogsAdapter;
   private View dotsContainer;
   private ImageView floatingButton;
   private RecyclerListView listView;
   private RecyclerListView listView2;
   private ThemePreviewActivity.MessagesAdapter messagesAdapter;
   private FrameLayout page1;
   private SizeNotifierFrameLayout page2;
   private File themeFile;

   public ThemePreviewActivity(File var1, Theme.ThemeInfo var2) {
      super.swipeBackEnabled = false;
      this.applyingTheme = var2;
      this.themeFile = var1;
   }

   // $FF: synthetic method
   static ActionBar access$000(ThemePreviewActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$100(ThemePreviewActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$1200(ThemePreviewActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBarLayout access$1300(ThemePreviewActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static int access$1800(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1900(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$200(ThemePreviewActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static int access$2000(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2100(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2200(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2300(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2400(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2500(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2600(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2700(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2800(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2900(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3000(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3100(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3200(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3300(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3400(ThemePreviewActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static ActionBar access$500(ThemePreviewActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$600(ThemePreviewActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static ActionBar access$700(ThemePreviewActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBar access$800(ThemePreviewActivity var0) {
      return var0.actionBar;
   }

   // $FF: synthetic method
   static ActionBarLayout access$900(ThemePreviewActivity var0) {
      return var0.parentLayout;
   }

   public View createView(Context var1) {
      this.page1 = new FrameLayout(var1);
      super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
         public boolean canCollapseSearch() {
            return true;
         }

         public void onSearchCollapse() {
         }

         public void onSearchExpand() {
         }

         public void onTextChanged(EditText var1) {
         }
      }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      super.actionBar.setBackButtonDrawable(new MenuDrawable());
      super.actionBar.setAddToContainer(false);
      super.actionBar.setTitle(LocaleController.getString("ThemePreview", 2131560896));
      this.page1 = new FrameLayout(var1) {
         protected boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = super.drawChild(var1, var2, var3);
            if (var2 == ThemePreviewActivity.access$500(ThemePreviewActivity.this) && ThemePreviewActivity.access$600(ThemePreviewActivity.this) != null) {
               ActionBarLayout var7 = ThemePreviewActivity.access$900(ThemePreviewActivity.this);
               int var6;
               if (ThemePreviewActivity.access$700(ThemePreviewActivity.this).getVisibility() == 0) {
                  var6 = ThemePreviewActivity.access$800(ThemePreviewActivity.this).getMeasuredHeight();
               } else {
                  var6 = 0;
               }

               var7.drawHeaderShadow(var1, var6);
            }

            return var5;
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var1);
            int var4 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(var3, var4);
            this.measureChildWithMargins(ThemePreviewActivity.access$000(ThemePreviewActivity.this), var1, 0, var2, 0);
            int var5 = ThemePreviewActivity.access$100(ThemePreviewActivity.this).getMeasuredHeight();
            int var6 = var4;
            if (ThemePreviewActivity.access$200(ThemePreviewActivity.this).getVisibility() == 0) {
               var6 = var4 - var5;
            }

            ((LayoutParams)ThemePreviewActivity.this.listView.getLayoutParams()).topMargin = var5;
            ThemePreviewActivity.this.listView.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var6, 1073741824));
            this.measureChildWithMargins(ThemePreviewActivity.this.floatingButton, var1, 0, var2, 0);
         }
      };
      this.page1.addView(super.actionBar, LayoutHelper.createFrame(-1, -2.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(true);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      RecyclerListView var2 = this.listView;
      boolean var3 = LocaleController.isRTL;
      byte var4 = 2;
      byte var5;
      if (var3) {
         var5 = 1;
      } else {
         var5 = 2;
      }

      var2.setVerticalScrollbarPosition(var5);
      this.page1.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.floatingButton = new ImageView(var1);
      this.floatingButton.setScaleType(ScaleType.CENTER);
      Drawable var6 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0F), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
      Object var13 = var6;
      if (VERSION.SDK_INT < 21) {
         Drawable var14 = var1.getResources().getDrawable(2131165387).mutate();
         var14.setColorFilter(new PorterDuffColorFilter(-16777216, Mode.MULTIPLY));
         var13 = new CombinedDrawable(var14, var6, 0, 0);
         ((CombinedDrawable)var13).setIconSize(AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
      }

      this.floatingButton.setBackgroundDrawable((Drawable)var13);
      this.floatingButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_actionIcon"), Mode.MULTIPLY));
      this.floatingButton.setImageResource(2131165386);
      if (VERSION.SDK_INT >= 21) {
         StateListAnimator var15 = new StateListAnimator();
         ObjectAnimator var18 = ObjectAnimator.ofFloat(this.floatingButton, "translationZ", new float[]{(float)AndroidUtilities.dp(2.0F), (float)AndroidUtilities.dp(4.0F)}).setDuration(200L);
         var15.addState(new int[]{16842919}, var18);
         var18 = ObjectAnimator.ofFloat(this.floatingButton, "translationZ", new float[]{(float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(2.0F)}).setDuration(200L);
         var15.addState(new int[0], var18);
         this.floatingButton.setStateListAnimator(var15);
         this.floatingButton.setOutlineProvider(new ViewOutlineProvider() {
            @SuppressLint({"NewApi"})
            public void getOutline(View var1, Outline var2) {
               var2.setOval(0, 0, AndroidUtilities.dp(56.0F), AndroidUtilities.dp(56.0F));
            }
         });
      }

      FrameLayout var16 = this.page1;
      ImageView var19 = this.floatingButton;
      if (VERSION.SDK_INT >= 21) {
         var5 = 56;
      } else {
         var5 = 60;
      }

      float var7;
      if (VERSION.SDK_INT >= 21) {
         var7 = 56.0F;
      } else {
         var7 = 60.0F;
      }

      byte var8;
      if (LocaleController.isRTL) {
         var8 = 3;
      } else {
         var8 = 5;
      }

      float var9;
      if (LocaleController.isRTL) {
         var9 = 14.0F;
      } else {
         var9 = 0.0F;
      }

      float var10;
      if (LocaleController.isRTL) {
         var10 = 0.0F;
      } else {
         var10 = 14.0F;
      }

      var16.addView(var19, LayoutHelper.createFrame(var5, var7, var8 | 80, var9, 0.0F, var10, 14.0F));
      this.dialogsAdapter = new ThemePreviewActivity.DialogsAdapter(var1);
      this.listView.setAdapter(this.dialogsAdapter);
      this.page2 = new SizeNotifierFrameLayout(var1) {
         protected boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = super.drawChild(var1, var2, var3);
            if (var2 == ThemePreviewActivity.this.actionBar2 && ThemePreviewActivity.access$1200(ThemePreviewActivity.this) != null) {
               ActionBarLayout var7 = ThemePreviewActivity.access$1300(ThemePreviewActivity.this);
               int var6;
               if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
                  var6 = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
               } else {
                  var6 = 0;
               }

               var7.drawHeaderShadow(var1, var6);
            }

            return var5;
         }

         protected void onMeasure(int var1, int var2) {
            int var3 = MeasureSpec.getSize(var1);
            int var4 = MeasureSpec.getSize(var2);
            this.setMeasuredDimension(var3, var4);
            this.measureChildWithMargins(ThemePreviewActivity.this.actionBar2, var1, 0, var2, 0);
            var2 = ThemePreviewActivity.this.actionBar2.getMeasuredHeight();
            var1 = var4;
            if (ThemePreviewActivity.this.actionBar2.getVisibility() == 0) {
               var1 = var4 - var2;
            }

            ((LayoutParams)ThemePreviewActivity.this.listView2.getLayoutParams()).topMargin = var2;
            ThemePreviewActivity.this.listView2.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var1, 1073741824));
         }
      };
      this.page2.setBackgroundImage(Theme.getCachedWallpaper(), Theme.isWallpaperMotion());
      this.messagesAdapter = new ThemePreviewActivity.MessagesAdapter(var1);
      this.actionBar2 = this.createActionBar(var1);
      this.actionBar2.setBackButtonDrawable(new BackDrawable(false));
      if (this.messagesAdapter.showSecretMessages) {
         this.actionBar2.setTitle("Telegram Beta Chat");
         this.actionBar2.setSubtitle(LocaleController.formatPluralString("Members", 505));
      } else {
         this.actionBar2.setTitle("Reinhardt");
         this.actionBar2.setSubtitle(LocaleController.formatDateOnline(System.currentTimeMillis() / 1000L - 3600L));
      }

      this.page2.addView(this.actionBar2, LayoutHelper.createFrame(-1, -2.0F));
      this.listView2 = new RecyclerListView(var1) {
         public boolean drawChild(Canvas var1, View var2, long var3) {
            boolean var5 = super.drawChild(var1, var2, var3);
            if (var2 instanceof ChatMessageCell) {
               ChatMessageCell var6 = (ChatMessageCell)var2;
               var6.getMessageObject();
               ImageReceiver var7 = var6.getAvatarImage();
               if (var7 != null) {
                  int var8 = var2.getTop();
                  int var10;
                  if (var6.isPinnedBottom()) {
                     RecyclerView.ViewHolder var9 = ThemePreviewActivity.this.listView2.getChildViewHolder(var2);
                     if (var9 != null) {
                        var10 = var9.getAdapterPosition();
                        if (ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(var10 - 1) != null) {
                           var7.setImageY(-AndroidUtilities.dp(1000.0F));
                           var7.draw(var1);
                           return var5;
                        }
                     }
                  }

                  float var11 = var6.getTranslationX();
                  int var12 = var2.getTop() + var6.getLayoutHeight();
                  int var13 = ThemePreviewActivity.this.listView2.getMeasuredHeight() - ThemePreviewActivity.this.listView2.getPaddingBottom();
                  var10 = var12;
                  if (var12 > var13) {
                     var10 = var13;
                  }

                  var13 = var8;
                  float var14 = var11;
                  if (var6.isPinnedTop()) {
                     RecyclerView.ViewHolder var16 = ThemePreviewActivity.this.listView2.getChildViewHolder(var2);
                     var13 = var8;
                     var14 = var11;
                     if (var16 != null) {
                        var12 = 0;
                        var13 = var8;

                        while(true) {
                           if (var12 >= 20) {
                              var14 = var11;
                              break;
                           }

                           ++var12;
                           var8 = var16.getAdapterPosition();
                           var16 = ThemePreviewActivity.this.listView2.findViewHolderForAdapterPosition(var8 + 1);
                           var14 = var11;
                           if (var16 == null) {
                              break;
                           }

                           var8 = var16.itemView.getTop();
                           float var15 = var11;
                           if (var10 - AndroidUtilities.dp(48.0F) < var16.itemView.getBottom()) {
                              var15 = Math.min(var16.itemView.getTranslationX(), var11);
                           }

                           View var17 = var16.itemView;
                           var13 = var8;
                           var14 = var15;
                           if (!(var17 instanceof ChatMessageCell)) {
                              break;
                           }

                           var13 = var8;
                           var11 = var15;
                           if (!((ChatMessageCell)var17).isPinnedTop()) {
                              var14 = var15;
                              var13 = var8;
                              break;
                           }
                        }
                     }
                  }

                  var8 = var10;
                  if (var10 - AndroidUtilities.dp(48.0F) < var13) {
                     var8 = var13 + AndroidUtilities.dp(48.0F);
                  }

                  if (var14 != 0.0F) {
                     var1.save();
                     var1.translate(var14, 0.0F);
                  }

                  var7.setImageY(var8 - AndroidUtilities.dp(44.0F));
                  var7.draw(var1);
                  if (var14 != 0.0F) {
                     var1.restore();
                  }
               }
            }

            return var5;
         }
      };
      this.listView2.setVerticalScrollBarEnabled(true);
      this.listView2.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView2.setLayoutAnimation((LayoutAnimationController)null);
      this.listView2.setPadding(0, AndroidUtilities.dp(4.0F), 0, AndroidUtilities.dp(4.0F));
      this.listView2.setClipToPadding(false);
      this.listView2.setLayoutManager(new LinearLayoutManager(var1, 1, true));
      var2 = this.listView2;
      var5 = var4;
      if (LocaleController.isRTL) {
         var5 = 1;
      }

      var2.setVerticalScrollbarPosition(var5);
      this.page2.addView(this.listView2, LayoutHelper.createFrame(-1, -1, 51));
      this.listView2.setAdapter(this.messagesAdapter);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var20 = (FrameLayout)super.fragmentView;
      final ViewPager var11 = new ViewPager(var1);
      var11.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         public void onPageScrollStateChanged(int var1) {
         }

         public void onPageScrolled(int var1, float var2, int var3) {
         }

         public void onPageSelected(int var1) {
            ThemePreviewActivity.this.dotsContainer.invalidate();
         }
      });
      var11.setAdapter(new PagerAdapter() {
         public void destroyItem(ViewGroup var1, int var2, Object var3) {
            var1.removeView((View)var3);
         }

         public int getCount() {
            return 2;
         }

         public int getItemPosition(Object var1) {
            return -1;
         }

         public Object instantiateItem(ViewGroup var1, int var2) {
            Object var3;
            if (var2 == 0) {
               var3 = ThemePreviewActivity.this.page1;
            } else {
               var3 = ThemePreviewActivity.this.page2;
            }

            var1.addView((View)var3);
            return var3;
         }

         public boolean isViewFromObject(View var1, Object var2) {
            boolean var3;
            if (var2 == var1) {
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
      });
      AndroidUtilities.setViewPagerEdgeEffectColor(var11, Theme.getColor("actionBarDefault"));
      var20.addView(var11, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
      View var17 = new View(var1);
      var17.setBackgroundResource(2131165408);
      var20.addView(var17, LayoutHelper.createFrame(-1, 3.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
      var16 = new FrameLayout(var1);
      var16.setBackgroundColor(-1);
      var20.addView(var16, LayoutHelper.createFrame(-1, 48, 83));
      this.dotsContainer = new View(var1) {
         private Paint paint = new Paint(1);

         protected void onDraw(Canvas var1) {
            int var2 = var11.getCurrentItem();

            for(int var3 = 0; var3 < 2; ++var3) {
               Paint var4 = this.paint;
               int var5;
               if (var3 == var2) {
                  var5 = -6710887;
               } else {
                  var5 = -3355444;
               }

               var4.setColor(var5);
               var1.drawCircle((float)AndroidUtilities.dp((float)(var3 * 15 + 3)), (float)AndroidUtilities.dp(4.0F), (float)AndroidUtilities.dp(3.0F), this.paint);
            }

         }
      };
      var16.addView(this.dotsContainer, LayoutHelper.createFrame(22, 8, 17));
      TextView var21 = new TextView(var1);
      var21.setTextSize(1, 14.0F);
      var21.setTextColor(-15095832);
      var21.setGravity(17);
      var21.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
      var21.setPadding(AndroidUtilities.dp(29.0F), 0, AndroidUtilities.dp(29.0F), 0);
      var21.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
      var21.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var16.addView(var21, LayoutHelper.createFrame(-2, -1, 51));
      var21.setOnClickListener(new _$$Lambda$ThemePreviewActivity$VGx4sgeoasvuNaWlUta0WpzoZKY(this));
      TextView var12 = new TextView(var1);
      var12.setTextSize(1, 14.0F);
      var12.setTextColor(-15095832);
      var12.setGravity(17);
      var12.setBackgroundDrawable(Theme.createSelectorDrawable(788529152, 0));
      var12.setPadding(AndroidUtilities.dp(29.0F), 0, AndroidUtilities.dp(29.0F), 0);
      var12.setText(LocaleController.getString("ApplyTheme", 2131558639).toUpperCase());
      var12.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      var16.addView(var12, LayoutHelper.createFrame(-2, -1, 53));
      var12.setOnClickListener(new _$$Lambda$ThemePreviewActivity$YPhE_lyYHwkD5fNafYq_tQN7oD0(this));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.emojiDidLoad) {
         RecyclerListView var4 = this.listView;
         if (var4 == null) {
            return;
         }

         var2 = var4.getChildCount();

         for(var1 = 0; var1 < var2; ++var1) {
            View var5 = this.listView.getChildAt(var1);
            if (var5 instanceof DialogCell) {
               ((DialogCell)var5).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$createView$0$ThemePreviewActivity(View var1) {
      Theme.applyPreviousTheme();
      super.parentLayout.rebuildAllFragmentViews(false, false);
      this.finishFragment();
   }

   // $FF: synthetic method
   public void lambda$createView$1$ThemePreviewActivity(View var1) {
      this.applied = true;
      super.parentLayout.rebuildAllFragmentViews(false, false);
      Theme.applyThemeFile(this.themeFile, this.applyingTheme.name, false);
      this.finishFragment();
   }

   public boolean onBackPressed() {
      Theme.applyPreviousTheme();
      super.parentLayout.rebuildAllFragmentViews(false, false);
      return super.onBackPressed();
   }

   public boolean onFragmentCreate() {
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoad);
      return super.onFragmentCreate();
   }

   public void onFragmentDestroy() {
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoad);
      super.onFragmentDestroy();
   }

   public void onPause() {
      super.onPause();
      SizeNotifierFrameLayout var1 = this.page2;
      if (var1 != null) {
         var1.onResume();
      }

   }

   public void onResume() {
      super.onResume();
      ThemePreviewActivity.DialogsAdapter var1 = this.dialogsAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      ThemePreviewActivity.MessagesAdapter var2 = this.messagesAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

      SizeNotifierFrameLayout var3 = this.page2;
      if (var3 != null) {
         var3.onResume();
      }

   }

   public class DialogsAdapter extends RecyclerListView.SelectionAdapter {
      private ArrayList dialogs;
      private Context mContext;

      public DialogsAdapter(Context var2) {
         this.mContext = var2;
         this.dialogs = new ArrayList();
         int var3 = (int)(System.currentTimeMillis() / 1000L);
         DialogCell.CustomDialog var4 = new DialogCell.CustomDialog();
         var4.name = "Eva Summer";
         var4.message = "Reminds me of a Chinese prove...";
         var4.id = 0;
         var4.unread_count = 0;
         var4.pinned = true;
         var4.muted = false;
         var4.type = 0;
         var4.date = var3;
         var4.verified = false;
         var4.isMedia = false;
         var4.sent = true;
         this.dialogs.add(var4);
         var4 = new DialogCell.CustomDialog();
         var4.name = "Your inner Competition";
         var4.message = "hey, I've updated the source code.";
         var4.id = 1;
         var4.unread_count = 2;
         var4.pinned = false;
         var4.muted = false;
         var4.type = 0;
         var4.date = var3 - 3600;
         var4.verified = false;
         var4.isMedia = false;
         var4.sent = false;
         this.dialogs.add(var4);
         var4 = new DialogCell.CustomDialog();
         var4.name = "Mike Apple";
         var4.message = "\ud83e\udd37\u200d♂️ Sticker";
         var4.id = 2;
         var4.unread_count = 3;
         var4.pinned = false;
         var4.muted = true;
         var4.type = 0;
         var4.date = var3 - 7200;
         var4.verified = false;
         var4.isMedia = true;
         var4.sent = false;
         this.dialogs.add(var4);
         var4 = new DialogCell.CustomDialog();
         var4.name = "Paul Newman";
         var4.message = "Any ideas?";
         var4.id = 3;
         var4.unread_count = 0;
         var4.pinned = false;
         var4.muted = false;
         var4.type = 2;
         var4.date = var3 - 10800;
         var4.verified = false;
         var4.isMedia = false;
         var4.sent = false;
         this.dialogs.add(var4);
         var4 = new DialogCell.CustomDialog();
         var4.name = "Old Pirates";
         var4.message = "Yo-ho-ho!";
         var4.id = 4;
         var4.unread_count = 0;
         var4.pinned = false;
         var4.muted = false;
         var4.type = 1;
         var4.date = var3 - 14400;
         var4.verified = false;
         var4.isMedia = false;
         var4.sent = true;
         this.dialogs.add(var4);
         var4 = new DialogCell.CustomDialog();
         var4.name = "Kate Bright";
         var4.message = "Hola!";
         var4.id = 5;
         var4.unread_count = 0;
         var4.pinned = false;
         var4.muted = false;
         var4.type = 0;
         var4.date = var3 - 18000;
         var4.verified = false;
         var4.isMedia = false;
         var4.sent = false;
         this.dialogs.add(var4);
         var4 = new DialogCell.CustomDialog();
         var4.name = "Nick K";
         var4.message = "These are not the droids you are looking for";
         var4.id = 6;
         var4.unread_count = 0;
         var4.pinned = false;
         var4.muted = false;
         var4.type = 0;
         var4.date = var3 - 21600;
         var4.verified = true;
         var4.isMedia = false;
         var4.sent = false;
         this.dialogs.add(var4);
         var4 = new DialogCell.CustomDialog();
         var4.name = "Adler Toberg";
         var4.message = "Did someone say peanut butter?";
         var4.id = 0;
         var4.unread_count = 0;
         var4.pinned = false;
         var4.muted = false;
         var4.type = 0;
         var4.date = var3 - 25200;
         var4.verified = true;
         var4.isMedia = false;
         var4.sent = false;
         this.dialogs.add(var4);
      }

      public int getItemCount() {
         return this.dialogs.size();
      }

      public int getItemViewType(int var1) {
         return var1 == this.dialogs.size() ? 1 : 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3 = true;
         if (var2 == 1) {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (var1.getItemViewType() == 0) {
            DialogCell var5 = (DialogCell)var1.itemView;
            int var3 = this.getItemCount();
            boolean var4 = true;
            if (var2 == var3 - 1) {
               var4 = false;
            }

            var5.useSeparator = var4;
            var5.setDialog((DialogCell.CustomDialog)this.dialogs.get(var2));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 == 0) {
            var3 = new DialogCell(this.mContext, false, false);
         } else if (var2 == 1) {
            var3 = new LoadingCell(this.mContext);
         } else {
            var3 = null;
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }

   public class MessagesAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;
      private ArrayList messages;
      private boolean showSecretMessages;

      public MessagesAdapter(Context var2) {
         int var3 = Utilities.random.nextInt(100);
         byte var4;
         if (BuildVars.DEBUG_VERSION) {
            var4 = 5;
         } else {
            var4 = 1;
         }

         boolean var5;
         if (var3 <= var4) {
            var5 = true;
         } else {
            var5 = false;
         }

         this.showSecretMessages = var5;
         this.mContext = var2;
         this.messages = new ArrayList();
         int var12 = (int)(System.currentTimeMillis() / 1000L) - 3600;
         TLRPC.TL_message var10;
         TLRPC.TL_message var13;
         if (this.showSecretMessages) {
            TLRPC.TL_user var9 = new TLRPC.TL_user();
            var9.id = Integer.MAX_VALUE;
            var9.first_name = "Me";
            TLRPC.TL_user var6 = new TLRPC.TL_user();
            var6.id = 2147483646;
            var6.first_name = "Serj";
            ArrayList var7 = new ArrayList();
            var7.add(var9);
            var7.add(var6);
            MessagesController.getInstance(ThemePreviewActivity.access$1800(ThemePreviewActivity.this)).putUsers(var7, true);
            TLRPC.TL_message var14 = new TLRPC.TL_message();
            var14.message = "Guess why Half-Life 3 was never released.";
            var3 = var12 + 960;
            var14.date = var3;
            var14.dialog_id = -1L;
            var14.flags = 259;
            var14.id = 2147483646;
            var14.media = new TLRPC.TL_messageMediaEmpty();
            var14.out = false;
            var14.to_id = new TLRPC.TL_peerChat();
            var14.to_id.chat_id = 1;
            var14.from_id = var6.id;
            this.messages.add(new MessageObject(ThemePreviewActivity.access$1900(ThemePreviewActivity.this), var14, true));
            var14 = new TLRPC.TL_message();
            var14.message = "No.\nAnd every unnecessary ping of the dev delays the release for 10 days.\nEvery request for ETA delays the release for 2 weeks.";
            var14.date = var3;
            var14.dialog_id = -1L;
            var14.flags = 259;
            var14.id = 1;
            var14.media = new TLRPC.TL_messageMediaEmpty();
            var14.out = false;
            var14.to_id = new TLRPC.TL_peerChat();
            var14.to_id.chat_id = 1;
            var14.from_id = var6.id;
            this.messages.add(new MessageObject(ThemePreviewActivity.access$2000(ThemePreviewActivity.this), var14, true));
            var13 = new TLRPC.TL_message();
            var13.message = "Is source code for Android coming anytime soon?";
            var13.date = var12 + 600;
            var13.dialog_id = -1L;
            var13.flags = 259;
            var13.id = 1;
            var13.media = new TLRPC.TL_messageMediaEmpty();
            var13.out = false;
            var13.to_id = new TLRPC.TL_peerChat();
            var13.to_id.chat_id = 1;
            var13.from_id = var9.id;
            this.messages.add(new MessageObject(ThemePreviewActivity.access$2100(ThemePreviewActivity.this), var13, true));
         } else {
            var10 = new TLRPC.TL_message();
            var10.message = "Reinhardt, we need to find you some new tunes \ud83c\udfb6.";
            var3 = var12 + 60;
            var10.date = var3;
            var10.dialog_id = 1L;
            var10.flags = 259;
            var10.from_id = UserConfig.getInstance(ThemePreviewActivity.access$2200(ThemePreviewActivity.this)).getClientUserId();
            var10.id = 1;
            var10.media = new TLRPC.TL_messageMediaEmpty();
            var10.out = true;
            var10.to_id = new TLRPC.TL_peerUser();
            var10.to_id.user_id = 0;
            MessageObject var11 = new MessageObject(ThemePreviewActivity.access$2300(ThemePreviewActivity.this), var10, true);
            var13 = new TLRPC.TL_message();
            var13.message = "I can't even take you seriously right now.";
            var13.date = var12 + 960;
            var13.dialog_id = 1L;
            var13.flags = 259;
            var13.from_id = UserConfig.getInstance(ThemePreviewActivity.access$2400(ThemePreviewActivity.this)).getClientUserId();
            var13.id = 1;
            var13.media = new TLRPC.TL_messageMediaEmpty();
            var13.out = true;
            var13.to_id = new TLRPC.TL_peerUser();
            var13.to_id.user_id = 0;
            this.messages.add(new MessageObject(ThemePreviewActivity.access$2500(ThemePreviewActivity.this), var13, true));
            var13 = new TLRPC.TL_message();
            var13.date = var12 + 130;
            var13.dialog_id = 1L;
            var13.flags = 259;
            var13.from_id = 0;
            var13.id = 5;
            var13.media = new TLRPC.TL_messageMediaDocument();
            TLRPC.MessageMedia var16 = var13.media;
            var16.flags |= 3;
            var16.document = new TLRPC.TL_document();
            TLRPC.Document var17 = var13.media.document;
            var17.mime_type = "audio/mp4";
            var17.file_reference = new byte[0];
            TLRPC.TL_documentAttributeAudio var18 = new TLRPC.TL_documentAttributeAudio();
            var18.duration = 243;
            var18.performer = "David Hasselhoff";
            var18.title = "True Survivor";
            var13.media.document.attributes.add(var18);
            var13.out = false;
            var13.to_id = new TLRPC.TL_peerUser();
            var13.to_id.user_id = UserConfig.getInstance(ThemePreviewActivity.access$2600(ThemePreviewActivity.this)).getClientUserId();
            this.messages.add(new MessageObject(ThemePreviewActivity.access$2700(ThemePreviewActivity.this), var13, true));
            var13 = new TLRPC.TL_message();
            var13.message = "Ah, you kids today with techno music! You should enjoy the classics, like Hasselhoff!";
            var13.date = var3;
            var13.dialog_id = 1L;
            var13.flags = 265;
            var13.from_id = 0;
            var13.id = 1;
            var13.reply_to_msg_id = 5;
            var13.media = new TLRPC.TL_messageMediaEmpty();
            var13.out = false;
            var13.to_id = new TLRPC.TL_peerUser();
            var13.to_id.user_id = UserConfig.getInstance(ThemePreviewActivity.access$2800(ThemePreviewActivity.this)).getClientUserId();
            MessageObject var15 = new MessageObject(ThemePreviewActivity.access$2900(ThemePreviewActivity.this), var13, true);
            var15.customReplyName = "Lucio";
            var15.replyMessageObject = var11;
            this.messages.add(var15);
            var13 = new TLRPC.TL_message();
            var13.date = var12 + 120;
            var13.dialog_id = 1L;
            var13.flags = 259;
            var13.from_id = UserConfig.getInstance(ThemePreviewActivity.access$3000(ThemePreviewActivity.this)).getClientUserId();
            var13.id = 1;
            var13.media = new TLRPC.TL_messageMediaDocument();
            var16 = var13.media;
            var16.flags |= 3;
            var16.document = new TLRPC.TL_document();
            var17 = var13.media.document;
            var17.mime_type = "audio/ogg";
            var17.file_reference = new byte[0];
            var18 = new TLRPC.TL_documentAttributeAudio();
            var18.flags = 1028;
            var18.duration = 3;
            var18.voice = true;
            var18.waveform = new byte[]{0, 4, 17, -50, -93, 86, -103, -45, -12, -26, 63, -25, -3, 109, -114, -54, -4, -1, -1, -1, -1, -29, -1, -1, -25, -1, -1, -97, -43, 57, -57, -108, 1, -91, -4, -47, 21, 99, 10, 97, 43, 45, 115, -112, -77, 51, -63, 66, 40, 34, -122, -116, 48, -124, 16, 66, -120, 16, 68, 16, 33, 4, 1};
            var13.media.document.attributes.add(var18);
            var13.out = true;
            var13.to_id = new TLRPC.TL_peerUser();
            var13.to_id.user_id = 0;
            var15 = new MessageObject(ThemePreviewActivity.access$3100(ThemePreviewActivity.this), var13, true);
            var15.audioProgressSec = 1;
            var15.audioProgress = 0.3F;
            var15.useCustomPhoto = true;
            this.messages.add(var15);
            this.messages.add(var11);
            var10 = new TLRPC.TL_message();
            var10.date = var12 + 10;
            var10.dialog_id = 1L;
            var10.flags = 257;
            var10.from_id = 0;
            var10.id = 1;
            var10.media = new TLRPC.TL_messageMediaPhoto();
            TLRPC.MessageMedia var19 = var10.media;
            var19.flags |= 3;
            var19.photo = new TLRPC.TL_photo();
            TLRPC.Photo var20 = var10.media.photo;
            var20.file_reference = new byte[0];
            var20.has_stickers = false;
            var20.id = 1L;
            var20.access_hash = 0L;
            var20.date = var12;
            TLRPC.TL_photoSize var21 = new TLRPC.TL_photoSize();
            var21.size = 0;
            var21.w = 500;
            var21.h = 302;
            var21.type = "s";
            var21.location = new TLRPC.TL_fileLocationUnavailable();
            var10.media.photo.sizes.add(var21);
            var10.message = "Bring it on! I LIVE for this!";
            var10.out = false;
            var10.to_id = new TLRPC.TL_peerUser();
            var10.to_id.user_id = UserConfig.getInstance(ThemePreviewActivity.access$3200(ThemePreviewActivity.this)).getClientUserId();
            var11 = new MessageObject(ThemePreviewActivity.access$3300(ThemePreviewActivity.this), var10, true);
            var11.useCustomPhoto = true;
            this.messages.add(var11);
         }

         var10 = new TLRPC.TL_message();
         var10.message = LocaleController.formatDateChat((long)var12);
         var10.id = 0;
         var10.date = var12;
         MessageObject var8 = new MessageObject(ThemePreviewActivity.access$3400(ThemePreviewActivity.this), var10, false);
         var8.type = 10;
         var8.contentType = 1;
         var8.isDateObject = true;
         this.messages.add(var8);
      }

      public int getItemCount() {
         return this.messages.size();
      }

      public int getItemViewType(int var1) {
         return var1 >= 0 && var1 < this.messages.size() ? ((MessageObject)this.messages.get(var1)).contentType : 4;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         MessageObject var3 = (MessageObject)this.messages.get(var2);
         View var4 = var1.itemView;
         if (var4 instanceof ChatMessageCell) {
            boolean var5;
            int var8;
            boolean var10;
            ChatMessageCell var14;
            label31: {
               var14 = (ChatMessageCell)var4;
               var5 = false;
               var14.isChat = false;
               int var6 = var2 - 1;
               int var7 = this.getItemViewType(var6);
               var8 = var2 + 1;
               var2 = this.getItemViewType(var8);
               if (!(var3.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup) && var7 == var1.getItemViewType()) {
                  MessageObject var9 = (MessageObject)this.messages.get(var6);
                  if (var9.isOutOwner() == var3.isOutOwner() && Math.abs(var9.messageOwner.date - var3.messageOwner.date) <= 300) {
                     var10 = true;
                     break label31;
                  }
               }

               var10 = false;
            }

            boolean var11 = var5;
            if (var2 == var1.getItemViewType()) {
               MessageObject var12 = (MessageObject)this.messages.get(var8);
               var11 = var5;
               if (!(var12.messageOwner.reply_markup instanceof TLRPC.TL_replyInlineMarkup)) {
                  var11 = var5;
                  if (var12.isOutOwner() == var3.isOutOwner()) {
                     var11 = var5;
                     if (Math.abs(var12.messageOwner.date - var3.messageOwner.date) <= 300) {
                        var11 = true;
                     }
                  }
               }
            }

            var14.isChat = this.showSecretMessages;
            var14.setFullyDraw(true);
            var14.setMessageObject(var3, (MessageObject.GroupedMessages)null, var10, var11);
         } else if (var4 instanceof ChatActionCell) {
            ChatActionCell var13 = (ChatActionCell)var4;
            var13.setMessageObject(var3);
            var13.setAlpha(1.0F);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 == 0) {
            var3 = new ChatMessageCell(this.mContext);
            ((ChatMessageCell)var3).setDelegate(new ChatMessageCell.ChatMessageCellDelegate() {
               // $FF: synthetic method
               public boolean canPerformActions() {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$canPerformActions(this);
               }

               // $FF: synthetic method
               public void didLongPress(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didLongPress(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressBotButton(ChatMessageCell var1, TLRPC.KeyboardButton var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressBotButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressCancelSendButton(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressCancelSendButton(this, var1);
               }

               // $FF: synthetic method
               public void didPressChannelAvatar(ChatMessageCell var1, TLRPC.Chat var2, int var3, float var4, float var5) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressChannelAvatar(this, var1, var2, var3, var4, var5);
               }

               // $FF: synthetic method
               public void didPressHiddenForward(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressHiddenForward(this, var1);
               }

               // $FF: synthetic method
               public void didPressImage(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressImage(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressInstantButton(ChatMessageCell var1, int var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressInstantButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressOther(ChatMessageCell var1, float var2, float var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressOther(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressReplyMessage(ChatMessageCell var1, int var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressReplyMessage(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressShare(ChatMessageCell var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressShare(this, var1);
               }

               // $FF: synthetic method
               public void didPressUrl(MessageObject var1, CharacterStyle var2, boolean var3) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressUrl(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressUserAvatar(ChatMessageCell var1, TLRPC.User var2, float var3, float var4) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressUserAvatar(this, var1, var2, var3, var4);
               }

               // $FF: synthetic method
               public void didPressViaBot(ChatMessageCell var1, String var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressViaBot(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressVoteButton(ChatMessageCell var1, TLRPC.TL_pollAnswer var2) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didPressVoteButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didStartVideoStream(MessageObject var1) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$didStartVideoStream(this, var1);
               }

               // $FF: synthetic method
               public boolean isChatAdminCell(int var1) {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$isChatAdminCell(this, var1);
               }

               // $FF: synthetic method
               public void needOpenWebView(String var1, String var2, String var3, String var4, int var5, int var6) {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$needOpenWebView(this, var1, var2, var3, var4, var5, var6);
               }

               // $FF: synthetic method
               public boolean needPlayMessage(MessageObject var1) {
                  return ChatMessageCell$ChatMessageCellDelegate$_CC.$default$needPlayMessage(this, var1);
               }

               // $FF: synthetic method
               public void videoTimerReached() {
                  ChatMessageCell$ChatMessageCellDelegate$_CC.$default$videoTimerReached(this);
               }
            });
         } else if (var2 == 1) {
            var3 = new ChatActionCell(this.mContext);
            ((ChatActionCell)var3).setDelegate(new ChatActionCell.ChatActionCellDelegate() {
               // $FF: synthetic method
               public void didClickImage(ChatActionCell var1) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$didClickImage(this, var1);
               }

               // $FF: synthetic method
               public void didLongPress(ChatActionCell var1, float var2, float var3) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$didLongPress(this, var1, var2, var3);
               }

               // $FF: synthetic method
               public void didPressBotButton(MessageObject var1, TLRPC.KeyboardButton var2) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$didPressBotButton(this, var1, var2);
               }

               // $FF: synthetic method
               public void didPressReplyMessage(ChatActionCell var1, int var2) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$didPressReplyMessage(this, var1, var2);
               }

               // $FF: synthetic method
               public void needOpenUserProfile(int var1) {
                  ChatActionCell$ChatActionCellDelegate$_CC.$default$needOpenUserProfile(this, var1);
               }
            });
         } else {
            var3 = null;
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }
}
