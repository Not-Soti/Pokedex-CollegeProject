package com.example.pokedex.Persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@androidx.room.Database(entities = PokemonTeamEntity.class, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract PokemonDAO pokemonDAO();
    private static Database INSTANCE;

    public static Database getInstance(final Context context){
        if(INSTANCE==null){
            synchronized (Database.class){
                if(INSTANCE==null){
                    //Se crea la base de datos
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class, "pokemon_database").addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Create and open callbacks
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //new PopulateDBAsync(INSTANCE).execute();
        }
    };

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }


    private static class PopulateDBAsync extends AsyncTask<Void, Void, Void>{

        private final PokemonDAO pokeDao;

        PopulateDBAsync(Database db){
            pokeDao=db.pokemonDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            pokeDao.deleteAll();

            for(int i=1; i<=30; i++) {
                PokemonTeamEntity poke = new PokemonTeamEntity();
                poke.setId(i);
                poke.setNombre("Nombre del poke "+i);
                poke.setMov1("mov 1 - poke " +i);
                poke.setMov1("mov 2 - poke " +i);
                poke.setMov1("mov 3 - poke " +i);
                poke.setMov1("mov 4 - poke " +i);

                Database.INSTANCE.pokemonDAO().insertPokemon(poke);
            }
            Log.d("---", "Añadidos");
            return null;
        }
    }
}
