package com.example.aaaaaac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.example.aaaaaac.model.Session
import com.example.aaaaaac.model.UserInfo
import com.example.aaaaaac.ui.chat.ChatActivity
import com.example.aaaaaac.ui.session.SessionListFragment
import com.example.aaaaaac.ui.session.SessionRepository
import com.example.aaaaaac.ui.user.UserListFragment

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), SessionListFragment.Callbacks ,UserListFragment.Callbacks{

    private val sessionRepository = SessionRepository.get()
    private lateinit var selfInfo: UserInfo
    private lateinit var mChatImageButton: ImageButton
    private lateinit var mUserImageButton: ImageButton
    private lateinit var mTitleTextView: TextView
    private var currentIndex:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        selfInfo = intent.getSerializableExtra("USER") as UserInfo

        mChatImageButton = findViewById<ImageButton>(R.id.chat_list)
        mUserImageButton = findViewById<ImageButton>(R.id.user_list)
        mTitleTextView = findViewById<TextView>(R.id.title)
        mTitleTextView.text = "会话列表"
        mChatImageButton.setOnClickListener {
            toSessionListFragment()
            mTitleTextView.text = "会话列表"
        }
        mUserImageButton.setOnClickListener {
            toUserListFragment()
            mTitleTextView.text = "在线列表"
        }



        val fragment = SessionListFragment.newInstance(selfInfo)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        mChatImageButton.setImageResource(R.drawable.baseline_chat_24)


    }


    private fun toSessionListFragment(){
        if (currentIndex != 0) {
            val fragment = SessionListFragment.newInstance(selfInfo)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            currentIndex = 0
            mChatImageButton.setImageResource(R.drawable.baseline_chat_24)
            mUserImageButton.setImageResource(R.drawable.outline_supervised_user_circle_24)
        }
    }
    private fun toUserListFragment(){
        if (currentIndex != 1) {
            val fragment = UserListFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            currentIndex = 1
            mChatImageButton.setImageResource(R.drawable.outline_chat_24)
            mUserImageButton.setImageResource(R.drawable.baseline_supervised_user_circle_24)

        }
    }
    private fun toChatActivity(session: Session){
        val intent = Intent(this,ChatActivity::class.java)
        intent.putExtra("SESSION",session)
        this.moveTaskToBack(true)
        startActivity(intent)
    }



    override fun onSessionSelected(session: Session) {
        toChatActivity(session)
    }
    override fun onUserSelected(userInfo: UserInfo) {
        val session= Session(selfInfo.uid, selfInfo.name,userInfo.uid,userInfo.name)
        sessionRepository.addSession(session)
        toSessionListFragment()
        onSessionSelected(session)
    }


}