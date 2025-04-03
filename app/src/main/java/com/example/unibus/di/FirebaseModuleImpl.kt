package com.example.unibus.di

import com.example.unibus.data.repository.AccountRepositoryImpl
import com.example.unibus.data.repository.StorageFirebaseRepositoryImpl
import com.example.unibus.domain.AccountRepository
import com.example.unibus.domain.StorageFirebaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModuleImpl {
    @Binds
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun provideStorageService(impl: StorageFirebaseRepositoryImpl): StorageFirebaseRepository

}