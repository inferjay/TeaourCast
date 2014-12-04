ATeahour.fm
===========

An Teahour.fm Android Client

Teahour.fm 由 Terry Tai，Dingding Ye, Daniel Lv, Kevin Wang 和 Xdite Cheng 主持，会专注程序员感兴趣的话题，包括 Web 设计和开发，移动应用设计和开发，创业以及一切 Geek 的话题。

# Screen Shots

<img width="360"src="https://raw.githubusercontent.com/inferjay/ATeahour.fm/master/art/Screenshot1.png"/>
<img width="360"src="https://raw.githubusercontent.com/inferjay/ATeahour.fm/master/art/Screenshot2.png"/>
<img width="360"src="https://raw.githubusercontent.com/inferjay/ATeahour.fm/master/art/Screenshot3.png"/>
<img width="360"src="https://raw.githubusercontent.com/inferjay/ATeahour.fm/master/art/Screenshot4.png"/>

# Build
1. Install `Android Studio 0.14+`、`gradle 2.0+`、`JDK 1.6+` and `Android SDK Tools` last version.

2. Clone Project and Import Project

	first clone project

		git clone https://github.com/inferjay/ATeahour.fm.git

	then	
	
		File -> Import Project ...

3. Modify `Teahour.fm/build.gradle` file 

	```	
	//Apk签名配置
	signingConfigs {
    	release {
       		keyAlias 'Your keyAlias'
       		keyPassword 'Your keyPassword'
       		storeFile file('Your keystore file')
       		storePassword 'Your storePassword'
     	}
	}
	```

4. Build Project 
	
	`Run -> Run｀Teahour.fm｀`
	
	or Run on the command line
	
	`./gradlew assembleDebug` (Mac/Linux)

	`gradlew.bat assembleDebug` (Windows)

>Android Studio、gradle、Android SDK Tools 土啬内 Download Site: [http://www.androiddevtools.cn](http://www.androiddevtools.cn)

# TODO
1. Refresh Podcasts List Data  
2. Podcasts List Data Cache  
3. Download podcasts Audio File  
4. Offline listening to podcast  
5. Playback controls  
6. Timing play  

# UI File

[Teahour.FM UI(Android).sketch](https://github.com/inferjay/CoderLearningDesign/tree/master/Teahour.fm)	

# Thanks

[Volley](https://android.googlesource.com/platform/frameworks/volley)  
[Picasso](https://github.com/square/picasso)  
[Butterknife](https://github.com/JakeWharton/butterknife)  
[LoadingEverywhere](https://github.com/lsjwzh/loadingeverywhere)  

# License

```
Copyright 2014 HC ZHANG http://www.inferjay.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```