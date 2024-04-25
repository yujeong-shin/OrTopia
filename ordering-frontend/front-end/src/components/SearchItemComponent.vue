<template>
  <v-main>
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-subheader>
            <span style="color: blue; font-weight: bold;">{{ this.$route.params.value }}</span> 에 대한 검색 결과
          </v-subheader>
        </v-col>
        <v-col
          v-for="item in this.itemList"
          :key="item.id"
          cols="12"
          sm="6"
          md="4"
          lg="3"
        >
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

<script>
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";

export default {
  created() {
    this.getItems();
    this.getNotice();
  },
  setup() {
    const search = ref("");
    const router = useRouter();

    const goToNotice = () => {
      router.push("/notice");
    };
    const goToDetailPage = (itemId) => {
      if (itemId !== null) {
        router.push({ name: "ItemComponent", params: { id: itemId } }); // Assuming the name of your route is 'notice'
      } else {
        alert("해당 아이템이 없습니다.");
      }
    };
    return {
      search,
      goToNotice,
      goToDetailPage,
    };
  },
  data() {
    return {
      userRole: null,
      // selectedCategory: null,
      itemList: [],
      noticeList: [],
    };
  },
  methods: {
    async getItems() {
      try {
        const data = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/item-service/item/items`
        );
        const key = this.$route.params.value;
        this.itemList = data.data.result.filter(item => item.name.includes(key) || item.category.includes(key));
        console.log(this.itemList);
      } catch (error) {
        alert(error.response.data.error_message);
        console.log(error);
      }
    },
    async getNotice() {
      try {
        const data = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/notice-service/notices`
        );
        this.noticeList = data.data.result;
        console.log(this.noticeList);
      } catch (error) {
        alert(error.response.data.error_message);
        console.log(error);
      }
    },
  },
};
</script>
<style scoped>
.v-carousel__controls__item {
  width: 900px !important;
  height: 300px !important;
}
.v-carousel__item {
  width: 100% !important;
  height: 300px !important;
}
.v-carousel__item img {
  width: 100% !important;
  height: 100% !important;
  object-fit: contain !important;
}
</style>