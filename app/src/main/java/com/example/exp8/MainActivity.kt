package com.example.exp8

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exp8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var drawerToggle: ActionBarDrawerToggle

    private val exploreFragment = ExploreFragment()
    private val webViewFragment = WebViewFragment()
    private val favouritesFragment = FavouritesFragment()

    private var activeFragment: Fragment = exploreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Fix status bar and camera notch overlap using System Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainContent) { _, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            binding.toolbar.setPadding(
                0,
                statusBarInset,
                0,
                0
            )

            binding.bottomNavigation.setPadding(
                0,
                0,
                0,
                navBarInset
            )

            insets
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupNavigationDrawer()
        setupFragments()
        setupBottomNavigation()
        setupBackPressHandler()
    }

    private fun setupNavigationDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.menu_home,
            R.string.menu_home
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (menuItem.itemId) {
                R.id.nav_drawer_explore -> {
                    navigateToTab(R.id.nav_explore)
                    true
                }
                R.id.nav_drawer_web -> {
                    navigateToTab(R.id.nav_web)
                    true
                }
                R.id.nav_drawer_favourites -> {
                    navigateToTab(R.id.nav_favourites)
                    true
                }
                R.id.nav_drawer_about -> {
                    showAboutDialog()
                    true
                }
                R.id.nav_drawer_settings -> {
                    showSettingsDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, favouritesFragment, "FAVOURITES").hide(favouritesFragment)
            add(R.id.fragmentContainer, webViewFragment, "WEB").hide(webViewFragment)
            add(R.id.fragmentContainer, exploreFragment, "EXPLORE")
        }.commit()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> {
                    switchFragment(exploreFragment)
                    binding.navigationView.setCheckedItem(R.id.nav_drawer_explore)
                    true
                }
                R.id.nav_web -> {
                    switchFragment(webViewFragment)
                    binding.navigationView.setCheckedItem(R.id.nav_drawer_web)
                    true
                }
                R.id.nav_favourites -> {
                    switchFragment(favouritesFragment)
                    binding.navigationView.setCheckedItem(R.id.nav_drawer_favourites)
                    true
                }
                else -> false
            }
        }
    }

    fun navigateToTab(menuItemId: Int) {
        binding.bottomNavigation.selectedItemId = menuItemId
    }

    private fun switchFragment(targetFragment: Fragment) {
        if (activeFragment != targetFragment) {
            supportFragmentManager.beginTransaction().apply {
                hide(activeFragment)
                show(targetFragment)
            }.commit()
            activeFragment = targetFragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_options_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.queryHint = getString(R.string.search_hint)

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.setSearchQuery("")
                return true
            }
        })

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setSearchQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        searchView?.setOnCloseListener {
            viewModel.setSearchQuery("")
            false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return when (item.itemId) {
            R.id.nav_home -> {
                navigateToTab(R.id.nav_explore)
                true
            }
            R.id.nav_web_options -> {
                navigateToTab(R.id.nav_web)
                true
            }
            R.id.action_select_all -> {
                if (activeFragment != exploreFragment) {
                    navigateToTab(R.id.nav_explore)
                }
                val isSelected = viewModel.toggleSelectAll()
                val msg = if (isSelected) "Selected all destinations" else "Cleared all selections"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_share -> {
                shareApplicationInfo()
                true
            }
            R.id.action_refresh -> {
                if (activeFragment == webViewFragment) {
                    webViewFragment.reloadCurrentPage()
                    Toast.makeText(this, "Reloading web page...", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.resetGallery()
                    Toast.makeText(this, "Gallery reset and refreshed", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareApplicationInfo() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out Heritage & Travel Explorer!\nDiscover Indian heritage destinations, Gemini AI insights, and travel guides.\nExperiment 8 - MCA Lab"
            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share Application"))
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.about_title)
            .setMessage(R.string.about_message)
            .setIcon(R.drawable.ic_info)
            .setPositiveButton(R.string.btn_close, null)
            .show()
    }

    private fun showSettingsDialog() {
        val options = arrayOf(
            "Google Search Default (Enabled)",
            "Gemini AI Assistant (Active)",
            "Clear Image Disk Cache"
        )
        AlertDialog.Builder(this)
            .setTitle("Application Settings")
            .setItems(options) { _, which ->
                when (which) {
                    2 -> {
                        viewModel.resetGallery()
                        Toast.makeText(this, "Cache cleared & gallery reset", Toast.LENGTH_SHORT).show()
                    }
                    else -> Toast.makeText(this, "${options[which]} is active", Toast.LENGTH_SHORT).show()
                }
            }
            .setPositiveButton(R.string.btn_close, null)
            .show()
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 1. Close Navigation Drawer if open
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return
                }

                // 2. Handle WebView Back History if WebView tab is active
                if (activeFragment == webViewFragment) {
                    if (webViewFragment.canGoBack()) {
                        webViewFragment.goBack()
                        return
                    } else {
                        // No history -> navigate back to Explore tab
                        navigateToTab(R.id.nav_explore)
                        return
                    }
                }

                // 3. Handle Favourites tab -> navigate back to Explore tab
                if (activeFragment == favouritesFragment) {
                    navigateToTab(R.id.nav_explore)
                    return
                }

                // 4. When Explore tab is active, exit the application
                if (activeFragment == exploreFragment) {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}