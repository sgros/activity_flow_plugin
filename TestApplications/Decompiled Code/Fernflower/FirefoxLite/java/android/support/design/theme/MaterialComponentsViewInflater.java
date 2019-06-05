package android.support.design.theme;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatViewInflater;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

@Keep
public class MaterialComponentsViewInflater extends AppCompatViewInflater {
   protected AppCompatButton createButton(Context var1, AttributeSet var2) {
      return new MaterialButton(var1, var2);
   }
}
