package com.daniilvdovin.tableapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Класс создания SQLiteOpenHelper
class DBH extends SQLiteOpenHelper {

    public static final String LOG_TAG = "DBH" ;
    public static final String TABLE_NAME = "products" ;

    public DBH(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table "+TABLE_NAME+" ("
                + "id integer primary key autoincrement,"
                + "article text,"
                + "price integer,"
                + "in_stock integer,"
                + "description text,"
                + "product text" + ");");
    }

    //Обновление таблицы
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
public class Core {

    //Класс управления SQLiteOpenHelper
    DBH databasehelper;
    //Активность
    Context context;
    //База
    SQLiteDatabase db;

    //Событие
    public interface Event{
        void Udpate();
    }
    Event EventListener;

    //Установка события
    public void setEventListener(Event eventListener) {
        EventListener = eventListener;
    }

    //Инициализация ядра
    public Core(Context context) {
        this.context = context;
        databasehelper = new DBH(this.context);
        init();
    }
    void init(){
        db = databasehelper.getWritableDatabase();
    }
    //Функция добавления строки в таблицу
    public void Add(Row product){
        ContentValues cv = new ContentValues();
        cv.put("article",product.article);
        cv.put("product",product.product);
        cv.put("description",product.description);
        cv.put("price",product.price);
        cv.put("in_stock",product.in_stock);

        long rowID = db.insert(DBH.TABLE_NAME, null,cv);
        Log.d(DBH.LOG_TAG, "row inserted, ID = " + rowID);
        EventListener.Udpate();
    }
    //Функция обновления строки в таблице
    public void Update(Row row) {
        ContentValues cv = new ContentValues();
        cv.put("article",row.article);
        cv.put("product",row.product);
        cv.put("description",row.description);
        cv.put("price",row.price);
        cv.put("in_stock",row.in_stock);

        long rowID = db.update(DBH.TABLE_NAME, cv,"article = ?",new String[]{row.article});
        Log.d(DBH.LOG_TAG, "row inserted, ID = " + rowID);
        EventListener.Udpate();
    }
    //Функция удаления строки с таблици
    public void Remove(String article) {
        long rowID = db.delete(DBH.TABLE_NAME, "article = ?", new String[]{article});
        Log.d(DBH.LOG_TAG, "row inserted, ID = " + rowID);
        EventListener.Udpate();
    }
    //Функция чтения всей таблицы
    public Table Read(){
        Cursor c = db.query(DBH.TABLE_NAME, null, null, null, null, null, null);
        return CursorToTable(c);
    }
    //Функция поиска
    public Table Search(String[] args){
        if(args[0]!=null && args[0]=="")Read();
        Cursor c = db.query(DBH.TABLE_NAME,
                new String[] { "article,product,description,price,in_stock"},
                "article = ? OR product = ? OR description = ?", args, null, null, null);
        return CursorToTable(c);
    }
    //Функция отчитски таблицы
    public void Clear(){
        int clearCount = db.delete(DBH.TABLE_NAME, null, null);
        Log.d(DBH.LOG_TAG, "deleted rows count = " + clearCount);
        EventListener.Udpate();
    }
    //Доп. иструмент ядра для преобразования Cursor в Table
    Table CursorToTable(Cursor c){
        Table table = new Table();
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int     articleColIndex = c.getColumnIndex("article"),
                    productColIndex = c.getColumnIndex("product"),
                    descriptionColIndex = c.getColumnIndex("description"),
                    priceColIndex = c.getColumnIndex("price"),
                    in_stockColIndex = c.getColumnIndex("in_stock");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                table.products.add(
                        new Row(
                                c.getString(articleColIndex),
                                c.getString(productColIndex),
                                c.getInt(priceColIndex),
                                c.getString(descriptionColIndex),
                                c.getInt(in_stockColIndex)));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());

            c.close();
            return table;
        } else
            Log.d(DBH.LOG_TAG, "0 rows");
        c.close();
        return new Table();
    }
}
