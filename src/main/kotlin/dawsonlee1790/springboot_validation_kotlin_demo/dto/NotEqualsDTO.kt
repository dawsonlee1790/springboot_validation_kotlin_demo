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