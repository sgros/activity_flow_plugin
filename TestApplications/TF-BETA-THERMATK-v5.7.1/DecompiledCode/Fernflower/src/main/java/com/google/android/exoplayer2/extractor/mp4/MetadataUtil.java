package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.InternalFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.ByteBuffer;

final class MetadataUtil {
   private static final int SHORT_TYPE_ALBUM = Util.getIntegerCodeForString("alb");
   private static final int SHORT_TYPE_ARTIST = Util.getIntegerCodeForString("ART");
   private static final int SHORT_TYPE_COMMENT = Util.getIntegerCodeForString("cmt");
   private static final int SHORT_TYPE_COMPOSER_1 = Util.getIntegerCodeForString("com");
   private static final int SHORT_TYPE_COMPOSER_2 = Util.getIntegerCodeForString("wrt");
   private static final int SHORT_TYPE_ENCODER = Util.getIntegerCodeForString("too");
   private static final int SHORT_TYPE_GENRE = Util.getIntegerCodeForString("gen");
   private static final int SHORT_TYPE_LYRICS = Util.getIntegerCodeForString("lyr");
   private static final int SHORT_TYPE_NAME_1 = Util.getIntegerCodeForString("nam");
   private static final int SHORT_TYPE_NAME_2 = Util.getIntegerCodeForString("trk");
   private static final int SHORT_TYPE_YEAR = Util.getIntegerCodeForString("day");
   private static final String[] STANDARD_GENRES = new String[]{"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "Jpop", "Synthpop"};
   private static final int TYPE_ALBUM_ARTIST = Util.getIntegerCodeForString("aART");
   private static final int TYPE_COMPILATION = Util.getIntegerCodeForString("cpil");
   private static final int TYPE_COVER_ART = Util.getIntegerCodeForString("covr");
   private static final int TYPE_DISK_NUMBER = Util.getIntegerCodeForString("disk");
   private static final int TYPE_GAPLESS_ALBUM = Util.getIntegerCodeForString("pgap");
   private static final int TYPE_GENRE = Util.getIntegerCodeForString("gnre");
   private static final int TYPE_GROUPING = Util.getIntegerCodeForString("grp");
   private static final int TYPE_INTERNAL = Util.getIntegerCodeForString("----");
   private static final int TYPE_RATING = Util.getIntegerCodeForString("rtng");
   private static final int TYPE_SORT_ALBUM = Util.getIntegerCodeForString("soal");
   private static final int TYPE_SORT_ALBUM_ARTIST = Util.getIntegerCodeForString("soaa");
   private static final int TYPE_SORT_ARTIST = Util.getIntegerCodeForString("soar");
   private static final int TYPE_SORT_COMPOSER = Util.getIntegerCodeForString("soco");
   private static final int TYPE_SORT_TRACK_NAME = Util.getIntegerCodeForString("sonm");
   private static final int TYPE_TEMPO = Util.getIntegerCodeForString("tmpo");
   private static final int TYPE_TRACK_NUMBER = Util.getIntegerCodeForString("trkn");
   private static final int TYPE_TV_SHOW = Util.getIntegerCodeForString("tvsh");
   private static final int TYPE_TV_SORT_SHOW = Util.getIntegerCodeForString("sosn");

   public static Format getFormatWithMetadata(int var0, Format var1, Metadata var2, Metadata var3, GaplessInfoHolder var4) {
      Format var12;
      if (var0 == 1) {
         Format var11 = var1;
         if (var4.hasGaplessInfo()) {
            var11 = var1.copyWithGaplessInfo(var4.encoderDelay, var4.encoderPadding);
         }

         var12 = var11;
         if (var2 != null) {
            var12 = var11.copyWithMetadata(var2);
         }
      } else {
         var12 = var1;
         if (var0 == 2) {
            var12 = var1;
            if (var3 != null) {
               Format var10;
               for(var0 = 0; var0 < var3.length(); var1 = var10) {
                  Metadata.Entry var13 = var3.get(var0);
                  var10 = var1;
                  if (var13 instanceof MdtaMetadataEntry) {
                     MdtaMetadataEntry var14 = (MdtaMetadataEntry)var13;
                     var10 = var1;
                     if ("com.android.capture.fps".equals(var14.key)) {
                        var10 = var1;
                        if (var14.typeIndicator == 23) {
                           label82: {
                              var10 = var1;

                              label66: {
                                 label79: {
                                    boolean var10001;
                                    try {
                                       var1 = var1.copyWithFrameRate(ByteBuffer.wrap(var14.value).asFloatBuffer().get());
                                    } catch (NumberFormatException var9) {
                                       var10001 = false;
                                       break label79;
                                    }

                                    var10 = var1;

                                    Metadata var5;
                                    try {
                                       var5 = new Metadata;
                                    } catch (NumberFormatException var8) {
                                       var10001 = false;
                                       break label79;
                                    }

                                    var10 = var1;

                                    try {
                                       var5.<init>(var14);
                                    } catch (NumberFormatException var7) {
                                       var10001 = false;
                                       break label79;
                                    }

                                    var10 = var1;

                                    try {
                                       var1 = var1.copyWithMetadata(var5);
                                       break label66;
                                    } catch (NumberFormatException var6) {
                                       var10001 = false;
                                    }
                                 }

                                 Log.w("MetadataUtil", "Ignoring invalid framerate");
                                 break label82;
                              }

                              var10 = var1;
                           }
                        }
                     }
                  }

                  ++var0;
               }

               var12 = var1;
            }
         }
      }

      return var12;
   }

   private static CommentFrame parseCommentAttribute(int var0, ParsableByteArray var1) {
      int var2 = var1.readInt();
      if (var1.readInt() == Atom.TYPE_data) {
         var1.skipBytes(8);
         String var4 = var1.readNullTerminatedString(var2 - 16);
         return new CommentFrame("und", var4, var4);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Failed to parse comment attribute: ");
         var3.append(Atom.getAtomTypeString(var0));
         Log.w("MetadataUtil", var3.toString());
         return null;
      }
   }

   private static ApicFrame parseCoverArt(ParsableByteArray var0) {
      int var1 = var0.readInt();
      if (var0.readInt() == Atom.TYPE_data) {
         int var2 = Atom.parseFullAtomFlags(var0.readInt());
         String var3;
         if (var2 == 13) {
            var3 = "image/jpeg";
         } else if (var2 == 14) {
            var3 = "image/png";
         } else {
            var3 = null;
         }

         if (var3 == null) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Unrecognized cover art flags: ");
            var5.append(var2);
            Log.w("MetadataUtil", var5.toString());
            return null;
         } else {
            var0.skipBytes(4);
            byte[] var4 = new byte[var1 - 16];
            var0.readBytes(var4, 0, var4.length);
            return new ApicFrame(var3, (String)null, 3, var4);
         }
      } else {
         Log.w("MetadataUtil", "Failed to parse cover art attribute");
         return null;
      }
   }

