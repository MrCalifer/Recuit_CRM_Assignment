package edu.califer.recuit_crmassignment.Adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**Setting up the Circular ImageView.*/
@BindingAdapter("setCircularImageView", requireAll = true)
fun setCircularImageView(view: ImageView?, url: String?) {
    if (view != null) {
        Glide
            .with(view.context)
            .load(url)
            .centerCrop()
            .into(view)
    }
}