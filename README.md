# Azure CLI MCP

This is an [MCP Server](https://modelcontextprotocol.io) that wraps the [Azure CLI](https://learn.microsoft.com/en-us/cli/azure/), adds a nice prompt to improve how it works, and exposes it.

## Demo

[![Demo](https://img.youtube.com/vi/y_OexCcfhW0/0.jpg)](https://www.youtube.com/watch?v=y_OexCcfhW0)

## What can it do?

It has access to the full Azure CLI, so it can do anything the Azure CLI can do. Here are a few scenarios:

- Listing your resources and checking their configuration. For example, you can get the rate limits of a model deployed
  to Azure OpenAI.
- Fixing some configuration or security issues. For example, you can ask it to secure a Blob Storage account.
- Creating resources. For example, you can ask it to create an Azure Container Apps instance, an Azure Container Registry, and connect them using managed identity.

## Is it safe to use?

As the MCP server is driven by an LLM, we would recommend to be careful and validate the commands it generates. Then, if you're using a good LLM like GPT-4o, which has 
excellent training data on Azure, our experience has been very good.

## How do I install it?

This MCP server currently only works with the `stdio` transport, so it should run locally on your machine, using your Azure CLI credentials.
This isn't a technical limitation, and we should have the `http` transport and OAuth authentication in the future.

### Prerequisites

- Install the Azure CLI: you can do this by following the instructions [here](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli).
- Authenticate to your Azure account. You can do this by running `az login` in your terminal.

### Installing the MCP server

_The easiest way to install `azure-cli-mcp` is by using Docker._

You can run it with the following command:

```bash
docker run -i docker.pkg.github.com/jdubois/azure-cli-mcp/azure-cli-mcp:latest
```

_If you don't want to use Docker, you can run `azure-cli-mcp` as a Java archive (you'll need Java installed), or as a
native binary for your platform (Linux, Mac OS, Windows)._

To run the _Java_ archive, you need to have a Java Virtual Machine (version 17 or higher) installed.

- Download the latest release: `gh release download --repo jdubois/azure-cli-mcp --pattern='azure-cli-mcp-*.jar'`
- Run the binary: `java -jar azure-cli-mcp-*.jar`

To run the binary on _Linux_, you need to:

- Download the latest release: `gh release download --repo jdubois/azure-cli-mcp --pattern='azure-cli-mcp-linux'`
- Make the binary executable: `chmod +x azure-cli-mcp-linux`
- Run the binary: `./azure-cli-mcp`

To run the binary on a _Mac OS_, you need to:

- Download the latest release: `gh release download --repo jdubois/azure-cli-mcp --pattern='azure-cli-mcp-macos'`
- If on Apple Silicon, install Rosetta if it's not already installed: `/usr/sbin/softwareupdate --install-rosetta --agree-to-license`
- Make the binary executable: `chmod +x azure-cli-mcp-macos`
- Allow Mac OS X to execute it: `xattr -d com.apple.quarantine azure-cli-mcp-macos`
- Run the binary: `./azure-cli-mcp-macos`

To run the binary on _Windows_, you need to:

- Download the latest release: `gh release download --repo jdubois/azure-cli-mcp --pattern='azure-cli-mcp-windows.exe'`
- Run the binary: `azure-cli-mcp-windows`

### Configuring the MCP server with Claude Desktop

Claude Desktop makes it easy to configure and chat with the MCP server. If you want a more advanced usage, we recommend using VS Code (see next section).

You need to add the server to your `claude_dekstop_config.json` file.

For _Java_:
```json
{
    "mcpServers": {
        "azure-cli": {
            "command": "java",
            "args": [
                "-jar",
                "/Users/julien/workspace/azure-cli-mcp/target/azure-cli-mcp-0.0.1-SNAPSHOT.jar"
            ]
        }
    }
}
```

For _Linux_, _Mac OS_, and _Windows_:
```json
{
    "mcpServers": {
        "azure-cli": {
            "command": "<path to the binary>",
            "args": []
        }
    }
}
```

### Configuring the MCP server with VS Code

_At the moment, this is only available with VS Code Insiders._

This configuration allows you to use GPT-4o, which has excellent training data on Azure.

Also, if you are developing a project and want to deploy it to Azure, VS Code will now have your project's context as well as this MCP Server, which will make it really good at deploying your project.

- Install GitHub Copilot
- Install this MCP Server using the command palette: `MCP: Add Server...`
- Configure GitHub Copilot to run in `Agent` mode, by clicking on the arrow at the bottom of the the chat window
- On top of the chat window, you should see the `azure-cli-mcp` server configured as a tool
