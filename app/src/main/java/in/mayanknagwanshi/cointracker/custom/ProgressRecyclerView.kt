package `in`.mayanknagwanshi.cointracker.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.mayanknagwanshi.cointracker.R
import `in`.mayanknagwanshi.cointracker.databinding.CustomProgressRecyclerviewBinding

class ProgressRecyclerView private constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: CustomProgressRecyclerviewBinding

    private val errorImage: Drawable?
    private val errorTitle: Int

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : this(context, attrs, defStyleAttr, 0)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ProgressRecyclerView)
        errorImage = a.getDrawable(R.styleable.ProgressRecyclerView_error_image)
        errorTitle = a.getResourceId(R.styleable.ProgressRecyclerView_error_text, 0)
        a.recycle()
        init()
    }

    private fun init() {
        binding =
            CustomProgressRecyclerviewBinding.inflate(LayoutInflater.from(context), this, true)

        binding.recyclerView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        if (errorImage != null) binding.imageViewError.setImageDrawable(errorImage)
        if (errorTitle != 0) binding.textViewError.text = context.getText(errorTitle)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun showError() {
        binding.recyclerView.visibility = GONE
        binding.progressBar.visibility = GONE
        binding.imageViewError.visibility = VISIBLE
        binding.textViewError.visibility = VISIBLE
    }

    fun showProgress() {
        binding.progressBar.visibility = VISIBLE
    }

    fun hideProgress() {
        binding.progressBar.visibility = GONE
    }

    fun showRecyclerView() {
        binding.recyclerView.visibility = VISIBLE
        binding.progressBar.visibility = GONE
        binding.imageViewError.visibility = GONE
        binding.textViewError.visibility = GONE
    }

    var recyclerView: RecyclerView = binding.recyclerView
        private set

}