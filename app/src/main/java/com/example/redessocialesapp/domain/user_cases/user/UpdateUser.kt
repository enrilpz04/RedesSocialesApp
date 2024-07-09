package com.example.redessocialesapp.domain.user_cases.user

import com.example.redessocialesapp.domain.model.User
import com.example.redessocialesapp.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUser @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(user: User) = repository.updateUser(user)
}