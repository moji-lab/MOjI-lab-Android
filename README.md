# MOJI_Android
사용자들에게 가장 트렌디한 여행지를 보여주는 SNS기반의 클라우드 App
 - MOJI


<br/><br/>

# Moji
> - 2019년 스마트 관광앱 공모전 모지 팀 'MOJI'
> - 프로젝트 기간 2019.07.19 ~ 진행중

![SDK Version](https://img.shields.io/badge/SDK-28-lightgray.svg) ![Kotlin Version](https://img.shields.io/badge/Kotlin-1.3.31-orange.svg)




# Dependency

```
implementation fileTree(dir: 'libs', include: ['*.jar'])
implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
implementation 'androidx.appcompat:appcompat:1.0.0'
implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
testImplementation 'junit:junit:4.12'
androidTestImplementation 'androidx.test:runner:1.1.0'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.1.0'
implementation "androidx.lifecycle:lifecycle-extensions:2.0.0"
implementation "androidx.lifecycle:lifecycle-viewmodel:2.0.0"

RxJava 라이브러리 적용
implementation "io.reactivex.rxjava2:rxjava:2.2.6"
implementation "io.reactivex.rxjava2:rxandroid:2.1.1"

// RecyclerView 라이브러리 적용
implementation 'androidx.recyclerview:recyclerview:1.0.0'

// Retrofit 통신 라이브러리 적용
implementation 'com.squareup.retrofit2:retrofit:2.5.0'
implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
implementation "com.squareup.retrofit2:adapter-rxjava2:2.5.0"
implementation "com.squareup.retrofit2:retrofit-mock:2.5.0"

 // Glide 라이브러리 적용
 implementation 'com.github.bumptech.glide:glide:4.9.0'
 // CardView 라이브러리 적용
 implementation 'androidx.cardview:cardview:1.0.0'

  // Room
  implementation 'androidx.room:room-runtime:2.1.0'
  kapt 'androidx.room:room-compiler:2.1.0'

  // LiveData
  implementation 'androidx.lifecycle:lifecycle-extensions:2.0.0'
  kapt 'androidx.lifecycle:lifecycle-compiler:2.0.0'


```

## 개발자

- **송제민** - [SongJemin](https://github.com/SongJemin) 
- **김무현** - [KimMooHyeon](https://github.com/KimMooHyeon) 
- **양승희** - [seunghee63](https://github.com/seunghee63) 

[기여자 목록](https://github.com/moji-lab/MOjI-lab-Android/graphs/contributors)을 확인하여 이 프로젝트에 참가하신 분들을 보실 수 있습니다.

## moji의 다른 프로젝트

- [SERVER](https://github.com/moji-lab/MOjI-lab-Core-Server)
