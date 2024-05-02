<template>
  <v-main>
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-subheader>
            <span style="color: blue; font-weight: bold;">{{ this.$route.params.value }}</span> 에 대한 검색 결과
          </v-subheader>
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
            <v-card-subtitle>
              <v-rating
                v-model="item.totalScore"
                :half-increments="true"
                :color="'red'"
                :background-color="'grey darken-3'"
                :size="'tiny'"
                readonly
              ></v-rating>
              <span style="vertical-align: top; margin-top: 2px">
                ({{ item.reviewNumber }})
              </span>
            </v-card-subtitle>
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
  },
  setup() {
    const search = ref("");
    const router = useRouter();

    const goToDetailPage = (itemId) => {
      if (itemId !== null) {
        router.push({ name: "ItemComponent", params: { id: itemId } }); // Assuming the name of your route is 'notice'
      } else {
        alert("해당 아이템이 없습니다.");
      }
    };
    return {
      search,
      goToDetailPage,
    };
  },
  data() {
    return {
      userRole: null,
      // selectedCategory: null,
      itemList: [],
      buttonText: "정렬 옵션",
    };
  },
  methods: {
    async getItems() {
      try {
        const data = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-item-service/item/items`
        );
        const key = this.$route.params.value;
        this.itemList = data.data.result.filter(item => item.name.includes(key) || item.category.includes(key));
        this.itemList.forEach((item) => {
          // 각 아이템의 score와 reviewNumber를 이용하여 totalScore 계산
          item.totalScore = item.score / item.reviewNumber;
        });
        console.log(this.itemList);
      } catch (error) {
        alert(error.response.data.error_message);
        console.log(error);
      }
    },
    async getNotice() {
      try {
        const data = await axios.get(
          `${process.env.VUE_APP_API_BASE_URL}/ortopia-notice-service/notices`
        );
        this.noticeList = data.data.result;
        console.log(this.noticeList);
      } catch (error) {
        alert(error.response.data.error_message);
        console.log(error);
      }
    },
    updateButtonText(text) {
      this.buttonText = text;
    },
    sortItems(sortBy) {
      console.log(this.itemList);
      this.sortBy = sortBy;
      switch (sortBy) {
        case "price-asc":
          this.itemList.sort(
            (a, b) => a.price - b.price
          );
          break;
        case "price-desc":
          this.itemList.sort(
            (a, b) => b.price - a.price
          );
          break;
        case "newest":
          this.itemList.sort(
            (a, b) => new Date(b.createdTime) - new Date(a.createdTime)
          );
          break;
        case "review-count":
          this.itemList.sort(
            (a, b) => b.reviewNumber - a.reviewNumber
          );
          break;
        case "rating-desc":
        this.itemList.sort((a, b) => {
            if (a.reviewNumber == 0) {
                return 1;
            } else if (b.reviewNumber == 0) {
                return -1;
            } else {
                return (b.score / b.reviewNumber) - (a.score / a.reviewNumber);
            }
        });
          break;
        default:
          this.sortedItemList = this.itemList;
          break;
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
.header-bottom-line {
  border-bottom: 1px solid #e0e0e0;
}
.my-custom-button {
  background-color: #ffffff !important;
  margin-top: 10px;
}
</style>