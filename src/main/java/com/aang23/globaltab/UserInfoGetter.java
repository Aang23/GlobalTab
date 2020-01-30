package com.aang23.globaltab;

import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

public class UserInfoGetter {
    public static String getPrefixFromUsername(String username) {
        if (GlobalTab.server.getPluginManager().isLoaded("luckperms")) {
            User lpUser = GlobalTab.lpApi.getUserManager().getUser(username);
            if (lpUser != null) {
                return lpUser.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
            }
        }
        return "";
    }

    public static String getSuffixFromUsername(String username) {
        if (GlobalTab.server.getPluginManager().isLoaded("luckperms")) {
            User lpUser = GlobalTab.lpApi.getUserManager().getUser(username);
            if (lpUser != null) {
                return lpUser.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getSuffix();
            }
        }
        return "";
    }
}