package org.NauhWuun.zio.kernel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Java SingleTon Pattern<p>
 * @Initialization on demand holder IDiom<p>
 * @Double checking<p>
 * @Thread Safe<p>
 * @Vars Initialize (Lazy/Early initialization)<p>
 * @OnlyOne Single Object<p>
 * @Null exception For Object<p>
 * @Runtime exception<p>
 * @DeConstructure<p>
 * @Serialization<p>
 * @Many ClassLoaders<p>
 * @Cloning<p>
 * @Combination QueueObject<p>
 */
public class SingleTon<T> implements Serializable, Cloneable 
{
    private static final long serialVersionUID = -540841140899265232L;
    @SuppressWarnings("rawtypes")
    static HashMap<String, SingleTon> map_instance = new HashMap<String, SingleTon>();
    T newT;

    private SingleTon() {
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T GetInstance(Class cls) throws RuntimeException {
        T _internal_instance = null;
        SingleTon hash_instance = map_instance.get(cls.getName());

        if (hash_instance == null) {
            try {
                synchronized (SingleTon.class) {
                    if (hash_instance == null) {
                        _internal_instance = (T) cls.getDeclaredConstructor().newInstance();
                        hash_instance = new SingleTon();
                        hash_instance.newT = _internal_instance;
                        map_instance.put(cls.getName(), hash_instance);
                    }
                }
            } catch (Exception ignored) {
            }
        }

        return (T) Objects.requireNonNull(hash_instance).newT;
    }

    @SuppressWarnings("rawtypes")
    public static final HashMap<String, SingleTon> DeConstrcorInterface() {
        return map_instance;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}