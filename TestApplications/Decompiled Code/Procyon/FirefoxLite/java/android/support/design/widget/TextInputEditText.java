// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.ViewParent;
import android.view.View;
import android.support.v7.widget.AppCompatEditText;

public class TextInputEditText extends AppCompatEditText
{
    private CharSequence getHintFromLayout() {
        final TextInputLayout textInputLayout = this.getTextInputLayout();
        CharSequence hint;
        if (textInputLayout != null) {
            hint = textInputLayout.getHint();
        }
        else {
            hint = null;
        }
        return hint;
    }
    
    private TextInputLayout getTextInputLayout() {
        for (ViewParent viewParent = this.getParent(); viewParent instanceof View; viewParent = viewParent.getParent()) {
            if (viewParent instanceof TextInputLayout) {
                return (TextInputLayout)viewParent;
            }
        }
        return null;
    }
    
    public CharSequence getHint() {
        final TextInputLayout textInputLayout = this.getTextInputLayout();
        if (textInputLayout != null && textInputLayout.isProvidingHint()) {
            return textInputLayout.getHint();
        }
        return super.getHint();
    }
    
    @Override
    public InputConnection onCreateInputConnection(final EditorInfo editorInfo) {
        final InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        if (onCreateInputConnection != null && editorInfo.hintText == null) {
            editorInfo.hintText = this.getHintFromLayout();
        }
        return onCreateInputConnection;
    }
}
