XBee Manager Sample Application
===============================

This application demonstrates the usage of the XBee Java Library in Android by
giving an example of all the available options using a local XBee module
attached to a development board.

Demo requirements
-----------------

To run this example you need:

* A compatible development board to host the application.
* A USB connection between the board and the host PC in order to transfer and
  launch the application.
* An XBee in API mode and preferably configured to join to a network.

Demo setup
----------

Make sure the hardware is set up correctly:

1. The XBee is attached to the board using the XBee socket or using
   the USB interface.
2. The development board is powered on.
3. The board is connected directly to the PC by the micro USB cable.

Demo run
--------

The example is already configured, so all you need to do is to build and
launch the project.

In the first page of the application you have to specify how the XBee is
attached to the development board:

* Using the USB interface.
* Using the Serial Port interface (XBee socket)
  * For ConnectCore 8X SBC Pro: `/dev/ttyMCA0`
  * For ConnectCore 8M Mini Development Kit: `/dev/ttymxc3`

Specify also the connection baudrate. By default, most XBee devices are
configured to run at 9600.

When you are ready click **Connect**. The application layout changes and three
new tabs are displayed:

* **XBee Device Info**: Displays information about the attached XBee.
  You can configure some parameters from this tab.
* **Remote XBee Devices**: Discovers XBee devices in the same network as the
  attached XBee. You can select a remote node to change some of its parameters
  or send data to it.
* **Received XBee Data**: Displays a table with received data from other XBee
  nodes in the network.

Compatible with
---------------

* ConnectCore 6 SBC
* ConnectCore 6 SBC v3
* ConnectCore 8X SBC Pro
* ConnectCore 8M Mini Development Kit

License
---------

Copyright (c) 2014-2021, Digi International Inc. <support@digi.com>

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.