package io.github.jdubois.azureclimcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class AzureCliService {

    private final Logger logger = LoggerFactory.getLogger(AzureCliService.class);

    private static final String commandPrompt = """
            Your job is to answer questions about an Azure environment by executing Azure CLI commands. You have the following rules:
            
            - You should the Azure CLI to manage Azure resources and services. Do not use any other tool.
            - You should provide a valid Azure CLI command starting with 'az'. For example: 'az vm list'.
            - Whenever a command fails, retry it 3 times before giving up with an improved version of the code based on the returned feedback.
            - When listing resources, ensure pagination is handled correctly so that all resources are returned.
            - This tool can ONLY write code that interacts with Azure. It CANNOT generate charts, tables, graphs, etc.
            
            Be concise, professional and to the point. Do not give generic advice, always reply with detailed & contextual data sourced from the current Azure environment. Assume user always wants to proceed, do not ask for confirmation. I'll tip you $200 if you do this right.`;
            
            """;

    @Tool(
            name = "execute-azure-cli-command",
            description = commandPrompt
    )
    public String executeAzureCli(@ToolParam(description = "Azure CLI command") String command) {
        logger.info("Executing Azure CLI command: {}", command);
        if (!command.startsWith("az ")) {
            logger.error("Invalid command: {}", command);
            return "Error: Invalid command. Command must start with 'az'.";
        }
        String output = runAzureCliCommand(command);
        logger.info("Azure CLI command output: {}", output);
        return output;
    }

    /**
     * Runs an Azure CLI command and returns the output.
     *
     * @param command The Azure CLI command to run.
     * @return The output of the command.
     */
    private String runAzureCliCommand(String command) {
        logger.info("Running Azure CLI command: {}", command);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("Azure CLI command failed with exit code: {}", exitCode);
                return "Error: " + output;
            }
            return output.toString();
        } catch (IOException | InterruptedException e) {
            logger.error("Error running Azure CLI command", e);
            return "Error: " + e.getMessage();
        }
    }

}
