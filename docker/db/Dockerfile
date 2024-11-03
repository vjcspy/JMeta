# Sử dụng Node.js image
FROM node:20-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy package.json và package-lock.json vào container
COPY package*.json ./

# Cài đặt dependencies
RUN npm install

# Cài đặt Prisma CLI toàn cục
# RUN npm install -g prisma

# Lệnh chạy ứng dụng
CMD ["npm", "--version"]
