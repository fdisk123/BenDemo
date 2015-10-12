package scan;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import create.old;

public class Proxy {

	public static <T> T X() throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {

		Class a = old.class;
		ClassPool classPool = ClassPool.getDefault();
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
		for (CtMethod m : ctMethods) {
			tempM = CtNewMethod.copy(m, newClass, null);
			if ("void".equals(tempM.getReturnType().getName()))
				tempM.setBody("{super." + tempM.getName() + "($$);}");
			else
				tempM.setBody("{ return super." + tempM.getName() + "($$);}");
			//				CtNewMethod.make(src, declaring, delegateObj, delegateMethod)
			//			if (m.getName().equals("x"))
			tempM.setBody("{$proceed($$)}", "this", "mba()");
			newClass.addMethod(tempM);
		}
		try {
			newClass.writeFile("C:/Users/hnbh/Desktop/1.class");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (T) newClass.toClass().newInstance();
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {
		old o = X();
		o.x();
		System.out.println(o.x2(10));
	}
}
