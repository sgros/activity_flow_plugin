package org.telegram.ui.Adapters;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LocationActivity;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Cells.LocationCell;
import org.telegram.ui.Cells.LocationLoadingCell;
import org.telegram.ui.Cells.LocationPoweredCell;
import org.telegram.ui.Cells.SendLocationCell;
import org.telegram.ui.Cells.SharingLiveLocationCell;
import org.telegram.ui.Components.RecyclerListView;

public class LocationActivityAdapter extends BaseLocationAdapter {
   private int currentAccount;
   private ArrayList currentLiveLocations;
   private MessageObject currentMessageObject;
   private Location customLocation;
   private long dialogId;
   private Location gpsLocation;
   private int liveLocationType;
   private Context mContext;
   private int overScrollHeight;
   private boolean pulledUp;
   private SendLocationCell sendLocationCell;
   private int shareLiveLocationPotistion;

   public LocationActivityAdapter(Context var1, int var2, long var3) {
      this.currentAccount = UserConfig.selectedAccount;
      this.shareLiveLocationPotistion = -1;
      this.currentLiveLocations = new ArrayList();
      this.mContext = var1;
      this.liveLocationType = var2;
      this.dialogId = var3;
   }

   private void updateCell() {
      SendLocationCell var1 = this.sendLocationCell;
      if (var1 != null) {
         if (this.customLocation != null) {
            var1.setText(LocaleController.getString("SendSelectedLocation", 2131560706), String.format(Locale.US, "(%f,%f)", this.customLocation.getLatitude(), this.customLocation.getLongitude()));
         } else if (this.gpsLocation != null) {
            var1.setText(LocaleController.getString("SendLocation", 2131560700), LocaleController.formatString("AccurateTo", 2131558490, LocaleController.formatPluralString("Meters", (int)this.gpsLocation.getAccuracy())));
         } else {
            var1.setText(LocaleController.getString("SendLocation", 2131560700), LocaleController.getString("Loading", 2131559768));
         }
      }

   }

   public Object getItem(int var1) {
      MessageObject var2 = this.currentMessageObject;
      if (var2 != null) {
         if (var1 == 1) {
            return var2;
         }

         if (var1 > 3 && var1 < super.places.size() + 3) {
            return this.currentLiveLocations.get(var1 - 4);
         }
      } else {
         int var3 = this.liveLocationType;
         if (var3 == 2) {
            if (var1 >= 2) {
               return this.currentLiveLocations.get(var1 - 2);
            }

            return null;
         }

         if (var3 == 1) {
            if (var1 > 3 && var1 < super.places.size() + 4) {
               return super.places.get(var1 - 4);
            }
         } else if (var1 > 2 && var1 < super.places.size() + 3) {
            return super.places.get(var1 - 3);
         }
      }

      return null;
   }

   public int getItemCount() {
      if (this.currentMessageObject != null) {
         int var3;
         if (this.currentLiveLocations.isEmpty()) {
            var3 = 0;
         } else {
            var3 = this.currentLiveLocations.size() + 2;
         }

         return var3 + 2;
      } else if (this.liveLocationType == 2) {
         return this.currentLiveLocations.size() + 2;
      } else {
         boolean var2 = super.searching;
         byte var1 = 4;
         if (var2 || !var2 && super.places.isEmpty()) {
            if (this.liveLocationType != 0) {
               var1 = 5;
            }

            return var1;
         } else {
            return this.liveLocationType == 1 ? super.places.size() + 4 + (super.places.isEmpty() ^ 1) : super.places.size() + 3 + (super.places.isEmpty() ^ 1);
         }
      }
   }

   public int getItemViewType(int var1) {
      if (var1 == 0) {
         return 0;
      } else if (this.currentMessageObject != null) {
         if (var1 == 2) {
            return 2;
         } else if (var1 == 3) {
            this.shareLiveLocationPotistion = var1;
            return 6;
         } else {
            return 7;
         }
      } else {
         int var2 = this.liveLocationType;
         if (var2 == 2) {
            if (var1 == 1) {
               this.shareLiveLocationPotistion = var1;
               return 6;
            } else {
               return 7;
            }
         } else {
            boolean var3;
            if (var2 == 1) {
               if (var1 == 1) {
                  return 1;
               }

               if (var1 == 2) {
                  this.shareLiveLocationPotistion = var1;
                  return 6;
               }

               if (var1 == 3) {
                  return 2;
               }

               var3 = super.searching;
               if (var3 || !var3 && super.places.isEmpty()) {
                  return 4;
               }

               if (var1 == super.places.size() + 4) {
                  return 5;
               }
            } else {
               if (var1 == 1) {
                  return 1;
               }

               if (var1 == 2) {
                  return 2;
               }

               var3 = super.searching;
               if (var3 || !var3 && super.places.isEmpty()) {
                  return 4;
               }

               if (var1 == super.places.size() + 3) {
                  return 5;
               }
            }

            return 3;
         }
      }
   }

   public boolean isEnabled(RecyclerView.ViewHolder var1) {
      int var2 = var1.getItemViewType();
      boolean var3 = false;
      boolean var4 = false;
      if (var2 == 6) {
         if (LocationController.getInstance(this.currentAccount).getSharingLocationInfo(this.dialogId) != null || this.gpsLocation != null) {
            var4 = true;
         }

         return var4;
      } else {
         if (var2 != 1 && var2 != 3) {
            var4 = var3;
            if (var2 != 7) {
               return var4;
            }
         }

         var4 = true;
         return var4;
      }
   }

