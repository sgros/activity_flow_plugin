package org.mozilla.focus.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.mozilla.focus.download.DownloadInfo;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.rocket.download.DownloadInfoViewModel;
import org.mozilla.threadutils.ThreadUtils;

public class DownloadListAdapter extends RecyclerView.Adapter {
   private static final List SPECIFIC_FILE_EXTENSION = Arrays.asList("apk", "zip", "gz", "tar", "7z", "rar", "war");
   private Context mContext;
   private List mDownloadInfo;
   private DownloadInfoViewModel viewModel;

   public DownloadListAdapter(Context var1, DownloadInfoViewModel var2) {
      this.mContext = var1;
      this.viewModel = var2;
      this.viewModel.loadMore(true);
      this.viewModel.setOpening(true);
   }

   // $FF: synthetic method
   public static boolean lambda$null$0(DownloadListAdapter var0, long var1, PopupMenu var3, MenuItem var4) {
      int var5 = var4.getItemId();
      if (var5 != 2131296396) {
         if (var5 != 2131296587) {
            return false;
         } else {
            var0.viewModel.remove(var1);
            TelemetryWrapper.downloadRemoveFile();
            var3.dismiss();
            return true;
         }
      } else {
         var0.viewModel.delete(var1);
         TelemetryWrapper.downloadDeleteFile();
         var3.dismiss();
         return true;
      }
   }

   // $FF: synthetic method
   public static void lambda$null$2(DownloadListAdapter var0, boolean var1, View var2, DownloadInfo var3) {
      if (var1) {
         IntentUtils.intentOpenFile(var2.getContext(), var3.getFileUri(), var3.getMimeType());
      } else {
         Toast.makeText(var0.mContext, 2131755082, 1).show();
      }

   }

   // $FF: synthetic method
   public static void lambda$null$3(DownloadListAdapter var0, DownloadInfo var1, View var2) {
      ThreadUtils.postToMainThread(new _$$Lambda$DownloadListAdapter$PGBJJr8kA8FE32gnuRZ8hPUh4vc(var0, (new File(URI.create(var1.getFileUri()).getPath())).exists(), var2, var1));
   }

   // $FF: synthetic method
   public static void lambda$onBindViewHolder$1(DownloadListAdapter var0, View var1) {
      long var2 = (Long)var1.getTag(2131296597);
      if ((Integer)var1.getTag(2131296669) == 2) {
         var0.viewModel.cancel(var2);
      } else {
         PopupMenu var4 = new PopupMenu(var1.getContext(), var1);
         var4.getMenuInflater().inflate(2131558403, var4.getMenu());
         var4.setOnMenuItemClickListener(new _$$Lambda$DownloadListAdapter$2RfKvQKet9_QfTnm5kGkUXuOnrw(var0, var2, var4));
         var4.show();
      }

      TelemetryWrapper.showFileContextMenu();
   }

   // $FF: synthetic method
   public static void lambda$onBindViewHolder$4(DownloadListAdapter var0, View var1) {
      DownloadInfo var2 = (DownloadInfo)var1.getTag();
      if (var2.getStatus() == 8) {
         TelemetryWrapper.downloadOpenFile(false);
         ThreadUtils.postToBackgroundThread((Runnable)(new _$$Lambda$DownloadListAdapter$qK4PYdp6P8b6gF442lg254YtYgw(var0, var2, var1)));
      }
   }

   private int mappingIcon(DownloadInfo var1) {
      if (SPECIFIC_FILE_EXTENSION.contains(var1.getFileExtension())) {
         int var6;
         if ("apk".equals(var1.getFileExtension())) {
            var6 = 2131230859;
         } else {
            var6 = 2131230860;
         }

         return var6;
      } else if (TextUtils.isEmpty(var1.getMimeType())) {
         return 2131230861;
      } else {
         byte var2;
         label44: {
            String var3 = var1.getMimeType();
            int var4 = var1.getMimeType().indexOf("/");
            var2 = 0;
            String var5 = var3.substring(0, var4);
            var4 = var5.hashCode();
            if (var4 != 3556653) {
               if (var4 != 93166550) {
                  if (var4 != 100313435) {
                     if (var4 == 112202875 && var5.equals("video")) {
                        var2 = 3;
                        break label44;
                     }
                  } else if (var5.equals("image")) {
                     var2 = 1;
                     break label44;
                  }
               } else if (var5.equals("audio")) {
                  var2 = 2;
                  break label44;
               }
            } else if (var5.equals("text")) {
               break label44;
            }

            var2 = -1;
         }

         switch(var2) {
         case 0:
            return 2131230861;
         case 1:
            return 2131230862;
         case 2:
            return 2131230863;
         case 3:
            return 2131230864;
         default:
            return 2131230861;
         }
      }
   }

