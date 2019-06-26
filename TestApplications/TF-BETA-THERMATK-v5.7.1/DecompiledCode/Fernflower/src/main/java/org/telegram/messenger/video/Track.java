package org.telegram.messenger.video;

import android.media.MediaFormat;
import android.media.MediaCodec.BufferInfo;
import com.coremedia.iso.boxes.AbstractMediaHeaderBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.AudioSpecificConfig;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.SLConfigDescriptor;
import com.mp4parser.iso14496.part15.AvcConfigurationBox;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Track {
   private static Map samplingFrequencyIndexMap = new HashMap();
   private Date creationTime = new Date();
   private long duration = 0L;
   private boolean first = true;
   private String handler;
   private AbstractMediaHeaderBox headerBox;
   private int height;
   private boolean isAudio;
   private int[] sampleCompositions;
   private SampleDescriptionBox sampleDescriptionBox;
   private long[] sampleDurations;
   private ArrayList samplePresentationTimes = new ArrayList();
   private ArrayList samples = new ArrayList();
   private LinkedList syncSamples = null;
   private int timeScale;
   private long trackId;
   private float volume = 0.0F;
   private int width;

   static {
      samplingFrequencyIndexMap.put(96000, 0);
      samplingFrequencyIndexMap.put(88200, 1);
      samplingFrequencyIndexMap.put(64000, 2);
      samplingFrequencyIndexMap.put(48000, 3);
      samplingFrequencyIndexMap.put(44100, 4);
      samplingFrequencyIndexMap.put(32000, 5);
      samplingFrequencyIndexMap.put(24000, 6);
      samplingFrequencyIndexMap.put(22050, 7);
      samplingFrequencyIndexMap.put(16000, 8);
      samplingFrequencyIndexMap.put(12000, 9);
      samplingFrequencyIndexMap.put(11025, 10);
      samplingFrequencyIndexMap.put(8000, 11);
   }

   public Track(int var1, MediaFormat var2, boolean var3) {
      this.trackId = (long)var1;
      this.isAudio = var3;
      if (!this.isAudio) {
         this.width = var2.getInteger("width");
         this.height = var2.getInteger("height");
         this.timeScale = 90000;
         this.syncSamples = new LinkedList();
         this.handler = "vide";
         this.headerBox = new VideoMediaHeaderBox();
         this.sampleDescriptionBox = new SampleDescriptionBox();
         String var4 = var2.getString("mime");
         if (var4.equals("video/avc")) {
            VisualSampleEntry var5 = new VisualSampleEntry("avc1");
            var5.setDataReferenceIndex(1);
            var5.setDepth(24);
            var5.setFrameCount(1);
            var5.setHorizresolution(72.0D);
            var5.setVertresolution(72.0D);
            var5.setWidth(this.width);
            var5.setHeight(this.height);
            AvcConfigurationBox var13 = new AvcConfigurationBox();
            if (var2.getByteBuffer("csd-0") != null) {
               ArrayList var6 = new ArrayList();
               ByteBuffer var7 = var2.getByteBuffer("csd-0");
               var7.position(4);
               byte[] var8 = new byte[var7.remaining()];
               var7.get(var8);
               var6.add(var8);
               ArrayList var17 = new ArrayList();
               ByteBuffer var18 = var2.getByteBuffer("csd-1");
               var18.position(4);
               byte[] var9 = new byte[var18.remaining()];
               var18.get(var9);
               var17.add(var9);
               var13.setSequenceParameterSets(var6);
               var13.setPictureParameterSets(var17);
            }

            if (var2.containsKey("level")) {
               var1 = var2.getInteger("level");
               if (var1 == 1) {
                  var13.setAvcLevelIndication(1);
               } else if (var1 == 32) {
                  var13.setAvcLevelIndication(2);
               } else if (var1 == 4) {
                  var13.setAvcLevelIndication(11);
               } else if (var1 == 8) {
                  var13.setAvcLevelIndication(12);
               } else if (var1 == 16) {
                  var13.setAvcLevelIndication(13);
               } else if (var1 == 64) {
                  var13.setAvcLevelIndication(21);
               } else if (var1 == 128) {
                  var13.setAvcLevelIndication(22);
               } else if (var1 == 256) {
                  var13.setAvcLevelIndication(3);
               } else if (var1 == 512) {
                  var13.setAvcLevelIndication(31);
               } else if (var1 == 1024) {
                  var13.setAvcLevelIndication(32);
               } else if (var1 == 2048) {
                  var13.setAvcLevelIndication(4);
               } else if (var1 == 4096) {
                  var13.setAvcLevelIndication(41);
               } else if (var1 == 8192) {
                  var13.setAvcLevelIndication(42);
               } else if (var1 == 16384) {
                  var13.setAvcLevelIndication(5);
               } else if (var1 == 32768) {
                  var13.setAvcLevelIndication(51);
               } else if (var1 == 65536) {
                  var13.setAvcLevelIndication(52);
               } else if (var1 == 2) {
                  var13.setAvcLevelIndication(27);
               }
            } else {
               var13.setAvcLevelIndication(13);
            }

            if (var2.containsKey("profile")) {
               var1 = var2.getInteger("profile");
               if (var1 == 1) {
                  var13.setAvcProfileIndication(66);
               } else if (var1 == 2) {
                  var13.setAvcProfileIndication(77);
               } else if (var1 == 4) {
                  var13.setAvcProfileIndication(88);
               } else if (var1 == 8) {
                  var13.setAvcProfileIndication(100);
               } else if (var1 == 16) {
                  var13.setAvcProfileIndication(110);
               } else if (var1 == 32) {
                  var13.setAvcProfileIndication(122);
               } else if (var1 == 64) {
                  var13.setAvcProfileIndication(244);
               }
            } else {
               var13.setAvcProfileIndication(100);
            }

            var13.setBitDepthLumaMinus8(-1);
            var13.setBitDepthChromaMinus8(-1);
            var13.setChromaFormat(-1);
            var13.setConfigurationVersion(1);
            var13.setLengthSizeMinusOne(3);
            var13.setProfileCompatibility(0);
            var5.addBox(var13);
            this.sampleDescriptionBox.addBox(var5);
         } else if (var4.equals("video/mp4v")) {
            VisualSampleEntry var10 = new VisualSampleEntry("mp4v");
            var10.setDataReferenceIndex(1);
            var10.setDepth(24);
            var10.setFrameCount(1);
            var10.setHorizresolution(72.0D);
            var10.setVertresolution(72.0D);
            var10.setWidth(this.width);
            var10.setHeight(this.height);
            this.sampleDescriptionBox.addBox(var10);
         }
      } else {
         this.volume = 1.0F;
         this.timeScale = var2.getInteger("sample-rate");
         this.handler = "soun";
         this.headerBox = new SoundMediaHeaderBox();
         this.sampleDescriptionBox = new SampleDescriptionBox();
         AudioSampleEntry var14 = new AudioSampleEntry("mp4a");
         var14.setChannelCount(var2.getInteger("channel-count"));
         var14.setSampleRate((long)var2.getInteger("sample-rate"));
         var14.setDataReferenceIndex(1);
         var14.setSampleSize(16);
         ESDescriptorBox var16 = new ESDescriptorBox();
         ESDescriptor var15 = new ESDescriptor();
         var15.setEsId(0);
         SLConfigDescriptor var19 = new SLConfigDescriptor();
         var19.setPredefined(2);
         var15.setSlConfigDescriptor(var19);
         DecoderConfigDescriptor var20 = new DecoderConfigDescriptor();
         var20.setObjectTypeIndication(64);
         var20.setStreamType(5);
         var20.setBufferSizeDB(1536);
         if (var2.containsKey("max-bitrate")) {
            var20.setMaxBitRate((long)var2.getInteger("max-bitrate"));
         } else {
            var20.setMaxBitRate(96000L);
         }

         var20.setAvgBitRate((long)this.timeScale);
         AudioSpecificConfig var11 = new AudioSpecificConfig();
         var11.setAudioObjectType(2);
         var11.setSamplingFrequencyIndex((Integer)samplingFrequencyIndexMap.get((int)var14.getSampleRate()));
         var11.setChannelConfiguration(var14.getChannelCount());
         var20.setAudioSpecificInfo(var11);
         var15.setDecoderConfigDescriptor(var20);
         ByteBuffer var12 = var15.serialize();
         var16.setEsDescriptor(var15);
         var16.setData(var12);
         var14.addBox(var16);
         this.sampleDescriptionBox.addBox(var14);
      }

   }

   // $FF: synthetic method
   static int lambda$prepare$0(Track.SamplePresentationTime var0, Track.SamplePresentationTime var1) {
      if (var0.presentationTime > var1.presentationTime) {
         return 1;
      } else {
         return var0.presentationTime < var1.presentationTime ? -1 : 0;
      }
   }

   public void addSample(long var1, BufferInfo var3) {
      boolean var4 = this.isAudio;
      boolean var5 = true;
      if (var4 || (var3.flags & 1) == 0) {
         var5 = false;
      }

      this.samples.add(new Sample(var1, (long)var3.size));
      LinkedList var6 = this.syncSamples;
      if (var6 != null && var5) {
         var6.add(this.samples.size());
      }

      ArrayList var7 = this.samplePresentationTimes;
      var7.add(new Track.SamplePresentationTime(var7.size(), (var3.presentationTimeUs * (long)this.timeScale + 500000L) / 1000000L));
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

   public ArrayList getSamples() {
      return this.samples;
   }

   public long[] getSyncSamples() {
      LinkedList var1 = this.syncSamples;
      if (var1 != null && !var1.isEmpty()) {
         long[] var3 = new long[this.syncSamples.size()];

         for(int var2 = 0; var2 < this.syncSamples.size(); ++var2) {
            var3[var2] = (long)(Integer)this.syncSamples.get(var2);
         }

         return var3;
      } else {
         return null;
      }
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
      ArrayList var1 = new ArrayList(this.samplePresentationTimes);
      Collections.sort(this.samplePresentationTimes, _$$Lambda$Track$WwpAJwhUb2DZllFb8kOYdyyS8pU.INSTANCE);
      this.sampleDurations = new long[this.samplePresentationTimes.size()];
      long var2 = Long.MAX_VALUE;
      int var4 = 0;
      boolean var5 = false;
      long var6 = 0L;

      while(true) {
         int var8 = this.samplePresentationTimes.size();
         byte var9 = 1;
         if (var4 >= var8) {
            long[] var16 = this.sampleDurations;
            if (var16.length > 0) {
               var16[0] = var2;
               this.duration += var2;
               var4 = var9;
            } else {
               var4 = var9;
            }

            while(true) {
               var9 = 0;
               if (var4 >= var1.size()) {
                  if (var5) {
                     this.sampleCompositions = new int[this.samplePresentationTimes.size()];

                     for(var4 = var9; var4 < this.samplePresentationTimes.size(); ++var4) {
                        Track.SamplePresentationTime var15 = (Track.SamplePresentationTime)this.samplePresentationTimes.get(var4);
                        this.sampleCompositions[var15.index] = (int)(var15.presentationTime - var15.dt);
                     }
                  }

                  return;
               }

               ((Track.SamplePresentationTime)var1.get(var4)).dt = this.sampleDurations[var4] + ((Track.SamplePresentationTime)var1.get(var4 - 1)).dt;
               ++var4;
            }
         }

         Track.SamplePresentationTime var10 = (Track.SamplePresentationTime)this.samplePresentationTimes.get(var4);
         long var11 = var10.presentationTime - var6;
         var6 = var10.presentationTime;
         this.sampleDurations[var10.index] = var11;
         if (var10.index != 0) {
            this.duration += var11;
         }

         long var13 = var2;
         if (var11 != 0L) {
            var13 = Math.min(var2, var11);
         }

         if (var10.index != var4) {
            var5 = true;
         }

         ++var4;
         var2 = var13;
      }
   }

   private class SamplePresentationTime {
      private long dt;
      private int index;
      private long presentationTime;

      public SamplePresentationTime(int var2, long var3) {
         this.index = var2;
         this.presentationTime = var3;
      }
   }
}
