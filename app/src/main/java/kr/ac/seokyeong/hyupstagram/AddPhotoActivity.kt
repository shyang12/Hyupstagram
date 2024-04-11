package kr.ac.seokyeong.hyupstagram

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kr.ac.seokyeong.hyupstagram.databinding.ActivityAddPhotoBinding
import kr.ac.seokyeong.hyupstagram.model.ContentModel
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    var photoUri : Uri? = null
    var photoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == RESULT_OK) {
            photoUri = it.data?.data
            binding.uploadImageview.setImageURI(photoUri)
        }
    }

    var storage = FirebaseStorage.getInstance()
    var auth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()
    lateinit var binding : ActivityAddPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_photo)
        binding.addphotoUploadBtn.setOnClickListener {
            contentUpload()
        }

        var i = Intent(Intent.ACTION_PICK)
        i.type = "image/*"
        photoResult.launch(i)
    }

    fun contentUpload() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_ " + timestamp + ".png"
        // images/IMAGE_234234_.png의 경로

        // https://wewewdfd와 같이 바꿔주는 과정
        var storagePath = storage.reference.child("images").child(imageFileName)

        storagePath.putFile(photoUri!!).continueWithTask {
            return@continueWithTask storagePath.downloadUrl
        }.addOnCompleteListener { downloadUrl ->
            var contentModel = ContentModel()
                contentModel.imageUrl = downloadUrl.result.toString()
                contentModel.explain = binding.addphotoEditEdittext.text.toString()
                contentModel.uid = auth.uid
                contentModel.userId = auth.currentUser?.email
                contentModel.timestamp = System.currentTimeMillis()

            firestore.collection("images").document().set(contentModel).addOnSuccessListener {
                Toast.makeText(this, "업로드 성공", Toast.LENGTH_LONG).show()
                finish()
            }

            Toast.makeText(this, "업로드 성공 : ${downloadUrl.result}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}