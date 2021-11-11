package com.example.newdemo.di

import android.content.Context
import com.example.newdemo.NewDemoApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

//    @Provides
//    @ActivityScoped
//    fun provideContext(@ApplicationContext app: Context): NewDemoApplication {
//        return app as NewDemoApplication
//    }
}
