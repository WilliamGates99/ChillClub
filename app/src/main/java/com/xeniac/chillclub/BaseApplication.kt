package com.xeniac.chillclub

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setAppTheme()
    }

    private fun setupTimber() = Timber.plant(Timber.DebugTree())

    private fun setAppTheme() = AppCompatDelegate.setDefaultNightMode(
        /* mode = */ AppCompatDelegate.MODE_NIGHT_YES
    )

    override fun newImageLoader(): ImageLoader = ImageLoader(context = this).newBuilder().apply {
        components {
            add(factory = SvgDecoder.Factory())
            add(
                factory = if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)) {
                    ImageDecoderDecoder.Factory()
                } else {
                    GifDecoder.Factory()
                }
            )
        }
        crossfade(enable = true)
        // Ignore the network cache headers and always read from/write to the disk cache.
        respectCacheHeaders(enable = false)
        memoryCachePolicy(policy = CachePolicy.ENABLED)
        diskCachePolicy(policy = CachePolicy.ENABLED)
        memoryCache {
            MemoryCache.Builder(this@BaseApplication)
                // Set the max size to 25% of the app's available memory.
                .maxSizePercent(percent = 0.25)
                .build()
        }
        diskCache {
            DiskCache.Builder()
                // Set cache directory folder name
                .directory(cacheDir.resolve("image_cache"))
                .maxSizePercent(percent = 0.03)
                .build()
        }
        okHttpClient {
            // Don't limit concurrent network requests by host.
            val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }

            // Lazily create the OkHttpClient that is used for network operations.
            OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .build()
        }
        if (BuildConfig.DEBUG) logger(logger = DebugLogger())
    }.build()
}