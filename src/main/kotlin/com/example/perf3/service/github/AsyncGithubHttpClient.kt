package com.example.perf3.service.github

import com.example.perf3.domain.RawUser
import io.reactivex.Observable
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import org.springframework.web.client.AsyncRestTemplate

@Component
class AsyncGithubHttpClient(
        val api_github_users: String = "https://api.github.com/users/%s"
) {
    val log = LoggerFactory.getLogger(AsyncGithubHttpClient::class.java)

//    val API_GITHUB_USERS = "https://api.github.com/users/%s"
//    val API_GITHUB_FOLLOWERS = "$API_GITHUB_USERS/followers"
//    val API_GITHUB_REPOS = "$API_GITHUB_USERS/repos"

    private val restTemplate: AsyncRestTemplate = AsyncRestTemplate()

    fun getUser(login: String): Observable<RawUser> {
        log.info("Get user {}", login)


        return Observable.create { s ->
            run {
                log.info("Create ListenableFuture")
                val user: ListenableFuture<ResponseEntity<RawUser>> = restTemplate.getForEntity(String.format(api_github_users, login), RawUser::class.java)
                user.addCallback(object: ListenableFutureCallback<ResponseEntity<RawUser>?> {
                    override fun onSuccess(result: ResponseEntity<RawUser>?) {
                        log.info("Log from ListenableFuture callback for user {}", result?.body)
                        s.onNext(result!!.body!!)
                        s.onComplete()
                    }

                    override fun onFailure(ex: Throwable) {
                      s.onError(ex)
                    }
                })
            }
        }
    }

}