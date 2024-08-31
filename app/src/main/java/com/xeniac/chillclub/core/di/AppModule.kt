package com.xeniac.chillclub.core.di

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.xeniac.chillclub.core.data.local.ChillClubDao
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.serialization.json.Json
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(NotificationManager::class.java)
    } else context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(ConnectivityManager::class.java)
    } else context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideAudioManager(
        @ApplicationContext context: Context
    ): AudioManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getSystemService(AudioManager::class.java)
    } else context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(engineFactory = OkHttp) {
        expectSuccess = true

        install(Logging) {
            level = LogLevel.INFO
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(HttpCache)
        install(ContentNegotiation) {
            register(
                contentType = ContentType.Text.Plain,
                converter = KotlinxSerializationConverter(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    coerceInputValues = true
                    isLenient = true
                })
            )
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                coerceInputValues = true
                isLenient = true
            })
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            retryOnException(
                maxRetries = 3,
                retryOnTimeout = true
            )
            exponentialDelay()
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 20000 // 20 seconds
            requestTimeoutMillis = 20000 // 20 seconds
            socketTimeoutMillis = 20000 // 20 seconds
        }
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
        }
    }

    @Provides
    @Singleton
    fun provideChillClubDatabase(
        @ApplicationContext context: Context
    ): ChillClubDatabase = Room.databaseBuilder(
        context = context,
        klass = ChillClubDatabase::class.java,
        name = "ChillClub.db"
    ).build()

    @Provides
    @Singleton
    fun provideChillClubDao(
        database: ChillClubDatabase
    ): ChillClubDao = database.dao

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = synchronized(lock = SynchronizedObject()) {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "settings") }
        )
    }

    @Provides
    fun provideCurrentAppTheme(
        preferencesRepository: PreferencesRepository
    ): AppTheme = preferencesRepository.getCurrentAppThemeSynchronously()

    @Provides
    @Singleton
    fun provideDecimalFormat(): DecimalFormat = DecimalFormat(
        /* pattern = */ "00",
        /* symbols = */ DecimalFormatSymbols(Locale.US)
    )
}