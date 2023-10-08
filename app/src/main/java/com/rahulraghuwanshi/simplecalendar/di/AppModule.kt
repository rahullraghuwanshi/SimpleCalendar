package com.rahulraghuwanshi.simplecalendar.di

import com.rahulraghuwanshi.simplecalendar.data.datasource.ApiConstants
import com.rahulraghuwanshi.simplecalendar.data.datasource.NetworkAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideDispatchersProvider(): DispatchersProvider {
//        return DispatcherProviderImpl()
//    }
    @Provides
    @Singleton
    fun provideAPIService(): NetworkAPI {
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(NetworkAPI::class.java)
    }

}