package mozilla.components.concept.engine;

public interface EngineView {
   void onCreate();

   void onDestroy();

   void onPause();

   void onResume();

   void onStart();

   void onStop();
}
