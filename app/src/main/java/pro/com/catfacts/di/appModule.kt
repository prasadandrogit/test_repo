package pro.com.catfacts.di

import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pro.com.catfacts.BuildConfig.FACTS_BASE_URL
import pro.com.catfacts.db.AppDatabase
import pro.com.catfacts.model.DataMapper
import pro.com.catfacts.network.ApiService
import pro.com.catfacts.views.RandomFactView
import pro.com.catfacts.views.RandomFactViewImpl
import pro.com.catfacts.views.RandomFactViewModel
import pro.com.catfacts.views.adapter.FactsRecyclerViewAdapter
import pro.com.catfacts.views.repo.RandomFactRepo
import pro.com.catfacts.views.repo.RandomFactRepoImpl
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    factory { FactsRecyclerViewAdapter() }
    factory<RandomFactView> { RandomFactViewImpl(adapter = get()) }
    viewModel { RandomFactViewModel(randomFactRepo = get()) }
    factory { DataMapper(dao = get()) }
    factory<RandomFactRepo> { RandomFactRepoImpl(apiService = get(), dataMapper = get()) }
    single { AppDatabase.getBTAppDatabase(context = get()).catDetailsDao() }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl(FACTS_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(ApiService::class.java)
    }
}