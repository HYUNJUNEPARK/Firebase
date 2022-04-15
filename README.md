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

-NoSQL 데이터베이스</br>
-테이블, 행 없음 대신 컬렉션으로 정리되는 문서에 데이터가 저장</br>
-key-value 쌍으로 데이터가 저장되며 모든 문서는 컬렉션에 저장</br>
-데이터가 문서 단위로 저장되고 문서는 컬렉션에 저장됨</br>
-Boolean 타입으로 프로퍼티명이 'is' 로 시작한다면 필드의 키에서 'is' 가 제거됨</br>
-> `@JvmField var isAdmin: Boolean`</br>
cf)Build</br>
`implementation 'com.google.firebase:firebase-firestore-ktx:21.2.1'`</br>

<br></br>
<br></br>

><a id = "content2">**2. 데이터 저장**</a></br>

**add()**</br>
-CollectionReference 객체에서 제공하므로 문서를 추가할 때 식별자가 자동으로 지정됨</br>

```
db.collection(USERS)
    .add(user)
    .addOnSuccessListener { documentReference ->
    }
    .addOnFailureListener { e ->
    }
```

**set()**</br>
-신규 데이터 뿐 아니라 기존 데이터를 변경할 때도 사용됨</br>
-DocumentReference 객체에서 제공하므로 document() 함수로 작업 대상 문서를 먼저 지정</br>
-컬렉션에 document 가 없으면 새로 만들어 데이터를 추가하고, 문서가 있으면 해당 문서 전체를 덮어씀</br>
```
db.collection(USERS)
    .document("ID01")
    .set(user)
```

<br></br>
<br></br>

><a id = "content3">**3. 데이터 불러오기**</a></br>

**get()**</br>
```
//컬렉션 내 모든 데이터 조회
Thread {
    db.collection(USERS)
        .get()
        .addOnSuccessListener { snapshot ->
            for (document in snapshot) {
                val id: String = document.id + "\n"
                sb.append(id)
            }
            runOnUiThread {
                binding.idTextView.text = sb
                binding.progressBar.visibility = View.GONE
                sb.setLength(0)
            }
        }
        .addOnFailureListener { e ->
            Log.d(TAG, "Error: $e")
            runOnUiThread {
            binding.progressBar.visibility = View.GONE
        }
    }
}.start()

//컬렉션 내 특정 다큐먼트 조회
Thread {
    val docRef = db.collection(USERS).document(searchId)
    docRef.get()
        .addOnSuccessListener { document ->
            initDeleteButton(document.id)
            initUpdateButton(document.id)

            if (document != null) {
                userObj = document.toObject(UserForUpdateDelete::class.java)
                sb.append(
                    "name : ${document.data?.get("name")} \n" +
                            "email : ${document.data?.get("email")} \n" +
                            "isAdmin : ${document.data?.get("isAdmin")}"
                )
                runOnUiThread {
                    binding.idTextView.text = sb
                    binding.searchIdTextView.text = null
                    binding.updateButton.visibility = View.VISIBLE
                    binding.deleteButton.visibility = View.VISIBLE
                    binding.updateNameTextView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                sb.setLength(0)
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Empty document", Toast.LENGTH_SHORT).show()
                }
            }
        }
        .addOnFailureListener { e ->
            runOnUiThread {
                Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
            }
        }
}.start()
```


<br></br>
<br></br>

><a id = "content4">**4. 데이터 업데이트**</a></br>

**update()**</br>
```
Thread {
    db.collection(USERS)
        .document(id)
        .update("name", userInput)
        .addOnSuccessListener {
            runOnUiThread {
                Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }

        }
        .addOnFailureListener {
            runOnUiThread {
                Toast.makeText(this, "수정 실패", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
}.start()
```


<br></br>
<br></br>

><a id = "content5">**5. 데이터 삭제**</a></br>

**delete()**</br>
```
Thread {
    db.collection(USERS)
        .document(id)
        .delete()
        .addOnSuccessListener {
            runOnUiThread {
                Toast.makeText(this, "데이터 삭제", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
        .addOnFailureListener {
            runOnUiThread {
                Toast.makeText(this, "데이터 삭제 실패", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
}.start()
```

<br></br>
<br></br>



><a id = "content6">**6. 다큐먼트 객체에 담기**</a></br>

-콜백 매개변수의 toObject() 함수를 이용하면 다큐먼트의 데이터를 자동으로 객체에 담아줌</br>
`userObj = document.toObject(UserForUpdateDelete::class.java)`</br>
-toObject() 함수에 지정하는 클래스는 매개변수가 없는 생성자가 있어야함</br>

```
class UserForUpdateDelete {
    var email: String ?= null
    var name: String ?= null
    var isAdmin: Boolean ?= null
}

Thread {
    val docRef = db.collection(USERS).document(searchId)
    docRef.get()
        .addOnSuccessListener { document ->
            initDeleteButton(document.id)
            initUpdateButton(document.id)

            if (document != null) {
                userObj = document.toObject(UserForUpdateDelete::class.java)
                sb.append(
                    "name : ${document.data?.get("name")} \n" +
                            "email : ${document.data?.get("email")} \n" +
                            "isAdmin : ${document.data?.get("isAdmin")}"
                )
                runOnUiThread {
                    binding.idTextView.text = sb
                    binding.searchIdTextView.text = null
                    binding.updateButton.visibility = View.VISIBLE
                    binding.deleteButton.visibility = View.VISIBLE
                    binding.updateNameTextView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                sb.setLength(0)
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Empty document", Toast.LENGTH_SHORT).show()
                }
            }
        }
        .addOnFailureListener { e ->
            runOnUiThread {
                Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()
            }
        }
}.start()
```


<br></br>
<br></br>
---

><a id = "ref">**참고링크**</a></br>
