package android.support.v4.app;

import android.app.RemoteInput.Builder;
import android.os.Bundle;
import java.util.Set;

public final class RemoteInput {
   private final boolean mAllowFreeFormTextInput;
   private final Set mAllowedDataTypes;
   private final CharSequence[] mChoices;
   private final Bundle mExtras;
   private final CharSequence mLabel;
   private final String mResultKey;

   static android.app.RemoteInput fromCompat(RemoteInput var0) {
      return (new Builder(var0.getResultKey())).setLabel(var0.getLabel()).setChoices(var0.getChoices()).setAllowFreeFormInput(var0.getAllowFreeFormInput()).addExtras(var0.getExtras()).build();
   }

   static android.app.RemoteInput[] fromCompat(RemoteInput[] var0) {
      if (var0 == null) {
         return null;
      } else {
         android.app.RemoteInput[] var1 = new android.app.RemoteInput[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = fromCompat(var0[var2]);
         }

         return var1;
      }
   }

   public boolean getAllowFreeFormInput() {
      return this.mAllowFreeFormTextInput;
   }

   public Set getAllowedDataTypes() {
      return this.mAllowedDataTypes;
   }

   public CharSequence[] getChoices() {
      return this.mChoices;
   }

   public Bundle getExtras() {
      return this.mExtras;
   }

   public CharSequence getLabel() {
      return this.mLabel;
   }

   public String getResultKey() {
      return this.mResultKey;
   }
}
