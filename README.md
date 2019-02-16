# ServerPinger :mailbox_closed:
Receive push notifications on your phone when your server goes online/offline! 

**I recommend running ServerPinger on a device that runs 24/7 and without problems, so you don't miss out any push notifications.**

### Screenshots

- The first image shows the push notification that comes when you start ServerPinger and everything works
- The second image shows a push notification when your servers comes back online
- The third image shows a push notification when your server goes down/offline

![](https://i.imgur.com/9pwSK5f.png)

### Download

[Click here](https://github.com/navopw/ServerPinger/releases) (requires Java 8 or later)

### Installation

- Download and run ServerPinger once so the configuration file gets generated
- Create a [Pushover](https://pushover.net) Account and download the app in your app store and login (7 days trial and then 4.99$ one time payment)
- Create an application on Pushover.net
- Paste **user key** and **application token** from Pushover.net to your config
- Write the ip addresses or domain names you want to monitor into the config (See configuration section)

I recommend using ServerPinger with `screen` so it runs in background.

For example: `screen -S serverpinger java -jar serverpinger-1.0.1.jar`

Press Ctrl + A + D to get out of the console screen

### Update

When you update ServerPinger please backup your configuration file and remove it from the folder where the .jar is located, so ServerPinger can generate a new one. Then merge your old configuration with the new generated one.

### Configuration

serverpinger.properties

| Property | Description | Example |
| --- | --- | --- |
| servers | Comma seperated ip addresses or domain names | 1.1.1.1,8.8.8.8,google.de |
| pushover_user | User key you can find in the Pushover dashboard | nK1ykIcIHV1r5Hf0AAg0xcasFmsXVLPqiCVa3Izz |
| pushover_token | Token you can find when you created an application on the Pushover website | xWcQRIn1d8TplyV06P32sdTsi0OhY3DS7E0InX94 |
| initial_notification | Tells whether an overview of the Online/Offline servers should be shown on start/first check | true |
| timeout | The time before the ServerPinger aborts the ping try and declares server as not reachable | 3000 (milliseconds) |
| period | The period in which the ServerPinger should ping the ip addresses | 5000 (milliseconds) |
| notifytime | The time a server has to be offline or online again to trigger a push notification | 20000 (milliseconds) |


### Any suggestions?

[Create an issue](https://github.com/navopw/ServerPinger/issues/new)

### License (MIT)

Copyright © 2019 [navopw](https://github.com/navopw)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
