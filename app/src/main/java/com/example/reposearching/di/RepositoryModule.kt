package com.example.reposearching.di

import com.example.reposearching.data.repository.GithubRepositoryImpl
import com.example.reposearching.data.repository.MockGithubRepositoryImpl
import com.example.reposearching.domain.repository.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // 💡【正常開發情境】請解開以下註解，並將 Mock 的註解起來
     @Binds
     abstract fun bindGithubRepository(
         githubRepositoryImpl: GithubRepositoryImpl
     ): GithubRepository

    // 💡【假資料測試情境】如果要使用 Mock API，請解開此設定
//    @Binds
//    abstract fun bindMockGithubRepository(
//        mockGithubRepositoryImpl: MockGithubRepositoryImpl
//    ): GithubRepository
}
