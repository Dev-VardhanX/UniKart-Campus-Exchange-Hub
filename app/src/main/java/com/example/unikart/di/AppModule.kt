package com.example.unikart.di

import android.app.Application
import com.example.unikart.data.repository.AuthRepositoryImpl
import com.example.unikart.data.repository.ItemRepositoryImpl
import com.example.unikart.domain.repository.AuthRepository
import com.example.unikart.domain.repository.ItemRepository
import com.example.unikart.domain.usecase.GetItemByIdUseCase
import com.example.unikart.domain.usecase.GetItemsUseCase
import com.example.unikart.domain.usecase.LoginUseCase
import com.example.unikart.domain.usecase.RegisterUseCase
import com.example.unikart.presentation.auth.GoogleAuthUIClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImpl(auth)
    }
    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSignInClient(app: Application): SignInClient {
        return Identity.getSignInClient(app)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUIClient(
        app: Application,
        signInClient: SignInClient
    ): GoogleAuthUIClient {
        return GoogleAuthUIClient(
            context = app,
            oneTapClient = signInClient
        )
    }


    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideItemRepository(
        firestore: FirebaseFirestore
    ): ItemRepository {
        return ItemRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideGetItemsUseCase(
        repository: ItemRepository
    ): GetItemsUseCase {
        return GetItemsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetItemByIdUseCase(
        repository: ItemRepository
    ): GetItemByIdUseCase {
        return GetItemByIdUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}