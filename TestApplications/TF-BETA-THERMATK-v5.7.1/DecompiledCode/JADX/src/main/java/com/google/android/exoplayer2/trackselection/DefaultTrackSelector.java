package com.google.android.exoplayer2.trackselection;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection.Definition;
import com.google.android.exoplayer2.trackselection.TrackSelection.Factory;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultTrackSelector extends MappingTrackSelector {
    private static final int[] NO_TRACKS = new int[0];
    private final AtomicReference<Parameters> parametersReference = new AtomicReference(Parameters.DEFAULT);
    private final Factory trackSelectionFactory;

    private static final class AudioConfigurationTuple {
        public final int channelCount;
        public final String mimeType;
        public final int sampleRate;

        public AudioConfigurationTuple(int i, int i2, String str) {
            this.channelCount = i;
            this.sampleRate = i2;
            this.mimeType = str;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || AudioConfigurationTuple.class != obj.getClass()) {
                return false;
            }
            AudioConfigurationTuple audioConfigurationTuple = (AudioConfigurationTuple) obj;
            if (!(this.channelCount == audioConfigurationTuple.channelCount && this.sampleRate == audioConfigurationTuple.sampleRate && TextUtils.equals(this.mimeType, audioConfigurationTuple.mimeType))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int i = ((this.channelCount * 31) + this.sampleRate) * 31;
            String str = this.mimeType;
            return i + (str != null ? str.hashCode() : 0);
        }
    }

    protected static final class AudioTrackScore implements Comparable<AudioTrackScore> {
        private final int bitrate;
        private final int channelCount;
        private final int defaultSelectionFlagScore;
        public final boolean isWithinConstraints;
        private final int matchLanguageScore;
        private final Parameters parameters;
        private final int sampleRate;
        private final int withinRendererCapabilitiesScore;

        public AudioTrackScore(Format format, Parameters parameters, int i) {
            this.parameters = parameters;
            boolean z = false;
            this.withinRendererCapabilitiesScore = DefaultTrackSelector.isSupported(i, false);
            this.matchLanguageScore = DefaultTrackSelector.formatHasLanguage(format, parameters.preferredAudioLanguage);
            this.defaultSelectionFlagScore = (format.selectionFlags & 1) != 0 ? 1 : 0;
            this.channelCount = format.channelCount;
            this.sampleRate = format.sampleRate;
            i = format.bitrate;
            this.bitrate = i;
            if (i == -1 || i <= parameters.maxAudioBitrate) {
                int i2 = format.channelCount;
                if (i2 == -1 || i2 <= parameters.maxAudioChannelCount) {
                    z = true;
                }
            }
            this.isWithinConstraints = z;
        }

        public int compareTo(AudioTrackScore audioTrackScore) {
            int i = this.withinRendererCapabilitiesScore;
            int i2 = audioTrackScore.withinRendererCapabilitiesScore;
            if (i != i2) {
                return DefaultTrackSelector.compareInts(i, i2);
            }
            i = this.matchLanguageScore;
            i2 = audioTrackScore.matchLanguageScore;
            if (i != i2) {
                return DefaultTrackSelector.compareInts(i, i2);
            }
            boolean z = this.isWithinConstraints;
            int i3 = -1;
            if (z != audioTrackScore.isWithinConstraints) {
                if (z) {
                    i3 = 1;
                }
                return i3;
            }
            if (this.parameters.forceLowestBitrate) {
                i = DefaultTrackSelector.compareFormatValues(this.bitrate, audioTrackScore.bitrate);
                if (i != 0) {
                    if (i <= 0) {
                        i3 = 1;
                    }
                    return i3;
                }
            }
            i = this.defaultSelectionFlagScore;
            i2 = audioTrackScore.defaultSelectionFlagScore;
            if (i != i2) {
                return DefaultTrackSelector.compareInts(i, i2);
            }
            int access$300;
            if (this.isWithinConstraints && this.withinRendererCapabilitiesScore == 1) {
                i3 = 1;
            }
            i = this.channelCount;
            i2 = audioTrackScore.channelCount;
            if (i != i2) {
                access$300 = DefaultTrackSelector.compareInts(i, i2);
            } else {
                i = this.sampleRate;
                i2 = audioTrackScore.sampleRate;
                if (i != i2) {
                    access$300 = DefaultTrackSelector.compareInts(i, i2);
                } else {
                    access$300 = DefaultTrackSelector.compareInts(this.bitrate, audioTrackScore.bitrate);
                }
            }
            return i3 * access$300;
        }
    }

    public static final class Parameters implements Parcelable {
        public static final Creator<Parameters> CREATOR = new C02181();
        public static final Parameters DEFAULT = new Parameters();
        public final boolean allowAudioMixedMimeTypeAdaptiveness;
        public final boolean allowAudioMixedSampleRateAdaptiveness;
        @Deprecated
        public final boolean allowMixedMimeAdaptiveness;
        @Deprecated
        public final boolean allowNonSeamlessAdaptiveness;
        public final boolean allowVideoMixedMimeTypeAdaptiveness;
        public final boolean allowVideoNonSeamlessAdaptiveness;
        public final int disabledTextTrackSelectionFlags;
        public final boolean exceedAudioConstraintsIfNecessary;
        public final boolean exceedRendererCapabilitiesIfNecessary;
        public final boolean exceedVideoConstraintsIfNecessary;
        public final boolean forceHighestSupportedBitrate;
        public final boolean forceLowestBitrate;
        public final int maxAudioBitrate;
        public final int maxAudioChannelCount;
        public final int maxVideoBitrate;
        public final int maxVideoFrameRate;
        public final int maxVideoHeight;
        public final int maxVideoWidth;
        public final String preferredAudioLanguage;
        public final String preferredTextLanguage;
        private final SparseBooleanArray rendererDisabledFlags;
        public final boolean selectUndeterminedTextLanguage;
        private final SparseArray<Map<TrackGroupArray, SelectionOverride>> selectionOverrides;
        public final int tunnelingAudioSessionId;
        public final int viewportHeight;
        public final boolean viewportOrientationMayChange;
        public final int viewportWidth;

        /* renamed from: com.google.android.exoplayer2.trackselection.DefaultTrackSelector$Parameters$1 */
        static class C02181 implements Creator<Parameters> {
            C02181() {
            }

            public Parameters createFromParcel(Parcel parcel) {
                return new Parameters(parcel);
            }

            public Parameters[] newArray(int i) {
                return new Parameters[i];
            }
        }

        public int describeContents() {
            return 0;
        }

        private Parameters() {
            SparseArray sparseArray = r1;
            SparseArray sparseArray2 = new SparseArray();
            SparseBooleanArray sparseBooleanArray = r1;
            SparseBooleanArray sparseBooleanArray2 = new SparseBooleanArray();
            this(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, true, Integer.MAX_VALUE, Integer.MAX_VALUE, true, null, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false, false, null, false, 0, false, false, true, 0, sparseArray, sparseBooleanArray);
        }

        Parameters(int i, int i2, int i3, int i4, boolean z, boolean z2, boolean z3, int i5, int i6, boolean z4, String str, int i7, int i8, boolean z5, boolean z6, boolean z7, String str2, boolean z8, int i9, boolean z9, boolean z10, boolean z11, int i10, SparseArray<Map<TrackGroupArray, SelectionOverride>> sparseArray, SparseBooleanArray sparseBooleanArray) {
            boolean z12 = z2;
            boolean z13 = z3;
            this.maxVideoWidth = i;
            this.maxVideoHeight = i2;
            this.maxVideoFrameRate = i3;
            this.maxVideoBitrate = i4;
            this.exceedVideoConstraintsIfNecessary = z;
            this.allowVideoMixedMimeTypeAdaptiveness = z12;
            this.allowVideoNonSeamlessAdaptiveness = z13;
            this.viewportWidth = i5;
            this.viewportHeight = i6;
            this.viewportOrientationMayChange = z4;
            this.preferredAudioLanguage = Util.normalizeLanguageCode(str);
            this.maxAudioChannelCount = i7;
            this.maxAudioBitrate = i8;
            this.exceedAudioConstraintsIfNecessary = z5;
            this.allowAudioMixedMimeTypeAdaptiveness = z6;
            this.allowAudioMixedSampleRateAdaptiveness = z7;
            this.preferredTextLanguage = Util.normalizeLanguageCode(str2);
            this.selectUndeterminedTextLanguage = z8;
            this.disabledTextTrackSelectionFlags = i9;
            this.forceLowestBitrate = z9;
            this.forceHighestSupportedBitrate = z10;
            this.exceedRendererCapabilitiesIfNecessary = z11;
            this.tunnelingAudioSessionId = i10;
            this.selectionOverrides = sparseArray;
            this.rendererDisabledFlags = sparseBooleanArray;
            this.allowMixedMimeAdaptiveness = z12;
            this.allowNonSeamlessAdaptiveness = z13;
        }

        Parameters(Parcel parcel) {
            this.maxVideoWidth = parcel.readInt();
            this.maxVideoHeight = parcel.readInt();
            this.maxVideoFrameRate = parcel.readInt();
            this.maxVideoBitrate = parcel.readInt();
            this.exceedVideoConstraintsIfNecessary = Util.readBoolean(parcel);
            this.allowVideoMixedMimeTypeAdaptiveness = Util.readBoolean(parcel);
            this.allowVideoNonSeamlessAdaptiveness = Util.readBoolean(parcel);
            this.viewportWidth = parcel.readInt();
            this.viewportHeight = parcel.readInt();
            this.viewportOrientationMayChange = Util.readBoolean(parcel);
            this.preferredAudioLanguage = parcel.readString();
            this.maxAudioChannelCount = parcel.readInt();
            this.maxAudioBitrate = parcel.readInt();
            this.exceedAudioConstraintsIfNecessary = Util.readBoolean(parcel);
            this.allowAudioMixedMimeTypeAdaptiveness = Util.readBoolean(parcel);
            this.allowAudioMixedSampleRateAdaptiveness = Util.readBoolean(parcel);
            this.preferredTextLanguage = parcel.readString();
            this.selectUndeterminedTextLanguage = Util.readBoolean(parcel);
            this.disabledTextTrackSelectionFlags = parcel.readInt();
            this.forceLowestBitrate = Util.readBoolean(parcel);
            this.forceHighestSupportedBitrate = Util.readBoolean(parcel);
            this.exceedRendererCapabilitiesIfNecessary = Util.readBoolean(parcel);
            this.tunnelingAudioSessionId = parcel.readInt();
            this.selectionOverrides = readSelectionOverrides(parcel);
            this.rendererDisabledFlags = parcel.readSparseBooleanArray();
            this.allowMixedMimeAdaptiveness = this.allowVideoMixedMimeTypeAdaptiveness;
            this.allowNonSeamlessAdaptiveness = this.allowVideoNonSeamlessAdaptiveness;
        }

        public final boolean getRendererDisabled(int i) {
            return this.rendererDisabledFlags.get(i);
        }

        public final boolean hasSelectionOverride(int i, TrackGroupArray trackGroupArray) {
            Map map = (Map) this.selectionOverrides.get(i);
            return map != null && map.containsKey(trackGroupArray);
        }

        public final SelectionOverride getSelectionOverride(int i, TrackGroupArray trackGroupArray) {
            Map map = (Map) this.selectionOverrides.get(i);
            return map != null ? (SelectionOverride) map.get(trackGroupArray) : null;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || Parameters.class != obj.getClass()) {
                return false;
            }
            Parameters parameters = (Parameters) obj;
            if (!(this.maxVideoWidth == parameters.maxVideoWidth && this.maxVideoHeight == parameters.maxVideoHeight && this.maxVideoFrameRate == parameters.maxVideoFrameRate && this.maxVideoBitrate == parameters.maxVideoBitrate && this.exceedVideoConstraintsIfNecessary == parameters.exceedVideoConstraintsIfNecessary && this.allowVideoMixedMimeTypeAdaptiveness == parameters.allowVideoMixedMimeTypeAdaptiveness && this.allowVideoNonSeamlessAdaptiveness == parameters.allowVideoNonSeamlessAdaptiveness && this.viewportOrientationMayChange == parameters.viewportOrientationMayChange && this.viewportWidth == parameters.viewportWidth && this.viewportHeight == parameters.viewportHeight && TextUtils.equals(this.preferredAudioLanguage, parameters.preferredAudioLanguage) && this.maxAudioChannelCount == parameters.maxAudioChannelCount && this.maxAudioBitrate == parameters.maxAudioBitrate && this.exceedAudioConstraintsIfNecessary == parameters.exceedAudioConstraintsIfNecessary && this.allowAudioMixedMimeTypeAdaptiveness == parameters.allowAudioMixedMimeTypeAdaptiveness && this.allowAudioMixedSampleRateAdaptiveness == parameters.allowAudioMixedSampleRateAdaptiveness && TextUtils.equals(this.preferredTextLanguage, parameters.preferredTextLanguage) && this.selectUndeterminedTextLanguage == parameters.selectUndeterminedTextLanguage && this.disabledTextTrackSelectionFlags == parameters.disabledTextTrackSelectionFlags && this.forceLowestBitrate == parameters.forceLowestBitrate && this.forceHighestSupportedBitrate == parameters.forceHighestSupportedBitrate && this.exceedRendererCapabilitiesIfNecessary == parameters.exceedRendererCapabilitiesIfNecessary && this.tunnelingAudioSessionId == parameters.tunnelingAudioSessionId && areRendererDisabledFlagsEqual(this.rendererDisabledFlags, parameters.rendererDisabledFlags) && areSelectionOverridesEqual(this.selectionOverrides, parameters.selectionOverrides))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int i;
            int i2 = (((((((((((((((((((this.maxVideoWidth + 31) * 31) + this.maxVideoHeight) * 31) + this.maxVideoFrameRate) * 31) + this.maxVideoBitrate) * 31) + this.exceedVideoConstraintsIfNecessary) * 31) + this.allowVideoMixedMimeTypeAdaptiveness) * 31) + this.allowVideoNonSeamlessAdaptiveness) * 31) + this.viewportOrientationMayChange) * 31) + this.viewportWidth) * 31) + this.viewportHeight) * 31;
            String str = this.preferredAudioLanguage;
            int i3 = 0;
            if (str == null) {
                i = 0;
            } else {
                i = str.hashCode();
            }
            i2 = (((((((((((i2 + i) * 31) + this.maxAudioChannelCount) * 31) + this.maxAudioBitrate) * 31) + this.exceedAudioConstraintsIfNecessary) * 31) + this.allowAudioMixedMimeTypeAdaptiveness) * 31) + this.allowAudioMixedSampleRateAdaptiveness) * 31;
            str = this.preferredTextLanguage;
            if (str != null) {
                i3 = str.hashCode();
            }
            return ((((((((((((i2 + i3) * 31) + this.selectUndeterminedTextLanguage) * 31) + this.disabledTextTrackSelectionFlags) * 31) + this.forceLowestBitrate) * 31) + this.forceHighestSupportedBitrate) * 31) + this.exceedRendererCapabilitiesIfNecessary) * 31) + this.tunnelingAudioSessionId;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.maxVideoWidth);
            parcel.writeInt(this.maxVideoHeight);
            parcel.writeInt(this.maxVideoFrameRate);
            parcel.writeInt(this.maxVideoBitrate);
            Util.writeBoolean(parcel, this.exceedVideoConstraintsIfNecessary);
            Util.writeBoolean(parcel, this.allowVideoMixedMimeTypeAdaptiveness);
            Util.writeBoolean(parcel, this.allowVideoNonSeamlessAdaptiveness);
            parcel.writeInt(this.viewportWidth);
            parcel.writeInt(this.viewportHeight);
            Util.writeBoolean(parcel, this.viewportOrientationMayChange);
            parcel.writeString(this.preferredAudioLanguage);
            parcel.writeInt(this.maxAudioChannelCount);
            parcel.writeInt(this.maxAudioBitrate);
            Util.writeBoolean(parcel, this.exceedAudioConstraintsIfNecessary);
            Util.writeBoolean(parcel, this.allowAudioMixedMimeTypeAdaptiveness);
            Util.writeBoolean(parcel, this.allowAudioMixedSampleRateAdaptiveness);
            parcel.writeString(this.preferredTextLanguage);
            Util.writeBoolean(parcel, this.selectUndeterminedTextLanguage);
            parcel.writeInt(this.disabledTextTrackSelectionFlags);
            Util.writeBoolean(parcel, this.forceLowestBitrate);
            Util.writeBoolean(parcel, this.forceHighestSupportedBitrate);
            Util.writeBoolean(parcel, this.exceedRendererCapabilitiesIfNecessary);
            parcel.writeInt(this.tunnelingAudioSessionId);
            writeSelectionOverridesToParcel(parcel, this.selectionOverrides);
            parcel.writeSparseBooleanArray(this.rendererDisabledFlags);
        }

        private static SparseArray<Map<TrackGroupArray, SelectionOverride>> readSelectionOverrides(Parcel parcel) {
            int readInt = parcel.readInt();
            SparseArray sparseArray = new SparseArray(readInt);
            for (int i = 0; i < readInt; i++) {
                int readInt2 = parcel.readInt();
                int readInt3 = parcel.readInt();
                HashMap hashMap = new HashMap(readInt3);
                for (int i2 = 0; i2 < readInt3; i2++) {
                    hashMap.put((TrackGroupArray) parcel.readParcelable(TrackGroupArray.class.getClassLoader()), (SelectionOverride) parcel.readParcelable(SelectionOverride.class.getClassLoader()));
                }
                sparseArray.put(readInt2, hashMap);
            }
            return sparseArray;
        }

        private static void writeSelectionOverridesToParcel(Parcel parcel, SparseArray<Map<TrackGroupArray, SelectionOverride>> sparseArray) {
            int size = sparseArray.size();
            parcel.writeInt(size);
            for (int i = 0; i < size; i++) {
                int keyAt = sparseArray.keyAt(i);
                Map map = (Map) sparseArray.valueAt(i);
                int size2 = map.size();
                parcel.writeInt(keyAt);
                parcel.writeInt(size2);
                for (Entry entry : map.entrySet()) {
                    parcel.writeParcelable((Parcelable) entry.getKey(), 0);
                    parcel.writeParcelable((Parcelable) entry.getValue(), 0);
                }
            }
        }

        private static boolean areRendererDisabledFlagsEqual(SparseBooleanArray sparseBooleanArray, SparseBooleanArray sparseBooleanArray2) {
            int size = sparseBooleanArray.size();
            if (sparseBooleanArray2.size() != size) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (sparseBooleanArray2.indexOfKey(sparseBooleanArray.keyAt(i)) < 0) {
                    return false;
                }
            }
            return true;
        }

        private static boolean areSelectionOverridesEqual(SparseArray<Map<TrackGroupArray, SelectionOverride>> sparseArray, SparseArray<Map<TrackGroupArray, SelectionOverride>> sparseArray2) {
            int size = sparseArray.size();
            if (sparseArray2.size() != size) {
                return false;
            }
            int i = 0;
            while (i < size) {
                int indexOfKey = sparseArray2.indexOfKey(sparseArray.keyAt(i));
                if (indexOfKey < 0 || !areSelectionOverridesEqual((Map) sparseArray.valueAt(i), (Map) sparseArray2.valueAt(indexOfKey))) {
                    return false;
                }
                i++;
            }
            return true;
        }

        private static boolean areSelectionOverridesEqual(Map<TrackGroupArray, SelectionOverride> map, Map<TrackGroupArray, SelectionOverride> map2) {
            if (map2.size() != map.size()) {
                return false;
            }
            for (Entry entry : map.entrySet()) {
                TrackGroupArray trackGroupArray = (TrackGroupArray) entry.getKey();
                if (map2.containsKey(trackGroupArray)) {
                    if (!Util.areEqual(entry.getValue(), map2.get(trackGroupArray))) {
                    }
                }
                return false;
            }
            return true;
        }
    }

    public static final class SelectionOverride implements Parcelable {
        public static final Creator<SelectionOverride> CREATOR = new C02191();
        public final int groupIndex;
        public final int length;
        public final int[] tracks = new int[this.length];

        /* renamed from: com.google.android.exoplayer2.trackselection.DefaultTrackSelector$SelectionOverride$1 */
        static class C02191 implements Creator<SelectionOverride> {
            C02191() {
            }

            public SelectionOverride createFromParcel(Parcel parcel) {
                return new SelectionOverride(parcel);
            }

            public SelectionOverride[] newArray(int i) {
                return new SelectionOverride[i];
            }
        }

        public int describeContents() {
            return 0;
        }

        SelectionOverride(Parcel parcel) {
            this.groupIndex = parcel.readInt();
            this.length = parcel.readByte();
            parcel.readIntArray(this.tracks);
        }

        public int hashCode() {
            return (this.groupIndex * 31) + Arrays.hashCode(this.tracks);
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || SelectionOverride.class != obj.getClass()) {
                return false;
            }
            SelectionOverride selectionOverride = (SelectionOverride) obj;
            if (!(this.groupIndex == selectionOverride.groupIndex && Arrays.equals(this.tracks, selectionOverride.tracks))) {
                z = false;
            }
            return z;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.groupIndex);
            parcel.writeInt(this.tracks.length);
            parcel.writeIntArray(this.tracks);
        }
    }

    private static int compareFormatValues(int i, int i2) {
        return i == -1 ? i2 == -1 ? 0 : -1 : i2 == -1 ? 1 : i - i2;
    }

    private static int compareInts(int i, int i2) {
        return i > i2 ? 1 : i2 > i ? -1 : 0;
    }

    protected static boolean isSupported(int i, boolean z) {
        i &= 7;
        return i == 4 || (z && i == 3);
    }

    public DefaultTrackSelector(Factory factory) {
        this.trackSelectionFactory = factory;
    }

    /* Access modifiers changed, original: protected|final */
    public final Pair<RendererConfiguration[], TrackSelection[]> selectTracks(MappedTrackInfo mappedTrackInfo, int[][][] iArr, int[] iArr2) throws ExoPlaybackException {
        Parameters parameters = (Parameters) this.parametersReference.get();
        int rendererCount = mappedTrackInfo.getRendererCount();
        Definition[] selectAllTracks = selectAllTracks(mappedTrackInfo, iArr, iArr2, parameters);
        int i = 0;
        while (true) {
            Definition definition = null;
            if (i >= rendererCount) {
                break;
            }
            if (parameters.getRendererDisabled(i)) {
                selectAllTracks[i] = null;
            } else {
                TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
                if (parameters.hasSelectionOverride(i, trackGroups)) {
                    SelectionOverride selectionOverride = parameters.getSelectionOverride(i, trackGroups);
                    if (selectionOverride != null) {
                        definition = new Definition(trackGroups.get(selectionOverride.groupIndex), selectionOverride.tracks);
                    }
                    selectAllTracks[i] = definition;
                }
            }
            i++;
        }
        TrackSelection[] createTrackSelections = this.trackSelectionFactory.createTrackSelections(selectAllTracks, getBandwidthMeter());
        RendererConfiguration[] rendererConfigurationArr = new RendererConfiguration[rendererCount];
        for (int i2 = 0; i2 < rendererCount; i2++) {
            Object obj = (parameters.getRendererDisabled(i2) || (mappedTrackInfo.getRendererType(i2) != 6 && createTrackSelections[i2] == null)) ? null : 1;
            rendererConfigurationArr[i2] = obj != null ? RendererConfiguration.DEFAULT : null;
        }
        maybeConfigureRenderersForTunneling(mappedTrackInfo, iArr, rendererConfigurationArr, createTrackSelections, parameters.tunnelingAudioSessionId);
        return Pair.create(rendererConfigurationArr, createTrackSelections);
    }

    /* Access modifiers changed, original: protected */
    public Definition[] selectAllTracks(MappedTrackInfo mappedTrackInfo, int[][][] iArr, int[] iArr2, Parameters parameters) throws ExoPlaybackException {
        int i;
        int i2;
        DefaultTrackSelector defaultTrackSelector = this;
        MappedTrackInfo mappedTrackInfo2 = mappedTrackInfo;
        Parameters parameters2 = parameters;
        int rendererCount = mappedTrackInfo.getRendererCount();
        Definition[] definitionArr = new Definition[rendererCount];
        Object obj = null;
        int i3 = 0;
        int i4 = 0;
        while (true) {
            i = 2;
            i2 = 1;
            if (i3 >= rendererCount) {
                break;
            }
            if (2 == mappedTrackInfo2.getRendererType(i3)) {
                if (obj == null) {
                    definitionArr[i3] = selectVideoTrack(mappedTrackInfo2.getTrackGroups(i3), iArr[i3], iArr2[i3], parameters, true);
                    obj = definitionArr[i3] != null ? 1 : null;
                }
                if (mappedTrackInfo2.getTrackGroups(i3).length <= 0) {
                    i2 = 0;
                }
                i4 |= i2;
            }
            i3++;
        }
        Definition definition = null;
        int i5 = -1;
        AudioTrackScore audioTrackScore = null;
        int i6 = -1;
        int i7 = -1;
        int i8 = Integer.MIN_VALUE;
        int i9 = 0;
        while (i9 < rendererCount) {
            Definition definition2;
            AudioTrackScore audioTrackScore2;
            Definition definition3;
            AudioTrackScore audioTrackScore3;
            int rendererType = mappedTrackInfo2.getRendererType(i9);
            if (rendererType != i2) {
                if (rendererType != i) {
                    if (rendererType != 3) {
                        definitionArr[i9] = defaultTrackSelector.selectOtherTrack(rendererType, mappedTrackInfo2.getTrackGroups(i9), iArr[i9], parameters2);
                    } else {
                        Pair selectTextTrack = defaultTrackSelector.selectTextTrack(mappedTrackInfo2.getTrackGroups(i9), iArr[i9], parameters2);
                        if (selectTextTrack != null && ((Integer) selectTextTrack.second).intValue() > i8) {
                            if (i7 != i5) {
                                definitionArr[i7] = definition;
                            }
                            definitionArr[i9] = (Definition) selectTextTrack.first;
                            i8 = ((Integer) selectTextTrack.second).intValue();
                            i7 = i9;
                            i2 = i7;
                            definition2 = definition;
                            i3 = i6;
                            audioTrackScore2 = audioTrackScore;
                            definition3 = definition2;
                            i9 = i2 + 1;
                            i5 = -1;
                            i = 2;
                            i2 = 1;
                            defaultTrackSelector = this;
                            mappedTrackInfo2 = mappedTrackInfo;
                            definition2 = definition3;
                            audioTrackScore = audioTrackScore2;
                            i6 = i3;
                            definition = definition2;
                        }
                    }
                }
                audioTrackScore3 = audioTrackScore;
                rendererType = i7;
                i = i8;
                i2 = i9;
                definition3 = definition;
                i3 = i6;
            } else {
                boolean z;
                TrackGroupArray trackGroups = mappedTrackInfo2.getTrackGroups(i9);
                int[][] iArr3 = iArr[i9];
                i2 = iArr2[i9];
                if (i4 == 0) {
                    i3 = i6;
                    z = true;
                } else {
                    i3 = i6;
                    z = false;
                }
                audioTrackScore3 = audioTrackScore;
                TrackGroupArray trackGroupArray = trackGroups;
                rendererType = i7;
                int[][] iArr4 = iArr3;
                i = i8;
                i8 = i2;
                i2 = i9;
                Pair selectAudioTrack = selectAudioTrack(trackGroupArray, iArr4, i8, parameters, z);
                if (selectAudioTrack == null || (audioTrackScore3 != null && ((AudioTrackScore) selectAudioTrack.second).compareTo(audioTrackScore3) <= 0)) {
                    definition3 = null;
                } else {
                    if (i3 != -1) {
                        definition3 = null;
                        definitionArr[i3] = null;
                    } else {
                        definition3 = null;
                    }
                    definitionArr[i2] = (Definition) selectAudioTrack.first;
                    audioTrackScore2 = (AudioTrackScore) selectAudioTrack.second;
                    i7 = rendererType;
                    i8 = i;
                    i3 = i2;
                    i9 = i2 + 1;
                    i5 = -1;
                    i = 2;
                    i2 = 1;
                    defaultTrackSelector = this;
                    mappedTrackInfo2 = mappedTrackInfo;
                    definition2 = definition3;
                    audioTrackScore = audioTrackScore2;
                    i6 = i3;
                    definition = definition2;
                }
            }
            audioTrackScore2 = audioTrackScore3;
            i7 = rendererType;
            i8 = i;
            i9 = i2 + 1;
            i5 = -1;
            i = 2;
            i2 = 1;
            defaultTrackSelector = this;
            mappedTrackInfo2 = mappedTrackInfo;
            definition2 = definition3;
            audioTrackScore = audioTrackScore2;
            i6 = i3;
            definition = definition2;
        }
        return definitionArr;
    }

    /* Access modifiers changed, original: protected */
    public Definition selectVideoTrack(TrackGroupArray trackGroupArray, int[][] iArr, int i, Parameters parameters, boolean z) throws ExoPlaybackException {
        Definition selectAdaptiveVideoTrack = (parameters.forceHighestSupportedBitrate || parameters.forceLowestBitrate || !z) ? null : selectAdaptiveVideoTrack(trackGroupArray, iArr, i, parameters);
        return selectAdaptiveVideoTrack == null ? selectFixedVideoTrack(trackGroupArray, iArr, parameters) : selectAdaptiveVideoTrack;
    }

    private static Definition selectAdaptiveVideoTrack(TrackGroupArray trackGroupArray, int[][] iArr, int i, Parameters parameters) {
        TrackGroupArray trackGroupArray2 = trackGroupArray;
        Parameters parameters2 = parameters;
        int i2 = parameters2.allowVideoNonSeamlessAdaptiveness ? 24 : 16;
        boolean z = parameters2.allowVideoMixedMimeTypeAdaptiveness && (i & i2) != 0;
        int i3 = 0;
        while (i3 < trackGroupArray2.length) {
            TrackGroup trackGroup = trackGroupArray2.get(i3);
            TrackGroup trackGroup2 = trackGroup;
            int[] adaptiveVideoTracksForGroup = getAdaptiveVideoTracksForGroup(trackGroup, iArr[i3], z, i2, parameters2.maxVideoWidth, parameters2.maxVideoHeight, parameters2.maxVideoFrameRate, parameters2.maxVideoBitrate, parameters2.viewportWidth, parameters2.viewportHeight, parameters2.viewportOrientationMayChange);
            if (adaptiveVideoTracksForGroup.length > 0) {
                return new Definition(trackGroup2, adaptiveVideoTracksForGroup);
            }
            i3++;
            trackGroupArray2 = trackGroupArray;
        }
        return null;
    }

    private static int[] getAdaptiveVideoTracksForGroup(TrackGroup trackGroup, int[] iArr, boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        TrackGroup trackGroup2 = trackGroup;
        if (trackGroup2.length < 2) {
            return NO_TRACKS;
        }
        List viewportFilteredTrackIndices = getViewportFilteredTrackIndices(trackGroup2, i6, i7, z2);
        if (viewportFilteredTrackIndices.size() < 2) {
            return NO_TRACKS;
        }
        String str;
        if (z) {
            str = null;
        } else {
            HashSet hashSet = new HashSet();
            String str2 = null;
            int i8 = 0;
            for (int i9 = 0; i9 < viewportFilteredTrackIndices.size(); i9++) {
                String str3 = trackGroup2.getFormat(((Integer) viewportFilteredTrackIndices.get(i9)).intValue()).sampleMimeType;
                if (hashSet.add(str3)) {
                    String str4 = str3;
                    int adaptiveVideoTrackCountForMimeType = getAdaptiveVideoTrackCountForMimeType(trackGroup, iArr, i, str3, i2, i3, i4, i5, viewportFilteredTrackIndices);
                    if (adaptiveVideoTrackCountForMimeType > i8) {
                        i8 = adaptiveVideoTrackCountForMimeType;
                        str2 = str4;
                    }
                }
            }
            str = str2;
        }
        filterAdaptiveVideoTrackCountForMimeType(trackGroup, iArr, i, str, i2, i3, i4, i5, viewportFilteredTrackIndices);
        return viewportFilteredTrackIndices.size() < 2 ? NO_TRACKS : Util.toArray(viewportFilteredTrackIndices);
    }

    private static int getAdaptiveVideoTrackCountForMimeType(TrackGroup trackGroup, int[] iArr, int i, String str, int i2, int i3, int i4, int i5, List<Integer> list) {
        int i6 = 0;
        for (int i7 = 0; i7 < list.size(); i7++) {
            int intValue = ((Integer) list.get(i7)).intValue();
            TrackGroup trackGroup2 = trackGroup;
            if (isSupportedAdaptiveVideoTrack(trackGroup.getFormat(intValue), str, iArr[intValue], i, i2, i3, i4, i5)) {
                i6++;
            }
        }
        return i6;
    }

    private static void filterAdaptiveVideoTrackCountForMimeType(TrackGroup trackGroup, int[] iArr, int i, String str, int i2, int i3, int i4, int i5, List<Integer> list) {
        List<Integer> list2 = list;
        for (int size = list.size() - 1; size >= 0; size--) {
            int intValue = ((Integer) list2.get(size)).intValue();
            TrackGroup trackGroup2 = trackGroup;
            if (!isSupportedAdaptiveVideoTrack(trackGroup.getFormat(intValue), str, iArr[intValue], i, i2, i3, i4, i5)) {
                list2.remove(size);
            }
        }
    }

    private static boolean isSupportedAdaptiveVideoTrack(Format format, String str, int i, int i2, int i3, int i4, int i5, int i6) {
        if (!isSupported(i, false) || (i & i2) == 0) {
            return false;
        }
        if (str != null && !Util.areEqual(format.sampleMimeType, str)) {
            return false;
        }
        int i7 = format.width;
        if (i7 != -1 && i7 > i3) {
            return false;
        }
        i7 = format.height;
        if (i7 != -1 && i7 > i4) {
            return false;
        }
        float f = format.frameRate;
        if (f != -1.0f && f > ((float) i5)) {
            return false;
        }
        int i8 = format.bitrate;
        if (i8 == -1 || i8 <= i6) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0074  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00bc  */
    /* JADX WARNING: Missing block: B:44:0x0097, code skipped:
            if (r0 < 0) goto L_0x0099;
     */
    private static com.google.android.exoplayer2.trackselection.TrackSelection.Definition selectFixedVideoTrack(com.google.android.exoplayer2.source.TrackGroupArray r18, int[][] r19, com.google.android.exoplayer2.trackselection.DefaultTrackSelector.Parameters r20) {
        /*
        r0 = r18;
        r1 = r20;
        r3 = -1;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = -1;
        r10 = -1;
    L_0x000b:
        r11 = r0.length;
        if (r5 >= r11) goto L_0x00e0;
    L_0x000f:
        r11 = r0.get(r5);
        r13 = r1.viewportWidth;
        r14 = r1.viewportHeight;
        r15 = r1.viewportOrientationMayChange;
        r13 = getViewportFilteredTrackIndices(r11, r13, r14, r15);
        r14 = r19[r5];
        r15 = r10;
        r10 = r9;
        r9 = r8;
        r8 = r7;
        r7 = r6;
        r6 = 0;
    L_0x0025:
        r2 = r11.length;
        if (r6 >= r2) goto L_0x00d1;
    L_0x0029:
        r2 = r14[r6];
        r12 = r1.exceedRendererCapabilitiesIfNecessary;
        r2 = isSupported(r2, r12);
        if (r2 == 0) goto L_0x00c6;
    L_0x0033:
        r2 = r11.getFormat(r6);
        r12 = java.lang.Integer.valueOf(r6);
        r12 = r13.contains(r12);
        if (r12 == 0) goto L_0x006a;
    L_0x0041:
        r12 = r2.width;
        if (r12 == r3) goto L_0x0049;
    L_0x0045:
        r4 = r1.maxVideoWidth;
        if (r12 > r4) goto L_0x006a;
    L_0x0049:
        r4 = r2.height;
        if (r4 == r3) goto L_0x0051;
    L_0x004d:
        r12 = r1.maxVideoHeight;
        if (r4 > r12) goto L_0x006a;
    L_0x0051:
        r4 = r2.frameRate;
        r12 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r12 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
        if (r12 == 0) goto L_0x0060;
    L_0x0059:
        r12 = r1.maxVideoFrameRate;
        r12 = (float) r12;
        r4 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
        if (r4 > 0) goto L_0x006a;
    L_0x0060:
        r4 = r2.bitrate;
        if (r4 == r3) goto L_0x0068;
    L_0x0064:
        r12 = r1.maxVideoBitrate;
        if (r4 > r12) goto L_0x006a;
    L_0x0068:
        r4 = 1;
        goto L_0x006b;
    L_0x006a:
        r4 = 0;
    L_0x006b:
        if (r4 != 0) goto L_0x0072;
    L_0x006d:
        r12 = r1.exceedVideoConstraintsIfNecessary;
        if (r12 != 0) goto L_0x0072;
    L_0x0071:
        goto L_0x00c6;
    L_0x0072:
        if (r4 == 0) goto L_0x0076;
    L_0x0074:
        r12 = 2;
        goto L_0x0077;
    L_0x0076:
        r12 = 1;
    L_0x0077:
        r3 = r14[r6];
        r0 = 0;
        r3 = isSupported(r3, r0);
        if (r3 == 0) goto L_0x0082;
    L_0x0080:
        r12 = r12 + 1000;
    L_0x0082:
        if (r12 <= r9) goto L_0x0086;
    L_0x0084:
        r0 = 1;
        goto L_0x0087;
    L_0x0086:
        r0 = 0;
    L_0x0087:
        if (r12 != r9) goto L_0x00b8;
    L_0x0089:
        r0 = r2.bitrate;
        r0 = compareFormatValues(r0, r10);
        r17 = r7;
        r7 = r1.forceLowestBitrate;
        if (r7 == 0) goto L_0x009d;
    L_0x0095:
        if (r0 == 0) goto L_0x009d;
    L_0x0097:
        if (r0 >= 0) goto L_0x009b;
    L_0x0099:
        r0 = 1;
        goto L_0x00ba;
    L_0x009b:
        r0 = 0;
        goto L_0x00ba;
    L_0x009d:
        r0 = r2.getPixelCount();
        if (r0 == r15) goto L_0x00a8;
    L_0x00a3:
        r0 = compareFormatValues(r0, r15);
        goto L_0x00ae;
    L_0x00a8:
        r0 = r2.bitrate;
        r0 = compareFormatValues(r0, r10);
    L_0x00ae:
        if (r3 == 0) goto L_0x00b5;
    L_0x00b0:
        if (r4 == 0) goto L_0x00b5;
    L_0x00b2:
        if (r0 <= 0) goto L_0x009b;
    L_0x00b4:
        goto L_0x0099;
    L_0x00b5:
        if (r0 >= 0) goto L_0x009b;
    L_0x00b7:
        goto L_0x0099;
    L_0x00b8:
        r17 = r7;
    L_0x00ba:
        if (r0 == 0) goto L_0x00c8;
    L_0x00bc:
        r10 = r2.bitrate;
        r15 = r2.getPixelCount();
        r8 = r6;
        r7 = r11;
        r9 = r12;
        goto L_0x00ca;
    L_0x00c6:
        r17 = r7;
    L_0x00c8:
        r7 = r17;
    L_0x00ca:
        r6 = r6 + 1;
        r3 = -1;
        r0 = r18;
        goto L_0x0025;
    L_0x00d1:
        r17 = r7;
        r5 = r5 + 1;
        r3 = -1;
        r0 = r18;
        r7 = r8;
        r8 = r9;
        r9 = r10;
        r10 = r15;
        r6 = r17;
        goto L_0x000b;
    L_0x00e0:
        if (r6 != 0) goto L_0x00e5;
    L_0x00e2:
        r16 = 0;
        goto L_0x00f2;
    L_0x00e5:
        r2 = new com.google.android.exoplayer2.trackselection.TrackSelection$Definition;
        r0 = 1;
        r0 = new int[r0];
        r1 = 0;
        r0[r1] = r7;
        r2.<init>(r6, r0);
        r16 = r2;
    L_0x00f2:
        return r16;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.trackselection.DefaultTrackSelector.selectFixedVideoTrack(com.google.android.exoplayer2.source.TrackGroupArray, int[][], com.google.android.exoplayer2.trackselection.DefaultTrackSelector$Parameters):com.google.android.exoplayer2.trackselection.TrackSelection$Definition");
    }

    /* Access modifiers changed, original: protected */
    public Pair<Definition, AudioTrackScore> selectAudioTrack(TrackGroupArray trackGroupArray, int[][] iArr, int i, Parameters parameters, boolean z) throws ExoPlaybackException {
        TrackGroupArray trackGroupArray2 = trackGroupArray;
        Parameters parameters2 = parameters;
        Object obj = null;
        AudioTrackScore audioTrackScore = null;
        int i2 = 0;
        int i3 = -1;
        int i4 = -1;
        while (i2 < trackGroupArray2.length) {
            TrackGroup trackGroup = trackGroupArray2.get(i2);
            int[] iArr2 = iArr[i2];
            AudioTrackScore audioTrackScore2 = audioTrackScore;
            int i5 = i4;
            i4 = i3;
            for (i3 = 0; i3 < trackGroup.length; i3++) {
                if (isSupported(iArr2[i3], parameters2.exceedRendererCapabilitiesIfNecessary)) {
                    AudioTrackScore audioTrackScore3 = new AudioTrackScore(trackGroup.getFormat(i3), parameters2, iArr2[i3]);
                    if ((audioTrackScore3.isWithinConstraints || parameters2.exceedAudioConstraintsIfNecessary) && (audioTrackScore2 == null || audioTrackScore3.compareTo(audioTrackScore2) > 0)) {
                        i4 = i2;
                        i5 = i3;
                        audioTrackScore2 = audioTrackScore3;
                    }
                }
            }
            i2++;
            i3 = i4;
            i4 = i5;
            audioTrackScore = audioTrackScore2;
        }
        if (i3 == -1) {
            return null;
        }
        TrackGroup trackGroup2 = trackGroupArray2.get(i3);
        if (!(parameters2.forceHighestSupportedBitrate || parameters2.forceLowestBitrate || !z)) {
            int[] adaptiveAudioTracks = getAdaptiveAudioTracks(trackGroup2, iArr[i3], parameters2.allowAudioMixedMimeTypeAdaptiveness, parameters2.allowAudioMixedSampleRateAdaptiveness);
            if (adaptiveAudioTracks.length > 0) {
                obj = new Definition(trackGroup2, adaptiveAudioTracks);
            }
        }
        if (obj == null) {
            obj = new Definition(trackGroup2, i4);
        }
        Assertions.checkNotNull(audioTrackScore);
        return Pair.create(obj, audioTrackScore);
    }

    private static int[] getAdaptiveAudioTracks(TrackGroup trackGroup, int[] iArr, boolean z, boolean z2) {
        int i;
        int adaptiveAudioTrackCount;
        HashSet hashSet = new HashSet();
        Object obj = null;
        int i2 = 0;
        for (i = 0; i < trackGroup.length; i++) {
            Format format = trackGroup.getFormat(i);
            AudioConfigurationTuple audioConfigurationTuple = new AudioConfigurationTuple(format.channelCount, format.sampleRate, format.sampleMimeType);
            if (hashSet.add(audioConfigurationTuple)) {
                adaptiveAudioTrackCount = getAdaptiveAudioTrackCount(trackGroup, iArr, audioConfigurationTuple, z, z2);
                if (adaptiveAudioTrackCount > i2) {
                    i2 = adaptiveAudioTrackCount;
                    obj = audioConfigurationTuple;
                }
            }
        }
        if (i2 <= 1) {
            return NO_TRACKS;
        }
        int[] iArr2 = new int[i2];
        i = 0;
        for (int i3 = 0; i3 < trackGroup.length; i3++) {
            Format format2 = trackGroup.getFormat(i3);
            adaptiveAudioTrackCount = iArr[i3];
            Assertions.checkNotNull(obj);
            if (isSupportedAdaptiveAudioTrack(format2, adaptiveAudioTrackCount, (AudioConfigurationTuple) obj, z, z2)) {
                i2 = i + 1;
                iArr2[i] = i3;
                i = i2;
            }
        }
        return iArr2;
    }

    private static int getAdaptiveAudioTrackCount(TrackGroup trackGroup, int[] iArr, AudioConfigurationTuple audioConfigurationTuple, boolean z, boolean z2) {
        int i = 0;
        for (int i2 = 0; i2 < trackGroup.length; i2++) {
            if (isSupportedAdaptiveAudioTrack(trackGroup.getFormat(i2), iArr[i2], audioConfigurationTuple, z, z2)) {
                i++;
            }
        }
        return i;
    }

    private static boolean isSupportedAdaptiveAudioTrack(Format format, int i, AudioConfigurationTuple audioConfigurationTuple, boolean z, boolean z2) {
        if (!isSupported(i, false)) {
            return false;
        }
        i = format.channelCount;
        if (i == -1 || i != audioConfigurationTuple.channelCount) {
            return false;
        }
        if (!z) {
            String str = format.sampleMimeType;
            if (str == null || !TextUtils.equals(str, audioConfigurationTuple.mimeType)) {
                return false;
            }
        }
        if (!z2) {
            int i2 = format.sampleRate;
            if (i2 == -1 || i2 != audioConfigurationTuple.sampleRate) {
                return false;
            }
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public Pair<Definition, Integer> selectTextTrack(TrackGroupArray trackGroupArray, int[][] iArr, Parameters parameters) throws ExoPlaybackException {
        TrackGroupArray trackGroupArray2 = trackGroupArray;
        Parameters parameters2 = parameters;
        int i = 0;
        TrackGroup trackGroup = null;
        int i2 = 0;
        int i3 = 0;
        while (i < trackGroupArray2.length) {
            TrackGroup trackGroup2 = trackGroupArray2.get(i);
            int[] iArr2 = iArr[i];
            int i4 = i3;
            i3 = i2;
            TrackGroup trackGroup3 = trackGroup;
            for (int i5 = 0; i5 < trackGroup2.length; i5++) {
                if (isSupported(iArr2[i5], parameters2.exceedRendererCapabilitiesIfNecessary)) {
                    int i6;
                    Format format = trackGroup2.getFormat(i5);
                    int i7 = format.selectionFlags & (parameters2.disabledTextTrackSelectionFlags ^ -1);
                    Object obj = (i7 & 1) != 0 ? 1 : null;
                    Object obj2 = (i7 & 2) != 0 ? 1 : null;
                    boolean formatHasLanguage = formatHasLanguage(format, parameters2.preferredTextLanguage);
                    if (formatHasLanguage || (parameters2.selectUndeterminedTextLanguage && formatHasNoLanguage(format))) {
                        i6 = obj != null ? 8 : obj2 == null ? 6 : 4;
                        i6 += formatHasLanguage;
                    } else if (obj != null) {
                        i6 = 3;
                    } else if (obj2 != null) {
                        i6 = formatHasLanguage(format, parameters2.preferredAudioLanguage) ? 2 : 1;
                    }
                    if (isSupported(iArr2[i5], false)) {
                        i6 += 1000;
                    }
                    if (i6 > i4) {
                        i4 = i6;
                        i3 = i5;
                        trackGroup3 = trackGroup2;
                    }
                }
            }
            i++;
            trackGroup = trackGroup3;
            i2 = i3;
            i3 = i4;
        }
        if (trackGroup == null) {
            return null;
        }
        return Pair.create(new Definition(trackGroup, i2), Integer.valueOf(i3));
    }

    /* Access modifiers changed, original: protected */
    public Definition selectOtherTrack(int i, TrackGroupArray trackGroupArray, int[][] iArr, Parameters parameters) throws ExoPlaybackException {
        TrackGroup trackGroup = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < trackGroupArray.length) {
            TrackGroup trackGroup2 = trackGroupArray.get(i2);
            int[] iArr2 = iArr[i2];
            int i5 = i4;
            i4 = i3;
            TrackGroup trackGroup3 = trackGroup;
            for (int i6 = 0; i6 < trackGroup2.length; i6++) {
                if (isSupported(iArr2[i6], parameters.exceedRendererCapabilitiesIfNecessary)) {
                    int i7 = ((trackGroup2.getFormat(i6).selectionFlags & 1) != 0 ? 1 : null) != null ? 2 : 1;
                    if (isSupported(iArr2[i6], false)) {
                        i7 += 1000;
                    }
                    if (i7 > i5) {
                        i4 = i6;
                        trackGroup3 = trackGroup2;
                        i5 = i7;
                    }
                }
            }
            i2++;
            trackGroup = trackGroup3;
            i3 = i4;
            i4 = i5;
        }
        if (trackGroup == null) {
            return null;
        }
        return new Definition(trackGroup, i3);
    }

    private static void maybeConfigureRenderersForTunneling(MappedTrackInfo mappedTrackInfo, int[][][] iArr, RendererConfiguration[] rendererConfigurationArr, TrackSelection[] trackSelectionArr, int i) {
        if (i != 0) {
            int i2;
            int i3 = 0;
            int i4 = 0;
            int i5 = -1;
            int i6 = -1;
            while (i4 < mappedTrackInfo.getRendererCount()) {
                int rendererType = mappedTrackInfo.getRendererType(i4);
                TrackSelection trackSelection = trackSelectionArr[i4];
                if ((rendererType == 1 || rendererType == 2) && trackSelection != null && rendererSupportsTunneling(iArr[i4], mappedTrackInfo.getTrackGroups(i4), trackSelection)) {
                    if (rendererType == 1) {
                        if (i6 == -1) {
                            i6 = i4;
                        }
                    } else if (i5 == -1) {
                        i5 = i4;
                    }
                    i2 = 0;
                    break;
                }
                i4++;
            }
            i2 = 1;
            if (!(i6 == -1 || i5 == -1)) {
                i3 = 1;
            }
            if ((i2 & i3) != 0) {
                RendererConfiguration rendererConfiguration = new RendererConfiguration(i);
                rendererConfigurationArr[i6] = rendererConfiguration;
                rendererConfigurationArr[i5] = rendererConfiguration;
            }
        }
    }

    private static boolean rendererSupportsTunneling(int[][] iArr, TrackGroupArray trackGroupArray, TrackSelection trackSelection) {
        if (trackSelection == null) {
            return false;
        }
        int indexOf = trackGroupArray.indexOf(trackSelection.getTrackGroup());
        for (int i = 0; i < trackSelection.length(); i++) {
            if ((iArr[indexOf][trackSelection.getIndexInTrackGroup(i)] & 32) != 32) {
                return false;
            }
        }
        return true;
    }

    protected static boolean formatHasNoLanguage(Format format) {
        return TextUtils.isEmpty(format.language) || formatHasLanguage(format, "und");
    }

    protected static boolean formatHasLanguage(Format format, String str) {
        return str != null && TextUtils.equals(str, Util.normalizeLanguageCode(format.language));
    }

    private static List<Integer> getViewportFilteredTrackIndices(TrackGroup trackGroup, int i, int i2, boolean z) {
        ArrayList arrayList = new ArrayList(trackGroup.length);
        for (int i3 = 0; i3 < trackGroup.length; i3++) {
            arrayList.add(Integer.valueOf(i3));
        }
        if (!(i == Integer.MAX_VALUE || i2 == Integer.MAX_VALUE)) {
            int i4 = Integer.MAX_VALUE;
            for (int i5 = 0; i5 < trackGroup.length; i5++) {
                Format format = trackGroup.getFormat(i5);
                int i6 = format.width;
                if (i6 > 0) {
                    int i7 = format.height;
                    if (i7 > 0) {
                        Point maxVideoSizeInViewport = getMaxVideoSizeInViewport(z, i, i2, i6, i7);
                        i7 = format.width;
                        int i8 = format.height;
                        int i9 = i7 * i8;
                        if (i7 >= ((int) (((float) maxVideoSizeInViewport.x) * 0.98f)) && i8 >= ((int) (((float) maxVideoSizeInViewport.y) * 0.98f)) && i9 < i4) {
                            i4 = i9;
                        }
                    }
                }
            }
            if (i4 != Integer.MAX_VALUE) {
                for (i = arrayList.size() - 1; i >= 0; i--) {
                    i2 = trackGroup.getFormat(((Integer) arrayList.get(i)).intValue()).getPixelCount();
                    if (i2 == -1 || i2 > i4) {
                        arrayList.remove(i);
                    }
                }
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Missing block: B:7:0x000d, code skipped:
            if (r1 != r3) goto L_0x0013;
     */
    private static android.graphics.Point getMaxVideoSizeInViewport(boolean r3, int r4, int r5, int r6, int r7) {
        /*
        if (r3 == 0) goto L_0x0010;
    L_0x0002:
        r3 = 1;
        r0 = 0;
        if (r6 <= r7) goto L_0x0008;
    L_0x0006:
        r1 = 1;
        goto L_0x0009;
    L_0x0008:
        r1 = 0;
    L_0x0009:
        if (r4 <= r5) goto L_0x000c;
    L_0x000b:
        goto L_0x000d;
    L_0x000c:
        r3 = 0;
    L_0x000d:
        if (r1 == r3) goto L_0x0010;
    L_0x000f:
        goto L_0x0013;
    L_0x0010:
        r2 = r5;
        r5 = r4;
        r4 = r2;
    L_0x0013:
        r3 = r6 * r4;
        r0 = r7 * r5;
        if (r3 < r0) goto L_0x0023;
    L_0x0019:
        r3 = new android.graphics.Point;
        r4 = com.google.android.exoplayer2.util.Util.ceilDivide(r0, r6);
        r3.<init>(r5, r4);
        return r3;
    L_0x0023:
        r5 = new android.graphics.Point;
        r3 = com.google.android.exoplayer2.util.Util.ceilDivide(r3, r7);
        r5.<init>(r3, r4);
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.trackselection.DefaultTrackSelector.getMaxVideoSizeInViewport(boolean, int, int, int, int):android.graphics.Point");
    }
}
