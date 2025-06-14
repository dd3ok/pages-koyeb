package com.dd3ok.pageskoyeb.domain.wedding.vo

@JvmInline
value class CommentPassword(private val value: String) {
    init {
        require(value.isNotBlank()) { "비밀번호는 필수입니다" }
        require(value.length in 4..20) { "비밀번호는 4-20자 사이여야 합니다" }
    }
    
    fun getValue(): String = value
}

@JvmInline
value class CommentMessage(private val value: String) {
    init {
        require(value.isNotBlank()) { "메시지는 필수입니다" }
        require(value.length <= 1000) { "메시지는 1000자를 초과할 수 없습니다" }
    }
    
    fun getValue(): String = value
}

@JvmInline
value class CommentAuthor(private val value: String) {
    init {
        require(value.isNotBlank()) { "작성자명은 필수입니다" }
        require(value.length <= 100) { "작성자명은 100자를 초과할 수 없습니다" }
    }
    
    fun getValue(): String = value
}
