package com.example.mysocialapp2.di

import com.example.mysocialapp2.firebase.MyFirebase
import com.example.mysocialapp2.repository.Repository
import com.example.mysocialapp2.viewmodel.UserViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideFirebase():MyFirebase{
        return MyFirebase()
    }

    @Provides
    @Singleton
    fun provideRepo(myFirebase: MyFirebase):Repository{
        return Repository(myFirebase)
    }

    @Provides
    @Singleton
    fun provideFactory(repository: Repository):UserViewModelFactory{
        return UserViewModelFactory(repository)
    }

}