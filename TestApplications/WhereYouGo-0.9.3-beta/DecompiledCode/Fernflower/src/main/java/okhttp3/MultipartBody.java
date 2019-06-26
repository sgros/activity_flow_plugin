package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;

public final class MultipartBody extends RequestBody {
   public static final MediaType ALTERNATIVE = MediaType.parse("multipart/alternative");
   private static final byte[] COLONSPACE = new byte[]{58, 32};
   private static final byte[] CRLF = new byte[]{13, 10};
   private static final byte[] DASHDASH = new byte[]{45, 45};
   public static final MediaType DIGEST = MediaType.parse("multipart/digest");
   public static final MediaType FORM = MediaType.parse("multipart/form-data");
   public static final MediaType MIXED = MediaType.parse("multipart/mixed");
   public static final MediaType PARALLEL = MediaType.parse("multipart/parallel");
   private final ByteString boundary;
   private long contentLength = -1L;
   private final MediaType contentType;
   private final MediaType originalType;
   private final List parts;

   MultipartBody(ByteString var1, MediaType var2, List var3) {
      this.boundary = var1;
      this.originalType = var2;
      this.contentType = MediaType.parse(var2 + "; boundary=" + var1.utf8());
      this.parts = Util.immutableList(var3);
   }

   static StringBuilder appendQuotedString(StringBuilder var0, String var1) {
      var0.append('"');
      int var2 = 0;

      for(int var3 = var1.length(); var2 < var3; ++var2) {
         char var4 = var1.charAt(var2);
         switch(var4) {
         case '\n':
            var0.append("%0A");
            break;
         case '\r':
            var0.append("%0D");
            break;
         case '"':
            var0.append("%22");
            break;
         default:
            var0.append(var4);
         }
      }

      var0.append('"');
      return var0;
   }

   private long writeOrCountBytes(BufferedSink var1, boolean var2) throws IOException {
      long var3 = 0L;
      Buffer var5 = null;
      if (var2) {
         var5 = new Buffer();
         var1 = var5;
      }

      int var6 = 0;
      int var7 = this.parts.size();

      long var12;
      while(true) {
         if (var6 >= var7) {
            ((BufferedSink)var1).write(DASHDASH);
            ((BufferedSink)var1).write(this.boundary);
            ((BufferedSink)var1).write(DASHDASH);
            ((BufferedSink)var1).write(CRLF);
            var12 = var3;
            if (var2) {
               var12 = var3 + var5.size();
               var5.clear();
            }
            break;
         }

         MultipartBody.Part var8 = (MultipartBody.Part)this.parts.get(var6);
         Headers var9 = var8.headers;
         RequestBody var14 = var8.body;
         ((BufferedSink)var1).write(DASHDASH);
         ((BufferedSink)var1).write(this.boundary);
         ((BufferedSink)var1).write(CRLF);
         if (var9 != null) {
            int var10 = 0;

            for(int var11 = var9.size(); var10 < var11; ++var10) {
               ((BufferedSink)var1).writeUtf8(var9.name(var10)).write(COLONSPACE).writeUtf8(var9.value(var10)).write(CRLF);
            }
         }

         MediaType var15 = var14.contentType();
         if (var15 != null) {
            ((BufferedSink)var1).writeUtf8("Content-Type: ").writeUtf8(var15.toString()).write(CRLF);
         }

         var12 = var14.contentLength();
         if (var12 != -1L) {
            ((BufferedSink)var1).writeUtf8("Content-Length: ").writeDecimalLong(var12).write(CRLF);
         } else if (var2) {
            var5.clear();
            var12 = -1L;
            break;
         }

         ((BufferedSink)var1).write(CRLF);
         if (var2) {
            var3 += var12;
         } else {
            var14.writeTo((BufferedSink)var1);
         }

         ((BufferedSink)var1).write(CRLF);
         ++var6;
      }

      return var12;
   }

   public String boundary() {
      return this.boundary.utf8();
   }

