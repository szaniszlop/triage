FROM node:latest
WORKDIR /usr/app
COPY package.json .
COPY package-lock.json .
RUN npm install --quiet
RUN npm install nodemon -g --save-dev
ENV PATH /usr/app/node_modules/.bin:$PATH
COPY . .