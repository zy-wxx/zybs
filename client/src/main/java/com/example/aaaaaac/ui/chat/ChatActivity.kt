package com.example.aaaaaac.ui.chat

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aaaaaac.R
import com.example.aaaaaac.model.ChatInfo
import com.example.aaaaaac.model.Session

private const val TAG = "ChatActivity"
class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var adapter: ChatAdapter? = ChatAdapter(emptyList())
    private val viewModel: ChatDetailViewModel by lazy {
        ViewModelProvider(this).get(ChatDetailViewModel::class.java)
    }
    private lateinit var mTitleTextView: TextView
    private lateinit var mBackImageButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
        mTitleTextView = findViewById<TextView>(R.id.title)
        mBackImageButton = findViewById<ImageButton>(R.id.back)
        val layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        initViewModel()

        val editText = findViewById<EditText>(R.id.edit)
        val tvSend = findViewById<TextView>(R.id.tv_sned)
        tvSend.setOnClickListener{
            val text:String = editText.text.toString()
            if (text.isNotEmpty()){
                editText.editableText.clear()
                viewModel.writeToRemote(text)
            }
        }

        viewModel.chatMsgList.observe(this){
            Log.e(TAG,it.size.toString())
            updateUI(it)
        }

        mBackImageButton.setOnClickListener{
            this.finish()
        }




        Log.d(TAG, "onCreateView")

    }
    /*
       view model 需要一些参数，用于加载初始化界面
       如果不这么做，则需要使用view model 的构造器，
       而view model 的构造又由 view model provider 来托管
    */
    private fun initViewModel(){
        val session = intent.getSerializableExtra("SESSION") as Session
        mTitleTextView.text = session.to_name
        viewModel.initSession(session)
    }
    private fun updateUI(msgList: List<ChatInfo>?){
        msgList?.let {
            adapter = ChatAdapter(it)
            recyclerView.adapter = adapter
            recyclerView.scrollToPosition(msgList.size -1)

        }
        Log.i(TAG, "updateUI()")
    }





    private inner class ChatHolder(view: View): RecyclerView.ViewHolder(view){
        private lateinit var msg: ChatInfo
        private val tv_text = view.findViewById<TextView>(R.id.tv_text)
        private val iv_icon = view.findViewById<ImageView>(R.id.iv_user_icon)
        fun bind(msg: ChatInfo){
            this.msg = msg
            tv_text.text = msg.text
        }
    }
    private inner class ChatAdapter(var msgs: List<ChatInfo>): RecyclerView.Adapter<ChatHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
            val v: View = if (viewType == 1) {
                layoutInflater.inflate(R.layout.chat_view_right, parent, false)
            }else{
                layoutInflater.inflate(R.layout.chat_view_left, parent, false)
            }
            return ChatHolder(v)
        }

        override fun getItemCount(): Int {
            return msgs.size
        }

        override fun onBindViewHolder(holder: ChatHolder, position: Int) {
            holder.bind(msgs[position])
        }

        override fun getItemViewType(position: Int): Int {
            return msgs[position].viewType
        }
    }
}