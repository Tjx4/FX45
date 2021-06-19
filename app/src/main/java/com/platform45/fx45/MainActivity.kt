package com.platform45.fx45

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.platform45.fx45.ui.dashboard.DashboardFragment
import com.platform45.fx45.ui.dashboard.DashboardFragmentDirections
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), MyDrawerController{
    override lateinit var navController: NavController
    override var toolbarMenu: Menu? = null
    private var convertMenuItem: MenuItem? = null
    private var findMenuItem: MenuItem? = null
    private var closeMenuItem: MenuItem? = null
    private var dbFragment: DashboardFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar)
        navController = this.findNavController(R.id.navControllerFragment)
        setupWithNavController(toolbar, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navControllerFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        convertMenuItem = menu.findItem(R.id.action_convert)
        findMenuItem = menu.findItem(R.id.action_refresh)
        closeMenuItem = menu.findItem(R.id.action_close_selection)
        toolbarMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_convert -> {
                hideActionBarIcon()
                val action = DashboardFragmentDirections.dashboardToConversion(null, null)
                navController.navigate(action)
            }
            R.id.action_refresh -> dbFragment?.refresh()
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
        flLoader.visibility = View.GONE
    }

    override fun showSelectionMode() {
        findMenuItem?.isVisible = false
        closeMenuItem?.isVisible = true
        convertMenuItem?.isVisible = false
        flLoader?.visibility = View.GONE
    }

    override fun setDashboardFragment(dashboardFragment: DashboardFragment) {
        dbFragment = dashboardFragment
    }
    
    override fun showLoading() {
        flLoader?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        flLoader?.visibility = View.GONE
    }

    override fun showMenu() {
        toolbarMenu?.let {
            menuInflater.inflate(R.menu.history_menu, it)
            convertMenuItem = it.findItem(R.id.action_convert)
            findMenuItem = it.findItem(R.id.action_refresh)
            closeMenuItem = it.findItem(R.id.action_close_selection)
        }
    }

    override fun hideMenu() {
         toolbarMenu?.clear()
    }

    override fun showActionBarIcon() {
        toolbar?.setLogo(R.drawable.logo_small)
    }

    override fun hideActionBarIcon() {
        toolbar?.logo = ColorDrawable(resources.getColor(android.R.color.transparent))
    }

    override fun onBackPressed() {
        if(dbFragment?.clPairSeriesInfo?.visibility == View.INVISIBLE){
            dbFragment?.showPairSeriesInfo()
        }
        else{
            super.onBackPressed()
        }
    }

}