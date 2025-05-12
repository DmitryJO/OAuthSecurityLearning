# Освоить настройку OAuth 2 в spring security для разных сервисов авторизации

* Реализовать возможность входа через сервисы Google, github

* Реализовать возможность входа через свой сервис keycloak

* Дополнительно реализовать возможность входа через свой сервис авторизации

## Конфигурация Keycloak (вручную)

1. Realm: `any realm`
2. Client: `your client name`
    - Client ID: `..`
    - Client Secret: `...`
    - Client Authentication: `authorization_code`
    - Add Role: `manager`, `admin`