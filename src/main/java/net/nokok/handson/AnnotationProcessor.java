package net.nokok.handson;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(
            Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv
    ) {
        Types typeUtils = processingEnv.getTypeUtils();
        Messager messager = processingEnv.getMessager();
        PrimitiveType intType = typeUtils.getPrimitiveType(TypeKind.INT);
        List<? extends Element> collect = annotations.stream().flatMap(r -> roundEnv.getElementsAnnotatedWith(r).stream()).collect(Collectors.toList());
        List<VariableElement> variableElements = ElementFilter.fieldsIn(collect);
        for (VariableElement variableElement : variableElements) {
            TypeMirror typeMirror = variableElement.asType();
            if (!typeUtils.isSameType(typeMirror, intType)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "int型のみ許可されています", variableElement);
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(IntField.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
