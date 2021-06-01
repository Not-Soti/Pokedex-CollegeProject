package com.example.pokedex;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.tabs.TabLayout;
import com.schibsted.spain.barista.rule.BaristaRule;
import com.schibsted.spain.barista.rule.flaky.Repeat;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kotlin.jvm.JvmField;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.schibsted.spain.barista.assertion.BaristaImageViewAssertions.assertHasDrawable;
import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.*;
import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.*;
import static com.schibsted.spain.barista.internal.matcher.HelperMatchers.atPosition;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PokemonIndexInstrumentedTest {

    private int downloadTime = 15000; //Tiempo para esperar a que descargue la informacion (en torno a 10 segundos)

    /**
     * Regla para lanzar la Main Activity
     */
    @Rule
    @JvmField
    public BaristaRule<MainActivity> baristaRule = BaristaRule.create(MainActivity.class);

    @Before
    public void setUp(){
        baristaRule.launchActivity();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.pokedex", appContext.getPackageName());
    }

    /**
     * Test que comprueba que al utilizar la pokedex
     * se descargan los datos y aparece el diálogo que lo indica
     */
    @Test
    public void openPokedexCheckDownloadDialogAppears(){
        clickOn(R.id.host_pokedexBtn);
        assertContains("Descargando");
    }

    /**
     * Test que comprueba que cuando se haya descargado toda
     * la informacion, los dialogos desaparecen
     */
    @Test
    @Repeat(times = 1) //Por defecto lo repite 5 veces
    public void downloadAllInfoDialogDisappears() throws InterruptedException {
        clickOn(R.id.host_pokedexBtn);
        Thread.sleep(downloadTime); //Esperar a que se descargue la informacion
        assertNotContains("Descargando");
    }

    /**
     * Test que hace a un pokemon favorito, y comprueba que se muestra
     * en la lista de favoritos
     */
    @Test
    @Repeat(times = 1)
    public void checkPokemonIsFavourite() throws InterruptedException {

        clickOn(R.id.host_pokedexBtn);
        Thread.sleep(downloadTime); //Esperar a la descarga

        //Se hace click en el boton de hacer favorito al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Check button drawable";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_fav_button);
                favButton.performClick();
            }
        }));

        //Se hace click en el TabLayout para ir a la pestaña de favoritos
        onView(withText("FAVORITOS")).perform(click());

        Thread.sleep(downloadTime); //Se espera a que descarguen los pokemon si fuese necesario
        assertContains("Bulbasaur"); //Se comprueba que se ha introducido
    }

    /**
     * Test que comprueba que al eliminar un pokemon de favoritos, desde la
     * pestaña de favoritos, deja de serlo
     */
    @Test
    @Repeat(times = 1)
    public void removeFavouriteFromFavTab() throws InterruptedException {

        clickOn(R.id.host_pokedexBtn);
        Thread.sleep(downloadTime); //Esperar a la descarga

        //Se hace click en el boton de hacer favorito al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on fav button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_fav_button);
                favButton.performClick();
            }
        }));

        //Se hace click en el TabLayout para ir a la pestaña de favoritos
        onView(withText("FAVORITOS")).perform(click());

        Thread.sleep(downloadTime); //Se espera a que descarguen los pokemon si fuese necesario

        //Se hace click en el boton de eliminar favorito al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on fav button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_fav_button);
                favButton.performClick();
            }
        }));
        assertNotContains("Bulbasaur"); //Se comprueba que ya no está en la lista
    }


    /**
     * Test que comprueba que al eliminar un pokemon de favoritos, desde la
     * pestaña de ver a todos los pokemon, no aparece en la pestaña de favoritos
     */
    @Test
    @Repeat(times = 1)
    public void removeFavouriteFromAllTab() throws InterruptedException {

        clickOn(R.id.host_pokedexBtn);
        Thread.sleep(downloadTime); //Esperar a la descarga

        //Se hace click en el boton de hacer favorito al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on fav button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_fav_button);
                favButton.performClick();
            }
        }));

        //Se hace click en el boton de eliminar favorito al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on fav button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_fav_button);
                favButton.performClick();
            }
        }));

        //Se hace click en el TabLayout para ir a la pestaña de favoritos
        onView(withText("FAVORITOS")).perform(click());

        Thread.sleep(downloadTime); //Se espera a que descarguen los pokemon si fuese necesario

        assertNotContains("Bulbasaur"); //Se comprueba que ya no está en la lista
    }

    /**
     * Test que añade un pokemon al equipo desde la lista total de pokemon,
     * y comprueba que aparece en la pantalla del equipo
     */
    @Test
    @Repeat(times = 1)
    public void addPokemonToTeam() throws InterruptedException {

        clickOn(R.id.host_pokedexBtn);
        Thread.sleep(downloadTime); //Esperar a la descarga

        //Se hace click en el boton de hacer favorito al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on add to team button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_addToTeam_button);
                favButton.performClick();
            }
        }));

        //Se va a la pantalla de inicio
        pressBack();

        //Se va a la pantalla de equipo
        clickOn(R.id.host_teamBtn);

        Thread.sleep(downloadTime); //Se espera a que se lean los pokemon de la BBDD

        assertContains("Bulbasaur"); //Se comprueba que está en la lista
    }

    /**
     * Test que añade un pokemon al equipo, y lo elimina desde la pantalla de ver el equipo,
     * comprobando que se elimina correctamente
     */
    @Test
    @Repeat(times = 1)
    public void removePokemonFromTeamFromTeamScreen() throws InterruptedException {

        clickOn(R.id.host_pokedexBtn);
        Thread.sleep(downloadTime); //Esperar a la descarga

        //Se hace click en el boton de añadir al equipo al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on add to team button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_addToTeam_button);
                favButton.performClick();
            }
        }));

        //Se va a la pantalla de inicio
        pressBack();

        //Se va a la pantalla de equipo
        clickOn(R.id.host_teamBtn);

        Thread.sleep(downloadTime); //Se espera a que se lean los pokemon de la BBDD

        //Se hace click en el boton de hacer eliminar del equipo al primer pokemon de la lista
        onView(withId(R.id.frag_team_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on remove to team button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.card_team_removeFromTeam_button);
                favButton.performClick();
            }
        }));

        assertNotContains("Bulbasaur"); //Se comprueba que está en la lista
    }


    /**
     * Test que añade un pokemon al equipo, y lo elimina desde la pantalla del listado
     * de pokemon, comprobando que no aparece en la de equipo
     */

    @Test
    @Repeat(times = 1)
    public void removePokemonFromTeamFromPokedexScreen() throws InterruptedException {

        clickOn(R.id.host_pokedexBtn);
        Thread.sleep(downloadTime); //Esperar a la descarga

        //Se hace click en el boton de añadir al equipo al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on add to team button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_addToTeam_button);
                favButton.performClick();
            }
        }));

        //Se hace click en el boton de eliminar del equipo al primer pokemon de la lista
        onView(withId(R.id.pokedexFrag_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "Click on remove from team button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View favButton = view.findViewById(R.id.pokeCard_addToTeam_button);
                favButton.performClick();
            }
        }));

        //Se va a la pantalla de inicio
        pressBack();

        //Se va a la pantalla de equipo
        clickOn(R.id.host_teamBtn);

        Thread.sleep(downloadTime); //Se espera a que se lean los pokemon de la BBDD

        assertNotContains("Bulbasaur"); //Se comprueba que está en la lista
    }
}