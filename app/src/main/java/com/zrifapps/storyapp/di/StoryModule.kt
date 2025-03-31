package com.zrifapps.storyapp.di

import com.zrifapps.storyapp.data.story.datasources.remote.StoryApi
import com.zrifapps.storyapp.data.story.repository.StoryRepositoryImpl
import com.zrifapps.storyapp.domain.story.repository.StoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object StoryModule {

    @Provides
    @Singleton
    fun provideStoryApi(retrofit: Retrofit): StoryApi {
        return retrofit.create(StoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStoryRepository(storyApi: StoryApi): StoryRepository {
        return StoryRepositoryImpl(storyApi)
    }
}
