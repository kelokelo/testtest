package com.example.perf3.service.github

import com.example.perf3.domain.GithubUser
import com.example.perf3.domain.RawUser
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class GithubService(
        val httpClient: GithubHttpClient,
        val asynHttpClient: AsyncGithubHttpClient
) {

    fun getUser(name: String): GithubUser {
        return GithubUser(
                httpClient.getUser(name)!!,
                httpClient.getFollowers(name),
                httpClient.getRepositories(name)
        )
    }

    fun getUsersRX(): GithubUser {

        val observable: Observable<GithubUser> =
                Observable
                        .just("kelokelo", "sienio", "davethomas11", "n1trux", "nodiscc", "falzm")
//                        .subscribeOn(Schedulers.io())
                        .map { t: String -> httpClient.getUser(t) }
                        .map { t: RawUser -> GithubUser(t, emptyList(), emptyList()) }


        val obs2 = asynHttpClient.getUser("kelokelo").map { t: RawUser -> GithubUser(t, emptyList(), emptyList()) }
        val obs3 = asynHttpClient.getUser("sienio").map { t: RawUser -> GithubUser(t, emptyList(), emptyList()) }
        val obs4 = asynHttpClient.getUser("davethomas11").map { t: RawUser -> GithubUser(t, emptyList(), emptyList()) }
        val obs5 = asynHttpClient.getUser("n1trux").map { t: RawUser -> GithubUser(t, emptyList(), emptyList()) }
        val obs6 = asynHttpClient.getUser("nodiscc").map { t: RawUser -> GithubUser(t, emptyList(), emptyList()) }
        val obs7 = asynHttpClient.getUser("falzm").map { t: RawUser -> GithubUser(t, emptyList(), emptyList()) }

        val listOf = listOf<Observable<GithubUser>>(obs2,
                obs3,
                obs4,
                obs5,
                obs6,
                obs7)

        val mergeWith = obs2
                .mergeWith(obs3)
                .mergeWith(obs4)

        val zip = Observable.zip(listOf) { args -> Arrays.asList(args) }
        Observable.zipIterable(listOf,
                fun(z: Any) {
                    log.info(z.toString())
                }
                , false, 10)
//        Observable<GithubUser>.zip(obs2, obs3, {l, r -> log.info("Log fromRX={}", item)})

        mergeWith.subscribe(
                { item -> log.info("Log fromRX={}", item) },
                { error -> log.error("RX error", error) },
                { log.info("RX done") }
        )

        return GithubUser(RawUser.empty(), emptyList(), emptyList())
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

}