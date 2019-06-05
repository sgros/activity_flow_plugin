package android.support.design.stateful;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.AbsSavedState;

public class ExtendableSavedState extends AbsSavedState {
   public static final Creator CREATOR = new ClassLoaderCreator() {
      public ExtendableSavedState createFromParcel(Parcel var1) {
         return new ExtendableSavedState(var1, (ClassLoader)null);
      }

      public ExtendableSavedState createFromParcel(Parcel var1, ClassLoader var2) {
         return new ExtendableSavedState(var1, var2);
      }

      public ExtendableSavedState[] newArray(int var1) {
         return new ExtendableSavedState[var1];
      }
   };
   public final SimpleArrayMap extendableStates;

   private ExtendableSavedState(Parcel var1, ClassLoader var2) {
      super(var1, var2);
      int var3 = var1.readInt();
      String[] var4 = new String[var3];
      var1.readStringArray(var4);
      Bundle[] var6 = new Bundle[var3];
      var1.readTypedArray(var6, Bundle.CREATOR);
      this.extendableStates = new SimpleArrayMap(var3);

      for(int var5 = 0; var5 < var3; ++var5) {
         this.extendableStates.put(var4[var5], var6[var5]);
      }

   }

   // $FF: synthetic method
   ExtendableSavedState(Parcel var1, ClassLoader var2, Object var3) {
      this(var1, var2);
   }

   public ExtendableSavedState(Parcelable var1) {
      super(var1);
      this.extendableStates = new SimpleArrayMap();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ExtendableSavedState{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" states=");
      var1.append(this.extendableStates);
      var1.append("}");
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      int var3 = this.extendableStates.size();
      var1.writeInt(var3);
      String[] var4 = new String[var3];
      Bundle[] var5 = new Bundle[var3];

      for(var2 = 0; var2 < var3; ++var2) {
         var4[var2] = (String)this.extendableStates.keyAt(var2);
         var5[var2] = (Bundle)this.extendableStates.valueAt(var2);
      }

      var1.writeStringArray(var4);
      var1.writeTypedArray(var5, 0);
   }
}
