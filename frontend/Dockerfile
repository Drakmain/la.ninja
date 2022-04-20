#node
FROM node:latest as node
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

#nginx
FROM nginx:alpine
COPY --from=node /app/dist/la.ninja /usr/share/nginx/html