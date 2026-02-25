package iesvdc.segdodam.recyclerviewmotos.data.local.dao

import androidx.room.*
import iesvdc.segdodam.recyclerviewmotos.data.local.entities.FavoriteVideoGame
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteVideoGameDao {
    @Query("SELECT * FROM favorite_video_games")
    fun getAllFavorites(): Flow<List<FavoriteVideoGame>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteVideoGame)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteVideoGame)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_video_games WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}
