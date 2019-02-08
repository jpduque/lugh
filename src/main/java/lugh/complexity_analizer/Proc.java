package lugh.complexity_analizer;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.StandardLocation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.NoSuchFileException;
import java.util.Set;

@SupportedAnnotationTypes("*")
class Proc extends AbstractProcessor {
    final String m;
    Proc(String m) {
        this.m = m;
    }

    int count;
    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || count++ > 0) {
            return false;
        }

        Filer filer = processingEnv.getFiler();
        Messager messager = processingEnv.getMessager();

        System.err.println("running Proc");
        try {
            int len = filer.getResource(StandardLocation.SOURCE_OUTPUT, "p", "C.java").getCharContent(false).length();
            messager.printMessage(Kind.NOTE, "C.java: found previous content of length " + len);
        } catch (FileNotFoundException | NoSuchFileException x) {
            messager.printMessage(Kind.NOTE, "C.java: not previously there");
        } catch (IOException x) {
            messager.printMessage(Kind.ERROR, "while reading: " + x);
        }

        try {
            String body = "package p; public class C { public static void " + m + "() {} }";
            Writer w = filer.createSourceFile("p.C").openWriter();
            w.write(body);
            w.close();
            messager.printMessage(Kind.NOTE, "C.java: wrote new content: " + body);
        } catch (IOException x) {
            messager.printMessage(Kind.ERROR, "while writing: " + x);
        }

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}