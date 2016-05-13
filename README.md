# VMUC-C_Exporter

This tool offers the possibility to download an export from a VMU-C ([more infos](https://www.gavazzionline.com/em79.htm)) device automatically.

## What the tool does

This tool loads the export of the data of all connected devices during the entered date span and loads the "average" data.

## Usage

To use this tool you can either provide the needed information as Command-Line-Arguments or let the program ask it from you.

To run the program compile the sources and then call `java -jar <your jar>`. If you want to provide the information via CLI-Arguments the usage is `java -jar <ip of your device> <username> <password> <start date (dd.MM.yyyy)> <end date (dd.MM.yyyy)> <output folder>`.

