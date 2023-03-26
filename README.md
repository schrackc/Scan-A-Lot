# Scan-A-Lot


## Description

Scan-A-Lot is a development by Saint Vincent College's CIS department Senior Project team "Scan-A-Lot", led by Curtis Schrack. The team has already begun developing an application which will help parking authorities with lot enforcement by accelerating the ticketing process. 

The project takes advantage of modern smartphone cameras to scan a license plate and automatically create the appropriate tickets based on information gathered through the scan, as well as through a connected database of parking pass holders. Finally, the app allows officers to print out a ticket with this information on it. This makes the process of creating citations much easier, as current workflows require manual entry of license plates and even manual writing of the infractions

To use the application, download the mobile app and web interface. Then connect the web interface to your own Firebase instance of allowed plate numbers. Connect the mobile app to a personal printer for printing tickets, and begin scanning plates on to see if they are on the whitelist. If one is not on the whitelist a ticketing process will begin.

## Getting Started

### Dependencies

* Android Studio.
* Firebase instance.
* List of allowed vehicles in specific lots. 

### Installing
* [Android App](https://github.com/schrackc/Scan-A-Lot)
* [Web Interface](https://github.com/schrackc/SeniorProjectWebsite)
* [Our Web Interface](https://scan-a-lot-management.firebaseapp.com/)
* A Firebase instance is needed for your specific use. 

### Executing program

* Using the mobile app, open to the "Select Lot" page and select a parking lot to begin scanning in. 
* Open the "Scan" page and point the camera guides at a license plate.
* The app will determine if the plate belongs to a vehicle that is allowed in the selected lot or not.
* If it is allowed, move on to the next one.
* If it is not allowed a ticketing procedure will begin and then a ticket will be printed out.

## Help

TODO: Advice for common problems or issues.

## Authors

Contributors names and contact info

- Curtis Schrack
- Andrew Hoffer
- Jeremy Capella
- Henry Beattie
- [Nickolas Downey](mailto:nickolas.downey@stvincent.edu)

## Acknowledgements

- Dr. William Birmingham - Primary Advisor for project development. 
- Saint Vincent College Department of Computing Information Systems - Project Advisory
- Saint Vincent College Public Safety Office - Development research.
- City of Latrobe - Development Research.
- Saint Vincent College Department of Criminology - Ethical council.

## License

This project is licensed under the GPLv3.0 License - see the [LICENSE.md](https://github.com/schrackc/Scan-A-Lot/blob/main/LICENSE) file for details
