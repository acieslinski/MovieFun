package pl.acieslinski.moviefun.managers;

import android.widget.Button;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.sql.SQLException;
import java.util.List;

import pl.acieslinski.moviefun.Application;
import pl.acieslinski.moviefun.BuildConfig;
import pl.acieslinski.moviefun.R;
import pl.acieslinski.moviefun.activities.MovieFun;
import pl.acieslinski.moviefun.fragments.SearchList;
import pl.acieslinski.moviefun.fragments.VideoList;
import pl.acieslinski.moviefun.models.Search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Arkadiusz Cieśliński 26.03.16.
 *         <acieslinski@gmail.com>
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21, packageName = "pl.acieslinski.moviefun")
public class DatabaseManagerUnitTest {
    private DatabaseManager databaseManager;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        ShadowLog.stream = System.out;

        databaseManager = Application.getInstance().getDatabaseManager();
    }

    @Test
    public void savingSearchObjectTest() throws Exception {
        String searchTestPhrase = "test";
        String yearTestPhrase = "1999";

        saveSearch(searchTestPhrase, yearTestPhrase);

        List<Search> searches = databaseManager.getSearches();

        assertEquals(1, searches.size());
        assertEquals(searchTestPhrase, searches.get(0).getSearch());
        assertEquals(yearTestPhrase, searches.get(0).getYear());
    }

    @Test
    public void savingRepeatedSearchObjectTest() throws Exception {
        String searchTestPhrase = "test";
        String yearTestPhrase = "1999";

        saveSearch(searchTestPhrase, yearTestPhrase);
        saveSearch(searchTestPhrase, yearTestPhrase);

        List<Search> searches = databaseManager.getSearches();

        // second the same search object should be ignored
        assertEquals(1, searches.size());
        assertEquals(searchTestPhrase, searches.get(0).getSearch());
        assertEquals(yearTestPhrase, searches.get(0).getYear());
    }

    // TODO variations of the savingSearchObjectTest

    @After
    public void tearDown(  ) throws Exception {
        databaseManager.truncate();
    }

    private void saveSearch(String searchPhrase, String yearPhrase) {
        Search search = new Search();

        search.setSearch(searchPhrase);
        search.setYear(yearPhrase);

        databaseManager.saveSearch(search);
    }
}
