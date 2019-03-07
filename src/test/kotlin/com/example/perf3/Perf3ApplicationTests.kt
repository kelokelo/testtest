package com.example.perf3

import com.example.perf3.service.github.GithubService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.beans.factory.annotation.Autowired


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Perf3ApplicationTests {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var githubService: GithubService

    @Test
    fun contextLoads() {
        val forObject = this.restTemplate
                .getForObject("http://localhost:$port/user/kelokelo",
                        String::class.java)

        println(forObject)
        assert(forObject.contains("id"))

    }

    @Test
    fun contextLoads2() {
        githubService.getUsersRX()

    }

}
