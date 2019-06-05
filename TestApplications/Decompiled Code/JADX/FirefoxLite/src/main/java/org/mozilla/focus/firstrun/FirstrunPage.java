package org.mozilla.focus.firstrun;

class FirstrunPage {
    public final int imageResource;
    public final String lottieString;
    public final CharSequence text;
    public final String title;

    FirstrunPage(String str, CharSequence charSequence, String str2) {
        this.title = str;
        this.text = charSequence;
        this.lottieString = str2;
        this.imageResource = -1;
    }

    FirstrunPage(String str, CharSequence charSequence, int i) {
        this.title = str;
        this.text = charSequence;
        this.lottieString = null;
        this.imageResource = i;
    }
}
