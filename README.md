# Firestore

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/androidProgramming/firestore1.png" height="400"/>

<img src="https://github.com/HYUNJUNEPARK/ImageRepository/blob/master/androidProgramming/firestore2.png" height="400"/>

---
1. <a href = "#content1">파이어스토어 데이터 모델</a></br>
2. <a href = "#content2">데이터 저장</a></br>
3. <a href = "#content3">데이터 불러오기</a></br>
4. <a href = "#content4">데이터 업데이트</a></br>
5. <a href = "#content5">데이터 삭제</a></br>
6. <a href = "#content6">다큐먼트 객체에 담기</a></br>
* <a href = "#ref">참고링크</a>
---
><a id = "content1">**1. 파이어스토어 데이터 모델**</a></br>



파이어베이스 스토리지
-앱의 파일 저장하는 기능
-사진을 선택하고 서버에 올린 후 다시 특정 시점에서 내려받을 수 있도록 할 수 있음

사용설정
`implementation 'com.google.firebase:firebase-storage-ktx'`


putBytes()
-바이트 배열을 스토리지에 저장할 때 사용 ex)뷰의 화면을 바이트로 읽어서 저장하는 경우

```kotlin
//스토리지 객체 얻기
val storage: FirebaseStorage = Firebase.storage
val storageRef: StorageReference = storage.reference
val imgRef: StorageReference = storageRef.child("images/a.jpg")

//이미지를 바이트 값으로 읽기
val bitmap = getBitmapFromView(binding.sampleImage)
val baos = ByteArrayOutputStream()
bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
val data = baos.toByteArray()

//바이트값을 스토리지에 저장하기
var uploadTask = imgRef.putBytes(data)
uploadTask
    .addOnCompleteListener { taskSnapshot -> }
    .addOnFailureListener { }

//화면을 비트맵 객체에 그리는 함수
fun getBitmapFromView(view: View) : Bitmap? {
    var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    var canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

```


<br></br>
<br></br>

><a id = "content2">**2. 데이터 저장**</a></br>


<br></br>
<br></br>

><a id = "content3">**3. 데이터 불러오기**</a></br>



<br></br>
<br></br>

><a id = "content4">**4. 데이터 업데이트**</a></br>



<br></br>
<br></br>

><a id = "content5">**5. 데이터 삭제**</a></br>


<br></br>
<br></br>



><a id = "content6">**6. 다큐먼트 객체에 담기**</a></br>



<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>
