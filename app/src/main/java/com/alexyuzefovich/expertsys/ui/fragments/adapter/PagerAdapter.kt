package com.alexyuzefovich.expertsys.ui.fragments.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alexyuzefovich.expertsys.R
import com.alexyuzefovich.expertsys.ui.fragments.GuessingFragment
import com.alexyuzefovich.expertsys.ui.fragments.ObjectsFragment

class PagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getPageTitle(position: Int): CharSequence? =
        when (position) {
            0 -> context.getString(R.string.objects)
            1 -> context.getString(R.string.let_is_guess)
            else -> ""
        }

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> ObjectsFragment()
            1 -> GuessingFragment()
            else -> ObjectsFragment()
        }

    override fun getCount(): Int = 2

}