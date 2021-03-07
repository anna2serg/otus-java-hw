package ru.otus.aop;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Objects;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        if (Objects.isNull(agentArgs)) {
            return;
        }
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                String targetClassName = agentArgs;
                byte[] byteCode = classfileBuffer;
                String finalTargetClassName = targetClassName.replaceAll("\\.", "/");
                if (className.equals(finalTargetClassName)) {
                    try {
                        ClassPool cp = ClassPool.getDefault();
                        cp.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
                        CtClass cc = cp.getCtClass(targetClassName);
                        CtMethod[] methods = cc.getDeclaredMethods();
                        for (CtMethod method : methods) {
                            Object logAnnotation = method.getAnnotation(Log.class);
                            if (Objects.nonNull(logAnnotation)) {
                                StringBuilder beforeBlock = new StringBuilder();
                                beforeBlock.append("System.out.print(\"executed method: " + method.getName() + ", param: \");");
                                beforeBlock.append("if ($args.length>0) {");
                                beforeBlock.append("    for (int i=0; i<$args.length; i++ ) {");
                                beforeBlock.append("        if (i>0) System.out.print(\", \");");
                                beforeBlock.append("        System.out.print($args[i]);");
                                beforeBlock.append("    }");
                                beforeBlock.append("} else {");
                                beforeBlock.append("    System.out.print(\"empty\");");
                                beforeBlock.append("}");
                                beforeBlock.append("System.out.println();");
                                method.insertBefore(beforeBlock.toString());
                                method.insertAfter("System.out.println();");
                            }
                        }
                        byteCode = cc.toBytecode();
                        cc.detach();
                        return byteCode;
                    } catch (CannotCompileException | NotFoundException | IOException | ClassNotFoundException e) {
                        System.out.println("Exception:" + e.toString());
                    }
                }
                return byteCode;
            }
        });
    }
}