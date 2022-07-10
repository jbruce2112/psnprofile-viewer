package com.bruce32.psnprofileviewer.trophylist

import com.bruce32.psnprofileviewer.common.ListItemViewModel
import com.bruce32.psnprofileviewer.model.Trophy

class TrophyViewModel(
    trophy: Trophy
) : ListItemViewModel {
    override val title = trophy.name
    override val description = trophy.description
    override val leadingIconURL = trophy.imageURL
    override val trailingText = trophy.grade
    override val highlighted = trophy.earned

    override val id: String? = null
    override val secondary: String? = null
}