package pro.com.catfacts.views.listener

import android.view.View
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.com.catfacts.views.RandomFactViewModel

class LongClickDeleteListenerTest {

    private val mockViewModel: RandomFactViewModel = mockk(relaxed = true)
    lateinit var instance: LongClickDeleteListener

    @BeforeEach
    fun beforeEachTest() {
        instance = LongClickDeleteListener(mockViewModel)
    }

    @AfterEach
    fun afterEachTest() {
        clearAllMocks()
    }

    @DisplayName("When call on longClick")
    @Nested
    inner class WhenLongClick {
        @DisplayName("Given view")
        @Nested
        inner class GiveView {
            @DisplayName("Then call the delete on viewModel")
            @Test
            fun test() {
                val catId = "catId"
                val mockView: View = mockk(relaxed = true)
                every { mockView.tag } returns catId

                instance.onLongClick(mockView)

                verify { mockViewModel.delete(catId) }
            }
        }
    }
}