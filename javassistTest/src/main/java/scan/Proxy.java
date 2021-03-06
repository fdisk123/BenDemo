package scan;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import create.n;
import create.old;

public class Proxy {

	public static <T> T X() throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
		Class a = old.class;
		ClassPool classPool = ClassPool.getDefault();
		classPool.importPackage("create.n");
		classPool.importPackage("java.lang.reflect.Method");
		CtClass old = classPool.get(a.getName());
		CtClass newClass = classPool.makeClass(a.getName() + "$Impl");
		newClass.setSuperclass(old);//继承
		CtConstructor tempC;
		CtConstructor[] ctConstructors = old.getDeclaredConstructors();
		newClass.addConstructor(CtNewConstructor.defaultConstructor(newClass));

		for (CtConstructor c : ctConstructors) {
			try {
				tempC = CtNewConstructor.copy(c, newClass, null);
				tempC.setBody("{super($$);}");
				newClass.addConstructor(tempC);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}

		CtMethod[] ctMethods = old.getDeclaredMethods();
		CtMethod tempM;//复制方法名
		Map<String, Method> tempMethod = new HashMap<String, Method>();
		for (CtMethod m : ctMethods) {
			tempMethod.put(String.format("%s %s %s(%s);", Modifier.toString(m.getModifiers()), m.getReturnType().getName(), m.getName(), Util.getParameterTypes(m.getParameterTypes())), null);
			//System.err.println(String.format("%s %s %s(%s);", Modifier.toString(m.getModifiers()), m.getReturnType().getName(), m.getName(), Util.getParameterTypes(m.getParameterTypes())));
			tempM = CtNewMethod.copy(m, newClass, null);
			if ("void".equals(tempM.getReturnType().getName()))
				tempM.setBody("{super." + tempM.getName() + "($$);}");
			else
				tempM.setBody("{ return super." + tempM.getName() + "($$);}");
			//				CtNewMethod.make(src, declaring, delegateObj, delegateMethod)
			if (m.getName().equals("x")) {
				//tempM.setBody("{$proceed($$);}", "this", "mba");
				//tempM.setBody("{n nn = new n();" + "Method a = n.class.getDeclaredMethod(\"a\", new Class[] { Integer.TYPE });" + "a.invoke(nn, new Object[] { Integer.valueOf(1) });}");
				tempM.setBody("{Method a = n.class.getDeclaredMethod(\"axx\", new Class[] { Integer.TYPE });}");
			}
			newClass.addMethod(tempM);
		}
		CtField[] ctFields = old.getDeclaredFields();
		for (CtField f : ctFields)
			System.out.println(f.getFieldInfo().getConstantValue());

		try {
			newClass.writeFile("D:/Desktop");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Class clazz = newClass.toClass();
		System.out.println(clazz.getCanonicalName());
		DefaultCachePoolFactory.newInstance().addNFloop4Map(new ClazzInfo(clazz, tempMethod), DefaultPool.NORNAL_BEAN, clazz.getCanonicalName());
		return (T) clazz.newInstance();
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {
		X();
//		if (!false)
//			return;
//		//		old o = X();
		Object ox = DefaultCachePoolFactory.newInstance().get4Map(DefaultPool.NORNAL_BEAN, "create.old$Impl");
		old o = ((Class<old>) DefaultCachePoolFactory.newInstance().get4Map(DefaultPool.NORNAL_BEAN, "create.old$Impl")).newInstance();
		//		o.x();
		System.out.println(o.getFX());
		System.out.println(o.x2(10));
		try {
			n nn = new n();
			Method a = n.class.getDeclaredMethod("a", int.class);
			a.invoke(nn, 1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
	}
}
