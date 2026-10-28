package cl.duocuc.miregistro.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/** Guarda protocolos localmente para poder registrar información sin conexión. */
class ProtocolosDbHelper(context: Context) : SQLiteOpenHelper(context, "induztek_registro_pruebas.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS protocolos (id INTEGER PRIMARY KEY AUTOINCREMENT, numero TEXT NOT NULL UNIQUE, cliente TEXT NOT NULL, instalacion TEXT NOT NULL, tag TEXT NOT NULL, tipo TEXT NOT NULL, tecnico TEXT NOT NULL, fecha TEXT NOT NULL, temperatura REAL NOT NULL, humedad REAL NOT NULL, lectura1 REAL NOT NULL, lectura10 REAL NOT NULL, resultado TEXT NOT NULL, estado TEXT NOT NULL, comentario TEXT NOT NULL DEFAULT '')")
        db.execSQL("INSERT OR IGNORE INTO protocolos(numero,cliente,instalacion,tag,tipo,tecnico,fecha,temperatura,humedad,lectura1,lectura10,resultado,estado,comentario) VALUES ('PT-2024-0312','Envasadora del Pacífico S.A.','Planta Lampa','TR-01','Transformador','Matías Godoy','14-09-2024',18,55,12500,31250,'Aprobado','Formalizado','Ejemplo histórico')")
        db.execSQL("INSERT OR IGNORE INTO protocolos(numero,cliente,instalacion,tag,tipo,tecnico,fecha,temperatura,humedad,lectura1,lectura10,resultado,estado,comentario) VALUES ('PT-2025-0288','Envasadora del Pacífico S.A.','Planta Lampa','TR-01','Transformador','Javiera Tapia','13-09-2025',21,48,9800,21560,'Aprobado','Formalizado','Ejemplo histórico')")
        db.execSQL("INSERT OR IGNORE INTO protocolos(numero,cliente,instalacion,tag,tipo,tecnico,fecha,temperatura,humedad,lectura1,lectura10,resultado,estado,comentario) VALUES ('PT-2026-0401','Envasadora del Pacífico S.A.','Planta Lampa','TR-01','Transformador','Matías Godoy','26-09-2026',19,60,6100,10980,'Observado','Registrado en terreno','Variación significativa respecto al ano anterior')")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { if (oldVersion < 3) onCreate(db) }

    fun listar(): List<Map<String, String>> {
        val lista = mutableListOf<Map<String, String>>()
        readableDatabase.rawQuery("SELECT numero,cliente,instalacion,tag,tipo,tecnico,fecha,temperatura,humedad,lectura1,lectura10,resultado,estado,comentario FROM protocolos ORDER BY id DESC", null).use { c ->
            while (c.moveToNext()) {
                lista.add(mapOf("numero" to c.getString(0), "cliente" to c.getString(1), "instalacion" to c.getString(2), "tag" to c.getString(3), "tipo" to c.getString(4), "tecnico" to c.getString(5), "fecha" to c.getString(6), "temperatura" to c.getString(7), "humedad" to c.getString(8), "lectura1" to c.getString(9), "lectura10" to c.getString(10), "resultado" to c.getString(11), "estado" to c.getString(12), "comentario" to c.getString(13)))
            }
        }
        return lista
    }

    fun insertar(numero: String, cliente: String, instalacion: String, tag: String, tipo: String, tecnico: String, fecha: String, temperatura: Double, humedad: Double, lectura1: Double, lectura10: Double, comentario: String): Boolean {
        if (numero.isBlank() || tag.isBlank() || lectura1 <= 0 || lectura10 <= 0 || temperatura < -20 || temperatura > 70 || humedad < 0 || humedad > 100) return false
        val ip = lectura10 / lectura1
        val resultado = when { lectura1 < 100 -> "Rechazado"; ip < 1.0 -> "Rechazado"; ip < 2.0 -> "Observado"; else -> "Aprobado" }
        val values = ContentValues().apply {
            put("numero", numero); put("cliente", cliente); put("instalacion", instalacion); put("tag", tag); put("tipo", tipo); put("tecnico", tecnico); put("fecha", fecha); put("temperatura", temperatura); put("humedad", humedad); put("lectura1", lectura1); put("lectura10", lectura10); put("resultado", resultado); put("estado", "Registrado en terreno"); put("comentario", comentario)
        }
        return writableDatabase.insert("protocolos", null, values) != -1L
    }
}
