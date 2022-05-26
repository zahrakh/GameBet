package com.zahra.gamebet.predictionsgameapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.zahra.gamebet.BuildConfig
import com.zahra.gamebet.predictionsgameapp.data.local.MatchDao
import com.zahra.gamebet.predictionsgameapp.data.local.MatchDatabase
import com.zahra.gamebet.predictionsgameapp.data.local.MatchDatabase.Companion.DATABASE_NAME
import com.zahra.gamebet.predictionsgameapp.data.local.MatchesDao
import com.zahra.gamebet.predictionsgameapp.data.remote.*
import com.zahra.gamebet.predictionsgameapp.data.remote.Api.Companion.BASE_URL
import com.zahra.gamebet.predictionsgameapp.data.repository.GameRepositoryImp
import com.zahra.gamebet.predictionsgameapp.domain.repository.GameRepository
import com.zahra.gamebet.predictionsgameapp.domain.usecases.GetMatchesListUseCase
import com.zahra.gamebet.predictionsgameapp.domain.usecases.GetMatchesResultListUseCase
import com.zahra.gamebet.predictionsgameapp.domain.usecases.ResetMatchUseCase
import com.zahra.gamebet.predictionsgameapp.domain.usecases.UpdateMatchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("Game_Bet", Context.MODE_PRIVATE)
    }

    //Database
    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application): MatchDatabase {
        return Room.databaseBuilder(
            app,
            MatchDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(matchDatabase: MatchDatabase): MatchDao {
        return matchDatabase.dao
    }


    @Provides
    @Singleton
    fun provideMatchesDao(matchDatabase: MatchDatabase): MatchesDao {
        return matchDatabase.matchesDao
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("okHttpLog", message)
        }.apply {
            setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            )
        }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor { chain ->
//
//            }
            .addInterceptor(logging)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofitApi(client: OkHttpClient): Api {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(api: Api, stringProvider: StringProvider): NetworkDataSource {
        return NetworkDataSourceImpl(api, stringProvider)
    }

    @Provides
    fun provideStringProvider(@ApplicationContext appContext: Context): StringProvider {
        return StringProviderImpl(appContext)
    }

    @Provides
    @Singleton
    fun provideGameRepository(repositoryImp: GameRepositoryImp): GameRepository = repositoryImp

    @Provides
    @Singleton
    fun provideGetMatchesListUseCase(repository: GameRepository): GetMatchesListUseCase {
        return GetMatchesListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMatchesResultListUseCase(repository: GameRepository): GetMatchesResultListUseCase {
        return GetMatchesResultListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateMatchUseCase(repository: GameRepository): UpdateMatchUseCase {
        return UpdateMatchUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideResetMatchUseCase(repository: GameRepository): ResetMatchUseCase {
        return ResetMatchUseCase(repository)
    }
}