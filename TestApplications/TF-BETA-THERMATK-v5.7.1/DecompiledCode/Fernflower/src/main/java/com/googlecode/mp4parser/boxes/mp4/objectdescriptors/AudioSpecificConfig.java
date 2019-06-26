package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Descriptor(
   objectTypeIndication = 64,
   tags = {5}
)
public class AudioSpecificConfig extends BaseDescriptor {
   public static Map audioObjectTypeMap = new HashMap();
   public static Map samplingFrequencyIndexMap = new HashMap();
   public boolean aacScalefactorDataResilienceFlag;
   public boolean aacSectionDataResilienceFlag;
   public boolean aacSpectralDataResilienceFlag;
   public int audioObjectType;
   public int channelConfiguration;
   byte[] configBytes;
   public int coreCoderDelay;
   public int dependsOnCoreCoder;
   public int directMapping;
   public AudioSpecificConfig.ELDSpecificConfig eldSpecificConfig;
   public int epConfig;
   public int erHvxcExtensionFlag;
   public int extensionAudioObjectType;
   public int extensionChannelConfiguration;
   public int extensionFlag;
   public int extensionFlag3;
   public int extensionSamplingFrequency;
   public int extensionSamplingFrequencyIndex;
   public int fillBits;
   public int frameLengthFlag;
   public boolean gaSpecificConfig;
   public int hilnContMode;
   public int hilnEnhaLayer;
   public int hilnEnhaQuantMode;
   public int hilnFrameLength;
   public int hilnMaxNumLine;
   public int hilnQuantMode;
   public int hilnSampleRateCode;
   public int hvxcRateMode;
   public int hvxcVarMode;
   public int isBaseLayer;
   public int layerNr;
   public int layer_length;
   public int numOfSubFrame;
   public int paraExtensionFlag;
   public int paraMode;
   public boolean parametricSpecificConfig;
   public boolean psPresentFlag;
   public int sacPayloadEmbedding;
   public int samplingFrequency;
   public int samplingFrequencyIndex;
   public boolean sbrPresentFlag;
   public int syncExtensionType;
   public int var_ScalableFlag;

   static {
      samplingFrequencyIndexMap.put(0, 96000);
      Map var0 = samplingFrequencyIndexMap;
      Integer var1 = 1;
      var0.put(var1, 88200);
      Map var2 = samplingFrequencyIndexMap;
      Integer var8 = 2;
      var2.put(var8, 64000);
      Map var3 = samplingFrequencyIndexMap;
      Integer var9 = 3;
      var3.put(var9, 48000);
      Map var4 = samplingFrequencyIndexMap;
      Integer var10 = 4;
      var4.put(var10, 44100);
      Map var5 = samplingFrequencyIndexMap;
      Integer var11 = 5;
      var5.put(var11, 32000);
      Map var6 = samplingFrequencyIndexMap;
      Integer var12 = 6;
      var6.put(var12, 24000);
      Map var7 = samplingFrequencyIndexMap;
      Integer var13 = 7;
      var7.put(var13, 22050);
      samplingFrequencyIndexMap.put(8, 16000);
      samplingFrequencyIndexMap.put(9, 12000);
      samplingFrequencyIndexMap.put(10, 11025);
      samplingFrequencyIndexMap.put(11, 8000);
      audioObjectTypeMap.put(var1, "AAC main");
      audioObjectTypeMap.put(var8, "AAC LC");
      audioObjectTypeMap.put(var9, "AAC SSR");
      audioObjectTypeMap.put(var10, "AAC LTP");
      audioObjectTypeMap.put(var11, "SBR");
      audioObjectTypeMap.put(var12, "AAC Scalable");
      audioObjectTypeMap.put(var13, "TwinVQ");
      audioObjectTypeMap.put(8, "CELP");
      audioObjectTypeMap.put(9, "HVXC");
      audioObjectTypeMap.put(10, "(reserved)");
      audioObjectTypeMap.put(11, "(reserved)");
      audioObjectTypeMap.put(12, "TTSI");
      audioObjectTypeMap.put(13, "Main synthetic");
      audioObjectTypeMap.put(14, "Wavetable synthesis");
      audioObjectTypeMap.put(15, "General MIDI");
      audioObjectTypeMap.put(16, "Algorithmic Synthesis and Audio FX");
      audioObjectTypeMap.put(17, "ER AAC LC");
      audioObjectTypeMap.put(18, "(reserved)");
      audioObjectTypeMap.put(19, "ER AAC LTP");
      audioObjectTypeMap.put(20, "ER AAC Scalable");
      audioObjectTypeMap.put(21, "ER TwinVQ");
      audioObjectTypeMap.put(22, "ER BSAC");
      audioObjectTypeMap.put(23, "ER AAC LD");
      audioObjectTypeMap.put(24, "ER CELP");
      audioObjectTypeMap.put(25, "ER HVXC");
      audioObjectTypeMap.put(26, "ER HILN");
      audioObjectTypeMap.put(27, "ER Parametric");
      audioObjectTypeMap.put(28, "SSC");
      audioObjectTypeMap.put(29, "PS");
      audioObjectTypeMap.put(30, "MPEG Surround");
      audioObjectTypeMap.put(31, "(escape)");
      audioObjectTypeMap.put(32, "Layer-1");
      audioObjectTypeMap.put(33, "Layer-2");
      audioObjectTypeMap.put(34, "Layer-3");
      audioObjectTypeMap.put(35, "DST");
      audioObjectTypeMap.put(36, "ALS");
      audioObjectTypeMap.put(37, "SLS");
      audioObjectTypeMap.put(38, "SLS non-core");
      audioObjectTypeMap.put(39, "ER AAC ELD");
      audioObjectTypeMap.put(40, "SMR Simple");
      audioObjectTypeMap.put(41, "SMR Main");
   }

