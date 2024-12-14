# WhitelistPlus

**WhitelistPlus** is a lightweight and enhanced whitelist management plugin for PaperMC servers (version **1.21.4**). It extends Minecraft's built-in whitelist functionality by providing advanced features such as asynchronous location detection, customizable messages, and streamlined command handling. Administrators can efficiently manage player access, receive real-time notifications for join attempts, and make informed decisions with ease.

## 📦 Features

- **Enhanced Whitelist Management:** Easily add or remove multiple players from the whitelist with a single command.
- **Asynchronous Location Detection:** Utilizes an external API to determine the country of a player's IP address upon join attempts.
- **Customizable Messages:** Set personalized messages for players awaiting approval or those denied access.
- **Real-Time Notifications:** Receive notifications when non-whitelisted players attempt to join, complete with actionable Accept and Deny buttons.
- **Flexible Notification Settings:** Choose to notify only server operators or all whitelisted players based on your preference.
- **Lightweight & Efficient:** Designed to have minimal impact on server performance while providing robust functionality.

## 🔧 Installation

### 1. **Download the Plugin**

Ensure you have the latest version of **WhitelistPlus** compatible with PaperMC.

### 2. **Place the JAR File**

- **Locate Your Server's Plugins Folder:**
  - Navigate to your PaperMC server directory.
  - Open the `plugins` folder.

- **Add the Plugin:**
  - Place the `WhitelistPlus.jar` file into the `plugins` directory.

### 3. **Start the Server**

- Launch or restart your PaperMC server.
- The plugin will generate a default `config.yml` in the `plugins/WhitelistPlus` directory upon first run.

## 🛠 Configuration

Customize **WhitelistPlus** by editing the `config.yml` file located in the `plugins/WhitelistPlus` directory.

```yaml
whitelist-status: true
await-message: "&eYou are not yet whitelisted. A request has been sent."
deny-message: "&cYou have been denied from the whitelist. Try again later."
notify-all-whitelisted: false
location-api:
  url: "http://ip-api.com/json/%s"
  timeout: 5000
```
## ⚙️ Configuration Options
 - whitelist-status (boolean):
    Enable or disable the whitelist.

  - await-message (string):
    Message sent to players attempting to join and awaiting approval.
    Default: &eYou are not yet whitelisted. A request has been sent.

  - deny-message (string):
    Message sent to players who have been denied access to the whitelist.
    Default: &cYou have been denied from the whitelist. Try again later.

  - notify-all-whitelisted (boolean):
    If set to true, all whitelisted players will receive join attempt notifications.
    If false, only players with the whitelistplus.receive permission will receive notifications.

  - location-api (object):

    -  url (string):
      -  The API endpoint used for location detection.
      -  Default: http://ip-api.com/json/%s

    -  timeout (integer):
      -  Timeout for API requests in milliseconds.
      -  Default: 5000

## 💡 Note:
**Location Detection*:
The plugin uses the ip-api.com service to determine the country of a player's IP address. Ensure your server has internet access to communicate with the API. You can replace the API URL in the configuration if you prefer a different service or require an API key.
## 🎮 Usage
Interact with WhitelistPlus using the /wlp command, which has been updated from the original /wl command.

## 📜 Available Commands
| **Command**             | **Description**                                           | **Usage Examples**                                |
|-------------------------|-----------------------------------------------------------|---------------------------------------------------|
| /wlp help               | Lists all available commands                              | /wlp help                                         |
| /wlp add <usernames>    | Adds one or multiple players to the whitelist             | /wlp remove Susan /wlp add Frank, John, Melissa   |
| /wlp remove <usernames> | Removes one or multiple players from  the whitelist       | /wlp remove Susan /wlp remove Frank, John         |
| /wlp awaitmsg <message> | Sets the message for players awaiting  whitelist approval | /wlp awaitmsg "&ePlease wait for admin approval." |
| /wlp denymsg <message>  | Sets the message for players denied  from the whitelist   | /wlp denymsg "&cAccess denied. Contact admin."    |
| /wlp on                 | nables the whitelist                                      |                                                   |
| /wlp off                | Disables the whitelist                                    |                                                   |
| /wlp status             | Displays the current status of the whitelist              |                                                   |
| /wlp accept <username>  | Approves a player's whitelist request                     | /wlp accept Frank                                 |
| /wlp deny <username>    | Denies a player's whitelist request                       | /wlp deny Frank                                   |

