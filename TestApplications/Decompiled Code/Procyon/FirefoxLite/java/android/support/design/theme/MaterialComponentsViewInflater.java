// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.theme;

import android.support.design.button.MaterialButton;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatViewInflater;

@Keep
public class MaterialComponentsViewInflater extends AppCompatViewInflater
{
    @Override
    protected AppCompatButton createButton(final Context context, final AttributeSet set) {
        return new MaterialButton(context, set);
    }
}
