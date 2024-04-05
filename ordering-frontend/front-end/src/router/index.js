import { createRouter, createWebHistory } from 'vue-router';
import HomeComponent from '@/components/HomeComponent.vue';
import NoticeComponent from '@/components/NoticeComponent.vue';
import ItemComponent from '@/components/ItemComponent.vue';
import MypageComponent from '@/components/MypageComponent.vue';
import LoginComponent from '@/components/LoginComponent.vue'; // 로그인 컴포넌트 import
import SignupComponent from '@/components/SignupComponent.vue'; // 회원가입 컴포넌트 import

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeComponent
  },
  {
    path: '/notice',
    name: 'Notice',
    component: NoticeComponent,
  },
  {
    path:'/item/:id',
    name: 'ItemComponent',
    component: ItemComponent,
  },
  {
    path: '/mypage', // 마이페이지
    name: 'Mypage',
    component: MypageComponent,
  },
  {
    path: '/login', // 로그인 페이지
    name: 'Login',
    component: LoginComponent,
  },
  {
    path: '/signup', // 회원가입 페이지
    name: 'Signup',
    component: SignupComponent,
  },

  // 기타 경로...
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;
