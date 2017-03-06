package com.bx.lessons.rest.storage.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bx.lessons.rest.entity.NoteEntity;

import java.util.ArrayList;
import java.util.List;

public class CRUDOperations {

	private MyDatabase helper;
	public CRUDOperations(SQLiteOpenHelper _helper) {
		super();
		// TODO Auto-generated constructor stub
		helper =(MyDatabase)_helper;
	}

	public void addNote(NoteEntity noteEntity)
	{
		 SQLiteDatabase db = helper.getWritableDatabase(); //modo escritura
		 ContentValues values = new ContentValues();
		 values.put(MyDatabase.KEY_NAME, noteEntity.getName());
		 values.put(MyDatabase.KEY_DESC, noteEntity.getDescription());
		 values.put(MyDatabase.KEY_PATH, noteEntity.getPath());
		 
		 db.insert(MyDatabase.TABLE_NOTES, null, values);
		 db.close();
	}
	
	public NoteEntity getNote(int id)
	{
		SQLiteDatabase db = helper.getReadableDatabase(); //modo lectura
		Cursor cursor = db.query(MyDatabase.TABLE_NOTES,
				new String[]{MyDatabase.KEY_ID, MyDatabase.KEY_NAME,
						MyDatabase.KEY_DESC, MyDatabase.KEY_PATH},
				MyDatabase.KEY_ID + "=?",
				new String[]{String.valueOf(id)}, null, null, null);
		if(cursor!=null)
		{
			cursor.moveToFirst();
		}
		int nid = Integer.parseInt(cursor.getString(0));
		String name = cursor.getString(1);
		String desc = cursor.getString(2);
		String path = cursor.getString(3);

		NoteEntity noteEntity= new NoteEntity(
				nid, name, desc,path);
		return noteEntity;
	}
	
	public List<NoteEntity> getAllNotes()
	{
		List<NoteEntity> lst =new ArrayList<NoteEntity>();
		String sql= "SELECT  * FROM " + MyDatabase.TABLE_NOTES;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
			do
			{
				NoteEntity noteEntity =new NoteEntity();
				noteEntity.setId(Integer.parseInt(cursor.getString(0)));
				noteEntity.setName(cursor.getString(1));
				noteEntity.setDescription(cursor.getString(2));
				noteEntity.setPath(cursor.getString(3));

				lst.add(noteEntity);
			}while(cursor.moveToNext());
		}
		return lst;
	}
	
	public int getNotesCount()
	{
		String sql= "SELECT * FROM "+MyDatabase.TABLE_NOTES;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		cursor.close();
		
		return cursor.getCount();
	}
	
	//--------------------------------------------
	
	public int updateNote(NoteEntity noteEntity)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(MyDatabase.KEY_NAME, noteEntity.getName());
		values.put(MyDatabase.KEY_DESC, noteEntity.getDescription());
		values.put(MyDatabase.KEY_PATH, noteEntity.getPath());

		return db.update(MyDatabase.TABLE_NOTES,
				values,
				MyDatabase.KEY_ID+"=?",
				new String[]{String.valueOf(noteEntity.getId())});
	}
	//--------------------------------------------
	
	public int deleteNote(NoteEntity noteEntity)
	{
		 SQLiteDatabase db = helper.getWritableDatabase(); 
		 int row= db.delete(MyDatabase.TABLE_NOTES,
				 MyDatabase.KEY_ID+"=?", 
				 new String[]{String.valueOf(noteEntity.getId())});
		 db.close();
		return row;
	}
}
