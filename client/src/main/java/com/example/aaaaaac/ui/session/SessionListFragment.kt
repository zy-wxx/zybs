package com.example.aaaaaac.ui.session

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
import com.example.aaaaaac.model.Session
import com.example.aaaaaac.model.UserInfo
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

private const val TAG = "SessionListFragment"
class SessionListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter:SessionAdapter? = SessionAdapter(emptyList())
    private val sessionListViewModel : SessionListViewModel by lazy {
        ViewModelProvider(this).get(SessionListViewModel::class.java)
    }
    private lateinit var selfInfo: UserInfo
    private var callbacks: Callbacks? = null



    companion object{
        fun newInstance(userInfo: UserInfo): SessionListFragment {
            val args = Bundle().apply {
                putSerializable("USER",userInfo)
            }
            return SessionListFragment().apply {
                arguments = args
            }
        }
    }
    /**
     * interface solve item onclick event
     */
    interface Callbacks {
        fun onSessionSelected(session: Session)
    }



    /**
     * lifecycle method
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_session, container, false)
        recyclerView = view.findViewById(R.id.recycle_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        selfInfo = arguments?.getSerializable("USER") as UserInfo
        sessionListViewModel.initUserInfo(selfInfo)

        Log.d(TAG, "onCreateView")
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionListViewModel.userList.observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?

    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    /**
     * UI method
     */
    private fun updateUI(sessions: List<Session>) {
        adapter = SessionAdapter(sessions)
        recyclerView.adapter = adapter
    }

    /**
     * inner class UserHolder
     */
    private inner class SessionHolder(view: View): RecyclerView.ViewHolder(view),
        View.OnClickListener,View.OnLongClickListener {
        private lateinit var session : Session
        val mTimeTextView: TextView = itemView.findViewById(R.id.time)
        val mNameTextView: TextView = itemView.findViewById(R.id.name)

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(session: Session, position: Int){
            this.session = session

            mTimeTextView.text = android.icu.text.DateFormat.getPatternInstance("MM-dd HH:mm").format(session.date)
            mNameTextView.text = this.session.to_name

        }

        override fun onClick(p0: View?) {
            callbacks?.onSessionSelected(session)
        }

        override fun onLongClick(p0: View?): Boolean {
            return false
        }

    }
    /**
     * inner class UserAdapter
     */
    private inner class SessionAdapter(var session:List<Session>): RecyclerView.Adapter<SessionHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
            val view = layoutInflater.inflate(R.layout.session_list_item,parent,false)
            return SessionHolder(view)
        }

        override fun getItemCount(): Int {
            return session.size
        }

        override fun onBindViewHolder(holder: SessionHolder, position: Int) {
            val session: Session = session[position]
            holder.bind(session,position)

        }

        }
}