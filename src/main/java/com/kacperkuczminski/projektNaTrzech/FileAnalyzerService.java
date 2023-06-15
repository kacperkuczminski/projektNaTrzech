package com.kacperkuczminski.projektNaTrzech;

import com.github.javaparser.ast.NodeList;
import com.kacperkuczminski.projektNaTrzech.FileAnalysisResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@Service
public class FileAnalyzerService {

    public FileAnalysisResultDTO analyzeFile(MultipartFile file) {
        FileAnalysisResultDTO result = new FileAnalysisResultDTO();

        try {
            ParseResult<CompilationUnit> parseResult = new JavaParser().parse(file.getInputStream());

            if (parseResult.isSuccessful()) {
                CompilationUnit compilationUnit = parseResult.getResult().get();
                CompilationUnitVisitor visitor = new CompilationUnitVisitor();
                visitor.visit(compilationUnit, result);
            } else {
                // handle parsing error
            }
        } catch (Exception e) {
            e.printStackTrace();
            // handle exception
        }

        return result;
    }

    private static class CompilationUnitVisitor extends VoidVisitorAdapter<FileAnalysisResultDTO> {

        @Override
        public void visit(ClassOrInterfaceDeclaration n, FileAnalysisResultDTO result) {
            super.visit(n, result);

            int variableCount = 0;
            int functionCount = 0;
            int objectCount = 0;

            NodeList<BodyDeclaration<?>> members = n.getMembers();
            for (BodyDeclaration<?> member : members) {
                if (member instanceof FieldDeclaration) {
                    variableCount++;
                } else if (member instanceof MethodDeclaration) {
                    functionCount++;
                    objectCount += countObjectCreations((MethodDeclaration) member);
                }
            }

            result.setVariableCount(variableCount);
            result.setFunctionCount(functionCount);
            result.setObjectCount(objectCount);
        }

        private int countObjectCreations(MethodDeclaration method) {
            ObjectCreationVisitor visitor = new ObjectCreationVisitor();
            visitor.visit(method, null);
            return visitor.getObjectCount();
        }
    }

    private static class ObjectCreationVisitor extends VoidVisitorAdapter<Void> {

        private int objectCount = 0;

        @Override
        public void visit(ObjectCreationExpr n, Void arg) {
            super.visit(n, arg);
            objectCount++;
        }

        public int getObjectCount() {
            return objectCount;
        }
    }
}
