package iesvdc.segdodam.recyclerviewmotos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import android.net.Uri
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import iesvdc.segdodam.recyclerviewmotos.databinding.ActivityMainBinding
import iesvdc.segdodam.recyclerviewmotos.databinding.NavHeaderBinding
import iesvdc.segdodam.recyclerviewmotos.ui.UserViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Configurar la Toolbar
        setSupportActionBar(binding.toolbar)

        // 2. Configurar Navegación
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavView = binding.bottomNavView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment.navController

        // Define los destinos de nivel superior. En estas pantallas se mostrará
        // el icono de menú (hamburguesa) en lugar de la flecha de "atrás".
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.videoGamesListFragment, R.id.galleryFragment, R.id.favoritesFragment
            ), drawerLayout
        )

        // Conecta la Toolbar, el Drawer y el BottomNav con el NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)

        // 3. Lógica de la cabecera del Drawer
        setupNavHeader(navView)


        userViewModel.logoutState.observe(this) { state ->
            when (state) {
                is UserViewModel.LogoutState.Success -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                is UserViewModel.LogoutState.Error -> {
                    // Podrías mostrar un mensaje si hace falta
                }
            }
        }

        // 4. Navegación normal en la barra inferior
        bottomNavView.setOnItemSelectedListener { item ->
            navController.navigate(item.itemId)
            true
        }

        navView.setNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.action_logout) {
                userViewModel.logout()
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            } else {
                val handled = NavigationUI.onNavDestinationSelected(item, navController)
                if (handled) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                handled
            }
        }
    }

    private fun setupNavHeader(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val headerBinding = NavHeaderBinding.bind(headerView)
        
        // Observar los cambios en el perfil del usuario
        userViewModel.userProfile.observe(this) { profile ->
            if (profile != null) {
                headerBinding.tvHeaderName.text = profile.name
                headerBinding.tvHeaderEmail.text = profile.email
                if (profile.photoUri != null) {
                    try {
                        headerBinding.ivHeaderImage.setImageURI(Uri.parse(profile.photoUri))
                    } catch (e: Exception) {
                        headerBinding.ivHeaderImage.setImageResource(R.drawable.ic_launcher_foreground)
                    }
                }
            } else {
                headerBinding.tvHeaderName.text = "Admin"
                headerBinding.tvHeaderEmail.text = "admin@concesionario.com"
            }
        }
    }

    // Infla el menú de opciones de la Toolbar (los 3 puntos)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Maneja los clics en el menú de opciones de la Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as androidx.navigation.fragment.NavHostFragment
            navHostFragment.navController.navigate(R.id.settingsFragment)
            return true
        }
        if (item.itemId == R.id.action_logout) {
            userViewModel.logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Permite que el NavController maneje el botón "arriba"/"atrás" de la Toolbar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
