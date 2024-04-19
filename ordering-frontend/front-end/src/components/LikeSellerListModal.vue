<template>
  <v-dialog :value="dialog" @input="updateDialog" max-width="1200px">
    <v-card>
      <v-card-title class="headline grey lighten-2 text-center" style="font-weight: bold; font-size: 1.2em;">
        즐겨찾기한 판매자 목록
      </v-card-title>
      <v-divider></v-divider> <!-- 머리글 아래 선 추가 -->
      <v-card-text>
        <v-container>
          <v-row class="text-center blue-grey lighten-5">
            <v-col cols="3" class="font-weight-bold">회사명</v-col>
            <v-col cols="3" class="font-weight-bold">사업자등록번호</v-col>
            <v-col cols="3" class="font-weight-bold">카테고리</v-col>
            <v-col cols="3" class="font-weight-bold">점수</v-col>
          </v-row>
          <v-row
            v-for="seller in sellers"
            :key="seller.id"
            class="my-1"
          >
            <v-col cols="3" class="subtitle-1 text-center">{{ seller.companyName }}</v-col>
            <v-col cols="3" class="subtitle-1 text-center">{{ seller.businessNumber }}</v-col>
            <v-col cols="3" class="subtitle-1 text-center">{{ seller.businessType }}</v-col>
            <v-col cols="3" class="subtitle-1 text-center">{{ seller.totalScore }}</v-col>
          </v-row>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="close">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from 'axios';

export default {
  created() {
    this.fetchFavoriteSellers();
  },
  props: {
    dialog: Boolean,
  },
  emits: ["update:dialog"],
  data() {
    return {
      sellers: [],
    };
  },
  methods: {
    async fetchFavoriteSellers() {
      try {
        const token = localStorage.getItem('accessToken');
        const refreshToken = localStorage.getItem('refreshToken');
        const headers = token ? { Authorization: `Bearer ${token}`, 'X-Refresh-Token': refreshToken } : {};
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member-service/member/likeSellers`, { headers });
        this.sellers = response.data.result;
      } catch (error) {
        console.error('Failed to fetch favorite sellers:', error);
      }
    },
    updateDialog(value) {
      this.$emit("update:dialog", value);
    },
    close() {
      this.$emit("update:dialog", false);
    },
  },
};
</script>