// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.concept.engine;

public interface EngineView
{
    void onCreate();
    
    void onDestroy();
    
    void onPause();
    
    void onResume();
    
    void onStart();
    
    void onStop();
}
