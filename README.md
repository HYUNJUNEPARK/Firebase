SHA1 지문 얻기
-앱 배포 시 인증하는 용도로 사용하는 암호화된 디지털 지문
-테스트용 : 디버그용 키를 이용해 SHA1 지문을 생성
-정식 배포용 : 명령 프롬프트에서 앱을 서명한 키로 keytool 명령을 실행해 SHA1 지문 생성
`keytool -list -keystore test.jks - alias key() -storepass ##### -keypass #####`
test.jks : 키 파일명
key() : 키 이름
##### : 키 파일 비밀번호, 키 비밀번호







///
Android Studio 에서 디버그 서명 인증서 SHA-1 확인하는법
https://singo112ok.tistory.com/49