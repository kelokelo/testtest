package com.example.perf3.service.github

import com.example.perf3.domain.RawUser
import com.example.perf3.domain.Repository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GithubHttpClient {
    val log = LoggerFactory.getLogger(GithubHttpClient::class.java)

    val API_GITHUB_USERS = "https://api.github.com/users/%s"
    val API_GITHUB_FOLLOWERS = "$API_GITHUB_USERS/followers"
    val API_GITHUB_REPOS = "$API_GITHUB_USERS/repos"

    private val restTemplate: RestTemplate = RestTemplate()

    fun getUser(login: String): RawUser? {
        log.info("Get user {}", login)
        return restTemplate.getForObject(String.format(API_GITHUB_USERS, login), RawUser::class.java)
    }

    fun getFollowers(login: String): List<RawUser> {
        log.info("Get followers {}", login)
        val array = restTemplate.getForObject<Array<RawUser>>(String.format(API_GITHUB_FOLLOWERS, login), Array<RawUser>::class.java)
        return array?.toList()?: emptyList()
    }

    fun getRepositories(login: String): List<Repository> {
        log.info("Get repos {}", login)
        val arrayOfRepositorys = restTemplate.getForObject<Array<Repository>>(String.format(API_GITHUB_REPOS, login), Array<Repository>::class.java)
        return arrayOfRepositorys?.toList()?: emptyList()
    }


}