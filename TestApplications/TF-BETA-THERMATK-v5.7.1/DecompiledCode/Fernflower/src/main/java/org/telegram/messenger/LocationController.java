package org.telegram.messenger;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseIntArray;
import java.util.ArrayList;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class LocationController implements NotificationCenter.NotificationCenterDelegate {
   private static final int BACKGROUD_UPDATE_TIME = 90000;
   private static final int FOREGROUND_UPDATE_TIME = 20000;
   private static volatile LocationController[] Instance = new LocationController[3];
   private static final int LOCATION_ACQUIRE_TIME = 10000;
   private static final double eps = 1.0E-4D;
   private LongSparseArray cacheRequests = new LongSparseArray();
   private int currentAccount;
   private LocationController.GpsLocationListener gpsLocationListener = new LocationController.GpsLocationListener();
   private Location lastKnownLocation;
   private boolean lastLocationByGoogleMaps;
   private long lastLocationSendTime;
   private long lastLocationStartTime;
   private LocationManager locationManager;
   private boolean locationSentSinceLastGoogleMapUpdate = true;
   public LongSparseArray locationsCache = new LongSparseArray();
   private LocationController.GpsLocationListener networkLocationListener = new LocationController.GpsLocationListener();
   private LocationController.GpsLocationListener passiveLocationListener = new LocationController.GpsLocationListener();
   private SparseIntArray requests = new SparseIntArray();
   private ArrayList sharingLocations = new ArrayList();
   private LongSparseArray sharingLocationsMap = new LongSparseArray();
   private LongSparseArray sharingLocationsMapUI = new LongSparseArray();
   public ArrayList sharingLocationsUI = new ArrayList();
   private boolean started;

   public LocationController(int var1) {
      this.currentAccount = var1;
      this.locationManager = (LocationManager)ApplicationLoader.applicationContext.getSystemService("location");
      AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$jwDhs2Wxth9unque4gfUrSG9YJ8(this));
      this.loadSharingLocations();
   }

   private void broadcastLastKnownLocation() {
      if (this.lastKnownLocation != null) {
         int var1;
         if (this.requests.size() != 0) {
            for(var1 = 0; var1 < this.requests.size(); ++var1) {
               ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.requests.keyAt(var1), false);
            }

            this.requests.clear();
         }

         int var2 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();

         for(var1 = 0; var1 < this.sharingLocations.size(); ++var1) {
            LocationController.SharingLocationInfo var3 = (LocationController.SharingLocationInfo)this.sharingLocations.get(var1);
            TLRPC.Message var4 = var3.messageObject.messageOwner;
            TLRPC.MessageMedia var5 = var4.media;
            if (var5 != null && var5.geo != null) {
               int var6 = var4.edit_date;
               if (var6 == 0) {
                  var6 = var4.date;
               }

               TLRPC.GeoPoint var9 = var3.messageObject.messageOwner.media.geo;
               if (Math.abs(var2 - var6) < 30 && Math.abs(var9.lat - this.lastKnownLocation.getLatitude()) <= 1.0E-4D && Math.abs(var9._long - this.lastKnownLocation.getLongitude()) <= 1.0E-4D) {
                  continue;
               }
            }

            TLRPC.TL_messages_editMessage var10 = new TLRPC.TL_messages_editMessage();
            var10.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var3.did);
            var10.id = var3.mid;
            var10.flags |= 16384;
            var10.media = new TLRPC.TL_inputMediaGeoLive();
            TLRPC.InputMedia var7 = var10.media;
            var7.stopped = false;
            var7.geo_point = new TLRPC.TL_inputGeoPoint();
            var10.media.geo_point.lat = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLatitude());
            var10.media.geo_point._long = AndroidUtilities.fixLocationCoord(this.lastKnownLocation.getLongitude());
            int[] var8 = new int[1];
            var8[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var10, new _$$Lambda$LocationController$GgGvUosGIle_dcnPqBDWFGDVM7A(this, var3, var8));
            this.requests.put(var8[0], 0);
         }

         ConnectionsManager.getInstance(this.currentAccount).resumeNetworkMaybe();
         this.stop(false);
      }
   }

   public static LocationController getInstance(int var0) {
      LocationController var1 = Instance[var0];
      LocationController var2 = var1;
      if (var1 == null) {
         synchronized(LocationController.class){}

         Throwable var10000;
         boolean var10001;
         label216: {
            try {
               var1 = Instance[var0];
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            var2 = var1;
            if (var1 == null) {
               LocationController[] var23;
               try {
                  var23 = Instance;
                  var2 = new LocationController(var0);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label216;
               }

               var23[var0] = var2;
            }

            label202:
            try {
               return var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label202;
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var2;
      }
   }

   public static int getLocationsCount() {
      int var0 = 0;

      int var1;
      for(var1 = 0; var0 < 3; ++var0) {
         var1 += getInstance(var0).sharingLocationsUI.size();
      }

      return var1;
   }

   private void loadSharingLocations() {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$LocationController$CedxaXVEsRh3MfbiwLY0cCEiBNY(this));
   }

   private void saveSharingLocation(LocationController.SharingLocationInfo var1, int var2) {
      MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$LocationController$yQY9F3qag_AziWeeanO81WIpkbw(this, var2, var1));
   }

   private void start() {
      if (!this.started) {
         this.lastLocationStartTime = System.currentTimeMillis();
         this.started = true;

         try {
            this.locationManager.requestLocationUpdates("gps", 1L, 0.0F, this.gpsLocationListener);
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }

         try {
            this.locationManager.requestLocationUpdates("network", 1L, 0.0F, this.networkLocationListener);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         try {
            this.locationManager.requestLocationUpdates("passive", 1L, 0.0F, this.passiveLocationListener);
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         if (this.lastKnownLocation == null) {
            try {
               this.lastKnownLocation = this.locationManager.getLastKnownLocation("gps");
               if (this.lastKnownLocation == null) {
                  this.lastKnownLocation = this.locationManager.getLastKnownLocation("network");
               }
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
            }
         }

      }
   }

   private void startService() {
      try {
         Context var1 = ApplicationLoader.applicationContext;
         Intent var2 = new Intent(ApplicationLoader.applicationContext, LocationSharingService.class);
         var1.startService(var2);
      } catch (Throwable var3) {
         FileLog.e(var3);
      }

   }

   private void stop(boolean var1) {
      this.started = false;
      this.locationManager.removeUpdates(this.gpsLocationListener);
      if (var1) {
         this.locationManager.removeUpdates(this.networkLocationListener);
         this.locationManager.removeUpdates(this.passiveLocationListener);
      }

   }

   private void stopService() {
      ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, LocationSharingService.class));
   }

   protected void addSharingLocation(long var1, int var3, int var4, TLRPC.Message var5) {
      LocationController.SharingLocationInfo var6 = new LocationController.SharingLocationInfo();
      var6.did = var1;
      var6.mid = var3;
      var6.period = var4;
      var6.messageObject = new MessageObject(this.currentAccount, var5, false);
      var6.stopTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + var4;
      LocationController.SharingLocationInfo var7 = (LocationController.SharingLocationInfo)this.sharingLocationsMap.get(var1);
      this.sharingLocationsMap.put(var1, var6);
      if (var7 != null) {
         this.sharingLocations.remove(var7);
      }

      this.sharingLocations.add(var6);
      this.saveSharingLocation(var6, 0);
      this.lastLocationSendTime = System.currentTimeMillis() - 90000L + 5000L;
      AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$zAJ9cmnQja1jAmGuw23_E99HA_0(this, var7, var6));
   }

   public void cleanup() {
      this.sharingLocationsUI.clear();
      this.sharingLocationsMapUI.clear();
      this.locationsCache.clear();
      this.cacheRequests.clear();
      this.stopService();
      Utilities.stageQueue.postRunnable(new _$$Lambda$LocationController$CqwMtWkVqaOCz_mvGUr_tzsuYCI(this));
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      var2 = NotificationCenter.didReceiveNewMessages;
      byte var4 = 0;
      long var5;
      ArrayList var7;
      MessageObject var8;
      boolean var11;
      ArrayList var12;
      if (var1 == var2) {
         var5 = (Long)var3[0];
         if (!this.isSharingLocation(var5)) {
            return;
         }

         var7 = (ArrayList)this.locationsCache.get(var5);
         if (var7 == null) {
            return;
         }

         var12 = (ArrayList)var3[1];
         var1 = 0;

         for(var11 = false; var1 < var12.size(); ++var1) {
            var8 = (MessageObject)var12.get(var1);
            if (var8.isLiveLocation()) {
               var2 = 0;

               while(true) {
                  if (var2 >= var7.size()) {
                     var11 = false;
                     break;
                  }

                  int var13 = ((TLRPC.Message)var7.get(var2)).from_id;
                  TLRPC.Message var9 = var8.messageOwner;
                  if (var13 == var9.from_id) {
                     var7.set(var2, var9);
                     var11 = true;
                     break;
                  }

                  ++var2;
               }

               if (!var11) {
                  var7.add(var8.messageOwner);
               }

               var11 = true;
            }
         }

         if (var11) {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsCacheChanged, var5, this.currentAccount);
         }
      } else {
         int var10;
         if (var1 == NotificationCenter.messagesDeleted) {
            if (!this.sharingLocationsUI.isEmpty()) {
               ArrayList var16 = (ArrayList)var3[0];
               var10 = (Integer)var3[1];
               var12 = null;

               for(var1 = 0; var1 < this.sharingLocationsUI.size(); var12 = var7) {
                  LocationController.SharingLocationInfo var17 = (LocationController.SharingLocationInfo)this.sharingLocationsUI.get(var1);
                  MessageObject var15 = var17.messageObject;
                  if (var15 != null) {
                     var2 = var15.getChannelId();
                  } else {
                     var2 = 0;
                  }

                  if (var10 != var2) {
                     var7 = var12;
                  } else {
                     var7 = var12;
                     if (var16.contains(var17.mid)) {
                        var7 = var12;
                        if (var12 == null) {
                           var7 = new ArrayList();
                        }

                        var7.add(var17.did);
                     }
                  }

                  ++var1;
               }

               if (var12 != null) {
                  for(var1 = var4; var1 < var12.size(); ++var1) {
                     this.removeSharingLocation((Long)var12.get(var1));
                  }
               }
            }
         } else if (var1 == NotificationCenter.replaceMessagesObjects) {
            var5 = (Long)var3[0];
            if (!this.isSharingLocation(var5)) {
               return;
            }

            var7 = (ArrayList)this.locationsCache.get(var5);
            if (var7 == null) {
               return;
            }

            var12 = (ArrayList)var3[1];
            var1 = 0;

            boolean var14;
            for(var11 = false; var1 < var12.size(); var11 = var14) {
               var8 = (MessageObject)var12.get(var1);
               var10 = 0;

               while(true) {
                  var14 = var11;
                  if (var10 >= var7.size()) {
                     break;
                  }

                  if (((TLRPC.Message)var7.get(var10)).from_id == var8.messageOwner.from_id) {
                     if (!var8.isLiveLocation()) {
                        var7.remove(var10);
                     } else {
                        var7.set(var10, var8.messageOwner);
                     }

                     var14 = true;
                     break;
                  }

                  ++var10;
               }

               ++var1;
            }

            if (var11) {
               NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsCacheChanged, var5, this.currentAccount);
            }
         }
      }

   }

   public LocationController.SharingLocationInfo getSharingLocationInfo(long var1) {
      return (LocationController.SharingLocationInfo)this.sharingLocationsMapUI.get(var1);
   }

   public boolean isSharingLocation(long var1) {
      boolean var3;
      if (this.sharingLocationsMapUI.indexOfKey(var1) >= 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   // $FF: synthetic method
   public void lambda$addSharingLocation$5$LocationController(LocationController.SharingLocationInfo var1, LocationController.SharingLocationInfo var2) {
      if (var1 != null) {
         this.sharingLocationsUI.remove(var1);
      }

      this.sharingLocationsUI.add(var2);
      this.sharingLocationsMapUI.put(var2.did, var2);
      this.startService();
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsChanged);
   }

   // $FF: synthetic method
   public void lambda$broadcastLastKnownLocation$2$LocationController(LocationController.SharingLocationInfo var1, int[] var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 != null) {
         if (var4.text.equals("MESSAGE_ID_INVALID")) {
            this.sharingLocations.remove(var1);
            this.sharingLocationsMap.remove(var1.did);
            this.saveSharingLocation(var1, 1);
            this.requests.delete(var2[0]);
            AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$xabGRpaKBh9IgfIoS_aqNvjHcyM(this, var1));
         }

      } else {
         TLRPC.Updates var8 = (TLRPC.Updates)var3;
         int var5 = 0;

         boolean var6;
         for(var6 = false; var5 < var8.updates.size(); ++var5) {
            TLRPC.Update var7 = (TLRPC.Update)var8.updates.get(var5);
            if (var7 instanceof TLRPC.TL_updateEditMessage) {
               var1.messageObject.messageOwner = ((TLRPC.TL_updateEditMessage)var7).message;
            } else {
               if (!(var7 instanceof TLRPC.TL_updateEditChannelMessage)) {
                  continue;
               }

               var1.messageObject.messageOwner = ((TLRPC.TL_updateEditChannelMessage)var7).message;
            }

            var6 = true;
         }

         if (var6) {
            this.saveSharingLocation(var1, 0);
         }

         MessagesController.getInstance(this.currentAccount).processUpdates(var8, false);
      }
   }

   // $FF: synthetic method
   public void lambda$cleanup$4$LocationController() {
      this.requests.clear();
      this.sharingLocationsMap.clear();
      this.sharingLocations.clear();
      this.lastKnownLocation = null;
      this.stop(true);
   }

   // $FF: synthetic method
   public void lambda$loadLiveLocations$18$LocationController(long var1, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$w0C2adFKR_FSdQ3YkLyieoe840U(this, var1, var3));
      }
   }

   // $FF: synthetic method
   public void lambda$loadSharingLocations$9$LocationController() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();

      label92: {
         Exception var10000;
         label91: {
            ArrayList var4;
            ArrayList var5;
            SQLiteCursor var6;
            boolean var10001;
            try {
               var4 = new ArrayList();
               var5 = new ArrayList();
               var6 = MessagesStorage.getInstance(this.currentAccount).getDatabase().queryFinalized("SELECT uid, mid, date, period, message FROM sharing_locations WHERE 1");
            } catch (Exception var19) {
               var10000 = var19;
               var10001 = false;
               break label91;
            }

            while(true) {
               LocationController.SharingLocationInfo var7;
               NativeByteBuffer var8;
               label97: {
                  try {
                     if (var6.next()) {
                        var7 = new LocationController.SharingLocationInfo();
                        var7.did = var6.longValue(0);
                        var7.mid = var6.intValue(1);
                        var7.stopTime = var6.intValue(2);
                        var7.period = var6.intValue(3);
                        var8 = var6.byteBufferValue(4);
                        break label97;
                     }
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break;
                  }

                  boolean var13;
                  try {
                     var6.dispose();
                     var13 = var5.isEmpty();
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                     break;
                  }

                  if (!var13) {
                     try {
                        MessagesStorage.getInstance(this.currentAccount).getChatsInternal(TextUtils.join(",", var5), var3);
                     } catch (Exception var15) {
                        var10000 = var15;
                        var10001 = false;
                        break;
                     }
                  }

                  try {
                     if (!var4.isEmpty()) {
                        MessagesStorage.getInstance(this.currentAccount).getUsersInternal(TextUtils.join(",", var4), var2);
                     }
                     break label92;
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break;
                  }
               }

               if (var8 != null) {
                  try {
                     MessageObject var9 = new MessageObject(this.currentAccount, TLRPC.Message.TLdeserialize(var8, var8.readInt32(false), false), false);
                     var7.messageObject = var9;
                     MessagesStorage.addUsersAndChatsFromMessage(var7.messageObject.messageOwner, var4, var5);
                     var8.reuse();
                  } catch (Exception var18) {
                     var10000 = var18;
                     var10001 = false;
                     break;
                  }
               }

               int var10;
               try {
                  var1.add(var7);
                  var10 = (int)var7.did;
                  long var11 = var7.did;
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break;
               }

               if (var10 != 0) {
                  if (var10 < 0) {
                     var10 = -var10;

                     try {
                        if (!var5.contains(var10)) {
                           var5.add(var10);
                        }
                     } catch (Exception var20) {
                        var10000 = var20;
                        var10001 = false;
                        break;
                     }
                  } else {
                     try {
                        if (!var4.contains(var10)) {
                           var4.add(var10);
                        }
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break;
                     }
                  }
               }
            }
         }

         Exception var23 = var10000;
         FileLog.e((Throwable)var23);
      }

      if (!var1.isEmpty()) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$0L0Myfw_8A7gbZ9tpB4LMVz7Qgw(this, var2, var3, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$new$0$LocationController() {
      LocationController var1 = getInstance(this.currentAccount);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.didReceiveNewMessages);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.messagesDeleted);
      NotificationCenter.getInstance(this.currentAccount).addObserver(var1, NotificationCenter.replaceMessagesObjects);
   }

   // $FF: synthetic method
   public void lambda$null$1$LocationController(LocationController.SharingLocationInfo var1) {
      this.sharingLocationsUI.remove(var1);
      this.sharingLocationsMapUI.remove(var1.did);
      if (this.sharingLocationsUI.isEmpty()) {
         this.stopService();
      }

      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsChanged);
   }

   // $FF: synthetic method
   public void lambda$null$11$LocationController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates)var1, false);
      }
   }

   // $FF: synthetic method
   public void lambda$null$12$LocationController(LocationController.SharingLocationInfo var1) {
      this.sharingLocationsUI.remove(var1);
      this.sharingLocationsMapUI.remove(var1.did);
      if (this.sharingLocationsUI.isEmpty()) {
         this.stopService();
      }

      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsChanged);
   }

   // $FF: synthetic method
   public void lambda$null$14$LocationController(TLObject var1, TLRPC.TL_error var2) {
      if (var2 == null) {
         MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates)var1, false);
      }
   }

   // $FF: synthetic method
   public void lambda$null$15$LocationController() {
      this.sharingLocationsUI.clear();
      this.sharingLocationsMapUI.clear();
      this.stopService();
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsChanged);
   }

   // $FF: synthetic method
   public void lambda$null$17$LocationController(long var1, TLObject var3) {
      this.cacheRequests.delete(var1);
      TLRPC.messages_Messages var6 = (TLRPC.messages_Messages)var3;

      int var5;
      for(int var4 = 0; var4 < var6.messages.size(); var4 = var5 + 1) {
         var5 = var4;
         if (!(((TLRPC.Message)var6.messages.get(var4)).media instanceof TLRPC.TL_messageMediaGeoLive)) {
            var6.messages.remove(var4);
            var5 = var4 - 1;
         }
      }

      MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(var6.users, var6.chats, true, true);
      MessagesController.getInstance(this.currentAccount).putUsers(var6.users, false);
      MessagesController.getInstance(this.currentAccount).putChats(var6.chats, false);
      this.locationsCache.put(var1, var6.messages);
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsCacheChanged, var1, this.currentAccount);
   }

   // $FF: synthetic method
   public void lambda$null$6$LocationController(ArrayList var1) {
      this.sharingLocationsUI.addAll(var1);

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         LocationController.SharingLocationInfo var3 = (LocationController.SharingLocationInfo)var1.get(var2);
         this.sharingLocationsMapUI.put(var3.did, var3);
      }

      this.startService();
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsChanged);
   }

   // $FF: synthetic method
   public void lambda$null$7$LocationController(ArrayList var1) {
      this.sharingLocations.addAll(var1);

      for(int var2 = 0; var2 < this.sharingLocations.size(); ++var2) {
         LocationController.SharingLocationInfo var3 = (LocationController.SharingLocationInfo)this.sharingLocations.get(var2);
         this.sharingLocationsMap.put(var3.did, var3);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$n806eE82hDy_EKjzj_moV7XdY8A(this, var1));
   }

   // $FF: synthetic method
   public void lambda$null$8$LocationController(ArrayList var1, ArrayList var2, ArrayList var3) {
      MessagesController.getInstance(this.currentAccount).putUsers(var1, true);
      MessagesController.getInstance(this.currentAccount).putChats(var2, true);
      Utilities.stageQueue.postRunnable(new _$$Lambda$LocationController$KzQNHV5exKVTMUhNM3DuUpb5ALk(this, var3));
   }

   // $FF: synthetic method
   public void lambda$removeAllLocationSharings$16$LocationController() {
      for(int var1 = 0; var1 < this.sharingLocations.size(); ++var1) {
         LocationController.SharingLocationInfo var2 = (LocationController.SharingLocationInfo)this.sharingLocations.get(var1);
         TLRPC.TL_messages_editMessage var3 = new TLRPC.TL_messages_editMessage();
         var3.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var2.did);
         var3.id = var2.mid;
         var3.flags |= 16384;
         var3.media = new TLRPC.TL_inputMediaGeoLive();
         TLRPC.InputMedia var4 = var3.media;
         var4.stopped = true;
         var4.geo_point = new TLRPC.TL_inputGeoPointEmpty();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$LocationController$o4AyynaxXNLOklLrLCjAeV7nwjM(this));
      }

      this.sharingLocations.clear();
      this.sharingLocationsMap.clear();
      this.saveSharingLocation((LocationController.SharingLocationInfo)null, 2);
      this.stop(true);
      AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$Kp3u6_UIa9KXJzaDwAYVrLB6ovM(this));
   }

   // $FF: synthetic method
   public void lambda$removeSharingLocation$13$LocationController(long var1) {
      LocationController.SharingLocationInfo var3 = (LocationController.SharingLocationInfo)this.sharingLocationsMap.get(var1);
      this.sharingLocationsMap.remove(var1);
      if (var3 != null) {
         TLRPC.TL_messages_editMessage var4 = new TLRPC.TL_messages_editMessage();
         var4.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var3.did);
         var4.id = var3.mid;
         var4.flags |= 16384;
         var4.media = new TLRPC.TL_inputMediaGeoLive();
         TLRPC.InputMedia var5 = var4.media;
         var5.stopped = true;
         var5.geo_point = new TLRPC.TL_inputGeoPointEmpty();
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var4, new _$$Lambda$LocationController$8lTryql87YvYEN2OCsICR_gOP3g(this));
         this.sharingLocations.remove(var3);
         this.saveSharingLocation(var3, 1);
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$N9sfOku6NSx_Uin6Q7qXme__lGo(this, var3));
         if (this.sharingLocations.isEmpty()) {
            this.stop(true);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$saveSharingLocation$10$LocationController(int var1, LocationController.SharingLocationInfo var2) {
      Exception var10000;
      boolean var10001;
      if (var1 == 2) {
         try {
            MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("DELETE FROM sharing_locations WHERE 1").stepThis().dispose();
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      } else if (var1 == 1) {
         if (var2 == null) {
            return;
         }

         try {
            SQLiteDatabase var3 = MessagesStorage.getInstance(this.currentAccount).getDatabase();
            StringBuilder var4 = new StringBuilder();
            var4.append("DELETE FROM sharing_locations WHERE uid = ");
            var4.append(var2.did);
            var3.executeFast(var4.toString()).stepThis().dispose();
            return;
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
         }
      } else {
         if (var2 == null) {
            return;
         }

         try {
            SQLitePreparedStatement var9 = MessagesStorage.getInstance(this.currentAccount).getDatabase().executeFast("REPLACE INTO sharing_locations VALUES(?, ?, ?, ?, ?)");
            var9.requery();
            NativeByteBuffer var10 = new NativeByteBuffer(var2.messageObject.messageOwner.getObjectSize());
            var2.messageObject.messageOwner.serializeToStream(var10);
            var9.bindLong(1, var2.did);
            var9.bindInteger(2, var2.mid);
            var9.bindInteger(3, var2.stopTime);
            var9.bindInteger(4, var2.period);
            var9.bindByteBuffer(5, (NativeByteBuffer)var10);
            var9.step();
            var9.dispose();
            var10.reuse();
            return;
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      Exception var8 = var10000;
      FileLog.e((Throwable)var8);
   }

   // $FF: synthetic method
   public void lambda$update$3$LocationController(LocationController.SharingLocationInfo var1) {
      this.sharingLocationsUI.remove(var1);
      this.sharingLocationsMapUI.remove(var1.did);
      if (this.sharingLocationsUI.isEmpty()) {
         this.stopService();
      }

      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.liveLocationsChanged);
   }

   public void loadLiveLocations(long var1) {
      if (this.cacheRequests.indexOfKey(var1) < 0) {
         this.cacheRequests.put(var1, true);
         TLRPC.TL_messages_getRecentLocations var3 = new TLRPC.TL_messages_getRecentLocations();
         var3.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int)var1);
         var3.limit = 100;
         ConnectionsManager.getInstance(this.currentAccount).sendRequest(var3, new _$$Lambda$LocationController$9e6Jr8NCwhd7x0jAE3itGEDvCZ8(this, var1));
      }
   }

   public void removeAllLocationSharings() {
      Utilities.stageQueue.postRunnable(new _$$Lambda$LocationController$unkueEB9icgZn5pQTky3flbGZuo(this));
   }

   public void removeSharingLocation(long var1) {
      Utilities.stageQueue.postRunnable(new _$$Lambda$LocationController$__2b05V3C269L8B6e6NaqY7nkE0(this, var1));
   }

   public void setGoogleMapLocation(Location var1, boolean var2) {
      if (var1 != null) {
         label30: {
            this.lastLocationByGoogleMaps = true;
            if (!var2) {
               Location var3 = this.lastKnownLocation;
               if (var3 == null || var3.distanceTo(var1) < 20.0F) {
                  if (this.locationSentSinceLastGoogleMapUpdate) {
                     this.lastLocationSendTime = System.currentTimeMillis() - 90000L + 20000L;
                     this.locationSentSinceLastGoogleMapUpdate = false;
                  }
                  break label30;
               }
            }

            this.lastLocationSendTime = System.currentTimeMillis() - 90000L;
            this.locationSentSinceLastGoogleMapUpdate = false;
         }

         this.lastKnownLocation = var1;
      }
   }

   protected void update() {
      if (!this.sharingLocations.isEmpty()) {
         int var4;
         for(int var1 = 0; var1 < this.sharingLocations.size(); var1 = var4 + 1) {
            LocationController.SharingLocationInfo var2 = (LocationController.SharingLocationInfo)this.sharingLocations.get(var1);
            int var3 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            var4 = var1;
            if (var2.stopTime <= var3) {
               this.sharingLocations.remove(var1);
               this.sharingLocationsMap.remove(var2.did);
               this.saveSharingLocation(var2, 1);
               AndroidUtilities.runOnUIThread(new _$$Lambda$LocationController$PRv_6ObbwXmRgZBGVbIVGOjYQD8(this, var2));
               var4 = var1 - 1;
            }
         }

         if (!this.started) {
            if (Math.abs(this.lastLocationSendTime - System.currentTimeMillis()) > 90000L) {
               this.lastLocationStartTime = System.currentTimeMillis();
               this.start();
            }
         } else if (this.lastLocationByGoogleMaps || Math.abs(this.lastLocationStartTime - System.currentTimeMillis()) > 10000L) {
            this.lastLocationByGoogleMaps = false;
            this.locationSentSinceLastGoogleMapUpdate = true;
            this.lastLocationSendTime = System.currentTimeMillis();
            this.broadcastLastKnownLocation();
         }

      }
   }

   private class GpsLocationListener implements LocationListener {
      private GpsLocationListener() {
      }

      // $FF: synthetic method
      GpsLocationListener(Object var2) {
         this();
      }

      public void onLocationChanged(Location var1) {
         if (var1 != null) {
            if (LocationController.this.lastKnownLocation != null && (this == LocationController.this.networkLocationListener || this == LocationController.this.passiveLocationListener)) {
               if (!LocationController.this.started && var1.distanceTo(LocationController.this.lastKnownLocation) > 20.0F) {
                  LocationController.this.lastKnownLocation = var1;
                  LocationController.this.lastLocationSendTime = System.currentTimeMillis() - 90000L + 5000L;
               }
            } else {
               LocationController.this.lastKnownLocation = var1;
            }

         }
      }

      public void onProviderDisabled(String var1) {
      }

      public void onProviderEnabled(String var1) {
      }

      public void onStatusChanged(String var1, int var2, Bundle var3) {
      }
   }

   public static class SharingLocationInfo {
      public long did;
      public MessageObject messageObject;
      public int mid;
      public int period;
      public int stopTime;
   }
}
