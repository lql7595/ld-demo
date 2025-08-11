# 构建阶段
FROM node:22-alpine as builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
ARG ENV_FILE=.env.production
COPY ${ENV_FILE} .env
RUN npm run build

FROM nginx:1.26.0

COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
