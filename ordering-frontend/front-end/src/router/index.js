import { createRouter, createWebHistory } from 'vue-router';
import HomeComponent from '@/components/HomeComponent.vue';
import NoticeComponent from '@/components/NoticeComponent.vue';
import ItemComponent from '@/components/ItemComponent.vue';
import MypageComponent from '@/components/MypageComponent.vue';
import LoginComponent from '@/components/LoginComponent.vue'; // 로그인 컴포넌트 import
import SignupComponent from '@/components/SignupComponent.vue'; // 회원가입 컴포넌트 import
import ItemCreateComponent from '@/components/ItemCreateComponent.vue';
import ItemBuyComponent from '@/components/ItemBuyComponent';
import MyCartComponent from '@/components/MyCartComponent.vue';
import OrderByKakaoComponent from '@/components/OrderByKakaoComponent.vue';
import NoticeDetail from '@/components/NoticeDetail.vue'


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
  {
    path: '/buypage', // 아이템 구매 페이지
    name: 'Buypage',
    component: ItemBuyComponent,
  },
  {
    path: '/mycart', // 아이템 구매 페이지
    name: 'MyCart',
    component: MyCartComponent,
  },
  {
    path: '/order/kakao', // 아이템 구매 페이지
    name: 'OrderByKakao',
    component: OrderByKakaoComponent,
  },
  {
    path: '/sell', // 회원가입 페이지
    name: 'ItemCreate',
    component: ItemCreateComponent,
  },
  {
    path: '/notice/:id',
    name: 'NoticeDetail',
    component: NoticeDetail,
    props: true
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;
