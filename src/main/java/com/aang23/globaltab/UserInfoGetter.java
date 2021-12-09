package com.aang23.globaltab;

public class UserInfoGetter {

    private GlobalTab plugin;

    public UserInfoGetter(GlobalTab plugin) {
        this.plugin = plugin;
    }

    public String getPrefixFromUsername(String username) {
        if (plugin.server.getPluginManager().isLoaded("luckperms")) {
            if (plugin.luckpermsapi.getUserManager().getUser(username) != null) {
                if (plugin.luckpermsapi.getUserManager().getUser(username).getCachedData().getMetaData().getPrefix() != null)
                    return plugin.luckpermsapi.getUserManager().getUser(username).getCachedData().getMetaData().getPrefix();
                else
                    return "";
            } else
                return "";
        } else
            return "";
    }

    public String getSuffixFromUsername(String username) {
        if (plugin.server.getPluginManager().isLoaded("luckperms")) {
            if (plugin.luckpermsapi.getUserManager().getUser(username) != null) {
                if (plugin.luckpermsapi.getUserManager().getUser(username).getCachedData().getMetaData().getSuffix() != null)
                    return plugin.luckpermsapi.getUserManager().getUser(username).getCachedData().getMetaData().getSuffix();
                else
                    return "";
            } else
                return "";
        } else
            return "";
    }
}