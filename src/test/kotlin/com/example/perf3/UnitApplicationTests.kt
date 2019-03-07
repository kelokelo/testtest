package com.example.perf3

import com.example.perf3.api.UserEndpoint
import com.example.perf3.domain.GithubUser
import com.example.perf3.domain.RawUser
import com.example.perf3.service.github.AsyncGithubHttpClient
import com.example.perf3.service.github.GithubHttpClient
import com.example.perf3.service.github.GithubService
import com.xebialabs.restito.builder.stub.StubHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.semantics.Condition
import com.xebialabs.restito.server.StubServer
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.glassfish.grizzly.http.util.HttpStatus
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import java.util.*


class UnitApplicationTests {
    val logger = LoggerFactory.getLogger(UnitApplicationTests::class.java)
    val service = GithubService(
            httpClient = GithubHttpClient(),
            asynHttpClient = AsyncGithubHttpClient("http://localhost:6666/user/%s")
    )
    val asynHttpClient = service.asynHttpClient


    @Test
    fun contextLoads() {
        val obs2 = asynHttpClient.getUser("kelokelo")
        val obs3 = asynHttpClient.getUser("sienio")
        val obs4 = asynHttpClient.getUser("davethomas11")
        val obs5 = asynHttpClient.getUser("n1trux")
        val obs6 = asynHttpClient.getUser("nodiscc")
        val obs7 = asynHttpClient.getUser("falzm")

        val mergeWith = obs2
                .mergeWith(obs3)
                .mergeWith(obs4)
                .mergeWith(obs5)
                .mergeWith(obs6)
                .mergeWith(obs7)

        mergeWith
                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.computation())
                .subscribe(
                        { item -> logger.info("Log from subscriber={}", item) },
                        { error -> logger.error("Log error from subscriber", error) },
                        { logger.info("Subscriber done") }
                )
        Thread.sleep(8000)
        logger.info("Test done")
    }


    lateinit var server : StubServer

    @Before
     fun start() {
        server = StubServer(6666).run()

        StubHttp.whenHttp(server)
                .match(Condition.get("/user/kelokelo"))
                .then(Action.status(HttpStatus.OK_200), Action.stringContent("""{"login":"kelokelo","id" : "1"}"""), Action.contentType("application/json"), Action.delay(200))
        StubHttp.whenHttp(server)
                .match(Condition.get("/user/sienio"))
                .then(Action.status(HttpStatus.OK_200), Action.stringContent("""{"login":"sienio","id" : "2"}"""), Action.contentType("application/json"), Action.delay(400))
        StubHttp.whenHttp(server)
                .match(Condition.get("/user/davethomas11"))
                .then(Action.status(HttpStatus.OK_200), Action.stringContent("""{"login":"davethomas11","id" : "3"}"""), Action.contentType("application/json"))
        StubHttp.whenHttp(server)
                .match(Condition.get("/user/n1trux"))
                .then(Action.status(HttpStatus.OK_200), Action.stringContent("""{"login":"n1trux","id" : "4"}"""), Action.contentType("application/json"))
        StubHttp.whenHttp(server)
                .match(Condition.get("/user/nodiscc"))
                .then(Action.status(HttpStatus.OK_200), Action.stringContent("""{"login":"nodiscc","id" : "5"}"""), Action.contentType("application/json"))
        StubHttp.whenHttp(server)
                .match(Condition.get("/user/falzm"))
                .then(Action.status(HttpStatus.OK_200), Action.stringContent("""{"login":"falzm","id" : "6"}"""), Action.contentType("application/json"))


    }

    @After
    fun stop() {
        server.stop()
    }
}
