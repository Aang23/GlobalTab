package com.aang23.globaltab;

import me.lucko.luckperms.api.Contexts;

public class UserInfoGetter {
    public static String getPrefixFromUsername(String username) {
        if (GlobalTab.server.getPluginManager().isLoaded("luckperms")) {
            if (GlobalTab.luckpermsapi.getUserSafe(username).isPresent()) {
                Contexts contexts = GlobalTab.luckpermsapi.getContextForUser(GlobalTab.luckpermsapi.getUser(username)).get();
                if (GlobalTab.luckpermsapi.getUser(username).getCachedData().getMetaData(contexts).getPrefix() != null)
                    return GlobalTab.luckpermsapi.getUser(username).getCachedData().getMetaData(contexts).getPrefix();
            }
        }

        return "";
    }

    public static String getSuffixFromUsername(String username) {
        if (GlobalTab.server.getPluginManager().isLoaded("luckperms")) {
            if (GlobalTab.luckpermsapi.getUserSafe(username).isPresent()) {
                Contexts contexts = GlobalTab.luckpermsapi.getContextForUser(GlobalTab.luckpermsapi.getUser(username)).get();
                if (GlobalTab.luckpermsapi.getUser(username).getCachedData().getMetaData(contexts).getSuffix() != null)
                    return GlobalTab.luckpermsapi.getUser(username).getCachedData().getMetaData(contexts).getSuffix();
            }
        }

        return "";
    }
}