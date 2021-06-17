# Web сервер

Администратор (role=admin): anna/anna1

*Доступно*:
* список клиентов
* получение клиента в json по id
* добавление клиента из формы
* добавление клиента из json

Пользователь (role=user): bob/bob1

*Доступно*:
* список клиентов
* получение клиента в json по id
  
Стартовая страница:http://localhost:8080

Страница клиентов: http://localhost:8080/clients

REST сервис:
* GET http://localhost:8080/api/client/0
* POST http://localhost:8080/api/client