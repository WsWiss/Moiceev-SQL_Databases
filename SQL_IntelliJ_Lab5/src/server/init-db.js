const pool = require('./db');
const insertData = require('./insert-data');

/**
 * Инициализация базы данных:
 * 1. Создание таблиц
 * 2. Вставка данных из .txt файлов
 */
(async () => {
  const client = await pool.connect();
  
  try {
    console.log('🗄️  Создание таблиц...');
    
    /* ---- PRODUCTS ---- */
    await client.query(`DROP TABLE IF EXISTS products`);
    await client.query(`CREATE TABLE products (
      id       SERIAL PRIMARY KEY,
      name     VARCHAR(255),
      supplier VARCHAR(255),
      category VARCHAR(255),
      cost     NUMERIC(10,2),
      amount   INTEGER
    )`);
    console.log('✅ Таблица products создана');

    /* ---- SUPPLIERS ---- */
    await client.query(`DROP TABLE IF EXISTS suppliers`);
    await client.query(`CREATE TABLE suppliers (
      id            SERIAL PRIMARY KEY,
      "companyName"   VARCHAR(255),
      "directorName"  VARCHAR(255),
      "directorPhone" VARCHAR(255),
      "directorEmail" VARCHAR(255)
    )`);
    console.log('✅ Таблица suppliers создана');

    client.release();
    
    // Вставляем данные из .txt файлов
    await insertData();
    
  } catch (error) {
    console.error('❌ Ошибка при инициализации:', error.message);
    process.exit(1);
  } finally {
    await pool.end();
  }
})();

