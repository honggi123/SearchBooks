package com.example.searchbooks.di

import com.example.searchbooks.data.repository.BookRepository
import com.example.searchbooks.data.repository.impl.BookRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindBookRepository(repository: BookRepositoryImpl): BookRepository
}