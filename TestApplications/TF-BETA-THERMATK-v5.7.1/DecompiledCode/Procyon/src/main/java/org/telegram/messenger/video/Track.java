// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.video;

import java.util.Comparator;
import java.util.Collections;
import java.util.Collection;
import android.media.MediaCodec$BufferInfo;
import java.nio.ByteBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.Box;
import java.util.List;
import com.mp4parser.iso14496.part15.AvcConfigurationBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import android.media.MediaFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.AbstractMediaHeaderBox;
import java.util.Date;
import java.util.Map;

public class Track
{
    private static Map<Integer, Integer> samplingFrequencyIndexMap;
    private Date creationTime;
    private long duration;
    private boolean first;
    private String handler;
    private AbstractMediaHeaderBox headerBox;
    private int height;
    private boolean isAudio;
    private int[] sampleCompositions;
    private SampleDescriptionBox sampleDescriptionBox;
    private long[] sampleDurations;
    private ArrayList<SamplePresentationTime> samplePresentationTimes;
    private ArrayList<Sample> samples;
    private LinkedList<Integer> syncSamples;
    private int timeScale;
    private long trackId;
    private float volume;
    private int width;
    
    static {
        (Track.samplingFrequencyIndexMap = new HashMap<Integer, Integer>()).put(96000, 0);
        Track.samplingFrequencyIndexMap.put(88200, 1);
        Track.samplingFrequencyIndexMap.put(64000, 2);
        Track.samplingFrequencyIndexMap.put(48000, 3);
        Track.samplingFrequencyIndexMap.put(44100, 4);
        Track.samplingFrequencyIndexMap.put(32000, 5);
        Track.samplingFrequencyIndexMap.put(24000, 6);
        Track.samplingFrequencyIndexMap.put(22050, 7);
        Track.samplingFrequencyIndexMap.put(16000, 8);
        Track.samplingFrequencyIndexMap.put(12000, 9);
        Track.samplingFrequencyIndexMap.put(11025, 10);
        Track.samplingFrequencyIndexMap.put(8000, 11);
    }
    
