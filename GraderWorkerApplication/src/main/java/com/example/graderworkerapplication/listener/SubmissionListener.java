package com.example.graderworkerapplication.listener;

import com.example.graderworkerapplication.config.RabbitMQConfig;
import com.example.graderworkerapplication.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SubmissionListener {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionListener.class);

    private final RabbitTemplate rabbitTemplate;

    public SubmissionListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.SUBMISSION_QUEUE)
    public void receiveMessage(SubmissionSendMessage sendMessage) {
        try {
            logger.info("Received message for Submission ID {}", sendMessage.getSubmissionDto().getId());

            SubmissionDto submissionDto = sendMessage.getSubmissionDto();
            List<TestCaseDto> testCases = sendMessage.getTestCasesDtoList();

            List<SubmissionResultMessage.TestCaseResult> results = new ArrayList<>();
            int correctTestCases = 0;

            for (TestCaseDto testCase : testCases) {
                String[] processedInput = testCase.getInput().split(",");
                String output = runTestCase(submissionDto.getCode(), submissionDto.getLanguage(), processedInput);

                boolean passed = output.trim().equals(testCase.getOutput().trim());
                if (passed) correctTestCases++;

                results.add(new SubmissionResultMessage.TestCaseResult(
                        testCase.getId(), passed, output
                ));

                logger.info("Input: {}", String.join(",", processedInput));
                logger.info("Output: {} | Expected: {}", output, testCase.getOutput());
            }

            float score = (float) correctTestCases * 100 / testCases.size();
            SubmissionResultMessage resultMessage = new SubmissionResultMessage(
                    submissionDto.getId(),
                    correctTestCases == testCases.size() ? "ACCEPTED" : "FAILED",
                    score,
                    results
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.RESULT_QUEUE, resultMessage);
            logger.info("Sent result for Submission ID {} with score {}", submissionDto.getId(), score);

        } catch (Exception e) {
            logger.error("Error processing submission", e);
        }
    }

    private String runTestCase(String code, String language, String[] input) {
        try {
            return switch (language.toLowerCase()) {
                case "python" -> executePython(code, input);
                case "javascript" -> executeJS(code, input);
                case "java" -> executeJava(code, input);
                case "c" -> executeC(code, input);
                case "cpp" -> executeCpp(code, input);
                case "csharp", "cs" -> "C# execution not supported in this environment";
                default -> "Unsupported language";
            };
        } catch (Exception e) {
            logger.error("Exception during test execution", e);
            return "Exception: " + e.getMessage();
        }
    }

    // ------------------- Executors -------------------
    private String executePython(String code, String[] input) throws IOException, InterruptedException {
        // Create temp file with the submitted code
        Path tempFile = Files.createTempFile("solution", ".py");
        Files.writeString(tempFile, code);

        ProcessBuilder processBuilder = new ProcessBuilder("python3", tempFile.toString());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Join input array into a single string separated by spaces
        String joinedInput = String.join(" ", input);

        // Send test case input to Python program's stdin
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            writer.write(joinedInput);
            writer.newLine(); // ensure input line ends
            writer.flush();
        }

        // Capture output
        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            output = reader.lines().collect(Collectors.joining("\n"));
        }

        int exitCode = process.waitFor();
        Files.deleteIfExists(tempFile);

        if (exitCode != 0) {
            logger.warn("Python process exited with code {}", exitCode);
        }

        return output.trim();
    }

    private String executeJS(String code, String[] input) throws Exception {
        File tempFile = File.createTempFile("solution", ".js");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(code + "\n");
            // Convert input array to JS numbers safely
            List<String> jsArgs = new ArrayList<>();
            for (String in : input) {
                // Split on space or comma
                for (String val : in.split("[,\\s]+")) {
                    if (!val.isEmpty()) jsArgs.add("Number(" + val + ")");
                }
            }
            writer.write("console.log(main(" + String.join(",", jsArgs) + "));");
        }

        Process proc = new ProcessBuilder("node", tempFile.getAbsolutePath())
                .redirectErrorStream(true)
                .start();

        if (!proc.waitFor(5, TimeUnit.SECONDS)) proc.destroyForcibly();

        String output = new String(proc.getInputStream().readAllBytes());
        Files.deleteIfExists(tempFile.toPath());
        return output.trim();
    }

    private String executeJava(String code, String[] input) throws Exception {
        String className = "Solution";

        // Parse all input values into a single array
        List<String> allInputs = new ArrayList<>();
        for (String inputStr : input) {
            // Split by comma and/or whitespace, filter empty strings
            String[] parts = inputStr.split("[,\\s]+");
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    allInputs.add(part.trim());
                }
            }
        }

        // Convert to array
        String[] cleanedInput = allInputs.toArray(new String[0]);

        // Prepare argument names with types
        String[] argNamesWithType = new String[cleanedInput.length];
        for (int i = 0; i < cleanedInput.length; i++) {
            argNamesWithType[i] = "int arg" + i;
        }

        // Prepare mainFunc argument list
        String argsDecl = String.join(", ", argNamesWithType);

        // Prepare mainFunc call from main()
        StringBuilder argsParse = new StringBuilder();
        for (int i = 0; i < cleanedInput.length; i++) {
            if (i > 0) argsParse.append(", ");
            argsParse.append("Integer.parseInt(args[").append(i).append("].trim())");
        }

        String javaCode = "public class " + className + " {\n" +
                "    public static int mainFunc(" + argsDecl + ") {\n" +
                "        " + code + "\n" +
                "    }\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(mainFunc(" + argsParse + "));\n" +
                "    }\n" +
                "}";

        File tempDir = Files.createTempDirectory("java").toFile();
        File javaFile = new File(tempDir, className + ".java");
        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write(javaCode);
        }

        // Compile
        Process compile = new ProcessBuilder("javac", javaFile.getAbsolutePath())
                .redirectErrorStream(true)
                .start();

        if (!compile.waitFor(5, TimeUnit.SECONDS)) {
            compile.destroyForcibly();
            return "Compilation timeout";
        }

        if (compile.exitValue() != 0) {
            String error = new String(compile.getInputStream().readAllBytes());
            deleteDirectory(tempDir);
            return "Compile error: " + error;
        }

        // Run with the cleaned input as arguments
        List<String> runCommand = new ArrayList<>();
        runCommand.add("java");
        runCommand.add("-cp");
        runCommand.add(tempDir.getAbsolutePath());
        runCommand.add(className);
        runCommand.addAll(Arrays.asList(cleanedInput));

        Process run = new ProcessBuilder(runCommand)
                .redirectErrorStream(true)
                .start();

        if (!run.waitFor(5, TimeUnit.SECONDS)) {
            run.destroyForcibly();
            deleteDirectory(tempDir);
            return "Execution timeout";
        }

        String output = new String(run.getInputStream().readAllBytes());
        deleteDirectory(tempDir);
        return output.trim();
    }

    private String executeC(String code, String[] input) throws Exception {
        // Parse all input values into a single array
        List<String> allInputs = new ArrayList<>();
        for (String inputStr : input) {
            String[] parts = inputStr.split("[,\\s]+");
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    allInputs.add(part.trim());
                }
            }
        }
        String[] cleanedInput = allInputs.toArray(new String[0]);

        // Generate argument declarations and parsing
        StringBuilder argDeclarations = new StringBuilder();
        StringBuilder argParsing = new StringBuilder();

        for (int i = 0; i < cleanedInput.length; i++) {
            argDeclarations.append("    int arg").append(i).append(";\n");
            argParsing.append("    arg").append(i).append(" = atoi(argv[").append(i + 1).append("]);\n");
        }

        String cCode = "#include <stdio.h>\n" +
                "#include <stdlib.h>\n\n" +
                "int main(int argc, char *argv[]) {\n" +
                argDeclarations.toString() +
                argParsing.toString() +
                "    " + code + "\n" +
                "    return 0;\n" +
                "}";

        File tempDir = Files.createTempDirectory("c").toFile();
        File srcFile = new File(tempDir, "main.c");
        File exeFile = new File(tempDir, "main");

        try (FileWriter writer = new FileWriter(srcFile)) {
            writer.write(cCode);
        }

        // Compile
        Process compile = new ProcessBuilder("gcc", srcFile.getAbsolutePath(), "-o", exeFile.getAbsolutePath())
                .redirectErrorStream(true)
                .start();

        if (!compile.waitFor(5, TimeUnit.SECONDS)) {
            compile.destroyForcibly();
            deleteDirectory(tempDir);
            return "Compilation timeout";
        }

        if (compile.exitValue() != 0) {
            String error = new String(compile.getInputStream().readAllBytes());
            deleteDirectory(tempDir);
            return "Compile error: " + error;
        }

        // Run with input as arguments
        List<String> runCommand = new ArrayList<>();
        runCommand.add(exeFile.getAbsolutePath());
        runCommand.addAll(Arrays.asList(cleanedInput));

        Process run = new ProcessBuilder(runCommand)
                .redirectErrorStream(true)
                .start();

        if (!run.waitFor(5, TimeUnit.SECONDS)) {
            run.destroyForcibly();
            deleteDirectory(tempDir);
            return "Execution timeout";
        }

        String output = new String(run.getInputStream().readAllBytes());
        deleteDirectory(tempDir);
        return output.trim();
    }

    private String executeCpp(String code, String[] input) throws Exception {
        // Parse all input values into a single array
        List<String> allInputs = new ArrayList<>();
        for (String inputStr : input) {
            String[] parts = inputStr.split("[,\\s]+");
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    allInputs.add(part.trim());
                }
            }
        }
        String[] cleanedInput = allInputs.toArray(new String[0]);

        // Generate argument declarations and parsing
        StringBuilder argDeclarations = new StringBuilder();
        StringBuilder argParsing = new StringBuilder();

        for (int i = 0; i < cleanedInput.length; i++) {
            argDeclarations.append("    int arg").append(i).append(";\n");
            argParsing.append("    arg").append(i).append(" = std::atoi(argv[").append(i + 1).append("]);\n");
        }

        String cppCode = "#include <iostream>\n" +
                "#include <cstdlib>\n\n" +
                "int main(int argc, char *argv[]) {\n" +
                argDeclarations.toString() +
                argParsing.toString() +
                "    " + code + "\n" +
                "    return 0;\n" +
                "}";

        File tempDir = Files.createTempDirectory("cpp").toFile();
        File srcFile = new File(tempDir, "main.cpp");
        File exeFile = new File(tempDir, "main");

        try (FileWriter writer = new FileWriter(srcFile)) {
            writer.write(cppCode);
        }

        // Compile
        Process compile = new ProcessBuilder("g++", srcFile.getAbsolutePath(), "-o", exeFile.getAbsolutePath())
                .redirectErrorStream(true)
                .start();

        if (!compile.waitFor(5, TimeUnit.SECONDS)) {
            compile.destroyForcibly();
            deleteDirectory(tempDir);
            return "Compilation timeout";
        }

        if (compile.exitValue() != 0) {
            String error = new String(compile.getInputStream().readAllBytes());
            deleteDirectory(tempDir);
            return "Compile error: " + error;
        }

        // Run with input as arguments
        List<String> runCommand = new ArrayList<>();
        runCommand.add(exeFile.getAbsolutePath());
        runCommand.addAll(Arrays.asList(cleanedInput));

        Process run = new ProcessBuilder(runCommand)
                .redirectErrorStream(true)
                .start();

        if (!run.waitFor(5, TimeUnit.SECONDS)) {
            run.destroyForcibly();
            deleteDirectory(tempDir);
            return "Execution timeout";
        }

        String output = new String(run.getInputStream().readAllBytes());
        deleteDirectory(tempDir);
        return output.trim();
    }

    //FIXME: Maybe use .net
    private String executeCSharp(String code, String[] input) throws Exception {
        // First check if dotnet is available (more common than csc)
        if (!isCommandAvailable("dotnet") && !isCommandAvailable("csc") && !isCommandAvailable("mcs")) {
            return "C# compiler not available (dotnet, csc, or mcs required)";
        }

        // Parse all input values into a single array
        List<String> allInputs = new ArrayList<>();
        for (String inputStr : input) {
            String[] parts = inputStr.split("[,\\s]+");
            for (String part : parts) {
                if (!part.trim().isEmpty()) {
                    allInputs.add(part.trim());
                }
            }
        }
        String[] cleanedInput = allInputs.toArray(new String[0]);

        File tempDir = Files.createTempDirectory("csharp").toFile();
        File csFile = new File(tempDir, "Program.cs");

        // Generate argument parameters for MainFunc
        StringBuilder argParams = new StringBuilder();
        StringBuilder argParsing = new StringBuilder();

        for (int i = 0; i < cleanedInput.length; i++) {
            if (i > 0) {
                argParams.append(", ");
                argParsing.append(", ");
            }
            argParams.append("int arg").append(i);
            argParsing.append("int.Parse(args[").append(i).append("])");
        }

        try (FileWriter writer = new FileWriter(csFile)) {
            writer.write("using System;\n\n");
            writer.write("public class Program {\n");
            writer.write("    public static int MainFunc(" + argParams.toString() + ") {\n");
            writer.write("        " + code + "\n");
            writer.write("    }\n\n");
            writer.write("    public static void Main(string[] args) {\n");
            writer.write("        Console.WriteLine(MainFunc(" + argParsing.toString() + "));\n");
            writer.write("    }\n");
            writer.write("}");
        }

        String executablePath;
        Process compile;

        if (isCommandAvailable("dotnet")) {
            File csprojFile = new File(tempDir, "temp.csproj");
            try (FileWriter writer = new FileWriter(csprojFile)) {
                writer.write("<Project Sdk=\"Microsoft.NET.Sdk\">\n");
                writer.write("  <PropertyGroup>\n");
                writer.write("    <OutputType>Exe</OutputType>\n");
                writer.write("    <TargetFramework>net6.0</TargetFramework>\n");
                writer.write("  </PropertyGroup>\n");
                writer.write("</Project>");
            }

            compile = new ProcessBuilder("dotnet", "build", "-o", tempDir.getAbsolutePath())
                    .directory(tempDir)
                    .redirectErrorStream(true)
                    .start();
            executablePath = new File(tempDir, "temp").getAbsolutePath();

        } else if (isCommandAvailable("mcs")) {
            // Use Mono C# compiler (Linux/macOS alternative)
            compile = new ProcessBuilder("mcs", "-out:" + new File(tempDir, "Program.exe").getAbsolutePath(),
                    csFile.getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();
            executablePath = new File(tempDir, "Program.exe").getAbsolutePath();

        } else {
            // Use Microsoft csc (Windows)
            compile = new ProcessBuilder("csc", "/out:" + new File(tempDir, "Program.exe").getAbsolutePath(),
                    csFile.getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();
            executablePath = new File(tempDir, "Program.exe").getAbsolutePath();
        }

        if (!compile.waitFor(10, TimeUnit.SECONDS)) {
            compile.destroyForcibly();
            deleteDirectory(tempDir);
            return "Compilation timeout";
        }

        if (compile.exitValue() != 0) {
            String error = new String(compile.getInputStream().readAllBytes());
            deleteDirectory(tempDir);
            return "Compile error: " + error;
        }

        // Run the compiled program with input as arguments
        List<String> runCommand = new ArrayList<>();

        if (isCommandAvailable("dotnet") && executablePath.endsWith("temp")) {
            runCommand.add("dotnet");
            runCommand.add(executablePath + ".dll");
        } else if (executablePath.endsWith(".exe") && isCommandAvailable("mono") && !System.getProperty("os.name").toLowerCase().contains("win")) {
            // Use mono to run .exe on non-Windows systems
            runCommand.add("mono");
            runCommand.add(executablePath);
        } else {
            runCommand.add(executablePath);
        }

        runCommand.addAll(Arrays.asList(cleanedInput));

        Process run = new ProcessBuilder(runCommand)
                .redirectErrorStream(true)
                .start();

        if (!run.waitFor(5, TimeUnit.SECONDS)) {
            run.destroyForcibly();
            deleteDirectory(tempDir);
            return "Execution timeout";
        }

        String output = new String(run.getInputStream().readAllBytes());
        deleteDirectory(tempDir);
        return output.trim();
    }

    private boolean isCommandAvailable(String command) {
        try {
            Process process = new ProcessBuilder(command, "--version")
                    .redirectErrorStream(true)
                    .start();
            boolean finished = process.waitFor(2, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
            }
            return finished && process.exitValue() == 0;
        } catch (Exception e) {
            try {
                Process process = new ProcessBuilder(command, "/?" )
                        .redirectErrorStream(true)
                        .start();
                boolean finished = process.waitFor(2, TimeUnit.SECONDS);
                if (!finished) {
                    process.destroyForcibly();
                }
                return finished;
            } catch (Exception e2) {
                return false;
            }
        }
    }

    private void deleteDirectory(File dir) throws Exception {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) deleteDirectory(f);
        }
        dir.delete();
    }
}