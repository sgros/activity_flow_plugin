package org.telegram.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class ProxyListActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private int callsDetailRow;
   private int callsRow;
   private int connectionsHeaderRow;
   private int currentConnectionState;
   private LinearLayoutManager layoutManager;
   private ProxyListActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private int proxyAddRow;
   private int proxyDetailRow;
   private int proxyEndRow;
   private int proxyStartRow;
   private int rowCount;
   private int useProxyDetailRow;
   private boolean useProxyForCalls;
   private int useProxyRow;
   private boolean useProxySettings;

   private void checkProxyList() {
      int var1 = SharedConfig.proxyList.size();

      for(int var2 = 0; var2 < var1; ++var2) {
         SharedConfig.ProxyInfo var3 = (SharedConfig.ProxyInfo)SharedConfig.proxyList.get(var2);
         if (!var3.checking && SystemClock.elapsedRealtime() - var3.availableCheckTime >= 120000L) {
            var3.checking = true;
            var3.proxyCheckPingId = ConnectionsManager.getInstance(super.currentAccount).checkProxy(var3.address, var3.port, var3.username, var3.password, var3.secret, new _$$Lambda$ProxyListActivity$FREOr2lMcXdjYTgHJySmzXcovWk(var3));
         }
      }

   }

   // $FF: synthetic method
   static void lambda$checkProxyList$4(SharedConfig.ProxyInfo var0, long var1) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ProxyListActivity$GXujEQ9q4Ax5_ntFNj2k4M90nXg(var0, var1));
   }

   // $FF: synthetic method
   static void lambda$null$3(SharedConfig.ProxyInfo var0, long var1) {
      var0.availableCheckTime = SystemClock.elapsedRealtime();
      var0.checking = false;
      if (var1 == -1L) {
         var0.available = false;
         var0.ping = 0L;
      } else {
         var0.ping = var1;
         var0.available = true;
      }

      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxyCheckDone, var0);
   }

   private void updateRows(boolean var1) {
      boolean var2 = false;
      boolean var3 = false;
      this.rowCount = 0;
      int var4 = this.rowCount++;
      this.useProxyRow = var4;
      var4 = this.rowCount++;
      this.useProxyDetailRow = var4;
      var4 = this.rowCount++;
      this.connectionsHeaderRow = var4;
      if (!SharedConfig.proxyList.isEmpty()) {
         var4 = this.rowCount;
         this.proxyStartRow = var4;
         this.rowCount = var4 + SharedConfig.proxyList.size();
         this.proxyEndRow = this.rowCount;
      } else {
         this.proxyStartRow = -1;
         this.proxyEndRow = -1;
      }

      var4 = this.rowCount++;
      this.proxyAddRow = var4;
      var4 = this.rowCount++;
      this.proxyDetailRow = var4;
      SharedConfig.ProxyInfo var5 = SharedConfig.currentProxy;
      if (var5 != null && !var5.secret.isEmpty()) {
         if (this.callsRow != -1) {
            var3 = true;
         }

         this.callsRow = -1;
         this.callsDetailRow = -1;
         if (!var1 && var3) {
            this.listAdapter.notifyItemChanged(this.proxyDetailRow);
            this.listAdapter.notifyItemRangeRemoved(this.proxyDetailRow + 1, 2);
         }
      } else {
         var3 = var2;
         if (this.callsRow == -1) {
            var3 = true;
         }

         int var6 = this.rowCount++;
         this.callsRow = var6;
         var6 = this.rowCount++;
         this.callsDetailRow = var6;
         if (!var1 && var3) {
            this.listAdapter.notifyItemChanged(this.proxyDetailRow);
            this.listAdapter.notifyItemRangeInserted(this.proxyDetailRow + 1, 2);
         }
      }

      this.checkProxyList();
      if (var1) {
         ProxyListActivity.ListAdapter var7 = this.listAdapter;
         if (var7 != null) {
            var7.notifyDataSetChanged();
         }
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setTitle(LocaleController.getString("ProxySettings", 2131560519));
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setAllowOverlayTitle(false);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ProxyListActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new ProxyListActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1);
      ((DefaultItemAnimator)this.listView.getItemAnimator()).setDelayAnimations(false);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var3 = this.listView;
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var4;
      var3.setLayoutManager(var4);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ProxyListActivity$_EXt_SJXR8hGLT7iiDQoSoNBA_A(this)));
      this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)(new _$$Lambda$ProxyListActivity$xYgyGp8TqWacHgvLq8TnwAzgoII(this)));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.proxySettingsChanged) {
         this.updateRows(true);
      } else {
         RecyclerListView.Holder var4;
         if (var1 == NotificationCenter.didUpdateConnectionState) {
            var1 = ConnectionsManager.getInstance(var2).getConnectionState();
            if (this.currentConnectionState != var1) {
               this.currentConnectionState = var1;
               if (this.listView != null && SharedConfig.currentProxy != null) {
                  var1 = SharedConfig.proxyList.indexOf(SharedConfig.currentProxy);
                  if (var1 >= 0) {
                     var4 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(var1 + this.proxyStartRow);
                     if (var4 != null) {
                        ((ProxyListActivity.TextDetailProxyCell)var4.itemView).updateStatus();
                     }
                  }
               }
            }
         } else if (var1 == NotificationCenter.proxyCheckDone && this.listView != null) {
            SharedConfig.ProxyInfo var5 = (SharedConfig.ProxyInfo)var3[0];
            var1 = SharedConfig.proxyList.indexOf(var5);
            if (var1 >= 0) {
               var4 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(var1 + this.proxyStartRow);
               if (var4 != null) {
                  ((ProxyListActivity.TextDetailProxyCell)var4.itemView).updateStatus();
               }
            }
         }
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, ProxyListActivity.TextDetailProxyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, 0, new Class[]{ProxyListActivity.TextDetailProxyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ProxyListActivity.TextDetailProxyCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueText6"), new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ProxyListActivity.TextDetailProxyCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ProxyListActivity.TextDetailProxyCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGreenText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ProxyListActivity.TextDetailProxyCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText4"), new ThemeDescription(this.listView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ProxyListActivity.TextDetailProxyCell.class}, new String[]{"checkImageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText3"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$ProxyListActivity(View var1, int var2) {
      Editor var3;
      boolean var4;
      RecyclerListView.Holder var6;
      Editor var7;
      SharedConfig.ProxyInfo var8;
      if (var2 == this.useProxyRow) {
         if (SharedConfig.currentProxy == null) {
            if (SharedConfig.proxyList.isEmpty()) {
               this.presentFragment(new ProxySettingsActivity());
               return;
            }

            SharedConfig.currentProxy = (SharedConfig.ProxyInfo)SharedConfig.proxyList.get(0);
            if (!this.useProxySettings) {
               MessagesController.getGlobalMainSettings();
               var3 = MessagesController.getGlobalMainSettings().edit();
               var3.putString("proxy_ip", SharedConfig.currentProxy.address);
               var3.putString("proxy_pass", SharedConfig.currentProxy.password);
               var3.putString("proxy_user", SharedConfig.currentProxy.username);
               var3.putInt("proxy_port", SharedConfig.currentProxy.port);
               var3.putString("proxy_secret", SharedConfig.currentProxy.secret);
               var3.commit();
            }
         }

         this.useProxySettings ^= true;
         MessagesController.getGlobalMainSettings();
         ((TextCheckCell)var1).setChecked(this.useProxySettings);
         if (!this.useProxySettings) {
            var6 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(this.callsRow);
            if (var6 != null) {
               ((TextCheckCell)var6.itemView).setChecked(false);
            }

            this.useProxyForCalls = false;
         }

         var7 = MessagesController.getGlobalMainSettings().edit();
         var7.putBoolean("proxy_enabled", this.useProxySettings);
         var7.commit();
         var4 = this.useProxySettings;
         var8 = SharedConfig.currentProxy;
         ConnectionsManager.setProxySettings(var4, var8.address, var8.port, var8.username, var8.password, var8.secret);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
         NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged);
         NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxySettingsChanged);

         for(var2 = this.proxyStartRow; var2 < this.proxyEndRow; ++var2) {
            var6 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(var2);
            if (var6 != null) {
               ((ProxyListActivity.TextDetailProxyCell)var6.itemView).updateStatus();
            }
         }
      } else if (var2 == this.callsRow) {
         this.useProxyForCalls ^= true;
         ((TextCheckCell)var1).setChecked(this.useProxyForCalls);
         var7 = MessagesController.getGlobalMainSettings().edit();
         var7.putBoolean("proxy_enabled_calls", this.useProxyForCalls);
         var7.commit();
      } else {
         int var5 = this.proxyStartRow;
         if (var2 >= var5 && var2 < this.proxyEndRow) {
            var8 = (SharedConfig.ProxyInfo)SharedConfig.proxyList.get(var2 - var5);
            this.useProxySettings = true;
            var3 = MessagesController.getGlobalMainSettings().edit();
            var3.putString("proxy_ip", var8.address);
            var3.putString("proxy_pass", var8.password);
            var3.putString("proxy_user", var8.username);
            var3.putInt("proxy_port", var8.port);
            var3.putString("proxy_secret", var8.secret);
            var3.putBoolean("proxy_enabled", this.useProxySettings);
            if (!var8.secret.isEmpty()) {
               this.useProxyForCalls = false;
               var3.putBoolean("proxy_enabled_calls", false);
            }

            var3.commit();
            SharedConfig.currentProxy = var8;

            for(var2 = this.proxyStartRow; var2 < this.proxyEndRow; ++var2) {
               RecyclerListView.Holder var9 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(var2);
               if (var9 != null) {
                  ProxyListActivity.TextDetailProxyCell var10 = (ProxyListActivity.TextDetailProxyCell)var9.itemView;
                  if (var10.currentInfo == var8) {
                     var4 = true;
                  } else {
                     var4 = false;
                  }

                  var10.setChecked(var4);
                  var10.updateStatus();
               }
            }

            this.updateRows(false);
            var6 = (RecyclerListView.Holder)this.listView.findViewHolderForAdapterPosition(this.useProxyRow);
            if (var6 != null) {
               ((TextCheckCell)var6.itemView).setChecked(true);
            }

            var4 = this.useProxySettings;
            var8 = SharedConfig.currentProxy;
            ConnectionsManager.setProxySettings(var4, var8.address, var8.port, var8.username, var8.password, var8.secret);
         } else if (var2 == this.proxyAddRow) {
            this.presentFragment(new ProxySettingsActivity());
         }
      }

   }

   // $FF: synthetic method
   public boolean lambda$createView$2$ProxyListActivity(View var1, int var2) {
      int var3 = this.proxyStartRow;
      if (var2 >= var3 && var2 < this.proxyEndRow) {
         SharedConfig.ProxyInfo var4 = (SharedConfig.ProxyInfo)SharedConfig.proxyList.get(var2 - var3);
         AlertDialog.Builder var5 = new AlertDialog.Builder(this.getParentActivity());
         var5.setMessage(LocaleController.getString("DeleteProxy", 2131559257));
         var5.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         var5.setTitle(LocaleController.getString("AppName", 2131558635));
         var5.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$ProxyListActivity$EKG2JZVGlri4YGLLqmSPnpTdC4w(this, var4));
         this.showDialog(var5.create());
         return true;
      } else {
         return false;
      }
   }

   // $FF: synthetic method
   public void lambda$null$1$ProxyListActivity(SharedConfig.ProxyInfo var1, DialogInterface var2, int var3) {
      SharedConfig.deleteProxy(var1);
      if (SharedConfig.currentProxy == null) {
         this.useProxyForCalls = false;
         this.useProxySettings = false;
      }

      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxySettingsChanged);
      this.updateRows(true);
   }

   protected void onDialogDismiss(Dialog var1) {
      DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      SharedConfig.loadProxyList();
      this.currentConnectionState = ConnectionsManager.getInstance(super.currentAccount).getConnectionState();
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxySettingsChanged);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.proxyCheckDone);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
      SharedPreferences var1 = MessagesController.getGlobalMainSettings();
      boolean var2;
      if (var1.getBoolean("proxy_enabled", false) && !SharedConfig.proxyList.isEmpty()) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.useProxySettings = var2;
      this.useProxyForCalls = var1.getBoolean("proxy_enabled_calls", false);
      this.updateRows(true);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxyCheckDone);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
   }

   public void onResume() {
      super.onResume();
      ProxyListActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return ProxyListActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != ProxyListActivity.this.useProxyDetailRow && var1 != ProxyListActivity.this.proxyDetailRow) {
            if (var1 == ProxyListActivity.this.proxyAddRow) {
               return 1;
            } else if (var1 != ProxyListActivity.this.useProxyRow && var1 != ProxyListActivity.this.callsRow) {
               if (var1 == ProxyListActivity.this.connectionsHeaderRow) {
                  return 2;
               } else {
                  return var1 >= ProxyListActivity.this.proxyStartRow && var1 < ProxyListActivity.this.proxyEndRow ? 5 : 4;
               }
            } else {
               return 3;
            }
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 == ProxyListActivity.this.useProxyRow || var2 == ProxyListActivity.this.callsRow || var2 == ProxyListActivity.this.proxyAddRow || var2 >= ProxyListActivity.this.proxyStartRow && var2 < ProxyListActivity.this.proxyEndRow) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            boolean var4 = true;
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 3) {
                     if (var3 != 4) {
                        if (var3 == 5) {
                           ProxyListActivity.TextDetailProxyCell var6 = (ProxyListActivity.TextDetailProxyCell)var1.itemView;
                           SharedConfig.ProxyInfo var5 = (SharedConfig.ProxyInfo)SharedConfig.proxyList.get(var2 - ProxyListActivity.this.proxyStartRow);
                           var6.setProxy(var5);
                           if (SharedConfig.currentProxy != var5) {
                              var4 = false;
                           }

                           var6.setChecked(var4);
                        }
                     } else {
                        TextInfoPrivacyCell var7 = (TextInfoPrivacyCell)var1.itemView;
                        if (var2 == ProxyListActivity.this.callsDetailRow) {
                           var7.setText(LocaleController.getString("UseProxyForCallsInfo", 2131560973));
                           var7.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                     }
                  } else {
                     TextCheckCell var8 = (TextCheckCell)var1.itemView;
                     if (var2 == ProxyListActivity.this.useProxyRow) {
                        var8.setTextAndCheck(LocaleController.getString("UseProxySettings", 2131560978), ProxyListActivity.this.useProxySettings, false);
                     } else if (var2 == ProxyListActivity.this.callsRow) {
                        var8.setTextAndCheck(LocaleController.getString("UseProxyForCalls", 2131560972), ProxyListActivity.this.useProxyForCalls, false);
                     }
                  }
               } else {
                  HeaderCell var9 = (HeaderCell)var1.itemView;
                  if (var2 == ProxyListActivity.this.connectionsHeaderRow) {
                     var9.setText(LocaleController.getString("ProxyConnections", 2131560517));
                  }
               }
            } else {
               TextSettingsCell var10 = (TextSettingsCell)var1.itemView;
               var10.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
               if (var2 == ProxyListActivity.this.proxyAddRow) {
                  var10.setText(LocaleController.getString("AddProxy", 2131558581), false);
               }
            }
         } else if (var2 == ProxyListActivity.this.proxyDetailRow && ProxyListActivity.this.callsRow == -1) {
            var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
         } else {
            var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        if (var2 != 5) {
                           var3 = null;
                        } else {
                           var3 = ProxyListActivity.this.new TextDetailProxyCell(this.mContext);
                           ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                     } else {
                        var3 = new TextInfoPrivacyCell(this.mContext);
                        ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                     }
                  } else {
                     var3 = new TextCheckCell(this.mContext);
                     ((TextCheckCell)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
               } else {
                  var3 = new HeaderCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextSettingsCell(this.mContext);
               ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new ShadowSectionCell(this.mContext);
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         if (var1.getItemViewType() == 3) {
            TextCheckCell var2 = (TextCheckCell)var1.itemView;
            int var3 = var1.getAdapterPosition();
            if (var3 == ProxyListActivity.this.useProxyRow) {
               var2.setChecked(ProxyListActivity.this.useProxySettings);
            } else if (var3 == ProxyListActivity.this.callsRow) {
               var2.setChecked(ProxyListActivity.this.useProxyForCalls);
            }
         }

      }
   }

   public class TextDetailProxyCell extends FrameLayout {
      private Drawable checkDrawable;
      private ImageView checkImageView;
      private int color;
      private SharedConfig.ProxyInfo currentInfo;
      private TextView textView;
      private TextView valueTextView;

      public TextDetailProxyCell(Context var2) {
         super(var2);
         this.textView = new TextView(var2);
         this.textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
         this.textView.setTextSize(1, 16.0F);
         this.textView.setLines(1);
         this.textView.setMaxLines(1);
         this.textView.setSingleLine(true);
         this.textView.setEllipsize(TruncateAt.END);
         TextView var9 = this.textView;
         boolean var3 = LocaleController.isRTL;
         byte var4 = 5;
         byte var5;
         if (var3) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var9.setGravity(var5 | 16);
         var9 = this.textView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var3 = LocaleController.isRTL;
         byte var6 = 56;
         byte var7;
         if (var3) {
            var7 = 56;
         } else {
            var7 = 21;
         }

         float var8 = (float)var7;
         if (LocaleController.isRTL) {
            var7 = 21;
         } else {
            var7 = 56;
         }

         this.addView(var9, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var8, 10.0F, (float)var7, 0.0F));
         this.valueTextView = new TextView(var2);
         this.valueTextView.setTextSize(1, 13.0F);
         var9 = this.valueTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         var9.setGravity(var5);
         this.valueTextView.setLines(1);
         this.valueTextView.setMaxLines(1);
         this.valueTextView.setSingleLine(true);
         this.valueTextView.setCompoundDrawablePadding(AndroidUtilities.dp(6.0F));
         this.valueTextView.setEllipsize(TruncateAt.END);
         this.valueTextView.setPadding(0, 0, 0, 0);
         var9 = this.valueTextView;
         if (LocaleController.isRTL) {
            var5 = 5;
         } else {
            var5 = 3;
         }

         if (LocaleController.isRTL) {
            var7 = 56;
         } else {
            var7 = 21;
         }

         var8 = (float)var7;
         var7 = var6;
         if (LocaleController.isRTL) {
            var7 = 21;
         }

         this.addView(var9, LayoutHelper.createFrame(-2, -2.0F, var5 | 48, var8, 35.0F, (float)var7, 0.0F));
         this.checkImageView = new ImageView(var2);
         this.checkImageView.setImageResource(2131165786);
         this.checkImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteGrayText3"), Mode.MULTIPLY));
         this.checkImageView.setScaleType(ScaleType.CENTER);
         this.checkImageView.setContentDescription(LocaleController.getString("Edit", 2131559301));
         ImageView var10 = this.checkImageView;
         var5 = var4;
         if (LocaleController.isRTL) {
            var5 = 3;
         }

         this.addView(var10, LayoutHelper.createFrame(48, 48.0F, var5 | 48, 8.0F, 8.0F, 8.0F, 0.0F));
         this.checkImageView.setOnClickListener(new _$$Lambda$ProxyListActivity$TextDetailProxyCell$X7y0ocxBETGvRg05YaI__kXxQ00(this));
         this.setWillNotDraw(false);
      }

      // $FF: synthetic method
      public void lambda$new$0$ProxyListActivity$TextDetailProxyCell(View var1) {
         ProxyListActivity.this.presentFragment(new ProxySettingsActivity(this.currentInfo));
      }

      protected void onAttachedToWindow() {
         super.onAttachedToWindow();
         this.updateStatus();
      }

      protected void onDraw(Canvas var1) {
         float var2;
         if (LocaleController.isRTL) {
            var2 = 0.0F;
         } else {
            var2 = (float)AndroidUtilities.dp(20.0F);
         }

         float var3 = (float)(this.getMeasuredHeight() - 1);
         int var4 = this.getMeasuredWidth();
         int var5;
         if (LocaleController.isRTL) {
            var5 = AndroidUtilities.dp(20.0F);
         } else {
            var5 = 0;
         }

         var1.drawLine(var2, var3, (float)(var4 - var5), (float)(this.getMeasuredHeight() - 1), Theme.dividerPaint);
      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(var1), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(64.0F) + 1, 1073741824));
      }

      public void setChecked(boolean var1) {
         if (var1) {
            if (this.checkDrawable == null) {
               this.checkDrawable = this.getResources().getDrawable(2131165794).mutate();
            }

            Drawable var2 = this.checkDrawable;
            if (var2 != null) {
               var2.setColorFilter(new PorterDuffColorFilter(this.color, Mode.MULTIPLY));
            }

            if (LocaleController.isRTL) {
               this.valueTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.checkDrawable, (Drawable)null);
            } else {
               this.valueTextView.setCompoundDrawablesWithIntrinsicBounds(this.checkDrawable, (Drawable)null, (Drawable)null, (Drawable)null);
            }
         } else {
            this.valueTextView.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
         }

      }

      public void setProxy(SharedConfig.ProxyInfo var1) {
         TextView var2 = this.textView;
         StringBuilder var3 = new StringBuilder();
         var3.append(var1.address);
         var3.append(":");
         var3.append(var1.port);
         var2.setText(var3.toString());
         this.currentInfo = var1;
      }

      public void setValue(CharSequence var1) {
         this.valueTextView.setText(var1);
      }

      public void updateStatus() {
         SharedConfig.ProxyInfo var1 = SharedConfig.currentProxy;
         SharedConfig.ProxyInfo var2 = this.currentInfo;
         String var3 = "windowBackgroundWhiteGrayText2";
         if (var1 == var2 && ProxyListActivity.this.useProxySettings) {
            if (ProxyListActivity.this.currentConnectionState != 3 && ProxyListActivity.this.currentConnectionState != 5) {
               this.valueTextView.setText(LocaleController.getString("Connecting", 2131559137));
            } else {
               if (this.currentInfo.ping != 0L) {
                  TextView var7 = this.valueTextView;
                  StringBuilder var5 = new StringBuilder();
                  var5.append(LocaleController.getString("Connected", 2131559136));
                  var5.append(", ");
                  var5.append(LocaleController.formatString("Ping", 2131560449, this.currentInfo.ping));
                  var7.setText(var5.toString());
               } else {
                  this.valueTextView.setText(LocaleController.getString("Connected", 2131559136));
               }

               SharedConfig.ProxyInfo var8 = this.currentInfo;
               if (!var8.checking && !var8.available) {
                  var8.availableCheckTime = 0L;
               }

               var3 = "windowBackgroundWhiteBlueText6";
            }
         } else {
            var2 = this.currentInfo;
            if (var2.checking) {
               this.valueTextView.setText(LocaleController.getString("Checking", 2131559085));
            } else if (var2.available) {
               if (var2.ping != 0L) {
                  TextView var4 = this.valueTextView;
                  StringBuilder var6 = new StringBuilder();
                  var6.append(LocaleController.getString("Available", 2131558807));
                  var6.append(", ");
                  var6.append(LocaleController.formatString("Ping", 2131560449, this.currentInfo.ping));
                  var4.setText(var6.toString());
               } else {
                  this.valueTextView.setText(LocaleController.getString("Available", 2131558807));
               }

               var3 = "windowBackgroundWhiteGreenText";
            } else {
               this.valueTextView.setText(LocaleController.getString("Unavailable", 2131560929));
               var3 = "windowBackgroundWhiteRedText4";
            }
         }

         this.color = Theme.getColor(var3);
         this.valueTextView.setTag(var3);
         this.valueTextView.setTextColor(this.color);
         Drawable var9 = this.checkDrawable;
         if (var9 != null) {
            var9.setColorFilter(new PorterDuffColorFilter(this.color, Mode.MULTIPLY));
         }

      }
   }
}
