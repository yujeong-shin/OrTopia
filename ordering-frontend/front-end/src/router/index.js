import { createRouter, createWebHistory } from 'vue-router'
import HomeComponent from '@/components/HomeComponent.vue'
import NoticeComponent from '@/components/NoticeComponent.vue'
import ItemComponent from '@/components/ItemComponent.vue'

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
    path:'/item',
    name: 'ItemComponent',
    component: ItemComponent,
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router