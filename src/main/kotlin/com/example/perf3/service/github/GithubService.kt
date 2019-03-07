package com.example.perf3.service.github

import com.example.perf3.domain.GithubUser
import com.example.perf3.domain.RawUser
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GithubService(
        val httpClient: GithubHttpClient,
        val asynHttpClient: AsyncGithubHttpClient
) {

    fun getUser(name: String) = httpClient.getUser(name) ?: RawUser.empty()

    fun getUsers(): List<GithubUser> {
//        observable()
//                .subscribe(
//                        { item -> log.info("Log from subscribe={}", item) },
//                        { error -> log.error("Subscribe error", error) },
//                        { log.info("Subscribe done") }
//                )
        return observable().toList().blockingGet()
    }

    fun getObsUsers() = observable()

    private fun observable(): Observable<GithubUser> {
        val obs1 = asynHttpClient.getUser("falzm")
        val obs2 = asynHttpClient.getUser("kelokelo")
        val obs3 = asynHttpClient.getUser("sienio")
        val obs4 = asynHttpClient.getUser("davethomas11")
        val obs5 = asynHttpClient.getUser("n1trux")
        val obs6 = asynHttpClient.getUser("nodiscc")

        val observable = obs2
                .mergeWith(obs3)
                .mergeWith(obs4)
                .map { t: RawUser -> GithubUser(t, emptyList(), emptyList()) }
//                .observeOn(Schedulers.io())
        return observable
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

}