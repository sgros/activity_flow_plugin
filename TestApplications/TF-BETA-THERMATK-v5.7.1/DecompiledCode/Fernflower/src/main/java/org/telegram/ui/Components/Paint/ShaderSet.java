package org.telegram.ui.Components.Paint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ShaderSet {
   private static final String ATTRIBUTES = "attributes";
   private static final Map AVAILBALBE_SHADERS = createMap();
   private static final String FRAGMENT = "fragment";
   private static final String PAINT_BLITWITHMASKLIGHT_FSH = "precision highp float; varying vec2 varTexcoord; uniform sampler2D texture; uniform sampler2D mask; uniform vec4 color; void main (void) { vec4 dst = texture2D(texture, varTexcoord.st, 0.0); vec3 maskColor = texture2D(mask, varTexcoord.st, 0.0).rgb; float srcAlpha = clamp(0.78 * maskColor.r + maskColor.b + maskColor.g, 0.0, 1.0); vec3 borderColor = mix(color.rgb, vec3(1.0, 1.0, 1.0), 0.86); vec3 finalColor = mix(color.rgb, borderColor, maskColor.g); finalColor = mix(finalColor.rgb, vec3(1.0, 1.0, 1.0), maskColor.b); float outAlpha = srcAlpha + dst.a * (1.0 - srcAlpha); gl_FragColor.rgb = (finalColor * srcAlpha + dst.rgb * dst.a * (1.0 - srcAlpha)) / outAlpha; gl_FragColor.a = outAlpha; gl_FragColor.rgb *= gl_FragColor.a; }";
   private static final String PAINT_BLITWITHMASK_FSH = "precision highp float; varying vec2 varTexcoord; uniform sampler2D texture; uniform sampler2D mask; uniform vec4 color; void main (void) { vec4 dst = texture2D(texture, varTexcoord.st, 0.0); float srcAlpha = color.a * texture2D(mask, varTexcoord.st, 0.0).a; float outAlpha = srcAlpha + dst.a * (1.0 - srcAlpha); gl_FragColor.rgb = (color.rgb * srcAlpha + dst.rgb * dst.a * (1.0 - srcAlpha)) / outAlpha; gl_FragColor.a = outAlpha; gl_FragColor.rgb *= gl_FragColor.a; }";
   private static final String PAINT_BLIT_FSH = "precision highp float; varying vec2 varTexcoord; uniform sampler2D texture; void main (void) { vec4 tex = texture2D(texture, varTexcoord.st, 0.0); gl_FragColor = texture2D(texture, varTexcoord.st, 0.0); gl_FragColor.rgb *= gl_FragColor.a; }";
   private static final String PAINT_BLIT_VSH = "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; varying vec2 varTexcoord; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; }";
   private static final String PAINT_BRUSHLIGHT_FSH = "precision highp float; varying vec2 varTexcoord; varying float varIntensity; uniform sampler2D texture; void main (void) { vec4 f = texture2D(texture, varTexcoord.st, 0.0); gl_FragColor = vec4(f.r * varIntensity, f.g, f.b, 0.0); }";
   private static final String PAINT_BRUSH_FSH = "precision highp float; varying vec2 varTexcoord; varying float varIntensity; uniform sampler2D texture; void main (void) { gl_FragColor = vec4(0, 0, 0, varIntensity * texture2D(texture, varTexcoord.st, 0.0).r); }";
   private static final String PAINT_BRUSH_VSH = "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; attribute float alpha; varying vec2 varTexcoord; varying float varIntensity; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; varIntensity = alpha; }";
   private static final String PAINT_COMPOSITEWITHMASKLIGHT_FSH = "precision highp float; varying vec2 varTexcoord; uniform sampler2D mask; uniform vec4 color; void main (void) { vec3 maskColor = texture2D(mask, varTexcoord.st, 0.0).rgb; float alpha = clamp(0.78 * maskColor.r + maskColor.b + maskColor.g, 0.0, 1.0); vec3 borderColor = mix(color.rgb, vec3(1.0, 1.0, 1.0), 0.86); vec3 finalColor = mix(color.rgb, borderColor, maskColor.g); finalColor = mix(finalColor.rgb, vec3(1.0, 1.0, 1.0), maskColor.b); gl_FragColor.rgb = finalColor; gl_FragColor.a = alpha; }";
   private static final String PAINT_COMPOSITEWITHMASK_FSH = "precision highp float; varying vec2 varTexcoord; uniform sampler2D mask; uniform vec4 color; void main(void) { float alpha = color.a * texture2D(mask, varTexcoord.st, 0.0).a; gl_FragColor.rgb = color.rgb; gl_FragColor.a = alpha; }";
   private static final String PAINT_NONPREMULTIPLIEDBLIT_FSH = "precision highp float; varying vec2 varTexcoord; uniform sampler2D texture; void main (void) { gl_FragColor = texture2D(texture, varTexcoord.st, 0.0); }";
   private static final String UNIFORMS = "uniforms";
   private static final String VERTEX = "vertex";

   private static Map createMap() {
      HashMap var0 = new HashMap();
      HashMap var1 = new HashMap();
      var1.put("vertex", "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; attribute float alpha; varying vec2 varTexcoord; varying float varIntensity; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; varIntensity = alpha; }");
      var1.put("fragment", "precision highp float; varying vec2 varTexcoord; varying float varIntensity; uniform sampler2D texture; void main (void) { gl_FragColor = vec4(0, 0, 0, varIntensity * texture2D(texture, varTexcoord.st, 0.0).r); }");
      var1.put("attributes", new String[]{"inPosition", "inTexcoord", "alpha"});
      var1.put("uniforms", new String[]{"mvpMatrix", "texture"});
      var0.put("brush", Collections.unmodifiableMap(var1));
      var1 = new HashMap();
      var1.put("vertex", "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; attribute float alpha; varying vec2 varTexcoord; varying float varIntensity; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; varIntensity = alpha; }");
      var1.put("fragment", "precision highp float; varying vec2 varTexcoord; varying float varIntensity; uniform sampler2D texture; void main (void) { vec4 f = texture2D(texture, varTexcoord.st, 0.0); gl_FragColor = vec4(f.r * varIntensity, f.g, f.b, 0.0); }");
      var1.put("attributes", new String[]{"inPosition", "inTexcoord", "alpha"});
      var1.put("uniforms", new String[]{"mvpMatrix", "texture"});
      var0.put("brushLight", Collections.unmodifiableMap(var1));
      var1 = new HashMap();
      var1.put("vertex", "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; varying vec2 varTexcoord; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; }");
      var1.put("fragment", "precision highp float; varying vec2 varTexcoord; uniform sampler2D texture; void main (void) { vec4 tex = texture2D(texture, varTexcoord.st, 0.0); gl_FragColor = texture2D(texture, varTexcoord.st, 0.0); gl_FragColor.rgb *= gl_FragColor.a; }");
      var1.put("attributes", new String[]{"inPosition", "inTexcoord"});
      var1.put("uniforms", new String[]{"mvpMatrix", "texture"});
      var0.put("blit", Collections.unmodifiableMap(var1));
      var1 = new HashMap();
      var1.put("vertex", "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; varying vec2 varTexcoord; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; }");
      var1.put("fragment", "precision highp float; varying vec2 varTexcoord; uniform sampler2D texture; uniform sampler2D mask; uniform vec4 color; void main (void) { vec4 dst = texture2D(texture, varTexcoord.st, 0.0); vec3 maskColor = texture2D(mask, varTexcoord.st, 0.0).rgb; float srcAlpha = clamp(0.78 * maskColor.r + maskColor.b + maskColor.g, 0.0, 1.0); vec3 borderColor = mix(color.rgb, vec3(1.0, 1.0, 1.0), 0.86); vec3 finalColor = mix(color.rgb, borderColor, maskColor.g); finalColor = mix(finalColor.rgb, vec3(1.0, 1.0, 1.0), maskColor.b); float outAlpha = srcAlpha + dst.a * (1.0 - srcAlpha); gl_FragColor.rgb = (finalColor * srcAlpha + dst.rgb * dst.a * (1.0 - srcAlpha)) / outAlpha; gl_FragColor.a = outAlpha; gl_FragColor.rgb *= gl_FragColor.a; }");
      var1.put("attributes", new String[]{"inPosition", "inTexcoord"});
      var1.put("uniforms", new String[]{"mvpMatrix", "texture", "mask", "color"});
      var0.put("blitWithMaskLight", Collections.unmodifiableMap(var1));
      var1 = new HashMap();
      var1.put("vertex", "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; varying vec2 varTexcoord; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; }");
      var1.put("fragment", "precision highp float; varying vec2 varTexcoord; uniform sampler2D texture; uniform sampler2D mask; uniform vec4 color; void main (void) { vec4 dst = texture2D(texture, varTexcoord.st, 0.0); float srcAlpha = color.a * texture2D(mask, varTexcoord.st, 0.0).a; float outAlpha = srcAlpha + dst.a * (1.0 - srcAlpha); gl_FragColor.rgb = (color.rgb * srcAlpha + dst.rgb * dst.a * (1.0 - srcAlpha)) / outAlpha; gl_FragColor.a = outAlpha; gl_FragColor.rgb *= gl_FragColor.a; }");
      var1.put("attributes", new String[]{"inPosition", "inTexcoord"});
      var1.put("uniforms", new String[]{"mvpMatrix", "texture", "mask", "color"});
      var0.put("blitWithMask", Collections.unmodifiableMap(var1));
      var1 = new HashMap();
      var1.put("vertex", "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; varying vec2 varTexcoord; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; }");
      var1.put("fragment", "precision highp float; varying vec2 varTexcoord; uniform sampler2D mask; uniform vec4 color; void main(void) { float alpha = color.a * texture2D(mask, varTexcoord.st, 0.0).a; gl_FragColor.rgb = color.rgb; gl_FragColor.a = alpha; }");
      var1.put("attributes", new String[]{"inPosition", "inTexcoord"});
      var1.put("uniforms", new String[]{"mvpMatrix", "mask", "color"});
      var0.put("compositeWithMask", Collections.unmodifiableMap(var1));
      var1 = new HashMap();
      var1.put("vertex", "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; varying vec2 varTexcoord; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; }");
      var1.put("fragment", "precision highp float; varying vec2 varTexcoord; uniform sampler2D mask; uniform vec4 color; void main (void) { vec3 maskColor = texture2D(mask, varTexcoord.st, 0.0).rgb; float alpha = clamp(0.78 * maskColor.r + maskColor.b + maskColor.g, 0.0, 1.0); vec3 borderColor = mix(color.rgb, vec3(1.0, 1.0, 1.0), 0.86); vec3 finalColor = mix(color.rgb, borderColor, maskColor.g); finalColor = mix(finalColor.rgb, vec3(1.0, 1.0, 1.0), maskColor.b); gl_FragColor.rgb = finalColor; gl_FragColor.a = alpha; }");
      var1.put("attributes", new String[]{"inPosition", "inTexcoord"});
      var1.put("uniforms", new String[]{"mvpMatrix", "texture", "mask", "color"});
      var0.put("compositeWithMaskLight", Collections.unmodifiableMap(var1));
      var1 = new HashMap();
      var1.put("vertex", "precision highp float; uniform mat4 mvpMatrix; attribute vec4 inPosition; attribute vec2 inTexcoord; varying vec2 varTexcoord; void main (void) { gl_Position = mvpMatrix * inPosition; varTexcoord = inTexcoord; }");
      var1.put("fragment", "precision highp float; varying vec2 varTexcoord; uniform sampler2D texture; void main (void) { gl_FragColor = texture2D(texture, varTexcoord.st, 0.0); }");
      var1.put("attributes", new String[]{"inPosition", "inTexcoord"});
      var1.put("uniforms", new String[]{"mvpMatrix", "texture"});
      var0.put("nonPremultipliedBlit", Collections.unmodifiableMap(var1));
      return Collections.unmodifiableMap(var0);
   }

   public static Map setup() {
      HashMap var0 = new HashMap();
      Iterator var1 = AVAILBALBE_SHADERS.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         Map var3 = (Map)var2.getValue();
         Shader var4 = new Shader((String)var3.get("vertex"), (String)var3.get("fragment"), (String[])var3.get("attributes"), (String[])var3.get("uniforms"));
         var0.put(var2.getKey(), var4);
      }

      return Collections.unmodifiableMap(var0);
   }
}
