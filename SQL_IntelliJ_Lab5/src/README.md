# База данных товаров и поставщиков

Веб-приложение для управления товарами и поставщиками с поиском и подсветкой результатов.

## Требования

- Node.js (v14+)
- PostgreSQL (или Docker для запуска в контейнере)

## Быстрый старт

### Вариант 1: С Docker (рекомендуется)

1. Установите [Docker Desktop](https://www.docker.com/products/docker-desktop)

2. Установите зависимости:
   ```bash
   npm install
   ```

3. Запустите всё одной командой:
   ```bash
   npm run docker:dev
   ```
   
   Это автоматически:
   - Запустит PostgreSQL в Docker
   - Инициализирует базу данных
   - Запустит сервер

4. Откройте браузер: http://localhost:3000

**Остановка:**
```bash
npm run docker:down
```

### Вариант 2: С локальным PostgreSQL

1. Установите и запустите PostgreSQL

2. Создайте базу данных:
   ```sql
   CREATE DATABASE shop;
   ```

3. Настройте подключение в `db.js`:
   ```javascript
   const pool = new Pool({
     host: 'localhost',
     port: 5432,
     database: 'shop',
     user: 'postgres',
     password: 'ваш_пароль'
   });
   ```

4. Установите зависимости:
   ```bash
   npm install
   ```

5. Инициализируйте базу данных:
   ```bash
   npm run init
   ```

6. Запустите сервер:
   ```bash
   npm start
   ```
   
   Или с автоматической проверкой PostgreSQL:
   ```bash
   npm run start:auto
   ```

## Доступные команды

- `npm run init` - Инициализация базы данных
- `npm start` - Запуск сервера
- `npm run start:auto` - Запуск с автоматической проверкой PostgreSQL
- `npm run docker:up` - Запуск PostgreSQL в Docker
- `npm run docker:down` - Остановка PostgreSQL в Docker
- `npm run docker:dev` - Полный запуск с Docker (БД + сервер)

## Структура проекта

- `server.js` - Express сервер с API endpoints
- `db.js` - Подключение к PostgreSQL
- `init-db.js` - Инициализация базы данных
- `public/index.html` - Frontend приложение
- `docker-compose.yml` - Конфигурация Docker для PostgreSQL
- `data.txt` - Данные товаров
- `suppliers.txt` - Данные поставщиков

## API Endpoints

- `GET /api/products?filter=текст` - Поиск товаров
- `GET /api/suppliers?filter=текст` - Поиск поставщиков

## Особенности

- ✅ Раздельный поиск по таблицам
- ✅ Подсветка найденного текста
- ✅ Поиск по всем полям (включая категории)
- ✅ Регистронезависимый поиск
- ✅ Поиск по нажатию Enter


