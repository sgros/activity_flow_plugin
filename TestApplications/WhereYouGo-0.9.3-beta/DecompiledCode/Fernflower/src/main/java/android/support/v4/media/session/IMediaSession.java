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

   String getTag() throws RemoteException;

   ParcelableVolumeInfo getVolumeAttributes() throws RemoteException;

   boolean isShuffleModeEnabled() throws RemoteException;

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

   void registerCallbackListener(IMediaControllerCallback var1) throws RemoteException;

   void removeQueueItem(MediaDescriptionCompat var1) throws RemoteException;

   void removeQueueItemAt(int var1) throws RemoteException;

   void rewind() throws RemoteException;

   void seekTo(long var1) throws RemoteException;

   void sendCommand(String var1, Bundle var2, MediaSessionCompat.ResultReceiverWrapper var3) throws RemoteException;

   void sendCustomAction(String var1, Bundle var2) throws RemoteException;

   boolean sendMediaButton(KeyEvent var1) throws RemoteException;

   void setRepeatMode(int var1) throws RemoteException;

   void setShuffleModeEnabled(boolean var1) throws RemoteException;

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
      static final int TRANSACTION_getTag = 7;
      static final int TRANSACTION_getVolumeAttributes = 10;
      static final int TRANSACTION_isShuffleModeEnabled = 38;
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
      static final int TRANSACTION_registerCallbackListener = 3;
      static final int TRANSACTION_removeQueueItem = 43;
      static final int TRANSACTION_removeQueueItemAt = 44;
      static final int TRANSACTION_rewind = 23;
      static final int TRANSACTION_seekTo = 24;
      static final int TRANSACTION_sendCommand = 1;
      static final int TRANSACTION_sendCustomAction = 26;
      static final int TRANSACTION_sendMediaButton = 2;
      static final int TRANSACTION_setRepeatMode = 39;
      static final int TRANSACTION_setShuffleModeEnabled = 40;
      static final int TRANSACTION_setVolumeTo = 12;
      static final int TRANSACTION_skipToQueueItem = 17;
      static final int TRANSACTION_stop = 19;
      static final int TRANSACTION_unregisterCallbackListener = 4;

      public Stub() {
         this.attachInterface(this, "android.support.v4.media.session.IMediaSession");
      }

      public static IMediaSession asInterface(IBinder var0) {
         Object var2;
         if (var0 == null) {
            var2 = null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.media.session.IMediaSession");
            if (var1 != null && var1 instanceof IMediaSession) {
               var2 = (IMediaSession)var1;
            } else {
               var2 = new IMediaSession.Stub.Proxy(var0);
            }
         }

         return (IMediaSession)var2;
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         byte var5 = 0;
         byte var6 = 0;
         byte var7 = 0;
         boolean var8 = true;
         boolean var9;
         byte var14;
         MediaDescriptionCompat var15;
         Bundle var16;
         Uri var22;
         String var23;
         String var26;
         switch(var1) {
         case 1:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            String var10 = var2.readString();
            Bundle var28;
            if (var2.readInt() != 0) {
               var28 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var28 = null;
            }

            MediaSessionCompat.ResultReceiverWrapper var29;
            if (var2.readInt() != 0) {
               var29 = (MediaSessionCompat.ResultReceiverWrapper)MediaSessionCompat.ResultReceiverWrapper.CREATOR.createFromParcel(var2);
            } else {
               var29 = null;
            }

            this.sendCommand(var10, var28, var29);
            var3.writeNoException();
            var9 = var8;
            break;
         case 2:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            KeyEvent var27;
            if (var2.readInt() != 0) {
               var27 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(var2);
            } else {
               var27 = null;
            }

            var9 = this.sendMediaButton(var27);
            var3.writeNoException();
            var14 = var7;
            if (var9) {
               var14 = 1;
            }

            var3.writeInt(var14);
            var9 = var8;
            break;
         case 3:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.registerCallbackListener(IMediaControllerCallback.Stub.asInterface(var2.readStrongBinder()));
            var3.writeNoException();
            var9 = var8;
            break;
         case 4:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.unregisterCallbackListener(IMediaControllerCallback.Stub.asInterface(var2.readStrongBinder()));
            var3.writeNoException();
            var9 = var8;
            break;
         case 5:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var9 = this.isTransportControlEnabled();
            var3.writeNoException();
            var14 = var5;
            if (var9) {
               var14 = 1;
            }

            var3.writeInt(var14);
            var9 = var8;
            break;
         case 6:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var26 = this.getPackageName();
            var3.writeNoException();
            var3.writeString(var26);
            var9 = var8;
            break;
         case 7:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var26 = this.getTag();
            var3.writeNoException();
            var3.writeString(var26);
            var9 = var8;
            break;
         case 8:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            PendingIntent var25 = this.getLaunchPendingIntent();
            var3.writeNoException();
            if (var25 != null) {
               var3.writeInt(1);
               var25.writeToParcel(var3, 1);
               var9 = var8;
            } else {
               var3.writeInt(0);
               var9 = var8;
            }
            break;
         case 9:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            long var12 = this.getFlags();
            var3.writeNoException();
            var3.writeLong(var12);
            var9 = var8;
            break;
         case 10:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            ParcelableVolumeInfo var24 = this.getVolumeAttributes();
            var3.writeNoException();
            if (var24 != null) {
               var3.writeInt(1);
               var24.writeToParcel(var3, 1);
               var9 = var8;
            } else {
               var3.writeInt(0);
               var9 = var8;
            }
            break;
         case 11:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.adjustVolume(var2.readInt(), var2.readInt(), var2.readString());
            var3.writeNoException();
            var9 = var8;
            break;
         case 12:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.setVolumeTo(var2.readInt(), var2.readInt(), var2.readString());
            var3.writeNoException();
            var9 = var8;
            break;
         case 13:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.play();
            var3.writeNoException();
            var9 = var8;
            break;
         case 14:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var23 = var2.readString();
            if (var2.readInt() != 0) {
               var16 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var16 = null;
            }

            this.playFromMediaId(var23, var16);
            var3.writeNoException();
            var9 = var8;
            break;
         case 15:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var23 = var2.readString();
            if (var2.readInt() != 0) {
               var16 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var16 = null;
            }

            this.playFromSearch(var23, var16);
            var3.writeNoException();
            var9 = var8;
            break;
         case 16:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            if (var2.readInt() != 0) {
               var22 = (Uri)Uri.CREATOR.createFromParcel(var2);
            } else {
               var22 = null;
            }

            if (var2.readInt() != 0) {
               var16 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var16 = null;
            }

            this.playFromUri(var22, var16);
            var3.writeNoException();
            var9 = var8;
            break;
         case 17:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.skipToQueueItem(var2.readLong());
            var3.writeNoException();
            var9 = var8;
            break;
         case 18:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.pause();
            var3.writeNoException();
            var9 = var8;
            break;
         case 19:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.stop();
            var3.writeNoException();
            var9 = var8;
            break;
         case 20:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.next();
            var3.writeNoException();
            var9 = var8;
            break;
         case 21:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.previous();
            var3.writeNoException();
            var9 = var8;
            break;
         case 22:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.fastForward();
            var3.writeNoException();
            var9 = var8;
            break;
         case 23:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.rewind();
            var3.writeNoException();
            var9 = var8;
            break;
         case 24:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.seekTo(var2.readLong());
            var3.writeNoException();
            var9 = var8;
            break;
         case 25:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            RatingCompat var21;
            if (var2.readInt() != 0) {
               var21 = (RatingCompat)RatingCompat.CREATOR.createFromParcel(var2);
            } else {
               var21 = null;
            }

            this.rate(var21);
            var3.writeNoException();
            var9 = var8;
            break;
         case 26:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var23 = var2.readString();
            if (var2.readInt() != 0) {
               var16 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var16 = null;
            }

            this.sendCustomAction(var23, var16);
            var3.writeNoException();
            var9 = var8;
            break;
         case 27:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            MediaMetadataCompat var20 = this.getMetadata();
            var3.writeNoException();
            if (var20 != null) {
               var3.writeInt(1);
               var20.writeToParcel(var3, 1);
               var9 = var8;
            } else {
               var3.writeInt(0);
               var9 = var8;
            }
            break;
         case 28:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            PlaybackStateCompat var19 = this.getPlaybackState();
            var3.writeNoException();
            if (var19 != null) {
               var3.writeInt(1);
               var19.writeToParcel(var3, 1);
               var9 = var8;
            } else {
               var3.writeInt(0);
               var9 = var8;
            }
            break;
         case 29:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            List var18 = this.getQueue();
            var3.writeNoException();
            var3.writeTypedList(var18);
            var9 = var8;
            break;
         case 30:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            CharSequence var17 = this.getQueueTitle();
            var3.writeNoException();
            if (var17 != null) {
               var3.writeInt(1);
               TextUtils.writeToParcel(var17, var3, 1);
               var9 = var8;
            } else {
               var3.writeInt(0);
               var9 = var8;
            }
            break;
         case 31:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var16 = this.getExtras();
            var3.writeNoException();
            if (var16 != null) {
               var3.writeInt(1);
               var16.writeToParcel(var3, 1);
               var9 = var8;
            } else {
               var3.writeInt(0);
               var9 = var8;
            }
            break;
         case 32:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var1 = this.getRatingType();
            var3.writeNoException();
            var3.writeInt(var1);
            var9 = var8;
            break;
         case 33:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.prepare();
            var3.writeNoException();
            var9 = var8;
            break;
         case 34:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var23 = var2.readString();
            if (var2.readInt() != 0) {
               var16 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var16 = null;
            }

            this.prepareFromMediaId(var23, var16);
            var3.writeNoException();
            var9 = var8;
            break;
         case 35:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var23 = var2.readString();
            if (var2.readInt() != 0) {
               var16 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var16 = null;
            }

            this.prepareFromSearch(var23, var16);
            var3.writeNoException();
            var9 = var8;
            break;
         case 36:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            if (var2.readInt() != 0) {
               var22 = (Uri)Uri.CREATOR.createFromParcel(var2);
            } else {
               var22 = null;
            }

            if (var2.readInt() != 0) {
               var16 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var16 = null;
            }

            this.prepareFromUri(var22, var16);
            var3.writeNoException();
            var9 = var8;
            break;
         case 37:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var1 = this.getRepeatMode();
            var3.writeNoException();
            var3.writeInt(var1);
            var9 = var8;
            break;
         case 38:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            var9 = this.isShuffleModeEnabled();
            var3.writeNoException();
            var14 = var6;
            if (var9) {
               var14 = 1;
            }

            var3.writeInt(var14);
            var9 = var8;
            break;
         case 39:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.setRepeatMode(var2.readInt());
            var3.writeNoException();
            var9 = var8;
            break;
         case 40:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            if (var2.readInt() != 0) {
               var9 = true;
            } else {
               var9 = false;
            }

            this.setShuffleModeEnabled(var9);
            var3.writeNoException();
            var9 = var8;
            break;
         case 41:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            if (var2.readInt() != 0) {
               var15 = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var2);
            } else {
               var15 = null;
            }

            this.addQueueItem(var15);
            var3.writeNoException();
            var9 = var8;
            break;
         case 42:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            MediaDescriptionCompat var11;
            if (var2.readInt() != 0) {
               var11 = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var2);
            } else {
               var11 = null;
            }

            this.addQueueItemAt(var11, var2.readInt());
            var3.writeNoException();
            var9 = var8;
            break;
         case 43:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            if (var2.readInt() != 0) {
               var15 = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(var2);
            } else {
               var15 = null;
            }

            this.removeQueueItem(var15);
            var3.writeNoException();
            var9 = var8;
            break;
         case 44:
            var2.enforceInterface("android.support.v4.media.session.IMediaSession");
            this.removeQueueItemAt(var2.readInt());
            var3.writeNoException();
            var9 = var8;
            break;
         case 1598968902:
            var3.writeString("android.support.v4.media.session.IMediaSession");
            var9 = var8;
            break;
         default:
            var9 = super.onTransact(var1, var2, var3, var4);
         }

         return var9;
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
            label36: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(31, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label36;
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
            label36: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(8, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label36;
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
            label36: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(27, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label36;
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
            label36: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(28, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label36;
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
            label36: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(30, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label36;
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
            label36: {
               try {
                  var5 = true;
                  var1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  this.mRemote.transact(10, var1, var2, 0);
                  var2.readException();
                  if (var2.readInt() != 0) {
                     var3 = (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(var2);
                     var5 = false;
                     break label36;
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

         public boolean isShuffleModeEnabled() throws RemoteException {
            boolean var1 = false;
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var7 = false;

            int var4;
            try {
               var7 = true;
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(38, var2, var3, 0);
               var3.readException();
               var4 = var3.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var3.recycle();
                  var2.recycle();
               }
            }

            if (var4 != 0) {
               var1 = true;
            }

            var3.recycle();
            var2.recycle();
            return var1;
         }

         public boolean isTransportControlEnabled() throws RemoteException {
            boolean var1 = false;
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();
            boolean var7 = false;

            int var4;
            try {
               var7 = true;
               var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
               this.mRemote.transact(5, var2, var3, 0);
               var3.readException();
               var4 = var3.readInt();
               var7 = false;
            } finally {
               if (var7) {
                  var3.recycle();
                  var2.recycle();
               }
            }

            if (var4 != 0) {
               var1 = true;
            }

            var3.recycle();
            var2.recycle();
            return var1;
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

         public void registerCallbackListener(IMediaControllerCallback var1) throws RemoteException {
            Parcel var2 = Parcel.obtain();
            Parcel var3 = Parcel.obtain();

            label116: {
               Throwable var10000;
               label120: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label120;
                  }

                  IBinder var16;
                  if (var1 != null) {
                     try {
                        var16 = var1.asBinder();
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label120;
                     }
                  } else {
                     var16 = null;
                  }

                  label108:
                  try {
                     var2.writeStrongBinder(var16);
                     this.mRemote.transact(3, var2, var3, 0);
                     var3.readException();
                     break label116;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label108;
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
            boolean var2 = true;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            int var5;
            label186: {
               Throwable var10000;
               label190: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label190;
                  }

                  if (var1 != null) {
                     try {
                        var3.writeInt(1);
                        var1.writeToParcel(var3, 0);
                     } catch (Throwable var24) {
                        var10000 = var24;
                        var10001 = false;
                        break label190;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label190;
                     }
                  }

                  label176:
                  try {
                     this.mRemote.transact(2, var3, var4, 0);
                     var4.readException();
                     var5 = var4.readInt();
                     break label186;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label176;
                  }
               }

               Throwable var26 = var10000;
               var4.recycle();
               var3.recycle();
               throw var26;
            }

            if (var5 == 0) {
               var2 = false;
            }

            var4.recycle();
            var3.recycle();
            return var2;
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

         public void setShuffleModeEnabled(boolean var1) throws RemoteException {
            byte var2 = 0;
            Parcel var3 = Parcel.obtain();
            Parcel var4 = Parcel.obtain();

            label80: {
               Throwable var10000;
               label85: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label85;
                  }

                  if (var1) {
                     var2 = 1;
                  }

                  label75:
                  try {
                     var3.writeInt(var2);
                     this.mRemote.transact(40, var3, var4, 0);
                     var4.readException();
                     break label80;
                  } catch (Throwable var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label75;
                  }
               }

               Throwable var5 = var10000;
               var4.recycle();
               var3.recycle();
               throw var5;
            }

            var4.recycle();
            var3.recycle();
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

            label116: {
               Throwable var10000;
               label120: {
                  boolean var10001;
                  try {
                     var2.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
                  } catch (Throwable var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label120;
                  }

                  IBinder var16;
                  if (var1 != null) {
                     try {
                        var16 = var1.asBinder();
                     } catch (Throwable var14) {
                        var10000 = var14;
                        var10001 = false;
                        break label120;
                     }
                  } else {
                     var16 = null;
                  }

                  label108:
                  try {
                     var2.writeStrongBinder(var16);
                     this.mRemote.transact(4, var2, var3, 0);
                     var3.readException();
                     break label116;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label108;
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
