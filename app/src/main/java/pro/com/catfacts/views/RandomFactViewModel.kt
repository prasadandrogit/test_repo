package pro.com.catfacts.views

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pro.com.catfacts.R
import pro.com.catfacts.util.Connectivity
import pro.com.catfacts.views.repo.RandomFactRepo

class RandomFactViewModel(private val randomFactRepo: RandomFactRepo) : ViewModel() {

    private val liveData = MutableLiveData<State>()

    fun loadFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue(State.RenderData(ArrayList(randomFactRepo.fetchDetails())))
        }
    }

    fun getRandomFact() {
        viewModelScope.launch(Dispatchers.IO) {
                randomFactRepo.fetchRandomFact().catch { exception ->
                    liveData.postValue(State.Error(exception))
                }
                .collect {
                    liveData.postValue(State.Add(it))
                }
        }
    }

    fun setObserver(owner: LifecycleOwner, observer: Observer<State>) {
        liveData.observe(owner, observer)
    }

    fun delete(catId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            randomFactRepo.delete(catId).catch { e ->
                liveData.postValue(State.Error(e))
            }.collect {
                liveData.postValue(State.Delete(it))
            }
        }
    }

    fun loadFacts(context: Context) {
        if (Connectivity.isConnected(context)) {
            getRandomFact()
        } else {
            loadFromDb()
            liveData.value = State.ShowToast(context.getString(R.string.no_internet_connection))
        }
    }
}