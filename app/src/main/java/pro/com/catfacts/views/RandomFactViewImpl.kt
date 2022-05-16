package pro.com.catfacts.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pro.com.catfacts.R
import pro.com.catfacts.views.adapter.FactsRecyclerViewAdapter
import pro.com.catfacts.views.adapter.RandomCatFacts
import pro.com.catfacts.views.listener.EmptyFactsListener
import pro.com.catfacts.views.listener.LongClickDeleteListener

class RandomFactViewImpl(private val adapter: FactsRecyclerViewAdapter) : RandomFactView, EmptyFactsListener {

    private var rootView: View? = null

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): View? {
        rootView = inflater.inflate(R.layout.activity_main, container, false)
        setupRecyclerView()
        return rootView
    }

    private fun setupRecyclerView() {
        rootView?.let {
            it.findViewById<TextView>(R.id.emptyTv).visibility = View.GONE
            val recyclerView = it.findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = adapter
            val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
            recyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    override fun changeState(state: RandomFactView.State) {
        when (state) {
            is RandomFactView.State.RenderData -> setAdapterData(state.randomFacts)
            is RandomFactView.State.Error -> showError(state.error)
            is RandomFactView.State.ShowToast -> showToast(state.message)
            is RandomFactView.State.Add -> addToFeed(state.fact, this)
            is RandomFactView.State.Delete -> removeFromFeed(state.catId, this)
        }
    }

    private fun removeFromFeed(catId: String, listener: EmptyFactsListener) {
        adapter.delete(catId, listener)
    }

    private fun addToFeed(fact: RandomCatFacts, listener: EmptyFactsListener) {
        adapter.add(fact, listener)
    }

    private fun showToast(message: String) {
        rootView?.let {
            val toast = Toast.makeText(it.context, message, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    override fun setLongClickListener(longClickDeleteListener: LongClickDeleteListener) {
        adapter.setLongClickListener(longClickDeleteListener)
    }

    private fun showError(error: Throwable) {
        error.message?.let { showToast(it) }
    }

    private fun showEmptyFacts(isVisible: Boolean) {
        rootView?.let {
            it.findViewById<TextView>(R.id.emptyTv).visibility = if (isVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun setAdapterData(list: ArrayList<RandomCatFacts>) {
        rootView?.let {
            if (list.isNotEmpty()) {
                adapter.setData(list)
            } else {
                showEmptyFacts(true)
            }
        }
    }

    override fun onEmptyFacts(isVisible: Boolean) {
        showEmptyFacts(isVisible)
    }
}