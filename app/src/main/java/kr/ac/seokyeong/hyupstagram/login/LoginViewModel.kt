package kr.ac.seokyeong.hyupstagram.login

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kr.ac.seokyeong.hyupstagram.R
import kr.ac.seokyeong.hyupstagram.model.User

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var auth = FirebaseAuth.getInstance()
    private lateinit var dbref: DatabaseReference
    var id : MutableLiveData<String> = MutableLiveData("hyup")
    var password : MutableLiveData<String> = MutableLiveData("")

    var showInputNumberActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    var showFindIdActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    var showMainActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val context = getApplication<Application>().applicationContext

    // 데이터베이스 초기화


    var googleSignInClient : GoogleSignInClient

    init {

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context,gso)

    }

    fun loginWithSignupEmail(){
        dbref = Firebase.database.reference
        println("Email")
        auth.createUserWithEmailAndPassword(id.value.toString(),password.value.toString()).addOnCompleteListener {
            if(it.isSuccessful){
                showInputNumberActivity.value = true
                addUserToDatabase(id.value.toString(), auth.currentUser?.uid!!)
            }else{
                //아이디가 있을경우
                loginEmail()
            }
        }
    }

    fun loginEmail(){
        auth.signInWithEmailAndPassword(id.value.toString(),password.value.toString()).addOnCompleteListener {
            if(it.isSuccessful) {
                showMainActivity.value = true
            }else{
                showInputNumberActivity.value = true
                }

        }
    }

    fun loginGoogle(view : View){
        var i = googleSignInClient.signInIntent
        (view.context as? LoginActivity)?.googleLoginResult?.launch(i)
    }

    fun firebaseAuthWithGoogle(idToken : String?){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.result.user?.isEmailVerified == true){
                showMainActivity.value = true

            } else{
                showInputNumberActivity.value = true
            }
        }
    }

    fun firebaseAuthWithFacbook(accessToken: AccessToken){
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful) {
                if(it.result.user?.isEmailVerified == true){
                    showMainActivity.value = true
                    println(it.result.user)
                } else{
                    showInputNumberActivity.value = true
                }
            }
        }
    }

    private fun addUserToDatabase(email: String, uId: String){
        dbref = Firebase.database.reference
        dbref.child("user").child(uId).setValue(User(email, uId))
    }
 }
