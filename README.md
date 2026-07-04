# dd3ok-pages-api

dd3ok.github.io에서 사용하는 API 서버입니다.

## 역할

- Contact 메시지를 저장하고 메일 알림을 보냅니다.
- 기본 노출 API는 healthcheck와 contact 생성 API로 제한합니다.
- 방명록/댓글 API는 `WEDDING_API_ENABLED=true`로 켤 수 있습니다.
- 배포 플랫폼에 종속되지 않도록 Spring Boot API 서버로 유지합니다.
