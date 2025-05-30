package com.example.attendify.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            val result=auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.sendEmailVerification()?.await()
            Result.success(auth.currentUser?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun logIn(email: String, password: String): Result<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }


    suspend fun isEmailVerified(): Boolean {
        auth.currentUser?.reload()?.await()
        return auth.currentUser?.isEmailVerified ?: false
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid


}










/*


suspend fun continueWithGoogle(credential: AuthCredential): Result<AuthResultData> {
    return try {
        val authResult = auth.signInWithCredential(credential).await()
        val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
        val firebaseUser = auth.currentUser

        Result.success(
            AuthResultData(
                isNewUser = isNewUser,
                displayName = firebaseUser?.displayName,
                email = firebaseUser?.email,
                uid = firebaseUser?.uid
            )
        )
    } catch (e: Exception) {
        Result.failure(e)
    }



}





suspend fun resetPassword(email: String): Result<Unit> {
    return try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun signOut(): Result<Unit> {
    return try {
        auth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

 */