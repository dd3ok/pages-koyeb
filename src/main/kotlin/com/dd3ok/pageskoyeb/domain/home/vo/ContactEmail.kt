package com.dd3ok.pageskoyeb.domain.home.vo

@JvmInline
value class ContactEmail(private val value: String) {
    init {
        require(value.isNotBlank()) { "이메일은 필수입니다" }
        require(isValidEmail(value)) { "유효하지 않은 이메일 형식입니다: $value" }
    }
    
    fun getValue(): String = value
    
    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"))
    }
}

@JvmInline
value class ContactMessage(private val value: String) {
    init {
        require(value.isNotBlank()) { "메시지는 필수입니다" }
        require(value.length <= 2000) { "메시지는 2000자를 초과할 수 없습니다" }
    }
    
    fun getValue(): String = value
}

@JvmInline
value class ContactName(private val value: String) {
    init {
        require(value.isNotBlank()) { "이름은 필수입니다" }
        require(value.length <= 100) { "이름은 100자를 초과할 수 없습니다" }
    }
    
    fun getValue(): String = value
}
