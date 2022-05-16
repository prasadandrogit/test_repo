package pro.com.catfacts.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.com.catfacts.R
import pro.com.catfacts.util.Constants
import pro.com.catfacts.views.listener.LongClickDeleteListener

class FactsViewHolderTest {

    @DisplayName("Given data")
    @Nested
    inner class GivenData {
        @SuppressLint("CheckResult")
        @DisplayName("Then render the views")
        @Test
        fun test() {

            val fact = "cat fact"
            val dateAdded = "2021-01-22 12:23:11"
            val id = "1234"

            val mockView: View = mockk(relaxed = true)
            val mockCatImageView: ImageView = mockk(relaxed = true)
            val mockRandomFact: TextView = mockk(relaxed = true)
            val mockDateAdded: TextView = mockk(relaxed = true)
            val mockContext: Context = mockk(relaxed = true)
            val mockData: RandomCatFacts = mockk(relaxed = true)
            val mockRequestManager: RequestManager = mockk(relaxed = true)
            val mockRequestBuilder: RequestBuilder<Drawable> = mockk(relaxed = true)
            val mockDrawable: Drawable = mockk(relaxed = true)
            val mockListener: LongClickDeleteListener = mockk(relaxed = true)

            mockkStatic(Glide::class)

            val instance = FactsViewHolder(mockView, mockListener)

            every { mockView.findViewById<ImageView>(R.id.catImage) } returns mockCatImageView
            every { mockView.findViewById<TextView>(R.id.randomFact) } returns mockRandomFact
            every { mockView.findViewById<TextView>(R.id.factDateAdded) } returns mockDateAdded
            every { mockView.context } returns mockContext
            every { mockData.fact } returns fact
            every { mockData.dateAdded } returns dateAdded
            every { mockData.id } returns id
            every { Glide.with(mockContext) } returns mockRequestManager
            every { mockRequestManager.load(mockDrawable) } returns mockRequestBuilder
            every { mockRequestBuilder.into(any()) } returns mockk(relaxed = true)

            instance.render(mockData)

            verify { mockRandomFact.text = fact }
            verify { mockDateAdded.text = dateAdded }
            verify { mockView.tag = id }
            verify { mockView.setOnLongClickListener(mockListener) }

            verify { Glide.with(mockContext) }
            verify { mockRequestManager.load(Constants.CAT_IMAGE_URL) }

            unmockkStatic(Glide::class)

        }
    }
}