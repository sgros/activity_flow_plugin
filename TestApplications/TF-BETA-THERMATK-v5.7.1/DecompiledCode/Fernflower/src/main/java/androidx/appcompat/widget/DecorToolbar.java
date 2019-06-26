package androidx.appcompat.widget;

import android.graphics.drawable.Drawable;
import android.view.Window.Callback;

public interface DecorToolbar {
   CharSequence getTitle();

   void setIcon(int var1);

   void setIcon(Drawable var1);

   void setLogo(int var1);

   void setWindowCallback(Callback var1);

   void setWindowTitle(CharSequence var1);
}