   public boolean isPulledUp() {
      return this.pulledUp;
   }

   // $FF: synthetic method
   public void lambda$setPulledUp$0$LocationActivityAdapter() {
      byte var1;
      if (this.liveLocationType == 0) {
         var1 = 2;
      } else {
         var1 = 3;
      }

      this.notifyItemChanged(var1);
   }

   public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
      int var3 = var1.getItemViewType();
      if (var3 != 0) {
         boolean var4 = true;
         if (var3 != 1) {
            byte var5 = 2;
            if (var3 != 2) {
               ArrayList var7;
               if (var3 != 3) {
                  if (var3 != 4) {
                     if (var3 != 6) {
                        if (var3 == 7) {
                           MessageObject var6 = this.currentMessageObject;
                           if (var6 != null && var2 == 1) {
                              ((SharingLiveLocationCell)var1.itemView).setDialog(var6, this.gpsLocation);
                           } else {
                              SharingLiveLocationCell var9 = (SharingLiveLocationCell)var1.itemView;
                              var7 = this.currentLiveLocations;
                              if (this.currentMessageObject != null) {
                                 var5 = 4;
                              }

                              var9.setDialog((LocationActivity.LiveLocation)var7.get(var2 - var5), this.gpsLocation);
                           }
                        }
                     } else {
                        SendLocationCell var8 = (SendLocationCell)var1.itemView;
                        if (this.gpsLocation == null) {
                           var4 = false;
                        }

                        var8.setHasLocation(var4);
                     }
                  } else {
                     ((LocationLoadingCell)var1.itemView).setLoading(super.searching);
                  }
               } else {
                  LocationCell var10;
                  if (this.liveLocationType == 0) {
                     var10 = (LocationCell)var1.itemView;
                     var7 = super.places;
                     var2 -= 3;
                     var10.setLocation((TLRPC.TL_messageMediaVenue)var7.get(var2), (String)super.iconUrls.get(var2), true);
                  } else {
                     var10 = (LocationCell)var1.itemView;
                     var7 = super.places;
                     var2 -= 4;
                     var10.setLocation((TLRPC.TL_messageMediaVenue)var7.get(var2), (String)super.iconUrls.get(var2), true);
                  }
               }
            } else if (this.currentMessageObject != null) {
               ((GraySectionCell)var1.itemView).setText(LocaleController.getString("LiveLocations", 2131559767));
            } else if (this.pulledUp) {
               ((GraySectionCell)var1.itemView).setText(LocaleController.getString("NearbyPlaces", 2131559888));
            } else {
               ((GraySectionCell)var1.itemView).setText(LocaleController.getString("ShowNearbyPlaces", 2131560781));
            }
         } else {
            this.sendLocationCell = (SendLocationCell)var1.itemView;
            this.updateCell();
         }
      } else {
         ((EmptyCell)var1.itemView).setHeight(this.overScrollHeight);
      }

   }

   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
      Object var3;
      switch(var2) {
      case 0:
         var3 = new EmptyCell(this.mContext);
         break;
      case 1:
         var3 = new SendLocationCell(this.mContext, false);
         break;
      case 2:
         var3 = new GraySectionCell(this.mContext);
         break;
      case 3:
         var3 = new LocationCell(this.mContext);
         break;
      case 4:
         var3 = new LocationLoadingCell(this.mContext);
         break;
      case 5:
         var3 = new LocationPoweredCell(this.mContext);
         break;
      case 6:
         var3 = new SendLocationCell(this.mContext, true);
         ((SendLocationCell)var3).setDialogId(this.dialogId);
         break;
      default:
         var3 = new SharingLiveLocationCell(this.mContext, true);
      }

      return new RecyclerListView.Holder((View)var3);
   }

   public void setCustomLocation(Location var1) {
      this.customLocation = var1;
      this.updateCell();
   }

   public void setGpsLocation(Location var1) {
      boolean var2;
      if (this.gpsLocation == null) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.gpsLocation = var1;
      if (var2) {
         int var3 = this.shareLiveLocationPotistion;
         if (var3 > 0) {
            this.notifyItemChanged(var3);
         }
      }

      if (this.currentMessageObject != null) {
         this.notifyItemChanged(1);
         this.updateLiveLocations();
      } else if (this.liveLocationType != 2) {
         this.updateCell();
      } else {
         this.updateLiveLocations();
      }

   }

   public void setLiveLocations(ArrayList var1) {
      this.currentLiveLocations = new ArrayList(var1);
      int var2 = UserConfig.getInstance(this.currentAccount).getClientUserId();

      for(int var3 = 0; var3 < this.currentLiveLocations.size(); ++var3) {
         if (((LocationActivity.LiveLocation)this.currentLiveLocations.get(var3)).id == var2) {
            this.currentLiveLocations.remove(var3);
            break;
         }
      }

      this.notifyDataSetChanged();
   }

   public void setMessageObject(MessageObject var1) {
      this.currentMessageObject = var1;
      this.notifyDataSetChanged();
   }

   public void setOverScrollHeight(int var1) {
      this.overScrollHeight = var1;
   }

   public void setPulledUp() {
      if (!this.pulledUp) {
         this.pulledUp = true;
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocationActivityAdapter$kPn2n4u9DAIYsBitfbC_1mB05AA(this));
      }
   }

   public void updateLiveLocations() {
      if (!this.currentLiveLocations.isEmpty()) {
         this.notifyItemRangeChanged(2, this.currentLiveLocations.size());
      }

   }
}
