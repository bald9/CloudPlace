package com.bald9.utils.url;

/**
 * @author bald9
 * @create 2022-02-06 14:27
 */

import com.bald9.utils.ClassFile;
import com.bald9.utils.file.FilesPatch;

import java.io.IOException;
import java.io.Serializable;
import java.net.CookieStore;
import java.net.URI;
import java.net.HttpCookie;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple in-memory java.net.CookieStore implementation
 *
 * @author Edward Wang
 * @since 1.6
 */
public class LocalCookieStore implements CookieStore, Serializable {
    // the in-memory representation of cookies
    private List<HttpCookie> cookieJar = null;

    // the cookies are indexed by its domain and associated uri (if present)
    // CAUTION: when a cookie removed from main data structure (i.e. cookieJar),
    //          it won't be cleared in domainIndex & uriIndex. Double-check the
    //          presence of cookie when retrieve one form index store.
    private Map<String, List<HttpCookie>> domainIndex = null;
    private Map<URI, List<HttpCookie>> uriIndex = null;
    private Map<String, List<HttpCookie>> nameIndex = null;



    // use ReentrantLock instead of syncronized for scalability
    private ReentrantLock lock = null;

    private Path storePath= Paths.get("data/CookiesStore.cookies").toAbsolutePath().normalize();

    public Path getStorePath() {
        return storePath;
    }

    /**
     * The default ctor
     */
    public LocalCookieStore() {
        cookieJar = new ArrayList<HttpCookie>();
        domainIndex = new HashMap<String, List<HttpCookie>>();
        uriIndex = new HashMap<URI, List<HttpCookie>>();
        nameIndex = new HashMap<String, List<HttpCookie>>();
        lock = new ReentrantLock(false);
    }
    public LocalCookieStore(Path path) {
        cookieJar = new ArrayList<HttpCookie>();
        domainIndex = new HashMap<String, List<HttpCookie>>();
        uriIndex = new HashMap<URI, List<HttpCookie>>();
        nameIndex = new HashMap<String, List<HttpCookie>>();
        lock = new ReentrantLock(false);
        storePath=path.toAbsolutePath().normalize();
    }

    public void setStorePath(Path storePath) {
        this.storePath = storePath;
        loadCookies();
    }

