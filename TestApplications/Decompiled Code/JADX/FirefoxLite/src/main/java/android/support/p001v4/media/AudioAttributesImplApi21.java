package android.support.p001v4.media;

import android.annotation.TargetApi;
import android.media.AudioAttributes;

@TargetApi(21)
/* renamed from: android.support.v4.media.AudioAttributesImplApi21 */
class AudioAttributesImplApi21 implements AudioAttributesImpl {
    AudioAttributes mAudioAttributes;
    int mLegacyStreamType = -1;

    AudioAttributesImplApi21() {
    }

    public int hashCode() {
        return this.mAudioAttributes.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AudioAttributesImplApi21)) {
            return false;
        }
        return this.mAudioAttributes.equals(((AudioAttributesImplApi21) obj).mAudioAttributes);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AudioAttributesCompat: audioattributes=");
        stringBuilder.append(this.mAudioAttributes);
        return stringBuilder.toString();
    }
}
