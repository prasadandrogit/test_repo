package pro.com.catfacts.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.com.catfacts.R
import pro.com.catfacts.views.listener.EmptyFactsListener
import pro.com.catfacts.views.listener.LongClickDeleteListener
import kotlin.test.assertEquals

class FactsRecyclerViewAdapterTest {

    lateinit var instance: FactsRecyclerViewAdapter
    val mockListener: LongClickDeleteListener = mockk(relaxed = true)

    @BeforeEach
    fun beforeEachTest() {
        instance = spyk(FactsRecyclerViewAdapter())
    }

    @AfterEach
    fun afterEachTest() {
        unmockkAll()
    }

    @DisplayName("When Call on OnCreateViewHolder")
    @Nested
    inner class WhenCallOnCreateViewHolder {
        @DisplayName("Given parent and viewType")
        @Nested
        inner class GivenParentAndViewType {
            @DisplayName("Then return FactsView Holder")
            @Test
            fun test() {

                val mockView: View = mockk(relaxed = true)
                val mockParentView: ViewGroup = mockk(relaxed = true)
                val mockContext: Context = mockk(relaxed = true)
                val mockInflater: LayoutInflater = mockk(relaxed = true)

                every { mockParentView.context } returns mockContext

                instance.setLongClickListener(mockListener)

                mockkStatic(LayoutInflater::class)
                every { LayoutInflater.from(mockContext) } returns mockInflater
                every { mockInflater.inflate(any<Int>(), mockParentView, any()) } returns mockView

                instance.onCreateViewHolder(mockParentView, 2)

                verify {
                    mockInflater.inflate(R.layout.cat_item, mockParentView, false)
                }

                unmockkStatic(LayoutInflater::class)
            }
        }
    }

    @DisplayName("When Call OnBindViewHolder")
    @Nested
    inner class WhenCallOnBindViewHolder {
        @DisplayName("Given VieHolder and position")
        @Nested
        inner class Given {
            @DisplayName("Then invoke render on viewHolder")
            @Test
            fun test() {

                val list = arrayListOf(RandomCatFacts(), RandomCatFacts())
                val mockViewHolder: FactsViewHolder = mockk(relaxed = true)
                val position = 0

                instance.setData(list)

                instance.onBindViewHolder(mockViewHolder, position)

                verify { mockViewHolder.render(list[position]) }
            }
        }
    }

    @DisplayName("When Call getItemCount")
    @Nested
    inner class WhenCallGetItemCount {
        @DisplayName("Then return the count")
        @Test
        fun test() {
            val list = arrayListOf(RandomCatFacts(), RandomCatFacts())

            instance.setData(list)

            val result = instance.itemCount

            assertEquals(2, result)
        }
    }

    @DisplayName("When Call setLongClickListener")
    @Nested
    inner class WhenSetLongClickListener {
        @DisplayName("Then set the value")
        @Test
        fun test() {
            instance.setLongClickListener(mockListener)
            assertEquals(LongClickDeleteListener::class, mockListener::class)
        }
    }

    @DisplayName("When call Add()")
    @Nested
    inner class WhenAdd {
        @DisplayName("Given fact and listener")
        @Nested
        inner class GivenFactAndListener {
            @DisplayName("Then insert data into list")
            @Test
            fun test() {
                val list = arrayListOf<RandomCatFacts>()
                val mockFact = RandomCatFacts()
                val mockListener: EmptyFactsListener = mockk(relaxed = true)
                every { instance.notifyItemInserted(any()) } just runs

                instance.setData(list)

                instance.add(mockFact, mockListener)

                verify { mockListener.onEmptyFacts(false) }
                verify(exactly = 0) { mockListener.onEmptyFacts(true) }
                verify { instance.notifyItemInserted(0) }

            }
        }
    }

    @DisplayName("When call delete()")
    @Nested
    inner class WhenDelete {
        @DisplayName("Given catId and listener")
        @Nested
        inner class GivenFactAndListener {
            @DisplayName("Then remove from the list")
            @Test
            fun test() {
                val list = arrayListOf(RandomCatFacts(id = "1234"))
                val catId = "1234"
                val mockListener: EmptyFactsListener = mockk(relaxed = true)
                every { instance.notifyItemRemoved(any()) } just runs

                instance.setData(list)

                instance.delete(catId, mockListener)

                verify { mockListener.onEmptyFacts(true) }
                verify(exactly = 0) { mockListener.onEmptyFacts(false) }
                verify { instance.notifyItemRemoved(0) }
            }
        }
    }
}