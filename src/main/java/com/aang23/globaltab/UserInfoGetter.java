package com.aang23.globaltab;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;

import java.util.Optional;

public class UserInfoGetter {
    public static String getPrefixFromUsername(String username) {
        Optional<MetaData> metaDataOptional = getUserMetaData(username);

        if (metaDataOptional.isPresent()) {
            if (metaDataOptional.get().getPrefix() != null) {
                return metaDataOptional.get().getPrefix();
            }
        }

        return "";
    }

    public static String getSuffixFromUsername(String username) {
        Optional<MetaData> metaDataOptional = getUserMetaData(username);

        if (metaDataOptional.isPresent()) {
            if (metaDataOptional.get().getSuffix() != null) {
                return metaDataOptional.get().getSuffix();
            }
        }

        return "";
    }

    private static Optional<MetaData> getUserMetaData(String username) {
        if (!GlobalTab.server.getPluginManager().isLoaded("luckperms")) {
            return Optional.empty();
        }

        Optional<User> userOptional = GlobalTab.luckpermsapi.getUserSafe(username);

        if (!userOptional.isPresent()) {
            return Optional.empty();
        }

        Optional<Contexts> contexts = GlobalTab.luckpermsapi.getContextForUser(userOptional.get());

        return contexts.map(value -> userOptional.get().getCachedData().getMetaData(value));
    }
}