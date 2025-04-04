package com.example.attendify.data.module

import com.example.attendify.data.repository.FirestoreRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides fun providesAuth(): FirebaseAuth = Firebase.auth

    @Provides fun providesFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides fun providesFirestoreRepository(db: FirebaseFirestore): FirestoreRepository {
        return FirestoreRepository(db)
    }
}