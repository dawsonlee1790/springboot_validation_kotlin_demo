package dawsonlee1790.springboot_validation_kotlin_demo.controller

import dawsonlee1790.springboot_validation_kotlin_demo.dto.ComplexDTO
import dawsonlee1790.springboot_validation_kotlin_demo.dto.LoginDTO
import dawsonlee1790.springboot_validation_kotlin_demo.dto.NotEqualsDTO
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * @author dawsonlee1790
 * @email dawsonlee1790@gamil.com
 * @date 2018-12-26
 */
@RestController
@RequestMapping("/loginController")
class LoginController {

    private val logger = LoggerFactory.getLogger(LoginController::class.java)

    @PostMapping("/login")
    fun login(@RequestBody @Validated loginDTO: LoginDTO) {
        println("Hello ${loginDTO.name}!")
    }

    @PostMapping("/complexValidate")
    fun complexValidate(@RequestBody @Valid complexDTO: ComplexDTO) {
        logger.info("complex validate successful")
    }

    @PostMapping("/notEqualsValidate")
    fun notEqualsValidate(@RequestBody @Valid dto: NotEqualsDTO){
        logger.info("not equals validate successful")
    }

}