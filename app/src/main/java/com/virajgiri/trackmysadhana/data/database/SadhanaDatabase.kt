package com.virajgiri.trackmysadhana.data.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.virajgiri.trackmysadhana.data.dao.SadhanaDao
import com.virajgiri.trackmysadhana.data.entity.JapEntry
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana
import com.virajgiri.trackmysadhana.data.entity.SubMantra

@Database(
    entities = [MukhyaSadhana::class, SubMantra::class, JapEntry::class],
    version = 4,
    exportSchema = false
)
abstract class SadhanaDatabase : RoomDatabase() {

    abstract fun sadhanaDao(): SadhanaDao

    companion object {
        @Volatile private var INSTANCE: SadhanaDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `sub_mantra` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `sadhana_id` INTEGER NOT NULL,
                        `type` TEXT NOT NULL,
                        `mantra_text` TEXT NOT NULL,
                        FOREIGN KEY(`sadhana_id`) REFERENCES `mukhya_sadhana`(`id`) ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_sub_mantra_sadhana_id` ON `sub_mantra` (`sadhana_id`)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE jap_entry ADD COLUMN sub_mantra_id INTEGER")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE mukhya_sadhana ADD COLUMN mukhya_mantra_text TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE sub_mantra ADD COLUMN daily_mala_target INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): SadhanaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SadhanaDatabase::class.java,
                    "sadhana_tracker.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
