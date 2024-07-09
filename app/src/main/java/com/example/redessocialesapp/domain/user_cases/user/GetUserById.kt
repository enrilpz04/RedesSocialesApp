package com.example.redessocialesapp.domain.user_cases.user

import com.example.redessocialesapp.domain.repository.ArticleRepository
import com.example.redessocialesapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUserById @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(id: String) = repository.getUserById(id)
}