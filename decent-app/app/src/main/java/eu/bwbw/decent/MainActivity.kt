package eu.bwbw.decent

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        // Preferences cleanup TODO REMOVE
        applicationContext.getSharedPreferences("eu.bwbw.decent.USER_DATA_KEY", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.welcomeFragment, R.id.receiverFragment, R.id.senderFragment, R.id.courierFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance())
            .get(MainActivityViewModel::class.java)

        setupListeners(navView)
        askForPermissions(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupListeners(navView: NavigationView) {
        findViewById<DrawerLayout>(R.id.drawer_layout).addDrawerListener(
            object : DrawerLayout.SimpleDrawerListener() {
                override fun onDrawerOpened(drawerView: View) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val headerView = navView.getHeaderView(0)
                        headerView.findViewById<TextView>(R.id.walletBalance).text =
                            getString(R.string.my_wallet_balance, viewModel.getWalletBalance())
                    }
                }
            })
    }

    private fun askForPermissions(activity: AppCompatActivity) {
        if (
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                APP_PERMISSIONS_REQUEST_CAMERA
            )
        }
    }

    companion object {
        const val APP_PERMISSIONS_REQUEST_CAMERA = 200
    }
}
