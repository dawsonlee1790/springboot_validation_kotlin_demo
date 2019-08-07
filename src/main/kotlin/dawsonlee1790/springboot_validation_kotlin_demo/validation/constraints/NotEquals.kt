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