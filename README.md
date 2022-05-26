# BSPQ22-E5
## The team and their objective
This project belongs to and has been developed by León Abascal, Jon Andoni Castillo, Hazal Demirci, Bidatz Beroiz and Iñigo de Dios.
Its purpose it's to develop the knowledge acquired during the subject "Software Process and Quality" in a practical manner.

## The software solution
The decision we have taken as a team is to develop and deploy a Social Network oriented to _RECIPE POSTING_.

## Documentation
https://spqe21-22.github.io/BSPQ22-E5/

## Repository
https://github.com/SPQE21-22/BSPQ22-E5

## Compilation instructions
1. Have Mongod services running (Linux: sudo systemclt start mongod.service; Windows: either start the service or have the exe file running in bg)
2. Have Maven installed (and added to the root)
3. Go to the root folder of the project (where the readme.md is)
4. Run *mvn clean && mvn clean install*
5. Go to the back-end folder. Run *mvn compile* && *mvn exec:java*. Have it running in the background.
6. Go to the front-end folder. Run *mvn javafx:run*.

## Software tools used
* Maven
* JavaFX + Gluon SceneBuilder 
* MongoDB + datanucleus
* Others (not listed)