    public Track(int n, final MediaFormat mediaFormat, final boolean isAudio) {
        this.samples = new ArrayList<Sample>();
        this.duration = 0L;
        this.syncSamples = null;
        this.creationTime = new Date();
        this.volume = 0.0f;
        this.samplePresentationTimes = new ArrayList<SamplePresentationTime>();
        this.first = true;
        this.trackId = n;
        if (!(this.isAudio = isAudio)) {
            this.width = mediaFormat.getInteger("width");
            this.height = mediaFormat.getInteger("height");
            this.timeScale = 90000;
            this.syncSamples = new LinkedList<Integer>();
            this.handler = "vide";
            this.headerBox = new VideoMediaHeaderBox();
            this.sampleDescriptionBox = new SampleDescriptionBox();
            final String string = mediaFormat.getString("mime");
            if (string.equals("video/avc")) {
                final VisualSampleEntry visualSampleEntry = new VisualSampleEntry("avc1");
                visualSampleEntry.setDataReferenceIndex(1);
                visualSampleEntry.setDepth(24);
                visualSampleEntry.setFrameCount(1);
                visualSampleEntry.setHorizresolution(72.0);
                visualSampleEntry.setVertresolution(72.0);
                visualSampleEntry.setWidth(this.width);
                visualSampleEntry.setHeight(this.height);
                final AvcConfigurationBox avcConfigurationBox = new AvcConfigurationBox();
                if (mediaFormat.getByteBuffer("csd-0") != null) {
                    final ArrayList<byte[]> sequenceParameterSets = new ArrayList<byte[]>();
                    final ByteBuffer byteBuffer = mediaFormat.getByteBuffer("csd-0");
                    byteBuffer.position(4);
                    final byte[] array = new byte[byteBuffer.remaining()];
                    byteBuffer.get(array);
                    sequenceParameterSets.add(array);
                    final ArrayList<byte[]> pictureParameterSets = new ArrayList<byte[]>();
                    final ByteBuffer byteBuffer2 = mediaFormat.getByteBuffer("csd-1");
                    byteBuffer2.position(4);
                    final byte[] array2 = new byte[byteBuffer2.remaining()];
                    byteBuffer2.get(array2);
                    pictureParameterSets.add(array2);
                    avcConfigurationBox.setSequenceParameterSets(sequenceParameterSets);
                    avcConfigurationBox.setPictureParameterSets(pictureParameterSets);
                }
                if (mediaFormat.containsKey("level")) {
                    n = mediaFormat.getInteger("level");
                    if (n == 1) {
                        avcConfigurationBox.setAvcLevelIndication(1);
                    }
                    else if (n == 32) {
                        avcConfigurationBox.setAvcLevelIndication(2);
                    }
                    else if (n == 4) {
                        avcConfigurationBox.setAvcLevelIndication(11);
                    }
                    else if (n == 8) {
                        avcConfigurationBox.setAvcLevelIndication(12);
                    }
                    else if (n == 16) {
                        avcConfigurationBox.setAvcLevelIndication(13);
                    }
                    else if (n == 64) {
                        avcConfigurationBox.setAvcLevelIndication(21);
                    }
                    else if (n == 128) {
                        avcConfigurationBox.setAvcLevelIndication(22);
                    }
                    else if (n == 256) {
                        avcConfigurationBox.setAvcLevelIndication(3);
                    }
                    else if (n == 512) {
                        avcConfigurationBox.setAvcLevelIndication(31);
                    }
                    else if (n == 1024) {
                        avcConfigurationBox.setAvcLevelIndication(32);
                    }
                    else if (n == 2048) {
                        avcConfigurationBox.setAvcLevelIndication(4);
                    }
                    else if (n == 4096) {
                        avcConfigurationBox.setAvcLevelIndication(41);
                    }
                    else if (n == 8192) {
                        avcConfigurationBox.setAvcLevelIndication(42);
                    }
                    else if (n == 16384) {
                        avcConfigurationBox.setAvcLevelIndication(5);
                    }
                    else if (n == 32768) {
                        avcConfigurationBox.setAvcLevelIndication(51);
                    }
                    else if (n == 65536) {
                        avcConfigurationBox.setAvcLevelIndication(52);
                    }
                    else if (n == 2) {
                        avcConfigurationBox.setAvcLevelIndication(27);
                    }
                }
                else {
                    avcConfigurationBox.setAvcLevelIndication(13);
                }
                if (mediaFormat.containsKey("profile")) {
                    n = mediaFormat.getInteger("profile");
                    if (n == 1) {
                        avcConfigurationBox.setAvcProfileIndication(66);
                    }
                    else if (n == 2) {
                        avcConfigurationBox.setAvcProfileIndication(77);
                    }
                    else if (n == 4) {
                        avcConfigurationBox.setAvcProfileIndication(88);
                    }
                    else if (n == 8) {
                        avcConfigurationBox.setAvcProfileIndication(100);
                    }
                    else if (n == 16) {
                        avcConfigurationBox.setAvcProfileIndication(110);
                    }
                    else if (n == 32) {
                        avcConfigurationBox.setAvcProfileIndication(122);
                    }
                    else if (n == 64) {
                        avcConfigurationBox.setAvcProfileIndication(244);
                    }
                }
                else {
                    avcConfigurationBox.setAvcProfileIndication(100);
                }
                avcConfigurationBox.setBitDepthLumaMinus8(-1);
                avcConfigurationBox.setBitDepthChromaMinus8(-1);
                avcConfigurationBox.setChromaFormat(-1);
                avcConfigurationBox.setConfigurationVersion(1);
                avcConfigurationBox.setLengthSizeMinusOne(3);
                avcConfigurationBox.setProfileCompatibility(0);
                visualSampleEntry.addBox(avcConfigurationBox);
                this.sampleDescriptionBox.addBox(visualSampleEntry);
            }
            else if (string.equals("video/mp4v")) {
                final VisualSampleEntry visualSampleEntry2 = new VisualSampleEntry("mp4v");
                visualSampleEntry2.setDataReferenceIndex(1);
                visualSampleEntry2.setDepth(24);
                visualSampleEntry2.setFrameCount(1);
                visualSampleEntry2.setHorizresolution(72.0);
                visualSampleEntry2.setVertresolution(72.0);
                visualSampleEntry2.setWidth(this.width);
                visualSampleEntry2.setHeight(this.height);
                this.sampleDescriptionBox.addBox(visualSampleEntry2);
            }
        }
        else {
            this.volume = 1.0f;
            this.timeScale = mediaFormat.getInteger("sample-rate");
            this.handler = "soun";
            this.headerBox = new SoundMediaHeaderBox();
            this.sampleDescriptionBox = new SampleDescriptionBox();
            final AudioSampleEntry audioSampleEntry = new AudioSampleEntry("mp4a");
            audioSampleEntry.setChannelCount(mediaFormat.getInteger("channel-count"));
            audioSampleEntry.setSampleRate(mediaFormat.getInteger("sample-rate"));
            audioSampleEntry.setDataReferenceIndex(1);
            audioSampleEntry.setSampleSize(16);
            final ESDescriptorBox esDescriptorBox = new ESDescriptorBox();
            final ESDescriptor esDescriptor = new ESDescriptor();
            esDescriptor.setEsId(0);
            final SLConfigDescriptor slConfigDescriptor = new SLConfigDescriptor();
            slConfigDescriptor.setPredefined(2);
            esDescriptor.setSlConfigDescriptor(slConfigDescriptor);
            final DecoderConfigDescriptor decoderConfigDescriptor = new DecoderConfigDescriptor();
            decoderConfigDescriptor.setObjectTypeIndication(64);
            decoderConfigDescriptor.setStreamType(5);
            decoderConfigDescriptor.setBufferSizeDB(1536);
            if (mediaFormat.containsKey("max-bitrate")) {
                decoderConfigDescriptor.setMaxBitRate(mediaFormat.getInteger("max-bitrate"));
            }
            else {
                decoderConfigDescriptor.setMaxBitRate(96000L);
            }
            decoderConfigDescriptor.setAvgBitRate(this.timeScale);
            final AudioSpecificConfig audioSpecificInfo = new AudioSpecificConfig();
            audioSpecificInfo.setAudioObjectType(2);
            audioSpecificInfo.setSamplingFrequencyIndex(Track.samplingFrequencyIndexMap.get((int)audioSampleEntry.getSampleRate()));
            audioSpecificInfo.setChannelConfiguration(audioSampleEntry.getChannelCount());
            decoderConfigDescriptor.setAudioSpecificInfo(audioSpecificInfo);
            esDescriptor.setDecoderConfigDescriptor(decoderConfigDescriptor);
            final ByteBuffer serialize = esDescriptor.serialize();
            esDescriptorBox.setEsDescriptor(esDescriptor);
            esDescriptorBox.setData(serialize);
            audioSampleEntry.addBox(esDescriptorBox);
            this.sampleDescriptionBox.addBox(audioSampleEntry);
        }
    }
    
