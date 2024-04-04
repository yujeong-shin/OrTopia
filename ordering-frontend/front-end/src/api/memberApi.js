import axios from 'axios';

// API 호출을 위한 axios 인스턴스 생성
const memberApi = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
  });
// 요청 인터셉터 추가
memberApi.interceptors.request.use(
  (config) => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (refreshToken) {
      config.headers['X-Refresh-Token'] = refreshToken;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default memberApi;
