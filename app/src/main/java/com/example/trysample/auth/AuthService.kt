package com.example.trysample.auth

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Service class that handles Firebase Authentication operations
 */
class AuthService {
    // Firebase Auth instance
    private val auth: FirebaseAuth = Firebase.auth
    
    /**
     * Creates a new user account with email and password
     * 
     * @param fullName User's full name
     * @param email User's email address
     * @param password User's password
     * @return Result containing the FirebaseUser on success or an exception on failure
     */
    suspend fun createUserWithEmailAndPassword(
        fullName: String,
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            // First create the user
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!
            
            // Update the user's display name
            val profileUpdates = userProfileChangeRequest {
                displayName = fullName
            }
            
            // Wait for the profile update to complete
            user.updateProfile(profileUpdates).await()
            
            // Reload the user to get the updated profile
            user.reload().await()
            
            // Return the updated user
            Result.success(auth.currentUser!!)
        } catch (e: Exception) {
            Log.e(TAG, "createUserWithEmail:failure", e)
            Result.failure(e)
        }
    }
    
    /**
     * Signs in a user with email and password
     * 
     * @param email User's email address
     * @param password User's password
     * @return Result containing the FirebaseUser on success or an exception on failure
     */
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Log.e(TAG, "signInWithEmail:failure", e)
            Result.failure(e)
        }
    }
    
    /**
     * Signs out the current user
     */
    fun signOut() {
        auth.signOut()
    }
    
    /**
     * Gets the current user if signed in
     * 
     * @return The current FirebaseUser or null if not signed in
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    
    /**
     * Sends email verification to the current user
     * 
     * @return Result indicating success or failure
     */
    suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("No user signed in"))
            user.sendEmailVerification().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "sendEmailVerification:failure", e)
            Result.failure(e)
        }
    }
    
    /**
     * Observes authentication state changes
     * 
     * @return Flow of FirebaseUser that emits the current user or null when auth state changes
     */
    fun observeAuthStateChanges(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        
        auth.addAuthStateListener(authStateListener)
        
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
    
    companion object {
        private const val TAG = "AuthService"
    }
} 