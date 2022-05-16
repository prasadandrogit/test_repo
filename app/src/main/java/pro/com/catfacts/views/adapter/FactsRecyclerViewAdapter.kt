package pro.com.catfacts.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pro.com.catfacts.R
import pro.com.catfacts.views.listener.EmptyFactsListener
import pro.com.catfacts.views.listener.LongClickDeleteListener

class FactsRecyclerViewAdapter : RecyclerView.Adapter<FactsViewHolder>() {

    private var factsList: ArrayList<RandomCatFacts> = arrayListOf()
    lateinit var longClickDeleteListener: LongClickDeleteListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cat_item, parent, false)
        return FactsViewHolder(view, longClickDeleteListener)
    }

    override fun onBindViewHolder(holder: FactsViewHolder, position: Int) {
        holder.render(factsList[position])
    }

    fun setData(list: ArrayList<RandomCatFacts>) {
        factsList.clear()
        factsList = list
    }

    fun add(fact: RandomCatFacts, listener: EmptyFactsListener) {
        factsList.add(fact)
        notifyItemInserted(factsList.size - 1)
        if (factsList.isNotEmpty()) {
            listener.onEmptyFacts(false)
        }
    }

    fun delete(catId: String, listener: EmptyFactsListener) {
        var position = 0
        factsList.forEachIndexed { index, value ->
            if (value.id == catId) {
                position = index
            }
        }
        factsList.removeAt(position)
        notifyItemRemoved(position)
        if (factsList.isEmpty()) {
            listener.onEmptyFacts(true)
        }
    }

    override fun getItemCount(): Int {
        return factsList.count()
    }

    fun setLongClickListener(listener: LongClickDeleteListener) {
        longClickDeleteListener = listener
    }
}