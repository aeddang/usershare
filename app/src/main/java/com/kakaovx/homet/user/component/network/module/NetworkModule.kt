package com.kakaovx.homet.user.component.network.module

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakaovx.homet.user.component.network.error.Rx2ErrorHandlingCallAdapterFactory
import com.kakaovx.homet.user.util.AppConst
import com.kakaovx.homet.user.util.AppFeature
import com.kakaovx.homet.user.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CONNECT_TIMEOUT: Long = 30
private const val WRITE_TIMEOUT: Long = 30
private const val READ_TIMEOUT: Long = 30

@Module
class NetworkModule {

    private val TAG = javaClass.simpleName

    private val baseUrl: String = AppConst.HOMET_DEFAULT_REST_ADDRESS

    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10MB
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideGs(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
        val logger = HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger { message ->
                var parse_message = message
                Log.d(TAG, parse_message)
                if (parse_message.contains("END")) {
                    Log.d(TAG, "\n")
                    parse_message += "\n"
                }
            })
        if (AppFeature.APP_LOG_DEBUG) {
            logger.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(CookieManager(null, CookiePolicy.ACCEPT_ALL)))
            .addInterceptor(logger)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gs: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(Rx2ErrorHandlingCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gs))
            .baseUrl(baseUrl)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val builder: Request.Builder = it.request().newBuilder()
            builder.header("User-Agent", "Android")
            it.proceed(builder.build())
        }
    }
}