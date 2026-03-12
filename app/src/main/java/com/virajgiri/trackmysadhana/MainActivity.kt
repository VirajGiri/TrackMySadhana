package com.virajgiri.trackmysadhana

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.virajgiri.trackmysadhana.databinding.ActivityMainBinding
import com.virajgiri.trackmysadhana.ui.addsadhana.AddSadhanaActivity
import com.virajgiri.trackmysadhana.ui.dashboard.DashboardFragment
import com.virajgiri.trackmysadhana.ui.history.HistoryFragment
import com.virajgiri.trackmysadhana.ui.reports.ReportsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (savedInstanceState == null) {
            loadFragment(DashboardFragment(), R.id.nav_dashboard)
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> { loadFragment(DashboardFragment(), item.itemId); true }
                R.id.nav_history   -> { loadFragment(HistoryFragment(),   item.itemId); true }
                R.id.nav_reports   -> { loadFragment(ReportsFragment(),   item.itemId); true }
                else -> false
            }
        }

        binding.fabAddSadhana.setOnClickListener {
            startActivity(Intent(this, AddSadhanaActivity::class.java))
        }
    }

    private fun loadFragment(fragment: Fragment, navId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        binding.fabAddSadhana.visibility =
            if (navId == R.id.nav_dashboard) View.VISIBLE else View.GONE
    }
}
