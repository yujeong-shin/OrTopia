import { createApp } from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'
import router from './router'
import store from './store/cart.js'
import axios from 'axios';

loadFonts()

axios.interceptors.response.use(response => response,error =>{
  if(error.response && error.response.status === 402){
      alert(error.response.data.message);
      window.location.href ="/";
  }
  // 나머지 에러는 무시하겠다.
  return Promise.reject(error);
})

createApp(App)
  .use(vuetify)
  .use(router)
  .use(store)
  .mount('#app')
  