   private String statusConvertStr(int var1) {
      if (var1 != 4) {
         if (var1 != 8) {
            if (var1 != 16) {
               switch(var1) {
               case 1:
                  return this.mContext.getResources().getString(2131755286);
               case 2:
                  return this.mContext.getResources().getString(2131755373);
               default:
                  return this.mContext.getResources().getString(2131755425);
               }
            } else {
               return this.mContext.getResources().getString(2131755195);
            }
         } else {
            return this.mContext.getResources().getString(2131755414);
         }
      } else {
         return this.mContext.getResources().getString(2131755285);
      }
   }

   public int getItemCount() {
      return this.mDownloadInfo != null && !this.mDownloadInfo.isEmpty() ? this.mDownloadInfo.size() : 1;
   }

   public int getItemViewType(int var1) {
      if (this.viewModel.isOpening()) {
         return 2;
      } else {
         return this.mDownloadInfo != null && !this.mDownloadInfo.isEmpty() ? 1 : 0;
      }
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      if (var1 instanceof DownloadListAdapter.DownloadViewHolder) {
         DownloadListAdapter.DownloadViewHolder var3 = (DownloadListAdapter.DownloadViewHolder)var1;
         DownloadInfo var4 = (DownloadInfo)this.mDownloadInfo.get(var2);
         if (!TextUtils.isEmpty(var4.getFileName())) {
            var3.title.setText(var4.getFileName());
         } else {
            var3.title.setText(2131755425);
         }

         var3.icon.setImageResource(this.mappingIcon(var4));
         StringBuilder var5;
         String var6;
         if (8 == var4.getStatus()) {
            var5 = new StringBuilder();
            var5.append(var4.getSize());
            var5.append(", ");
            var5.append(var4.getDate());
            var6 = var5.toString();
            var3.progressBar.setVisibility(8);
            var3.action.setImageLevel(0);
         } else if (2 == var4.getStatus()) {
            var2 = (int)(var4.getSizeSoFar() * 100.0D / var4.getSizeTotal());
            var3.progressBar.setProgress(var2);
            var3.progressBar.setVisibility(0);
            var5 = new StringBuilder();
            var5.append(var2);
            var5.append("%");
            var6 = var5.toString();
            var3.action.setImageLevel(1);
         } else {
            var6 = this.statusConvertStr(var4.getStatus());
            var3.progressBar.setVisibility(8);
            var3.action.setImageLevel(0);
         }

         var3.subtitle.setText(var6);
         var3.action.setTag(2131296669, var4.getStatus());
         var3.action.setTag(2131296597, var4.getRowId());
         var3.action.setOnClickListener(new _$$Lambda$DownloadListAdapter$0rq30Ts28y_ROX4x8guNtYgA6uM(this));
         var3.itemView.setTag(var4);
         var3.itemView.setOnClickListener(new _$$Lambda$DownloadListAdapter$rmqxPk6X72MzbSacIqXzLvkcKPY(this));
      } else if (var1 instanceof DownloadListAdapter.OnOpeningViewHolder) {
         var1.itemView.setVisibility(8);
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      if (1 == var2) {
         return new DownloadListAdapter.DownloadViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492946, var1, false));
      } else {
         return (RecyclerView.ViewHolder)(2 == var2 ? new DownloadListAdapter.OnOpeningViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492944, var1, false)) : new DownloadListAdapter.DownloadEmptyViewHolder(LayoutInflater.from(var1.getContext()).inflate(2131492944, var1, false)));
      }
   }

   public void setList(List var1) {
      if (this.mDownloadInfo == null) {
         this.mDownloadInfo = var1;
      }

   }

   public static class DownloadEmptyViewHolder extends RecyclerView.ViewHolder {
      ImageView imag;

      DownloadEmptyViewHolder(View var1) {
         super(var1);
         this.imag = (ImageView)var1.findViewById(2131296480);
      }
   }

   public static class DownloadViewHolder extends RecyclerView.ViewHolder {
      ImageView action;
      ImageView icon;
      ProgressBar progressBar;
      TextView subtitle;
      TextView title;

      DownloadViewHolder(View var1) {
         super(var1);
         this.icon = (ImageView)var1.findViewById(2131296480);
         this.title = (TextView)var1.findViewById(2131296697);
         this.subtitle = (TextView)var1.findViewById(2131296675);
         this.action = (ImageView)var1.findViewById(2131296504);
         this.progressBar = (ProgressBar)var1.findViewById(2131296576);
      }
   }

   public static class OnOpeningViewHolder extends RecyclerView.ViewHolder {
      OnOpeningViewHolder(View var1) {
         super(var1);
         var1.setVisibility(8);
      }
   }
}
