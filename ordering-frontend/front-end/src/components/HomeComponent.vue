<template>
  <v-main>
    <v-container>
      <v-row align="start" class="top-minus-margin">
        <v-col cols="5" style="height: 500px">
          <v-carousel cycle hide-delimiters v-if="noticeList.length > 0">
            <v-carousel-item
              v-for="(notice, index) in noticeList"
              :key="notice.id"
              :v-show="index === 0"
              contain
            >
              <v-img
                :src="notice.imagePath"
                height="100%"
                width="100%"
                @click="goToNotice(notice)"
              ></v-img>
            </v-carousel-item>
          </v-carousel>
        </v-col>
        <v-col cols="5" style="height: 510px">
          <img
            src="/mainCommunityImage.png"
            style="width: 150%; height: 103%"
          />
        </v-col>
      </v-row>
      <br />
      <br />
      <br />
      <div class="header-bottom-line"></div>
      <div class="text-center">
        <!-- <v-select
          v-model="location"
          :items="locations"
          label="Location"
        ></v-select> -->
        <v-menu :location="location">
          <template v-slot:activator="{ props }">
            <v-row justify="end">
              <v-col cols="auto">
                <v-btn
                  color="primary"
                  dark
                  flat
                  v-bind="props"
                  class="my-custom-button"
                >
                  <span style="color: #000000">{{ buttonText }}</span>
                  <v-icon class="rotate-180" style="color: #000000"
                    >mdi-chevron-down</v-icon
                  >
                </v-btn>
              </v-col>
            </v-row>
          </template>
          <v-list>
            <v-list-item
              @click="
                sortItems('price-asc');
                updateButtonText('낮은 가격순');
              "
            >
              <v-list-item-title>낮은 가격순</v-list-item-title>
            </v-list-item>
            <v-list-item
              @click="
                sortItems('price-desc');
                updateButtonText('높은 가격순');
              "
            >
              <v-list-item-title>높은 가격순</v-list-item-title>
            </v-list-item>
            <v-list-item
              @click="
                sortItems('newest');
                updateButtonText('최신순');
              "
            >
              <v-list-item-title>최신순</v-list-item-title>
            </v-list-item>
            <v-list-item
              @click="
                sortItems('review-count');
                updateButtonText('리뷰 많은순');
              "
            >
              <v-list-item-title>리뷰 많은순</v-list-item-title>
            </v-list-item>
            <v-list-item
              @click="
                sortItems('rating-desc');
                updateButtonText('별점 높은순');
              "
            >
              <v-list-item-title>별점 높은순</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
      <div>
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
      </div>
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
    this.sortItems("rating-desc"); // 초기 정렬 기준: 별점 높은순
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
      itemList: [],
      noticeList: [],
      sortedItemList: [],
      buttonText: "정렬 옵션",
    };
  },
  methods: {
    updateButtonText(text) {
      this.buttonText = text;
    },
    sortItems(sortBy) {
      this.sortBy = sortBy;
      switch (sortBy) {
        case "price-asc":
          this.sortedItemList = [...this.itemList].sort(
            (a, b) => a.price - b.price
          );
          break;
        case "price-desc":
          this.sortedItemList = [...this.itemList].sort(
            (a, b) => b.price - a.price
          );
          break;
        case "newest":
          this.sortedItemList = [...this.itemList].sort(
            (a, b) => b.createdAt - a.createdAt
          );
          break;
        case "review-count":
          this.sortedItemList = [...this.itemList].sort(
            (a, b) => b.reviewCount - a.reviewCount
          );
          break;
        case "rating-desc":
          this.sortedItemList = [...this.itemList].sort(
            (a, b) => b.rating - a.rating
          );
          break;
        default:
          this.sortedItemList = this.itemList;
          break;
      }
    },
    async getItems() {
      try {
        const data = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/item-service/item/items`
        );
        this.itemList = data.data.result;
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
  margin-top: -50px;
  width: 500px !important;
  height: 500px !important;
}
.v-carousel__item {
  width: 100% !important;
  height: 300px !important;
}
.v-carousel__item__img {
  width: 100% !important;
  height: 100% !important;
  object-fit: contain !important;
}
.top-minus-margin {
  margin-top: -60px;
}
.top-plus-margin {
  margin-top: 30px;
}
.header-bottom-line {
  border-bottom: 1px solid #e0e0e0;
}
.my-custom-button {
  background-color: #ffffff !important;
  margin-top: 10px;
}
</style>
