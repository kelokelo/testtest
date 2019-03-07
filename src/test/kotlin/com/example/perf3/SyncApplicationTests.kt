package com.example.perf3

import com.example.perf3.api.UserEndpoint
import com.example.perf3.domain.GithubUser
import com.example.perf3.domain.RawUser
import com.example.perf3.service.github.AsyncGithubHttpClient
import com.example.perf3.service.github.GithubHttpClient
import com.example.perf3.service.github.GithubService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import java.util.*


class SyncApplicationTests {
    val logger = LoggerFactory.getLogger(SyncApplicationTests::class.java)
    val service = GithubService(
            httpClient = GithubHttpClient(),
            asynHttpClient = AsyncGithubHttpClient()
    )
    val asynHttpClient = AsyncGithubHttpClient()

    @Test
    fun contextLoads2() {
        val observable: Observable<RawUser> = Observable
                .just("kelokelo", "sienio", "davethomas11", "n1trux", "nodiscc", "falzm")
                .map { t: String -> service.httpClient.getUser(t) }
        observable
//                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.computation())
                .subscribe(
                        { item -> logger.info("Log from subscriber={}", item) },
                        { error -> logger.error("Log error from subscriber", error) },
                        { logger.info("Subscriber done") }
                )


    }
}
