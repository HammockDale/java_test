# java_test


Существует условный сервер, на котором в оперативной памяти хранится очень длинная таблица (миллионы строк).
Строки отсортированы в некотором порядке, принцип сортировки может меняться, каждая строка имеет свой уникальный идентификатор.
Таблица живая, постоянно изменяется (несколько сотен/десятков изменений в секунду). В нее добавляются новые строки, обновляются и удаляются существующие.

Кроме этого, существует условный клиент, задача которого - отображение этой таблицы в реальном времени (все изменения видны сразу). При этом памяти на клиенте недостаточно для выгрузки всей таблицы.
Отображение осуществляется за счет того, что пользователю доступно окно высотой в N строк и в нём скроллер, позволяющий передвигаться по списку и сортировать ее по столбцам. При этом пользователь не ограничен в своих действиях - он может скроллироваться в любую часть списка и выбирать любой столбец для сортировки. Например, если на сервере список из миллиона записей и на клиенте скроллер передвинут на середину, то клиент должен отобразить N записей начиная с полумллионной.

Задача - придумать способ для хранения таблицы на сервере и протокол взаимодействия клиента и сервера для быстрого и корректного отображения данных клиентами. Важно, чтобы решение не предполагало использование сторонних продуктов (например, СУБД) или фреймворков. Следует использовать базовые контейнеры данных для выбранного для решения задачи языка (например, контейнеры стандартной библиотеки STL в случае реализации на C++ - map, vector, list или их аналоги в других языках).
В идеале - создать прототип на любом языке программирования, но вполне достаточно четко описать предполагаемую реализацию.

client & server запускаются из одного процесса. Можно собрать и запустить проект следующим способом:

cd ./src
javac ./*.java && java UITable  && rm ./*.class

