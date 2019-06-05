// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.firstrun;

class FirstrunPage
{
    public final int imageResource;
    public final String lottieString;
    public final CharSequence text;
    public final String title;
    
    FirstrunPage(final String title, final CharSequence text, final int imageResource) {
        this.title = title;
        this.text = text;
        this.lottieString = null;
        this.imageResource = imageResource;
    }
    
    FirstrunPage(final String title, final CharSequence text, final String lottieString) {
        this.title = title;
        this.text = text;
        this.lottieString = lottieString;
        this.imageResource = -1;
    }
}
