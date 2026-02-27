# komi-vyltorjas
Коми кывъя выльторъяслӧн сервислы бэкэнд

## Running with Docker Compose

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) with Compose plugin (v2)

### Setup

Create a `.env` file in the project root with the required secrets:

```
TELEGRAM_TOKEN=your_telegram_bot_token
OPENAI_API_KEY=your_openai_api_key
```

### Build and run

```bash
docker compose up --build
```
or
```bash
TELEGRAM_TOKEN=xxx OPENAI_API_KEY=xxx docker compose up --build
```

This builds the application image and starts both the app and a PostgreSQL database.
The database data is persisted in `./data/postgres/` on your host machine.

### Stop

```bash
docker compose down
```

To also remove the database volume, delete the `./data/postgres/` directory manually.
