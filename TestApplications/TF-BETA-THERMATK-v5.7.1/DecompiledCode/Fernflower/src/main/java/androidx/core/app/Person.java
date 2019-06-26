package androidx.core.app;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;

public class Person {
   IconCompat mIcon;
   boolean mIsBot;
   boolean mIsImportant;
   String mKey;
   CharSequence mName;
   String mUri;

   Person(Person.Builder var1) {
      this.mName = var1.mName;
      this.mIcon = var1.mIcon;
      this.mUri = var1.mUri;
      this.mKey = var1.mKey;
      this.mIsBot = var1.mIsBot;
      this.mIsImportant = var1.mIsImportant;
   }

   public IconCompat getIcon() {
      return this.mIcon;
   }

   public String getKey() {
      return this.mKey;
   }

   public CharSequence getName() {
      return this.mName;
   }

   public String getUri() {
      return this.mUri;
   }

   public boolean isBot() {
      return this.mIsBot;
   }

   public boolean isImportant() {
      return this.mIsImportant;
   }

   public android.app.Person toAndroidPerson() {
      android.app.Person.Builder var1 = (new android.app.Person.Builder()).setName(this.getName());
      Icon var2;
      if (this.getIcon() != null) {
         var2 = this.getIcon().toIcon();
      } else {
         var2 = null;
      }

      return var1.setIcon(var2).setUri(this.getUri()).setKey(this.getKey()).setBot(this.isBot()).setImportant(this.isImportant()).build();
   }

   public Bundle toBundle() {
      Bundle var1 = new Bundle();
      var1.putCharSequence("name", this.mName);
      IconCompat var2 = this.mIcon;
      Bundle var3;
      if (var2 != null) {
         var3 = var2.toBundle();
      } else {
         var3 = null;
      }

      var1.putBundle("icon", var3);
      var1.putString("uri", this.mUri);
      var1.putString("key", this.mKey);
      var1.putBoolean("isBot", this.mIsBot);
      var1.putBoolean("isImportant", this.mIsImportant);
      return var1;
   }

   public static class Builder {
      IconCompat mIcon;
      boolean mIsBot;
      boolean mIsImportant;
      String mKey;
      CharSequence mName;
      String mUri;

      public Person build() {
         return new Person(this);
      }

      public Person.Builder setIcon(IconCompat var1) {
         this.mIcon = var1;
         return this;
      }

      public Person.Builder setName(CharSequence var1) {
         this.mName = var1;
         return this;
      }
   }
}
