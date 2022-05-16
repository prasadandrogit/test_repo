package pro.com.catfacts.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.com.catfacts.R
import pro.com.catfacts.views.adapter.FactsRecyclerViewAdapter
import pro.com.catfacts.views.adapter.RandomCatFacts
import pro.com.catfacts.views.listener.LongClickDeleteListener
import kotlin.test.assertEquals

class RandomFactViewImplTest {

    lateinit var instance: RandomFactViewImpl
    val mockView: View = mockk(relaxed = true)
    val mockInflater: LayoutInflater = mockk(relaxed = true)
    val mockRecyclerView: RecyclerView = mockk(relaxed = true)
    val mockVieGroup: ViewGroup = mockk(relaxed = true)
    val mockEmptyView: TextView = mockk(relaxed = true)
    val mockAdapter: FactsRecyclerViewAdapter = mockk(relaxed = true)


    @BeforeEach
    fun beforeEachTest() {
        every { mockInflater.inflate(any<Int>(), any(), any()) } returns mockView
        every { mockView.findViewById<RecyclerView>(R.id.recyclerView) } returns mockRecyclerView
        every { mockView.findViewById<TextView>(R.id.emptyTv) } returns mockEmptyView
        every { mockRecyclerView.adapter } returns mockAdapter

        instance = RandomFactViewImpl(mockAdapter)
    }

    @AfterEach
    fun afterEachTest() {
        unmockkAll()
    }

    @DisplayName("When call inflate()")
    @Nested
    inner class WhenCallInflate {
        @DisplayName("Given layoutManager and viewGroup")
        @Nested
        inner class GivenParams {
            @DisplayName("Then return the view")
            @Test
            fun test() {

                val result = instance.inflate(mockInflater, mockVieGroup)

                assertEquals(mockView, result)
            }
        }
    }

    @DisplayName("When call setup recyclerView()")
    @Nested
    inner class WhenSetupRecyclerView {

        @DisplayName("Then set the linear layout manager and adapter to recyclerview")
        @Test
        fun test() {

            every { mockRecyclerView.context } returns mockk(relaxed = true)
            val layoutManagerSlot = CapturingSlot<LinearLayoutManager>()
            val adapterSlot = CapturingSlot<FactsRecyclerViewAdapter>()
            val dividerSlot = CapturingSlot<DividerItemDecoration>()

            instance.inflate(mockInflater, mockVieGroup)

            verify { mockRecyclerView.layoutManager = capture(layoutManagerSlot) }
            verify { mockRecyclerView.adapter = capture(adapterSlot) }
            verify { mockRecyclerView.addItemDecoration(capture(dividerSlot)) }

            assertEquals(LinearLayoutManager::class, layoutManagerSlot.captured::class)
            assertEquals(FactsRecyclerViewAdapter::class, adapterSlot.captured::class)
            assertEquals(DividerItemDecoration::class, dividerSlot.captured::class)

        }

        @DisplayName("When rootView is null")
        @Nested
        inner class WhenRootViewIsNull {

            @DisplayName("Then don't call anything")
            @Test
            fun test() {
                verify(exactly = 0) { mockRecyclerView.layoutManager = any() }
                verify(exactly = 0) { mockRecyclerView.adapter = any() }
            }
        }
    }

    @DisplayName("When call on ChangeState()")
    @Nested
    inner class WhenChangeState {

        @BeforeEach
        fun beforeTest() {
            instance.inflate(mockInflater, mockVieGroup)
        }

        @DisplayName("Given state is RenderData With Non-Empty list")
        @Nested
        inner class GivenStateIsRenderData {
            @DisplayName("Then set list data to adapter")
            @Test
            fun test() {
                val list = arrayListOf(RandomCatFacts())

                instance.changeState(RandomFactView.State.RenderData(list))

                verify { mockAdapter.setData(list) }

            }
        }

        @DisplayName("Given state is RenderData With Empty list")
        @Nested
        inner class GivenStateIsRenderDataWithEmptyList {
            @DisplayName("Then show emptyView with text")
            @Test
            fun test() {
                val emptyList = arrayListOf<RandomCatFacts>()

                instance.changeState(RandomFactView.State.RenderData(emptyList))

                verify { mockEmptyView.visibility = View.VISIBLE }

            }
        }

        @DisplayName("Given state is Error")
        @Nested
        inner class GivenStateIsError {

            @BeforeEach
            fun beforeEachTest() {
                mockkStatic(Toast::class)
            }

            @AfterEach
            fun afterEachTest() {
                unmockkStatic(Toast::class)
            }

            @DisplayName("Then show the error")
            @Test
            fun test() {
                val mockThrowable: Throwable = mockk(relaxed = true)
                val mockContext: Context = mockk(relaxed = true)
                val mockToast: Toast = mockk(relaxed = true)
                val errorMsg = "error occurred"


                every { Toast.makeText(mockContext, any<String>(), any()) } returns mockToast
                every { mockView.context } returns mockContext
                every { mockThrowable.message } returns errorMsg

                instance.changeState(RandomFactView.State.Error(mockThrowable))

                verify {
                    Toast.makeText(mockContext, errorMsg, Toast.LENGTH_SHORT)
                    mockToast.show()
                }
            }
        }

        @DisplayName("Given state is Add")
        @Nested
        inner class GivenStateIsAdd {
            @DisplayName("Then call add on adapter")
            @Test
            fun test() {
                val mockData: RandomCatFacts = mockk(relaxed = true)

                instance.changeState(RandomFactView.State.Add(mockData))

                verify { mockAdapter.add(mockData, instance) }
            }
        }

        @DisplayName("Given state is Delete")
        @Nested
        inner class GivenStateIsDelete {
            @DisplayName("Then call delete on adapter")
            @Test
            fun test() {
                val catId = "134"

                instance.changeState(RandomFactView.State.Delete(catId))

                verify { mockAdapter.delete(catId, instance) }
            }
        }
    }

    @DisplayName("When call on setLongClickListener")
    @Nested
    inner class WhenSetLongClickOnListener {
        @DisplayName("Given listener")
        @Nested
        inner class GivenParams {
            @DisplayName("Then set the listener to adapter")
            @Test
            fun test() {
                val slot = CapturingSlot<LongClickDeleteListener>()
                val mockListener: LongClickDeleteListener = mockk(relaxed = true)

                instance.setLongClickListener(mockListener)

                verify { mockAdapter.setLongClickListener(capture(slot)) }

                assertEquals(mockListener::class, slot.captured::class)

            }
        }
    }

    @DisplayName("When call onEmptyFacts")
    @Nested
    inner class EmptyFacts {

        @BeforeEach
        fun beforeEachTest() {
            instance.inflate(mockInflater, mockVieGroup)
        }
        @DisplayName("Given value is true")
        @Nested
        inner class GivenValueIsTrue {
            @DisplayName("Then empty facts textView should be visible")
            @Test
            fun test() {
                instance.onEmptyFacts(true)

                verify { mockEmptyView.visibility = View.VISIBLE }
            }
        }

        @DisplayName("Given value is false")
        @Nested
        inner class GivenValueIsFalse {
            @DisplayName("Then hide the empty facts textView")
            @Test
            fun test() {
                instance.onEmptyFacts(false)

                verify { mockEmptyView.visibility = View.GONE }
            }
        }
    }
}
