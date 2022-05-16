package pro.com.catfacts.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pro.com.catfacts.views.adapter.RandomCatFacts
import pro.com.catfacts.views.listener.LongClickDeleteListener

interface RandomFactView {

    fun inflate(inflater: LayoutInflater, container: ViewGroup?): View?
    fun changeState(state: State)
    fun setLongClickListener(longClickDeleteListener: LongClickDeleteListener)

    sealed class State {
        class RenderData(val randomFacts: ArrayList<RandomCatFacts>) : State()
        class Add(val fact: RandomCatFacts) : State()
        class Delete(val catId: String) : State()
        class Error(val error: Throwable) : State()
        class ShowToast(val message: String) : State()
    }
}