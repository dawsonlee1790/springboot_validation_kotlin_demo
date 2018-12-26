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