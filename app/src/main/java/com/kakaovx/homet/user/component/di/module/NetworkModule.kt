package com.kakaovx.homet.user.component.di.module

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakaovx.homet.user.component.di.annotation.PageScope
import com.kakaovx.homet.user.component.network.api.RestfulApi
import com.kakaovx.homet.user.component.network.error.Rx2ErrorHandlingCallAdapterFactory
import com.kakaovx.homet.user.constant.AppFeature
import com.kakaovx.homet.user.constant.NetworkConst
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


@Module
class NetworkModule {

    private val CONNECT_TIMEOUT: Long = 30
    private val WRITE_TIMEOUT: Long = 30
    private val READ_TIMEOUT: Long = 30

    private val TAG = javaClass.simpleName

    private val baseUrl: String = NetworkConst.HOMET_DEFAULT_REST_ADDRESS

    @Provides
    @PageScope
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10MB
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @PageScope
    fun provideGs(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @PageScope
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
    @PageScope
    fun provideRetrofit(gs: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(Rx2ErrorHandlingCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gs))
            .baseUrl(baseUrl)
            .client(client)
            .build()
    }

    @Provides
    @PageScope
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val builder: Request.Builder = it.request().newBuilder()
            builder.header("User-Agent", "Android")
            it.proceed(builder.build())
        }
    }

    @Provides
    @PageScope
    fun provideRestApi(retrofit: Retrofit): RestfulApi {
        return retrofit.create(RestfulApi::class.java)
    }
}