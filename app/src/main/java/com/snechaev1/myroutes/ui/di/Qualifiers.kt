package com.snechaev1.myroutes.ui.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DbInMemory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Db


