# 🍲 Recipe Master
![Contributors](https://img.shields.io/github/contributors/Andowna9/Recipe-Master?style=for-the-badge)
![Maven Workflow Status](https://img.shields.io/github/actions/workflow/status/Andowna9/Recipe-Master/maven.yml?branch=main&style=for-the-badge)
![Contributors](https://img.shields.io/github/issues/Andowna9/Recipe-Master?style=for-the-badge)

## About The Project
**Recipe Master** is a social networking platform for sharing and discovering recipes worldwide.  
Developed by León Abascal, Jon Andoni Castillo, Hazal Demirci, Bidatz Beroiz, and Iñigo de Dios  
as part of the *Software Process and Quality* course, the project applies Agile practices  
using the Scrum framework.

The team has been structured into roles:
- The **Scrum Master** manages tasks and workflow via YouTrack.
- The **Product Owner** ensures the delivery of valuable features.
- **Developers** implement functionalities and track progress.

Recipe Master combines practical software development with a passion for food,  
creating a space for global culinary connection.
## Developed With
![Java](https://img.shields.io/badge/Java-orange?logo=OpenJDK)
![Maven](https://img.shields.io/badge/Build%20Tool-Maven-yellow?logo=apachemaven&logoColor=white)
![JavaFX](https://img.shields.io/badge/UI-JavaFX-blue?logo=java)
![Grizzly](https://img.shields.io/badge/Backend-Jersey%20Grizzly-white)
![MongoDB](https://img.shields.io/badge/Database-MongoDB-47A248?logo=mongodb&logoColor=white)
![DataNucleus](https://img.shields.io/badge/ORM-DataNucleus-pink)

## Language Support
The application is available in the following languages:

- 🇬🇧 English
- 🇪🇸 Spanish
- 🇹🇷 Turkish
- 🏴 Basque

> The language is automatically loaded based on the system's locale. If the locale does not match any language, it defaults to English.


## Features

- 🆕 Register user accounts.

![Register](img/register.PNG)

- 🔐 Login with existing accounts and remember email if needed.

![Login](img/login.PNG)

- 🔍 Search for recipes/users for some social interaction.

<img src="img/search.PNG" width="580px" alt="Search" />


- ✍️ View and create recipes written using markdown.

![View Recipe](img/view_recipe.PNG)

- 📚 Manage your own list of recipe posts and favourites.

![Personal List](img/personal_list.PNG)

- 🧑‍🍳 Create a rich user profile.

![Profile](img/profile.PNG)

- 🏠 Access home to check most popular recipes and recent additions.

![Home](img/home.PNG)



## Getting Started
This guide will help you set up and run the project.

### Prerequisites
Before you begin, ensure you have the following installed:
 - Java 11
 - Apache Maven
 - Docker (MongoDB)

### Running Locally (Dev)

Run a MongoDB database instance in a Docker container:
```shell
docker run --name recipe-master-mongo -p 27017:27017 -d mongo:4.4
```
Run the following command from the root folder:
```shell
mvn clean && mvn clean install
```

Go to the back-end folder and run the commands below. Keep the back-end running in the background:
```shell
cd back-end
mvn compile && mvn exec:java
```
Open a new terminal, go to the front-end folder, and run:

```shell
cd front-end
mvn javafx:run
```

## Docs
Check the Doxygen generated [documentation](https://andowna9.github.io/Recipe-Master/).

## Contributors
<a href="https://github.com/Andowna9/Recipe-Master/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Andowna9/Recipe-Master" alt="contrib.rocks image" />
</a>

## License
Distributed under the Apache License. See [License](LICENSE).

