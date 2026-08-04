package com.dermaroutine.presentation.onboarding

import android.content.Context
import androidx.annotation.DrawableRes
import com.dermaroutine.R

sealed class OnboardingPage(
    val titleRes: Int,
    val descriptionRes: Int,
    @DrawableRes val image: Int
) {
    data object First : OnboardingPage(
        titleRes = R.string.onboarding_title_1,
        descriptionRes = R.string.onboarding_desc_1,
        image = R.drawable.ic_splash_logo
    )

    data object Second : OnboardingPage(
        titleRes = R.string.onboarding_title_2,
        descriptionRes = R.string.onboarding_desc_2,
        image = R.drawable.ic_splash_logo
    )

    data object Third : OnboardingPage(
        titleRes = R.string.onboarding_title_3,
        descriptionRes = R.string.onboarding_desc_3,
        image = R.drawable.ic_splash_logo
    )
}