   public static Metadata.Entry parseIlstElement(ParsableByteArray var0) {
      int var1;
      TextInformationFrame var4;
      label8954: {
         label8953: {
            label8952: {
               Id3Frame var938;
               label8951: {
                  label8950: {
                     ApicFrame var939;
                     label8949: {
                        label8948: {
                           label8947: {
                              label8946: {
                                 label8945: {
                                    label8944: {
                                       label8943: {
                                          label8942: {
                                             label8941: {
                                                label8940: {
                                                   label8939: {
                                                      label8938: {
                                                         CommentFrame var935;
                                                         label8937: {
                                                            label8936: {
                                                               label8935: {
                                                                  label8934: {
                                                                     label8933: {
                                                                        label8932: {
                                                                           label8931: {
                                                                              label8930: {
                                                                                 Throwable var10000;
                                                                                 label8956: {
                                                                                    int var2;
                                                                                    boolean var10001;
                                                                                    label8963: {
                                                                                       var1 = var0.getPosition() + var0.readInt();
                                                                                       var2 = var0.readInt();
                                                                                       int var3 = var2 >> 24 & 255;
                                                                                       if (var3 != 169 && var3 != 253) {
                                                                                          try {
                                                                                             if (var2 == TYPE_GENRE) {
                                                                                                var4 = parseStandardGenreAttribute(var0);
                                                                                                break label8954;
                                                                                             }
                                                                                          } catch (Throwable var931) {
                                                                                             var10000 = var931;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_DISK_NUMBER) {
                                                                                                var4 = parseIndexAndCountAttribute(var2, "TPOS", var0);
                                                                                                break label8953;
                                                                                             }
                                                                                          } catch (Throwable var930) {
                                                                                             var10000 = var930;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_TRACK_NUMBER) {
                                                                                                var4 = parseIndexAndCountAttribute(var2, "TRCK", var0);
                                                                                                break label8952;
                                                                                             }
                                                                                          } catch (Throwable var929) {
                                                                                             var10000 = var929;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_TEMPO) {
                                                                                                var938 = parseUint8Attribute(var2, "TBPM", var0, true, false);
                                                                                                break label8951;
                                                                                             }
                                                                                          } catch (Throwable var928) {
                                                                                             var10000 = var928;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_COMPILATION) {
                                                                                                var938 = parseUint8Attribute(var2, "TCMP", var0, true, true);
                                                                                                break label8950;
                                                                                             }
                                                                                          } catch (Throwable var927) {
                                                                                             var10000 = var927;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_COVER_ART) {
                                                                                                var939 = parseCoverArt(var0);
                                                                                                break label8949;
                                                                                             }
                                                                                          } catch (Throwable var926) {
                                                                                             var10000 = var926;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_ALBUM_ARTIST) {
                                                                                                var4 = parseTextAttribute(var2, "TPE2", var0);
                                                                                                break label8948;
                                                                                             }
                                                                                          } catch (Throwable var925) {
                                                                                             var10000 = var925;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_SORT_TRACK_NAME) {
                                                                                                var4 = parseTextAttribute(var2, "TSOT", var0);
                                                                                                break label8947;
                                                                                             }
                                                                                          } catch (Throwable var924) {
                                                                                             var10000 = var924;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_SORT_ALBUM) {
                                                                                                var4 = parseTextAttribute(var2, "TSO2", var0);
                                                                                                break label8946;
                                                                                             }
                                                                                          } catch (Throwable var923) {
                                                                                             var10000 = var923;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_SORT_ARTIST) {
                                                                                                var4 = parseTextAttribute(var2, "TSOA", var0);
                                                                                                break label8945;
                                                                                             }
                                                                                          } catch (Throwable var922) {
                                                                                             var10000 = var922;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_SORT_ALBUM_ARTIST) {
                                                                                                var4 = parseTextAttribute(var2, "TSOP", var0);
                                                                                                break label8944;
                                                                                             }
                                                                                          } catch (Throwable var921) {
                                                                                             var10000 = var921;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_SORT_COMPOSER) {
                                                                                                var4 = parseTextAttribute(var2, "TSOC", var0);
                                                                                                break label8943;
                                                                                             }
                                                                                          } catch (Throwable var920) {
                                                                                             var10000 = var920;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_RATING) {
                                                                                                var938 = parseUint8Attribute(var2, "ITUNESADVISORY", var0, false, false);
                                                                                                break label8942;
                                                                                             }
                                                                                          } catch (Throwable var919) {
                                                                                             var10000 = var919;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_GAPLESS_ALBUM) {
                                                                                                var938 = parseUint8Attribute(var2, "ITUNESGAPLESS", var0, false, true);
                                                                                                break label8941;
                                                                                             }
                                                                                          } catch (Throwable var918) {
                                                                                             var10000 = var918;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_TV_SORT_SHOW) {
                                                                                                var4 = parseTextAttribute(var2, "TVSHOWSORT", var0);
                                                                                                break label8940;
                                                                                             }
                                                                                          } catch (Throwable var917) {
                                                                                             var10000 = var917;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_TV_SHOW) {
                                                                                                var4 = parseTextAttribute(var2, "TVSHOW", var0);
                                                                                                break label8939;
                                                                                             }
                                                                                          } catch (Throwable var916) {
                                                                                             var10000 = var916;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var2 == TYPE_INTERNAL) {
                                                                                                var938 = parseInternalAttribute(var0, var1);
                                                                                                break label8938;
                                                                                             }
                                                                                          } catch (Throwable var915) {
                                                                                             var10000 = var915;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }
                                                                                       } else {
                                                                                          var3 = 16777215 & var2;

                                                                                          try {
                                                                                             if (var3 == SHORT_TYPE_COMMENT) {
                                                                                                var935 = parseCommentAttribute(var2, var0);
                                                                                                break label8937;
                                                                                             }
                                                                                          } catch (Throwable var932) {
                                                                                             var10000 = var932;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          label8922:
                                                                                          try {
                                                                                             if (var3 != SHORT_TYPE_NAME_1 && var3 != SHORT_TYPE_NAME_2) {
                                                                                                break label8922;
                                                                                             }
                                                                                             break label8963;
                                                                                          } catch (Throwable var934) {
                                                                                             var10000 = var934;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          label8961: {
                                                                                             try {
                                                                                                if (var3 != SHORT_TYPE_COMPOSER_1 && var3 != SHORT_TYPE_COMPOSER_2) {
                                                                                                   break label8961;
                                                                                                }
                                                                                             } catch (Throwable var933) {
                                                                                                var10000 = var933;
                                                                                                var10001 = false;
                                                                                                break label8956;
                                                                                             }

                                                                                             try {
                                                                                                var4 = parseTextAttribute(var2, "TCOM", var0);
                                                                                             } catch (Throwable var906) {
                                                                                                var10000 = var906;
                                                                                                var10001 = false;
                                                                                                break label8956;
                                                                                             }

                                                                                             var0.setPosition(var1);
                                                                                             return var4;
                                                                                          }

                                                                                          try {
                                                                                             if (var3 == SHORT_TYPE_YEAR) {
                                                                                                var4 = parseTextAttribute(var2, "TDRC", var0);
                                                                                                break label8936;
                                                                                             }
                                                                                          } catch (Throwable var914) {
                                                                                             var10000 = var914;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var3 == SHORT_TYPE_ARTIST) {
                                                                                                var4 = parseTextAttribute(var2, "TPE1", var0);
                                                                                                break label8935;
                                                                                             }
                                                                                          } catch (Throwable var913) {
                                                                                             var10000 = var913;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var3 == SHORT_TYPE_ENCODER) {
                                                                                                var4 = parseTextAttribute(var2, "TSSE", var0);
                                                                                                break label8934;
                                                                                             }
                                                                                          } catch (Throwable var912) {
                                                                                             var10000 = var912;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var3 == SHORT_TYPE_ALBUM) {
                                                                                                var4 = parseTextAttribute(var2, "TALB", var0);
                                                                                                break label8933;
                                                                                             }
                                                                                          } catch (Throwable var911) {
                                                                                             var10000 = var911;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var3 == SHORT_TYPE_LYRICS) {
                                                                                                var4 = parseTextAttribute(var2, "USLT", var0);
                                                                                                break label8932;
                                                                                             }
                                                                                          } catch (Throwable var910) {
                                                                                             var10000 = var910;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var3 == SHORT_TYPE_GENRE) {
                                                                                                var4 = parseTextAttribute(var2, "TCON", var0);
                                                                                                break label8931;
                                                                                             }
                                                                                          } catch (Throwable var909) {
                                                                                             var10000 = var909;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }

                                                                                          try {
                                                                                             if (var3 == TYPE_GROUPING) {
                                                                                                var4 = parseTextAttribute(var2, "TIT1", var0);
                                                                                                break label8930;
                                                                                             }
                                                                                          } catch (Throwable var908) {
                                                                                             var10000 = var908;
                                                                                             var10001 = false;
                                                                                             break label8956;
                                                                                          }
                                                                                       }

                                                                                       try {
                                                                                          StringBuilder var937 = new StringBuilder();
                                                                                          var937.append("Skipped unknown metadata entry: ");
                                                                                          var937.append(Atom.getAtomTypeString(var2));
                                                                                          Log.d("MetadataUtil", var937.toString());
                                                                                       } catch (Throwable var905) {
                                                                                          var10000 = var905;
                                                                                          var10001 = false;
                                                                                          break label8956;
                                                                                       }

                                                                                       var0.setPosition(var1);
                                                                                       return null;
                                                                                    }

                                                                                    try {
                                                                                       var4 = parseTextAttribute(var2, "TIT2", var0);
                                                                                    } catch (Throwable var907) {
                                                                                       var10000 = var907;
                                                                                       var10001 = false;
                                                                                       break label8956;
                                                                                    }

                                                                                    var0.setPosition(var1);
                                                                                    return var4;
                                                                                 }

                                                                                 Throwable var936 = var10000;
                                                                                 var0.setPosition(var1);
                                                                                 throw var936;
                                                                              }

                                                                              var0.setPosition(var1);
                                                                              return var4;
                                                                           }

                                                                           var0.setPosition(var1);
                                                                           return var4;
                                                                        }

                                                                        var0.setPosition(var1);
                                                                        return var4;
                                                                     }

                                                                     var0.setPosition(var1);
                                                                     return var4;
                                                                  }

                                                                  var0.setPosition(var1);
                                                                  return var4;
                                                               }

                                                               var0.setPosition(var1);
                                                               return var4;
                                                            }

