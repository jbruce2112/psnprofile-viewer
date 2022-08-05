package com.bruce32.psnprofileviewer.profile

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bruce32.psnprofileviewer.R

class ProfileStatItemView(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    var viewModel: ProfileStatViewModel? = null
        set(value) {
            findViewById<TextView>(R.id.statValue).text = value?.value
            findViewById<TextView>(R.id.statLabel).text = value?.label?.getString(context)
            field = value
        }

    init {
        inflate(getContext(), R.layout.stat_item_profile,this);
    }
}
