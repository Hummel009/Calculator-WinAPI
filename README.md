[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=code_smells)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=bugs)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=duplicated_lines_density)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=sqale_index)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Hummel009_Calculator-WinAPI&metric=ncloc)](https://sonarcloud.io/summary/overall?id=Hummel009_Calculator-WinAPI)

Простейший графический калькулятор на WinAPI. Реализован на С, С++, C#, Java, Kotlin (JVM), Kotlin (Native), FASM (x86).
Все эти языки либо напрямую совместимы с WinAPI, либо имеют возможность обращаться к WinAPI через обёртки.

Изначально также планировался проект на языке Scala, но оказалось, что полная работа с WinAPI в языке невозможна в силу
принудительной инкапсуляции полей в обёртках для структур.

## Алгоритм

Калькулятор оснащён хранилищем вида "операнд 1, операция, операнд 2". Нажатие различных кнопок по-разному заполняет это
хранилище и очищает экран. Нажатие кнопки равенства вычисляет результат того, что лежит в хранилище. По размеру
хранилища можно определить, пригодно ли содержимое для вычисления или нет.
