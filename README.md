# EPX UI [![Build Status](https://travis-ci.com/ranraj/epx-course-ui.svg?branch=main)](https://travis-ci.com/ranraj/epx-course-ui)
epx ui

Docker build and run
```
docker build . -t epx-ui
docker run --env-file ./env.list -p 9090:9090 epx-ui:latest 
```

Prerequistes 
- Dev
 - Create Google OAuth2 client_id and secret 
 - epx-api should be up and running
- DevOps
 - Docker Hub
 - Travis CI
Note :  Keep seperate OAuth2 key details for Production and Development 
