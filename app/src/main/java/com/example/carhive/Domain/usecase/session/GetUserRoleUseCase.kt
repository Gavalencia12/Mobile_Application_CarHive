package com.example.carhive.Domain.usecase.session

import com.example.carhive.Data.repository.SessionRepository

class GetUserRoleUseCase(private val repository: SessionRepository) {
    suspend operator fun invoke(userId:String): Result<Int?> {
        return repository.getUserRole(userId)
    }
}