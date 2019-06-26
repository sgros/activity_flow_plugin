package androidx.core.app;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import java.util.HashSet;
import java.util.Set;

public final class RemoteInput {
   private final boolean mAllowFreeFormTextInput;
   private final Set mAllowedDataTypes;
   private final CharSequence[] mChoices;
   private final Bundle mExtras;
   private final CharSequence mLabel;
   private final String mResultKey;

   RemoteInput(String var1, CharSequence var2, CharSequence[] var3, boolean var4, Bundle var5, Set var6) {
      this.mResultKey = var1;
      this.mLabel = var2;
      this.mChoices = var3;
      this.mAllowFreeFormTextInput = var4;
      this.mExtras = var5;
      this.mAllowedDataTypes = var6;
   }

   static android.app.RemoteInput fromCompat(RemoteInput var0) {
      return (new android.app.RemoteInput.Builder(var0.getResultKey())).setLabel(var0.getLabel()).setChoices(var0.getChoices()).setAllowFreeFormInput(var0.getAllowFreeFormInput()).addExtras(var0.getExtras()).build();
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

   private static Intent getClipDataIntentFromIntent(Intent var0) {
      ClipData var2 = var0.getClipData();
      if (var2 == null) {
         return null;
      } else {
         ClipDescription var1 = var2.getDescription();
         if (!var1.hasMimeType("text/vnd.android.intent")) {
            return null;
         } else {
            return !var1.getLabel().equals("android.remoteinput.results") ? null : var2.getItemAt(0).getIntent();
         }
      }
   }

   public static Bundle getResultsFromIntent(Intent var0) {
      int var1 = VERSION.SDK_INT;
      if (var1 >= 20) {
         return android.app.RemoteInput.getResultsFromIntent(var0);
      } else if (var1 >= 16) {
         var0 = getClipDataIntentFromIntent(var0);
         return var0 == null ? null : (Bundle)var0.getExtras().getParcelable("android.remoteinput.resultsData");
      } else {
         return null;
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

   public boolean isDataOnly() {
      boolean var1;
      if (!this.getAllowFreeFormInput() && (this.getChoices() == null || this.getChoices().length == 0) && this.getAllowedDataTypes() != null && !this.getAllowedDataTypes().isEmpty()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static final class Builder {
      private boolean mAllowFreeFormTextInput = true;
      private final Set mAllowedDataTypes = new HashSet();
      private CharSequence[] mChoices;
      private final Bundle mExtras = new Bundle();
      private CharSequence mLabel;
      private final String mResultKey;

      public Builder(String var1) {
         if (var1 != null) {
            this.mResultKey = var1;
         } else {
            throw new IllegalArgumentException("Result key can't be null");
         }
      }

      public RemoteInput build() {
         return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mExtras, this.mAllowedDataTypes);
      }

      public RemoteInput.Builder setLabel(CharSequence var1) {
         this.mLabel = var1;
         return this;
      }
   }
}
