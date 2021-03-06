package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.DecipherFunction;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.squareup.duktape.Duktape;
import retrofit2.Call;

import java.util.Arrays;
import java.util.List;

public class AppService {
    private static AppService sInstance;
    private final AppManager mAppManager;
    private final Duktape mDuktape;
    private String mCachedDecipherFunction;

    private AppService() {
        mAppManager = RetrofitHelper.withRegExp(AppManager.class);
        mDuktape = Duktape.create();
    }

    public static AppService instance() {
        if (sInstance == null) {
            sInstance = new AppService();
        }

        return sInstance;
    }

    /**
     * Decipher strings using js code
     */
    public List<String> decipher(List<String> ciphered) {
        if (isAllNulls(ciphered)) {
            return ciphered;
        }

        String decipherCode = createDecipherCode(ciphered);

        return runDecipherCode(decipherCode);
    }

    private static boolean isAllNulls(List<String> ciphered) {
        for (String cipher : ciphered) {
            if (cipher != null) {
                return false;
            }
        }

        return true;
    }

    private String getDecipherFunction() {
        if (mCachedDecipherFunction != null) {
            return mCachedDecipherFunction;
        }

        Call<AppInfo> wrapper = mAppManager.getAppInfo(AppConstants.USER_AGENT_SAMSUNG_1);
        AppInfo appInfo = RetrofitHelper.get(wrapper);

        if (appInfo != null) {
            String playerUrl = appInfo.getPlayerUrl();

            if (playerUrl != null) {
                Call<DecipherFunction> decipherWrapper = mAppManager.getDecipherFunction(AppConstants.SCRIPTS_URL_BASE + playerUrl.replace("\\/", "/"));
                DecipherFunction decipherFunction = RetrofitHelper.get(decipherWrapper);

                if (decipherFunction != null) {
                    String content = decipherFunction.getContent();

                    if (content != null) {
                        mCachedDecipherFunction = Helpers.replace(content, AppConstants.DECIPHER_ORIGINAL_SIGNATURE, "function decipherSignature");
                    }
                }
            }
        }

        return mCachedDecipherFunction;
    }

    private String createDecipherCode(List<String> ciphered) {
        String decipherFunction = getDecipherFunction();

        if (decipherFunction == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        result.append(decipherFunction);
        result.append("var result = [];");

        for (String cipher : ciphered) {
            result.append(String.format("result.push(decipherSignature('%s'));", cipher));
        }

        result.append("result.toString();");

        return result.toString();
    }

    private List<String> runDecipherCode(String decipherCode) {
        String result = mDuktape.evaluate(decipherCode).toString();

        String[] values = result.split(",");

        return Arrays.asList(values);
    }
}
