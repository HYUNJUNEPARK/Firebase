# Firestore Storage

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/androidProgramming/firebaseStorage.png" height="400"/>

---
1. <a href = "#content1">파이어베이스 스토리지</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. 파이어베이스 스토리지**</a></br>


파이어베이스 스토리지</br>
-앱의 파일 저장하는 기능</br>
-사진을 선택하고 서버에 올린 후 다시 특정 시점에서 내려받을 수 있도록 할 수 있음</br>
-해당 샘플앱은 Store 에 저장되는 document 의 id 를 업로드 이미지의 이름으로 하여 Storage 에 저장</br>
-이미지 파일의 경로를 uri 로 바꿔 `putFile()` 에 전달해 Storage 업로드</br>

사용설정</br>
`implementation 'com.google.firebase:firebase-storage-ktx'`</br>


<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>
