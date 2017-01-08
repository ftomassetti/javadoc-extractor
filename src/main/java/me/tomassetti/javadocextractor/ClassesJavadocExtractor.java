package me.tomassetti.javadocextractor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;
import me.tomassetti.javadocextractor.support.DirExplorer;

import java.io.File;
import java.io.IOException;

/**
 * Iterate over the classes and print their Javadoc.
 */
public class ClassesJavadocExtractor {

    public static void main(String[] args) {
        File projectDir = new File("source_to_parse/");
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                new VoidVisitorAdapter<Object>() {
                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        if (n.getComment() != null && n.getComment() instanceof JavadocComment) {
                            String title = String.format("%s (%s)", n.getName(), path);
                            System.out.println(title);
                            System.out.println(Strings.repeat("=", title.length()));
                            System.out.println(n.getComment());
                        }
                    }
                }.visit(JavaParser.parse(file), null);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }

}
