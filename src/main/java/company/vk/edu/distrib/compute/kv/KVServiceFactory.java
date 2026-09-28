package company.vk.edu.distrib.compute.kv;

import company.vk.edu.distrib.compute.AbstractHttpServiceFactory;

/**
 * Constructs {@link KVService} instances.
 *
 */
public abstract class KVServiceFactory extends AbstractHttpServiceFactory<KVService> {
    /**
     * Constructor for subclasses.
     */
    protected KVServiceFactory() {
        super();
    }
}
