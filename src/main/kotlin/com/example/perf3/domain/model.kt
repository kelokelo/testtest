package com.example.perf3.domain

data class GithubUser(
        val user: RawUser,
        val followers: List<RawUser>,
        val repositories: List<Repository>
)

data class Repository(
        val name: String,
        val url: String
)

data class RawUser(
        val login: String,
        val id: Int,
        val name: String?,
        val avatarUrl: String?
){
    companion object {
        fun empty() = RawUser("", 0, "" ,"")
    }
}