   private int gaSpecificConfigSize() {
      return 0;
   }

   private int getAudioObjectType(BitReaderBuffer var1) throws IOException {
      int var2 = var1.readBits(5);
      int var3 = var2;
      if (var2 == 31) {
         var3 = var1.readBits(6) + 32;
      }

      return var3;
   }

   private void parseErHvxcConfig(int var1, int var2, int var3, BitReaderBuffer var4) throws IOException {
      this.hvxcVarMode = var4.readBits(1);
      this.hvxcRateMode = var4.readBits(2);
      this.erHvxcExtensionFlag = var4.readBits(1);
      if (this.erHvxcExtensionFlag == 1) {
         this.var_ScalableFlag = var4.readBits(1);
      }

   }

   private void parseGaSpecificConfig(int var1, int var2, int var3, BitReaderBuffer var4) throws IOException {
      this.frameLengthFlag = var4.readBits(1);
      this.dependsOnCoreCoder = var4.readBits(1);
      if (this.dependsOnCoreCoder == 1) {
         this.coreCoderDelay = var4.readBits(14);
      }

      this.extensionFlag = var4.readBits(1);
      if (var2 == 0) {
         throw new UnsupportedOperationException("can't parse program_config_element yet");
      } else {
         if (var3 == 6 || var3 == 20) {
            this.layerNr = var4.readBits(3);
         }

         if (this.extensionFlag == 1) {
            if (var3 == 22) {
               this.numOfSubFrame = var4.readBits(5);
               this.layer_length = var4.readBits(11);
            }

            if (var3 == 17 || var3 == 19 || var3 == 20 || var3 == 23) {
               this.aacSectionDataResilienceFlag = var4.readBool();
               this.aacScalefactorDataResilienceFlag = var4.readBool();
               this.aacSpectralDataResilienceFlag = var4.readBool();
            }

            this.extensionFlag3 = var4.readBits(1);
         }

         this.gaSpecificConfig = true;
      }
   }

   private void parseHilnConfig(int var1, int var2, int var3, BitReaderBuffer var4) throws IOException {
      this.hilnQuantMode = var4.readBits(1);
      this.hilnMaxNumLine = var4.readBits(8);
      this.hilnSampleRateCode = var4.readBits(4);
      this.hilnFrameLength = var4.readBits(12);
      this.hilnContMode = var4.readBits(2);
   }

   private void parseHilnEnexConfig(int var1, int var2, int var3, BitReaderBuffer var4) throws IOException {
      this.hilnEnhaLayer = var4.readBits(1);
      if (this.hilnEnhaLayer == 1) {
         this.hilnEnhaQuantMode = var4.readBits(2);
      }

   }