    public void storeCookies(){
        CookiesSave cookiesSave = new CookiesSave(this);
        try {
            FilesPatch.createFileForce(storePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassFile.write(storePath,cookiesSave);
    }

    public void loadCookies(){
        if(Files.exists(storePath)){
            CookiesSave cs= ClassFile.read(storePath);
            this.removeAll(false);
            if(cs!=null){
                cs.backToCookieStore(this);
            }
        }
    }

    public void showCookies(){
        System.out.println("总共个数："+cookieJar.size());
        for (String domain : domainIndex.keySet()) {
            System.out.println("-------------domain: "+domain+" ---------------");
            int i=1;
            for (HttpCookie cookie : domainIndex.get(domain)) {
                System.out.print(i+" ");
                System.out.println(cookie);
                //System.out.println(cookie.getPath());
                //System.out.println(cookie.getDomain());
                i+=1;
            }
            System.out.println("-----------------------------------");
        }
    }

    public List<HttpCookie> get(String name) {
        // argument can't be null
        if (name == null) {
            throw new NullPointerException("name is null");
        }

        lock.lock();
        try {
            List<HttpCookie> httpCookies = nameIndex.get(name);
            return httpCookies;
        } finally {
            lock.unlock();
        }
    }

    public List<HttpCookie> get(String name,String domain) {
        // argument can't be null
        if (name == null) {
            throw new NullPointerException("name is null");
        }

        List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        lock.lock();
        try {
            List<HttpCookie> httpCookies = nameIndex.get(name);
            for (HttpCookie httpCookie : httpCookies) {
                if (httpCookie.getDomain().endsWith(domain)) {
                    cookies.add(httpCookie);
                }
            }
        } finally {
            lock.unlock();
        }
        return cookies;
    }



    /**
     * Add one cookie into cookie store.
     */
    public void add(URI uri, HttpCookie cookie) {
        // pre-condition : argument can't be null
        if (cookie == null) {
            throw new NullPointerException("cookie is null");
        }


        lock.lock();
        try {
            // remove the ole cookie if there has had one
            cookieJar.remove(cookie);

            // add new cookie if it has a non-zero max-age
            if (cookie.getMaxAge() != 0) {
                cookieJar.add(cookie);
                // and add it to domain index
                if (cookie.getDomain() != null) {
                    addIndex(domainIndex, cookie.getDomain(), cookie);
                }
                if (uri != null) {
                    // add it to uri index, too
                    addIndex(uriIndex, getEffectiveURI(uri), cookie);
                }
                if (cookie.getName() != null) {
                    // add it to uri index, too
                    addIndex(nameIndex, cookie.getName(), cookie);
                }
            }
        } finally {
            lock.unlock();
        }
        storeCookies();
    }


    /**
     * Get all cookies, which:
     *  1) given uri domain-matches with, or, associated with
     *     given uri when added to the cookie store.
     *  3) not expired.
     * See RFC 2965 sec. 3.3.4 for more detail.
     */
    public List<HttpCookie> get(URI uri) {
        // argument can't be null
        if (uri == null) {
            throw new NullPointerException("uri is null");
        }

        List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        boolean secureLink = "https".equalsIgnoreCase(uri.getScheme());
        lock.lock();
        try {
            // check domainIndex first
            getInternal1(cookies, domainIndex, uri.getHost(), secureLink);
            // check uriIndex then
            getInternal2(cookies, uriIndex, getEffectiveURI(uri), secureLink);
        } finally {
            lock.unlock();
        }

        return cookies;
    }

    /**
     * Get all cookies in cookie store, except those have expired
     */
    public List<HttpCookie> getCookies() {
        List<HttpCookie> rt;

        lock.lock();
        try {
            Iterator<HttpCookie> it = cookieJar.iterator();
            while (it.hasNext()) {
                if (it.next().hasExpired()) {
                    it.remove();
                }
            }
        } finally {
            rt = Collections.unmodifiableList(cookieJar);
            lock.unlock();
        }

        return rt;
    }

    /**
     * Get all URIs, which are associated with at least one cookie
     * of this cookie store.
     */
    public List<URI> getURIs() {
        List<URI> uris = new ArrayList<URI>();

        lock.lock();
        try {
            Iterator<URI> it = uriIndex.keySet().iterator();
            while (it.hasNext()) {
                URI uri = it.next();
                List<HttpCookie> cookies = uriIndex.get(uri);
                if (cookies == null || cookies.size() == 0) {
                    // no cookies list or an empty list associated with
                    // this uri entry, delete it
                    it.remove();
                }
            }
        } finally {
            uris.addAll(uriIndex.keySet());
            lock.unlock();
        }

        return uris;
    }


    /**
     * Remove a cookie from store
     */
    public boolean remove(URI uri, HttpCookie ck) {
        // argument can't be null
        if (ck == null) {
            throw new NullPointerException("cookie is null");
        }

        boolean modified = false;
        lock.lock();
        try {
            modified = cookieJar.remove(ck);
        } finally {
            lock.unlock();
        }
        storeCookies();
        return modified;
    }


    /**
     * Remove all cookies in this cookie store.
     */
    public boolean removeAll() {
        return removeAll(true);
    }
    public boolean removeAll(boolean isLocal) {
        lock.lock();
        try {
            if (cookieJar.isEmpty()) {
                return false;
            }
            cookieJar.clear();
            domainIndex.clear();
            uriIndex.clear();
            nameIndex.clear();
        } finally {
            lock.unlock();
        }
        if(isLocal){
            this.storeCookies();
        }
        return true;
    }


    /* ---------------- Private operations -------------- */


    /*
     * This is almost the same as HttpCookie.domainMatches except for
     * one difference: It won't reject cookies when the 'H' part of the
     * domain contains a dot ('.').
     * I.E.: RFC 2965 section 3.3.2 says that if host is x.y.domain.com
     * and the cookie domain is .domain.com, then it should be rejected.
     * However that's not how the real world works. Browsers don't reject and
     * some sites, like yahoo.com do actually expect these cookies to be
     * passed along.
     * And should be used for 'old' style cookies (aka Netscape type of cookies)
     */
    private boolean netscapeDomainMatches(String domain, String host)
    {
        if (domain == null || host == null) {
            return false;
        }

        // if there's no embedded dot in domain and domain is not .local
        boolean isLocalDomain = ".local".equalsIgnoreCase(domain);
        int embeddedDotInDomain = domain.indexOf('.');
        if (embeddedDotInDomain == 0) {
            embeddedDotInDomain = domain.indexOf('.', 1);
        }
        if (!isLocalDomain && (embeddedDotInDomain == -1 || embeddedDotInDomain == domain.length() - 1)) {
            return false;
        }

        // if the host name contains no dot and the domain name is .local
        int firstDotInHost = host.indexOf('.');
        if (firstDotInHost == -1 && isLocalDomain) {
            return true;
        }

        int domainLength = domain.length();
        int lengthDiff = host.length() - domainLength;
        if (lengthDiff == 0) {
            // if the host name and the domain name are just string-compare euqal
            return host.equalsIgnoreCase(domain);
        } else if (lengthDiff > 0) {
            // need to check H & D component
            String H = host.substring(0, lengthDiff);
            String D = host.substring(lengthDiff);

            return (D.equalsIgnoreCase(domain));
        } else if (lengthDiff == -1) {
            // if domain is actually .host
            return (domain.charAt(0) == '.' &&
                    host.equalsIgnoreCase(domain.substring(1)));
        }

        return false;
    }

    private void getInternal1(List<HttpCookie> cookies, Map<String, List<HttpCookie>> cookieIndex,
                              String host, boolean secureLink) {
        // Use a separate list to handle cookies that need to be removed so
        // that there is no conflict with iterators.
        ArrayList<HttpCookie> toRemove = new ArrayList<HttpCookie>();
        for (Map.Entry<String, List<HttpCookie>> entry : cookieIndex.entrySet()) {
            String domain = entry.getKey();
            List<HttpCookie> lst = entry.getValue();
            for (HttpCookie c : lst) {
                if ((c.getVersion() == 0 && netscapeDomainMatches(domain, host)) ||
                        (c.getVersion() == 1 && HttpCookie.domainMatches(domain, host))) {
                    if ((cookieJar.indexOf(c) != -1)) {
                        // the cookie still in main cookie store
                        if (!c.hasExpired()) {
                            // don't add twice and make sure it's the proper
                            // security level
                            if ((secureLink || !c.getSecure()) &&
                                    !cookies.contains(c)) {
                                cookies.add(c);
                            }
                        } else {
                            toRemove.add(c);
                        }
                    } else {
                        // the cookie has beed removed from main store,
                        // so also remove it from domain indexed store
                        toRemove.add(c);
                    }
                }
            }
            // Clear up the cookies that need to be removed
            for (HttpCookie c : toRemove) {
                lst.remove(c);
                cookieJar.remove(c);

            }
            toRemove.clear();
        }
    }

    // @param cookies           [OUT] contains the found cookies
    // @param cookieIndex       the index
    // @param comparator        the prediction to decide whether or not
    //                          a cookie in index should be returned
    private <T> void getInternal2(List<HttpCookie> cookies,
                                  Map<T, List<HttpCookie>> cookieIndex,
                                  Comparable<T> comparator, boolean secureLink)
    {
        for (T index : cookieIndex.keySet()) {
            if (comparator.compareTo(index) == 0) {
                List<HttpCookie> indexedCookies = cookieIndex.get(index);
                // check the list of cookies associated with this domain
                if (indexedCookies != null) {
                    Iterator<HttpCookie> it = indexedCookies.iterator();
                    while (it.hasNext()) {
                        HttpCookie ck = it.next();
                        if (cookieJar.indexOf(ck) != -1) {
                            // the cookie still in main cookie store
                            if (!ck.hasExpired()) {
                                // don't add twice
                                if ((secureLink || !ck.getSecure()) &&
                                        !cookies.contains(ck))
                                    cookies.add(ck);
                            } else {
                                it.remove();
                                cookieJar.remove(ck);
                            }
                        } else {
                            // the cookie has beed removed from main store,
                            // so also remove it from domain indexed store
                            it.remove();
                        }
                    }
                } // end of indexedCookies != null
            } // end of comparator.compareTo(index) == 0
        } // end of cookieIndex iteration
    }

    // add 'cookie' indexed by 'index' into 'indexStore'
    private <T> void addIndex(Map<T, List<HttpCookie>> indexStore,
                              T index,
                              HttpCookie cookie)
    {
        if (index != null) {
            List<HttpCookie> cookies = indexStore.get(index);
            if (cookies != null) {
                // there may already have the same cookie, so remove it first
                cookies.remove(cookie);

                cookies.add(cookie);
            } else {
                cookies = new ArrayList<HttpCookie>();
                cookies.add(cookie);
                indexStore.put(index, cookies);
            }
        }
    }


    //
    // for cookie purpose, the effective uri should only be http://host
    // the path will be taken into account when path-match algorithm applied
    //
    private URI getEffectiveURI(URI uri) {
        URI effectiveURI = null;
        try {
            effectiveURI = new URI("http",
                    uri.getHost(),
                    null,  // path component
                    null,  // query component
                    null   // fragment component
            );
        } catch (URISyntaxException ignored) {
            effectiveURI = uri;
        }

        return effectiveURI;
    }
}

