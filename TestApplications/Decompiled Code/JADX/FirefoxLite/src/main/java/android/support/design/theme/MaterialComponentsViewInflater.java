package android.support.design.theme;

import android.content.Context;
import android.support.annotation.Keep;
import android.support.design.button.MaterialButton;
import android.support.p004v7.app.AppCompatViewInflater;
import android.support.p004v7.widget.AppCompatButton;
import android.util.AttributeSet;

@Keep
public class MaterialComponentsViewInflater extends AppCompatViewInflater {
    /* Access modifiers changed, original: protected */
    public AppCompatButton createButton(Context context, AttributeSet attributeSet) {
        return new MaterialButton(context, attributeSet);
    }
}