                                                            var0.setPosition(var1);
                                                            return var4;
                                                         }

                                                         var0.setPosition(var1);
                                                         return var935;
                                                      }

                                                      var0.setPosition(var1);
                                                      return var938;
                                                   }

                                                   var0.setPosition(var1);
                                                   return var4;
                                                }

                                                var0.setPosition(var1);
                                                return var4;
                                             }

                                             var0.setPosition(var1);
                                             return var938;
                                          }

                                          var0.setPosition(var1);
                                          return var938;
                                       }

                                       var0.setPosition(var1);
                                       return var4;
                                    }

                                    var0.setPosition(var1);
                                    return var4;
                                 }

                                 var0.setPosition(var1);
                                 return var4;
                              }

                              var0.setPosition(var1);
                              return var4;
                           }

                           var0.setPosition(var1);
                           return var4;
                        }

                        var0.setPosition(var1);
                        return var4;
                     }

                     var0.setPosition(var1);
                     return var939;
                  }

                  var0.setPosition(var1);
                  return var938;
               }

               var0.setPosition(var1);
               return var938;
            }

            var0.setPosition(var1);
            return var4;
         }

         var0.setPosition(var1);
         return var4;
      }

      var0.setPosition(var1);
      return var4;
   }

   private static TextInformationFrame parseIndexAndCountAttribute(int var0, String var1, ParsableByteArray var2) {
      int var3 = var2.readInt();
      if (var2.readInt() == Atom.TYPE_data && var3 >= 22) {
         var2.skipBytes(10);
         var3 = var2.readUnsignedShort();
         if (var3 > 0) {
            StringBuilder var4 = new StringBuilder();
            var4.append("");
            var4.append(var3);
            String var8 = var4.toString();
            var0 = var2.readUnsignedShort();
            String var6 = var8;
            if (var0 > 0) {
               StringBuilder var7 = new StringBuilder();
               var7.append(var8);
               var7.append("/");
               var7.append(var0);
               var6 = var7.toString();
            }

            return new TextInformationFrame(var1, (String)null, var6);
         }
      }

      StringBuilder var5 = new StringBuilder();
      var5.append("Failed to parse index/count attribute: ");
      var5.append(Atom.getAtomTypeString(var0));
      Log.w("MetadataUtil", var5.toString());
      return null;
   }

   private static Id3Frame parseInternalAttribute(ParsableByteArray var0, int var1) {
      String var2 = null;
      String var3 = var2;
      int var4 = -1;
      int var5 = -1;

      while(var0.getPosition() < var1) {
         int var6 = var0.getPosition();
         int var7 = var0.readInt();
         int var8 = var0.readInt();
         var0.skipBytes(4);
         if (var8 == Atom.TYPE_mean) {
            var2 = var0.readNullTerminatedString(var7 - 12);
         } else if (var8 == Atom.TYPE_name) {
            var3 = var0.readNullTerminatedString(var7 - 12);
         } else {
            if (var8 == Atom.TYPE_data) {
               var4 = var6;
               var5 = var7;
            }

            var0.skipBytes(var7 - 12);
         }
      }

      if (var2 != null && var3 != null && var4 != -1) {
         var0.setPosition(var4);
         var0.skipBytes(16);
         return new InternalFrame(var2, var3, var0.readNullTerminatedString(var5 - 16));
      } else {
         return null;
      }
   }

   public static MdtaMetadataEntry parseMdtaMetadataEntryFromIlst(ParsableByteArray var0, int var1, String var2) {
      while(true) {
         int var3 = var0.getPosition();
         if (var3 >= var1) {
            return null;
         }

         int var4 = var0.readInt();
         if (var0.readInt() == Atom.TYPE_data) {
            var1 = var0.readInt();
            var3 = var0.readInt();
            var4 -= 16;
            byte[] var5 = new byte[var4];
            var0.readBytes(var5, 0, var4);
            return new MdtaMetadataEntry(var2, var5, var3, var1);
         }

         var0.setPosition(var3 + var4);
      }
   }

   private static TextInformationFrame parseStandardGenreAttribute(ParsableByteArray var0) {
      String var3;
      label16: {
         int var1 = parseUint8AttributeValue(var0);
         if (var1 > 0) {
            String[] var2 = STANDARD_GENRES;
            if (var1 <= var2.length) {
               var3 = var2[var1 - 1];
               break label16;
            }
         }

         var3 = null;
      }

      if (var3 != null) {
         return new TextInformationFrame("TCON", (String)null, var3);
      } else {
         Log.w("MetadataUtil", "Failed to parse standard genre code");
         return null;
      }
   }

   private static TextInformationFrame parseTextAttribute(int var0, String var1, ParsableByteArray var2) {
      int var3 = var2.readInt();
      if (var2.readInt() == Atom.TYPE_data) {
         var2.skipBytes(8);
         return new TextInformationFrame(var1, (String)null, var2.readNullTerminatedString(var3 - 16));
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Failed to parse text attribute: ");
         var4.append(Atom.getAtomTypeString(var0));
         Log.w("MetadataUtil", var4.toString());
         return null;
      }
   }

   private static Id3Frame parseUint8Attribute(int var0, String var1, ParsableByteArray var2, boolean var3, boolean var4) {
      int var5 = parseUint8AttributeValue(var2);
      int var6 = var5;
      if (var4) {
         var6 = Math.min(1, var5);
      }

      if (var6 >= 0) {
         Object var8;
         if (var3) {
            var8 = new TextInformationFrame(var1, (String)null, Integer.toString(var6));
         } else {
            var8 = new CommentFrame("und", var1, Integer.toString(var6));
         }

         return (Id3Frame)var8;
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("Failed to parse uint8 attribute: ");
         var7.append(Atom.getAtomTypeString(var0));
         Log.w("MetadataUtil", var7.toString());
         return null;
      }
   }

   private static int parseUint8AttributeValue(ParsableByteArray var0) {
      var0.skipBytes(4);
      if (var0.readInt() == Atom.TYPE_data) {
         var0.skipBytes(8);
         return var0.readUnsignedByte();
      } else {
         Log.w("MetadataUtil", "Failed to parse uint8 attribute value");
         return -1;
      }
   }
}
