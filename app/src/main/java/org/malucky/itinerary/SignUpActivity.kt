package org.malucky.itinerary

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun getCallingIntent(activity: Activity): Intent {
            return Intent(activity, SignUpActivity::class.java)
        }
        private const val TAG = "EmailPassword"

    }

    override fun getView(): Int = R.layout.activity_sign_up

    override fun onActivityCreated() {
        auth = FirebaseAuth.getInstance()

        Glide.with(this)
            .load(R.drawable.logo_treavel)
            .into(imageView6)

        txt_btnMasuk.setOnClickListener {
            navigate.signin(this)
        }
        btn_daftaar.setOnClickListener {
            createAccount(et_email_daftar.text.toString(), et_pass_daftar.text.toString())
        }

    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    navigate.mainActivity(this)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true


        val email = et_email_daftar.text.toString()
        if (TextUtils.isEmpty(email)) {
            et_email_daftar.error = "Required."
            valid = false
        } else {
            et_email_daftar.error = null
        }

        val pass = et_pass_daftar.text.toString()
        if (TextUtils.isEmpty(pass)) {
            et_pass_daftar.error = "Required."
            valid = false
        } else {
            et_pass_daftar.error = null
        }

        val konf = et_konf_pass.text.toString()
        if (TextUtils.isEmpty(konf)) {
            et_konf_pass.error = "Required."
            valid = false
        } else {
            et_konf_pass.error = null
        }

        if (pass.equals(konf)){
            valid
        } else{
            valid = false
        }

        return valid
    }

}
