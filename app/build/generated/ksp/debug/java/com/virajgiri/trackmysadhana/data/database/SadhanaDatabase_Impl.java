package com.virajgiri.trackmysadhana.data.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.virajgiri.trackmysadhana.data.dao.SadhanaDao;
import com.virajgiri.trackmysadhana.data.dao.SadhanaDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SadhanaDatabase_Impl extends SadhanaDatabase {
  private volatile SadhanaDao _sadhanaDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `mukhya_sadhana` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `daily_mala_target` INTEGER NOT NULL, `total_mala_target` INTEGER NOT NULL, `start_date` TEXT NOT NULL, `end_date` TEXT NOT NULL, `mukhya_mantra_text` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sub_mantra` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sadhana_id` INTEGER NOT NULL, `type` TEXT NOT NULL, `mantra_text` TEXT NOT NULL, `daily_mala_target` INTEGER NOT NULL, FOREIGN KEY(`sadhana_id`) REFERENCES `mukhya_sadhana`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sub_mantra_sadhana_id` ON `sub_mantra` (`sadhana_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `jap_entry` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sadhana_id` INTEGER NOT NULL, `date` TEXT NOT NULL, `mala_count` INTEGER NOT NULL, `experience_note` TEXT NOT NULL, `sub_mantra_id` INTEGER, FOREIGN KEY(`sadhana_id`) REFERENCES `mukhya_sadhana`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_jap_entry_sadhana_id` ON `jap_entry` (`sadhana_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '758bd33ce123cd53f338426ce6850126')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `mukhya_sadhana`");
        db.execSQL("DROP TABLE IF EXISTS `sub_mantra`");
        db.execSQL("DROP TABLE IF EXISTS `jap_entry`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMukhyaSadhana = new HashMap<String, TableInfo.Column>(7);
        _columnsMukhyaSadhana.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMukhyaSadhana.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMukhyaSadhana.put("daily_mala_target", new TableInfo.Column("daily_mala_target", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMukhyaSadhana.put("total_mala_target", new TableInfo.Column("total_mala_target", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMukhyaSadhana.put("start_date", new TableInfo.Column("start_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMukhyaSadhana.put("end_date", new TableInfo.Column("end_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMukhyaSadhana.put("mukhya_mantra_text", new TableInfo.Column("mukhya_mantra_text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMukhyaSadhana = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMukhyaSadhana = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMukhyaSadhana = new TableInfo("mukhya_sadhana", _columnsMukhyaSadhana, _foreignKeysMukhyaSadhana, _indicesMukhyaSadhana);
        final TableInfo _existingMukhyaSadhana = TableInfo.read(db, "mukhya_sadhana");
        if (!_infoMukhyaSadhana.equals(_existingMukhyaSadhana)) {
          return new RoomOpenHelper.ValidationResult(false, "mukhya_sadhana(com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana).\n"
                  + " Expected:\n" + _infoMukhyaSadhana + "\n"
                  + " Found:\n" + _existingMukhyaSadhana);
        }
        final HashMap<String, TableInfo.Column> _columnsSubMantra = new HashMap<String, TableInfo.Column>(5);
        _columnsSubMantra.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubMantra.put("sadhana_id", new TableInfo.Column("sadhana_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubMantra.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubMantra.put("mantra_text", new TableInfo.Column("mantra_text", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubMantra.put("daily_mala_target", new TableInfo.Column("daily_mala_target", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubMantra = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSubMantra.add(new TableInfo.ForeignKey("mukhya_sadhana", "CASCADE", "NO ACTION", Arrays.asList("sadhana_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSubMantra = new HashSet<TableInfo.Index>(1);
        _indicesSubMantra.add(new TableInfo.Index("index_sub_mantra_sadhana_id", false, Arrays.asList("sadhana_id"), Arrays.asList("ASC")));
        final TableInfo _infoSubMantra = new TableInfo("sub_mantra", _columnsSubMantra, _foreignKeysSubMantra, _indicesSubMantra);
        final TableInfo _existingSubMantra = TableInfo.read(db, "sub_mantra");
        if (!_infoSubMantra.equals(_existingSubMantra)) {
          return new RoomOpenHelper.ValidationResult(false, "sub_mantra(com.virajgiri.trackmysadhana.data.entity.SubMantra).\n"
                  + " Expected:\n" + _infoSubMantra + "\n"
                  + " Found:\n" + _existingSubMantra);
        }
        final HashMap<String, TableInfo.Column> _columnsJapEntry = new HashMap<String, TableInfo.Column>(6);
        _columnsJapEntry.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJapEntry.put("sadhana_id", new TableInfo.Column("sadhana_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJapEntry.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJapEntry.put("mala_count", new TableInfo.Column("mala_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJapEntry.put("experience_note", new TableInfo.Column("experience_note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJapEntry.put("sub_mantra_id", new TableInfo.Column("sub_mantra_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysJapEntry = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysJapEntry.add(new TableInfo.ForeignKey("mukhya_sadhana", "CASCADE", "NO ACTION", Arrays.asList("sadhana_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesJapEntry = new HashSet<TableInfo.Index>(1);
        _indicesJapEntry.add(new TableInfo.Index("index_jap_entry_sadhana_id", false, Arrays.asList("sadhana_id"), Arrays.asList("ASC")));
        final TableInfo _infoJapEntry = new TableInfo("jap_entry", _columnsJapEntry, _foreignKeysJapEntry, _indicesJapEntry);
        final TableInfo _existingJapEntry = TableInfo.read(db, "jap_entry");
        if (!_infoJapEntry.equals(_existingJapEntry)) {
          return new RoomOpenHelper.ValidationResult(false, "jap_entry(com.virajgiri.trackmysadhana.data.entity.JapEntry).\n"
                  + " Expected:\n" + _infoJapEntry + "\n"
                  + " Found:\n" + _existingJapEntry);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "758bd33ce123cd53f338426ce6850126", "28bfc2e5e8a70f6ca9c5904f8d09bcba");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "mukhya_sadhana","sub_mantra","jap_entry");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `mukhya_sadhana`");
      _db.execSQL("DELETE FROM `sub_mantra`");
      _db.execSQL("DELETE FROM `jap_entry`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SadhanaDao.class, SadhanaDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SadhanaDao sadhanaDao() {
    if (_sadhanaDao != null) {
      return _sadhanaDao;
    } else {
      synchronized(this) {
        if(_sadhanaDao == null) {
          _sadhanaDao = new SadhanaDao_Impl(this);
        }
        return _sadhanaDao;
      }
    }
  }
}
