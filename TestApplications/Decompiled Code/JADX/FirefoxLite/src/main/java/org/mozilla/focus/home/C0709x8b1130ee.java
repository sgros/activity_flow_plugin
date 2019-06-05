package org.mozilla.focus.home;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$LoadRootConfigTask$N7ozq39_Jbe9oQNEx5wgAWpoCWU */
public final /* synthetic */ class C0709x8b1130ee implements OnConfigLoadedListener {
    private final /* synthetic */ LoadRootConfigTask f$0;
    private final /* synthetic */ String[] f$1;
    private final /* synthetic */ OnRootConfigLoadedListener f$2;

    public /* synthetic */ C0709x8b1130ee(LoadRootConfigTask loadRootConfigTask, String[] strArr, OnRootConfigLoadedListener onRootConfigLoadedListener) {
        this.f$0 = loadRootConfigTask;
        this.f$1 = strArr;
        this.f$2 = onRootConfigLoadedListener;
    }

    public final void onConfigLoaded(String str, int i) {
        LoadRootConfigTask.lambda$onPostExecute$0(this.f$0, this.f$1, this.f$2, str, i);
    }
}
