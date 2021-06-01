package com.platform45.fx45

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.platform45.fx45.ui.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), MyDrawerController{
    override lateinit var navController: NavController
    override var toolbarMenu: Menu? = null
    var convertMenuItem: MenuItem? = null
    var findMenuItem: MenuItem? = null
    var closeMenuItem: MenuItem? = null
    var dbf: DashboardFragment? = null

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

    override fun setTitle(title: String) {
       
    }

    override fun showMenu() {
       
    }

    override fun hideMenu() {
       
    }

    override fun setDashboardFragment(dashboardFragment: DashboardFragment) {
       
    }

    override fun showContent() {
       
    }

    override fun showSelectionMode() {
       
    }

    override fun hideToolbar() {
       
    }

}