    public void addSample(final long n, final MediaCodec$BufferInfo mediaCodec$BufferInfo) {
        final boolean isAudio = this.isAudio;
        boolean b = true;
        if (isAudio || (mediaCodec$BufferInfo.flags & 0x1) == 0x0) {
            b = false;
        }
        this.samples.add(new Sample(n, mediaCodec$BufferInfo.size));
        final LinkedList<Integer> syncSamples = this.syncSamples;
        if (syncSamples != null && b) {
            syncSamples.add(this.samples.size());
        }
        final ArrayList<SamplePresentationTime> samplePresentationTimes = this.samplePresentationTimes;
        samplePresentationTimes.add(new SamplePresentationTime(samplePresentationTimes.size(), (mediaCodec$BufferInfo.presentationTimeUs * this.timeScale + 500000L) / 1000000L));
    }
    
    public Date getCreationTime() {
        return this.creationTime;
    }
    
    public long getDuration() {
        return this.duration;
    }
    
    public String getHandler() {
        return this.handler;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public AbstractMediaHeaderBox getMediaHeaderBox() {
        return this.headerBox;
    }
    
    public int[] getSampleCompositions() {
        return this.sampleCompositions;
    }
    
    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }
    
    public long[] getSampleDurations() {
        return this.sampleDurations;
    }
    
