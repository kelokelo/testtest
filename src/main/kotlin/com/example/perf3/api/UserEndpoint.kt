package com.example.perf3.api

import com.example.perf3.domain.GithubUser
import com.example.perf3.service.github.GithubHttpClient
import com.example.perf3.service.github.GithubService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserEndpoint(
        val service: GithubService
) {

    val logger = LoggerFactory.getLogger(UserEndpoint::class.java)

    @GetMapping(path = ["/user/{name}"])
    fun getUser(@PathVariable name: String): GithubUser {
        logger.info("Request get user id={}", name)
        return service.getUsersRX()
    }

}