package org.mozilla.rocket.urlinput;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import java.util.ArrayList;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.focus.utils.IOUtils;
import org.mozilla.threadutils.ThreadUtils;

public final class QuickSearchUtils {
   public static final QuickSearchUtils INSTANCE = new QuickSearchUtils();

   private QuickSearchUtils() {
   }

   private final void loadEnginesFromAssets(final Context var1, final int var2, final MutableLiveData var3) {
      ThreadUtils.postToBackgroundThread((Runnable)(new Runnable() {
         public final void run() {
            label52: {
               JSONArray var1x;
               ArrayList var2x;
               boolean var10001;
               try {
                  var1x = IOUtils.readRawJsonArray(var1, var2);
                  var2x = new ArrayList();
               } catch (JSONException var18) {
                  var10001 = false;
                  break label52;
               }

               int var3x = 0;

               int var4;
               try {
                  var4 = var1x.length();
               } catch (JSONException var17) {
                  var10001 = false;
                  break label52;
               }

               for(; var3x < var4; ++var3x) {
                  Object var5;
                  try {
                     var5 = var1x.get(var3x);
                  } catch (JSONException var16) {
                     var10001 = false;
                     break label52;
                  }

                  if (var5 == null) {
                     try {
                        TypeCastException var19 = new TypeCastException("null cannot be cast to non-null type org.json.JSONObject");
                        throw var19;
                     } catch (JSONException var13) {
                        var10001 = false;
                        break label52;
                     }
                  }

                  try {
                     JSONObject var6 = (JSONObject)var5;
                     String var21 = var6.optString("name");
                     Intrinsics.checkExpressionValueIsNotNull(var21, "jsonObj.optString(\"name\")");
                     StringBuilder var8 = new StringBuilder();
                     var8.append("file:///android_asset/topsites/icon/");
                     var8.append(var6.optString("icon"));
                     String var9 = var8.toString();
                     String var10 = var6.optString("searchUrlPattern");
                     Intrinsics.checkExpressionValueIsNotNull(var10, "jsonObj.optString(\"searchUrlPattern\")");
                     String var11 = var6.optString("homeUrl");
                     Intrinsics.checkExpressionValueIsNotNull(var11, "jsonObj.optString(\"homeUrl\")");
                     String var12 = var6.optString("urlPrefix");
                     Intrinsics.checkExpressionValueIsNotNull(var12, "jsonObj.optString(\"urlPrefix\")");
                     String var22 = var6.optString("urlSuffix");
                     Intrinsics.checkExpressionValueIsNotNull(var22, "jsonObj.optString(\"urlSuffix\")");
                     QuickSearch var7 = new QuickSearch(var21, var9, var10, var11, var12, var22, var6.optBoolean("patternEncode"), var6.optBoolean("permitSpace", true));
                     var2x.add(var7);
                  } catch (JSONException var15) {
                     var10001 = false;
                     break label52;
                  }
               }

               try {
                  var3.postValue(var2x);
                  return;
               } catch (JSONException var14) {
                  var10001 = false;
               }
            }

            StringBuilder var20 = new StringBuilder();
            var20.append("Corrupt JSON asset (");
            var20.append(var2);
            var20.append(')');
            throw (Throwable)(new AssertionError(var20.toString()));
         }
      }));
   }

   public final void loadDefaultEngines$app_focusWebkitRelease(Context var1, MutableLiveData var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "liveData");
      this.loadEnginesFromAssets(var1, 2131689483, var2);
   }

   public final void loadEnginesByLocale$app_focusWebkitRelease(Context var1, MutableLiveData var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "liveData");
      this.loadEnginesFromAssets(var1, 2131689482, var2);
   }
}
