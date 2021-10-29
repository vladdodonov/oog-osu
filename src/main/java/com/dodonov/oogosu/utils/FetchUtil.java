package com.dodonov.oogosu.utils;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Set;

@Slf4j
public class FetchUtil {

    private FetchUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static Fetch setFetch(FetchParent fetch, String fetchesStr) {
        String[] fetches = fetchesStr.split("\\.");
        Fetch newFetch = null;
        for (Object f : fetch.getFetches())
            if (((Fetch) f).getAttribute().getName().equals(fetches[0]))
                newFetch = (Fetch) f;
        if (newFetch == null)
            newFetch = fetch.fetch(fetches[0], JoinType.LEFT);
        if (fetches.length > 1)
            return setFetch(newFetch, fetchConcat(fetches));
        return newFetch;
    }

    private static String fetchConcat(String[] fetches) {
        String newFetchesStr = "";
        for (int i = 0; fetches.length > i; i++) {
            if (i == 1)
                newFetchesStr = newFetchesStr.concat(fetches[i]);
            if (i > 1)
                newFetchesStr = newFetchesStr.concat(".").concat(fetches[i]);
        }
        return newFetchesStr;
    }

    public static void setFetch(Root root, Set<String> fetches) {
        for (String fetch : fetches) {
            setFetch(root, fetch);
        }
    }
}
