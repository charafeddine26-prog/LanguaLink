package com.example.langualink.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.langualink.data.repository.BadgeRepository
import com.example.langualink.data.repository.UserRepository
import com.example.langualink.model.UserWithBadges
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val badgeRepository: BadgeRepository
) : ViewModel() {

    private val _userWithBadges = MutableStateFlow<UserWithBadges?>(null)
    val userWithBadges: StateFlow<UserWithBadges?> = _userWithBadges

    init {
        viewModelScope.launch {
            // We assume user id is 1
            badgeRepository.getUserWithBadges(1).collect { userWithBadges ->
                Log.d("ProfileViewModel", "User with badges: $userWithBadges")
                _userWithBadges.value = userWithBadges
            }
        }
    }
}
