package com.example.carhive.Presentation.user.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carhive.Data.model.UserEntity
import com.example.carhive.Domain.usecase.auth.GetCurrentUserIdUseCase
import com.example.carhive.Domain.usecase.database.GetUserDataUseCase
import com.example.carhive.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _userData = MutableLiveData<Result<List<UserEntity>>>()
    val userData: LiveData<Result<List<UserEntity>>> get() = _userData

    // Método para obtener los datos del usuario
    fun fetchUserData() {
        viewModelScope.launch {
            val userIdResult = getCurrentUserIdUseCase() // Obtén el ID del usuario
            val userId = userIdResult.getOrNull()
            Log.i("angel", "es $userId")
            userIdResult.onSuccess {
                if (userId != null) {
                    val result = getUserDataUseCase(userId) // Usa el ID para obtener los datos del usuario
                    _userData.value = result
                    Log.i("angel", "datos $result")
                } else {
                    _userData.value = Result.failure(Exception("No se pudo obtener el ID del usuario"))
                }
            }.onFailure {
                _userData.value = Result.failure(it)
            }
        }
    }

    // Listas de opciones y sus íconos
    val profileOptions = listOf(
        "Notificaciones",
        "Modo pantalla",
        "Idioma",
        "Reportar un problema",
        "Ayuda",
        "Términos y condiciones",
        "Quieres ser un Vendedor?",
        "Cerrar sesión"
    )

    val profileIcons = listOf(
        R.drawable.ic_notifications,
        R.drawable.ic_mode_screen,
        R.drawable.ic_language,
        R.drawable.ic_report_problem,
        R.drawable.ic_help,
        R.drawable.ic_terms,
        R.drawable.ic_change_account,
        R.drawable.ic_logout
    )

    // Método para manejar el cierre de sesión
    fun logout() {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
        }
    }

}
