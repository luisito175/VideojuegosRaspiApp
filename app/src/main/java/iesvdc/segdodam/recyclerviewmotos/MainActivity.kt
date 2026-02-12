package iesvdc.segdodam.recyclerviewmotos

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import iesvdc.segdodam.recyclerviewmotos.databinding.ActivityMainBinding
import iesvdc.segdodam.recyclerviewmotos.databinding.NavHeaderBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

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
                R.id.motosListFragment, R.id.galleryFragment, R.id.slideshowFragment
            ), drawerLayout
        )

        // Conecta la Toolbar, el Drawer y el BottomNav con el NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)

        // 3. Lógica de la cabecera del Drawer
        setupNavHeader(navView)

        // 4. Navegación normal en la barra inferior
        bottomNavView.setOnItemSelectedListener { item ->
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as androidx.navigation.fragment.NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(item.itemId)
            true
        }
    }

    private fun setupNavHeader(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val headerBinding = NavHeaderBinding.bind(headerView)
        // Personalizar la cabecera con el nombre de usuario (en el futuro se podría pasar desde el Login)
        headerBinding.tvHeaderName.text = "Admin"
        headerBinding.tvHeaderEmail.text = "admin@concesionario.com"
    }

    // Infla el menú de opciones de la Toolbar (los 3 puntos)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    // Maneja los clics en el menú de opciones de la Toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    // Permite que el NavController maneje el botón "arriba"/"atrás" de la Toolbar
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