   private void parseParaConfig(int var1, int var2, int var3, BitReaderBuffer var4) throws IOException {
      this.paraMode = var4.readBits(2);
      if (this.paraMode != 1) {
         this.parseErHvxcConfig(var1, var2, var3, var4);
      }

      if (this.paraMode != 0) {
         this.parseHilnConfig(var1, var2, var3, var4);
      }

      this.paraExtensionFlag = var4.readBits(1);
      this.parametricSpecificConfig = true;
   }

   private void parseParametricSpecificConfig(int var1, int var2, int var3, BitReaderBuffer var4) throws IOException {
      this.isBaseLayer = var4.readBits(1);
      if (this.isBaseLayer == 1) {
         this.parseParaConfig(var1, var2, var3, var4);
      } else {
         this.parseHilnEnexConfig(var1, var2, var3, var4);
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && AudioSpecificConfig.class == var1.getClass()) {
         AudioSpecificConfig var2 = (AudioSpecificConfig)var1;
         if (this.aacScalefactorDataResilienceFlag != var2.aacScalefactorDataResilienceFlag) {
            return false;
         } else if (this.aacSectionDataResilienceFlag != var2.aacSectionDataResilienceFlag) {
            return false;
         } else if (this.aacSpectralDataResilienceFlag != var2.aacSpectralDataResilienceFlag) {
            return false;
         } else if (this.audioObjectType != var2.audioObjectType) {
            return false;
         } else if (this.channelConfiguration != var2.channelConfiguration) {
            return false;
         } else if (this.coreCoderDelay != var2.coreCoderDelay) {
            return false;
         } else if (this.dependsOnCoreCoder != var2.dependsOnCoreCoder) {
            return false;
         } else if (this.directMapping != var2.directMapping) {
            return false;
         } else if (this.epConfig != var2.epConfig) {
            return false;
         } else if (this.erHvxcExtensionFlag != var2.erHvxcExtensionFlag) {
            return false;
         } else if (this.extensionAudioObjectType != var2.extensionAudioObjectType) {
            return false;
         } else if (this.extensionChannelConfiguration != var2.extensionChannelConfiguration) {
            return false;
         } else if (this.extensionFlag != var2.extensionFlag) {
            return false;
         } else if (this.extensionFlag3 != var2.extensionFlag3) {
            return false;
         } else if (this.extensionSamplingFrequency != var2.extensionSamplingFrequency) {
            return false;
         } else if (this.extensionSamplingFrequencyIndex != var2.extensionSamplingFrequencyIndex) {
            return false;
         } else if (this.fillBits != var2.fillBits) {
            return false;
         } else if (this.frameLengthFlag != var2.frameLengthFlag) {
            return false;
         } else if (this.gaSpecificConfig != var2.gaSpecificConfig) {
            return false;
         } else if (this.hilnContMode != var2.hilnContMode) {
            return false;
         } else if (this.hilnEnhaLayer != var2.hilnEnhaLayer) {
            return false;
         } else if (this.hilnEnhaQuantMode != var2.hilnEnhaQuantMode) {
            return false;
         } else if (this.hilnFrameLength != var2.hilnFrameLength) {
            return false;
         } else if (this.hilnMaxNumLine != var2.hilnMaxNumLine) {
            return false;
         } else if (this.hilnQuantMode != var2.hilnQuantMode) {
            return false;
         } else if (this.hilnSampleRateCode != var2.hilnSampleRateCode) {
            return false;
         } else if (this.hvxcRateMode != var2.hvxcRateMode) {
            return false;
         } else if (this.hvxcVarMode != var2.hvxcVarMode) {
            return false;
         } else if (this.isBaseLayer != var2.isBaseLayer) {
            return false;
         } else if (this.layerNr != var2.layerNr) {
            return false;
         } else if (this.layer_length != var2.layer_length) {
            return false;
         } else if (this.numOfSubFrame != var2.numOfSubFrame) {
            return false;
         } else if (this.paraExtensionFlag != var2.paraExtensionFlag) {
            return false;
         } else if (this.paraMode != var2.paraMode) {
            return false;
         } else if (this.parametricSpecificConfig != var2.parametricSpecificConfig) {
            return false;
         } else if (this.psPresentFlag != var2.psPresentFlag) {
            return false;
         } else if (this.sacPayloadEmbedding != var2.sacPayloadEmbedding) {
            return false;
         } else if (this.samplingFrequency != var2.samplingFrequency) {
            return false;
         } else if (this.samplingFrequencyIndex != var2.samplingFrequencyIndex) {
            return false;
         } else if (this.sbrPresentFlag != var2.sbrPresentFlag) {
            return false;
         } else if (this.syncExtensionType != var2.syncExtensionType) {
            return false;
         } else if (this.var_ScalableFlag != var2.var_ScalableFlag) {
            return false;
         } else {
            return Arrays.equals(this.configBytes, var2.configBytes);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      byte[] var1 = this.configBytes;
      int var2;
      if (var1 != null) {
         var2 = Arrays.hashCode(var1);
      } else {
         var2 = 0;
      }

      return (((((((((((((((((((((((((((((((((((((((((var2 * 31 + this.audioObjectType) * 31 + this.samplingFrequencyIndex) * 31 + this.samplingFrequency) * 31 + this.channelConfiguration) * 31 + this.extensionAudioObjectType) * 31 + this.sbrPresentFlag) * 31 + this.psPresentFlag) * 31 + this.extensionSamplingFrequencyIndex) * 31 + this.extensionSamplingFrequency) * 31 + this.extensionChannelConfiguration) * 31 + this.sacPayloadEmbedding) * 31 + this.fillBits) * 31 + this.epConfig) * 31 + this.directMapping) * 31 + this.syncExtensionType) * 31 + this.frameLengthFlag) * 31 + this.dependsOnCoreCoder) * 31 + this.coreCoderDelay) * 31 + this.extensionFlag) * 31 + this.layerNr) * 31 + this.numOfSubFrame) * 31 + this.layer_length) * 31 + this.aacSectionDataResilienceFlag) * 31 + this.aacScalefactorDataResilienceFlag) * 31 + this.aacSpectralDataResilienceFlag) * 31 + this.extensionFlag3) * 31 + this.gaSpecificConfig) * 31 + this.isBaseLayer) * 31 + this.paraMode) * 31 + this.paraExtensionFlag) * 31 + this.hvxcVarMode) * 31 + this.hvxcRateMode) * 31 + this.erHvxcExtensionFlag) * 31 + this.var_ScalableFlag) * 31 + this.hilnQuantMode) * 31 + this.hilnMaxNumLine) * 31 + this.hilnSampleRateCode) * 31 + this.hilnFrameLength) * 31 + this.hilnContMode) * 31 + this.hilnEnhaLayer) * 31 + this.hilnEnhaQuantMode) * 31 + this.parametricSpecificConfig;
   }

   public void parseDetail(ByteBuffer var1) throws IOException {
      ByteBuffer var2 = var1.slice();
      var2.limit(super.sizeOfInstance);
      var1.position(var1.position() + super.sizeOfInstance);
      this.configBytes = new byte[super.sizeOfInstance];
      var2.get(this.configBytes);
      var2.rewind();
      BitReaderBuffer var4 = new BitReaderBuffer(var2);
      this.audioObjectType = this.getAudioObjectType(var4);
      this.samplingFrequencyIndex = var4.readBits(4);
      if (this.samplingFrequencyIndex == 15) {
         this.samplingFrequency = var4.readBits(24);
      }

      this.channelConfiguration = var4.readBits(4);
      int var3 = this.audioObjectType;
      if (var3 != 5 && var3 != 29) {
         this.extensionAudioObjectType = 0;
      } else {
         this.extensionAudioObjectType = 5;
         this.sbrPresentFlag = true;
         if (this.audioObjectType == 29) {
            this.psPresentFlag = true;
         }

         this.extensionSamplingFrequencyIndex = var4.readBits(4);
         if (this.extensionSamplingFrequencyIndex == 15) {
            this.extensionSamplingFrequency = var4.readBits(24);
         }

         this.audioObjectType = this.getAudioObjectType(var4);
         if (this.audioObjectType == 22) {
            this.extensionChannelConfiguration = var4.readBits(4);
         }
      }

      var3 = this.audioObjectType;
      switch(var3) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 6:
      case 7:
      case 17:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
         this.parseGaSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, var3, var4);
      case 5:
      case 10:
      case 11:
      case 18:
      case 29:
      case 31:
      default:
         break;
      case 8:
         throw new UnsupportedOperationException("can't parse CelpSpecificConfig yet");
      case 9:
         throw new UnsupportedOperationException("can't parse HvxcSpecificConfig yet");
      case 12:
         throw new UnsupportedOperationException("can't parse TTSSpecificConfig yet");
      case 13:
      case 14:
      case 15:
      case 16:
         throw new UnsupportedOperationException("can't parse StructuredAudioSpecificConfig yet");
      case 24:
         throw new UnsupportedOperationException("can't parse ErrorResilientCelpSpecificConfig yet");
      case 25:
         throw new UnsupportedOperationException("can't parse ErrorResilientHvxcSpecificConfig yet");
      case 26:
      case 27:
         this.parseParametricSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, var3, var4);
         break;
      case 28:
         throw new UnsupportedOperationException("can't parse SSCSpecificConfig yet");
      case 30:
         this.sacPayloadEmbedding = var4.readBits(1);
         throw new UnsupportedOperationException("can't parse SpatialSpecificConfig yet");
      case 32:
      case 33:
      case 34:
         throw new UnsupportedOperationException("can't parse MPEG_1_2_SpecificConfig yet");
      case 35:
         throw new UnsupportedOperationException("can't parse DSTSpecificConfig yet");
      case 36:
         this.fillBits = var4.readBits(5);
         throw new UnsupportedOperationException("can't parse ALSSpecificConfig yet");
      case 37:
      case 38:
         throw new UnsupportedOperationException("can't parse SLSSpecificConfig yet");
      case 39:
         this.eldSpecificConfig = new AudioSpecificConfig.ELDSpecificConfig(this.channelConfiguration, var4);
         break;
      case 40:
      case 41:
         throw new UnsupportedOperationException("can't parse SymbolicMusicSpecificConfig yet");
      }

