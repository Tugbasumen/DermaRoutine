package com.dermaroutine.presentation.onboarding

import androidx.annotation.DrawableRes
import com.dermaroutine.R

sealed class OnboardingPage(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
) {
    data object First : OnboardingPage(
        title = "Build Your Routine",
        description = "Create and organize your personal morning and evening skincare routine.",
        image = R.drawable.ic_splash_logo
    )

    data object Second : OnboardingPage(
        title = "Track Your Skin Journey",
        description = "Add photos and notes to follow your personal progress over time.",
        image = R.drawable.ic_splash_logo
    )

    data object Third : OnboardingPage(
        title = "Understand Your Products",
        description = "Explore your skincare products and learn about their ingredients.",
        image = R.drawable.ic_splash_logo
    )
}
