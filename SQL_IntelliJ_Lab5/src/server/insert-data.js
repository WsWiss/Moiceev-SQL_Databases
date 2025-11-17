const pool = require('./db');

/**
 * Данные товаров
 * Формат: [id, name, supplier, category, cost, amount]
 */
const PRODUCTS_DATA = [
  ['1', 'ASUS VivoBook 15', 'ASUS Suppliers', 'Ноутбук', '54999', '24'],
  ['2', 'Apple MacBook Air 13', 'Apple Distributors', 'Ноутбук', '99990', '12'],
  ['3', 'Acer Aspire 5', 'Acer Partners', 'Ноутбук', '42999', '24'],
  ['4', 'Lenovo IdeaPad 3', 'Lenovo Providers', 'Ноутбук', '37990', '12'],
  ['5', 'HP Pavilion 15', 'HP Solutions', 'Ноутбук', '58900', '24'],
  ['6', 'Dell XPS 13', 'Dell Technologies', 'Ноутбук', '89990', '24'],
  ['7', 'Samsung Galaxy A54', 'Samsung Mobile', 'Смартфон', '29999', '18'],
  ['8', 'Xiaomi Redmi Note 12', 'Xiaomi Official', 'Смартфон', '19999', '12'],
  ['9', 'iPhone 14', 'iPhone Center', 'Смартфон', '79990', '12'],
  ['10', 'Realme 10 Pro', 'Realme Distributors', 'Смартфон', '24999', '12'],
  ['11', 'Google Pixel 7', 'Google Store', 'Смартфон', '59990', '24'],
  ['12', 'Sony Xperia 10 IV', 'Sony Mobile', 'Смартфон', '44990', '24'],
  ['13', 'Sony WH-1000XM4', 'Sony Audio', 'Аудиотехника', '19990', '12'],
  ['14', 'JBL Flip 6', 'JBL Official', 'Аудиотехника', '8990', '12'],
  ['15', 'Apple AirPods Pro', 'Apple Audio', 'Аудиотехника', '24990', '12'],
  ['16', 'Marshall Major IV', 'Marshall Sound', 'Аудиотехника', '12990', '12'],
  ['17', 'Sennheiser HD 450BT', 'Sennheiser Pro', 'Аудиотехника', '14990', '24'],
  ['18', 'Blue Yeti', 'Blue Microphones', 'Аудиотехника', '15990', '12'],
  ['19', 'Dell S2721HS', 'Dell Monitors', 'Монитор', '28999', '24'],
  ['20', 'AOC 24G2U', 'AOC Displays', 'Монитор', '18990', '18'],
  ['21', 'Samsung Odyssey G5', 'Samsung Mobile', 'Монитор', '32990', '24'],
  ['22', 'LG UltraGear 27GN800', 'AOC Displays', 'Монитор', '41990', '24'],
  ['23', 'ASUS TUF Gaming VG249Q', 'ASUS Suppliers', 'Монитор', '23990', '24'],
  ['24', 'Apple iPad Air', 'Apple Distributors', 'Планшет', '59990', '12'],
  ['25', 'Samsung Galaxy Tab S9', 'Samsung Mobile', 'Планшет', '79990', '24'],
  ['26', 'Huawei MatePad Pro', 'Xiaomi Official', 'Планшет', '45990', '12'],
  ['27', 'Xiaomi Pad 6', 'Xiaomi Official', 'Планшет', '34990', '12'],
  ['28', 'Lenovo Tab P11 Plus', 'Lenovo Providers', 'Планшет', '27990', '12'],
  ['29', 'Logitech MX Master 3', 'Dell Technologies', 'Периферия', '8990', '6'],
  ['30', 'Keychron K8', 'Acer Partners', 'Периферия', '12000', '6'],
  ['31', 'Razer DeathAdder V2', 'ASUS Suppliers', 'Периферия', '5990', '12'],
  ['32', 'ASUS ROG Strix Scope', 'ASUS Suppliers', 'Периферия', '10990', '12'],
  ['33', 'HyperX Cloud II', 'HP Solutions', 'Периферия', '8990', '12'],
  ['34', 'LG OLED55C2', 'Sony Audio', 'Телевизор', '89999', '36'],
  ['35', 'Samsung QE55Q70B', 'Samsung Mobile', 'Телевизор', '69999', '24'],
  ['36', 'Sony XR-55X90K', 'Sony Mobile', 'Телевизор', '79990', '36'],
  ['37', 'TCL 55C735', 'Xiaomi Official', 'Телевизор', '54990', '24'],
  ['38', 'Philips 50PUS8807', 'AOC Displays', 'Телевизор', '65990', '24'],
  ['39', 'Canon EOS R50', 'Google Store', 'Фототехника', '54990', '12'],
  ['40', 'Sony ZV-1', 'Sony Mobile', 'Фототехника', '69990', '12'],
  ['41', 'Nikon Z50', 'Apple Distributors', 'Фототехника', '64990', '24'],
  ['42', 'Fujifilm X-T30 II', 'Sony Mobile', 'Фототехника', '89990', '12'],
  ['43', 'TP-Link Archer AX73', 'Dell Technologies', 'Сетевое оборудование', '12999', '12'],
  ['44', 'ASUS RT-AX82U', 'ASUS Suppliers', 'Сетевое оборудование', '15990', '36'],
  ['45', 'Xiaomi AX6000', 'Xiaomi Official', 'Сетевое оборудование', '8990', '12'],
  ['46', 'APC Back-UPS 1100', 'HP Solutions', 'Сетевое оборудование', '19990', '12'],
  ['47', 'Seagate BarraCuda 2TB', 'Dell Technologies', 'Накопитель', '5990', '24'],
  ['48', 'WD Blue SN570 1TB', 'Acer Partners', 'Накопитель', '7990', '60'],
  ['49', 'Samsung 870 EVO 500GB', 'Samsung Mobile', 'Накопитель', '5990', '60'],
  ['50', 'Kingston A400 480GB', 'Lenovo Providers', 'Накопитель', '3490', '36']
];

