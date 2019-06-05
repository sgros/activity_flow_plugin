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

/* compiled from: QuickSearchUtils.kt */
final class QuickSearchUtils$loadEnginesFromAssets$1 implements Runnable {
    final /* synthetic */ Context $context;
    final /* synthetic */ MutableLiveData $liveData;
    final /* synthetic */ int $resId;

    QuickSearchUtils$loadEnginesFromAssets$1(Context context, int i, MutableLiveData mutableLiveData) {
        this.$context = context;
        this.$resId = i;
        this.$liveData = mutableLiveData;
    }

    public final void run() {
        try {
            JSONArray readRawJsonArray = IOUtils.readRawJsonArray(this.$context, this.$resId);
            ArrayList arrayList = new ArrayList();
            int i = 0;
            int length = readRawJsonArray.length();
            while (i < length) {
                Object obj = readRawJsonArray.get(i);
                if (obj != null) {
                    JSONObject jSONObject = (JSONObject) obj;
                    String optString = jSONObject.optString("name");
                    Intrinsics.checkExpressionValueIsNotNull(optString, "jsonObj.optString(\"name\")");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("file:///android_asset/topsites/icon/");
                    stringBuilder.append(jSONObject.optString("icon"));
                    String stringBuilder2 = stringBuilder.toString();
                    String optString2 = jSONObject.optString("searchUrlPattern");
                    Intrinsics.checkExpressionValueIsNotNull(optString2, "jsonObj.optString(\"searchUrlPattern\")");
                    String optString3 = jSONObject.optString("homeUrl");
                    Intrinsics.checkExpressionValueIsNotNull(optString3, "jsonObj.optString(\"homeUrl\")");
                    String optString4 = jSONObject.optString("urlPrefix");
                    Intrinsics.checkExpressionValueIsNotNull(optString4, "jsonObj.optString(\"urlPrefix\")");
                    String optString5 = jSONObject.optString("urlSuffix");
                    Intrinsics.checkExpressionValueIsNotNull(optString5, "jsonObj.optString(\"urlSuffix\")");
                    arrayList.add(new QuickSearch(optString, stringBuilder2, optString2, optString3, optString4, optString5, jSONObject.optBoolean("patternEncode"), jSONObject.optBoolean("permitSpace", true)));
                    i++;
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type org.json.JSONObject");
                }
            }
            this.$liveData.postValue(arrayList);
        } catch (JSONException unused) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Corrupt JSON asset (");
            stringBuilder3.append(this.$resId);
            stringBuilder3.append(')');
            throw new AssertionError(stringBuilder3.toString());
        }
    }
}
