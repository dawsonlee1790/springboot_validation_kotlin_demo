package dawsonlee1790.springboot_validation_kotlin_demo.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Pattern

/**
 * @author dawsonlee1790
 * @email dawsonlee1790@gamil.com
 * @date 2018-12-26
 */
data class LoginDTO(
        @field:Pattern(regexp = "[a-zA-Z0-9]+", message = "姓名只能由数字和大小写字母组成")
        val name: String,
        @get:Max(18, message = "居然超过18岁了，可怕，不允许")
        val age: Int
)

/*
//============springboot validation 不生效============
data class LoginDTO(
        @Pattern(regexp = "[a-zA-Z0-9]+", message = "姓名只能由数字和大小写字母组成")
        val name: String,
        @Max(18, message = "居然超过18岁了，可怕，不允许")
        val age: Int
)
*/

