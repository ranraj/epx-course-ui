#Add export environment variables
#export CLIENT_ID=<CONFIGURE_ENV_PROPERTY>
#export CLIENT_SECRET=<CONFIGURE_ENV_PROPERTY>
mvn install
docker build . -t epx-ui
docker run --env-file ./env.list -p 9090:9090 epx-ui:latest
