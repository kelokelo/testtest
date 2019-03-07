package com.example.perf3.api

import com.example.perf3.domain.GithubUser
import com.example.perf3.domain.RawUser
import com.example.perf3.service.github.GithubService
import io.reactivex.Observable
import io.reactivex.Single
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import kotlin.math.log

@RestController
class UserEndpoint(
        val service: GithubService
) {

    val logger = LoggerFactory.getLogger(UserEndpoint::class.java)

    @GetMapping(path = ["/user"])
    fun getUsers(): List<GithubUser> {
        logger.info("Request get Users")
        val users = service.getUsers()
        logger.info("Sending response to client {}", users)
        return users
    }

    @GetMapping(path = ["/async/user"])
    fun getAsyncUsers(): Observable<GithubUser> {
        logger.info("Request get Users")
        return service.getObsUsers().also { logger.info("Sending response to client observable={}", it) }
    }

    @GetMapping(path = ["/user/{name}"])
    fun getUser(@PathVariable name: String): RawUser {
        logger.info("Request get user id={}", name)
        return service.getUser(name)
    }

}