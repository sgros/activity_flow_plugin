package net.sqlcipher;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class BulkCursorNative extends Binder implements IBulkCursor {
   public BulkCursorNative() {
      this.attachInterface(this, "android.content.IBulkCursor");
   }

   public static IBulkCursor asInterface(IBinder var0) {
      if (var0 == null) {
         return null;
      } else {
         IBulkCursor var1 = (IBulkCursor)var0.queryLocalInterface("android.content.IBulkCursor");
         return (IBulkCursor)(var1 != null ? var1 : new BulkCursorProxy(var0));
      }
   }

   public IBinder asBinder() {
      return this;
   }

   public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
      Exception var10000;
      byte var5 = 0;
      byte var6 = 0;
      byte var7 = 0;
      boolean var8;
      boolean var10001;
      byte var26;
      Bundle var27;
      label133:
      switch(var1) {
      case 1:
         CursorWindow var29;
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var29 = this.getWindow(var2.readInt());
         } catch (Exception var25) {
            var10000 = var25;
            var10001 = false;
            break;
         }

         if (var29 == null) {
            try {
               var3.writeInt(0);
               return true;
            } catch (Exception var23) {
               var10000 = var23;
               var10001 = false;
            }
         } else {
            try {
               var3.writeNoException();
               var3.writeInt(1);
               var29.writeToParcel(var3, 0);
               return true;
            } catch (Exception var24) {
               var10000 = var24;
               var10001 = false;
            }
         }
         break;
      case 2:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var1 = this.count();
            var3.writeNoException();
            var3.writeInt(var1);
            return true;
         } catch (Exception var22) {
            var10000 = var22;
            var10001 = false;
            break;
         }
      case 3:
         String[] var28;
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var28 = this.getColumnNames();
            var3.writeNoException();
            var3.writeInt(var28.length);
            var4 = var28.length;
         } catch (Exception var21) {
            var10000 = var21;
            var10001 = false;
            break;
         }

         for(var1 = var6; var1 < var4; ++var1) {
            try {
               var3.writeString(var28[var1]);
            } catch (Exception var20) {
               var10000 = var20;
               var10001 = false;
               break label133;
            }
         }

         return true;
      case 4:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var8 = this.updateRows(var2.readHashMap((ClassLoader)null));
            var3.writeNoException();
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
            break;
         }

         var26 = var5;
         if (var8) {
            var26 = 1;
         }

         try {
            var3.writeInt(var26);
            return true;
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break;
         }
      case 5:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var8 = this.deleteRow(var2.readInt());
            var3.writeNoException();
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break;
         }

         var26 = var7;
         if (var8) {
            var26 = 1;
         }

         try {
            var3.writeInt(var26);
            return true;
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break;
         }
      case 6:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            this.deactivate();
            var3.writeNoException();
            return true;
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break;
         }
      case 7:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var1 = this.requery(IContentObserver.Stub.asInterface(var2.readStrongBinder()), (CursorWindow)CursorWindow.CREATOR.createFromParcel(var2));
            var3.writeNoException();
            var3.writeInt(var1);
            var3.writeBundle(this.getExtras());
            return true;
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break;
         }
      case 8:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            this.onMove(var2.readInt());
            var3.writeNoException();
            return true;
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break;
         }
      case 9:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var26 = this.getWantsAllOnMoveCalls();
            var3.writeNoException();
            var3.writeInt(var26);
            return true;
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
            break;
         }
      case 10:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var27 = this.getExtras();
            var3.writeNoException();
            var3.writeBundle(var27);
            return true;
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break;
         }
      case 11:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            var27 = this.respond(var2.readBundle(this.getClass().getClassLoader()));
            var3.writeNoException();
            var3.writeBundle(var27);
            return true;
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break;
         }
      case 12:
         try {
            var2.enforceInterface("android.content.IBulkCursor");
            this.close();
            var3.writeNoException();
            return true;
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break;
         }
      default:
         return super.onTransact(var1, var2, var3, var4);
      }

      Exception var30 = var10000;
      DatabaseUtils.writeExceptionToParcel(var3, var30);
      return true;
   }
}
