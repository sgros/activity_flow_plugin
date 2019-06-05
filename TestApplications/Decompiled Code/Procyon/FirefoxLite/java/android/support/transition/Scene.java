// 
// Decompiled by Procyon v0.5.34
// 

package android.support.transition;

import android.view.View;
import android.view.ViewGroup;

public class Scene
{
    private Runnable mExitAction;
    private ViewGroup mSceneRoot;
    
    static Scene getCurrentScene(final View view) {
        return (Scene)view.getTag(R.id.transition_current_scene);
    }
    
    static void setCurrentScene(final View view, final Scene scene) {
        view.setTag(R.id.transition_current_scene, (Object)scene);
    }
    
    public void exit() {
        if (getCurrentScene((View)this.mSceneRoot) == this && this.mExitAction != null) {
            this.mExitAction.run();
        }
    }
}
