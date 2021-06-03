package com.platform45.fx45

import android.view.Menu
import androidx.navigation.NavController
import com.platform45.fx45.ui.dashboard.DashboardFragment

interface MyDrawerController {
    var navController: NavController
    var toolbarMenu: Menu?
    fun setTitle(title: String)
    fun showMenu()
    fun hideMenu()
    fun showActionBarIcon()
    fun hideActionBarIcon()
    fun setDashboardFragment(dashboardFragment: DashboardFragment)
    fun showContent()
    fun showSelectionMode()
    fun showLoading()
    fun hideLoading()
}