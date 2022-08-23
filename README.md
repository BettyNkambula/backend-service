# backend-service
================

Handles payments
Trigger build & deploy


# Database

TLDR
```shell script
docker-compose up db
```

The project uses Oracle DB. 

For development purposes a docker compose file is provided. The docker compose 
file makes use of oracle/database:18.4.0-xe.

Oracle does not publicly release docker images. However, Oracle does publish
scripts to build the images.

## Oracle DB Set Up

In order to build the docker image locally you need to clone the GitHub project
and run a script. You will need a shell compatible terminal.

NOTE: The image build process takes a very long time, please be patient!

 1. git clone https://github.com/oracle/docker-images.git
 2. cd docker-images/OracleDatabase/SingleInstance/dockerfiles
 3. ./buildDockerImage.sh -v 18.4.0 -x

## Running Oracle DB

The docker compose file requires two environment variables to be set. 
 
 1. ORA_DATA
 
ORA_DATA should be the path to the location on the host where you want the db
data to be stored. 

NOTE: User id `54321` needs to be able to write to this directory.

e.g.: `/opt/oracle/oradata`
 
 2. SCRIPTS_STARTUP
 
SCRIPTS_STARTUP should be the path to any folder containing scripts which should
be run on start up. The first time you run the application you need to set up 
the system user which this project will use to connect to the db. This script 
can be found in `src/sql`.

e.g.: `{path_to_repository}/src/sql`
