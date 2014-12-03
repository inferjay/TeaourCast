# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/yugy/Documents/android sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keep public class com.inferjay.teahour.fm.android.R$*{
    public static final int *;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.umeng.fb.ui.ThreadView {
}

# 以下类过滤不混淆
-keep public class * extends com.umeng.**
# 以下包不进行过滤
-keep class com.umeng.** { *; }
-dontwarn com.umeng.**

-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keep class javax.lang.model.element.** { *; }
-keep class javax.annotation.processing..** { *; }
-keepnames class * { @butterknife.InjectView *;}
-keep class com.squareup.picasso.** { *; }
-dontwarn com.squareup.**

-keep class com.sharesdk.** { *; }
-dontwarn com.sharesdk.**

## support-v4
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment