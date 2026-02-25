package iesvdc.segdodam.recyclerviewmotos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import iesvdc.segdodam.recyclerviewmotos.data.local.dao.FavoriteVideoGameDao
import iesvdc.segdodam.recyclerviewmotos.data.local.dao.UserProfileDao
import iesvdc.segdodam.recyclerviewmotos.data.local.entities.FavoriteVideoGame
import iesvdc.segdodam.recyclerviewmotos.data.local.entities.UserProfile

@Database(entities = [FavoriteVideoGame::class, UserProfile::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVideoGameDao(): FavoriteVideoGameDao
    abstract fun userProfileDao(): UserProfileDao
}
