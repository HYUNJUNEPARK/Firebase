# Firebase Auth

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/androidProgramming/FirebaseAuth.jpg" height="400"/>

---
1. <a href = "#content1">SHA1 지문 얻기</a></br>
2. <a href = "#content2">Build</a></br>
3. <a href = "#content3">MultiDexApplication()</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1.SHA1 지문 얻기**</a></br>



-앱 배포 시 인증하는 용도로 사용하는 암호화된 디지털 지문</br>
-테스트용 : 디버그용 키를 이용해 SHA1 지문을 생성</br>
-정식 배포용 : 명령 프롬프트에서 앱을 서명한 키로 keytool 명령을 실행해 SHA1 지문 생성</br>
`keytool -list -keystore test.jks - alias key() -storepass ##### -keypass #####`</br>
test.jks : 키 파일명</br>
key() : 키 이름</br>
`#####` : 키 파일 비밀번호, 키 비밀번호</br>


<br></br>
<br></br>

><a id = "content2">**2. Build**</a></br>


`implementation 'com.google.firebase:firebase-auth-ktx'`</br>

`implementation 'com.google.android.gms:play-service-auth:18.1.0'`</br>
-플레이 서비스 인증 라이브러리</br>
-구글 인증은 AOS 에 설치된 구글 인증 앱과 연동하여 처리함</br>

`implementation platform('com.google.firebase:firebase-bom:29.3.0')`</br>
-BoM(bill of materials) 를 사용하면 BoM 버전에 매핑된 개별 라이브러리 버전을 가져옴</br>
-다른 파이어베이스 라이브러리를 등록할 때 버전을 지정하지 않아도 됨</br>


<br></br>
<br></br>

><a id = "content3">**3. MultiDexApplication()**</a></br>


-메서드가 65536 개를 넘을때 사용</br>
-앱 전역에서 파이어베이스 인증 객체를 이용하고자 Application 을 상속받은 클래스 작성 후 패니페스트에 등록</br>
```
<application
    android:name=".MyApplication"
</application>
```

-To solve Cannot fit requested classes in a single dex file</br>
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

<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>

Android Studio 에서 디버그 서명 인증서 SHA-1 확인하는법</br>
https://singo112ok.tistory.com/49</br>

Android - MultiDex 적용하는 방법</br>
https://codechacha.com/ko/androidx-multidex/</br>

메서드가 64K개를 초과하는 앱에 관해 멀티덱스 사용 설정</br>
https://developer.android.com/studio/build/multidex.html</br>

Google 로그인 에러(com.google.Android.gms.common.api.ApiException : 10)</br>
https://acver.tistory.com/entry/Google%EB%A1%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%97%90%EB%9F%ACcomgoogleAndroidgmscommonapiApiException-10</br>

default_web_client_id</br>
https://bada744.tistory.com/m/161</br>

Firebase 에서 사용자 관리하기</br>
https://firebase.google.com/docs/auth/android/manage-users?hl=ko</br>

Dialog 팝업창 종류 및 커스텀 Dialog 팝업창</br>
https://mixup.tistory.com/36</br>