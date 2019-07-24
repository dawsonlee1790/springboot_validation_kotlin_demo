package dawsonlee1790.springboot_validation_kotlin_demo.exception

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class BadReqeustExceptionHandler {

    @Autowired
    private lateinit var requst: HttpServletRequest

    /**
     * request请求校验不通过的异常统一处理
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(
            exception: MethodArgumentNotValidException,
            response: HttpServletResponse
    ): ResponseBody {
        val responseBody = ResponseBody(
                timestamp = Timestamp(Date().time),
                path = requst.servletPath,
                message = "请求的Body数据不能通过校验",
                errors = emptyList<Error>().toMutableList()
        )
        exception.bindingResult.fieldErrors.forEach {
            val error = Error(
                    field = it.field,
                    message = it.defaultMessage
            )
            responseBody.errors.add(error)
        }
        response.status = 400
        return responseBody
    }
}

data class ResponseBody(
        val timestamp: Timestamp,
        val path: String,
        val message: String,
        val errors: MutableList<Error>
)

data class Error(
        val field: String,
        val message: String?
)