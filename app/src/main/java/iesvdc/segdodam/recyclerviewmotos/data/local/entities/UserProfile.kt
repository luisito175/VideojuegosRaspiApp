package iesvdc.segdodam.recyclerviewmotos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa el perfil del usuario.
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Usamos ID fijo 1 ya que solo hay un perfil de usuario local
    val name: String,
    val email: String,
    val photoUri: String? = null
)
