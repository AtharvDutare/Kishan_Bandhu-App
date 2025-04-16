package com.example.trysample.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trysample.Helper
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val authService = AuthService()

    // Authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    private val firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    // Current user
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()


    private val _currentUserImage = MutableStateFlow<String>("")
    val currentUserImage : StateFlow<String> get() = _currentUserImage.asStateFlow()


    init {
        // Check if user is already signed in
        _currentUser.value = authService.getCurrentUser()
        if (_currentUser.value != null) {
            _authState.value = AuthState.SignedIn(_currentUser.value!!)
        }

        // Observe authentication state changes
        viewModelScope.launch {
            authService.observeAuthStateChanges().collect { user ->
                _currentUser.value = user
                _authState.value = if (user != null) {
                    AuthState.SignedIn(user)
                } else {
                    AuthState.SignedOut
                }
            }


            _currentUser.collectLatest {currentUser->
                currentUser?.email?.let {
                    fetchUserImage(Helper.getUserId(it))
                }
            }
    }
    }

    fun fetchUserImage(currentUserId : String){
        viewModelScope.launch {
            Log.d("UserImage","Fetching user image ")
            val fetchedUserImage =  firebaseFirestore.collection("user_images").document(currentUserId).get()
            Log.d("UserImage","Fetched User Image")

        }
    }

    fun updateImage(currentUserImage : String){
        viewModelScope.launch {
            _currentUser.value?.email?.let {
                val userId = Helper.getUserId(it)
                firebaseFirestore.collection("user_images").document(userId).set(mapOf("image" to currentUserImage))
            }

        }
    }

    /**
     * Creates a new user account with email and password
     *
     * @param fullName User's full name
     * @param email User's email address
     * @param password User's password
     */
    fun createUserWithEmailAndPassword(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            authService.createUserWithEmailAndPassword(fullName, email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                    _authState.value = AuthState.SignedIn(user)
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Authentication failed")
                }
        }
    }

    /**
     * Signs in a user with email and password
     *
     * @param email User's email address
     * @param password User's password
     */
    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            authService.signInWithEmailAndPassword(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState.SignedIn(user)
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Authentication failed")
                }
        }
    }

    /**
     * Signs out the current user
     * This method updates the authentication state to SignedOut
     */
    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authService.signOut()
            _authState.value = AuthState.SignedOut
            _currentUser.value = null
        }
    }

}

/**
 * Represents the different states of authentication
 */
sealed class AuthState {
    /**
     * Initial state before any authentication attempt
     */
    object Initial : AuthState()

    /**
     * Loading state during authentication operations
     */
    object Loading : AuthState()

    /**
     * State when user is signed in
     *
     * @param user The FirebaseUser object representing the signed-in user
     */
    data class SignedIn(val user: FirebaseUser) : AuthState()

    /**
     * State when user is signed out
     */
    object SignedOut : AuthState()

    /**
     * State when email verification has been sent
     *
     * @param user The FirebaseUser object that verification was sent to
     */
    data class EmailVerificationSent(val user: FirebaseUser) : AuthState()

    /**
     * State when an error occurs during authentication
     *
     * @param message The error message
     */
    data class Error(val message: String) : AuthState()
} 