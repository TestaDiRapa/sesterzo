# R8 keep rules for the release build.
#
# Most dependencies (Compose, Ktor, OkHttp, SQLDelight, AndroidX, DataStore) ship their
# own consumer rules, so only the things R8 cannot see through are listed here: code
# reached by reflection (kotlinx.serialization, BouncyCastle's JCE providers) and code
# reached from native code (the zxing-cpp JNI bridge).

# --- kotlinx.serialization ------------------------------------------------------------
# Serializers are looked up reflectively via the Companion / serializer() members, so the
# generated $$serializer classes and those members must survive shrinking and renaming.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault,InnerClasses,Signature

-keepclassmembers class kotlinx.serialization.json.** {
	*** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
	kotlinx.serialization.KSerializer serializer(...);
}

# Our own @Serializable models, plus those coming from the iCure libraries (kryptom and
# kerberus both serialize their own payload types).
-keep,includedescriptorclasses class org.testadirapa.sesterzo.**$$serializer { *; }
-keepclassmembers class org.testadirapa.sesterzo.** {
	*** Companion;
}
-keepclasseswithmembers class org.testadirapa.sesterzo.** {
	kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.icure.**$$serializer { *; }
-keepclassmembers class com.icure.** {
	*** Companion;
}
-keepclasseswithmembers class com.icure.** {
	kotlinx.serialization.KSerializer serializer(...);
}

# --- JNI ------------------------------------------------------------------------------
# Anything native code binds to by name. zxingcpp's BarcodeReader is instantiated and
# read back from C++, so its members cannot be renamed or stripped.
-keepclasseswithmembernames,includedescriptorclasses class * {
	native <methods>;
}
-keep class zxingcpp.** { *; }

# --- BouncyCastle ---------------------------------------------------------------------
# Pulled in by kryptom. The JCE/JCA providers are resolved by reflection from algorithm
# name strings, so the provider packages have to be kept whole.
-keep class org.bouncycastle.jcajce.provider.** { *; }
-keep class org.bouncycastle.jce.provider.** { *; }
-dontwarn org.bouncycastle.**
-dontwarn javax.naming.**

# --- Misc -----------------------------------------------------------------------------
# Ktor and SLF4J reference optional backends that are absent on Android.
-dontwarn org.slf4j.**
-dontwarn java.lang.management.**
