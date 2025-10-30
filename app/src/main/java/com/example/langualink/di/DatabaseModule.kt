package com.example.langualink.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.langualink.data.local.database.AppDatabase
import com.example.langualink.model.Language
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "langualink-db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Pre-populate the database with initial data
                CoroutineScope(Dispatchers.IO).launch {
                    val database = provideAppDatabase(context)
                    database.languageDao().insertLanguages(getInitialLanguages())
                    database.exerciseDao().insertExercises(getMockExercises())
                }
            }
        }).build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    fun provideLanguageDao(appDatabase: AppDatabase) = appDatabase.languageDao()

    @Provides
    fun provideBadgeDao(appDatabase: AppDatabase) = appDatabase.badgeDao()

    @Provides
    fun provideExerciseDao(appDatabase: AppDatabase) = appDatabase.exerciseDao()

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): com.example.langualink.data.local.SettingsDataStore {
        return com.example.langualink.data.local.SettingsDataStore(context)
    }

    private fun getInitialLanguages(): List<Language> {
        return listOf(
            Language(1, "Français"),
            Language(2, "Español"),
            Language(3, "Deutsch"),
            Language(4, "日本語"),
            Language(5, "Português"),
            Language(6, "Italiano"),
            Language(7, "한국어"),
            Language(8, "Русский"),
            Language(9, "中文"),
            Language(10, "العربية")
        )
    }

    private fun getMockExercises(): List<com.example.langualink.model.Exercise> {
        return listOf(
            // French A1
            com.example.langualink.model.Exercise(1, 1, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Quelle est la couleur du ciel?", listOf("Bleu", "Rouge", "Vert", "Jaune"), "Bleu"),
            com.example.langualink.model.Exercise(2, 1, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'cat'", listOf("Chat", "Chien", "Poisson", "Oiseau"), "Chat"),
            com.example.langualink.model.Exercise(3, 1, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Que dit une vache?", listOf("Meuh", "Bêêê", "Ouin", "Coin"), "Meuh"),
            com.example.langualink.model.Exercise(4, 1, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'book'", listOf("Livre", "Table", "Chaise", "Porte"), "Livre"),
            com.example.langualink.model.Exercise(5, 1, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Combien de jours dans une semaine?", listOf("7", "5", "10", "12"), "7"),

            // French A2
            com.example.langualink.model.Exercise(6, 1, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Quel est le passé composé de 'manger' (avec 'je')?", listOf("J'ai mangé", "Je mangeais", "Je mangerai", "Je mange"), "J'ai mangé"),
            com.example.langualink.model.Exercise(7, 1, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'Yesterday, I visited my friends'", listOf("Hier, j'ai rendu visite à mes amis", "Demain, je visiterai mes amis", "Aujourd'hui, je visite mes amis", "Je vais visiter mes amis"), "Hier, j'ai rendu visite à mes amis"),
            com.example.langualink.model.Exercise(8, 1, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Complétez : 'Je vais ___ marché.'", listOf("au", "à la", "aux", "à l'"), "au"),

            // French B1
            com.example.langualink.model.Exercise(9, 1, com.example.langualink.model.Level.B1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Si j'avais de l'argent, ...", listOf("j'achèterais une voiture", "j'ai acheté une voiture", "j'achète une voiture", "j'achèterai une voiture"), "j'achèterais une voiture"),
            com.example.langualink.model.Exercise(10, 1, com.example.langualink.model.Level.B1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'I have been living here for 5 years.'", listOf("J'habite ici depuis 5 ans", "J'ai habité ici pendant 5 ans", "J'habiterai ici pour 5 ans", "Je vivais ici depuis 5 ans"), "J'habite ici depuis 5 ans"),

            // French B2
            com.example.langualink.model.Exercise(11, 1, com.example.langualink.model.Level.B2, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Lequel utilise correctement le subjonctif : 'Il faut que ...'", listOf("tu fasses tes devoirs", "tu fais tes devoirs", "tu feras tes devoirs", "tu as fait tes devoirs"), "tu fasses tes devoirs"),
            com.example.langualink.model.Exercise(12, 1, com.example.langualink.model.Level.B2, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'Although it is raining, we will go out.'", listOf("Bien qu'il pleuve, nous sortirons", "Comme il pleut, nous sortirons", "Puisqu'il pleut, nous sortirons", "Même s'il pleut, nous sortirons"), "Bien qu'il pleuve, nous sortirons"),

            // French C1
            com.example.langualink.model.Exercise(13, 1, com.example.langualink.model.Level.C1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Choisissez le synonyme de 'désuet'.", listOf("Obsolète", "Moderne", "Efficace", "Nouveau"), "Obsolète"),
            com.example.langualink.model.Exercise(14, 1, com.example.langualink.model.Level.C1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'He is said to be the best player.'", listOf("On dit qu'il est le meilleur joueur", "Il a dit être le meilleur joueur", "Il se dit le meilleur joueur", "Il est dit par le meilleur joueur"), "On dit qu'il est le meilleur joueur"),

            // French C2
            com.example.langualink.model.Exercise(15, 1, com.example.langualink.model.Level.C2, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Quel est le passé antérieur de 'finir' (avec 'dès que nous...')?", listOf("dès que nous eûmes fini", "dès que nous avions fini", "dès que nous finissions", "dès que nous avons fini"), "dès que nous eûmes fini"),
            com.example.langualink.model.Exercise(16, 1, com.example.langualink.model.Level.C2, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'Had I known, I would have come sooner.'", listOf("Si j'avais su, je serais venu plus tôt", "Si je savais, je viendrais plus tôt", "Sachant, je serais venu plus tôt", "Ayant su, je viendrais plus tôt"), "Si j'avais su, je serais venu plus tôt"),

            // Spanish A1
            com.example.langualink.model.Exercise(21, 2, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "¿Cuál es la capital de España?", listOf("Madrid", "Barcelona", "Lisboa", "Roma"), "Madrid"),
            com.example.langualink.model.Exercise(22, 2, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'Hello'", listOf("Hola", "Adiós", "Gracias", "Por favor"), "Hola"),
            com.example.langualink.model.Exercise(23, 2, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "¿Cómo se dice 'dog' en español?", listOf("Perro", "Gato", "Pájaro", "Pez"), "Perro"),
            com.example.langualink.model.Exercise(24, 2, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'Good morning'", listOf("Buenos días", "Buenas tardes", "Buenas noches", "Adiós"), "Buenos días"),

            // Spanish A2
            com.example.langualink.model.Exercise(25, 2, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Ayer, yo ___ al cine.", listOf("fui", "voy", "iré", "iba"), "fui"),
            com.example.langualink.model.Exercise(26, 2, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'I have to work'", listOf("Tengo que trabajar", "Quiero trabajar", "Puedo trabajar", "Voy a trabajar"), "Tengo que trabajar"),

            // Spanish B1
            com.example.langualink.model.Exercise(27, 2, com.example.langualink.model.Level.B1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "No creo que ___ verdad.", listOf("sea", "es", "está", "será"), "sea"),
            com.example.langualink.model.Exercise(28, 2, com.example.langualink.model.Level.B1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'When I was a child, I used to play here.'", listOf("Cuando era niño, jugaba aquí", "Cuando soy niño, juego aquí", "Cuando fui niño, jugué aquí", "Cuando sea niño, jugaré aquí"), "Cuando era niño, jugaba aquí"),

            // German A1
            com.example.langualink.model.Exercise(31, 3, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Wie sagt man 'water' auf Deutsch?", listOf("Wasser", "Brot", "Milch", "Saft"), "Wasser"),
            com.example.langualink.model.Exercise(32, 3, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'My name is...'", listOf("Mein Name ist...", "Ich bin...", "Du bist...", "Sein Name ist..."), "Mein Name ist..."),

            // German A2
            com.example.langualink.model.Exercise(33, 3, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Ich habe ___ Apfel gegessen.", listOf("einen", "ein", "eine", "einem"), "einen"),
            com.example.langualink.model.Exercise(34, 3, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'I can swim.'", listOf("Ich kann schwimmen.", "Ich will schwimmen.", "Ich muss schwimmen.", "Ich mag schwimmen."), "Ich kann schwimmen."),

            // German B1
            com.example.langualink.model.Exercise(35, 3, com.example.langualink.model.Level.B1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Wenn ich Zeit hätte, ___ ich ins Kino gehen.", listOf("würde", "werde", "wäre", "hätte"), "würde"),
            com.example.langualink.model.Exercise(36, 3, com.example.langualink.model.Level.B1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'He is waiting for the bus.'", listOf("Er wartet auf den Bus.", "Er wartet für den Bus.", "Er wartet den Bus.", "Er wartet mit dem Bus."), "Er wartet auf den Bus."),

            // Japanese A1
            com.example.langualink.model.Exercise(41, 4, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "「猫」は何ですか？", listOf("Cat", "Dog", "Bird", "Fish"), "Cat"),
            com.example.langualink.model.Exercise(42, 4, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'This is a pen.'", listOf("これはペンです。", "それはペンです。", "あれはペンです。", "どれがペンですか。" ), "これはペンです。" ),

            // Japanese A2
            com.example.langualink.model.Exercise(43, 4, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "昨日、映画を___。", listOf("見ました", "見ます", "見たい", "見てください"), "見ました"),
            com.example.langualink.model.Exercise(44, 4, com.example.langualink.model.Level.A2, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'I want to eat sushi.'", listOf("寿司が食べたいです。", "寿司を食べました。", "寿司を食べます。", "寿司を食べています。" ), "寿司が食べたいです。" ),

            // Portuguese A1
            com.example.langualink.model.Exercise(51, 5, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Como se diz 'man' em português?", listOf("Homem", "Mulher", "Menino", "Menina"), "Homem"),
            com.example.langualink.model.Exercise(52, 5, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'I drink water.'", listOf("Eu bebo água.", "Eu como pão.", "Ela bebe leite.", "Ele come queijo."), "Eu bebo água."),

            // Italian A1
            com.example.langualink.model.Exercise(61, 6, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.MULTIPLE_CHOICE, "Come si dice 'apple' in italiano?", listOf("Mela", "Pera", "Arancia", "Banana"), "Mela"),
            com.example.langualink.model.Exercise(62, 6, com.example.langualink.model.Level.A1, com.example.langualink.model.ExerciseType.TRANSLATION, "Traduisez : 'She writes a book'", listOf("Lei scrive un libro", "Lui legge un giornale", "Noi mangiamo una pizza", "Voi bevete acqua"), "Lei scrive un libro")
        )
    }
}