const express = require('express');
const path = require('path');
const db   = require('./db');

const app  = express();
const PORT = 3000;

app.use(express.static(path.join(__dirname,'../public')));

/* ----- PRODUCTS (уже было) ----- */
app.get('/api/products', async (req,res)=>{
  const q = (req.query.filter||'').trim().toLowerCase();
  let sql = `SELECT * FROM products`;
  const p = [];
  if(q){
    sql+=` WHERE (
      LOWER(name)     LIKE $1 OR
      LOWER(supplier) LIKE $2 OR
      LOWER(category) LIKE $3 OR
      CAST(cost AS TEXT) LIKE $4 OR
      CAST(amount AS TEXT) LIKE $5 )`;
    const searchPattern = `%${q}%`;
    p.push(searchPattern, searchPattern, searchPattern, searchPattern, searchPattern);
  }
  try {
    const result = await db.query(sql, p);
    res.json(result.rows);
  } catch(err) {
    res.status(500).json({error:err.message});
  }
});

/* ----- SUPPLIERS (новое) ----- */
app.get('/api/suppliers', async (req,res)=>{
  const q = (req.query.filter||'').trim().toLowerCase();
  let sql = `SELECT id, "companyName", "directorName", "directorPhone", "directorEmail" FROM suppliers`;
  const p = [];
  if(q){
    sql+=` WHERE (
      LOWER("companyName")   LIKE $1 OR
      LOWER("directorName")  LIKE $2 OR
      LOWER("directorPhone") LIKE $3 OR
      LOWER("directorEmail") LIKE $4 )`;
    const searchPattern = `%${q}%`;
    p.push(searchPattern, searchPattern, searchPattern, searchPattern);
  }
  try {
    const result = await db.query(sql, p);
    res.json(result.rows);
  } catch(err) {
    res.status(500).json({error:err.message});
  }
});

app.listen(PORT,()=>console.log(`🚀 http://localhost:${PORT}`));

