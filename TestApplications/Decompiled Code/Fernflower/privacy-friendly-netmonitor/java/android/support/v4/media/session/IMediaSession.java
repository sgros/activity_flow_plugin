package android.support.v4.media.session;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public interface IMediaSession extends IInterface {
   void addQueueItem(MediaDescriptionCompat var1) throws RemoteException;

   void addQueueItemAt(MediaDescriptionCompat var1, int var2) throws RemoteException;

   void adjustVolume(int var1, int var2, String var3) throws RemoteException;

   void fastForward() throws RemoteException;

   Bundle getExtras() throws RemoteException;

   long getFlags() throws RemoteException;

   PendingIntent getLaunchPendingIntent() throws RemoteException;

   MediaMetadataCompat getMetadata() throws RemoteException;

   String getPackageName() throws RemoteException;

   PlaybackStateCompat getPlaybackState() throws RemoteException;

   List getQueue() throws RemoteException;

   CharSequence getQueueTitle() throws RemoteException;

   int getRatingType() throws RemoteException;

   int getRepeatMode() throws RemoteException;

   int getShuffleMode() throws RemoteException;

   String getTag() throws RemoteException;

   ParcelableVolumeInfo getVolumeAttributes() throws RemoteException;

   boolean isCaptioningEnabled() throws RemoteException;

   boolean isShuffleModeEnabledDeprecated() throws RemoteException;

   boolean isTransportControlEnabled() throws RemoteException;

   void next() throws RemoteException;

   void pause() throws RemoteException;

   void play() throws RemoteException;

   void playFromMediaId(String var1, Bundle var2) throws RemoteException;

   void playFromSearch(String var1, Bundle var2) throws RemoteException;

   void playFromUri(Uri var1, Bundle var2) throws RemoteException;

   void prepare() throws RemoteException;

   void prepareFromMediaId(String var1, Bundle var2) throws RemoteException;

   void prepareFromSearch(String var1, Bundle var2) throws RemoteException;

   void prepareFromUri(Uri var1, Bundle var2) throws RemoteException;

   void previous() throws RemoteException;

   void rate(RatingCompat var1) throws RemoteException;

   void rateWithExtras(RatingCompat var1, Bundle var2) throws RemoteException;

   void registerCallbackListener(IMediaControllerCallback var1) throws RemoteException;

   void removeQueueItem(MediaDescriptionCompat var1) throws RemoteException;

   void removeQueueItemAt(int var1) throws RemoteException;

   void rewind() throws RemoteException;

   void seekTo(long var1) throws RemoteException;

   void sendCommand(String var1, Bundle var2, MediaSessionCompat.ResultReceiverWrapper var3) throws RemoteException;

   void sendCustomAction(String var1, Bundle var2) throws RemoteException;

   boolean sendMediaButton(KeyEvent var1) throws RemoteException;

   void setCaptioningEnabled(boolean var1) throws RemoteException;

   void setRepeatMode(int var1) throws RemoteException;

   void setShuffleMode(int var1) throws RemoteException;

   void setShuffleModeEnabledDeprecated(boolean var1) throws RemoteException;

   void setVolumeTo(int var1, int var2, String var3) throws RemoteException;

   void skipToQueueItem(long var1) throws RemoteException;

   void stop() throws RemoteException;

   void unregisterCallbackListener(IMediaControllerCallback var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IMediaSession {
      private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaSession";
      static final int TRANSACTION_addQueueItem = 41;
      static final int TRANSACTION_addQueueItemAt = 42;
      static final int TRANSACTION_adjustVolume = 11;
      static final int TRANSACTION_fastForward = 22;
      static final int TRANSACTION_getExtras = 31;
      static final int TRANSACTION_getFlags = 9;
      static final int TRANSACTION_getLaunchPendingIntent = 8;
      static final int TRANSACTION_getMetadata = 27;
      static final int TRANSACTION_getPackageName = 6;
      static final int TRANSACTION_getPlaybackState = 28;
      static final int TRANSACTION_getQueue = 29;
      static final int TRANSACTION_getQueueTitle = 30;
      static final int TRANSACTION_getRatingType = 32;
      static final int TRANSACTION_getRepeatMode = 37;
      static final int TRANSACTION_getShuffleMode = 47;
      static final int TRANSACTION_getTag = 7;
      static final int TRANSACTION_getVolumeAttributes = 10;
      static final int TRANSACTION_isCaptioningEnabled = 45;
      static final int TRANSACTION_isShuffleModeEnabledDeprecated = 38;
      static final int TRANSACTION_isTransportControlEnabled = 5;
      static final int TRANSACTION_next = 20;
      static final int TRANSACTION_pause = 18;
      static final int TRANSACTION_play = 13;
      static final int TRANSACTION_playFromMediaId = 14;
      static final int TRANSACTION_playFromSearch = 15;
      static final int TRANSACTION_playFromUri = 16;
      static final int TRANSACTION_prepare = 33;
      static final int TRANSACTION_prepareFromMediaId = 34;
      static final int TRANSACTION_prepareFromSearch = 35;
      static final int TRANSACTION_prepareFromUri = 36;
      static final int TRANSACTION_previous = 21;
      static final int TRANSACTION_rate = 25;
      static final int TRANSACTION_rateWithExtras = 51;
      static final int TRANSACTION_registerCallbackListener = 3;
      static final int TRANSACTION_removeQueueItem = 43;
      static final int TRANSACTION_removeQueueItemAt = 44;
      static final int TRANSACTION_rewind = 23;
      static final int TRANSACTION_seekTo = 24;
      static final int TRANSACTION_sendCommand = 1;
      static final int TRANSACTION_sendCustomAction = 26;
      static final int TRANSACTION_sendMediaButton = 2;
      static final int TRANSACTION_setCaptioningEnabled = 46;
      static final int TRANSACTION_setRepeatMode = 39;
      static final int TRANSACTION_setShuffleMode = 48;
      static final int TRANSACTION_setShuffleModeEnabledDeprecated = 40;
      static final int TRANSACTION_setVolumeTo = 12;
      static final int TRANSACTION_skipToQueueItem = 17;
      static final int TRANSACTION_stop = 19;
      static final int TRANSACTION_unregisterCallbackListener = 4;

      public Stub() {
         this.attachInterface(this, "android.support.v4.media.session.IMediaSession");
      }

      public static IMediaSession asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.media.session.IMediaSession");
            return (IMediaSession)(var1 != null && var1 instanceof IMediaSession ? (IMediaSession)var1 : new IMediaSession.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         Object var5 = null;
         Object var6 = null;
         String var7 = null;
         Bundle var8 = null;
         Object var9 = null;
         Object var10 = null;
         Object var11 = null;
         Object var12 = null;
         Object var13 = null;
         Object var14 = null;
         Object var15 = null;
         MediaSessionCompat.ResultReceiverWrapper var16 = null;
         Object var17 = null;
         Object var18 = null;
         RatingCompat var32;
         Bundle var35;
         if (var1 != 51) {
            if (var1 != 1598968902) {
               boolean var19 = false;
               boolean var20 = false;
               byte var23;
               String var31;
               MediaDescriptionCompat var33;
               Uri var34;
               String var36;
               switch(var1) {
               case 1:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var7 = var2.readString();
                  if (var2.readInt() != 0) {
                     var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  } else {
                     var8 = null;
                  }

                  if (var2.readInt() != 0) {
                     var16 = (MediaSessionCompat.ResultReceiverWrapper)MediaSessionCompat.ResultReceiverWrapper.CREATOR.createFromParcel(var2);
                  }

                  this.sendCommand(var7, var8, var16);
                  var3.writeNoException();
                  return true;
               case 2:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  KeyEvent var37 = (KeyEvent)var15;
                  if (var2.readInt() != 0) {
                     var37 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(var2);
                  }

                  var23 = this.sendMediaButton(var37);
                  var3.writeNoException();
                  var3.writeInt(var23);
                  return true;
               case 3:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.registerCallbackListener(IMediaControllerCallback.Stub.asInterface(var2.readStrongBinder()));
                  var3.writeNoException();
                  return true;
               case 4:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.unregisterCallbackListener(IMediaControllerCallback.Stub.asInterface(var2.readStrongBinder()));
                  var3.writeNoException();
                  return true;
               case 5:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var23 = this.isTransportControlEnabled();
                  var3.writeNoException();
                  var3.writeInt(var23);
                  return true;
               case 6:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var31 = this.getPackageName();
                  var3.writeNoException();
                  var3.writeString(var31);
                  return true;
               case 7:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var31 = this.getTag();
                  var3.writeNoException();
                  var3.writeString(var31);
                  return true;
               case 8:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  PendingIntent var30 = this.getLaunchPendingIntent();
                  var3.writeNoException();
                  if (var30 != null) {
                     var3.writeInt(1);
                     var30.writeToParcel(var3, 1);
                  } else {
                     var3.writeInt(0);
                  }

                  return true;
               case 9:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  long var21 = this.getFlags();
                  var3.writeNoException();
                  var3.writeLong(var21);
                  return true;
               case 10:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  ParcelableVolumeInfo var29 = this.getVolumeAttributes();
                  var3.writeNoException();
                  if (var29 != null) {
                     var3.writeInt(1);
                     var29.writeToParcel(var3, 1);
                  } else {
                     var3.writeInt(0);
                  }

                  return true;
               case 11:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.adjustVolume(var2.readInt(), var2.readInt(), var2.readString());
                  var3.writeNoException();
                  return true;
               case 12:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.setVolumeTo(var2.readInt(), var2.readInt(), var2.readString());
                  var3.writeNoException();
                  return true;
               case 13:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.play();
                  var3.writeNoException();
                  return true;
               case 14:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var36 = var2.readString();
                  var8 = (Bundle)var14;
                  if (var2.readInt() != 0) {
                     var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  }

                  this.playFromMediaId(var36, var8);
                  var3.writeNoException();
                  return true;
               case 15:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var36 = var2.readString();
                  var8 = (Bundle)var13;
                  if (var2.readInt() != 0) {
                     var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  }

                  this.playFromSearch(var36, var8);
                  var3.writeNoException();
                  return true;
               case 16:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  if (var2.readInt() != 0) {
                     var34 = (Uri)Uri.CREATOR.createFromParcel(var2);
                  } else {
                     var34 = null;
                  }

                  var35 = (Bundle)var12;
                  if (var2.readInt() != 0) {
                     var35 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  }

                  this.playFromUri(var34, var35);
                  var3.writeNoException();
                  return true;
               case 17:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.skipToQueueItem(var2.readLong());
                  var3.writeNoException();
                  return true;
               case 18:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.pause();
                  var3.writeNoException();
                  return true;
               case 19:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.stop();
                  var3.writeNoException();
                  return true;
               case 20:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.next();
                  var3.writeNoException();
                  return true;
               case 21:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.previous();
                  var3.writeNoException();
                  return true;
               case 22:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.fastForward();
                  var3.writeNoException();
                  return true;
               case 23:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.rewind();
                  var3.writeNoException();
                  return true;
               case 24:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.seekTo(var2.readLong());
                  var3.writeNoException();
                  return true;
               case 25:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var32 = (RatingCompat)var11;
                  if (var2.readInt() != 0) {
                     var32 = (RatingCompat)RatingCompat.CREATOR.createFromParcel(var2);
                  }

                  this.rate(var32);
                  var3.writeNoException();
                  return true;
               case 26:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var36 = var2.readString();
                  var8 = (Bundle)var10;
                  if (var2.readInt() != 0) {
                     var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  }

                  this.sendCustomAction(var36, var8);
                  var3.writeNoException();
                  return true;
               case 27:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  MediaMetadataCompat var28 = this.getMetadata();
                  var3.writeNoException();
                  if (var28 != null) {
                     var3.writeInt(1);
                     var28.writeToParcel(var3, 1);
                  } else {
                     var3.writeInt(0);
                  }

                  return true;
               case 28:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  PlaybackStateCompat var27 = this.getPlaybackState();
                  var3.writeNoException();
                  if (var27 != null) {
                     var3.writeInt(1);
                     var27.writeToParcel(var3, 1);
                  } else {
                     var3.writeInt(0);
                  }

                  return true;
               case 29:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  List var26 = this.getQueue();
                  var3.writeNoException();
                  var3.writeTypedList(var26);
                  return true;
               case 30:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  CharSequence var25 = this.getQueueTitle();
                  var3.writeNoException();
                  if (var25 != null) {
                     var3.writeInt(1);
                     TextUtils.writeToParcel(var25, var3, 1);
                  } else {
                     var3.writeInt(0);
                  }

                  return true;
               case 31:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  Bundle var24 = this.getExtras();
                  var3.writeNoException();
                  if (var24 != null) {
                     var3.writeInt(1);
                     var24.writeToParcel(var3, 1);
                  } else {
                     var3.writeInt(0);
                  }

                  return true;
               case 32:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var1 = this.getRatingType();
                  var3.writeNoException();
                  var3.writeInt(var1);
                  return true;
               case 33:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.prepare();
                  var3.writeNoException();
                  return true;
               case 34:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var36 = var2.readString();
                  var8 = (Bundle)var9;
                  if (var2.readInt() != 0) {
                     var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  }

                  this.prepareFromMediaId(var36, var8);
                  var3.writeNoException();
                  return true;
               case 35:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var36 = var2.readString();
                  if (var2.readInt() != 0) {
                     var8 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  }

                  this.prepareFromSearch(var36, var8);
                  var3.writeNoException();
                  return true;
               case 36:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  if (var2.readInt() != 0) {
                     var34 = (Uri)Uri.CREATOR.createFromParcel(var2);
                  } else {
                     var34 = null;
                  }

                  var35 = var7;
                  if (var2.readInt() != 0) {
                     var35 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                  }

                  this.prepareFromUri(var34, var35);
                  var3.writeNoException();
                  return true;
               case 37:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var1 = this.getRepeatMode();
                  var3.writeNoException();
                  var3.writeInt(var1);
                  return true;
               case 38:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var23 = this.isShuffleModeEnabledDeprecated();
                  var3.writeNoException();
                  var3.writeInt(var23);
                  return true;
               case 39:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.setRepeatMode(var2.readInt());
                  var3.writeNoException();
                  return true;
               case 40:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var20 = var19;
                  if (var2.readInt() != 0) {
                     var20 = true;
                  }

                  this.setShuffleModeEnabledDeprecated(var20);
                  var3.writeNoException();
                  return true;
               case 41:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var33 = (MediaDescriptionCompat)var6;
                  if (var2.readInt() != 0) {
                     var33 = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var2);
                  }

                  this.addQueueItem(var33);
                  var3.writeNoException();
                  return true;
               case 42:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var33 = (MediaDescriptionCompat)var5;
                  if (var2.readInt() != 0) {
                     var33 = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var2);
                  }

                  this.addQueueItemAt(var33, var2.readInt());
                  var3.writeNoException();
                  return true;
               case 43:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var33 = (MediaDescriptionCompat)var18;
                  if (var2.readInt() != 0) {
                     var33 = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var2);
                  }

                  this.removeQueueItem(var33);
                  var3.writeNoException();
                  return true;
               case 44:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.removeQueueItemAt(var2.readInt());
                  var3.writeNoException();
                  return true;
               case 45:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var23 = this.isCaptioningEnabled();
                  var3.writeNoException();
                  var3.writeInt(var23);
                  return true;
               case 46:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  if (var2.readInt() != 0) {
                     var20 = true;
                  }

                  this.setCaptioningEnabled(var20);
                  var3.writeNoException();
                  return true;
               case 47:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  var1 = this.getShuffleMode();
                  var3.writeNoException();
                  var3.writeInt(var1);
                  return true;
               case 48:
                  var2.enforceInterface("android.support.v4.media.session.IMediaSession");
                  this.setShuffleMode(var2.readInt());
                  var3.writeNoException();
                  return true;
               default:
                  return super.onTransact(var1, var2, var3, var4);
               }
            } else {
               var3.writeString("android.support.v4.media.session.IMediaSession");
               return true;
            }
         } else {
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            if (var2.readInt() != 0) {
               var32 = (RatingCompat)RatingCompat.CREATOR.createFromParcel(var2);
            } else {
               var32 = null;
            }

            var35 = (Bundle)var17;
            if (var2.readInt() != 0) {
               var35 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            }

            this.rateWithExtras(var32, var35);
            var3.writeNoException();
            return true;
         }
      }

      private static class Proxy implements IMediaSession {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public void addQueueItem(MediaDescriptionCompat var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(41, var2, var3, 0);
                     var3.readException();
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               var2.recycle();
               throw var24;
            }

            var3.recycle();
            var2.recycle();
         }

         public void addQueueItemAt(MediaDescriptionCompat var1, int var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(42, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void adjustVolume(int var1, int var2, String var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var4.writeInt(var1);
               var4.writeInt(var2);
               var4.writeString(var3);
               this.mRemote.transact(11, var4, var5, 0);
               var5.readException();
            } finally {
               var5.recycle();
               var4.recycle();
            }

         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public void fastForward() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(22, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public Bundle getExtras() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();
            boolean var5 = false;

            Bundle var3;
            label37: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(31, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var2.recycle();
                     var1.recycle();
                  }
               }

               var3 = null;
            }

            var2.recycle();
            var1.recycle();
            return var3;
         }

         public long getFlags() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            long var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(9, var1, var2, 0);
               var2.readException();
               var3 = var2.readLong();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public String getInterfaceDescriptor() {
            return "android.support.v4.media.session.IMediaSession";
         }

         public PendingIntent getLaunchPendingIntent() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();
            boolean var5 = false;

            PendingIntent var3;
            label37: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(8, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var2.recycle();
                     var1.recycle();
                  }
               }

               var3 = null;
            }

            var2.recycle();
            var1.recycle();
            return var3;
         }

         public MediaMetadataCompat getMetadata() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();
            boolean var5 = false;

            MediaMetadataCompat var3;
            label37: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(27, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var2.recycle();
                     var1.recycle();
                  }
               }

               var3 = null;
            }

            var2.recycle();
            var1.recycle();
            return var3;
         }

         public String getPackageName() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            String var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(6, var1, var2, 0);
               var2.readException();
               var3 = var2.readString();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public PlaybackStateCompat getPlaybackState() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();
            boolean var5 = false;

            PlaybackStateCompat var3;
            label37: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(28, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var2.recycle();
                     var1.recycle();
                  }
               }

               var3 = null;
            }

            var2.recycle();
            var1.recycle();
            return var3;
         }

         public List getQueue() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            ArrayList var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(29, var1, var2, 0);
               var2.readException();
               var3 = var2.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR);
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public CharSequence getQueueTitle() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();
            boolean var5 = false;

            CharSequence var3;
            label37: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(30, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var2.recycle();
                     var1.recycle();
                  }
               }

               var3 = null;
            }

            var2.recycle();
            var1.recycle();
            return var3;
         }

         public int getRatingType() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            int var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(32, var1, var2, 0);
               var2.readException();
               var3 = var2.readInt();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public int getRepeatMode() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            int var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(37, var1, var2, 0);
               var2.readException();
               var3 = var2.readInt();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public int getShuffleMode() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            int var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(47, var1, var2, 0);
               var2.readException();
               var3 = var2.readInt();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public String getTag() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            String var3;
            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(7, var1, var2, 0);
               var2.readException();
               var3 = var2.readString();
            } finally {
               var2.recycle();
               var1.recycle();
            }

            return var3;
         }

         public ParcelableVolumeInfo getVolumeAttributes() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();
            boolean var5 = false;

            ParcelableVolumeInfo var3;
            label37: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(10, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label37;
                  }

                  var5 = false;
               } finally {
                  if (var5) {
                     var2.recycle();
                     var1.recycle();
                  }
               }

               var3 = null;
            }

            var2.recycle();
            var1.recycle();
            return var3;
         }

         public boolean isCaptioningEnabled() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            boolean var4;
            int var5;
            label80: {
               Throwable var10000;
               label84: {
                  boolean var10001;
                  IBinder var3;
                  try {
                     var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3 = this.mRemote;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label84;
                  }

                  var4 = false;

                  label75:
                  try {
                     var3.transact(45, var1, var2, 0);
                     var2.readException();
                     var5 = var2.readInt();
                     break label80;
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var12 = var10000;
               var2.recycle();
               var1.recycle();
               throw var12;
            }

            if (var5 != 0) {
               var4 = true;
            }

            var2.recycle();
            var1.recycle();
            return var4;
         }

         public boolean isShuffleModeEnabledDeprecated() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            boolean var4;
            int var5;
            label80: {
               Throwable var10000;
               label84: {
                  boolean var10001;
                  IBinder var3;
                  try {
                     var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3 = this.mRemote;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label84;
                  }

                  var4 = false;

                  label75:
                  try {
                     var3.transact(38, var1, var2, 0);
                     var2.readException();
                     var5 = var2.readInt();
                     break label80;
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var12 = var10000;
               var2.recycle();
               var1.recycle();
               throw var12;
            }

            if (var5 != 0) {
               var4 = true;
            }

            var2.recycle();
            var1.recycle();
            return var4;
         }

         public boolean isTransportControlEnabled() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            boolean var4;
            int var5;
            label80: {
               Throwable var10000;
               label84: {
                  boolean var10001;
                  IBinder var3;
                  try {
                     var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3 = this.mRemote;
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label84;
                  }

                  var4 = false;

                  label75:
                  try {
                     var3.transact(5, var1, var2, 0);
                     var2.readException();
                     var5 = var2.readInt();
                     break label80;
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var12 = var10000;
               var2.recycle();
               var1.recycle();
               throw var12;
            }

            if (var5 != 0) {
               var4 = true;
            }

            var2.recycle();
            var1.recycle();
            return var4;
         }

         public void next() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(20, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void pause() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(18, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void play() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(13, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void playFromMediaId(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(14, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void playFromSearch(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(15, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void playFromUri(Uri var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label309;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(16, var3, var4, 0);
                     var4.readException();
                     break label305;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var47 = var10000;
               var4.recycle();
               var3.recycle();
               throw var47;
            }

            var4.recycle();
            var3.recycle();
         }

         public void prepare() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(33, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void prepareFromMediaId(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(34, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void prepareFromSearch(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(35, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public void prepareFromUri(Uri var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label309;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(36, var3, var4, 0);
                     var4.readException();
                     break label305;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var47 = var10000;
               var4.recycle();
               var3.recycle();
               throw var47;
            }

            var4.recycle();
            var3.recycle();
         }

         public void previous() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(21, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void rate(RatingCompat var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(25, var2, var3, 0);
                     var3.readException();
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               var2.recycle();
               throw var24;
            }

            var3.recycle();
            var2.recycle();
         }

         public void rateWithExtras(RatingCompat var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var46) {
                     var10000 = var46;
                     var10001 = false;
                     break label309;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(51, var3, var4, 0);
                     var4.readException();
                     break label305;
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var47 = var10000;
               var4.recycle();
               var3.recycle();
               throw var47;
            }

            var4.recycle();
            var3.recycle();
         }

         public void registerCallbackListener(IMediaControllerCallback var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label124: {
               Throwable var10000;
               label128: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label128;
                  }

                  IBinder var16;
                  if (var1 != null) {
                     try {
                        var16 = var1.asBinder();
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label128;
                     }
                  } else {
                     var16 = null;
                  }

                  label115:
                  try {
                     var2.writeStrongBinder(var16);
                     this.mRemote.transact(3, var2, var3, 0);
                     var3.readException();
                     break label124;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label115;
                  }
               }

               Throwable var17 = var10000;
               var3.recycle();
               var2.recycle();
               throw var17;
            }

            var3.recycle();
            var2.recycle();
         }

         public void removeQueueItem(MediaDescriptionCompat var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(43, var2, var3, 0);
                     var3.readException();
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               var2.recycle();
               throw var24;
            }

            var3.recycle();
            var2.recycle();
         }

         public void removeQueueItemAt(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var2.writeInt(var1);
               this.mRemote.transact(44, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void rewind() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(23, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void seekTo(long var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var3.writeLong(var1);
               this.mRemote.transact(24, var3, var4, 0);
               var4.readException();
            } finally {
               var4.recycle();
               var3.recycle();
            }

         }

         public void sendCommand(String var1, Bundle var2, MediaSessionCompat.ResultReceiverWrapper var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            label305: {
               Throwable var10000;
               label309: {
                  boolean var10001;
                  try {
                     var4.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var4.writeString(var1);
                  } catch (Throwable var47) {
                     var10000 = var47;
                     var10001 = false;
                     break label309;
                  }

                  if (var2 != null) {
                     try {
                        var4.writeInt(1);
                        var2.writeToParcel(var4, 0);
                     } catch (Throwable var46) {
                        var10000 = var46;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label309;
                     }
                  }

                  if (var3 != null) {
                     try {
                        var4.writeInt(1);
                        var3.writeToParcel(var4, 0);
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label309;
                     }
                  } else {
                     try {
                        var4.writeInt(0);
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label309;
                     }
                  }

                  label290:
                  try {
                     this.mRemote.transact(1, var4, var5, 0);
                     var5.readException();
                     break label305;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label290;
                  }
               }

               Throwable var48 = var10000;
               var5.recycle();
               var4.recycle();
               throw var48;
            }

            var5.recycle();
            var4.recycle();
         }

         public void sendCustomAction(String var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                     var3.writeString(var1);
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(26, var3, var4, 0);
                     var4.readException();
                     break label166;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var25 = var10000;
               var4.recycle();
               var3.recycle();
               throw var25;
            }

            var4.recycle();
            var3.recycle();
         }

         public boolean sendMediaButton(KeyEvent var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            boolean var4;
            int var5;
            label187: {
               Throwable var10000;
               label191: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label191;
                  }

                  var4 = true;
                  if (var1 != null) {
                     try {
                        var2.writeInt(1);
                        var1.writeToParcel(var2, 0);
                     } catch (Throwable var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label191;
                     }
                  } else {
                     try {
                        var2.writeInt(0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label191;
                     }
                  }

                  label177:
                  try {
                     this.mRemote.transact(2, var2, var3, 0);
                     var3.readException();
                     var5 = var3.readInt();
                     break label187;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label177;
                  }
               }

               Throwable var26 = var10000;
               var3.recycle();
               var2.recycle();
               throw var26;
            }

            if (var5 == 0) {
               var4 = false;
            }

            var3.recycle();
            var2.recycle();
            return var4;
         }

         public void setCaptioningEnabled(boolean var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var2.writeInt(var1);
               this.mRemote.transact(46, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void setRepeatMode(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var2.writeInt(var1);
               this.mRemote.transact(39, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void setShuffleMode(int var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var2.writeInt(var1);
               this.mRemote.transact(48, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void setShuffleModeEnabledDeprecated(boolean var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            try {
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var2.writeInt(var1);
               this.mRemote.transact(40, var2, var3, 0);
               var3.readException();
            } finally {
               var3.recycle();
               var2.recycle();
            }

         }

         public void setVolumeTo(int var1, int var2, String var3) throws RemoteException {
            Parcel var4 = Parcel.obtain();
            Parcel var5 = Parcel.obtain();

            try {
               var4.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var4.writeInt(var1);
               var4.writeInt(var2);
               var4.writeString(var3);
               this.mRemote.transact(12, var4, var5, 0);
               var5.readException();
            } finally {
               var5.recycle();
               var4.recycle();
            }

         }

         public void skipToQueueItem(long var1) throws RemoteException {
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            try {
               var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               var3.writeLong(var1);
               this.mRemote.transact(17, var3, var4, 0);
               var4.readException();
            } finally {
               var4.recycle();
               var3.recycle();
            }

         }

         public void stop() throws RemoteException {
            Parcel var1 = Parcel.obtain();
            Parcel var2 = Parcel.obtain();

            try {
               var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(19, var1, var2, 0);
               var2.readException();
            } finally {
               var2.recycle();
               var1.recycle();
            }

         }

         public void unregisterCallbackListener(IMediaControllerCallback var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label124: {
               Throwable var10000;
               label128: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label128;
                  }

                  IBinder var16;
                  if (var1 != null) {
                     try {
                        var16 = var1.asBinder();
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label128;
                     }
                  } else {
                     var16 = null;
                  }

                  label115:
                  try {
                     var2.writeStrongBinder(var16);
                     this.mRemote.transact(4, var2, var3, 0);
                     var3.readException();
                     break label124;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label115;
                  }
               }

               Throwable var17 = var10000;
               var3.recycle();
               var2.recycle();
               throw var17;
            }

            var3.recycle();
            var2.recycle();
         }
      }
   }
}
