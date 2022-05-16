package pro.com.catfacts.views.listener

import android.view.View
import pro.com.catfacts.views.RandomFactViewModel

class LongClickDeleteListener(private val viewModel: RandomFactViewModel) :
    View.OnLongClickListener {
    override fun onLongClick(view: View): Boolean {
        viewModel.delete(view.tag as String)
        return true
    }
}