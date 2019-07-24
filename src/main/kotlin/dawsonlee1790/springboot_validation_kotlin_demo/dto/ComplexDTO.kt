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