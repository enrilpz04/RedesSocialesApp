package com.example.redessocialesapp.domain.repository

import com.example.redessocialesapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserById(id: String) : Flow<User>

    fun updateUser(user: User) : Flow<Unit>
}