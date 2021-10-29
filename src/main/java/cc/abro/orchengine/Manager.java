package cc.abro.orchengine;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.Caching;

public class Manager {

    private static final DefaultPicoContainer picoContainerServices = new DefaultPicoContainer(new Caching());
    private static final DefaultPicoContainer picoContainerBeans = new DefaultPicoContainer(picoContainerServices);

    public static void addService(Class<?> serviceClass){
        picoContainerServices.addComponent(serviceClass);
    }

    public static void addService(Object service){
        picoContainerServices.addComponent(service);
    }

    public static <T> T getService(Class<T> serviceClass){
        return picoContainerServices.getComponent(serviceClass);
    }

    public static void addBean(Class<?> beanClass){
        picoContainerBeans.addComponent(beanClass);
    }

    public static <T> T createBean(Class<T> beanClass){
        return picoContainerBeans.getComponent(beanClass);
    }

    public static void start(){
        picoContainerServices.start();
    }

    protected static void stop(){
        picoContainerServices.stop();
    }
}
