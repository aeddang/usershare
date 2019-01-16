package com.kakaovx.homet

import android.app.Application
import android.content.Context
import com.kakaovx.homet.component.app.AppComponent
import com.kakaovx.homet.component.app.AppModule
import com.kakaovx.homet.component.app.DaggerAppComponent
import com.kakaovx.homet.component.network.NetworkModule
import com.kakaovx.homet.component.preference.PreferenceModule

class App: Application() {

    val singleton: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .preferenceModule(PreferenceModule(this))
            .build()
    }

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            return (context.applicationContext as App).singleton
        }
    }
}