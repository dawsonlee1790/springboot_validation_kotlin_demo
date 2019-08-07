## springboot validation 不生效

`LoginDTO.kt`

    data class LoginDTO(
            @Pattern(regexp = "[a-zA-Z0-9]+", message = "姓名只能由数字和大小写字母组成")
            val name: String,
            @Max(18, message = "居然超过18岁了，可怕，不允许")
            val age: Int
    )

* 测试代码不通过

![validation不生效](/images/validation不生效.png)

## 当使用 `@field:` 或 `@get:` 标识符后，验证通过

`LoginDTO.kt`

    data class LoginDTO(
            @field:Pattern(regexp = "[a-zA-Z0-9]+", message = "姓名只能由数字和大小写字母组成")
            val name: String,
            @get:Max(18, message = "居然超过18岁了，可怕，不允许")
            val age: Int
    )
    
* 测试代码通过

![validation生效运行结果图](/images/validation生效运行结果图.png)

---
## validation不通过的返回错误信息
![validation不通过的返回错误信息](/images/validation不通过返回的错误信息.png)

## validation自定义返回的错误信息

`BadReqeustExceptionHandler.kt`
    
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

* 定义更复杂的DTO

`ComplexDTO`
    
    package dawsonlee1790.springboot_validation_kotlin_demo.dto
    
    import java.util.*
    import javax.validation.constraints.*
    
    data class ComplexDTO(
            @field: DecimalMin(value = "10000", inclusive = false, message = "你的年薪必须大于1000000，等于1000000都不允许")
            val annualSalary: Long,
    
            @field: AssertTrue(message = "你必须是帅气的，不接受反驳")
            val handsome: Boolean,
    
            @field: Email
            val email: String?,
    
            @field: Past
            val birthday: Date,
    
            @field: NotNull
            val notNullNotWork: Int,
    
            @field: NotNull
            val notNull: Int?,
    
            @field: NotBlank
            val notBlankNotWork: String,
    
            @field: NotBlank
            val NotBlank: String?
    )

* 使用postMan进行接口测试可得

![自定义返回错误信息](/images/validation不通过返回的自定义错误信息.png)

**观察post Body中的信息会发现几个奇怪的现象**

1. 发现`String`类型的属性`notBlankNotWork`，如果不传会报错`MissingKotlinParameterException`，如下图所示。
`String?`类型属性`notBlank`可以被Bean Validation的`@NotBlank`校验

![](/images/String类型会报错.png)

2. `Int`类型属性`notNullNotWork`不传，非但不会像`String`类型属性那样抛错，居然也能通过Bean Validation的`@NotNull`校验。
但`String`

3. `Int?`类型属性`notNull`可以被Bean Validation的`@NotNull`检验。

原因：如果是`Int`类型属性，当json传null值时，fasterXML/Jackson 会默认赋值0。
如果是`String`类型属性，当json传null值时，fasterXML/Jackson 不会处理，此时kotlin发现非空类型被赋予空值时抛错
`MissingKotlinParameterException`

## Bean Validation 自定义注解

`NotEquals.kt`

    package dawsonlee1790.springboot_validation_kotlin_demo.validation.constraints
    
    import java.math.BigDecimal
    import javax.validation.Constraint
    import javax.validation.ConstraintValidator
    import javax.validation.ConstraintValidatorContext
    import javax.validation.Payload
    import kotlin.reflect.KClass
    
    @Target(
            AnnotationTarget.FUNCTION,
            AnnotationTarget.PROPERTY_GETTER,
            AnnotationTarget.PROPERTY_SETTER,
            AnnotationTarget.FIELD,
            AnnotationTarget.ANNOTATION_CLASS,
            AnnotationTarget.CONSTRUCTOR,
            AnnotationTarget.VALUE_PARAMETER
    )
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    @Constraint(validatedBy = [NotEqualsConstraintValidator::class])
    annotation class NotEquals(
            val message: String = "cannot equal value",
            val groups: Array<KClass<*>> = [],
            val payload: Array<KClass<out Payload>> = [],
            val values: DoubleArray
    )
    
    
    class NotEqualsConstraintValidator : ConstraintValidator<NotEquals, Number> {
    
        private val values: MutableList<BigDecimal> = mutableListOf()
    
        override fun initialize(constraintAnnotation: NotEquals) {
            for (number in constraintAnnotation.values) {
                // 为了确保精度,必须使用 Double.toString() 初始化BigDecimal
                values.add(BigDecimal(number.toString()))
            }
        }
    
        /**
         * return false means the filed can not pass validation
         *                          value == null       value != null
         * values.isEmpty()             true                true
         * values.isNotEmpty()          true               !values.contains(BigDecimal(value.toString()))
         */
        override fun isValid(value: Number?, context: ConstraintValidatorContext): Boolean = when {
            values.isNotEmpty() && value != null -> {
                // 为了确保精度,必须使用 Double.toString() 初始化BigDecimal
                val valueDecimal = BigDecimal(value.toString())
                values.none { it.compareTo(valueDecimal) == 0 }
            }
            else -> true
        }
    }

`NotEqualsDTO.kt`

    package dawsonlee1790.springboot_validation_kotlin_demo.dto
    
    import dawsonlee1790.springboot_validation_kotlin_demo.validation.constraints.NotEquals
    import java.math.BigDecimal
    
    data class NotEqualsDTO(
            @field: NotEquals(values = [0.0], message = "不能为0")
            val number: BigDecimal,
            @field: NotEquals(values = [1.0], message = "不能为1")
            val number1: BigDecimal,
            @field: NotEquals(values = [2.0], message = "不能为2")
            val number2: BigDecimal?
    )
    
 



