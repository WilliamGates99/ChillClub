package com.xeniac.chillclub.core.di

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.net.ConnectivityManager
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessaging
import com.xeniac.chillclub.BuildConfig
import com.xeniac.chillclub.core.data.local.ChillClubDatabase
import com.xeniac.chillclub.core.domain.models.AppTheme
import com.xeniac.chillclub.core.domain.models.MiscellaneousPreferences
import com.xeniac.chillclub.core.domain.models.MiscellaneousPreferencesSerializer
import com.xeniac.chillclub.core.domain.models.MusicPlayerPreferences
import com.xeniac.chillclub.core.domain.models.MusicPlayerPreferencesSerializer
import com.xeniac.chillclub.core.domain.models.PermissionsPreferences
import com.xeniac.chillclub.core.domain.models.PermissionsPreferencesSerializer
import com.xeniac.chillclub.core.domain.models.SettingsPreferences
import com.xeniac.chillclub.core.domain.models.SettingsPreferencesSerializer
import com.xeniac.chillclub.core.domain.repositories.SettingsDataStoreRepository
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
import io.ktor.client.plugins.cache.storage.FileStorage
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = context.getSystemService(NotificationManager::class.java)

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)

    @Provides
    @Singleton
    fun provideAudioManager(
        @ApplicationContext context: Context
    ): AudioManager = context.getSystemService(AudioManager::class.java)

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        coerceInputValues = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context,
        json: Json
    ): HttpClient = HttpClient(engineFactory = OkHttp) {
        expectSuccess = true

        install(Logging) {
            logger = Logger.ANDROID
            level = if (BuildConfig.DEBUG) LogLevel.INFO else LogLevel.NONE
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(HttpCache) {
            val cacheDir = context.cacheDir.resolve(relative = "ktor_cache")
            privateStorage(FileStorage(directory = cacheDir))
        }
        install(ContentNegotiation) {
            register(
                contentType = ContentType.Text.Plain,
                converter = KotlinxSerializationConverter(format = json)
            )
            json(json = json)
        }
        install(HttpRequestRetry) {
            retryOnExceptionOrServerErrors(maxRetries = 3)
            exponentialDelay()
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 30_000 // 30 seconds
            requestTimeoutMillis = 30_000 // 30 seconds
            socketTimeoutMillis = 30_000 // 30 seconds
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

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    fun providePermissionDataStore(
        @ApplicationContext context: Context
    ): DataStore<PermissionsPreferences> = synchronized(lock = SynchronizedObject()) {
        DataStoreFactory.create(
            serializer = PermissionsPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { PermissionsPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "Permissions.pb") }
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): DataStore<SettingsPreferences> = synchronized(lock = SynchronizedObject()) {
        DataStoreFactory.create(
            serializer = SettingsPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { SettingsPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "Settings.pb") }
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideMiscellaneousDataStore(
        @ApplicationContext context: Context
    ): DataStore<MiscellaneousPreferences> = synchronized(lock = SynchronizedObject()) {
        DataStoreFactory.create(
            serializer = MiscellaneousPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { MiscellaneousPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "Miscellaneous.pb") }
        )
    }

    @OptIn(InternalCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideMusicPlayerDataStore(
        @ApplicationContext context: Context
    ): DataStore<MusicPlayerPreferences> = synchronized(lock = SynchronizedObject()) {
        DataStoreFactory.create(
            serializer = MusicPlayerPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { MusicPlayerPreferences() },
            scope = CoroutineScope(context = Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name = "MusicPlayer.pb") }
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    fun provideAppTheme(
        settingsDataStoreRepository: SettingsDataStoreRepository
    ): AppTheme = settingsDataStoreRepository.getCurrentAppThemeSynchronously()

    @Provides
    @Singleton
    fun provideDecimalFormat(): DecimalFormat = DecimalFormat(
        /* pattern = */ "00",
        /* symbols = */ DecimalFormatSymbols(Locale.US)
    )
}