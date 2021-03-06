package com.liskovsoft.youtubeapi.app;

import android.Manifest;
import androidx.test.rule.GrantPermissionRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Note: Robolectric doesn't support loading native libraries (*.so)
 */
public class AppServiceInstrumentedTest {
    private AppService mAppService;

    @Rule
    public GrantPermissionRule mInternetPermissionRule = GrantPermissionRule.grant(Manifest.permission.INTERNET);

    @Before
    public void setUp() {
        mAppService = AppService.instance();
    }

    /**
     * Note: Robolectric doesn't support loading native libraries (*.so)
     */
    @Test
    public void testThatItemsDecipheredCorrectly() {
        List<String> ciphered = new ArrayList<>();
        String cipher = "ADBVCGD2934FBBBBBDDDFFF";
        ciphered.add(cipher);
        ciphered.add(cipher);
        ciphered.add(cipher);

        List<String> deciphered = mAppService.decipher(ciphered);

        assertNotNull("Deciphered not null", deciphered);
        assertFalse("Deciphered not empty", deciphered.isEmpty());

        for (String decipher : deciphered) {
             assertNotEquals("Cipher and decipher not the same", decipher, cipher);
        }
    }
}