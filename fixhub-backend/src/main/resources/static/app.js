// 简单的登录 / 注册逻辑，使用 fetch 调用后端接口
document.addEventListener('DOMContentLoaded', () => {
  const tabLogin = document.getElementById('tab-login');
  const tabRegister = document.getElementById('tab-register');
  const loginForm = document.getElementById('login-form');
  const registerForm = document.getElementById('register-form');
  const msgEl = document.getElementById('msg');
  const respEl = document.getElementById('response');

  function showMsg(text, isError) {
    msgEl.textContent = text;
    msgEl.className = isError ? 'msg error' : 'msg success';
    if (!text) msgEl.className = 'msg';
    // hide response area when only showing messages
    if (!text) {
      respEl.classList.add('hidden');
      respEl.textContent = '';
    }
  }

  function showResponse(obj) {
    if (!obj) {
      respEl.classList.add('hidden');
      respEl.textContent = '';
      return;
    }
    respEl.classList.remove('hidden');
    try {
      respEl.textContent = JSON.stringify(obj, null, 2);
    } catch (e) {
      respEl.textContent = String(obj);
    }
  }

  tabLogin.addEventListener('click', () => {
    tabLogin.classList.add('active');
    tabRegister.classList.remove('active');
    loginForm.classList.remove('hidden');
    registerForm.classList.add('hidden');
    showMsg('');
  });
  tabRegister.addEventListener('click', () => {
    tabRegister.classList.add('active');
    tabLogin.classList.remove('active');
    registerForm.classList.remove('hidden');
    loginForm.classList.add('hidden');
    showMsg('');
  });

  loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    showMsg('正在登录...');
    const form = new FormData(loginForm);
    const payload = {
      username: form.get('username'),
      password: form.get('password')
    };
    try {
      const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      // 显示完整返回数据便于调试
      showResponse(data);
      if (!res.ok) throw new Error(data.message || JSON.stringify(data));
      // 登录成功：如果后端返回 token，可存储
      if (data.token) {
        localStorage.setItem('fixhub_token', data.token);
      }
      showMsg(data.message || '登录成功');
    } catch (err) {
      console.error(err);
      showMsg('登录失败：' + err.message, true);
    }
  });

  registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    showMsg('正在注册...');
    const form = new FormData(registerForm);
    // 后端 RegisterUserRequest 支持 username, password, displayName, role
    const payload = {
      username: form.get('username'),
      password: form.get('password'),
      displayName: form.get('displayName') || undefined
      // 如果需要 role，可加上 role: form.get('role')
    };
    try {
      const res = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      showResponse(data);
      if (!res.ok) throw new Error(data.message || JSON.stringify(data));
      showMsg('注册成功，请使用刚才的账号登录');
      // 切回登录标签
      tabLogin.click();
    } catch (err) {
      console.error(err);
      showMsg('注册失败：' + err.message, true);
    }
  });
});
