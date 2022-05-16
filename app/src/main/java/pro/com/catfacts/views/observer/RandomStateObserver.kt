package pro.com.catfacts.views.observer

import androidx.lifecycle.Observer
import pro.com.catfacts.views.RandomFactView
import pro.com.catfacts.views.State

class RandomStateObserver(private val view: RandomFactView) : Observer<State> {
    override fun onChanged(state: State) {
        when (state) {
            is State.RenderData ->
                view.changeState(RandomFactView.State.RenderData(state.randomFacts))
            is State.Error ->
                view.changeState(RandomFactView.State.Error(state.error))
            is State.ShowToast ->
                view.changeState(RandomFactView.State.ShowToast(state.message))
            is State.Add ->
                view.changeState(RandomFactView.State.Add(state.fact))
            is State.Delete ->
                view.changeState(RandomFactView.State.Delete(state.catId))
        }
    }
}