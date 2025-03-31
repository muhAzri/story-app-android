package com.zrifapps.storyapp.di

import com.zrifapps.storyapp.data.auth.datasources.remote.AuthApi
import com.zrifapps.storyapp.data.auth.repository.AuthRepositoryImpl
import com.zrifapps.storyapp.domain.auth.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(authApi)
    }

}
