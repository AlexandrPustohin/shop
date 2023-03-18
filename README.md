# Shop
Магазин на микросервисах

Регистрация
http://localhost:8088/api/auth/signup POST
{"username": "pustohinae",
"email": "kich_alex654@mail.ru",
"password":"j3qq4h7h2v"}

По умолчанию роль - USER

Вход:
http://localhost:8088/api/auth/signin POST
{"usernameOrEmail": "pustohinae",
 "password":"j3qq4h7h2v"}
или
{"usernameOrEmail": "kich_alex654@mail.ru",
"password":"j3qq4h7h2v"}