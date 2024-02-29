# Ticketer

A Sample ticketing Backend API using Java and only Java.

### Requirements

- JDK 21
- Gradle build system (tested with `8.6`, but any older version should work too)

### Dependency

- Gson
- Jetbrains annotations
- Junit

### Usage:

- Run `Ticketer`
- Hit the endpoints using `curl`, [Postman](https://www.postman.com/)
  or [Thunder Client](https://www.thunderclient.com/)
- Try it out

### API Endpoints

`POST /api/purchase`

Request Body:

```json
{
  "firstname": "Firstname",
  "lastname": "Lastname",
  "email": "first.last@email.com",
  "from": "Kolkata",
  "to": "Bangalore",
  "seat_section": "A"
}
```

Response:

```json
{
  "ticket": {
    "id": 100,
    "passengerId": 1709220124850,
    "seat": {
      "section": "A",
      "number": 1
    },
    "from": "Kolkata",
    "to": "Bangalore",
    "price": 20.0
  },
  "passenger": {
    "id": 1709220124850,
    "firstname": "Firstname",
    "lastName": "Lastname",
    "email": "first.last@email.com"
  }
}
```

<br/>

`GET /api/ticket/receipt?ticketId=value`

Response:

```json
{
  "ticket": {
    "id": 100,
    "passengerId": 1709220124850,
    "seat": {
      "section": "A",
      "number": 1
    },
    "from": "Kolkata",
    "to": "Bangalore",
    "price": 20.0
  },
  "passenger": {
    "id": 1709220124850,
    "firstname": "Firstname",
    "lastName": "Lastname",
    "email": "first.last@email.com"
  }
}
```

<br/>

`PUT /api/ticket/modify?ticketId=value&seat_section=value`

Response:

```json
{
  "id": 100,
  "passengerId": 1709220124850,
  "seat": {
    "section": "B",
    "number": 1
  },
  "from": "Kolkata",
  "to": "Bangalore",
  "price": 20.0
}
```

<br/>

`DELETE /api/ticket/cancel?ticketId=value`

Response:`Success`

<br/>

`GET /api/passengers?seat_section=value`

Response:

```json
{
  "Passenger[id=1709220124850, firstname=Firstname, lastName=Lastname, email=first.last@email.com]": {
    "section": "B",
    "number": 1
  }
}
```
