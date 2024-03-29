package org.malucky.itinerary

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.malucky.itinerary.data.Kategori
import org.malucky.itinerary.data.ResultsItem
import org.malucky.itinerary.data.Settings
import org.malucky.itinerary.reusable.Navigator

abstract class BaseActivity: AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var storageReference: StorageReference
    var settingsItem: ArrayList<Settings>? = null


    var navigate: Navigator = Navigator()

    abstract fun getView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getView())
        onActivityCreated()
    }

    abstract fun onActivityCreated()

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun signOut(){
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        navigate.signin(this)
    }




}