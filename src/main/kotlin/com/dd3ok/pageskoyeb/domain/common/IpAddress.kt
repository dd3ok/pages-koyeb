package com.dd3ok.pageskoyeb.domain.common

@JvmInline
value class IpAddress(private val value: String?) {
    init {
        value?.let { ip ->
            if (ip.isNotBlank()) {
                require(isValidIp(ip)) { "유효하지 않은 IP 주소입니다: $ip" }
            }
        }
    }

    fun getValue(): String? = value?.takeIf { it.isNotBlank() }

    private fun isValidIp(ip: String): Boolean {
        return try {
            // IPv4 체크
            ip.matches(Regex("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) ||
                    // IPv6 체크 (간단한 버전)
                    ip.contains(":") && ip.length <= 39
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        fun fromString(ip: String?): IpAddress {
            return IpAddress(ip?.trim()?.takeIf { it.isNotBlank() })
        }
    }
}
