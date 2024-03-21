# 1. Project Structure

### Dependency Graph
![project dot](https://github.com/kylixeza/beu-remaster-backend/assets/58837451/9c16d999-bfb6-423f-8842-1aca8ed2f8de)


# 2. Setup
### Google Cloud
- Login to your google cloud console (register if you don't have one)
- Create a new project
- Select Cloud Storage -> Bucket on the left side
    - <img width="300" alt="Screenshot 2024-03-21 at 11 49 21" src="https://github.com/kylixeza/beu-remaster-backend/assets/58837451/1d01aeca-a92b-4236-ac4d-41c28185d6f2">
- Create new Bucket
- Fill the form, you can fill according to your needs (for the access control you can use fine-grained an)
- You need to edit your bucket permission to public so anyone through any accounts can see it. To make it public go to this link: https://cloud.google.com/storage/docs/access-control/making-data-public#buckets
- Create credential key throught these steps:
  - Go to **IAM & Admin** and select **Service Accounts**
  - Select the service account that used in your project (create new one if you don't have)
  - Go to **KEYS** tab and **ADD KEY**
  - Create new key and save the key as json format
- In this project put that json in `core` -> `src` -> `main` -> `resources` (create it by right click on the `main` dir -> New -> Directory).
  - By default the file should be named beu.json (check `di` -> `DataModule.kt`)

### Environment Variables (Local)
| Name | Value |
| ---- | ----- |
| BUCKET_NAME | beu-dev (your bucket name that you created in cloud storage) | 
| DATABASE_URL | jdbc:postgresql:your_database_name?user\=your_user&password\=your_password |
| JDBC_DRIVER | org.postgresql.Driver |
| ENV | DEV |
| JWT_AUDIENCE | user |
| JWT_DOMAIN | http://0.0.0.0:8080 |
| JWT_ISSUER | http://0.0.0.0:8080 |
| JWT_REALM | beu |
| JWT_SECRET | JWT_SECRET | 
| PORT | 8080 |

# 3. Next Update
- Create help center that integrated with email
- Integrate with Twilio for the phone verification
- Integrate with OneSignal to send a notification through the app
