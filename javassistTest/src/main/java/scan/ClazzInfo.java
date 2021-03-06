package scan;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import create.old;

/***
 * 类信息
 * @author hnbh
 *
 */
public class ClazzInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class clazz;
	private Map<Class, Annotation> classAnnotation;
	private Map<String, Method> Methods;

	public ClazzInfo(Class c) {
		this.clazz = c;
	}

	public ClazzInfo(Class clazz, Map<String, Method> methods) {
		super();
		this.clazz = clazz;
		Methods = methods;
	}

	protected static final Map<String, Method> scanMethod(Class o) {

		Map<String, Method> objAnnotation = new HashMap<String, Method>();
		Method[] M = o.getDeclaredMethods();
		for (Method m : M) {
			String ax = String.format("%s %s %s(%s);", Modifier.toString(m.getModifiers()), m.getReturnType().getName(), m.getName(), Util.getParameterTypes(m.getParameterTypes()));
			System.out.println(ax);
		}
		return objAnnotation;
	}

	public static void main(String[] args) {
		//		new ClazzInfo(old.class).scanAnnotation(old.class);
		scanMethod(old.class);
	}
}
