package okhttp3.internal.tls;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

public final class OkHostnameVerifier implements HostnameVerifier {
   private static final int ALT_DNS_NAME = 2;
   private static final int ALT_IPA_NAME = 7;
   public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();

   private OkHostnameVerifier() {
   }

   public static List allSubjectAltNames(X509Certificate var0) {
      List var1 = getSubjectAltNames(var0, 7);
      List var2 = getSubjectAltNames(var0, 2);
      ArrayList var3 = new ArrayList(var1.size() + var2.size());
      var3.addAll(var1);
      var3.addAll(var2);
      return var3;
   }

   private static List getSubjectAltNames(X509Certificate var0, int var1) {
      ArrayList var2 = new ArrayList();

      Object var13;
      label69: {
         boolean var10001;
         Collection var12;
         try {
            var12 = var0.getSubjectAlternativeNames();
         } catch (CertificateParsingException var11) {
            var10001 = false;
            break label69;
         }

         if (var12 == null) {
            try {
               var13 = Collections.emptyList();
               return (List)var13;
            } catch (CertificateParsingException var5) {
               var10001 = false;
            }
         } else {
            label65: {
               Iterator var3;
               try {
                  var3 = var12.iterator();
               } catch (CertificateParsingException var10) {
                  var10001 = false;
                  break label65;
               }

               while(true) {
                  var13 = var2;

                  List var14;
                  try {
                     if (!var3.hasNext()) {
                        return (List)var13;
                     }

                     var14 = (List)var3.next();
                  } catch (CertificateParsingException var9) {
                     var10001 = false;
                     break;
                  }

                  if (var14 != null) {
                     Integer var4;
                     try {
                        if (var14.size() < 2) {
                           continue;
                        }

                        var4 = (Integer)var14.get(0);
                     } catch (CertificateParsingException var8) {
                        var10001 = false;
                        break;
                     }

                     if (var4 != null) {
                        String var15;
                        try {
                           if (var4 != var1) {
                              continue;
                           }

                           var15 = (String)var14.get(1);
                        } catch (CertificateParsingException var7) {
                           var10001 = false;
                           break;
                        }

                        if (var15 != null) {
                           try {
                              var2.add(var15);
                           } catch (CertificateParsingException var6) {
                              var10001 = false;
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      var13 = Collections.emptyList();
      return (List)var13;
   }

   private boolean verifyHostname(String var1, String var2) {
      boolean var3 = false;
      boolean var4 = var3;
      if (var1 != null) {
         var4 = var3;
         if (var1.length() != 0) {
            var4 = var3;
            if (!var1.startsWith(".")) {
               if (var1.endsWith("..")) {
                  var4 = var3;
               } else {
                  var4 = var3;
                  if (var2 != null) {
                     var4 = var3;
                     if (var2.length() != 0) {
                        var4 = var3;
                        if (!var2.startsWith(".")) {
                           var4 = var3;
                           if (!var2.endsWith("..")) {
                              String var5 = var1;
                              if (!var1.endsWith(".")) {
                                 var5 = var1 + '.';
                              }

                              var1 = var2;
                              if (!var2.endsWith(".")) {
                                 var1 = var2 + '.';
                              }

                              var1 = var1.toLowerCase(Locale.US);
                              if (!var1.contains("*")) {
                                 var4 = var5.equals(var1);
                              } else {
                                 var4 = var3;
                                 if (var1.startsWith("*.")) {
                                    var4 = var3;
                                    if (var1.indexOf(42, 1) == -1) {
                                       var4 = var3;
                                       if (var5.length() >= var1.length()) {
                                          var4 = var3;
                                          if (!"*.".equals(var1)) {
                                             var1 = var1.substring(1);
                                             var4 = var3;
                                             if (var5.endsWith(var1)) {
                                                int var6 = var5.length() - var1.length();
                                                if (var6 > 0) {
                                                   var4 = var3;
                                                   if (var5.lastIndexOf(46, var6 - 1) != -1) {
                                                      return var4;
                                                   }
                                                }

                                                var4 = true;
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var4;
   }

   private boolean verifyHostname(String var1, X509Certificate var2) {
      var1 = var1.toLowerCase(Locale.US);
      boolean var3 = false;
      List var4 = getSubjectAltNames(var2, 2);
      int var5 = 0;
      int var6 = var4.size();

      boolean var7;
      while(true) {
         if (var5 >= var6) {
            if (!var3) {
               String var8 = (new DistinguishedNameParser(var2.getSubjectX500Principal())).findMostSpecific("cn");
               if (var8 != null) {
                  var7 = this.verifyHostname(var1, var8);
                  break;
               }
            }

            var7 = false;
            break;
         }

         var3 = true;
         if (this.verifyHostname(var1, (String)var4.get(var5))) {
            var7 = true;
            break;
         }

         ++var5;
      }

      return var7;
   }

   private boolean verifyIpAddress(String var1, X509Certificate var2) {
      List var6 = getSubjectAltNames(var2, 7);
      int var3 = 0;
      int var4 = var6.size();

      boolean var5;
      while(true) {
         if (var3 >= var4) {
            var5 = false;
            break;
         }

         if (var1.equalsIgnoreCase((String)var6.get(var3))) {
            var5 = true;
            break;
         }

         ++var3;
      }

      return var5;
   }

   public boolean verify(String var1, X509Certificate var2) {
      boolean var3;
      if (Util.verifyAsIpAddress(var1)) {
         var3 = this.verifyIpAddress(var1, var2);
      } else {
         var3 = this.verifyHostname(var1, var2);
      }

      return var3;
   }

   public boolean verify(String var1, SSLSession var2) {
      boolean var3;
      try {
         var3 = this.verify(var1, (X509Certificate)var2.getPeerCertificates()[0]);
      } catch (SSLException var4) {
         var3 = false;
      }

      return var3;
   }
}
