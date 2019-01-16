package com.kakaovx.homet.component.network
import android.support.v4.app.Fragment
import android.view.View
import com.kakaovx.homet.component.annotation.PageScope
import com.kakaovx.homet.component.annotation.UserScope
import com.kakaovx.homet.component.app.AppComponent
import com.kakaovx.homet.component.network.api.GitHubApi
import com.kakaovx.homet.page.PageNetworkTest
import dagger.Component

@PageScope
@Component(dependencies = [AppComponent::class], modules = [ApiModule::class])
interface ApiComponent {
    fun inject(context: View)
    fun inject(context: PageNetworkTest)
}