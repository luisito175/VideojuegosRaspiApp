package iesvdc.segdodam.recyclerviewmotos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import iesvdc.segdodam.recyclerviewmotos.data.local.dao.FavoriteVideoGameDao
import iesvdc.segdodam.recyclerviewmotos.data.local.entities.FavoriteVideoGame

@Database(entities = [FavoriteVideoGame::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteVideoGameDao(): FavoriteVideoGameDao
}
