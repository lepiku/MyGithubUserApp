package id.oktoluqman.consumerapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.oktoluqman.consumerapp.model.GithubUser

class FavoriteViewModel : ViewModel() {
    private val listGithubUsers = MutableLiveData<List<GithubUser>>()

    fun setUsers(users: List<GithubUser>) {
        listGithubUsers.postValue(users)
    }

    fun getUsers(): LiveData<List<GithubUser>> {
        return listGithubUsers
    }
}