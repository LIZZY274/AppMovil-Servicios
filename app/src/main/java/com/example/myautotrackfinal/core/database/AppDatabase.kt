package com.example.myautotrackfinal.core.database

import androidx.room.*
import android.content.Context
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val token: String?,
    val isLoggedIn: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "services_offline",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["createdAt"]),
        Index(value = ["needsSync"])
    ]
)
data class ServiceEntity(
    @PrimaryKey
    val id: String,
    val tipo: String,
    val fecha: String,
    val costo: Double,
    val taller: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val userId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val needsSync: Boolean = false,
    val syncAction: String = "CREATE"
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getLoggedInUser(): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()

    @Query("UPDATE users SET isLoggedIn = 1 WHERE id = :userId")
    suspend fun setUserLoggedIn(userId: String)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: String)
}

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services_offline WHERE userId = :userId ORDER BY createdAt DESC")
    fun getServicesByUser(userId: String): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services_offline WHERE userId = :userId AND needsSync = 1")
    suspend fun getUnsyncedServices(userId: String): List<ServiceEntity>

    @Query("SELECT * FROM services_offline WHERE id = :id")
    suspend fun getServiceById(id: String): ServiceEntity?

    @Query("SELECT COUNT(*) FROM services_offline WHERE userId = :userId")
    suspend fun getServiceCount(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Update
    suspend fun updateService(service: ServiceEntity)

    @Query("UPDATE services_offline SET isSynced = 1, needsSync = 0 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("UPDATE services_offline SET needsSync = 1, syncAction = :action WHERE id = :id")
    suspend fun markForSync(id: String, action: String)

    @Delete
    suspend fun deleteService(service: ServiceEntity)

    @Query("DELETE FROM services_offline WHERE id = :id")
    suspend fun deleteServiceById(id: String)

    @Query("DELETE FROM services_offline WHERE userId = :userId")
    suspend fun deleteAllUserServices(userId: String)

    @Query("DELETE FROM services_offline WHERE isSynced = 1")
    suspend fun deleteSyncedServices()
}

@Database(
    entities = [UserEntity::class, ServiceEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun serviceDao(): ServiceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "autotrack_simple_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}


class DatabaseConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}