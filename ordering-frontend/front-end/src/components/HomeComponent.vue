<template>
    <v-main>
      <v-container>
        <v-row>
          <v-col cols="12">
            <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              label="상품을 검색해보세요"
              single-line
              hide-details
            ></v-text-field>
          </v-col>
        </v-row>
        
        <v-row>
          <v-col cols="12">
            <v-carousel cycle hide-delimiters height="200">
            <v-carousel-item
              v-for="notice in noticeList"
              :key="notice.id" 
              :src="notice.imagePath" 
              @click="goToNotice(notice)" 
            ></v-carousel-item>
          </v-carousel>
          </v-col>
        </v-row>
  
        <v-row>
          <v-col cols="12">
            <v-subheader>판매중인 상품들</v-subheader>
          </v-col>
          <v-col v-for="item in this.itemList" :key="item.id" cols="12" sm="6" md="4" lg="3">
            <v-card @click="goToDetailPage(item.id)">
              <v-img :src="item.imagePath" height="200px"></v-img>
              <v-card-title>{{ item.name }}</v-card-title>
              <v-card-subtitle>{{ item.price }}원</v-card-subtitle>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </template>
  
<script >
  import axios from 'axios';
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'

  export default {
  created(){
    this.getItems();
    this.getNotice();
  },
  setup() {
    const search = ref('')
    const router = useRouter()

    const goToNotice = () => {
      router.push('/notice')
    }
    const goToDetailPage = (itemId) =>{
      if (itemId !== null) {
        router.push({ name: 'ItemComponent', params: { id: itemId } }) // Assuming the name of your route is 'notice'
      } else {
        alert("해당 아이템이 없습니다.");
      }
    }
    return {
      search,
      goToNotice,
      goToDetailPage,
    }
  },
  data(){
        return{
            itemList : [],
            noticeList: [],
        }
    },
    methods:{
        async getItems(){
            // const token = localStorage.getItem('token');
            // const refreshToken = localStorage.getItem('refreshToken');
            // const headers = token ? { Authorization: `Bearer ${token}`, "X-Refresh-Token": `${refreshToken}` } : {};
            try{
                const data = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/item-service/item/items`);
                this.itemList = data.data.result; 
                console.log(this.itemList);
            }catch(error){
                alert(error.response.data.error_message);
                console.log(error);
            }
        },
        async getNotice(){
          try{
              const data = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/notice-service/notices`);
              this.noticeList = data.data.result; 
              console.log(this.noticeList);
            }catch(error){
              alert(error.response.data.error_message);
              console.log(error);
            }
        }
    }
}
</script>