   public long contentLength() throws IOException {
      long var1 = this.contentLength;
      if (var1 == -1L) {
         var1 = this.writeOrCountBytes((BufferedSink)null, true);
         this.contentLength = var1;
      }

      return var1;
   }

   public MediaType contentType() {
      return this.contentType;
   }

   public MultipartBody.Part part(int var1) {
      return (MultipartBody.Part)this.parts.get(var1);
   }

   public List parts() {
      return this.parts;
   }

   public int size() {
      return this.parts.size();
   }

   public MediaType type() {
      return this.originalType;
   }

   public void writeTo(BufferedSink var1) throws IOException {
      this.writeOrCountBytes(var1, false);
   }

   public static final class Builder {
      private final ByteString boundary;
      private final List parts;
      private MediaType type;

      public Builder() {
         this(UUID.randomUUID().toString());
      }

      public Builder(String var1) {
         this.type = MultipartBody.MIXED;
         this.parts = new ArrayList();
         this.boundary = ByteString.encodeUtf8(var1);
      }

      public MultipartBody.Builder addFormDataPart(String var1, String var2) {
         return this.addPart(MultipartBody.Part.createFormData(var1, var2));
      }

      public MultipartBody.Builder addFormDataPart(String var1, String var2, RequestBody var3) {
         return this.addPart(MultipartBody.Part.createFormData(var1, var2, var3));
      }

      public MultipartBody.Builder addPart(Headers var1, RequestBody var2) {
         return this.addPart(MultipartBody.Part.create(var1, var2));
      }

      public MultipartBody.Builder addPart(MultipartBody.Part var1) {
         if (var1 == null) {
            throw new NullPointerException("part == null");
         } else {
            this.parts.add(var1);
            return this;
         }
      }

      public MultipartBody.Builder addPart(RequestBody var1) {
         return this.addPart(MultipartBody.Part.create(var1));
      }

      public MultipartBody build() {
         if (this.parts.isEmpty()) {
            throw new IllegalStateException("Multipart body must have at least one part.");
         } else {
            return new MultipartBody(this.boundary, this.type, this.parts);
         }
      }

      public MultipartBody.Builder setType(MediaType var1) {
         if (var1 == null) {
            throw new NullPointerException("type == null");
         } else if (!var1.type().equals("multipart")) {
            throw new IllegalArgumentException("multipart != " + var1);
         } else {
            this.type = var1;
            return this;
         }
      }
   }

   public static final class Part {
      final RequestBody body;
      final Headers headers;

      private Part(Headers var1, RequestBody var2) {
         this.headers = var1;
         this.body = var2;
      }

      public static MultipartBody.Part create(Headers var0, RequestBody var1) {
         if (var1 == null) {
            throw new NullPointerException("body == null");
         } else if (var0 != null && var0.get("Content-Type") != null) {
            throw new IllegalArgumentException("Unexpected header: Content-Type");
         } else if (var0 != null && var0.get("Content-Length") != null) {
            throw new IllegalArgumentException("Unexpected header: Content-Length");
         } else {
            return new MultipartBody.Part(var0, var1);
         }
      }

      public static MultipartBody.Part create(RequestBody var0) {
         return create((Headers)null, var0);
      }

      public static MultipartBody.Part createFormData(String var0, String var1) {
         return createFormData(var0, (String)null, RequestBody.create((MediaType)null, (String)var1));
      }

      public static MultipartBody.Part createFormData(String var0, String var1, RequestBody var2) {
         if (var0 == null) {
            throw new NullPointerException("name == null");
         } else {
            StringBuilder var3 = new StringBuilder("form-data; name=");
            MultipartBody.appendQuotedString(var3, var0);
            if (var1 != null) {
               var3.append("; filename=");
               MultipartBody.appendQuotedString(var3, var1);
            }

            return create(Headers.of("Content-Disposition", var3.toString()), var2);
         }
      }

      public RequestBody body() {
         return this.body;
      }

      public Headers headers() {
         return this.headers;
      }
   }
}
