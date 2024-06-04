import android.app.Application
import androidx.room.Room
import com.example.mytimer.TimerDatabase

class AppDB : Application() {
    lateinit var database: TimerDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            TimerDatabase::class.java,
            "app_database"
        ).build()
    }
}