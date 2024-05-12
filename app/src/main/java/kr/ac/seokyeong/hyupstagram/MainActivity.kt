package kr.ac.seokyeong.hyupstagram


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kr.ac.seokyeong.hyupstagram.databinding.ActivityMainBinding
import kr.ac.seokyeong.hyupstagram.fragment.AlarmFragment
import kr.ac.seokyeong.hyupstagram.fragment.DetailViewFragment
import kr.ac.seokyeong.hyupstagram.fragment.GridFragment
import kr.ac.seokyeong.hyupstagram.fragment.UserFragment
import kr.ac.seokyeong.hyupstagram.util.FcmPush

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)

        val intent = Intent(this, ChatMainActivity::class.java)
        binding.dmBtn.setOnClickListener { startActivity(intent) }

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            setToolbarDefault()
            when(it.itemId) {
                R.id.action_home -> {
                    var f = DetailViewFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, f).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_search -> {
                    var f = GridFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, f).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_add_photo -> {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        var i = Intent(this, AddPhotoActivity::class.java)
                        startActivity(i)
                    }

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_favorite_alarm -> {
                    var f = AlarmFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, f).commit()

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_account -> {
                    var f = UserFragment()
                    var bundle = Bundle()
                    var uid = FirebaseAuth.getInstance().currentUser?.uid
                    bundle.putString("destinationUid", uid)
                    f.arguments = bundle

                    supportFragmentManager.beginTransaction().replace(R.id.main_content, f).commit()

                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
        registerPushToken()
    }
    fun setToolbarDefault(){
        binding.toolbarUsername.visibility = View.GONE
        binding.toolbarBtnBack.visibility = View.GONE
        binding.toolbarLogo.visibility = View.VISIBLE
    }

    fun registerPushToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            task ->
                if(task.isSuccessful) {
                    val token = task.result?:""
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    val map = mutableMapOf<String,Any>()
                    map["pushToken"] = token!!

                    FirebaseFirestore.getInstance().collection("pushtokens").document(uid!!).set(map)
                }
        }
    }

     /*override fun onStop() {
        super.onStop()
        FcmPush.instance.sendMessage("pS14mwEQfuMxllFuZb8XUHLQO2z2", "hi", "bye")
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == UserFragment.PICK_PROFILE_FROM_ALBUM && resultCode == RESULT_OK) {
            var imageUri = data?.data
            var uid = FirebaseAuth.getInstance().currentUser?.uid
            var storageRef = FirebaseStorage.getInstance().reference.child("userProfileImages").child(uid!!)
            storageRef.putFile(imageUri!!).continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener { uri ->
                var map = HashMap<String, Any>()
                map["image"] = uri.toString()
                FirebaseFirestore.getInstance().collection("profileImages").document(uid).set(map)
            }
        }
    }
}