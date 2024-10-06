package com.example.carhive.Presentation.user.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.carhive.Presentation.user.viewModel.UserViewModel

@Composable
fun sellerScreen(
    viewModel: UserViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Seller")
        Button(onClick = {
            viewModel.onLogicClick()
            navigateToLogin()
        }) {
            Text("Sign out")
        }
    }

}