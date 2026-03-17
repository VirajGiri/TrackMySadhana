# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ── Keep source file & line numbers for crash reporting ───────────────────
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ── Kotlin ────────────────────────────────────────────────────────────────
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.Metadata { public <methods>; }

# ── Kotlin Coroutines ─────────────────────────────────────────────────────
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** { volatile <fields>; }
-dontwarn kotlinx.coroutines.**

# ── Room Database ─────────────────────────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *
-keepclassmembers @androidx.room.Entity class * { *; }
-dontwarn androidx.room.paging.**

# ── App Data Entities & DAOs ──────────────────────────────────────────────
-keep class com.virajgiri.trackmysadhana.data.** { *; }

# ── Lifecycle / ViewModel ─────────────────────────────────────────────────
-keep class * extends androidx.lifecycle.ViewModel { <init>(); }
-keep class * extends androidx.lifecycle.AndroidViewModel { <init>(android.app.Application); }
-keepclassmembers class * extends androidx.lifecycle.ViewModel { <fields>; }

# ── ViewBinding ───────────────────────────────────────────────────────────
-keep class * implements androidx.viewbinding.ViewBinding {
    public static ** inflate(android.view.LayoutInflater);
    public static ** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
    public static ** bind(android.view.View);
}

# ── MPAndroidChart ────────────────────────────────────────────────────────
-keep class com.github.mikephil.charting.** { *; }
-dontwarn com.github.mikephil.charting.**

# ── Splash Screen ─────────────────────────────────────────────────────────
-keep class androidx.core.splashscreen.** { *; }

# ── AndroidX / Material ───────────────────────────────────────────────────
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# ── Suppress common harmless warnings ─────────────────────────────────────
-dontwarn org.xmlpull.v1.**
-dontwarn javax.annotation.**
-dontwarn sun.misc.Unsafe
