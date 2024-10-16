/**
 * Implementación principal del repositorio que coordina todas las operaciones con Firebase.
 *
 * Esta clase actúa como un punto centralizado para interactuar con las diferentes fuentes de datos
 * de Firebase, facilitando así las operaciones de autenticación, almacenamiento de datos de usuario
 * y gestión de archivos multimedia. Al hacerlo, proporciona una API unificada para que las capas
 * superiores de la aplicación puedan acceder a estas funcionalidades de manera coherente y sencilla.
 *
 * Esta clase implementa la interfaz [AuthRepository], lo que garantiza que las operaciones relacionadas
 * con la autenticación y los datos del usuario se gestionen de manera estandarizada. Se basa en el uso
 * de tres fuentes de datos específicas:
 * - Firebase Auth para operaciones de autenticación.
 * - Firebase Realtime Database para el almacenamiento y recuperación de datos de usuario.
 * - Firebase Storage para la gestión de archivos multimedia, como imágenes de perfil.
 */
package com.example.carhive.Data.datasource.remote

import android.net.Uri
import com.example.carhive.Data.datasource.remote.Firebase.FirebaseAuthDataSource
import com.example.carhive.Data.datasource.remote.Firebase.FirebaseDatabaseDataSource
import com.example.carhive.Data.datasource.remote.Firebase.FirebaseStorageDataSource
import com.example.carhive.Data.mapper.UserMapper
import com.example.carhive.Data.model.CarEntity
import com.example.carhive.Data.model.UserEntity
import com.example.carhive.Data.repository.AuthRepository
import com.example.carhive.Domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RepositoryImpl(
    private val auth: FirebaseAuth,                          // Instancia principal de Firebase Auth, utilizada para autenticar usuarios.
    private val database: FirebaseDatabase,                  // Instancia principal de Firebase Database, utilizada para operar con la base de datos en tiempo real.
    private val storage: FirebaseStorage,                    // Instancia principal de Firebase Storage, utilizada para almacenar archivos multimedia.
    private val dataSource: FirebaseDatabaseDataSource,      // Fuente de datos para operaciones de base de datos, maneja el almacenamiento y recuperación de datos de usuario.
    private val dataSourceAuth: FirebaseAuthDataSource,      // Fuente de datos para operaciones de autenticación, maneja el inicio de sesión y registro de usuarios.
    private val dataSourceStorage: FirebaseStorageDataSource, // Fuente de datos para operaciones de almacenamiento, maneja la subida y recuperación de archivos.
    private val userMapper: UserMapper                       // Mapper para convertir entre modelos de dominio (User) y datos (UserEntity).
) : AuthRepository {

    override suspend fun uploadProfileImage(userId: String, uri: Uri): Result<String> {
        return dataSourceStorage.uploadProfileImage(userId, uri)
    }

    override suspend fun saveUserToDatabase(userId: String, user: User): Result<Unit> {
        val userEntity = userMapper.mapToEntity(user) // Mapea el objeto User a UserEntity
        return dataSource.saveUserToDatabase(userId, userEntity)
    }

    override suspend fun getAllCarsFromDatabase(): Result<List<CarEntity>> {
        return dataSource.getAllCarsFromDatabase()
    }

    override suspend fun getUserData(userId: String): Result<List<UserEntity>> {
        return dataSource.getUserData(userId)
    }

    override suspend fun getUserRole(userId: String): Result<Int?> {
        return dataSource.getUserRole(userId)
    }

    override suspend fun loginUser(email: String, password: String): Result<String?> {
        return dataSourceAuth.loginUser(email, password)
    }

    override suspend fun registerUser(email: String, password: String): Result<String> {
        return dataSourceAuth.registerUser(email, password)
    }

    override suspend fun getCurrentUserId(): Result<String?> {
        return dataSourceAuth.getCurrentUserId()
    }
}
