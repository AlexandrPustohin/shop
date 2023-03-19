# Shop
Магазин на микросервисах

Регистрация <br>
http://localhost:8088/api/auth/signup POST<br>
{"username": "pustohinae",<br>
"email": "kich_alex654@mail.ru",<br>
"password":"j3qq4h7h2v"}

По умолчанию роль - USER

Вход:<br>
http://localhost:8088/api/auth/signin POST<br>
{"usernameOrEmail": "pustohinae",<br>
 "password":"j3qq4h7h2v"}<br>
или<br>
{"usernameOrEmail": "kich_alex654@mail.ru",<br>
"password":"j3qq4h7h2v"}