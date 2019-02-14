# ServerPinger
Receive push notifications to your phone when your server goes online/offline! :mailbox_closed:

### Screenshots

![](https://i.imgur.com/rmb2AfB.jpg)


### Installation

- Run ServerPinger once so the configuration file gets generated
- Create a https://pushover.net/ Account
- Create an application on Pushover.net
- Paste **user key** and **application token** from Pushover.net to your config
- Enter the ip addresses you want to ping in the config


### Run ServerPinger

I recommend using ServerPinger with `screen` so it runs in background.

For example: `screen -S serverpinger java -jar ServerPinger-1.0.0.jar`

Press Ctrl + A + D to get out of the console screen

### Configuration
serverpinger.properties

| Property | Description | Example |
| --- | --- | --- |
| servers | Comma seperated ip addresses | 1.1.1.1,8.8.8.8 |
| pushover_user | User key you can find in the Pushover dashboard | nK1ykIcIHV1r5Hf0AAg0xcasFmsXVLPqiCVa3Izz |
| pushover_token | Token you can find when you created an application on the Pushover website | xWcQRIn1d8TplyV06P32sdTsi0OhY3DS7E0InX94 |
| timeout | The time before the ServerPinger aborts the ping try and declares server as not reachable | 3000 |
| period | The period in which the ServerPinger should ping the ip addresses in milliseconds | 5000 |

### License
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)

MIT License