      label114: {
         var3 = this.audioObjectType;
         if (var3 != 17 && var3 != 39) {
            switch(var3) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
               break;
            default:
               break label114;
            }
         }

         this.epConfig = var4.readBits(2);
         var3 = this.epConfig;
         if (var3 == 2 || var3 == 3) {
            throw new UnsupportedOperationException("can't parse ErrorProtectionSpecificConfig yet");
         }

         if (var3 == 3) {
            this.directMapping = var4.readBits(1);
            if (this.directMapping == 0) {
               throw new RuntimeException("not implemented");
            }
         }
      }

      if (this.extensionAudioObjectType != 5 && var4.remainingBits() >= 16) {
         this.syncExtensionType = var4.readBits(11);
         if (this.syncExtensionType == 695) {
            this.extensionAudioObjectType = this.getAudioObjectType(var4);
            if (this.extensionAudioObjectType == 5) {
               this.sbrPresentFlag = var4.readBool();
               if (this.sbrPresentFlag) {
                  this.extensionSamplingFrequencyIndex = var4.readBits(4);
                  if (this.extensionSamplingFrequencyIndex == 15) {
                     this.extensionSamplingFrequency = var4.readBits(24);
                  }

                  if (var4.remainingBits() >= 12) {
                     this.syncExtensionType = var4.readBits(11);
                     if (this.syncExtensionType == 1352) {
                        this.psPresentFlag = var4.readBool();
                     }
                  }
               }
            }

            if (this.extensionAudioObjectType == 22) {
               this.sbrPresentFlag = var4.readBool();
               if (this.sbrPresentFlag) {
                  this.extensionSamplingFrequencyIndex = var4.readBits(4);
                  if (this.extensionSamplingFrequencyIndex == 15) {
                     this.extensionSamplingFrequency = var4.readBits(24);
                  }
               }

               this.extensionChannelConfiguration = var4.readBits(4);
            }
         }
      }

   }

   public ByteBuffer serialize() {
      ByteBuffer var1 = ByteBuffer.allocate(this.serializedSize());
      IsoTypeWriter.writeUInt8(var1, 5);
      IsoTypeWriter.writeUInt8(var1, this.serializedSize() - 2);
      BitWriterBuffer var2 = new BitWriterBuffer(var1);
      var2.writeBits(this.audioObjectType, 5);
      var2.writeBits(this.samplingFrequencyIndex, 4);
      if (this.samplingFrequencyIndex != 15) {
         var2.writeBits(this.channelConfiguration, 4);
         return var1;
      } else {
         throw new UnsupportedOperationException("can't serialize that yet");
      }
   }

   public int serializedSize() {
      if (this.audioObjectType == 2) {
         return this.gaSpecificConfigSize() + 4;
      } else {
         throw new UnsupportedOperationException("can't serialize that yet");
      }
   }

   public void setAudioObjectType(int var1) {
      this.audioObjectType = var1;
   }

   public void setChannelConfiguration(int var1) {
      this.channelConfiguration = var1;
   }

   public void setSamplingFrequencyIndex(int var1) {
      this.samplingFrequencyIndex = var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("AudioSpecificConfig");
      var1.append("{configBytes=");
      var1.append(Hex.encodeHex(this.configBytes));
      var1.append(", audioObjectType=");
      var1.append(this.audioObjectType);
      var1.append(" (");
      var1.append((String)audioObjectTypeMap.get(this.audioObjectType));
      var1.append(")");
      var1.append(", samplingFrequencyIndex=");
      var1.append(this.samplingFrequencyIndex);
      var1.append(" (");
      var1.append(samplingFrequencyIndexMap.get(this.samplingFrequencyIndex));
      var1.append(")");
      var1.append(", samplingFrequency=");
      var1.append(this.samplingFrequency);
      var1.append(", channelConfiguration=");
      var1.append(this.channelConfiguration);
      if (this.extensionAudioObjectType > 0) {
         var1.append(", extensionAudioObjectType=");
         var1.append(this.extensionAudioObjectType);
         var1.append(" (");
         var1.append((String)audioObjectTypeMap.get(this.extensionAudioObjectType));
         var1.append(")");
         var1.append(", sbrPresentFlag=");
         var1.append(this.sbrPresentFlag);
         var1.append(", psPresentFlag=");
         var1.append(this.psPresentFlag);
         var1.append(", extensionSamplingFrequencyIndex=");
         var1.append(this.extensionSamplingFrequencyIndex);
         var1.append(" (");
         var1.append(samplingFrequencyIndexMap.get(this.extensionSamplingFrequencyIndex));
         var1.append(")");
         var1.append(", extensionSamplingFrequency=");
         var1.append(this.extensionSamplingFrequency);
         var1.append(", extensionChannelConfiguration=");
         var1.append(this.extensionChannelConfiguration);
      }

      var1.append(", syncExtensionType=");
      var1.append(this.syncExtensionType);
      if (this.gaSpecificConfig) {
         var1.append(", frameLengthFlag=");
         var1.append(this.frameLengthFlag);
         var1.append(", dependsOnCoreCoder=");
         var1.append(this.dependsOnCoreCoder);
         var1.append(", coreCoderDelay=");
         var1.append(this.coreCoderDelay);
         var1.append(", extensionFlag=");
         var1.append(this.extensionFlag);
         var1.append(", layerNr=");
         var1.append(this.layerNr);
         var1.append(", numOfSubFrame=");
         var1.append(this.numOfSubFrame);
         var1.append(", layer_length=");
         var1.append(this.layer_length);
         var1.append(", aacSectionDataResilienceFlag=");
         var1.append(this.aacSectionDataResilienceFlag);
         var1.append(", aacScalefactorDataResilienceFlag=");
         var1.append(this.aacScalefactorDataResilienceFlag);
         var1.append(", aacSpectralDataResilienceFlag=");
         var1.append(this.aacSpectralDataResilienceFlag);
         var1.append(", extensionFlag3=");
         var1.append(this.extensionFlag3);
      }

      if (this.parametricSpecificConfig) {
         var1.append(", isBaseLayer=");
         var1.append(this.isBaseLayer);
         var1.append(", paraMode=");
         var1.append(this.paraMode);
         var1.append(", paraExtensionFlag=");
         var1.append(this.paraExtensionFlag);
         var1.append(", hvxcVarMode=");
         var1.append(this.hvxcVarMode);
         var1.append(", hvxcRateMode=");
         var1.append(this.hvxcRateMode);
         var1.append(", erHvxcExtensionFlag=");
         var1.append(this.erHvxcExtensionFlag);
         var1.append(", var_ScalableFlag=");
         var1.append(this.var_ScalableFlag);
         var1.append(", hilnQuantMode=");
         var1.append(this.hilnQuantMode);
         var1.append(", hilnMaxNumLine=");
         var1.append(this.hilnMaxNumLine);
         var1.append(", hilnSampleRateCode=");
         var1.append(this.hilnSampleRateCode);
         var1.append(", hilnFrameLength=");
         var1.append(this.hilnFrameLength);
         var1.append(", hilnContMode=");
         var1.append(this.hilnContMode);
         var1.append(", hilnEnhaLayer=");
         var1.append(this.hilnEnhaLayer);
         var1.append(", hilnEnhaQuantMode=");
         var1.append(this.hilnEnhaQuantMode);
      }

      var1.append('}');
      return var1.toString();
   }

   public class ELDSpecificConfig {
      public boolean aacScalefactorDataResilienceFlag;
      public boolean aacSectionDataResilienceFlag;
      public boolean aacSpectralDataResilienceFlag;
      public boolean frameLengthFlag;
      public boolean ldSbrCrcFlag;
      public boolean ldSbrPresentFlag;
      public boolean ldSbrSamplingRate;

      public ELDSpecificConfig(int var2, BitReaderBuffer var3) {
         this.frameLengthFlag = var3.readBool();
         this.aacSectionDataResilienceFlag = var3.readBool();
         this.aacScalefactorDataResilienceFlag = var3.readBool();
         this.aacSpectralDataResilienceFlag = var3.readBool();
         this.ldSbrPresentFlag = var3.readBool();
         if (this.ldSbrPresentFlag) {
            this.ldSbrSamplingRate = var3.readBool();
            this.ldSbrCrcFlag = var3.readBool();
            this.ld_sbr_header(var2, var3);
         }

         while(var3.readBits(4) != 0) {
            var2 = var3.readBits(4);
            byte var4 = 0;
            int var5;
            if (var2 == 15) {
               var5 = var3.readBits(8);
               var2 += var5;
            } else {
               var5 = 0;
            }

            int var6 = var4;
            int var7 = var2;
            if (var5 == 255) {
               var7 = var2 + var3.readBits(16);
               var6 = var4;
            }

            while(var6 < var7) {
               var3.readBits(8);
               ++var6;
            }
         }

      }

      public void ld_sbr_header(int var1, BitReaderBuffer var2) {
         int var3 = 0;
         byte var4;
         switch(var1) {
         case 1:
         case 2:
            var4 = 1;
            break;
         case 3:
            var4 = 2;
            break;
         case 4:
         case 5:
         case 6:
            var4 = 3;
            break;
         case 7:
            var4 = 4;
            break;
         default:
            var4 = 0;
         }

         while(var3 < var4) {
            AudioSpecificConfig.this.new sbr_header(var2);
            ++var3;
         }

      }
   }

   public class sbr_header {
      public boolean bs_alter_scale;
      public boolean bs_amp_res;
      public int bs_freq_scale;
      public boolean bs_header_extra_1;
      public boolean bs_header_extra_2;
      public boolean bs_interpol_freq;
      public int bs_limiter_bands;
      public int bs_limiter_gains;
      public int bs_noise_bands;
      public int bs_reserved;
      public boolean bs_smoothing_mode;
      public int bs_start_freq;
      public int bs_stop_freq;
      public int bs_xover_band;

      public sbr_header(BitReaderBuffer var2) {
         this.bs_amp_res = var2.readBool();
         this.bs_start_freq = var2.readBits(4);
         this.bs_stop_freq = var2.readBits(4);
         this.bs_xover_band = var2.readBits(3);
         this.bs_reserved = var2.readBits(2);
         this.bs_header_extra_1 = var2.readBool();
         this.bs_header_extra_2 = var2.readBool();
         if (this.bs_header_extra_1) {
            this.bs_freq_scale = var2.readBits(2);
            this.bs_alter_scale = var2.readBool();
            this.bs_noise_bands = var2.readBits(2);
         }

         if (this.bs_header_extra_2) {
            this.bs_limiter_bands = var2.readBits(2);
            this.bs_limiter_gains = var2.readBits(2);
            this.bs_interpol_freq = var2.readBool();
         }

         this.bs_smoothing_mode = var2.readBool();
      }
   }
}
