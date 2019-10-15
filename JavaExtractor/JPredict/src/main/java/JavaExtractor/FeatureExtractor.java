package JavaExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.*;
import com.github.javaparser.ast.Node;
import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.Common.Common;
import JavaExtractor.Common.MethodContent;
import java.io.PrintWriter;
import java.io.FileWriter;

// import JavaExtractor.FeaturesEntities.ProgramFeatures;
// import JavaExtractor.FeaturesEntities.Property;
// import JavaExtractor.Visitors.FunctionVisitor;

@SuppressWarnings("StringEquality")
public class FeatureExtractor {
	private CommandLineValues m_CommandLineValues;
	private static Set<String> s_ParentTypeToAddChildId = Stream
			.of("AssignExpr", "ArrayAccessExpr", "FieldAccessExpr", "MethodCallExpr")
			.collect(Collectors.toCollection(HashSet::new));

	final static String lparen = "(";
	final static String rparen = ")";
	final static String upSymbol = "^";
	final static String downSymbol = "_";

	public FeatureExtractor(CommandLineValues commandLineValues) {
		this.m_CommandLineValues = commandLineValues;
	}

	public CompilationUnit extractFeatures(String code, String codename) throws ParseException, IOException {
		CompilationUnit compilationUnit = parseFileWithRetries(code);
		YamlPrinter printer = new YamlPrinter(true);
		String filename = "/tmp/yamls/" + codename + ".yaml";
		System.out.println(filename);
		FileWriter fileWriter = new FileWriter(filename);
		PrintWriter out = new PrintWriter(fileWriter);
		out.println(printer.output(compilationUnit));
		fileWriter.close();

		// DotPrinter printer = new DotPrinter(true);
		// String filename = "/tmp/dots2/" + codename + ".dot";
		// System.out.println(filename);
		// // PrintWriter out = new PrintWriter(filename);
		// // out.print(printer.output(compilationUnit));
		// FileWriter fileWriter = new FileWriter(filename);
		// PrintWriter printWriter = new PrintWriter(fileWriter);
		// printWriter.print(printer.output(compilationUnit));
		// fileWriter.close();

		// JsonPrinter printer = new JsonPrinter(true);
		// String filename = "/tmp/json/" + codename + ".json";
		// System.out.println(filename);
		// PrintWriter out = new PrintWriter(filename);
		// out.println(printer.output(compilationUnit));

		// System.out.println("HERE!");
		// System.exit(0);

		// FunctionVisitor functionVisitor = new FunctionVisitor();

		// functionVisitor.visit(compilationUnit, null);

		// ArrayList<MethodContent> methods = functionVisitor.getMethodContents();
		// ArrayList<ProgramFeatures> programs = generatePathFeatures(methods);

		return compilationUnit;
	}

	private CompilationUnit parseFileWithRetries(String code) throws IOException {
		// final String classPrefix = "public class Test {";
		// final String classSuffix = "}";
		// final String methodPrefix = "SomeUnknownReturnType f() {";
		// final String methodSuffix = "return noSuchReturnValue; }";

		// String originalContent = code;
		// String content = originalContent;
		// CompilationUnit parsed = null;
		// try {
		// 	parsed = JavaParser.parse(content);
		// } catch (ParseProblemException e1) {
		// 	// Wrap with a class and method
		// 	try {
		// 		content = classPrefix + methodPrefix + originalContent + methodSuffix + classSuffix;
		// 		parsed = JavaParser.parse(content);
		// 	} catch (ParseProblemException e2) {
		// 		// Wrap with a class only
		// 		content = classPrefix + originalContent + classSuffix;
		// 		parsed = JavaParser.parse(content);
		// 	}
		// }
		CompilationUnit parsed = StaticJavaParser.parse(code);

		return parsed;
	}
}