## 🔑 Permissions
| **Permission**        | **Description**                                       | **Default** |
|-----------------------|-------------------------------------------------------|-------------|
| whitelistplus.*       | Allows all WhitelistPlus commands and functionalities | op          |
| whitelistplus.receive | Allows receiving join attempt notifications           | op          |

### 🔍 How It Works

1. **Join Attempt by a Non-Whitelisted Player:**
   - The player is denied access and receives the `await-message`.
   - Depending on the configuration, designated recipients (operators or whitelisted players with `whitelistplus.receive`) receive a notification with the player's country and actionable Accept/Deny buttons.

2. **Admin Actions:**
   - **Accepting a Player:**
     - Adds the player to the whitelist.
     - The player can now join the server.
   
   - **Denying a Player:**
     - Sends the `deny-message` to the player if they attempt to join.
     - The player remains off the whitelist.

## 📂 Project Structure
```css
WhitelistPlus/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── botmodeengage/
        │           └── whitelistplus/
        │               ├── WhitelistPlus.java
        │               ├── commands/
        │               │   └── WhitelistCommand.java
        │               └── events/
        │                   └── PlayerJoinListener.java
        └── resources/
            ├── plugin.yml
            └── config.yml
```

## 📤 Deployment

1. **Locate the Generated JAR:**
   - After building, find the `WhitelistPlus-1.0.0.jar` file in the `target` directory of your project.

2. **Copy to Plugins Folder:**
   - Navigate to your PaperMC server's root directory.
   - Open the `plugins` folder within the server directory.
   - Copy the `WhitelistPlus-1.0.0.jar` file into the `plugins` folder.

3. **Start the Server:**
   - Launch or restart your PaperMC server.
   - Upon startup, the plugin will initialize and generate a default `config.yml` in the `plugins/WhitelistPlus` directory if it doesn't already exist.

4. **Verify Plugin Loading:**
   - Check the server console to ensure that `WhitelistPlus` has loaded without errors.
   - You should see a message indicating that the plugin has been enabled.

5. **Configure the Plugin:**
   - Open the generated `config.yml` located in `plugins/WhitelistPlus`.
   - Adjust settings as needed:
     - **Whitelist Status:** Enable or disable the whitelist.
     - **Custom Messages:** Customize `await-message` and `deny-message`.
     - **Notification Settings:** Set `notify-all-whitelisted` to `true` or `false` based on who should receive join attempt notifications.
     - **Location API:** Modify the `url` or `timeout` if you wish to use a different API service.

6. **Set Permissions:**
   - Ensure that server operators have the `whitelistplus.*` permission to access all plugin commands and functionalities.
   - Assign the `whitelistplus.receive` permission to players who should receive join attempt notifications if `notify-all-whitelisted` is set to `false`.

### 📜 Uninstallation

If you need to remove **WhitelistPlus** from your server:

1. **Stop the Server:**

2. **Remove the Plugin JAR:**
   - Navigate to the `plugins` folder.
   - Delete the `WhitelistPlus-1.0.3.jar` file.

3. **Delete Configuration Files (Optional):**
   - If you wish to remove all plugin data, delete the `WhitelistPlus` folder within the `plugins` directory, which contains `config.yml` and other plugin-related files.

4. **Start the Server:**
   - Launch your server. **WhitelistPlus** will no longer be active.

---
