package dawsonlee1790.springboot_validation_kotlin_demo.controller

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * @author dawsonlee1790
 * @email dawsonlee1790@gamil.com
 * @date 2018-12-26
 */
@RunWith(SpringRunner::class)
@SpringBootTest
class LoginControllerTest {

    @Autowired
    private lateinit var webAppilicationContext: WebApplicationContext

    lateinit var mockMvc: MockMvc

    val baseUrl = "/loginController"

    @Before
    fun init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppilicationContext).build()
    }

    private fun post(url: String, content: String): MvcResult {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn()
    }

    @Test
    fun loginSuccessful() {
        val url = "$baseUrl/login"
        val contextJson = "{\n  \"name\": \"dawsonlee1790\",\n  \"age\": 17\n}"
        val result = post(url, contextJson)
        Assert.assertEquals(200, result.response.status)
    }

    @Test
    fun loginErrorByName() {
        val url = "$baseUrl/login"
        val contextJson = "{\n  \"name\": \"===dawsonlee1790\",\n  \"age\": 17\n}"
        val result = post(url, contextJson)
        Assert.assertEquals(400, result.response.status)
    }

    @Test
    fun loginErrorByAge() {
        val url = "$baseUrl/login"
        val contextJson = "{\n  \"name\": \"===dawsonlee1790\",\n  \"age\": 18\n}"
        val result = post(url, contextJson)
        Assert.assertNotEquals(200, result.response.status)
    }

}