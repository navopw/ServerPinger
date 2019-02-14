# ServerPinger
Receive push notifications on your phone when your server goes online/offline! :mailbox_closed:

**I definetly recommend running ServerPinger on a Raspberry Pi or something compareable, so you don't miss out any push notifications.**

### Screenshots

![](https://i.imgur.com/rmb2AfB.jpg)


### Installation

- Download and run ServerPinger once so the configuration file gets generated
- Create a https://pushover.net/ Account (7 days trial and one time payment 4.99$)
- Create an application on Pushover.net
- Paste **user key** and **application token** from Pushover.net to your config
- Enter the ip addresses you want to ping in the config


### Run ServerPinger

I recommend using ServerPinger with `screen` so it runs in background.

For example: `screen -S serverpinger java -jar serverpinger-1.0.0.jar`

Press Ctrl + A + D to get out of the console screen

### Download

[Click here](https://github.com/navopw/ServerPinger/releases)

### Configuration
serverpinger.properties

| Property | Description | Example |
| --- | --- | --- |
| servers | Comma seperated ip addresses | 1.1.1.1,8.8.8.8 |
| pushover_user | User key you can find in the Pushover dashboard | nK1ykIcIHV1r5Hf0AAg0xcasFmsXVLPqiCVa3Izz |
| pushover_token | Token you can find when you created an application on the Pushover website | xWcQRIn1d8TplyV06P32sdTsi0OhY3DS7E0InX94 |
| timeout | The time before the ServerPinger aborts the ping try and declares server as not reachable | 3000 (milliseconds) |
| period | The period in which the ServerPinger should ping the ip addresses | 5000 (milliseconds) |
| notifytime | The time a server has to be offline or online again to trigger a push notification | 10000 (milliseconds) |
### Any suggestions?

[Create an issue](https://github.com/navopw/ServerPinger/issues/new)

### License
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)

MIT License
