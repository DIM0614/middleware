package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.lookup.Lookup;
import com.sun.xml.internal.ws.assembler.jaxws.TerminalTubeFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Singleton used by clients to ease the intantiation of
 * client proxies.
 *
 * @author vitorgreati
 */
public class ProxyCreator {

    private final Lookup lookup;

    private static Wrapper<ProxyCreator> wrapper;

    public ProxyCreator() {
        lookup = null;//TODO Middleware.getService("lookup") ;
    }

    /**
     * Used to create a client proxy.
     *
     * @param objectName
     * @param proxyClass
     * @return
     * @throws RemoteError
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public ClientProxy create(String objectName, Class<? extends ClientProxy> proxyClass) throws RemoteError, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // get AOR
        AbsoluteObjectReference aor = lookup.find(objectName);
        // make proxy
        return proxyClass.getConstructor(AbsoluteObjectReference.class).newInstance(aor);
    }

    public static ProxyCreator getInstance() {
        Wrapper<ProxyCreator> w = wrapper;
        if (w == null) {
            synchronized (ProxyCreator.class) {
                w = wrapper;
                if (w == null) {
                    w = new Wrapper<ProxyCreator>(new ProxyCreator());
                    wrapper = w;
                }
            }
        }
        return w.getInstance();
    }

    /**
     * Wrapper class to allow final modifier.
     *
     * @param <T>
     */
    private static class Wrapper<T> {
        private final T instance;
        public Wrapper(T instance) { this.instance = instance; }
        public T getInstance() { return instance; }
    }
}
