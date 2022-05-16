package pro.com.catfacts.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import pro.com.catfacts.R
import pro.com.catfacts.views.listener.LongClickDeleteListener
import pro.com.catfacts.views.observer.RandomStateObserver


class MainActivity : AppCompatActivity() {
    private val viewModel: RandomFactViewModel by inject()
    private val randomFactView: RandomFactView by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(randomFactView.inflate(LayoutInflater.from(this), null))
        viewModel.setObserver(this, RandomStateObserver(randomFactView))
        viewModel.loadFromDb()

        randomFactView.setLongClickListener(LongClickDeleteListener(viewModel))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.loadFact -> {
                viewModel.loadFacts(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}