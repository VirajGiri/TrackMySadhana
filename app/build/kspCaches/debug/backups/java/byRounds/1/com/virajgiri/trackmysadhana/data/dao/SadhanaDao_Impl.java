package com.virajgiri.trackmysadhana.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.virajgiri.trackmysadhana.data.entity.JapEntry;
import com.virajgiri.trackmysadhana.data.entity.MukhyaSadhana;
import com.virajgiri.trackmysadhana.data.entity.SubMantra;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SadhanaDao_Impl implements SadhanaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MukhyaSadhana> __insertionAdapterOfMukhyaSadhana;

  private final EntityInsertionAdapter<SubMantra> __insertionAdapterOfSubMantra;

  private final EntityInsertionAdapter<JapEntry> __insertionAdapterOfJapEntry;

  private final EntityDeletionOrUpdateAdapter<MukhyaSadhana> __deletionAdapterOfMukhyaSadhana;

  private final EntityDeletionOrUpdateAdapter<JapEntry> __deletionAdapterOfJapEntry;

  private final EntityDeletionOrUpdateAdapter<MukhyaSadhana> __updateAdapterOfMukhyaSadhana;

  private final EntityDeletionOrUpdateAdapter<JapEntry> __updateAdapterOfJapEntry;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSubMantrasForSadhana;

  public SadhanaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMukhyaSadhana = new EntityInsertionAdapter<MukhyaSadhana>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `mukhya_sadhana` (`id`,`name`,`daily_mala_target`,`total_mala_target`,`start_date`,`end_date`,`mukhya_mantra_text`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MukhyaSadhana entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getDailyMalaTarget());
        statement.bindLong(4, entity.getTotalMalaTarget());
        statement.bindString(5, entity.getStartDate());
        statement.bindString(6, entity.getEndDate());
        statement.bindString(7, entity.getMukhyaMantraText());
      }
    };
    this.__insertionAdapterOfSubMantra = new EntityInsertionAdapter<SubMantra>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sub_mantra` (`id`,`sadhana_id`,`type`,`mantra_text`,`daily_mala_target`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubMantra entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSadhanaId());
        statement.bindString(3, entity.getType());
        statement.bindString(4, entity.getMantraText());
        statement.bindLong(5, entity.getDailyMalaTarget());
      }
    };
    this.__insertionAdapterOfJapEntry = new EntityInsertionAdapter<JapEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `jap_entry` (`id`,`sadhana_id`,`date`,`mala_count`,`experience_note`,`sub_mantra_id`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JapEntry entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSadhanaId());
        statement.bindString(3, entity.getDate());
        statement.bindLong(4, entity.getMalaCount());
        statement.bindString(5, entity.getExperienceNote());
        if (entity.getSubMantraId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getSubMantraId());
        }
      }
    };
    this.__deletionAdapterOfMukhyaSadhana = new EntityDeletionOrUpdateAdapter<MukhyaSadhana>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `mukhya_sadhana` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MukhyaSadhana entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfJapEntry = new EntityDeletionOrUpdateAdapter<JapEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `jap_entry` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JapEntry entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMukhyaSadhana = new EntityDeletionOrUpdateAdapter<MukhyaSadhana>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `mukhya_sadhana` SET `id` = ?,`name` = ?,`daily_mala_target` = ?,`total_mala_target` = ?,`start_date` = ?,`end_date` = ?,`mukhya_mantra_text` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MukhyaSadhana entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getDailyMalaTarget());
        statement.bindLong(4, entity.getTotalMalaTarget());
        statement.bindString(5, entity.getStartDate());
        statement.bindString(6, entity.getEndDate());
        statement.bindString(7, entity.getMukhyaMantraText());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__updateAdapterOfJapEntry = new EntityDeletionOrUpdateAdapter<JapEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `jap_entry` SET `id` = ?,`sadhana_id` = ?,`date` = ?,`mala_count` = ?,`experience_note` = ?,`sub_mantra_id` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final JapEntry entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSadhanaId());
        statement.bindString(3, entity.getDate());
        statement.bindLong(4, entity.getMalaCount());
        statement.bindString(5, entity.getExperienceNote());
        if (entity.getSubMantraId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getSubMantraId());
        }
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSubMantrasForSadhana = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sub_mantra WHERE sadhana_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSadhana(final MukhyaSadhana sadhana,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMukhyaSadhana.insertAndReturnId(sadhana);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSubMantra(final SubMantra mantra,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSubMantra.insert(mantra);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertJapEntry(final JapEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfJapEntry.insert(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSadhana(final MukhyaSadhana sadhana,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMukhyaSadhana.handle(sadhana);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteJapEntry(final JapEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfJapEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSadhana(final MukhyaSadhana sadhana,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMukhyaSadhana.handle(sadhana);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateJapEntry(final JapEntry entry, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfJapEntry.handle(entry);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSubMantrasForSadhana(final long sadhanaId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSubMantrasForSadhana.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, sadhanaId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSubMantrasForSadhana.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<MukhyaSadhana>> getAllSadhanas() {
    final String _sql = "SELECT * FROM mukhya_sadhana ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"mukhya_sadhana"}, false, new Callable<List<MukhyaSadhana>>() {
      @Override
      @Nullable
      public List<MukhyaSadhana> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDailyMalaTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "daily_mala_target");
          final int _cursorIndexOfTotalMalaTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "total_mala_target");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
          final int _cursorIndexOfMukhyaMantraText = CursorUtil.getColumnIndexOrThrow(_cursor, "mukhya_mantra_text");
          final List<MukhyaSadhana> _result = new ArrayList<MukhyaSadhana>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MukhyaSadhana _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpDailyMalaTarget;
            _tmpDailyMalaTarget = _cursor.getInt(_cursorIndexOfDailyMalaTarget);
            final int _tmpTotalMalaTarget;
            _tmpTotalMalaTarget = _cursor.getInt(_cursorIndexOfTotalMalaTarget);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            final String _tmpMukhyaMantraText;
            _tmpMukhyaMantraText = _cursor.getString(_cursorIndexOfMukhyaMantraText);
            _item = new MukhyaSadhana(_tmpId,_tmpName,_tmpDailyMalaTarget,_tmpTotalMalaTarget,_tmpStartDate,_tmpEndDate,_tmpMukhyaMantraText);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getSadhanaCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM mukhya_sadhana";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSadhanaById(final long id,
      final Continuation<? super MukhyaSadhana> $completion) {
    final String _sql = "SELECT * FROM mukhya_sadhana WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MukhyaSadhana>() {
      @Override
      @Nullable
      public MukhyaSadhana call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDailyMalaTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "daily_mala_target");
          final int _cursorIndexOfTotalMalaTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "total_mala_target");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
          final int _cursorIndexOfMukhyaMantraText = CursorUtil.getColumnIndexOrThrow(_cursor, "mukhya_mantra_text");
          final MukhyaSadhana _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpDailyMalaTarget;
            _tmpDailyMalaTarget = _cursor.getInt(_cursorIndexOfDailyMalaTarget);
            final int _tmpTotalMalaTarget;
            _tmpTotalMalaTarget = _cursor.getInt(_cursorIndexOfTotalMalaTarget);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            final String _tmpMukhyaMantraText;
            _tmpMukhyaMantraText = _cursor.getString(_cursorIndexOfMukhyaMantraText);
            _result = new MukhyaSadhana(_tmpId,_tmpName,_tmpDailyMalaTarget,_tmpTotalMalaTarget,_tmpStartDate,_tmpEndDate,_tmpMukhyaMantraText);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<SubMantra>> getSubMantrasForSadhana(final long sadhanaId) {
    final String _sql = "SELECT * FROM sub_mantra WHERE sadhana_id = ? ORDER BY type ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sadhanaId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"sub_mantra"}, false, new Callable<List<SubMantra>>() {
      @Override
      @Nullable
      public List<SubMantra> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSadhanaId = CursorUtil.getColumnIndexOrThrow(_cursor, "sadhana_id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfMantraText = CursorUtil.getColumnIndexOrThrow(_cursor, "mantra_text");
          final int _cursorIndexOfDailyMalaTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "daily_mala_target");
          final List<SubMantra> _result = new ArrayList<SubMantra>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubMantra _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSadhanaId;
            _tmpSadhanaId = _cursor.getLong(_cursorIndexOfSadhanaId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpMantraText;
            _tmpMantraText = _cursor.getString(_cursorIndexOfMantraText);
            final int _tmpDailyMalaTarget;
            _tmpDailyMalaTarget = _cursor.getInt(_cursorIndexOfDailyMalaTarget);
            _item = new SubMantra(_tmpId,_tmpSadhanaId,_tmpType,_tmpMantraText,_tmpDailyMalaTarget);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getSubMantrasOnce(final long sadhanaId,
      final Continuation<? super List<SubMantra>> $completion) {
    final String _sql = "SELECT * FROM sub_mantra WHERE sadhana_id = ? ORDER BY type ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sadhanaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SubMantra>>() {
      @Override
      @NonNull
      public List<SubMantra> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSadhanaId = CursorUtil.getColumnIndexOrThrow(_cursor, "sadhana_id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfMantraText = CursorUtil.getColumnIndexOrThrow(_cursor, "mantra_text");
          final int _cursorIndexOfDailyMalaTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "daily_mala_target");
          final List<SubMantra> _result = new ArrayList<SubMantra>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubMantra _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSadhanaId;
            _tmpSadhanaId = _cursor.getLong(_cursorIndexOfSadhanaId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpMantraText;
            _tmpMantraText = _cursor.getString(_cursorIndexOfMantraText);
            final int _tmpDailyMalaTarget;
            _tmpDailyMalaTarget = _cursor.getInt(_cursorIndexOfDailyMalaTarget);
            _item = new SubMantra(_tmpId,_tmpSadhanaId,_tmpType,_tmpMantraText,_tmpDailyMalaTarget);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalMala(final long sadhanaId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(mala_count), 0) FROM jap_entry WHERE sadhana_id = ? AND sub_mantra_id IS NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sadhanaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTodayMala(final long sadhanaId, final String date,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(mala_count), 0) FROM jap_entry WHERE sadhana_id = ? AND date = ? AND sub_mantra_id IS NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sadhanaId);
    _argIndex = 2;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDaysCompleted(final long sadhanaId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(DISTINCT date) FROM jap_entry WHERE sadhana_id = ? AND sub_mantra_id IS NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sadhanaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSubMantraTodayMala(final long subMantraId, final String date,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(mala_count), 0) FROM jap_entry WHERE sub_mantra_id = ? AND date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subMantraId);
    _argIndex = 2;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSubMantraTotalMala(final long subMantraId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(mala_count), 0) FROM jap_entry WHERE sub_mantra_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subMantraId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<JapEntry>> getEntriesForSadhana(final long sadhanaId) {
    final String _sql = "SELECT * FROM jap_entry WHERE sadhana_id = ? AND sub_mantra_id IS NULL ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sadhanaId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"jap_entry"}, false, new Callable<List<JapEntry>>() {
      @Override
      @Nullable
      public List<JapEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSadhanaId = CursorUtil.getColumnIndexOrThrow(_cursor, "sadhana_id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMalaCount = CursorUtil.getColumnIndexOrThrow(_cursor, "mala_count");
          final int _cursorIndexOfExperienceNote = CursorUtil.getColumnIndexOrThrow(_cursor, "experience_note");
          final int _cursorIndexOfSubMantraId = CursorUtil.getColumnIndexOrThrow(_cursor, "sub_mantra_id");
          final List<JapEntry> _result = new ArrayList<JapEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JapEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSadhanaId;
            _tmpSadhanaId = _cursor.getLong(_cursorIndexOfSadhanaId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpMalaCount;
            _tmpMalaCount = _cursor.getInt(_cursorIndexOfMalaCount);
            final String _tmpExperienceNote;
            _tmpExperienceNote = _cursor.getString(_cursorIndexOfExperienceNote);
            final Long _tmpSubMantraId;
            if (_cursor.isNull(_cursorIndexOfSubMantraId)) {
              _tmpSubMantraId = null;
            } else {
              _tmpSubMantraId = _cursor.getLong(_cursorIndexOfSubMantraId);
            }
            _item = new JapEntry(_tmpId,_tmpSadhanaId,_tmpDate,_tmpMalaCount,_tmpExperienceNote,_tmpSubMantraId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<JapEntry>> getAllEntries() {
    final String _sql = "SELECT * FROM jap_entry ORDER BY date DESC, sadhana_id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"jap_entry"}, false, new Callable<List<JapEntry>>() {
      @Override
      @Nullable
      public List<JapEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSadhanaId = CursorUtil.getColumnIndexOrThrow(_cursor, "sadhana_id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMalaCount = CursorUtil.getColumnIndexOrThrow(_cursor, "mala_count");
          final int _cursorIndexOfExperienceNote = CursorUtil.getColumnIndexOrThrow(_cursor, "experience_note");
          final int _cursorIndexOfSubMantraId = CursorUtil.getColumnIndexOrThrow(_cursor, "sub_mantra_id");
          final List<JapEntry> _result = new ArrayList<JapEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JapEntry _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSadhanaId;
            _tmpSadhanaId = _cursor.getLong(_cursorIndexOfSadhanaId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpMalaCount;
            _tmpMalaCount = _cursor.getInt(_cursorIndexOfMalaCount);
            final String _tmpExperienceNote;
            _tmpExperienceNote = _cursor.getString(_cursorIndexOfExperienceNote);
            final Long _tmpSubMantraId;
            if (_cursor.isNull(_cursorIndexOfSubMantraId)) {
              _tmpSubMantraId = null;
            } else {
              _tmpSubMantraId = _cursor.getLong(_cursorIndexOfSubMantraId);
            }
            _item = new JapEntry(_tmpId,_tmpSadhanaId,_tmpDate,_tmpMalaCount,_tmpExperienceNote,_tmpSubMantraId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<DailyTotal>> getDailyTotals() {
    final String _sql = "SELECT date, SUM(mala_count) as total FROM jap_entry WHERE sub_mantra_id IS NULL GROUP BY date ORDER BY date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"jap_entry"}, false, new Callable<List<DailyTotal>>() {
      @Override
      @Nullable
      public List<DailyTotal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = 0;
          final int _cursorIndexOfTotal = 1;
          final List<DailyTotal> _result = new ArrayList<DailyTotal>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyTotal _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpTotal;
            _tmpTotal = _cursor.getInt(_cursorIndexOfTotal);
            _item = new DailyTotal(_tmpDate,_tmpTotal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
