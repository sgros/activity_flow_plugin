package org.telegram.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.DividerCell;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class PhonebookShareActivity extends BaseFragment {
   private PhonebookShareActivity.ListAdapter adapter;
   private BackupImageView avatarImage;
   private FrameLayout bottomLayout;
   private TLRPC.User currentUser;
   private PhonebookSelectActivity.PhonebookSelectActivityDelegate delegate;
   private int detailRow;
   private int emptyRow;
   private int extraHeight;
   private View extraHeightView;
   private boolean isImport;
   private LinearLayoutManager layoutManager;
   private RecyclerListView listView;
   private TextView nameTextView;
   private ArrayList other = new ArrayList();
   private int overscrollRow;
   private int phoneDividerRow;
   private int phoneEndRow;
   private int phoneStartRow;
   private ArrayList phones = new ArrayList();
   private int rowCount;
   private View shadowView;
   private TextView shareTextView;
   private int vcardEndRow;
   private int vcardStartRow;

   public PhonebookShareActivity(ContactsController.Contact var1, Uri var2, File var3, String var4) {
      ArrayList var5 = new ArrayList();
      ArrayList var9;
      if (var2 != null) {
         var9 = AndroidUtilities.loadVCardFromStream(var2, super.currentAccount, false, var5, var4);
      } else if (var3 != null) {
         var9 = AndroidUtilities.loadVCardFromStream(Uri.fromFile(var3), super.currentAccount, false, var5, var4);
         var3.delete();
         this.isImport = true;
      } else {
         String var10 = var1.key;
         if (var10 != null) {
            var9 = AndroidUtilities.loadVCardFromStream(Uri.withAppendedPath(Contacts.CONTENT_VCARD_URI, var10), super.currentAccount, true, var5, var4);
         } else {
            this.currentUser = var1.user;
            AndroidUtilities.VcardItem var12 = new AndroidUtilities.VcardItem();
            var12.type = 0;
            ArrayList var11 = var12.vcardData;
            StringBuilder var14 = new StringBuilder();
            var14.append("TEL;MOBILE:+");
            var14.append(this.currentUser.phone);
            var4 = var14.toString();
            var12.fullData = var4;
            var11.add(var4);
            this.phones.add(var12);
            var9 = null;
         }
      }

      if (var9 != null) {
         for(int var6 = 0; var6 < var5.size(); ++var6) {
            AndroidUtilities.VcardItem var13 = (AndroidUtilities.VcardItem)var5.get(var6);
            if (var13.type != 0) {
               this.other.add(var13);
            } else {
               int var7 = 0;

               boolean var15;
               while(true) {
                  if (var7 >= this.phones.size()) {
                     var15 = false;
                     break;
                  }

                  if (((AndroidUtilities.VcardItem)this.phones.get(var7)).getValue(false).equals(var13.getValue(false))) {
                     var15 = true;
                     break;
                  }

                  ++var7;
               }

               if (var15) {
                  var13.checked = false;
               } else {
                  this.phones.add(var13);
               }
            }
         }

         if (var9 != null && !var9.isEmpty()) {
            this.currentUser = (TLRPC.User)var9.get(0);
            if (var1 != null) {
               TLRPC.User var8 = var1.user;
               if (var8 != null) {
                  this.currentUser.photo = var8.photo;
               }
            }
         }
      }

   }

   // $FF: synthetic method
   static ActionBarLayout access$200(PhonebookShareActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static TLRPC.User access$2000(PhonebookShareActivity var0) {
      return var0.currentUser;
   }

   // $FF: synthetic method
   static ActionBarLayout access$300(PhonebookShareActivity var0) {
      return var0.parentLayout;
   }

   // $FF: synthetic method
   static View access$700(PhonebookShareActivity var0) {
      return var0.fragmentView;
   }

   // $FF: synthetic method
   static View access$800(PhonebookShareActivity var0) {
      return var0.fragmentView;
   }

   private void fixLayout() {
      View var1 = super.fragmentView;
      if (var1 != null) {
         var1.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
               if (PhonebookShareActivity.access$700(PhonebookShareActivity.this) != null) {
                  PhonebookShareActivity.this.needLayout();
                  PhonebookShareActivity.access$800(PhonebookShareActivity.this).getViewTreeObserver().removeOnPreDrawListener(this);
               }

               return true;
            }
         });
      }
   }

   private void needLayout() {
      boolean var1 = super.actionBar.getOccupyStatusBar();
      byte var2 = 0;
      int var3;
      if (var1) {
         var3 = AndroidUtilities.statusBarHeight;
      } else {
         var3 = 0;
      }

      var3 += ActionBar.getCurrentActionBarHeight();
      RecyclerListView var4 = this.listView;
      if (var4 != null) {
         LayoutParams var11 = (LayoutParams)var4.getLayoutParams();
         if (var11.topMargin != var3) {
            var11.topMargin = var3;
            this.listView.setLayoutParams(var11);
            this.extraHeightView.setTranslationY((float)var3);
         }
      }

      if (this.avatarImage != null) {
         float var5 = (float)this.extraHeight / (float)AndroidUtilities.dp(88.0F);
         this.extraHeightView.setScaleY(var5);
         this.shadowView.setTranslationY((float)(var3 + this.extraHeight));
         BackupImageView var12 = this.avatarImage;
         float var6 = (18.0F * var5 + 42.0F) / 42.0F;
         var12.setScaleX(var6);
         this.avatarImage.setScaleY(var6);
         var3 = var2;
         if (super.actionBar.getOccupyStatusBar()) {
            var3 = AndroidUtilities.statusBarHeight;
         }

         var6 = (float)var3;
         float var7 = (float)ActionBar.getCurrentActionBarHeight() / 2.0F;
         float var8 = AndroidUtilities.density;
         this.avatarImage.setTranslationX((float)(-AndroidUtilities.dp(47.0F)) * var5);
         var12 = this.avatarImage;
         double var9 = (double)(var6 + var7 * (var5 + 1.0F) - 21.0F * var8 + var8 * 27.0F * var5);
         var12.setTranslationY((float)Math.ceil(var9));
         this.nameTextView.setTranslationX(AndroidUtilities.density * -21.0F * var5);
         this.nameTextView.setTranslationY((float)Math.floor(var9) - (float)Math.ceil((double)AndroidUtilities.density) + (float)Math.floor((double)(AndroidUtilities.density * 7.0F * var5)));
         TextView var13 = this.nameTextView;
         var5 = var5 * 0.12F + 1.0F;
         var13.setScaleX(var5);
         this.nameTextView.setScaleY(var5);
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackgroundColor(Theme.getColor("avatar_backgroundActionBarBlue"));
      super.actionBar.setItemsBackgroundColor(Theme.getColor("avatar_actionBarSelectorBlue"), false);
      super.actionBar.setItemsColor(Theme.getColor("avatar_actionBarIconBlue"), false);
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAddToContainer(false);
      this.extraHeight = 88;
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               PhonebookShareActivity.this.finishFragment();
            }

         }
      });
      super.fragmentView = new FrameLayout(var1) {
         protected boolean drawChild(Canvas var1, View var2, long var3) {
            if (var2 != PhonebookShareActivity.this.listView) {
               return super.drawChild(var1, var2, var3);
            } else {
               boolean var5 = super.drawChild(var1, var2, var3);
               if (PhonebookShareActivity.access$200(PhonebookShareActivity.this) != null) {
                  int var6 = this.getChildCount();
                  byte var7 = 0;
                  int var8 = 0;

                  int var9;
                  while(true) {
                     var9 = var7;
                     if (var8 >= var6) {
                        break;
                     }

                     View var10 = this.getChildAt(var8);
                     if (var10 != var2 && var10 instanceof ActionBar && var10.getVisibility() == 0) {
                        var9 = var7;
                        if (((ActionBar)var10).getCastShadows()) {
                           var9 = var10.getMeasuredHeight();
                        }
                        break;
                     }

                     ++var8;
                  }

                  PhonebookShareActivity.access$300(PhonebookShareActivity.this).drawHeaderShadow(var1, var9);
               }

               return var5;
            }
         }
      };
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var3 = this.listView;
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager = var4;
      var3.setLayoutManager(var4);
      this.listView.setGlowColor(Theme.getColor("avatar_backgroundActionBarBlue"));
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 0.0F, 0.0F, 48.0F));
      this.listView.setAdapter(new PhonebookShareActivity.ListAdapter(var1));
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$PhonebookShareActivity$87T4KO49pRqiUUhu1YF8Uwv0ffg(this)));
      this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$PhonebookShareActivity$ih_UP_iiSTWRH840i078MY0XuHs(this)));
      var2.addView(super.actionBar);
      this.extraHeightView = new View(var1);
      this.extraHeightView.setPivotY(0.0F);
      this.extraHeightView.setBackgroundColor(Theme.getColor("avatar_backgroundActionBarBlue"));
      var2.addView(this.extraHeightView, LayoutHelper.createFrame(-1, 88.0F));
      this.shadowView = new View(var1);
      this.shadowView.setBackgroundResource(2131165407);
      var2.addView(this.shadowView, LayoutHelper.createFrame(-1, 3.0F));
      this.avatarImage = new BackupImageView(var1);
      this.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0F));
      this.avatarImage.setPivotX(0.0F);
      this.avatarImage.setPivotY(0.0F);
      var2.addView(this.avatarImage, LayoutHelper.createFrame(42, 42.0F, 51, 64.0F, 0.0F, 0.0F, 0.0F));
      this.nameTextView = new TextView(var1);
      this.nameTextView.setTextColor(Theme.getColor("profile_title"));
      this.nameTextView.setTextSize(1, 18.0F);
      this.nameTextView.setLines(1);
      this.nameTextView.setMaxLines(1);
      this.nameTextView.setSingleLine(true);
      this.nameTextView.setEllipsize(TruncateAt.END);
      this.nameTextView.setGravity(3);
      this.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.nameTextView.setPivotX(0.0F);
      this.nameTextView.setPivotY(0.0F);
      var2.addView(this.nameTextView, LayoutHelper.createFrame(-2, -2.0F, 51, 118.0F, 8.0F, 10.0F, 0.0F));
      this.needLayout();
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            if (PhonebookShareActivity.this.layoutManager.getItemCount() != 0) {
               var2 = 0;
               byte var6 = 0;
               View var5 = var1.getChildAt(0);
               if (var5 != null) {
                  if (PhonebookShareActivity.this.layoutManager.findFirstVisibleItemPosition() == 0) {
                     int var4 = AndroidUtilities.dp(88.0F);
                     var2 = var6;
                     if (var5.getTop() < 0) {
                        var2 = var5.getTop();
                     }

                     var2 += var4;
                  }

                  if (PhonebookShareActivity.this.extraHeight != var2) {
                     PhonebookShareActivity.this.extraHeight = var2;
                     PhonebookShareActivity.this.needLayout();
                  }
               }

            }
         }
      });
      this.bottomLayout = new FrameLayout(var1);
      this.bottomLayout.setBackgroundDrawable(Theme.createSelectorWithBackgroundDrawable(Theme.getColor("passport_authorizeBackground"), Theme.getColor("passport_authorizeBackgroundSelected")));
      var2.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 80));
      this.bottomLayout.setOnClickListener(new _$$Lambda$PhonebookShareActivity$r6QmJiqDVDqQ_a81o_t3bySVxWU(this));
      this.shareTextView = new TextView(var1);
      this.shareTextView.setCompoundDrawablePadding(AndroidUtilities.dp(8.0F));
      this.shareTextView.setTextColor(Theme.getColor("passport_authorizeText"));
      if (this.isImport) {
         this.shareTextView.setText(LocaleController.getString("AddContactChat", 2131558568));
      } else {
         this.shareTextView.setText(LocaleController.getString("ContactShare", 2131559146));
      }

      this.shareTextView.setTextSize(1, 14.0F);
      this.shareTextView.setGravity(17);
      this.shareTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
      this.bottomLayout.addView(this.shareTextView, LayoutHelper.createFrame(-2, -1, 17));
      View var5 = new View(var1);
      var5.setBackgroundResource(2131165408);
      var2.addView(var5, LayoutHelper.createFrame(-1, 3.0F, 83, 0.0F, 0.0F, 0.0F, 48.0F));
      AvatarDrawable var6 = new AvatarDrawable();
      var6.setProfile(true);
      TLRPC.User var8 = this.currentUser;
      var6.setInfo(5, var8.first_name, var8.last_name, false);
      var6.setColor(Theme.getColor("avatar_backgroundInProfileBlue"));
      this.avatarImage.setImage((ImageLocation)ImageLocation.getForUser(this.currentUser, false), "50_50", (Drawable)var6, (Object)this.currentUser);
      TextView var9 = this.nameTextView;
      TLRPC.User var7 = this.currentUser;
      var9.setText(ContactsController.formatName(var7.first_name, var7.last_name));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{PhonebookShareActivity.TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.shareTextView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "passport_authorizeText"), new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_BACKGROUNDFILTER, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "passport_authorizeBackground"), new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "passport_authorizeBackgroundSelected"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{PhonebookShareActivity.TextCheckBoxCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{PhonebookShareActivity.TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxSquareUnchecked"), new ThemeDescription(this.listView, 0, new Class[]{PhonebookShareActivity.TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxSquareDisabled"), new ThemeDescription(this.listView, 0, new Class[]{PhonebookShareActivity.TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxSquareBackground"), new ThemeDescription(this.listView, 0, new Class[]{PhonebookShareActivity.TextCheckBoxCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "checkboxSquareCheck")};
   }

   // $FF: synthetic method
   public void lambda$createView$1$PhonebookShareActivity(View var1, int var2) {
      int var3 = this.phoneStartRow;
      AndroidUtilities.VcardItem var4;
      if (var2 >= var3 && var2 < this.phoneEndRow) {
         var4 = (AndroidUtilities.VcardItem)this.phones.get(var2 - var3);
      } else {
         var3 = this.vcardStartRow;
         if (var2 >= var3 && var2 < this.vcardEndRow) {
            var4 = (AndroidUtilities.VcardItem)this.other.get(var2 - var3);
         } else {
            var4 = null;
         }
      }

      if (var4 != null) {
         boolean var5 = this.isImport;
         boolean var6 = true;
         if (var5) {
            var2 = var4.type;
            if (var2 == 0) {
               try {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("tel:");
                  var7.append(var4.getValue(false));
                  Intent var10 = new Intent("android.intent.action.DIAL", Uri.parse(var7.toString()));
                  var10.addFlags(268435456);
                  this.getParentActivity().startActivityForResult(var10, 500);
               } catch (Exception var9) {
                  FileLog.e((Throwable)var9);
               }
            } else {
               StringBuilder var11;
               if (var2 == 1) {
                  Activity var16 = this.getParentActivity();
                  var11 = new StringBuilder();
                  var11.append("mailto:");
                  var11.append(var4.getValue(false));
                  Browser.openUrl(var16, (String)var11.toString());
               } else if (var2 == 3) {
                  String var14 = var4.getValue(false);
                  String var12 = var14;
                  if (!var14.startsWith("http")) {
                     var11 = new StringBuilder();
                     var11.append("http://");
                     var11.append(var14);
                     var12 = var11.toString();
                  }

                  Browser.openUrl(this.getParentActivity(), (String)var12);
               } else {
                  AlertDialog.Builder var13 = new AlertDialog.Builder(this.getParentActivity());
                  String var17 = LocaleController.getString("Copy", 2131559163);
                  _$$Lambda$PhonebookShareActivity$g_17B0C2dU4u_JSxmNJh3l3Yxbg var15 = new _$$Lambda$PhonebookShareActivity$g_17B0C2dU4u_JSxmNJh3l3Yxbg(this, var4);
                  var13.setItems(new CharSequence[]{var17}, var15);
                  this.showDialog(var13.create());
               }
            }
         } else {
            var4.checked ^= true;
            if (var2 >= this.phoneStartRow && var2 < this.phoneEndRow) {
               var2 = 0;

               while(true) {
                  if (var2 >= this.phones.size()) {
                     var6 = false;
                     break;
                  }

                  if (((AndroidUtilities.VcardItem)this.phones.get(var2)).checked) {
                     break;
                  }

                  ++var2;
               }

               this.bottomLayout.setEnabled(var6);
               TextView var18 = this.shareTextView;
               float var8;
               if (var6) {
                  var8 = 1.0F;
               } else {
                  var8 = 0.5F;
               }

               var18.setAlpha(var8);
            }

            ((PhonebookShareActivity.TextCheckBoxCell)var1).setChecked(var4.checked);
         }

      }
   }

   // $FF: synthetic method
   public boolean lambda$createView$3$PhonebookShareActivity(View var1, int var2) {
      int var3 = this.phoneStartRow;
      AndroidUtilities.VcardItem var6;
      if (var2 >= var3 && var2 < this.phoneEndRow) {
         var6 = (AndroidUtilities.VcardItem)this.phones.get(var2 - var3);
      } else {
         var3 = this.vcardStartRow;
         if (var2 >= var3 && var2 < this.vcardEndRow) {
            var6 = (AndroidUtilities.VcardItem)this.other.get(var2 - var3);
         } else {
            var6 = null;
         }
      }

      if (var6 == null) {
         return false;
      } else {
         AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
         String var5 = LocaleController.getString("Copy", 2131559163);
         _$$Lambda$PhonebookShareActivity$0f5AxQdt4YhBWK3SWXLFBDqSogw var7 = new _$$Lambda$PhonebookShareActivity$0f5AxQdt4YhBWK3SWXLFBDqSogw(this, var6);
         var4.setItems(new CharSequence[]{var5}, var7);
         this.showDialog(var4.create());
         return true;
      }
   }

   // $FF: synthetic method
   public void lambda$createView$4$PhonebookShareActivity(View var1) {
      String var3;
      if (this.isImport) {
         if (this.getParentActivity() == null) {
            return;
         }

         AlertDialog.Builder var2 = new AlertDialog.Builder(this.getParentActivity());
         var2.setTitle(LocaleController.getString("AddContactTitle", 2131558569));
         var2.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         var3 = LocaleController.getString("CreateNewContact", 2131559170);
         String var8 = LocaleController.getString("AddToExistingContact", 2131558590);
         PhonebookShareActivity$5 var4 = new PhonebookShareActivity$5(this);
         var2.setItems(new CharSequence[]{var3, var8}, var4);
         var2.show();
      } else {
         TLRPC.User var9 = this.currentUser;
         var3 = var9.restriction_reason;
         StringBuilder var10;
         if (var3 != null) {
            var10 = new StringBuilder(var3);
         } else {
            var10 = new StringBuilder(String.format(Locale.US, "BEGIN:VCARD\nVERSION:3.0\nFN:%1$s\nEND:VCARD", ContactsController.formatName(var9.first_name, var9.last_name)));
         }

         int var5 = var10.lastIndexOf("END:VCARD");
         if (var5 >= 0) {
            this.currentUser.phone = null;
            int var6 = this.phones.size() - 1;

            while(true) {
               int var7;
               AndroidUtilities.VcardItem var11;
               StringBuilder var13;
               if (var6 < 0) {
                  for(var6 = this.other.size() - 1; var6 >= 0; --var6) {
                     var11 = (AndroidUtilities.VcardItem)this.other.get(var6);
                     if (var11.checked) {
                        for(var7 = var11.vcardData.size() - 1; var7 >= 0; --var7) {
                           var13 = new StringBuilder();
                           var13.append((String)var11.vcardData.get(var7));
                           var13.append("\n");
                           var10.insert(var5, var13.toString());
                        }
                     }
                  }

                  this.currentUser.restriction_reason = var10.toString();
                  break;
               }

               var11 = (AndroidUtilities.VcardItem)this.phones.get(var6);
               if (var11.checked) {
                  TLRPC.User var12 = this.currentUser;
                  if (var12.phone == null) {
                     var12.phone = var11.getValue(false);
                  }

                  for(var7 = 0; var7 < var11.vcardData.size(); ++var7) {
                     var13 = new StringBuilder();
                     var13.append((String)var11.vcardData.get(var7));
                     var13.append("\n");
                     var10.insert(var5, var13.toString());
                  }
               }

               --var6;
            }
         }

         this.delegate.didSelectContact(this.currentUser);
         this.finishFragment();
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$PhonebookShareActivity(AndroidUtilities.VcardItem var1, DialogInterface var2, int var3) {
      if (var3 == 0) {
         try {
            ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", var1.getValue(false)));
            Toast.makeText(this.getParentActivity(), LocaleController.getString("TextCopied", 2131560887), 0).show();
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$2$PhonebookShareActivity(AndroidUtilities.VcardItem var1, DialogInterface var2, int var3) {
      if (var3 == 0) {
         try {
            ((ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", var1.getValue(false)));
            if (var1.type == 0) {
               Toast.makeText(this.getParentActivity(), LocaleController.getString("PhoneCopied", 2131560423), 0).show();
            } else if (var1.type == 1) {
               Toast.makeText(this.getParentActivity(), LocaleController.getString("EmailCopied", 2131559328), 0).show();
            } else if (var1.type == 3) {
               Toast.makeText(this.getParentActivity(), LocaleController.getString("LinkCopied", 2131559751), 0).show();
            } else {
               Toast.makeText(this.getParentActivity(), LocaleController.getString("TextCopied", 2131560887), 0).show();
            }
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
      this.fixLayout();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      if (this.currentUser == null) {
         return false;
      } else {
         this.rowCount = 0;
         int var1 = this.rowCount++;
         this.overscrollRow = var1;
         var1 = this.rowCount++;
         this.emptyRow = var1;
         if (this.phones.isEmpty()) {
            this.phoneStartRow = -1;
            this.phoneEndRow = -1;
         } else {
            var1 = this.rowCount;
            this.phoneStartRow = var1;
            this.rowCount = var1 + this.phones.size();
            this.phoneEndRow = this.rowCount;
         }

         if (this.other.isEmpty()) {
            this.phoneDividerRow = -1;
            this.vcardStartRow = -1;
            this.vcardEndRow = -1;
         } else {
            if (this.phones.isEmpty()) {
               this.phoneDividerRow = -1;
            } else {
               var1 = this.rowCount++;
               this.phoneDividerRow = var1;
            }

            var1 = this.rowCount;
            this.vcardStartRow = var1;
            this.rowCount = var1 + this.other.size();
            this.vcardEndRow = this.rowCount;
         }

         var1 = this.rowCount++;
         this.detailRow = var1;
         return true;
      }
   }

   public void onResume() {
      super.onResume();
      this.fixLayout();
   }

   public void setDelegate(PhonebookSelectActivity.PhonebookSelectActivityDelegate var1) {
      this.delegate = var1;
   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return PhonebookShareActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != PhonebookShareActivity.this.emptyRow && var1 != PhonebookShareActivity.this.overscrollRow) {
            if (var1 >= PhonebookShareActivity.this.phoneStartRow && var1 < PhonebookShareActivity.this.phoneEndRow || var1 >= PhonebookShareActivity.this.vcardStartRow && var1 < PhonebookShareActivity.this.vcardEndRow) {
               return 1;
            } else if (var1 == PhonebookShareActivity.this.phoneDividerRow) {
               return 2;
            } else {
               return var1 == PhonebookShareActivity.this.detailRow ? 3 : 2;
            }
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if ((var2 < PhonebookShareActivity.this.phoneStartRow || var2 >= PhonebookShareActivity.this.phoneEndRow) && (var2 < PhonebookShareActivity.this.vcardStartRow || var2 >= PhonebookShareActivity.this.vcardEndRow)) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 == 1) {
               PhonebookShareActivity.TextCheckBoxCell var4 = (PhonebookShareActivity.TextCheckBoxCell)var1.itemView;
               int var5 = PhonebookShareActivity.this.phoneStartRow;
               var3 = 0;
               AndroidUtilities.VcardItem var6;
               AndroidUtilities.VcardItem var7;
               if (var2 >= var5 && var2 < PhonebookShareActivity.this.phoneEndRow) {
                  var6 = (AndroidUtilities.VcardItem)PhonebookShareActivity.this.phones.get(var2 - PhonebookShareActivity.this.phoneStartRow);
                  var7 = var6;
                  if (var2 == PhonebookShareActivity.this.phoneStartRow) {
                     var3 = 2131165791;
                     var7 = var6;
                  }
               } else {
                  var6 = (AndroidUtilities.VcardItem)PhonebookShareActivity.this.other.get(var2 - PhonebookShareActivity.this.vcardStartRow);
                  var7 = var6;
                  if (var2 == PhonebookShareActivity.this.vcardStartRow) {
                     var3 = 2131165786;
                     var7 = var6;
                  }
               }

               var4.setVCardItem(var7, var3);
            }
         } else if (var2 == PhonebookShareActivity.this.overscrollRow) {
            ((EmptyCell)var1.itemView).setHeight(AndroidUtilities.dp(88.0F));
         } else {
            ((EmptyCell)var1.itemView).setHeight(AndroidUtilities.dp(16.0F));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     var3 = null;
                  } else {
                     var3 = new ShadowSectionCell(this.mContext);
                     ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                  }
               } else {
                  var3 = new DividerCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  ((View)var3).setPadding(AndroidUtilities.dp(72.0F), AndroidUtilities.dp(8.0F), 0, AndroidUtilities.dp(8.0F));
               }
            } else {
               var3 = PhonebookShareActivity.this.new TextCheckBoxCell(this.mContext);
               ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new EmptyCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }

   public class TextCheckBoxCell extends FrameLayout {
      private CheckBoxSquare checkBox;
      private ImageView imageView;
      private TextView textView;
      private TextView valueTextView;

      public TextCheckBoxCell(Context var2) {
         super(var2);
         this.textView = new TextView(var2);
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.textView.setTextSize(1, 16.0F);
         this.textView.setSingleLine(false);
         TextView var3 = this.textView;
         boolean var4 = LocaleController.isRTL;
         byte var5 = 5;
         byte var6;
         if (var4) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         var3.setGravity(var6 | 48);
         this.textView.setEllipsize(TruncateAt.END);
         var3 = this.textView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         byte var7;
         float var8;
         if (LocaleController.isRTL) {
            if (PhonebookShareActivity.this.isImport) {
               var7 = 17;
            } else {
               var7 = 64;
            }

            var8 = (float)var7;
         } else {
            var8 = 71.0F;
         }

         float var9;
         if (LocaleController.isRTL) {
            var9 = 71.0F;
         } else {
            if (PhonebookShareActivity.this.isImport) {
               var7 = 17;
            } else {
               var7 = 64;
            }

            var9 = (float)var7;
         }

         this.addView(var3, LayoutHelper.createFrame(-1, -1.0F, var6 | 48, var8, 10.0F, var9, 0.0F));
         this.valueTextView = new TextView(var2);
         this.valueTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText2"));
         this.valueTextView.setTextSize(1, 13.0F);
         this.valueTextView.setLines(1);
         this.valueTextView.setMaxLines(1);
         this.valueTextView.setSingleLine(true);
         var3 = this.valueTextView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         var3.setGravity(var6);
         var3 = this.valueTextView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            if (PhonebookShareActivity.this.isImport) {
               var7 = 17;
            } else {
               var7 = 64;
            }

            var8 = (float)var7;
         } else {
            var8 = 71.0F;
         }

         if (LocaleController.isRTL) {
            var9 = 71.0F;
         } else {
            if (PhonebookShareActivity.this.isImport) {
               var7 = 17;
            } else {
               var7 = 64;
            }

            var9 = (float)var7;
         }

         this.addView(var3, LayoutHelper.createFrame(-2, -2.0F, var6, var8, 35.0F, var9, 0.0F));
         this.imageView = new ImageView(var2);
         this.imageView.setScaleType(ScaleType.CENTER);
         this.imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayIcon"), Mode.MULTIPLY));
         ImageView var11 = this.imageView;
         if (LocaleController.isRTL) {
            var6 = 5;
         } else {
            var6 = 3;
         }

         if (LocaleController.isRTL) {
            var8 = 0.0F;
         } else {
            var8 = 16.0F;
         }

         if (LocaleController.isRTL) {
            var9 = 16.0F;
         } else {
            var9 = 0.0F;
         }

         this.addView(var11, LayoutHelper.createFrame(-2, -2.0F, var6 | 48, var8, 20.0F, var9, 0.0F));
         if (!PhonebookShareActivity.this.isImport) {
            this.checkBox = new CheckBoxSquare(var2, false);
            this.checkBox.setDuplicateParentStateEnabled(false);
            this.checkBox.setFocusable(false);
            this.checkBox.setFocusableInTouchMode(false);
            this.checkBox.setClickable(false);
            CheckBoxSquare var10 = this.checkBox;
            var6 = var5;
            if (LocaleController.isRTL) {
               var6 = 3;
            }

            this.addView(var10, LayoutHelper.createFrame(18, 18.0F, var6 | 16, 19.0F, 0.0F, 19.0F, 0.0F));
         }

      }

      public void invalidate() {
         super.invalidate();
         CheckBoxSquare var1 = this.checkBox;
         if (var1 != null) {
            var1.invalidate();
         }

      }

      public boolean isChecked() {
         CheckBoxSquare var1 = this.checkBox;
         boolean var2;
         if (var1 != null && var1.isChecked()) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
         super.onLayout(var1, var2, var3, var4, var5);
         var2 = this.textView.getMeasuredHeight() + AndroidUtilities.dp(13.0F);
         TextView var6 = this.valueTextView;
         var6.layout(var6.getLeft(), var2, this.valueTextView.getRight(), this.valueTextView.getMeasuredHeight() + var2);
      }

      protected void onMeasure(int var1, int var2) {
         this.measureChildWithMargins(this.textView, var1, 0, var2, 0);
         this.measureChildWithMargins(this.valueTextView, var1, 0, var2, 0);
         this.measureChildWithMargins(this.imageView, var1, 0, var2, 0);
         CheckBoxSquare var3 = this.checkBox;
         if (var3 != null) {
            this.measureChildWithMargins(var3, var1, 0, var2, 0);
         }

         this.setMeasuredDimension(MeasureSpec.getSize(var1), Math.max(AndroidUtilities.dp(64.0F), this.textView.getMeasuredHeight() + this.valueTextView.getMeasuredHeight() + AndroidUtilities.dp(20.0F)));
      }

      public void setChecked(boolean var1) {
         CheckBoxSquare var2 = this.checkBox;
         if (var2 != null) {
            var2.setChecked(var1, true);
         }

      }

      public void setVCardItem(AndroidUtilities.VcardItem var1, int var2) {
         this.textView.setText(var1.getValue(true));
         this.valueTextView.setText(var1.getType());
         CheckBoxSquare var3 = this.checkBox;
         if (var3 != null) {
            var3.setChecked(var1.checked, false);
         }

         if (var2 != 0) {
            this.imageView.setImageResource(var2);
         } else {
            this.imageView.setImageDrawable((Drawable)null);
         }

      }
   }
}
