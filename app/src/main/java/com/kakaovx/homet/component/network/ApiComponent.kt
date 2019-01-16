package com.kakaovx.homet.component.network
import android.view.View
import com.kakaovx.homet.component.annotation.PageScope
import com.kakaovx.homet.component.app.AppComponent
import com.kakaovx.homet.component.ui.ApiPageFragment
import dagger.Component

@PageScope
@Component(dependencies = [AppComponent::class], modules = [ApiModule::class])
interface ApiComponent {
    fun inject(context: View)
    fun inject(context: ApiPageFragment)
}