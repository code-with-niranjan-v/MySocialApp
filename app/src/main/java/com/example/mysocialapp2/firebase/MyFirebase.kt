package com.example.mysocialapp2.firebase

import android.net.Uri
import android.util.Log
import com.example.mysocialapp2.data.Post
import com.example.mysocialapp2.data.User
import com.example.mysocialapp2.paths.Post_Media_Url
import com.example.mysocialapp2.paths.Post_Url
import com.example.mysocialapp2.paths.Profile_Url
import com.example.mysocialapp2.paths.Users_Url
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class MyFirebase {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()



    suspend fun signUp(password:String,user: User,switchActivity: () -> Unit){

        firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener {
            if (it.isSuccessful){
                val userData = User(uid = firebaseAuth.currentUser?.uid.toString(), email = user.email, userName = user.userName, about = user.about)
                saveUser(userData)
                switchActivity()
            }
        }


    }

    fun saveUser(user: User){

        firebaseStore.collection(Users_Url).document(user.uid).set(user).addOnSuccessListener {

        }.addOnFailureListener {
            Log.e("Error-1","${it.toString()}")
        }

    }

    suspend fun signIn(email:String,password: String,switchActivity: () -> Unit){

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                switchActivity()

            }
        }.addOnFailureListener {
            Log.e("Error-1","${it.toString()}")
        }

    }

    suspend fun storePost(uri: Uri,post: Post,user: User){
        val ref = firebaseStorage.reference.child(Post_Media_Url)
        ref.child(post.uid).putFile(uri).addOnSuccessListener {
            ref.child(post.uid).downloadUrl.addOnSuccessListener {
                val postData = Post(
                    uid = post.uid,
                    url = it.toString(), description = post.description, timeStamp = System.currentTimeMillis(), userName = user.userName, userUid = user.uid, userProfilePic = user.profile  )
                savePost(postData,user)
            }

        }
    }

    fun savePost(post: Post,user: User){
        firebaseStore.collection(Post_Url).document(post.uid).set(post).addOnSuccessListener {
            user.post.add(post.uid)
            val userData = User(user.uid,user.userName,user.email,user.about,user.profile,user.followers,user.following,user.post,)
            saveUser(userData)
        }
    }

    suspend fun getCurrentUser():User?{
        var user:User? = User()
        firebaseStore.collection(Users_Url).document(firebaseAuth.currentUser?.uid.toString()).get().addOnSuccessListener {
            val data = it.toObject(User::class.java)
            user = data
        }.await()

        return user
    }

    suspend fun loadPostImg(postUIDs:List<String>):List<String>{
        var uri = mutableListOf<String>()


        for (postUid in postUIDs){
            firebaseStorage.reference.child(Post_Media_Url).child(postUid).downloadUrl.addOnSuccessListener {
                uri.add(it.toString())
            }.await()

        }

        return uri
    }

    suspend fun loadAllPost():List<Post>{
        val listOfPost = mutableListOf<Post>()
        firebaseStore.collection(Post_Url).get().addOnSuccessListener {
            for(post in it){
                listOfPost.add(post.toObject(Post::class.java))
            }
        }.await()
        return listOfPost
    }

    suspend fun storeProfile(uri: Uri,user: User){
        firebaseStorage.reference.child(Profile_Url).child(user.uid).putFile(uri).addOnSuccessListener {
            firebaseStorage.reference.child(Profile_Url).child(user.uid).downloadUrl.addOnSuccessListener{
                val userData = User(user.uid,user.userName,user.email,user.about,it.toString(),user.followers,user.following,user.post,)
                saveUser(userData)
            }

        }
    }

    suspend fun onLike(post: Post,user: User){
        if(post.likes != null) {
            if (!post.likes!!.contains(firebaseAuth.currentUser?.uid.toString())) {
                post.likes.add(firebaseAuth.currentUser?.uid.toString())

                val postData = Post(
                    uid = post.uid,
                    url = post.url,
                    description = post.description,
                    timeStamp = post.timeStamp,
                    userName = user.userName,
                    userUid = user.uid,
                    userProfilePic = user.profile,
                    likes = post.likes
                )
                updatePost(postData)
            }
        }

    }

    suspend fun updatePost(post: Post){
        firebaseStore.collection(Post_Url).document(post.uid).set(post)
    }

    suspend fun loadCurrentPost(uid:String):Post{
        var postData = Post()
        firebaseStore.collection(Post_Url).document(uid).get().addOnSuccessListener {
            postData = it.toObject(Post::class.java)!!
        }.await()
        return postData
    }

    suspend fun loadAllUser():List<User>{
        val listOfUser = mutableListOf<User>()
        firebaseStore.collection(Users_Url).get().addOnSuccessListener {
            for (snapShot in it){
                val user = snapShot.toObject(User::class.java)
                listOfUser.add(user)
            }
        }.await()

        return listOfUser
    }

    fun addToFollowers(user:User,followingUser: User){
        val followers:MutableList<String> = user.followers
        val following = user.following
        if (!followers.contains(firebaseAuth.currentUser?.uid)){
//          followers.add(firebaseAuth.currentUser?.uid.toString())
            followingUser.following.add(user.uid)
            user.followers.add(firebaseAuth.currentUser?.uid.toString())
            saveUser(user)
            saveUser(followingUser)
            //val user = User(user.uid,user.userName,user.email,user.about,user.profile,followers,user.following)
        }
    }


}