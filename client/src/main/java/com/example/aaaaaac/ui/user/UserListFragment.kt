package com.example.aaaaaac.ui.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aaaaaac.R
import com.example.aaaaaac.model.UserInfo

private const val TAG = "UserListFragment"
class UserListFragment:Fragment() {

    private lateinit var mRecyclerView:RecyclerView
    private var mAdapter: UserAdapter = UserAdapter(emptyList())

    private val viewModel:UserListViewModel by lazy {
        ViewModelProvider(this).get(UserListViewModel::class.java)
    }



    private var callbacks:Callbacks? = null

    interface Callbacks {
        fun onUserSelected(userInfo: UserInfo)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user,container,false)
        mRecyclerView = view.findViewById(R.id.recycle_view) as RecyclerView
        val layoutManager  = LinearLayoutManager(context)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = mAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserListLiveData().observe(viewLifecycleOwner){
            updateUI(it)
        }
        viewModel.sendREQ()
    }
    private fun updateUI(userList:List<UserInfo>){
        mAdapter = UserAdapter(userList)
        mRecyclerView.adapter = mAdapter
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
        Log.e(TAG,"onAttach")

    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
        Log.e(TAG,"omDetach")
    }




    private inner class UserHolder(view: View): RecyclerView.ViewHolder(view),View.OnClickListener{

        private lateinit var userInfo: UserInfo
        private val mIdTextView = view.findViewById<TextView>(R.id.time)
        private val mNameTextView = view.findViewById<TextView>(R.id.name)

        init{
           itemView.setOnClickListener(this)
        }

        fun bind(userInfo: UserInfo){
            this.userInfo = userInfo
            mIdTextView.text = userInfo.uid
            mNameTextView.text = userInfo.name
        }

        override fun onClick(p0: View?) {
            callbacks?.onUserSelected(userInfo)
        }

    }
    private inner class UserAdapter(var users: List<UserInfo>): RecyclerView.Adapter<UserHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
            val v = layoutInflater.inflate(R.layout.user_list_item,parent,false)
            return UserHolder(v)
        }

        override fun getItemCount(): Int {
            return users.size
        }

        override fun onBindViewHolder(holder: UserHolder, position: Int) {
            holder.bind(users[position])
        }

    }
}