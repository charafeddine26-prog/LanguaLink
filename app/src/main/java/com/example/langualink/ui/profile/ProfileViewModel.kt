package com.example.langualink.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langualink.data.local.dao.UserDao
import com.example.langualink.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        // In a real app, you would fetch the user's progress from a repository.
        // For now, we'll just get the user from the database.
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _user.value = user
            }
        }
    }
}
