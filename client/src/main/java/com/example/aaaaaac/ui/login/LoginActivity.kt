package com.example.aaaaaac.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.aaaaaac.MainActivity
import com.example.aaaaaac.R
import com.example.aaaaaac.model.UserInfo

class LoginActivity: AppCompatActivity() {
    private lateinit var mEditId:EditText
    private lateinit var mEditName:EditText
    private lateinit var mLoginButton:Button
    private lateinit var mTextView:TextView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var name:String
    private lateinit var uid:String

    private val viewModel:LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mEditId = findViewById<EditText>(R.id.edit_id)
        mEditName = findViewById<EditText>(R.id.edit_name)
        mLoginButton = findViewById<Button>(R.id.btn_login)
        mTextView = findViewById<TextView>(R.id.tv_error)
        mProgressBar = findViewById<ProgressBar>(R.id.progress_bar)
        mLoginButton.setOnClickListener { login() }
        viewModel.status.observe(this) {
            if (it.equals("success")){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER",UserInfo(uid, name))
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"登录失败",Toast.LENGTH_SHORT).show()
            }

        }
        viewModel.getError().observe(this){

            mProgressBar.visibility = View.INVISIBLE
            if (it){
                mTextView.text = "连接服务器失败 "
                mTextView.setTextColor(Color.RED)
                mTextView.visibility = View.VISIBLE
            }else{
                mTextView.visibility = View.GONE
                mEditId.visibility = View.VISIBLE
                mEditName.visibility = View.VISIBLE
                mLoginButton.visibility = View.VISIBLE
            }
        }

    }

    private fun login(){
        checkAllPermission()
        uid = mEditId.text.toString()
        name = mEditName.text.toString()
        if (uid.isEmpty()||name.isEmpty()){
            val toast = Toast.makeText(this,"id 或 name 不能为空",Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER,0,-400)
            toast.show()
            return
        }
        viewModel.login(uid,name)
    }
    private fun checkAllPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.INTERNET)
            val requestCode = 100
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }
}