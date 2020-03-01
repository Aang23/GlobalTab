package com.aang23.globaltab;

import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

public class UserInfoGetter {
    public static String getPrefixFromUsername(String username) {
        if (GlobalTab.server.getPluginManager().isLoaded("luckperms")) {
            User lpUser = GlobalTab.lpApi.getUserManager().getUser(username);
            if (lpUser != null) {
                String prefix = lpUser.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
                if (prefix != null) {
                    return prefix;
                }
            }
        }
        return "";
    }

    public static String getSuffixFromUsername(String username) {
        if (GlobalTab.server.getPluginManager().isLoaded("luckperms")) {
            User lpUser = GlobalTab.lpApi.getUserManager().getUser(username);
            if (lpUser != null) {
                String suffix = lpUser.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getSuffix();
                if (suffix != null) {
                    return suffix;
                }
            }
        }
        return "";
    }
}
