package com.aang23.globaltab;

import me.lucko.luckperms.api.Contexts;

public class UserInfoGetter {
    public static String getPrefixFromUsername(String username) {
        if (GlobalTab.luckpermsapi.getUserSafe(username).isPresent()) {
            Contexts contexts = GlobalTab.luckpermsapi.getContextForUser(GlobalTab.luckpermsapi.getUser(username))
                    .get();
            return GlobalTab.luckpermsapi.getUser(username).getCachedData().getMetaData(contexts).getPrefix()
                    .toString();
        } else
            return null;
    }
}