/**
 * Данные поставщиков
 * Формат: [id, companyName, directorName, directorPhone, directorEmail]
 */
const SUPPLIERS_DATA = [
  ['1', 'ASUS Suppliers', 'Алексей Иванов', '+7-911-123-4567', 'a.ivanov@asus-suppliers.ru'],
  ['2', 'Apple Distributors', 'Мария Петрова', '+7-911-234-5678', 'm.petrova@apple-dist.ru'],
  ['3', 'Acer Partners', 'Сергей Сидоров', '+7-911-345-6789', 's.sidorov@acer-partners.ru'],
  ['4', 'Lenovo Providers', 'Ольга Кузнецова', '+7-911-456-7890', 'o.kuznetsova@lenovo-prov.ru'],
  ['5', 'HP Solutions', 'Дмитрий Попов', '+7-911-567-8901', 'd.popov@hp-solutions.ru'],
  ['6', 'Dell Technologies', 'Екатерина Васильева', '+7-911-678-9012', 'e.vasileva@dell-tech.ru'],
  ['7', 'Samsung Mobile', 'Андрей Смирнов', '+7-911-789-0123', 'a.smirnov@samsung-mobile.ru'],
  ['8', 'Xiaomi Official', 'Наталья Морозова', '+7-911-890-1234', 'n.morozova@xiaomi-official.ru'],
  ['9', 'iPhone Center', 'Игорь Новиков', '+7-911-901-2345', 'i.novikov@iphone-center.ru'],
  ['10', 'Realme Distributors', 'Анна Федорова', '+7-911-012-3456', 'a.fedorova@realme-dist.ru'],
  ['11', 'Google Store', 'Павел Волков', '+7-911-123-4567', 'p.volkov@google-store.ru'],
  ['12', 'Sony Mobile', 'Юлия Алексеева', '+7-911-234-5678', 'y.alekseeva@sony-mobile.ru'],
  ['13', 'Sony Audio', 'Артем Лебедев', '+7-911-345-6789', 'a.lebedev@sony-audio.ru'],
  ['14', 'JBL Official', 'Ксения Семенова', '+7-911-456-7890', 'k.semenova@jbl-official.ru'],
  ['15', 'Apple Audio', 'Владимир Егоров', '+7-911-567-8901', 'v.egorov@apple-audio.ru'],
  ['16', 'Marshall Sound', 'Татьяна Орлова', '+7-911-678-9012', 't.orlova@marshall-sound.ru'],
  ['17', 'Sennheiser Pro', 'Максим Козлов', '+7-911-789-0123', 'm.kozlov@sennheiser-pro.ru'],
  ['18', 'Blue Microphones', 'Елена Павлова', '+7-911-890-1234', 'e.pavlova@blue-mic.ru'],
  ['19', 'Dell Monitors', 'Александра Николаева', '+7-911-901-2345', 'a.nikolaeva@dell-monitors.ru'],
  ['20', 'AOC Displays', 'Роман Захаров', '+7-911-012-3456', 'r.zakharov@aoc-displays.ru']
];

/**
 * Вставляет данные в базу данных
 */
async function insertData() {
  const client = await pool.connect();
  
  try {
    console.log('📥 Начинаем вставку данных...');
    
    /* ---- PRODUCTS ---- */
    console.log('📦 Вставка товаров...');
    let productsCount = 0;
    for (const r of PRODUCTS_DATA) {
      await client.query(
        `INSERT INTO products (name, supplier, category, cost, amount) VALUES ($1, $2, $3, $4, $5)`,
        [r[1], r[2], r[3], parseFloat(r[4]), parseInt(r[5])]
      );
      productsCount++;
    }
    console.log(`✅ Вставлено ${productsCount} товаров`);

    /* ---- SUPPLIERS ---- */
    console.log('👥 Вставка поставщиков...');
    let suppliersCount = 0;
    for (const r of SUPPLIERS_DATA) {
      await client.query(
        `INSERT INTO suppliers ("companyName", "directorName", "directorPhone", "directorEmail") VALUES ($1, $2, $3, $4)`,
        [r[1], r[2], r[3], r[4]]
      );
      suppliersCount++;
    }
    console.log(`✅ Вставлено ${suppliersCount} поставщиков`);

    console.log('✅ Все данные успешно загружены в базу данных');
  } catch (error) {
    console.error('❌ Ошибка при вставке данных:', error.message);
    throw error;
  } finally {
    client.release();
    // Не закрываем pool здесь, так как он может использоваться в init-db.js
  }
}

// Запуск, если файл вызван напрямую
if (require.main === module) {
  insertData()
    .then(async () => {
      await pool.end();
      console.log('✅ Готово!');
      process.exit(0);
    })
    .catch(async (error) => {
      await pool.end();
      console.error('❌ Критическая ошибка:', error);
      process.exit(1);
    });
}

module.exports = insertData;

