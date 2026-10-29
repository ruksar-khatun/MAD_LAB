package com.example.exp11

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.exp11.databinding.ActivityMainBinding
import com.example.exp11.ui.fragments.ExploreFragment
import com.example.exp11.ui.fragments.ItineraryFragment
import com.example.exp11.ui.fragments.PassportFragment
import com.example.exp11.ui.fragments.ToursFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val exploreFragment = ExploreFragment()
    private val itineraryFragment = ItineraryFragment()
    private val toursFragment = ToursFragment()
    private val passportFragment = PassportFragment()

    private var activeFragment: Fragment = exploreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragmentContainer, passportFragment, "4").hide(passportFragment)
            add(R.id.fragmentContainer, toursFragment, "3").hide(toursFragment)
            add(R.id.fragmentContainer, itineraryFragment, "2").hide(itineraryFragment)
            add(R.id.fragmentContainer, exploreFragment, "1")
        }.commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> {
                    switchFragment(exploreFragment)
                    exploreFragment.loadDestinations()
                    true
                }
                R.id.nav_itinerary -> {
                    switchFragment(itineraryFragment)
                    itineraryFragment.loadItinerary()
                    true
                }
                R.id.nav_tours -> {
                    switchFragment(toursFragment)
                    toursFragment.loadTours()
                    true
                }
                R.id.nav_passport -> {
                    switchFragment(passportFragment)
                    passportFragment.loadPassportData()
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(targetFragment: Fragment) {
        if (activeFragment != targetFragment) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(targetFragment)
                .commit()
            activeFragment = targetFragment
        }
    }
}
