package edu.califer.recuit_crmassignment.Adapter

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

/**Setting up the Circular ImageView.*/
@BindingAdapter("setCircularImageView", requireAll = true)
fun setCircularImageView(view: CircleImageView?, url: String?) {
    if (view != null) {
        Glide
            .with(view.context)
            .load(url)
            .centerCrop()
            .into(view)
    }
}