    public ArrayList<Sample> getSamples() {
        return this.samples;
    }
    
    public long[] getSyncSamples() {
        final LinkedList<Integer> syncSamples = this.syncSamples;
        if (syncSamples != null && !syncSamples.isEmpty()) {
            final long[] array = new long[this.syncSamples.size()];
            for (int i = 0; i < this.syncSamples.size(); ++i) {
                array[i] = this.syncSamples.get(i);
            }
            return array;
        }
        return null;
    }
    
    public int getTimeScale() {
        return this.timeScale;
    }
    
    public long getTrackId() {
        return this.trackId;
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public boolean isAudio() {
        return this.isAudio;
    }
    
    public void prepare() {
        final ArrayList<SamplePresentationTime> list = new ArrayList<SamplePresentationTime>(this.samplePresentationTimes);
        Collections.sort(this.samplePresentationTimes, (Comparator<? super SamplePresentationTime>)_$$Lambda$Track$WwpAJwhUb2DZllFb8kOYdyyS8pU.INSTANCE);
        this.sampleDurations = new long[this.samplePresentationTimes.size()];
        long a = Long.MAX_VALUE;
        int index = 0;
        boolean b = false;
        long access$000 = 0L;
        int n;
        while (true) {
            final int size = this.samplePresentationTimes.size();
            n = 1;
            if (index >= size) {
                break;
            }
            final SamplePresentationTime samplePresentationTime = this.samplePresentationTimes.get(index);
            final long b2 = samplePresentationTime.presentationTime - access$000;
            access$000 = samplePresentationTime.presentationTime;
            this.sampleDurations[samplePresentationTime.index] = b2;
            if (samplePresentationTime.index != 0) {
                this.duration += b2;
            }
            long min = a;
            if (b2 != 0L) {
                min = Math.min(a, b2);
            }
            if (samplePresentationTime.index != index) {
                b = true;
            }
            ++index;
            a = min;
        }
        final long[] sampleDurations = this.sampleDurations;
        int index2;
        if (sampleDurations.length > 0) {
            sampleDurations[0] = a;
            this.duration += a;
            index2 = n;
        }
        else {
            index2 = n;
        }
        int n2;
        while (true) {
            n2 = 0;
            if (index2 >= list.size()) {
                break;
            }
            list.get(index2).dt = this.sampleDurations[index2] + list.get(index2 - 1).dt;
            ++index2;
        }
        if (b) {
            this.sampleCompositions = new int[this.samplePresentationTimes.size()];
            for (int i = n2; i < this.samplePresentationTimes.size(); ++i) {
                final SamplePresentationTime samplePresentationTime2 = this.samplePresentationTimes.get(i);
                this.sampleCompositions[samplePresentationTime2.index] = (int)(samplePresentationTime2.presentationTime - samplePresentationTime2.dt);
            }
        }
    }
    
    private class SamplePresentationTime
    {
        private long dt;
        private int index;
        private long presentationTime;
        
        public SamplePresentationTime(final int index, final long presentationTime) {
            this.index = index;
            this.presentationTime = presentationTime;
        }
    }
}
