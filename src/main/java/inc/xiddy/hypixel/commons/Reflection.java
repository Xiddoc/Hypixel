package inc.xiddy.hypixel.commons;

import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class Reflection {

	public static <T> Set<T> getSubclassInstancesOf(Class<T> clazz, String packageName) throws ReflectiveOperationException {
		Set<T> instances = new HashSet<>();

		for (Class<? extends T> subClazz: getSubclassesOf(clazz, packageName)) {
			instances.add(subClazz.newInstance());
		}

		return instances;
	}

	public static <T> Set<Class<? extends T>> getSubclassesOf(Class<T> clazz, String packagePath) {
		return new Reflections(packagePath).getSubTypesOf(clazz);
	}

}
