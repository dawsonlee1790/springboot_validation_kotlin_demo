package dawsonlee1790.springboot_validation_kotlin_demo.exception

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {

    @Autowired
    private lateinit var requst: HttpServletRequest

    /**
     * request请求校验不通过的异常统一处理
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: MethodArgumentNotValidException): ResponseBody {
        return ResponseBody(
            timestamp = Timestamp(Date().time),
            path = requst.servletPath,
            message = "请求的Body数据不能通过校验",
            errors = exception.bindingResult.fieldErrors.map {
                ResponseBody.Error(
                    field = it.field,
                    message = it.defaultMessage
                )
            }
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: ConstraintViolationException): ResponseBody {
        return ResponseBody(
            timestamp = Timestamp(Date().time),
            path = requst.servletPath,
            message = "请求的Body数据不能通过校验",
            errors = exception.constraintViolations.map {
                ResponseBody.Error(
                    field = "",
                    message = it.message
                )
            }
        )
    }
}

data class ResponseBody(
    val timestamp: Timestamp,
    val path: String,
    val message: String,
    val errors: List<Error>
) {
    data class Error(
        val field: String,
        val message: String?
    )
}