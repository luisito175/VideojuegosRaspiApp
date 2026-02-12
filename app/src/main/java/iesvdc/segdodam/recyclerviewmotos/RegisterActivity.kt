package iesvdc.segdodam.recyclerviewmotos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity de compatibilidad tras eliminar Firebase.
 */
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}
