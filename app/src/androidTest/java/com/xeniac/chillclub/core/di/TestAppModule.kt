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
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
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
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
internal object TestAppModule {

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
            logger = Logger.ANDROID
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
    ): ChillClubDatabase = Room.inMemoryDatabaseBuilder(
        context = context,
        klass = ChillClubDatabase::class.java
    ).build()

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    @SettingsDataStoreQualifier
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = synchronized(lock = SynchronizedObject()) {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "Settings") }
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    @MusicPlayerDataStoreQualifier
    fun provideMusicPlayerDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = synchronized(lock = SynchronizedObject()) {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "MusicPlayer") }
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    @MiscellaneousDataStoreQualifier
    fun provideMiscellaneousDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = synchronized(lock = SynchronizedObject()) {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "Miscellaneous") }
        )
    }

    @Provides
    fun provideCurrentAppTheme(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): AppTheme = settingsDataStoreRepository.getCurrentAppThemeSynchronously()

    @Provides
    @Singleton
    fun provideDecimalFormat(): DecimalFormat = DecimalFormat(
        /* pattern = */ "00",
        /* symbols = */ DecimalFormatSymbols(Locale.US)
    )
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SettingsDataStoreQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MusicPlayerDataStoreQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MiscellaneousDataStoreQualifier