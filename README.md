XBee Manager Sample Application
===============================

This application demonstrates the usage of the XBee Java API in Android by
giving an example of all the available options using a local XBee device
attached to the SBC board.

Demo requirements
-----------------

To run this example you need:

* One compatible device to host the application.
* A USB connection between the device and the host PC in order to transfer and
  launch the application.
* An XBee device in API mode and preferably configured to join to a network.

Demo setup
----------

Make sure the hardware is set up correctly:

1. The device is powered on.
2. The XBee device is attached to the SBC board using the XBee socket or using
   the USB interface.
3. The device is connected directly to the PC by the micro USB cable.

Demo run
--------

The example is already configured, so all you need to do is to build and 
launch the project.
  
In the first page of the application you have to specify how the XBee device
is attached to the SBC board:
  
* Using the USB interface.
* Using the Serial Port interface (XBee socket). This port is usually set on
  `/dev/ttymxc4`.
	  
Specify also the connection baudrate. By default, most of XBee devices are
configured to run at 9600.
  
When you are ready click **Connect**. The application layout changes and three
new tabs are displayed:
  
* **XBee Device Info**: Displays information about the attached XBee device.
  You can configure some parameters from this tab.
* **Remote XBee Devices**: Discovers XBee devices in the same network as your
  XBee device. You can select a remote device, change some of its parameters,
  and send data to it.
* **Received XBee Data**: Displays a table with received data from other XBee
  devices in the network.

Compatible with
---------------

* ConnectCore 6 SBC
* ConnectCore 6 SBC v3

License
---------

Copyright (c) 2014-2016, Digi International Inc. <support@digi.com>

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