package dawsonlee1790.springboot_validation_kotlin_demo.controller

import dawsonlee1790.springboot_validation_kotlin_demo.dto.LoginDTO
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author dawsonlee1790
 * @email dawsonlee1790@gamil.com
 * @date 2018-12-26
 */
@RestController
@RequestMapping("/loginController")
class LoginController {

    @PostMapping("/login")
    fun login(@RequestBody @Validated loginDTO: LoginDTO) {
        println("Hello ${loginDTO.name}!")
    }

}