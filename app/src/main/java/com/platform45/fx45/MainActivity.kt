package com.platform45.fx45

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.platform45.fx45.ui.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), MyDrawerController{
    override lateinit var navController: NavController
    override var toolbarMenu: Menu? = null
    var convertMenuItem: MenuItem? = null
    var findMenuItem: MenuItem? = null
    var closeMenuItem: MenuItem? = null
    var dbFragment: DashboardFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)
        navController = this.findNavController(R.id.navControllerFragment)
        setupWithNavController(toolbar, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        convertMenuItem = menu.findItem(R.id.action_convert)
        findMenuItem = menu.findItem(R.id.action_tweak)
        closeMenuItem = menu.findItem(R.id.action_close_selection)
        toolbarMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_convert ->  Toast.makeText(this, "Action convert", Toast.LENGTH_SHORT).show() //navController.navigate(R.id.dashboard_to_conversion)
            R.id.action_tweak -> dbFragment?.showPairSelector()
            R.id.action_close_selection -> dbFragment?.showPairSeriesInfo()
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

    override fun setTitle(title: String) {
        toolbar?.title = title
    }

    override fun showContent() {
        findMenuItem?.isVisible = true
        closeMenuItem?.isVisible = false
        convertMenuItem?.isVisible = true
        toolbar?.isVisible = true
    }

    override fun showSelectionMode() {
        findMenuItem?.isVisible = false
        closeMenuItem?.isVisible = true
        convertMenuItem?.isVisible = false
        toolbar?.isVisible = true
    }

    override fun setDashboardFragment(dashboardFragment: DashboardFragment) {
        dbFragment = dashboardFragment
    }
    
    override fun hideToolbar() {
        toolbar?.isVisible = false
    }

    override fun showMenu() {
        toolbarMenu?.let {
            menuInflater.inflate(R.menu.history_menu, it)
            convertMenuItem = it.findItem(R.id.action_convert)
            findMenuItem = it.findItem(R.id.action_tweak)
            closeMenuItem = it.findItem(R.id.action_close_selection)
        }
    }

    override fun hideMenu() {
        toolbarMenu?.clear()
    }

}