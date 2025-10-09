# School Management System - React Frontend

## Cài đặt và chạy

### 1. Cài đặt Node.js
Tải và cài đặt Node.js từ: https://nodejs.org/

### 2. Cài đặt dependencies
```bash
cd frontend
npm install
```

### 3. Chạy development server
```bash
npm run dev
```

Frontend sẽ chạy tại: http://localhost:5173

## Tài khoản đăng nhập

- **Super Admin**: superadmin@example.com / 123456
- **Admin**: admin@example.com / 123456  
- **Teacher**: teacher@example.com / 123456
- **Student**: student@example.com / 123456

## Cấu trúc project

- `src/components/Login.jsx` - Trang đăng nhập
- `src/components/Dashboard.jsx` - Trang dashboard chính
- `src/components/ProtectedRoute.jsx` - Route bảo vệ
- `src/App.jsx` - Component chính với routing

## API Endpoints

- `POST /api/auth/login` - Đăng nhập
- `GET /api/user/profile` - Lấy thông tin profile

## Lưu ý

- Backend Spring Boot phải chạy tại port 8080
- Frontend React chạy tại port 5173
- CORS đã được cấu hình để cho phép giao tiếp giữa frontend và backend






