FROM debian:stable-slim

WORKDIR /app

COPY . . 

RUN apt update

RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    nodejs \
    && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/openjdk-17-jdk

ENV PATH $PATH:$JAVA_HOME/bin
RUN apt-get update 
RUN apt-get install -y npm --fix-missing 

RUN npm install ether && npm install bip39

EXPOSE 3000

