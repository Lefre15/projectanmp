package com.example.projectanmp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectanmp.model.HabitDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
    private val db = HabitDatabase.getDatabase(application)

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage
    private val _sessionStatus = MutableLiveData<Boolean>()
    val sessionStatus: LiveData<Boolean> get() = _sessionStatus

    fun login(username: String, password: String) {
        launch {
            val user = db.userDao().login(username, password)
            if (user != null) {
                db.userDao().logoutAll()
                user.isLoggedIn = true
                db.userDao().update(user)
                _loginStatus.postValue(true)
                _errorMessage.postValue(null)
            } else {
                _loginStatus.postValue(false)
                _errorMessage.postValue("Invalid username or password")
            }
        }
    }
    fun checkSession() {
        launch {
            val user = db.userDao().getLoggedInUser()
            _sessionStatus.postValue(user != null)
        }

    }
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
