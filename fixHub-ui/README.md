# fixHub UI (Vue + Vite)

轻量的前端项目，包含登录/注册示例，使用 Vue 3 + Vite + Element Plus。用于与后端 `fixhub-backend` 交互（默认相同 host）。

快速开始：

```powershell
cd fixHub-ui
npm install
npm run dev
```

开发服务器默认端口为 3000。页面路由：
- /login
- /register
- /dashboard

说明：
- 登录/注册会请求后端 `/api/auth/login` 与 `/api/auth/register`，因此运行前端时请确保后端也在运行或调整代理/baseURL。
