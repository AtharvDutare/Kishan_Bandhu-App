package com.example.trysample.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.trysample.auth.AuthState
import com.example.trysample.auth.AuthViewModel
import com.example.trysample.ui.screens.auth.LoginScreen
import com.example.trysample.ui.screens.auth.SignUpScreen

const val AUTH_ROUTE = "auth"      // The main route for the authentication navigation graph
const val LOGIN_ROUTE = "login"    // Route for the login screen
const val SIGNUP_ROUTE = "signup"  // Route for the signup screen


fun NavGraphBuilder.authNavigation(
    navController: NavController,
    onAuthSuccess: () -> Unit
) {
    // Create a nested navigation graph for authentication
    // This allows the auth flow to be self-contained and reusable
    navigation(startDestination = LOGIN_ROUTE, route = AUTH_ROUTE) {
        // Define the login screen destination
        composable(LOGIN_ROUTE) {
            // Get the AuthViewModel
            val authViewModel: AuthViewModel = viewModel()
            val authState by authViewModel.authState.collectAsState()
            
            // Handle authentication state changes
            LaunchedEffect(authState) {
                when (authState) {
                    is AuthState.SignedIn -> {
                        onAuthSuccess()
                    }
                    is AuthState.Error -> {
                        // Show error message
                        val errorMessage = (authState as AuthState.Error).message
                        Toast.makeText(
                            navController.context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {} // Handle other states if needed
                }
            }
            
            LoginScreen(
                // Callback for when the user attempts to log in
                onLoginClick = { email, password ->
                    authViewModel.signInWithEmailAndPassword(email, password)
                },
                // Callback for when the user wants to navigate to the signup screen
                onSignUpClick = {
                    // Navigate to the signup screen within the auth graph
                    navController.navigate(SIGNUP_ROUTE)
                }
            )
        }
        
        // Define the signup screen destination
        composable(SIGNUP_ROUTE) {
            // Get the AuthViewModel
            val authViewModel: AuthViewModel = viewModel()
            val authState by authViewModel.authState.collectAsState()
            
            // Handle authentication state changes
            LaunchedEffect(authState) {
                when (authState) {
                    is AuthState.SignedIn -> {
                        onAuthSuccess()
                    }
                    is AuthState.Error -> {
                        // Show error message
                        val errorMessage = (authState as AuthState.Error).message
                        Toast.makeText(
                            navController.context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {} // Handle other states if needed
                }
            }
            
            SignUpScreen(
                // Callback for when the user attempts to sign up
                onSignUpClick = { fullName, email, password, confirmPassword ->
                    // Validate passwords match
                    if (password != confirmPassword) {
                        Toast.makeText(
                            navController.context,
                            "Passwords do not match",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@SignUpScreen
                    }
                    
                    // Create user with email and password
                    authViewModel.createUserWithEmailAndPassword(fullName, email, password)
                },
                // Callback for when the user wants to go back to the login screen
                onLoginClick = {
                    // Navigate back to the previous screen (login)
                    navController.navigateUp()
                }
            )
        }
    }
} 