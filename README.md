# 🌤️ Weather App

[![Java](https://img.shields.io/badge/Java-23-orange.svg)](https://openjdk.org/projects/jdk/23/)
[![Spring](https://img.shields.io/badge/Spring-MVC-brightgreen.svg)](https://spring.io/)
[![Hibernate](https://img.shields.io/badge/Hibernate-ORM-blue.svg)](https://hibernate.org/)
[![Liquibase](https://img.shields.io/badge/Liquibase-Migrations-lightblue.svg)](https://www.liquibase.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-Build-yellow.svg)](https://maven.apache.org/)

Веб-приложение для отслеживания погоды в различных локациях с пользовательской аутентификацией.

## 🚀 Особенности

- ✅ **Пользовательская система** - регистрация и аутентификация
- ✅ **Управление локациями** - добавление/удаление мест для отслеживания погоды
- ✅ **База данных** - миграции через Liquibase
- ✅ **Spring MVC** - классический веб-интерфейс
- ✅ **Hibernate ORM** - объектно-реляционное отображение

## 🛠️ Технологический стек

| Технология | Версия | Назначение |
|------------|--------|------------|
| **Java** | 23 | Основной язык программирования |
| **Spring MVC** | 6.x | Веб-фреймворк |
| **Hibernate** | 6.x | ORM для работы с БД |
| **Liquibase** | 4.x | Миграции базы данных |
| **PostgreSQL** | 13+ | База данных |
| **Maven** | 3.6+ | Сборка проекта |
| **Tomcat** | 11.x | Веб-сервер |

## 📋 Предварительные требования

- **Java 23** или выше
- **Maven 3.6** или выше
- **PostgreSQL 13** или выше
- **Tomcat 11** (для деплоя)

### 1. Клонирование репозитория
```bash
git clone https://github.com/yourusername/weather-app.git
cd weather-app
```
### 2. Настройка базы данных
```bash
-- Создание базы данных
CREATE DATABASE mydb;

-- Создание пользователя (опционально)
CREATE USER weather_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE mydb TO weather_user;
```
### 3. Конфигурация приложения
Добавьте переменные окружения:
 - DATABASE_DRIVER
 - DATABASE_URL
 - DATABASE_USER
 - DATABASE_PASSWORD
 - WEATHER_API_KEY
### 4. Запуск приложения
```bash
# Сборка проекта
mvn clean package -DskipTests

# Деплой в Tomcat
cp target/weather-app.war $CATALINA_HOME/webapps/
```
## Структура базы данных
Приложение автоматически создает следующие таблицы через Liquibase:
 - users - пользователи системы
 - sessions - сессии пользователей
 - locations - локации пользователей

Миграции находятся в **src/main/resources/db/changelog/**

