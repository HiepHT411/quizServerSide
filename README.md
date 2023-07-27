# GoQuiz

GoQuiz is a side project developed to learn Flutter/Dart for the frontend and Java Spring for the backend. It is a full-stack application that demonstrates the implementation of JWT (JSON Web Tokens) for user authentication.

## Features

![Application Features](https://www.github.com/gio-del/GoQuiz/blob/main/.github/assets/app.png)

- User Registration: New users can sign up and create an account.
- User Login: Existing users can log in to access the application's features.
- JWT Authentication: Secure authentication using JSON Web Tokens.
- Quiz creation and retrieval: Users can create and retrieve quizzes.
- Quiz taking: Users can take quizzes and receive a score.

## Technologies Used

- Flutter / Dart: Frontend development framework for building reactive and attractive user interfaces.
- Java Spring: Backend development framework for creating scalable and maintainable Java applications.
- JWT (JSON Web Tokens): A secure method for authenticating users and transmitting information between parties.
- Docker: hosting the database, PostgreSQL.

## Installation

Clone the repository from GitHub.

```bash
git clone https://github.com/gio-del/GoQuiz.git
```

### Frontend Setup

1) Make sure you have Flutter installed. If not, follow the Flutter installation guide: [Flutter Install Guide](https://flutter.dev/docs/get-started/install)

2) Install Flutter dependencies.

    ```bash
    flutter pub get
    ```

### Backend Setup

1) Make sure you have Java JDK and Maven installed. If not, follow the Java and Maven installation guides: [Java Install Guide](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) and [Maven Install Guide](https://maven.apache.org/download.cgi)

2) Import the Java Spring project into your preferred IDE (e.g., IntelliJ, Eclipse).

3) Resolve the Maven dependencies and start the backend server.

4) Modify accordingly the entry in `goquiz_ui/lib/constants api_endpoints.dart` to point to your backend server.

### Database Setup

1) Create a postgreSQL database to store user information.

2) Update the backend application properties with your database credentials.

### Run the Application

1) Start the backend server.

2) Launch the Flutter app on an emulator or a physical device.

## API Endpoints

WIP

## License

[MIT](https://choosealicense.com/licenses/mit/)
