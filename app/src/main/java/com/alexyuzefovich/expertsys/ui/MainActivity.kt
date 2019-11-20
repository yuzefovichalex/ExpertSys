package com.alexyuzefovich.expertsys.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alexyuzefovich.expertsys.R
import com.alexyuzefovich.expertsys.ui.fragments.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        initViewPager()
        initTabLayout()
    }

    private fun initViewPager() {
        viewPager.adapter = PagerAdapter(this, supportFragmentManager)
    }

    private fun initTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
    }

}
