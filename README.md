1.SHA1 지문 얻기
-앱 배포 시 인증하는 용도로 사용하는 암호화된 디지털 지문
-테스트용 : 디버그용 키를 이용해 SHA1 지문을 생성
-정식 배포용 : 명령 프롬프트에서 앱을 서명한 키로 keytool 명령을 실행해 SHA1 지문 생성
`keytool -list -keystore test.jks - alias key() -storepass ##### -keypass #####`
test.jks : 키 파일명
key() : 키 이름
##### : 키 파일 비밀번호, 키 비밀번호

2.Build
`implementation 'com.google.firebase:firebase-auth-ktx'`

`implementation 'com.google.android.gms:play-service-auth:18.1.0'`
-플레이 서비스 인증 라이브러리
-구글 인증은 AOS 에 설치된 구글 인증 앱과 연동하여 처리함

`implementation platform('com.google.firebase:firebase-bom:29.3.0')`
-BoM(bill of materials) 를 사용하면 BoM 버전에 매핑된 개별 라이브러리 버전을 가져옴
-다른 파이어베이스 라이브러리를 등록할 때 버전을 지정하지 않아도 됨



3.MultiDexApplication()
-앱 전역에서 파이어베이스 인증 객체를 이용하고자 Application 을 상속받은 클래스 작성 후 패니페스트에 등록
```
<application
    android:name=".MyApplication"
</application>
```

-To solve Cannot fit requested classes in a single dex file
```
android {
    defaultConfig {
        multiDexEnabled true
    }
}

dependencies {
    implementation 'androidx.multidex:multidex:2.0.1'
}
```



//
Android Studio 에서 디버그 서명 인증서 SHA-1 확인하는법
https://singo112ok.tistory.com/49

Android - MultiDex 적용하는 방법
https://codechacha.com/ko/androidx-multidex/