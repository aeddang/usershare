package com.kakaovx.homet.user.component.annotation

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ComponentScope(
    val value: String = ""
)